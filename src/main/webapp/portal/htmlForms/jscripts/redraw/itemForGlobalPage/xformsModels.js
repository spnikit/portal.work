/**
 * @include "../../includes.js"
 */

/**
 * объект, хранящий информацию о всех xform-моделях в виде их ассоативного
 * массива по id. АВТОМАТИЧЕСКИ ПАРСИТ ПЕРЕДАННЫЙ ЭЛЕМЕНТ И СОЗДАЕТ ИЗ НЕГО
 * ОБЪЕКТЫ XformsModel
 * 
 * 
 * @constructor
 * @class
 * 
 * @note дефолтная модель храниться по <b>id='_model+'</b>, при помощи этого id
 *       её можно получить но сам объект Xformsmodel в свойстве id не содержит
 *       данной строки. Символ '+' добавлен специально для того, чтобы не
 *       попалось таких id'шников которые совпадают с данным
 */
function TreeXformsModels(elXformsModels) {
	/**
	 * ссылка на тэг &lt;xformsmodels>
	 * 
	 * @type Element
	 */
	this.elXformsModels = elXformsModels;

	/**
	 * количество xform-моделей
	 * 
	 * @type Number
	 */
	this.numberXformsModels = 0;

	/**
	 * 
	 * @type Object
	 * @private
	 */
	this._massXformsModels = {};

	this.defaultXformModel = null;

	this._parseThisEl();

};
TreeXformsModels.prototype = {
	/**
	 * добавить информацию об элемента в структуру
	 * 
	 * @param {XformsModel}
	 *            info - объект хранящий информацию об xform-модели
	 * 
	 * @note если у info отсутсвует id то эта модель станет дефолтной
	 * @note дефолтная модель храниться по id='_model+', при помощи этого id её
	 *       можно получить но сам объект Xformsmodel в свойстве id не содержит
	 *       данной строки. Символ '+' добавлен специально для того, чтобы не
	 *       попалось таких id'шников которые совпадают с данным
	 */
	addXformsModel : function(xformsModel) {
		if (!xformsModel.id) {
			if (this.defaultXformModel)
				console.errDraw("Ошибка. Два тэга <xforms:model> без id ");
			else
				this.numberXformsModels++;
			this.defaultXformModel = xformsModel;
			this._massXformsModels['_model+'] = xformsModel;

		} else {
			if (this._massXformsModels[xformsModel.id]) {
				console.errDraw("Ошибка. Два тэга <xforms:model> c id = "
						+ xformsModel.id);
			} else {
				this.numberXformsModels++;
			}
			this._massXformsModels[xformsModel.id] = xformsModel;
		}

	},
	/**
	 * удалить информацию о модели из структуры
	 * 
	 * @param {String}
	 *            idXformsModel - id модели
	 * @note чтобы удалить дефолтное пространство используйте
	 *       idXformsModel="_model+"
	 */
	delXformsModel : function(idXformsModel) {
		if (this._massEls[idXformsModel]) {
			this.numberElems--;
			if (idXformsModel == '_model+')
				this.defaultXformModel = null
			this._massXformsModels[idXformsModel] = null;
		}
	},

	/**
	 * получить информацию об элементе из структуры
	 * 
	 * @param {String}
	 *            fullSidRef - id модели
	 * 
	 * @return {XformsModel} информацию о модели *
	 * @note дефолтная модель храниться по id='_model+', при помощи этого id её
	 *       можно получить но сам объект Xformsmodel в свойстве id не содержит
	 *       данной строки. Символ '+' добавлен специально для того, чтобы не
	 *       попалось таких id'шников которые совпадают с данным
	 */
	getXformsModel : function(/* XformsModel, */realRef) {
		if (this._massXformsModels[realRef]) {
			return this._massEls[realRef];
		}
		return null;
	},

	/**
	 * распарсить переданный элемент
	 * 
	 * @private
	 */
	_parseThisEl : function() {
		/**
		 * 
		 * @type Node|Element
		 */
		var childEl = null;

		childEl = this.elXformsModels.firstChild;
		while (childEl) {
			if (childEl.nodeType == Node.ELEMENT_NODE) {

				if (childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
					switch (childEl.tagName.replace(new RegExp('.+:','g'), '')) {
						case 'model' :
							var xformModel = new XformsModel(childEl);
							this.addXformsModel(xformModel);
							break;
						default :
							console.warnDraw("Для тэга <" + childEl.tagName
									+ "> нет распознания");
							break;
					}

				}
			}
			childEl = childEl.nextSibling;
		}

	}

};
TreeXformsModels.prototype.constructor = TreeXformsModels;

