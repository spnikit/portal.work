Ext.namespace('ETDSendMRM');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSendMRM.load = null;


ETDSendMRM.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSendMRM.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSendMRM.proxy = new Ext.data.DataProxy({});
ETDSendMRM.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSendMRM.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSendMRM.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSendMRM.proxy,
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
ETDSendMRM.store.setDefaultSort('vagnum', 'DESC');


ETDSendMRM.cm = new Ext.grid.ColumnModel([
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


ETDSendMRM.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSendMRM.grid = new Ext.grid.GridPanel({
    margins: '10 10 10 10',
	id: 'ID_SEND_GRID',
	enableHdMenu: true,
	height:600,
	stripeRows: true,
	autoExpandColumn: 'expand',
	region: 'center',
	title:'Документы в работе',
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
	sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSendMRM.filters,
	cm : ETDSendMRM.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSendMRM.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSendMRM.store,
	//Флаг загрузки данных (если меняем активный таб и loaded=false, то загружаем данные)
	isLoaded : false,
	//Колв-во документов для изменения поля Документов при смене таба
	count : 0,
	ppage : 1,
	loadPage : function(num, str)
		{
			this.store.load({params :{start : (num-1)*ETD.docsPerPage, limit : ETD.docsPerPage,dir:'DESC', shift: shParam}});
			this.ppage = num;
			
		},
	loadFirstPage : function(str)
	{
		this.store.load({params :{start : 0, limit : ETD.docsPerPage, dir:'DESC',shift: shParam}});
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

}}
}
    });

ETDSendMRM.addRequestParameter = function(name, value){
	ETDSendMRM.store.baseParams[name] = value;
};

ETDSendMRM.removeRequestParameter = function(name){
	ETDSendMRM.store.baseParams[name] = null;
};

ETDSendMRM.setRequestParameter = function(name, value){
	ETDSendMRM.store.baseParams[name] = value;
};

ETDSendMRM.setFormType = function(formType)
{
	ETDSendMRM.setRequestParameter('formType', formType);
};

ETDSendMRM.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSendMRM.grid.ppage = (params.start/ETD.docsPerPage)+1;
	ret = eval('('+document.applet_adapter.getSendDocuments(req)+')');
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
	ETDSendMRM.grid.isLoaded = true;
	ETDSendMRM.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};




	