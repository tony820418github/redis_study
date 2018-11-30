package com.zfc.sh;

import redis.clients.jedis.Jedis;

public class Util {
    public static Jedis getJedis(){
        Jedis jedis =  new Jedis("localhost");
        jedis.auth("pp511622");
        return  jedis;
    }

    static void close(Jedis jedis){
        jedis.close();
    }
}
