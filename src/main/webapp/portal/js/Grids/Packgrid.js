Ext.namespace('Pack');

Pack.load = null;

Pack.store = new Ext.data.JsonStore({
	root : 'pack',
	totalProperty : 'totalCount',
	successProperty : 'success',
	proxy : Pack.proxy,
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

Pack.req = null;
Pack.grid = null;
Pack.sm = new Ext.grid.RowSelectionModel({
	singleSelect : true
});
Pack.load = function(req) {
	ETD.busy();
	ret = document.applet_adapter.getpackDocs(req);
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
	Pack.grid.isLoaded = true;
	var response = Ext.util.JSON.decode(ret);
	if (response.pack) {
		Pack.store.loadData(response);

	} else {
		viewpackwin.destroy();
		Ext.MessageBox.alert('Сообщение', 'В пакете нет ни одного документа');
	}
	return null;
};

Pack.mkpackwin = function(req) {

	Pack.packcreate(req);

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

				items : [ Pack.grid ],
				buttons : [
						{
							text : 'Подписать пакет',
							id : 'signpackbut',
							disabled : true,
							handler : Pack.signpack
						},
						{
							text : 'Отклонить пакет',
							id : 'packdropoack',
							handler : function() {
							
								
								Ext.Ajax
										.request({
											url : 'forms/droppack',
											params : {
												docid : ETD.packid
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
							text : 'История ремонта',
							icon: 'imgs/icons/repair_history.png',
							cls: 'x-btn-text-icon',
							id : 'history',
							handler : openHistory
						},
						{
							text : 'Открыть документ',
							handler : function() {

								var grid = Ext.getCmp('packgrid');
								if (grid.getSelectionModel().hasSelection()) {
									var row = grid.getSelectionModel()
											.getSelected();

									Pack.openDoc(row);
								}
							}
						},

						
						{
							text : 'Закрыть',
							handler : function() {
								ETD.dropPackid();
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

		} else if (ETD.fr == 1) {
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

	// viewpackwin.focus();
}

Pack.signpack = function(){
	
Ext.Ajax.request({
						url : 'forms/signpack',
				params : {
					docid : ETD.packid
				},
				callback : function(options, success, response) {
					var resp = response.responseText;
					var arr = Ext.util.JSON.decode(resp);

					if (arr == 'true') {

						ETD.busy();
						var succ = document.applet_adapter
								.fakesignpack(ETD.packid);
						ETD.ready();
						if (succ == false) {
							Ext.MessageBox
									.alert('Ошибка',
											'Ошибка при согласовании пакета документов');
						}

						else if (succ == true) {

							viewpackwin.close();
							ETD.dropPackid();
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

Pack.packcreate = function(req) {
	Pack.grid = new Ext.grid.GridPanel({
		id : 'packgrid',
		store : Pack.store,
		isLoaded : false,
		cm : new Ext.grid.ColumnModel([ {
			id : 'name',
			header : ETD.headers[10],

			sortable : false,
			dataIndex : 'name',
			tdCls : 'test'
		},

		{
			header : 'Номер вагона',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'vagnum'
		}, {

			header : 'Дата ремонта',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'reqdate'
		}, {
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

		sm : Pack.sm,

		viewConfig : {
			forceFit : true
		},
		frame : true,
		autoScroll : true,
		stripeRows : true,
		viewConfig : {
			forceFit : true,

			// ПАААААААААДСВЕТАЧКА
			getRowClass : function(record, index) {

				var c = record.get('status');
				// alert(c);
				if (c == 1)
					return 'acc-row';
				else if (c == 2)
					return 'dropped-row';
				else if (c == 3)
					return 'warn-row';
			}

		},
		listeners : {
			render : function(panel) {
				Pack.load(req);
			},

			dblclick : {
				fn : function(e) {

					if (Ext.getCmp('packgrid').getSelectionModel()
							.hasSelection()) {
						var row = Ext.getCmp('packgrid').getSelectionModel()
								.getSelected();
						
						Pack.openDoc(row);
					}
				}
			},

			rowclick : {
				fn : function(grid, rowIndex, e) {
					var row = grid.getSelectionModel().getSelected();
					var status = row.get('status');
					if (status != -1) {
						var mess = document.applet_adapter.getDocStatus(row
								.get('id'));
						viewpackwin.hide();
						Ext.ux.prompt.confirm('Внимание!', 'Документ '
								+ mess.split('&&')[0] + '<br/><br/>'
								+ mess.split('&&')[1], function(button) {
							if (button == 'yes') {
								ETD.openDocument(row.get('id'),
										row.get('name'), null, 0);
							} else if (button == 'no') {
								viewpackwin.show();
							}
						});
					}
				}
			}

		}
	});
}

var droppackdoc = function() {

	var grid = Ext.getCmp('packgrid');

	var select = grid.getSelectionModel();

	var getsel = select.getSelected();

	if (select.hasSelection()) {

		var selectname = getsel.get('name');

		if (ETD.packdocs.indexOf(selectname) != -1) {

			if (getsel.get('signed') === 'Согласован'
					&& getsel.get('name') === 'РДВ'
					|| getsel.get('signed') === 'Подписан') {
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
					Pack.mkpackwin(ETD.packid);

				}).defer(1);

			}

		} else {
			Ext.MessageBox.alert('Сообщение',
					'Нельзя забраковать данный документ');
		}

	}
};

var droppackfn = function(btn, text, docid) {
	var grid = Ext.getCmp('packgrid');
	var tpanel = Ext.getCmp('tpanel');
	var docid = grid.getSelectionModel().getSelected().get('id');

	if (btn == 'ok') {
		if (text.length > 0) {

			if (text.length > 500) {
				Ext.MessageBox.alert('Ошибка',
						'Причина не должна привышать 500 символов.');
			} else {

				tab = 0;
				// var selected = grid.getSelectionModel().getSelected();

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
					Pack.mkpackwin(ETD.packid);

				}).defer(1);
			}
		} else {
			Ext.MessageBox
					.alert('Ошибка',
							'Документ не забракован. Необходимо ввести причину браковки.');
		}

	}
};

Pack.reload = function() {
	Pack.load(req);
};

Pack.openDoc = function(row){
	if (row.get('name') == 'ФПУ-26'
		&& ETD.tabname =='ID_SEND_GRID'){
Ext.Ajax.request({
												url : 'forms/fpuacc',
				params : {
					docid : row.get('id'),
					packid: ETD.packid
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

}
	
	
	

else if (row.get('name') == 'РДВ'){
	if (row.get('signed')=='Согласован'||row.get('signed')=='Забракован')
	ETD.isSend = 1;
	else ETD.isSend = 0;
//	alert(ETD.isSend);
	openinpack(row.get('id'), row
			.get('name'));
} else if(ETD.tabname == 'ID_SEND_GRID' && row.get('name') =='Акт приема передачи ТМЦ') {
	Ext.Ajax.request({
        url: 'forms/checkActTMC',
        method:'POST',
        params : {
    		id : row.get('id')
        },
        callback:function(options, success, response){
      	    var json = Ext.util.JSON.decode(response.responseText);
        	ETD.isSend = Number(json[0].isSend);
			openinpack(row.get('id'), row.get('name'));
        }
	});
}



else {
	openinpack(row.get('id'), row.get('name'));
}
}


var openinpack = function(id, name) {
	var send;
	ETD.openDocument(id, name, null, 0);
	viewpackwin.hide();
	viewpackwin.destroy();
};

var replaceQuot = new RegExp("\n", "g");