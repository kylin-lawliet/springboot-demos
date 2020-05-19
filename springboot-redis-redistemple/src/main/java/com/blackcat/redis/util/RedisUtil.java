package com.blackcat.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p> 描述 : RedisUtil工具类
 * @author : blackcat
 * @date  : 2020/5/13 14:03
 * 注：方法中key不能为null 否则报错：non null key required
*/
@Slf4j
@Component
@SuppressWarnings("all")
public class RedisUtil {

	@Autowired
	private RedisTemplate<Object ,Object> redisTemplate;
	/**  默认过期时长：一天  60秒*60*24 **/
	public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
	/**  默认过期时长单位 ：秒  **/
	public final static TimeUnit TIMEUNIT = TimeUnit.SECONDS;
	/**  不设置过期时长  **/
	public final static long NOT_EXPIRE = -1;

	/**
	 * <p> 描述 : 设置key-value
	 * 注: 若已存在相同的key, 那么原来的key-value会被丢弃。
	 * @author : blackcat
	 * @date  : 2020/5/13 15:25
	 * @param key 对应的key
	 * @param value 对应的value
	 * @return void
	*/
	public void set(String key,Object value){
		log.info("set(...) => key -> {}, value -> {}", key, value);
		try {
			set(key, value, NOT_EXPIRE, TIMEUNIT);
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
	}

	/**
	 * <p> 描述 : 设置key-value
	 * 注: 若已存在相同的key, 那么原来的key-value会被丢弃。
	 * 注: 指定失效时间, 过期之后会自动删除
	 * @author : blackcat
	 * @date  : 2020/5/13 15:33
	 * @param key 对应的key
	 * @param value 对应的value
	 * @param timeout 过时时长
	 * @param unit timeout的单位
	 * @return void
	*/
	public void set(String key, Object value, long timeout, TimeUnit unit){
		log.info("setEx(...) => key -> {}, value -> {}, timeout -> {}, unit -> {}",
				key, value, timeout, unit);
		try {
			if(timeout != NOT_EXPIRE){
				redisTemplate.opsForValue().set(key,value,timeout,unit);
			}else {
				redisTemplate.opsForValue().set(key,value,DEFAULT_EXPIRE,unit);
			}
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
	}

	/**
	 * <p> 描述 : 根据key，获取到对应的value值
	 * 注: 若key不存在， 则返回null。
	 * @author : blackcat
	 * @date  : 2020/5/13 15:39
	 * @param key  对应的key
	 * @param t  返回对象value类型
	 * @return T key对应的值
	*/
	public <T> T get(String key,Class<?> t){
		log.info("get(...) => key -> {}", key);
		try {
			T entity = (T) redisTemplate.opsForValue().get(key);
			log.info("get(...) => result -> {} ", entity);
			return entity;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * <p> 描述 : 根据key，获取到对应的value值
	 * 注: 若key不存在， 则返回null
	 * @author : blackcat
	 * @date  : 2020/5/13 15:46
	 * @param key 对应的key
	 * @return key对应的值
	*/
	public Object get(String key){
		log.info("get(...) => key -> {}", key);
		try {
			Object result = redisTemplate.opsForValue().get(key);
			log.info("get(...) => result -> {} ", result);
			return result;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * <p> 描述 : 是否存在对应的key-value
	 * @author : blackcat
	 * @date  : 2020/5/13 15:53
	 * @param key 对应的key
	 * @return 是否存在对应的key-value
	*/
	public boolean hasKey(String key){
		log.info("hasKey(...) => key -> {}", key);
		try {
			Boolean result = redisTemplate.hasKey(key);
			log.info("hasKey(...) => result -> {}", result);
			if (result == null) {
				throw new RedisResultIsNullException();
			}
			return result;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * <p> 描述 : 根据key设置过期时间
	 * 给指定的key对应的key-value设置: 多久过时
	 * 注:过时后，redis会自动删除对应的key-value。
	 * 注:若key不存在，那么也会返回false。
	 * @author : blackcat
	 * @date  : 2020/5/13 15:59
	 * @param key 对应的key
	 * @param timeout 过时时间
	 * @param unit  timeout的单位
	 * @return 操作是否成功
	*/
	public boolean setExpire(String key,long timeout,TimeUnit unit){
		log.info("expire(...) => key -> {}, timeout -> {}, unit -> {}", key, timeout, unit);
		try {
			Boolean result = redisTemplate.expire(key, timeout, unit);
			log.info("expire(...) => result is -> {}", result);
			if (result == null) {
				throw new RedisResultIsNullException();
			}
			return result;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * <p> 描述 : 获取过期时间
	 * @author : blackcat
	 * @date  : 2020/5/16 16:28
	 * @param key 对应的key
	 * @return 返回时间 时间单位 秒
	*/
	public long getExpire(String key) {
		log.info("getExpire(...) => key -> {}", key);
		try {
			return redisTemplate.getExpire(key, TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * <p> 描述 : 删除一个或多个key
	 * 根据key, 删除redis中的对应key-value
	 * 注: 若删除失败, 则返回false
	 * @author : blackcat
	 * @date  : 2020/5/13 16:07
	 * @param key 对应的key
	 */
	public void delete(Object... key) {
		log.info("delete(...) => key -> {}", key);
		try {
			Boolean result;
			if (key != null && key.length > 0) {
				if (key.length == 1) {
					redisTemplate.delete(key[0].toString());
				} else {
					redisTemplate.delete(CollectionUtils.arrayToList(key));
				}
			}
		} catch (Exception e) {
			log.error("删除一个或多个key",e);
		}
	}

	/**
	 * <p> 描述 : 删除一个或多个key 拼接字符串+id
	 * @author : blackcat
	 * @date  : 2020/5/18 15:19
	 * @param subName key拼接字符串
	 * @param key 原id数组
	 * @return void
	 */
	public void delete(String subName,Object... key){
		log.info("delete(...) => key -> {}", key);
		try {
			Boolean result;
			if (ArrayUtils.isEmpty(key)) {
				if (ArrayUtils.getLength(key) == 1) {
					redisTemplate.delete(subName+key[0]);
				} else {
					StringBuilder str = new StringBuilder();
					for (int i = 0; i < key.length; i++) {
						str.append(","+subName + key[i]);
					}
					String idsString = str.toString();
					idsString=idsString.replaceFirst(",","");
					redisTemplate.delete(Arrays.asList(idsString.split(",")));
				}
			}
		} catch (Exception e) {
			log.error("删除一个或多个key",e);
		}
	}

	/**
	 * Hash get
	 *
	 * @param key 对应的key
	 * @param item
	 * @return
	 */
	public Object hget(String key, String item) {
		log.info("delete(...) => key -> {}", key);
		try {
			return redisTemplate.opsForHash().get(key, item);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * <p> 描述 : 获取hashKey对应的所有键值
	 * @author : blackcat
	 * @date  : 2020/5/16 18:12
	 * @param key  对应的key
	 * @return 对应的多个键值
	*/
	public Map<Object, Object> hmget(String key) {
		try {
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
		return redisTemplate.opsForHash().entries(key);
	}

	/**
	 * Hash set
	 *
	 * @param key 对应的key
	 * @param map
	 * @return
	 */
	public boolean hmset(String key, Map<String, Object> map) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * Hash set and expire time set
	 *
	 * @param key 对应的key
	 * @param map
	 * @param time
	 * @return
	 */
	public boolean hmset(String key, Map<String, Object> map, long time, TimeUnit unit) {
		try {
			redisTemplate.opsForHash().putAll(key, map);
			if (time > 0) {
				setExpire(key, time,unit);
			}
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key
	 * @param item
	 * @param value
	 * @return
	 */
	public boolean hset(String key, String item, Object value) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 向一张hash表中放入数据,如果不存在将创建
	 *
	 * @param key
	 * @param item
	 * @param value
	 * @param time  如果已存在的hash表有时间,这里将会替换原有的时间
	 * @return
	 */
	public boolean hset(String key, String item, Object value, long time, TimeUnit unit) {
		try {
			redisTemplate.opsForHash().put(key, item, value);
			if (time > 0) {
				setExpire(key, time, unit);
			}
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 删除hash表中的值
	 *
	 * @param key
	 * @param item Object...
	 */
	public void hdel(String key, Object... item) {
		try {
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
		redisTemplate.opsForHash().delete(key, item);
	}

	/**
	 * 判断hash表中是否有该项的值
	 *
	 * @param key
	 * @param item
	 * @return
	 */
	public boolean hHasKey(String key, String item) {
		try {
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
		return redisTemplate.opsForHash().hasKey(key, item);
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key
	 * @param item
	 * @param by
	 * @return
	 */
	public double hincr(String key, String item, double by) {
		try {
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	/**
	 * hash递减
	 *
	 * @param key
	 * @param item
	 * @param by
	 * @return
	 */
	public double hdecr(String key, String item, double by) {
		try {
		} catch (Exception e) {
			log.error(key.toString(), e);
		}
		return redisTemplate.opsForHash().increment(key, item, -by);
	}

	// ============================set=============================

	/**
	 * 根据key获取Set中的所有值
	 *
	 * @param key 键
	 * @return
	 */
	public Set<Object> sGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean sHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 将数据放入set缓存
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSet(String key, Object... values) {
		try {
			return redisTemplate.opsForSet().add(key, values);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long sSetAndTime(String key, long time, TimeUnit unit, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				setExpire(key, time, unit);
			}
			return count;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long sGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long setRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	// ============================zset=============================
	/**
	 * 根据key获取Set中的所有值
	 *
	 * @param key 键
	 * @return
	 */
	public Set<Object> zSGet(String key) {
		try {
			return redisTemplate.opsForSet().members(key);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * 根据value从一个set中查询,是否存在
	 *
	 * @param key   键
	 * @param value 值
	 * @return true 存在 false不存在
	 */
	public boolean zSHasKey(String key, Object value) {
		try {
			return redisTemplate.opsForSet().isMember(key, value);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	public Boolean zSSet(String key, Object value, double score) {
		try {
			return redisTemplate.opsForZSet().add(key, value, 2);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 将set数据放入缓存
	 *
	 * @param key    键
	 * @param time   时间(秒)
	 * @param values 值 可以是多个
	 * @return 成功个数
	 */
	public long zSSetAndTime(String key, long time, TimeUnit unit, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().add(key, values);
			if (time > 0) {
				setExpire(key, time, unit);
			}
			return count;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 获取set缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long zSGetSetSize(String key) {
		try {
			return redisTemplate.opsForSet().size(key);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 移除值为value的
	 *
	 * @param key    键
	 * @param values 值 可以是多个
	 * @return 移除的个数
	 */
	public long zSetRemove(String key, Object... values) {
		try {
			Long count = redisTemplate.opsForSet().remove(key, values);
			return count;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	// ===============================list=================================
	/**
	 * 获取list缓存的内容
	 *
	 * @param key   键
	 * @param start 开始 0 是第一个元素
	 * @param end   结束 -1代表所有值
	 * @return
	 * @取出来的元素 总数 end-start+1
	 */
	public List<Object> lGet(String key, long start, long end) {
		try {
			return redisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * 获取list缓存的长度
	 *
	 * @param key 键
	 * @return
	 */
	public long lGetListSize(String key) {
		try {
			return redisTemplate.opsForList().size(key);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}

	/**
	 * 通过索引 获取list中的值
	 *
	 * @param key   键
	 * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
	 * @return
	 */
	public Object lGetIndex(String key, long index) {
		try {
			return redisTemplate.opsForList().index(key, index);
		} catch (Exception e) {
			log.error(key.toString(), e);
			return null;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, Object value) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, Object value, long time, TimeUnit unit) {
		try {
			redisTemplate.opsForList().rightPush(key, value);
			if (time > 0) {
				setExpire(key, time, unit);
			}
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @return
	 */
	public boolean lSet(String key, List<Object> value) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 将list放入缓存
	 *
	 * @param key   键
	 * @param value 值
	 * @param time  时间(秒)
	 * @return
	 */
	public boolean lSet(String key, List<Object> value, long time, TimeUnit unit) {
		try {
			redisTemplate.opsForList().rightPushAll(key, value);
			if (time > 0) {
				setExpire(key, time, unit);
			}
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 根据索引修改list中的某条数据
	 *
	 * @param key   键
	 * @param index 索引
	 * @param value 值
	 * @return
	 */
	public boolean lUpdateIndex(String key, long index, Object value) {
		try {
			redisTemplate.opsForList().set(key, index, value);
			return true;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return false;
		}
	}

	/**
	 * 移除N个值为value
	 *
	 * @param key   键
	 * @param count 移除多少个
	 * @param value 值
	 * @return 移除的个数
	 */
	public long lRemove(String key, long count, Object value) {
		try {
			Long remove = redisTemplate.opsForList().remove(key, count, value);
			return remove;
		} catch (Exception e) {
			log.error(key.toString(), e);
			return 0;
		}
	}



	/**
	 * 当使用Pipeline 或 Transaction操作redis时, (不论redis中实际操作是否成功, 这里)结果(都)会返回null。
	 * 此时，如果试着将null转换为基本类型的数据时，会抛出此异常。
	 *
	 * 即: 此工具类中的某些方法, 希望不要使用Pipeline或Transaction操作redis。
	 *
	 * 注: Pipeline 或 Transaction默认是不启用的， 可详见源码:
	 *     @see LettuceConnection#isPipelined()
	 *     @see LettuceConnection#isQueueing()
	 *     @see JedisConnection#isPipelined()
	 *     @see JedisConnection#isQueueing()
	 *
	 * @author JustryDeng
	 * @date 2020/3/14 21:22:39
	 */
	public static class RedisResultIsNullException extends NullPointerException {

		public RedisResultIsNullException() {
			super();
		}

		public RedisResultIsNullException(String message) {
			super(message);
		}
	}
}
