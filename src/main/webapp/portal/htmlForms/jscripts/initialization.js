/**
 * @include "includes.js"
 */

/**
 * !Все глобальные переменные стандартных и методы начальной инициализации
 * должны находиться здесь
 */

var ieXmlDom = null;

/**
 * @type DOMParser
 */
var parser = null;

/**
 * тип браузера
 * 
 * @type String
 */
var browser = getBrowserName();

/**
 * тэг-заглушка для скриптов
 * 
 * @type Element
 */
var nullTeg = null;

/**
 * объект для печати DOM-узлов
 * 
 * @type XMLSerializer
 */
var serial;

/**
 * @type Element
 */
// var but = getElementFoFullSidRef("PAGE1.BUTTON1");

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
	else if (navigator.userAgent.indexOf("MSIE") != -1 || (navigator.userAgent.indexOf("Trident/7.0") != -1))
		return "MSIE";
	else if (navigator.userAgent.indexOf("Navigator") != -1)
		return "Navigator";
	else if (navigator.userAgent.indexOf("Firefox") != -1)
		return "Firefox";
	else if (navigator.userAgent.indexOf("Konqueror") != -1)
		return "Konqueror";
	return "";
}

/**
 * функция начальной инициализации <br/> <b>!Вся работа вьювера начинается
 * именно с неё</b>
 * 
 * @param {String|null}
 *            xfdlString - строка содержащая текущий xfdl'ник в виде строки
 */
