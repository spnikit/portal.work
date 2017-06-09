var chromeDatePickerCSS = ".x-date-picker {border-color: #1b376c;background-color:#fff;position: relative;width: 185px;}";
	Ext.util.CSS.createStyleSheet(chromeDatePickerCSS,'chromeDatePickerStyle');
	
ru.aisa.createProtocol = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		conf = {xtype:'button',text:'Выгрузка'}
		ru.aisa.createProtocol.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			var width = ru.aisa.width;
			var address = "";
			var link = "";
			var st1 = new Ext.data.SimpleStore({url:'controllers/predid',fields:['id','text'],autoLoad:false});
			var predid = new Ext.form.ComboBox({fieldLabel:'Предприятие',editable:true,forceSelection:true,triggerAction:'all',mode:'local',store:st1,displayField:'text',valueField:'id',width:width,hiddenName:'rec',allowBlank:false});
			var dateFrom = new Ext.form.DateField({xtype:'datefield',id:'repfrom',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'С',width:150});
			var dateTo = new Ext.form.DateField({xtype:'datefield',id:'repto',maxDate: new Date(),hidden: false,editable: false,fieldLabel:'По',width:150});
			var link1;
			//var link = new Ext.form.TextField({id:'link',fieldLabel:'Ссылка', name:'link', width:width});
			//var link1 = new Ext.Component({id:'link1',autoEl:{tag: 'a',href: 'http://www.10.0.0.78:48080/IIT/uploadFileArchive',html: 'Скачать архив', display: 'block'}});
			
			//st2.load({callback:function(){
				st1.load({callback:function(){
				var width = ru.aisa.width;
				if(config.fullwidth =='true') width=ru.aisa.width1;
			var items = [  {
				xtype : 'hidden',
				value:'new',name:'act'
			},
			predid,dateFrom,dateTo];//,link1];
			
			
			
			var form1 = new Ext.form.FormPanel({
				id:'form1',
				border:false,
				autoScroll:false,
				autoHeight:true,
				style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
				labelWidth:120,
				buttons:[{xtype:'button',text:'Выгрузить',handler:function(){
				//	createwin.destroy();
					console.log("handler")
					if(predid.getValue().length<1)
						grid.getStore().baseParams.predid=null;
					else{ 
						grid.getStore().baseParams.predid=predid.getValue();
						grid.getStore().baseParams.searchField = button.field;
					}
					
					if(dateFrom.getValue().length<1)
						grid.getStore().baseParams.dateFrom=null;
					else{ 
						grid.getStore().baseParams.dateFrom=dateFrom.getValue().format('Y-m-d');
						grid.getStore().baseParams.searchField = button.field;	
					}
					if(dateTo.getValue().length<1)
						grid.getStore().baseParams.dateTo=null;
					else{ 
						grid.getStore().baseParams.dateTo=dateTo.getValue().format('Y-m-d');
						grid.getStore().baseParams.searchField = button.field;	
					}
					
					var param ='act=export&predid='+predid.getValue()+'&dateFrom='+dateFrom.getValue().format('Y-m-d')+'&dateTo='+dateTo.getValue().format('Y-m-d');
					var form = Ext.DomHelper.append(document.body, {
				        tag : 'form',
				        method : 'post',
				        action : 'controllers/uploadProtocol'+'?'+param		            
				    });
				    document.body.appendChild(form);
				    form.submit();
				    document.body.removeChild(frame);  
					createwin.destroy()
					console.log("handler11")
				}},
					{xtype:'button',text:'Выход',handler:function(){createwin.destroy()}},
					{xtype:'button',text:'Очистить',handler:function(){
						createwin.destroy();
						grid.getStore().baseParams.predid=null;
						grid.getStore().baseParams.dateFrom=null;
						grid.getStore().baseParams.dateTo=null;
						grid.getStore().baseParams.searchField=null;
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