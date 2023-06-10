package org.looko.mycloud.user.util;

import lombok.extern.slf4j.Slf4j;
import org.looko.mycloud.user.enumeration.BusinessTypeEnum;
import org.looko.mycloud.user.exception.MyRedisException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.looko.mycloud.user.constant.Parameters.dLockPrefix;
import static org.looko.mycloud.user.constant.Parameters.validcodeExpireMinutes;

@Slf4j
@Component
public class ValidcodeUtils {

    private final RedissonClient redissonClient;

    public ValidcodeUtils(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
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
     * 验证码存储到 Redis
     * @param businessTypeEnum 业务类型
     * @param email 收件人邮箱
     * @param validcode 验证码
     * @throws MyRedisException redis 依赖异常
     */
    public void saveValidcode(BusinessTypeEnum businessTypeEnum, String email, String validcode) throws MyRedisException {
        String storeKey = genStoreKey(businessTypeEnum, email);
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                redissonClient.getBucket(storeKey).set(validcode, validcodeExpireMinutes, TimeUnit.MINUTES);
            } catch (Exception e) {
                throw new MyRedisException("Redis服务暂不可用", e);
            } finally {
                rLock.unlock();
            }
        }
    }

    /**
     * 从 Redis 获取验证码
     * @param businessTypeEnum 业务类型
     * @param email 收件人邮箱
     * @return 验证码
     * @throws MyRedisException redis 依赖异常
     */
    public String getValidcode(BusinessTypeEnum businessTypeEnum, String email) throws MyRedisException {
        String storeKey = genStoreKey(businessTypeEnum, email);
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                return (String) redissonClient.getBucket(storeKey).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                rLock.unlock();
            }
        }
        return null;
    }

    public boolean checkValidcodeFailed(BusinessTypeEnum businessTypeEnum, String email, String validcode) {
        return !checkValidcodeSuccess(businessTypeEnum, email, validcode);
    }

    public boolean checkValidcodeSuccess(BusinessTypeEnum businessTypeEnum, String email, String validcode) {
        return validcode.equals(getValidcode(businessTypeEnum, email));
    }

    /**
     * 删除特定业务类型和发件人邮箱的验证码
     * 删除出错的情况：
     *      要么中途宕机要么网络断连, 属极端情况, 在有接口流控和 Key 自动过期的情况下可暂不处理
     * @param businessTypeEnum 业务类型
     * @param email 发件人邮箱
     */
    public void deleteValidcode(BusinessTypeEnum businessTypeEnum, String email) {
        String storeKey = genStoreKey(businessTypeEnum, email);
        // 加分布式锁
        RLock rLock = redissonClient.getLock(dLockPrefix + storeKey);
        if (rLock.tryLock()) {
            try {
                redissonClient.getBucket(storeKey).delete();
            } catch (Exception e) {
                log.error("删除 RedisKey[{}] 出错", storeKey, e);
            } finally {
                rLock.unlock();
            }
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