/**
 * класс-оболочка для работы с xform-моделями(&lt;xforms:model>). АВТОМАТИЧЕСКИ
 * ПАРСИТ ПЕРЕДАННЫЙ ЭЛЕМЕНТ
 * 
 * @constructor
 * @class
 * @param {Element}
 */
function XformsModel(el) {
	/**
	 * ссылка на тэг &lt;xforms:model>
	 * 
	 * @type Element
	 */
	this.elXformsModels = el;

	/**
	 * id связанного тэга &lt;xforms:model>
	 * 
	 * @type String|null
	 */

	this.id = el.getAttribute("id");

	/**
	 * дефолтная(если отсутствует аттрибут <b>id</b>) ли xforms-модель
	 * 
	 * @type Boolean
	 */
	this.isDefault = null;
	if (!this.id)
		this.isDefault = true;
	else
		this.isDefault = false;

	/**
	 * запущена ли цепочка вычислений xforms:bind
	 * 
	 * @type Boolean
	 */
	this.isRunUpdating = false;
	/**
	 * массив для хранения всех bind по их порядку
	 * 
	 * @type XformsBind[]
	 */
	this.massBind = new Array();
	/**
	 * асоциативный массив для доставания bind по id'шникам
	 * 
	 * @type
	 */
	this.massBindForId = {};

	/**
	 * массив для хранения всех Submission по их порядку
	 * 
	 * @type XformsSubmission[]
	 */
	this.massSubmission = new Array();
	/**
	 * асоциативный массив для доставания Submission по id'шникам
	 * 
	 * @type
	 */
	this.massSubmissionForId = {};

	/**
	 * асоциативный массив для доставания Instance по id'шникам
	 * 
	 * @type
	 */
	this.massInstance = {};

	// TODO доделать
	this._parseThisEl();
}

function XformsInstance(el) {
	this.id = el.getAttribute("id");
	this.getInstaceData = function(tag) {
		var childs = el.getElementsByTagName(tag);
		// var text = '<?xml version="1.0" encoding="utf-8"?>';
		// text += strFromXmlDom(childs[0])
		var text = '';
		if (typeof window.XMLSerializer != 'undefined') {
			var serializer = new XMLSerializer();
			text = serializer.serializeToString(childs[0]);
		} else {
			text = childs[0].xml;
		}
		return text;
	}
}

/*
 * function strFromXmlDom(el){ var attr = el.attributes; var text = '<'+el.tagName
 * if (attr.length>0){ } text += '>'; if (el.hasChildNodes()){ var childs =
 * el.childNodes; for (var i=0;i<childs.length;i++){ if (childs[i].nodeType ==
 * Node.ELEMENT_NODE) text += strFromXmlDom(childs[i],text); } } text += '</'+el.tagName+'>';
 * return text; }
 */

/**
 * распарсить переданный элемент
 * 
 * @private
 */
XformsModel.prototype._parseThisEl = function() {

	/**
	 * @type Node|Element
	 */
	var childEl = null;

	childEl = this.elXformsModels.firstChild;
	var stackBindRef = new Array(0);

	if (childEl)
		while (childEl != this.elXformsModels) {
			if (childEl.nodeType == Node.ELEMENT_NODE) {

				if (childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
					switch (childEl.tagName.replace(new RegExp('.+:', 'g'), '')) {
						case 'instance' :
							if (stackBindRef.length) break;
							var instanceInfo = new XformsInstance(childEl);
							this.massInstance[instanceInfo.id] = instanceInfo;

							// var allChild =
							// childEl.xpath("./child::*//@*|./child::*//*");
							/*
							 * var tmpNode = allChild.iterateNext(); while
							 * (tmpNode) { console.info('!!! ' +
							 * tmpNode.localName); //console.info(this);
							 * tmpNode.setParentXformsModel(this);// Добавить
							 * каждому // значимому узлу ссылку на // оболочку
							 * для его // xforms-модели tmpNode =
							 * allChild.iterateNext(); }
							 */
							break;

						case 'bind' :
							var parentBindId = stackBindRef.length
									? stackBindRef[stackBindRef.length - 1]
									: '';
							var bindInfo = new XformsBind(childEl, parentBindId, null, this, null);
							this.massBind.push(bindInfo);
							if (bindInfo.id)
								this.massBindForId[bindInfo.id] = bindInfo;
//							if (childEl.firstChild) {
//								childEl = childEl.firstChild;
//								stackBindRef.push(generateRealXRef(bindInfo.id,
//										parentBindId));
//							}
							break;

						case 'submission' :
							var submInfo = new XformsSubmission(childEl);
							this.massSubmission.push(submInfo);
							if (submInfo.id)
								this.massSubmissionForId[submInfo.id] = submInfo;
							break;

						default :
							console
									.warnDraw("Для тэга <"
											+ childEl.tagName
											+ "> внутри <xfroms:model> нет распознания");
							break;
					}

				}
			}
			if (childEl.nextSibling)
				childEl = childEl.nextSibling;
			else
			// TODO так можно хорошо пройти только bind
			{
				childEl = childEl.parentNode;
				if (stackBindRef.length)
					stackBindRef.pop();
			}
		}
	this.runUpdating(true);
}
/**
 * запустить обновления текущей модели, т.е. запустить вычисления в
 * &lt;xforms:bind/>
 * 
 * @param {Boolean}
 *            isNoSynhr - отменить синхронизацию с xfdl(используется только при
 *            начальной отрисовке)
 */
