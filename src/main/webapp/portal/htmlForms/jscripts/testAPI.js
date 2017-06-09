/**
 * файл для тестирования скриптов
 * 
 * @include "includes.js"
 * 
 */

(function()
	{
	
	isViewerDebug = true;
	
	// console.log(sc);
	// console.log(transXFDLScript.translateXfdlScript(sc));
	// console.log(xfdlWorkScript.getHour());
	// var rez = xfdlForm.xpath("//page");
	// console.log(xfdlForm.evaluate("/XFDL/xfdl:page",xfdlForm,getNameSpaceForXfdlForm,XPathResult.ANY_TYPE,null).iterateNext().namespaceURI);
	// redrawXFDL(xfdlForm);
	// console.log(xfdlForm.xpath("string(/xfdl:XFDL/xfdl:globalpage/xfdl:global/designer:date)"));
	// alert(xfdlForm.xpath("/xfdl:XFDL/xfdl:globalpage/xfdl:global/designer:date").snapshotLength);
	// dopDivSize = document.createElement("div");
	// dopDivSize.style.whiteSpace = "pre-wrap";
	// dopDivSize.style.wordWrap = "break-word";
	// document.body.appendChild(dopDivSize);
	//
	// dopDivSize.style.fontSize = 13 + "px;";
	// dopDivSize.style.fontName = "Verdana";
	// dopDivSize.style.minHeight = "1px";
	// dopDivSize.style.maxHeight = "30px";
	// dopDivSize.style.position = "absolute";
	// dopDivSize.style.height = "40px";
	// dopDivSize.textContent = "qqqqqqqqqqqq\neqweqw\n\nwewe";
	// result = dopDivSize.style.height;
	
	var thisPath = '' + window.location.pathname;
	nameXFDL = thisPath.substring(thisPath.lastIndexOf('/') + 1, thisPath
	        .indexOf(".html"));
	if (nameXFDL.toLowerCase() == "testapi"
	    || nameXFDL.toLowerCase() == "testdomie")
		{
			// nameXFDL = "testT.xfdl";
			// nameXFDL = "form.xfdl";
			// nameXFDL = "Poruchenie_3.46.3.xfdl";
			// nameXFDL = "script.xfdl";
			// nameXFDL = "Real(1).xfdl";
			
			// nameXFDL = "Reestr_3.46.2.xfdl";
			// nameXFDL = "Reestr_3.46.2 (2).xfdl";
			// nameXFDL = "PPoV_3.46.1.xfdl";
			// nameXFDL = "Akkreditiv_3.46.2.xfdl";
			
			// nameXFDL = "PU-13.20_3.46.9.xfdl";
			// nameXFDL = "PU-14g_3.46.8.xfdl";
			// nameXFDL = "EU-66_3.46.4.xfdl";
			
			// nameXFDL="PPrV_3.46.1.xfdl";
			// nameXFDL="EU-83_3.46.24.xfdl";
			// nameXFDL = "TU-174_3.46.57.xfdl";
			// nameXFDL = "testCombobox.xfdl";
			
			// nameXFDL = "testIE.xfdl";
			
			// nameXFDL = "nk_test.xfdl";
			// nameXFDL = "test_11000.xfdl";
			// nameXFDL = "form_temp.xfdl";
			// nameXFDL = "TU-174_3.46.57.xfdl";
			// nameXFDL = "PP_3.46.3.xfdl";
			// nameXFDL = "Poruchenie_3.46.3.xfdl";
			// nameXFDL = "DU-1.xfdl";
			// nameXFDL = "VU-14.xfdl";
			// nameXFDL = "Poruchenie_3.46.3.xfdl";
			// nameXFDL = "testTable.xfdl";
			// nameXFDL = "schet2.xfdl";
			// nameXFDL = "TORG-12_3.46.1.xfdl";
			// nameXFDL = "tmp2.xfdl";
			// nameXFDL = "simpleTest.xfdl";
			{// здесь оригиналы форм
			// nameXFDL = "Akkreditiv_3.46.2.xfdl";
			// nameXFDL = "IP_3.46.2.xfdl";
			// nameXFDL = "Poruchenie_3.46.3.xfdl";
			// nameXFDL = "PP_3.46.2.xfdl";
			// nameXFDL = "PP_3.46.3.xfdl";
			// nameXFDL = "PPoV_3.46.2.xfdl";
			// nameXFDL = "PPrV_3.46.2.xfdl";
			// nameXFDL = "PT_3.46.3.xfdl";
			// nameXFDL = "Reestr_3.46.2.xfdl";
			// nameXFDL = "Schet_facturaOriginal.xfdl";
			// nameXFDL = "Schet_po_zakazyOriginal.xfdl";
			// nameXFDL = "SVO_3.46.3.xfdl";
			// nameXFDL = "TORG-12_3.46.1.xfdl";
			// nameXFDL = "VLS_3.46.3.xfdl";
			// nameXFDL = "VPP_3.46.2.xfdl";//1234567890
			
			nameXFDL = "end/FORMS/ZA_3.46.3.xfdl";
			}
		// nameXFDL = "DU-1.xfdl";
		// nameXFDL = "script2.xfdl";
		// nameXFDL = "vavaka2.xfdl";
		// nameXFDL = "ZA_3.46.3.xfdl";
		// nameXFDL = "form.xfdl";
		// nameXFDL="TU133(1)_3.46.38.xfdl";
		
		// nameXFDL="1.xfdl";
		// nameXFDL="2.xfdl";
		// nameXFDL="3.xfdl";
		// nameXFDL="4.xfdl";
		// nameXFDL="5.xfdl";
		// nameXFDL="6.xfdl";
		// nameXFDL="7.xfdl";
		// nameXFDL="8.xfdl";
		// nameXFDL = "9.xfdl";
		// nameXFDL = 'signForm.xfdl';
		// nameXFDL = "Schet_po_zakazyOriginal.xfdl";
		// nameXFDL = "Schet_facturaOriginal.xfdl";
		// nameXFDL = 'TORG-12_3.46.1.xfdl';
		// nameXFDL = "testT1.xfdl";
		// nameXFDL = "testCombobox.xfdl";
		nameXFDL = "VPP_3.46.2.xfdl";
		//nameXFDL = "testImage2.xfdl";
		}
	
	/**
	 * url для запроса формы при помощи GET, где form='имя_формы'
	 * 
	 * @type String
	 */
	var urlForm = "http://xfdlToHTml/getSourceForm.php?form=" + nameXFDL;
	
	saveURL = "http://xfdlToHTml/saveForm.php";
	
	/**
	 * 
	 * @type String
	 */
	var xfdlStr = "";
	
	console.log("urlForm = " + urlForm);
	/**
	 * ajax-клиент
	 * 
	 */
	var XMLHttp = getXMLTttp();
	
	if (xfdlForm == null)
		{
		XMLHttp.open("GET", urlForm, false);
		XMLHttp.onreadystatechange = endGetForm;
		XMLHttp.send(null);
		// alert('3');
		if (XMLHttp.readyState == 4 && XMLHttp.status == 200)
			{
			xfdlStr = XMLHttp.responseText;
			// if (browser == "MSIE")
			// {
			// if (parser)
			// {
			// xfdlForm = parser.parseFromString(XMLHttp.responseText, "text/xml");
			// xfdlForm.normalize();
			// }
			// else
			// {
			// xfdlForm = new ActiveXObject("Microsoft.XMLDOM");
			// xfdlForm.async = false;
			// xfdlForm.loadXML(XMLHttp.responseText);
			// }
			//			
			// // console.log(xfdlForm)
			//			
			// }
			// else
			// {
			// xfdlForm = XMLHttp.responseXML;
			// xfdlForm.normalize();
			// }
			//		
			// var parser = new DOMParser();
			// xfdlForm = parser.parseFromString(XMLHttp.responseText, "text/xml");
			// xfdlForm.normalize();
			}
		else
			{
			
			}
		
		}
	
	/**
	 * 
	 * @returns {XMLHttpRequest}
	 */
	
	function getXMLTttp()
		{
		
		var tmpXMLHttp = null;
		if (XMLHttp == null)
			{
			if (window.XMLHttpRequest)
				{
				try
					{
					tmpXMLHttp = new XMLHttpRequest();
					
					tmpXMLHttp.overrideMimeType('text/xml');
					}
				catch (e)
					{
					}
				}
			else
				if (window.ActiveXObject)
					{
					try
						{
						tmpXMLHttp = new ActiveXObject("Msxml2.XMLHTTP");
						}
					catch (e)
						{
						try
							{
							tmpXMLHttp = new ActiveXObject("Microsoft.XMLHTTP");
							}
						catch (e)
							{
							}
						}
					}
			return tmpXMLHttp;
			}
		else
			{
			return XMLHttp;
			}
		
		}
	
	function endGetForm()
		{
		
		}
	
	addPathResource = "";
	initXFDLViewer(xfdlStr);
	//setTimeout('afterDrawXFDLForm();', 5000);
	//XFDLViever = document.body;
	//createEventListeners();
	})();

function afterDrawXFDLForm()
	{
	console.logCall('call afterDrawXFDLForm()');
	var dataEl = xfdlForm.createElementNS(getNSX('xfdl'), 'data')
	dataEl.setAttribute('sid', 'a342');
	
	var mimetypeEl = xfdlForm.createElementNS(getNSX('xfdl'), 'mimetype')
	mimetypeEl.setValue('image/jpeg');
	
	var filenameEl = xfdlForm.createElementNS(getNSX('xfdl'), 'filename')
	filenameEl.setValue('236.jpg');
	var mimedataEl = xfdlForm.createElementNS(getNSX('xfdl'), 'mimedata')
	mimedataEl.setAttribute("encoding", "base64");
	mimedataEl
	    .setValue("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");
	dataEl.appendChild(mimetypeEl);
	dataEl.appendChild(filenameEl);
	dataEl.appendChild(mimedataEl);
	xfdlForm.xpath("/xfdl:XFDL/xfdl:page").iterateNext().appendChild(dataEl);
	redrawXFDLElement(dataEl);
	
	};
	
	
	
	
	
console.log('load testApi.js');