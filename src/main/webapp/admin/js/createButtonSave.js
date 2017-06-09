/**
 * 
 */

var  idTmr  =  ""; 
ru.aisa.createButtonSave = Ext.extend(Ext.Button,{

	constructor: function(config,grid){
		
		conf = {xtype:'button',text:'Выгрузить'}
		 ru.aisa.createButtonSave.superclass.constructor.apply(this,[conf]);
		this.on('click',function(button){
			 var cm = grid.getColumnModel();
			  var store = grid.getStore();
			  var title = grid.header.dom.firstChild.innerHTML;
			  
			  
			  var it = store.data.items;
			  var rows = it.length;
			  
			  try{
				  var   oXL   =   new   ActiveXObject("Excel.application");
				  var   oWB   =   oXL.Workbooks.Add();     
				  var   oSheet   =   oWB.ActiveSheet; 
			  }
			  catch(e)
			  {
				 alert(e.message);
				 oXL = null;
				 oWB = null;
				 oSheet = null;
				 return;
			  }

			  oXL.Visible = true;
			  

			  //oSheet.Cells(1, 1).Interior.ColorIndex = 37
			  var k = 0;
			  var colorSwitch = 4;
			  var letterWidth = 1;
			  var spacing = 5;
			  
			  oSheet.Cells(1, 1).value = title;
			  oSheet.Cells(1, 1).ColumnWidth = title.length*letterWidth+ spacing;
			  for (var i = 0 ; i < cm.getColumnCount(); i++) {
				  
			   
			   if (!cm.isHidden(i)) {
				   //oSheet.Cells(1, k+1).Interior.ColorIndex = 34;
				   oSheet.Cells(2, k + 1).value = cm.getColumnHeader(i);
					var width = oSheet.Cells(2, k+1).ColumnWidth;
					if(width<cm.getColumnHeader(i).toString().length*letterWidth + spacing){
						   oSheet.Cells(2, k+1).ColumnWidth = cm.getColumnHeader(i).toString().length*letterWidth + spacing;
						}
					colorSwitch ^= 4;

			    	for (var j = 0; j < rows; j++) {
			    		//oSheet.Cells(j+3, k+1).Interior.ColorIndex = 36 + colorSwitch;
			    	r = it[j].data;
			    	var v = r[cm.getDataIndex(i)];
			    	var fld = store.recordType.prototype.fields.get(cm.getDataIndex(i));
			    	if(fld.type == 'date')
			    	{
			    		v = v.format('Y-m-d');    
			    	}
			    
			    	if(!cm.isHidden(i)){
			    		var _v = v;//TrimStr(v);
			    		oSheet.Cells(3 + j, k + 1).value = _v.toString();
			    		var width = oSheet.Cells(j+3, k+1).ColumnWidth;
			    		if(width<_v.length*letterWidth){
			    			oSheet.Cells(j+3, k+1).ColumnWidth = _v.toString().length*letterWidth + spacing;
			    			}
			    			
			    		}
			    	}
			    	k = k+1;
			    	
			   }
			  }
			  
			  //Границы ячеек
			 var xlDiagonalDown =5 		//Диагональная от верхнего левого угла в нижний правый каждой ячейки в диапазоне 
			 var xlDiagonalUp =6 		//Диагональная из нижнего левого угла в правый верхний каждой ячейки в диапазоне. 
			 var xlEdgeBottom =9 		//Нижнаяя для всего диапазона ячеек 
			 var xlEdgeLeft =7 			//Левая для всего диапазона ячеек. 
			 var xlEdgeRight =10 		//Правая для всего диапазона ячеек. 
			 var xlEdgeTop =8 			//Верхняя для всего диапазона ячеек. 
			 var xlInsideHorizontal =12 //Горизонтальные границы всех внутренних ячеек диапазона 
			 var xlInsideVertical =11 	//Вертикальные границы всех внутренних ячеек диапазона
			 
			 //выравнивание текста в ячейке
			 var xlCenter = -4108	//по центру
			 var xlRight = -4152	//справа
			 var xlLeft = -4131		//слева


			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlInsideHorizontal).Weight = 2;
			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlInsideVertical).Weight = 2;
			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeBottom).Weight = 2;
			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeRight).Weight = 2;
			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeLeft).Weight = 2;
			  oSheet.Range(oSheet.Cells(2 , 1), oSheet.Cells( rows+2, k)).Borders(xlEdgeTop).Weight = 2;
			  oSheet.Rows(1).HorizontalAlignment = xlCenter;
			  oSheet.Range(oSheet.Cells(2, 1), oSheet.Cells(2, k)).Cells.HorizontalAlignment = xlCenter
			  oSheet.Cells(1, 1).Font.Size = 12;
			  oSheet.Cells(1, 1).Font.Bold = true;
			  oSheet.Range(oSheet.Cells(2, 1), oSheet.Cells(2, k)).Cells.Font.Bold = true
			  oSheet.Range(oSheet.Cells(1,1), oSheet.Cells(1, k)).Merge();
			  oSheet.Rows.VerticalAlignment = xlCenter;
			  
			  oSheet.Cells(1, 1).RowHeight += 5;
			  oSheet.Cells(2, 1).RowHeight += 3;


			  oSheet = null;
			  oWB = null;
			  oXL = null;
			    idTmr = window.setInterval("Cleanup();",1);
			  
			  function Cleanup() {
				    window.clearInterval(idTmr);
				    CollectGarbage();
				};
				function TrimStr(s) {
					  s = s.replace( /^\s+/g, '');
					  return s.replace( /\s+$/g, '');
					}

		})	
	}

    
});