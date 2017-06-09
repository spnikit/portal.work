/**
 * @include "ieInclude.js"
 */

/**
 * @type Element
 */
var tegA = xmlDoc.getElementsByTagName('a1')[0];
// console.log(tegA.namespaceURI);
// console.log(tegA.textContent);
/**
 * @type Element
 */
var tegB = xmlDoc.getElementsByTagName('b')[0];
function getXpathNS(prefix) {
	if (prefix == 'xhtml')
		return "http://www.w3.org/1999/xhtml";
	if (prefix != '')
		return prefix;
	else
		return '';
}

getXpathNS = {
	lookupNamespaceURI : function(prefix) {
		switch (prefix) {
			// resolve "x" prefix
			case 'xhtml' :
				return 'http://www.w3.org/1999/xhtml';
			case 'ns1' :
				return 'ns1';
			case 'ns2' :
				return 'ns2';
			default :
				return null;
		}
	}
}

// console.log(xmlDoc.evaluate("string(//a1)", xmlDoc.documentElement,
// getXpathNS,0, null).stringValue);

// console.log(xmlDoc.evaluate("//a1", xmlDoc.documentElement, getXpathNS,
// XPathResult.ANY_TYPE, null));

XPathJS.bindDomLevel3XPath();

/**
 * @constructor
 * @param {Element}
 *            n
 */

var xpathEv = null;
var resolver = null;
xpathEv = new XPathEvaluator();
resolver = xpathEv.createNSResolver(xmlDoc.documentElement);

console.dir(resolver);
console.dir(resolver.ns);
console.dir(resolver.lookupNamespaceURI);

if ('evaluate' in xpathEv)
	console.log("xml "
			+ xpathEv.evaluate("/ns1:root/ns1:b", xmlDoc, resolver,
					XPathResult.ANY_TYPE, null).iterateNext().tagName);

Element.prototype.a = function() {
	return 'ss';
}

// console.log(xmlDoc.getElementsByTagNameNS);
// console.log(document.getElementsByTagNameNS);
