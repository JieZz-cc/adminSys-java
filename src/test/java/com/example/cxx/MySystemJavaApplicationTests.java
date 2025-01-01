package com.example.cxx;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class MySystemJavaApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void calce() {
        Integer[] arr1 = {1,2,3,4,5};   // old
        Integer[] arr2 = {1,2,3,6};     // new
        List<Integer> oldList = Arrays.asList(arr1);
        List<Integer> newList = Arrays.asList(arr2);
        List<Integer> delRes = new ArrayList<>();
        List<Integer> addRes = new ArrayList<>();
        oldList.forEach(i -> {
            newList.forEach(e -> {
                if (!newList.contains(i)) delRes.add(i);
                if (!oldList.contains(e)) addRes.add(e);
            });
        });
        Integer[] a = addRes.stream().distinct().toArray(Integer[]::new);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
        addRes.toArray();
        System.out.println();
        delRes.stream().distinct().collect(Collectors.toList());

    }

}
