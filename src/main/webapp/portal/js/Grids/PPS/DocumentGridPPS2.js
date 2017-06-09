Ext.namespace('ETDDocPPS_Role2');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocPPS_Role2.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS_Role2.proxy = new Ext.data.DataProxy({});
ETDDocPPS_Role2.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocPPS_Role2.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDocPPS_Role2.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS_Role2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocPPS_Role2.proxy,
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
		{name: 'otcname'}
	]
});

ETDDocPPS_Role2.store.setDefaultSort('vagnum', 'DESC');
ETDDocPPS_Role2.cm = new Ext.grid.ColumnModel([
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
		//renderer: Ext.util.Format.dateRenderer('Y-m-d'), 
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'reqdate'
	}
	]);

//ETDDocPPS_Role2.cm.defaultSortable = true;

ETDDocPPS_Role2.sm = new Ext.grid.RowSelectionModel({singleSelect:true});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDocPPS_Role2.grid = new Ext.grid.GridPanel({
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
	sm : ETDDocPPS_Role2.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocPPS_Role2.filters,
	cm : ETDDocPPS_Role2.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocPPS_Role2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDocPPS_Role2.store,
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
	if(row.get('name') == 'Заявка ППС') {
		Ext.Ajax.request({
	        url: 'forms/reportPPS',
	        method:'POST',
	        params : {
	        	predid: ETD.predid,
	        	action: 'checkOpenReportForClaim'
	        },
	        success:function(response) {
	        	var jsn = Ext.util.JSON.decode(response.responseText);
	        	if(jsn.countOpenReport == '1') {
	        		Ext.MessageBox.alert('Сообщение','В данный момент редактируется отчет обработки вагонов-цистерн, корректировка заявки невозможна');
	        	} else {
	        		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	        	}
	        }
	    });
	} else {
		ETD.openDocument(row.get('id'), row.get('name'),null,0);
	}

}
}}
}
	
	
    });

ETDDocPPS_Role2.addRequestParameter = function(name, value){
	ETDDocPPS_Role2.store.baseParams[name] = value;
};

ETDDocPPS_Role2.removeRequestParameter = function(name){
	ETDDocPPS_Role2.store.baseParams[name] = null;
};

ETDDocPPS_Role2.setRequestParameter = function(name, value){
	ETDDocPPS_Role2.store.baseParams[name] = value;
};

ETDDocPPS_Role2.setFormType = function(formType){
	ETDDocPPS_Role2.setRequestParameter('formType', formType);
};

ETDDocPPS_Role2.reload = function(){
	
	this.store.reload();
};
	
ETDDocPPS_Role2.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDocPPS_Role2.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocPPS_Role2.grid.isLoaded = true;
	
	ETDDocPPS_Role2.grid.count = ret.totalCount || 0;
	
	show_news();
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();
//	ETDDocPPS_Role2.cm.setHidden(0, true);
	
//	alert(ETDDocPPS_Role2.cm.getColumnHeader(0));
//	alert(ETDDocPPS_Role2.cm.getColumnId(0));
//	alert(ETDDocPPS_Role2.cm.findColumnIndex('vagnum'));
	
	
	//Информация о количестве документов 
	//total_num.setText(ret.totalCount || 0);
	
	return ret;
};



	