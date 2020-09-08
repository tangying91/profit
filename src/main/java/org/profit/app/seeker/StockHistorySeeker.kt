package org.profit.app.seeker

import org.jsoup.nodes.Document
import org.profit.config.StockProperties
import org.profit.util.FileUtils

/**
 * 历史数据爬取
 */
class StockHistorySeeker(code: String) : StockSeeker(code, StockProperties.historyUrl) {

    /**
     *  解析数据
     */
    override fun handle(doc: Document) {
        // 解析数据表格
        val div = doc.getElementById("ctl16_contentdiv")
        val table = div.getElementsByTag("table")

        // 检查条件是否匹配
        if (table != null) {
            val sb = StringBuffer()
            // 以tr每行作为一个单独对象判断
            for (tr in table.select("tr")) {
                val tds = tr.select("td")
                val td = tds.removeClass("altertd").text()

                // 過濾垃圾數據
                if (td.contains("日期") || td.contains("End")) {
                    continue
                }

                // 保存數據
                if (sb.isNotEmpty()) {
                    sb.append("\r\n").append(td)
                } else {
                    sb.append(td)
                }
            }

            // 寫入文件
            // 日期 开盘 最高 最低 收盘 成交量 成交金额 升跌$ 升跌% 缩 高低差% SZ深证 SZ%
            if (sb.isNotEmpty()) {
                FileUtils.writeHistory(code, sb.toString())
            }
        }
    }
}