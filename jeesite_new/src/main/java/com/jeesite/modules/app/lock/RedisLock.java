package com.jeesite.modules.app.lock;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 利用redis实现分布式锁
 * <p>
 * Redis有一系列的命令，特点是以NX结尾，NX是Not eXists的缩写，如SETNX命令就应该理解为：SET if Not Exists。这系列的命令非常有用
 * 这里讲使用SETNX来实现分布式锁。
 * 利用SETNX非常简单地实现分布式锁。例如：某客户端要获得一个名字foo的锁，客户端使用下面的命令进行获取：
 * SETNX lock.foo <current Unix time + lock timeout + 1>
 * 如返回1，则该客户端获得锁，把lock.foo的键值设置为时间值表示该键已被锁定，该客户端最后可以通过DEL lock.foo来释放该锁。
 * 如返回0，表明该锁已被其他客户端取得，这时我们可以先返回或进行重试等对方完成或等待锁超时。
 * <p>
 * 解决死锁：
 * 上面的锁定逻辑有一个问题：如果一个持有锁的客户端失败或崩溃了不能释放锁，该怎么解决？
 * 我们可以通过锁的键对应的时间戳来判断这种情况是否发生了，如果当前的时间已经大于lock.foo的值，说明该锁已失效，可以被重新使用。
 * 发生这种情况时，可不能简单的通过DEL来删除锁，然后再SETNX一次，当多个客户端检测到锁超时后都会尝试去释放它，这里就可能出现一个竞态条件，让我们模拟一下这个场景：
 * C0操作超时了，但它还持有着锁，C1和C2读取lock.foo检查时间戳，先后发现超时了。
 * C1 发送DEL lock.foo
 * C1 发送SETNX lock.foo 并且成功了。
 * C2 发送DEL lock.foo
 * C2 发送SETNX lock.foo 并且成功了。
 * 这样一来，C1，C2都拿到了锁！问题大了！
 * <p>
 * 幸好这种问题是可以避免D，让我们来看看C3这个客户端是怎样做的：
 * C3发送SETNX lock.foo 想要获得锁，由于C0还持有锁，所以Redis返回给C3一个0
 * C3发送GET lock.foo 以检查锁是否超时了，如果没超时，则等待或重试。
 * 反之，如果已超时，C3通过下面的操作来尝试获得锁：
 * GETSET lock.foo <current Unix time + lock timeout + 1>
 * 通过GETSET，C3拿到的时间戳如果仍然是超时的，那就说明，C3如愿以偿拿到锁了。
 * 如果在C3之前，有个叫C4的客户端比C3快一步执行了上面的操作，那么C3拿到的时间戳是个未超时的值，这时，C3没有如期获得锁，需要再次等
 * 待或重试。留意一下，尽管C3没拿到锁，但它改写了C4设置的锁的超时值，不过这一点非常微小的误差带来的影响可以忽略不计。
 * <p>
 * 注意：为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端
 * 因为某个耗时的操作而挂起，操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。
 * <p>
 * 另外：每台服务器时间存在差异，如果从服务器本身获取时间，则会导致多个实例在不同服务器上时存在时间差，导致锁失效。
 * 所以必须使用统一的时间，那么可以从redis获取一个统一的时间。
 *
 * @author 陈默涵
 */
@Component
public class RedisLock {

    /**
     * 锁定超时时间
     */
    private static final int LOCK_TIMEOUT = 30;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取redis上的时间
     * 由于各个服务器时间可能存在不同步，导致获取的时间不一致，所以这里统一从redis获取时间
     */
    private int getRedisTime() {
        long time = new Date().getTime();//同一台服务器可以不用通过redis获取时间
        return (int) (time / 1000);
    }

    /**
     * 获取锁
     */
    public void lock(String key) {
        this.lock(key, LOCK_TIMEOUT);
    }

    /**
     * 获取锁
     *
     * @param key         key名称
     * @param lockTimeout 超时时间（秒）
     */
    public void lock(String key, int lockTimeout) {
        while (true) {

            // 获取当前时间
            int now = getRedisTime();

            // 当前时间 + 锁定超时时间 + 1秒
            int timestamp = now + lockTimeout + 1;

            // 尝试获取锁
            boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(key, String.valueOf(timestamp));
            if (lock) {

                // 如果获取成功，则直接退出死循环
                break;
            } else {

                // 获取锁住的时间
                String lockTime = stringRedisTemplate.opsForValue().get(key);
                if (lockTime != null) {

                    // 检查锁是否超时了，如果没有超时，则继续等待并重试
                    if (now > Integer.parseInt(lockTime)) {

                        // 通过GETSET，拿到的时间戳如果仍然是超时的，那就说明，如愿以偿拿到锁了。
                        // 如果在之前，其他的客户端比当前快一步执行了GETSET操作，那么当前拿到的时间戳是个未超时的值
                        // 这时，没有如期获得锁，需要再次等待或重试。
                        // 留意一下，尽管没拿到锁，但它改写了其他客户端设置的锁的超时值，不过这一点非常微小的误差带来的影响可以忽略不计。
                        lockTime = stringRedisTemplate.opsForValue().getAndSet(key, String.valueOf(timestamp));
                        if (lockTime == null) {
                            break;
                        }

                        if (now > Integer.parseInt(lockTime)) {
                            break;
                        }
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取锁（非阻塞方法）
     *
     * @return true=获取锁成功，false=获取锁失败
     */
    public boolean lockNonBlock(String key) {
        return this.lockNonBlock(key, LOCK_TIMEOUT);
    }

    /**
     * 获取锁（非阻塞方法）
     *
     * @param key         key名称
     * @param lockTimeout 超时时间（秒）
     */
    public boolean lockNonBlock(String key, int lockTimeout) {

        // 获取当前时间
        int now = getRedisTime();

        // 当前时间 + 锁定超时时间 + 1秒
        int timestamp = now + lockTimeout + 1;

        // 尝试获取锁
        boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(key, String.valueOf(timestamp));
        if (lock) {
            return true;
        } else {

            // 获取锁住的时间
            String lockTime = stringRedisTemplate.opsForValue().get(key);
            if (lockTime != null) {

                // 检查锁是否超时了，如果没有超时，说明锁被占用
                if (now > Integer.parseInt(lockTime)) {

                    // 通过GETSET，拿到的时间戳如果仍然是超时的，那就说明，如愿以偿拿到锁了。
                    // 如果在之前，其他的客户端比当前快一步执行了GETSET操作，那么当前拿到的时间戳是个未超时的值
                    // 这时，没有如期获得锁，需要再次等待或重试。
                    // 留意一下，尽管没拿到锁，但它改写了其他客户端设置的锁的超时值，不过这一点非常微小的误差带来的影响可以忽略不计。
                    lockTime = stringRedisTemplate.opsForValue().getAndSet(key, String.valueOf(timestamp));
                    if (lockTime == null) {
                        return true;
                    }

                    return now > Integer.parseInt(lockTime);
                }
            }
        }

        return false;
    }

    /**
     * 释放锁
     */
    public void unlock(String key) {

        // 获取当前时间
        int now = getRedisTime();

        // 获取锁住的时间
        String lockTime = stringRedisTemplate.opsForValue().get(key);
        if (lockTime != null) {
            // 在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能
            // 客户端因为某个耗时的操作而挂起，操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。
            if (now < Integer.parseInt(lockTime)) {
                stringRedisTemplate.delete(key);
            }
        }
    }
}
