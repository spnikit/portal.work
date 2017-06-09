Ext.namespace('ETDArchMRM');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchMRM.load = null;

ETDArchMRM.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchMRM.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchMRM.proxy = new Ext.data.DataProxy({});
ETDArchMRM.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		
		var records = reader.readRecords(ETDArchMRM.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDArchMRM.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchMRM.proxy,
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
		{name: 'etdid', type: 'int'}
	]
});
ETDArchMRM.store.setDefaultSort('vagnum', 'DESC');

ETDArchMRM.cm = new Ext.grid.ColumnModel([
{
id:'name',
header: ETD.headers[10], 
sortable: true,
dataIndex: 'name'
},
{
header: ETD.headers[13], 
sortable: true,
renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
dataIndex: 'createDate'
},
{
	header: 'Номер',
	sortable: true,
 renderer: ETD.shortDescriptionRender,
 dataIndex: 'dognum'
	
},
{id:'expand',
	header: 'Краткое содержание',
	sortable: true,
 renderer: ETD.shortDescriptionRender,
 dataIndex: 'content'
	
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
	header: 'ID', 
	sortable: true,
	dataIndex: 'etdid'
}
 ]);


ETDArchMRM.cm.defaultSortable = true;

ETDArchMRM.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchMRM.grid = new Ext.grid.GridPanel({
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
//	plugins: ETDArchMRM.filters,
	cm : ETDArchMRM.cm,
	sm: ETDArchMRM.sm,
viewConfig: {
    forceFit: true,

// ПАААААААААДСВЕТАЧКА
    getRowClass: function(record, index) {
	
        var c = record.get('status');
        //alert(c); 
        if (c==1) {
            return 'acc-row';
        } else if (c==2) {
     	   return 'dropped-row';
        } else if (c==3) {
            return 'warn-row';
        } else if (c==4) {
     	   return 'income-row';
 	    }
    }
    
},
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArchMRM.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchMRM.store,
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
	if (row.get('name')!='Пакет документов'){
		ETD.dropPackid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}
else {
ETD.setPackid(row.get('id'));
Pack.req = row.get('id');
Pack.mkpackwin(row.get('id'));

}
}
}}/*,

rowclick : {fn : function(grid, rowIndex, e){

		var row = grid.getSelectionModel().getSelected();

var status=row.get('status');

if (status!=-1)
{
var mess = document.applet_adapter.getDocStatus(row.get('id'));
Ext.ux.prompt.confirm('Внимание!','Документ '+mess.split('&&')[0]+'<br/><br/>'+mess.split('&&')[1],function(button){
if(button=='yes'){
if (row.get('name')!='Пакет документов'){
		ETD.dropPackid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	} else {
		ETD.setPackid(row.get('id'));
		Pack.req = row.get('id');
		Pack.mkpackwin(row.get('id'));
		
		}
}
});
}

}}*/
}
    });

ETDArchMRM.addRequestParameter = function(name, value){
	ETDArchMRM.store.baseParams[name] = value;
};

ETDArchMRM.removeRequestParameter = function(name){
	ETDArchMRM.store.baseParams[name] = null;
};

ETDArchMRM.setRequestParameter = function(name, value){
	ETDArchMRM.store.baseParams[name] = value;
};

ETDArchMRM.setFormType = function(formType)
{
	ETDArchMRM.setRequestParameter('formType', formType);
};

ETDArchMRM.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchMRM.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchMRM.grid.isLoaded = true;
	//ETDArchMRM.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};
