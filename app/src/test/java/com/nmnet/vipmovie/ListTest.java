package com.nmnet.vipmovie;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NMNET on 2017/3/9 0009.
 */

public class ListTest {

    @Test
    public void test() {
        List<String> strs = new ArrayList<>();
        strs.add("1");
        strs.add("1");
        strs.add("1");
        strs.add("2");
        strs.add("2");
        strs.add("2");
        strs.add("3");
        strs.add("3");
        strs.add("3");

        for (int i = 0; i < strs.size(); i++)  //外循环是循环的次数
        {
            for (int j = strs.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
            {
                if (strs.get(i).equals(strs.get(j))) {
                    strs.remove(j);
                }

            }
        }
        System.out.println(strs);
    }

}
