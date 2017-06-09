Ext.namespace('PrintPack');

PrintPack.mkpackwin = function createWindows(arg) {	

	  var fields = [
         {name: 'id_doc', mapping : 'id_doc'},
	     {name: 'id_package', mapping : 'id_pak'},
	     {name: 'vagon', mapping : 'vagnum'},
	     {name: 'formname', mapping : 'formname'},
	     {name: 'onPrint'}
	  ];
	  
	  var json = Ext.util.JSON.decode(arg.responseText);

	  var gridStore = new Ext.data.JsonStore({
		  fields : fields,
		  data   : json,
		  root   : 'data'
	  });
      
	  var cols = [
	    {id :'id_doc', header: "id_doc", sortable: false, dataIndex: 'id_doc', align: 'center'},
	    {header: "Наименование", sortable: false, dataIndex: 'formname', align: 'center'},
	    {header: "Вагон", sortable: false, dataIndex: 'vagon', align: 'center'},
	    {header: "Пакет", sortable: false, dataIndex: 'id_package', align: 'center'},
	    {
            header: 'Отправить на печать',
            dataIndex: 'onPrint',
            align: 'center',
       	    renderer: function(value, metaData, record, rowIndex, colIndex, store) {
	         	if(record.get('formname') == 'Пакет документов') {
	         		return;
	         	} else {
	         		var idDoc = record.get('id_doc');
	            	return "<input type='checkbox' name = 'check[]' value = " + idDoc +" checked='checked'>";
	            }
       	    }
        }
	  ];

	    var grid = new Ext.grid.GridPanel({
	    	id : 'printgrid',
	        store: gridStore,
	        columns: cols,
	        enableDragDrop: true,
			frame: true,
//			autoHeight: true,
//			autoWidth: true,
			resizable : true,
			viewConfig: {
//				autoFill: true,
//				scrollOffset: 0,
				forceFit: true
			},
			listeners : {
	

			},
			deferRowRender: false,
			autoScroll: true,
	        stripeRows: true,
	        autoExpandColumn : 'id_doc',
	        width: 600,
	        height: 400
	    }); 
        
	    grid.getColumnModel().setHidden(0, true);
	    
	    viewpackwin = new Ext.Window({
	    	id : 'windowSendOnPrint', 
			plain : 'true',
			title : 'Документы для отправки на печать',
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
				
			},
			defaults : {
				labelStyle : 'width:120px;'
			},
	
			items : [ grid],
			buttons : [
						{
							text : 'Отправить на печать',
							id : 'print',
							disabled : false,
							handler : function(){
								PrintPack.send();
							}
						},
						{
							text : 'Закрыть',
							id : 'close',
							disabled : false,
							handler : function() {
								viewpackwin.destroy();
							}
						}
					 ]
	});
	viewpackwin.show();	
};

PrintPack.send = function sendToGenerate() {
	 var check = document.getElementsByName('check[]');
	 var checkLength = check.length;
	 var idDocs = '';
	 for(var i=0; i < checkLength; i++) {
		 if(check[i].checked) {
			 if(idDocs == '') {
			    idDocs = check[i].value;
			 } else {
			    idDocs = idDocs + ';' + check[i].value;
			 }
		 }
	}
	if(idDocs == '') {
		Ext.MessageBox.alert('Ошибка','Выберите документы для отправки на печать');
	} else {
		Ext.Ajax.request({
	        url: 'forms/gen_html',
	        method:'POST',
	        params : {
	        	idDocuments: idDocs
	        },
	        success:function(response) {
	    		var resp = response.responseText;
	    		//console.log(resp);
	    		var data = Ext.util.JSON.decode(resp);
	    		//console.log(data.data.length);
				for(var i = 0; i < data.data.length; i++) {
		    	    var newWin = window.open('', "print"+i, "width=200,height=200");
		    		newWin.document.write(data.data[i].html);
		    		newWin.document.close();
		    		//newWin.focus();
		    		newWin.window.print();
		    		//newWin.window.close();
		    	}
	        }
	    });
	}
}







	
	
	

