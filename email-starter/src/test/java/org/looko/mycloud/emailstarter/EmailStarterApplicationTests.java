package org.looko.mycloud.emailstarter;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.looko.mycloud.emailstarter.autoconfigure.EmailAutoConfiguration;
import org.looko.mycloud.emailstarter.component.EmailManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest(classes = EmailAutoConfiguration.class)
@TestPropertySource("classpath:test.properties")
class EmailStarterApplicationTests {

	@Autowired
	EmailManager emailManager;
	@Disabled
	@Test
	void contextLoads() {
		emailManager.sendEmail("Subject", "ContentText", List.of("xxx@qq.com"));
	}

}
