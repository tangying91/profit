package org.profit.persist.domain.stock;

public class Found {

    private int id;
    private String code;        // 股票代码
    private String day;         // 日期
    private double total;      // 总资金
    private double min;        // 散户
    private double mid;        // 中户
    private double big;        // 大户
    private double max;        // 超大
    private int turnover;      // 成交额

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public double getBig() {
        return big;
    }

    public void setBig(double big) {
        this.big = big;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    @Override
    public boolean equals(Object obj) {
        Found d = (Found) obj;
        if (d == null) {
            return false;
        }
        return d.getDay().equals(day) && d.getCode().equals(code);
    }
}
