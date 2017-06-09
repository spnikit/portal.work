Ext.namespace('ETDArchCSS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchCSS.load = null;

ETDArchCSS.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchCSS.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchCSS.proxy = new Ext.data.DataProxy({});
ETDArchCSS.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		var records = reader.readRecords(ETDArchCSS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchCSS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchCSS.proxy,
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
ETDArchCSS.store.setDefaultSort('vagnum', 'DESC');
ETDArchCSS.cm = new Ext.grid.ColumnModel([

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
	renderer : ETD.shortDescriptionRender,
	dataIndex: 'content'
},
{
	header: 'Пакет',
	sortable: true,
	renderer : ETD.shortDescriptionRender,
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
	}
,{
	//id:'expand',
	header: 'Вид СФ',
	//width: 170, 
	sortable: true,
	dataIndex: 'sftype'
}

]);

ETDArchCSS.cm.defaultSortable = true;

ETDArchCSS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchCSS.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_ARCHIVE_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Архив',
	loadMask : false,
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDArchCSS.filters,
	cm : ETDArchCSS.cm,
	sm: ETDArchCSS.sm,
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
			store: ETDArchCSS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchCSS.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
			
		},
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage, dir:'DESC', shift: shParam}});
	},

listeners: {
dblclick : {fn : function(e){
	if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();
	//ETD.openDocument(row.get('id'), row.get('name'),null,0);
	if (row.get('name')!='Пакет документов' && row.get('name') !='Пакет документов ЦСС'){
		ETD.dropPackid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}
else {
	if(row.get('name') =='Пакет документов') {
		ETD.setPackid(row.get('id'));
		Pack.req = row.get('id');
		Pack.mkpackwin(row.get('id'));
	} else if(row.get('name') =='Пакет документов ЦСС'){
		ETD.setPackCSSid(row.get('id'));
		PackCSS.req = row.get('id');
		PackCSS.mkpackwin(row.get('id'));
	}
}
}
}}

}
    });

ETDArchCSS.addRequestParameter = function(name, value){
	ETDArchCSS.store.baseParams[name] = value;
};

ETDArchCSS.removeRequestParameter = function(name){
	ETDArchCSS.store.baseParams[name] = null;
};

ETDArchCSS.setRequestParameter = function(name, value){
	ETDArchCSS.store.baseParams[name] = value;
};

ETDArchCSS.setFormType = function(formType)
{
	ETDArchCSS.setRequestParameter('formType', formType);
};

ETDArchCSS.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchCSS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchCSS.grid.isLoaded = true;
	ETDArchCSS.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').enable();
//	Ext.getCmp('utoch').disable();
	
	
	if (ETD.expdoc){
		Ext.getCmp('exportdoc').show();
	}
	else {
		Ext.getCmp('exportdoc').hide();
	}
	Ext.getCmp('acceptDocs').hide();

	
	return ret;
};
