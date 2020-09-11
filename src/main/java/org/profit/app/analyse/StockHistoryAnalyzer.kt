package org.profit.app.analyse

/**
 * 股价历史回顾
 * 股票从上市到现在的所有数据
 */
class StockHistoryAnalyzer(code: String) : StockAnalyzer(code) {

    /**
     * 统计样本数
     */
    private val statCount = Int.MAX_VALUE

    /**
     * 1.周期内股票上涨金额和涨幅
     * 2.周期内最高股价
     * 3.周期内最高成交量
     * 4.周期内累计上涨天数
     */
    override fun analyse() {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.isEmpty()) {
            println("$code 数据不足")
            return
        }

//        val totalDay = list.size
//        val maxVolume = list.map { it.volume }.max() ?: 0L
//        val minVolume = list.map { it.volume }.min() ?: 0L
//        val avgVolume = (list.map { it.volume }.sum() - minVolume - maxVolume).div(totalDay - 2)
//        val maxVolumeRate = maxVolume / avgVolume
//        val serialRiseDays = serialDaysOfRise(list)
//        val riseDays = list.count { it.volume > avgVolume }
//        val result = if (riseDays.div(totalDay.toDouble()) <= 0.2) {
//            "成交量出现超级异动"
//        } else {
//            if (serialRiseDays > 5) {
//                "成交量出现连续放量"
//            } else {
//                if (maxVolumeRate >= 3) {
//                    "出现放量现象"
//                } else {
//                    ""
//                }
//            }
//        }
//
//        println("$code 一共$totalDay 天，最大连续上涨$serialRiseDays 天，大于平均成交量$riseDays 天，最大成交量是平均成交量的$maxVolumeRate 倍 $result")
    }
}