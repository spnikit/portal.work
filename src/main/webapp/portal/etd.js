var applet_adapter;
var name;
var depname;
var predname;
var total_num;
var crwintitle='';
var sendArr;
var activesaveOCO=true;
var before = new Date();
var after = new Date(new Date().getTime()-172800000);
var dateslist = null;
var isdateslist = false;
var itms = {};
var width = 200;
var shParam = null;
var shRepParam = null;
var datepickerplusmenu_1;
var busy_win = new Ext.Window({
	resizable:false,
	headerAsText:false,
	plain:true,
	layout: 'fit',
	width:1,
	height:1,
	modal:true,
	shadow:false,
	bodyBorder: false,
	border: false,
	hideBorders: true,
	closable: false
});


var createTypesAdd,combostore_OCO,period_store,doc_store,res_store,counterparts_store,counterpers_store,combocrstore,combocrstore_filt,combocrstoreMSG,combo,combocr,tpanel, combocrstoreDI, combocrstoreREM
var toppanel,toolbarpanel,btnwidth,crwinOCO,filters, filters_arch ,filterwin_newall, reportwin, viewrepwin, mkwin
var reportname='';
var diArray;


Ext.onReady(function(){

	
	
	document.applet_adapter =new NpapiPlugin('aisa');
	
	
	if (!ETD.newapp){
		
		(function(){}).defer(5000);
		
		}
	
		
Ext.QuickTips.init();
	
	ETD.busy();
	globalInit();
	
	var chromeDatePickerCSS = ".x-date-picker {border-color: #1b376c;background-color:#fff;position: relative;width: 185px;}";
	Ext.util.CSS.createStyleSheet(chromeDatePickerCSS,'chromeDatePickerStyle');
	
	Ext.get(document.getElementsByTagName('body')[0]).addClass('white-body');
	try{
	/**
	* for viewport
	*/
	btnwidth=150;
	
datepickerplusmenu_1 = new Ext.menu.DateMenu({
			    id			             : 'datepickerplusmenu_1',
			    usePickerPlus			   : true,
			    noOfMonth			       : 1,
			    multiSelection			 : true,
			    strictRangeSelect			: false,
			    markNationalHolidays	: false,
			    multiSelectByCTRL			: true,
			    maxSelectionDays			: 31,
			    useQuickTips			   : false,
			    showWeekNumber			 : false,
			    selectedDates			   : [new Date(new Date().getTime() - 172800000),
			        new Date(new Date().getTime() - 86400000), new Date()],
			    __year			         : (new Date()).getFullYear(),
			    // так как внизу устарело
			    // __year : (new Date()).getYear(),
			    __month			         : (new Date()).getMonth() + 1,
			    renderTodayButton			: false,
			    showToday			       : false, // Ext 2.2 own option
			    allClicked			     : function()
				    {
				    console.log("call allClicked()");
				    isdateslist = false;
				    this.clearSelectedDates();
				    Ext.getCmp('datepickerbutton').hideMenu();
				    before = new Date('01/01/2100');
				    after = new Date('01/01/2000');
				    
				   
				    
				    Ext.getCmp('datepickerbutton').setText('-   Все документы   -');
				    ETD.busy();
				    tpanel.items.each(function(item, index, length)
					        {
					        // TODO почему используется только первый параметр
					        console.log("call tpanel.items.each()");
					        item.isLoaded = false
					        });
				    tpanel.getActiveTab().loadFirstPage();
				    ETD.ready();
				    },
			    /**
					 * обработчик при нажатии кнопки <b>"За месяц"</b> в календаре
					 * 
					 * @this
					 */
			    monthClicked			   : function()
				    {
			    	 isdateslist = false;
				    console.logCall("call monthClicked()");
				    
				    this.clearSelectedDates();
				    Ext.getCmp('datepickerbutton').hideMenu();
				    before = new Date(new Date((this.__month + 1) + '/01/'
				        + this.__year).getTime()
				        - 86400000);
				    after = new Date((this.__month) + '/1/' + this.__year);
				    Ext.getCmp('datepickerbutton').setText(' ' + after.format('j M Y')
				        + '  -  ' + before.format('j M Y') + ' ');
				    ETD.busy();
				    tpanel.items.each(function(item, index, length)
					        {
					        // TODO почему используется только первый параметр
//					        console.log("call tpanel.items.each");
					        item.isLoaded = false
					        });
				    
//				    console.log(this.selectedDates);
				    
				    tpanel.getActiveTab().loadFirstPage();
				    ETD.ready();
				    },
				    //вместо снятия дат ставятся дефолтные три дня
			    undoClicked : function(){
			    	 isdateslist = false;
				    	console.logCall("undo()");
				    	this.clearSelectedDates();
				    	this.setSelectedDates(new Date(new Date().getTime() - 172800000), true);
				    	this.setSelectedDates(new Date(new Date().getTime() - 86400000), true);
				    	this.setSelectedDates(new Date(new Date().getTime()), true);
				    	__month = (new Date()).getMonth() + 1;
			    Ext.getCmp('datepickerbutton').hideMenu();
			    	before = new Date(new Date().getTime());
			    	after  = new Date(new Date().getTime() - 172800000);
			    	Ext.getCmp('datepickerbutton').setText(' ' + after.format('j M Y')
				        + '  -  ' + before.format('j M Y') + ' ');
    ETD.busy();
				    		 tpanel.items.each(function(item, index, length)
					        {
					        console.log("call tpanel.items.each");
					        item.isLoaded = false
					        });
				    		 tpanel.getActiveTab().loadFirstPage();
 ETD.ready();
				    		
			    },
			    handler			         : function(dp, date)
				    {
				    console.logCall("call handler()");
				    
				    isdateslist = true;
				      dateslist = this.selectedDates;
				
				    if (date.length > 0)
					    {
					    if (Ext.isDate(date))
						    {
						    before = date;
						    after = date;
						    Ext.getCmp('datepickerbutton').setText(' '
						        + date.format('j M Y') + '  -  ' + date.format('j M Y')
						        + ' ');
						    ETD.busy();
						    tpanel.items.each(function(item, index, length)
							        {
							        item.isLoaded = false
							        });
						    tpanel.getActiveTab().loadFirstPage();
						    ETD.ready();
						    }
					    else
						    {
						    
						    before = date[date.length - 1];
						    after = date[0];
						    Ext.getCmp('datepickerbutton').setText(' '
						        + date[0].format('j M Y') + '  -  '
						        + date[date.length - 1].format('j M Y') + ' ');
						    ETD.busy();
						    tpanel.items.each(function(item, index, length)
							        {
							        item.isLoaded = false
							        });
						    tpanel.getActiveTab().loadFirstPage();
						    ETD.ready();
						    }
					    }
				    },
			    listeners			       : {
				    'aftermonthchange'	: function(dm, oldM, newM)
					    {
					    console.logCall("call aftermonthchange()");
					    dm.__month = newM + 1;
					    return true;
					    },
				    'afteryearchange'		: function(dm, oldY, newY)
					    {
					    console.logCall("call aftermonthchange()");
					    dm.__year = newY;
					    return true;
					    }
			    }
		    });
					    
	
	tpanel = new Ext.TabPanel({
		
		activeTab: '0',
		autoheight:true,
		region:'center',
		id:'tpanel',
		enableTabScroll:true,
		bodyBorder:false

	});
	

calendar = new Ext.Button({
	text	: ' '
+ datepickerplusmenu_1.selectedDates[0].format('j M Y')
+ '  -  '
+ datepickerplusmenu_1.selectedDates[datepickerplusmenu_1.selectedDates.length
    - 1].format('j M Y') + ' ',
id		: 'datepickerbutton',
menu	: datepickerplusmenu_1,
style :'float: left;', 
region: 'center'
});



	logopanel = new Ext.Panel({
			region			 : 'north',
			layout			 : 'column',
			height			 : tph,
			id			     : 'logopanel',
			style			   : 'margin-left:15px;margin-top:15px; padding-right:0px;',
			animCollapse	: false,
			collapsed			: false,
			bodyStyle			: 'background-color: transparent;',
			border			 : false,
			items			   : [ {
				xtype			: 'panel',
			width			: 200,
			html			: '<img id="small-logo" src="pics/portal_menu.png"></img>',
			border		: false,
			bodyStyle	: 'background-color: transparent; ',
				style			   : 'margin-left:15px;margin-top:15px; padding-right:0px;'
		}]
			
		});


	toppanel = new Ext.Panel({
		region:'north',
		layout:'column',
		height: tph+70,
		id: 'toppanell',
		style:'margin-top:0px; padding-right:10px;',
		animCollapse:false,
		collapsed:true,
		bodyStyle: 'background-color: transparent; ',
		border:false,
		items:[
		       {
		xtype:'panel',
		id: 'btnpanel',
		region:'west',
		animCollapse:false,
		collapsed:true,
		border:false,
		layout:'column',
		layoutConfig: {
			columns: 4
		},
		width:650,
style:'margin-left: 10px;margin-top:10px;',
		bodyStyle: 'background-color: transparent; '
		,items:[
{xtype:"button",id:'createbut',cls: 'x-btn-text-icon', /*colspan: 1,*/ minWidth: btnwidth, icon: 'imgs/icons/create.png',width:btnwidth , text:" Создать документ",handler:function(){
	if (ETD.fr!=7){
	
			if(combocrstore.getTotalCount()>0){
			crwinOCO.show();
			crwinOCO.focus();
			}
			else {
				 Ext.MessageBox
		            .alert('Сообщение',
		                'Нет документов для создания');
	}
	} else {
		var grid = Ext.getCmp('tpanel').getActiveTab();
		var selections = grid.getSelectionModel().getSelections();
		if(selections==null || selections.length==0) {
			if(combocrstore.getTotalCount()>0)	
			{
			
crwinOCO.show();
crwinOCO.focus();

			}
			else {
				 Ext.MessageBox
		            .alert('Сообщение',
		                'Нет документов для создания');
	}
		}
		else {
			
			Ext.Ajax.request({
				url: 'forms/getEtdidbyId',
				params: {id:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('id')},
				callback:function(options, success, response){
				var resp = response.responseText;
				var arr= Ext.util.JSON.decode(resp);
					if (arr[0].success==true){

						document.applet_adapter.createZPPS(combocrstore.getAt(0).get('id'),combocrstore.getAt(0).get('type'),0,'', arr[0].etdid, ETD.fr);
						CreateOpenDoc();
					} else if (arr[0].success==false){
						Ext.MessageBox.alert('Внимание', 'Нет пакета документов с таким идентификатором');
					}
				}
				});
			}
	}
		
//	var extwin = new Ext.Window({
//		  title: '',
//		  width: 640,
//		  height: 400,
//		  resizable: true,
//		maximizable: true,
//		id:'htmlwindow',
//		modal: false,
//		layout: "fit",
//		 border: false,
//		tbar: [
//		{text: 'Печать', handler: function(){
//			printDoc();
//		
//		}},
//		{text: 'Закрыть', handler: function(){
//			extwin.close();
//		}}
//		],
//		items:[{
//			type: 'panel',
//			id: 'html',
//			 autoScroll: true,
//			 border: false,
//			 bodyStyle:{"background-color":"white"}
//		, html: testform
//		}]
//		
//		});
//	extwin.show();
		
	
	},style:'margin-left: 5px;margin-top:5px;'},
		{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/ minWidth: btnwidth, icon: 'imgs/icons/open.png', id:'open',text:"Открыть документ",
			handler:opendoc,
		
			style:'margin-left: 5px;margin-top:5px;'},
		{xtype:"button", cls: 'x-btn-text-icon',  /*colspan: 1,*/minWidth: btnwidth, icon: 'imgs/icons/filter.png',	iconAlign:'right',id:'rqnfilter',text:" Фильтр",handler:function(){
			Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
			
			
			if (ETD.fr!=7&&ETD.fr!=8&&ETD.fr!=9&&ETD.fr!=15&&ETD.fr!=16&& ETD.fr!=17){
		
diArray = eval('('+document.applet_adapter.getDI()+')');
	
				if (diArray.datadi){				
combocrstoreDI.loadData(diArray);
				};
				
	if (ETD.di.length==0){			
if (diArray.datarem){

	combocrstoreREM.loadData(diArray);
}
} else {
	remArray = eval('('+document.applet_adapter.getLoadRem(ETD.di)+')');
	
if (remArray.datarem){

	Ext.getCmp('rempred').menu.removeAll();
	combocrstoreREM.loadData(remArray, false);
	Ext.getCmp('rempred').vovako();
}
}
predtypesArray = eval('('+document.applet_adapter.getPersTypes()+')');

if (predtypesArray.sign){				
combocrstoreNames.loadData(predtypesArray);
				};
			
				
if (predtypesArray.types){

	combocrstoreTypes.loadData(predtypesArray);
}

			var srchTab = new Array();
			
			if (shParam!=null)			
			{	srchTab = shParam.split('|');
			
			
					(srchTab[1]!='---'&&srchTab[1].length>0) ?Ext.getCmp('from').setValue(srchTab[1]):Ext.getCmp('from').setValue('');
					(srchTab[2]!='---'&&srchTab[2].length>0) ?Ext.getCmp('to').setValue(srchTab[2]):Ext.getCmp('to').setValue('');
					(srchTab[3]!='---'&&srchTab[3].length>0) ?Ext.getCmp('content').setValue(srchTab[3]):Ext.getCmp('content').setValue('');
					(srchTab[4]!='---'&&srchTab[4].length>0) ?Ext.getCmp('dognum').setValue(srchTab[4]):Ext.getCmp('dognum').setValue('');
					(srchTab[5]!='---'&&srchTab[5].length>0) ?Ext.getCmp('package').setValue(srchTab[5]):Ext.getCmp('package').setValue('');
					(srchTab[6]!='---'&&srchTab[6].length>0) ?Ext.getCmp('vagnum').setValue(srchTab[6]):Ext.getCmp('vagnum').setValue('');
					(srchTab[7]!='---'&&srchTab[7].length>0) ?Ext.getCmp('rf').setValue(srchTab[7]):Ext.getCmp('rf').setValue('');
					(srchTab[8]!='---'&&srchTab[8].length>0) ?Ext.getCmp('di').setValue(srchTab[8]):Ext.getCmp('di').setValue('');
					(srchTab[9]!='---'&&srchTab[9].length>0) ?Ext.getCmp('rempred').setValue(srchTab[9]):Ext.getCmp('rempred').setValue('');
					(srchTab[10]!='---'&&srchTab[10].length>0) ?Ext.getCmp('types').setValue(srchTab[10]):Ext.getCmp('types').setValue('');
					(srchTab[11]!='---'&&srchTab[11].length>0) ?Ext.getCmp('signer').setValue(srchTab[11]):Ext.getCmp('signer').setValue('');
					(srchTab[12]!='---'&&srchTab[12].length>0) ?Ext.getCmp('stat').setValue(srchTab[12]):Ext.getCmp('stat').setValue('');
					(srchTab[13]!='---'&&srchTab[13].length>0) ?Ext.getCmp('rt').setValue(srchTab[13]):Ext.getCmp('rt').setValue('');
					(srchTab[14]!='---'&&srchTab[14].length>0) ?Ext.getCmp('servicetype').setValue(srchTab[14]):Ext.getCmp('servicetype').setValue('');
					(srchTab[15]!='---'&&srchTab[15].length>0) ?Ext.getCmp('color').setValue(srchTab[15]):Ext.getCmp('color').setValue('');
					(srchTab[16]!='---'&&srchTab[16].length>0) ?Ext.getCmp('sftype').setValue(srchTab[16]):Ext.getCmp('sftype').setValue('');
					(srchTab[17]!='---'&&srchTab[17].length>0) ?Ext.getCmp('otc_code').setValue(srchTab[17]):Ext.getCmp('otc_code').setValue('');
					(srchTab[18]!='---'&&srchTab[18].length>0) ?Ext.getCmp('refer_type').setValue(srchTab[18]):Ext.getCmp('refer_type').setValue('');
			}
			
			
			Ext.Msg.hide();

			
			filterwin_newall.show(); 
			filterwin_newall.focus();
			if (ETD.fr==5){
				Ext.getCmp('content').disable();
				Ext.getCmp('dognum').disable();
				Ext.getCmp('package').disable();
				Ext.getCmp('vagnum').disable();
				Ext.getCmp('stat').disable();
				Ext.getCmp('di').disable();
				Ext.getCmp('rempred').disable();
				Ext.getCmp('rf').disable();
				Ext.getCmp('rt').disable();
				Ext.getCmp('servicetype').disable();
				Ext.getCmp('sftype').disable();
//				Ext.getCmp('color').hide();
				Ext.getCmp('otc_code').disable();
				Ext.getCmp('refer_type').disable();
			}
			if(ETD.fr==21) {
			//	Ext.getCmp('package').hide();
				Ext.getCmp('package').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('vagnum').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('stat').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('di').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('rempred').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('rf').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('rt').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('servicetype').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('sftype').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('color').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('otc_code').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('refer_type').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('content').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('dognum').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('from').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('to').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('signer').getEl().up('.x-form-item').setDisplayed(false);
				Ext.getCmp('dateLastSign').hide();
				Ext.getCmp('dateRepair').hide();
			}
 if (ETD.predname.indexOf('Трансойл')>-1){
	 showCombo(Ext.getCmp('color'));
 } else {
	 hideCombo(Ext.getCmp('color'));
 }
 
 if (ETD.fr==5 || ETD.fr == 11 || ETD.fr == 12) {
	 showCombo(Ext.getCmp('etdid'));
 } else {
	 hideCombo(Ext.getCmp('etdid'));
 }
			
 if (ETD.fr==5 || ETD.fr == 11 || ETD.fr == 12) {
	 showCombo(Ext.getCmp('etdid'));
 } else {
	 hideCombo(Ext.getCmp('etdid'));
 }
 
 		
			}
			
			else if (ETD.fr==7||ETD.fr==8){
				predtypesArray = eval('('+document.applet_adapter.getPersTypes()+')');
				if (predtypesArray.sign){				
				combocrstoreNames.loadData(predtypesArray);
								};
				if (predtypesArray.types){
				combocrstoreTypes.loadData(predtypesArray);
				}
//				var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
			
					var tabname = Ext.getCmp('tpanel').getActiveTab().getId();
					var srchTab = new Array();
					if (shParam!=null)			
					{	srchTab = shParam.split('|');
					
							(srchTab[1]!='---'&&srchTab[1].length>0) ?Ext.getCmp('frompps').setValue(srchTab[1]):Ext.getCmp('frompps').setValue('');
							(srchTab[2]!='---'&&srchTab[2].length>0) ?Ext.getCmp('topps').setValue(srchTab[2]):Ext.getCmp('topps').setValue('');
							(srchTab[3]!='---'&&srchTab[3].length>0) ?Ext.getCmp('contentpps').setValue(srchTab[3]):Ext.getCmp('contentpps').setValue('');
							(srchTab[4]!='---'&&srchTab[4].length>0) ?Ext.getCmp('dognumpps').setValue(srchTab[4]):Ext.getCmp('dognumpps').setValue('');
							(srchTab[7]!='---'&&srchTab[7].length>0) ?Ext.getCmp('datefrompps').setValue(srchTab[7]):Ext.getCmp('datefrompps').setValue('');
							(srchTab[9]!='---'&&srchTab[9].length>0) ?Ext.getCmp('pps').setValue(srchTab[9]):Ext.getCmp('pps').setValue('');
							(srchTab[10]!='---'&&srchTab[10].length>0) ?Ext.getCmp('typespps').setValue(srchTab[10]):Ext.getCmp('typespps').setValue('');
							(srchTab[11]!='---'&&srchTab[11].length>0) ?Ext.getCmp('signerpps').setValue(srchTab[11]):Ext.getCmp('signerpps').setValue('');
							(srchTab[13]!='---'&&srchTab[13].length>0) ?Ext.getCmp('datetopps').setValue(srchTab[13]):Ext.getCmp('datetopps').setValue('');
						
						
					}
					if (tabname =='ID_DOCUMENT_GRID'){
						Ext.getCmp('frompps').disable();
						Ext.getCmp('topps').disable();
						Ext.getCmp('signerpps').disable();
						Ext.getCmp('datefrompps').disable();
						Ext.getCmp('datetopps').disable();
						}
						
						else {
						Ext.getCmp('frompps').enable();
						Ext.getCmp('topps').enable();
						Ext.getCmp('signerpps').enable();
						Ext.getCmp('datefrompps').enable();
						Ext.getCmp('datetopps').enable();
						}
					
				Ext.Msg.hide();
				pps_filter.show();
				pps_filter.focus();
				}
			
			
			else if (ETD.fr==9){
				
//				var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
			
//					var tabname = Ext.getCmp('tpanel').getActiveTab().getId();
					var srchTab = new Array();
					if (shParam!=null)			
					{	srchTab = shParam.split('|');
					
							(srchTab[4]!='---'&&srchTab[4].length>0) ?Ext.getCmp('dognumrtk').setValue(srchTab[4]):Ext.getCmp('dognumrtk').setValue('');
							(srchTab[5]!='---'&&srchTab[5].length>0) ?Ext.getCmp('packagertk').setValue(srchTab[5]):Ext.getCmp('packagertk').setValue('');
						
					}
					
				Ext.Msg.hide();
				rtk_filter.show();
				rtk_filter.focus();
				}
else if (ETD.fr==15||ETD.fr==16){
	predtypesArray = eval('('+document.applet_adapter.getPersTypes()+')');

	if (predtypesArray.sign){				
	combocrstoreNames.loadData(predtypesArray);
					};
				
					
	if (predtypesArray.types){

		combocrstoreTypes.loadData(predtypesArray);
	}
					var srchTab = new Array();
					if (shParam!=null)			
					{	srchTab = shParam.split('|');
							(srchTab[1]!='---'&&srchTab[1].length>0) ?Ext.getCmp('fromrzds').setValue(srchTab[1]):Ext.getCmp('fromrzds').setValue('');
							(srchTab[2]!='---'&&srchTab[2].length>0) ?Ext.getCmp('torzds').setValue(srchTab[2]):Ext.getCmp('torzds').setValue('');
							(srchTab[3]!='---'&&srchTab[3].length>0) ?Ext.getCmp('contentrzds').setValue(srchTab[3]):Ext.getCmp('contentrzds').setValue('');
							(srchTab[4]!='---'&&srchTab[4].length>0) ?Ext.getCmp('dognumrzds').setValue(srchTab[4]):Ext.getCmp('dognumrzds').setValue('');
							(srchTab[5]!='---'&&srchTab[5].length>0) ?Ext.getCmp('packagerzds').setValue(srchTab[5]):Ext.getCmp('packagerzds').setValue('');
							(srchTab[10]!='---'&&srchTab[10].length>0) ?Ext.getCmp('typesrzds').setValue(srchTab[10]):Ext.getCmp('typesrzds').setValue('');
							(srchTab[11]!='---'&&srchTab[11].length>0) ?Ext.getCmp('signerrzds').setValue(srchTab[11]):Ext.getCmp('signerrzds').setValue('');
						
					}
					if (tabname =='ID_DOCUMENT_GRID'){
						Ext.getCmp('fromrzds').disable();
						Ext.getCmp('torzds').disable();
						Ext.getCmp('signerrzds').disable();
					}
						
						else {
						Ext.getCmp('fromrzds').enable();
						Ext.getCmp('torzds').enable();
						Ext.getCmp('signerrzds').enable();
					
						}
				Ext.Msg.hide();
				rzds_filter.show();
				rzds_filter.focus();
				}else if (ETD.fr==17){
					predtypesArray = eval('('+document.applet_adapter.getPersTypes()+')');
					if (predtypesArray.types) {
						combocrstoreTypes.loadData(predtypesArray);
					}
					if (predtypesArray.sign){				
						combocrstoreNames.loadData(predtypesArray);
				    }
					Ext.Msg.hide();
					css_filter.show();
					css_filter.focus();
				}
			
		
		},style:'margin-left: 5px;margin-top:5px'},
		{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/ minWidth: btnwidth,icon: 'imgs/icons/icon_reject.png', id:'drop',text:" Отклонить документ",handler:function(){
			
			
			var grid = Ext.getCmp('tpanel').getActiveTab();
			var tag = grid.getId();
			var selections = grid.getSelectionModel().getSelections();
			if(selections==null || selections.length==0) return;
			
//			if (ETD.fr==2&&selections[0].get('name').indexOf('чет-фактура')==-1||ETD.fr==3&&selections[0].get('name').indexOf('чет-фактура')==-1){
			if (ETD.fr==2&&ETD.dropdocsroles2.indexOf(selections[0].get('name'))==-1||ETD.fr==3&&ETD.dropdocsroles3.indexOf(selections[0].get('name'))==-1 || ETD.fr!=22 && tag == 'ID_DOCUMENT_GRID' && selections[0].get('name').indexOf('чет-фактура')>-1){
					Ext.MessageBox.alert('Внимание', 'Нельзя отклонить данный документ');
			}else if(selections[0].get('name').indexOf('чет-фактура')>-1 && ETD.fr == 22 && tag == 'ID_DOCUMENT_GRID'){
				Ext.Ajax.request({
					url: 'forms/checksign',
					params: {id:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('id')},
					callback:function(options, success, response){
					var resp = response.responseText;
					var arr= Ext.util.JSON.decode(resp);
						if (arr[0].signed==true){
							dropdoc(false);

						} else if (arr[0].signed==false){
							Ext.MessageBox.alert('Внимание', 'Нельзя отклонить новый документ');
						}
					}
					});
				
			}else if (selections[0].get('name').indexOf('чет-фактура')>-1 && tag != 'ID_DOCUMENT_GRID'){
			
			Ext.Ajax.request({
				url: 'forms/checksign',
				params: {id:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('id')},
				callback:function(options, success, response){
				var resp = response.responseText;
				var arr= Ext.util.JSON.decode(resp);
					if (arr[0].signed==true){
						dropdoc(false);

					} else if (arr[0].signed==false){
						Ext.MessageBox.alert('Внимание', 'Нельзя отклонить новый документ');
					}
				}
				});
			} else if (selections[0].get('name').indexOf('ВУ-23_О')>-1){
				Ext.Ajax.request({
					url: 'forms/checkDeclVRK',
					params: {id:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('id')},
					callback:function(options, success, response){
					var resp = response.responseText;
					var arr= Ext.util.JSON.decode(resp);
						if (arr[0].result==true){
							dropdoc(false);

						} else if (arr[0].result==false){
							Ext.MessageBox.alert('Внимание', 'Нельзя отклонить данный документ');
						}
					}
					});
				}
		else dropdoc(false);
			},style:'margin-left: 5px;margin-top:5px;'},
		
		{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/ icon: 'imgs/icons/report_tor.png',minWidth: btnwidth,	iconAlign:'right',id:'repfilter',text:"Отчет ТОР", style:'margin-left: 5px;margin-top:5px;',handler:function(){
			
			otchettwin.show();
			
			
}}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'export',text:"Выгрузить С-ф",handler:mkfile,style:'margin-left: 5px;margin-top:5px;'}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/ minWidth: btnwidth,icon: 'imgs/icons/view_receipt.png',id:'view',text:"Просмотр квитанций",style:'margin-left: 5px;margin-top:5px;',handler: function(){
		

var grid = Ext.getCmp('tpanel').getActiveTab();
		var selections = grid.getSelectionModel().getSelections();			
		
SFdoc.req = selections[0].get('id');		
if(selections==null || selections.length==0) return;
if (selections.length>1){
	Ext.MessageBox
    .alert('Ошибка',
        'Необходимо выбрать один документ');
    return;
}
if (selections[0].get('name').indexOf('чет-фактура')>-1){
	mkviewwin();
} else {
    Ext.MessageBox
    .alert('Ошибка',
        'Просмотр квитанций доступен только для документов типа "Счет-фактура"');
}
		},style:'margin-left: 5px;margin-top:5px;'}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/repair_history.png', id:'history',text:"История ремонта",handler:openHistory,style:'margin-left: 5px;margin-top:5px;'}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/view_package.png', id:'sfpack',text:"Просмотр пакета",handler:function(){
			
if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
	
if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('name').indexOf('чет-фактура')>-1
	||Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('name')=='ТОРГ-12'){
	Ext.Ajax.request({
url: 'forms/sfpack',
params: {idpak:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('idpak')},
callback:function(options, success, response){
var resp = response.responseText;
var arr= Ext.util.JSON.decode(resp);
	if (arr[0].success==true){
		ETD.packview = true;
		ETD.setPackid(arr[0].docid);
		Pack.mkpackwin(arr[0].docid);

	} else if (arr[0].success==false){
		Ext.MessageBox.alert('Внимание', 'Нет пакета документов с таким идентификатором');
	}
}
});


}		else {
	Ext.MessageBox.alert('Внимание', 'Только для типов документов Счет-фактура и ТОРГ-12');
}

	}

		},style:'margin-left: 5px;margin-top:5px;'}
		
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/clarify_invoice.png', id:'utoch',text:"Уточнение С-ф",handler:function()	{
			if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('packst')=='Подлежит оформлению'){
			dropdoc(true);
		} else {
			Ext.MessageBox.alert('Внимание', 'Не все технические квитанции оформлены');
		}
			}
			
			,style:'margin-left: 5px;margin-top:5px;'}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/icon_download.png', id:'exportdoc',text:"Выгрузить документ",handler:mkdocfile
			
			,style:'margin-left: 5px;margin-top:5px;'}
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/icon_download.png', id:'packview',text:"Основной пакет",handler:function(){
			if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().hasSelection()){
				
				if (Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('name')=='Пакет документов'&&Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('packst')=='Дополнительный'){
					Ext.Ajax.request({
				url: 'forms/mainpack',
				params: {id:Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected().get('id')},
				callback:function(options, success, response){
				var resp = response.responseText;
				var arr= Ext.util.JSON.decode(resp);
					if (arr[0].success==true){
						ETD.packview = true;
						ETD.setPackid(arr[0].docid);
						Pack.mkpackwin(arr[0].docid);

					} else if (arr[0].success==false){
						Ext.MessageBox.alert('Внимание', 'Основной пакет документов с данным идентификатором не существует');
					}
				}
				});


				}		else {
					Ext.MessageBox.alert('Внимание', 'Указанный пакет документов не является дополнительным');
				}

					}
		}
			
			,style:'margin-left: 5px;margin-top:5px;'}
		
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'exportexcel',text:"Выгрузить в excel",
			        	handler: exporttoex,
