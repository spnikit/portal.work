﻿$.fn.center = function(){
	$(this).each(function(){

		var obj = $(this);
		var win = $(window);
		obj.css({position: 'absolute', 
			left: win.width()/2-obj.width()/2, 
			top: win.height()/2-obj.height()/2});

	});		
};


var name;
var depname;



function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\\\s)"+searchClass+"(\\\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}

function SayIt(str){
	Ext.MessageBox.alert('Ошибка',str);
}

function show_pbar(){
	$('.pbline').css({width:0});
	$('.progress_bar_item').show();
	$('.pbfog').show();
	$('.progress_bar_item').show();
	$('#loading').show();

}

function infinite_pbar(period){

	period = period == null ? 2500 : period; 


	// if pbar is running then simply return
	if(window.pbar_timer!=null)
		return;

	var interval = period / 3;
	var progress = 0.0;
	update_pbar(0);

	pbar_timer = setInterval(function(){

		if(progress>=1.0)
			progress = 0.0;
		else
			progress += interval / period;

		update_pbar(progress);

	}, interval);

}

function hide_pbar(){

	if(window.pbar_timer!=null){
		clearInterval(pbar_timer);
		pbar_timer = null ;
	}


	$('.progress_bar_item').hide();
	$('.progress_bar_pane').hide();
	$('.pbfog').hide();



}

function update_pbar(progress){

	if(progress>1.0)
		progress = 1.0;
	if(progress<0.0)
		progress = 0.0;


	$('.pbline').css({width: 310.0 * progress});

}

function busy(){
	show_pbar();
	// infinite_pbar();
}



function busy_1(){

	show_pbar();

}


function ready()
{
	// busyWin.hide();
	document.getElementById('loading').style.visibility='hidden';
};


function login(){
	if(eval('('+document.applet_adapter.login(' ')+')').value){
		ETD.busy();
		select_depo();
	}else{
		ETD.ready();
		Ext.MessageBox.alert('Ошибка','необходима авторизация',function(btn){
			Refresh();
		});
	};

	ETD.ready();
};

var show_news = function () {

	var hasnews = eval('('+document.applet_adapter.HasNews()+')');
	if(hasnews!=1)
		return;
	var message = document.applet_adapter.GetNews(); // сюда кидаем сообщение
	// сервера

	on_ok = function(){
		win_news.destroy();
		document.applet_adapter.DeleteNews();
	}

	var text_data = new Ext.form.Label({
		text: message
	});

	var win_news = new Ext.Window({
		title: 'Объявление:',
		layout:'fit',
		width: 250,
		autoHeight: true,
		border:false,
		hideBorders:true,
		style:'text-align:center;',
		resizable: false,
		buttonAlign:'center',
		closable: false,
		modal: true,
		items : text_data,
		buttons: [{text: 'OK',	handler:on_ok}]
	});

	win_news.show();
}
var select_depo = function(){
	var json = eval('('+document.applet_adapter.getWorkPosition()+')');
	// Dev.logger.log("Select depo : return ", json);
	if(json.error){
		ETD.ready();
		Ext.MessageBox.alert("Ошибка!",json.error,function(btn){Refresh();});
	}else{
		if(json.name && json.rolename){
			predname=json.predname;
			dorname=json.dorname;
			name=json.name;
			depname = json.rolename;
			rolename=json.rolename;
			ETD.fr = json.fr;
			ETD.predid = json.predid;
			ETD.predname = json.predname;
			ETD.wrkid = json.wrkid;
			ETD.expdoc = json.expdoc;
			ETD.pdfcheck = json.pdfcheck;
			mainwin();
			if(!predname){
				ETD.busy();
				if(!dorname){
					Ext.getCmp('combdor').enable();
				}
				var predprs = eval('('+document.applet_adapter.getPredprs()+')');
				if(predprs.data)
				{
					comboPredprStore.loadData(predprs);
					comboPredpr.setValue(predprs.data[0].name);
				}
				Ext.getCmp('combdor').show();
				Ext.getCmp('combpred').show();
				ETD.ready();
			}
			Ext.get(document.getElementsByTagName('html')[0]).addClass('x-viewport');
			Ext.get(document.getElementsByTagName('body')[0]).addClass('x-border-layout-ct');

		}
		else{
			// Auto size
			var Size=333;
			var MaxLen=0;
			var WinSize=Ext.getBody().getWidth();

			for (i=0;i<json.values.length;i++)
			{

				if (json.values[i].length>MaxLen)	MaxLen=json.values[i].length;
			}

			if (MaxLen*6>WinSize) MaxLen=(WinSize-200)/6;
			// Auto size
			var combopred = new Ext.form.ComboBox({
				store: json.values,
				width: MaxLen*6-20,
				editable: false,
				hideLabel:true,
				value: json.values[0],
				mode: 'local',
				triggerAction: 'all',
				selectOnFocus: true
			});
			on_ok = function(){

				ETD.busy();
				win.destroy();				
				(function(){
					json = eval('('+document.applet_adapter.setWorkPosition(combopred.getValue())+')');
					name = json.name;
					rolename=json.rolename;
					ETD.fr = json.fr;
					dorname=json.dorname;
					predname=json.predname;
					depname = json.rolename;
					ETD.predid = json.predid;
					ETD.predname = json.predname;
					ETD.wrkid = json.wrkid;
					ETD.expdoc = json.expdoc;
					ETD.pdfcheck = json.pdfcheck;
					mainwin();
					if(!predname){
						ETD.busy();
						if(!dorname){
							Ext.getCmp('combdor').enable();
						}
						var predprs = eval('('+document.applet_adapter.getPredprs()+')');
						if(predprs.data)
						{
						}
						ETD.ready();
					}
					Ext.get(document.getElementsByTagName('html')[0]).addClass('x-viewport');
					Ext.get(document.getElementsByTagName('body')[0]).addClass('x-border-layout-ct');
				}).defer(1);
			}
			var win = new Ext.Window({
				title: 'Выберите предприятие:',
				layout:'fit',
				width: 400,
				border:false,
				maximizable:true,
				hideBorders:true,
				resizable: true,
				buttonAlign:'center',
				closable: false,
				items:combopred,
				buttons: [{text: 'OK',	handler:on_ok}]
			});
			combopred
			ETD.ready();
			win.show();
			combopred.on('specialkey',function(field,e){
				if(e.getKey()==13) on_ok();
			})
		}
	}
};



var update_table =function(){
	var grid;
	grid = Ext.getCmp('tpanel').getActiveTab();
	var index;

	index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());

};

