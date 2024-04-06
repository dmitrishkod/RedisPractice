package com.skillbox.redisdemo;

import java.util.Random;

import static java.lang.System.out;

public class RedisTest {

    // Запуск докер-контейнера:
    // docker run --rm --name skill-redis -p 127.0.0.1:6379:6379/tcp -d redis

    // Предположим что на сайте 20 зарегистрированных пользователей
    private static final int USERS = 20;

    // Задержка между регистрациями в секунду или между показами
    private static final int SLEEP = 1000; // 1 секунда


    private static void log(String user) {
        String log = String.format("— На главной странице показываем пользователя: " + user);
        out.println(log);
    }

    public static void main(String[] args) throws InterruptedException {

        RedisStorage redis = new RedisStorage();
        redis.init();
        for (int i = 1; i <= USERS; i++) {
            redis.reg_user(i);

        }

        while (true){
            // Эмулируем показы пользователей на сайте
            int i = 0;
            String vip = "";
            for (String user: redis.getAllUsers()){
                if (i % 10 == 0){
                    String userId = String.valueOf(new Random().nextInt(USERS));
                    out.println("> Пользователь " + userId + " оплатил платную услугу");
                    vip = userId;
                    log(userId);
                    Thread.sleep(SLEEP);
                }
                if (!user.equals(vip)){
                    log(user);
                }
                i++;
                Thread.sleep(SLEEP);
            }
        }
    }
}
