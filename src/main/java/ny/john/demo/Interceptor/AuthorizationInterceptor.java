package ny.john.demo.Interceptor;

import ny.john.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private String[] needLoginUriArr= new String[]{"/weather/today"};

    @Resource
    private UserDao userDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String   uidAttr         = request.getHeader("uid");

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        for (String needLoginUri : needLoginUriArr) {
            if (request.getRequestURI().startsWith(needLoginUri) &&
                    (uidAttr == null || userDao.get(Integer.parseInt(uidAttr)) == null)) {
                response.getWriter().write("{\"error\":\"you are not authorized to visit this page.\"}");//直接将完整的表单html输出到页面
                response.getWriter().flush();
                response.getWriter().close();
                return false;
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
