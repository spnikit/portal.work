
/**
 * 
 * @include "includes.js"
 */

/**
 * инициализирование некторых глобальных переменных
 */
function globalInit()
	{
	//console.logCall("call globalInit()");
	browser = getBrowserName();
	//console.info("browser = " + browser);
	isMyDegug = false;
	isFormView = false;
	viewerIFrame = document.getElementById("viewerid");
	// по умолчанию LOTUS viewer
	useViewer = "IBM";
	isHV = 0;
	isUserTester = true;
	// useViewer = "AISA";
	
	// document.applet_adapter=document.applet;
	}

browser = getBrowserName();

var idInterval;

if (!console.trace)
	console.trace = function()
		{
		};

console.mytrace = function()
	{
	};
console.mytrace = console.trace;

console.logCall = function()
	{
	};

// console.logCall = console.log;

if (console.info)
	console.logCall = console.info;

/**
 * Ext.Viewport
 * 
 * @type
 */
var extViewport;

/**
 * 
 * возвращает имя браузера
 * 
 * @return {String} - имя браузера
 */
function getBrowserName()
	{
	if (navigator.userAgent.indexOf("Opera") != -1)
		return "Opera";
	else
		if (navigator.userAgent.indexOf("Firebird") != -1)
			return "Firebird";
		else
			if (navigator.userAgent.indexOf("K-Meleon") != -1)
				return "K-Meleon";
			else
				if (navigator.userAgent.indexOf("Phoenix") != -1)
					return "Phoenix";
				else
					if (navigator.userAgent.indexOf("Safari") != -1)
						return "Safari";
					else
						if (navigator.userAgent.indexOf("Lotus-Notes") != -1)
							return "Lotus-Notes";
						else
							if (navigator.userAgent.indexOf("Lynx") != -1)
								return "Lynx";
							else
								if (navigator.userAgent.indexOf("Crazy") != -1)
									return "Crazy";
								else
									if (navigator.userAgent.indexOf("Galeon") != -1)
										return "Galeon";
									else
										if (navigator.userAgent.indexOf("Flock") != -1)
											return "Flock";
										else
											if (navigator.userAgent.indexOf("MSIE") != -1)
												return "MSIE";
											else
												if (navigator.userAgent.indexOf("Navigator") != -1)
													return "Navigator";
												else
													if (navigator.userAgent.indexOf("Firefox") != -1)
														return "Firefox";
													else
														if (navigator.userAgent.indexOf("Konqueror") != -1)
															return "Konqueror";
	return "";
	}

function myDebug()
	{
	if (isMyDegug)
		try
			{
			console.log("\t=========Start==========");
			// console.trace();
			console.log("\tdocument.applet_adapter =" + document.applet_adapter);
			console.log("\ttypeof document.applet_adapter ="
			    + typeof document.applet_adapter);
			if (document.applet_adapter != undefined)
				{
				console.log("\tdocument.applet_adapter.getDocuments = "
				    + document.applet_adapter.getDocuments);
				console.log("\ttypeof document.applet_adapter.getDocuments = "
				    + typeof document.applet_adapter.getDocuments);
				}
			console.log("\t=========end===========");
			if (document.applet_adapter && !document.applet_adapter.getDocuments
			    && idInterval)
				clearInterval(idInterval);
			}
		catch (e)
			{
			console.warn(e.message);
			console.trace();
			isMyDegug = false;
			console.logCall = function()
				{
				};
			if (idInterval)
				clearInterval(idInterval);
			}
	}
var myDebug2 = myDebug;
function myDebug3()
	{
	try
		{
		console.debug("\t=========Start==========");
		console.debug("\tdocument.applet_adapter =" + document.applet_adapter);
		console.debug("\ttypeof document.applet_adapter ="
		    + typeof document.applet_adapter);
		if (document.applet_adapter != undefined)
			{
			console.debug("\tdocument.applet_adapter.getDocuments = "
			    + document.applet_adapter.getDocuments);
			console.debug("\ttypeof document.applet_adapter.getDocuments = "
			    + typeof document.applet_adapter.getDocuments);
			}
		console.debug("\t=========end===========");
		if (document.applet_adapter && !document.applet_adapter.getDocuments
		    && idInterval)
			clearInterval(idInterval);
		}
	catch (e)
		{
		console.warn(e.message);
		if (idInterval)
			clearInterval(idInterval);
		}
	}

console.log("load js/globalInit.js");
