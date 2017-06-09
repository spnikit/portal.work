/**
 * Здесь храняться глобальные переменные необходимые для работы с самой xfdl-кой
 * 
 * 
 * @include "includes.js"
 * 
 * @see "coreXFDL.js"
 * 
 */

/**
 * 
 * массив всех связей xfdl с xform
 * 
 * @type {DuplexXToX[]}
 */
var massDuplexXtoX;

/**
 * массив всех скриптов. Упорядочен в порядке просмотра
 * 
 * @type {UnitScript[]}
 */
var massAllScripts = null;// new Array();

/**
 * массив содержащий все элементы типа label которые связаны с тэгами
 * data(лэйблы-картинки)
 * 
 * @type InfoElement[]
 */
var massLabelImage = null;

/**
 * @type Document
 */
var xfdlForm = null;

/**
 * ассоцитивный массив используемый для получения шаболнов по умолчанию для
 * записи в <b>xform</b>-узлы.<br/>
 * 
 * @constant
 * @type {Object}
 * 
 * @todo TODO доделать дургие шаблоны
 */
var defXFormPattern = {
	date	: "yyyy-MM-dd",
	time	: "hh:mm:ss"
	
};

/**
 * текущая страница, нужна чтобы перерисовке, отобразилась она же, а не первая
 * 
 * @type Integer
 */
var currentPage = 0;

/**
 * дерево для всех xforms - моделей
 * 
 * @type TreeXformsModels
 */
var treeXformsModels;

/**
 * @type {TreeInfoElements|null}
 */
var treeInfoEls = null;

/**
 * 
 * @type TreeInfoPages
 */
var treeInfoPages;

/**
 * 
 * @type TreeSignatures
 */
var treeSignatures;

/**
 * ссылка на &lt;global> из globalPage типа InfoElement
 * 
 * @type InfoGlobalElement
 */
var globalInfo;

/**
 * элемент на котором нажата мышь(нужен для реализации механизма переключения в свойстве
 * activated off->maybe->off->on->off)
 * 
 * @type InfoElement
 */
var infoElWithMouseDown = null;

/**
 * объект в котором находится полезная информация информация об xfdl'нике
 * 
 * @type Object
 */
var xfdlInfo = {

};

/**
 * Последний атрисованный элемент
 * 
 * @type 
 */
var lastDrawingItem = null;

/**
 * Объект состояния отрисовки
 * 
 * @type
 */
