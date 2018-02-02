package org.profit.config;

import org.springframework.stereotype.Component;

/**
 * @author TangYing
 */
@Component
public class StockProperties {

    /**
     * 默认资金单位
     */
    public static String defaultFoundUnit = "万元";

    /**
     * 股票资金数据下载页面字符编码
     */
    public static String foundHtmlCharset = "gb2312";

    /**
     * 股票资金数据下载表格查找条件
     */
    public static String foundTableCssQuery = "table[class=lable_tab01]";

    /**
     * 股票资金数据下载基础链接地址
     */
    public static String baseUrlStockFound = "http://www.gpcxw.com/funds/${code}.html";

    /**
     * 股票基础数据下载基础链接地址1
     */
    public static String baseUrlStockData1 = "http://ichart.yahoo.com/table.csv?s=${s}&a=${a}&b=${b}&c=${c}&d=${d}&e=${e}&f=${f}&g=d&ignore=.csv";

    /**
     * 股票基础数据下载链接2
     */
    public static String baseUrlStockData2 = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/#code.phtml";
}
