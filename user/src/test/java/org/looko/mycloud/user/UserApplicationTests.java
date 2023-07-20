package org.looko.mycloud.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.looko.mycloud.user.controller.UserController;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.util.EmailUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class UserApplicationTests {

    @Autowired
    EmailUtils emailUtils;

    @Disabled
    @Test
    void contextLoads() {
    }

    @Disabled
    @Test
    void testEncrypt() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String p1 = encoder.encode("123456");
        String p2 = encoder.encode("123456");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(encoder.matches("123456", p1));
        System.out.println(new BCryptPasswordEncoder().matches("123456", p2));
    }

    @Disabled
    @Test
    void testEmail() throws Exception {
        emailUtils.publishValidcodeEmail(BusinessTypeEnum.VALIDCODE_LOGIN, "your-addr@outlook.com", "123456");
    }

    @Autowired
    UserController userController;
    @Disabled
    @Test
    void testCommon() {
        Object obj = userController.getById("1");
        System.out.println(obj);
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Disabled
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

    @Autowired
    RedissonClient redissonClient;
    @Disabled
    @Test
    void redissonTest() throws InterruptedException {
        RLock lock = redissonClient.getLock("myLock");
        if (lock.tryLock(1, TimeUnit.MINUTES)) {
            try {
                redissonClient.getBucket("myLockBucket").set("world", 2, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
