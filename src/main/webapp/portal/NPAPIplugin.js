function NpapiPlugin(obj_name) {
	var isInited = false;
	var plugin = null;
	var objname = obj_name;
	var localheaderString = "application/vnd.xfdl;content-encoding=\"base64-gzip\"\n";
	BrowserDetect.init();
	//alert(BrowserDetect.browser);
	// Firefox
	if ((BrowserDetect.browser == 'Explorer')) {
		plugin = new ActiveXObject("Aisa.AisaCMSPlugin.1");
	} else if ((BrowserDetect.browser == 'Firefox')) {

		var newElement = '<object id="'
				+ objname
				+ '" type="application/x-aisacmsplugin" width="0" height="0"></object>';
		var bodyElement = document.body;
		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
		plugin = document.getElementById(objname);
	} else if ((BrowserDetect.browser == 'Chrome')) {

		var newElement = '<object id="'
				+ objname
				+ '" type="application/x-aisacmsplugin" width="0" height="0"></object>';
		var bodyElement = document.body;
		bodyElement.innerHTML = bodyElement.innerHTML + newElement;
		plugin = document.getElementById(objname);
	} else {
		throw "Browser is not supported";
	}
	this.GetCertID = function() {
		if (isInited) {
			return plugin.GetCertID();
		} else {
			throw "Object not inited";
		}
	};
	this.SignBytes = function(bytes) {
		if (isInited) {
			return plugin.SignBytes(bytes);
		} else {
			throw "Object not inited";
		}
	};
	this.GetCertificate = function() {
		if (isInited) {
			return plugin.GetCertificate();
		} else {
			throw "Object not inited";
		}
	};
	this.GetSubject = function() {
		if (isInited) {
			return plugin.GetSubject();
		} else {
			throw "Object not inited";
		}
	};
	this.FromGzBase64=  function(form){ 
		if (isInited) {
			return plugin.FromGzBase64(form.replace(localheaderString,""));
		} else {
			throw "Object not inited";
		}
	};
	this.SignForm= function(form, button){
		if (isInited) {
			return plugin.SignForm(form,button);
		} else {
			throw "Object not inited";
		} 
	}
	this.SignFormAndCompressBase64Gz= function(form, button){
		if (isInited) {
			return localheaderString+plugin.ToGzBase64(plugin.SignForm(form,button));
		} else {
			throw "Object not inited";
		} 
	}
	this.ToGzBase64=  function(form){ 
		if (isInited) {
			return localheaderString+plugin.ToGzBase64(form);
		} else {
			throw "Object not inited";
		}
	};
	this.GetLastErr = function() {
		if (isInited) {
			return plugin.GetLastErr();
		} else {
			throw "Object not inited";
		}
	};
	isInited = true;
}