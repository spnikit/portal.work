Ext.namespace('ETDDocR2');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDocR2.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocR2.proxy = new Ext.data.DataProxy({});
ETDDocR2.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		/*ETD.*/busy();
		var records = reader.readRecords(ETDDocR2.load(params));
		/*ETD.*/ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocR2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocR2.proxy,
	remoteSort: true,
	fields: 
	[
	 	{name: 'creator'},
	    {name: 'name'},
		{name: 'number'},
		{name: 'createDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
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
ETDDocR2.cm = new Ext.grid.ColumnModel([
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
		id:'expand',
		header: ETD.headers[2], 
		width: 240, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},/*{
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
	},*//*{
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
		hidden:true
	},{
		header:'cCreateSource',
		dataIndex:'cCreateSource',
		hidden:true
	}*/]);

//ETDDoc.cm.defaultSortable = true;

ETDDocR2.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocR2.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_DOCUMENT_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Новые',
	loadMask : false,
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDocR2.sm,
	enableColumnHide: false,
	enableColumnMove: false,
	//plugins: ETDDoc.filters,
	cm : ETDDocR2.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocR2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDocR2.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	loadFirstPage : function()
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage}});
	}
    });

ETDDocR2.addRequestParameter = function(name, value){
	ETDDocR2.store.baseParams[name] = value;
};

ETDDocR2.removeRequestParameter = function(name){
	ETDDocR2.store.baseParams[name] = null;
};

ETDDocR2.setRequestParameter = function(name, value){
	ETDDocR2.store.baseParams[name] = value;
};

ETDDocR2.setFormType = function(formType){
	ETDDocR2.setRequestParameter('formType', formType);
};

ETDDocR2.reload = function(){
	this.store.reload();
};
	
ETDDocR2.load = function(params)
{
	alert("!!");
	var req = ETD.convertRequestParams(params);
	applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	alert("!");
	ret = eval('('+applet_adapter.getDocuments(req)+')');
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
	ETDDocR2.grid.isLoaded = true;
	ETDDocR2.grid.count = ret.totalCount || 0;
	show_news();
	//Информация о количестве документов 
	total_num.setText(ret.totalCount || 0);
	return ret;
};

ETDDocR2.grid.on({
	rowdblclick : {fn : function(grid, rowIndex, e)
	{
		var row = grid.getSelectionModel().getSelected();
		ETD.openDocument(row.get('id'), row.get('name'));
	}}
});
ETDDocR2.sm.on({
	rowdeselect : { fn : function()
	{
		Ext.getCmp('drop').disable();
	}},
	rowselect : { fn : function(sm, num, r)
	{
		if (r.get('cDel')=='1') Ext.getCmp('drop').enable(); 
		else Ext.getCmp('drop').disable();
	}}

});




	