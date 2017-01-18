/**
 * 客户端主程序入口
 */
Ext.onReady(function () {
    console.log("App on ready.");

    Ext.application({
        name: 'APP',
        appFolder: 'assets/app',
        controllers: [
            'Modules'
        ],
        launch: function () {
            Ext.create('APP.view.Viewport');
            console.log("App viewport created.");
        }
    });
    console.log("App started successful.");
});

