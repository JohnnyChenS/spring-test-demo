package ny.john.demo.service;

import com.alibaba.fastjson.JSONObject;
import ny.john.demo.utils.HttpUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/12.
 */
@Service
public class WeatherService {

    public String getFromBaiduApi(String city) throws IOException {
        String urlStr = "http://api.map.baidu.com/telematics/v3/weather?";

        Map<String, String> params = new HashMap<>();
        params.put("location", city);
        params.put("output", "json");
        params.put("ak", "E4805d16520de693a3fe707cdc962045");

        return HttpUtil.get(urlStr, params);
    }

    public JSONObject getToday(String city) throws IOException {
        String weatherData = getFromBaiduApi(city);

        return JSONObject.parseObject(weatherData);
    }
}
