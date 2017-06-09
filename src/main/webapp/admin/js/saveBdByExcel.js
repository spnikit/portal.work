/**
 * 
 */

var  idTmr  =  ""; 
ru.aisa.saveBdByExcel = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		
		conf = {xtype:'button',text:'Загрузить из файла'}
		 ru.aisa.saveBdByExcel.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			
			var saveButton =new Ext.form.FileUploadField({fieldLabel:'Файл',width:220,buttonCfg: {text: 'Выбрать'},name:'formfile',id:'formfile',allowBlank:false});
			
			/*{name:'formfile',type:'file',header:'Файл'};{
			        
			        xtype: 'filefield',
			        name: 'file',
			        fieldLabel: 'Файл',
			        labelWidth: 50,
			        msgTarget: 'side',
			        allowBlank: false,
			        anchor: '100%',
			        buttonText: 'Select File...'
			    };*/
			var items = [  {
				xtype : 'hidden',
				value:'new',name:'act'
			},
			saveButton];
			
			var form1 = new Ext.form.FormPanel({
				id:'form1',
				fileUpload:true,
				border:false,
				autoScroll:false,
				autoHeight:true,
				style:'margin-left: 5px;margin-top:5px;margin-right: 5px;',
				labelWidth:120,
				buttons:[{xtype:'button',text:'Применить',handler:function(){
					
					//var form = this.up('formfile').getForm();
		            if(form1.getForm().isValid()){
		                form1.getForm().submit({
		                    url: 'controllers/SaveInBdByExcel',
		                    waitMsg: 'Uploading your file...',
		                    success: function(fp, o) {
		                    	createwin.destroy();
		                        //Ext.Msg.alert('Success', 'Your file has been uploaded.');
		                    },failure:function(form,action){
		        				//Ext.Msg.alert('Ошибка',action.result.description);
		        				createwin.destroy();}
		                });
		            }
		            
					//saveButton.getValue()
					
				}},
					{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}},
					{xtype:'button',text:'Очистить',handler:function(){
						createwin.destroy();
						grid.getStore().baseParams.saveButton=null;
						grid.getStore().load();
						}}],
				items:items
				});	
			var createwin = new Ext.Window({closable:false,
				width:ru.aisa.width+180,
				autoScroll:true,
				resizable :false,
				title:'Введите параметры поиска',
				items:[form1],
				modal:true
			});
			createwin.show();
		  
		})	
	}

    
});