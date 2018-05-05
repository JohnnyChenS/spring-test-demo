package ny.john.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"ny.john.demo"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
