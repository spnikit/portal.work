/**
 * 
 */

var  idTmr  =  ""; 
var width = ru.aisa.width;
ru.aisa.deleteAllServices = Ext.extend(Ext.Button,{

	constructor: function(config,grid){

		conf = {xtype:'button',text:'Удалить все записи контрагента'};
		ru.aisa.deleteAllServices.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){

			//var saveButton =new Ext.form.FileUploadField({fieldLabel:'Файл',width:220,buttonCfg: {text: 'Выбрать'},name:'formfile',id:'formfile',allowBlank:false});
			var st = new Ext.data.SimpleStore({url:'controllers/PriceCheckPredid',fields:['id','text'],autoLoad:false});
			//	var st = new Ext.data.SimpleStore({url:'controllers/predid',fields:['id','text'],autoLoad:false});
			var predid = new Ext.form.ComboBox({fieldLabel:'Контрагенты',editable:true,forceSelection:true,triggerAction:'all',mode:'local',store:st,displayField:'text',valueField:'id',width:width,hiddenName:'rec',allowBlank:true, id:'pred', name:'pred'});
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
			st.load({callback:function(){


				var items = [  {
					xtype : 'hidden',
					value:'delete',name:'act'
				},predid];

				var form1 = new Ext.form.FormPanel({
					id:'form1',
					fileUpload:true,
					border:false,
					autoScroll:false,
					autoHeight:true,
					style:'margin-left: 5px;margin-top:5px;margin-right: 5px;',
					labelWidth:120,
					buttons:[{xtype:'button',text:'Применить',handler:function(){
						/*createwin.destroy();
					if(predid.getValue().length<1)
						grid.getStore().baseParams.predid=null;
					else{ 
						grid.getStore().baseParams.predid=predid.getValue();
						grid.getStore().baseParams.searchField = button.field;
					}

					grid.getStore().load();*/
						//var form = this.up('formfile').getForm();
						/* if(form1.getForm().isValid()){
		                form1.getForm().submit({
		                    url: 'controllers/DeleteAllServices',
		                    //params:{ 'act': 'delete', 'predid': predid.getValue()},
		                   // waitMsg: 'Delete your data...',
		                    success: function(fp, o) {
		                    	createwin.destroy();
		                        //Ext.Msg.alert('Success', 'Your file has been uploaded.');
		                    },failure:function(form,action){
		        				//Ext.Msg.alert('Ошибка',action.result.description);
		        				createwin.destroy();}
		                });*/
						Ext.Ajax.request({
							url: 'controllers/directory_service', // you can fix a parameter like this : url?action=anAction1
							params: {
								act: 'delete',
								predid: Ext.getCmp('pred').getValue()
								// all your params.... 
							},
							success: function (result, request){
								var result = result.responseText;
								var isDelete =  Ext.util.JSON.decode(result);
								if(isDelete.success===true){
									createwin.destroy();
								}else{
									alert('Для данного контрагента отсутствуют заведенные услуги');
								}

							}
							/*failure: function (result, request){
				            alert('Для данного контрагента отсутствуют заведенные услуги');
				        }*/
						});

						//saveButton.getValue()

					}},
					{xtype:'button',text:'Отмена',handler:function(){createwin.destroy()}},
					{xtype:'button',text:'Очистить',handler:function(){
						predid.setValue('');
						grid.getStore().baseParams.predid=null;
						grid.getStore().baseParams.searchField=null;
						grid.getStore().load();
					}}],
					items:items
				});	
				var createwin = new Ext.Window({closable:false,
					width:ru.aisa.width+150,
					autoScroll:true,
					resizable :false,
					title:'Введите параметры поиска',
					items:[form1],
					modal:true
				});
				createwin.show();
			}
			});
		});	
	} 
});