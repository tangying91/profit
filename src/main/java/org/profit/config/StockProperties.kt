package org.profit.config

/**
 * @author TangYing
 */
object StockProperties {

    /**
     * 股票资金数据下载基础链接地址
     */
    const val foundUrl = "http://www.gpcxw.com/funds/\${code}.html"

    /**
     * 股票基础数据下载链接
     */
    const val historyUrl = "http://www.aigaogao.com/tools/history.html?s=#code"
}