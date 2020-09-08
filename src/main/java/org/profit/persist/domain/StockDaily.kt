package org.profit.persist.domain

class StockDaily {
    var code: String = ""           // 股票代码
    var day : String = ""           // 日期
    var open: Double = 0.0          // 当日开盘价 = 0.0
    var close: Double = 0.0         // 当日收盘价 = 0.0
    var high: Double = 0.0          // 当日最高价 = 0.0
    var low: Double = 0.0           // 当日最低价 = 0.0
    var adj: Double = 0.0           // 收盘除权价 = 0.0
    var volume : Long = 0L          // 成交量（单位：万手）
}