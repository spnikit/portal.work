Ext.namespace('ETDArchPPS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchPPS.load = null;

ETDArchPPS.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchPPS.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchPPS.proxy = new Ext.data.DataProxy({});
ETDArchPPS.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		
		var records = reader.readRecords(ETDArchPPS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchPPS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchPPS.proxy,
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
		{name: 'otcname'}
	]
});
ETDArchPPS.store.setDefaultSort('vagnum', 'DESC');
ETDArchPPS.cm = new Ext.grid.ColumnModel([
{
	header: 'Номер',
	sortable: true,
    renderer : ETD.shortDescriptionRender,
	dataIndex: 'dognum'
},	
{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},

	 {id:'expand',
		header: ETD.headers[12],
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'content'
		
	},

	
	{
		header: 'Исполнитель', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'rem_pred'
	},
	
   {
		
		header: 'Дата', 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'reqdate'
	}
	]);

ETDArchPPS.cm.defaultSortable = true;

ETDArchPPS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchPPS.grid = new Ext.grid.GridPanel({
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
	
//	plugins: ETDArchPPS.filters,
	cm : ETDArchPPS.cm,
	sm: ETDArchPPS.sm,
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
			store: ETDArchPPS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchPPS.store,
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
	ETD.openDocument(row.get('id'), row.get('name'),null,0);
	
}
}
}
//, 
//rowcontextmenu: {fn: function(grid, index, event){
//	event.preventDefault(); 
//	showMenu(grid, index, event);
//	}
//}
	

}
    });
function showMenu(grid, index, event) {
    event.stopEvent();
    var record = grid.getStore().getAt(index);
    var menu = new Ext.menu.Menu({
        items: [{
            text: 'ID',
            handler: function() {
                alert(record.get('id'));
            }
        }, {
            text: 'Name',
            handler: function() {
                alert(record.get('name'));
            }
        }]
    }).showAt(event.xy);
}

ETDArchPPS.addRequestParameter = function(name, value){
	ETDArchPPS.store.baseParams[name] = value;
};

ETDArchPPS.removeRequestParameter = function(name){
	ETDArchPPS.store.baseParams[name] = null;
};

ETDArchPPS.setRequestParameter = function(name, value){
	ETDArchPPS.store.baseParams[name] = value;
};

ETDArchPPS.setFormType = function(formType)
{
	ETDArchPPS.setRequestParameter('formType', formType);
};

ETDArchPPS.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchPPS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchPPS.grid.isLoaded = true;
	//ETDArchPPS.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	
	
	
	
	
	
	return ret;
};
