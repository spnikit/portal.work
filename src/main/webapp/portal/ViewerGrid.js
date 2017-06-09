
Ext.define('Aisa.static.ViewerGrid', {
	extend: 'Ext.grid.Panel',
	border: false,
	
	url: null,
	workerId: null,
	predId: null,
	persId: null,
	isSysWrkr: 0,
	dateAfter: null,
	dateBefore: null,
	
	isChanges: 0,
	isRender: 0,
	initComponent: function() {
		var store = new Ext.data.JsonStore({
		    proxy: {
		        type: 'ajax',
		        url: this.url,
		    	extraParams: {
		    		workerId: this.workerId,
		    		predId: this.predId,
		    		persId: this.persId,
		    		isSysWrkr: this.isSysWrkr,
		    		dateAfter: this.dateAfter,
					dateBefore: this.dateBefore
		    	},
		        reader: {
		            type: 'json',
		            root: 'data',
		            totalProperty: 'total'
		        }
		    },
		    pageSize: 20,
		    fields: ['id', 'name','cDel','number','cCreateSourse',
		    	'predId','createDate','lastDate','lastSigner'] //docData,shortContent
		});
		
		var cols = {
			items: [
				{xtype:'rownumberer'},
				{text:'ID документа', dataIndex:'id'},
				{text:'Имя документа', dataIndex:'name'},
				{text:'createDate', dataIndex:'createDate'},
				{text:'cDel', dataIndex:'cDel'},
				{text:'number', dataIndex:'number'},
				{text:'cCreateSourse', dataIndex:'cCreateSourse'},
				{text:'predId', dataIndex:'predId'},
				{text:'lastDate', dataIndex:'lastDate'},
				{text:'lastSigner', dataIndex:'lastSigner'}
			],
			defaults: {
				menuDisabled: true,
				sortable: false,
				width: 200
			}
		};
		
		Ext.apply(this, {
			store: store,
			columns: cols,
			listeners: {
				itemdblclick: function(table,rec){
					this.doubleClick(rec.getData());
				},
				render: function() {
					store.load();
					this.isRender = 1;
				},
				activate: function() {
					if (this.isChanges==1){
						this.isChanges = 0;
						store.load();
					}
				}
			},
			dockedItems: [{
	            xtype: 'pagingtoolbar',
	            store: store,
	            dock: 'bottom',
	            displayInfo: true
	        }]
		});
		this.callParent(arguments);
	},
	doubleClick: function(rec){
		var view = this.findParentByType('aisaviewer');
		view.ajaxForm(rec.id);
	}
});
