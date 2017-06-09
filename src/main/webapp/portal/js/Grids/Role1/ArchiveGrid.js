Ext.namespace('ETDArch');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArch.load = null;

ETDArch.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArch.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArch.proxy = new Ext.data.DataProxy({});
ETDArch.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		var records = reader.readRecords(ETDArch.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArch.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArch.proxy,
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
		{name: 'sftype'},
		{name: 'numberFpu'},
		{name: 'numberSf'}
	]
});
ETDArch.store.setDefaultSort('vagnum', 'DESC');
ETDArch.cm = new Ext.grid.ColumnModel([
                                       
{
	header: ETD.headers[1],
	sortable: true,
 renderer : ETD.shortDescriptionRender,
	dataIndex: 'vagnum'
},
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
{
	header: 'Код неисправности',
	sortable: true,
	renderer: ETD.shortDescriptionRender,
	dataIndex: 'otcname'
},
{
	header: 'Вид неисправности',
	sortable: true,
	renderer: ETD.shortDescriptionRender,
	dataIndex: 'otctype'
},
{
	header:ETD.headers[16], 
	sortable: true,
	renderer : ETD.shortDescriptionRender,
	dataIndex: 'reqdate'
},
 {id:'expand',
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
{
	header: 'ДИ', 
	sortable: true,
	dataIndex: 'di'
},
{
	header: 'ВЧДЭ', 
	sortable: true,
renderer: ETD.shortDescriptionRender,
	dataIndex: 'rem_pred'
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
	header: 'Стоимость ремонта',
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
},
{
	header: '№ФПУ',
	sortable: true,
	dataIndex: 'numberFpu',
	hidden: true 
},
{
	header: '№СФ',
	sortable: true,
	dataIndex: 'numberSf',
	hidden: true 
}

]);

ETDArch.cm.defaultSortable = true;

ETDArch.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArch.grid = new Ext.grid.GridPanel({
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
//	plugins: ETDArch.filters,
	cm : ETDArch.cm,
	sm: ETDArch.sm,
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
			store: ETDArch.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArch.store,
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

ETDArch.addRequestParameter = function(name, value){
	ETDArch.store.baseParams[name] = value;
};

ETDArch.removeRequestParameter = function(name){
	ETDArch.store.baseParams[name] = null;
};

ETDArch.setRequestParameter = function(name, value){
	ETDArch.store.baseParams[name] = value;
};

ETDArch.setFormType = function(formType)
{
	ETDArch.setRequestParameter('formType', formType);
};

ETDArch.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArch.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArch.grid.isLoaded = true;
	ETDArch.grid.count = ret.totalCount || 0;
	show_news();
	if (ETD.fr==6||ETD.fr==13){
		Ext.getCmp('drop').disable();
		Ext.getCmp('utoch').disable();
	}
	else {Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').enable();
	}
	
	if (ETD.fr == 2||ETD.fr == 3||ETD.fr == 1) {
		Ext.getCmp('exportXML').show();
	}

	
	
	
	
	if (ETD.expdoc){
		Ext.getCmp('exportdoc').show();
	}
	else {
		Ext.getCmp('exportdoc').hide();
	}
	
	Ext.getCmp('export').enable();
	
	if (ETD.predname.indexOf('АО "ПГК"')>-1){
		Ext.getCmp('reportTOR').show();
	}else {
		Ext.getCmp('reportTOR').hide();
	}
	Ext.getCmp('acceptDocs').hide();
	
	if(ETD.fr==2 && rolename == 'Специалист' && ETD.predname.indexOf('АО "ПГК"')>-1) {
		Ext.getCmp('createPretension').show();
	}
	
	
	
	return ret;
};
