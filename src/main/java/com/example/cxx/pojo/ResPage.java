package com.example.cxx.pojo;

import java.util.List;

public class ResPage <T> {
    private long total;
    private List<T> rows;

    public ResPage() {
    }

    public ResPage(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    /**
     * 获取
     * @return total
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 获取
     * @return items
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * 设置
     * @param items
     */
    public void setItems(List<T> items) {
        this.rows = items;
    }

    public String toString() {
        return "ResPage{total = " + total + ", items = " + rows + "}";
    }
}
