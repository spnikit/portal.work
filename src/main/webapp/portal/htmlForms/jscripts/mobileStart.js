/**
 * файл для тестирования скриптов
 * 
 * @include "includes.js"
 * 
 */

(function() {
	isViewerDebug = true;
	isGlobalEdits = true;
	addPathResource = "";

	/**
	 * TODO именно в эту переменную надо запихнуть форму(несжатую)
	 * 
	 * @type String
	 */
	var xfdlStr = "";

	// Далее получение формы с серва 10.0.0.74. Это лишь пример
	{
		function endGetForm() {

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
		var thisPath = '' + window.location.pathname;
		nameXFDL = thisPath.substring(thisPath.lastIndexOf('/') + 1, thisPath
						.indexOf(".html"));
		if (nameXFDL.toLowerCase() == "testapi"
				|| nameXFDL.toLowerCase() == "testdomie"
				|| nameXFDL.toLowerCase() == "mobile") {
			nameXFDL = "VPP_3.46.2.xfdl";
		}

		/**
		 * url для запроса формы при помощи GET, где form='имя_формы'
		 * 
		 * @type String
		 */
		var urlForm = "http://" + window.location.hostname
				+ "/getSourceForm.php?form=" + nameXFDL;

		saveURL = "http://" + window.location.hostname + "/saveForm.php";

		console.log("urlForm = " + urlForm);
		/**
		 * ajax-клиент
		 * 
		 */
		var XMLHttp = getXMLTttp();

		if (xfdlForm == null) {
			XMLHttp.open("GET", urlForm, false);
			XMLHttp.onreadystatechange = endGetForm;
			XMLHttp.send(null);
			// alert('3');
			if (XMLHttp.readyState == 4 && XMLHttp.status == 200) {
				xfdlStr = XMLHttp.responseText;
			} else {

			}

		}

	}

	initXFDLViewer(xfdlStr);
	// setTimeout('afterDrawXFDLForm();', 5000);
	// XFDLViever = document.body;
	// createEventListeners();
})();

/**
 * 
 * @returns {XMLHttpRequest}
 */

function afterDrawXFDLForm() {
};
