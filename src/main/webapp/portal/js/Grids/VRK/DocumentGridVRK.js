Ext.namespace('ETDDocVRK');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocVRK.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocVRK.proxy = new Ext.data.DataProxy({});
ETDDocVRK.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocVRK.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocVRK.proxy,
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
		{name: 'packst'},
		{name: 'servicetype'},
		{name: 'price'},
		{name: 'otcname'},
		{name: 'otctype'},
		{name: 'sftype'}
	]
});

ETDDocVRK.store.setDefaultSort('vagnum', 'DESC');
ETDDocVRK.cm = new Ext.grid.ColumnModel([

{
		id:'expand',
//		width: 300,  
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},

//	 {id:'expand',
//		header: ETD.headers[12],
//		//width: 200,  
//		sortable: true,
//	 renderer: ETD.shortDescriptionRender,
//	 dataIndex: 'content'
//		
//	},
	
	
   {
		
		header: ETD.headers[13], 
		width: 200, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	}
	]);

//ETDDocVRK.cm.defaultSortable = true;

ETDDocVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocVRK.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_DOCUMENT_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Текущие',
	loadMask : false,
	sm : ETDDocVRK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocVRK.filters,
	cm : ETDDocVRK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocVRK.store,
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
	
ETD.openDocument(row.get('id'), row.get('name'),null,0);
}
}}}
 });

ETDDocVRK.addRequestParameter = function(name, value){
	ETDDocVRK.store.baseParams[name] = value;
};

ETDDocVRK.removeRequestParameter = function(name){
	ETDDocVRK.store.baseParams[name] = null;
};

ETDDocVRK.setRequestParameter = function(name, value){
	ETDDocVRK.store.baseParams[name] = value;
};

ETDDocVRK.setFormType = function(formType){
	ETDDocVRK.setRequestParameter('formType', formType);
};

ETDDocVRK.reload = function(){
	
	this.store.reload();
};
	
ETDDocVRK.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
		
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
	ETDDocVRK.grid.isLoaded = true;
	
	ETDDocVRK.grid.count = ret.totalCount || 0;
	
	show_news();
	
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();

	
	return ret;
};



	