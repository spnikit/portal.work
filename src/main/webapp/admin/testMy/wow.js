





Ext.onReady(function () {

    new Ext.Window({
        width: 200,
        height: 150,
        title: 'Accordion window',
        layout: 'accordion',
        border: false,
        layoutConfig: {
            animate: true
        },

        items: [
            {
                xtype: 'panel',
                title: 'Plain Panel',
                html: 'Panel with an xtype specified'
            },
            {
                title: 'Plain Panel 2',
                html: 'Panel with <b>no</b> xtype specified'
            }
        ]
    }).show();

});