/**
 * @include "includes.js"
 */
// -----
/**
 * 
 * @param {InfoElement}
 *            infoEl
 * @param {String}
 *            nameTeg
 * @param {Node}
 *            xformTeg
 * @constructor
 * @class
 */
function DuplexXtoX(infoEl, nameTeg, xformTeg, emulateNode) {
	/**
	 * @type InfoElement
	 */
	this.infoEl = infoEl;
	/**
	 * 
	 * @type
	 */
	this.nameTeg = nameTeg;
	/**
	 * 
	 * @type
	 */
	this.xformTeg = xformTeg;
	/**
	 * 
	 * @type EmulateNode
	 */
	this.emulateNode = emulateNode;

	/**
	 * идет ли синхронизация
	 * 
	 * @type Boolean
	 */
	this.isSynhronize = false;

	massDuplexXtoX.push(this);
}

/**
 * синхронизировать содержимое xform-узла с элементами ссылающимися на него
 * 
 * @param {Element|Node}
 *            xformNode - xform-узел
 * @param {InfoElement}
 *            thisInfoEl - элемент который не надо синхронизировать
 * @param {String}
 *            properties - свойства которые надо синхронизироватьв виде строки
 *            слов, разделенный пробелами(по умолчанию только "value")
 */
function sinchrXtoX(xformNode, thisInfoEl, properties) {
	// console.logCall('call sinchrXtoX() ');
	if (!properties)
		properties = "value";
	else
		properties = properties.trim();
	/**
	 * @type String[]
	 */
	var props = properties.split(/\s+/);
	/**
	 * 
	 * @type DuplexXtoX[]
	 */
	var massLinks = xformNode.getXformLinks();
	if (massLinks)
		for (var i = 0; i < massLinks.length; i++) {

			if (massLinks[i].infoEl != thisInfoEl) {
				if (massLinks[i].isSynhronize == false) {
					var j;
					for (j = 0; j < props.length; j++) {
						massLinks[i].isSynhronize = true;
						massLinks[i].infoEl.updateProrertyFromXForm(props[j]);
						massLinks[i].isSynhronize = false;
					}
				}
			}
		}

}
