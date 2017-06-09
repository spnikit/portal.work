Ext.namespace('ETDArchR2');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchR2.load = null;


ETDArchR2.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchR2.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchR2.proxy = new Ext.data.DataProxy({});
ETDArchR2.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDArchR2.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDArchR2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchR2.proxy,
	remoteSort: true,
	fields: 
	[
	 	{name: 'creator'},
	    {name: 'name'},
		{name: 'number'},
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'}/*,
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});
ETDArchR2.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDArchR2.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDArchR2.cm = new Ext.grid.ColumnModel([
	{
		header: ETD.headers[6], 
		width: 170, 
		sortable: true,
		dataIndex: 'creator'
	},
    {
		id:'name',
		header: ETD.headers[0], 
		width: 220, 
		sortable: true,
		dataIndex: 'name'
	},{
		header: ETD.headers[1],
		width: 170,  
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
		id:'expand',
		header: ETD.headers[4], 
		width: 170, 
		sortable: true, 
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'lastDate'
	},/*{
		id:'expand',
		header: ETD.headers[5], 
		width: 150, 
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'short'
	},*/{
		header: 'id',
		dataIndex: 'id',
		hidden: true
	}/*,{
		header:'cDel',
		dataIndex:'cDel',
		hidden: true
	},{
		header:'cCreateSource',
		dataIndex:'cCreateSource',
		hidden:true
	}*/]);

ETDArchR2.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchR2.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_ARCHIVE_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Архив',
	loadMask : false,
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: false,
	enableColumnMove: false,
//	plugins: ETDArch.filters,
	cm : ETDArchR2.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArchR2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchR2.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}
    });

ETDArchR2.addRequestParameter = function(name, value){
	ETDArchR2.store.baseParams[name] = value;
};

ETDArchR2.removeRequestParameter = function(name){
	ETDArchR2.store.baseParams[name] = null;
};

ETDArchR2.setRequestParameter = function(name, value){
	ETDArchR2.store.baseParams[name] = value;
};

ETDArchR2.setFormType = function(formType)
{
	ETDArchR2.setRequestParameter('formType', formType);
};

ETDArchR2.load = function(params){
	var req = ETD.convertRequestParams(params);
	applet_adapter = document.applet;
	ret = eval('('+applet_adapter.getArchiveDocuments(req)+')');
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
	ETDArchR2.grid.isLoaded = true;
	ETDArchR2.grid.count = ret.totalCount || 0;
	show_news();
	//Информация о количестве документов 
	total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDArchR2.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'));
	}}
});



	