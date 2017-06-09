
/**
 * объект для хранения пары значений {значенние,метка}
 * 
 * @param {String}
 *          value - значение
 * @param {String}
 *          label - метка(совпадет со значение если отсутсвует либо пустая
 *          строка)
 * @constructor
 * @class
 */
function SelectItem(value, label)
	{
	/**
	 * значение
	 * 
	 * @type String
	 */
	this.value = value;
	
	/**
	 * метка
	 * 
	 * @type String
	 */
	this.label = null;
	
	if (label)
		this.label = label;
	else
		this.label = value;
		
	}