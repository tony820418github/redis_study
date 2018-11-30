package com.zfc.sh.char3;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.TimeUnit;

public class SubThread extends  Thread {
    private final JedisPool jedisPool;
    private final Subscriber subscriber = new Subscriber();

    private final String channel = "mychannel";

    public SubThread(JedisPool jedisPool) {
        super("SubThread");
        this.jedisPool = jedisPool;
    }

    @Override
    public void run() {
        System.out.println(String.format("subscribe redis, channel %s, thread will be blocked", channel));
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.auth("pp511622");
            jedis.subscribe(subscriber, channel);
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JedisPool jpool = new JedisPool(new JedisPoolConfig(), "localhost" , 6379);
        SubThread subt = new SubThread(jpool);
        subt.start();

        TimeUnit.SECONDS.sleep(1);

        Jedis j = jpool.getResource();
        j.auth("pp511622");
        j.publish("mychannel","myfirst Message!");
        j.close();

    }
}
