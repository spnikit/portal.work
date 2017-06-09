Ext.ux.grid.filter.ComboFilter = Ext.extend(Ext.ux.grid.filter.Filter, {

	
	init: function(){

		this.menu.add(new Ext.form.ComboBox());

	},
	
	isActivatable: function(){
		return false;
	},
	
	setValue: function(value){
	
	},
	
	getValue: function()
	{
		return 666;
	},
	
	serialize: function(){
		var args = [666];
    	this.fireEvent('serialize', args, this);
		return args;
	},
	
	validateRecord: function(record){
		return true;
	}
});