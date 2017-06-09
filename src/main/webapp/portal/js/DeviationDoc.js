Ext.namespace('DPD');

DPD.deviationDoc = function (title, mess, docid) {

	
	
	var btnok='ok';
	var myDocs = new Ext.data.SimpleStore({url:'forms/doctypeform',fields:['id','name'],autoLoad:false});
var myReason = new Ext.data.SimpleStore({url:'forms/docreason',fields:['group','name'],autoLoad:false});


myReason.load({callback:function(){
	myDocs.load({callback:function(){

var flag = true;
var getDoc = new Ext.form.ComboBox({ fieldLabel:'Выберите документ', matchFieldWidth: false,emptyText: 'Выберите документ',editable: false,store:myDocs,displayField:'name',lazyRender:true,valueField:'id',mode:'local',triggerAction:'all',forceSelection:true,listeners:{select:{fn:function(combo, value) {
    var comboDoc = Ext.getCmp('combo-doc');        
    comboDoc.clearValue();
    

    comboDoc.store.filter('group', combo.getValue());
    comboDoc.enable();
    
}}
}
});



var getReason = new Ext.form.ComboBox({ fieldLabel:'Выберите причину отклонения',matchFieldWidth: false,emptyText: 'Выберите причину',editable: false,disabled:true,lazyRender:true,store:myReason,displayField:'name',valueField:'name',id:'combo-doc',triggerAction:'all',lastQuery:'',mode:'local',listeners:{
    change:function(){
    	//alert(getReason.getValue());
    	if(getReason.getValue()=='Прочие') {
    		Ext.getCmp('textfield').enable();
    		}
    	else{Ext.getCmp('textfield').disable();
    	Ext.getCmp('textfield').allowBlank = true;
    	Ext.getCmp('textfield').validate();}
    	}}});
    var counter = 0;
    var value = mess;
    var datatextfield = "; ";
    
var addButton = new Ext.Button({text: 'Добавить',style:'margin-left: 130px;margin-top:10px',handler: function(){
	 		
	mytextfield = Ext.getCmp('textfield').getValue();
    		var valueGetReason = getReason.getValue();
    		if(valueGetReason == "Прочие") valueGetReason = Ext.getCmp('textfield').getValue();
    		if(counter == 0){
			if (mess!=''){
				value = value + "; "+getDoc.getRawValue()+". "+valueGetReason;
				}
			else {
				value =value + getDoc.getRawValue()+". "+valueGetReason;
    		}
    		}else{
    			value = value + "; " +getDoc.getRawValue()+"."+valueGetReason;
    		}
    		counter++;
            Ext.getCmp('textareafield').setValue(value);
  
}});
    
    var clearButton = new Ext.Button({text: 'Очистить', style:'margin-left: 205 px;margin-top:-21px',handler: function(){
    	value = mess;
    	counter = 0;
    	Ext.getCmp('textareafield').setValue(value);
    }});

var textArea = new Ext.form.TextArea({
    xtype     : 'textareafield',
    readOnly: true,
    name      : 'message',
    value: mess,
    anchor    : '100%',
    height:110,
    id:'textareafield'
});
var text = new Ext.form.TextArea({
    xtype: 'textareafield',
    fieldLabel: 'Введите причину',
    allowBlank:false,
    name: 'text',
    width: 420,
    disabled: flag,
    id:'textfield'
    	
}); 

getDoc.on('expand', function( comboBox ){
	  var tm = Ext.util.TextMetrics.createInstance( comboBox.innerList );
	  var store = comboBox.store;
	  var maxWidth = 0;
	  for(var i = 0, len = store.getCount(); i < len; i++){
	    var r = store.getAt(i);
	    var displayText = r.data[comboBox.displayField];
	    var displayWidth = tm.getWidth( displayText );
	    
	    if (displayWidth > maxWidth) {
	      maxWidth = displayWidth;
	    }
	  }
	  
	  comboBox.list.setWidth( maxWidth );
	  comboBox.innerList.setWidth( maxWidth );
	  comboBox.innerList.setWidth( maxWidth - comboBox.list.getFrameWidth('lr') );

	}, this, { single: false });

getReason.on('expand', function( comboBox ){
	  var tm = Ext.util.TextMetrics.createInstance( comboBox.innerList );
	  var store = comboBox.store;
	  var maxWidth = 0;
	  for(var i = 0, len = store.getCount(); i < len; i++){
	    var r = store.getAt(i);
	    var displayText = r.data[comboBox.displayField];
	    var displayWidth = tm.getWidth( displayText );
	    
	    if (displayWidth > maxWidth) {
	      maxWidth = displayWidth;
	    }
	  }
	  
	  comboBox.list.setWidth( maxWidth );
	  comboBox.innerList.setWidth( maxWidth );
	  comboBox.innerList.setWidth( maxWidth - comboBox.list.getFrameWidth('lr') );

	}, this, { single: false });






var items = [getDoc,getReason,text,addButton,clearButton, {xtype:'fieldset',
             
            layout:'fit',                    
            title:'Причины отклонения',
            style: 'margin-top:20px',
items:[textArea]}];

var form1 = new Ext.form.FormPanel({
	id:'form1',
	border:false,
	autoScroll:false,
	autoHeight:true,	
	style:'margin-left: 5px;margin-top:5px;margin-right: 5px',
	labelWidth:120,
	buttons:[{xtype:'button',name: 'ok',id:'ok',text:'Ок',handler:function(){
		dropfn(btnok,Ext.getCmp('textareafield').getValue());
		createwin.destroy();
		}},
		{xtype:'button',name: 'cancel',id:'cancel',text:'Отмена',handler:function(){
			createwin.destroy();
			}}],
	items:items
	});		



var createwin = new Ext.Window({closable:false,
	height:430,
	width:600,
	autoScroll:true,
	resizable :false,
	title:title,
	items:[form1],
	modal:true
});
createwin.show();
	}
	})
	}
	})
};