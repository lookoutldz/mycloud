package org.looko.mycloud.user.serialization;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.mail.SimpleMailMessage;

import java.nio.charset.StandardCharsets;

public class KafkaSimpleMailMessageSerializer implements Serializer<SimpleMailMessage> {

    @Override
    public byte[] serialize(String topic, SimpleMailMessage data) {
        if (data == null) {
            return null;
        }
        String json = new Gson().toJson(data);
        return json.getBytes(StandardCharsets.UTF_8);
    }
}
