package org.looko.mycloud.user.util;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.mail.SimpleMailMessage;

import java.nio.charset.StandardCharsets;

public class SimpleMailMessageDeserializer implements Deserializer<SimpleMailMessage> {
    @Override
    public SimpleMailMessage deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        String json = new String(data, StandardCharsets.UTF_8);
        return new Gson().fromJson(json, SimpleMailMessage.class);
    }
}