function  update_table_addonbase (){
	var grid = Ext.getCmp('tpanel_test').getActiveTab();
	var table = eval('('+document.applet_adapter.table(fr_addonbaseid)+')');
	if (table.error) Refresh();
	if(table.length!=0) grid.getStore().loadData(table.rows);
	else grid.getStore().removeAll();
	// total_num.setText(table.count);
	grid.getBottomToolbar().items.get(2).setText(table.page);
	vis=!(table.first && table.last);
	grid.getBottomToolbar().items.get(3).setVisible(vis);
	grid.getBottomToolbar().items.get(1).setVisible(vis);
	if(table.first) grid.getBottomToolbar().items.get(1).disable();
	else grid.getBottomToolbar().items.get(1).enable();
	if(table.last) grid.getBottomToolbar().items.get(3).disable();
	else grid.getBottomToolbar().items.get(3).enable();
};

var Refresh_tab =function(){
	busy();
	var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
	var tmp=null;
	if (massiv.length>0) tmp = massiv.join("|");
	document.applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),tmp,Ext.getCmp('ROWSCOUNTID').getValue());
	update_table();
	ready();
}


var ShowFilterWind=function(){
	var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());

	filterwin_newall.show();filterwin_newall.focus();


}
var DateWinShow=function(){
	if (oco_or_snt_index==1) {
		DateWinSNTShow();}
	else {
		DateWinOCOShow();
	}
}

var mkfile = function(){
	makefile();
}	
function makefile(){

	var items = [];
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selections = grid.getSelectionModel().getSelections();
	if(selections==null || selections.length==0)
		return;
	var idsSf = "";
	var isPack = "";
	for(var j=0; j<selections.length; j++){

		if (selections[j].get('name')!=='Счет-фактура' && selections[j].get('name')!=='Счет-фактура ЦСС'&& selections[j].get('name')!=='Пакет документов РТК'){
			Ext.MessageBox.alert('Ошибка','Выгрузка возможна только для документов типа "Счет-фактура"');	
			return;
		}

		var ispackrtk = 0;
		if (selections[j].get('name').indexOf('Пакет документов РТК')>-1){
			ispackrtk=1;
		}

		if(j < selections.length-1){
			idsSf = idsSf + selections[j].get('id')+";";
			isPack = isPack + ispackrtk + ";";
		}else{
			idsSf = idsSf + selections[j].get('id');
			isPack = isPack + ispackrtk;
		}

	}

	Ext.Ajax.request({
		url : 'forms/getXML',
		params : {
			act: 'get',
			idSf: idsSf,
			isPack: isPack
		},
		callback : function(options, success, response) {

			var resp = response.responseText;
			var arr = Ext.util.JSON.decode(resp);
			var isEmpty = 0;
			var contain = 0;
			var goodDoc = 0;
			var idsEmtyPack = "";
			var idsNotContainBase = "";
			var idsPackNotContainSf = "";
			var idsSfWhithoutPack = "";
			var countEmpty = 0;
			var countNotContainDoc = 0;

			for(var i = 0; i < arr.data.length; i++){
				if(arr.data[i].isEmpty == 0 && arr.data[i].containBase == 1){
					goodDoc+=1;
				}else if(arr.data[i].isEmpty != 0){
					countEmpty += 1;
				}else if(arr.data[i].isEmpty == 0 && arr.data[i].containBase != 1){
					countNotContainDoc += 1;
				}
				
			}
			if(goodDoc == 0){
				for(var i = 0; i < arr.data.length; i++){
					if(arr.data[i].isEmpty != 0 ){

						if(countEmpty > 1){
							idsEmtyPack = idsEmtyPack + arr.data[i].idPack + ", ";
							countEmpty -= 1;
						}else if(countEmpty == 1){
							idsEmtyPack = idsEmtyPack + arr.data[i].idPack;
						}
					}

					if(arr.data[i].isEmpty == 0 && arr.data[i].containBase != 1){
						
						if(countNotContainDoc > 1){
							if(arr.data[i].idPack != ""){
								idsPackNotContainSf = idsPackNotContainSf + arr.data[i].idPack + ", ";
								idsNotContainBase = idsNotContainBase + arr.data[i].sfId + ", ";
							}else{
								idsSfWhithoutPack = idsSfWhithoutPack + arr.data[i].sfId + ", ";
							}
							countNotContainDoc -= 1;
						}else if(countNotContainDoc == 1){
							if(arr.data[i].idPack != ""){
								idsPackNotContainSf = idsPackNotContainSf + arr.data[i].idPack;
								idsNotContainBase = idsNotContainBase + arr.data[i].sfId;
							}else{
								idsSfWhithoutPack = idsSfWhithoutPack + arr.data[i].sfId;
							}
						}
					}
				}
				
				if(idsEmtyPack != "" && idsNotContainBase != "" && idsSfWhithoutPack != ""){
					Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! В пакетах документов '+idsEmtyPack+' нет счетов-фактур. По счетам-фактурам '+idsNotContainBase + ", "+idsSfWhithoutPack+ ' не сформированы данные.');
					return;
				}else if(idsEmtyPack != "" && idsNotContainBase != "" && idsSfWhithoutPack == ""){
					Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! В пакетах документов '+idsEmtyPack+' нет счетов-фактур. По счетам-фактурам '+idsNotContainBase + ' не сформированы данные.');
					return;
				}else if(idsEmtyPack != "" && idsNotContainBase == "" && idsSfWhithoutPack != ""){
					Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! В пакетах документов '+idsEmtyPack+' нет счетов-фактур. По счетам-фактурам '+idsSfWhithoutPack + ' не сформированы данные.');
					return;
				}else if(idsEmtyPack != "" && idsNotContainBase == "" && idsSfWhithoutPack == ""){
					Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! В пакетах документов '+idsEmtyPack+' нет счетов-фактур. ');	
					return;
				}else if(idsEmtyPack == "" && idsNotContainBase != "" && idsSfWhithoutPack == ""){
					if(ETD.fr == 1 || ETD.fr == 2 || ETD.fr == 3 || ETD.fr == 4 || ETD.fr == 6 || ETD.fr == 9 || ETD.fr == 13 || ETD.fr == 14 || ETD.fr == 22){
						Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! По счетам-фактурам в пакетах документов '+idsPackNotContainSf+ ' не сформированы данные.');	
					}else{
						Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! По счетам-фактурам '+idsNotContainBase+ ' не сформированы данные.');	
					}
					return;
				}else if(idsEmtyPack == "" && idsNotContainBase == "" && idsSfWhithoutPack != ""){
					Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! По счетам-фактурам '+idsSfWhithoutPack+ ' не сформированы данные.');
					return;
				}else if(idsEmtyPack == "" && idsNotContainBase != "" && idsSfWhithoutPack != ""){
					if(ETD.fr == 1 || ETD.fr == 2 || ETD.fr == 3 || ETD.fr == 4 || ETD.fr == 6 || ETD.fr == 9 || ETD.fr == 13 || ETD.fr == 14 || ETD.fr == 22){
						Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! По счетам-фактурам в пакетах документов '+idsPackNotContainSf+ ' не сформированы данные. По счетам-фактурам '+idsSfWhithoutPack+' не сформированы данные.');	
					}else{
						Ext.MessageBox.alert('Ошибка','Выгрузка невозможна! По счетам-фактурам '+idsNotContainBase+ ' не сформированы данные. По счетам-фактурам '+idsSfWhithoutPack+' не сформированы данные.');	
					}
					return;
				}
			}else{

				var param='act=export';

				for(var i = 0; i < arr.data.length; i++){
					param=param+'&'+'id'+arr.data[i].iter+'='+arr.data[i].id+'&'+'ispack'+arr.data[i].iter+'='+arr.data[i].isIdPack;
				}

				Ext.DomHelper.append(document.body, {
					tag: 'iframe',
					id:'downloadIframe',
					frameBorder: 0,
					width: 0,
					height: 0,
					css: 'display:none;visibility:hidden;height:0px;',
					src: 'forms/getXML'+'?'+param
				});
			}

		}
	});

}
var excelsaver = function(){
	savedocexcel();
}

