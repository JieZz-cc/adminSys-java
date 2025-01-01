package com.example.cxx.utils;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import javax.persistence.Table;
import javax.persistence.Column;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ExportUtils {
    public static <T> void exportData(HttpServletResponse response, List<T> dataList, Class<T> EntityClass) throws IOException {
        // 创建Excel工作簿
        Workbook sheets = WorkbookFactory.create(true);

        // 创建工作表
        Sheet sheet = sheets.createSheet(EntityClass.getSimpleName());

        // 设置标题行的字体样式
        Font font = sheets.createFont();
        font.setBold(true); // 将字体设置为加粗样式

        // 获取表名
        Table tableAnnotation = EntityClass.getAnnotation(Table.class);
        String tableName = "data";
        if (tableAnnotation != null) {
            tableName = tableAnnotation.name();
        }

        /**
         *    创建标题行
         * 1. 要根据实体类的 @Column(name = "姓名") 注解中的 name 值来设置标题行，
         * 2. 你可以通过反射获取实体类的字段名称。
         * 3. 可以使用 Class.getDeclaredFields() 方法来获取所有的字段，
         * 4. 然后使用 Field.getAnnotation() 方法获取 @Column 注解，最后通过 name() 方法获取注解中设置的名称。
         */

        Row headerRow = sheet.createRow(0);
        Field[] declaredFields = EntityClass.getDeclaredFields();

        // 数据不为空，写数据，为空，下载模板，模板无序号
        int columnIndex = 0;
        for (Field declaredField : declaredFields) {
            Column annotation = declaredField.getAnnotation(Column.class);
            if (annotation != null) {
                String name = annotation.name();
                // 排除id、createTime、updateTime、isDeleted等属性的导出
                if (Objects.isNull(dataList)) {
                    if (!("id".equals(declaredField.getName()) || "createTime".equals(declaredField.getName())
                            || "updateTime".equals(declaredField.getName()))) {
                        Cell headerCell = headerRow.createCell(columnIndex);
                        headerCell.setCellValue(name);

                        // 应用字体样式到标题单元格
                        CellStyle cellStyle = sheets.createCellStyle();
                        cellStyle.setFont(font);
                        headerCell.setCellStyle(cellStyle);

                        columnIndex++;
                    }
                } else {
                    Cell headerCell = headerRow.createCell(columnIndex);
                    headerCell.setCellValue(name);

                    // 应用字体样式到标题单元格
                    CellStyle cellStyle = sheets.createCellStyle();
                    cellStyle.setFont(font);
                    headerCell.setCellStyle(cellStyle);

                    columnIndex++;
                }
            }
        }

        /**
         * 1. 可以使用反射获取实体类的字段数量，
         * 2. 然后在循环中根据字段数来创建单元格
         */
        // 数据不为空-填充数据到Excel表格
        // 数据为空，下载模板
        if (Objects.nonNull(dataList)) {
            int rowNumber = 1;
            for (T data : dataList) {
                Row row = sheet.createRow(rowNumber);
                for (int i = 0; i < declaredFields.length; i++) {
                    Field field = declaredFields[i];
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation != null) {
                        Cell cell = row.createCell(i);
                        setCellValue(cell, data, field);
                    }
                }
                rowNumber++;
            }
        }

        // 设置响应头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        String encodedTableName = URLEncoder.encode(tableName, "UTF-8");
        String filename = encodedTableName + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        // 将Excel写入响应输出流
        sheets.write(response.getOutputStream());
        sheets.close();
    }

    // 调用方法 setCellValue() 在 setCellValue() 方法中根据字段的类型来设置单元格的值
    private static <T> void setCellValue(Cell cell, T data, Field field) {
        field.setAccessible(true);
        try {
            Object value = field.get(data);
            // 根据属性的类型来设置单元格的值
            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof String) {
                cell.setCellValue((String) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else if (value instanceof Date) {
                DateTimeFormatter sourceFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                DateTimeFormatter targetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(value.toString(), sourceFormatter);
                String convertedTime = dateTime.format(targetFormatter);
                cell.setCellValue(convertedTime);
            } else if (value instanceof Double) {
                cell.setCellValue(value.toString());
            } else {

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T> void exportByEasyExcel(HttpServletResponse response, List<T> dataList, Class<T> EntityClass, String sheetName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = sheetName;
        response.setHeader("Content-disposition", "attachment; filename=\"" + URLEncoder.encode(fileName) + ".xlsx" + "\";charset=utf-8");
        EasyExcel.write(response.getOutputStream(), EntityClass).sheet(sheetName).doWrite(dataList);
    }
}
