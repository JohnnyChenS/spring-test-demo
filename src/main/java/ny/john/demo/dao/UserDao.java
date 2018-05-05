package ny.john.demo.dao;

import ny.john.demo.entity.UserEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 */
@Repository
public class UserDao {
    @Resource
    private JdbcTemplate jdbcTemplate;

    public UserEntity get(int userId){
        List<UserEntity> userEntities =  jdbcTemplate.query("select * from user where id = ?", new Object[]{userId}, new BeanPropertyRowMapper<>(UserEntity.class));

        return userEntities.size() == 0 ? null : userEntities.get(0);
    }


    public int insert(UserEntity user){
        String sql = "insert into user (`username`, `age`, `gender`) values (?, ?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getAge(), user.getGender());
    }
}