function savedocexcel(){

	var grid = Ext.getCmp('tpanel').getActiveTab();
	var cm = grid.getColumnModel();
	var store = grid.getStore();
	var title = grid.title;
//	alert(title);

	var it = store.data.items;
	var rows = it.length;

	try{
		var   oXL   =   new   ActiveXObject("Excel.application");
		var   oWB   =   oXL.Workbooks.Add();     
		var   oSheet   =   oWB.ActiveSheet; 
	}
	catch(e)
	{
		alert(e.message);
		oXL = null;
		oWB = null;
		oSheet = null;
		return;
	}

	oXL.Visible = true;


	//oSheet.Cells(1, 1).Interior.ColorIndex = 37
	var k = 0;
	var colorSwitch = 4;
	var letterWidth = 1;
	var spacing = 5;

	oSheet.Cells(1, 1).value = title;
	oSheet.Cells(1, 1).ColumnWidth = title.length*letterWidth+ spacing;
	for (var i = 0 ; i < cm.getColumnCount(); i++) {


		if (!cm.isHidden(i)) {
			//oSheet.Cells(1, k+1).Interior.ColorIndex = 34;
			oSheet.Cells(2, k + 1).value = cm.getColumnHeader(i);
			var width = oSheet.Cells(2, k+1).ColumnWidth;
			if(width<cm.getColumnHeader(i).toString().length*letterWidth + spacing){
				oSheet.Cells(2, k+1).ColumnWidth = cm.getColumnHeader(i).toString().length*letterWidth + spacing;
			}
			colorSwitch ^= 4;

			for (var j = 0; j < rows; j++) {
				//oSheet.Cells(j+3, k+1).Interior.ColorIndex = 36 + colorSwitch;
				r = it[j].data;
				var v = r[cm.getDataIndex(i)];
				var fld = store.recordType.prototype.fields.get(cm.getDataIndex(i));
				if(fld.type == 'date')
				{
					v = v.format('Y-m-d H:i:s');    
				}

				if(!cm.isHidden(i)){
					var _v = v;//TrimStr(v);
					oSheet.Cells(3 + j, k + 1).value = _v.toString();
					var width = oSheet.Cells(j+3, k+1).ColumnWidth;
					if(width<_v.length*letterWidth){
						oSheet.Cells(j+3, k+1).ColumnWidth = _v.toString().length*letterWidth + spacing;
					}

				}
			}
			k = k+1;

		}
	}

	//Границы ячеек
	var xlDiagonalDown =5   //Диагональная от верхнего левого угла в нижний правый каждой ячейки в диапазоне 
	var xlDiagonalUp =6   //Диагональная из нижнего левого угла в правый верхний каждой ячейки в диапазоне. 
	var xlEdgeBottom =9   //Нижнаяя для всего диапазона ячеек 
	var xlEdgeLeft =7    //Левая для всего диапазона ячеек. 
	var xlEdgeRight =10   //Правая для всего диапазона ячеек. 
	var xlEdgeTop =8    //Верхняя для всего диапазона ячеек. 
	var xlInsideHorizontal =12 //Горизонтальные границы всех внутренних ячеек диапазона 
	var xlInsideVertical =11  //Вертикальные границы всех внутренних ячеек диапазона

	//выравнивание текста в ячейке
	var xlCenter = -4108 //по центру
	var xlRight = -4152 //справа
	var xlLeft = -4131  //слева


	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlInsideHorizontal).Weight = 2;
	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlInsideVertical).Weight = 2;
	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeBottom).Weight = 2;
	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeRight).Weight = 2;
	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeLeft).Weight = 2;
	oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeTop).Weight = 2;
	oSheet.Rows(1).HorizontalAlignment = xlCenter;
	oSheet.Range(oSheet.Cells(2, 1), oSheet.Cells(2, k)).Cells.HorizontalAlignment = xlCenter
	oSheet.Cells(1, 1).Font.Size = 12;
	oSheet.Cells(1, 1).Font.Bold = true;
	oSheet.Range(oSheet.Cells(2, 1), oSheet.Cells(2, k)).Cells.Font.Bold = true
	oSheet.Range(oSheet.Cells(1,1), oSheet.Cells(1, k)).Merge();
	oSheet.Rows.VerticalAlignment = xlCenter;

	oSheet.Cells(1, 1).RowHeight += 5;
	oSheet.Cells(2, 1).RowHeight += 3;


	oSheet = null;
	oWB = null;
	oXL = null;
	idTmr = window.setInterval("Cleanup();",1);

	function Cleanup() {
		window.clearInterval(idTmr);
		CollectGarbage();
	};
	function TrimStr(s) {
		s = s.replace( /^\s+/g, '');
		return s.replace( /\s+$/g, '');
	}
}