//			handler:excelsaver,      
			        	
			style:'margin-left: 5px;margin-top:5px;'}
		
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'reportTOR',text:"Аналитическая справка",
        	handler: exportreportTOR,
            style:'margin-left: 5px;margin-top:5px;'}
		
		
		
		,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'exportexcelsf',text:"Отчет по С-ф",
	        	handler: exporttoexsf,
//				handler:excelsaver,        	
				style:'margin-left: 5px;margin-top:5px;'}
				,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'printPackage',text:"Печать",
        	handler: printPackage,
            style:'margin-left: 5px;margin-top:5px;'}
	,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'acceptDocs',text:"Согласовать",
        	handler: acceptDocs,
        	hidden: true, 
            style:'margin-left: 5px;margin-top:5px;'}
	,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'reportRTK',text:"Отчет",
    	handler: reportRTK,
    	hidden: true, 
        style:'margin-left: 5px;margin-top:5px;'},
        {xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'packsgn',text:"Подпись пакета",
        	handler: signvrkdocs,
        	hidden: true, 
            style:'margin-left: 5px;margin-top:5px;'}
            ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'downloadProposal',text:"Загрузить заявку",
    	handler: downloadProp,
    	hidden: true, 
        style:'margin-left: 5px;margin-top:5px;'}
    ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'reportPPS',text:"Отчет ППС",
    	handler: reportPPS,
    	hidden: true, 
        style:'margin-left: 5px;margin-top:5px;'}  
    ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'createPretension',text:"Оформить претензию",
        handler: createPretension,
        hidden: true, 
          style:'margin-left: 5px;margin-top:5px;'} 
    ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'reportTorFromArchive',text:"Отчет",
        handler: exportTorFromArchive,
        hidden: true, 
          style:'margin-left: 5px;margin-top:5px;'}  
    ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'exportXML',text:"Выгрузить XML",
        handler: exportXML,
        hidden: true, 
          style:'margin-left: 5px;margin-top:5px;'}
    ,{xtype:"button",cls: 'x-btn-text-icon',/*colspan: 1,*/minWidth: btnwidth,icon: 'imgs/icons/download_invoice.png', id:'exportPDF',text:"Выгрузить в PDF",
        handler: exportPDF,
        hidden: true, 
          style:'margin-left: 5px;margin-top:5px;'}
          
   ]
		

},
 

