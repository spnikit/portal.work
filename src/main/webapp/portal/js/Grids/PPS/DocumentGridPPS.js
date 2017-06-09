Ext.namespace('ETDDocPPS');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocPPS.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS.proxy = new Ext.data.DataProxy({});
ETDDocPPS.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocPPS.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocPPS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocPPS.proxy,
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
		{name: 'otcname'}
	]
});

ETDDocPPS.store.setDefaultSort('vagnum', 'DESC');
ETDDocPPS.cm = new Ext.grid.ColumnModel([
{
	header: 'Номер',
	sortable: true,
    renderer : ETD.shortDescriptionRender,
	dataIndex: 'dognum'
},	
{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},


//{
//	
//	header:ETD.headers[16], 
//	sortable: true,
//	renderer : ETD.shortDescriptionRender,
//	dataIndex: 'reqdate'
//},
	 {id:'expand',
		header: ETD.headers[12],
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'content'
		
	},

	
	{
		header: 'Исполнитель', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'rem_pred'
	},
	
   {
		
		header: 'Дата', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'reqdate'
	}
	]);

//ETDDocPPS.cm.defaultSortable = true;

ETDDocPPS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS.grid = new Ext.grid.GridPanel({
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
	    forceFit: true,

	// ПАААААААААДСВЕТАЧКА
	    getRowClass: function(record, index) {
		
	        var c = record.get('status');
	        //alert(c); 
	       if (c==1)
	            return 'acc-row';
	         else if (c==2)
	       	return 'dropped-row';
	       	  else if (c==3)
	         	return 'warn-row';
	       	 else if (c==4)
	           	return 'income-row';
	    }
	    
	},
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDocPPS.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocPPS.filters,
	cm : ETDDocPPS.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocPPS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocPPS.store,
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

ETDDocPPS.addRequestParameter = function(name, value){
	ETDDocPPS.store.baseParams[name] = value;
};

ETDDocPPS.removeRequestParameter = function(name){
	ETDDocPPS.store.baseParams[name] = null;
};

ETDDocPPS.setRequestParameter = function(name, value){
	ETDDocPPS.store.baseParams[name] = value;
};

ETDDocPPS.setFormType = function(formType){
	ETDDocPPS.setRequestParameter('formType', formType);
};

ETDDocPPS.reload = function(){
	
	this.store.reload();
};
	
ETDDocPPS.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocPPS.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocPPS.grid.isLoaded = true;
	
	ETDDocPPS.grid.count = ret.totalCount || 0;
	
	show_news();
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();
//	ETDDocPPS.cm.setHidden(0, true);
	
//	alert(ETDDocPPS.cm.getColumnHeader(0));
//	alert(ETDDocPPS.cm.getColumnId(0));
//	alert(ETDDocPPS.cm.findColumnIndex('vagnum'));
	
	
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};



	