var mkdocfile = function(){
	makedocfile();
}	
function makedocfile(){
	var items = [];
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selections = grid.getSelectionModel().getSelections();
	if(selections==null || selections.length==0) return;
	var param='act=export';	
	var i = 0;
	var groupId = 0;
	massiv=new Array();
	var countId =0;
	var countDoc = 0;

	if (ETD.fr!=9){
		for(var j=0; j<selections.length; j++)
		{
			if(selections[j].get('name')!='Пакет документов'&& 
					selections[j].get('name')!='Пакет документов ЦСС'){
				countDoc++;
			}else{
				countId++;
			}
		}

		if(countId == 0){
			param+='&groupDoc=';
			for(var j=0; j<selections.length; j++)
			{
				groupId=selections[j].get('id');
				param +=groupId;
				if(j!=selections.length-1){
					param+=';';
				}else{
					param+='';
				}
			}
		}else if(countDoc==0){

			param+='&groupId=';

			for(var j=0; j<selections.length; j++){
				groupId=selections[j].get('id');
				param+=groupId
				if(j!=selections.length-1){
					param+=';';
				}else{
					param+='';
				}
			}
		}else{

			for(var i = 0; i <2;i++){
				if(i==0){param+='&groupId=';}
				if(i==1){param+='&groupDoc=';}
				for(var j=0; j<selections.length; j++)
				{
					if(selections[j].get('name')!='Пакет документов' && 
							selections[j].get('name')!='Пакет документов ЦСС'){
						if(i==0){
							param+='';
						}else{
							groupId=selections[j].get('id');
							param +=groupId;
							if(j!=selections.length-1){
								param+=';';
							}else{
								param+='';
							}
						}
					}else{
						if(i==1){
							param+='';
						}else{
							groupId=selections[j].get('id');
							param +=groupId;
							if(j!=selections.length-1){
								param+=';';
							}else{
								param+='';
							}
						}
					}			
//					vvod
				}
			}
		}
		try {
//			alert(param);

			Ext.destroy(Ext.get('downloadIframe'));
		}
		catch(e) {}
		Ext.DomHelper.append(document.body, {
			tag: 'iframe',
			id:'downloadIframe',
			frameBorder: 0,
			width: 0,
			height: 0,
			css: 'display:none;visibility:hidden;height:0px;',
			src: 'forms/getDocument'+'?'+param
		});
	}
	if (ETD.fr==9){
		try {
			massiv=new Array();
			for(var j=0; j<selections.length; j++)
			{

				if (selections[j].get('name')!=='Пакет документов РТК'){
					Ext.MessageBox.alert('Ошибка','Выгрузка возможна только для документов типа "Пакет документов РТК"');	
					return;
				}


				param=param+'&'+'id'+j+'='+selections[j].get('id');

			};
			Ext.destroy(Ext.get('downloadIframe'));
		}
		catch(e) {}
		Ext.DomHelper.append(document.body, {
			tag: 'iframe',
			id:'downloadIframe',
			frameBorder: 0,
			width: 0,
			height: 0,
			css: 'display:none;visibility:hidden;height:0px;',
			src: 'forms/getDocumentRTK'+'?'+param
		});
	}

}

var exporttoex = function(){
//	alert(tpanel.getActiveTab().count);

	if (tpanel.getActiveTab().count<=15000){
		ETD.exporttoexcel();
	} else {
		Ext.MessageBox
		.alert('Внимание',
		'Невозможно выгрузить данное количество записей. Установите другие параметры фильтрации');
	}
}

var exportreportTOR = function(){
	ETD.exportTORreport();
}

var printPackage = function(){
	ETD.printPackage();
}

var downloadProp = function() {
	ETD.downloadProposal();
}

var acceptDocs= function(){
	ETD.acceptDocs();
}
var reportRTK= function(){
	ETD.reportRTK();
}
var reportPPS = function(){
	ETD.reportPPS();
}
var exporttoexsf = function(){
	ETD.exporttoexcelsf(name,depname,ETD.predid);
}
var signvrkdocs= function(){
	ETD.signvrkDocs();
}
var createPretension = function() {
	ETD.createPretension();
}
var exportTorFromArchive = function() {
	ETD.exportTorFromArchive();
}
var exportXML = function() {
	ETD.exportXML();
}

var exportPDF = function() {
	ETD.exportPDF();
}

