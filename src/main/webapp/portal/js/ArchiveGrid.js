Ext.namespace('ETDArch');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArch.load = null;


ETDArch.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArch.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArch.proxy = new Ext.data.DataProxy({});
ETDArch.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDArch.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDArch.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArch.proxy,
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
			{name: 'id', type: 'int'}
	]
});
ETDArch.store.setDefaultSort('idpak', 'ASC');
ETDArch.cm = new Ext.grid.ColumnModel([
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

ETDArch.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArch.grid = new Ext.grid.GridPanel({
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
	cm : ETDArch.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArch.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArch.store,
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
		this.store.load({params :{start : 0, limit : ETD.docsPerPage, dir:'DESC', shift: shParam}});
	}
    });

ETDArch.addRequestParameter = function(name, value){
	ETDArch.store.baseParams[name] = value;
};

ETDArch.removeRequestParameter = function(name){
	ETDArch.store.baseParams[name] = null;
};

ETDArch.setRequestParameter = function(name, value){
	ETDArch.store.baseParams[name] = value;
};

ETDArch.setFormType = function(formType)
{
	ETDArch.setRequestParameter('formType', formType);
};

ETDArch.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	
	ETDArch.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
	ret = eval('('+document.applet_adapter.getArchiveDocuments(req)+')');
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
	ETDArch.grid.isLoaded = true;
	ETDArch.grid.count = ret.totalCount || 0;
	//show_news();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDArch.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'),null,1);
	}}
});



	