import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * <p>
 * CyclicBarrierMethod 方式
 */
public class CyclicBarrierMethod {

    private volatile Integer value = null;
    CyclicBarrier barrier;

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public void sum(int num) throws BrokenBarrierException, InterruptedException {
        value = fibo(num);
        barrier.await();
    }

    private int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    public int getValue() throws InterruptedException {
        return value;
    }

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        final CyclicBarrierMethod method = new CyclicBarrierMethod();
        CyclicBarrier barrier = new CyclicBarrier(1, () -> {
            int result = 0; //这是得到的返回值
            try {
                result = method.getValue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("异步计算结果为：" + result);

            System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        });
        method.setBarrier(barrier);

        Thread thread = new Thread(() -> {
            try {
                method.sum(45);
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
