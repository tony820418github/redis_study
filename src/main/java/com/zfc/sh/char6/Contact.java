package com.zfc.sh.char6;

import com.zfc.sh.Util;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

public class Contact {
    public static final String RECENT = "recent:";
    public static final String EMAIL = "email";

    public boolean addUpdateContact(String user, String contact) {
        String acList = RECENT + user;
        Jedis j = Util.getJedis();
        Transaction t = j.multi();
        t.lrem(acList, 1, contact);
        t.lpush(acList, contact);
        t.ltrim(acList, 0, 99);
        t.exec();
        j.close();
        return true;
    }

    public boolean removeContact(String user, String contact) {
        String acList = RECENT + user;
        Jedis j = Util.getJedis();
        j.lrem(acList, 1, contact);
        j.close();
        return true;
    }

    public boolean findPrefixRange(String prefix) {
        String constAsc = "`abcdefghijklmnopqrstuvwxyz{";
        String prefixPost = prefix + "{" + System.currentTimeMillis();
        char suff = constAsc.charAt(constAsc.indexOf(prefix.charAt(prefix.length() - 1)) - 1);
        String prefixPre = prefix.substring(0, prefix.length() - 1) + suff + "{" + System.currentTimeMillis();

        Jedis j = Util.getJedis();

        j.watch(EMAIL);
        boolean flag = true;
        while (flag) {
            Transaction t = j.multi();
            t.zadd(EMAIL, 0, prefixPre);
            t.zadd(EMAIL, 0, prefixPost);
            Set<String> emails = t.zrangeByLex(EMAIL, "(" + prefixPre, "(" + prefixPost).get();
            t.zrem(EMAIL, prefixPre);
            t.zrem(EMAIL, prefixPost);
            List<Object> result = t.exec();
            if (result == null) {
                continue;
            }
            flag = false;
        }

        return true;
    }
}
