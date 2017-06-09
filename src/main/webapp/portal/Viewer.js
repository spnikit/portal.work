
Ext.define('Aisa.static.Viewer', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.aisaviewer',
	
	getTypesURL: 'app/viewer/getcreatetypes',
	getDocumentURL: 'app/viewer/getdocument',
	getTemplateURL: 'app/viewer/gettemplate',
	closeWindowURL: 'app/viewer/canceldocument',
	workerId: null,
	predId: null,
	persId: null,
	isSysWrkr: 0,
	customPref: 'app/viewer/',
	dateAfter: '2000-01-01',
	dateBefore: Ext.Date.format(Ext.Date.add(new Date(), Ext.Date.DAY, 1), 'Y-m-d'),
	appName: null,
	
	funcSave: function(){},
	funcSaveExit: function(){},
	funcCancel: function(){},
	funcAddingSave: function(){},
	
	layout: 'fit',
	initComponent : function() {
		var pn = this;
		if (pn.appName!=null && pn.appName!='') pn.appName = '/'+pn.appName+'/';
		
		
		var scrollMenu = Ext.create('Ext.menu.Menu');
		var store = new Ext.data.JsonStore({
		    proxy: {
		        type: 'ajax',
		        url: (pn.appName==null?'':pn.appName) + pn.getTypesURL,
		        extraParams: {
		    		workerId: pn.workerId
		        },
		        reader: 'json'
		    },
		    fields: ['id', 'name'],
		    listeners: {
		    	load: function(store,recs,success,opts){
		    		var len = recs.length;
		    		if (len>0){
		    			for (var i=0;i<len;i++){
		    				var rec = recs[i].getData();
		    				scrollMenu.add({
					            text: rec.name,
					            formId: rec.id,
					            handler: function(bt){
									pn.ajaxForm(bt.formId, pn.getTemplateURL);
								}
					        });
		    			}
		    		}
		    	}
		    }
		});
		
		Ext.apply(this, {
 			items: [new Ext.TabPanel({
				border: false,
				layout: 'fit',
				activeTab: 0,
				items:[]
			})],
			dockedItems: [
			              {
		        xtype: 'toolbar',
		        dock: 'top',
		        items: [{
		            text: 'Создать',
		            menu: scrollMenu
		        },'-',{
		            text: 'Открыть',
		            handler: function(){
		            	pn.loadClick(this);
		            }
		        }]
		    }
			              ]
		, 
		    listeners: {
		    	beforerender: function(){
		    		if(pn.workerId==null || pn.predId==null || pn.persId==null){
		    			pn.close();
		    			return false;
		    		} else {
		    			store.load();
		    		}
		    	}
		    }
		});
		this.callParent(arguments);
	},
	addTable: function(title,url){
		if (url!=null){
			var pn = this;
			var grid = Ext.create('Aisa.static.ViewerGrid',{
		    	title: title,
		    	url: (pn.appName==null?'':pn.appName) + url,
		    	workerId: pn.workerId,
				predId: pn.predId,
				persId: pn.persId,
				isSysWrkr: pn.isSysWrkr,
				dateAfter: pn.dateAfter,
				dateBefore: pn.dateBefore
		    });
		    this.getComponent(0).add(grid);
		    var at = this.getComponent(0).getActiveTab();
		    if (at){
		    	this.getComponent(0).setActiveTab(at);
		    } else {
		    	this.getComponent(0).setActiveTab(0);
		    }
		}
	},
	setDateAfter: function(date){
		if (date!=null&&date!=''){
			this.dateAfter = date;
			var len = this.getComponent(0).items.getCount();
			for (var i=0;i<len;i++){
				var grid = this.getComponent(0).items.getAt(i);
				grid.dateAfter = date;
				var eparams = grid.getStore().getProxy().extraParams;
				eparams.dateAfter = date;
				if (grid.isRender==1) grid.isChanges = 1;
			}
			var gridCur = this.getComponent(0).getActiveTab();
			gridCur.isChanges = 0;
			gridCur.getStore().load();
		}
	},
	setDateBefore: function(date){
		if (date!=null&&date!=''){
			this.dateBefore = date;
			var len = this.getComponent(0).items.getCount();
			for (var i=0;i<len;i++){
				var grid = this.getComponent(0).items.getAt(i);
				grid.dateBefore = date;
				var eparams = grid.getStore().getProxy().extraParams;
				eparams.dateBefore = date;
				if (grid.isRender==1) grid.isChanges = 1;
			}
			var gridCur = this.getComponent(0).getActiveTab();
			gridCur.isChanges = 0;
			gridCur.getStore().load();
		}
	},
	loadClick: function(bt){
		var at = this.getComponent(0).getActiveTab();
		if (at.getSelectionModel().getCount()>0){
			var rec = at.getSelectionModel().getSelection()[0];
			this.ajaxForm(rec.getData().id);
		}
	},
	ajaxForm: function(formId, tUrl){
		var view = this;
		var url = view.getDocumentURL;
		if (tUrl) url = tUrl;
		Ext.Ajax.request({
			url: (view.appName==null?'':view.appName) + url,
			params: {
				id: formId,
				workerId: view.workerId,
				persId: view.persId
			},
			callback:function(options, success, response){
				var resp = Ext.JSON.decode(response.responseText);
				if (resp.status=='success'){
					view.createViewer(resp.data, resp.formid);
				} else if (resp.status=='declined'){
					Ext.Msg.show({
					     title:'Продолжить просмотр?',
					     msg: 'Форма была отклонена. Продолжить просмотр?',
					     closable: false,
					     buttons: Ext.Msg.OKCANCEL,
					     icon: Ext.Msg.QUESTION,
					     fn: function(buttonId){
					     	if (buttonId=='ok'){
					     		view.createViewer(resp.data, resp.formid);
					     	}
					     }
					});
				} else if (resp.status=='busy'){
					Ext.Msg.show({
					     title: 'Форма заблокирована',
					     msg: 'Форма уже редактируется пользоваталем '+ resp.data,
					     buttons: Ext.Msg.OK,
					     icon: Ext.Msg.INFO
					});
				}
			}
		});
	},
	createViewer: function(formstring, formid){
		var view = this;
		var viewer = Ext.create('Aisa.static.ViewerFrame',{
			form: formstring,
			formid: formid,
			viewer: view
		});
		var localWindow = Ext.create('Ext.window.Window',{
			layout: 'fit',
			modal:true,
		 	width: 800,
//			/maximizable: true,
			closable : false,
			maximized:true,
			height: window.innerHeight,
			y: 0,
			resizable: true,
			shadow: false,
			items: [viewer],
			listeners: {
				show: function(win){
					viewer.setHeight(win.getHeight());
					viewer.setWidth(win.getWidth())
				}
			},
			ajaxClose: function(doexit){
				if (doexit==1){
					Ext.Ajax.request({
						url: (view.appName==null?'':view.appName) + view.closeWindowURL,
						params: {
							formid: formid,
							persId: view.persId
						},
						callback:function(options, success, response){
							localWindow.close();
						}
					});
				} else {
					localWindow.close();
				}
			}
		}).show();
	}
});