{xtype:'panel',
bodyStyle: 'background-color: transparent;',
border:false,
autoWidth: true,
layout: 'anchor',
html:'<img src="'+Ext.BLANK_IMAGE_URL+'"></img>'
},
{xtype: 'panel', 
border:false,	
layout: 'anchor',
autoWidth: true,
height: tph+50,
items:[

{xtype:'label',	
	html:'<div class="my-label x-unselectable" onselectstart="return false" style="float: right;margin-right:5px;">'+
	'<div id="name" style="text-align: center; padding=4px; ">Пользователь: </div>' +
	'<div id="depname" style="text-align: center; padding: 4px; ">Роль: </div>'+
	'<div id="predname" style="text-align: center; padding: 4px; ">Предприятие: </div></div>',
border:true,
bodyStyle: 'background-color: transparent;'
}],
buttons: [calendar]

}
 	    ]
	});
	


	//tph=tph+75;
	
	cpanel = new Ext.Panel({
		

		region: 'center',

		id: 'cpanel',
		autoheight:true,
		bodyBorder: false,
		border: false,
		layout: 'border',
		items: [tpanel]
	});

	cpanel_layout=new Ext.Panel({
		id:'cpanel_layout',
	border: false,
	margins: '5 5 5 5',
		region:'center',
		layout:'border',
		items:[cpanel]
	});

	new Ext.Viewport({
		layout: 'border',
		id:'main',
		items: [toppanel,cpanel_layout]
		});

	
	
	Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
	Ext.get(document.getElementsByTagName('body')[0]).removeClass('x-border-layout-ct');
	Ext.getCmp('btnpanel').collapse(false);
	Ext.getCmp('btnpanel').hide();
	cpanel_layout.collapse(false);
	cpanel_layout.hide();
	cpanel.collapse(false);
	cpanel.hide();
	
	toppanel.collapse(false);
	toppanel.hide();
	Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
	/**
	* end
	*/
	}catch(e){return}

	if (document.applet_adapter == null ) 
	{
		ETD.ready();
		Ext.MessageBox.alert('Error','java applet not found');
	}
	else 
	(function(){
		var updated=eval('('+document.applet_adapter.Updated()+')');
		
		
		if(updated.updated)
		{
		login();
		} else{	

			ETD.ready(); Ext.MessageBox.alert('Внимание','Необходимо обновить программное обеспечение, версия <a href="'+updated.url+'">'+updated.version+'</a>');}
	}).defer(1)

});


