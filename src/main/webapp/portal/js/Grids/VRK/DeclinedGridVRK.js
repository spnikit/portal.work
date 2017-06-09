Ext.namespace('ETDDeclVRK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDeclVRK.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDeclVRK.proxy = new Ext.data.DataProxy({});
ETDDeclVRK.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDDeclVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDeclVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDeclVRK.proxy,
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
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});

ETDDeclVRK.store.setDefaultSort('vagnum', 'DESC');

ETDDeclVRK.cm = new Ext.grid.ColumnModel([

{
		id:'expand',
		header: ETD.headers[10], 
	//	width: 220, 
		sortable: true,
		dataIndex: 'name'
	},


//	 {id:'expand',
//		header: ETD.headers[12],
//		//width: 200,  
//		sortable: true,
//	 renderer: ETD.shortDescriptionRender,
//		dataIndex: 'content'
//	},
	
   {
	//id:'expand',
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
	{//id:'expand',
		header: ETD.headers[14], 
		width: 200, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'lastDate'
	}
	
	]);

//ETDDoc.cm.defaultSortable = true;

ETDDeclVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDeclVRK.grid = new Ext.grid.GridPanel({
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
	sm : ETDDeclVRK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDeclVRK.filters,
	cm : ETDDeclVRK.cm,
	
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDeclVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDeclVRK.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			
//		alert('ppage: '+ppage);
			this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
			this.ppage = num;
			
		},
	loadFirstPage : function(str)
	{
		
		this.store.load({params :{start : 0, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
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

ETDDeclVRK.addRequestParameter = function(name, value){
	ETDDeclVRK.store.baseParams[name] = value;
};

ETDDeclVRK.removeRequestParameter = function(name){
	ETDDeclVRK.store.baseParams[name] = null;
};

ETDDeclVRK.setRequestParameter = function(name, value){
	ETDDeclVRK.store.baseParams[name] = value;
};

ETDDeclVRK.setFormType = function(formType){
	ETDDeclVRK.setRequestParameter('formType', formType);
};

ETDDeclVRK.reload = function(){
	this.store.reload();
};
	
ETDDeclVRK.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	ETDDeclVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDeclVRK.grid.isLoaded = true;
	ETDDeclVRK.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();

			Ext.getCmp('exportdoc').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};
