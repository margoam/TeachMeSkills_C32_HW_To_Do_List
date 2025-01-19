package com.teachmeskills.hw.lesson_26.util;

import com.teachmeskills.hw.lesson_26.model.User;
import com.teachmeskills.hw.lesson_26.storage.UserStorage;

public class UserValidator {

    public static boolean validateUserName(String username, String password) {

        User user = UserStorage.userDatabase.get(username);

        return user != null && user.getPassword().equals(password);
    }
}
