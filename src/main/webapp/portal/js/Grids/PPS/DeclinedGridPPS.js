Ext.namespace('ETDDeclPPS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDeclPPS.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDeclPPS.proxy = new Ext.data.DataProxy({});
ETDDeclPPS.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDDeclPPS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDeclPPS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDeclPPS.proxy,
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
		{name: 'otcname'}
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});

ETDDeclPPS.store.setDefaultSort('vagnum', 'DESC');
//ETDDoc.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDDoc.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}	
	]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDDeclPPS.cm = new Ext.grid.ColumnModel([
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


{
	
	header:'Дата', 
	sortable: true,
	renderer : ETD.shortDescriptionRender,
	dataIndex: 'reqdate'
},
	 {id:'expand',
		header: ETD.headers[12],
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'content'
		
	},

	
	{
		header: 'ППС', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'rem_pred'
	},
	{
		header: 'ВЧДЭ', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'otcname'
	},
	{
	header: 'ДИ', 
	sortable: true,
	renderer: ETD.shortDescriptionRender,
	dataIndex: 'di'
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

//ETDDoc.cm.defaultSortable = true;

ETDDeclPPS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDeclPPS.grid = new Ext.grid.GridPanel({
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
	sm : ETDDeclPPS.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDeclPPS.filters,
	cm : ETDDeclPPS.cm,
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
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDeclPPS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDeclPPS.store,
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
	ETD.openDocument(row.get('id'), row.get('name'),null,0);
	
}
}}

}
	
    });

ETDDeclPPS.addRequestParameter = function(name, value){
	ETDDeclPPS.store.baseParams[name] = value;
};

ETDDeclPPS.removeRequestParameter = function(name){
	ETDDeclPPS.store.baseParams[name] = null;
};

ETDDeclPPS.setRequestParameter = function(name, value){
	ETDDeclPPS.store.baseParams[name] = value;
};

ETDDeclPPS.setFormType = function(formType){
	ETDDeclPPS.setRequestParameter('formType', formType);
};

ETDDeclPPS.reload = function(){
	this.store.reload();
};
	
ETDDeclPPS.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	ETDDeclPPS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDeclPPS.grid.isLoaded = true;
	ETDDeclPPS.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};
