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
import org.profit.app.realm.StockHall;
import org.profit.config.StockProperties;
import org.profit.persist.domain.Stock;
import org.profit.persist.domain.stock.Found;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 股票资金下载器
 *
 * @author TangYing
 */
public class SimpleFoundDownload {

    private Stock stock;
    private List<Found> foundList;

    public SimpleFoundDownload(Stock stock) {
        this.stock = stock;
        this.foundList = new ArrayList<Found>();
    }

    /**
     * 解析资金数据网址，下载股票资金数据
     */
    public List<Found> execute() {
        // 解析URL连接
        String url = StockProperties.baseUrlStockFound.replace("${code}", stock.getCode());
        try {
            // Clear
            this.foundList.clear();

            // 下载逻辑
            download(url);
        } catch (Exception e) {
//            LOG.error("Exception in download stock found. try end...{} {}", stock.getCode(), e.getMessage());
        }

        return foundList;
    }

    /**
     * 下载股票资金数据
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

                            String name = doc.title().split("\\(")[0];
                            if (!name.equals(stock.getName())) {
                                stock.setName(name);
                                StockHall.update(stock);
                            }

                            // 解析表格资金数据
                            Element table = doc.select(StockProperties.foundTableCssQuery).first();
                            // 检查条件是否匹配
                            if (table != null) {
                                // 以tr每行作为一个单独对象判断
                                for (Element tr : table.select("tr")) {
                                    Elements tds = tr.select("td");
                                    // 2014-04-09 2079.7244万元 -26.7519万元 805.2753万元 1184.7064万元 116.4949万元 10285万元 0.752%
                                    if (tds.size() >= 8) {
                                        String sdate = tds.get(0).text();
                                        String stotal = tds.get(1).text().replace(StockProperties.defaultFoundUnit, "");
                                        String smin = tds.get(2).text().replace(StockProperties.defaultFoundUnit, "");
                                        String smid = tds.get(3).text().replace(StockProperties.defaultFoundUnit, "");
                                        String sbig = tds.get(4).text().replace(StockProperties.defaultFoundUnit, "");
                                        String smax = tds.get(5).text().replace(StockProperties.defaultFoundUnit, "");
                                        String sturnover = tds.get(6).text().replace(StockProperties.defaultFoundUnit, "");

                                        long date = 0L;
                                        double total = 0.0, min = 0.0, mid = 0.0, big = 0.0, max = 0.0;
                                        int turnover = 0;
                                        try {
//                                            date = DateUtils.getZeroTime(DateUtils.parseTime(sdate));
                                            total = Double.parseDouble(stotal);
                                            min = Double.parseDouble(smin);
                                            mid = Double.parseDouble(smid);
                                            big = Double.parseDouble(sbig);
                                            max = Double.parseDouble(smax);
                                            turnover = Integer.parseInt(sturnover);

                                            Found found = new Found();
                                            found.setCode(stock.getCode());
                                            found.setDay(sdate);
                                            found.setTotal(total);
                                            found.setMin(min);
                                            found.setMid(mid);
                                            found.setBig(big);
                                            found.setMax(max);
                                            found.setTurnover(turnover);

                                            if (found.getTurnover() > 0) {
                                                foundList.add(found);
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
