package ny.john.demo.controller;

import com.alibaba.fastjson.JSONObject;
import ny.john.demo.dao.UserDao;
import ny.john.demo.entity.UserEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="chz0321@gmail.com">johnny</a>
 * @created on 2018/5/1.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Resource
    private UserDao userDao;

    @RequestMapping(value = "/get/{userId}")
    public String getUser(@PathVariable("userId") int userId) {
        UserEntity user = userDao.get(userId);

        if (user == null)
            return "{}";

        JSONObject userJson = new JSONObject();
        userJson.put("id", user.getId());
        userJson.put("username", user.getUsername());
        userJson.put("gender", getGenderText(user.getGender()));
        userJson.put("age", user.getAge());

        return userJson.toJSONString();
    }

    private String getGenderText(int genderValue) {
        for (UserEntity.GENDER gender : UserEntity.GENDER.values())
            if (gender.ordinal() == genderValue)
                return gender.name();

        return UserEntity.GENDER.Male.name();
    }
}
