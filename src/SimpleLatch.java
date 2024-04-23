import java.util.concurrent.TimeUnit;

/**
 * 简单Latch
 */
public class SimpleLatch {
    /**
     * 计数
     */
    private Integer count;

    /**
     * 构造函数
     * @param count 计数
     */
    public SimpleLatch(int count) {
        this.count = count;
    }

    /**
     * 构造函数
     */
    public SimpleLatch() {
        this(1);
    }

    /**
     * 获取当前计数
     * @return 当前计数
     */
    public synchronized Integer getCount() {
        return count;
    }

    /**
     * 等待
     * @throws InterruptedException 线程中断异常
     */
    public synchronized void await() throws InterruptedException {
        while (count > 0) wait();
    }

    /**
     * 等待指定时间
     * @param time 时间
     * @param timeUnit 时间单位
     * @return 是否等待成功
     * @throws InterruptedException 线程中断异常
     */
    public synchronized boolean await(Long time, TimeUnit timeUnit) throws InterruptedException {
        long timeoutMillis = timeUnit.toMillis(time);
        long startTime = System.currentTimeMillis();
        while (count > 0) {
            long remainingTime = timeoutMillis - (System.currentTimeMillis() - startTime);
            if (remainingTime <= 0) return false;
            wait(timeoutMillis);
        }
        return true;
    }

    /**
     * 计数减一
     */
    public synchronized void countDown() {
        count--;
        awaken();
    }

    /**
     * 唤醒
     */
    private  void awaken() {
        if (count == 0) notify();
    }

    /**
     * 立即唤醒
     */
    public synchronized void awakenNow() {
        count = 0;
        notify();
    }

    /**
     * 计数加一
     */
    public synchronized void countUp() {
        count++;
        awaken();
    }
}
