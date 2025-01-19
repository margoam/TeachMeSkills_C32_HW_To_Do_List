package com.teachmeskills.hw.lesson_26.util;

import com.teachmeskills.hw.lesson_26.logging.Logger;
import com.teachmeskills.hw.lesson_26.model.User;
import com.teachmeskills.hw.lesson_26.storage.UserStorage;
import jakarta.servlet.http.HttpServletResponse;

public class UserValidator {

    public static boolean validateUserName(String username, String password) {

        User user = UserStorage.userDatabase.get(username);

        return user != null && user.getPassword().equals(password);
    }
}
