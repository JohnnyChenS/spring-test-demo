package ny.john.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 * <p>
 * <p>
 * 默认的定时器只有单线程来调度所有的计划任务，
 * 即使两个定时器的initialDelay在同一时刻，
 * 但谁先抢到谁就先执行，而其他则需要等待该任务执行完成后被调度；
 * 而无论fixedDelay或fixedRate也是一样，执行时间到了依然需要等待主线程调度
 * <p>
 * @EnableAsync 告诉框架启用异步调用的方式，对代码中的@Async方法开启一个新的线程，进行异步调用；
 * 框架将寻找是否指定了TaskExecutor，如果没有则默认使用SimpleAsyncTaskExecutor
 * （框架自带）来进行异步调用;
 */
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
