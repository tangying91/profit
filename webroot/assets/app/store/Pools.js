Ext.define('APP.store.Pools', {
    extend: 'Ext.data.Store',
    model: 'APP.model.Pool',
    autoLoad: true,
    pageSize: 25,

    proxy: {
        type: 'ajax',
        url: ACTION.STOCK_POOL_LOAD,
        method: 'GET',
        extraParams: {
            type: 5,
            riseDay: GLOBAL.DEFAULT_RISE_DAY,
            volumeRate: GLOBAL.DEFAULT_VOLUME_RATE,
            gainsRange: GLOBAL.DEFAULT_GAINS_RANGE,
            fundRange: GLOBAL.DEFAULT_FUND_RANGE,
            day: Ext.Date.format(new Date(defaultDate), 'Y-m-d')
        },

        reader: {
            type: 'json',
            root: 'items',
            totalProperty: 'count',
            successProperty: 'success'
        }
    }
});