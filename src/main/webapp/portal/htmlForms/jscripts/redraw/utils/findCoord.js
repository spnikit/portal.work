/**
 * @include "../main.js"
 */
/**
 * !временная функция
 * 
 * @param {Element}
 *            el
 * @param {HTMLElement}
 *            htmlEl
 * @param {InfoElement}
 *            infoEl
 */
function findCoord(el, htmlEl, infoEl)
{

	htmlEl.style.position = "absolute";

	if(el.xpath("count(xfdl:itemlocation/xfdl:x)>0").booleanValue)
	{

		infoEl.x = el.xpath("number(./xfdl:itemlocation/xfdl:x)").numberValue;
		htmlEl.style.left = '' + infoEl.x + 'px';
		// console.log("infoEl.x = " + infoEl.x);
	}
	if(el.xpath("count(xfdl:itemlocation/xfdl:y)>0").booleanValue)
	{
		infoEl.y = el.xpath("number(xfdl:itemlocation/xfdl:y)").numberValue;
		htmlEl.style.top = '' + infoEl.y + 'px';
	}
	if(el.xpath("count(xfdl:itemlocation/xfdl:width)>0").booleanValue)
	{
		infoEl.width = el.xpath("number(xfdl:itemlocation/xfdl:width)").numberValue;
		htmlEl.style.width = '' + infoEl.width + 'px';
	}
	if(el.xpath("count(xfdl:itemlocation/xfdl:height)>0").booleanValue)
	{
		infoEl.height = el.xpath("number(xfdl:itemlocation/xfdl:height)").numberValue;
		htmlEl.style.height = '' + infoEl.height + 'px';
	}

}