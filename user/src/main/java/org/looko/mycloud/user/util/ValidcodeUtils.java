package org.looko.mycloud.user.util;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.exception.RedisException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.looko.mycloud.user.util.Constants.validcodeExpireMinutes;

@Slf4j
@Component
public class ValidcodeUtils {

    private final StringRedisTemplate stringRedisTemplate;
    private final ValueOperations<String, String> vOps;

    public ValidcodeUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        vOps = stringRedisTemplate.opsForValue();
    }

    /**
     * 生成 6 位数的随机数做为验证码
     * @return String
     */
    public String genValidcode() {
        int validcode = new Random().nextInt(999999 - 100000 + 1) + 100000;
        return String.valueOf(validcode);
    }

    /**
     * TODO 加分布式锁
     * 验证码存储到 Redis
     * @param businessTypeEnum 业务类型
     * @param email 收件人邮箱
     * @param validcode 验证码
     * @throws RedisException redis 依赖异常
     */
    public void saveValidcode(BusinessTypeEnum businessTypeEnum, String email, String validcode) throws RedisException {
        try {
            vOps.set(genStoreKey(businessTypeEnum, email), validcode, validcodeExpireMinutes, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new RedisException("Redis服务暂不可用", e);
        }
    }

    /**
     * TODO 加分布式锁
     * 从 Redis 获取验证码
     * @param businessTypeEnum 业务类型
     * @param email 收件人邮箱
     * @return 验证码
     * @throws RedisException redis 依赖异常
     */
    public String getValidcode(BusinessTypeEnum businessTypeEnum, String email) throws RedisException {
        try {
            return vOps.get(genStoreKey(businessTypeEnum, email));
        } catch (Exception e) {
            throw new RedisException("Redis服务暂不可用", e);
        }
    }

    public boolean checkValidcodeFailed(BusinessTypeEnum businessTypeEnum, String email, String validcode) {
        return !checkValidcodeSuccess(businessTypeEnum, email, validcode);
    }

    public boolean checkValidcodeSuccess(BusinessTypeEnum businessTypeEnum, String email, String validcode) {
        return validcode.equals(getValidcode(businessTypeEnum, email));
    }

    /**
     * TODO 加分布式锁
     * 删除特定业务类型和发件人邮箱的验证码
     * @param businessTypeEnum 业务类型
     * @param email 发件人邮箱
     */
    public void deleteValidcode(BusinessTypeEnum businessTypeEnum, String email) {
        String storeKey = genStoreKey(businessTypeEnum, email);
        try {
            stringRedisTemplate.delete(storeKey);
        } catch (Exception e) {
            log.error("删除 RedisKey[" + storeKey + "] 出错", e);
        }
    }

    /**
     * 生成存储验证码所用的 Key
     * @param businessTypeEnum 业务类型
     * @param email 收件人邮箱
     * @return key
     */
    public String genStoreKey(BusinessTypeEnum businessTypeEnum, String email) {
        return "mycloud:user:" + businessTypeEnum.name() + ":email:" + email;
    }
}
