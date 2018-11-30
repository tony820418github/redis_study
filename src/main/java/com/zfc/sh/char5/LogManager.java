package com.zfc.sh.char5;

import com.zfc.sh.Util;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

public class LogManager {

    void logRescive(Jedis j , String name, String level , String message ){
        String destination = "recent:"+name+":"+level;
        message = System.currentTimeMillis()+":" + message;
        Pipeline pipe = j.pipelined();
        pipe.lpush(destination,message);
        pipe.ltrim(destination,0 ,99);
        pipe.sync();
    }

    void logCommon(Jedis j , String name, String level , String message){
        String destination = "common:"+name+":"+level;
        String startKey = destination + ":start";

    }

    public static void main(String[] args) {
        Jedis jedis = Util.getJedis();
        jedis.flushDB();
        LogManager lm = new LogManager();
        lm.logRescive(jedis,"zfc" , "INFO" , "start main()");

        System.out.println(jedis.lrange("recent:zfc:INFO" , 0 , -1));
        jedis.close();
    }

}
