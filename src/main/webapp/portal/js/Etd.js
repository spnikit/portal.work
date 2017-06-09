var applet_adapter;
var name;
var rolename;
var dorname;
var predname;
var before = new Date();
var after = new Date(new Date().getTime()-172800000);

Ext.QuickTips.init();

function SayIt(str,unlock){
if (unlock=='1') Ext.MessageBox.alert('Ошибка',str);
else if (unlock=='0') alert('Ошибка\n'+str);
}




var store,store_arch,comboViewStore,comboCreateStore, comboView,comboReportStore,gridpanel_arch,tpanel, datepickerplusmenu_1;
var total_num;

var cpanel,opendoc,dropdoc,toppanel,toolbarpanel,btnwidth,datewin;

var reportname='';
Ext.onReady(function(){
	ETD.busy();
	if(Ext.isIE6){
		document.getElementById('loading').style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='loadpic.png', sizingMethod='scale')";
		document.getElementById('loading').src=Ext.BLANK_IMAGE_URL;
	}
	Ext.get(document.getElementsByTagName('body')[0]).addClass('white-body');
	try{
	/**
	* for viewport
	*/
	btnwidth=150;

		
	comboViewStore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
	comboCreateStore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
	comboReportStore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
  	comboDorStore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'name']
	});
  	comboPredprStore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'name']
	});
	
	comboView = new Ext.form.ComboBox({
    store: comboViewStore,
		width:btnwidth,
		displayField:'type',
		id:'combosel',
		labelSeparator:'',
		fieldLabel: '<span class="my-label x-unselectable" onselectstart="return false">Тип документа:</span>',
		hidden: false,
		editable: false,
    	mode: 'local',
		triggerAction: 'all',
    	selectOnFocus: true
	});
	
	
	datepickerplusmenu_1 =  new Ext.menu.DateMenu({
		id: 'datepickerplusmenu_1',
		usePickerPlus	: true,
		noOfMonth : 1 ,
		multiSelection: true,
		strictRangeSelect : true,
		markNationalHolidays: false,
        multiSelectByCTRL: true,
        maxSelectionDays : 31,
		useQuickTips:false,
		showWeekNumber : false,
		selectedDates : [new Date(new Date().getTime()-172800000),new Date(new Date().getTime()-86400000),new Date()],
		__year:(new Date()).getYear(),
		__month:(new Date()).getMonth()+1,
		renderTodayButton:false,
		showToday:false,	//Ext 2.2 own option
		allClicked : function() {
			this.clearSelectedDates();
			Ext.getCmp('datepickerbutton').hideMenu();
			before = new Date('01/01/2100');
			after = new Date('01/01/2000');
			Ext.getCmp('datepickerbutton').setText('-   Все документы   -');
			ETD.busy();
			tpanel.items.each(function(item, index, length){ item.isLoaded = false});
			tpanel.getActiveTab().loadFirstPage();
			ETD.ready();
		},
		monthClicked : function() {
			this.clearSelectedDates();
			Ext.getCmp('datepickerbutton').hideMenu();
			before = new Date(new Date((this.__month+1)+'/01/'+this.__year).getTime()-86400000);
			after = new Date((this.__month)+'/1/'+this.__year);
			Ext.getCmp('datepickerbutton').setText(' '+after.format('j M Y')+'  -  '+before.format('j M Y')+' ');
			ETD.busy();
			tpanel.items.each(function(item, index, length){ item.isLoaded = false});
			tpanel.getActiveTab().loadFirstPage();
			ETD.ready();
		},
		handler : function(dp, date){
			if (date.length > 0){
			if (Ext.isDate(date)) {
				before = date;
				after = date;
				Ext.getCmp('datepickerbutton').setText(' '+date.format('j M Y')+'  -  '+date.format('j M Y')+' ');				
				ETD.busy();
				tpanel.items.each(function(item, index, length){ item.isLoaded = false});
				tpanel.getActiveTab().loadFirstPage();
				ETD.ready();
			}
			else {
				before = date[date.length-1];
				after = date[0];
				Ext.getCmp('datepickerbutton').setText(' '+date[0].format('j M Y')+'  -  '+date[date.length-1].format('j M Y')+' ');
				ETD.busy();
				tpanel.items.each(function(item, index, length){ item.isLoaded = false});
				tpanel.getActiveTab().loadFirstPage();
				ETD.ready();
			}
			}
		},
		listeners:{'aftermonthchange':function(dm,oldM,newM){
			dm.__month = newM+1;
			//alert(dm.__month);
			return true;
		},
		'afteryearchange':function(dm,oldY,newY){
			dm.__year = newY;
			return true;
		}}
	});
  
  	comboDor = new Ext.form.ComboBox({
    	store: comboDorStore,
		width:btnwidth,
		displayField:'name',
		id:'combdor',
		labelSeparator:'',
		fieldLabel: '<span id="dorLabel" class="my-label x-unselectable" onselectstart="return false">Дорога:</span>',
//		hidden: true,
		editable: true,
		forceSelection:true,
	    mode: 'local',
		triggerAction: 'all',
    	selectOnFocus: true,
    	disabled:true,
    	typeAhead: true,
    	onHide: function(){this.getEl().up('.x-form-item').setDisplayed(false);},
    	onShow: function(){this.getEl().up('.x-form-item').setDisplayed(true);}
	});
	
	comboDor.hide();
		  
  	comboPredpr = new Ext.form.ComboBox({
	    store: comboPredprStore,
		width:btnwidth,
		displayField:'name',
		id:'combpred',
		labelSeparator:'',
		fieldLabel: '<span class="my-label x-unselectable" onselectstart="return false">Предприятие:</span>',
//		hidden: true,
		editable: true,
		forceSelection:true,
    	mode: 'local',
		triggerAction: 'all',
	    selectOnFocus: true,
	    typeAhead: true,
    	onHide: function(){this.getEl().up('.x-form-item').setDisplayed(false);},
    	onShow: function(){this.getEl().up('.x-form-item').setDisplayed(true);}
//	   	disabled:true
	});
	
	comboPredpr.hide();

	tpanel = new Ext.TabPanel({
		autoheight:true,
		region:'center',
		id:'tpanel',
		margins:'0 10 10 10',
		activeTab: '0',
		items: [ETDDoc.grid,ETDInwork.grid, ETDArch.grid, ETDDrop.grid]
	});



