package org.profit.persist.domain;

public class Stock {

    private String code;                // 股票代码
    private String name;                // 股票名称
    private Long lastAnalyticTime;      // 上一次分析时间

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getLastAnalyticTime() {
        return lastAnalyticTime;
    }

    public void setLastAnalyticTime(Long lastAnalyticTime) {
        this.lastAnalyticTime = lastAnalyticTime;
    }
}
