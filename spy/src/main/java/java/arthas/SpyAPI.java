package java.arthas;

/**
 * <pre>
 * 一个adviceId 是什么呢？ 就是一个trace/monitor/watch命令能对应上的一个id，比如一个类某个函数，它的 enter/end/exception 统一是一个id，分配完了就不会再分配。
 * 
 * 同样一个method，如果它trace之后，也会有一个 adviceId， 这个method里的所有invoke都是统一处理，认为是一个 adviceId 。 但如果有匹配到不同的 invoke的怎么分配？？
 * 好像有点难了。。
 * 
 * 其实就是把所有可以插入的地方都分类好，那么怎么分类呢？？ 或者是叫同一种匹配，就是同一种的 adviceId? 
 * 
 * 比如入参是有  class , method ,是固定的  ,  某个行号，或者 某个
 * 
 * aop插入的叫 adviceId ， command插入的叫 ListenerId？
 * 
 * 
 * 
 * </pre>
 * 
 * @author hengyunabc
 *
 */
//SpyApi里是静态方法
public class SpyAPI {
    private static final AbstractSpy NOPSPY = new NopSpy();
    private static volatile AbstractSpy spyInstance = new NopSpy();

    public static AbstractSpy getSpy() {
        return spyInstance;
    }

    public static void setSpy(AbstractSpy spy) {
        spyInstance = spy;
    }
    
    public static void setNopSpy() {
        setSpy(NOPSPY);
    }

    public static void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {
        spyInstance.atEnter(clazz, methodInfo, target, args);
    }

    public static void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args,
            Object returnObject) {
        spyInstance.atExit(clazz, methodInfo, target, args, returnObject);
    }

    public static void atExceptionExit(Class<?> clazz, String methodInfo, Object target,
            Object[] args, Throwable throwable) {
        spyInstance.atExceptionExit(clazz, methodInfo, target, args, throwable);
    }

    public static void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {
        spyInstance.atBeforeInvoke(clazz, invokeInfo, target);
    }

    public static void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target) {
        spyInstance.atAfterInvoke(clazz, invokeInfo, target);
    }

    public static void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable) {
        spyInstance.atInvokeException(clazz, invokeInfo, target, throwable);
    }

    public static abstract class AbstractSpy {//抽象spy
        //进入
        public abstract void atEnter(Class<?> clazz, String methodInfo, Object target,
                Object[] args);

        //退出
        public abstract void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args,
                Object returnObject);

        //异常退出
        public abstract void atExceptionExit(Class<?> clazz, String methodInfo, Object target,
                Object[] args, Throwable throwable);

        //调用前
        public abstract void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target);

        //调用后
        public abstract void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target);

        //调用异常
        public abstract void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable);
    }

    static class NopSpy extends AbstractSpy {

        @Override
        public void atEnter(Class<?> clazz, String methodInfo, Object target, Object[] args) {
        }

        @Override
        public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args,
                Object returnObject) {
        }

        @Override
        public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args,
                Throwable throwable) {
        }

        @Override
        public void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {

        }

        @Override
        public void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target) {

        }

        @Override
        public void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable) {

        }

    }
}
