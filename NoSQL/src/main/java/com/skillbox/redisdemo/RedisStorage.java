package com.skillbox.redisdemo;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.client.RedisConnectionException;
import org.redisson.config.Config;

import java.util.Date;
import java.util.LinkedList;

import static java.lang.System.out;

public class RedisStorage {


    // Объект для работы с Redis
    private RedissonClient redisson;

    // Объект для работы с ключами
    private RKeys rKeys;

    // Объект для работы с Sorted Set'ом
    private RScoredSortedSet<String> registeredUsers;

    private final static String KEY = "DATING_SITE_USERS";

    private double getTs() {
        return new Date().getTime() / 1000;
    }

    void init() {
        Config config = new Config();
        config.useSingleServer().setAddress("Вставьте ваш айпи");
        try {
            redisson = Redisson.create(config);
        } catch (RedisConnectionException Exc) {
            out.println("Не удалось подключиться к Redis");
            out.println(Exc.getMessage());
        }
        rKeys = redisson.getKeys();
        registeredUsers = redisson.getScoredSortedSet(KEY);
        rKeys.delete(KEY);
    }

    void shutdown() {
        redisson.shutdown();
    }

    // Фиксирует посещение пользователем страницы
    void reg_user(int user_id)
    {
        //ZADD DATING_SITE_USERS
        registeredUsers.add(getTs(), String.valueOf(user_id));
    }
    LinkedList<String> getAllUsers()
    {
        LinkedList<String> linkedList = new LinkedList<>();
        // Получение множества (Set) элементов из ключа "DATING_SITE_USERS"
        for (String user: registeredUsers){
                linkedList.add(user);
        }
        // Вывод списка элементов
        return linkedList;
    }
}
