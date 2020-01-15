package ny.john.demo;

import ny.john.demo.Interceptor.AuthorizationInterceptor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 * <p>
 *
 * ThreadPoolTaskExecutor 和 ThreadPoolTaskScheduler 的区别：
 * 他们都implements TaskExecutor,
 * ThreadPoolTaskScheduler 用于创建一个多线程的定时任务调度器，执行定时任务时可以并行执行同一时刻需要触发的任务
 * ThreadPoolTaskExecutor 用于创建一个多线程任务执行器，可以处理需要异步执行的任务
 *
 * 使用@EnableAsync注解，不implements AsyncConfigurer的话, Spring会指定一个默认的ThreadPoolTaskExecutor实现 -- SimpleAsyncTaskExecutor
 * 你也可以implements AsyncConfigurer来自己指定一个ThreadPoolTaskExecutor
 */
@Configuration
public class Configure extends WebMvcConfigurerAdapter implements /*AsyncConfigurer,*/ SchedulingConfigurer {
    @Resource
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }

//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(3);
//        executor.setMaxPoolSize(10);
//        executor.initialize();
//
//        return executor;
//    }

//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return null;
//    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setBeanName("taskScheduler");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setAwaitTerminationSeconds(10);
        scheduler.initialize();
        taskRegistrar.setScheduler(scheduler);
    }
}
