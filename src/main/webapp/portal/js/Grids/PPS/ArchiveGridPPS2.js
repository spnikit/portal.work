Ext.namespace('ETDArchPPS2');


/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/
ETDArchPPS2.load = null;

ETDArchPPS2.setLoadFunction = function(func)
{
	if (typeof func === 'function') ETDArchPPS2.load = func;
	else alert('Error: Load handler initialization. Parameter isn\'t function');
};

//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDArchPPS2.proxy = new Ext.data.DataProxy({});
ETDArchPPS2.proxy.load = function(params, reader, callback, scope, arg)
{
		ETD.busy();
		console.log(params);
		
		var records = reader.readRecords(ETDArchPPS2.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
};

//	Store //////////////////////////////////////////////////////////////////////////////////


ETDArchPPS2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDArchPPS2.proxy,
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
ETDArchPPS2.store.setDefaultSort('vagnum', 'DESC');
ETDArchPPS2.cm = new Ext.grid.ColumnModel([
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
	  header: 'Заказчик', 
	  sortable: true,
	  renderer: ETD.shortDescriptionRender,
	  dataIndex: 'rem_pred'
  },
  {
	  header: 'Договор', 
	  sortable: true,
	  renderer: ETD.shortDescriptionRender,
	  dataIndex: 'di'
  },

  {

	  header: 'Дата', 
	  sortable: true,
	  renderer: ETD.shortDescriptionRender,
	  dataIndex: 'reqdate'
  }
	]);

ETDArchPPS2.cm.defaultSortable = true;

ETDArchPPS2.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDArchPPS2.grid = new Ext.grid.GridPanel({
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
	
//	plugins: ETDArchPPS2.filters,
	cm : ETDArchPPS2.cm,
	sm: ETDArchPPS2.sm,
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
			store: ETDArchPPS2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),
	store: ETDArchPPS2.store,
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

ETDArchPPS2.addRequestParameter = function(name, value){
	ETDArchPPS2.store.baseParams[name] = value;
};

ETDArchPPS2.removeRequestParameter = function(name){
	ETDArchPPS2.store.baseParams[name] = null;
};

ETDArchPPS2.setRequestParameter = function(name, value){
	ETDArchPPS2.store.baseParams[name] = value;
};

ETDArchPPS2.setFormType = function(formType)
{
	ETDArchPPS2.setRequestParameter('formType', formType);
};

ETDArchPPS2.load = function(params){
	
	params.shift = shParam;
	var req = ETD.convertRequestParams(params);
	
	ETDArchPPS2.grid.ppage = (params.start/ETD.docsPerPage)+1;
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
	ETDArchPPS2.grid.isLoaded = true;
	//ETDArchPPS2.grid.count = ret.totalCount || 0;
	show_news();
	Ext.getCmp('drop').disable();
	Ext.getCmp('utoch').disable();
	
	
	
	
	
	
	return ret;
};
