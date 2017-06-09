ru.aisa.SearchSNT = Ext.extend(Ext.Button,{
	
	constructor: function(config,grid){
	conf = {xtype:'button',text:'Поиск'}
	ru.aisa.SearchOCO.superclass.constructor.apply(this,[conf]);
	this.on('click',function(button){
		var search5 = new Ext.form.TextField({fieldLabel:'Номер',name:'patter5',width:200,value:grid.getStore().baseParams.search5,allowBlank:true});
		var search6 = new Ext.form.TextField({fieldLabel:'Предприятие',name:'pattern6',width:200,value:grid.getStore().baseParams.search6,allowBlank:true});
		var search7 = new Ext.form.TextField({fieldLabel:'Родительский номер',name:'pattern7',width:200,value:grid.getStore().baseParams.search7,allowBlank:true});
		
	var items = [  {
		xtype : 'hidden',
		value:'new',name:'act'
	},
	search5,search6,search7];
		
	var form1 = new Ext.form.FormPanel({
		id:'form1',
		border:false,
		autoScroll:false,
		autoHeight:true,
		style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
		labelWidth:120,
		buttons:[{xtype:'button',text:'Применить',handler:function(){
			createwin.destroy();
		if(search5.getValue().length<1)
			grid.getStore().baseParams.search5=null;
		else{ 
			grid.getStore().baseParams.search5=search5.getValue();
			grid.getStore().baseParams.searchField = button.field;
		}
		grid.getStore().load();
		if(search6.getValue().length<1)
			grid.getStore().baseParams.search6=null;
		else{ 
			grid.getStore().baseParams.search6=search6.getValue().toUpperCase();
			grid.getStore().baseParams.searchField = button.field;		
		}
		grid.getStore().load();
		if(search7.getValue().length<1)
			grid.getStore().baseParams.search7=null;
		else{ 
			grid.getStore().baseParams.search7=search7.getValue();
			grid.getStore().baseParams.searchField = button.field;
		}
		grid.getStore().load();
		
		}},
			{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}},
			{xtype:'button',text:'Очистить',handler:function(){
				createwin.destroy();
				grid.getStore().baseParams.search5=null;
				grid.getStore().baseParams.search6=null;
				grid.getStore().baseParams.search7=null;
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