XformsModel.prototype.runUpdating = function runUpdating(isNoSynhr) {
	if (!isNoSynhr)
		isNoSynhr = false
	var i;
	for (i = 0; i < this.massBind.length; i++) {
		this.massBind[i].run(isNoSynhr);
	}
	for (i = 0; i < this.massBind.length; i++) {
		this.massBind[i]._isCycleRun = false;
	}
}

/**
 * @param {Element}
 *            el
 * @param {String}
 *            parentRef
 * @class
 * @constructor
 */
function XformsBind(el, parentNodeSetRef, keyNodeSet, XformsModel, keySetRef) {
	/**
	 * xpath-ссылка на множество узлов с которым будет производиться работа
	 * 
	 * @type String
	 */
	this.nodeSetRef = el.getAttribute("nodeset");
	if (parentNodeSetRef)
		this.nodeSetRef = generateRealXRef(this.nodeSetRef, parentNodeSetRef);

	var tempNodeSet;
    if(ieXmlDom != null) {
        var it = 0;
        tempNodeSet = ieXpathEvaluate(el, this.nodeSetRef);
    } else {
        if (keyNodeSet){
            tempNodeSet = el.xpath(keySetRef+'/'+this.nodeSetRef)
        } else {
            tempNodeSet = el.xpath(this.nodeSetRef);
        }
    }

//	if (keyNodeSet) {
//		console.log(keyNodeSet.xpath('./'+this.nodeSetRef).iterateNext());
//		console.log(el.xpath(keySetRef+'/'+this.nodeSetRef).iterateNext());
//	}
		
	this.id = el.getAttribute("id");
	this.childBinds = new Array();
	/**
	 * массив узлов с которым будет производиться работа
	 * 
	 * @type {Element[]|Attr[]|Node[]}
	 */
	this.nodeSet = new Array(0);
	/**
	 * @type Element|Attr
	 */
    var tmpEl;
    if(ieXmlDom != null) {
        tmpEl = tempNodeSet[it++];
    } else {
        tmpEl = tempNodeSet.iterateNext();
    }
	while (tmpEl) {
		if (this.type)
			tmpEl.setXformOption('type', this.type)
		this.nodeSet.push(tmpEl);
        if(ieXmlDom != null)
		    tmpEl = tempNodeSet[it++];
        else
            tmpEl = tempNodeSet.iterateNext();
	}
	
	var childEl = el.firstChild;
	while (childEl) {
		if (childEl.nodeType == Node.ELEMENT_NODE
			&& childEl.namespaceURI == getNameSpaceForXfdlForm("xforms")){
		    if (childEl.tagName.replace(new RegExp('.+:', 'g'), '') == "bind"){
		    	var bindInfo = new XformsBind(childEl, null, this.nodeSet[0], XformsModel, this.nodeSetRef);
		    	XformsModel.massBind.push(bindInfo);
		    	if (bindInfo.id) XformsModel.massBindForId[bindInfo.id] = bindInfo;
				this.childBinds.push(bindInfo);
		    }
		}
		childEl = childEl.nextSibling;
	}
	
	this.working = false;
	
	if (this.childBinds.length==0){
		this.pref = keyNodeSet;
		this.working = true;
		this.refCalculate = el.getAttribute("calculate");
		this.refConstraint = el.getAttribute("constraint");
		this.refReadonly = null;
		if (!this.refCalculate) this.refReadonly = el.getAttribute("readonly");
		this.refRelevant = el.getAttribute("relevant");
		this.refRequired = el.getAttribute("required");
		this.type = el.getAttribute("type");
		/**
		 * Запущены ли вычисления в текущем bind'е
		 * 
		 * @type Boolean
		 * @note сделано для исключения петель запуска bind'ов. Отиличие от
		 *       <b>_isCycleRun</b> в том что эта переменная уставливается
		 *       внутренне при запуске функции run.Нехорошо,но пока пусть будут обе.
		 * @private
		 */
		this._isRun = false;
		/**
		 * Запущены ли вычисления bind'ов в текущей xfomd-модели
		 * 
		 * @type Boolean
		 * @note сделано для исключения петель запуска bind'ов. Отиличие от
		 *       <b>_isRun</b> в том что эту переменную надо сбросить внешне из
		 *       родительского объекта оболочки для <xfroms:model>. Нехорошо,но пока
		 *       пусть будут обе.
		 * @private
		 */
		this._isCycleRun;
	}
}

