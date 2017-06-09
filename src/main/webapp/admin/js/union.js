ru.aisa.megabutton = Ext.extend(Ext.Button,{
	
	constructor: function(config,grid){
	conf = {xtype:'button',text:'Добавить несколько'}
	ru.aisa.megabutton.superclass.constructor.apply(this,[conf]);
	this.on('click',function(){
		
		var st = new Ext.data.SimpleStore({url:'controllers/formtypes',fields:['id','text'],autoLoad:false});
		var st1 = new Ext.data.SimpleStore({url:'controllers/wrknamesoco',fields:['id','text'],autoLoad:false});
		st1.load({callback:function(){
		st.load({callback:function(){
		var width = ru.aisa.width;
		if(config.fullwidth =='true') width=ru.aisa.width1;
		var items = [ {
			xtype : 'hidden',
			value:'new',name:'act'
		}, 
		/*new Ext.form.ComboBox({fieldLabel:'Начальный отдел',
			editable:true,
			forceSelection:true,
			triggerAction:'all',
			mode:'local',
			store:config.columns[4].store(),
			displayField:'text',valueField:'id',
			width:width,
			hiddenName:'otd_first',
			allowBlank:false})*/
		new Ext.form.MultiSelectField({
			mode:'local',
			displayField:'text',valueField:'id',
			width:width,
			fieldLabel:'Начальный отдел',
			hiddenName:'otd_first',
			allowBlank:false,
			store:st1	})
		,
			new Ext.form.ComboBox({fieldLabel:'Конечный отдел',
				editable:true,
				forceSelection:true,
				triggerAction:'all',
				mode:'local',
				store:config.columns[4].store(),
				displayField:'text',valueField:'id',
				width:width,
				hiddenName:'otd_last',
				allowBlank:false}),
			new Ext.form.MultiSelectField({
			mode:'local',
			displayField:'text',valueField:'id',
			width:width,
			fieldLabel:'Формы',
			hiddenName:'forms',
			allowBlank:false,
			store:st
			})
		];
		var form = new Ext.form.FormPanel({
			id:'form',
			border:false,
			autoScroll:false,
			autoHeight:true,
			style:'margin-left: 5px;',
			labelWidth:120,
			buttons:[{xtype:'button',text:'Применить',handler:function()
			{
			if(form.getForm().isValid()){
				form.getForm().submit({url:'controllers/manySignatures',
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
			width:width+165,
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