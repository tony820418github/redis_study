package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

public class ZSetRedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.auth("pp511622");
        jedis.del("myZSet");
        jedis.zadd("myZSet" , 1,"1key");
        jedis.zadd("myZSet" , 3,"3key");
        jedis.zadd("myZSet" , 2,"2key");
        jedis.zadd("myZSet" , 1,"1key");


        System.out.println(jedis.zrange("myZSet" , 0 ,-1));
        System.out.println(jedis.zrangeByScore("myZSet" , 0 ,2));

        System.out.println(jedis.zscore("myZSet" , "1key"));
        jedis.close();
    }
}