//Верхняя панель
	toolbarpanel = new Ext.Panel({
		height: 80,
		region: 'north',
		layout: 'column',
		margins: '10 10 10 10',
		bodyBorder: false,
		border: false,
		items: [{
			xtype:"panel",
			border: false,
			width: btnwidth+10,
			items: [{xtype:'panel',border:false,html:'<div id="button"><input type="text" name="combocr" id="combocr" style="height: 16px; "></input></div>', style:'margin-left: 5px; margin-top:5px; height: 21px;'},
				{xtype:"button",id:'open',text:"Открыть документ",minWidth: btnwidth,handler:ETD.onOpenDocument,style:'margin-left: 5px;margin-top:5px;'},
				{xtype:'button',id:'drop',text:'Отклонить документ',minWidth:btnwidth,handler:ETD.dropDocument,style:'margin-left: 5px;margin-top:5px;',disabled:true}]
		},{
			xtype:"panel",
			width: btnwidth+10,
			id:'yopt',
			border: false,
			items: [{xtype:'panel',border:false,html:'<div id="buttonrep"><input type="text" name="comborep" id="comborep" style="height: 16px; "></input></div>', style:'margin: 5px;'},
				{xtype:'button',id:'createFrom',text:'Документ с данными', minWidth:btnwidth,handler:ETD.createDocumentFromData,style:'margin-left: 5px;margin-top:5px;'}]
		},{
			xtype:"form",
			width: btnwidth+105,
      		style:'margin-top: 5px;',
			border: false,
			items: [comboDor, comboPredpr]
		},/*{
			xtype:"panel",
			width: btnwidth+10,
			//id:'id_createFromPamel',
			border: false,
			items: [{xtype:'button',id:'createFrom',text:'Документ с данными', minWidth:btnwidth,handler:ETD.createDocumentFromData,style:'margin-left: 5px;margin-top:5px;'}]
		},*/{
			border: false,
			xtype:'panel',
			html: '<img src="'+Ext.BLANK_IMAGE_URL + '"></img>',
			columnWidth: 1
		},{
			xtype:"form",
			width: btnwidth+105,
			align:"right",
			style:'margin-top: 5px; margin-right:5px;',
			border: false,
			items: [comboView,
					new Ext.Button({
					 	text : ' '+datepickerplusmenu_1.selectedDates[0].format('j M Y')+'  -  '+datepickerplusmenu_1.selectedDates[datepickerplusmenu_1.selectedDates.length-1].format('j M Y')+' ',
                    	id : 'datepickerbutton',
                     	menu: datepickerplusmenu_1
					})]
		}		]
	});
	toppanel = new Ext.Panel({
		region:'north',
		layout:'column',
		height: tph,
		id: 'toppanell',
		style:'margin-left:15px;margin-top:15px; padding-right:25px;',
		animCollapse:false,
		collapsed:true,
		bodyStyle: 'background-color: transparent;',
		border:false,
		items:[{xtype:'panel',
			width:283,
			html:'<img id="small-logo" src="logo.png" height="45" width="283"></img>',
			border:false,
			bodyStyle: 'background-color: transparent; '
		},{xtype:'panel',
			bodyStyle: 'background-color: transparent;',
			border:false,
			columnWidth:1,
			html:'<img src="'+Ext.BLANK_IMAGE_URL+'"></img>'
		},{xtype:'label',		
			html:'<span class="my-label x-unselectable" onselectstart="return false" style="float: right;margin-right:5px;"><div style="background: url(i/bl.gif) 0 100% no-repeat #a3b0b8;"><div style="background: url(i/br.gif) 100% 100% no-repeat;"><div style="background: url(i/tr.gif) 100% 0 no-repeat;"><div style="background: url(i/tl.gif) 0 0 no-repeat; padding:3px; min-width: 350px;"><div id="name" style="text-align: center; padding=3px;">Ответственное лицо: </div><div id="rolename" style="text-align: center; padding: 3px; ">Роль: </div><div id="predname" style="text-align: center; padding: 3px; ">Предприятие: </div></div></div></div></div></span>',
			border:false,
//			style:'position:absolute; right:0px;top:0px;',
			bodyStyle: 'background-color: transparent;'
		}]
	});
	tph=tph+5;
	document.getElementById('tr1').height=tph;
	cpanel = new Ext.Panel({
//		allowDomMove:false,
		region: 'center',
//		applyTo:'cpanel',
		animCollapse:false,
		id: 'cpanel',
//		frame:true,
		margins: '10 10 15 10',
		bodyBorder: false,
		border: true,
		layout: 'border',
		items: [toolbarpanel,tpanel],
		title: '<span style="float: left; margin-left: 20px;">Список документов</span><span id="total_number" style="float: right;margin-right: 20px">Всего документов: </span>'
	});
