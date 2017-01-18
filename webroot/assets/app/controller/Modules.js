Ext.define('APP.controller.Modules', {
    extend: 'Ext.app.Controller',

    stores: [
        'ComboYesNo@APP.store',
        'ComboAnalyticType@APP.store',
        'Pools@APP.store'
    ],

    views: [
        'StockPoolPanel@APP.view.stock'
    ],

    refs: [
        { ref: 'stockPool', selector: 'stock_pool'}
    ],

    init: function () {
        console.log("Modules controller init");
        this.control({
            'stock_pool': {
                celldblclick: function (view, td, cellIndex, record) {
                    var code = record.get("code");

                    if (code > 600000) {
                        code = "sh" + code;
                    } else {
                        code = "sz" + code;
                    }

                    var url = "http://finance.sina.com.cn/realstock/company/#code/nc.shtml";
                    url = url.replace("#code", code);
                    window.open(url);
                }
            }
        });
    }
});
