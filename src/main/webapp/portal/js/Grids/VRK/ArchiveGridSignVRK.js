Ext.namespace('ArchiveGridSignVRK');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ArchiveGridSignVRK.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ArchiveGridSignVRK.proxy = new Ext.data.DataProxy({});
ArchiveGridSignVRK.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ArchiveGridSignVRK.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ArchiveGridSignVRK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ArchiveGridSignVRK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ArchiveGridSignVRK.proxy,
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

ArchiveGridSignVRK.store.setDefaultSort('vagnum', 'DESC');
ArchiveGridSignVRK.cm = new Ext.grid.ColumnModel([

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

//ArchiveGridSignVRK.cm.defaultSortable = true;

ArchiveGridSignVRK.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ArchiveGridSignVRK.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_ARCHIVE_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Архив',
	loadMask : false,
	sm : ArchiveGridSignVRK.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ArchiveGridSignVRK.filters,
	cm : ArchiveGridSignVRK.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ArchiveGridSignVRK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ArchiveGridSignVRK.store,
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

ArchiveGridSignVRK.addRequestParameter = function(name, value){
	ArchiveGridSignVRK.store.baseParams[name] = value;
};

ArchiveGridSignVRK.removeRequestParameter = function(name){
	ArchiveGridSignVRK.store.baseParams[name] = null;
};

ArchiveGridSignVRK.setRequestParameter = function(name, value){
	ArchiveGridSignVRK.store.baseParams[name] = value;
};

ArchiveGridSignVRK.setFormType = function(formType){
	ArchiveGridSignVRK.setRequestParameter('formType', formType);
};

ArchiveGridSignVRK.reload = function(){
	
	this.store.reload();
};
	
ArchiveGridSignVRK.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ArchiveGridSignVRK.grid.ppage = (params.start/ETD.docsPerPage)+1;
		
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
	ArchiveGridSignVRK.grid.isLoaded = true;
	
	ArchiveGridSignVRK.grid.count = ret.totalCount || 0;
	Ext.getCmp('packsgn').disable();
	Ext.getCmp('drop').disable();
	show_news();
	
	return ret;
};



	