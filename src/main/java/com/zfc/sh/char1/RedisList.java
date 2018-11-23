package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

public class RedisList {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.del("myList");
        jedis.rpush("myList" , "1");
        jedis.rpush("myList" , "2");
        jedis.lpush("myList" , "3");

        System.out.println(jedis.lrange("myList",0,-1));

        jedis.lpop("myList");

        System.out.println(jedis.lrange("myList",0,-1));

        jedis.close();

    }
}
