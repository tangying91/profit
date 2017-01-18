package org.profit.app.logic.magic;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.profit.config.StockProperties;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Daily;
import org.profit.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SimpleDailyDownloadFromYahoo {

    private final Logger LOG = LoggerFactory.getLogger(SimpleDailyDownloadFromYahoo.class);

    protected final String suffix_ss = ".ss";
    protected final String suffix_sz = ".sz";

    private Stock stock;
    private final String fromDate;
    private final String toDate;
    private final List<Daily> result;

    public SimpleDailyDownloadFromYahoo(Stock stock) {
        this.stock = stock;
        this.result = new ArrayList<Daily>();

        long date = System.currentTimeMillis() - DateUtils.MILLISECOND_PER_DAY * 60;
        this.fromDate = DateUtils.formatTime(date, "yyyy-MM-dd");
        this.toDate = DateUtils.formatTime(System.currentTimeMillis(), "yyyy-MM-dd");
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
        String s = code.startsWith("6") ? code + suffix_ss : code + suffix_sz;
        String[] fromArray = fromDate.split("-");
        String[] toArray = toDate.split("-");
        String a = (Integer.valueOf(fromArray[1]) - 1) + "";// a – 起始时间，月
        String b = fromArray[2];// b – 起始时间，日
        String c = fromArray[0];// c – 起始时间，年
        String d = (Integer.valueOf(toArray[1]) - 1) + "";// d – 结束时间，月
        String e = toArray[2];// e – 结束时间，日
        String f = toArray[0];// f – 结束时间，年

        // 解析URL连接
        String url = StockProperties.baseUrlStockData1
                .replace("${s}", s)
                .replace("${a}", a)
                .replace("${b}", b)
                .replace("${c}", c)
                .replace("${d}", d)
                .replace("${e}", e)
                .replace("${f}", f);

        try {
            // Clear
            this.result.clear();

            // 下载逻辑
            download(url);
        } catch (Exception e1) {
//            LOG.error("Exception in download stock daily. try end...{} {}", code, e1.getMessage());
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
                            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
                            BufferedReader buff = new BufferedReader(inputStreamReader);
                            // 第一行空数据多余的
                            String newLine = buff.readLine();
                            while ((newLine = buff.readLine()) != null) {
                                String stockInfo[] = newLine.trim().split(",");
                                if (stockInfo.length == 7) {
                                    Daily sd = new Daily();

                                    sd.setCode(stock.getCode());
                                    sd.setDay(stockInfo[0]);
                                    sd.setOpen(Float.valueOf(stockInfo[1]));
                                    sd.setHigh(Float.valueOf(stockInfo[2]));
                                    sd.setLow(Float.valueOf(stockInfo[3]));
                                    sd.setClose(Float.valueOf(stockInfo[4]));
                                    sd.setAdj(Float.valueOf(stockInfo[6]));
                                    sd.setVolume(Long.valueOf(stockInfo[5]));

                                    if (sd.getVolume() > 0) {
                                        result.add(sd);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            throw e;
                        } finally {
                            in.close();
                        }
                    }
                }
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
