Ext.namespace('ETDSend');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSend.load = null;


ETDSend.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSend.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSend.proxy = new Ext.data.DataProxy({});
ETDSend.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSend.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSend.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSend.proxy,
	remoteSort: true,
	fields: 
	[
	 	{name: 'creator'},
	    {name: 'name'},
		{name: 'number'},
		{name: 'idpak'},
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
	
		{name: 'id', type: 'int'}//,
		/*{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});
ETDSend.store.setDefaultSort('idpak', 'ASC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDSend.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSend.cm = new Ext.grid.ColumnModel([
{
	id:'name',
	header: ETD.headers[10], 
	width: 220, 
	sortable: true,
	dataIndex: 'name'
},
 {
	header: ETD.headers[12],
	width: 170,  
	sortable: true,
	dataIndex: 'number'
},
{
	id:'expand',
	header: 'Пакет',
	width: 170, 
	sortable: true,
	dataIndex: 'idpak'
},
{
	header: ETD.headers[9], 
	width: 170, 
	sortable: true,
	dataIndex: 'creator'
},
{
//	id:'expand',
	header: ETD.headers[13], 
	width: 170, 
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'createDate'
},
{
	header: ETD.headers[11], 
	width: 170, 
	sortable: true,
	dataIndex: 'lastSigner'
},
{
	header: ETD.headers[14], 
	width: 170, 
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'lastDate'
},

	{
		header: 'id',
		dataIndex: 'id',
		hidden: true
	}]);

ETDSend.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSend.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_SEND_GRID',
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
//	plugins: ETDSend.filters,
	cm : ETDSend.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSend.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSend.store,
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
	}
	/*loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}*/
    });

ETDSend.addRequestParameter = function(name, value){
	ETDSend.store.baseParams[name] = value;
};

ETDSend.removeRequestParameter = function(name){
	ETDSend.store.baseParams[name] = null;
};

ETDSend.setRequestParameter = function(name, value){
	ETDSend.store.baseParams[name] = value;
};

ETDSend.setFormType = function(formType)
{
	ETDSend.setRequestParameter('formType', formType);
};

ETDSend.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSend.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDSend.grid.isLoaded = true;
	ETDSend.grid.count = ret.totalCount || 0;
	//show_news();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDSend.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'),null,1);
	}}
});



	