var mainwin=function()
{
    
	
inittabpanel();	

tpanel.setActiveTab(0);
	ETD.busy();
	var initjson=eval('('+document.applet_adapter.getinitvalues()+')');
	if(initjson.error) {
		ETD.ready();
		Ext.get(document.getElementsByTagName('html')[0]).removeClass('x-viewport');
		Ext.get(document.getElementsByTagName('body')[0]).removeClass('x-border-layout-ct');
		Ext.MessageBox.alert('Ошибка!',initjson.error);
		return;
	}
	
	
	
	
	Ext.get(document.getElementsByTagName('body')[0]).removeClass('white-body');
	
	cpanel.updatetable = function(){
		if(tpanel.getActiveTab().getId() == 'grid')
			update_table();
		else
			update_table_arch();
		};
	
	combocrstore = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
	
	combocrstore_filt = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
	
	combocrstoreMSG = new Ext.data.JsonStore({
	    root: 'data',
	    fields: ['id', 'type']
	});
	
	combocrstoreDI = new Ext.data.JsonStore({
	    root: 'datadi',
	    fields: ['id', 'text']
	});	

combocrstoreREM= new Ext.data.JsonStore({
	    root: 'datarem',
	    fields: ['id', 'text']
	});	

combocrstoreTypes= new Ext.data.JsonStore({
	    root: 'types',
	    fields: ['id', 'name']
	});	
	
combocrstoreTypesPPS= new Ext.data.JsonStore({
    root: 'types',
    fields: ['id', 'name']
});	

combocrstoreNames= new Ext.data.JsonStore({
	    root: 'sign',
	    fields: ['id', 'name']
	});	

	type_store =  new Ext.data.SimpleStore({			   
    	
    	fields:['id', 'text'],
    	data:[
    	['0', 'ТОР'], 
    	['1', 'Связь'],
    	['2', 'Перевозка']
    	]
    });
	
    
stat_store =  new Ext.data.SimpleStore({			   
    	
    	fields:['id', 'text'],
    	data:[
    	['0', 'Первичный'], 
    	['1', 'Вторичный'],
    	['2', 'Дополнительный']
    	]
    });

report_store =  new Ext.data.SimpleStore({			   
	
	fields:['id', 'text'],
	data:[
	  ['0', 'Пакеты документов']
	, ['1', 'Ремонты вагонов']
	]
});

servicetype_store =  new Ext.data.SimpleStore({			   
	
	fields:['stringvalue', 'text'],
	data:[
	  ['01', 'ТР-1' ]
	, ['02', 'ТР-2']
	, ['03', 'Рекламация']
	, ['04', 'Хранение запасных частей и лома']
	, ['05', 'Погрузка/выгрузка']
	, ['06', 'Продажа деталей заказчика подрядчику']
	, ['07', 'Претензионная работа']
	, ['08', 'Продажа з/ч']
	, ['09', 'Связь']
	  ]
});

color_store =  new Ext.data.SimpleStore({			   
	
	fields:['id', 'text'],
	data:[
	['-1', 'Нет статуса'], 
	['1', 'Согласовано'],
	['2', 'Отклонено'],
	['3', 'Частично согласовано'],
	['4', 'Поступило']
	]
});
sftype_store =  new Ext.data.SimpleStore({			   
	
	fields:['id', 'text'],
	data:[
	  ['0', 'Исправленный']
	, ['1', 'Корректировочный']
	, ['2', 'Первичный']
	]
});

refer_store =  new Ext.data.SimpleStore({			   
	
	fields:['id', 'text'],
	data:[
	  ['19603', 'Повреждения']
	, ['19602', 'Эксплуатационные']
	, ['19601', 'Технологические']
	]
});

ppsstore= new Ext.data.JsonStore({
baseParams: {predid:ETD.predid},
url:'/IIT/portal/forms/pps',
fields: ['id','value']
});	

if (ETD.fr==7||ETD.fr==8){
ppsstore.load();
}
filterwin_newall = new Ext.Window({
	plain: 'true',
	width: 330,
	minwidth:330,
//	height:400,
	layout: 'form',
	closeAction: 'close', 
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	shadow: false,
	autoScroll: true,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [
         {xtype:'textfield',width:150,displayField:'value',hidden: false,editable: true, triggerAction: 'all',id:'vagnum',fieldLabel:'Номер вагона',  allowBlank:true,
      validator: function(val){
	   return ETD.validateVagnum(val);
}
            },
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreTypes,width:150,displayField:'name',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'types',fieldLabel:'Тип документа',  allowBlank:true ,width:150},
	       	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:servicetype_store,width:150,displayField:'text',valueField:'stringvalue', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'servicetype',fieldLabel:'Наименование услуги',  allowBlank:true ,width:150},
	       	{xtype:'textfield',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'otc_code',fieldLabel:'Код неисправности',width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:refer_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'refer_type',fieldLabel:'Вид неисправности',  allowBlank:true ,width:150},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата ремонта</div>',style:'font-size: 12px;', id:'dateRepair'},
		    {xtype:'datefield',id:'rf',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'rt',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150},	       	
	        {xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'content',fieldLabel:'Описание',width:150},
			{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'dognum',fieldLabel:'Договор',width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreDI,width:150,displayField:'text',valueField:'id',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'di',fieldLabel:'ДИ', allowBlank:true ,width:150},		
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreREM,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'rempred',fieldLabel:'ВЧДЭ',  allowBlank:true ,width:150},
			{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'package',fieldLabel:'Пакет',width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreNames,width:150,displayField:'name',
			valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'signer',fieldLabel:'Текущий исполнитель',  allowBlank:true ,width:150},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата последней подписи</div>',style:'font-size: 12px;', id:'dateLastSign'},
			{xtype:'datefield',id:'from',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'to',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150},
			{xtype:'combo',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:stat_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'stat',fieldLabel:'Статус',  allowBlank:true ,width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:color_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'color',fieldLabel:'Цветовой признак',  allowBlank:true ,width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:sftype_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'sftype',fieldLabel:'Вид СФ',  allowBlank:true ,width:150},
			{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'etdid',fieldLabel:'ID',width:150}
					],
					
