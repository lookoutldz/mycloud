package org.looko.mycloud.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.looko.mycloud.user.controller.UserController;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserApplicationTests {

    @Autowired
    EmailUtils emailUtils;

    @Disabled
    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }

    @Disabled
    @Test
    void testEmail() {
        emailUtils.postValidcodeEmail(BusinessTypeEnum.VALIDCODE_LOGIN, "your-addr@outlook.com", "123456");
    }

    @Autowired
    UserController userController;
    @Test
    void testCommon() {
        Object obj = userController.getById("1");
        System.out.println(obj);
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Test
    void redisConnectionTest() {
        String k = "theKey";
        String v = "theValue";
        ValueOperations<String, String> vOps = stringRedisTemplate.opsForValue();
        // set value
        vOps.set(k, v);
        // get value
        String value = vOps.get(k);
        System.out.println(value);
        assert v.equals(value);
    }
}
