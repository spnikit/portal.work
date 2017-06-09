/**
 * @include "coreXFDL.js"
 * @include "includes.js"
 */

// function emulateWindowOnLoad()
// document.onload =;
// document.addEventListener('load',documentLoad,true);
/**
 * функция вызываемая для добавления слушателей действий
 */
// function documentLoad()
function createEventListeners() {

	/**
	 * @type HTMLElement
	 */
	document.thisFocusEl = null;
	/**
	 * @type HTMLElement
	 */
	document.previousFocusEl = null;
	// console.log("bo");
	$(XFDLViever).on("change", 'input, textarea,select',
			function(/* Event */je) {
				// console.logCall("call onChange()");

				/**
				 * @type HTMLElement
				 */
				var target = je.target;

				if (UtilsHTMLEl.isLinkXFDL(target)) {

					// console.log("yes!!");
					var val;
					if (target.tagName.replace(/.+:/g, '') == "input") {
						if (target.type == "checkbox") {

							val = target.checked ? "on" : "off";

						} else
							val = target.value;
					} else {
						val = target.value;
					}
					updateHTMLtoXFDL(target, 'value', val);
				}

			});
	// console.log("bo1");
	$(XFDLViever).on("focusin", '*', function(/* Event */je) {

		// console.log("yes1");
		/**
		 * @type HTMLElement
		 */
		var target = je.target;
		// console.log("Фокус на " + target);
		document.thisFocusEl = target;
		// TODO: скоректировать фокус для элементов которые находятся
		// внутри &lt;pane> и &lt;table>

		if (UtilsHTMLEl.isLinkXFDL(target))
			thisGlobalForPage.setProperty("focuseditem", UtilsHTMLEl
							.getInfoElRef(target).sid);
		return true;
	});
	// console.log("bo2");
	$(XFDLViever).on("focusout", '*', function(/* Event */je) {
				var e = je.originalEvent;
				// console.log("yes2");
				/**
				 * @type HTMLElement
				 */
				var target = je.target;

				// console.log("Фокус потерян с " + target);
				if (UtilsHTMLEl.isLinkXFDL(target)) {
					document.previousFocusEl = target;
					document.thisFocusEl = null;
				}
				return true;
			});

	$(XFDLViever).on("mousedown", '*', function(/* MouseEvent */je) {
				var e = je.originalEvent;
				// console.log("yes2");
				/**
				 * @type HTMLElement
				 */
				var target = je.target;
				// console.log("Нажата левая кнопка мыши на " + target);
				/**
				 * @type InfoElement
				 */
				var infoEl = UtilsHTMLEl.getInfoElRef(target);
				if (infoEl && infoEl.typeEl == "button" && e.which == 1) {

					infoEl.setProperty("activated", "maybe");
					infoElWithMouseDown = infoEl;
				}

				return true;
			});
	$(XFDLViever).on("mouseup", '*', function(/* MouseEvent */je) {

				var e = je.originalEvent;
				// console.log("yes2");
				/**
				 * @type HTMLElement
				 */
				var target = je.target;
				// console.log("Отпущена левая кнопка мыши с" + target);
				/**
				 * @type InfoElement
				 */
				var infoEl = UtilsHTMLEl.getInfoElRef(target);

				if (infoEl && infoEl.typeEl == "button" && e.which == 1) {
					if (infoEl == infoElWithMouseDown)
						infoEl.setProperty("activated", "off");
					infoElWithMouseDown = null;
				}

				return true;
			});

	$(XFDLViever).on("click", 'button', function(/* MouseEvent */je) {
				var e = je.originalEvent;
				// console.log("yes2");
				/**
				 * @type HTMLElement
				 */
				var target = je.target;
				// console.log("Нажата левая кнопка мыши на " + target);
				/**
				 * @type InfoElement
				 */
				var infoEl = UtilsHTMLEl.getInfoElRef(target);
				if (infoEl && infoEl.typeEl == "button") {
					infoEl.setProperty("activated", "on");
					infoEl.setProperty("activated", "off");
				}

				return true;
			});

	// TODO on() почему то не работает, вероятно баг jquery
	$(XFDLViever).live("mouseover", '*', function(/* MouseEvent */je) {

		// console.log("event mouseover()");
		/**
		 * @type HTMLElement
		 */
		var target = je.target;

		/**
		 * @type InfoElement
		 */
		var infoEl = UtilsHTMLEl.getInfoElRef(target);

		if (infoEl && infoEl.invalidState) {
			UtilsHTMLEl.setValue(windowForMandatoryMes,
					infoEl.formatObj.crSetting.message
							|| 'Поле заполнено неверно');
			var offsetPos = $(target).offset();
			var offsetPosTop = offsetPos.top;
			var offsetPosLeft = offsetPos.left;

			windowForMandatoryMes.style.top = (offsetPosTop + infoEl.height + 3)
					+ 'px';
			windowForMandatoryMes.style.left = (offsetPosLeft) + 'px';
			windowForMandatoryMes.style.display = "block";
		}

		return true;
	}, false);
	$(XFDLViever).on("mouseout", '*', function(/* MouseEvent */je) {
				// console.log("event mouseout()");
				/**
				 * @type HTMLElement
				 */
				var target = je.target;

				/**
				 * @type InfoElement
				 */
				var infoEl = UtilsHTMLEl.getInfoElRef(target);

				if (infoEl) {
					if (infoEl == infoElWithMouseDown)
						infoEl.setProperty("activated", "off");
					infoElWithMouseDown = null;
					windowForMandatoryMes.style.display = "none";

				}

				return true;
			});

	// console.log("bo3");
};

/**
 * ищет имя пространства имен, которому соответсвует заданный префикс
 * 
 * @type Function
 * 
 * @param {String}
 *            nsPrefix - префикс пространства имен
 * @return {String} URL пространства имен
 */
var getNameSpaceForHTML;

if (document.createNSResolver) {
	getNameSpaceForHTMLForm = document
			.createNSResolver(document.ownerDocument == null
					? document.documentElement
					: document.ownerDocument.documentElement)
} else {
	/**
	 * ищет имя пространства имен, которому соответсвует заданный префикс
	 * 
	 * @type Function
	 * 
	 * @param {String}
	 *            nsPrefix - префикс пространства имен
	 * @return {String} URL пространства имен
	 */
	getNameSpaceForHTMLForm = function(nsPrefix) {
		/**
		 * 
		 * @type String
		 */
		var ns;

		while (true) {
			if (nsPrefix == "xhtml") {
				ns = "http://www.w3.org/1999/xhtml";
				break;
			}

			ns = "http://www.w3.org/1999/xhtml";
			break;
		}
		return ns;
	}
}
/**
 * сокращение для getNameSpaceForHTML
 * 
 * @type Function
 */
var getNSH;
getNSH = getNameSpaceForHTML;
