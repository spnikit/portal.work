Ext.namespace('ETDDocR2');

/*
Функция загружающая данные в таблицу должна быть инициализирована перед использованием
@param params{start, limit, sort, dir}
*/

ETDDocR2.load = null;
var isDblClick = false;
//	Proxy  //////////////////////////////////////////////////////////////////////////////////
ETDDocR2.proxy = new Ext.data.DataProxy({});
ETDDocR2.proxy.load = function(params, reader, callback, scope, arg)
{
	
		//Dev.logger.log("ETDDocR2.proxy.load started ...");
		var start = new Date().getTime();
		ETD.busy();
		
		var records = reader.readRecords(ETDDocR2.load(params));
		ETD.ready();
		callback.call(scope, records,arg, true);
		var finish = new Date().getTime();
		//Dev.logger.log("Data lodaing time = ", (finish - start));
};

//	Store //////////////////////////////////////////////////////////////////////////////////
ETDDocR2.store = new Ext.data.JsonStore
({
	root: 'documents',
	totalProperty: 'totalCount',
	successProperty : 'success',
	proxy : ETDDocR2.proxy,
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

ETDDocR2.store.setDefaultSort('vagnum', 'DESC');
ETDDocR2.cm = new Ext.grid.ColumnModel([
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

//ETDDocR2.cm.defaultSortable = true;

ETDDocR2.sm = new Ext.grid.RowSelectionModel({singleSelect:false}); 

//	Grid  //////////////////////////////////////////////////////////////////////////////////   
ETDDocR2.grid = new Ext.grid.GridPanel({
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

    	var d = record.get('color');
    	var c = record.get('status');
    	//if(ETD.priceCheckEnable){
    		//var d = record.get('color');
    		if (c==1 || d==1){
    			return 'acc-row';
    		}else if (c==2 || d==2){
    			return 'dropped-row';
    		}else if (c==3 || d==3){
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
	sm : ETDDocR2.sm,
	enableColumnHide: true,
	enableColumnMove: true,
	//plugins: ETDDocR2.filters,
	cm : ETDDocR2.cm,
	bbar : new Ext.PagingToolbar({
			pageSize: ETD.docsPerPage,
			store: ETDDocR2.store,
			displayInfo: true,
			displayMsg: 'Документы {0} - {1} из {2}',
			emptyMsg: "Нет документов"
		}),

	
	store: ETDDocR2.store,
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
				if (row.get('name').indexOf('ФПУ-26 ППС')>-1&&row.get('packst')=='Забраковано'&&ETD.fr==3){
					ETD.openDocumentFPU26PPS(row.get('id'), row.get('name'),null,0);
				}

				else if (row.get('name').indexOf('Пакет документов')==-1){	
//					else if (row.get('name')!='Пакет документов'&&row.get('name')!='Пакет документов РТК'){
					ETD.dropPackid();
					ETD.openDocument(row.get('id'), row.get('name'),null,0);
				}else if (row.get('name')=='Пакет документов'){
					if(row.get('color')!=0){
						Ext.Ajax.request({
							url: 'forms/listchanges',
							params: {idpak:row.get('idpak')},
							callback : function(options, success, response) {
								var resp = response.responseText;
								var mass = Ext.util.JSON.decode(resp);
								//var mass = Ext.util.JSON.decode(resp);
								if(/*!ETD.priceCheckEnable ||*/ mass == null || mass == "" || resp == "{}"){
									ETD.firstpack(row.get('id'));
									ETD.setPackid(row.get('id'));
									Pack.req = row.get('id');
									Pack.mkpackwin(row.get('id'));
								}else{

									var rdvMessegeName="";
									var rdvMessegeCode="";
									var mxMessegeName="";
									var mxMessegeCode="";
									var mess = "";
									if(mass[0].name.length != 0){
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
									if(mess!=""){
										Ext.Msg.show({
											title:'<div style="margin-left:15px;text-align:center;">Внимание</div>',
											msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:justify;">'+mess+'</div>',
											buttons :{ ok: "Продолжить работу", no: "Отклонить Пакет", cancel: "Отмена" },

											animEl: 'elId',
											fn:function(button, text){

												if (button=='ok'){
													ETD.firstpack(row.get('id'));
													ETD.setPackid(row.get('id'));
													Pack.req = row.get('id');
													Pack.mkpackwin(row.get('id'));
													// ETD.openDocument(row.get('id'), row.get('name'),null, 0);

												}
												if (button=='no'){
													ETD.setPackid(row.get('id'));
													Ext.Ajax
													.request({
														url : 'forms/droppack',
														params : {
															docid : ETD.packid
														},
														callback : function(options,
																success, response) {
															var resp = response.responseText;
															var arr = Ext.util.JSON
															.decode(resp);

															if (arr == 'true') {
																viewpackwin.hide();
																dropdoc(false);

															} else {
																Ext.MessageBox
																.alert('Сообщение',
																'В пакете есть непроверенные документы');
															}

														}
													});
												}

											}
										});
									}
								}
							}
						});
					}else{
						ETD.firstpack(row.get('id'));
						ETD.setPackid(row.get('id'));
						Pack.req = row.get('id');
						Pack.mkpackwin(row.get('id'));
					}
				}
		
else if (row.get('name')=='Пакет документов РТК'){
	ETD.setPackRTKid(row.get('id'));
	PackRTK.req = row.get('id');
	PackRTK.mkPackRTKwin(row.get('id'));
	
	}
	else if (row.get('name')=='Пакет документов ЦСС'){
	ETD.setPackCSSid(row.get('id'));
	PackCSS.req = row.get('id');
	PackCSS.mkpackwin(row.get('id'));
	}
}
	}},
	
	rowclick : {fn : function(grid, rowIndex, e){
	
			var row = grid.getSelectionModel().getSelected();
	
var status=row.get('status');

//if (status==1||status==2||status==3)
//{
//	var mess = document.applet_adapter.getDocStatus(row.get('id'));
//	Ext.ux.prompt.confirm('Внимание!',mess.split('&&')[0]+'<br/><br/>'+mess.split('&&')[1],function(button){
//if(button=='yes'){
//	if (row.get('name')!='Пакет документов' && row.get('name')!='Пакет документов ЦСС'){
//			ETD.dropPackid();
//			ETD.openDocument(row.get('id'), row.get('name'),null,0);
//		} else {
//		if(row.get('name') == 'Пакет документов') {
//			ETD.firstpack(row.get('id'));	
//			ETD.setPackid(row.get('id'));
//			Pack.req = row.get('id');
//			Pack.mkpackwin(row.get('id'));
//			} else if(row.get('name') == 'Пакет документов ЦСС'){
//				ETD.setPackCSSid(row.get('id'));
//				PackCSS.req = row.get('id');
//				PackCSS.mkpackwin(row.get('id'));
//			}
//			
//			}
//}
//});
//}
	
if (status>-1)
{
var mess = document.applet_adapter.getDocStatus(row.get('id'));

if (mess.split('&&')[1].length>0){
	ETD.openStatusDoc(row.get('id'), row.get('name'), mess.split('&&')[1]);
//	ETD.setTransPackid(statid);

}

}

}}
}
 });

ETDDocR2.addRequestParameter = function(name, value){
	ETDDocR2.store.baseParams[name] = value;
};

ETDDocR2.removeRequestParameter = function(name){
	ETDDocR2.store.baseParams[name] = null;
};

ETDDocR2.setRequestParameter = function(name, value){
	ETDDocR2.store.baseParams[name] = value;
};

ETDDocR2.setFormType = function(formType){
	ETDDocR2.setRequestParameter('formType', formType);
};

ETDDocR2.reload = function(){
	
	this.store.reload();
};
	
ETDDocR2.load = function(params)
{
	params.shift = shParam;
	
	var req = ETD.convertRequestParams(params);

	ETDDocR2.grid.ppage = (params.start/ETD.docsPerPage)+1;
	
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
	ETDDocR2.grid.isLoaded = true;
	
	ETDDocR2.grid.count = ret.totalCount || 0;
	
	show_news();
	if (ETD.fr==6||ETD.fr==13){
		Ext.getCmp('drop').disable();
		Ext.getCmp('utoch').disable();
	}
	else {Ext.getCmp('drop').enable();
	Ext.getCmp('utoch').enable();
	}
	
	Ext.getCmp('exportdoc').hide();
	Ext.getCmp('export').enable();
	
	if (ETD.predname.indexOf('АО "ПГК"')>-1){
		Ext.getCmp('reportTOR').show();
	}else {
		Ext.getCmp('reportTOR').hide();
	}
	Ext.getCmp('acceptDocs').show();
	Ext.getCmp('createPretension').hide();
	Ext.getCmp('exportXML').hide();
		//console.log("ret4 = "+Ext.util.JSON.decode(ret));
	return ret;
};


	