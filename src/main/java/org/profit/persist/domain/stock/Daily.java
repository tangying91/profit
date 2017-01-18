package org.profit.persist.domain.stock;

public class Daily {

    private int id;
    private String code;         // 股票代码
    private String day;          // 日期
    private double open;        // 当日开盘价
    private double close;       // 当日收盘价
    private double high;        // 当日最高价
    private double low;         // 当日最低价
    private double adj;         // 收盘除权价
    private long volume;        // 成交量（单位：万手）

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

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getAdj() {
        return adj;
    }

    public void setAdj(double adj) {
        this.adj = adj;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object obj) {
        Daily d = (Daily) obj;
        if (d == null) {
            return false;
        }
        return d.getDay().equals(day) && d.getCode().equals(code);
    }
}