var opendoc=function(){

	ETD.dropPackid();
	opendocOCO(activesaveOCO);

}
var dropdoc=function(utoch){
	ETD.setUtoch(utoch);
	var tpanel = Ext.getCmp('tpanel');
	if (tpanel.getActiveTab().getSelectionModel().hasSelection()){ 
		var status = tpanel.getActiveTab().getSelectionModel().getSelected().get('status');
		var selectname = tpanel.getActiveTab().getSelectionModel()
		.getSelected().get('name');

		if (selectname=="Пакет документов"&&status==1||selectname=="Пакет документов"&&status==3||selectname=="Пакет документов"&&status==4
				||selectname=="Пакет документов ЦСС"&&status==1||selectname=="Пакет документов ЦСС"&&status==3||selectname=="Пакет документов ЦСС"&&status==4
				||selectname.indexOf("чет-фактура")>-1&&status==1||selectname.indexOf("чет-фактура")>-1&&status==3||selectname.indexOf("чет-фактура")>-1&&status==4
				||selectname.indexOf("ГУ-23")>-1&&status==1||selectname.indexOf("ГУ-23")>-1&&status==3||selectname.indexOf("ГУ-23")>-1&&status==4
				||selectname.indexOf("ГУ-45")>-1&&status==1||selectname.indexOf("ГУ-45")>-1&&status==3||selectname.indexOf("ГУ-45")>-1&&status==4
				||selectname.indexOf("ГУ-2в")>-1&&status==1||selectname.indexOf("ГУ-2в")>-1&&status==3||selectname.indexOf("ГУ-2в")>-1&&status==4
				||selectname.indexOf("ГУ-2б")>-1&&status==1||selectname.indexOf("ГУ-2б")>-1&&status==3||selectname.indexOf("ГУ-2б")>-1&&status==4){
			Ext.MessageBox.alert('Сообщение', 'Нельзя отклонить данный документ');
		}
//		else if (ETD.fr==2&&ETD.dropdocsroles23.indexOf(selectname)==-1
//		||ETD.fr==3&&ETD.dropdocsroles23.indexOf(selectname)==-1
//		||ETD.fr==17&&ETD.dropdocsroles23.indexOf(selectname)==-1){
//		Ext.MessageBox.alert('Сообщение', 'Нельзя отклонить данный документ');
//		}

		else if (tpanel.getActiveTab().getId() == 'ID_DOCUMENT_GRID'
			||tpanel.getActiveTab().getId() == 'ID_SEND_GRID'&&selectname=="Пакет документов"
				||tpanel.getActiveTab().getId() == 'ID_SEND_GRID'&&selectname=="Пакет документов ЦСС"
					||tpanel.getActiveTab().getId() == 'ID_ARCHIVE_GRID'&&selectname.indexOf("чет-фактура")>-1
					||selectname=="Пакет документов РТК"||selectname=="Счет-фактура ЦСС"){




			if (ETD.dropdocs.indexOf(selectname)>-1&&utoch==false||selectname.indexOf("чет-фактура")>-1&&utoch==true){
				var mess = '';
				var docid;
				if (ETD.transpackid>0){
					docid = ETD.transpackid;
					ETD.dropTransPackid();
				} else {
					docid = tpanel.getActiveTab().getSelectionModel()
					.getSelected().get('id');
				}
				if (status>-1){

					mess = document.applet_adapter.getDocStatus(docid).split('&&')[1];

				}
				var title='';
				var msg='';
				var btnok='ok';

				if (selectname.indexOf("чет-фактура")>-1&&utoch==true){
					title = 'Запрос на уточнение счета-фактуры';
					msg = 'Комментарий по уточнению:';
				}

				else {
					title = 'Отклонение документа';
					msg = 'Введите причину отклонения:';
				}

				if(selectname=="Пакет документов")
				{
					title = 'Мотивированный отказ';
					DPD.deviationDoc(title, mess, docid);

				}
				else if(selectname=="Пакет документов ЦСС")
				{
					var record= tpanel.getActiveTab().getStore().find('id',docid);
					var status = tpanel.getActiveTab().getStore().getAt(record).get('status');

					var mess = '';

					Ext.MessageBox.show({
						title:'<div style="margin-left:15px;text-align:center;">Отклонение документа</div>',
						msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">Введите причину отклонения:</div>',
						buttons: Ext.Msg.OKCANCEL,
						value: mess,
						fn:dropfn,
						resizable: true,
						minWidth: 600,
						animEl: 'elId',
						multiline: true
					});
				}

				else{
					Ext.MessageBox.show({



						title:'<div style="margin-left:15px;text-align:center;">'+title+'</div>',
						msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">'+msg+'</div>',
						buttons: {ok: btnok, cancel: 'Отмена'},
						value: mess,
						fn:dropfn,
						resizable: true,
						width: 600,
						animEl: 'elId',
						multiline: true
					});
				}

			}
			else if (utoch==false){
				Ext.MessageBox
				.alert('Ошибка',
				'Нельзя отклонить данный документ');
			}

			else if (utoch==true){
				Ext.MessageBox
				.alert('Ошибка',
				'Только для счет-фактур');
			}

		}else if (utoch==false){
			Ext.MessageBox
			.alert('Ошибка',
			'Нельзя отклонить данный документ');
		}

		else if (utoch==true){
			Ext.MessageBox
			.alert('Ошибка',
			'Только для счет-фактур');
		}

	} else if (ETD.packid>0){

		var record= tpanel.getActiveTab().getStore().find('id',ETD.packid);
		var status = tpanel.getActiveTab().getStore().getAt(record).get('status');
		var title ='Отклонение документа';
		var mess = '';
		var docid=ETD.packid;
		if (status>-1){

			mess = document.applet_adapter.getDocStatus(docid).split('&&')[1];
		}
		DPD.deviationDoc(title, mess, docid);
	}
	else if (ETD.packRTKid>0){
		var record= tpanel.getActiveTab().getStore().find('id',ETD.packRTKid);
		// alert(record);
		var status = tpanel.getActiveTab().getStore().getAt(record).get('status');

		var mess = '';

		Ext.MessageBox.show({
			title:'<div style="margin-left:15px;text-align:center;">Отклонение документа</div>',
			msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">Введите причину отклонения:</div>',
			buttons: Ext.Msg.OKCANCEL,
			value: mess,
			fn:dropfn,
			resizable: true,
			minWidth: 600,
			animEl: 'elId',
			multiline: true
		});
	}
	else if (ETD.packCSSid>0){

		var record= tpanel.getActiveTab().getStore().find('id',ETD.packCSSid);
		var status = tpanel.getActiveTab().getStore().getAt(record).get('status');

		var mess = '';

		Ext.MessageBox.show({
			title:'<div style="margin-left:15px;text-align:center;">Отклонение документа</div>',
			msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">Введите причину отклонения:</div>',
			buttons: Ext.Msg.OKCANCEL,
			value: mess,
			fn:dropfn,
			resizable: true,
			minWidth: 600,
			animEl: 'elId',
			multiline: true
		});

	}
}

var dropfn = function(btn, text) {
	var select  = tpanel.getActiveTab().getSelectionModel()
	.getSelected();
	if (btn == 'ok'){
		if (text.length > 0){
			//для мотиворованного	        	
//			if (text.length > 2000&&select.get('name')=='Пакет документов'){
//			Ext.MessageBox
//			.alert('Ошибка',
//			'Причина отказа не должна привышать 2000 символов.');
//			}else
			//   	

			if (text.length > 500&&select.get('name')!='Счет-фактура'){
				Ext.MessageBox
				.alert('Ошибка',
				'Причина отклонения не должна привышать 500 символов.');
			} 

			else if (text.length > 500&&select.get('name')=='Счет-фактура'){
				Ext.MessageBox
				.alert('Ошибка',
				'Запрос на уточнение не должен привышать 500 символов.');
			}    

			else {
				if (tpanel.getActiveTab().getId() == 'ID_DOCUMENT_GRID'||tpanel.getActiveTab().getId() == 'ID_SEND_GRID'||tpanel.getActiveTab().getId() == 'ID_ARCHIVE_GRID')
					tab = 0;
				else
					tab = 1;
				var selected = tpanel.getActiveTab().getSelectionModel()
				.getSelected();
				ETD.busy();
				(function(){
					var decline = true;    		        	
					var docid;
					if (ETD.packid>0){
						docid=ETD.packid;
						decline=false;
						ETD.dropPackid();
						(ETD.dropMOfn(text, docid)).defer(1);	

					}         
					else if (ETD.transpackid>0){
						docid = ETD.transpackid;
					}

					else {
						docid = selected
						.get('id');
					}        

					if (tpanel.getActiveTab().getSelectionModel().hasSelection()){	

						if (select.get('name').indexOf("чет-фактура")>-1&&ETD.utochnenie){

							Ext.Ajax.request({
								url : 'forms/insertDfKorr',
								params : {
									docid : docid

								}
							});
//							ret = eval('('+document.applet_adapter.dropSF(text, docid) + ')');
						}



//						для мотивированного
						if (select.get('name')=='Пакет документов'&&decline||ETD.transpackid>0){
							ETD.dropTransPackid();
							decline=false;
							(ETD.dropMOfn(text, docid)).defer(1);

						}	
					}

					if (decline){
						var resp = eval('('
								+ document.applet_adapter.dropdoc(text, tab, docid) + ')');
						if (resp.error){
							Ext.MessageBox.alert('Ошибка!', resp.error);
						}
						else
							if (tab == 0){
								tpanel.getActiveTab().getStore().reload();

							}
					}


					ETD.ready();
				}).defer(1);
			}
		}
		else if (select.get('name').indexOf("чет-фактура")=-1){
			Ext.MessageBox
			.alert('Ошибка',
			'Документ не отклонен. Необходимо ввести причину отклонения.');
		}

		else if (select.get('name').indexOf("чет-фактура")>-1){
			Ext.MessageBox
			.alert('Ошибка',
			'Запрос не отправлен. Необходимо ввести текст запроса на уточнение.');
		}
	}


	else if (btn=='cancel'){
		if (ETD.packid>0)  {
			Pack.mkpackwin(ETD.packid);
		}             
		if (ETD.transpackid>0){
			ETD.dropTransPackid();
		}
	}
}; 


