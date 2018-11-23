package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

public class StringRedis {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
        jedis.del("runoobkey");
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
        jedis.set("myage",  String.valueOf(35));
        System.out.println(jedis.incr("myage"));

    }
}