new Ext.Viewport({
		layout: 'border',
		renderTo: Ext.getBody(),
		id:'main',
		items: [toppanel,cpanel]
		});
	Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
	Ext.get(document.getElementsByTagName('body')[0]).removeClass('x-border-layout-ct');
	
	
	cpanel.collapse();
	cpanel.hide();
	toppanel.hide();
	if(Ext.isIE6)  Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');

	/**
	* end
	*/
	}catch(e){return}
	applet_adapter = document.applet;
	//documentGrid.setApplet(document.applet);

	if (applet_adapter == null ) 
	{
		ETD.ready();
		Ext.MessageBox.alert('Error','java applet not found');
	}
	else 
	(function(){
		var updated=eval('('+applet_adapter.Updated()+')');
		if(updated.updated)
		{
		login();
		} else{	ETD.ready(); Ext.MessageBox.alert('Внимание','Необходимо обновить программное обеспечение, версия <a href="'+updated.url+'">'+updated.version+'</a>');}
	}).defer(1)
});

function login(){

				if(eval('('+applet_adapter.login(' ')+')').value)
				{
//				ETD.busy();
				select_depo();
				}else{
				ETD.ready();
				Ext.MessageBox.alert('Ошибка','необходима авторизация',function(btn){Refresh();});
				
				};

		ETD.ready();
};

