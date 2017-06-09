//Ext.namespace('SampleSearchData');
if(typeof SampleSearchData == "undefined" || !SampleSearchData){SampleSearchData={}};

var chromeDatePickerCSS = ".x-date-picker {border-color: #1b376c;background-color:#fff;position: relative;width: 185px;}";
Ext.util.CSS.createStyleSheet(chromeDatePickerCSS,'chromeDatePickerStyle');

ru.aisa.createButtonSample = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		conf = {xtype:'button',text:'Выборка'}
		ru.aisa.createButtonSample.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			var width = ru.aisa.width;
			var st1 = new Ext.data.SimpleStore({url:'controllers/predid',fields:['id','text'],autoLoad:false});
			var predid = new Ext.form.ComboBox({fieldLabel:'Предприятие',editable:true,forceSelection:true,triggerAction:'all',mode:'local',store:st1,displayField:'text',valueField:'id',width:width,hiddenName:'rec',allowBlank:false,value:grid.getStore().baseParams.predid});
			var dateFrom = new Ext.form.DateField({xtype:'datefield',id:'repfrom',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'С',width:150,value:grid.getStore().baseParams.dateFrom});
			var dateTo = new Ext.form.DateField({xtype:'datefield',id:'repto',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'По',width:150,value:grid.getStore().baseParams.dateTo});


			//st2.load({callback:function(){
			st1.load({callback:function(){
				var width = ru.aisa.width;
				if(config.fullwidth =='true') width=ru.aisa.width1;
				var items = [  {
					xtype : 'hidden',
					value:'new',name:'act'
				},
				predid,dateFrom,dateTo];

				var form1 = new Ext.form.FormPanel({
					id:'form1',
					border:false,
					autoScroll:false,
					autoHeight:true,
					style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
					labelWidth:120,
					buttons:[{xtype:'button',text:'Применить',handler:function(){
						createwin.destroy();
						if(predid.getValue().length<1){
							grid.getStore().baseParams.predid=null;
							SampleSearchData.predid = null;
						}else{ 
							grid.getStore().baseParams.predid=predid.getValue();
							grid.getStore().baseParams.searchField = button.field;
							SampleSearchData.predid = predid.getValue();
						}

						if(dateFrom.getValue().length<1){
							grid.getStore().baseParams.dateFrom=null;
							SampleSearchData.dateFrom = null;
						}else{ 
							grid.getStore().baseParams.dateFrom=dateFrom.getValue().format('Y-m-d');
							grid.getStore().baseParams.searchField = button.field;
							SampleSearchData.dateFrom = dateFrom.getValue().format('Y-m-d');
							//alert(dateFrom.getValue().format('Y-m-d'));
						}
						if(dateTo.getValue().length<1){
							grid.getStore().baseParams.dateTo=null;
							SampleSearchData.dateTo = null;
						}else{ 
							grid.getStore().baseParams.dateTo=dateTo.getValue().format('Y-m-d');
							grid.getStore().baseParams.searchField = button.field;
							SampleSearchData.dateTo = dateTo.getValue().format('Y-m-d');
							//alert(dateTo.getValue().format('Y-m-d'));
						}
						grid.getStore().load();

					}},
					{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}},
					{xtype:'button',text:'Очистить',handler:function(){
						createwin.destroy();
						grid.getStore().baseParams.predid=null;
						grid.getStore().baseParams.dateFrom=null;
						grid.getStore().baseParams.dateTo=null;
						grid.getStore().baseParams.searchField=null;
						SampleSearchData.predid = null;
						SampleSearchData.dateFrom = null;
						SampleSearchData.dateTo = null;
						grid.getStore().load();
					}}],
					items:items
				});		

				var createwin = new Ext.Window({closable:false,
					width:ru.aisa.width+155,
					autoScroll:true,
					resizable :false,
					title:'Введите параметры поиска',
					items:[form1],
					modal:true
				});
				createwin.show();
			}
			})

		})
	}});