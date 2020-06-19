package com.insit.mark.blog.web.config;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {

    private static String algorithmName = "md5";
    private static int hashIterations = 2;

    public static String encryptPassword(String useName,String passWord) {
        String newPassword = new SimpleHash(algorithmName, passWord,  ByteSource.Util.bytes(useName), hashIterations).toHex();
        return newPassword;
    }
}
