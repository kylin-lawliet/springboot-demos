package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p> 描述 : Key相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
 */
@SpringBootTest
class KeyOpsTests {

    @Test
    void deleteTest() {
        // prepare data
        RedisStringUtil.StringOps.set("ds", "邓沙利文");
        // test
        RedisStringUtil.KeyOps.delete("ds");
    }

    @Test
    void batchDeleteTest() {
        // prepare data
        RedisStringUtil.StringOps.set("ds1", "邓沙利文");
        RedisStringUtil.StringOps.set("ds2", "邓二洋");
        RedisStringUtil.StringOps.set("ds3", "JustryDeng");
        RedisStringUtil.StringOps.set("ds4", "dengshuai");
        // test
        RedisStringUtil.KeyOps.delete(
                Lists.newArrayList("ds1", "ds2", "ds3", "ds4")
        );
    }

    @Test
    void dumpAndRestoreTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("key-abc", "hello dengshuai~");
        RedisStringUtil.StringOps.set("12345", "上山打老虎~");

        /// test  dump
        byte[] serializedValue = RedisStringUtil.KeyOps.dump("key-abc");

        /// test  restore
        // redis中会出现新的key-value, ("new-key", "hello dengshuai~"), 并在60秒后过期
        RedisStringUtil.KeyOps.restore("new-key", serializedValue, 60, TimeUnit.SECONDS);
        //
        try {
            RedisStringUtil.KeyOps.restore("12345", serializedValue, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            // 会出现RedisSystemException异常。 输出: org.springframework.data.redis.RedisSystemException
            System.err.println(e.getClass().getName());
        }
        // redis中原来的key-value ("12345", "上山打老虎~")会被替换为("12345", "hello dengshuai~"), 且该key-value会在60秒后过期
        RedisStringUtil.KeyOps.restore("12345", serializedValue, 60, TimeUnit.SECONDS, true);
    }

