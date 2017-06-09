Ext.namespace('ETDDocSignVRK');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocSignVRK.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocSignVRK.proxy = new Ext.data.DataProxy({});
ETDDocSignVRK.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocSignVRK.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocSignVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocSignVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocSignVRK.proxy,
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

ETDDocSignVRK.store.setDefaultSort('vagnum', 'DESC');
ETDDocSignVRK.cm = new Ext.grid.ColumnModel([

{
		id:'expand',
//		width: 300,  
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
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
		
		header: ETD.headers[13], 
		width: 200, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},
	{
//	 id:'expand',
			header: ETD.headers[12],
			width: 200,  
			sortable: true,
		 renderer: ETD.shortDescriptionRender,
		 dataIndex: 'content'
			
		}
	]);

//ETDDocSignVRK.cm.defaultSortable = true;

ETDDocSignVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocSignVRK.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_DOCUMENT_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Текущие',
	loadMask : false,
	sm : ETDDocSignVRK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocSignVRK.filters,
	cm : ETDDocSignVRK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocSignVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocSignVRK.store,
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
//	if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
//	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();
//	
//ETD.openDocument(row.get('id'), row.get('name'),null,0);
//}
}}}
 });

ETDDocSignVRK.addRequestParameter = function(name, value){
	ETDDocSignVRK.store.baseParams[name] = value;
};

ETDDocSignVRK.removeRequestParameter = function(name){
	ETDDocSignVRK.store.baseParams[name] = null;
};

ETDDocSignVRK.setRequestParameter = function(name, value){
	ETDDocSignVRK.store.baseParams[name] = value;
};

ETDDocSignVRK.setFormType = function(formType){
	ETDDocSignVRK.setRequestParameter('formType', formType);
};

ETDDocSignVRK.reload = function(){
	
	this.store.reload();
};
	
ETDDocSignVRK.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocSignVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
		
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
	ETDDocSignVRK.grid.isLoaded = true;
	ETDDocSignVRK.grid.count = ret.totalCount || 0;
	Ext.getCmp('packsgn').enable();
	Ext.getCmp('drop').enable();
	show_news();
	
	return ret;
};



	