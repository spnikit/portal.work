/**
 * @author qinjinwei
 * 
 * time: 2008-7-24 20:28:02
 */
var  idTmr  =  ""; 
Ext.ux.Grid2Excel = {
 
 Save2Excel : function(grid)
 {
  var cm = grid.getColumnModel();
  var store = grid.getStore();
  
  var it = store.data.items;
  var rows = it.length;
  
  var   oXL   =   new   ActiveXObject("Excel.application");     
  var   oWB   =   oXL.Workbooks.Add();     
  var   oSheet   =   oWB.ActiveSheet; 
  
  var k = 0;
  for (var i = 0 ; i < cm.getColumnCount(); i++) {
   
   if (!cm.isHidden(i)) { 
	   oSheet.Cells(2, k + 1).value = cm.getColumnHeader(i);
	   
   
    	for (var j = 0; j < rows; j++) {
    	r = it[j].data;
    	var v = r[cm.getDataIndex(i)];
    	var fld = store.recordType.prototype.fields.get(cm.getDataIndex(i));
    	if(fld.type == 'date')
    	{
    		v = v.format('Y-m-d');    
    	}
    
    	if(!cm.isHidden(i)){
    		oSheet.Cells(3 + j, k + 1).value = v;
    		}
    	}
    	k = k+1;
    	
   }
  }
        oXL.DisplayAlerts = false;
  oXL.Save();
  oXL.DisplayAlerts = true;                    
  oXL.Quit();
  oXL = null;
    idTmr = window.setInterval("Cleanup();",1);
  }
};
function Cleanup() {
    window.clearInterval(idTmr);
    CollectGarbage();
};