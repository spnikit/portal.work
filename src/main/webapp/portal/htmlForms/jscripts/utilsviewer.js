/**
 * Этот файл подгружается в случае использование aisa-viewer
 * 
 * @include "includes.js"
 */

/*
 * function addViewer(containerDiv, tagID, _width, _height) { console.log("call
 * addViewer()"); var _app = navigator.appName; var parentDiv =
 * document.getElementById(containerDiv); var element =
 * document.createElement('object');
 * 
 * for (var i = 4; i < arguments.length; i += 2) { var param =
 * document.createElement('param'); param.setAttribute('name', arguments[i]);
 * param.setAttribute('value', arguments[i + 1]); element.appendChild(param); }
 * var style = "position: absolute;width:" + _width + ";height:" + _height +
 * ";left:0;top:0"; // element.setAttribute('height', _height); //
 * element.setAttribute('width', _width); element.setAttribute('id', tagID);
 * element.setAttribute('style', style);
 * 
 * if (_app == 'Microsoft Internet Explorer') element.setAttribute('classid',
 * 'clsid:354913B2-7190-49C0-944B-1507C9125367'); else
 * element.setAttribute('type', 'application/vnd.xfdl');
 * parentDiv.appendChild(element); }
 */

/**
 * 
 * @param {String}
 *          xfdl
 */
/*
 * function InsertScript(xfdl) { console.log("call InsertScript()"); var s =
 * document.createElement('SCRIPT'); // s.setAttribute('type', //
 * 'application/vnd.xfdl;wrapped="comment";content-encoding="base64"');
 * s.setAttribute('type', 'application/vnd.xfdl'); s.setAttribute('language',
 * 'XFDL'); s.setAttribute('id', 'XFDLData'); if
 * (navigator.userAgent.indexOf("MSIE") != -1) { s.text = xfdl; } else //
 * mozilla { // TODO здесь могут быть и косяки при представлении символов if
 * (false && xfdl.indexOf("--") != -1) { console.warn("есть --"); xfdl =
 * xfdl.replace(new RegExp("--", "g"), "&x2D;&x2D;"); } console.info(xfdl); //
 * var commentXFDL = document.createComment(xfdl); // console.info(commentXFDL); //
 * s.textContent = xfdl ; s.appendChild(document.createTextNode(xfdl)); }
 * document.getElementsByTagName('head').item(0).appendChild(s); }
 */

/*
 * function CreateViewer() { console.log("call CreateViewer()");
 *//**
		 * @type HTMLElement
		 */
/*
 * var s = document.createElement('script');
 * 
 * s.setAttribute('type', 'text/javascript'); // s.setAttribute('id',
 * 'vieweridid');
 * 
 * if (navigator.userAgent.indexOf("MSIE") != -1) { s.text =
 * 'addViewer("viewer","objid",\'100%\',\'100%\',"XFDLID", "XFDLData");'; } else //
 * mozilla { // s.textContent = //
 * 'addViewer("viewer","CPClient",\'500px\',\'500px\',"XFDLID", //
 * "XFDLData","detach_id",value="2507088000");'; // TODO разобраться зачем
 * detach_id, хотя и без него работает // TODO третий параметр(height) почему то
 * не работает как проценты s.textContent =
 * 'addViewer("viewer","CPClient",\'100%\',\'100%\',"XFDLID",
 * "XFDLData","detach_id",value="2507088000");'; } //
 * document.getElementsByTagName('head')[0].appendChild(s);
 * 
 * document.getElementById('viewer').appendChild(s); }
 */

/**
 * Открыть xfdl-документ во вьювере
 * 
 * @param {String}
 *          xfdl
 */

function SerializeXFDL(){
	return serial.serializeToString(xfdlForm);
}


function initBaseFunction(appURL,fSave,fCancel,fSaveExit,fAddingSave, windowId){
	this.appURL = appURL;
	this.funcSave = fSave.substring(12,fSave.length-1);
	this.funcCancel = fCancel.substring(12,fCancel.length-1);
	this.funcSaveExit = fSaveExit.substring(12,fSaveExit.length-1);
	this.funcAddingSave = fAddingSave.substring(12,fAddingSave.length-1);
}

function CreateOpenViewer(xfdl,readonly)
	{
	console.log("call AISA CreateOpenViewer()");
	// abc();
	// TODO Lotus-xfdlviewer
	// InsertScript(xfdl);
	// CreateViewer();
	// TODO aisa-xfdlviewer
	if (readonly) isGlobalEdit = false;
	setASETDWindowConext();
	// console.group("XFDL");
	// console.log(xfdl);
	// console.groupEnd();
	initXFDLViewer(xfdl);
	}
/**
 * установить родительское окно
 * 
 * @param {Window}
 *          win
 */
function setASETDWindowConext()
	{
	glWindow = window.parent;
	}

function saveandnoexit()
	{
	console.info("call AISA saveandnoexit()");
	//window.parent.saveandnoexit();
	if (funcSave.length>0){
		eval(funcSave);
	}
	
	//console.trace();
	// if (window.parent && window.parent.saveandnoexit)
	// window.parent.saveandnoexit();
	// else
	// console.warn("нет window.parent");
	//var saveresp = window.parent.document.applet_adapter.saveDoc('0');
	//console.info("saveresp = " + saveresp);
	//if (saveresp != 'Документ сохранен успешно')
		//alert(saveresp);
	}

function say(text)
	{
	console.log("call say()");
	createCustomAlert(text);
	}

var ALERT_TITLE = "!!!";
var ALERT_BUTTON_TEXT = "ok";

function createCustomAlert(txt)
	{
	console.log("call createCustomAlert()");
	d = document;
	if (d.getElementById("modalContainer"))
		return;
	mObj = d.getElementsByTagName("body")[0].appendChild(d.createElement("div"));
	mObj.id = "modalContainer";
	mObj.style.height = document.documentElement.scrollHeight + "px";
	alertObj = mObj.appendChild(d.createElement("div"));
	alertObj.id = "alertBox";
	if (d.all && !window.opera)
		alertObj.style.top = document.documentElement.scrollTop + "px";
	alertObj.style.left = (d.documentElement.scrollWidth - alertObj.offsetWidth)
	    / 2 + "px";
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
	btn.onclick = function()
		{
		removeCustomAlert();
		return false;
		}
	}

function removeCustomAlert()
	{
	console.log("call createCustomAlert()");
	document.getElementsByTagName("body")[0].removeChild(document
	    .getElementById("modalContainer"));
	parent.resizeaftersay();
	}

console.log("load htmlforms/utilsviwer.js");
