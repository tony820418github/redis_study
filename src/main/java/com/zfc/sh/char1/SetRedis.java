package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

public class SetRedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.del("mySet");
        jedis.sadd("mySet" , "1");
        jedis.sadd("mySet" , "2");
        jedis.sadd("mySet" , "1");

        System.out.println(jedis.smembers("mySet"));
        System.out.println(jedis.sismember("mySet","1"));
        jedis.srem("mySet" , "1");
        System.out.println(jedis.sismember("mySet","1"));
        jedis.close();
    }
}
