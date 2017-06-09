ru.aisa.megabuttonrights = Ext.extend(Ext.Button,{
	
	constructor: function(config,grid){
	conf = {xtype:'button',text:'Создать'}
	ru.aisa.megabuttonrights.superclass.constructor.apply(this,[conf]);
	this.on('click',function(){
		
		var st = new Ext.data.SimpleStore({url:'controllers/formtypes',fields:['id','text'],autoLoad:false,baseParams:{all:true}});
		var st1 = new Ext.data.SimpleStore({url:'controllers/wrknamesoco',fields:['id','text'],autoLoad:false});
		st1.load({callback:function(){
		st.load({callback:function(){
		var width = ru.aisa.width;
		if(config.fullwidth =='true') width=ru.aisa.width1;
		var items = [ {
			xtype : 'hidden',
			value:'new',name:'act'
		},
			new Ext.form.MultiSelectField({
			mode:'local',
			displayField:'text',valueField:'id',
			width:width,
			fieldLabel:'Формы',
			hiddenName:'docname',
			allowBlank:false,
			store:st
			}),
			new Ext.form.MultiSelectField({
				mode:'local',
				displayField:'text',valueField:'id',
				width:width,
				fieldLabel:'Должности',
				hiddenName:'wrkname',
				allowBlank:false,
				store:st1
				}),
			new Ext.form.FieldSet({
				style:'margin-bottom:0px;padding:5px;',
				border:false,autoHeight: true,
				items:[{fieldLabel: 'Просмотр',boxLabel: 'Разрешить',name: 'cview'}],defaultType:'checkbox'})
		,
		new Ext.form.FieldSet({
			style:'margin-bottom:0px;padding:5px;',
			border:false,autoHeight: true,
			items:[{fieldLabel: 'Редактирование',boxLabel: 'Разрешить',name: 'cedit'}],defaultType:'checkbox'})
		,
		new Ext.form.FieldSet({
			style:'margin-bottom:0px;padding:5px;',
			border:false,autoHeight: true,
			items:[{fieldLabel: 'Создание',boxLabel: 'Разрешить',name: 'cnew'}],defaultType:'checkbox'}),
		//счетчик запросов. может иметь значение first/second
		//first - первичный запрос
		//second - подтверждение
		{xtype: 'hidden', value: 'first', name: 'reqcount', itemId: 'reqcnt'}	
		];

		
		var form = new Ext.form.FormPanel({
			id:'form',
			border:false,
			autoScroll:false,
			autoHeight:true,
			style:'margin-left: 5px;',
			labelWidth:120,
			buttons:[{xtype:'button',text:'Применить',handler:function(){
						//first request
						if(form.getForm().isValid())
						{
							form.getForm().submit({
								url:'controllers/manyRights',
								waitMsg:'Подождите',
								success:function(form, action){
									if(action.result.hasintersect){
										ensurewin.getComponent('ensurelabel').setText(action.result.description);
										ensurewin.show();}
									else{
										//ensurewin.destroy();
										createwin.destroy();
										grid.getStore().reload();
									};},										
								failure:function(form,action){
									Ext.Msg.alert('Ошибка',action.result.description);
										createwin.destroy();}})			
						}}},
					{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()/*; ensurewin.hide()*/}}],
			items:items
			});
		var ensureform = new Ext.form.FormPanel({
			id:'ensureform',
			border:false,
			autoScroll:false,
			autoHeight:true,
			style:'margin-left: 5px;',
			labelWidth:120,
			buttons:[{xtype:'button',text:'Обновить',handler:function()
						{	
						//second request
						form.getComponent('reqcnt').setValue('second'); 			
						if(form.getForm().isValid()){
						form.getForm().submit({url:'controllers/manyRights',
						waitMsg:'Подождите',success:function(){
							ensurewin.destroy();
							createwin.destroy();
							grid.getStore().reload()},failure:function(form,action){
								Ext.Msg.alert('Ошибка',action.result.description);
								ensurewin.destroy(); createwin.destroy();}
						})}}},
					{xtype:'button',text:'Не обновлять',handler:function(){ensurewin.hide()}}],
			items:[{xtype: 'hidden', name: 'sdfs'}]
			});
		var ensurewin = new Ext.Window({closable:false,
			width:width+165,
			autoScroll:true,
			resizable :false,
			visible: false,
			title:'Сообщение',
			items:[new Ext.form.Label({text: 'default text', itemId: 'ensurelabel'}),ensureform],
			modal:true
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