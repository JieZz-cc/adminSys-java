package com.example.cxx.listen;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.cxx.pojo.UserEntity;
import com.example.cxx.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<UserEntity> {
    private List<UserEntity> list = new ArrayList<>();
    private static final int BATCH_COUNT = 10;

    private UserService userService;

    public ExcelListener(UserService sysUserService) {
        this.userService = sysUserService;
    }

    @Override
    public void invoke(UserEntity userEntity, AnalysisContext analysisContext) {
        list.add(userEntity);
        if(list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();     //确保所有数据都能入库
    }

    private void saveData() {
        userService.importUsers(list);
    }
}