function opendocOCO (activesave){
//	alert('opendocOCO');
	var row = tpanel.getActiveTab().getSelectionModel().getSelected();
	var tabname = Ext.getCmp('tpanel').getActiveTab().getId();


	if (
//			tabname=='ID_FIN_GRID'&&row.get('name')=='Пакет документов'	||
			tabname=='ID_SEND_GRID'&&row.get('name')=='Пакет документов'
				||tabname=='ID_ARCHIVE_GRID'&&row.get('name')=='Пакет документов'
					||tabname=='ID_DOCUMENT_GRID'&&row.get('name')=='Пакет документов'&&ETD.fr>1){
		if (tabname=='ID_DOCUMENT_GRID'&&row.get('name')=='Пакет документов'){
			ETD.firstpack(row.get('id'));
		}
//		alert('else');
		ETD.setPackid(row.get('id'));
		Pack.req = row.get('id');
		Pack.mkpackwin(row.get('id'));


	} 

	else if (row.get('name')=='Пакет документов РТК'){
		ETD.setPackRTKid(row.get('id'));
		PackRTK.req = row.get('id');
		PackRTK.mkPackRTKwin(row.get('id'));
	}

//	else if (tabname=='ID_SEND_GRID'&&row.get('name')=='Пакет документов'){
//	alert('111');
//	ETD.openDocument(row.get('id'), row.get('name'),null, isSend);	
//	}

	else {
		if (tabname=='ID_DOCUMENT_GRID'&&row.get('name')=='Пакет документов'){
			ETD.firstpack(row.get('id'));
		}
		var isSend = 0;
		if (Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_ARCHIVE_GRID'&&row.get('name')=='Счет-фактура'
			||Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_SEND_GRID'){
			isSend = 1;
		}
		ETD.openDocument(row.get('id'), row.get('name'),null, isSend);	

	}
};

function openHistory (){

	if (tpanel.getActiveTab().getSelectionModel().hasSelection())	
	{
		var row = tpanel.getActiveTab().getSelectionModel().getSelected();

		if (row.get('name')=='Пакет документов'){

			History.mkHistorywin(row.get('vagnum'), row.get('reqdate'),row.get('id'));
		}
		else {
			Ext.MessageBox
			.alert('Сообщение',
			'Просмотр возможен только для пакетов документов.');
		}
	}

	else if (ETD.packid>0){
		Ext.Ajax.request({
			url : 'forms/vagnum',
			params : {
				docid : ETD.packid
			},
			callback : function(options, success, response) {
				var resp = response.responseText;
				var arr = Ext.util.JSON.decode(resp);

				if (arr[0].success == true) {
					History.mkHistorywin(arr[0].vagnum, arr[0].repdate, ETD.packid);
				} else {
					Ext.MessageBox
					.alert('Ошибка',
					'Ошибка при открытии истории ремонта');
				}
			}
		});
	}
}


function opendocSNT (){

	var tpanel = Ext.getCmp('tpanel');
	var index = tpanel.items.indexOf(tpanel.getActiveTab());
	if(tpanel.getActiveTab().getSelectionModel().hasSelection()){
		var selected = tpanel.getActiveTab().getSelectionModel().getSelected();
		busy();
		(function(){
			ret = eval('('+document.applet_adapter.opendocSNT(selected.get('rownum'),index,activesaveSNT,updateSNT)+')');
			ready();
			if(ret.refresh) Refresh();
			else if(ret.error) Ext.MessageBox.alert('',ret.error);
			else if(ret.declined) 
				Ext.MessageBox.alert('Причина отклонения',ret.declined, function(btn){CreateOpenDoc();});		
			else CreateOpenDoc();
		}).defer(1);
	}
};
function opendoc_addonbase (ocoorsnt) {
	var tpanel = Ext.getCmp('tpanel_test');
	if(tpanel.getActiveTab().getSelectionModel().hasSelection()){
		var selected = tpanel.getActiveTab().getSelectionModel().getSelected();
		busy();
		(function(){
			ret = eval('('+document.applet_adapter.opendocOCO(selected.get('rownum'),fr_addonbaseid)+')');
			ready();
			if(ret.refresh) Refresh();
			else if(ret.error) Ext.MessageBox.alert('',ret.error);
			else if(ret.declined) 
				Ext.MessageBox.alert('Причина отклонения',ret.declined, function(btn){CreateOpenDoc();});		
			else CreateOpenDoc();
		}).defer(1);
	}
};

