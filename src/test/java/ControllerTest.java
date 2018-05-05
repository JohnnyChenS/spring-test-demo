import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ny.john.demo.dao.UserDao;
import ny.john.demo.entity.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
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
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(locations = {"classpath:spring-test.xml"})
public class ControllerTest {
    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext context;

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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
