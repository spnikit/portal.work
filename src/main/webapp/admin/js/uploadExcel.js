/**
 * 
 */

var  idTmr  =  ""; 
ru.aisa.uploadExcel = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		
		conf = {xtype:'button',text:'Выгрузить'}
		 ru.aisa.uploadExcel.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			
			var cm = grid.getColumnModel();
			var sm = grid.getSelectionModel();
			var headerField = "";
			var url = 'controllers/sampleUploadExcel?';
			var tag = Ext.getCmp('treepanel').getSelectionModel().getSelectedNode().text;
			//var id = "";
			
			 for (var i = 0 ; i < cm.getColumnCount(); i++) {
				 if (!cm.isHidden(i)) {
					 if(i == cm.getColumnCount()-1)
						 headerField  = headerField+cm.getColumnHeader(i).toString();
					 else
						 headerField  = headerField+cm.getColumnHeader(i).toString()+";"; 
				
				 }	 
			 }

			var param ='act=export&predid='+SampleSearchData.predid+'&headerField='+headerField+'&dateFrom='+SampleSearchData.dateFrom+'&dateTo='+SampleSearchData.dateTo;
			param = encodeURI(param);

				    var form = Ext.DomHelper.append(document.body, {
				        tag : 'form',
				        method : 'post',
				        action : url+param		            
				    });
				    document.body.appendChild(form);
				    form.submit();
				    document.body.removeChild(frame);  

		})	
	}

    
});