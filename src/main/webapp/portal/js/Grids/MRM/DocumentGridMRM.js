Ext.namespace('ETDDocMRM');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocMRM.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocMRM.proxy = new Ext.data.DataProxy({});
ETDDocMRM.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocMRM.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		///*ETD.*/busy();
		var records = reader.readRecords(ETDDocMRM.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocMRM.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocMRM.proxy,
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
		{name: 'etdid', type: 'int'}
	]
});

ETDDocMRM.store.setDefaultSort('vagnum', 'DESC');

ETDDocMRM.cm = new Ext.grid.ColumnModel([
{
id:'name',
header: ETD.headers[10], 
sortable: true,
dataIndex: 'name',
tdCls: 'test'
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
		
		header: ETD.headers[13], 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
	},
	{
		header: 'ID', 
		sortable: true,
		dataIndex: 'etdid'
	}
]);


ETDDocMRM.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocMRM.grid = new Ext.grid.GridPanel({
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
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDocMRM.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocMRM.filters,
	cm : ETDDocMRM.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocMRM.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocMRM.store,
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
}},

rowclick : {fn : function(grid, rowIndex, e){

		var row = grid.getSelectionModel().getSelected();

var status=row.get('status');

if (status!=-1) {
var mess = document.applet_adapter.getDocStatus(row.get('id'));
if (mess.split('&&')[1].length>0){
	mess = mess.split('&&')[1];
	Ext.Msg.show({
		title:'<div style="margin-left:15px;text-align:center;">Внимание</div>',
		msg:'<div style="margin-bottom:-8px;top:6px;text-align:center;">'+mess +'</div>',
		buttons :{ ok: "Продолжить работу", no: "Отклонить документ", cancel: "Отмена" },
		animEl: 'elId',
		fn:function(button, text){

			if (button=='ok'){
				ETD.openDocument(row.get('id'), row.get('name'),null,0);        		      
			}
			if (button=='no'){							
				dropdoc(false);

			}

		}
	});
}
}
if (Ext.getCmp('drop').disabled) {
	Ext.getCmp('drop').enable();
}

if (row.get('name')=='ГУ-23'&&ETD.fr==5 || row.get('name')=='ГУ-45'&&ETD.fr==5){
	Ext.getCmp('drop').disable();
}


}}
}
	
    });

ETDDocMRM.addRequestParameter = function(name, value){
	ETDDocMRM.store.baseParams[name] = value;
};

ETDDocMRM.removeRequestParameter = function(name){
	ETDDocMRM.store.baseParams[name] = null;
};

ETDDocMRM.setRequestParameter = function(name, value){
	ETDDocMRM.store.baseParams[name] = value;
};

ETDDocMRM.setFormType = function(formType){
	ETDDocMRM.setRequestParameter('formType', formType);
};

ETDDocMRM.reload = function(){
	
	this.store.reload();
};
	
ETDDocMRM.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocMRM.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocMRM.grid.isLoaded = true;
	
	ETDDocMRM.grid.count = ret.totalCount || 0;
	
	show_news();
	Ext.getCmp('drop').enable();

	return ret;
};



	