Ext.namespace('PackCSS');

PackCSS.load = null;

PackCSS.store = new Ext.data.JsonStore({
	root : 'pack',
	totalProperty : 'totalCount',
	successProperty : 'success',
	proxy : PackCSS.proxy,
	remoteSort : true,
	fields : [

	{name : 'name'}, 
	{name : 'content'}, 
	{name : 'dognum'},
	{name : 'idpak'}, 
	{name : 'creator'}, {name : 'createDate',type : 'date',dateFormat : 'Y-m-d H:i:s'}, 
	{name : 'lastSigner'}, 
	{name : 'lastDate',type : 'date',dateFormat : 'Y-m-d H:i:s'}, 
	{name : 'reqdate'},
	{name : 'vagnum'},
	{name : 'id',type : 'int'},
	{name : 'signed'}, 
	{name : 'count',type : 'int'}, 
	{name : 'status',typed : 'int'}, 
	{name : 'visible',typed : 'int'}
	]
});

PackCSS.req = null;
PackCSS.grid = null;
PackCSS.sm = new Ext.grid.RowSelectionModel({
	singleSelect : true
});
PackCSS.load = function(req) {
	ETD.busy();
	ret = document.applet_adapter.getcssDocs(req);
	ETD.ready();

	if (ret.error) {
		Ext.Msg.show({
			title : 'Ошибка',
			msg : ret.error,
			buttons : Ext.Msg.OK,
			fn : Refresh(),
			icon : Ext.MessageBox.ERROR
		});
	};

	PackCSS.grid.isLoaded = true;
	var response = Ext.util.JSON.decode(ret);
	if (response.pack) {
		PackCSS.store.loadData(response);
	} else {
		viewpackwin.destroy();
		Ext.MessageBox.alert('Сообщение', 'В пакете нет ни одного документа');
	}
	return null;
};

PackCSS.mkpackwin = function(req) {

	PackCSS.packcreate(req);

	viewpackwin = new Ext.Window(
			{
				plain : 'true',
				title : 'Документы в пакете',
				width : 600,
				height : 400,
				layout : 'fit',
				resizable : true,
				modal : true,
				border : true,
				buttonAlign : 'center',
				closable : true,
				style : 'text-align: center;',
				listeners : {
					close : function() {
						ETD.dropPackid();
						
					}

				},

				defaults : {
					labelStyle : 'width:120px;'
				},

				items : [ PackCSS.grid ],
				buttons : [
						{
							text : 'Подписать пакет',
							id : 'signpackbut',
							disabled : true,
							handler : PackCSS.signpack
						},
						{
							text : 'Отклонить пакет',
							id : 'packdropoack',
							handler : function() {
							
								
								Ext.Ajax
										.request({
											url : 'forms/droppack',
											params : {
												docid : ETD.packCSSid
											},
											callback : function(options,
													success, response) {
												var resp = response.responseText;
												var arr = Ext.util.JSON
														.decode(resp);

												if (arr == 'true') {
													viewpackwin.hide();
													dropdoc(false);
												} else {
													Ext.MessageBox
															.alert('Сообщение',
																	'В пакете есть непроверенные документы');
												}

											}
										});
						}
						},
						{
							text : 'Открыть документ',
							handler : function() {

								var grid = Ext.getCmp('packgridcss');
								if (grid.getSelectionModel().hasSelection()) {
									var row = grid.getSelectionModel()
											.getSelected();

									PackCSS.openDoc(row);
								}
							}
						},
						{
							text : 'Закрыть',
							handler : function() {
								ETD.dropPackCSSid();
								viewpackwin.close();
							}
						} ]
			});
	viewpackwin.show();
	
	if (ETD.packview){
		Ext.getCmp('packdropoack').disable();
		Ext.getCmp('signpackbut').disable();
	}
	
	
	else if (Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_DOCUMENT_GRID') {
		 if (ETD.fr==6){
			Ext.getCmp('packdropoack').disable();
			Ext.getCmp('signpackbut').disable();
		}
		
		 else Ext.getCmp('signpackbut').enable();

	} else if (Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_SEND_GRID') {

		
		if (ETD.fr == 2) {
			Ext.getCmp('packdropoack').disable();

		} else if (ETD.fr == 1 || ETD.fr == 17) {
			Ext.getCmp('packdropoack').disable();

		}

		else if (ETD.fr==6){
			Ext.getCmp('packdropoack').disable();
			Ext.getCmp('signpackbut').disable();
		}
		
		
	} else if (Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_FIN_GRID'
			|| Ext.getCmp('tpanel').getActiveTab().getId() == 'ID_ARCHIVE_GRID') {

		Ext.getCmp('packdropoack').disable();

	}

}

PackCSS.signpack = function(){
	
Ext.Ajax.request({
						url : 'forms/signPackCSS',
				params : {
					docid : ETD.packCSSid
				},
				callback : function(options, success, response) {
					var resp = response.responseText;
					var arr = Ext.util.JSON.decode(resp);

					if (arr == 'true') {
						ETD.busy();
						var succ = document.applet_adapter
								.fakesignpack(ETD.packCSSid);
						ETD.ready();
						if (succ == false) {
							Ext.MessageBox
									.alert('Ошибка',
											'Ошибка при согласовании пакета документов');
						}

						else if (succ == true) {
							viewpackwin.close();
							ETD.dropPackCSSid();
							Ext.getCmp('tpanel').getActiveTab().loadFirstPage();
						}

					} else if (arr == '0') {
						Ext.MessageBox
								.alert('Сообщение',
										'Нельзя согласовать пакет с забракованными документами');
					}

					else {
						Ext.MessageBox.alert('Сообщение',
								'В пакете есть несогласованные документы');
					}

				}
			});

}