/**
 * запустить вычисления из данного xforms:bind
 * 
 * @this {XformsBind}
 * @param {Boolean}
 *            isNoSynhr - отменить синхронизацию с xfdl(используется только при
 *            начальной отрисовке)
 * 
 * @note <b>ЮЗЕР.СБРОСЬ ПЕРЕМЕННУЮ _isCycleRun ПОСЛЕ ОТРАБОТКИ ЦИКЛА СКРИПТОВ</b>
 */
XformsBind.prototype.run = function(isNoSynhr) {

	if (this._isRun || this._isCycleRun || !this.working)
		return;
	console.logCall("call XformsBind.prototype.run(" + isNoSynhr + ")");
	console.group("Xform:bind");
	this._isRun = true;
	this._isCycleRun = true;
	var i;
	/**
	 * имена свойств которые надо синхронизировать
	 */
	var synhrProps = "";
	var val;
	for (i = 0; i < this.nodeSet.length; i++) {
		if (this.refCalculate) {
			var val;
			
			if (this.refCalculate.indexOf("if")==0){
				var calcstr = this.refCalculate.substring(3, this.refCalculate.length-1);
				var args = calcstr.split(',');
				var arg = xfdlForm.xpath(args[0],XPathResult.STRING_TYPE).stringValue;
				if (arg=='true'){
					val = (args[1].length>0)?xfdlForm.xpath(args[1],XPathResult.STRING_TYPE).stringValue:'';
				} else {
					val = (args[2].length>0)?xfdlForm.xpath(args[2],XPathResult.STRING_TYPE).stringValue:'';
				}
			} else {
				val = this.nodeSet[i].xpath(this.refCalculate,
					XPathResult.STRING_TYPE).stringValue;
			}
			
			if (val != this.nodeSet[i].getXformOption('calculate')) {
				this.nodeSet[i].setXformOption('calculate', val)
				if (this.nodeSet[i].setValue(val)){
					synhrProps += "value ";
				}
			}
			
		}
		if (this.refConstraint) {
			val = this.nodeSet[i].xpath(this.refConstraint,
					XPathResult.STRING_TYPE).stringValue;
			if (val != this.nodeSet[i].getXformOption('constraint')) {
				this.nodeSet[i].setXformOption('constraint', val)
				synhrProps += "constraint ";
			}
		}
		if (this.refRelevant) {
			val = this.nodeSet[i].xpath(this.refRelevan,
					XPathResult.STRING_TYPE).stringValue;
			if (val != this.nodeSet[i].getXformOption('relevant')) {
				this.nodeSet[i].setXformOption('relevant', val)
				synhrProps += "relevant ";
			}

		}
		if (this.refRequired) {
			val = this.nodeSet[i].xpath(this.refRequired,
					XPathResult.STRING_TYPE).stringValue;
			if (val != this.nodeSet[i].getXformOption('required')) {
				this.nodeSet[i].setXformOption('required', val)
				synhrProps += "required ";
			}
		}
		if (this.refReadonly) {
			val = this.nodeSet[i].xpath(this.refReadonly,
					XPathResult.STRING_TYPE).stringValue;
			if (val != this.nodeSet[i].getXformOption('readonly')) {
				this.nodeSet[i].setXformOption('readonly', val)
				synhrProps += "readonly ";
			}
		}
		if (isNoSynhr)
			synhrProps = '';
		if (synhrProps) {
			sinchrXtoX(this.nodeSet[i], null, synhrProps);
			// TODO здесь синхронизация
		}
	}
	console.groupEnd();
	this._isRun = false;
}

