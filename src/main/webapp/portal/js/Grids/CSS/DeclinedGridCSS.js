Ext.namespace('ETDDeclCSS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDeclCSS.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDeclCSS.proxy = new Ext.data.DataProxy({});
ETDDeclCSS.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDeclCSS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDeclCSS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDeclCSS.proxy,
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
	]
});

ETDDeclCSS.store.setDefaultSort('vagnum', 'DESC');
//ETDDoc.store.setDefaultSort('createDate', 'DESC');


//	Column model /////////////////////////////////////////////////////////////////////////////
ETDDeclCSS.cm = new Ext.grid.ColumnModel([
	{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name'
	},
	{
		
		header: 'Наименование услуги', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'servicetype'
	},
	 {id:'expand',
		header: ETD.headers[12],
		sortable: true,
	 renderer: ETD.shortDescriptionRender,
		dataIndex: 'content'
	},
	{
		
		header: 'Пакет',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
	},
   {
		header: ETD.headers[13], 
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
		header: 'Стоимость',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'price'
	},
	{
		header: 'Статус',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	},
	{
		header: 'Вид СФ',
		sortable: true,
		dataIndex: 'sftype'
	}
	]);


ETDDeclCSS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDeclCSS.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_FIN_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Отклоненные',
	loadMask : false,
	enableColumnHide: true,
	enableColumnMove: true,
	cm : ETDDeclCSS.cm,
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
			store: ETDDeclCSS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDeclCSS.store,
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
	if (row.get('name')!='Пакет документов' && row.get('name')!='Пакет документов ЦСС'){
		ETD.dropPackid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}
else {
	ETD.openDocument(row.get('id'), row.get('name'),null,0);
}
}
}}
}
	
    });

ETDDeclCSS.addRequestParameter = function(name, value){
	ETDDeclCSS.store.baseParams[name] = value;
};

ETDDeclCSS.removeRequestParameter = function(name){
	ETDDeclCSS.store.baseParams[name] = null;
};

ETDDeclCSS.setRequestParameter = function(name, value){
	ETDDeclCSS.store.baseParams[name] = value;
};

ETDDeclCSS.setFormType = function(formType){
	ETDDeclCSS.setRequestParameter('formType', formType);
};

ETDDeclCSS.reload = function(){
	this.store.reload();
};
	
ETDDeclCSS.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	ETDDeclCSS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDeclCSS.grid.isLoaded = true;
	ETDDeclCSS.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('acceptDocs').hide();
	return ret;
};
