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
        val sb = StringBuffer()

        // 正常数据解析数据表格
        val div = doc.getElementById("ctl16_contentdiv")
        val table = div.getElementsByTag("table")

        // 检查条件是否匹配
        if (table != null) {
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
            if (sb.isNotEmpty()) {
                FileUtils.writeHistory(code, sb.toString())
                FileUtils.writeLog(code)
            }
        }
    }
}