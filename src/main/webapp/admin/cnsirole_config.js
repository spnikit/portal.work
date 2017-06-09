
			{notgrid:true,title:'Синхронизация с ЦНСИ',config:{title:'Синхронизация с ЦНСИ',id:'CNSISyncPanel',items:[new Ext.form.FormPanel({width:300,id:'cnsi-form',border:false,reader: new Ext.data.JsonReader(),
			items:[{xtype:'hidden',name:'act',value:'get'},{xtype:'hidden',name:'cnsi_sync',value:'get_timetable'},
			{
				xtype:'label',
				text: 'Дата последней синхронизации:',
				width: 250,
				id:'CNSIlastsync',
				style:'margin-left: 5px; margin-top:5px;'
			}],
			buttons:[{xtype:'button',text:'Настройка расписания',handler:function(){
			
			
			var panel1 = new Ext.FormPanel({
        	layout: 'table',
        	layoutConfig: 
        	{
        		columns: 2
    		},
        	frame: true,
        	width: 250,
        	items: [
			{xtype:'hidden',name:'act',value:'edit'},
			{xtype:'hidden',name:'cnsi_sync',value:'edit'},
        	{
     			xtype:'checkbox',
   				name: 'Mo',
				id: 'Mo',
     			hideLabel: true,
     			boxLabel: 'Понедельник',
     			width: 155
			},
			{
  				xtype: 'timefield',
 				name: 'MoT',
 				id: 'MoT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false
 				
			},
			
			{
  				xtype: 'checkbox',
 				name: 'Tu',
 				id: 'Tu',
     			hideLabel: true,
     			boxLabel: 'Вторник'
			},
			{
  				xtype: 'timefield',
 				name: 'TuT',
 				id: 'TuT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false 
			},
			{
  				xtype: 'checkbox',
 				name: 'We',
 				id: 'We',
     			hideLabel: true,
     			boxLabel: 'Среда'
			},
			{
  				xtype: 'timefield',
 				name: 'WeT',
 				id: 'WeT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false 
			},
			{
  				xtype: 'checkbox',
 				name: 'Th',
 				id: 'Th',
     			hideLabel: true,
     			boxLabel: 'Четверг'
			},
			{
  				xtype: 'timefield',
 				name: 'ThT',
 				id: 'ThT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false 
			},			
			{
  				xtype: 'checkbox',
 				name: 'Fr',
 				id: 'Fr',
     			hideLabel: true,
     			boxLabel: 'Пятница'
			},
			{
  				xtype: 'timefield',
 				name: 'FrT',
 				id: 'FrT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false 
			},
			{
  				xtype: 'checkbox',
 				name: 'Sa',
 				id: 'Sa',
     			hideLabel: true,
     			boxLabel: 'Суббота'
			},
			{
  				xtype: 'timefield',
 				name: 'SaT',
 				id: 'SaT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false  
			},
			{
  				xtype: 'checkbox',
 				name: 'Su',
 				id: 'Su',
     			hideLabel: true,
     			boxLabel: 'Воскресенье'
			},
			{
  				xtype: 'timefield',
 				name: 'SuT',
 				id: 'SuT',
 				width: 70,
				increment: 5,
				format: 'H:i',
				editable: false 
			}
			
        	],
			buttons:
			[
				{	text:'Сохранить',handler:function()
				{
								panel1.getForm().submit(
								{url:'controllers/cnsi',
								waitMsg:'Подождите',failure:function(panel1,action)
									{
										Ext.Msg.alert('Ошибка',action.result.description);
									},
									success: function(panel1, action) 
									{
									var cmp = Ext.getCmp('CNSIlastsync');	
									var t = 'Дата последней синхронизации: ' + action.result.data.lastsync;				
                    				cmp.setText(t);
									w1.destroy();
									}
								});								
								 
				}}
			]
   			}
   			);
			
			
			var panel3 = new Ext.FormPanel({
        	frame: true,
        	width: 250,
			items: [
						{
							xtype:'label',
							text: 'Укажите день и время запуска процесса синхронизации',
							width: 200
						}
					]						
			});
			
			
			var w1 = new Ext.Window(
			{
				width: 250,
				autoHeight: true,
				resizable : false,
				title:'Настройка расписания',
				items:[panel3,panel1],
				modal:true
			});
			
			var mainform = Ext.getCmp('cnsi-form');
			mainform.getForm().submit(
			{url:'controllers/cnsi',
				waitMsg:'Подождите',failure:function(mainform,action)
				{
					Ext.Msg.alert('Ошибка',action.result.description);
				},
				success: function(mainform, action) 
				{
					var cmp = Ext.getCmp('Mo');					
                    cmp.setValue(action.result.data[0].Mo);
                    cmp = Ext.getCmp('Tu');					
                    cmp.setValue(action.result.data[0].Tu);
                    cmp = Ext.getCmp('We');					
                    cmp.setValue(action.result.data[0].We);
                    cmp = Ext.getCmp('Th');	
					cmp.setValue(action.result.data[0].Th);
					cmp = Ext.getCmp('Fr');	
					cmp.setValue(action.result.data[0].Fr);
					cmp = Ext.getCmp('Sa');	
					cmp.setValue(action.result.data[0].Sa);
					cmp = Ext.getCmp('Su');	
					cmp.setValue(action.result.data[0].Su);	
					
								
					cmp = Ext.getCmp('MoT');	
					cmp.setValue(action.result.data[0].MoT);
					cmp = Ext.getCmp('TuT');	
					cmp.setValue(action.result.data[0].TuT);
					cmp = Ext.getCmp('WeT');	
					cmp.setValue(action.result.data[0].WeT);
					cmp = Ext.getCmp('ThT');	
					cmp.setValue(action.result.data[0].ThT);
					cmp = Ext.getCmp('FrT');	
					cmp.setValue(action.result.data[0].FrT);
					cmp = Ext.getCmp('SaT');	
					cmp.setValue(action.result.data[0].SaT);
					cmp = Ext.getCmp('SuT');	
					cmp.setValue(action.result.data[0].SuT);
				
					
                }

			});			
				
				w1.show();
			
			 
			
   			
   			}
   			},
			{		
				xtype:'button',text:'Принудительный запуск',handler:function()
				{				
					var panel2 = new Ext.FormPanel(
					{
						id:'prinud_zap',
        				frame: true,
        				width: 200,
        				items: 
        				[
							{xtype:'hidden',name:'act',value:'edit'},
							{xtype:'hidden',name:'cnsi_sync',value:'forcibly'},
        					{
							xtype:'label',
							text: 'Для запуска процесса синхронизации нажмите "Старт"',
							width: 170
							}	
        				],
						buttons:
						[{
							text:'Старт',
							handler:function()
							{
								var form = Ext.getCmp('prinud_zap');
								form.getForm().submit(
								{url:'controllers/cnsi',
								waitMsg:'Подождите',failure:function(form,action)
									{
										Ext.Msg.alert('Ошибка',action.result.description);
									}
								});								
								w2.hide();
							}
						}]
        			});
					
					var w2 = new Ext.Window({
					autoHeight:true,
					width: 200,
					resizable : false,
					title:'Принудительный запуск',
					items:[
					panel2					
					],
					
					modal:true
					});
					w2.show();
        			
				}
			}
				
		]
		
		})]}}