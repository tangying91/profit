Ext.define('APP.view.stock.StockPoolPanel', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.stock_pool',
    title: "股票池 v2.0",

    initComponent: function () {
        console.log("init component.");

        Ext.apply(this, {
            store: 'Pools',
            columns: [
                {header: "代码", dataIndex: 'code', flex: 1, align: "center"},
                {header: "名称", dataIndex: 'name', flex: 1, align: "center"},
                {header: "周期", dataIndex: 'type', width: 100, align: "center"},
                {header: "周期上涨天数", dataIndex: 'riseDay', width: 100, align: "center"},
                {header: "总成交量", dataIndex: 'volumeTotal', width: 100, align: "center"},
                {header: "平均量比", dataIndex: 'volumeRate', width: 100, align: "center"},
                {header: "振幅", dataIndex: 'amplitude', width: 100, align: "center", renderer: RenderUtil.percent},
                {header: "涨幅", dataIndex: 'gains', flex: 1, align: "center", renderer: RenderUtil.percent},
                {header: "散户", dataIndex: 'minInflow', width: 100, align: "center", renderer: RenderUtil.change},
                {header: "中户", dataIndex: 'midInflow', width: 100, align: "center", renderer: RenderUtil.change},
                {header: "大户", dataIndex: 'bigInflow', width: 100, align: "center", renderer: RenderUtil.change},
                {header: "超大", dataIndex: 'maxInflow', width: 100, align: "center", renderer: RenderUtil.change},
                {header: "平均日（万）", dataIndex: 'inflowAvg', flex: 1, align: "center", renderer: RenderUtil.change}
            ],

            viewConfig: {
                enableTextSelection: true
            },

            tbar: [
                {
                    xtype: 'datefield',
                    id: 'analytic_day',
                    fieldLabel: "分期日期",
                    labelAlign: 'right',
                    labelWidth: 60,
                    format: 'Y-m-d',
                    value: Ext.Date.format(new Date(defaultDate), 'Y-m-d')
                },
                '-',
                {
                    xtype: 'combobox',
                    id: 'analytic_type',
                    fieldLabel: "指标类型",
                    store: 'ComboAnalyticType',
                    valueField: 'value',
                    displayField: 'display',
                    labelWidth: 60,
                    editable: false,
                    allowBlank: false,
                    value: 0
                },
                '-',
                {
                    xtype: 'button',
                    text: LANG.BTN.search,
                    iconCls: 'icon-search',
                    handler: function () {
                        var day = Ext.Date.format(new Date(Ext.getCmp('analytic_day').getValue()), 'Y-m-d');
                        var analyticType = Ext.getCmp('analytic_type').getValue();

                        var type = GLOBAL.DEFAULT_TYPE;
                        var riseDay = GLOBAL.DEFAULT_RISE_DAY;
                        var volumeRate = GLOBAL.DEFAULT_VOLUME_RATE;
                        var gainsRange = GLOBAL.DEFAULT_GAINS_RANGE;
                        var fundRange = GLOBAL.DEFAULT_FUND_RANGE;

                        // 积，周期内，收涨天数大于一定比率
                        if (analyticType == 1) {
                            type = GLOBAL.RISE_TYPE;
                            riseDay = GLOBAL.RISE_RISE_DAY;
                            volumeRate = GLOBAL.RISE_VOLUME_RATE;
                            gainsRange = GLOBAL.RISE_GAINS_RANGE;
                            fundRange = GLOBAL.RISE_FUND_RANGE;
                        }

                        else if (analyticType == 2) {
                            type = GLOBAL.BREAK_TYPE;
                            riseDay = GLOBAL.BREAK_RISE_DAY;
                            volumeRate = GLOBAL.BREAK_VOLUME_RATE;
                            gainsRange = GLOBAL.BREAK_GAINS_RANGE;
                            fundRange = GLOBAL.BREAK_FUND_RANGE;
                        }

                        else if (analyticType == 3) {
                            type = GLOBAL.FUND_TYPE;
                            riseDay = GLOBAL.FUND_RISE_DAY;
                            volumeRate = GLOBAL.FUND_VOLUME_RATE;
                            gainsRange = GLOBAL.FUND_GAINS_RANGE;
                            fundRange = GLOBAL.FUND_FUND_RANGE;
                        }

                        else if (analyticType == 4) {
                            type = GLOBAL.BOTTOM_TYPE;
                            riseDay = GLOBAL.BOTTOM_RISE_DAY;
                            volumeRate = GLOBAL.BOTTOM_VOLUME_RATE;
                            gainsRange = GLOBAL.BOTTOM_GAINS_RANGE;
                            fundRange = GLOBAL.BOTTOM_FUND_RANGE;
                        }

                        else {
                            // Default...
                        }

                        Ext.getStore('Pools').proxy.extraParams = {type: type, day: day, riseDay: riseDay, volumeRate: volumeRate, gainsRange: gainsRange, fundRange: fundRange};
                        Ext.getStore('Pools').reload();
                    }
                },

                '->',

                '*每天下午8点开始下载并分析当天数据'
            ],

            dockedItems: [
                {
                    xtype: 'pagingtoolbar',
                    store: 'Pools',
                    dock: 'bottom',
                    displayInfo: true
                }
            ]
        });
        this.callParent();
    }
});
