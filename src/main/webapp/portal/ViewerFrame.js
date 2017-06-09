
Ext.define('Aisa.static.ViewerFrame', {
	extend: 'Ext.panel.Panel',
	border: false,
	winframe: null,
	form: null,
	formid: -1,
	viewer: null,
	initComponent : function() {
		var pn = this;
		
		Ext.apply(this, {
			border: false,
			html: 	'<div style="background-image: url('+"'"+Ext.Loader.getPath('Aisa.static').replace('.js','')+"/pics/2.jpg'"+')">'+
						'<iframe height="100%" width="100%" frameborder="0" name="viewernameWin" src="'+Ext.Loader.getPath('Aisa.static').replace('.js','')+'/aisaViewer.html">'+
						'</iframe>'+
						'<div style="position:absolute;text-align:center;top:50%;visibility:hidden;width:100%;">'+
							'<h2>Пожалуйста, подождите.</h2></br><h2>Документ сохраняется.</h2>'+
						'</div>'+
					'</div>',
			listeners: {
				afterrender: function(panel){
					pn.winframe = Ext.query('iframe',pn.body.dom)[0];
					if (pn.winframe.attachEvent) pn.winframe.attachEvent('onload', function(){
						try {
							pn.winframe.contentWindow.initBaseFunction(
								(pn.viewer.appName==null?window.location.pathname:pn.viewer.appName)+pn.viewer.customPref,
								(''+pn.viewer.funcSave)
									.replace('#formid',pn.formid+'')
									.replace('#persId',pn.viewer.persId+'')
									.replace('#predId',pn.viewer.predId+'')
									.replace('#workerId',pn.viewer.workerId+''),
								(''+pn.viewer.funcCancel)
									.replace('#winId',pn.up('window').getId()),
								(''+pn.viewer.funcSaveExit)
									.replace('#formid',pn.formid+'')
									.replace('#persId',pn.viewer.persId+'')
									.replace('#predId',pn.viewer.predId+'')
									.replace('#workerId',pn.viewer.workerId+'')
									.replace('#winId',pn.up('window').getId()),
								''+pn.viewer.funcAddingSave
							);
							pn.winframe.contentWindow.CreateOpenViewer(pn.form);
						} catch(e){

							pn.createError(e);
						}
					});
					else pn.winframe.addEventListener('load', function(){
						try {
							pn.winframe.contentWindow.initBaseFunction(
								(pn.viewer.appName==null?window.location.pathname:pn.viewer.appName)+pn.viewer.customPref,
								(''+pn.viewer.funcSave)
									.replace('#formid',pn.formid+'')
									.replace('#persId',pn.viewer.persId+'')
									.replace('#predId',pn.viewer.predId+'')
									.replace('#workerId',pn.viewer.workerId+''),
								(''+pn.viewer.funcCancel)
									.replace('#winId',pn.up('window').getId()),
								(''+pn.viewer.funcSaveExit)
									.replace('#formid',pn.formid+'')
									.replace('#persId',pn.viewer.persId+'')
									.replace('#predId',pn.viewer.predId+'')
									.replace('#workerId',pn.viewer.workerId+'')
									.replace('#winId',pn.up('window').getId()),
								''+pn.viewer.funcAddingSave
							);
							pn.winframe.contentWindow.CreateOpenViewer(pn.form);
						} catch(e){

							pn.createError(e);
						}
					}, false);
				}
			}
		});
		this.callParent(arguments);
	},
	getWFrame : function(){
		return this.winframe;
	},
	setHeight : function(height){
		this.getWFrame().style.height = (height-32) + 'px';
	},
	getStringForm: function(){
		return this.getWFrame().contentWindow.SerializeXFDL();
	},
	isMaySign: function(){
		return this.getWFrame().contentWindow.maySign==1?true:false;
	},
	isMaySave: function(){
		return this.getWFrame().contentWindow.saveError==0?true:false;
	},
	createError: function(e){
//		alert('Error!!!');
		console.log(e);
	}
});