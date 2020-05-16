package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * <p> 描述 :  List相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
 */
@SpringBootTest
class ListOpsTests {

    @Test
    void lLeftPushTest() {
        /// prepare data
        // lLeftPush(...) => key -> nameList, item -> 张三, size -> 1
        RedisStringUtil.ListOps.lLeftPush("nameList", "张三");
        /// test
        // lRange(...) => key -> nameList, start -> 0, end -> -1, result -> [张三]
        RedisStringUtil.ListOps.lRange("nameList", 0, -1);
    }

    @Test
    void lLeftPushAllTest() {
        /// test lLeftPushAll(String key, String... items)
        // lLeftPushAll(...) => key -> nameList, items -> [张三, 李四, 王五, 张三], size -> 4
        RedisStringUtil.ListOps.lLeftPushAll("nameList", "张三", "李四", "王五", "张三");
        // lRange(...) => key -> nameList, start -> 0, end -> -1, result -> [张三, 王五, 李四, 张三]
        RedisStringUtil.ListOps.lRange("nameList", 0, -1);

        /// test lLeftPushAll(String key, Collection<String> items)
        // lLeftPushAll(...) => key -> hobbyList, items -> [油炸, 烧烤, 冰激凌, 串串, 火锅], size -> 5
        RedisStringUtil.ListOps.lLeftPushAll("hobbyList", "油炸", "烧烤", "冰激凌", "串串", "火锅");
        // lRange(...) => key -> hobbyList, start -> 0, end -> -1, result -> [火锅, 串串, 冰激凌, 烧烤, 油炸]
        RedisStringUtil.ListOps.lRange("hobbyList", 0, -1);
    }

    @Test
    void lLeftPushIfPresentTest() {
        // lLeftPushIfPresent(...) => key -> nameList, item -> 张三, size -> 0
        RedisStringUtil.ListOps.lLeftPushIfPresent("nameList", "张三");
        // lLeftPush(...) => key -> nameList, item -> 李四, size -> 1
        RedisStringUtil.ListOps.lLeftPush("nameList", "李四");
        // lLeftPushIfPresent(...) => key -> nameList, item -> 王五, size -> 2
        RedisStringUtil.ListOps.lLeftPushIfPresent("nameList", "王五");
        // lRange(...) => key -> nameList, start -> 0, end -> -1, result -> [王五, 李四]
        RedisStringUtil.ListOps.lRange("nameList", 0, -1);
    }

    @Test
    void lLeftPushPivotTest() {
        /// prepare data
        RedisStringUtil.ListOps.lLeftPushAll("letterList", "Y", "D", "X", "D", "B", "A");

        /// test
        // lLeftPush(...) => key -> letterList, pivot -> N, item -> M, size -> -1
        RedisStringUtil.ListOps.lLeftPush("letterList", "N", "M");
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [A, B, D, X, D, Y]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);

