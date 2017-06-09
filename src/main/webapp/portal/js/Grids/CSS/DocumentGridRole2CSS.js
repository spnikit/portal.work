Ext.namespace('ETDDocR2CSS');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocR2CSS.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocR2CSS.proxy = new Ext.data.DataProxy({});
ETDDocR2CSS.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocR2CSS.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		
		var records = reader.readRecords(ETDDocR2CSS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocR2CSS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocR2CSS.proxy,
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

ETDDocR2CSS.store.setDefaultSort('vagnum', 'DESC');
ETDDocR2CSS.cm = new Ext.grid.ColumnModel([
   {
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},
	
	{
		header: 'Наименование услуги', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'servicetype'
	},
	 {id:'expand',
		header: ETD.headers[12],
		//width: 200,  
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
		//id:'expand',
		header: 'Вид СФ',
		//width: 170, 
		sortable: true,
		dataIndex: 'sftype'
	}
	]);

//ETDDocR2CSS.cm.defaultSortable = true;

ETDDocR2CSS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocR2CSS.grid = new Ext.grid.GridPanel({
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
	//sm : ETDDocR2CSS.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocR2CSS.filters,
	cm : ETDDocR2CSS.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocR2CSS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),

	
	store: ETDDocR2CSS.store,
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
		if (row.get('name').indexOf('ФПУ-26 ППС')>-1&&row.get('packst')=='Забраковано'&&ETD.fr==3){
		ETD.openDocumentFPU26PPS(row.get('id'), row.get('name'),null,0);
		}
		
		else if (row.get('name')!='Пакет документов'&&row.get('name')!='Пакет документов РТК' 
			&&row.get('name')!='Пакет документов ЦСС'){
			ETD.dropPackid();
			ETD.openDocument(row.get('id'), row.get('name'),null,0);
		}
else if (row.get('name')=='Пакет документов'){
	ETD.firstpack(row.get('id'));
	ETD.setPackid(row.get('id'));
	Pack.req = row.get('id');
	Pack.mkpackwin(row.get('id'));
	
	}
		
else if (row.get('name')=='Пакет документов РТК'){
	ETD.setPackRTKid(row.get('id'));
	PackRTK.req = row.get('id');
	PackRTK.mkPackRTKwin(row.get('id'));
	
	}
else if (row.get('name')=='Пакет документов ЦСС'){
	ETD.setPackCSSid(row.get('id'));
	PackCSS.req = row.get('id');
	PackCSS.mkpackwin(row.get('id'));
	}
}
	}},
	
	rowclick : {fn : function(grid, rowIndex, e){
	
			var row = grid.getSelectionModel().getSelected();
	
var status=row.get('status');

if (status==1||status==2||status==3)
{
	var mess = document.applet_adapter.getDocStatus(row.get('id'));
	Ext.ux.prompt.confirm('Внимание!','Документ '+mess.split('&&')[0]+'<br/><br/>'+mess.split('&&')[1],function(button){
if(button=='yes'){
	if (row.get('name')!='Пакет документов' && row.get('name')!='Пакет документов ЦСС'){
			ETD.dropPackid();
			ETD.openDocument(row.get('id'), row.get('name'),null,0);
		} else {
			if(row.get('name') == 'Пакет документов') {
				ETD.firstpack(row.get('id'));	
				ETD.setPackid(row.get('id'));
				Pack.req = row.get('id');
				Pack.mkpackwin(row.get('id'));
			} else if(row.get('name') == 'Пакет документов ЦСС')
				ETD.setPackCSSid(row.get('id'));
				PackCSS.req = row.get('id');
				PackCSS.mkpackwin(row.get('id'));
			}
}
});
}
	
}}
}
 });

ETDDocR2CSS.addRequestParameter = function(name, value){
	ETDDocR2CSS.store.baseParams[name] = value;
};

ETDDocR2CSS.removeRequestParameter = function(name){
	ETDDocR2CSS.store.baseParams[name] = null;
};

ETDDocR2CSS.setRequestParameter = function(name, value){
	ETDDocR2CSS.store.baseParams[name] = value;
};

ETDDocR2CSS.setFormType = function(formType){
	ETDDocR2CSS.setRequestParameter('formType', formType);
};

ETDDocR2CSS.reload = function(){
	
	this.store.reload();
};
	
ETDDocR2CSS.load = function(params)
{
console.log('params: '+params);
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);

	ETDDocR2CSS.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocR2CSS.grid.isLoaded = true;
	
	ETDDocR2CSS.grid.count = ret.totalCount || 0;
	
	show_news();
	
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('acceptDocs').show();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};


	