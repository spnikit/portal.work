

var DateWinSNTShow = function(){
datewinSNT.show();datewinSNT.focus();
};

var DateWinOCOShow = function(){
datewinOCO.show();datewinOCO.focus();
};

//var CrwinShow=function(){
//crwinOCO.show();crwinOCO.focus();
//};

var CrwinShow=function(){
	
			 	
			if(combocrstore.getTotalCount()>1||combocrstore.getTotalCount()==0)
			{
				crwinOCO.show();crwinOCO.focus();
			}
			else
			{		
				
				Ext.getCmp('combocrOCO').setValue(combocrstore.getAt(0).get('type'));
				if (Ext.getCmp('combocrOCO').getValue().length>0){  
					Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
					counterpartsArray = eval('('+document.applet_adapter.getCounterPartTypes()+')');					    	
					sendArr = counterpartsArray;
					counterparts_store.loadData(counterpartsArray);
		    		(Ext.getCmp('comboСounterparts').getValue()!=null&&Ext.getCmp('comboСounterparts').getValue().length>0)?Ext.getCmp('comboСounterparts').setValue(Ext.getCmp('comboСounterparts').getValue()):Ext.getCmp('comboСounterparts').setValue(counterparts_store.getTotalCount()>0?counterparts_store.getAt(0).get('text'):"");
					
		    		//Ext.Msg.hide();
					
    				Ext.getCmp('comboCounterPers').disable();    				
    				if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1&&Ext.getCmp('comboСounterparts').getValue()!=null&&Ext.getCmp('comboСounterparts').getValue().length>0)
    	    		{	
    					Ext.getCmp('comboCounterPers').enable();
    		    		var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
    		    		//Ext.Msg.wait('Пожалуйста, подождите');		    		
    		    		counterpartsArray = eval('('+document.applet_adapter.getCounterPersTypes( counterparts_store.getAt(rec).get('id'))+')');					    	
    		    		
    		    		counterpers_store.loadData(counterpartsArray);
    		    		(Ext.getCmp('comboCounterPers').getValue()!=null&&Ext.getCmp('comboCounterPers').getValue().length>0)?Ext.getCmp('comboCounterPers').setValue(Ext.getCmp('comboCounterPers').getValue()):Ext.getCmp('comboCounterPers').setValue(counterpers_store.getTotalCount()>0?counterpers_store.getAt(0).get('text'):"");
						
    		    		//Ext.Msg.hide();
    		    	} 
    				
    				Ext.Msg.hide();
    				if((counterpers_store.getTotalCount()>1&&Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1)||counterparts_store.getTotalCount()>1||counterparts_store.getTotalCount()==0)
    				{    					
    					counterpartsWin.show();
    				}
    				else if(/*counterpers_store.getTotalCount()==1&&*/counterparts_store.getTotalCount()==1)
        			{
    					var fla = 0;
    					if(Ext.getCmp('comboCounterPers').disabled)
    					{
    						fla = 1;
    					}
    					else if(counterpers_store.getTotalCount()==1&&counterparts_store.getTotalCount()>0)
    					{
    						fla = 1;
    					}
    					
    					if(fla==1)
    					{
	    					
	    					//Ext.getCmp('comboCounterPers').setValue(counterpers_store.getAt(0).get('text'));
	    					
	    					//Ext.getCmp('comboСounterparts').setValue(counterparts_store.getAt(0).get('text'));  
	    					
	    					if (Ext.getCmp('comboСounterparts').getValue().length>0){    	    				
	    	    				busy();    	    				
	    	    				if(Ext.getCmp('combocrOCO').getValue().indexOf('Документ')!=-1)    	        				
	    	        				{      	    						
	    								if (Ext.getCmp('comboCounterPers').getValue().length>0){
	    									
	    				    				(function(){    				
	    				    					var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	    				    					var rec = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());    									 
	    										
	    				    					var recdCounter = counterpers_store.find('text', Ext.getCmp('comboCounterPers').getValue());
	    				    					//applet_adapter.createdocOCO(Ext.getCmp('combocrOCO').getValue(),false);
	    				    					ETD.busy();
	    				    					document.applet_adapter.createDocumentMSG(sendArr,combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterpers_store.getAt(recdCounter).get('id'),counterpers_store.getAt(recdCounter).get('text'), counterparts_store.getAt(rec).get('id'),counterparts_store.getAt(rec).get('text')/*record.get('id')record.get('type')*/);
	    				    					ETD.ready();
	    				    				CreateOpenDoc();    				    				
	    				    				}).defer(1);
	    				    			}    								
	    	        				}
	    	        				else
	    	        				{
	    	        					
	    	        					(function(){    				
	    	            					var recd = combocrstore.find('type', Ext.getCmp('combocrOCO').getValue());
	    	            	    			var recdCounter = counterparts_store.find('text', Ext.getCmp('comboСounterparts').getValue());
	    	            					//applet_adapter.createdocOCO(Ext.getCmp('combocrOCO').getValue(),false);
	    	            	    			ETD.busy();
	    	            	    			document.applet_adapter.createDocument(null,combocrstore.getAt(recd).get('id'),combocrstore.getAt(recd).get('type'),counterparts_store.getAt(recdCounter).get('id'),counterparts_store.getAt(recdCounter).get('text')/*record.get('id')record.get('type')*/);
	    	            	    			ETD.ready();
	    	            				CreateOpenDoc();
	    	            				//ETD.ready();
	    	            				
	    	            				}).defer(1);
	    	        				}     	    			
	    	    			}
    					}
    					
    				}
    			}
				
			//}
	}
	
};