        // lLeftPush(...) => key -> letterList, pivot -> D, item -> C, size -> 7
        RedisStringUtil.ListOps.lLeftPush("letterList", "D", "C");
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [A, B, C, D, X, D, Y]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);
    }

    @Test
    void lLeftPopTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "A", "B", "C", "D", "E");
        RedisStringUtil.ListOps.lRightPush("tmpList", "α");
        /// test
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [A, B, C, D, E]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);
        // lLeftPop(...) => key -> letterList, item -> A
        RedisStringUtil.ListOps.lLeftPop("letterList");
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [B, C, D, E]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);

        // lLeftPop(...) => key -> tmpList, item -> α
        RedisStringUtil.ListOps.lLeftPop("tmpList");
        // lLeftPop(...) => key -> tmpList, item -> null
        RedisStringUtil.ListOps.lLeftPop("tmpList");

        // lLeftPop(...) => key -> no-exist-key, item -> null
        RedisStringUtil.ListOps.lLeftPop("no-exist-key");

        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("numberList", "zero");
        // hasKey(...) => key -> numberList  value -> true
        RedisStringUtil.KeyOps.hasKey("numberList");
        // lLeftPop(...) => key -> numberList, item -> zero
        RedisStringUtil.ListOps.lLeftPop("numberList");
        // hasKey(...) => key -> numberList  value -> false
        RedisStringUtil.KeyOps.hasKey("numberList");
    }

    @Test
    void lLeftPopTimeoutTest() {
        /// prepare data
        RedisStringUtil.ListOps.lLeftPush("tmpList", "α");
        /// test
        // lLeftPop(...) => key -> tmpList, timeout -> 30, unit -> SECONDS, item -> α
        RedisStringUtil.ListOps.lLeftPop("tmpList", 30 , TimeUnit.SECONDS);
        System.out.println("-----------------------");
        // 阻塞30秒, 后， 日志输出: lLeftPop(...) => key -> tmpList, timeout -> 30, unit -> SECONDS, item -> null
        RedisStringUtil.ListOps.lLeftPop("tmpList", 30 , TimeUnit.SECONDS);
        System.out.println("-----------------------");
        // 阻塞30秒, 后， 日志输出: lLeftPop(...) => key -> no-exist-key, timeout -> 30, unit -> SECONDS, item -> null
        RedisStringUtil.ListOps.lLeftPop("no-exist-key", 30 , TimeUnit.SECONDS);
    }

    @Test
    void lRightPopAndLeftPushTest() {
        /// prepare data
        // lRightPushAll(...) => key -> sourceKey, items -> [a, b, c], size -> 3
        RedisStringUtil.ListOps.lRightPushAll("sourceKey", "a", "b", "c");
        // lRightPushAll(...) => key -> destinationKey, items -> [X, Y, Z], size -> 3
        RedisStringUtil.ListOps.lRightPushAll("destinationKey", "X", "Y", "Z");
        /// test 基础测试
        // lRightPopAndLeftPush(...) => sourceKey -> sourceKey, destinationKey -> destinationKey, item -> c
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        // lRange(...) => key -> sourceKey, start -> 0, end -> -1, result -> [a, b]
        RedisStringUtil.ListOps.lRange("sourceKey", 0, -1);
        // lRange(...) => key -> destinationKey, start -> 0, end -> -1, result -> [c, X, Y, Z]
        RedisStringUtil.ListOps.lRange("destinationKey", 0, -1);

        // test null测试
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        System.out.println("-------------------");
        // lRightPopAndLeftPush(...) => sourceKey -> sourceKey, destinationKey -> destinationKey, item -> null
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        // sourceKey, start -> 0, end -> -1, result -> []
        RedisStringUtil.ListOps.lRange("sourceKey", 0, -1);
        // lRange(...) => key -> destinationKey, start -> 0, end -> -1, result -> [a, b, c, X, Y, Z]
        RedisStringUtil.ListOps.lRange("destinationKey", 0, -1);

        /// test 若将(sourceKey对应的)list中的所有元素都pop完了，那么该sourceKey会被删除
        RedisStringUtil.ListOps.lRightPushAll("sourceKey123", "abc");
        // hasKey(...) => key -> sourceKey123  value -> true
        RedisStringUtil.KeyOps.hasKey("sourceKey123");
        // lRightPopAndLeftPush(...) => sourceKey -> sourceKey123, destinationKey -> destinationKey123, item -> abc
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey123", "destinationKey123");
        // hasKey(...) => key -> sourceKey123  value -> false
        RedisStringUtil.KeyOps.hasKey("sourceKey123");
    }

    @Test
    void lRightPopAndLeftPushTimeoutTest() {
        /// prepare data
        // lRightPushAll(...) => key -> sourceKey, items -> [a, b, c], size -> 3
        RedisStringUtil.ListOps.lRightPushAll("sourceKey", "a", "b", "c");
        // lRightPushAll(...) => key -> destinationKey, items -> [X, Y, Z], size -> 3
        RedisStringUtil.ListOps.lRightPushAll("destinationKey", "X", "Y", "Z");
        /// test 基础测试
        // lRightPopAndLeftPush(...) => sourceKey -> sourceKey, destinationKey -> destinationKey, item -> c
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        // lRange(...) => key -> sourceKey, start -> 0, end -> -1, result -> [a, b]
        RedisStringUtil.ListOps.lRange("sourceKey", 0, -1);
        // lRange(...) => key -> destinationKey, start -> 0, end -> -1, result -> [c, X, Y, Z]
        RedisStringUtil.ListOps.lRange("destinationKey", 0, -1);

        // test null测试
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey");
        System.out.println("-------------------");
        // lRightPopAndLeftPush(...) => sourceKey -> sourceKey, destinationKey -> destinationKey, timeout -> 30, unit -> SECONDS, item -> null
        RedisStringUtil.ListOps.lRightPopAndLeftPush("sourceKey", "destinationKey", 30, TimeUnit.SECONDS);
        // sourceKey, start -> 0, end -> -1, result -> []
        RedisStringUtil.ListOps.lRange("sourceKey", 0, -1);
        // lRange(...) => key -> destinationKey, start -> 0, end -> -1, result -> [a, b, c, X, Y, Z]
        RedisStringUtil.ListOps.lRange("destinationKey", 0, -1);
    }

    @Test
    void lSetTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "a", "b", "c");
        /// test
        // lSet(...) => key -> letterList, index -> 2, item -> C
        RedisStringUtil.ListOps.lSet("letterList", 2, "C");
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [a, b, C]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);

        // 若索引越界， 会抛出org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.ListOps.lSet("letterList", 100, "hah");
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }

        // key不存在时，也会抛出org.springframework.data.redis.RedisSystemException
        try {
            RedisStringUtil.ListOps.lSet("non-exist-key", 0, "any");
        } catch (Exception e) {
            System.err.println(e.getClass().getName());
        }
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);
    }

    @Test
    void lIndexTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "a", "b", "c");
        /// test
        // lIndex(...) => key -> letterList, index -> 0, item -> a
        RedisStringUtil.ListOps.lIndex("letterList", 0);
        // lIndex(...) => key -> letterList, index -> -3, item -> a
        RedisStringUtil.ListOps.lIndex("letterList", -3);

        // lIndex(...) => key -> letterList, index -> 100, item -> null
        RedisStringUtil.ListOps.lIndex("letterList", 100);
        // lIndex(...) => key -> no-exist-key, index -> -3, item -> null
        RedisStringUtil.ListOps.lIndex("no-exist-key", -3);
    }

    @Test
    void lRangeTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "a", "b", "c");
        /// test - 获取key对应的整个list
        // lRange(...) => key -> letterList, start -> 0, end -> -1, result -> [a, b, c]
        RedisStringUtil.ListOps.lRange("letterList", 0, -1);
        /// test - 普通获取
        // lRange(...) => key -> letterList, start -> 0, end -> 1, result -> [a, b]
        RedisStringUtil.ListOps.lRange("letterList", 0, 1);
        /// test - 当获取的范围比list的范围还要大时，获取到的是这两个范围的交集。
        // lRange(...) => key -> letterList, start -> 0, end -> 100, result -> [a, b, c]
        RedisStringUtil.ListOps.lRange("letterList", 0, 100);
        /// test - 当key不存在时，获取到的是空的集合
        // lRange(...) => key -> no-exist-key, start -> 0, end -> -1, result -> []
        RedisStringUtil.ListOps.lRange("no-exist-key", 0, -1);
    }

    @Test
    void lSizeTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "a", "b", "c");
        /// test
        // lSize(...) => key -> letterList, size -> 3
        RedisStringUtil.ListOps.lSize("letterList");
        // lSize(...) => key -> no-exist-key, size -> 0
        RedisStringUtil.ListOps.lSize("no-exist-key");
    }

    @Test
    void lRemoveTest() {
        /// test - 全部删除
        RedisStringUtil.ListOps.lRightPushAll("list1", "a", "A", "b", "A", "c", "A", "d");
        // lRemove(...) => key -> list1, index -> 0, expectCount -> A, actualCount -> 3
        RedisStringUtil.ListOps.lRemove("list1", 0, "A");
        // lWholeList(...) => key -> list1, result -> [a, b, c, d]
        RedisStringUtil.ListOps.lWholeList("list1");

        /// test - 从左往右删除
        RedisStringUtil.ListOps.lRightPushAll("list2", "a", "A", "b", "A", "c", "A", "d");
        // lRemove(...) => key -> list2, index -> 2, expectCount -> A, actualCount -> 2
        RedisStringUtil.ListOps.lRemove("list2", 2, "A");
        // lWholeList(...) => key -> list2, result -> [a, b, c, A, d]
        RedisStringUtil.ListOps.lWholeList("list2");

        /// test - 从右往左删除
        RedisStringUtil.ListOps.lRightPushAll("list3", "a", "A", "b", "A", "c", "A", "d");
        // lRemove(...) => key -> list3, index -> -2, expectCount -> A, actualCount -> 2
        RedisStringUtil.ListOps.lRemove("list3", -2, "A");
        // lWholeList(...) => key -> list3, result -> [a, A, b, c, d]
        RedisStringUtil.ListOps.lWholeList("list3");


        /// test - 当list中,值等于item的项的个数少于expectCount时，那么会删除list中所有的值等于item的项
        RedisStringUtil.ListOps.lRightPushAll("list4", "a", "A", "b", "A", "c", "A", "d");
        // lRemove(...) => key -> list4, index -> 100, expectCount -> A, actualCount -> 3
        RedisStringUtil.ListOps.lRemove("list4", 100, "A");
        // lWholeList(...) => key -> list4, result -> [a, b, c, d]
        RedisStringUtil.ListOps.lWholeList("list4");


        /// test - 当key不存在时, 返回0
        // lRemove(...) => key -> no-exist-key, index -> 1, expectCount -> A, actualCount -> 0
        RedisStringUtil.ListOps.lRemove("no-exist-key", 1, "A");

        /// test 若lRemove后， 将(key对应的)list中没有任何元素了，那么该key会被删除
        RedisStringUtil.ListOps.lRightPushAll("sourceKey123", "abc");
        // hasKey(...) => key -> sourceKey123  value -> true
        RedisStringUtil.KeyOps.hasKey("sourceKey123");
        // lRemove(...) => key -> sourceKey123, index -> 1, expectCount -> abc, actualCount -> 1
        RedisStringUtil.ListOps.lRemove("sourceKey123", 1, "abc");
        // hasKey(...) => key -> sourceKey123  value -> false
        RedisStringUtil.KeyOps.hasKey("sourceKey123");
    }

    @Test
    void lTrimTest() {
        /// prepare data
        RedisStringUtil.ListOps.lRightPushAll("letterList", "a", "b", "c", "d", "e", "f");
        RedisStringUtil.ListOps.lRightPushAll("numberList", "0", "1", "2", "3");
        RedisStringUtil.ListOps.lRightPushAll("nameList", "张三", "李四");
        /// test - 正常测试
        RedisStringUtil.ListOps.lTrim("letterList", 1, 3);
        // lWholeList(...) => key -> letterList, result -> [b, c, d]
        RedisStringUtil.ListOps.lWholeList("letterList");

        /// test - 当[start-end]范围超出list的的范围时，取交集
        RedisStringUtil.ListOps.lTrim("numberList", 1, 100);
        // lWholeList(...) => key -> numberList, result -> [1, 2, 3]
        RedisStringUtil.ListOps.lWholeList("numberList");

        /// test - 当[start-end]范围超出list的的范围时，取交集
        // hasKey(...) => key -> nameList  value -> true
        RedisStringUtil.KeyOps.hasKey("nameList");
        RedisStringUtil.ListOps.lTrim("nameList", 100, 1000);
        // lWholeList(...) => key -> nameList, result -> []
        RedisStringUtil.ListOps.lWholeList("nameList");
        // hasKey(...) => key -> nameList  value -> false
        RedisStringUtil.KeyOps.hasKey("nameList");
    }
}