listeners: {
	show:function(){
	
	Ext.getCmp('rempred').menu.olddi = Ext.getCmp('di').getValue();
	

	if (Ext.getCmp('rempred').menu.dorem==null){

Ext.getCmp('rempred').menu.dorem=1;
		Ext.getCmp('rempred').menu.on('show',function(){
		var di = Ext.getCmp('di').getValue();
//		alert(di);
		if (di!==Ext.getCmp('rempred').menu.olddi){

Ext.getCmp('rempred').menu.olddi = di;

remArray = eval('('+document.applet_adapter.getLoadRem(di)+')');
	
if (remArray.datarem){

	Ext.getCmp('rempred').menu.removeAll();
	combocrstoreREM.loadData(remArray, false);
	Ext.getCmp('rempred').vovako();
}


	}
});}
	}
	}, 
	
		buttons: [{text: 'OK', id: 'submitfind',
		handler:function(){
		if (Ext.getCmp('vagnum').isValid()){
			ETD.busy();
			(function(){
				filterwin_newall.hide();
			Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				
				srchArr[0]='';
				srchArr[1]=Ext.getCmp('from').getRawValue();
				srchArr[2]=Ext.getCmp('to').getRawValue();
				srchArr[3]=Ext.getCmp('content').getValue();
				srchArr[4]=Ext.getCmp('dognum').getValue();
				srchArr[5]=Ext.getCmp('package').getValue();
				srchArr[6]=Ext.getCmp('vagnum').getValue();
				srchArr[7]=Ext.getCmp('rf').getRawValue();
				srchArr[8]=Ext.getCmp('di').getValue();
				if (srchArr[8].length>0)
				ETD.di = srchArr[8];
				srchArr[9]=Ext.getCmp('rempred').getValue();
				srchArr[10]=Ext.getCmp('types').getValue();
				srchArr[11]=Ext.getCmp('signer').getValue();
				srchArr[12]=Ext.getCmp('stat').getValue();
				srchArr[13]=Ext.getCmp('rt').getRawValue();
				srchArr[14]=Ext.getCmp('servicetype').getValue();
				srchArr[15]=Ext.getCmp('color').getValue();
				srchArr[16]=Ext.getCmp('sftype').getValue();
				srchArr[17]=Ext.getCmp('otc_code').getValue();
				srchArr[18]=Ext.getCmp('refer_type').getValue();
				srchArr[19]=Ext.getCmp('etdid').getValue();
				for (i=0;i<srchArr.length;i++) if (srchArr[i].length==0) srchArr[i]='---';
				srchArrStr = srchArr.join("|");
				shParam = srchArrStr;
				
var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
				update_table();
				ETD.ready();
				
			
			}).defer(1)
					}	else Ext.Msg.alert('Сообщение', 'Введен неверный номер вагона');
			}
		
	},{text:'Отмена',
		handler:function(){
		
		filterwin_newall.hide();
	
	}
	},{text:'Сбросить',
		handler:function(){	
		ETD.busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			Ext.getCmp('from').setValue('');
			Ext.getCmp('to').setValue('');
			Ext.getCmp('content').setValue('');
			Ext.getCmp('dognum').setValue('');
			Ext.getCmp('package').setValue('');
			Ext.getCmp('vagnum').setValue('');
			Ext.getCmp('rf').setValue('');
			Ext.getCmp('rt').setValue('');
			Ext.getCmp('di').setValue('');
			Ext.getCmp('rempred').setValue('');
			Ext.getCmp('types').setValue('');
			Ext.getCmp('signer').setValue('');
			Ext.getCmp('stat').setValue('');
			Ext.getCmp('servicetype').setValue('');
			Ext.getCmp('color').setValue('');
			Ext.getCmp('sftype').setValue('');
			Ext.getCmp('otc_code').setValue('');
			Ext.getCmp('refer_type').setValue('');
			Ext.getCmp('etdid').setValue('');
			ETD.di = "";
			filterwin_newall.hide();
			srchArrStr = null;
			shParam = srchArrStr;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
		Ext.getCmp('rempred').menu.removeAll();
		remArray = eval('('+document.applet_adapter.getLoadRem("")+')');
		combocrstoreREM.loadData(remArray, false);
		Ext.getCmp('rempred').vovako();
		update_table();	
		ETD.ready();
		}).defer(1)
		}
		}
		
	
	]
});
	
//Фильтр ППС
//Это все бред, надо переписать все фильтры к одному


