package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ScanOptions;

/**
 * <p> 描述 : Set相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
 */
@SpringBootTest
class SetOpsTests {

    @Test
    void sAddTest() {
        // sAdd(...) => key -> name, items -> [张三, 李四, 张三], count -> 2
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "张三");
        // sAdd(...) => key -> name, items -> [张三], count -> 0
        RedisStringUtil.SetOps.sAdd("name", "张三");
        // sMembers(...) => key -> name, members -> [李四, 张三]
        RedisStringUtil.SetOps.sMembers("name");
    }

    @Test
    void sRemoveTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "王五");
        RedisStringUtil.SetOps.sAdd("numberSet", "1", "2", "3");
        RedisStringUtil.SetOps.sAdd("letterSet", "a");
        /// test
        RedisStringUtil.SetOps.sRemove("name", "张三", "李四");
        // key -> name, members -> [王五]
        RedisStringUtil.SetOps.sMembers("name");

        /// test
        RedisStringUtil.SetOps.sRemove("numberSet", "1", "2", "3", "4");
        // sMembers(...) => key -> numberSet, members -> []
        RedisStringUtil.SetOps.sMembers("numberSet");

        /// test
        // sRemove(...) => key -> no-exist-key, items -> [any], count -> 0
        RedisStringUtil.SetOps.sRemove("no-exist-key", "any");

        /// test
        // hasKey(...) => key -> letterSet  value -> true
        RedisStringUtil.KeyOps.hasKey("letterSet");
        // sRemove(...) => key -> letterSet, items -> [a], count -> 1
        RedisStringUtil.SetOps.sRemove("letterSet", "a");
        // hasKey(...) => key -> letterSet  value -> false
        RedisStringUtil.KeyOps.hasKey("letterSet");
    }

    @Test
    void sPopTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "张三");
        RedisStringUtil.SetOps.sAdd("age", "18");
        /// test
        // sPop(...) => key -> name, popItem -> 李四
        RedisStringUtil.SetOps.sPop("name");
        // sMembers(...) => key -> name, members -> [张三]
        RedisStringUtil.SetOps.sMembers("name");

        /// test
        // hasKey(...) => key -> age  value -> true
        RedisStringUtil.KeyOps.hasKey("age");
        // sPop(...) => key -> age, popItem -> 18
        RedisStringUtil.SetOps.sPop("age");
        // hasKey(...) => key -> age  value -> false
        RedisStringUtil.KeyOps.hasKey("age");
    }

    @Test
    void sMoveTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "张三", "李四");
        RedisStringUtil.SetOps.sAdd("age1", "18", "26", "30");
        /// test - 正常移动
        // sMove(...) => sourceKey -> name1, destinationKey -> age1, item -> 李四, result -> true
        RedisStringUtil.SetOps.sMove("name1", "李四", "age1");
        // sMembers(...) => key -> name1, members -> [张三]
        RedisStringUtil.SetOps.sMembers("name1");
        // sMembers(...) => key -> age1, members -> [26, 30, 李四, 18]
        RedisStringUtil.SetOps.sMembers("age1");


        /// prepare data
        RedisStringUtil.SetOps.sAdd("name2", "张三", "李四");
        RedisStringUtil.SetOps.sAdd("age2", "18", "26", "30");
        /// test - 移动不存在的item
        // sMove(...) => sourceKey -> name2, destinationKey -> age2, item -> 王五, result -> false
        RedisStringUtil.SetOps.sMove("name2", "王五", "age2");
        // sMembers(...) => key -> name2, members -> [李四, 张三]
        RedisStringUtil.SetOps.sMembers("name2");
        // sMembers(...) => key -> age2, members -> [18, 26, 30]
        RedisStringUtil.SetOps.sMembers("age2");


        /// prepare data
        RedisStringUtil.SetOps.sAdd("age3", "18", "26", "30");
        /// test - 当sourceKey不存在时
        // sMove(...) => sourceKey -> name3, destinationKey -> age3, item -> 王五, result -> false
        RedisStringUtil.SetOps.sMove("name3", "王五", "age3");
        // sMembers(...) => key -> name3, members -> []
        RedisStringUtil.SetOps.sMembers("name3");


        /// prepare data
        RedisStringUtil.SetOps.sAdd("name4", "张三", "李四");
        /// test - 当destinationKey不存在时
        // sMove(...) => sourceKey -> name4, destinationKey -> age4, item -> 张三, result -> true
        RedisStringUtil.SetOps.sMove("name4", "张三", "age4");
        // sMembers(...) => key -> age4, members -> [张三]
        RedisStringUtil.SetOps.sMembers("age4");


        /// test
        RedisStringUtil.SetOps.sAdd("age", "11");
        // hasKey(...) => key -> age  value -> true
        RedisStringUtil.KeyOps.hasKey("age");
        // sMove(...) => sourceKey -> age, destinationKey -> age123, item -> 11, result -> true
        RedisStringUtil.SetOps.sMove("age", "11", "age123");
        // hasKey(...) => key -> age  value -> false
        RedisStringUtil.KeyOps.hasKey("age");
    }

    @Test
    void sSizeTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "张三");
        RedisStringUtil.SetOps.sAdd("name", "王二麻子", "邓沙利文", "邓二洋", "亨得帅", "JustryDeng");
        /// test
        // sSize(...) => key -> name, size -> 7
        RedisStringUtil.SetOps.sSize("name");
        // sMembers(...) => key -> name, members -> [王二麻子, 张三, 亨得帅, 李四, 邓沙利文, 邓二洋, JustryDeng]
        RedisStringUtil.SetOps.sMembers("name");

        //sSize(...) => key -> no-exist-key, size -> 0
        RedisStringUtil.SetOps.sSize("no-exist-key");
    }

    @Test
    void sIsMemberTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "王五");
        /// test
        // sSize(...) => key -> name, size -> 李四, result -> true
        RedisStringUtil.SetOps.sIsMember("name", "李四");

        // sSize(...) => key -> no-exist-key, size -> any, result -> false
        RedisStringUtil.SetOps.sIsMember("no-exist-key", "any");
    }

    @Test
    void sIntersectTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "张三", "李四", "王五");
        RedisStringUtil.SetOps.sAdd("name2", "张三", "王五", "王二麻子");
        RedisStringUtil.SetOps.sAdd("name3", "李四", "邓二洋", "王二麻子");
        RedisStringUtil.SetOps.sAdd("name4", "JustryDeng", "亨得帅");

        /// test
        // sIntersect(...) => key -> name1, otherKey -> name2, intersectResult -> [王五, 张三]
        RedisStringUtil.SetOps.sIntersect("name1", "name2");
        // sIntersect(...) => key -> name2, otherKey -> name3, intersectResult -> [王二麻子]
        RedisStringUtil.SetOps.sIntersect("name2", "name3");
        // sIntersect(...) => key -> name3, otherKey -> name4, intersectResult -> []
        RedisStringUtil.SetOps.sIntersect("name3", "name4");
        // sIntersect(...) => key -> name3, otherKey -> no-exist-key1, intersectResult -> []
        RedisStringUtil.SetOps.sIntersect("name3", "no-exist-key1");
        // sIntersect(...) => key -> no-exist-key2, otherKey -> no-exist-key3, intersectResult -> []
        RedisStringUtil.SetOps.sIntersect("no-exist-key2", "no-exist-key3");
    }

    @Test
    void sIntersectMultiTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        RedisStringUtil.SetOps.sAdd("name2", "0", "1", "2", "3", "4", "5", "6", "7");
        RedisStringUtil.SetOps.sAdd("name3", "0", "1", "2", "3", "4", "5");
        RedisStringUtil.SetOps.sAdd("name4", "0", "1", "2", "3");

        /// test
        // sIntersect(...) => key -> name1, otherKeys -> [name2, name3, name4], intersectResult -> [0, 1, 2, 3]
        RedisStringUtil.SetOps.sIntersect("name1", Lists.newArrayList("name2", "name3", "name4"));
        //  sIntersect(...) => key -> name1, otherKeys -> [name2, name3, no-exist-key], intersectResult -> []
        RedisStringUtil.SetOps.sIntersect("name1", Lists.newArrayList("name2", "name3", "no-exist-key"));
    }

    @Test
    void sIntersectAndStoreTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "张三", "李四", "王五");
        RedisStringUtil.SetOps.sAdd("name2", "张三", "王五", "王二麻子");
        RedisStringUtil.SetOps.sAdd("name3", "李四", "邓二洋", "王二麻子");
        RedisStringUtil.SetOps.sAdd("name4", "JustryDeng", "亨得帅");

        RedisStringUtil.SetOps.sAdd("key123", "0", "1");

        /// test
        // sIntersectAndStore(...) => key -> name1, otherKey -> name2, storeKey -> key123, size -> 2
        RedisStringUtil.SetOps.sIntersectAndStore("name1", "name2", "key123");
        // sMembers(...) => key -> key123, members -> [张三, 王五]
        RedisStringUtil.SetOps.sMembers("key123");

        // sIntersectAndStore(...) => key -> name1, otherKey -> name2, storeKey -> no-exist-key1, size -> 2
        RedisStringUtil.SetOps.sIntersectAndStore("name1", "name2", "no-exist-key1");
        // sMembers(...) => key -> no-exist-key1, members -> [张三, 王五]
        RedisStringUtil.SetOps.sMembers("no-exist-key1");

        // sIntersectAndStore(...) => key -> name3, otherKey -> name4, storeKey -> no-exist-key2, size -> 0
        RedisStringUtil.SetOps.sIntersectAndStore("name3", "name4", "no-exist-key2");
        // sMembers(...) => key -> no-exist-key2, members -> []
        RedisStringUtil.SetOps.sMembers("no-exist-key2");
    }

    @Test
    void sUnionTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2", "3");
        RedisStringUtil.SetOps.sAdd("name2", "2", "X", "Y", "Z");

        /// test
        // sUnion(...) => key -> name1, otherKey -> name2, unionResult -> [1, 3, X, 2, Y, Z, 0]
        RedisStringUtil.SetOps.sUnion("name1", "name2");
    }


    @Test
    void sUnionMultiTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2", "3");
        RedisStringUtil.SetOps.sAdd("name2", "2", "X", "Y", "Z");
        RedisStringUtil.SetOps.sAdd("name3", "a", "b", "Y", "Z");
        RedisStringUtil.SetOps.sAdd("name4", "c", "b");

        /// test
        // sUnion(...) => key -> name1, otherKeys -> [name2, name3, name4], unionResult -> [2, Z, Y, 0, 1, 3, a, c, X, b]
        RedisStringUtil.SetOps.sUnion("name1", Lists.newArrayList("name2", "name3", "name4"));
    }

    @Test
    void sUnionAndStoreTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "a");
        RedisStringUtil.SetOps.sAdd("name2", "b");

        RedisStringUtil.SetOps.sAdd("key123", "value123");

        /// test
        // sUnionAndStore(...) => key -> name1, otherKey -> name2, storeKey -> key123, size -> 2
        RedisStringUtil.SetOps.sUnionAndStore("name1", "name2", "key123");
        // sMembers(...) => key -> key123, members -> [b, a]
        RedisStringUtil.SetOps.sMembers("key123");

        // sUnionAndStore(...) => key -> name1, otherKey -> name2, storeKey -> no-exist-key1, size -> 2
        RedisStringUtil.SetOps.sUnionAndStore("name1", "name2", "no-exist-key1");
        // sMembers(...) => key -> no-exist-key1, members -> [b, a]
        RedisStringUtil.SetOps.sMembers("no-exist-key1");

        // sUnionAndStore(...) => key -> no-exist-key2, otherKey -> no-exist-key3, storeKey -> no-exist-key4, size -> 0
        RedisStringUtil.SetOps.sUnionAndStore("no-exist-key2", "no-exist-key3", "no-exist-key4");
        // hasKey(...) => key -> no-exist-key4  value -> false
        RedisStringUtil.KeyOps.hasKey("no-exist-key4");
    }

    @Test
    void sDifferenceTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2", "3");
        RedisStringUtil.SetOps.sAdd("name2", "2", "X", "Y", "Z");
        /// test
        // sDifference(...) => key -> name1, otherKey -> name2, differenceResult -> [0, 1, 3]
        RedisStringUtil.SetOps.sDifference("name1", "name2");
        // sDifference(...) => key -> name1, otherKey -> no-exist-key, differenceResult -> [0, 1, 2, 3]
        RedisStringUtil.SetOps.sDifference("name1", "no-exist-key");
        // sDifference(...) => key -> no-exist-key, otherKey -> name1, differenceResult -> []
        RedisStringUtil.SetOps.sDifference("no-exist-key", "name1");
        // sDifference(...) => key -> no-exist-key, otherKey -> no-exist-key, differenceResult -> []
        RedisStringUtil.SetOps.sDifference("no-exist-key", "no-exist-key");
    }


    @Test
    void sDifferenceMultiTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2", "3", "4", "5");
        RedisStringUtil.SetOps.sAdd("name2", "3");
        RedisStringUtil.SetOps.sAdd("name3", "2", "4");
        RedisStringUtil.SetOps.sAdd("name4", "1");
        /// test
        // sDifference(...) => key -> name1, otherKeys -> [name2, name3, name4], differenceResult -> [0, 5]
        RedisStringUtil.SetOps.sDifference("name1", Lists.newArrayList("name2", "name3", "name4"));

        // sDifference(...) => key -> name1, otherKeys -> [no-exist-key], differenceResult -> [0, 1, 2, 3, 4, 5]
        RedisStringUtil.SetOps.sDifference("name1", Lists.newArrayList("no-exist-key"));
        // sDifference(...) => key -> no-exist-key, otherKeys -> [name1], differenceResult -> []
        RedisStringUtil.SetOps.sDifference("no-exist-key", Lists.newArrayList("name1"));
        // sDifference(...) => key -> no-exist-key, otherKeys -> [no-exist-key], differenceResult -> []
        RedisStringUtil.SetOps.sDifference("no-exist-key", Lists.newArrayList("no-exist-key"));
    }

    @Test
    void sDifferenceAndStoreTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name1", "0", "1", "2");
        RedisStringUtil.SetOps.sAdd("name2", "2");

        RedisStringUtil.SetOps.sAdd("key123", "value123");

        /// test
        // sDifferenceAndStore(...) => key -> name1, otherKey -> name2, storeKey -> key123, size -> 2
        RedisStringUtil.SetOps.sDifferenceAndStore("name1", "name2", "key123");
        // sMembers(...) => key -> key123, members -> [0, 1]
        RedisStringUtil.SetOps.sMembers("key123");

        // sDifferenceAndStore(...) => key -> name1, otherKey -> name2, storeKey -> no-exist-key1, size -> 2
        RedisStringUtil.SetOps.sDifferenceAndStore("name1", "name2", "no-exist-key1");
        // sMembers(...) => key -> no-exist-key1, members -> [0, 1]
        RedisStringUtil.SetOps.sMembers("no-exist-key1");

        // sDifferenceAndStore(...) => key -> no-exist-key2, otherKey -> no-exist-key3, storeKey -> no-exist-key4, size -> 0
        RedisStringUtil.SetOps.sDifferenceAndStore("no-exist-key2", "no-exist-key3", "no-exist-key4");
        // hasKey(...) => key -> no-exist-key4  value -> false
        RedisStringUtil.KeyOps.hasKey("no-exist-key4");
    }

    @Test
    void sMembersTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "张三");
        /// test
        // sMembers(...) => key -> name, members -> [李四, 张三]
        RedisStringUtil.SetOps.sMembers("name");
        // sMembers(...) => key -> no-exist-key, members -> []
        RedisStringUtil.SetOps.sMembers("no-exist-key");
    }

    @Test
    void sRandomMemberTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四", "王五", "邓二洋");
        /// test
        // sRandomMember(...) => key -> name, randomItem -> 邓二洋
        RedisStringUtil.SetOps.sRandomMember("name");
    }

    @Test
    void sRandomMembersTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("name", "张三", "李四");
        /// test
        // sRandomMembers(...) => key -> name, count -> 1, randomItems -> [李四]
        RedisStringUtil.SetOps.sRandomMembers("name", 1);
        // sRandomMembers(...) => key -> name, count -> 5, randomItems -> [李四, 张三, 张三, 李四, 李四]
        RedisStringUtil.SetOps.sRandomMembers("name", 5);
    }

    @Test
    void sDistinctRandomMembersTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("number",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
        );
        /// test
        // sDistinctRandomMembers(...) => key -> number, count -> 21,
        // distinctRandomItems -> [0, 16, 11, 19, 8, 18, 14, 1, 10, 4, 6, 9, 15, 3, 13, 12, 2, 7, 5, 17]
        RedisStringUtil.SetOps.sDistinctRandomMembers("number", 21);
    }

    @Test
    void sScanTest() {
        /// prepare data
        RedisStringUtil.SetOps.sAdd("nameList", "ne","name","name123","name456","nameAbc","nameXyz");

        /// test
        // sScan(...) => key -> nameList, options -> {}, cursor -> ["name456","ne","nameXyz","name123","name","nameAbc"]
        RedisStringUtil.SetOps.sScan("nameList", ScanOptions.NONE);
        // sScan(...) => key -> nameList, options -> {"pattern":"name*"}, cursor -> ["name456","nameXyz","name123","name","nameAbc"]
        RedisStringUtil.SetOps.sScan("nameList", ScanOptions.scanOptions().match("name*").build());
        // sScan(...) => key -> nameList, options -> {"pattern":"*a*"}, cursor -> ["name456","nameXyz","name123","name","nameAbc"]
        RedisStringUtil.SetOps.sScan("nameList", ScanOptions.scanOptions().match("*a*").build());
        //  sScan(...) => key -> nameList, options -> {"pattern":"n??e"}, cursor -> ["name"]
        RedisStringUtil.SetOps.sScan("nameList", ScanOptions.scanOptions().match("n??e").build());
    }

}