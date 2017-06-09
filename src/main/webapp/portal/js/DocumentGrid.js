Ext.namespace('ETDDoc');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDoc.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDoc.proxy = new Ext.data.DataProxy({});
ETDDoc.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		///*ETD.*/busy();
		var records = reader.readRecords(ETDDoc.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDoc.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDoc.proxy,
	remoteSort: true,
	fields: 
	[
	 	
	    {name: 'name'},
	    {name: 'number'},
	    {name: 'idpak'},
	    {name: 'creator'},		
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		/*{name: 'pole'},*/
		{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		
		{name: 'id', type: 'int'}
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});
//ETDDoc.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDDoc.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}	
	]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDDoc.store.setDefaultSort('idpak', 'ASC');
ETDDoc.cm = new Ext.grid.ColumnModel([
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
	//	id:'expand',
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
		
		header: ETD.headers[13], 
		width: 240, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},
	
	/*{
		header: ETD.headers[11], 
		width: 170, 
		sortable: true,
		dataIndex: 'lastSigner'
	},*/
	{
		id:'expand',
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

//ETDDoc.cm.defaultSortable = true;

ETDDoc.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDoc.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_DOCUMENT_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Текущие',
	loadMask : false,
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDoc.sm,
	enableColumnHide: false,
	enableColumnMove: false,
	//plugins: ETDDoc.filters,
	cm : ETDDoc.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDoc.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDoc.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
	},
	loadPage : function(num, str)
	{
		this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
		this.ppage = num;
		
	}
	/*loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}*/
    });

ETDDoc.addRequestParameter = function(name, value){
	ETDDoc.store.baseParams[name] = value;
};

ETDDoc.removeRequestParameter = function(name){
	ETDDoc.store.baseParams[name] = null;
};

ETDDoc.setRequestParameter = function(name, value){
	ETDDoc.store.baseParams[name] = value;
};

ETDDoc.setFormType = function(formType){
	ETDDoc.setRequestParameter('formType', formType);
};

ETDDoc.reload = function(){
	this.store.reload();
};
	
ETDDoc.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDoc.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
	//applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	
	ret = eval('('+document.applet_adapter.getDocuments(req)+')');
	
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
	ETDDoc.grid.isLoaded = true;
	
	ETDDoc.grid.count = ret.totalCount || 0;
	
	//show_news();
	
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};

ETDDoc.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
	
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}}
});
ETDDoc.sm.on({
	rowdeselect : { fn : function()
	{
		//Ext.getCmp('drop').disable();
	}},
	rowselect : { fn : function(sm, num, r)
	{
		//if (r.get('cDel')=='1') Ext.getCmp('drop').enable(); 
		//else Ext.getCmp('drop').disable();
	}}

});




	