var objStateDrawing = {
	
	/**
	 * есть ли меню, определяется настройками в
	 * &lt;globalpage>-&lt;global>-&lt;ufv_settings>-&lt;menu>
	 * 
	 * @type Boolean
	 */
	isShowMenu	             : true,
	
	/**
	 * идет ли процесс отрисовки
	 * 
	 * @type Boolean
	 */
	isDraw	                 : false,
	/**
	 * текущий уровень вложенности от page
	 * 
	 * @type Number
	 *       @example
	 *       для тэга находящегося в page thisNestingLevel = 0; для тэга находящегося в pane и в table thisNestingLevel = 2
	 * @note вложенности типа &lt;xfroms:repeat> и &lt;xfroms:group> не считаются
	 */
	thisNestingDrawLevel	   : 0,
	/**
	 * каким образом производится стандартная компоновка(block или inline)
	 * 
	 * @note все элементы по умолчанию компонуются как inline, кроме
	 *       &lt;pane>,&lt;table> и контента внутри &lt;radiogroups> и
	 *       &lt;checkgroups>
	 * @note &lt;itemlocation> переопределяет компоновку через &lt;layoutflow>
	 * @type String[]
	 * @note The default block flow is applied to Table, Pane, and the contents of
	 *       Checkgroups, and Radiogroups. The default inline flow is applied to
	 *       all other items.
	 * 
	 */
	stackLayoutFlow	         : new Array(),
	
	// размер вычисляется так Размер_в_пикселях = Размер * 2 -3
	convertFontSize	         : {
		6		: 8,
		7		: 9,
		8		: 11,
		9		: 12,
		10	: 13,
		11	: 15,
		12	: 16,
		13	: 17,
		14	: 19,
		15	: 21,
		16	: 22,
		18	: 24,
		24	: 32,
		36	: 48,
		48	: 93
		
	},
	
	menuItems	               : {
		save	: 'on',
		print	: 'on'
	},
	numVisibleMenuItems	     : 2,
	
	/**
	 * ссылка на &lt;div>, в котором находится меню
	 * 
	 * @type HTMLElement
	 */
	menuDiv	                 : null,
	/**
	 * Цвет фона для нормально заполненного поля
	 * 
	 * @type String
	 */
	colorBackgroundNormal	   : '#ffffff',
	/**
	 * Цвет фона для поля требующего заполнения
	 * 
	 * @type String
	 */
	colorBackgroundMandatory	: '#ffffd0',
	/**
	 * Цвет фона для поля данные которого не соответсвуют шаблону
	 * 
	 * @type String
	 */
	colorBackgroundError	   : '#ff8080',
	
	/**
	 * текущая отрисовываемая страница
	 * 
	 * @type InfoPage
	 */
	thisDrawPage	           : null,
	
	/**
	 * ссылка на предыдущий отрисованный пункт(правила её формирования аналогичны
	 * тэгу <b>&lt;itemprevios></b>)
	 * 
	 * @type InfoElement
	 */
	itemPreviousInfo	       : null,
	
	/**
	 * ссылка на первый отрисованный пункт(правила её формирования аналогичны тэгу
	 * <b>&lt;itemfirst></b>)
	 * 
	 * @type InfoElement
	 */
	itemFirstInfo	           : null,
	/**
	 * ссылка на последний отрисованный пункт(правила её формирования аналогичны
	 * тэгу <b>&lt;itemlast></b>)
	 * 
	 * @type InfoElement
	 */
	itemLastInfo	           : null,
	
	/**
	 * info о текущем отрисовываемом элементе
	 * 
	 * @type InfoElement
	 */
	thisItemInfo	           : null,
	
	/**
	 * сбросить все переменные к исходным значениям
	 */
	reset	                   : function()
		{
		this.itemLastInfo = null;
		this.itemFirstInfo = null;
		this.itemPreviosInfo = null;
		this.thisDrawPage = null;
		
		this.isShowMenu = true;
		
		this.isDraw = false;
		
		this.thisNestingDrawLevel = 0;
		
		this.stackLayoutFlow = new Array();
		
		this.menuItems = {
			save	: 'on',
			print	: 'on'
		};
		this.numVisibleMenuItems = 2;
		
		this.menuDiv = null;
		
		this.colorBackgroundNormal = '#ffffff';
		
		this.colorBackgroundMandatory = '#ffffd0';
		
		this.colorBackgroundError = '#ff8080';
		}
	
}

/**
 * InfoElement для &lt;global> на текущей странице.<br/> В нем содержаться:<br/>
 * &#x9;элемент на котором находится фокус(опция <b>focusitem</b>)<br/> &#x9;
 * ...
 * 
 * @type {InfoElement}
 */
var thisGlobalForPage;

/**
 * все узлы xforms:instance
 * 
 * @type NodeList
 */
var xformsInstance;

/**
 * ассоцитивный массив используемый для получения шаболнов по умолчанию для
 * записи в <b>xfdl</b>-узлы.<br/>
 * 
 * @constant
 * @type {Object}
 * 
 * @todo TODO доделать дургие шаблоны
 */
