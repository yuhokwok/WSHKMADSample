package org.wshk.wshksample;

public class WSHKAPI {
    static String url = "https://reqres.in/api/";

    static String loginString() {
        return url + "login";
    }
    static String registerString() {
        return url + "register";
    }
    static String listUsers() { return url + "users"; };
    static String listUser(int id) { return url + "users/" + id; }
}