filterwin_OCO = new Ext.Window({
	plain: 'true',
	width: 300,
	height:250,
	layout: 'form',
	resizable: false,
	modal:true,
	border:true,
	buttonAlign:'center',
	closable: false,
	title:'Введите параметры поиска',
	style:'text-align: center;',
	items: [
			{xtype:'label',html:'<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">Дата создания:</div>',style:'font-size: 12px;'},
			{xtype:'datefield',id:'dcrdate',fieldLabel:'С',style:'width: 80px;'},
			{xtype:'datefield',id:'dcrdateto',fieldLabel:'По',style:'width: 80px;'},
			{xtype:'textfield',id:'creator',fieldLabel:'Создатель',style:'width: 150px;'},
			{xtype:'textfield',id:'name',fieldLabel:'Наименование документа',style:'width: 150px;'},
			{xtype:'textfield',id:'num_vag',fieldLabel:'Номер вагона',style:'width: 150px;'}
		],
		buttons: [{text: 'OK',
		handler:function(){
			busy();
			(function(){
				filterwin_OCO.hide();
				Ext.getCmp('rqnfilter').addClass('but-flt');
				var srchArr = new Array();
				srchArr[0]=''//Ext.getCmp('num').getValue();
				srchArr[1]=''//Ext.getCmp('dfrom').getValue();
				srchArr[2]=''//Ext.getCmp('dto').getValue();
				srchArr[3]=Ext.getCmp('dcrdate').getValue();
				srchArr[4]=Ext.getCmp('dcrdateto').getValue();
				srchArr[5]=Ext.getCmp('creator').getValue();
				srchArr[6]=Ext.getCmp('name').getValue();
				srchArr[7]=Ext.getCmp('num_vag').getValue();
				srchArr[8]=''//Ext.getCmp('bal').getValue();
			
					//	srchArr[9]=Ext.getCmp('num_vag').getValue();
				for (i=0;i<9;i++) if (srchArr[i].length==0) srchArr[i]='---';
				srchArrStr = srchArr.join("|");
				var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
				document.applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),srchArrStr,Ext.getCmp('ROWSCOUNTID').getValue());
				
				//if(tpanel.getActiveTab().getId() == 'grid') 
				//	applet_adapter.filter_OCO(getAfterDate(),getBeforeDate(),gr_f,gr_d,srchArrStr);
				//else applet_adapter.table_arch_scroll(getAfterDate_arch(),getBeforeDate_arch(),gra_f,gra_d,srchArrStr);
				update_table();
				ready();}).defer(1)
						}	
	},{text:'Отмена',
		handler:function(){
		filterwin_OCO.hide();
		}
	},{text:'Сбросить',
		handler:function(){	
		busy();
		(function(){
			Ext.getCmp('rqnfilter').removeClass('but-flt');
			//Ext.getCmp('num').setValue('');
			//Ext.getCmp('dfrom').setValue('');
			//Ext.getCmp('dto').setValue('');
			Ext.getCmp('dcrdate').setValue('');
			Ext.getCmp('dcrdateto').setValue('');
			Ext.getCmp('creator').setValue('');
			Ext.getCmp('name').setValue('');
			Ext.getCmp('num_vag').setValue('');
			filterwin_OCO.hide();
			srchArrStr = null;
		var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
		document.applet_adapter.table_scroll(getAfterDate(),getBeforeDate(),gr_f,gr_d,index,combo.getValue(),null,Ext.getCmp('ROWSCOUNTID').getValue());
			update_table();	
		ready();}).defer(1)
		}
		}
	]
});



var FilterWinSNT = function(){
	
	throw new Error('NOT IMPLEMENTED');
	//rqnfilterwin_SNT.show();rqnfilterwin_SNT.focus();
	
};

var FilterWinOCO = function(){
	
	var index = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel').getActiveTab());
	
	filterwin_newall.show();filterwin_newall.focus();
	/*switch(index)
	{
		case 0:
			filterwin_income.show();filterwin_income.focus();
			break;
		case 1:
			filterwin_fin.show();filterwin_fin.focus();
			break;
		case 2:
			filterwin_send.show();filterwin_send.focus();
			break;
		case 3:
			filterwin_rough.show();filterwin_rough.focus();
			break;
		case 4:
			filterwin_arch.show();filterwin_arch.focus();
			break;
	}*/
	
	
};