Ext.namespace('ETDDeclRTK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDeclRTK.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDeclRTK.proxy = new Ext.data.DataProxy({});
ETDDeclRTK.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDDeclRTK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDeclRTK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDeclRTK.proxy,
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
		{name: 'droptxt'},
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});

ETDDeclRTK.store.setDefaultSort('vagnum', 'DESC');
//ETDDoc.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDDoc.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}	
	]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDDeclRTK.cm = new Ext.grid.ColumnModel([

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
		header: 'Причина отклонения', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'droptxt'
	},
	{
		header: 'Статус',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	}
	]);

//ETDDoc.cm.defaultSortable = true;

ETDDeclRTK.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDeclRTK.grid = new Ext.grid.GridPanel({
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
	sm : ETDDeclRTK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDeclRTK.filters,
	cm : ETDDeclRTK.cm,
	viewConfig: {
	    forceFit: true
	},
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDeclRTK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDeclRTK.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			
		alert('ppage: '+ppage);
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
//	 if (row.get('name')=='Пакет документов РТК'){
//		
//		ETD.setPackRTKid(row.get('id'));
//		 ETD.openDocument(row.get('id'), row.get('name'),null,0);
//		}
//	
//	 else
		 ETD.openDocument(row.get('id'), row.get('name'),null,0);
	
}
}}

}
	
    });

ETDDeclRTK.addRequestParameter = function(name, value){
	ETDDeclRTK.store.baseParams[name] = value;
};

ETDDeclRTK.removeRequestParameter = function(name){
	ETDDeclRTK.store.baseParams[name] = null;
};

ETDDeclRTK.setRequestParameter = function(name, value){
	ETDDeclRTK.store.baseParams[name] = value;
};

ETDDeclRTK.setFormType = function(formType){
	ETDDeclRTK.setRequestParameter('formType', formType);
};

ETDDeclRTK.reload = function(){
	this.store.reload();
};

ETDDeclRTK.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	ETDDeclRTK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDeclRTK.grid.isLoaded = true;
	ETDDeclRTK.grid.count = ret.totalCount || 0;
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
