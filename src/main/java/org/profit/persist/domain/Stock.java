package org.profit.persist.domain;

public class Stock {

    private String code;                    // 股票代码
    private String name;                    // 股票名称
    private Boolean downloadedBase;         // 基础数据是否下载
    private Boolean downloadedFund;         // 资金数据是否下载
    private Boolean analyticed;             // 是否已经分析

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

    public Boolean getDownloadedBase() {
        return downloadedBase;
    }

    public void setDownloadedBase(Boolean downloadedBase) {
        this.downloadedBase = downloadedBase;
    }

    public Boolean getDownloadedFund() {
        return downloadedFund;
    }

    public void setDownloadedFund(Boolean downloadedFund) {
        this.downloadedFund = downloadedFund;
    }

    public Boolean getAnalyticed() {
        return analyticed;
    }

    public void setAnalyticed(Boolean analyticed) {
        this.analyticed = analyticed;
    }
}
