package org.profit.app.analyse

import org.profit.util.StockUtils

/**
 * 回溯历史数据
 * 根据既定策略进行模拟交易
 */
class StockMockTrading(code: String, private val statCount: Int = 100) : StockAnalyzer(code) {

    private var initMoney = 100000.0        // 初始本金
    private var totalMoney = initMoney      // 初始本金
    private var leftMoney = initMoney       // 剩余资金
    private val initPositions = 0.3         // 初始仓位
    private val perMoney = 3000             // 每次加减仓金额
    private var stockNumber = 0             // 初始股票数量

    private var lastBuyPrice = 0.0
    private var serialSellCount = 0         // 连续卖出次数

    override fun analyse() {
        // 获取数据，后期可以限制天数
        val list = readHistories(statCount).sortedBy { it.dateTime }
        if (list.size != statCount) {
            return
        }

        // 购入初始仓位，以第一天的尾盘价格买入
        val sn = calcStockNumber(money = (leftMoney * initPositions).toInt(), price = list[0].close)
        val cost = sn * list[0].close
        leftMoney -= cost
        stockNumber += sn
        lastBuyPrice = list[0].close

        println("仓位初始化成功，买入价格${list[0].close}, 当前持有股票数量$stockNumber 股，剩余资金 $leftMoney")

        // 后面循环处理
        for (i in 1 until list.size) {
            val data = list[i]
            val open = data.open
            val date = data.date

            // 看下降了多少个百分比
            val downPercent = (data.low - open).div(open)
            val upPercent = (data.high - open).div(open)

            // 下降3个点
            val p1 = -0.03
            if (downPercent <= p1) {
                doStockBuy(date, StockUtils.twoDigits(open * (1 + p1)).toDouble())
            }

            // 下降6个点
            val p2 = -0.06
            if (downPercent <= p2) {
                doStockBuy(date, StockUtils.twoDigits(open * (1 + p1)).toDouble())
            }

            // 上涨3个点
            val u1 = 0.02
            if (upPercent >= u1) {
                serialSellCount++
            }

            if (serialSellCount >= 3) {
                doStockSell(date, StockUtils.twoDigits(open * (1 + u1)).toDouble())
            }
        }

        val totalDay = list.size
        val percent = (list[statCount - 1].open - list[0].close).div(list[0].close)
        println("$code 完成了 $totalDay 个交易日的模拟交易.")
        println("目前剩余本金 $leftMoney, 股票数量 $stockNumber, 总市值 ${stockNumber * list[statCount - 1].close}, 剩余资金 $leftMoney, 总资产${stockNumber * list[statCount - 1].close + leftMoney}")
        println("$code 在过去 $totalDay 个交易日里，区间涨幅为 ${StockUtils.twoDigits(percent * 100)}%. 如果一次性买入，最终剩余 ${initMoney * (1 + percent)}")
    }

    /**
     * 根据价格换算股票数量
     * 股票只能是100的整数倍
     */
    private fun calcStockNumber(money: Int, price: Double): Int {
        return ((money.toDouble().div(price).toInt() / 100) * 100)
    }

    /**
     * 执行买入
     */
    private fun doStockBuy(date: String, price: Double) {
        val buyMoney = if (stockNumber == 0) {
            (initMoney * initPositions).toInt()
        } else {
            perMoney
        }

        // 先检查钱是否足够
        if (leftMoney < buyMoney) {
            println("$date 触发加仓价格$price, 但是当前只有 $leftMoney，加仓失败！")
            return
        }

        // 加仓成功
        val sn = calcStockNumber(money = buyMoney, price = price)
        val cost = sn * price
        leftMoney -= cost
        stockNumber += sn
        serialSellCount = 0
        lastBuyPrice = price

        println("$date 触发加仓价格$price, 买入，当前剩余$stockNumber 股，总市值${stockNumber * price}, 剩余资金 $leftMoney, 总资产${stockNumber * price + leftMoney}")
    }

    /**
     * 执行卖出策略
     */
    private fun doStockSell(date: String, price: Double) {
        // 先检查股票数量是否足够
        val sn = calcStockNumber(money = perMoney, price = price)
        if (sn > stockNumber) {
            println("$date 触发卖出价格$price, 但是当前只有 $stockNumber 股，卖出失败！")
            return
        }

        // 卖出成功
        val sell = sn * price
        leftMoney += sell
        stockNumber -= sn

        println("$date 触发卖出价格$price, 卖出成功，当前剩余$stockNumber 股，总市值${stockNumber * price}, 剩余资金 $leftMoney, 总资产${stockNumber * price + leftMoney}")

        // 连续卖出三次，清仓处理
        if (serialSellCount >= 4) {
            val sell1 = stockNumber * price
            leftMoney += sell1
            stockNumber = 0
            serialSellCount = 0

            println("$date 触发清仓策略$price, 剩余资金 $leftMoney!!!")
        }
    }
}