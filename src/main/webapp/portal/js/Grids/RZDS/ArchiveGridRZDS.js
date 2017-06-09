Ext.namespace('ETDArchRZDS');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchRZDS.load = null;

ETDArchRZDS.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchRZDS.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchRZDS.proxy = new Ext.data.DataProxy({});
ETDArchRZDS.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		
		var records = reader.readRecords(ETDArchRZDS.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchRZDS.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchRZDS.proxy,
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
		{name: 'packst'}
	]
});
ETDArchRZDS.store.setDefaultSort('vagnum', 'DESC');
ETDArchRZDS.cm = new Ext.grid.ColumnModel([

{	header: 'Номер комплекта',
	sortable: true,
	renderer: ETD.shortDescriptionRender,
	dataIndex: 'idpak'
	
},
{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},
	{
		header: 'Договор',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'dognum'
		
	},
	{
		id:'expand',
		header: 'Номер документа',
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'content'
	},

   {
		
		header: 'Дата создания', 
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
	}
	]);

ETDArchRZDS.cm.defaultSortable = true;

ETDArchRZDS.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchRZDS.grid = new Ext.grid.GridPanel({
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
	
//	plugins: ETDArchRZDS.filters,
	cm : ETDArchRZDS.cm,
	sm: ETDArchRZDS.sm,
viewConfig: {
    forceFit: true
},
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArchRZDS.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchRZDS.store,
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

ETDArchRZDS.addRequestParameter = function(name, value){
	ETDArchRZDS.store.baseParams[name] = value;
};

ETDArchRZDS.removeRequestParameter = function(name){
	ETDArchRZDS.store.baseParams[name] = null;
};

ETDArchRZDS.setRequestParameter = function(name, value){
	ETDArchRZDS.store.baseParams[name] = value;
};

ETDArchRZDS.setFormType = function(formType)
{
	ETDArchRZDS.setRequestParameter('formType', formType);
};

ETDArchRZDS.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchRZDS.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchRZDS.grid.isLoaded = true;
	//ETDArchRZDS.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('exportdoc').enable();
	
	
	
	
	return ret;
};
