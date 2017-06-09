Ext.namespace('ETDSendRTK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSendRTK.load = null;


ETDSendRTK.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSendRTK.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSendRTK.proxy = new Ext.data.DataProxy({});
ETDSendRTK.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSendRTK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSendRTK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSendRTK.proxy,
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
		{name: 'packst'}
	]
});
ETDSendRTK.store.setDefaultSort('vagnum', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDSendRTK.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSendRTK.cm = new Ext.grid.ColumnModel([

{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},
	{
		id:'expand',
		header: ETD.headers[12],
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'content'
	},
	{
		header: ETD.headers[15],
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'dognum'
		
	},
	 {	header: 'Пакет',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
		
	},

   {
		
		header: 'Дата создания', 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},
	{
		header: ETD.headers[11], 
		sortable: true,
		dataIndex: 'lastSigner'
	},
	{
		header: ETD.headers[14], 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'lastDate'
	},
	{
		header: 'Статус',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	}
	]);

ETDSendRTK.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSendRTK.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_SEND_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Документы в работе',
	loadMask : false,
viewConfig: {
    forceFit: true
},
	sm: new Ext.grid.RowSelectionModel({singleSelect:false}),
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSendRTK.filters,
	cm : ETDSendRTK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSendRTK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSendRTK.store,
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
	if (row.get('name')!='Пакет документов РТК'){
		ETD.dropPackRTKid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}
	
else if (row.get('name')=='Пакет документов РТК'){

ETD.setPackRTKid(row.get('id'));
PackRTK.req = row.get('id');
PackRTK.mkPackRTKwin(row.get('id'));

}
	
}
}}
}
    });

ETDSendRTK.addRequestParameter = function(name, value){
	ETDSendRTK.store.baseParams[name] = value;
};

ETDSendRTK.removeRequestParameter = function(name){
	ETDSendRTK.store.baseParams[name] = null;
};

ETDSendRTK.setRequestParameter = function(name, value){
	ETDSendRTK.store.baseParams[name] = value;
};

ETDSendRTK.setFormType = function(formType)
{
	ETDSendRTK.setRequestParameter('formType', formType);
};

ETDSendRTK.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSendRTK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDSendRTK.grid.isLoaded = true;
	ETDSendRTK.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('export').enable();
	Ext.getCmp('exportXML').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};




	