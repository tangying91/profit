package org.profit.app.vo;

import org.profit.app.realm.StockHall;
import org.profit.persist.domain.stock.Pool;

public class StockNode {

	// 组合主键
	private String day;		        // 日期
	private int type;               // 分析周期类型
	private String code;            // 股票代码
	private String name;            // 股票名称

	// 成交量趋势分析，监测是否有放量趋势
	private long volumeTotal;	     // 总成交量
	private double volumeRate;	     // 平均量量比系数=（最近一日成交量 / 前面几日的平均成交量）

	// 上涨趋势分析，监测上涨趋势
	private int riseDay;		     // 上涨天数（今日收盘价高于开盘价视为上涨）
	private double amplitude;		 // 振幅=（周期最高价-周期最低价）/ 周期前一天收盘价
	private double gains;		     // 涨幅= 周期开始开盘价 - 周期结束收盘价

	// 资金分类动态
	private double minInflow;       // 散户资金流入
	private double midInflow;       // 中户资金流入
	private double bigInflow;       // 大户资金流入
	private double maxInflow;       // 超大资金流入
	// 资金流入分析，监测资金动态
	private double inflowAvg;       // 平均资金流入= （净资金流入 / 周期天数）

	public StockNode(Pool pool) {
		this.day = pool.getDay();
		this.type = pool.getType();
		this.code = pool.getCode();
		this.name = StockHall.getStockName(code);
		this.volumeTotal = pool.getVolumeTotal();
		this.volumeRate = pool.getVolumeRate();
		this.riseDay = pool.getRiseDay();
		this.amplitude = pool.getAmplitude();
		this.gains = pool.getGains();
		this.minInflow = pool.getMinInflow();
		this.midInflow = pool.getMidInflow();
		this.bigInflow = pool.getBigInflow();
		this.maxInflow = pool.getMaxInflow();
		this.inflowAvg = pool.getInflowAvg();
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getVolumeTotal() {
		return volumeTotal;
	}

	public void setVolumeTotal(long volumeTotal) {
		this.volumeTotal = volumeTotal;
	}

	public double getVolumeRate() {
		return volumeRate;
	}

	public void setVolumeRate(double volumeRate) {
		this.volumeRate = volumeRate;
	}

	public int getRiseDay() {
		return riseDay;
	}

	public void setRiseDay(int riseDay) {
		this.riseDay = riseDay;
	}

	public double getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(double amplitude) {
		this.amplitude = amplitude;
	}

	public double getGains() {
		return gains;
	}

	public void setGains(double gains) {
		this.gains = gains;
	}

	public double getMinInflow() {
		return minInflow;
	}

	public void setMinInflow(double minInflow) {
		this.minInflow = minInflow;
	}

	public double getMidInflow() {
		return midInflow;
	}

	public void setMidInflow(double midInflow) {
		this.midInflow = midInflow;
	}

	public double getBigInflow() {
		return bigInflow;
	}

	public void setBigInflow(double bigInflow) {
		this.bigInflow = bigInflow;
	}

	public double getMaxInflow() {
		return maxInflow;
	}

	public void setMaxInflow(double maxInflow) {
		this.maxInflow = maxInflow;
	}

	public double getInflowAvg() {
		return inflowAvg;
	}

	public void setInflowAvg(double inflowAvg) {
		this.inflowAvg = inflowAvg;
	}
}
