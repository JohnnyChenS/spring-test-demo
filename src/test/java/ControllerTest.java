import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ny.john.demo.dao.UserDao;
import ny.john.demo.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import org.springframework.web.context.WebApplicationContext;

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
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Resource
    private MockMvc mockMvc;

//    @Resource
//    private WebApplicationContext context;

    @Resource
    private DataSource dataSource;

    @Resource
    protected JdbcTemplate jdbcTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserDao userDao;

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
        //使用@AutoConfigureMockMvc注解可以自动生成上下文，省去显式赋值的方式
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        truncateData();
    }

    @Test
    public void getUser() throws Exception {
        mockUser();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user/get/1")).andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String response = result.getResponse().getContentAsString();

        JSONObject responseJson = JSON.parseObject(response);
        Assert.assertEquals(1, responseJson.getIntValue("id"));
        Assert.assertEquals(32, responseJson.getIntValue("age"));
    }

    private void mockUser(){
        UserEntity user = new UserEntity();
        user.setUsername("Johnny");
        user.setAge(32);
        user.setGender(1);

        userDao.insert(user);
    }
}
