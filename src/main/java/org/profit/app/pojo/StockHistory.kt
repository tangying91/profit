package org.profit.app.pojo

class StockHistory {
    var date : String = ""                  // 时间
    var dateTime : Long = 0L                // 时间
    var open: Double = 0.0                  // 当日开盘价 = 0.0
    var close: Double = 0.0                 // 当日收盘价 = 0.0
    var high: Double = 0.0                  // 当日最高价 = 0.0
    var low: Double = 0.0                   // 当日最低价 = 0.0
    var volume : Long = 0L                  // 成交量（单位：手）
    var turnover : Long = 0L                // 成交金额（单位：万）
    var stockPercent : Double = 0.0         // 个股涨跌百分比
    var stockIndexPercent : Double = 0.0    // （所在大盘）指数涨跌百分比
}