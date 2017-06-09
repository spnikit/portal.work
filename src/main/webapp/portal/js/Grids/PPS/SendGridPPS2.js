Ext.namespace('ETDSendPPS2');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSendPPS2.load = null;


ETDSendPPS2.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSendPPS2.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSendPPS2.proxy = new Ext.data.DataProxy({});
ETDSendPPS2.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSendPPS2.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSendPPS2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSendPPS2.proxy,
	remoteSort: true,
	fields: 
	[
	 	{name: 'creator'},
	    {name: 'name'},
		{name: 'content'},
		{name: 'dognum'},
		{name: 'idpak'},
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'reqdate'},
		{name: 'vagnum'},
		{name: 'id', type: 'int'},
		{name: 'status', type:'int'},
		{name: 'di'},
		{name: 'rem_pred'},
		{name: 'packst'},
		{name: 'otcname'}
	]
});
ETDSendPPS2.store.setDefaultSort('vagnum', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDSendPPS2.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSendPPS2.cm = new Ext.grid.ColumnModel([
   {
       	header: 'Номер',
    	sortable: true,
        renderer : ETD.shortDescriptionRender,
    	dataIndex: 'dognum'
    },	
    {
    		id:'name',
    		header: ETD.headers[10], 
    		sortable: true,
    		dataIndex: 'name',
    		tdCls: 'test'
    	},

    	 {id:'expand',
    		header: ETD.headers[12],
    		sortable: true,
    		renderer: ETD.shortDescriptionRender,
    		dataIndex: 'content'
    		
    	},

    	
    	{
    		header: 'Заказчик', 
    		sortable: true,
    		renderer: ETD.shortDescriptionRender,
    		dataIndex: 'rem_pred'
    	},
    	  {
    		  header: 'Договор', 
    		  sortable: true,
    		  renderer: ETD.shortDescriptionRender,
    		  dataIndex: 'di'
    	  },
    	
       {
    		
    		header: 'Дата', 
    		sortable: true,
    		renderer: ETD.shortDescriptionRender,
    		dataIndex: 'reqdate'
    	}
	]);

ETDSendPPS2.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSendPPS2.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_SEND_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Документы в работе',
	loadMask : false,
	viewConfig: {
	    forceFit: true,

	// ПАААААААААДСВЕТАЧКА
	    getRowClass: function(record, index) {
		
	        var c = record.get('status');
	        //alert(c); 
	       if (c==1)
	            return 'acc-row';
	         else if (c==2)
	       	return 'dropped-row';
	       	  else if (c==3)
	         	return 'warn-row';
	       	 else if (c==4)
	           	return 'income-row';
	    }
	    
	},
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSendPPS2.filters,
	cm : ETDSendPPS2.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSendPPS2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSendPPS2.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
			this.ppage = num;
			
		},
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage, dir:'DESC',shift: shParam}});
	},

listeners: {
dblclick : {fn : function(e){
	if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();
	ETD.openDocument(row.get('id'), row.get('name'),null,1);
	
}
}}
}
    });

ETDSendPPS2.addRequestParameter = function(name, value){
	ETDSendPPS2.store.baseParams[name] = value;
};

ETDSendPPS2.removeRequestParameter = function(name){
	ETDSendPPS2.store.baseParams[name] = null;
};

ETDSendPPS2.setRequestParameter = function(name, value){
	ETDSendPPS2.store.baseParams[name] = value;
};

ETDSendPPS2.setFormType = function(formType)
{
	ETDSendPPS2.setRequestParameter('formType', formType);
};

ETDSendPPS2.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSendPPS2.grid.ppage = (params.start/ETD.docsPerPage)+1;
	ret = eval('('+document.applet_adapter.getSendDocuments(req)+')');
	if (ret.error)
	{
		Ext.Msg.show({
		   title:'Ошибка',
		   msg: ret.error,
		   buttons: Ext.Msg.OK,
		   fn: Refresh(),
		   icon: Ext.MessageBox.ERROR
		});
	};
	//TODO Сделать проверку удачной загрузки
	ETDSendPPS2.grid.isLoaded = true;
	ETDSendPPS2.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};




	