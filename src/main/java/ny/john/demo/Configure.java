package ny.john.demo;

import ny.john.demo.Interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 */
@Configuration
public class Configure extends WebMvcConfigurerAdapter{
    @Resource
    private AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }
}
