function NpapiPlugin(obj_name) {
	var isInited = false;
	var plugin = null;
	var objname = obj_name; 
 
	BrowserDetect.init(); 
	if ((BrowserDetect.browser == 'Explorer')||(BrowserDetect.browser == 'Mozilla')) {
	 try{
		 plugin = document.getElementById('aisa.aisaplugin.1');		
		 ETD.newapp = true;
		
		 } catch (e){
		
			 plugin = document.getElementById("aisa.aisaplugin");		
			 
			 ETD.newapp = true;
			 
			 
		 }
	} else if ((BrowserDetect.browser == 'Firefox')) {
		 
		var newElement = '<object id="'
				+ objname
				+ '" type="application/x-aisaplugin" width="0" height="0"> '
				+'<param name="jvmPath" value="C:\\aisa\\jre\\bin\\client">'
				+'<param name="classPath" value="C:\\aisa\\applet.jar">'
				+'<param name="className" value="mypack/zzz">'
				+'<param name="jarsPath" value="C:\\aisa\\dependency">'
				+'</object>'
				;
		var bodyElement = document.body;
	 
		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
		plugin = document.getElementById(objname);
		 try{
//		plugin.getVersion();
          } catch (e){
        	  alert(e);
        	
        		var newElement ='<applet id="aisa_applet" code="mypack.zzz.class" name="zzz"'+
                'archive="applet.jar , applet-xsd.jar,NoticeSheme-xsd.jar,"'+
       		 ' height="0" width="0">'+
       		  '<param name="updateurl" value="http://www.google.com">'+
       		 '<param name="version" value="1.0.0">'+
       		  '<param name="scriptable" value="true" />'+
       		  '<param name="codebase_lookup" value="false" />'+
       		  '</applet>';
       		var bodyElement = document.body;
       		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
       		plugin = document.getElementById('aisa_applet');
		 }
	} else if ((BrowserDetect.browser == 'Chrome')) {

		var newElement = '<object id="'
				+ objname
				+ '" type="application/x-aisaplugin" width="0" height="0"><param name="updateurl" value="http://www.google.com"> <param name="version" value="1.0.0"></object>';
		var bodyElement = document.body;
		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
		plugin = document.getElementById(objname);
		 try{
//				plugin.getVersion();
		          } catch (e){
		        	
		        		var newElement =  '<applet id="'+objname+'_applet" code="mypack.zzz.class" name="zzz"'+
		                 'archive="applet.jar , applet-xsd.jar,NoticeSheme-xsd.jar,"'+
		        		 ' height="0" width="0" >'+
		        		  '<param name="updateurl" value="http://www.google.com">'+
		        		 '<param name="version" value="1.0.0">'+
		        		  '<param name="scriptable" value="true" />'+
		        		  '<param name="codebase_lookup" value="false" />'+
		        		  '</applet>';
		        		var bodyElement = document.body;
		        		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
		        		plugin = document.getElementById(objname);
   	 
				 }
	} else {
		throw "Browser is not supported";
	}
 
	 
	isInited = true;
	 
	return plugin;
}
 