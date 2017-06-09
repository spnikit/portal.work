/**
 * файл для тестирования рвботы скриптов в IE
 * 
 * @include "includes.js"
 * 
 */

(function() {

	XMLHttp = getXMLTttp();

	var thisPath = '' + window.location.pathname;

	var nameXML;

	nameXML = thisPath.substring(thisPath.lastIndexOf('/') + 1, thisPath
					.indexOf(".html"));
	if (nameXML.toLowerCase() == "testie") {
		nameXML = "iexml.xml";

	}
	/**
	 * url для запроса формы при помощи GET, где form='имя_формы'
	 * 
	 * @type String
	 */
	var urlForm = "http://xfdlToHTml/getSourceForm.php?form=" + nameXML;
	/**
	 * 
	 * @type String
	 */
	var xmlStr = "";

	console.log("urlForm = " + urlForm);
	XMLHttp.open("GET", urlForm, false);
	XMLHttp.onreadystatechange = endGetForm;
	XMLHttp.send(null);
	// alert('3');
	if (XMLHttp.readyState == 4 && XMLHttp.status == 200) {
		// xmlStr = XMLHttp.responseText;
		if (browser == "MSIE") {
			if (parser)// Потому что хреново работает DOMParser от microsoft
			{

				xmlDoc = parser.parseFromString(XMLHttp.responseText,
						"text/xml");
				xmlDoc.normalize();
				console.info("DOMParser");
			} else {
				xmlDoc = new ActiveXObject("Msxml2.DOMDocument.6.0");
				if (!xmlDoc)
					xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
				else
					console.info("Msxml2.DOMDocument.6.0");
				if (!xmlDoc) {
					xmlDoc = new ActiveXObject("Msxml.DOMDocument");
					console.info("Msxml.DOMDocument");
				} else
					console.info("Microsoft.XMLDOM");
				xmlDoc.async = false;
				xmlDoc.loadXML(XMLHttp.responseText);
			}

			// console.log(xmlDoc)

		} else {
			xmlDoc = XMLHttp.responseXML;
			xmlDoc.normalize();
		}
	}

	/**
	 * 
	 * @returns {XMLHttpRequest}
	 */

	console.log(xmlDoc);
	addPathResource = "";

})();

console.log('load ieAdapter/testFunc.js');