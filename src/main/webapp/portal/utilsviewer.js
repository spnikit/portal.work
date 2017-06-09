function addViewer(containerDiv, tagID, _width, _height)
{
 	var _app = navigator.appName;
	var parentDiv = document.getElementById(containerDiv);
	var element;

		 element = document.createElement('object'); 
	for (var i = 4; i < arguments.length; i+=2)
	{
		var param = document.createElement('param');
		param.setAttribute('name', arguments[i]);
		param.setAttribute('value',arguments[i+1]);
		element.appendChild(param);
	}
	var param = document.createElement('param');
	param.setAttribute('name', 'retain_Viewer');
	param.setAttribute('value','on');
	element.appendChild(param);
	
	var param = document.createElement('param');
	param.setAttribute('name', 'TTL');
	param.setAttribute('value','5');
	element.appendChild(param);
	
	
	element.setAttribute('id',tagID);
	element.setAttribute('height',_height);
	element.setAttribute('width',_width);
//alert(_app);
	if (_app == 'Microsoft Internet Explorer')
	{
		parentDiv.appendChild(element);
		element.setAttribute('classid', 'clsid:354913B2-7190-49C0-944B-1507C9125367');
	}
	
	else if ((!window.ActiveXObject) &&"ActiveXObject" in window) {
		
		element.sethtmlAttribute('classid', 'clsid:354913B2-7190-49C0-944B-1507C9125367');
		parentDiv.appendChild(element);
	}
	
}


function CreateOpenViewer(xfdl)
{
	
InsertScript(xfdl);
CreateViewer();

}

function CreateViewer()
{
//console.log("call CreateViewer()");
/**
 * @type HTMLElement
 */
var s = document.createElement('script');

s.setAttribute('type', 'text/javascript');

var docname = window.parent.document.applet_adapter.generateSessionId();

if (navigator.userAgent.indexOf("MSIE") != -1||(!(window.ActiveXObject) &&"ActiveXObject" in window)){

	
	s.text = 'addViewer("viewer","objid",\'100%\',\'100%\',"XFDLID", "XFDLData","_aisa_etd_st_sessionId",+"'
		    + 	docname + '");';
		
	}

else {
	// mozilla
	
	//s.textContent = 'addViewer("viewer","forMozillabrowser",\'100%\',\'100%\',"XFDLID", "XFDLData","detach_id",value="2507088000");';
	
s.textContent = 'addViewer("viewer","CPClient",\'100%\',\'100%\',"XFDLID", "XFDLData","detach_id",value="2507088000","_aisa_etd_st_sessionId",+"'
		   + 	docname + '");';
}	
 document.getElementsByTagName('head')[0].appendChild(s);
 
 //window.parent.document.getElementById('ViewerWindow').appendChild(s);
 //window.parent.document.getElementById('ViewerWindow').appendChild(s);
 
 
}


/*function CreateViewer()
{
var s=document.createElement('SCRIPT');
  s.setAttribute('type','text/javascript');
 s.setAttribute('id','vieweridid');
s.text='addViewer("viewer","objid",\'100%\',\'100%\',"XFDLID", "XFDLData");';
document.getElementById('viewer').appendChild(s);
}*/


function InsertScript(xfdl)
{
//console.log("call InsertScript() " +xfdl);
	var s = document.createElement('SCRIPT');
	// s.setAttribute('type',
	// 'application/vnd.xfdl;wrapped="comment";content-encoding="base64"');
	s.setAttribute('type', 'application/vnd.xfdl');
	s.setAttribute('language', 'XFDL');
	s.setAttribute('id', 'XFDLData');
if (navigator.userAgent.indexOf("MSIE") != -1||navigator.userAgent.indexOf("Mozilla") != -1)
	{
//	console.log(xfdl);
	s.text = xfdl;
	}
/*
else if (navigator.userAgent.indexOf("Chrome") != -1){
	s.text = xfdl;
}

*/
else
	// mozilla
	{
	// TODO здесь могут быть и косяки при представлении символов
	if (false && xfdl.indexOf("--") != -1)
		{
		//console.warn("есть --");
		xfdl = xfdl.replace(new RegExp("--", "g"), "&x2D;&x2D;");
		}

	s.appendChild(document.createTextNode(xfdl));
	
	
	}
document.getElementsByTagName('head')[0].appendChild(s);

}

/*function InsertScript(xfdl)
{
var s = document.createElement('SCRIPT');
  s.setAttribute('type','application/vnd.xfdl;content-encoding="base64"');
  s.setAttribute('language','XFDL');
  s.setAttribute('id','XFDLData');
s.text=xfdl;
document.getElementsByTagName('head')[0].appendChild(s);

}*/

function say(text)
{
createCustomAlert(text);
}

function saveandnoexit()
{

var result = window.parent.saveandnoexit();
 
}

var ALERT_TITLE = "!!!";
var ALERT_BUTTON_TEXT = "ok";


function createCustomAlert(txt) {
	d = document;
	if(d.getElementById("modalContainer")) return;
	mObj = d.getElementsByTagName("body")[0].appendChild(d.createElement("div"));
	mObj.id = "modalContainer";
	mObj.style.height = document.documentElement.scrollHeight + "px";
	alertObj = mObj.appendChild(d.createElement("div"));
	alertObj.id = "alertBox";
	if(d.all && !window.opera) alertObj.style.top = document.documentElement.scrollTop + "px";
	alertObj.style.left = (d.documentElement.scrollWidth - alertObj.offsetWidth)/2 + "px";
	alertObj.style.width = 400 + "px";
	h1 = alertObj.appendChild(d.createElement("h1"));
	h1 = h1.appendChild(d.createElement("center"));
	h1.appendChild(d.createTextNode(ALERT_TITLE));
	msg = alertObj.appendChild(d.createElement("p"));
	msg.appendChild(d.createTextNode(txt));
	temp = alertObj.appendChild(d.createElement("center"));
	btn = temp.appendChild(d.createElement("a"));
	btn.id = "closeBtn";
	btn.appendChild(d.createTextNode(ALERT_BUTTON_TEXT));
	btn.href = "#";
	btn.onclick = function() { removeCustomAlert();return false; }	
}

function removeCustomAlert() {
	document.getElementsByTagName("body")[0].removeChild(document.getElementById("modalContainer"));
	parent.resizeaftersay();
}
