ru.aisa.clearButton = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		conf = {xtype:'button',text:'Очистить'}
		ru.aisa.clearButton.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			var tag = Ext.getCmp('treepanel').getSelectionModel().getSelectedNode().text;
			if(tag == 'Выборка'){
				grid.getStore().baseParams.predid=null;
				grid.getStore().baseParams.dateFrom=null;
				grid.getStore().baseParams.dateTo=null;
				grid.getStore().baseParams.searchField=null;
				SampleSearchData.predid = null;
				SampleSearchData.dateFrom = null;
				SampleSearchData.dateTo = null;
			}

				var search1;
				grid.getStore().baseParams.search1=null;
				grid.getStore().load();
		})
	}});