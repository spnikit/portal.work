Ext.namespace('ETDDoc');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDoc.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDoc.proxy = new Ext.data.DataProxy({});
ETDDoc.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDoc.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		var records = reader.readRecords(ETDDoc.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDoc.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDoc.proxy,
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
		{name: 'servicetype'},
		{name: 'price'},
		{name: 'otcname'},
		{name: 'otctype'},
		{name: 'sftype'},
		{name: 'color', type:'int'},
		{name: 'numberFpu'},
		{name: 'numberSf'}
	]
});

ETDDoc.store.setDefaultSort('vagnum', 'DESC');
ETDDoc.cm = new Ext.grid.ColumnModel([
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
		dataIndex: 'name',
		tdCls: 'test'
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
	//width: 120, 
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
		//id:'expand',
		header: 'Пакет',
		//width: 170, 
		sortable: true,
		renderer: ETD.shortDescriptionRender,
		dataIndex: 'idpak'
	},
	
	
   {
		
		header: ETD.headers[13], 
		//width: 120, 
		sortable: true,
		renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s'), 
		dataIndex: 'createDate'
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

//ETDDoc.cm.defaultSortable = true;

ETDDoc.sm = new Ext.grid.RowSelectionModel({singleSelect:false});

//	Grid  //////////////////////////////////////////////////////////////////////////////////
ETDDoc.grid = new Ext.grid.GridPanel({
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
    	var d = record.get('color');
//    	ETDDoc.getDataCompare(record.get('idpak'),record.get('name'));

    	//alert(c); 
    	//if(ETD.priceCheckEnable){
    		//var d = record.get('color');
    		if (c==1 || d==1){
    			return 'acc-row';
    		}else if (c==2 || d==2){
    			return 'dropped-row';
    		}else if (c==3 || d ==3){
    			return 'warn-row';
    		}else if (c==4 || d==4){
    			return 'income-row';
    		}
    	/*}else{
    		if (c==1){
    			return 'acc-row';
    		}else if (c==2){
    			return 'dropped-row';
    		}else if (c==3){
    			return 'warn-row';
    		}else if (c==4){
    			return 'income-row';
    		}
    	}*/
    }
    
},
	//sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	sm : ETDDoc.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDoc.filters,
	cm : ETDDoc.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDoc.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),


	
	store: ETDDoc.store,
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
	if (row.get('name').indexOf('ФПУ-26 ППС')>-1&&row.get('packst')=='Забраковано'){
		ETD.openDocumentFPU26PPS(row.get('id'), row.get('name'),null,0);
		}else if (row.get('name')=='Пакет документов'){
			if(row.get('color')!=0){
				Ext.Ajax.request({
					url: 'forms/listchanges',
					params: {idpak:row.get('idpak')},
					callback : function(options, success, response) {
						var resp = response.responseText;;
						var mass = Ext.util.JSON.decode(resp);
						if(mass == null || mass == ""|| resp == "{}"){
							ETD.firstpack(row.get('id'));
							ETD.openDocument(row.get('id'), row.get('name'),null,0);
						}else{
							var rdvMessegeName="";
							var rdvMessegeCode="";
							var mxMessegeName="";
							var mxMessegeCode="";
							var mess = "";
							if(mass[0].name[0].length != 0){
								var nameWork = "";
								for(var i = 0; i < mass[0].name.length; i++){
									if(i!= mass[0].name.length-1){
										nameWork+="{"+mass[0].name[i]+"},";
									}else{
										nameWork+="{"+mass[0].name[i]+"}";
									}
								}
								if(nameWork!="" && nameWork!="{}"){
									rdvMessegeName = "Внимание!<br>Ценовые показатели "+nameWork+" в РДВ не соответствуют справочнику.";
								}else{
									rdvMessegeName="";
								}
							}
							if(mass[0].code.length != 0){
								var codeWork = "";
								for(var i = 0; i < mass[0].code.length; i++){
									if(i!= mass[0].code.length-1){
										if(mass[0].code[i]!=""){
											codeWork+="{"+mass[0].code[i]+"},";
										}
									}else{
										if(mass[0].code[i]!=""){
											codeWork+="{"+mass[0].code[i]+"}";
										}
									}
								}
								if(codeWork!="" && codeWork!="{}"){
									rdvMessegeCode = "Внимание!<br>Отсутствует значение для кода услуги "+codeWork+" для РДВ в справочнике.";
								}else{
									rdvMessegeCode="";
								}
							}

							if(rdvMessegeName!="" && rdvMessegeCode!=""){
								mess = rdvMessegeName+"<br>"+rdvMessegeCode;
							}else if(rdvMessegeName!=""){
								mess = rdvMessegeName;
							}else if(rdvMessegeCode!=""){
								mess = rdvMessegeCode;
							}

							if(mxMessegeName!="" && mxMessegeCode!=""){
								mess+= "<br>"+mxMessegeName+"<br>"+mxMessegeCode;
							}else if(mxMessegeName!=""){
								mess+= "<br>"+mxMessegeName;
							}else if(mxMessegeCode!=""){
								mess+= "<br>"+mxMessegeCode;
							}
							if (mess.length>0){
								Ext.Msg.show({
									title:'<div style="margin-left:15px;text-align:center;">Внимание</div>',
									msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:justify;">'+mess+'</div>',
									buttons :{ ok: "Продолжить работу", no: "Отклонить Пакет", cancel: "Отмена" },

									animEl: 'elId',
									fn:function(button, text){

										if (button=='ok'){
											ETD.firstpack(row.get('id'));
											ETD.openDocument(row.get('id'), row.get('name'),null,0);        		      
										}
										if (button=='no'){							
											dropdoc(false);

										}

									}
								});
							}
							//}
						}
					}
				});
			}else{
				ETD.firstpack(row.get('id'));
				ETD.openDocument(row.get('id'), row.get('name'),null,0);        
			}
		//ETD.firstpack(row.get('id'));
	}
	
		else {
ETD.openDocument(row.get('id'), row.get('name'),null,0);
		}
}
}},

rowclick : {fn : function(grid, rowIndex, e){

		var row = grid.getSelectionModel().getSelected();

var status=row.get('status');
var statid = row.get('id');
if (status>-1)
{
var mess = document.applet_adapter.getDocStatus(statid);

if (mess.split('&&')[1].length>0){
	ETD.openStatusDoc(statid, row.get('name'), mess.split('&&')[1]);
	ETD.setTransPackid(statid);

}

}

}}
}
	
    });

ETDDoc.addRequestParameter = function(name, value){
	ETDDoc.store.baseParams[name] = value;
};

ETDDoc.removeRequestParameter = function(name){
	ETDDoc.store.baseParams[name] = null;
};

ETDDoc.setRequestParameter = function(name, value){
	ETDDoc.store.baseParams[name] = value;
};

ETDDoc.setFormType = function(formType){
	ETDDoc.setRequestParameter('formType', formType);
};

ETDDoc.reload = function(){
	
	this.store.reload();
};
	
ETDDoc.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);
	ETDDoc.grid.ppage = (params.start/ETD.docsPerPage)+1;
		
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
	ETDDoc.grid.isLoaded = true;
	
	ETDDoc.grid.count = ret.totalCount || 0;
	
	show_news();
	
	Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('exportXML').hide();
	if (ETD.predname.indexOf('АО "ПГК"')>-1){
		Ext.getCmp('reportTOR').show();
	}else {
		Ext.getCmp('reportTOR').hide();
	}
	Ext.getCmp('acceptDocs').hide();
	Ext.getCmp('createPretension').hide();
	return ret;
};



	