    @Test
    void hasKeyTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "hello JustryDeng~");
        /// test
        RedisStringUtil.KeyOps.hasKey("ds");
        RedisStringUtil.KeyOps.hasKey("ds123");
    }

    @Test
    void expireAndExpireAtTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("jd", "hello JustryDeng~");
        RedisStringUtil.StringOps.set("ds", "hello dengshuai~");

        /// test expire
        // expire(...) => key [jd], timeout -> 100, unit -> SECONDS, result is -> true
        RedisStringUtil.KeyOps.expire("jd", 100, TimeUnit.SECONDS);
        // expire(...) => key [no-exist-key], timeout -> 100, unit -> SECONDS, result is -> false
        RedisStringUtil.KeyOps.expire("no-exist-key", 100, TimeUnit.SECONDS);

        /// test expireAt
        long nowTimestamp = new Date().getTime();
        long sixtySeconds = 60 * 1000;
        Date sixtySecondsDate = new Date(nowTimestamp + sixtySeconds);
        // expireAt(...) => key [ds], date -> Sat Mar 14 20:39:50 CST 2020, result is -> true
        RedisStringUtil.KeyOps.expireAt("ds", sixtySecondsDate);
        // expireAt(...) => key [no-exist-key], date -> Sat Mar 14 20:40:58 CST 2020, result is -> false
        RedisStringUtil.KeyOps.expireAt("no-exist-key", sixtySecondsDate);
    }

    /**
     * 慎用keys方法！ 该方法性能较低，若redis中键值对较多， 则该方法可能耗时较长。
     */
    @Test
    void keysTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("key-abc", "hello dengshuai~");
        RedisStringUtil.StringOps.set("d", "1~");
        RedisStringUtil.StringOps.set("ds", "2~");
        RedisStringUtil.StringOps.set("ds123", "3~");
        RedisStringUtil.StringOps.set("ds456", "4~");

        /// test
        // find match pattern [d*] keys -> [ds456, ds, ds123, d]
        RedisStringUtil.KeyOps.keys("d*");
        // find match pattern [*s*] keys -> [ds456, ds, ds123]
        RedisStringUtil.KeyOps.keys("*s*");
        // find match pattern [*s*3*] keys -> [ds123]
        RedisStringUtil.KeyOps.keys("*s*3*");
        // 全模糊， 如: find match pattern [*] keys -> [key-abc, ds, ds123, ds456]
        RedisStringUtil.KeyOps.keys("*");
        // find match pattern [d?] keys -> [ds]
        RedisStringUtil.KeyOps.keys("d?");
    }

    @Test
    void moveTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "hello JustryDeng~");
        /// test
        RedisStringUtil.KeyOps.move("ds", 1);
    }

    @Test
    void persistTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("jd", "hello JustryDeng~");
        RedisStringUtil.StringOps.setEx("ds", "hello dengshuai~", 60, TimeUnit.SECONDS);
        /// test
        // persist key[jd] corresponding key-value, result -> false
        RedisStringUtil.KeyOps.persist("jd");
        // persist key[ds] corresponding key-value, result -> true
        RedisStringUtil.KeyOps.persist("ds");
        // persist key[no-exist-key] corresponding key-value, result -> false
        RedisStringUtil.KeyOps.persist("no-exist-key");
    }

    @Test
    void getExpireTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("jd", "hello JustryDeng~");
        RedisStringUtil.StringOps.setEx("ey", "hello dengshuai~", 500, TimeUnit.MILLISECONDS);
        RedisStringUtil.StringOps.setEx("ds", "hello dengshuai~", 1001, TimeUnit.MILLISECONDS);
        RedisStringUtil.StringOps.setEx("foo", "hello dengshuai~", 1900, TimeUnit.MILLISECONDS);
        RedisStringUtil.StringOps.setEx("oop", "hello dengshuai~", 2000, TimeUnit.MILLISECONDS);
        /// test getExpire
        // key[jd] corresponding key-value, timeout is -> -1 SECONDS
        RedisStringUtil.KeyOps.getExpire("jd");
        // key[ey] corresponding key-value, timeout is -> 0 SECONDS
        RedisStringUtil.KeyOps.getExpire("ey");
        // key[ds] corresponding key-value, timeout is -> 1 SECONDS
        RedisStringUtil.KeyOps.getExpire("ds");
        // key[foo] corresponding key-value, timeout is -> 2 SECONDS
        RedisStringUtil.KeyOps.getExpire("foo");
        // key[oop] corresponding key-value, timeout is -> 2 SECONDS
        RedisStringUtil.KeyOps.getExpire("oop");
        // key[no-exist-key] corresponding key-value, timeout is -> -2 SECONDS
        RedisStringUtil.KeyOps.getExpire("no-exist-key");

        /// test getExpire
        // key[jd] corresponding key-value, timeout is -> -1 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("jd", TimeUnit.MILLISECONDS);
        // key[ey] corresponding key-value, timeout is -> -2 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("ey", TimeUnit.MILLISECONDS);
        // key[ds] corresponding key-value, timeout is -> 170 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("ds", TimeUnit.MILLISECONDS);
        // key[foo] corresponding key-value, timeout is -> 1070 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("foo", TimeUnit.MILLISECONDS);
        // key[oop] corresponding key-value, timeout is -> 1177 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("oop", TimeUnit.MILLISECONDS);
        // key[no-exist-key] corresponding key-value, timeout is -> -2 MILLISECONDS
        RedisStringUtil.KeyOps.getExpire("no-exist-key", TimeUnit.MILLISECONDS);
    }

    @Test
    void randomKeyTest() {
        RedisStringUtil.KeyOps.randomKey();
    }

    @Test
    void renameTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("abc", "hello JustryDeng~");
        RedisStringUtil.StringOps.set("9527", "你好 华安~");
        RedisStringUtil.StringOps.set("8888", "你好 8888~");
        /// test
        RedisStringUtil.KeyOps.rename("abc", "xyz");

        try {
            RedisStringUtil.KeyOps.rename("qwer", "xxx");
        } catch (Exception e) {
            /* 若oldKey不存在，则抛出org.springframework.data.redis.RedisSystemException:
             *                         Error in execution; nested exception is
             *                         io.lettuce.core.RedisCommandExecutionException:
             *                             ERR no such key
             */
            System.out.println(e.getClass().getName());
        }
        RedisStringUtil.KeyOps.rename("8888", "9527");

    }

    @Test
    void renameIfAbsentTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("abc", "hello JustryDeng~");
        RedisStringUtil.StringOps.set("9527", "你好 华安~");
        RedisStringUtil.StringOps.set("8888", "你好 8888~");
        /// test
        RedisStringUtil.KeyOps.renameIfAbsent("abc", "xyz");

        try {
            RedisStringUtil.KeyOps.renameIfAbsent("qwer", "xxx");
        } catch (Exception e) {
            /* 若oldKey不存在，则抛出org.springframework.data.redis.RedisSystemException:
             *                         Error in execution; nested exception is
             *                         io.lettuce.core.RedisCommandExecutionException:
             *                             ERR no such key
             */
            System.out.println(e.getClass().getName());
        }
        RedisStringUtil.KeyOps.renameIfAbsent("8888", "9527");
    }

    @Test
    void typeTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "hello JustryDeng~");
        /// test
        // key [ds] corresponding value DataType is -> STRING
        RedisStringUtil.KeyOps.type("ds");
        RedisStringUtil.KeyOps.type("no-exist-key");
    }
}