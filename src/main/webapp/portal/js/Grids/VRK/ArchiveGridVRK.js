Ext.namespace('ETDArchVRK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchVRK.load = null;

ETDArchVRK.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchVRK.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchVRK.proxy = new Ext.data.DataProxy({});
ETDArchVRK.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		var records = reader.readRecords(ETDArchVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchVRK.proxy,
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
		{name: 'servicetype'},
		{name: 'price'},
		{name: 'otcname'},
		{name: 'otctype'},
		{name: 'sftype'}
	]
});
ETDArchVRK.store.setDefaultSort('vagnum', 'DESC');
ETDArchVRK.cm = new Ext.grid.ColumnModel([

{
	id:'expand',
	header: ETD.headers[10], 
	sortable: true,
	dataIndex: 'name'
},


// {id:'expand',
//	header: ETD.headers[12],
//	sortable: true,
// renderer : ETD.shortDescriptionRender,
//	dataIndex: 'content'
//},

{
	header: ETD.headers[13], 
	width:200,
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'createDate'
},
{
	header: ETD.headers[11], 
	width:200,
	sortable: true,
	dataIndex: 'lastSigner'
},
{
	header: ETD.headers[14],
	width:200,
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'lastDate'
}

]);

ETDArchVRK.cm.defaultSortable = true;

ETDArchVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchVRK.grid = new Ext.grid.GridPanel({
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
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDArchVRK.filters,
	cm : ETDArchVRK.cm,
	sm: ETDArchVRK.sm,

	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArchVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchVRK.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
			
		},
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage, dir:'DESC', shift: shParam}});
	},

listeners: {
dblclick : {fn : function(e){
	if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();
	ETD.openDocument(row.get('id'), row.get('name'),null,0);
	

}
}}

}
    });

ETDArchVRK.addRequestParameter = function(name, value){
	ETDArchVRK.store.baseParams[name] = value;
};

ETDArchVRK.removeRequestParameter = function(name){
	ETDArchVRK.store.baseParams[name] = null;
};

ETDArchVRK.setRequestParameter = function(name, value){
	ETDArchVRK.store.baseParams[name] = value;
};

ETDArchVRK.setFormType = function(formType)
{
	ETDArchVRK.setRequestParameter('formType', formType);
};

ETDArchVRK.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchVRK.grid.isLoaded = true;
	ETDArchVRK.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').enable();
//	Ext.getCmp('utoch').disable();
	
	
	if (ETD.expdoc){
		Ext.getCmp('exportdoc').show();
	}
	else {
		Ext.getCmp('exportdoc').hide();
	}

	
	
	return ret;
};
