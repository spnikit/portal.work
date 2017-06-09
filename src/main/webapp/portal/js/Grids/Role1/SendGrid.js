Ext.namespace('ETDSend');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDSend.load = null;


ETDSend.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDSend.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDSend.proxy = new Ext.data.DataProxy({});
ETDSend.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		var records = reader.readRecords(ETDSend.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDSend.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDSend.proxy,
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
ETDSend.store.setDefaultSort('vagnum', 'DESC');

// Filter //////////////////////////////////////////////////////////////////////////////////
/*ETDSend.filters = new Ext.ux.grid.GridFilters({
	filters:[
		{type: 'date',  dataIndex: 'createDate'}
]});*/

//	Column model /////////////////////////////////////////////////////////////////////////////
ETDSend.cm = new Ext.grid.ColumnModel([
{
	header: ETD.headers[1],
	//width: 150,  
	sortable: true,
 renderer : ETD.shortDescriptionRender,
	dataIndex: 'vagnum'
},
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
	//width: 100, 
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
	//width: 200,  
	sortable: true,
 renderer: ETD.shortDescriptionRender,
	dataIndex: 'dognum'
},
{
	header: 'ДИ', 
	//width: 100, 
	sortable: true,
	dataIndex: 'di'
},
{
	header: 'ВЧДЭ', 
	//width: 120, 
	sortable: true,
renderer: ETD.shortDescriptionRender,
	dataIndex: 'rem_pred'
},
{
	
	header: 'Пакет',
	//width: 150, 
	sortable: true,
renderer: ETD.shortDescriptionRender,
	dataIndex: 'idpak'
},

{
//	id:'expand',
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
{
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

ETDSend.cm.defaultSortable = true;

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDSend.grid = new Ext.grid.GridPanel({
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
	sm: new Ext.grid.RowSelectionModel({singleSelect:false}),
	enableColumnHide: true,
	enableColumnMove: true,
//	plugins: ETDSend.filters,
	cm : ETDSend.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDSend.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDSend.store,
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

if (status>-1)
{
	var mess = document.applet_adapter.getDocStatus(row.get('id'));
	
	if (mess.split('&&')[1].length>0){
	Ext.ux.prompt.confirm('Внимание!','<br/>'+mess.split('&&')[1],function(button){
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
			} else if(row.get('name') == 'Пакет документов ЦСС'){
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

ETDSend.addRequestParameter = function(name, value){
	ETDSend.store.baseParams[name] = value;
};

ETDSend.removeRequestParameter = function(name){
	ETDSend.store.baseParams[name] = null;
};

ETDSend.setRequestParameter = function(name, value){
	ETDSend.store.baseParams[name] = value;
};

ETDSend.setFormType = function(formType)
{
	ETDSend.setRequestParameter('formType', formType);
};

ETDSend.load = function(params){
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	//applet_adapter = document.applet;
	ETDSend.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDSend.grid.isLoaded = true;
	ETDSend.grid.count = ret.totalCount || 0;
	show_news();
	if (ETD.fr==1){
		Ext.getCmp('drop').enable();
	} else {
	Ext.getCmp('drop').disable();
	}
	Ext.getCmp('utoch').disable();
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




	