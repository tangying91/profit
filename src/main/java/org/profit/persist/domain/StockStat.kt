package org.profit.persist.domain

class StockStat {
    var day: String = "" // 日期
    var code: String = "" // 股票代码
    var type: Int = 0 // 分析周期类型 = 0

    // 成交量趋势分析，监测是否有放量趋势
    var volumeTotal: Long = 0       // 总成交量
    var volumeRate: Double = 0.0    // 平均量量比系数=（最近一日成交量 / 前面几日的平均成交量） = 0.0

    // 上涨趋势分析，监测上涨趋势
    var riseDay: Int = 0            // 上涨天数（今日收盘价高于开盘价视为上涨） = 0
    var amplitude: Double = 0.0     // 振幅=（周期最高价-周期最低价）/ 周期前一天收盘价 = 0.0
    var gains: Double = 0.0         // 涨幅= 周期开始开盘价 - 周期结束收盘价 = 0.0

    // 资金分类动态
    var minInflow: Double = 0.0     // 散户资金流入 = 0.0
    var midInflow: Double = 0.0     // 中户资金流入 = 0.0
    var bigInflow: Double = 0.0     // 大户资金流入 = 0.0
    var maxInflow: Double = 0.0     // 超大资金流入 = 0.0

    // 资金流入分析，监测资金动态
    var inflowAvg: Double = 0.0     // 平均资金流入= （净资金流入 / 周期天数） = 0.0
}