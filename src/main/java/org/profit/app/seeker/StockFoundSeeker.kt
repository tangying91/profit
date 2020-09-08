package org.profit.app.seeker

import org.jsoup.nodes.Document
import org.profit.config.StockProperties

/**
 * 资金数据爬取
 */
class StockFoundSeeker(code: String) : StockSeeker(code, StockProperties.foundUrl) {

    /**
     *  解析数据
     */
    override fun handle(doc: Document) {

    }
}