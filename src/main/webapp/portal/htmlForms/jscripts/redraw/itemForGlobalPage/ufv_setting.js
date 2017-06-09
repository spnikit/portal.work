/**
 * @include "../../includes.js"
 */
/**
 * 
 * @param {Element}
 *            el
 */
function parseUfvSettings(el) {
	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = null;

	childEl = el.firstChild;

	while (childEl)// можно использовать любое из данных
	// условий
	{
		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

			switch (childEl.tagName.replace(new RegExp('.+:','g'), '')) {
				case 'menu' :
					parseXfdlMenu(childEl)
					break;
				default :
					console.warnDraw("Для тэга <" + childEl.tagName
							+ "> нет распознания");
					break;
			}

		}

		childEl = childEl.nextSibling;

	}

}

function parseXfdlMenu(el) {
	/**
	 * 
	 * @type Node|Element
	 */
	var childEl = null;

	childEl = el.firstChild;

	while (childEl)// можно использовать любое из данных
	// условий
	{

		if (childEl.nodeType == Node.ELEMENT_NODE
				&& childEl.namespaceURI == getNameSpaceForXfdlForm("xfdl")) {

            if(ieXmlDom != null) {
                if (childEl.tagName.replace(/.+:/g, '') == 'save' || childEl.tagName.replace(/.+:/g, '') == 'print') {
                    objStateDrawing.menuItems[childEl.tagName.replace(/.+:/g, '')] = (childEl
                        .text == 'hidden' || childEl.text == 'off')
                        ? childEl.text
                        : 'on';
                    if (objStateDrawing.menuItems[childEl.tagName.replace(/.+:/g, '')] == 'hidden')
                        objStateDrawing.numVisibleMenuItems--;
                }
                if (childEl.tagName.replace(/.+:/g, '') == 'visible' && childEl.text == 'off') {
                    objStateDrawing.numVisibleMenuItems = 0;
                    return;
                }
            } else {
                if (childEl.localName == 'save' || childEl.localName == 'print') {
                    objStateDrawing.menuItems[childEl.localName] = (childEl
                            .getValue() == 'hidden' || childEl.getValue() == 'off')
                            ? childEl.getValue()
                            : 'on';
                    if (objStateDrawing.menuItems[childEl.localName] == 'hidden')
                        objStateDrawing.numVisibleMenuItems--;
                }
                if (childEl.localName == 'visible' && childEl.getValue() == 'off') {
                    objStateDrawing.numVisibleMenuItems = 0;
                    return;
                }
            }
		}

		childEl = childEl.nextSibling;
	}
}

/**
 * нарисовать меню
 * 
 * @note читает данные из {@link objStateDrawing.menuItems}
 */
function drawXfdlMenu() {
	var tmpVal;
	if (objStateDrawing.numVisibleMenuItems) {

		/**
		 * сама полоска с кнопками
		 * 
		 * @type HTMLElement
		 */
		var xfdlMenuDiv = document.createElement("div");
		var bounds = "position:fixed;";
		bounds = bounds + "top:0px;";
		bounds = bounds + "left:0px;";
		bounds = bounds + "width:100%;";
		bounds = bounds + "height:22px;"
		bounds = bounds + "z-index: 999;";
		bounds = bounds + "background: #F0F0F0;";
		xfdlMenuDiv.setAttribute("style", bounds);
		xfdlMenuDiv.setAttribute("class", "noPrint");
		/**
		 * разделитель меню и самой формы
		 * 
		 * @type HTMLElement
		 */
		var delimiterXfdlMenu = document.createElement("div");
		bounds = "position:fixed;";
		bounds = bounds + "top:22px;";
		bounds = bounds + "left:0px;";
		bounds = bounds + "width:100%;";
		bounds = bounds + "height:2px;"
		bounds = bounds + "z-index: 999;";
		bounds = bounds + "background: #646464;";
		delimiterXfdlMenu.setAttribute("style", bounds);
//		delimiterXfdlMenu.setAttribute("class", "noPrint");

		XFDLViever.appendChild(xfdlMenuDiv);
		XFDLViever.appendChild(delimiterXfdlMenu)

		objStateDrawing.isShowMenu = true;

		tmpVal = objStateDrawing.menuItems['save'];
		var button;
		if (tmpVal != 'hidden') {
			/**
			 * @type HTMLButtonElement
			 */
			button = document.createElement("button");

			button
					.setAttribute(
							"style",
							"border:none;float:left;background-image: url('"
									+ addPathResource
									+ "./images/toolbar_save_button.jpg"
									+ "');background-position:center;background-repeat: no-repeat; width:22px; height:22px;");

			$(button).button({
						text : false
					});
			$(button).on('click', toolbar_save);
			button.alt = 'Сохранить';
			button.title = 'Сохранить';
			if (tmpVal == 'off')
				button.disabled = true;
			xfdlMenuDiv.appendChild(button);
			button = document.createElement("button");
			button
					.setAttribute(
							"style",
							"border:none;float:left;background-image: url('"
									+ addPathResource
									+ "./images/toolbar_save_as_button.jpg"
									+ "');background-position:center;background-repeat: no-repeat; width:22px; height:22px;");

			$(button).on('click', toolbar_save_as);
			/*
			 * $(button).button({ text : false })
			 */

			button.alt = 'Сохранить как';
			button.title = 'Сохранить как';
			if (tmpVal == 'off')
				button.disabled = true;
			xfdlMenuDiv.appendChild(button);
		}

		tmpVal = objStateDrawing.menuItems['print'];
		var button;
		if (tmpVal != 'hidden') {
			/**
			 * @type HTMLButtonElement
			 */
			button = document.createElement("button");
			button
					.setAttribute(
							"style",
							"border:none;float:left;background-image: url('"
									+ addPathResource
									+ "./images/toolbar_print_button.jpg"
									+ "');background-position:center;background-repeat: no-repeat; width:22px; height:22px;");
			// $(button).button({
			// text : false
			// })

			$(button).on('click', toolbar_print);
			button.alt = 'Печать формы';
			button.title = 'Печать формы';
			if (tmpVal == 'off')
				button.disabled = true;
			xfdlMenuDiv.appendChild(button);

		}

	} else {
		objStateDrawing.isShowMenu = false;
	}
}

