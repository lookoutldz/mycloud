package org.looko.mycloud.commonstarter;

import org.junit.jupiter.api.Test;
import org.looko.mycloud.commonstarter.component.CommonDemo;
import org.looko.mycloud.commonstarter.config.CommonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {CommonAutoConfiguration.class})
@TestPropertySource("classpath:test.properties")
class CommonStarterApplicationTests {

    @Autowired
    private CommonDemo commonDemo;

    @Test
    void contextLoads() {
        commonDemo.showMessage();
    }

}
