package com.zfc.sh.char1;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ArticleVote {

    private static int ONE_WEEK_IN_SECOND = 7 * 86400;
    private static int VOTE_SCORE = 432;
    private static int NUM_PERPAGE = 2;
    private static String ARTICLE = "article:";
    private static String USER = "user:";
    private static String VOTE = "voted:";

    public Jedis getJedis() {
        return jedis;
    }

    Jedis jedis;


    private void connect() {
        jedis = new Jedis("localhost");
        jedis.del("article:");
    }

    public void close() {
        jedis.close();
    }


    public void vote(String userID, String articleID) {
        int cutoff = (int) (System.currentTimeMillis() / 1000 - ONE_WEEK_IN_SECOND);
        Double articleTime = jedis.zscore("time:", ARTICLE + articleID);
        if (articleTime == null || articleTime < cutoff) {
            return;
        }
        if (jedis.sadd("voted:" + articleID, USER + userID) == 1) {
            jedis.zincrby("score:", VOTE_SCORE, ARTICLE + articleID);
            jedis.hincrBy(ARTICLE + articleID, "votes", 1);
        }
    }

    public void postArticle(final String user, final String title, final String link) {
        Long articleID = jedis.incr("article:");
        String voted = VOTE + articleID;
        jedis.sadd(voted, USER + user);
        jedis.expire(voted, ONE_WEEK_IN_SECOND);
        String article = ARTICLE + articleID;
        final String now = String.valueOf(System.currentTimeMillis()/1000);
        jedis.hmset(article, new HashMap<String, String>() {
            {
                put("title",title);
                put("link" , link);
                put("poster" , user);
                put("time" ,now );
                put("votes"  , String.valueOf(1));
            }
        });

        jedis.zadd("time:", Double.parseDouble(now), article);
        jedis.zadd("score:" ,Double.parseDouble(now+VOTE_SCORE) , article);
    }

    public void getArticle(String order){
        Set<String> zrevrange = jedis.zrevrange(order, 0, 2);
        zrevrange.stream().forEach((article)->{
            System.out.println(jedis.hgetAll(article));
        });
    }

    public static void main(String[] args) throws InterruptedException {
        ArticleVote av = new ArticleVote();
        av.connect();
        av.postArticle("1", "第一", "http://first");
        TimeUnit.SECONDS.sleep(1);
        av.postArticle("1", "第二", "http://second");
        TimeUnit.SECONDS.sleep(1);
        av.postArticle("1", "第三", "http://third");
        av.vote("2","1");
        av.vote("2","3");
        av.vote("3","3");
        System.out.println("按score排序");
        av.getArticle("score:");
        System.out.println("按time排序");
        av.getArticle("time:");
        av.close();
    }

}
