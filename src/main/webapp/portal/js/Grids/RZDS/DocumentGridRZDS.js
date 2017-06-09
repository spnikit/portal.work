Ext.namespace('ETDDocRZDS');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocRZDS.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocRZDS.proxy = new Ext.data.DataProxy({});
ETDDocRZDS.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocRZDS.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocRZDS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocRZDS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocRZDS.proxy,
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

ETDDocRZDS.store.setDefaultSort('idpak', 'DESC');
ETDDocRZDS.cm = new Ext.grid.ColumnModel([
                                         
{	header: 'Номер комплекта',
	sortable: true,
	renderer: ETD.shortDescriptionRender,
	dataIndex: 'idpak'
	
},
{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},
	{
		header: 'Договор',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'dognum'
		
	},
	{
		id:'expand',
		header: 'Номер документа',
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'content'
	},
	
	 
 {
		header: 'Дата создания', 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	}
//	,{
//		header: 'Статус',
//		sortable: true,
//		renderer: ETD.shortDescriptionRender,
//		dataIndex: 'packst'
//	}
	]);

//ETDDocRZDS.cm.defaultSortable = true;

ETDDocRZDS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocRZDS.grid = new Ext.grid.GridPanel({
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
	sm : ETDDocRZDS.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocRZDS.filters,
	cm : ETDDocRZDS.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocRZDS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocRZDS.store,
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
	}}
}
	
	
    });

ETDDocRZDS.addRequestParameter = function(name, value){
	ETDDocRZDS.store.baseParams[name] = value;
};

ETDDocRZDS.removeRequestParameter = function(name){
	ETDDocRZDS.store.baseParams[name] = null;
};

ETDDocRZDS.setRequestParameter = function(name, value){
	ETDDocRZDS.store.baseParams[name] = value;
};

ETDDocRZDS.setFormType = function(formType){
	ETDDocRZDS.setRequestParameter('formType', formType);
};

ETDDocRZDS.reload = function(){
	
	this.store.reload();
};
	
ETDDocRZDS.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocRZDS.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocRZDS.grid.isLoaded = true;
	
	ETDDocRZDS.grid.count = ret.totalCount || 0;
	
	show_news();
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').disable();
	
//	ETDDocRZDS.cm.setHidden(0, true);
	
//	alert(ETDDocRZDS.cm.getColumnHeader(0));
//	alert(ETDDocRZDS.cm.getColumnId(0));
//	alert(ETDDocRZDS.cm.findColumnIndex('vagnum'));
	
	
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};



	