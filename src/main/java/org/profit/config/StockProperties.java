package org.profit.config;

import org.profit.util.PathUtils;
import org.profit.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * @author TangYing
 */
@Component
public class StockProperties {

    private static final Logger LOG = LoggerFactory.getLogger(StockProperties.class);

    // 默认资金单位
    public static String defalutFoundUnit = "万元";
    // 股票资金数据下载页面字符编码
    public static String foundHtmlCharset = "gb2312";
    // 股票资金数据下载表格查找条件
    public static String foundTableCssQuery = "table[class=lable_tab01]";
    // 股票资金数据下载基础链接地址
    public static String baseUrlStockFound = "http://www.gpcxw.com/funds/${code}.html";

    // 股票基础数据下载基础链接地址1
    public static String baseUrlStockData1 = "http://ichart.yahoo.com/table.csv?s=${s}&a=${a}&b=${b}&c=${c}&d=${d}&e=${e}&f=${f}&g=d&ignore=.csv";
    // 股票基础数据下载链接2
    public static String baseUrlStockData2 = "http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/#code.phtml";

    private static final String PROFIT_PROPS = "profit.properties";
    private static Properties props = new Properties();

    public static boolean download = true;

    static {
        String filePath = PathUtils.getConfPath() + File.separator + PROFIT_PROPS;

        try {
            InputStreamReader in = new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8"));
            props.load(in);

            download = Boolean.parseBoolean(get("app.download"));

            LOG.info("Server properties load completed.");
        } catch (IOException e) {
            LOG.error("Server properties load failed.", e);
        }
    }

    public static String get(String key) {
        return StringUtils.replaceBlank(props.getProperty(key));
    }
}
