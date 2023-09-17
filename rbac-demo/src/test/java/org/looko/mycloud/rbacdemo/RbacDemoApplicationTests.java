package org.looko.mycloud.rbacdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.looko.mycloud.rbacdemo.job.GenData;
import org.looko.mycloud.rbacdemo.util.SnowflakeIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@Slf4j
@SpringBootTest
class RbacDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private SnowflakeIdWorker idWorker;
    @Test
    void snowflake() {
        int num = 100000;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            long id = idWorker.nextId();
//            System.out.println(id);
        }
        long end = System.currentTimeMillis();
        log.info("数据量：{}, 消耗时间：{}ms",num, (end - start));
    }


    @Autowired
    private GenData genData;
    @Test
    void testGetUsername() throws IOException {
        List<String> username = genData.getUsername();
        System.out.println(String.join(",", username));
    }

}
