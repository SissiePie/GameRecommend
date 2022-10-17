package com.laioffer.jupiter.util;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;

//加密设置
public class Util {
    // Help encrypt the user password before save to the database
    public static String encryptPassword(String userId, String password) throws IOException {
        return DigestUtils.md5Hex(userId + DigestUtils.md5Hex(password)).toLowerCase();
    }

}
/*
   frontend send the password then we encrypt the password, then store to the db.

 */