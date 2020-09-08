package org.profit.persist.domain

class Stock {
    var code : String = ""                  // 股票代码
    var name : String = ""                  // 股票名称
    var downloadedBase : Boolean = false    // 基础数据是否下载
    var downloadedFund : Boolean = false    // 资金数据是否下载
    var analyticed : Boolean = false        // 是否已经分析
}