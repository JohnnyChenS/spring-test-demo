package ny.john.demo.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2019-01-04.
 *
 */
@Component
public class MultiThreadScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MultiThreadScheduler.class);

    /**
     * fixedDelay 上次执行结束和下次执行开始之间的时间间隔
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = 2 * 1000, initialDelay = 1000)
//    @Scheduled(cron = "* * * * * *")
    public void task1() throws InterruptedException {
        logger.info("task1 start : thread ---> " + Thread.currentThread().getName());
        Thread.sleep(8000);
        logger.info("task1 end : thread ---> " + Thread.currentThread().getName());
    }

    /**
     * fixedRate 每次以指定的时间间隔执行
     * @throws InterruptedException
     */
    @Scheduled(fixedRate = 2 * 1000, initialDelay = 1000)
    public void task2() throws InterruptedException {
        logger.info("task2 start : thread ---> " + Thread.currentThread().getName());
        Thread.sleep(3000);
        logger.info("task2 end : thread ---> " + Thread.currentThread().getName());
    }

    /**
     * fixedRate 每次以指定的时间间隔执行
     * Async 该注解使方法可以被异步调用，也就是主线程无需等待该方法执行完再调度其他方法
     *       为了让Async可以生效，还必须加上@EnableAsync;
     *       该注释可以用在任何方法上
     * @throws InterruptedException
     */
    @Async
    @Scheduled(fixedDelay = 2 * 1000, initialDelay = 1000)
    public void task3() throws InterruptedException {
        logger.info("task3 start : thread ---> " + Thread.currentThread().getName());
        Thread.sleep(3000);
        logger.info("task3 end : thread ---> " + Thread.currentThread().getName());
    }
}
