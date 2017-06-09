Ext.ux.grid.filter.DateFilter = Ext.extend(Ext.ux.grid.filter.Filter, {
	dateFormat: 'm/d/Y',
	pickerOpts: {},
	MDI : "Filter MY",
	
	init: function(){
		var opts = Ext.apply(this.pickerOpts, {
			minDate: this.minDate, 
			maxDate: this.maxDate, 
			hideOnClick : false,
			format:  this.dateFormat
		});
		var dates = this.dates = {
			'before': new Ext.menu.CheckItem({text: "До", hideOnClick : false, menu: new Ext.menu.DateMenu(opts)}),
			'after':  new Ext.menu.CheckItem({text: "После", hideOnClick : false, menu: new Ext.menu.DateMenu(opts)})
			//'on':     new Ext.menu.CheckItem({text: "Точно", menu: new Ext.menu.DateMenu(opts)})
			};
				
		var applyButton = new Ext.menu.Item({text : "Применить"});
		applyButton.on('click', function()
		{
				this.setActive(this.isActivatable(), true);
				this.fireEvent("update", this);
		}, this);
		
		this.menu.add(dates.after, dates.before, "-", applyButton);
		
		for(var key in dates){
			var date = dates[key];
			date.menu.on('select', function(date, menuItem, value, picker)
			{
				date.setChecked(true);
				
				if(date == dates.after && dates.before.menu.picker.value < value)
           			dates.before.setChecked(false, true);
         		else if (date == dates.before && dates.after.menu.picker.value > value)
           			dates.after.setChecked(false, true);
				
				date.menu.hide(false);
				//this.fireEvent("update", this);
			}.createDelegate(this, [date], 0));
			
			/*date.on('checkchange', function(){
				this.setActive(this.isActivatable());
			}, this);*/
		};
	},
	
	getFieldValue: function(field){
		return this.dates[field].menu.picker.getValue();
	},
	
	getPicker: function(field){
		return this.dates[field].menu.picker;
	},
	
	isActivatable: function(){
		return this.dates.after.checked || this.dates.before.checked;
	},
	
	setValue: function(value){
		for(var key in this.dates)
			if(value[key]){
				this.dates[key].menu.picker.setValue(value[key]);
				this.dates[key].setChecked(true);
			} else {
				this.dates[key].setChecked(false);
			}
	},
	
	getValue: function(){
		var result = {};
		for(var key in this.dates)
			if(this.dates[key].checked)
				result[key] = this.dates[key].menu.picker.getValue();
				
		return result;
	},
	
	serialize: function()
	{
		var args = [];
		if(this.dates.before.checked)
			args = [{type: 'date', comparison: 'lt', value: this.getFieldValue('before').format(this.dateFormat)}];
		if(this.dates.after.checked)
			args.push({type: 'date', comparison: 'gt', value: this.getFieldValue('after').format(this.dateFormat)});

	    this.fireEvent('serialize', args, this);
			return args;
	},
	
	validateRecord: function(record)
	{
		var val = record.get(this.dataIndex).clearTime(true).getTime();
		
		if(this.dates.before.checked && val >= this.getFieldValue('before').clearTime(true).getTime())
			return false;
		
		if(this.dates.after.checked && val <= this.getFieldValue('after').clearTime(true).getTime())
			return false;
			
		return true;
	}
});