var defXfdlPattern = {
	date	                 : "dd.MM.yy",
	'date-numeric'	       : "yyyyMMdd",
	'date-short'	         : "dd.MM.yy",
	'date-medium'	         : "dd.MM.yyyy",
	'date-long'	           : "d MMMM yyyy 'г.'",
	'date-full'	           : "d MMMM yyyy 'г.'",
	
	day_of_month	         : "d",
	'day_of_month-numeric'	: "d",
	'day_of_month-short'	 : "d",
	'day_of_month-medium'	 : "dd",
	'day_of_month-long'	   : "d",
	'day_of_month-full'	   : "d",
	
	day_of_week	           : "e",
	'	day_of_week-numeric'	: "e",
	'	day_of_week-short'	 : "e",
	'	day_of_week-medium'	 : "EEE",
	'	day_of_week-long'	   : "EEEE",
	'	day_of_week-full'	   : "EEEE",
	
	date_time	             : "dd.MM.yy H:mm",
	'date_time-numeric'	   : "yyyyMMdd H.mm",
	'date_time-short'	     : "dd.MM.yy H:mm",
	'date_time-medium'	   : "dd.MM.yyyy H:mm:ss",
	// 'date_time-long' : "",
	// 'date_time-full' : "",
	
	month	                 : "M",
	'month-numeric'	       : "M",
	'month-short'	         : "M",
	'month-medium'	       : "MMM",
	'month-long'	         : "MMMM",
	'month-full'	         : "MMMM",
	
	time	                 : "H.mm",
	'time-numeric'	       : "H.mm",
	'time-short'	         : "H.mm",
	'time-medium'	         : "H:mm:ss",
	// 'time-long' : "",
	// 'time-full' : "",
	
	year	                 : "yy",
	'year-numeric'	       : "yyyy",
	'year-short'	         : "yy",
	'year-medium'	         : "yyyy",
	'year-long'	           : "yyyy",
	'year-full'	           : "yyyy G",
	
	string	               : "[\s\S]*"// нет ограничений
	
};

/**
 * хранилище для пунктов требующих ввода
 * 
 * @type
 */
var mapMandatoryItems = {
	_massItems	   : {},
	_numberItem	   : 0,
	/**
	 * @param {InfoElement}
	 *          infoEl
	 */
	addItem	       : function(infoEl)
		{
		if (!this._massItems[infoEl.fullSidRef])
			{
			this._massItems[infoEl.fullSidRef] = infoEl;
			this._numberItem++;
			}
		},
	/**
	 * @param {InfoElement|String}
	 *          infoEl
	 */
	delItem	       : function(infoElOrFullSidRef)
		{
		var fullSidRef;
		if (typeof infoElOrFullSidRef != 'string')
			fullSidRef = infoElOrFullSidRef.fullSidRef;
		if (this._massItems[fullSidRef])
			{
			this._massItems[fullSidRef] = null;
			this._numberItem--;
			}
		},
	clear	         : function()
		{
		this._massItems = {};
		this._numberItem = 0;
		},
	getNumberItems	: function()
		{
		return this._numberItem;
		}
	
};

/**
 * @type XPathEvaluator
 */
var xpathEvaluator = null;
/**
 * @type XPathNSResolver
 */
var xfdlNSResolver = null;
/**
 * ассоциативный массив для хранения допустимых имен опций для xform-узлов.
 * Возвращает <b>true</b> если такая опция допустима, иначе вернет <b>undefined</b>
 * 
 * @type Object
 * @see список опций:<br/><b> {Boolean} constraint </b> - соответствует ли
 *      значение назначенным ограничениям <br/><b> {Boolean} readonly </b>-
 *      только для чтения(переопределяет аналгичную опцию у связанного пункта)
 *      <br/> <b> {Boolean} relevant</b> - скрывает связанный пункт(однако не
 *      может переопределить опции active и visible у связанного пункта)<br/>
 *      <b> {Boolean} requred </b> -требует ли поле ввода(отражается в связанном
 *      пункте) <br/> <b>{String} type </b>- тип значения(дополняет datatype в
 *      связанном пункте, т.е. введенное значение должно соответсвовать обоим
 *      шаблонам)<br/>
 */
var namesXformOpt = {
	constraint	: true,
	readonly	 : true,
	relevant	 : true,
	requred	   : true,
	type	     : true
	
};

/**
 * стэк функций для выполнения после отрисовки(выполняются один раз, после
 * отрисовки, но до выполнения xfdl-скриптов)
 * 
 * @type Function[];
 * @note функции должны быть без параметров
 */
var stackForActionsAfterRedraw = null;