var show_news = function () {
	
	var hasnews = applet_adapter.HasNews();
	if(!hasnews)
		return;
	var message = applet_adapter.GetNews(); // сюда кидаем сообщение сервера
	on_ok = function(){
		win_news.destroy();
		applet_adapter.DeleteNews();
	}
	
	var text_data = new Ext.form.Label({
		text: message
		});
	
	var win_news = new Ext.Window({
		title: 'Сообщение:',
		layout:'fit',
		width: 400,
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
	var json = eval('('+applet_adapter.getWorkPosition()+')');
	Dev.logger.log("Select depo : return ",  json);
	if(json.error){
		//ETD.ready();
		Ext.MessageBox.alert("Ошибка!",json.error,function(btn){Refresh();});
	}else{
		if(json.name && json.rolename){
			predname=json.predname
			dorname=json.dorname;
			name=json.name;
			rolename=json.rolename;
			mainwin();
			if(!predname){
				ETD.busy();
	        	if(!dorname){
	        		Ext.getCmp('combdor').enable();
	        	}
		        var predprs = eval('('+applet_adapter.getPredprs()+')');
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
			//Auto size
			var Size=333;
			var MaxLen=0;
			var WinSize=Ext.getBody().getWidth();
			
			for (i=0;i<json.values.length;i++)
			{
				
			if (json.values[i].length>MaxLen)	MaxLen=json.values[i].length;
			}
			
			if (MaxLen*6>WinSize) MaxLen=(WinSize-200)/6;
			//Auto size
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
					json = eval('('+applet_adapter.setWorkPosition(combopred.getValue())+')');
					
					name = json.name;
					rolename=json.rolename;
					dorname=json.dorname;
					predname=json.predname;
					mainwin();
					if(!predname){
						ETD.busy();
						Ext.getCmp('createButton').disable();
						Ext.getCmp('reportButton').disable();
						Ext.getCmp('createFrom').disable();
			        	if(!dorname){
			        		Ext.getCmp('combdor').enable();
			        	}
				        var predprs = eval('('+applet_adapter.getPredprs()+')');
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

var mainwin=function()
{
	
	var initjson=eval('('+applet_adapter.getinitvalues()+')');
	
	/*if(initjson.error) {
		Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
		Ext.get(document.getElementsByTagName('body')[0]).removeClass('x-border-layout-ct');
		ETD.ready();	
		Ext.MessageBox.alert('Ошибка!',initjson.error,function(btn){Refresh();});
		return;
	}*/
	toppanel.show();
	toppanel.expand();
	cpanel.show();
	cpanel.expand();
	if(Ext.isIE6){
		Ext.get(document.getElementsByTagName('html')[0]).addClass('x-viewport');
		document.getElementById('small-logo').style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='logo.png', sizingMethod='scale')";
		document.getElementById('small-logo').src=Ext.BLANK_IMAGE_URL;
	}
//	document.getElementsByTagName('body')[0].style.backgroundImage='';

	Ext.get(document.getElementsByTagName('body')[0]).removeClass('white-body');
	
	var radioPeriod = 1;
	//Диалог-календарь для выбора даты отчета
	datewin = new Ext.Window({
		plain: 'true',
		width: 250,
		layout: 'form',
		resizable: false,
		modal:true,
		border:false,
		buttonAlign:'center',
		closable: false,
		style:'text-align: center;',
		items: [{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Введите период формирования отчета</div>',style:'font-size: 12px;'},
			{xtype:'radio',
                checked: true,
                hideLabel: true,
                boxLabel: 'произвольный',
                name: 'reportPeriod',
                inputValue: '1',
                handler: function(checkBox, checked){
                		if (checked == true){
							Ext.getCmp('to').enable();
							radioPeriod = 1;
                		}
                	}
            }, {xtype:'radio',
                hideLabel: true,
                boxLabel: 'за сутки',
                name: 'reportPeriod',
                inputValue: '2',
                handler: function(checkBox, checked){
                		if (checked == true){
							Ext.getCmp('to').disable();
							Ext.getCmp('to').setValue(Ext.getCmp('from').getValue());
							radioPeriod = 2;
                		}
                	}
            }, {xtype:'radio',
                hideLabel: true,
                boxLabel: 'за месяц',
                name: 'reportPeriod',
                inputValue: '3',
                handler: function(checkBox, checked){
                		if (checked == true){
							Ext.getCmp('to').disable();
							Ext.getCmp('from').setValue(Ext.getCmp('from').getValue().getFirstDateOfMonth());
							var dateTo = Ext.getCmp('from').getValue().getLastDateOfMonth();
							if (dateTo > new Date()){
								dateTo = new Date();
							}
							Ext.getCmp('to').setValue(dateTo);
							radioPeriod = 3;
                		}
                	}
            },
            {xtype:'datefield',id:'from',maxValue: new Date(),fieldLabel:'Дата начала',style:'width: 90px;', allowBlank: false, readOnly: true},
      		{xtype:'datefield',id:'to',maxValue: new Date(),fieldLabel:'Дата окончания',style:'width: 90px;',allowBlank: false, readOnly: true}
      		],
		buttons: [{text: 'OK', id: 'okbutton',
			handler:function(){
				var dtfrom = Ext.getCmp('from').getValue();
				if (radioPeriod == 2){
					Ext.getCmp('to').setValue(dtfrom);
				}else if (radioPeriod == 3){
					Ext.getCmp('from').setValue(dtfrom.getFirstDateOfMonth());
					var dateTo = dtfrom.getLastDateOfMonth();
					if (dateTo > new Date()){
						dateTo = new Date();
					}
					Ext.getCmp('to').setValue(dateTo);
				}
				dtfrom = Ext.getCmp('from').getValue();
				var dtto = Ext.getCmp('to').getValue();
				datewin.hide();
				ETD.busy();
				(function(){
				//openreport
				var ret = eval('('+applet_adapter.getReport(reportname,dtfrom.format('d.m.Y'),dtto.format('d.m.Y'))+')');
				ETD.ready();
				if(!ret.error) CreateOpenReport();
				else if (ret.error == 'refresh') Refresh();
				else Ext.MessageBox.alert('Ошибка!',ret.error);
				}).defer(1);			
			}
		},{text:'Отмена',
			handler:function(){
			datewin.hide();
			}}]
	});
	Ext.getCmp('from').setValue(new Date((new Date()).getTime()-86400000));
	Ext.getCmp('to').setValue(new Date());
	
	Ext.getCmp('from').on('change', function(thisField, date){
		if (radioPeriod == 2){
			Ext.getCmp('to').setValue(date);
		}else if (radioPeriod == 3){
			thisField.setValue(date.getFirstDateOfMonth());
			var dateTo = date.getLastDateOfMonth();
			if (dateTo > new Date()){
				dateTo = new Date();
			}
			Ext.getCmp('to').setValue(dateTo);
		}
	});
	
	var button = new Ext.Button({
		id: 'createButton',
		text:'Создать документ',
		minWidth:btnwidth,
		renderTo:'button'
	});
	var buttonrep = new Ext.Button({
		id: 'reportButton',
		text:'Открыть отчет',
		minWidth:btnwidth,
		renderTo:'buttonrep'
	});
	var comboCreate = new Ext.form.ComboBox({
		store: comboCreateStore,
		//data:initjson.types_for_combo_create,
		width:btnwidth,
		displayField:'type',
		applyTo:'combocr',
		style:'position:relative; top:-2px;',
		displayField:'type',
		hideTrigger: true,
		hidden: true,
		emptyText: 'Создать документ',
		editable: false,
		mode: 'local',
		triggerAction: 'all',
		selectOnFocus: false
	});
	var comboReport = new Ext.form.ComboBox({
		store: comboReportStore,
		//data:initjson.types_for_combo_create,
		width:btnwidth,
		displayField:'type',
		applyTo:'comborep',
		style:'position:relative; top:-2px;',
		displayField:'type',
		hideTrigger: true,
		hidden: true,
		emptyText: 'Открыть отчет',
		editable: false,
		mode: 'local',
		triggerAction: 'all',
		selectOnFocus: false
	});
	var buttonclass;
	if(Ext.isGecko)buttonclass='somebutton-moz'
	else buttonclass='somebutton'
	button.on('click',function(){
			Ext.getCmp('open').addClass(buttonclass);
			Ext.getCmp('drop').addClass(buttonclass);
		comboCreate.setVisible(true);
		button.addClass(buttonclass);
		comboCreate.focus();
		(function(){
			comboCreate.expand();
			toolbarpanel.doLayout(true);
			}).defer(1);
	});
	buttonrep.on('click',function(){
		comboReport.setVisible(true);
		buttonrep.addClass(buttonclass);
		comboReport.focus();
		(function(){
			comboReport.expand();
			toolbarpanel.doLayout(true);
			}).defer(1);
	});

	comboCreate.on('collapse',function()
	{
	   Ext.getCmp('open').removeClass(buttonclass);
		Ext.getCmp('drop').removeClass(buttonclass);
		comboCreate.setVisible(false);
		button.removeClass(buttonclass)
		button.suspendEvents();
		(function(){button.resumeEvents()}).defer(500);
	});
	
	function getRecordIndexByName(store, value){
		var storeItems = store.data.items;
		var id = -1;
		var founded = false;
		for(i=0; i<storeItems.length; i++){
			if(storeItems[i].data.type.toString()==value){
				founded = true;
				id = storeItems[i].id;
			}
			if(founded) break;
		}
		return id;
	}
	
	function DeleteFromStore(store, value){
		var delRecord = getRecordIndexByName(store, value);
		if(delRecord!=null && delRecord>-1){
			delRecord = store.getById(delRecord);
			store.remove(delRecord);
		}
	}
	
	comboReport.on('collapse',function(){
		comboReport.setVisible(false);
		buttonrep.removeClass(buttonclass)
		buttonrep.suspendEvents();
		(function(){buttonrep.resumeEvents()}).defer(500);
	});
	comboCreate.on('select',function(combo,record,index ){
		ETD.busy();
		(function()
		{
			applet_adapter.createDocument(record.get('id'), record.get('type'));
			comboCreate.clearValue();
			ETD.ready();
			CreateOpenDoc();
		}).defer(1);
	});
	comboReport.on('select',function(combo,record,index ){
		reportname=record.get('type');
		//alert(record.get('type'));
		datewin.show();//Ext.getCmp('datepicker').focus();
		comboReport.clearValue();
	});
	
//	}catch(err) {return};

	//loading combos data
	var viewTypes = eval('('+applet_adapter.getViewTypes()+')');
	if(viewTypes.data)
	{
		comboViewStore.loadData(viewTypes);
		comboView.setValue(viewTypes.data[0].type);
	}
	
	var createTypes = eval('('+applet_adapter.getCreateTypes()+')');
	if(createTypes.data) 
	{
		comboCreateStore.loadData(createTypes);
		//comboCreate.setValue(createTypes.data[0].type);
	}
	
	var reportTypes = eval('('+applet_adapter.getReportTypes()+')');
	if(reportTypes.data) 
	{
		comboReportStore.loadData(reportTypes);
		//comboReport.setValue(reportTypes.data[0].type);
	}
	
	var dors = eval('('+applet_adapter.getDors()+')');
	if(dors.data)
	{
	  comboDorStore.loadData(dors);
	  comboDor.setValue(dors.data[0].name);
	}
	
	var name_div = new Ext.form.Label({
		renderTo: 'name',
		html: name
	});
	var rolename_div = new Ext.form.Label({
		renderTo: 'rolename',
		html: rolename
	});
	var predname_div = new Ext.form.Label({
		renderTo: 'predname',
		html: predname
	});
	
	var predn_val = document.getElementById('predname').innerHTML.toString();
	var predn_index1 = predn_val.indexOf('>');
	var predn_index2 = predn_val.indexOf('/');
	var sub_predn = predn_val.substring(predn_index1+1, predn_index2-1);
	if(sub_predn.length>0){
		DeleteFromStore(comboReportStore, 'ВУ-11');
	}
	
	/*var depname_div = new Ext.form.Label({
		renderTo: 'depname',
		html: depname
	});*/
	total_num = new Ext.form.Label({
		renderTo: 'total_number'
	});
	toppanel.doLayout();
	/*
	Some shit to align buttons in grid panels center
	*/
	function alignbuttons(panel){
		var arr=panel.getBottomToolbar().getEl().dom.getElementsByTagName('td');
		arr[0].width='50%';
		arr[arr.length-1].width='50%';
	}
	var page_number = Ext.getCmp('page_number');
	var next_button = Ext.getCmp('next_page');
	var prev_button = Ext.getCmp('prev_page');
	var page_number_arch;
	var next_button_arch;
	var prev_button_arch;
	




	tpanel.on('tabchange',function(tabPanel, tab){
		//tpanel.getActiveTab()
		Dev.logger.log('Active tab changed ID : ', tab.getId());
		Dev.logger.log('isLoaded : ', tab.isLoaded);
		if (tab.getId() == 'ID_DOCUMENT_GRID')
		{
			if (tab.getSelectionModel().hasSelection())
			{
				var record = tpanel.getActiveTab().getSelectionModel().getSelected();
				var deletePermission = record.get('cDel');
				if (deletePermission == '1') Ext.getCmp('drop').enable();
			};
		}
		else if(tab.getId() == 'ID_ARCHIVE_GRID' || tab.getId() == 'ID_DROP_GRID' || tab.getId() == 'ID_INWORK_GRID')
		{
			Ext.getCmp('drop').disable();
		}
		;
		if (!tab.isLoaded) tab.loadFirstPage();
		else total_num.setText(tab.count);
		
		
	});

	comboDor.on('select', function(combo, record, index){		
		ETD.busy();
		Ext.getCmp('reportButton').disable();
		var name = record.get("name");
		var id = record.get("id");
		eval('('+applet_adapter.setDor(id, name)+')');
		var predprs = eval('('+applet_adapter.getPredprs()+')');
		if(comboDor.getRawValue()!='-')Ext.getCmp('reportButton').enable();
		else Ext.getCmp('reportButton').disable();
		if(predprs.data)
		{
	  		comboPredprStore.loadData(predprs);
	  		comboPredpr.setValue(predprs.data[0].name);
	  		tpanel.items.each(function(item, index, length){ item.isLoaded = false});
			tpanel.getActiveTab().loadFirstPage();
		}
		ETD.ready();
	});
	
	var VU11deleted;
	
	comboPredpr.on('select', function(combo, record, index){		
		ETD.busy();
		var name = record.get("name");
		var id = record.get("id");
		eval('('+applet_adapter.setPred(id, name)+')');
		tpanel.items.each(function(item, index, length){ item.isLoaded = false});
		tpanel.getActiveTab().loadFirstPage();
		if (comboDor.getRawValue()=='-' && comboPredpr.getRawValue()=='-')Ext.getCmp('reportButton').disable();
		else Ext.getCmp('reportButton').enable();
		ETD.ready();
		if(comboPredpr.getRawValue()=='-'){
			if(VU11deleted!=null && VU11deleted){
				comboReportStore.loadData(reportTypes);
				VU11deleted = false;
			}
		}
		else if(comboPredpr.getRawValue()!='-'){
			DeleteFromStore(comboReportStore, 'ВУ-11');
			VU11deleted = true;
		}
	});
	
	comboPredpr.on('show', function(){
		//alert(comboDor.disabled);
		if(comboDor.disabled) Ext.getCmp('reportButton').enable();
	});
	
	comboView.on('select', function(combo, record, index){
		
		ETD.busy();
		//(function(){
			//var t = eval('(' + applet_adapter.select(combobox.getValue()) + ')');
			var type = record.get("type");
			var id = record.get("id");
			//alert(id + " " + type);
			ETDDoc.setFormType(id == -1 ? null : id);
			ETDArch.setFormType(id == -1 ? null : id);
			ETDDrop.setFormType(id == -1 ? null : id);
			ETDInwork.setFormType(id == -1 ? null : id);
			tpanel.items.each(function(item, index, length){ item.isLoaded = false});
			tpanel.getActiveTab().loadFirstPage();
			ETD.ready();
			//}).defer(1);
	});
	(function(){
		if (gup('id').length > 0) Ext.getCmp('datepickerbutton').disable();
		ETDDoc.grid.loadFirstPage();
		ETD.ready();
	}).defer(1);
		
}
