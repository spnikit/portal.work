Ext.namespace('ETDDeclSignVRK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDeclSignVRK.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDeclSignVRK.proxy = new Ext.data.DataProxy({});
ETDDeclSignVRK.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDDeclSignVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDeclSignVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDeclSignVRK.proxy,
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

ETDDeclSignVRK.store.setDefaultSort('vagnum', 'DESC');

ETDDeclSignVRK.cm = new Ext.grid.ColumnModel([

{
		id:'expand',
		header: ETD.headers[10], 
	//	width: 220, 
		sortable: true,
		dataIndex: 'name'
	},
	{
		//id:'expand',
		header: 'Пакет',
		width: 170, 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
	},
	
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
	},
	{
//		 id:'expand',
				header: ETD.headers[12],
				width: 200,  
				sortable: true,
			 renderer: ETD.shortDescriptionRender,
			 dataIndex: 'content'
				
			}
	]);

//ETDDoc.cm.defaultSortable = true;

ETDDeclSignVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDeclSignVRK.grid = new Ext.grid.GridPanel({
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
	sm : ETDDeclSignVRK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDeclSignVRK.filters,
	cm : ETDDeclSignVRK.cm,
	
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDeclSignVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDeclSignVRK.store,
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
	
//		ETD.openDocument(row.get('id'), row.get('name'),null,0);

}
}}

}
	
    });

ETDDeclSignVRK.addRequestParameter = function(name, value){
	ETDDeclSignVRK.store.baseParams[name] = value;
};

ETDDeclSignVRK.removeRequestParameter = function(name){
	ETDDeclSignVRK.store.baseParams[name] = null;
};

ETDDeclSignVRK.setRequestParameter = function(name, value){
	ETDDeclSignVRK.store.baseParams[name] = value;
};

ETDDeclSignVRK.setFormType = function(formType){
	ETDDeclSignVRK.setRequestParameter('formType', formType);
};

ETDDeclSignVRK.reload = function(){
	this.store.reload();
};
	
ETDDeclSignVRK.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	ETDDeclSignVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDeclSignVRK.grid.isLoaded = true;
	ETDDeclSignVRK.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('packsgn').disable();
	Ext.getCmp('drop').disable();
	return ret;
};
