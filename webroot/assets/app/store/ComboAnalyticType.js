Ext.define('APP.store.ComboAnalyticType', {
    extend: 'Ext.data.Store',
    model: 'APP.model.Combo',

    data: {'items': [
        { 'display': '综', "value": 0},
        { 'display': '积', "value": 1},
        { 'display': '突', "value": 2},
        { 'display': '金', "value": 3},
        { 'display': '底', "value": 4}
    ]},
    proxy: {
        type: 'memory',
        reader: {
            type: 'json',
            root: 'items'
        }
    }
});