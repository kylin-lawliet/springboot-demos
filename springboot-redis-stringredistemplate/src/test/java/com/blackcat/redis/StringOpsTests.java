package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p> 描述 : String相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
 */
@SpringBootTest
class StringOpsTests {

    @Test
    void setTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("key123", "value456");
    }

    @Test
    void setBitTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("key", "abc");
        // 字符串abc, 转换为二进制为(为了便于观察，这里主动在8位之间空了一格, 实际上是没有空格的): 01100001 01100010 01100011
        RedisStringUtil.StringOps.get("key");
        /// test
        /*
         * 将offset为6的位置的值设置为1, 将offset为7的位置的值设置为0.
         */
        RedisStringUtil.StringOps.setBit("key", 6, true);
        RedisStringUtil.StringOps.setBit("key", 7, false);
        /*
         * 此时，redis中存储的对应值就变为了(为了便于观察，这里主动在8位之间空了一格, 实际上是没有空格的): 01100010 01100010 01100011
         * 此时，获取到字符串的话，key对应的value值就变为了 bbc
         */
        RedisStringUtil.StringOps.get("key");

        RedisStringUtil.StringOps.setBit("key", 24, true);
    }

    @Test
    void setExTest() {
        RedisStringUtil.StringOps.setEx("ds", "邓沙利文", 30, TimeUnit.SECONDS);
    }

    @Test
    void setIfAbsentTest() {
        /// prepare data
        // setIfAbsent(...) => key -> [ds], value -> [A], result -> true
        RedisStringUtil.StringOps.setIfAbsent("ds", "A");
        /// test
        // setIfAbsent(...) => key -> [ds], value -> [B], result -> false
        RedisStringUtil.StringOps.setIfAbsent("ds", "B");
    }

    @Test
    void setRangeTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "0123456789");
        RedisStringUtil.StringOps.set("jd", "0123456789");
        RedisStringUtil.StringOps.set("ey", "0123456789");
        /// test
        RedisStringUtil.StringOps.setRange("ds", "abcdefghijk", 3);
        RedisStringUtil.StringOps.setRange("jd", "xyz", 3);
        RedisStringUtil.StringOps.setRange("ey", "qwer", 15);
        // get(...) => got value [012abcdefghijk] by key [ds]
        RedisStringUtil.StringOps.get("ds");
        // get(...) => got value [012xyz6789] by key [jd]
        RedisStringUtil.StringOps.get("jd");
        // get(...) => got value [0123456789     qwer] by key [ey]
        RedisStringUtil.StringOps.get("ey");
    }

    @Test
    void sizeTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "hello JustryDeng~");
        /// test
        // size(...) => key [ds] result is -> 17
        RedisStringUtil.StringOps.size("ds");
        // size(...) => key [no-exist-key] result is -> 0
        RedisStringUtil.StringOps.size("no-exist-key");
    }

    @Test
    void multiSetTest() {
        /// prepare data
        Map<String, String> map = new HashMap<>(4);
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        /// test
        RedisStringUtil.StringOps.multiSet(map);
    }

    @Test
    void multiSetIfAbsentTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("k1", "abc");
        Map<String, String> map = new HashMap<>(4);
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        /// test
        // multiSetIfAbsent(...) => maps -> {k3=v3, k1=v1, k2=v2}, result -> false
        RedisStringUtil.StringOps.multiSetIfAbsent(map);

        Map<String, String> mapTwo = new HashMap<>(4);
        mapTwo.put("k4", "v4");
        mapTwo.put("k5", "v5");
        /// test
        // multiSetIfAbsent(...) => maps -> {k4=v4, k5=v5}, result -> true
        RedisStringUtil.StringOps.multiSetIfAbsent(mapTwo);
    }

    @Test
    void incrByTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "123");
        RedisStringUtil.StringOps.set("jd", "123");
        RedisStringUtil.StringOps.set("ey", "我是一只小小小小鸟~嗷嗷！");
        /// test 增
        RedisStringUtil.StringOps.incrBy("ds", 100);
        // get(...) => got value [223] by key [ds]
        RedisStringUtil.StringOps.get("ds");
        /// test 减
        RedisStringUtil.StringOps.incrBy("jd", -100);
        // get(...) => got value [23] by key [jd]
        RedisStringUtil.StringOps.get("jd");

        // 若key对应的value值不是数字，不支持增/减操作， 那么会抛出org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.StringOps.incrBy("ey", 100);
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }
    }

    @Test
    void incrByFloatByTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "123");
        RedisStringUtil.StringOps.set("jd", "123");
        RedisStringUtil.StringOps.set("ey", "我是一只小小小小鸟~嗷嗷！");
        /// test 增
        RedisStringUtil.StringOps.incrByFloat("ds", 100.6);
        // get(...) => got value [223.60000000000000001] by key [ds]
        RedisStringUtil.StringOps.get("ds");
        /// test 减
        RedisStringUtil.StringOps.incrByFloat("jd", -100.7);
        // get(...) => got value [22.3] by key [jd]
        RedisStringUtil.StringOps.get("jd");

        // 若key对应的value值不是数字，不支持增/减操作， 那么会抛出org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.StringOps.incrByFloat("ey", 100.8);
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }
    }

    @Test
    void appendTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "123");
        /// test redis中原本存在key
        RedisStringUtil.StringOps.append("ds", "xyz");
        // append(...) => key -> ds, value -> xyz, result -> 6
        RedisStringUtil.StringOps.get("ds");
        /// test redis中原本不存在key
        RedisStringUtil.StringOps.append("non-exist-key", "qwer");
        // append(...) => key -> non-exist-key, value -> qwer, result -> 4
        RedisStringUtil.StringOps.get("non-exist-key");
    }

    @Test
    void getTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "hello JustryDeng~");
        /// test
        // get(...) => got value [hello JustryDeng~] by key [ds]
        RedisStringUtil.StringOps.get("ds");
        // get(...) => got value [null] by key [non-exist-key]
        RedisStringUtil.StringOps.get("non-exist-key");
    }

    @Test
    void getRangeTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "0123456789");
        /// test
        // getRange(...) => got value [3456] by key [ds]
        RedisStringUtil.StringOps.getRange("ds", 3, 6);
        // getRange(...) => got value [789] by key [ds]
        RedisStringUtil.StringOps.getRange("ds", 7, 15);
        // getRange(...) => got value [] by key [ds]
        RedisStringUtil.StringOps.getRange("ds", 11, 14);
    }

    @Test
    void getAndSetTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("ds", "邓沙利文");
        /// test redis中存在该key
        // getAndSet(...) => key -> ds, value -> 邓二洋, oldValue -> 邓沙利文
        RedisStringUtil.StringOps.getAndSet("ds", "邓二洋");
        // get(...) => got value [邓二洋] by key [ds]
        RedisStringUtil.StringOps.get("ds");

        /// test redis中不存在该key
        // getAndSet(...) => key -> no-exist-key, value -> 亨得帅, oldValue -> null
        RedisStringUtil.StringOps.getAndSet("no-exist-key", "亨得帅");
        // get(...) => got value [亨得帅] by key [no-exist-key]
        RedisStringUtil.StringOps.get("no-exist-key");
    }

    @Test
    void getBitTest() {
        /// prepare data
        RedisStringUtil.StringOps.set("akey", "a");
        /// test
        // getBit(...) => key -> akey, offset -> 6, result -> false
        RedisStringUtil.StringOps.getBit("akey", 6);
        // getBit(...) => key -> akey, offset -> 60, result -> false
        RedisStringUtil.StringOps.getBit("akey", 60);
    }

    @Test
    void multiGetTest() {
        /// prepare data
        Map<String, String> map = new HashMap<>(4);
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        RedisStringUtil.StringOps.multiSetIfAbsent(map);
        /// test
        // multiGet(...) => keys -> [k1, k2, k3, no-exist-key], result -> [v1, v2, v3, null]
        RedisStringUtil.StringOps.multiGet(Lists.newArrayList("k1", "k2", "k3", "no-exist-key"));
    }

}