pps_filter = new Ext.Window({
	plain: 'true',
	width: 300,
	//height:420,
	layout: 'form',
	closeAction: 'close', 
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [	
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'dognumpps',fieldLabel:'Номер',width:150},
        	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreTypes,width:150,displayField:'name',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'typespps',fieldLabel:'Тип документа',  allowBlank:true ,width:150},
        	{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата</div>',style:'font-size: 12px;'},
		    {xtype:'datefield',id:'datefrompps',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'datetopps',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150},	       	
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'contentpps',fieldLabel:'Описание',width:150},
        	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:ppsstore,width:150,displayField:'value',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'pps',fieldLabel:'ППС',  allowBlank:true ,width:150},
        	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreNames,width:150,displayField:'name',
			valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'signerpps',fieldLabel:'Текущий исполнитель',  allowBlank:true ,width:150},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата последней подписи</div>',style:'font-size: 12px;'},
			{xtype:'datefield',id:'frompps',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'topps',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150}
		],
	
	
		buttons: [{text: 'OK', id: 'submitfind',
		handler:function(){
		
			ETD.busy();
			(function(){
				pps_filter.hide();
			Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				
				srchArr[0]='';
				srchArr[1]=Ext.getCmp('frompps').getRawValue();
				srchArr[2]=Ext.getCmp('topps').getRawValue();
				srchArr[3]=Ext.getCmp('contentpps').getValue();
				srchArr[4]=Ext.getCmp('dognumpps').getValue();
				srchArr[5]='';
				srchArr[6]='';
				srchArr[7]=Ext.getCmp('datefrompps').getRawValue();
				srchArr[8]='';
				srchArr[9]=Ext.getCmp('pps').getValue();
				srchArr[10]=Ext.getCmp('typespps').getValue();
				srchArr[11]=Ext.getCmp('signerpps').getValue();
				srchArr[12]='';
				srchArr[13]=Ext.getCmp('datetopps').getRawValue();
				srchArr[14]='';
				srchArr[15]='';
				srchArr[16]='';
				srchArr[17]='';
				srchArr[18]='';
				srchArr[19]='';
				for (i=0;i<20;i++) {
					if (srchArr[i].length==0)
					srchArr[i]='---';
				}
				srchArrStr = srchArr.join("|");
				shParam = srchArrStr;
				
var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
				update_table();
				ETD.ready();
				
			
			}).defer(1)
					
			}
		
	},{text:'Отмена',
		handler:function(){
		
			pps_filter.hide();
	
	}
	},{text:'Сбросить',
		handler:function(){	
		ETD.busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			Ext.getCmp('frompps').setValue('');
			Ext.getCmp('topps').setValue('');
			Ext.getCmp('contentpps').setValue('');
			Ext.getCmp('dognumpps').setValue('');
			Ext.getCmp('typespps').setValue('');
			Ext.getCmp('signerpps').setValue('');
			Ext.getCmp('datefrompps').setValue('');
			Ext.getCmp('datetopps').setValue('');
			Ext.getCmp('pps').setValue('');
			pps_filter.hide();
			srchArrStr = null;
			shParam = srchArrStr;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
		
		update_table();	
		ETD.ready();
		}).defer(1)
		}
		}
		
	
	]
});



rtk_filter = new Ext.Window({
	plain: 'true',
	width: 300,
	layout: 'form',
	closeAction: 'close', 
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [	
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'dognumrtk',fieldLabel:'Номер договора',width:150},
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'packagertk',fieldLabel:'Пакет',width:150}
		],
	
	
		buttons: [{text: 'OK', id: 'submitfind',
		handler:function(){
		
			ETD.busy();
			(function(){
				rtk_filter.hide();
			Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				
				srchArr[0]='';
				srchArr[1]='';
				srchArr[2]='';
				srchArr[3]='';
				srchArr[4]=Ext.getCmp('dognumrtk').getValue();
				srchArr[5]=Ext.getCmp('packagertk').getValue();
				srchArr[6]='';
				srchArr[7]='';
				srchArr[8]='';
				srchArr[9]='';
				srchArr[10]='';
				srchArr[11]='';
				srchArr[12]='';
				srchArr[13]='';
				srchArr[14]='';
				srchArr[15]='';
				srchArr[16]='';
				srchArr[17]='';
				srchArr[18]='';
				srchArr[19]='';
				for (i=0;i<20;i++) {
					if (srchArr[i].length==0)
					srchArr[i]='---';
				}
				srchArrStr = srchArr.join("|");
				shParam = srchArrStr;
				
var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
				update_table();
				ETD.ready();
				
			
			}).defer(1)
					
			}
		
	},{text:'Отмена',
		handler:function(){
		
			rtk_filter.hide();
	
	}
	},{text:'Сбросить',
		handler:function(){	
		ETD.busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			Ext.getCmp('dognumrtk').setValue('');
			Ext.getCmp('packagertk').setValue('');
			rtk_filter.hide();
			srchArrStr = null;
			shParam = srchArrStr;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
		
		update_table();	
		ETD.ready();
		}).defer(1)
		}
		}
		
	
	]
});


rzds_filter = new Ext.Window({
	plain: 'true',
	width: 300,
	layout: 'form',
	closeAction: 'close', 
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	
	defaults: {
		labelStyle: 'width:120px;'
	},
		
items: [	
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'packagerzds',fieldLabel:'Номер комплекта',width:150},
        	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreTypes,width:150,displayField:'name',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'typesrzds',fieldLabel:'Тип документа',  allowBlank:true ,width:150},
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'dognumrzds',fieldLabel:'Договор',width:150},
        	{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'contentrzds',fieldLabel:'Номер документа',width:150},
        	{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreNames,width:150,displayField:'name',
					valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'signerrzds',fieldLabel:'Текущий исполнитель',  allowBlank:true ,width:150},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата последней подписи</div>',style:'font-size: 12px;'},
			{xtype:'datefield',id:'fromrzds',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'torzds',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150}
		],
	
	
		buttons: [{text: 'OK', id: 'submitfind',
		handler:function(){
		
			ETD.busy();
			(function(){
				rzds_filter.hide();
			Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				
				srchArr[0]='';
				srchArr[1]=Ext.getCmp('fromrzds').getRawValue();
				srchArr[2]=Ext.getCmp('torzds').getRawValue();
				srchArr[3]=Ext.getCmp('contentrzds').getValue();
				srchArr[4]=Ext.getCmp('dognumrzds').getValue();
				srchArr[5]=Ext.getCmp('packagerzds').getValue();
				srchArr[6]='';
				srchArr[7]='';
				srchArr[8]='';
				srchArr[9]='';
				srchArr[10]=Ext.getCmp('typesrzds').getValue();
				srchArr[11]=Ext.getCmp('signerrzds').getValue();
				srchArr[12]='';
				srchArr[13]='';
				srchArr[14]='';
				srchArr[15]='';
				srchArr[16]='';
				srchArr[17]='';
				srchArr[18]='';
				srchArr[19]='';
				for (i=0;i<20;i++) {
					if (srchArr[i].length==0)
					srchArr[i]='---';
				}
				srchArrStr = srchArr.join("|");
				shParam = srchArrStr;
				
var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
				update_table();
				ETD.ready();
				
			
			}).defer(1)
					
			}
		
	},{text:'Отмена',
		handler:function(){
		
			rzds_filter.hide();
	
	}
	},{text:'Сбросить',
		handler:function(){	
		ETD.busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			Ext.getCmp('dognumrzds').setValue('');
			Ext.getCmp('packagerzds').setValue('');
			Ext.getCmp('typesrzds').setValue('');
			Ext.getCmp('contentrzds').setValue('');
			Ext.getCmp('fromrzds').setValue('');
			Ext.getCmp('torzds').setValue('');
			Ext.getCmp('signerrzds').setValue('');
			rzds_filter.hide();
			srchArrStr = null;
			shParam = srchArrStr;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
		
		update_table();	
		ETD.ready();
		}).defer(1)
		}
		}
		
	
	]
});

reportwin = new Ext.Window({
	plain: 'true',
	width: 300,
	height:150,
	layout: 'form',
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'right',
	closable: false,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	defaults: {
		labelStyle: 'width:120px;' 
	},
	
items: [
	        {xtype:'combo',store:type_store,width:150,displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'doctype',fieldLabel:'Вид формы'},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата создания</div>',style:'font-size: 12px;'},
			{xtype:'datefield',id:'repfrom',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'repto',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'По',width:150}
					],
		buttons: [{text: 'OK',
		handler:function(){
			
			(function(){
				
				Ext.getCmp('repfilter').addClass('but-flt');
				var srchRepArr = new Array();
				srchRepArr[0]=Ext.getCmp('doctype').getValue();
				srchRepArr[1] = Ext.getCmp('repfrom').getValue();
				srchRepArr[2]=Ext.getCmp('repto').getValue();
				
				for (i=0;i<3;i++) if (srchRepArr[i].length==0) srchRepArr[i]='---';
				srchRepArrStr = srchRepArr.join("|");
				shRepParam = srchRepArrStr;
				
reportwin.hide();
	
ETD.openDocument('13026', 'Акт',null,1);

	

			}).defer(1)	
		}	
	},{text:'Отмена',
		handler:function(){
		reportwin.hide();
		}
	},{text:'Сбросить',
		handler:function(){	
	
		(function(){
			Ext.getCmp('repfilter').removeClass('but-flt');
			Ext.getCmp('doctype').setValue('');
			Ext.getCmp('repfrom').setValue('');
			Ext.getCmp('repto').setValue('');
			reportwin.hide();
			srchRepArrStr = null;
			shRepParam = srchRepArrStr;
		}).defer(1)
		}
		}
	]
});


otchettwin = new Ext.Window({
	plain: 'true',
	width: 300,
	id:'otchettwin',

	layout: 'form',
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: true,
	closeAction: 'hide',
	title:'Выберите отчет',
	style:'text-align: center;',
	defaults: {
		labelStyle: 'width:120px;'
	},

items: [{xtype:'combo',width:270,store:report_store,displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'reportdoctype', hideLabel:'true'}],
		buttons: [{text: 'OK',
		handler:function(){
			
			(function(){
				otchettwin.hide();
				document.applet_adapter.createReport(Ext.getCmp('reportdoctype').getValue());
				CreateOpenReport();
				
				
				
			}).defer(1)	
		}	
	},{text:'Отмена',
		handler:function(){
			otchettwin.hide();
		}
	}
	]
});



