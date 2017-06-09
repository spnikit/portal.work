

ru.aisa.advancedSearch = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		conf = {xtype:'button',text:'Поиск'}
		ru.aisa.advancedSearch.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
	
			var cm = grid.getColumnModel();
			 var t;
			 var u;
			 var result='';
			 var k=0;
			 var arr2 = [];
			
	
			 for (var i = 0 ; i < cm.getColumnCount(); i++) {
			
				 if (!cm.isHidden(i)) {
					
					 t = cm.getColumnHeader(i);
					 u = cm.getColumnId(i);
					// alert(t);
					//alert(u);

					 var arr = [];
					 arr [0] = u;
					 arr [1] = t;
					arr2.push(arr);
					 
					//alert(arr2);
					 /*if(k==0){
					 result = result +"["+(i+1)+",'"+t+"']";
					 }else{
						 result = result +",["+(i+1)+",'"+t+"']";
					 }*/
					// k++;
				 }
				 
			 }
			
			// alert(arr);
			
			var width = ru.aisa.width;
			var st1 = new Ext.data.SimpleStore({/*url:'controllers/predid',*/fields:['id','text'],data:arr2/*,autoLoad:false,value:grid.getStore().baseParams.st1*/});
			var search = new Ext.form.ComboBox({fieldLabel:'Выберите столбец',triggerAction:'all',mode:'local',store:arr2,displayField:'text',valueField:'id',emptyText: 'Выберите столбец',lastQuery:'', value:grid.getStore().baseParams.search});
			var text = new Ext.form.TextField({
			  
			    fieldLabel: 'Введите значение поиска',
			    allowBlank:false,
			    
			    //hidden: flag,
			   // disabled: flag,
			    name:'text',
			    value:grid.getStore().baseParams.text
			    	
			}); 
			//st1.load({callback:function(){
/*var searchbutton=new Ext.Button({xtype:'button',text:config.search[i].text});
			searchbutton.field = config.search[i].field;
			searchbutton.reg = config.search[i].reg;
			searchbutton.on('click',function(button){
			var value;
			if(grid.getStore().baseParams.searchField == button.field)
				value = grid.getStore().baseParams.search;
			else value = '';
			var search=new Ext.form.TextField({fieldLabel:'Строка для поиска по первому полю',name:'pattern',width:ru.aisa.width,value:value,regex:button.reg});
			Ext.QuickTips.getQuickTip().register({target:search,text:'Введите критерии поиска, допустимые символы подстановки: _,%'//,title:'zxc'//,width: 100
			
					//		dismissDelay: 200
			});*/
			
				var width = ru.aisa.width;
				if(config.fullwidth =='true') width=ru.aisa.width1;
			var items = [  {
				xtype : 'hidden',
				value:'new',name:'act'
			},
			search,text];
			
			var form = new Ext.form.FormPanel({
				id:'form',
				border:false,
				autoScroll:false,
				autoHeight:true,
				style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
				labelWidth:120,
					//reader:new Ext.data.JsonReader(),
					buttons:[{xtype:'button',text:'Применить',handler:function()
					{createwin.destroy();
					if(search.getValue().length<1)
						grid.getStore().baseParams.search=null;
					
					else {
						grid.getStore().baseParams.search=search.getValue();//.toUpperCase();
						grid.getStore().baseParams.searchField = button.field;
						//grid.getStore().baseParams.text=null;
						}
				
					if(text.getValue().length<1)
						grid.getStore().baseParams.text=null;
					
					else {
						grid.getStore().baseParams.text=text.getValue();
						grid.getStore().baseParams.searchField = button.field;
						
						//alert(text.getValue().toUpperCase());
						}
						grid.getStore().load();
					}},
						{xtype:'button',text:'Отмена',handler:function(){createwin.destroy();}},
						{xtype:'button',text:'Очистить',handler:function(){
						createwin.destroy();
						grid.getStore().baseParams.search=null;
						grid.getStore().baseParams.text=null;
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
				items:[form],
				modal:true
			});
			
			createwin.show();
			//}
		//})
		
		})
}});