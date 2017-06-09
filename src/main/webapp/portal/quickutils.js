/**
 * @include "includes.js"
 */

function addViewer(containerDiv, tagID, _width, _height)
	{

	var _app = navigator.appName;
	var parentDiv = document.getElementById(containerDiv);
	
	
	var element = document.createElement('object');
	
	for (var i = 4; i < arguments.length; i += 2)
		{
		var param = document.createElement('param');
		param.setAttribute('name', arguments[i]);
		param.setAttribute('value', arguments[i + 1]);
		element.appendChild(param);
		}
	var style = "position: absolute;width:" + _width + ";height:" + _height
	    + ";left:0;top:0";
	element.setAttribute('id', tagID);
	element.setAttribute('style', style);
	
	element.setAttribute('height', _height);
	element.setAttribute('width', _width);
	
	if (_app == 'Microsoft Internet Explorer')
		{
		parentDiv.appendChild(element);
		element.setAttribute('classid',
		    'clsid:354913B2-7190-49C0-944B-1507C9125367');
		}
	else
		{
		element.setAttribute('type', 'application/vnd.xfdl');
		parentDiv.appendChild(element);
		}
	}

function gup(name)
	{
//	console.logCall("call quickutils/gup()");
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null)
		return "";
	else
		return results[1];
	}

var objectID = "obj";
var appletID = "applet";

function deletecookies()
	{
//	console.logCall("call quickutils/deletecookies()");
	Delete_Cookie("quickcookie");
	Delete_Cookie("quickdocid");
	Delete_Cookie("quickcertserial");
	}

function set_cookie(name, value, exp_y, exp_m, exp_d, path, domain, secure)
	{
//	console.logCall("call quickutils/set_cookie()");
	var cookie_string = name + "=" + escape(value);
	
	if (exp_y)
		{
		var expires = new Date(exp_y, exp_m, exp_d);
		cookie_string += "; expires=" + expires.toGMTString();
		}
	
	if (path)
		cookie_string += "; path=" + escape(path);
	
	if (domain)
		cookie_string += "; domain=" + escape(domain);
	
	if (secure)
		cookie_string += "; secure";
	
	document.cookie = cookie_string;
	}

function Get_Cookie(name)
	{
//	console.logCall("call quickutils/Get_Cookie()");
	var start = document.cookie.indexOf(name + "=");
	var len = start + name.length + 1;
	if ((!start) && (name != document.cookie.substring(0, name.length)))
		{
		return null;
		}
	if (start == -1)
		return null;
	var end = document.cookie.indexOf(";", len);
	if (end == -1)
		end = document.cookie.length;
	return unescape(document.cookie.substring(len, end));
	}

function Delete_Cookie(name, path, domain)
	{
//	console.logCall("call quickutils/Delete_Cookie()");
	if (Get_Cookie(name))
		document.cookie = name + "=" + ((path) ? ";path=" + path : "")
		    + ((domain) ? ";domain=" + domain : "")
		    + ";expires=Thu, 01-Jan-1970 00:00:01 GMT";
	}

function ResizeAppletSmall()
	{
	document.getElementById('viewerid').className='';
	document.getElementById('viewerid').height=document.body.clientHeight-2; 
//	document.getElementById('viewerid').height = "100%";
	}

/*function OpenDoc()
{
	alert("quick utils in new openDoc");
	var docid =document.applet_adapter.getDocid();
	var certserial = document.applet_adapter.getCertSer();
	ETD.setTemp(docid, certserial);	
	var xfdl=document.applet_adapter.getXmldata();	
	isHV = document.applet_adapter.getIsHV();

	switch (isHV){
	case 0:
		useViewer = "IBM";
		break;
	case 1:
		useViewer = "AISA";
		break;
	}
	
if (useViewer=="IBM"){
	ResizeAppletSmall();	
	//document.getElementById("tableForViewer").style.zIndex = "2";
	//document.getElementById("tableForViewer").focus();
		viewername.CreateOpenViewer(xfdl);
}
if (useViewer=="AISA"){
	newFormWindow(xfdl);
}
}*/

