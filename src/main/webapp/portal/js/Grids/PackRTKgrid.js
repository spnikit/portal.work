Ext.namespace('PackRTK');

PackRTK.load = null;

PackRTK.store = new Ext.data.JsonStore({
	root : 'pack',
	totalProperty : 'totalCount',
	successProperty : 'success',
	proxy : PackRTK.proxy,
	remoteSort : true,
	fields : [
	{name : 'id',type : 'int'},
	{name : 'name'}, 
	{name : 'content'}, 
	{name : 'signed'}, 
	{name : 'visible',type : 'int'}
	]
});

PackRTK.req = null;
PackRTK.grid = null;
PackRTK.sm = new Ext.grid.RowSelectionModel({
	singleSelect : true
});
PackRTK.load = function(req) {
	ETD.busy();
//	alert(req);
	ret = document.applet_adapter.getrtkDocs(req);
	ETD.ready();

	if (ret.error) {
		Ext.Msg.show({
			title : 'Ошибка',
			msg : ret.error,
			buttons : Ext.Msg.OK,
			fn : Refresh(),
			icon : Ext.MessageBox.ERROR
		});
	}
	;

	// TODO Сделать проверку удачной загрузки
	PackRTK.grid.isLoaded = true;
	var response = Ext.util.JSON.decode(ret);
	
	if (response.pack) {
		PackRTK.store.loadData(response);

	} else {
		viewPackRTKwin.destroy();
		Ext.MessageBox.alert('Сообщение', 'В пакете нет ни одного документа');
	}
	return null;
};

PackRTK.mkPackRTKwin = function(req) {
//	var filename = getfilename(ETD.packRTKid);
	
	
	PackRTK.PackRTKcreate(req);
var textbtn;

if (Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_DOCUMENT_GRID'){
	textbtn='Добавить файл';
} else textbtn='Открыть пакет';

	viewPackRTKwin = new Ext.Window(
			{
				plain : 'true',
				title : 'Документы в пакете',
				width : 600,
				height : 400,
				layout : 'fit',
				resizable : true,
				modal : true,
				border : true,
				header: true,
				buttonAlign : 'center',
				closable : true,
				style : 'text-align: center;',
				listeners : {
					close : function() {
						ETD.dropPackRTKid();
					}
					

				},
				bbar:  [{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable"></div>',style:'font-size: 12px;'}],
				defaults : {
					labelStyle : 'width:120px;'
				},

				items : [ PackRTK.grid ],
				buttons : [
						{
							text : 'Подписать пакет',
							id : 'signPackRTKbut',
							handler : PackRTK.signPackRTK
						},
						{
							text : 'Отклонить пакет',
							id : 'PackRTKdropoack',
							handler : function() {
							
								viewPackRTKwin.hide();
								dropdoc(false);
								
						}
						},
						{
							text : textbtn,
							id : 'PackRTKadd',
							handler : function() {
								if (Ext.getCmp('tpanel').getActiveTab().getId() != 'ID_FIN_GRID'){
								ETD.openDocument(ETD.packRTKid, 'Пакет документов РТК', null, 0);
								viewPackRTKwin.hide();
								viewPackRTKwin.destroy();
								} else {
									
//									alert(ETD.packRTKid);
									document.applet_adapter.openDroppedDocument(ETD.packRTKid, 'Пакет документов РТК',1, ETD.tabname);
									viewPackRTKwin.hide();
									viewPackRTKwin.destroy();
									CreateOpenDoc();
								}
						}
						},

						{
							text : 'Открыть документ',
							handler : function() {

								var grid = Ext.getCmp('PackRTKgrid');
								if (grid.getSelectionModel().hasSelection()) {
									var row = grid.getSelectionModel()
											.getSelected();

									PackRTK.openDoc(row);
								}
							}
						},

						
						{
							text : 'Закрыть',
							handler : function() {
								ETD.dropPackRTKid();
								viewPackRTKwin.close();
								
							}
						} ]
			});
	viewPackRTKwin.show();
	
	if (Ext.getCmp('tpanel').getActiveTab().getId() != 'ID_DOCUMENT_GRID') {
		Ext.getCmp('signPackRTKbut').disable();
		Ext.getCmp('PackRTKdropoack').disable();
//		Ext.getCmp('PackRTKadd').disable();
		
	}
	
	
}

PackRTK.signPackRTK = function(){
	
Ext.Ajax.request({
						url : 'forms/signPackRTK',
				params : {
					docid : ETD.packRTKid
				},
				callback : function(options, success, response) {
					var resp = response.responseText;
					var arr = Ext.util.JSON.decode(resp);

					if (arr == 'true') {

						ETD.busy();
						var succ = document.applet_adapter
								.fakesignpack(ETD.packRTKid);
						
						ETD.ready();
						if (succ == false) {
							Ext.MessageBox
									.alert('Ошибка',
											'Ошибка при подписании пакета документов');
						}

						else if (succ == true) {

							viewPackRTKwin.close();
							ETD.dropPackRTKid();
							Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
						}

					}
					else {
						Ext.MessageBox.alert('Сообщение',
								'В пакете есть неподписанные документы');
					}

				}
			});

}

PackRTK.PackRTKcreate = function(req) {
	PackRTK.grid = new Ext.grid.GridPanel({
		id : 'PackRTKgrid',
		store : PackRTK.store,
		isLoaded : false,
		cm : new Ext.grid.ColumnModel([ {
			id : 'name',
			header : ETD.headers[10],
			sortable : false,
			dataIndex : 'name',
			tdCls : 'test'
		},{
			header : ETD.headers[12],
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'content'
		}, {
			header : 'Статус',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'signed'
		} ]),

		sm : PackRTK.sm,

		viewConfig : {
			forceFit : true
		},
		frame : true,
		autoScroll : true,
		stripeRows : true,
		
		listeners : {
			render : function(panel) {
				PackRTK.load(req);
			},

			dblclick : {
				fn : function(e) {

					if (Ext.getCmp('PackRTKgrid').getSelectionModel()
							.hasSelection()) {
						var row = Ext.getCmp('PackRTKgrid').getSelectionModel()
								.getSelected();
						
						PackRTK.openDoc(row);
					}
				}
			}

		}
	});
}



PackRTK.reload = function() {
	PackRTK.load(req);
};

PackRTK.openDoc = function(row){

	openinPackRTK(row.get('id'), row.get('name'));

}

var getfilename = function(id){
	var file;
	Ext.Ajax.request({
		url : 'forms/getFileNameRTK',
	params : {id : id},
	method: 'POST',

	
	success: function(response){
			var result = response.responseText;
			if(result){
			var resp=Ext.util.JSON.decode(result);
			file=resp[0].filename;
//			alert(file);
			}
			
	}
	

});
//	alert(file);
	return file;
};


var openinPackRTK = function(id, name) {
	ETD.openDocument(id, name, null, 0);
	viewPackRTKwin.hide();
	viewPackRTKwin.destroy();
};

var replaceQuot = new RegExp("\n", "g");