var inittabpanel = function(){

	var tpanel = Ext.getCmp('tpanel');
	if (ETD.fr=='1'){
		Ext.getCmp('repfilter').enable();
		Ext.getCmp('tpanel').add(ETDDoc.grid);
		Ext.getCmp('tpanel').add(ETDSend.grid);
		Ext.getCmp('tpanel').add(ETDDecl.grid);
		Ext.getCmp('tpanel').add(ETDArch.grid);
		Ext.getCmp('exportexcelsf').show();
		Ext.getCmp('reportTOR').show();
		Ext.getCmp('printPackage').show();
		Ext.getCmp('reportTorFromArchive').show();
		if(ETD.pdfcheck==true)
		{Ext.getCmp('exportPDF').show();}
		else
		{Ext.getCmp('exportPDF').enable();}
		Ext.getCmp('export').enable();

	}
	else if(ETD.fr=='2'){
		tpanel.add(ETDDocR2.grid);
		tpanel.add(ETDSend.grid);
		tpanel.add(ETDDecl.grid);
		tpanel.add(ETDArch.grid);
		Ext.getCmp('drop').disable();
		Ext.getCmp('exportexcelsf').show();
		Ext.getCmp('reportTOR').show();
		Ext.getCmp('printPackage').show();
		Ext.getCmp('reportTorFromArchive').show();
		if(ETD.pdfcheck==true)
		{Ext.getCmp('exportPDF').show();}
		else
		{Ext.getCmp('exportPDF').enable();}
	}

	else if (ETD.fr=='3'){
		tpanel.add(ETDDocR2.grid);
		tpanel.add(ETDSend.grid);
		tpanel.add(ETDDecl.grid);
		tpanel.add(ETDArch.grid);
		Ext.getCmp('drop').disable();
		Ext.getCmp('exportexcelsf').show();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').show();
		if(ETD.pdfcheck==true)
		{Ext.getCmp('exportPDF').show();}
		else
		{Ext.getCmp('exportPDF').enable();}
		Ext.getCmp('reportTorFromArchive').show();
	}
	else if (ETD.fr=='5'||ETD.fr=='11'||ETD.fr=='12'||ETD.fr=='4'){
		tpanel.add(ETDDocMRM.grid);
		tpanel.add(ETDSendMRM.grid);
		tpanel.add(ETDDeclMRM.grid);
		tpanel.add(ETDArchMRM.grid);
		Ext.getCmp('createbut').enable();
		Ext.getCmp('repfilter').hide();
		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
	} else if (ETD.fr=='6'){
		tpanel.add(ETDDocR2.grid);
		tpanel.add(ETDSend.grid);
		tpanel.add(ETDDecl.grid);
		tpanel.add(ETDArch.grid);
		Ext.getCmp('createbut').disable();
//		Ext.getCmp('drop').disable();
		Ext.getCmp('drop').hide();
		Ext.getCmp('export').disable();
		Ext.getCmp('utoch').disable();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').show();
		Ext.getCmp('reportTOR').hide();
		if(ETD.pdfcheck==true)
		{Ext.getCmp('exportPDF').show();}
		else
		{Ext.getCmp('exportPDF').enable();}

	}
	else if (ETD.fr=='7'){
		tpanel.add(ETDDocPPS_Role2.grid);
		tpanel.add(ETDSendPPS2.grid);
		tpanel.add(ETDArchPPS2.grid);
		Ext.getCmp('drop').hide();
		Ext.getCmp('repfilter').hide();
		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('downloadProposal').show();
		Ext.getCmp('reportPPS').show();
	}
	else if (ETD.fr=='19'){
		tpanel.add(ETDDocPPS.grid);
		tpanel.add(ETDSendPPS.grid);
		tpanel.add(ETDArchPPS.grid);
		Ext.getCmp('drop').hide();
		Ext.getCmp('repfilter').hide();
		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('downloadProposal').hide();
		Ext.getCmp('reportPPS').hide();
	}
	else if (ETD.fr=='9'){
		tpanel.add(ETDDocRTK.grid);
		tpanel.add(ETDSendRTK.grid);
		tpanel.add(ETDDeclRTK.grid);
		tpanel.add(ETDArchRTK.grid);
		Ext.getCmp('repfilter').hide();
//		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
//		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('drop').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('reportRTK').show();
	}
	else if (ETD.fr=='10'){
		Ext.getCmp('repfilter').enable();
		Ext.getCmp('tpanel').add(ETDDoc.grid);
		Ext.getCmp('tpanel').add(ETDSend.grid);
		Ext.getCmp('tpanel').add(ETDDecl.grid);
		Ext.getCmp('tpanel').add(ETDArch.grid);
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('export').enable();
	}
	else if (ETD.fr=='13'){
		tpanel.add(ETDArch.grid);
		Ext.getCmp('drop').disable();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
	}
	else if (ETD.fr=='15'||ETD.fr=='16'){
		tpanel.add(ETDDocRZDS.grid);
		tpanel.add(ETDSendRZDS.grid);
		tpanel.add(ETDDeclRZDS.grid);
		tpanel.add(ETDArchRZDS.grid);

		Ext.getCmp('repfilter').disable();
		Ext.getCmp('view').disable();
		Ext.getCmp('history').disable();
		Ext.getCmp('sfpack').disable();
		Ext.getCmp('packview').disable();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
	}
	else if (ETD.fr=='14'){
		tpanel.add(ETDDocVRK.grid);
		tpanel.add(ETDSendVRK.grid);
		tpanel.add(ETDDeclVRK.grid);
		tpanel.add(ETDArchVRK.grid);
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('repfilter').hide();
//		Ext.getCmp('export').hide();
//		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
//		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();

	}else if (ETD.fr=='17'){
		Ext.getCmp('repfilter').enable();
		Ext.getCmp('tpanel').add(ETDDocCSS.grid);
		Ext.getCmp('tpanel').add(ETDSendCSS.grid);
		Ext.getCmp('tpanel').add(ETDDeclCSS.grid);
		Ext.getCmp('tpanel').add(ETDArchCSS.grid);
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('repfilter').hide();     
		Ext.getCmp('acceptDocs').hide();
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('export').enable();
	}
	else if (ETD.fr=='18' ||ETD.fr=='8'){
		tpanel.add(ETDDocPPS_Role2.grid);
		tpanel.add(ETDSendPPS2.grid);
		tpanel.add(ETDArchPPS2.grid);
		Ext.getCmp('repfilter').hide();
		Ext.getCmp('drop').hide();
		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
	}	
	else if (ETD.fr=='20'){
		Ext.getCmp('repfilter').enable();
		Ext.getCmp('tpanel').add(ETDDocSignVRK.grid);
		Ext.getCmp('tpanel').add(ETDDeclSignVRK.grid);
		Ext.getCmp('tpanel').add(ArchiveGridSignVRK.grid);
		Ext.getCmp('export').enable();

		for (var a=0; a <Ext.getCmp('toppanell').items.get(0).items.getCount() ; a++){
			Ext.getCmp('toppanell').items.get(0).items.get(a).hide();
		}
		Ext.getCmp('toppanell').items.get(0).items.get('drop').show();	
		Ext.getCmp('toppanell').items.get(0).items.get('packsgn').show();	


	} else if (ETD.fr=='21'){
		tpanel.add(ETDDocVRK.grid);
		tpanel.add(ETDSendVRK.grid);
		tpanel.add(ETDDeclVRK.grid);
		tpanel.add(ETDArchVRK.grid);
		Ext.getCmp('exportexcelsf').hide();
		Ext.getCmp('repfilter').hide();
		Ext.getCmp('export').hide();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
//		Ext.getCmp('exportdoc').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('reportTOR').hide();
		Ext.getCmp('printPackage').hide();
		Ext.getCmp('drop').hide();
	}else if(ETD.fr=='22'){
		Ext.getCmp('repfilter').enable();
		Ext.getCmp('tpanel').add(ETDDoc.grid);
		Ext.getCmp('tpanel').add(ETDSend.grid);
		Ext.getCmp('tpanel').add(ETDDecl.grid);
		Ext.getCmp('tpanel').add(ETDArch.grid);
		Ext.getCmp('exportexcelsf').show();
		Ext.getCmp('reportTOR').show();
		Ext.getCmp('printPackage').show();
		Ext.getCmp('drop').show();
		Ext.getCmp('view').hide();
		Ext.getCmp('history').hide();
		Ext.getCmp('sfpack').hide();
		Ext.getCmp('utoch').hide();
		Ext.getCmp('packview').hide();
		Ext.getCmp('createbut').hide();
		Ext.getCmp('export').enable();
	}
	if (ETD.fr=='5'){
		Ext.getCmp('reportPPS').show();
	}
	tpanel.setActiveTab(tpanel.items.first());
	ETD.tabname = tpanel.getActiveTab().getId();

};


