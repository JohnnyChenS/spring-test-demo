package ny.john.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 */
@RestController
@RequestMapping(value = "/index")
public class IndexController {
    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello world!";
    }
}