function OpenDoc()
	{
	console.log("in quickUtills openDoc");
	console.logCall("call quickutils/OpenDoc()");
	set_cookie("quickcookie", "qc");
	/*
	set_cookie("quickdocid", document.applet_adapter.getDocid());
	set_cookie("quickcertserial", document.applet_adapter.getCertSer());*/
	ResizeAppletSmall();
	var xfdl = document.applet_adapter.getXmldata();
	isHV = document.applet_adapter.getIsHV();
	
	
//	console.info("isHV = " + isHV);
	
	// TODO сделать обратное скрытие таблицы,содержащей вьювер, при закрытии
	// формы(сделано)
	// document.getElementById("tableForViewer").style.zIndex = "1";
	
	//**
	 //* какой вьювер использовался до. Нужно для того чтобы не перезагружать
	 //* страницу со вьювером
	 //* 
	 //* @type String
	 //*//*
	var oldViewer = viewerIFrame.getAttribute("src");
	
	//*//**
	// * флаг смены вьювера
	// * 
///	 * @type Boolean
	// *//*
	var flag = false;
	// TODO здесь должно определяться как будет отрисовываться документ
	
	switch (isHV)
		{
		case 0 :
			useViewer = "IBM";
			break;
		case 1 :
			if ('DOMParser' in window)
				{
				useViewer = "AISA";
				}
			else
				{
				useViewer = "IBM";
				
				}
			break;
		case 2 :
			if (isUserTester)
				{
				if ('DOMParser' in window)
					{
					useViewer = "AISA";
					}
				else
					{
					useViewer = "IBM";
					}
				}
			else
				{
				useViewer = "IBM";
				}
			break;
		default :
			break;
		
		}
	switch (useViewer)
		{
		case "AISA" :
			{
			if (!oldViewer != "aisaViewer.html")
				{
				viewerIFrame.removeAttribute("src");
				
				viewerIFrame.setAttribute("src", "aisaViewer.html");
				flag = true;
				}
			}
			break;
		case "IBM" :
			{
			if (oldViewer != "viewer.html")
				{

				viewerIFrame.removeAttribute("src");
				viewerIFrame.setAttribute("src", "viewer.html");
				// viewerIFrame.setAttribute("name", "viewername");
				flag = true;
				
				}

			break;
			}
		default :
			throw new Error("Неопределено каким средством открывать форму");
			
		}
	
//	console.info("flag = " + flag);
	console.log("flag ",flag);
	if (flag)
		{

		viewerIFrame.onload = function()// ждем пока iframe перенастроиться
			{
			viewerIFrame.onload = null;
			// console.log(new XMLSerializer().serializeToString(viewerIFrame));
			if (!isFormView)
				{
				// console.info('1');
				isFormView = true;
				
				 viewername.CreateOpenViewer(xfdl);
				 
				//viewerIFrame.contentWindow.CreateOpenViewer(xfdl);
				}
			else
				{
//				console.warn("флаг isFormView не был сброшен")
				 viewername.CreateOpenViewer(xfdl);
				 
				//viewerIFrame.contentWindow.CreateOpenViewer(xfdl);
				// console.info('2');
				}
			
			}
		}
	else
		{
		isFormView = true;
		// viewername.CreateOpenViewer(xfdl);;
		viewerIFrame.contentWindow.CreateOpenViewer(xfdl,true);
		console.log("viewerIFrame.contentWindow.CreateOpenViewer(xfdl);");
		}
	//ETD.unlockDoc(document.applet_adapter.getDocid(), document.applet_adapter.getCertSer());
	}

function doRedirect(str)
	{
//	console.logCall("call quickutils/doRedirect)");
//	alert(str);
	viewername.CreateOpenViewer(str);
	}

function Refresh()
	{
//	console.logCall("call quickutils/Refresh()");
	window.location.href = window.location.href;
	}

var show_news = function()
	{
//	console.logCall("call quickutils/show_news()");
	var hasnews = document.applet_adapter.HasNews();
	if (!hasnews)
		return;
	var message = document.applet_adapter.GetNews(); // сюда кидаем сообщение
	// сервера
	on_ok = function()
		{
		win_news.destroy();
		document.applet_adapter.DeleteNews();
		}
	
	var text_data = new Ext.form.Label({
		    text	: message
	    });
	
	var win_news = new Ext.Window({
		    title		    : 'Сообщение:',
		    layout		  : 'fit',
		    width		    : 400,
		    border		  : false,
		    hideBorders	: true,
		    style		    : 'text-align:center;',
		    resizable		: false,
		    buttonAlign	: 'center',
		    closable		: false,
		    modal		    : true,
		    items		    : text_data,
		    buttons		  : [{
			        text		: 'OK',
			        handler	: on_ok
		        }]
	    });
	
	win_news.show();
	}

/**
 * вызывается после нажатия кнопки "Сохранить и выйти"
 */
function resizeLarge(saveParam)
	{

	
	document.applet_adapter.saveDoc("1");
	
//	deletecookies();
//	saveParam=''+saveParam;
//	var saveresp = document.applet_adapter.saveDoc('1',saveParam);
//	json = eval('(' + document.applet_adapter.doaftersave(saveresp) + ')');
//	document.getElementById('viewerid').height = 0;
//	
//	show_news();
	document.getElementById('ret').appendChild(document.createTextNode("Документ сохранен"));
	document.getElementById('ret').style.top = document.body.clientHeight / 2
	    - 15 + 'px';
	ETD.unlockDoc(document.applet_adapter.getDocid(), document.applet_adapter.getCertSer());
	}
function endStage()
	{
	resizeLarge('endStage');
	}

function resizeaftersay()
	{
	document.applet_adapter.doaftersave();
	document.getElementById('viewerid').height = 0;
	document.getElementById('tr2'), height = "0%";
	document.getElementById('tr1').height = "100%";

	}

/**
 * вызывается после нажатия кнопки "Отмена"
 */
function resizeLargeAfterCancel()
	{

//	deletecookies();
//	json = eval('(' + document.applet_adapter.doaftersave("Документ закрыт")   + ')');
//	show_news();
	document.getElementById('viewerid').height = 0;
	document.getElementById('ret').appendChild(document.createTextNode("Документ закрыт"));
	document.getElementById('ret').style.top = document.body.clientHeight / 2
	    - 15 + 'px';
	ETD.unlockDoc(document.applet_adapter.getDocid(), document.applet_adapter.getCertSer());
	}

function saveandnoexit()
	{
//	console.logCall("call quickutils/saveandnoexit()");
	var saveresp = document.applet_adapter.saveDoc('0');
//	console.info("saveresp = " + saveresp);
	if (saveresp != 'Документ сохранен успешно')
//		alert(saveresp);
		ETD.unlockDoc(document.applet_adapter.getDocid(), document.applet_adapter.getCertSer());
	return saveresp;
	}
