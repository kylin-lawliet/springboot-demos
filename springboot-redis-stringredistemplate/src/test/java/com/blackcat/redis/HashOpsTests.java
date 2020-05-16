package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ScanOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 描述 : Hash相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
 */
@SpringBootTest
class HashOpsTests {

    @Test
    void hPutTest() {
        // hPut(...) => key -> ds, entryKey -> name, entryValue -> 邓沙利文
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        // hGetAll(...) => key -> ds, result -> {name=邓沙利文}
        RedisStringUtil.HashOps.hGetAll("ds");

        // hPut(...) => key -> ds, entryKey -> name, entryValue -> 邓二洋
        RedisStringUtil.HashOps.hPut("ds", "name", "邓二洋");
        // hGetAll(...) => key -> ds, result -> {name=邓二洋}
        RedisStringUtil.HashOps.hGetAll("ds");
    }

    @Test
    void hPutAllTest() {
        /// prepare data
        Map<String, String> map = new HashMap<>(4);
        map.put("entryKey1", "entryValue111");
        map.put("entryKey2", "entryValue222");
        map.put("entryKey3", "entryValue333");
        /// test
        // hPutAll(...) => key -> ds, maps -> {entryKey2=entryValue2, entryKey1=entryValue1, entryKey3=entryValue3}
        RedisStringUtil.HashOps.hPutAll("ds", map);
    }

