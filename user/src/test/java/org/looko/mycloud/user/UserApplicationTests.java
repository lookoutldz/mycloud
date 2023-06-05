package org.looko.mycloud.user;

import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.looko.mycloud.emailstarter.component.EmailManager;
import org.looko.mycloud.user.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@SpringBootTest
class UserApplicationTests {

    @Autowired
    EmailManager emailManager;

    @Disabled
    @Test
    void contextLoads() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));
    }

    @Disabled
    @Test
    void testEmail() {
        emailManager.sendEmail("test", "hello", Collections.singleton("lookoutldz@outlook.com"));
    }

    @Autowired
    UserController userController;
    @Test
    void testCommon() {
        Object obj = userController.getById("1");
        System.out.println(obj);
    }

}
