Ext.namespace('ETDRough');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDRough.load = null;


ETDRough.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDRough.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDRough.proxy = new Ext.data.DataProxy({});
ETDRough.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDRough.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDRough.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDRough.proxy,
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
		
		{name: 'id', type: 'int'}/*,
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});
ETDRough.store.setDefaultSort('idpak', 'ASC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDRough.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDRough.cm = new Ext.grid.ColumnModel([
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
	//id:'expand',
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

ETDRough.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDRough.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_ROUGH_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Черновики',
	loadMask : false,
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: false,
	enableColumnMove: false,
//	plugins: ETDRough.filters,
	cm : ETDRough.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDRough.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDRough.store,
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
	/*loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}*/
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
	}
    });

ETDRough.addRequestParameter = function(name, value){
	ETDRough.store.baseParams[name] = value;
};

ETDRough.removeRequestParameter = function(name){
	ETDRough.store.baseParams[name] = null;
};

ETDRough.setRequestParameter = function(name, value){
	ETDRough.store.baseParams[name] = value;
};

ETDRough.setFormType = function(formType)
{
	ETDRough.setRequestParameter('formType', formType);
};

ETDRough.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	
	ETDRough.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
	ret = eval('('+document.applet_adapter.getRoughDocuments(req)+')');
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
	ETDRough.grid.isLoaded = true;
	ETDRough.grid.count = ret.totalCount || 0;
	//show_news();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDRough.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}}
});



	