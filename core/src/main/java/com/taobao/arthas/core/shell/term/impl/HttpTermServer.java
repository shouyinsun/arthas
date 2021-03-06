package com.taobao.arthas.core.shell.term.impl;

import com.alibaba.arthas.deps.org.slf4j.Logger;
import com.alibaba.arthas.deps.org.slf4j.LoggerFactory;
import com.taobao.arthas.core.shell.future.Future;
import com.taobao.arthas.core.shell.handlers.Handler;
import com.taobao.arthas.core.shell.term.Term;
import com.taobao.arthas.core.shell.term.TermServer;
import com.taobao.arthas.core.shell.term.impl.http.NettyWebsocketTtyBootstrap;
import io.netty.util.concurrent.EventExecutorGroup;
import io.termd.core.function.Consumer;
import io.termd.core.tty.TtyConnection;

import java.util.concurrent.TimeUnit;

/**
 * @author beiwei30 on 18/11/2016.
 */
//http终端server
public class HttpTermServer extends TermServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpTermServer.class);

    private Handler<Term> termHandler;
    private NettyWebsocketTtyBootstrap bootstrap;
    private String hostIp;
    private int port;
    private long connectionTimeout;
    private EventExecutorGroup workerGroup;

    public HttpTermServer(String hostIp, int port, long connectionTimeout, EventExecutorGroup workerGroup) {
        this.hostIp = hostIp;
        this.port = port;
        this.connectionTimeout = connectionTimeout;
        this.workerGroup = workerGroup;
    }

    @Override
    public TermServer termHandler(Handler<Term> handler) {
        this.termHandler = handler;
        return this;
    }

    @Override
    public TermServer listen(Handler<Future<TermServer>> listenHandler) {
        // TODO: charset and inputrc from options
        bootstrap = new NettyWebsocketTtyBootstrap(workerGroup).setHost(hostIp).setPort(port);
        try {
            bootstrap.start(new Consumer<TtyConnection>() {
                @Override
                public void accept(final TtyConnection conn) {
                    //handle
                    termHandler.handle(new TermImpl(Helper.loadKeymap(), conn));
                }
            }).get(connectionTimeout, TimeUnit.MILLISECONDS);
            listenHandler.handle(Future.<TermServer>succeededFuture());
        } catch (Throwable t) {
            logger.error("Error listening to port " + port, t);
            listenHandler.handle(Future.<TermServer>failedFuture(t));
        }
        return this;
    }

    @Override
    public int actualPort() {
        return bootstrap.getPort();
    }

    @Override
    public void close() {
        close(null);
    }

    @Override
    public void close(Handler<Future<Void>> completionHandler) {
        if (bootstrap != null) {
            bootstrap.stop();
            if (completionHandler != null) {
                completionHandler.handle(Future.<Void>succeededFuture());
            }
        } else {
            if (completionHandler != null) {
                completionHandler.handle(Future.<Void>failedFuture("telnet term server not started"));
            }
        }
    }
}