getAfterDate = function(){
	var filters;
	if	(tpanel_or_tpanel_test==1)  filters = Ext.getCmp('tpanel_test').getActiveTab().getFilter();
	else filters = Ext.getCmp('tpanel').getActiveTab().getFilter();
	if (filters.isActiveAft()) return filters.getAftCbx().menu.picker.getValue();
	else return null;
};
getBeforeDate = function(){
	var filters;
	if	(tpanel_or_tpanel_test==1)  filters = Ext.getCmp('tpanel_test').getActiveTab().getFilter();
	else filters = Ext.getCmp('tpanel').getActiveTab().getFilter();
	if (filters.isActiveBef()) return filters.getBefCbx().menu.picker.getValue();
	else return null;
};


function saveandnoexit()
{
	// alert ("savecall");

	var saveresp = document.applet_adapter.saveDocNoExit('0');
//	console.log('saveresp = ' + saveresp);
	if (saveresp.indexOf('Документ сохранен успешно')==-1)
		alert(saveresp.split(',')[0]);
	return saveresp;
}


var droptorg =function()	{

	var title='';
	var msg='';
	var btnok='Ок';

	title = 'Браковка документа';
	msg = 'Введите причину отклонения:';


	Ext.MessageBox.show({
		title:'<div style="margin-left:15px;text-align:center;">'+title+'</div>',
		msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">'+msg+'</div>',
		buttons: {ok: btnok, cancel: 'Отмена'},
//		value: mess,
		fn:droptorg12,
//		fn:dropfn,
		resizable: true,
		minWidth: 600,
		animEl: 'elId',
		multiline: true
	});

}


var droptorg12 = function(btn, text) {
	var select  = tpanel.getActiveTab().getSelectionModel()
	.getSelected();

	if (btn == 'ok'){
		if (text.length > 0){


			if (text.length > 500){
				Ext.MessageBox
				.alert('Ошибка',
						'Причина отклонения не должна привышать 500 символов.');
			} 


			else {

				ETD.busy();
				(function(){

//					Ext.Ajax.request({
//	url : 'forms/torg12drop.interface',
//					params : {
//					docid : ETD.droptorgid,
//					reason: text
//					},
//					callback : function(options, success, response) {
//					var resp = response.responseText;
//					var arr = Ext.util.JSON.decode(resp);

//					if (arr == 'true') {
//					ETD.droptorgid = 0;
//					} else {
//					Ext.MessageBox
//					.alert('Ошибка',
//					'Ошибка при браковке документа');
//					}


//					}
//					});
//					alert('test');
					var resp = eval('('
							+ document.applet_adapter.dropdoc(text, 0, ETD.droptorgid) + ')');
//					alert(resp);
					if (resp.error){
						Ext.MessageBox.alert('Ошибка!', resp.error);
					}


					else   {
						tpanel.getActiveTab().getStore().reload();
						ETD.droptorgid = 0;
					}
					ETD.ready();
				}).defer(1);
			}
		}

		else {
			Ext.MessageBox
			.alert('Ошибка',
			'Документ не забракован. Необходимо ввести причину браковки');
		}
	}


	else if (btn=='cancel'){

	}
}; 


var dropfpu26pps =function()	{

	var title='';
	var msg='';
	var btnok='Ок';

	title = 'Браковка документа';
	msg = 'Введите причину мотивированного отказа:';


	Ext.MessageBox.show({
		title:'<div style="margin-left:15px;text-align:center;">'+title+'</div>',
		msg: '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">'+msg+'</div>',
		buttons: {ok: btnok, cancel: 'Отмена'},
		//   value: mess,
		fn:dropfpu26ppsfn,
		resizable: true,
		minWidth: 600,
		animEl: 'elId',
		multiline: true
	});

}


var dropfpu26ppsfn = function(btn, text) {
	var tpanel = Ext.getCmp('tpanel');
	if (btn == 'ok'){
		if (text.length > 0){


			if (text.length > 500){
				Ext.MessageBox
				.alert('Ошибка',
						'Причина отклонения не должна привышать 500 символов.');
			} 


			else {

				tab = 0;

				ETD.busy();
				(function(){

					Ext.Ajax.request({
						url : 'forms/fpu26ppsdrop.interface',
						params : {
							docid : ETD.fpu26ppsdropid,
							reason: text
						},
						callback : function(options, success, response) {
							var resp = response.responseText;
							var arr = Ext.util.JSON.decode(resp);

							if (arr == 'true') {
								ETD.fpu26ppsdropid = 0;
								tpanel.getActiveTab().getStore().reload();

							} else {
								Ext.MessageBox
								.alert('Ошибка',
								'Ошибка при браковке документа');
							}
						}
					});
					ETD.ready();
				}).defer(1);
			}
		}

		else {
			Ext.MessageBox
			.alert('Ошибка',
					'Документ не забракован. Необходимо ввести причину браковки');
		}
	}


	else if (btn=='cancel'){

	}
}; 


