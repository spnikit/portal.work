
Ext.grid.GridFilters = function(config){		
 /* this.filters = new Ext.util.MixedCollection();
	this.filters.getKey = function(o){return o ? o.dataIndex : null};
	
	for(var i=0, len=config.filters.length; i<len; i++)
		this.addFilter(config.filters[i]);
	
//	this.deferredUpdate = new Ext.util.DelayedTask(this.reload, this);
	
	delete config.filters;*/

	Ext.apply(this, config);
};
Ext.extend(Ext.grid.GridFilters, Ext.util.Observable, {
	
	updateBuffer: 500,
	
	paramPrefix: 'filter',
	
	filterCls: 'ux-filtered-column',
	
	local: false,
	
	autoReload: true,
	
	stateId: undefined,
	
	showMenu: true,
   
    filtersText: 'Filters',
    

	init: function(grid){
    
      this.grid  = grid;
      
      this.grid.filters = this;
      
      grid.on("render", this.onRender, this);	

	},

	onRender: function(){
		var hmenu;
		
		if(this.showMenu) {
			hmenu = this.grid.getView().hmenu;
			
				
			this.bef  = hmenu.add(
			new Ext.menu.CheckItem({
			text: 'До',
			hideOnClick : false,
			menu: new Ext.menu.DateMenu({})
			}));
			
			this.aft  = hmenu.add(
			new Ext.menu.CheckItem({
			text: 'После',
			hideOnClick : false,
			menu: new Ext.menu.DateMenu({})
			}));
			
			this.clear  = hmenu.add(
			new Ext.menu.Item({
			text: 'Очистить'
			}));
			
			this.activeAft = false;
			this.activeBef = false;
		hmenu.on('beforeshow', this.onMenu, this);
		
		this.aft.menu.on('select', function(picker,date){
			var gridpanel;
			if	(tpanel_or_tpanel_test==1) gridpanel = Ext.getCmp('tpanel_test').getActiveTab();
			else gridpanel = Ext.getCmp('tpanel').getActiveTab();
			var filters = gridpanel.getFilter();
		filters.getAftCbx().setChecked(true);
		filters.setActiveAft(true);
		Ext.getCmp('rqnfilter').removeClass('but-flt');
		busy();
		(function(){
			var index;
			if	(tpanel_or_tpanel_test==1) index = fr_addonbaseid;
			else index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
			applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),null);
			update_table();
			ready();}).defer(1)
		var hds = gridpanel.getView().mainHd.select('td').removeClass("filtered");
		var i=0;
		for (i=0;i<gridpanel.getColumnModel().getColumnCount(true);i++)
		if (!gridpanel.getColumnModel().isMenuDisabled(i)) hds.item(i).addClass("filtered");
		});
		
		this.bef.menu.on('select', function(picker,date){
			var gridpanel;
			if	(tpanel_or_tpanel_test==1) gridpanel = Ext.getCmp('tpanel_test').getActiveTab();
			else gridpanel = Ext.getCmp('tpanel').getActiveTab();
		var filters = gridpanel.getFilter();
		filters.getBefCbx().setChecked(true);
		filters.setActiveBef(true);
		Ext.getCmp('rqnfilter').removeClass('but-flt');
		busy();
		(function(){
			var index;
			if	(tpanel_or_tpanel_test==1) index = fr_addonbaseid;
			else index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
			applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),null);
			update_table();
			ready();}).defer(1)
		var hds = gridpanel.getView().mainHd.select('td').removeClass("filtered");
		var i=0;
		for (i=0;i<gridpanel.getColumnModel().getColumnCount(true);i++)
		if (!gridpanel.getColumnModel().isMenuDisabled(i)) hds.item(i).addClass("filtered");
		});
		
		this.clear.on('click', function(item,ev){
			var gridpanel;
			if	(tpanel_or_tpanel_test==1) gridpanel = Ext.getCmp('tpanel_test').getActiveTab();
			else gridpanel = Ext.getCmp('tpanel').getActiveTab();
		var filters = gridpanel.getFilter();
		filters.getAftCbx().setChecked(false);
		filters.getBefCbx().setChecked(false);
		filters.setActiveAft(false);
		filters.setActiveBef(false);
		Ext.getCmp('rqnfilter').removeClass('but-flt');
		busy();
		(function(){
			var index;
			if	(tpanel_or_tpanel_test==1) index = fr_addonbaseid;
			else index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
			applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),null);
			update_table();
			ready();}).defer(1)
		var hds = gridpanel.getView().mainHd.select('td').removeClass("filtered");
		});
		}
},
	
	getAftCbx: function(){
	return this.aft;
	},
	getBefCbx: function(){
	return this.bef;
	},
	getClear: function(){
	return this.clear;
	},
	setActiveAft: function(bol){
	this.activeAft = bol;
	},
	setActiveBef: function(bol){
	this.activeBef = bol;
	},
	isActiveAft: function(){
	return this.activeAft;
	},
	isActiveBef: function(){
	return this.activeBef;
	},
	
	onMenu: function(filterMenu) {
		this.bef.setVisible(true);
		this.aft.setVisible(true);
		this.clear.setVisible(true);
	},
	
	/*addFilter: function(config){
		var filter = config.menu ? config : 
				new (this.getFilterClass(config.type))(config);
		this.filters.add(filter);
		
	//	Ext.util.Observable.capture(filter, this.onStateChange, this);
		return filter;
	},
	*/

	getFilterClass: function(type){
		return Ext.grid.filter[type.substr(0, 1).toUpperCase() + type.substr(1) + 'Filter'];
	}
});