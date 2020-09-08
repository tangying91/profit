package org.profit.persist.domain

class StockFound {
    var code: String = ""      // 股票代码
    var day: String = ""       // 日期
    var total: Double = 0.0     // 总资金 = 0.0
    var min: Double = 0.0       // 散户 = 0.0
    var mid: Double = 0.0       // 中户 = 0.0
    var big: Double = 0.0       // 大户 = 0.0
    var max: Double = 0.0       // 超大 = 0.0
    var turnover: Int = 0       // 成交额 = 0
}