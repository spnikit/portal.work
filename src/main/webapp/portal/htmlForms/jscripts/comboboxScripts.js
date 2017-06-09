/**
 * @include "docs.js"
 */

/**
 * функция для скрытия/отображения элемента если он до этого был видим/невидим
 * 
 * @param el -
 *            элемент, который скрывается/отображается
 */
function resetVisible(el) {
	// console.trace();
	var styleDisplay;

	styleDisplay = ('' + el.style.display).toLowerCase();

	if ((styleDisplay > 'none') || (styleDisplay < 'none')) {
		el.style.display = 'none';
		return false;
	} else {
		el.style.display = '';
		return true;
	}

}

function setValueThisBlock(valueBlock, block) {

	el = block.children[0];
	el.value = valueBlock;

}