css_filter = new Ext.Window({
	plain: 'true',
	width: 330,
	minwidth:330,
	height:400,
	layout: 'form',
	closeAction: 'close', 
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	shadow: false,
	autoScroll: true,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreTypes,width:150,displayField:'name',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'typesCSS',fieldLabel:'Тип документа',  allowBlank:true ,width:150},      	
	        {xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'contentCSS',fieldLabel:'Описание',width:150},
			{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'packageCSS',fieldLabel:'Пакет',width:150},
			{xtype:'field',labelStyle:Ext.isIE?'width:120px;':'width:109px;',displayField:'text',hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'priceCSS',fieldLabel:'Стоимость',width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:combocrstoreNames,width:150,displayField:'name',
			valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'signerCSS',fieldLabel:'Текущий исполнитель',  allowBlank:true ,width:150},
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата последней подписи</div>',style:'font-size: 12px;'},
			{xtype:'datefield',id:'fromCSS',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'С',width:150},
			{xtype:'datefield',id:'toCSS',maxDate: new Date(),format : 'Y-m-d',hidden: false,editable: false,fieldLabel:'По',width:150},
			{xtype:'combo',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:stat_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'statCSS',fieldLabel:'Статус',  allowBlank:true ,width:150},
			{xtype:'multiselect',labelStyle:Ext.isIE?'width:120px;':'width:109px;',store:sftype_store,width:150,displayField:'text',valueField:'id', hidden: false,editable: false,mode: 'local',triggerAction: 'all',id:'sftypeCSS',fieldLabel:'Вид СФ',  allowBlank:true ,width:150}
					],
					
listeners: {
	
	}, 
	
		buttons: [{text: 'OK', id: 'submitfind',
		handler:function(){
			ETD.busy();
			(function(){
				css_filter.hide();
			Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				srchArr[0]='';
				srchArr[1]=Ext.getCmp('fromCSS').getRawValue();
				srchArr[2]=Ext.getCmp('toCSS').getRawValue();
				srchArr[3]=Ext.getCmp('contentCSS').getValue();
				srchArr[4]='';
				srchArr[5]=Ext.getCmp('packageCSS').getValue();
				srchArr[6]='';
				srchArr[7]='';
				srchArr[8]='';
				srchArr[9]='';
				srchArr[10]=Ext.getCmp('typesCSS').getValue();
				srchArr[11]=Ext.getCmp('signerCSS').getValue();
				srchArr[12]=Ext.getCmp('statCSS').getValue();
				srchArr[13]='';
				srchArr[14]='';
				srchArr[15]='';
				srchArr[16]=Ext.getCmp('sftypeCSS').getValue();
				srchArr[17]='';
				srchArr[18]='';
				srchArr[19]=Ext.getCmp('priceCSS').getValue();
				for (i=0;i<20;i++) if (srchArr[i].length==0) srchArr[i]='---';
				srchArrStr = srchArr.join("|");
				shParam = srchArrStr;
				var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
				update_table();
				ETD.ready();
			}).defer(1)
			}
		
	},{text:'Отмена',
		handler:function(){
		css_filter.hide();
	
	}
	},{text:'Сбросить',
		handler:function(){	
		ETD.busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			Ext.getCmp('fromCSS').setValue('');
			Ext.getCmp('toCSS').setValue('');
			Ext.getCmp('contentCSS').setValue('');
			Ext.getCmp('packageCSS').setValue('');
			Ext.getCmp('signerCSS').setValue('');
			Ext.getCmp('statCSS').setValue('');
			Ext.getCmp('typesCSS').setValue('');
			Ext.getCmp('sftypeCSS').setValue('');
			Ext.getCmp('priceCSS').setValue('');
			ETD.di = "";
			css_filter.hide();
			srchArrStr = null;
			shParam = srchArrStr;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage(srchArrStr);
		remArray = eval('('+document.applet_adapter.getLoadRem("")+')');
		combocrstoreREM.loadData(remArray, false);
		update_table();	
		ETD.ready();
		}).defer(1)
		}
		}
		
	
	]
});





	var counterpartsArray;
	sendArr = counterpartsArray;
	counterparts_store = new Ext.data.SimpleStore({

	autoLoad: false,
	fields:['id', 'text']
	}); 

	var counterpersArray;
	counterpers_store = new Ext.data.SimpleStore({

	autoLoad: false,
	fields:['id', 'text']
	}); 

	crwinOCO = new Ext.Window({
	plain: 'true',
	id: 'crwinOCO',
	title: "Выберите документ",
	width: 270,
	height:100,
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	style:'text-align: center;',
	
	buttons: [{text: 'Создать',
	handler:function(){ 

	if (Ext.getCmp('combocrOCO').getValue().length>0){
	crwinOCO.hide();
	
	
	if (Ext.getCmp('combocrOCO').getValue().indexOf('Документ')==-1){
	
	(function(){ 
		
		var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
		
		document.applet_adapter.createDocument(combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),0,'');
		
		CreateOpenDoc();
		
		}).defer(1);
	}
	
	else {
		
		
	
	Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
	counterpartsArray = eval('('+document.applet_adapter.getCounterPartTypes()+')');	
	sendArr = counterpartsArray;
	counterparts_store.loadData(counterpartsArray);
	(Ext.getCmp('comboСounterparts').getValue()!=null&&Ext.getCmp('comboСounterparts').getValue().length>0)?Ext.getCmp('comboСounterparts').setValue(Ext.getCmp('comboСounterparts').getValue()):Ext.getCmp('comboСounterparts').setValue(counterparts_store.getTotalCount()>0?counterparts_store.getAt(0).get('text'):"");

	Ext.getCmp('comboCounterPers').disable();
	
	if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1&&Ext.getCmp('comboСounterparts').getValue()!=null&&Ext.getCmp('comboСounterparts').getValue().length>0)
	{	
	Ext.getCmp('comboCounterPers').enable();
	var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
	
	counterpartsArray = eval('('+document.applet_adapter.getCounterPersTypes( counterparts_store.getAt(rec).get('id'))+')');	
	counterpers_store.loadData(counterpartsArray);
	(Ext.getCmp('comboCounterPers').getValue()!=null&&Ext.getCmp('comboCounterPers').getValue().length>0)?Ext.getCmp('comboCounterPers').setValue(Ext.getCmp('comboCounterPers').getValue()):Ext.getCmp('comboCounterPers').setValue(counterpers_store.getTotalCount()>0?counterpers_store.getAt(0).get('text'):"");

	}
	Ext.Msg.hide(); 


	
	if((counterpers_store.getTotalCount()>1&&Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1)||counterparts_store.getTotalCount()>1||counterparts_store.getTotalCount()==0)
	{ 
	counterpartsWin.show();
	}
	else if(counterparts_store.getTotalCount()==1)
	{
	var fla = 0;
	if(Ext.getCmp('comboCounterPers').disabled)
	{
	fla = 1;
	}
	else if(counterpers_store.getTotalCount()==1&&counterparts_store.getTotalCount()>0)
	{
	fla = 1;
	}

	if(fla==1)
	{

	if (Ext.getCmp('comboСounterparts').getValue().length>0){

	if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1) 
	{ 
	if (Ext.getCmp('comboCounterPers').getValue().length>0){

	(function(){ 
	var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue()); 
	var recdCounter = counterpers_store.find('text', Ext.getCmp('comboCounterPers').getValue());
	document.applet_adapter.createDocumentMSG(sendArr,combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterpers_store.getAt(recdCounter).get('id'),counterpers_store.getAt(recdCounter).get('text'), counterparts_store.getAt(rec).get('id'),counterparts_store.getAt(rec).get('text'));
	CreateOpenDoc(); 
	}).defer(1);
	} 
	}
	else
	{

	(function(){ 
	var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	var recdCounter = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
	//ETD.busy();
	document.applet_adapter.createDocument(combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterparts_store.getAt(recdCounter).get('id'),counterparts_store.getAt(recdCounter).get('text'));
	//ETD.ready();
	CreateOpenDoc();
	
	}).defer(1);
	} 
	}
	}

	}
	}

	}
	}
	},{text:'Отмена',
	handler:function(){
	crwinOCO.hide();
	}}]
	});

	crwinOCO.add({xtype:'combo',id:'combocrOCO',store:combocrstore, width:220,displayField:'type',hidden: false,editable: false,mode: 'local',triggerAction: 'all' });




	counterpartsWin = new Ext.Window({
	plain: 'true',
	id: 'counterpartsWin',
	title: "Выберите адресата",
	width: 270,
	height:150,
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	style:'text-align: center;',
	items: [{xtype:'combo',id:'comboСounterparts',store:counterparts_store, width:220,displayField:'text'/*,valueField:'id'*/,hidden: false,editable: false,mode: 'local',triggerAction: 'all',style:"margin-top: 7px; margin-bottom:7px;",listeners:{'select':function(combo, record, index){
	if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1)
	{	
	Ext.getCmp('comboCounterPers').enable();
	var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
	
	Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
	counterpartsArray = eval('('+document.applet_adapter.getCounterPersTypes( counterparts_store.getAt(rec).get('id'))+')');	
	counterpers_store.loadData(counterpartsArray);
	Ext.Msg.hide();
	
	}
	else
	{
	Ext.getCmp('comboCounterPers').disable();
	}
	}} }
	,{xtype:'combo',id:'comboCounterPers',style:"margin-top: 7px; margin-bottom:7px;",store:counterpers_store, width:220,displayField:'text'/*,valueField:'id'*/,hidden: false,editable: false,mode: 'local',triggerAction: 'all' }],
	
	buttons: [{text: 'OK',
	handler:function(){ 
	if (Ext.getCmp('comboСounterparts').getValue().length>0){
	counterpartsWin.hide();
	
	if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1)
	
	{ 
	

	if (Ext.getCmp('comboCounterPers').getValue().length>0){

	(function(){ 
	var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue()); 

	var recdCounter = counterpers_store.find('text', Ext.getCmp('comboCounterPers').getValue());
	document.applet_adapter.createDocumentMSG(sendArr,combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterpers_store.getAt(recdCounter).get('id'),counterpers_store.getAt(recdCounter).get('text'), counterparts_store.getAt(rec).get('id'),counterparts_store.getAt(rec).get('text')/*record.get('id')record.get('type')*/);
	CreateOpenDoc();
	
	}).defer(1);
	}

	}
	else
	{

	(function(){ 
	var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	var recdCounter = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
	document.applet_adapter.createDocument(null,combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterparts_store.getAt(recdCounter).get('id'),counterparts_store.getAt(recdCounter).get('text')/*record.get('id')record.get('type')*/);
	CreateOpenDoc();
	}).defer(1);
	} 

	}
	}
	},{text:'Отмена',
	handler:function(){
	counterpartsWin.hide();
	}}]
	});
    
    	
    	counterPersWin = new Ext.Window({
    		plain: 'true',
    	        id: 'counterPersWin',
    		title: "Выберите получателя",
    		width: 270,
    		height:100,
    		resizable: false,
    		modal:true,
    		border:true,
    		buttonAlign:'center',
    		closable: false,
    		style:'text-align: center;',
    		buttons: [{text: 'OK',
    			handler:function(){    			
    			if (Ext.getCmp('comboCounterPers').getValue().length>0){
    				counterPersWin.hide();
    				busy();
    				(function(){    				
    					var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
    					var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());    									 
						var recdCounter = counterpers_store.find('text', Ext.getCmp('comboCounterPers').getValue());
    					document.applet_adapter.createDocumentMSG(combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterpers_store.getAt(recdCounter).get('id'),counterpers_store.getAt(recdCounter).get('text'), counterparts_store.getAt(rec).get('id'),counterparts_store.getAt(rec).get('text')/*record.get('id')record.get('type')*/);
    					CreateOpenDoc();
    				}).defer(1);
    			}
    			}
    		},{text:'Отмена',
    			handler:function(){
    			counterPersWin.hide();
    			}}]
    	});
    
	
	


	var buttonclass;
	if(Ext.isGecko)buttonclass='somebutton-moz'
	else buttonclass='somebutton'
		
	
	var createTypes = eval('('+document.applet_adapter.getCreateTypes(false)+')');
	if(createTypes.data) 
	{
		combocrstore.loadData(createTypes);
		
	
	}

	var name_div = new Ext.form.Label({
		renderTo: 'name',
		html: name
	});

	var depname_div = new Ext.form.Label({
		renderTo: 'depname',
		html: depname
	});
	var predname_div = new Ext.form.Label({
		renderTo: 'predname',
		html: predname
	});
	
	
