Ext.namespace('ETDInwork');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDInwork.load = null;


ETDInwork.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDInwork.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDInwork.proxy = new Ext.data.DataProxy({});
ETDInwork.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDInwork.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDInwork.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDInwork.proxy,
	remoteSort: true,
	fields: 
	[
	    {name: 'name'},
		{name: 'number'},
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}
	]
});
ETDInwork.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDInwork.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDInwork.cm = new Ext.grid.ColumnModel([{
		id:'name',
		header: ETD.headers[0], 
		width: 120, 
		sortable: true,
		dataIndex: 'name'
	},{
		header: ETD.headers[1],
		width: 70,  
		sortable: true,
		dataIndex: 'number'
	},{
		header: ETD.headers[2], 
		width: 140, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},{
		header: ETD.headers[3], 
		width: 230, 
		sortable: true,
		dataIndex: 'lastSigner'
	},{
		header: ETD.headers[4], 
		width: 170, 
		sortable: true, 
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'lastDate'
	},{
		id:'expand',
		header: ETD.headers[5], 
		width: 150, 
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'short'
	},{
		header: 'id',
		dataIndex: 'id',
		hidden: true
	},{
		header:'cDel',
		dataIndex:'cDel',
		hidden: true
	},{
		header:'cCreateSource',
		dataIndex:'cCreateSource',
		hidden:true
	}]);

ETDInwork.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDInwork.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_INWORK_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Документы в работе',
	loadMask : false,
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: false,
	enableColumnMove: false,
//	plugins: ETDInwork.filters,
	cm : ETDInwork.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDInwork.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDInwork.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}
    });

ETDInwork.addRequestParameter = function(name, value){
	ETDInwork.store.baseParams[name] = value;
};

ETDInwork.removeRequestParameter = function(name){
	ETDInwork.store.baseParams[name] = null;
};

ETDInwork.setRequestParameter = function(name, value){
	ETDInwork.store.baseParams[name] = value;
};

ETDInwork.setFormType = function(formType)
{
	ETDInwork.setRequestParameter('formType', formType);
};


var show_news = function () {
	
	var hasnews = applet_adapter.HasNews();
	if(!hasnews)
		return;
	var message = applet_adapter.GetNews(); // сюда кидаем сообщение сервера
	on_ok = function(){
		win_news.destroy();
		applet_adapter.DeleteNews();
	}
	
	var text_data = new Ext.form.Label({
		text: message
		});
	
	var win_news = new Ext.Window({
		title: 'Сообщение:',
		layout:'fit',
		width: 400,
		border:false,
		hideBorders:true,
		style:'text-align:center;',
		resizable: false,
		buttonAlign:'center',
		closable: false,
		modal: true,
		items : text_data,
		buttons: [{text: 'OK',	handler:on_ok}]
	});
	
	win_news.show();
}



ETDInwork.load = function(params){
	var req = ETD.convertRequestParams(params);
	applet_adapter = document.applet;
	ret = eval('('+applet_adapter.getInworkDocuments(req)+')');
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
	ETDInwork.grid.isLoaded = true;
	ETDInwork.grid.count = ret.totalCount || 0;
	show_news();
	//Информация о количестве документов 
	total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDInwork.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'));
	}}
});



	