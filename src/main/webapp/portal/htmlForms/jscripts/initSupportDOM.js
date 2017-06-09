

var supportProps = {
	isDOMStandart : window.DOMParser ? true : false,
	isNativeXPath : (window.Document && window.Document.prototype.evaluate)
			? true
			: false

};