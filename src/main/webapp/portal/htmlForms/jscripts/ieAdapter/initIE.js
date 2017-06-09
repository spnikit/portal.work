

/**
 * !Все глобальные переменные стандартных и методы начальной инициализации
 * должны находиться здесь
 */

/**
 * @type DOMParser
 */
var parser = null;
if ('DOMParser' in window)
	parser = new DOMParser();
console.log(parser);

/**
 * тип браузера
 * 
 * @type String
 */
var browser = getBrowserName();

/**
 * ajax-клиент
 * 
 */
var XMLHttp = null;
/**
 * объект для печати DOM-узлов
 * 
 * @type XMLSerializer
 */
var serial;

/**
 * 
 * @type Document
 */
var xmlDoc;

/**
 * @type Element
 */
// var but = getElementFoFullSidRef("PAGE1.BUTTON1");
/**
 * @type Element
 */
// var custom =
// but.getElementsByTagNameNS("http://www.ibm.com/xmlns/prod/XFDL/Custom",
// "option").item(0);
// alert(serial.serializeToString(custom));
// alert(custom.getAttributeNS("http://www.ibm.com/xmlns/prod/XFDL/7.5",
// "compute"));
/*
 * function getXFDLForm() {
 * 
 * if (XMLHttp == null) { if (window.XMLHttpRequest) { try { XMLHttp = new
 * XMLHttpRequest();
 * 
 * XMLHttp.overrideMimeType('text/xml'); } catch (e) { } } else if
 * (window.ActiveXObject) { try { XMLHttp = new ActiveXObject("Msxml2.XMLHTTP"); }
 * catch (e) { try { XMLHttp = new ActiveXObject("Microsoft.XMLHTTP"); } catch
 * (e) { } } } } var thisPath = '' + window.location.pathname; var nameXFDL =
 * thisPath.substring(thisPath.lastIndexOf('/') + 1, thisPath
 * .indexOf(".html")); var urlForm = "http://xfdlToHTml/getSourceForm.php?form=" +
 * nameXFDL;
 * 
 * XMLHttp.open("GET", url, true); XMLHttp.onreadystatechange = endGetForm;
 * XMLHttp.send(null);
 * 
 * if (XMLHttp.readyState == 4) return XMLHttp.responseXML; }
 */

/**
 * 
 * возвращает имя браузера
 * 
 * @return {String} - имя браузера
 */
function getBrowserName() {
	if (navigator.userAgent.indexOf("Opera") != -1)
		return "Opera";
	else if (navigator.userAgent.indexOf("Firebird") != -1)
		return "Firebird";
	else if (navigator.userAgent.indexOf("K-Meleon") != -1)
		return "K-Meleon";
	else if (navigator.userAgent.indexOf("Phoenix") != -1)
		return "Phoenix";
	else if (navigator.userAgent.indexOf("Safari") != -1)
		return "Safari";
	else if (navigator.userAgent.indexOf("Lotus-Notes") != -1)
		return "Lotus-Notes";
	else if (navigator.userAgent.indexOf("Lynx") != -1)
		return "Lynx";
	else if (navigator.userAgent.indexOf("Crazy") != -1)
		return "Crazy";
	else if (navigator.userAgent.indexOf("Galeon") != -1)
		return "Galeon";
	else if (navigator.userAgent.indexOf("Flock") != -1)
		return "Flock";
	else if (navigator.userAgent.indexOf("MSIE") != -1)
		return "MSIE";
	else if (navigator.userAgent.indexOf("Navigator") != -1)
		return "Navigator";
	else if (navigator.userAgent.indexOf("Firefox") != -1)
		return "Firefox";
	else if (navigator.userAgent.indexOf("Konqueror") != -1)
		return "Konqueror";
	return "";
}

function getXMLTttp() {

	var tmpXMLHttp = null;
	if (XMLHttp == null) {
		if (window.XMLHttpRequest) {
			try {
				tmpXMLHttp = new XMLHttpRequest();

				tmpXMLHttp.overrideMimeType('text/xml');
			} catch (e) {
			}
		} else if (window.ActiveXObject) {
			try {
				tmpXMLHttp = new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
				try {
					tmpXMLHttp = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {
				}
			}
		}
		return tmpXMLHttp;
	} else {
		return XMLHttp;
	}

}

function endGetForm() {

}