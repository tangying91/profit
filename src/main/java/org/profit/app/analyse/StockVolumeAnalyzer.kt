package org.profit.app.analyse

/**
 * 【突】周期末,股价成交量大幅拉升
 * 周期内成交量是否有异动
 */
class StockVolumeAnalyzer(code: String, private val statCount: Int) : StockAnalyzer(code) {

    /**
     * 1.周期内平均成交量（去除最低和最高）
     * 2.最高成交量和平均成交量的关系
     * 3.周内成交量上涨天数
     */
    override fun analyse() {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount)

        // 如果没有数据
        if (list.size <= 2) {
            return
        }

        val totalDay = list.size
        val avgVolume = (list.map { it.volume }.sum()).div(totalDay)
        val riseDays = list.count { it.volume > avgVolume }
        println("大于平均成交量$riseDays 天")
    }
}