package com.blackcat.redis;

import com.blackcat.redis.util.RedisStringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.HashSet;
import java.util.Set;

/**
 * <p> 描述 : ZSet相关的操作测试
 * @author : blackcat
 * @date  : 2020/5/11 16:13
*/
@SpringBootTest
class ZSetOpsTests {

    @Test
    void zAddTest() {
        /// test - 基本使用
        // zAdd(...) => key -> name, item -> 张三, score -> 1.0, result -> true
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 0.1);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "邓二洋", 0.5);
        // zWholeZSetItem(...) => key -> name, result -> [张三, 邓二洋, 王五]
        RedisStringUtil.ZSetOps.zWholeZSetItem("name");

        /// test - 因为已存在相同的item了，所以会返回false; 但是 还item的score会被更新
        // zAdd(...) => key -> name, item -> 张三, score -> 2.0, result -> false
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 2);
        // zWholeZSetItem(...) => key -> name, result -> [邓二洋, 王五, 张三]
        RedisStringUtil.ZSetOps.zWholeZSetItem("name");

        /// test - 若score的值一样，则按照item排序
        RedisStringUtil.ZSetOps.zAdd("age", "18", 95.27);
        RedisStringUtil.ZSetOps.zAdd("age", "26", 95.27);
        RedisStringUtil.ZSetOps.zAdd("age", "30", 95.27);
        // zWholeZSetItem(...) => key -> age, result -> [18, 26, 30]
        RedisStringUtil.ZSetOps.zWholeZSetItem("age");
    }

    @Test
    void zAddEntriesTest() {
        /// test - 普通测试
        Set<ZSetOperations.TypedTuple<String>> entries = new HashSet<>();
        entries.add(new DefaultTypedTuple<>("item1", 123D));
        entries.add(new DefaultTypedTuple<>("item2", 124D));
        entries.add(new DefaultTypedTuple<>("item3", 12.3));
        RedisStringUtil.ZSetOps.zAdd("zset-key", entries);
        /*
         * zAdd(...) => key -> zset-key, entries ->
         * [{"score":12.3,"value":"item3"},{"score":123.0,"value":"item1"},{"score":124.0,"value":"item2"}],
         * count -> 3
         */
        RedisStringUtil.ZSetOps.zWholeZSetItem("zset-key");

        /// test - 测试返回值是此次添加的成功的个数(,而不是对应zset的size)
        entries.clear();
        entries.add(new DefaultTypedTuple<>("item4", 12.5));
        // zAdd(...) => key -> zset-key, entries -> [{"score":12.5,"value":"item4"}], count -> 1
        RedisStringUtil.ZSetOps.zAdd("zset-key", entries);
        // zWholeZSetItem(...) => key -> zset-key, result -> [item3, item4, item1, item2]
        RedisStringUtil.ZSetOps.zWholeZSetItem("zset-key");

        /// test - 测试相同的项添加会失败，但是score会被更新
        entries.clear();
        entries.add(new DefaultTypedTuple<>("item1", 123D));
        entries.add(new DefaultTypedTuple<>("item2", 124D));
        entries.add(new DefaultTypedTuple<>("item1", 666.6));
        /*
         * Add(...) => key -> zset-key123, entries ->
         * [{"score":66.6,"value":"item1"},{"score":123.0,"value":"item1"},{"score":124.0,"value":"item2"}],
         * count -> 2
         */
        RedisStringUtil.ZSetOps.zAdd("zset-key123", entries);
        // zWholeZSetItem(...) => key -> zset-key123, result -> [item1, item2]
        RedisStringUtil.ZSetOps.zWholeZSetItem("zset-key123");

        entries.clear();
        entries.add(new DefaultTypedTuple<>("item1", 666.6));
        //  zAdd(...) => key -> zset-key123, entries -> [{"score":666.6,"value":"item1"}], count -> 0
        RedisStringUtil.ZSetOps.zAdd("zset-key123", entries);
        // zWholeZSetItem(...) => key -> zset-key123, result -> [item2, item1]
        RedisStringUtil.ZSetOps.zWholeZSetItem("zset-key123");
    }

    @Test
    void zRemoveTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "邓二洋", 4);

        /// test
        // zRemove(...) => key -> name, items -> [李四, 王五, JustryDeng], count -> 2
        RedisStringUtil.ZSetOps.zRemove("name", "李四", "王五", "JustryDeng");
        // zWholeZSetItem(...) => key -> name, result -> [张三, 邓二洋]
        RedisStringUtil.ZSetOps.zWholeZSetItem("name");

        // zRemove(...) => key -> no-exist-key, items -> [any], count -> 0
        RedisStringUtil.ZSetOps.zRemove("no-exist-key", "any");
    }

    @Test
    void zRemoveRangeTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "邓二洋", 4);

        RedisStringUtil.ZSetOps.zAdd("site", "百度", 1);
        RedisStringUtil.ZSetOps.zAdd("site", "腾讯", 2);
        RedisStringUtil.ZSetOps.zAdd("site", "字节跳动", 3);
        RedisStringUtil.ZSetOps.zAdd("site", "阿里巴巴", 4);

        /// test - 使用正向索引(0、1、2...)
        // zRemoveRange(...) => key -> name, startRange -> 0, endRange -> 2, count -> 3
        RedisStringUtil.ZSetOps.zRemoveRange("name", 0, 2);
        // zWholeZSetItem(...) => key -> name, result -> [邓二洋]
        RedisStringUtil.ZSetOps.zWholeZSetItem("name");

        /// test - 使用反向索引(-1、-2...)
        // zRemoveRange(...) => key -> site, startRange -> -2, endRange -> -1, count -> 2
        RedisStringUtil.ZSetOps.zRemoveRange("site", -2, -1);
        // zWholeZSetItem(...) => key -> site, result -> [百度, 腾讯]
        RedisStringUtil.ZSetOps.zWholeZSetItem("site");

        /// test - key不存在时, 返回0
        // zRemoveRange(...) => key -> no-exist-key, startRange -> 0, endRange -> 1, count -> 0
        RedisStringUtil.ZSetOps.zRemoveRange("no-exist-key", 0, 1);
    }

    @Test
    void zRemoveRangeByScoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "邓二洋", 4);

        /// test - 普通测试
        // zRemoveRangeByScore(...) => key -> name, startIndex -> 1.0, startIndex -> 3.0, count -> 3
        RedisStringUtil.ZSetOps.zRemoveRangeByScore("name", 1, 3);
        // zWholeZSetItem(...) => key -> name, result -> [邓二洋]
        RedisStringUtil.ZSetOps.zWholeZSetItem("name");

        /// test - key不存在时, 返回0
        // zRemoveRangeByScore(...) => key -> no-exist-key, startIndex -> 0.0, startIndex -> 1.0, count -> 0
        RedisStringUtil.ZSetOps.zRemoveRangeByScore("no-exist-key", 0, 1);
    }

    @Test
    void zIncrementScoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        /// test
        //  zIncrementScore(...) => key -> name, item -> 张三, delta -> -123.0, scoreValue -> -122.0
        RedisStringUtil.ZSetOps.zIncrementScore("name", "张三", -123);
        // zScore(...) => key -> name, item -> 张三, score -> -122.0
        RedisStringUtil.ZSetOps.zScore("name", "张三");
    }

    @Test
    void zRankTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "A张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "C李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "B王五", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "D赵六", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "E田七", 4);

        /// test - key或item不存在时， 返回null
        // zRank(...) => key -> no-exist-key, item -> any, rank -> null
        RedisStringUtil.ZSetOps.zRank("no-exist-key", "any");
        // zRank(...) => key -> name, item -> no-exist-item, rank -> null
        RedisStringUtil.ZSetOps.zRank("name", "no-exist-item");

        /// test - 排名从0开始(即: 此时，排名值等于位置索引值)
        // zRank(...) => key -> name, item -> A张三, rank -> 0
        RedisStringUtil.ZSetOps.zRank("name", "A张三");

        /// test - 排序规则是score,item: 优先以score排序，若score相同，则再按item排序
        // key -> name, item -> B王五, rank -> 1
        RedisStringUtil.ZSetOps.zRank("name", "B王五");

        /// test - 普通测试
        // zRank(...) => key -> name, item -> E田七, rank -> 4
        RedisStringUtil.ZSetOps.zRank("name", "E田七");
    }

    @Test
    void zReverseRankTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "A张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "C李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "B王五", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "D赵六", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "E田七", 4);

        /// test - key或item不存在时， 返回null
        // zReverseRank(...) => key -> no-exist-key, item -> any, reverseRank -> null
        RedisStringUtil.ZSetOps.zReverseRank("no-exist-key", "any");
        // zReverseRank(...) => key -> name, item -> no-exist-item, reverseRank -> null
        RedisStringUtil.ZSetOps.zReverseRank("name", "no-exist-item");

        /// test - 排名从0开始(即: 此时，排名值等于位置索引值)
        // zReverseRank(...) => key -> name, item -> A张三, reverseRank -> 4
        RedisStringUtil.ZSetOps.zReverseRank("name", "A张三");

        /// test - 排序规则是score,item: 优先以score排序，若score相同，则再按item排序
        // zReverseRank(...) => key -> name, item -> B王五, reverseRank -> 3
        RedisStringUtil.ZSetOps.zReverseRank("name", "B王五");

        /// test - 普通测试
        // zReverseRank(...) => key -> name, item -> E田七, reverseRank -> 0
        RedisStringUtil.ZSetOps.zReverseRank("name", "E田七");
    }

    @Test
    void zRangeTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        // zRange(...) => key -> name, start -> 0, end -> 2, result -> [张三, 李四, 王五]
        RedisStringUtil.ZSetOps.zRange("name", 0, 2);

        /// test - 普通测试
        // zRange(...) => key -> name, start -> -3, end -> -1, result -> [王五, 赵六, 田七]
        RedisStringUtil.ZSetOps.zRange("name", -3, -1);

        /// test - 当[start, end]的范围比实际zset的范围大时, 返回范围上"交集"对应的项集合
        // zRange(...) => key -> name, start -> 0, end -> 100, result -> [张三, 李四, 王五, 赵六, 田七]
        RedisStringUtil.ZSetOps.zRange("name", 0, 100);

        /// test - key不存在时， 返回空的集合
        // zRange(...) => key -> no-exist-key, start -> 0, end -> 1, result -> []
        RedisStringUtil.ZSetOps.zRange("no-exist-key", 0, 1);
    }

    @Test
    void zRangeWithScoresTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        /*
         * zRangeWithScores(...) => key -> name, start -> 0, end -> 2, entries ->
         * [
         *  {"score":1.0,"value":"张三"},
         *  {"score":2.0,"value":"李四"},
         *  {"score":3.0,"value":"王五"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeWithScores("name", 0, 2);

        /// test - 普通测试
        /*
         * zRangeWithScores(...) => key -> name, start -> -3, end -> -1, entries ->
         * [
         *  {"score":3.0,"value":"王五"},
         *  {"score":4.0,"value":"赵六"},
         *  {"score":5.0,"value":"田七"}
         * ]
         */
        //
        RedisStringUtil.ZSetOps.zRangeWithScores("name", -3, -1);

        /// test - 当[start, end]的范围比实际zset的范围大时, 返回范围上"交集"对应的entry集合
        /*
         * zRangeWithScores(...) => key -> name, start -> 0, end -> 100, entries ->
         * [
         *  {"score":1.0,"value":"张三"},
         *  {"score":2.0,"value":"李四"},
         *  {"score":3.0,"value":"王五"},
         *  {"score":4.0,"value":"赵六"},
         *  {"score":5.0,"value":"田七"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeWithScores("name", 0, 100);

        /// test - key不存在时， 返回空的集合
        // zRangeWithScores(...) => key -> no-exist-key, start -> 0, end -> 1, entries -> []
        RedisStringUtil.ZSetOps.zRangeWithScores("no-exist-key", 0, 1);
    }

    @Test
    void zRangeByScoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        // zRangeByScore(...) => key -> name, minScore -> 0.9, maxScore -> 3.0, items -> [张三, 李四, 王五]
        RedisStringUtil.ZSetOps.zRangeByScore("name", 0.9, 3.0);

        /// test - 当[minScore, maxScore]的范围比实际zset中score的范围大时, 返回范围上"交集"对应的项集合
        /*
         * zRangeByScore(...) => key -> name, minScore -> 1.0, maxScore -> 100.123, items ->
         * [张三, 李四, 王五, 赵六, 田七]
         */
        RedisStringUtil.ZSetOps.zRangeByScore("name", 1, 100.123);

        /// test - key不存在时， 返回空的集合
        // zRangeByScore(...) => key -> no-exist-key, minScore -> 1.0E-4, maxScore -> 123.123, items -> []
        RedisStringUtil.ZSetOps.zRangeByScore("no-exist-key", 0.0001, 123.123);
    }

    @Test
    void zRangeByScoreTwoTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 负数索引测试
        // zRangeByScore(...) => key -> name, minScore -> 0.9, maxScore -> 3.0, offset -> 1, count -> -4, items -> [李四, 王五]
        RedisStringUtil.ZSetOps.zRangeByScore("name", 0.9, 3.0, 1, -4);

        /// test - 正数offset-count测试
        /*
         * zRangeByScore(...) => key -> name, minScore -> 1.0, maxScore -> 100.123, offset -> 2,
         * count -> 3, items -> [王五, 赵六, 田七]
         */
        RedisStringUtil.ZSetOps.zRangeByScore("name", 1, 100.123, 2, 3);

        /// test - key不存在时， 返回空的集合
        // zRangeByScore(...) => key -> no-exist-key, minScore -> 1.0E-4, maxScore -> 123.123, offset -> 1, count -> 1, items -> []
        RedisStringUtil.ZSetOps.zRangeByScore("no-exist-key", 0.0001, 123.123, 1, 1);
    }

    @Test
    void zRangeByScoreWithScoresTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        /*
         * zRangeByScoreWithScores(...) => key -> name, minScore -> 0.9, maxScore -> 3.0, entries ->
         * [
         *  {"score":1.0,"value":"张三"},
         *  {"score":2.0,"value":"李四"},
         *  {"score":3.0,"value":"王五"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("name", 0.9, 3.0);

        /// test - 当[minScore, maxScore]的范围比实际zset中score的范围大时, 返回范围上"交集"对应的项集合
        /*
          * zRangeByScoreWithScores(...) => key -> name, minScore -> 1.0, maxScore -> 100.123, entries ->
          * [
          *  {"score":1.0,"value":"张三"},
          *  {"score":2.0,"value":"李四"},
          *  {"score":3.0,"value":"王五"},
          *  {"score":4.0,"value":"赵六"},
          *  {"score":5.0,"value":"田七"}
          * ]
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("name", 1, 100.123);

        /// test - key不存在时， 返回空的集合
        /*
         * zRangeByScoreWithScores(...) => key -> no-exist-key, minScore -> 1.0E-4, maxScore -> 123.123,
         * entries -> []
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("no-exist-key", 0.0001, 123.123);
    }

    @Test
    void zRangeByScoreWithScoresTwoTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 负数索引测试(不推荐使用负数， 容易引起歧义)
        /*
         * zRangeByScoreWithScores(...) => key -> name, minScore -> 0.9, maxScore -> 4.0, offset -> 1,
         * count -> -2, entries ->
         * [
         *  {"score":2.0,"value":"李四"},
         *  {"score":3.0,"value":"王五"},
         *  {"score":4.0,"value":"赵六"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("name", 0.9, 4.0,
                1, -2);

        /// test - 正数offset-count个数测试
        /*
         * zRangeByScoreWithScores(...) => key -> name, minScore -> 0.9, maxScore -> 4.0,
         * offset -> 1, count -> 3, entries ->
         * [
         *  "score":2.0,"value":"李四"},
         *  {"score":3.0,"value":"王五"},
         *  {"score":4.0,"value":"赵六"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("name", 0.9, 4.0,
                1, 3);

        /// test - 普通测试
        /*
         * zRangeByScoreWithScores(...) => key -> name, minScore -> 0.9, maxScore -> 5.0, offset -> 2, count -> 3, entries ->
         * [
         *  {"score":3.0,"value":"王五"},
         *  {"score":4.0,"value":"赵六"},
         *  {"score":5.0,"value":"田七"}
         * ]
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("name", 0.9, 5.0,
                2, 3);

        /// test - key不存在时， 返回空的集合
        /*
         * zRangeByScoreWithScores(...) => key -> no-exist-key, minScore -> 1.0E-4,
         * maxScore -> 123.123, offset -> 0, count -> 100, entries -> []
         */
        RedisStringUtil.ZSetOps.zRangeByScoreWithScores("no-exist-key", 0.0001,
                123.123, 0, 100);
    }

    @Test
    void zReverseRangeTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        //  zReverseRange(...) => key -> name, start -> 1, end -> 2, entries -> [赵六, 王五]
        RedisStringUtil.ZSetOps.zReverseRange("name", 1, 2);

        /// test - key不存在时， 返回空的集合
        // zReverseRange(...) => key -> no-exist-key, start -> 0, end -> 100, entries -> []
        RedisStringUtil.ZSetOps.zReverseRange("no-exist-key", 0, 100);
    }

    @Test
    void zReverseRangeByScoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);

        /// test - 普通测试
        // zReverseRangeByScore(...) => key -> name, minScore -> 0.9, maxScore -> 3.0, items -> [王五, 李四, 张三]
        RedisStringUtil.ZSetOps.zReverseRangeByScore("name", 0.9, 3.0);

        /// test - 当[minScore, maxScore]的范围比实际zset中score的范围大时, 返回范围上"交集"对应的项集合
        /*
         * zReverseRangeByScore(...) => key -> name, minScore -> 1.0, maxScore -> 100.123,
         * items -> [田七, 赵六, 王五, 李四, 张三]
         */
        RedisStringUtil.ZSetOps.zReverseRangeByScore("name", 1, 100.123);

        /// test - key不存在时， 返回空的集合
        // zReverseRangeByScore(...) => key -> no-exist-key, minScore -> 1.0E-4, maxScore -> 123.123, items -> []
        RedisStringUtil.ZSetOps.zReverseRangeByScore("no-exist-key", 0.0001, 123.123);
    }

    @Test
    void zCountTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);
        /// test
        // zCount(...) => key -> name, minScore -> 1.9, maxScore -> 4.5, count -> 3
        RedisStringUtil.ZSetOps.zCount("name", 1.9, 4.5);
    }

    @Test
    void zSizeTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name", "李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name", "王五", 3);
        RedisStringUtil.ZSetOps.zAdd("name", "赵六", 4);
        RedisStringUtil.ZSetOps.zAdd("name", "田七", 5);
        /// test
        //  zSize(...) => key -> name, size -> 5
        RedisStringUtil.ZSetOps.zSize("name");
        /// test
        //  zZCard(...) => key -> name, size -> 5
        RedisStringUtil.ZSetOps.zZCard("name");
    }

    @Test
    void zScoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name", "张三", 1.06006);
        /// test
        // zScore(...) => key -> name, item -> 张三, score -> 1.06006
        RedisStringUtil.ZSetOps.zScore("name", "张三");
    }

    @Test
    void zUnionAndStoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name1", "a", 1);
        RedisStringUtil.ZSetOps.zAdd("name2", "a", 2);
        RedisStringUtil.ZSetOps.zAdd("name2", "b", 2);

        RedisStringUtil.ZSetOps.zAdd("key123", "value123", 111);

        /// test
        // zUnionAndStore(...) => key -> name1, otherKey -> name2, storeKey -> key123, size -> 2
        RedisStringUtil.ZSetOps.zUnionAndStore("name1", "name2", "key123");
        // zWholeZSetItem(...) => key -> key123, result -> [a, b]
        RedisStringUtil.ZSetOps.zWholeZSetItem("key123");
        // key -> key123, entries -> [{"score":2.0,"value":"b"},{"score":3.0,"value":"a"}]
        RedisStringUtil.ZSetOps.zWholeZSetEntry("key123");

        // zUnionAndStore(...) => key -> name1, otherKey -> name2, storeKey -> no-exist-key1, size -> 2
        RedisStringUtil.ZSetOps.zUnionAndStore("name1", "name2", "no-exist-key1");
        // zWholeZSetItem(...) => key -> no-exist-key1, result -> [a, b]
        RedisStringUtil.ZSetOps.zWholeZSetItem("no-exist-key1");

        // zUnionAndStore(...) => key -> no-exist-key2, otherKey -> no-exist-key3, storeKey -> no-exist-key4, size -> 0
        RedisStringUtil.ZSetOps.zUnionAndStore("no-exist-key2", "no-exist-key3", "no-exist-key4");
        // hasKey(...) => key -> no-exist-key4  value -> false
        RedisStringUtil.KeyOps.hasKey("no-exist-key4");
    }

    @Test
    void zIntersectAndStoreTest() {
        /// prepare data
        RedisStringUtil.ZSetOps.zAdd("name1", "张三", 1);
        RedisStringUtil.ZSetOps.zAdd("name1","李四", 2);
        RedisStringUtil.ZSetOps.zAdd("name1", "王五", 3);

        RedisStringUtil.ZSetOps.zAdd("name2", "张三", 4);
        RedisStringUtil.ZSetOps.zAdd("name2","王五", 5);
        RedisStringUtil.ZSetOps.zAdd("name2", "王二麻子", 6);

        RedisStringUtil.ZSetOps.zAdd("name3", "李四", 7);
        RedisStringUtil.ZSetOps.zAdd("name3","邓二洋", 8);
        RedisStringUtil.ZSetOps.zAdd("name3", "王二麻子", 9);

        RedisStringUtil.ZSetOps.zAdd("name4", "JustryDeng", 10);
        RedisStringUtil.ZSetOps.zAdd("name4","亨得帅", 11);

        RedisStringUtil.ZSetOps.zAdd("key123", "itemOne", 12);
        RedisStringUtil.ZSetOps.zAdd("key123","itemTwo ", 13);


        /// test
        // zWholeZSetEntry(...) => key -> key123, entries -> [{"score":12.0,"value":"itemOne"},{"score":13.0,"value":"itemTwo"}]
        RedisStringUtil.ZSetOps.zWholeZSetEntry("key123");
        // zIntersectAndStore(...) => key -> name1, otherKey -> name2, storeKey -> key123, size -> 2
        RedisStringUtil.ZSetOps.zIntersectAndStore("name1", "name2", "key123");
        // zWholeZSetEntry(...) => key -> key123, entries -> [{"score":5.0,"value":"张三"},{"score":8.0,"value":"王五"}]
        RedisStringUtil.ZSetOps.zWholeZSetEntry("key123");

        // zIntersectAndStore(...) => key -> name1, otherKey -> name2, storeKey -> no-exist-key1, size -> 2
        RedisStringUtil.ZSetOps.zIntersectAndStore("name1", "name2", "no-exist-key1");
        // zWholeZSetEntry(...) => key -> no-exist-key1, entries -> [{"score":5.0,"value":"张三"},{"score":8.0,"value":"王五"}]
        RedisStringUtil.ZSetOps.zWholeZSetEntry("no-exist-key1");

        // zIntersectAndStore(...) => key -> name3, otherKey -> name4, storeKey -> no-exist-key2, size -> 0
        RedisStringUtil.ZSetOps.zIntersectAndStore("name3", "name4", "no-exist-key2");
        // hasKey(...) => key -> no-exist-key2  value -> false
        RedisStringUtil.KeyOps.hasKey("no-exist-key2");
    }
}