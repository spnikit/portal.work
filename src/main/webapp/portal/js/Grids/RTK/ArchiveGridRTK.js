Ext.namespace('ETDArchRTK');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchRTK.load = null;

ETDArchRTK.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchRTK.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchRTK.proxy = new Ext.data.DataProxy({});
ETDArchRTK.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		
		var records = reader.readRecords(ETDArchRTK.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchRTK.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchRTK.proxy,
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
ETDArchRTK.store.setDefaultSort('vagnum', 'DESC');
ETDArchRTK.cm = new Ext.grid.ColumnModel([

{
		id:'name',
		header: ETD.headers[10], 
		sortable: true,
		dataIndex: 'name',
		tdCls: 'test'
	},
	{
		id:'expand',
		header: ETD.headers[12],
		sortable: true,
		renderer : ETD.shortDescriptionRender,
		dataIndex: 'content'
	},
	{
		header: ETD.headers[15],
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'dognum'
		
	},
	 {	header: 'Пакет',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
		
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
	},
	{
		header: 'Статус',
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'packst'
	}
	]);

ETDArchRTK.cm.defaultSortable = true;

ETDArchRTK.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchRTK.grid = new Ext.grid.GridPanel({
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
	
//	plugins: ETDArchRTK.filters,
	cm : ETDArchRTK.cm,
	sm: ETDArchRTK.sm,
viewConfig: {
    forceFit: true
},
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDArchRTK.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchRTK.store,
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
	if (row.get('name')!='Пакет документов РТК'){
		ETD.dropPackRTKid();
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}
	
else if (row.get('name')=='Пакет документов РТК'){

ETD.setPackRTKid(row.get('id'));
PackRTK.req = row.get('id');
PackRTK.mkPackRTKwin(row.get('id'));

}
	
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

ETDArchRTK.addRequestParameter = function(name, value){
	ETDArchRTK.store.baseParams[name] = value;
};

ETDArchRTK.removeRequestParameter = function(name){
	ETDArchRTK.store.baseParams[name] = null;
};

ETDArchRTK.setRequestParameter = function(name, value){
	ETDArchRTK.store.baseParams[name] = value;
};

ETDArchRTK.setFormType = function(formType)
{
	ETDArchRTK.setRequestParameter('formType', formType);
};

ETDArchRTK.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchRTK.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchRTK.grid.isLoaded = true;
	//ETDArchRTK.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	Ext.getCmp('export').enable();
	Ext.getCmp('exportXML').show();
	if (ETD.expdoc){
		Ext.getCmp('exportdoc').show();
	}
	else {
		Ext.getCmp('exportdoc').hide();
	}
	
	
	
	
	return ret;
};
