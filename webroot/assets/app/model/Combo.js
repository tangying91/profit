Ext.define('APP.model.Combo', {
	extend : 'Ext.data.Model',
	
    requires: [
        'Ext.data.reader.Json'
    ],
	
	fields : [ 'display', 'value']
});