Ext.getCmp('tpanel').getActiveTab().getBottomToolbar().add(
		new Ext.form.ComboBox({
		store:new Ext.data.SimpleStore({
	        fields: ['rows'],
	        data : [[20],[50],[100],[250]]}),
		width:60,
		value:ETD.docsPerPage,
		id: 'ROWSCOUNTID',
		displayField:'rows',
		hidden: false,
		editable: false,
	    mode: 'local',
		triggerAction: 'all',
	    selectOnFocus: true

			}));


	Ext.getCmp('ROWSCOUNTID').on('select',function(combobox){
		busy();
		(function(){
			ETD.busy();
			ETD.docsPerPage = parseInt(Ext.getCmp('ROWSCOUNTID').getValue(), 10);
			Ext.getCmp('tpanel').getActiveTab().getBottomToolbar().pageSize =  parseInt(Ext.getCmp('ROWSCOUNTID').getValue(), 10);
			Ext.getCmp('tpanel').getActiveTab().isLoaded = true;
			Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
			ETD.ready();
		}).defer(1);
	});




	tpanel.on('tabchange',function(tabPanel, tab){
	
ETD.tabname = tab.getId();


		Dev.logger.log('Active tab changed ID : ', tab.getId());
		Dev.logger.log('isLoaded : ', tab.isLoaded);
		
		tab.getBottomToolbar().pageSize =  ETD.docsPerPage;
		
		if (tab.getId() == 'ID_DOCUMENT_GRID')
		{
			if (tab.getSelectionModel().hasSelection())
			{
				var record = tpanel.getActiveTab().getSelectionModel().getSelected();
				
			};
		}
		else if(tab.getId() == 'ID_ARCHIVE_GRID' || tab.getId() == 'ID_DROP_GRID' )
		{
			
		};
		
(function (){

			Ext.getCmp('ROWSCOUNTID').destroy();
			
			

			tab.getBottomToolbar().add(new Ext.form.ComboBox({
				store:new Ext.data.SimpleStore({
			        fields: ['rows'],
			        data : [[20],[50],[100],[250]]}),
				width:60,
				value:ETD.docsPerPage,
				id: 'ROWSCOUNTID',
				displayField:'rows',
				hidden: false,
				editable: false,
			    mode: 'local',
				triggerAction: 'all',
			    selectOnFocus: true

			}));
		

			Ext.getCmp('ROWSCOUNTID').on('select',function(combobox){
				busy();
				(function(){
					ETD.busy();
					
					
			ETD.docsPerPage = parseInt(Ext.getCmp('ROWSCOUNTID').getValue(), 10);
			Ext.getCmp('tpanel').getActiveTab().getBottomToolbar().pageSize =  parseInt(Ext.getCmp('ROWSCOUNTID').getValue(), 10);
			Ext.getCmp('tpanel').getActiveTab().isLoaded = true;
			Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
			ETD.ready();
			}).defer(1);
			});
}).defer(1);


		clear_win();
		
		tab.loadFirstPage();
	
		
	});

	Ext.override(Ext.data.SimpleStore, {
 		applySort : function() {
  	}
	});
	
	Ext.override(Ext.layout.FormLayout, {
	    renderItem : function(c, position, target){
	        if(c && !c.rendered && c.isFormField && c.inputType != 'hidden'){
	            var args = [
	                   c.id, c.fieldLabel,
	                   c.labelStyle||this.labelStyle||'',
	                   this.elementStyle||'',
	                   typeof c.labelSeparator == 'undefined' ? this.labelSeparator : c.labelSeparator,
	                   (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
	                   c.clearCls || 'x-form-clear-left' 
	            ];
	            if(typeof position == 'number'){
	                position = target.dom.childNodes[position] || null;
	            }
	            if(position){
	                c.formItem = this.fieldTpl.insertBefore(position, args, true);
	            }else{
	                c.formItem = this.fieldTpl.append(target, args, true);
	            }

	            c.on('destroy', c.formItem.remove, c.formItem, {single: true});
	            c.render('x-form-el-'+c.id);
	        }else {
	            Ext.layout.FormLayout.superclass.renderItem.apply(this, arguments);
	        }
	    }
	});
	//for SFGrid
	mkviewwin = function (){
		ETD.sfcreate();
	
viewsgnswin = new Ext.Window({
	plain: 'true',
	width: 700,
//	autoWidth:true,
	layout: 'form',
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'right',
	closable: false,
	style:'text-align: center;',
	defaults: {
		labelStyle: 'width:120px;'
	},
	
items: [SFdoc.grid],
		buttons: [{text:'Закрыть',
		handler:function(){
		viewsgnswin.close();
		}
	}]});		
viewsgnswin.show();
viewsgnswin.focus();
	}
	
	
Ext.form.TriggerField.override({
afterRender : function(){
Ext.form.TriggerField.superclass.afterRender.call(this);
var y;
if(Ext.isIE && !this.hideTrigger && this.el.getY() != (y = this.trigger.getY())){
this.el.position();
this.el.setY(y);
}
}
});

Ext.getCmp('combocrOCO').setValue(combocrstore.getTotalCount()>0?combocrstore.getAt(0).get('type'):"");	
	Ext.getCmp('comboСounterparts').setValue(counterparts_store.getTotalCount()>0?counterparts_store.getAt(0).get('text'):"");
	Ext.getCmp('comboCounterPers').setValue(counterpers_store.getTotalCount()>0?counterpers_store.getAt(0).get('text'):"");
	
	
	(function(){
	
		Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
	
		
		toppanel.show();
		toppanel.expand(false);
		cpanel_layout.show();
		cpanel_layout.expand(false);
		
		
		cpanel.show();
		cpanel.expand(false);
		Ext.getCmp('btnpanel').show();
		Ext.getCmp('btnpanel').expand(false);
		
		ETD.ready();
	}).defer(1);
	

}

function printDoc() {
	myWin = open('', 'displayWindow', 'width=800,height=600');
	myWin.document.open();
	myWin.document.write(testform);
	myWin.PrintWindow();
	}


function PrintWindow()
{                     
   
   CheckWindowState(); 
}

function CheckWindowState()
{            
    if(document.readyState=="complete")
    {
      document.execCommand('print', false, null);
	  window.close();
    }
    else
    {            
        setTimeout("CheckWindowState()", 500);
    }
}    

function hideCombo(combo){
	combo.disable();
	combo.hide();
	combo.getEl().up('.x-form-item').setDisplayed(false); // hide label
}
function showCombo(combo){
	combo.enable();
	combo.show();
	combo.getEl().up('.x-form-item').setDisplayed(true); // hide label
}