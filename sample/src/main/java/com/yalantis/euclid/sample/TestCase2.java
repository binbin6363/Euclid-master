package com.yalantis.euclid.sample;

/**
 * Created by bbwang on 2016/8/20.
 */
public class TestCase2 {
    public static void main(String[] args) {

        System.out.println("show main:" + test());

    }

    private static String test() {
        String msg = "1";

        try {
            return msg;
        } finally {
            System.out.println("finally ...");
            msg = "0";
        }
    }
}
