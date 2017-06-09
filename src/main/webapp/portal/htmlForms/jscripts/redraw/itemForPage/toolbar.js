/**
 * @include "../../includes.js"
 */

/**
 * unUsed
 * 
 * @param {Element}
 *            el - отрисовываемый элемент
 * @param {String}
 *            parentId - родительский sid
 * @param {String}
 *            parentXformRef - родительский xformRef
 * @return {InfoElement} - полученный элемент
 */
function drawToolbar(el, parentId, parentXformRef) {
	var info = new InfoElement();
	var elemHTML = document.createElement("div");
	info.bounds = new Bounds();
	//var menuButtonHeight = objStateDrawing.isShowMenu ? 24 : 0;

	info.sid = el.getAttribute('sid');

	info.fullSidRef = parentId + '-' + info.sid;
	info.typeEl = 'toolbar';
	info.refElem = el;
	info.refHTMLElem = elemHTML;
	info.refHTMLElemDiv = elemHTML;
	treeInfoEls.addElemInfo(info);

	//var style = "position:fixed;";
	//style += "top:" + menuButtonHeight + "px;";
	var style = "position:relative;";
	style += "left:0px;";
	style += "width:100%;";
	var tmp1;
	if (tmp1 = el.xpath("./designer:height").iterateNext()) {
		style = style + "height:" + (info.height = ieXmlDom != null ? tmp1.text : tmp1.getValue()) + "px;";
	} else {
		info.height = 0;
		style = style + "height:-1px;";
	}
	style = style + "z-index: 999;";
	style = style + "background: #FFFFFF;";

	elemHTML.setAttribute("style", style);
	elemHTML.setAttribute("class", "toolbarStyle noPrint");

	// toolbarDiv.setAttribute('id',"TOOLBAR")
	// alert(toolbarDiv.Height);
	return info;
}