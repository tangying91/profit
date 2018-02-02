package org.profit.app.logic.magic;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.profit.config.StockProperties;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SimpleDailyDownloadFromSina {

    private final Logger LOG = LoggerFactory.getLogger(SimpleDailyDownloadFromSina.class);

    private Stock stock;
    private final List<Daily> result;

    public SimpleDailyDownloadFromSina(Stock stock) {
        this.stock = stock;
        this.result = new ArrayList<Daily>();
    }

    /**
     * 参数说明
     * s – 股票名称
     * a – 起始时间，月
     * b – 起始时间，日
     * c – 起始时间，年
     * d – 结束时间，月
     * e – 结束时间，日
     * f – 结束时间，年
     * g – 时间周期。Example: g=w, 表示周期是’周’。d->’日’(day), w->’周’(week)，m->’月’(mouth)，v->’dividends only’
     * 一定注意月份参数，其值比真实数据-1。如需要9月数据，则写为08。
     */
    public List<Daily> execute() {
        String code = stock.getCode();

        // 解析URL连接
        String url = StockProperties.baseUrlStockData2.replace("#code", code);

        try {
            // Clear
            this.result.clear();

            // 下载逻辑
            download(url);
        } catch (Exception e1) {
            LOG.error("Exception in download stock daily. try end...{} {}", code, e1.getMessage());
        }

        return result;
    }

    /**
     * 下载方法
     *
     * @param url 获取数据地址
     * @return
     */
    private void download(String url) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            // Request configuration can be overridden at the request level.
            // They will take precedence over the one set at the client level.
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(3000)
                    .setConnectTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);

            CloseableHttpResponse response = httpClient.execute(httpGet);

            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        InputStream in = entity.getContent();
                        try {
                            Document doc = Jsoup.parse(in, StockProperties.foundHtmlCharset, url);

                            // 解析表格资金数据
                            Element table = doc.getElementById("FundHoldSharesTable");
                            // 检查条件是否匹配
                            if (table != null) {
                                // 以tr每行作为一个单独对象判断
                                for (Element tr : table.select("tr")) {
                                    Elements tds = tr.select("td");

                                    // 日期 开盘价 最高价 收盘价 最低价 交易量(股) 交易金额(元)
                                    if (tds.size() >= 7) {
                                        if (tds.get(0).text().equals("日期")) {
                                            continue;
                                        }

                                        try {
                                            Daily sd = new Daily();

                                            sd.setCode(stock.getCode());
                                            sd.setDay(tds.get(0).text());
                                            sd.setOpen(Float.valueOf(tds.get(1).text()));
                                            sd.setHigh(Float.valueOf(tds.get(2).text()));
                                            sd.setClose(Float.valueOf(tds.get(3).text()));
                                            sd.setLow(Float.valueOf(tds.get(4).text()));
                                            sd.setAdj(Float.valueOf(tds.get(3).text()));
                                            sd.setVolume(Long.valueOf(tds.get(5).text()));

                                            if (sd.getVolume() > 0) {
                                                result.add(sd);
                                            }
                                        } finally {

                                        }
                                    }
                                }
                            } else {
//                                LOG.info("Table not detected, please check the conditions!");
                            }
                        } catch (IOException e) {
                            throw e;
                        } finally {
                            in.close();
                        }
                    }
//                    LOG.debug("URL: {} download successful.", url);
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
