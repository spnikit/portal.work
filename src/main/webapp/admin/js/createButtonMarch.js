ru.aisa.extCreateButt = Ext.extend(Ext.Button,{
	
	constructor: function(config,grid){
	conf = {xtype:'button',text:'Маршруты'}
	ru.aisa.extCreateButt.superclass.constructor.apply(this,[conf]);
	this.on('click',function(){
		
		var st = new Ext.data.SimpleStore({url:'controllers/wrknamessnt',fields:['id','text'],autoLoad:false});
		var st1 = new Ext.data.SimpleStore({url:'controllers/docdoctype',fields:['id','text'],autoLoad:false});
		st1.load({callback:function(){
		st.load({callback:function(){
		var width = ru.aisa.width;
		if(config.fullwidth =='true') width=ru.aisa.width1;
		var items = [ {
			xtype : 'hidden',
			value:'new',name:'act'
		},		
		new Ext.form.ComboBox({fieldLabel:'Получатель',
			editable:true,
			forceSelection:true,
			triggerAction:'all',
			mode:'local',
			store:st,
			displayField:'text',valueField:'id',
			width:width+50,
			hiddenName:'rec',
			allowBlank:false})
		,
			new Ext.form.ComboBox({fieldLabel:'Вид документооборота',
				editable:true,
				forceSelection:true,
				triggerAction:'all',
				mode:'local',
				store:st1,
				displayField:'text',valueField:'id',
				width:width+50,
				hiddenName:'docdoctype',
				allowBlank:false})
		];
		var form = new Ext.form.FormPanel({
			id:'form',
			border:false,
			autoScroll:false,
			autoHeight:true,
			style:'margin-left: 5px;',
			labelWidth:140,
			buttons:[{xtype:'button',text:'Применить',handler:function()
			{
			if(form.getForm().isValid()){
				form.getForm().submit({url:'controllers/manySignaturesTypes',
				waitMsg:'Подождите',success:function(){
					createwin.destroy();
					grid.getStore().reload()},failure:function(form,action){
			Ext.Msg.alert('Ошибка',action.result.description);
			createwin.destroy();}
			})}
			}},
				{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}}],
			items:items
			});
		var createwin = new Ext.Window({closable:false,
			width:width+165+70,
			autoScroll:true,
			resizable :false,
			title:'Добавить новые значения',
			items:[form],
			modal:true
			});
		createwin.show();
		}
		})
		}
		})
	})
}
});