/**
 * функция для втавки меню с кнопками печать, сохранить и т.д.
 * 
 * @param {Element}
 *            pageElement - ссылка на page xdfl-form
 * @param {HTMLElement}
 *            bodyEl - ссылка на элемент в который будет производится добавление
 *            новых(не обязательно body)
 * @return {Boolean} - если меню нету то вернет false
 * @deprecated
 */
function makeButtonMenu(pageElement, bodyEl) {
	var showButtons = false;

	var buttonMenuDiv = document.createElement("div");
	var bounds = "position:fixed;";
	bounds = bounds + "top:0px;";
	bounds = bounds + "left:0px;";
	bounds = bounds + "width:100%;";
	bounds = bounds + "height:22px;"
	bounds = bounds + "z-index: 999;";
	bounds = bounds + "background: #F0F0F0;";
	buttonMenuDiv.setAttribute("style", bounds);
	buttonMenuDiv.setAttribute("class", "noPrint");

	var buttonMenuTd = document.createElement("td");
	buttonMenuDiv
			.appendChild(document.createElement("table").appendChild(document
					.createElement("tr").appendChild(buttonMenuTd)));

	/**
	 * {Element}
	 */
	var menu = pageElement
			.xpath("//xfdl:globalpage/xfdl:global/xfdl:ufv_settings/xfdl:menu")
			.iterateNext();
	if (!menu)
		return showButtons;

	var currentEl = menu.firstChild;

	if (menu
			.xpath("count(xfdl:open) = number(0) or string(xfdl:open) != 'hidden'").booleanValue) {
		showButtons = true;
		var button = document.createElement("input");
		button.id = "toolbar_open";
		button.type = "image";
		button.alt = "Открыть";
		button.title = "Окрыть";
		button.onClick = "toolbar_open()";
		button.src = addPathResource + "./images/toolbar_open_button.jpg";
		button.setAttribute("style", "width:16px; height:16px;");
		buttonMenuTd.appendChild(button);
	}

	if (menu
			.xpath("count(xfdl:save) = number(0) or string(xfdl:save) != 'hidden'").booleanValue) {
		showButtons = true;
		/**
		 * @type Element
		 */
		var button = document.createElement("input");
		button.id = "toolbar_save";
		button.type = "image";
		button.alt = "Сохранить";
		button.title = "Сохранить";
		$(button).on("click", toolbar_save);

		button.src = addPathResource + "./images/toolbar_save_button.jpg";
		button.setAttribute("style", "width:16px; height:16px;");
		buttonMenuTd.appendChild(button);

		button = document.createElement("input");
		button.id = "toolbar_save_as";
		button.type = "image";
		button.alt = "Сохранить как";
		button.title = "Сохранить как";
		button.onClick = "toolbar_save_as()";
		button.src = addPathResource + "./images/toolbar_save_as_button.jpg";
		button.setAttribute("style", "width:16px; height:16px;");
		buttonMenuTd.appendChild(button);
	}

	if (menu
			.xpath("count(xfdl:mail) = number(0) or string(xfdl:mail) != 'hidden'").booleanValue) {
		showButtons = true;
		var button = document.createElement("input");
		button.id = "toolbar_send";
		button.type = "image";
		button.alt = "Отправить форму";
		button.title = "Отправить форму";
		button.onClick = "toolbar_send()";
		button.src = addPathResource + "./images/toolbar_send_button.jpg";
		button.setAttribute("style", "width:16px; height:16px;");
		buttonMenuTd.appendChild(button);
	}

	if (menu
			.xpath("count(xfdl:print) = number(0) or string(xfdl:print) != 'hidden'").booleanValue) {
		showButtons = true;
		var button = document.createElement("input");
		button.id = "toolbar_print";
		button.type = "image";
		button.alt = "Печать формы";
		button.title = "Печать формы";
		button.onClick = "toolbar_print()";
		button.src = addPathResource + "./images/toolbar_print_button.jpg";
		button.setAttribute("style", "width:16px; height:16px;");
		buttonMenuTd.appendChild(button);
	}

	if (!showButtons)
		return showButtons;

	bodyEl.appendChild(buttonMenuDiv);

	var buttonMenuDelimiterDiv = document.createElement("div");
	bounds = "position:fixed;";
	bounds = bounds + "top:22px;";
	bounds = bounds + "left:0px;";
	bounds = bounds + "width:100%;";
	bounds = bounds + "height:2px;"
	bounds = bounds + "z-index: 999;";
	bounds = bounds + "background: #646464;";
	buttonMenuDelimiterDiv.setAttribute("style", bounds);

	bodyEl.appendChild(buttonMenuDelimiterDiv);

	return showButtons;
}
