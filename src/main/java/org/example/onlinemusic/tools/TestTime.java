package org.example.onlinemusic.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {
    public static void main(String[] args) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        String time1 = sf1.format(new Date());
        System.out.println(time1);

    }
}
