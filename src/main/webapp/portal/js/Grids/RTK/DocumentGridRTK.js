Ext.namespace('ETDDocRTK');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocRTK.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocRTK.proxy = new Ext.data.DataProxy({});
ETDDocRTK.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocRTK.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocRTK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocRTK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocRTK.proxy,
	remoteSort: true,
	fields: 
	[
	 	
	    {name: 'name'},
	    {name: 'content'},
	    {name: 'dognum'},
	    {name: 'idpak'},
	    {name: 'creator'},		
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

ETDDocRTK.store.setDefaultSort('idpak', 'DESC');
ETDDocRTK.cm = new Ext.grid.ColumnModel([

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
		header: 'Статус',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	}
	]);

//ETDDocRTK.cm.defaultSortable = true;

ETDDocRTK.sm = new Ext.grid.RowSelectionModel({singleSelect:false});
//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocRTK.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_DOCUMENT_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Текущие',
	loadMask : false,
viewConfig: {
    forceFit: true
},
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDocRTK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocRTK.filters,
	cm : ETDDocRTK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocRTK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocRTK.store,
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
		
	},

listeners: {
	dblclick : {fn : function(e){
		if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
		var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();
//		ETD.openDocument(row.get('id'), row.get('name'),null,0);
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

ETDDocRTK.addRequestParameter = function(name, value){
	ETDDocRTK.store.baseParams[name] = value;
};

ETDDocRTK.removeRequestParameter = function(name){
	ETDDocRTK.store.baseParams[name] = null;
};

ETDDocRTK.setRequestParameter = function(name, value){
	ETDDocRTK.store.baseParams[name] = value;
};

ETDDocRTK.setFormType = function(formType){
	ETDDocRTK.setRequestParameter('formType', formType);
};

ETDDocRTK.reload = function(){
	
	this.store.reload();
};
	
ETDDocRTK.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocRTK.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocRTK.grid.isLoaded = true;
	
	ETDDocRTK.grid.count = ret.totalCount || 0;
	
	show_news();
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('export').disable();
	Ext.getCmp('exportXML').hide();
//	ETDDocRTK.cm.setHidden(0, true);
	
//	alert(ETDDocRTK.cm.getColumnHeader(0));
//	alert(ETDDocRTK.cm.getColumnId(0));
//	alert(ETDDocRTK.cm.findColumnIndex('vagnum'));
	
	
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};



	