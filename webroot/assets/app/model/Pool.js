Ext.define('APP.model.Pool', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.reader.Json'
    ],

    fields: ['day', 'code', 'name', 'type', 'volumeTotal', 'volumeRate', 'riseDay', 'amplitude', 'gains',
        'inflowAvg', 'minInflow',  'midInflow',  'bigInflow', 'maxInflow']
});