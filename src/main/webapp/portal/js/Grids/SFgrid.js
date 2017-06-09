Ext.namespace('SFdoc');

SFdoc.load = null;

SFdoc.store  = new Ext.data.JsonStore({
root:'notice', 	
remoteSort: false,
fields: ['NAME', 'STATUSNAME', 'STATUS', 'ID', 'NO']

}); 


SFdoc.req=null;
SFdoc.grid = null;

	SFdoc.load = function(req)
{
    Ext.Ajax.request({
        url: 'forms/previewreseipts',
        params: {
            id: req,
            action:'1'
        },
        callback: function (options, success, response) {
  
        	var resp = response.responseText;
            if (resp.error)
            {
                Ext.Msg.show({
                    title:'Ошибка',
                    msg: resp.error,
                    buttons: Ext.Msg.OK,
                    fn: Refresh(),
                    icon: Ext.MessageBox.ERROR
                });
            };

            SFdoc.grid.isLoaded = true;
            var response = Ext.util.JSON.decode(resp);
            response = response[0];
            
            SFdoc.store.loadData(response);
            return null;
        }
    });
};

viewwin = function(){
	viewxml = new Ext.Window({
	plain: 'true',
	width: 500,
	height:300,
	layout: 'fit',
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'right',
	closable: false,
	title:'Просмотр квитанции',
	style:'text-align: center;',
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [
	        {xtype:'textarea',displayField:'text',hidden: false,disabled: false,mode: 'local',triggerAction: 'all',id:'xml', width:500, height: 500}
					],
		buttons: [{text: 'OK',
		handler:function(){
			viewxml.close();
		}	
	}
	]
	});
}

	
	var replaceQuot = new RegExp("\n", "g");