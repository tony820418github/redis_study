package com.zfc.sh.char3;

import com.zfc.sh.Util;
import redis.clients.jedis.Jedis;

public class JedisSort {
    public static void main(String[] args) {
        Jedis j = Util.getJedis();
        j.del("sort-input");
        j.rpush("sort-input" , "23" , "15" , "7" , "110");
        System.out.println(j.sort("sort-input"));

        j.hset("d-7" , "field" , "5");
        j.hset("d-15" , "field" , "1");
        j.hset("d-23" , "field" , "9");
        j.hset("d-110" , "field" , "3");

        System.out.println(j.sort("sort-input" ,"d-*->field"));


    }
}
