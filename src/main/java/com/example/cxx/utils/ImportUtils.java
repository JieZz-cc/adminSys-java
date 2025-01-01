package com.example.cxx.utils;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
//import org.apache.poi.ss.usermodel.*;

import javax.persistence.Column;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportUtils {
    public static <T> List<T> importEntities (MultipartFile file, Class<T> entityClass) throws IOException {
        // 创建Excel工作簿
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        // 获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        // 获取标题行的列名
        Row titleRow = sheet.getRow(0);
        List<String> columnNames = getColumnNames(titleRow);

        // 创建实体对象列表
        List<T> entities = new ArrayList<>();
        // 迭代每一行（跳过标题行）
        Iterator<Row> iterator = sheet.iterator();
        iterator.next(); // 跳过标题行
        while (iterator.hasNext()) {
            Row row = iterator.next();
            // 读取单元格数据并创建实体对象
            try {
                T entity = createEntityFromRow(row, columnNames, entityClass);
                entities.add(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        workbook.close();
        return entities;
    }

    // 获取列名
    private static List<String> getColumnNames(Row row) {
        List<String> columnNames = new ArrayList<>();
        for (Cell cell : row) {
            columnNames.add(getStringCellValue(cell));
        }
        return columnNames;
    }

    // 根据行数据创建实体对象
    private static <T> T createEntityFromRow(Row row, List<String> columnNames, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < columnNames.size(); i++) {
            String columnName = columnNames.get(i);
            String cellValue = getStringCellValue(row.getCell(i));

            for (Field field : fields) {
                field.setAccessible(true);
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation != null && columnAnnotation.name().equals(columnName)) {
                    setFieldValue(entity, field, cellValue);
                    break;
                }
            }
        }
        return entity;
    }

    // 获取单元格的字符串值
    private static String getStringCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        String cellValue;
        // 根据单元格类型进行值转换
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                // 判断是否为日期类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellValue = sdf.format(cell.getDateCellValue());
                } else {
                    // 将数字类型转换为字符串类型
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getStringCellValue();
                }
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                // 如果公式计算结果为字符串类型，则获取字符串值
                if (cell.getCachedFormulaResultType() == CellType.STRING) {
                    cellValue = cell.getRichStringCellValue().getString();
                }
                // 如果公式计算结果为数字类型，则获取数字值并转换为字符串
                else if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                    cellValue = String.valueOf(cell.getNumericCellValue());
                }
                // 如果公式计算结果为布尔类型，则获取布尔值并转换为字符串
                else if (cell.getCachedFormulaResultType() == CellType.BOOLEAN) {
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                } else {
                    cellValue = "";
                }
                break;
            default:
                cellValue = "";
                break;
        }
        return cellValue;
    }

    // 设置实体类属性的值
    private static <T> void setFieldValue(T entity, Field field, String cellValue) throws Exception {
        field.setAccessible(true);
        Class<?> fieldType = field.getType();

        // 根据属性的类型进行赋值
        if (fieldType == String.class) {
            field.set(entity, cellValue);
        } else if (fieldType == Integer.class) {
            field.set(entity, Integer.valueOf(cellValue));
        } else if (fieldType == Double.class) {
//            field.set(entity, Double.valueOf(cellValue));
            field.set(entity, cellValue);

        }
        // 在此处可以根据需要添加其他类型的赋值判断
        else {
            field.set(entity, null);
        }
    }
}
