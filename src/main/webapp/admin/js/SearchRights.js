ru.aisa.SearchRights = Ext.extend(Ext.Button,{
	
	constructor: function(config,grid){
	conf = {xtype:'button',text:'Поиск'}
	ru.aisa.SearchRights.superclass.constructor.apply(this,[conf]);
	this.on('click',function(button){
		var search = new Ext.form.TextField({fieldLabel:'Форма',name:'pattern1',width:200,value:grid.getStore().baseParams.search,allowBlank:true});
		var search3 = new Ext.form.TextField({fieldLabel:'Должность',name:'pattern2',width:200,value:grid.getStore().baseParams.search3,allowBlank:true});
		
	var items = [  {
		xtype : 'hidden',
		value:'new',name:'act'
	},
	search,search3];
		
	var form1 = new Ext.form.FormPanel({
		id:'form1',
		border:false,
		autoScroll:false,
		autoHeight:true,
		style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
		labelWidth:120,
		buttons:[{xtype:'button',text:'Применить',handler:function(){
			createwin.destroy();
		if(search.getValue().length<1)
			grid.getStore().baseParams.search=null;
		else{ 
			grid.getStore().baseParams.search=search.getValue().toUpperCase();
			grid.getStore().baseParams.searchField = button.field;
		}
		//grid.getStore().load();
		if(search3.getValue().length<1)
			grid.getStore().baseParams.search3=null;
		else{ 
			grid.getStore().baseParams.search3=search3.getValue().toUpperCase();
			grid.getStore().baseParams.searchField = button.field;		
		}
		grid.getStore().load();
		}},
			{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}},
			{xtype:'button',text:'Очистить',handler:function(){
				createwin.destroy();
				grid.getStore().baseParams.search=null;
				grid.getStore().baseParams.search3=null;
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

	})
}});