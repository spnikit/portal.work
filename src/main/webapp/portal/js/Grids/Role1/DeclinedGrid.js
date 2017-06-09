Ext.namespace('ETDDecl');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDDecl.load = null;

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDecl.proxy = new Ext.data.DataProxy({});
ETDDecl.proxy.load = function(params, reader, callback, scope, arg)
{
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		///*ETD.*/busy();
		ETD.busy();
		var records = reader.readRecords(ETDDecl.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDecl.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDecl.proxy,
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
		/*{name: 'lastSigner'},
		{name: 'lastDate', type: 'date',dateFormat: 'Y-m-d H:i:s'},
		{name: 'id', type: 'int'},
		{name: 'cDel'},
		{name: 'cCreateSource'},
		{name: 'short'}*/
	]
});

ETDDecl.store.setDefaultSort('vagnum', 'DESC');
//ETDDoc.store.setDefaultSort('createDate', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDDoc.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}	
	]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDDecl.cm = new Ext.grid.ColumnModel([
{
	header: ETD.headers[1],
//	width: 150,  
	sortable: true,
 renderer : ETD.shortDescriptionRender,
	dataIndex: 'vagnum'
},	
{
		id:'name',
		header: ETD.headers[10], 
	//	width: 220, 
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
	//id:'expand',
	header:ETD.headers[16], 
	//width: 120, 
	sortable: true,
	renderer : ETD.shortDescriptionRender,
	dataIndex: 'reqdate'
},
	 {id:'expand',
		header: ETD.headers[12],
		//width: 200,  
		sortable: true,
	 renderer: ETD.shortDescriptionRender,
		dataIndex: 'content'
	},
	{
		header: ETD.headers[15],
	//	width: 200,  
		sortable: true,
	 renderer: ETD.shortDescriptionRender,
		dataIndex: 'dognum'
	},
	{
		header: 'ДИ', 
	//	width: 100, 
		sortable: true,
		dataIndex: 'di'
	},
	{
		header: 'ВЧДЭ', 
	//	width: 120, 
		sortable: true,
	renderer: ETD.shortDescriptionRender,
		dataIndex: 'rem_pred'
	},
	{
		
		header: 'Пакет',
	//	width: 170, 
		sortable: true,
renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
	},
	/*{
		header: ETD.headers[9], 
		width: 120, 
		sortable: true,
		dataIndex: 'creator'
	},*/
   {
	//id:'expand',
		header: ETD.headers[13], 
		//width: 120, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},
	{
		header: ETD.headers[11], 
		//width: 170, 
		sortable: true,
		dataIndex: 'lastSigner'
	},
	{//id:'expand',
		header: ETD.headers[14], 
		//width: 170, 
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
		//id:'expand',
		header: 'Статус',
		//width: 170, 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	},
	{
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

//ETDDoc.cm.defaultSortable = true;

ETDDecl.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDecl.grid = new Ext.grid.GridPanel({
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
	sm : ETDDecl.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDecl.filters,
	cm : ETDDecl.cm,
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
			store: ETDDecl.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDDecl.store,
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
	//ETD.openDocument(row.get('id'), row.get('name'),null,0);
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

ETDDecl.addRequestParameter = function(name, value){
	ETDDecl.store.baseParams[name] = value;
};

ETDDecl.removeRequestParameter = function(name){
	ETDDecl.store.baseParams[name] = null;
};

ETDDecl.setRequestParameter = function(name, value){
	ETDDecl.store.baseParams[name] = value;
};

ETDDecl.setFormType = function(formType){
	ETDDecl.setRequestParameter('formType', formType);
};

ETDDecl.reload = function(){
	this.store.reload();
};
	
ETDDecl.load = function(params)
{
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	//var f = '('+ applet_adapter.getDocuments(req)+')';
	ETDDecl.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDDecl.grid.isLoaded = true;
	ETDDecl.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
//	if (ETD.predname.indexOf('Трансгарант')>-1
//			||ETD.predname.indexOf('НефтеТрансСервис')>-1
//			||ETD.predname.indexOf('ПГК')>-1
//			||ETD.predname.indexOf('Спецэнерготранс')>-1
//			||ETD.predname.indexOf('Трансойл')>-1
//			||ETD.predname.indexOf('ЛитКол')>-1){
//			Ext.getCmp('exportdoc').show();
//		} else 
			Ext.getCmp('exportdoc').hide();
			Ext.getCmp('acceptDocs').hide();
			Ext.getCmp('export').enable();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
			if (ETD.predname.indexOf('АО "ПГК"')>-1){
				Ext.getCmp('reportTOR').show();
			}else {
				Ext.getCmp('reportTOR').hide();
			}	
			Ext.getCmp('createPretension').hide();
			Ext.getCmp('exportXML').hide();
	return ret;
};
