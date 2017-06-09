Ext.namespace('ETDSendCSS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSendCSS.load = null;


ETDSendCSS.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSendCSS.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSendCSS.proxy = new Ext.data.DataProxy({});
ETDSendCSS.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSendCSS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSendCSS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSendCSS.proxy,
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
ETDSendCSS.store.setDefaultSort('vagnum', 'DESC');

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSendCSS.cm = new Ext.grid.ColumnModel([
{
	id:'name',
	header: ETD.headers[10], 
	//width: 220, 
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

ETDSendCSS.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSendCSS.grid = new Ext.grid.GridPanel({
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
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSendCSS.filters,
	cm : ETDSendCSS.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSendCSS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSendCSS.store,
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
}},

rowclick : {fn : function(grid, rowIndex, e){

		var row = grid.getSelectionModel().getSelected();

var status=row.get('status');

if (status==1||status==2||status==3)
{
var mess = document.applet_adapter.getDocStatus(row.get('id'));
if (mess.split('&&')[1].length>0){
Ext.ux.prompt.confirm('Внимание!','Документ '+mess.split('&&')[0]+'<br/><br/>'+mess.split('&&')[1],function(button){
if(button=='yes'){
if (row.get('name')!='Пакет документов' && row.get('name') !='Пакет документов ЦСС'){
		ETD.dropPackid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	} else {
		if(row.get('name') =='Пакет документов' ) {
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
});
}
}

}}
}
    });

ETDSendCSS.addRequestParameter = function(name, value){
	ETDSendCSS.store.baseParams[name] = value;
};

ETDSendCSS.removeRequestParameter = function(name){
	ETDSendCSS.store.baseParams[name] = null;
};

ETDSendCSS.setRequestParameter = function(name, value){
	ETDSendCSS.store.baseParams[name] = value;
};

ETDSendCSS.setFormType = function(formType)
{
	ETDSendCSS.setRequestParameter('formType', formType);
};

ETDSendCSS.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSendCSS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDSendCSS.grid.isLoaded = true;
	ETDSendCSS.grid.count = ret.totalCount || 0;
	show_news();
	if (ETD.fr==1 || ETD.fr==17){
		Ext.getCmp('drop').enable();
	} else {
	Ext.getCmp('drop').disable();
	}
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('acceptDocs').hide();
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	return ret;
};




	