function initXFDLViewer(xfdlString) {
	
    wgxpath.install();
    try {
        plug = new NpapiPlugin('aisa');
    } catch(e) {

    }
	
	/*if (!window.parent.document.applet_adapter || !window.parent.document.applet_adapter) {
		// alert('Ваш браузер не поддерживает java');
		// return false;
	}*/
	
	if (!('XPathEvaluator' in window)) XPathJS.bindDomLevel3XPath();
	
	if (xfdlString.substring(0, base64GZipHeader.length + 30).indexOf(base64GZipHeaderOld)!=-1){
		xfdlString = base64GZipHeader+xfdlString.replace(base64GZipHeaderOld,"");
	}
	if (xfdlString.substring(0, base64GZipHeader.length + 30).indexOf(base64GZipHeader)!=-1) {
		/*if (window.parent && window.parent.document.applet_adapter
				&& window.parent.document.applet_adapter.decodeFormBase64GZip) {
			xfdlString = window.parent.document.applet_adapter
					.decodeFormBase64GZip(xfdlString);
			if (!xfdlString) {
				var e = new Error();
				e.isMyErr = true;
				e.message = "Ошибка при раскодировании формы из base64-gzip представления";
				e.name = "Ошибка иниициализации xfdl-вьювера";
				throw e;
			}
		} else {
			var e = new Error();
			e.isMyErr = true;
			e.message = "Нет возможности раскодировать форму из base64-gzip представления";
			e.name = "Ошибка иниициализации xfdl-вьювера";
			throw e;
		}*/
		try {
			xfdlString = plug.FromGzBase64(xfdlString);
		} catch(err){
			var e = new Error();
			e.isMyErr = true;
			e.message = "Нет возможности раскодировать форму из base64-gzip представления";
			e.name = "Ошибка иниициализации xfdl-вьювера";
			throw e;
		}
	}

	$.fx.off = true;// отключаем всю анимацию jquery, оно здесь на хрен не надо
	$.datepicker.setDefaults($.extend($.datepicker.regional["ru"]));// меняем язык календаря на русский
	
	windowForSignManager = document.getElementById("windowForSignManager");
	$(windowForSignManager).dialog({
		resizable : false,
		position : 'center',
		closeOnEscape : false,
		draggable : false,
		title : 'Окно просмотра цифровых подписей',
		height : 300,
		width : 320,
		modal : true,
		autoOpen : false
	});

	windowForWarring = document.getElementById("windowForWarring");
	$(windowForWarring).dialog({
		resizable : false,
		position : 'center',
		closeOnEscape : false,
		draggable : false,
		title : 'ПРЕДУПРЕЖДЕНИЕ',
		text : 'Некоторые поля формы содержат недопустимые значения. Продолжить?',
		height : 150,
		width : 500,
		modal : true,
		autoOpen : false,
		buttons : {
			'Ok' : function() {
				$(this).dialog('close');

			},
			'Отмена' : function() {
				$(this).dialog('close');
			}
		}

	});

    windowInfo = document.getElementById("windowInfo");
    $(windowInfo).dialog({
        resizable : false,
        position : 'center',
        closeOnEscape : false,
        draggable : false,
        title : 'СКЗИ на данной машине недоступно',
        dialogClass: 'noTitleStuff',
        height : 100,
        width : 200,
        modal : true,
        autoOpen : false,
        buttons : {
            'Ok' : function() {
                $(this).dialog('close');

            }
        }
    });

	windowForMandatoryMes = document.getElementById('windowForMandatoryMes');
	// 'Выбор идентификационных данных для подписи';

	moment.lang('ru');
	XFDLViever = document.getElementById('viewer');
	createEventListeners();

	document.getElementsByTagName("body").item(0)
			.setAttribute("height", "100%");
	document.getElementsByTagName("body").item(0).setAttribute("width", "100%");

	dopDivSize = document.getElementById("dopDivSize");

	XFDLViever.setAttribute("height", "100%");
	XFDLViever.setAttribute("width", "100%");

	if (!parser && window.DOMParser) {
		parser = new DOMParser();
	}
	if (!serial && window.XMLSerializer)
		serial = new XMLSerializer();

	// TODO для IE<8 не работает
	if (browser == "MSIE" && false) {
		if (parser)// Потому что хреново работает DOMParser от microsoft
		{
			xfdlForm = parser.parseFromString(xfdlString, "text/xml");
			xfdlForm.normalize();
		} else {
			console.errDraw("Под MSIE < 8 нет поддержки");
			throw new Error("Под MSIE < 8 нет поддержки");
		}
    } else {
        xfdlForm = parser.parseFromString(xfdlString, "text/xml");
        xfdlForm.normalize();
        try {
//            ieXmlDom = new ActiveXObject('Microsoft.XMLDOM');
        } catch(e) {

        }
        if(ieXmlDom != null) {
            var xfdlNewString = new XMLSerializer().serializeToString(xfdlForm);

            var ns = new RegExp('xmlns=\"[^\"]+\"', 'g').exec(xfdlNewString)[0];
            xfdlNewString = xfdlNewString.replace(new RegExp('<XFDL[^>]+', 'g'), "<XFDL " + ns);

            // Вставляем CDATA, потому что иначе IE все пробелы слева и справа от значений убивает
            // (важно, когда нужно зафиксировать значения, состоящие из одних пробелов!)
            var startPoint;
            for(var i=0; i<xfdlNewString.length; i++) {
                if(xfdlNewString.substr(i, 1) == "<" && xfdlNewString.substr(i, 2) != "</") {
                    do {
                        i++;
                    } while(xfdlNewString.substr(i, 1) != ">");
                    if(xfdlNewString.substr(i-1, 2) != "/>") {
                        startPoint = ++i;
                        while(xfdlNewString.substr(i, 1) != "<") {
                            i++;
                        }
                        if(i != startPoint && xfdlNewString.substr(i, 2) == "</") {
                            xfdlNewString = xfdlNewString.substr(0, startPoint) + "<![CDATA[" + xfdlNewString.substr(startPoint, i-startPoint) + "]]>" + xfdlNewString.substr(i, xfdlNewString.length-i);
                            i = i + "<![CDATA[]]>".length;
                        }
                    }
                }
            }
            ieXmlDom.loadXML(xfdlNewString);
        }
        console.log("Документ отпарсен");
	}
	// TODO ошибки парсинга

	// 

	if (xfdlForm.documentElement.nodeName == "parsererror") {
		console.error("При парсинге xml-документа возникли ошибки");
		// var errStr = xfdlForm.documentElement.childNodes[0].nodeValue;
		// errStr = errStr.replace(/</g, "&lt;");
		// document.write(errStr);
		return;
	}

	/**
	 * ищет имя пространства имен, которому соответсвует заданный префикс
	 * 
	 * @type Function
	 * 
	 * @param {String}
	 *            nsPrefix - префикс пространства имен
	 * @return {String} URL пространства имен
	 */
	getNameSpaceForXfdlForm = function(nsPrefix) {
		/**
		 * 
		 * @type String
		 */
		var ns;

		while (true) {
			if (nsPrefix == "custom") {
				ns = "http://www.ibm.com/xmlns/prod/XFDL/Custom";
				break;
			}
			if (nsPrefix == "designer") {
				ns = "http://www.ibm.com/xmlns/prod/workplace/forms/designer/2.6";
				break;
			}
			if (nsPrefix == "ev") {
				ns = "http://www.w3.org/2001/xml-events";
				break;
			}
			if (nsPrefix == "xfdl") {
				ns = "http://www.ibm.com/xmlns/prod/XFDL/7.5";
				break;
			}
			if (nsPrefix == "xforms") {
				ns = "http://www.w3.org/2002/xforms";
				break;
			}
			if (nsPrefix == "xsd") {
				ns = "http://www.w3.org/2001/XMLSchema";
				break;
			}
			if (nsPrefix == "xsi") {
				ns = "http://www.w3.org/2001/XMLSchema-instance";
				break;
			}
			if (nsPrefix == "xhtml") {
				ns = "http://www.w3.org/2001/XMLSchema-instance";
				break;
			}
			if (nsPrefix == "soap") {
				ns = "http://schemas.xmlsoap.org/soap/envelope/";
				break;
			}
			if (nsPrefix == "asut") {
				ns = "http://etd.ocrv.rzd:8888/WAS/ETD/asutr";
				break;
			}
			if (nsPrefix == "p285") {
				ns = "http://etd.ocrv.rzd:8888/WAS/ETD/asutr";
				break;
			}
			if (nsPrefix == "defaultns") {
				ns = "http://rzd/util";
				break;
			}
			if (nsPrefix == "soapenv") {
				ns = "http://schemas.xmlsoap.org/soap/envelope/";
				break;
			}
			// ns = "http://www.ibm.com/xmlns/prod/XFDL/7.5";
			break;
		}
		return ns;
	};

	getNS = getNameSpaceForXfdlForm;
	getNSX = getNameSpaceForXfdlForm;
	// TODO xfdlForm.createNSResolver

	if (typeof XPathEvaluator != "undefined") {
		xpathEvaluator = new XPathEvaluator();
		// TODO что то там с подключаемой библиотекой для DOMParser не так
		xfdlNSResolver = xpathEvaluator
				.createNSResolver(xfdlForm.documentElement);
		xfdlNSResolver = getNS;
		xfdlNSResolver = {
			lookupNamespaceURI : getNameSpaceForXfdlForm
		};
	} else {
        xpathEvaluator = null;
        xfdlNSResolver = {
			lookupNamespaceURI : getNameSpaceForXfdlForm
		};
	}

	nullTeg = new EmulateNode("nullTeg", "", null);
	nullTeg.setValue = function() {};
	nullTeg.addScriptListener = function() {};

	redrawXFDL(xfdlForm);
}
