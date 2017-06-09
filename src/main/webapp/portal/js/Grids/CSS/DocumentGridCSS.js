Ext.namespace('ETDDocCSS');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocCSS.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocCSS.proxy = new Ext.data.DataProxy({});
ETDDocCSS.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocCSS.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocCSS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocCSS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocCSS.proxy,
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

ETDDocCSS.store.setDefaultSort('vagnum', 'DESC');
ETDDocCSS.cm = new Ext.grid.ColumnModel([
{
		id:'name',
		header: ETD.headers[10], 
		//width: 220, 
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
		//id:'expand',
		header: 'Пакет',
		//width: 170, 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
	},
   {
		
		header: ETD.headers[13], 
		//width: 120, 
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
	}
	]);

//ETDDocCSS.cm.defaultSortable = true;

ETDDocCSS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocCSS.grid = new Ext.grid.GridPanel({
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
	//sm : ETDDocCSS.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocCSS.filters,
	cm : ETDDocCSS.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocCSS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocCSS.store,
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
	if (row.get('name').indexOf('ФПУ-26 ППС')>-1&&row.get('packst')=='Забраковано'){
		ETD.openDocumentFPU26PPS(row.get('id'), row.get('name'),null,0);
		}
	else if (row.get('name')=='Пакет документов'){
		ETD.firstpack(row.get('id'));
	}
	
	
ETD.openDocument(row.get('id'), row.get('name'),null,0);
}
}},

rowclick : {fn : function(grid, rowIndex, e){

		var row = grid.getSelectionModel().getSelected();
var status=row.get('status');
var statid = row.get('id');
if (status>-1)
{
var mess = document.applet_adapter.getDocStatus(statid);
if (mess.split('&&')[1].length>0){
	ETD.openStatusDoc(statid, row.get('name'), mess.split('&&')[1]);
	ETD.setTransPackid(statid);
//Ext.ux.prompt.confirm('Внимание!','Документ '+mess.split('&&')[0]+'<br/><br/>'+mess.split('&&')[1],function(button){
//if(button=='yes'){
//if (row.get('name')!='Пакет документов'){
//		ETD.dropPackid();
//		ETD.openDocument(row.get('id'), row.get('name'),null,0);
//	} else {
//		ETD.openDocument(row.get('id'), row.get('name'),null,0);
//
//		}
//}
//});

}

}

}}
}
	
    });

ETDDocCSS.addRequestParameter = function(name, value){
	ETDDocCSS.store.baseParams[name] = value;
};

ETDDocCSS.removeRequestParameter = function(name){
	ETDDocCSS.store.baseParams[name] = null;
};

ETDDocCSS.setRequestParameter = function(name, value){
	ETDDocCSS.store.baseParams[name] = value;
};

ETDDocCSS.setFormType = function(formType){
	ETDDocCSS.setRequestParameter('formType', formType);
};

ETDDocCSS.reload = function(){
	
	this.store.reload();
};
	
ETDDocCSS.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocCSS.grid.ppage = (params.start/ETD.docsPerPage)+1;
		
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
	ETDDocCSS.grid.isLoaded = true;
	
	ETDDocCSS.grid.count = ret.totalCount || 0;
	
	show_news();
	
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();

	return ret;
};



	