/**
 * конструтор оболочки для xforms:submission
 * 
 * @param {Element}
 *            el - элемент xforms:submission
 * 
 * @class
 * @constructor
 */
function XformsSubmission(el) {
	this.id = el.getAttribute("id");
	this.url = el.getAttribute("action");
	this.instance = el.getAttribute("instance");
	this.mediatype = el.getAttribute("mediatype");
	this.method = el.getAttribute("method");
	this.ref = el.getAttribute("ref");
	this.replace = el.getAttribute("replace");

	this.subDone = new Array();
	this.subErr = new Array();
	this.subBef = new Array();

	var childs = el.childNodes;
	for (var i = 0; i < childs.length; i++) {
		var child = childs[i];
		if (child.nodeType == Node.ELEMENT_NODE
				&& child.namespaceURI == getNameSpaceForXfdlForm("xforms"))
			parseAction(child, this.subDone, this.subErr, this.subBef, null);
	}
}

/**
 * Запускаем выполнение данного submission и лежащих внутри него элементов
 * (action)
 * 
 * @this {XformsSubmission}
 */
XformsSubmission.prototype.run = function() {
	var submiss = this;
	var data = replaceNewLine(serial.serializeToString(xfdlForm.xpath(submiss.ref).iterateNext()));
	for (var i=0;i<submiss.subBef.length;i++){
		submiss.subBef[i].work();
	}
	
	$.ajax({
		url : submiss.url,
		type : submiss.method,
		data: data,
		contentType : submiss.mediatype,
		async : true,
		timeout : 10000,
		complete : function(response, status) {
			if (status=='success'){
				if (submiss.replace=='instance'){
					//var XformsInstance = treeXformsModels.defaultXformModel.massInstance[submiss.instance];
					var instance = xfdlForm.xpath("instance('"+submiss.instance+"')").iterateNext();
					var par = instance.parentNode;
					par.replaceChild(response.responseXML.firstChild,instance);
				}
				for (var i=0;i<submiss.subDone.length;i++){
					submiss.subDone[i].work();
				}
				//redrawXFDL(xfdlForm);
			} else {
				for (var i=0;i<submiss.subErr.length;i++){
					submiss.subErr[i].work();
				}
			}
		}
	});
}

/**
 * пробегается по чайлдам submission, выдерает оттуда action и распихивает их по
 * массивам
 * 
 * @param {Element}
 *            child - соотвественно элемент над которым происходит действие
 * @param {Array}
 *            subDone - массив с action, которые будут отрабатываться при
 *            success
 * @param {Array}
 *            subErr - массив с action, которые будут отрабатываться при error
 * @param {Array}
 *            subBef - массив с action, которые будут отрабатываться перед
 *            запросом
 * @param {String}
 *            event - переменная для определения в какой массив запихивать
 *            данный action, используется только в случае если child является не
 *            конкретным action, а оболочкой для нескольких action
 */
function parseAction(child, subDone, subErr, subBef, event) {
	if (child.tagName.replace(/.+:/g, '') == "action") {
		if (child.childNodes.length > 0) {
			var subchilds = child.childNodes;
			for (var i = 0; i < subchilds.length; i++) {
				var subchild = subchilds[i];
				if (subchild.nodeType == Node.ELEMENT_NODE
						&& subchild.namespaceURI == getNameSpaceForXfdlForm("xforms")) {
					parseAction(subchild, subDone, subErr, subBef, child
									.getAttribute("ev:event"));
				}
			}
		}
	} else {
		var ev = (event == null) ? child.getAttribute("ev:event") : event;
		var xfscript = new xFormsScript(child.attributes, child.localName, null, false);
		switch (ev) {
			case "xforms-submit-done" :
				subDone.push(xfscript);
				break;
			case "xforms-submit-error" :
				subErr.push(xfscript);
				break;
			case "xforms-submit" :
				subBef.push(xfscript);
				break;
		}
	}
}
