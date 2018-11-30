package com.zfc.sh.char6;

import com.zfc.sh.Util;
import jdk.internal.org.objectweb.asm.util.TraceAnnotationVisitor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.concurrent.TimeUnit;

public class MyLock {
    private final String LOCK = "lock:";

    public boolean aacquireLock(String lockName, int timeout) throws InterruptedException {
        long end = System.currentTimeMillis() + timeout;
        String lock = LOCK + lockName;


        Jedis j = Util.getJedis();
        while (System.currentTimeMillis() < end) {
            if (1 == j.setnx(lock, "mylockzfc")) {
                return true;
            }
            TimeUnit.MILLISECONDS.sleep(50);
        }
        return false;
    }

    public boolean releaseLock(String lockName, String identify) {
        Jedis j = Util.getJedis();
        String lock = LOCK + lockName;

        while (true) {
            j.watch(lock);
            if (j.get(lock) == identify) {
                Transaction t = j.multi();
                t.del(lock);
                t.exec();
                return true;
            }else {
                j.unwatch();
                break;
            }
        }
        return false;
    }

}
