Ext.namespace('Shep.widgets');
Ext.namespace('Shep');

Shep.widgets.addarchdocs = Ext.extend(Ext.grid.GridPanel, {
	border : false,
	initComponent : function() {
		
		var cols = ["Наименование","Номер","Балансовя единица","Дата/Время создания","Персона последней подписи","Дата/Время последней подписи","Создатель","От"];

		var store = new Ext.data.SimpleStore({
		fields: [
		    {name: 'name'},
			{name: 'number'},
			{name: 'datecreated', type: 'date',dateFormat: 'Y-m-d H:i:s'},
			{name: 'balance'},
			{name: 'signer'},
			{name: 'datelastsigned', type: 'date', dateFormat: 'Y-m-d H:i:s'},
			{name: 'creator'},
			{name: 'datefrom', type: 'date', dateFormat: 'Y-m-d'},
			{name: 'rownum', type: 'int'}
			]
	});
	
	var	filters = new Ext.grid.GridFilters({
	  filters:[
	    {type: 'date',  dataIndex: 'datecreated'}
	]});
		this.on('celldblclick',function(){
			opendoc_addonbase(0);
		});
		this.on('sortchange',function( grid, sortInfo ){
						gr_f = sortInfo.field;
						gr_d = sortInfo.direction;
						Ext.getCmp('rqnfilter').removeClass('but-flt');
						busy();
						(function(){
					//	var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
						applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,fr_addonbaseid,combo.getValue(),null,Ext.getCmp('ROWSCOUNTID').getValue());
						update_table();
						ready();}).defer(1);
		});
		
		
		Ext.apply(this, {
	
			 viewConfig: {
            forceFit: true
        },
	//	margins: '10 10 10 10',
		store: store,
		id: 'arch11',
	//	enableHdMenu: false,
		columns: [
		    {id:'creator',header: cols[6], width: 150, dataIndex: 'creator', menuDisabled: true},
			{header: cols[0], width: 150, dataIndex: 'name', menuDisabled: true},
			{header: cols[7], width: 150, dataIndex: 'datefrom', menuDisabled: true,renderer: Ext.util.Format.dateRenderer('d.m.Y')},
			{header: cols[1], width: 60, dataIndex: 'number', menuDisabled: true,sortable:true},
			{header: cols[2],width: 125,  dataIndex: 'balance',menuDisabled: true,sortable:true},
			{header: cols[3],width: 125,  dataIndex: 'datecreated',sortable:true,renderer: Ext.util.Format.dateRenderer('d.m.Y H:i')},
			{header: cols[4],width: 125,  dataIndex: 'signer',menuDisabled: true,sortable:true},
            {id:'expand',header: cols[5],width: 125,  dataIndex: 'datelastsigned',menuDisabled: true,sortable:true,renderer: Ext.util.Format.dateRenderer('d.m.Y H:i')},
			{header: '#',dataIndex: 'rownum',hidden:true}
		],
		stripeRows: true,
		autoExpandColumn: 'expand',
		region: 'center',
		title:'Архив',
		loadMask:true,
		plugins: filters,
		sm: new Ext.grid.RowSelectionModel({singleSelect:false}),
		enableColumnHide: false,
		enableColumnMove: false,
		bbar:[{xtype: 'tbspacer'},
			{text:'<< ', minWidth:130, id:'prev_page1',style:'margin-right: 20px;',disabled:true,handler : function() {
						busy();
						(function(){
						
						applet_adapter.table_prev(getAfterDate(),getBeforeDate(),gr_f,gr_d,fr_addonbaseid,combo.getValue(),Ext.getCmp('ROWSCOUNTID').getValue());
						update_table();
						ready();}).defer(1);
					}
			},
			{xtype:'button',width:100,id:'page_number1',handler : function() {
						Ext.getCmp('rqnfilter').removeClass('but-flt');
						busy();
						(function(){
					
						applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,fr_addonbaseid,combo.getValue(),null,Ext.getCmp('ROWSCOUNTID').getValue());
						update_table();
						ready();}).defer(1)
					}
			},
			{text:'>>', minWidth:130, id:'next_page1',style:'margin-left: 20px;',disabled:true,handler : function() {
						busy();
						(function(){
				
						applet_adapter.table_next(getAfterDate(),getBeforeDate(),gr_f,gr_d,fr_addonbaseid,combo.getValue(),Ext.getCmp('ROWSCOUNTID').getValue());
						update_table();
						ready();}).defer(1);
					}
			},
			{xtype: 'tbspacer'}]
					
			});
				
		Shep.widgets.addarchdocs.superclass.initComponent.apply(this,
				arguments);
	},
	onRender : function() {
		Shep.widgets.addarchdocs.superclass.onRender.apply(this, arguments);
	},
	
	getFilter : function(){
	
		
		return this.filters;	
	}
});


Ext.reg('Shep.widgets.addarchdocs', Shep.widgets.addarchdocs);

	