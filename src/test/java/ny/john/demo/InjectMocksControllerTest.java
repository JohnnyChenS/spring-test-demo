package ny.john.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ny.john.demo.Interceptor.AuthorizationInterceptor;
import ny.john.demo.controller.WeatherController;
import ny.john.demo.dao.UserDao;
import ny.john.demo.entity.UserEntity;
import ny.john.demo.service.WeatherService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 */
@RunWith(SpringRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-test.xml"})
@SpringBootTest
public class InjectMocksControllerTest {
    private MockMvc mockMvc;

    @Resource
    private DataSource dataSource;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserDao userDao;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    @Resource
    private WeatherController weatherController;

    @Resource
    private AuthorizationInterceptor authorizationInterceptor;

    /**
     * truncat the mysql and redis test db
     * between every test method
     */
    private void truncateData() {
        try {
            Connection       conn = dataSource.getConnection();
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet        rs   = meta.getTables(null, null, "%", null);

            while (rs.next())
                if (rs.getString(4).toLowerCase().equals("table"))
                    jdbcTemplate.update("TRUNCATE TABLE " + rs.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(weatherController).addInterceptors(authorizationInterceptor).build();
        truncateData();
    }

    @Test
    public void getTodayNoAuthorization() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/weather/today")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String response = result.getResponse().getContentAsString();

        JSONObject responseJson = JSON.parseObject(response);
        Assert.assertEquals("you are not authorized to visit this page.", responseJson.getString("error"));
    }

    @Test
    public void getToday() throws Exception {
        mockUser();
        JSONObject weatherJson = JSONObject.parseObject(mockWeatherResponse());
        Mockito.when(weatherService.getToday(Mockito.anyString())).thenReturn(weatherJson);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/weather/today").header("uid",1)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String response = result.getResponse().getContentAsString();

        Assert.assertEquals("hello Johnny: \n Here is today's weather: 雷阵雨 28 ~ 16℃", response);
    }

    private String mockWeatherResponse(){
        return "{\"error\":0,\"status\":\"success\",\"date\":\"2018-05-12\",\"results\":[{\"currentCity\":\"北京\",\"pm25\":\"252\",\"index\":[{\"des\":\"建议着长袖T恤、衬衫加单裤等服装。年老体弱者宜着针织长袖衬衫、马甲和长裤。\",\"tipt\":\"穿衣指数\",\"title\":\"穿衣\",\"zs\":\"舒适\"},{\"des\":\"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。\",\"tipt\":\"洗车指数\",\"title\":\"洗车\",\"zs\":\"不宜\"},{\"des\":\"各项气象条件适宜，无明显降温过程，发生感冒机率较低。\",\"tipt\":\"感冒指数\",\"title\":\"感冒\",\"zs\":\"少发\"},{\"des\":\"有降水，推荐您在室内进行低强度运动；若坚持户外运动，须注意选择避雨防滑并携带雨具。\",\"tipt\":\"运动指数\",\"title\":\"运动\",\"zs\":\"较不宜\"},{\"des\":\"紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。\",\"tipt\":\"紫外线强度指数\",\"title\":\"紫外线强度\",\"zs\":\"弱\"}],\"weather_data\":[{\"date\":\"周六 05月12日 (实时：22℃)\",\"dayPictureUrl\":\"http://api.map.baidu.com/images/weather/day/leizhenyu.png\",\"nightPictureUrl\":\"http://api.map.baidu.com/images/weather/night/leizhenyu.png\",\"weather\":\"雷阵雨\",\"wind\":\"南风微风\",\"temperature\":\"28 ~ 16℃\"},{\"date\":\"周日\",\"dayPictureUrl\":\"http://api.map.baidu.com/images/weather/day/qing.png\",\"nightPictureUrl\":\"http://api.map.baidu.com/images/weather/night/qing.png\",\"weather\":\"晴\",\"wind\":\"南风微风\",\"temperature\":\"30 ~ 17℃\"},{\"date\":\"周一\",\"dayPictureUrl\":\"http://api.map.baidu.com/images/weather/day/qing.png\",\"nightPictureUrl\":\"http://api.map.baidu.com/images/weather/night/duoyun.png\",\"weather\":\"晴转多云\",\"wind\":\"南风微风\",\"temperature\":\"33 ~ 22℃\"},{\"date\":\"周二\",\"dayPictureUrl\":\"http://api.map.baidu.com/images/weather/day/duoyun.png\",\"nightPictureUrl\":\"http://api.map.baidu.com/images/weather/night/xiaoyu.png\",\"weather\":\"多云转小雨\",\"wind\":\"东南风微风\",\"temperature\":\"33 ~ 22℃\"}]}]}";
    }

    private void mockUser(){
        UserEntity user = new UserEntity();
        user.setUsername("Johnny");
        user.setAge(32);
        user.setGender(1);

        userDao.insert(user);
    }
}