    @Test
    void hPutIfAbsentTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        /// test
        // hPutIfAbsent(...) => key -> ds, entryKey -> name, entryValue -> JustryDeng, result -> false
        RedisStringUtil.HashOps.hPutIfAbsent("ds", "name", "JustryDeng");
        // hPutIfAbsent(...) => key -> ds, entryKey -> gender, entryValue -> 男, result -> true
        RedisStringUtil.HashOps.hPutIfAbsent("ds", "gender", "男");
    }

    @Test
    void hGetTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        /// test
        // hGet(...) => key -> ds, got entryValue [邓沙利文] by entryKey [name]
        RedisStringUtil.HashOps.hGet("ds", "name");
        // hGet(...) => key -> ds, got entryValue [null] by entryKey [non-exist-entryKey]
        RedisStringUtil.HashOps.hGet("ds", "non-exist-entryKey");
        // hGet(...) => key -> non-exist-key, got entryValue [null] by entryKey [any]
        RedisStringUtil.HashOps.hGet("non-exist-key", "any");
    }

    @Test
    void hGetAllTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "motto", "我是一只小小小小鸟~");
        /// test
        // get hash result [{name=邓沙利文, motto=我是一只小小小小鸟~}] by key [ds]
        RedisStringUtil.HashOps.hGetAll("ds");
        // hGetAll(...) => get hash result [{}] by key [non-exist-key]
        RedisStringUtil.HashOps.hGetAll("non-exist-key");
    }

    @Test
    void hMultiGetTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "birthday", "1994-02-05");
        /// test
        // hMultiGet(...) => key -> ds, entryKeys -> [name, motto, birthday], entryValues -> [邓沙利文, null, 1994-02-05]
        RedisStringUtil.HashOps.hMultiGet("ds", Lists.newArrayList("name", "motto", "birthday"));
        // hMultiGet(...) => key -> no-exist-key, entryKeys -> [name, motto, birthday], entryValues -> [null, null, null]
        RedisStringUtil.HashOps.hMultiGet("no-exist-key", Lists.newArrayList("name", "motto", "birthday"));
    }

    @Test
    void hDeleteTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "birthday", "1994-02-05");
        RedisStringUtil.HashOps.hPut("ds", "hobby", "女");
        /// test
        // key -> ds, entryKeys -> [name, birthday, hobby, non-exist-entryKey], count -> 3
        RedisStringUtil.HashOps.hDelete("ds", "name", "birthday", "hobby", "non-exist-entryKey");
        // hDelete(...) => key -> non-exist-key, entryKeys -> [any], count -> 0
        RedisStringUtil.HashOps.hDelete("non-exist-key", "any");

        RedisStringUtil.HashOps.hPut("jd", "name", "JustryDeng");
        // hasKey(...) => key -> jd  value -> true
        RedisStringUtil.KeyOps.hasKey("jd");
        // hDelete(...) => key -> jd, entryKeys -> [name], count -> 1
        RedisStringUtil.HashOps.hDelete("jd", "name");
        // hasKey(...) => key -> jd  value -> false
        RedisStringUtil.KeyOps.hasKey("jd");
    }

    @Test
    void hExistsTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        /// test
        // hDelete(...) => key -> ds, entryKeys -> name, exist -> true
        RedisStringUtil.HashOps.hExists("ds", "name");
        // hDelete(...) => key -> ds, entryKeys -> non-exist-entryKey, exist -> false
        RedisStringUtil.HashOps.hExists("ds", "non-exist-entryKey");
        // hDelete(...) => key -> non-exist-key, entryKeys -> any, exist -> false
        RedisStringUtil.HashOps.hExists("non-exist-key", "any");
    }

    @Test
    void hIncrByTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "age", "26");
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        /// test
        // hIncrBy(...) => key -> ds, entryKey -> age, increment -> 40, result -> 66
        RedisStringUtil.HashOps.hIncrBy("ds", "age", 40);
        // hIncrBy(...) => key -> ds, entryKey -> non-exist-entryKey, increment -> 40, result -> 40
        RedisStringUtil.HashOps.hIncrBy("ds", "non-exist-entryKey", 40);
        // hIncrBy(...) => key -> non-exist-key, entryKey -> any, increment -> 40, result -> 40
        RedisStringUtil.HashOps.hIncrBy("non-exist-key", "any", 40);

        // org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.HashOps.hIncrBy("ds", "name", 40);
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }
    }

    @Test
    void hIncrByFloatTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "age", "123");
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        /// test
        // key -> ds, entryKey -> age, increment -> 100.6, result -> 223.6
        RedisStringUtil.HashOps.hIncrByFloat("ds", "age", 100.6);
        // hIncrByFloat(...) => key -> ds, entryKey -> non-exist-entryKey, increment -> -100.6, result -> -100.6
        RedisStringUtil.HashOps.hIncrByFloat("ds", "non-exist-entryKey", -100.6);
        // hIncrByFloat(...) => key -> non-exist-key, entryKey -> any, increment -> 100.6, result -> 100.6
        RedisStringUtil.HashOps.hIncrByFloat("non-exist-key", "any", 100.6);

        // org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.HashOps.hIncrByFloat("ds", "name", 6.66);
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }
    }

    @Test
    void hKeysTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "age", "62");
        /// test
        // key -> ds, entryKeys -> [name, age]
        RedisStringUtil.HashOps.hKeys("ds");
        // hKeys(...) => key -> no-exist-key, entryKeys -> []
        RedisStringUtil.HashOps.hKeys("no-exist-key");
    }

    @Test
    void hValuesTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "age", "62");
        /// test
        // hValues(...) => key -> ds, entryValues -> [邓沙利文, 62]
        RedisStringUtil.HashOps.hValues("ds");
        // hValues(...) => key -> no-exist-key, entryValues -> []
        RedisStringUtil.HashOps.hValues("no-exist-key");
    }

    @Test
    void hSizeTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "name", "邓沙利文");
        RedisStringUtil.HashOps.hPut("ds", "age", "62");
        /// test
        // hSize(...) => key -> ds, count -> 2
        RedisStringUtil.HashOps.hSize("ds");
        // key -> no-exist-key, count -> 0
        RedisStringUtil.HashOps.hSize("no-exist-key");
    }

    @Test
    void hScanTest() {
        /// prepare data
        RedisStringUtil.HashOps.hPut("ds", "ne", "v0");
        RedisStringUtil.HashOps.hPut("ds", "name", "v1");
        RedisStringUtil.HashOps.hPut("ds", "name123", "v2");
        RedisStringUtil.HashOps.hPut("ds", "name456", "v3");
        RedisStringUtil.HashOps.hPut("ds", "nameAbc", "v4");
        RedisStringUtil.HashOps.hPut("ds", "nameXyz", "v5");
        /// test
        // hScan(...) => key -> ds, options -> {}, cursor -> [{"ne":"v0"},{"name":"v1"},{"name123":"v2"},{"name456":"v3"},{"nameAbc":"v4"},{"nameXyz":"v5"}]
        RedisStringUtil.HashOps.hScan("ds", ScanOptions.NONE);
        // hScan(...) => key -> ds, options -> {"pattern":"name*"}, cursor -> [{"name":"v1"},{"name123":"v2"},{"name456":"v3"},{"nameAbc":"v4"},{"nameXyz":"v5"}]
        RedisStringUtil.HashOps.hScan("ds", ScanOptions.scanOptions().match("name*").build());
        // hScan(...) => key -> ds, options -> {"pattern":"*a*"}, cursor -> [{"name":"v1"},{"name123":"v2"},{"name456":"v3"},{"nameAbc":"v4"},{"nameXyz":"v5"}]
        RedisStringUtil.HashOps.hScan("ds", ScanOptions.scanOptions().match("*a*").build());
        // hScan(...) => key -> ds, options -> {"pattern":"n??e"}, cursor -> [{"name":"v1"}]
        RedisStringUtil.HashOps.hScan("ds", ScanOptions.scanOptions().match("n??e").build());
    }
}