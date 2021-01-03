package com.imooc.admin;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class PWDTest {
    public static void main(String[] args) {
        System.out.println(BCrypt.hashpw("admin", BCrypt.gensalt()));
    }
}
