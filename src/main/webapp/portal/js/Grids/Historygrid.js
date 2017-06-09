Ext.namespace('History');

History.load = null;

History.store = new Ext.data.JsonStore({
	root : 'history',
	totalProperty : 'totalCount',
	successProperty : 'success',
	proxy : History.proxy,
	remoteSort : true,
	fields : [ {
		name : 'id',
		type : 'int'
	}, {
		name : 'name'
	}, {
		name : 'droptxt'
	}, {
		name : 'createDate',
		type : 'date',
		dateFormat : 'Y-m-d H:i:s'
	}, {
		name : 'lastDate',
		type : 'date',
		dateFormat : 'Y-m-d H:i:s'
	}, {
		name : 'id_pak'
	}

	]
});

History.req = null;
History.grid = null;
History.sm = new Ext.grid.RowSelectionModel({
	singleSelect : true
});
History.load = function(vagnum, reqdate, docid) {
	ETD.busy();
//	ret = document.applet_adapter.getHistory(req, Ext.getCmp('tpanel')
//			.getActiveTab().getSelectionModel().getSelected().get('reqdate'),
//			Ext.getCmp('tpanel').getActiveTab().getSelectionModel()
//					.getSelected().get('id'));
	ret = document.applet_adapter.getHistory(vagnum, reqdate, docid);
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
	History.grid.isLoaded = true;
	var response = Ext.util.JSON.decode(ret);
	if (response.history)
		History.store.loadData(response);

	else {
		viewHistorywin.destroy();
		Ext.MessageBox.alert('Сообщение',
				'Нет ранее созданных пакетов по этому ремонту вагона');
	}
	return null;
};

History.mkHistorywin = function(vagnum, reqdate, docid) {

	History.Historycreate(vagnum, reqdate, docid);

	viewHistorywin = new Ext.Window({
		plain : 'true',
		title : 'История ремонта вагона № ' + vagnum,
		width : 600,
		height : 330,
		layout : 'form',
		resizable : false,
		modal : true,
		border : true,
		buttonAlign : 'right',
		closable : true,
		style : 'text-align: center;',
		defaults : {
			labelStyle : 'width:120px;'
		},

		items : [ History.grid
		 , new Ext.form.TextArea({
		 xtype : 'textareafield',
		 width:580,
		 height:60,
		 hideLabel:true,
		 readOnly: true,
		 name : 'remmessage',
		 id:'remtextarea'
		 })
		],
		buttons : [// {text:'Отклонить', handler:dropHistorydoc},
		{
			text : 'Закрыть',
			handler : function() {
				viewHistorywin.close();
			}
		} ]
	});
	viewHistorywin.show();
	// viewHistorywin.focus();
}

History.Historycreate = function(vagnum, reqdate, docid) {

	History.grid = new Ext.grid.GridPanel({
		id : 'Historygrid',
		store : History.store,
		isLoaded : false,
//		autoSizeHeaders: true,
//		autoSizeColumns: true,
		height : 200,
		cm : new Ext.grid.ColumnModel([

		{
			header : 'Наименование',
			sortable : false,
			dataIndex : 'name'
		}, {
			header : 'Дата поступления',
			sortable : false,
			renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
			dataIndex : 'createDate'
		}, {
			header : 'Дата отклонения',
			sortable : false,
			renderer : Ext.util.Format.dateRenderer('Y-m-d H:i:s'),
			dataIndex : 'lastDate'

		}, {
			header : 'Пакет',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'id_pak'

		}, {
			id : 'expand',
			header : 'Причина отклонения',
			sortable : false,
			renderer : ETD.shortDescriptionRender,
			dataIndex : 'droptxt'
		} ]),
		sm : History.sm,
		frame : true,
		autoScroll : true,
		 stripeRows: true,
         autoExpandColumn:'expand',
//         forceFit:true,
		listeners : {
			render : function(panel) {
				History.load(vagnum, reqdate, docid);
			}
		}
	
	});

}

History.sm.on({
	rowdeselect : {
		fn : function() {
			 Ext.getCmp('remtextarea').setValue('');
		}
	},
	rowselect : {
		fn : function(sm, num, r) {
			var row = sm.getSelected();

			 Ext.getCmp('remtextarea').setValue(row.get('droptxt'));

		}
	}

});

var replaceQuot = new RegExp("\n", "g");