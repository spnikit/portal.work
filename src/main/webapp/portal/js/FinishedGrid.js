Ext.namespace('ETDFin');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDFin.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDFin.proxy = new Ext.data.DataProxy({});
ETDFin.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDFin.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDFin.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDFin.proxy,
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
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});

ETDFin.store.setDefaultSort('idpak', 'ASC');
//ETDDoc.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDDoc.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}	
	]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDFin.cm = new Ext.grid.ColumnModel([
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

//ETDDoc.cm.defaultSortable = true;

ETDFin.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDFin.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_FIN_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Отклоненные',
	loadMask : false,
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDFin.sm,
	enableColumnHide: false,
	enableColumnMove: false,
	//plugins: ETDFin.filters,
	cm : ETDFin.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDFin.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDFin.store,
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
		this.store.load({params :{start : 0, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
	}
	/*loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}*/
    });

ETDFin.addRequestParameter = function(name, value){
	ETDFin.store.baseParams[name] = value;
};

ETDFin.removeRequestParameter = function(name){
	ETDFin.store.baseParams[name] = null;
};

ETDFin.setRequestParameter = function(name, value){
	ETDFin.store.baseParams[name] = value;
};

ETDFin.setFormType = function(formType){
	ETDFin.setRequestParameter('formType', formType);
};

ETDFin.reload = function(){
	this.store.reload();
};
	
ETDFin.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	ETDFin.grid.ppage = (params.start/ETD.docsPerPage)+1;
	ret = eval('('+document.applet_adapter.getFinishedDocuments(req)+')');
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
	ETDFin.grid.isLoaded = true;
	ETDFin.grid.count = ret.totalCount || 0;
	//show_news();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDFin.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}}
});
ETDFin.sm.on({
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




	