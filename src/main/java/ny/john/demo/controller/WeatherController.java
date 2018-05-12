package ny.john.demo.controller;

import com.alibaba.fastjson.JSONObject;
import ny.john.demo.dao.UserDao;
import ny.john.demo.entity.UserEntity;
import ny.john.demo.service.WeatherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 */
@RestController
@RequestMapping(value = "/weather")
public class WeatherController {

    @Resource
    private WeatherService weatherService;

    @Resource
    private UserDao userDao;

    @RequestMapping(value = "/today")
    public String hello(HttpServletRequest request) throws IOException {
        int        userId = Integer.parseInt(request.getHeader("uid"));
        UserEntity user   = userDao.get(userId);

        String ret = "hello " + user.getUsername() + ": \n ";

        JSONObject weatherToday = weatherService.getToday("北京");
        if (!weatherToday.getString("status").equals("success"))
            return ret + "sorry, error occur: " + weatherToday.getString("message");

        JSONObject today = weatherToday.getJSONArray("results").getJSONObject(0).getJSONArray("weather_data").getJSONObject(0);

        return ret + "Here is today's weather: " + today.getString("weather") + " " + today.getString("temperature");
    }
}
