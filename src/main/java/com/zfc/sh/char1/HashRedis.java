package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

public class HashRedis {
    public static void main(String[] args) {

        Jedis jedis = new Jedis("localhost");
        jedis.del("myHash");
        jedis.hset("myHash","1","first");
        jedis.hset("myHash","2","second");
        jedis.hset("myHash","3","third");
        System.out.println(jedis.hgetAll("myHash"));

        jedis.hdel("myHash" , "2");
        System.out.println(jedis.hget("myHash","2"));

        System.out.println(jedis.hgetAll("myHash"));


    }
}
