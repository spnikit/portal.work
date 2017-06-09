/**
 * 
 * 
 */

var nameXFDL;
var saveURL;
/**
 * 
 * @type Document
 */
var xfdlForm;
var addPathResource;

/**
 * 
 * @type String
 */
var xfdlStr = "";
var browser = "MSIE";
var xpathEvaluator;

var xfdlNSResolver = {
	lookupNamespaceURI	: function(nsPrefix)
		{
		/**
		 * 
		 * @type String
		 */
		var ns;
		
		while (true)
			{
			
			if (nsPrefix == "custom")
				{
				ns = "http://www.ibm.com/xmlns/prod/XFDL/Custom";
				break;
				}
			if (nsPrefix == "designer")
				{
				ns = "http://www.ibm.com/xmlns/prod/workplace/forms/designer/2.6";
				break;
				}
			if (nsPrefix == "ev")
				{
				ns = "http://www.w3.org/2001/xml-events";
				break;
				}
			if (nsPrefix == "xfdl")
				{
				ns = "http://www.ibm.com/xmlns/prod/XFDL/7.5";
				break;
				}
			if (nsPrefix == "xforms")
				{
				ns = "http://www.w3.org/2002/xforms";
				break;
				}
			if (nsPrefix == "xsd")
				{
				ns = "http://www.w3.org/2001/XMLSchema";
				break;
				}
			if (nsPrefix == "xsi")
				{
				ns = "http://www.w3.org/2001/XMLSchema-instance";
				break;
				}
			if (nsPrefix == "xhtml")
				{
				ns = "http://www.w3.org/2001/XMLSchema-instance";
				break;
				}
			if (nsPrefix == "soap")
				{
				ns = "http://schemas.xmlsoap.org/soap/envelope/";
				break;
				}
			if (nsPrefix == "asut")
				{
				ns = "http://etd.ocrv.rzd:8888/WAS/ETD/asutr";
				break;
				}
			if (nsPrefix == "p285")
				{
				ns = "http://etd.ocrv.rzd:8888/WAS/ETD/asutr";
				break;
				}
			if (nsPrefix == "defaultns")
				{
				ns = "http://rzd/util";
				break;
				}
			if (nsPrefix == "soapenv")
				{
				ns = "http://schemas.xmlsoap.org/soap/envelope/";
				break;
				}
			if (nsPrefix == "b")
				{
				ns = "b";
				break;
				}
			
			// ns = "http://www.ibm.com/xmlns/prod/XFDL/7.5";
			break;
			}
		return ns;
		}
};
(function()
	{
	isViewerDebug = true;
	
	/**
	 * ajax-клиент
	 * 
	 */
	var XMLHttp = getXMLTttp();
	
	var thisPath = '' + window.location.pathname;
	nameXFDL = thisPath.substring(thisPath.lastIndexOf('/') + 1, thisPath
	        .indexOf(".html"));
	if (nameXFDL.toLowerCase() == "testieapi")
		{
		nameXFDL = "testxml.xfdl";
		// nameXFDL = "testCombobox.xfdl";
		}
	
	/**
	 * url для запроса формы при помощи GET, где form='имя_формы'
	 * 
	 * @type String
	 */
	var urlForm = "http://xfdlToHTml/getSourceForm.php?form=" + nameXFDL;
	
	saveURL = "http://xfdlToHTml/saveForm.php";
	
	console.log("urlForm = " + urlForm);
	if (xfdlForm == null)
		{
		XMLHttp.open("GET", urlForm, false);
		XMLHttp.onreadystatechange = endGetForm;
		XMLHttp.send(null);
		// alert('3');
		if (XMLHttp.readyState == 4 && XMLHttp.status == 200)
			{
			xfdlStr = XMLHttp.responseText;
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
			return XMLHttp;
		
		}
	
	function endGetForm()
		{
		
		}
	
	addPathResource = "";
	if (!('XPathEvaluator' in window))
		XPathJS.bindDomLevel3XPath();
	xpathEvaluator = new XPathEvaluator();
	var domPars = new DOMParser();
	xfdlForm = domPars.parseFromString(xfdlStr, 'text/xml');
	xfdlForm.normalize();
	// console.dir(Node.prototype);
	var domSerial = new XMLSerializer();
	
	/**
	 * @type Node|Attr|Element
	 */
	var node1 = xfdlForm.xpath('string(//b:a)').stringValue;
	
	console.log('node1 =' + node1);
	
	/**
	 * @type Node|Attr|Element
	 */
	var node2 = xfdlForm.xpath('//b:b').iterateNext();
	
	// Object.defineProperty(node2, "zzz", {
	// value : null
	// writable : true,
	// enumerable : true,
	// configurable : true,
	/**
	 * @this Node|Attr|Element
	 */
	// get : function()
	// {
	// console.log(this);
	// return 'pr tc';
	// },
	// /**
	// * @this Node|Attr|Element
	// */
	// set : function(newVal)
	// {
	//			    	
	// this.aaa = newVal;
	// }
	// });
	console.log('node2 =' + node2);
	console.log('node2 =' + node2.textContent);
	
	//var a = {rrr:'asd'};
	//Object.defineProperty(a, 'zzz', {
		//    value	: 3
//	    });
	//console.log(a.zzz);
	})();
console.log('load testApi.js');