PackCSS.packcreate = function(req) {
	PackCSS.grid = new Ext.grid.GridPanel({
		id : 'packgridcss',
		store : PackCSS.store,
		isLoaded : false,
		cm : new Ext.grid.ColumnModel([ {
			id : 'name',
			header : ETD.headers[10],

			sortable : false,
			dataIndex : 'name',
			tdCls : 'test'
		},
		{
			header : ETD.headers[12],
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'content'

		},

		{
			header : 'Статус',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'signed'

		} ]),

		sm : PackCSS.sm,

		viewConfig : {
			forceFit : true
		},
		frame : true,
		autoScroll : true,
		stripeRows : true,
		viewConfig : {
			forceFit : true
		},
		listeners : {
			render : function(panel) {
				PackCSS.load(req);
			},

			dblclick : {
				fn : function(e) {
					if (Ext.getCmp('packgridcss').getSelectionModel()
							.hasSelection()) {
						var row = Ext.getCmp('packgridcss').getSelectionModel()
								.getSelected();
						PackCSS.openDoc(row);
					}
				}
			}

		}
	});
}

var droppackdoc = function() {
	var grid = Ext.getCmp('packgridcss');

	var select = grid.getSelectionModel();

	var getsel = select.getSelected();

	if (select.hasSelection()) {

		var selectname = getsel.get('name');

		if (ETD.packdocs.indexOf(selectname) != -1) {

			if (getsel.get('signed') === 'Подписан') {
				Ext.MessageBox.alert('Сообщение',
						'Нельзя забраковать подписанный документ');
			}

			else if (getsel.get('signed') === 'Забракован') {
				Ext.MessageBox.alert('Сообщение', 'Документ уже забракован');
			}

			else {
				var tpanel = Ext.getCmp('tpanel');
				var docid = getsel.get('id');
				var status = getsel.get('status');
				var mess = '';
				if (status==1||status==2||status==3) {
					mess = document.applet_adapter.getDocStatus(docid).split(
							'&&')[1];

				}

				else {
					mess = 'Документ забракован собственником';
				}
				viewpackwin.hide();
				ETD.busy();
				(function() {

					var resp = eval('('
							+ document.applet_adapter.dropdoc(mess, 0, docid)
							+ ')');
					if (resp.error)
						Ext.MessageBox.alert('Ошибка!', resp.error);

					tpanel.getActiveTab().getStore().reload();
					ETD.ready();
					PackCSS.mkpackwin(ETD.packCSSid);

				}).defer(1);

			}

		} else {
			Ext.MessageBox.alert('Сообщение',
					'Нельзя забраковать данный документ');
		}

	}
};

var droppackfn = function(btn, text, docid) {
	var grid = Ext.getCmp('packgridcss');
	var tpanel = Ext.getCmp('tpanel');
	var docid = grid.getSelectionModel().getSelected().get('id');

	if (btn == 'ok') {
		if (text.length > 0) {

			if (text.length > 500) {
				Ext.MessageBox.alert('Ошибка',
						'Причина не должна привышать 500 символов.');
			} else {

				tab = 0;

				viewpackwin.hide();
				ETD.busy();
				(function() {

					var resp = eval('('
							+ document.applet_adapter.dropdoc(text, tab, docid)
							+ ')');
					if (resp.error)
						Ext.MessageBox.alert('Ошибка!', resp.error);

					tpanel.getActiveTab().getStore().reload();
					ETD.ready();
					PackCSS.mkpackwin(ETD.packCSSid);

				}).defer(1);
			}
		} else {
			Ext.MessageBox
					.alert('Ошибка',
							'Документ не забракован. Необходимо ввести причину браковки.');
		}

	}
};

PackCSS.reload = function() {
	PackCSS.load(req);
};

PackCSS.openDoc = function(row) {
	/*if (row.get('name') == 'ФПУ-26 АСР'
		&& ETD.tabname =='ID_SEND_GRID'){
		Ext.Ajax.request({
			url : 'forms/fpuacc',
			params : {
				docid : row.get('id'),
				packid: ETD.packCSSid
			},
			callback : function(options, success,
					response) {
				var resp = response.responseText;
				var arr = Ext.util.JSON
				.decode(resp);

				if (arr == 'true') {
					ETD.isSend = 0;
					openinpack(row.get('id'), row
							.get('name'));
				} else if (arr == 'false') {
					ETD.isSend = 1;
					openinpack(row.get('id'), row
							.get('name'));
				}

			}
		});
	} else {*/
	    ETD.isSend = 0;
		openinpack(row.get('id'), row.get('name'));
	//}
}

var openinpack = function(id, name) {
	var send;
	ETD.openDocument(id, name, null, 0);
	viewpackwin.hide();
	viewpackwin.destroy();
};

var replaceQuot = new RegExp("\n", "g");