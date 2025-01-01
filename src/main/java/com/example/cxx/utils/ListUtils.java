package com.example.cxx.utils;

import com.example.cxx.pojo.Menu;

//import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {
    public static List<Menu> listToTree(List<Menu> flatList, Integer parentId) {
        List<Menu> list = flatList.stream()
                        .filter(item -> item.getParentId().equals(parentId))         // 过滤父节点
                        .map(child -> {
                            child.setChildren(listToTree(flatList, child.getId()));
                            return child;
                        }).collect(Collectors.toList());
        return list;
    }
}
