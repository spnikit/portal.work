Ext.namespace('ETDSendVRK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSendVRK.load = null;


ETDSendVRK.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSendVRK.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSendVRK.proxy = new Ext.data.DataProxy({});
ETDSendVRK.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSendVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSendVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSendVRK.proxy,
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
ETDSendVRK.store.setDefaultSort('vagnum', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDSendVRK.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSendVRK.cm = new Ext.grid.ColumnModel([

{
	id:'expand',
	header: ETD.headers[10], 
	//width: 220, 
	sortable: true,
	dataIndex: 'name'
},

// {id:'expand',
//	header: ETD.headers[12],
//	//width: 200,  
//	sortable: true,
// renderer: ETD.shortDescriptionRender,
//	dataIndex: 'content'
//},


{
//	id:'expand',
	header: ETD.headers[13], 
	width: 200, 
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'createDate'
},
{
	header: ETD.headers[11], 
	width: 200, 
	sortable: true,
	dataIndex: 'lastSigner'
},
{
	header: ETD.headers[14], 
	width: 200, 
	sortable: true,
	renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
	dataIndex: 'lastDate'
}

]);

ETDSendVRK.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSendVRK.grid = new Ext.grid.GridPanel({
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
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSendVRK.filters,
	cm : ETDSendVRK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSendVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSendVRK.store,
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
	ETD.openDocument(row.get('id'), row.get('name'),null,0);

}
}}
}
});

ETDSendVRK.addRequestParameter = function(name, value){
	ETDSendVRK.store.baseParams[name] = value;
};

ETDSendVRK.removeRequestParameter = function(name){
	ETDSendVRK.store.baseParams[name] = null;
};

ETDSendVRK.setRequestParameter = function(name, value){
	ETDSendVRK.store.baseParams[name] = value;
};

ETDSendVRK.setFormType = function(formType)
{
	ETDSendVRK.setRequestParameter('formType', formType);
};

ETDSendVRK.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSendVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDSendVRK.grid.isLoaded = true;
	ETDSendVRK.grid.count = ret.totalCount || 0;
	show_news();
	if (ETD.fr==1){
		Ext.getCmp('drop').enable();
	} else {
	Ext.getCmp('drop').disable();
	}
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};




	