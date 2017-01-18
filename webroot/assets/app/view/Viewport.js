/**
 * Created by TangYing
 */
Ext.define('APP.view.Viewport', {
    extend: 'Ext.container.Viewport',

    layout: 'anchor',
   	hideBorders : true,
	
    items : [{
	    anchor: '100% 100%',
	    xtype : 'stock_pool'
	}]
});