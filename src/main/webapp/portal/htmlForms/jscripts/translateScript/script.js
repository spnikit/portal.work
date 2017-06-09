/**
 * @include "../../includes.js"
 */


/**
 * Ассоциативный массив со скриптами xForms
 * 
 * @type array
 */
var xFormsScriptMass = {};

/**
 * Добавление элемента в ассоциативный массив со скриптами xForms
 * 
 * @param {String}
 *            ref - ссылка на элемент к которому привязан скрипт
 * @param {xFormsScript}
 *            xfscript - объект скрипта xForms
 */
function addElemToXFormsScriptMass(ref, xfscript) {
	if (xFormsScriptMass[ref] != null) {
		var temp = xFormsScriptMass[ref];
		temp.push(xfscript);
		xFormsScriptMass[ref] = temp;
	} else {
		var temp = new Array();
		temp.push(xfscript);
		xFormsScriptMass[ref] = temp;
	}
}


function parseXFormsForScript(el, keyNodeRef, isKeyInherit, infoSid){
    var name;
    if(!el.tagName)
        name = el.tagName;
    else
        name = el.tagName.replace(new RegExp('.+:','g'), '');
	if (el.nodeType == Node.ELEMENT_NODE
		&& el.namespaceURI == getNameSpaceForXfdlForm("xforms")){
		if (name == "trigger" || name == "input" ||
			name == "select1" /*|| name == "textarea"*/){
			var refNodeSet = el.getAttribute("ref");
			var isInherit = (name=="trigger"&&refNodeSet!=null)?true:false;
			//var refNode = el.xpath(refNodeSet).iterateNext();
			if (ieXmlDom != null ? el.childNodes.length>0 : el.childElementCount>0){
				var sub = el.firstChild;
				while (sub){
					parseXFormsForScript(sub, refNodeSet, isInherit, infoSid);
					sub = sub.nextSibling;
				}
			}
		}
		if (name == "setvalue" || name == "send" || name == "setfocus"){
			var xfscript = new xFormsScript(el.attributes, name, keyNodeRef, isKeyInherit);
		    addElemToXFormsScriptMass((keyNodeRef!=null)?keyNodeRef:el.getAttribute("ref"),xfscript);
		    if (keyNodeRef!=infoSid && el.getAttribute("ref")!=infoSid) 
		    	addElemToXFormsScriptMass(infoSid,xfscript);
		}
		if (name == "action"){
			var xfscript = new xFormsScript(el.attributes, name, keyNodeRef, isKeyInherit); 
			if (ieXmlDom != null ? el.childNodes.length>0 : el.childElementCount>0){
				var sub = el.firstChild;
                var nameSub = sub.tagName;
                if(nameSub != null) nameSub = nameSub.replace(/.+:/g, '');
				while (sub){
					if (sub.nodeType == Node.ELEMENT_NODE
						&& sub.namespaceURI == getNameSpaceForXfdlForm("xforms")){
						var temp = new xFormsScript(sub.attributes, nameSub, keyNodeRef, isKeyInherit);
						xfscript._childScript.push(temp);
					}
					sub = sub.nextSibling;
				}
			}
			addElemToXFormsScriptMass(infoSid,xfscript);
		}
	}
}


/**
 * Объекты скриптов для элементов xForms
 * 
 * @constructor
 * @param {String}
 *            evAttr - список атрибутов узла для скрипта
 * @param {String}
 *            name - локальное имя узла
 * @param {String}
 *            keyNodeRef - xpath путь к главному родительскому элементу
 * @param {String}
 *            isInherit - нужно ли наследование xpath от родительского элемента
 */
function xFormsScript(evAttr, name, keyNodeRef, isInherit) {
	var script = this;
	this._name = name;
	this._isInherit = isInherit;
	this._keyNodeRef = keyNodeRef;
	this._childScript = new Array();

	for (var i = 0; i < evAttr.length; i++) {
        var name = evAttr[i].tagName;
        if(name != null) name = name.replace(/.+:/g, '');
		switch (name) {
			case 'event' : script._event = evAttr[i].value; break;
			case 'ref' : script._ref = evAttr[i].value; break;
			case 'value' : script._value = evAttr[i].value; break;
			case 'submission' : script._submission = evAttr[i].value; break;
			case 'if' : script._if = evAttr[i].value; break;
			case 'control' : script._control = evAttr[i].value; break;
		}
	}
}

xFormsScript.prototype.work = function() {
	if (this._name == 'setvalue') {
		var val;
		if (this._value!=''){
			try {
				if (this._isInherit && this._value.indexOf('.')!=-1){
					var inhEl;
                    if(ieXmlDom != null) {
                        inhEl = ieXpathEvaluate(ieXmlDom, this._keyNodeRef)[0];
                        val = ieXpathEvaluate(inhEl, this._value)[0].text;
                    } else {
                        inhEl = xfdlForm.xpath(this._keyNodeRef).iterateNext();
                        val = inhEl.xpath(this._value).iterateNext().getValue();
                    }
				} else {
                    if(ieXmlDom != null)
                        val = ieXpathEvaluate(inhEl, this._value)[0].text;
                    else
					    val = xfdlForm.xpath(this._value).iterateNext().getValue();
				}
			} catch (e) {
				val = this._parseVal(this._value);
			}
		} else {
			val = '';
		}
		var path;
		if (this._isInherit){
            var inhEl;
            if(ieXmlDom != null) {
                inhEl = ieXpathEvaluate(ieXmlDom, this._keyNodeRef)[0];
                path = ieXpathEvaluate(inhEl, this._ref)[0];
            } else {
                inhEl = xfdlForm.xpath(this._keyNodeRef).iterateNext();
                path = inhEl.xpath(this._ref).iterateNext();
            }
		} else {
            if(ieXmlDom != null) {
                path = ieXpathEvaluate(ieXmlDom, this._ref)[0];
            } else {
                path = xfdlForm.xpath(this._ref).iterateNext();
            }
		}
		if (path){
			path.setValue(val);
			refreshEl((this._isInherit)?this._keyNodeRef:this._ref, val);
//			var els = xfdlForm.xpath('/xfdl:XFDL/xfdl:page//*[@ref="'+((this._isInherit)?this._keyNodeRef:this._ref)+'"]');
//			try {
//				var el = els.iterateNext();
//			  	while (el) {
//			  		if (el) refreshEl(el, val);
//					el = els.iterateNext();
//				}
//			} catch (e) {}
			if (xFormsScriptMass[(this._isInherit)?this._keyNodeRef:this._ref]) {
				var arr = xFormsScriptMass[(this._isInherit)?this._keyNodeRef:this._ref];
				for (var i = 0; i < arr.length; i++) {
					if (arr[i]._event == 'xforms-value-changed')
						arr[i].work();
				}
			}
		}
	}
	if (this._name == 'send') {
		var XformsSubmission = treeXformsModels.defaultXformModel.massSubmissionForId[this._submission];
		if (this._if){
			var val;
			if (this._isInherit){
				var inhEl = xfdlForm.xpath(this._keyNodeRef).iterateNext();
				val = inhEl.xpath(this._if, XPathResult.STRING_TYPE).stringValue;
			} else {
				val = xfdlForm.xpath(this._if, XPathResult.STRING_TYPE).stringValue;
			}
			if (val=='true'){
				XformsSubmission.run();
			}
		} else {
			XformsSubmission.run();
		}
	}
	if (this._name == 'action') {
		if (this._if){
			var val;
			if (this._isInherit){
				var inhEl = xfdlForm.xpath(this._keyNodeRef).iterateNext();
				val = inhEl.xpath(this._if, XPathResult.STRING_TYPE).stringValue;
			} else {
				val = xfdlForm.xpath(this._if, XPathResult.STRING_TYPE).stringValue;
			}
			if (val=='true'){
				for(var i=0;i<this._childScript.length;i++){
					this._childScript[i].work();
				}
			}
		} else {
			for(var i=0;i<this._childScript.length;i++){
				this._childScript[i].work();
			}
		}
	}
}


xFormsScript.prototype._parseVal = function(val){
	//optimize ну это пиздец, а не функция
	if (!isNaN(val)) return val;
	if (val == '(.) + 1'){
		var a = xfdlForm.xpath(this._keyNodeRef).iterateNext().getValue();
		var val = parseInt(a) + 1;
		return val;
	}
	if (val == '(.) - 1'){
		var a = xfdlForm.xpath(this._keyNodeRef).iterateNext().getValue();
		var val = parseInt(a) - 1;
		return val;
	}
	return val;
}



function refreshEl(ref, val){
	if (ref.indexOf('/row[')!=-1){
		var dopref = ref.substring(0, ref.lastIndexOf('['));
		var newsid = ref.substring(ref.lastIndexOf('/')+1);
		var els = xfdlForm.xpath('/xfdl:XFDL/xfdl:page//*[@nodeset="'+dopref+'"]');
		try {
			var el = els.iterateNext();
		  	while (el) {
		  		var panesid = el.children[0].getAttribute("sid");
				var newref = findFullSidRefForEl(el)+panesid+'-'+newsid;
				var targetInfoEl = treeInfoEls.getElemInfo(newref);
				var emNode = targetInfoEl.hiddenTeg['value'];
				if (emNode){
					if (emNode.getScriptListeners()!=null){
						emNode.setValue(val);
					} else {
						if (targetInfoEl.typeEl == 'field'){
							targetInfoEl.refHTMLElem.value = val;
						}
					}
				}
		  		el = els.iterateNext();
			}
		} catch (e) {}
	} else {
		var els = xfdlForm.xpath('/xfdl:XFDL/xfdl:page//*[@ref="'+ref+'"]');
		try {
			var el = els.iterateNext();
		  	while (el) {
		  		var par = el.parentNode;
				if (par.localName == 'field'){
					//findFullSidRefForEl() - вроде универсальный теперь, для длинных путей будет находить элемент
					var newref = findFullSidRefForEl(par)+par.getAttribute("sid");
					var targetInfoEl = treeInfoEls.getElemInfo(newref);
					var emNode = targetInfoEl.hiddenTeg['value'];
					if (emNode){
						emNode.setValue(val);
					}
				}
				el = els.iterateNext();
			}
		} catch (e) {}
	}
}

function findFullSidRefForEl(el){
	var parentArr = new Array();
	var parentEl = el.parentNode;
	while (parentEl.localName != 'XFDL'){
		if (parentEl.getAttribute("sid")!=null){
			parentArr.push(parentEl);
		}
		parentEl = parentEl.parentNode;
	}
	var fullSidRef = "";
	for (var i=parentArr.length-1;i>-1;i--){
		var pel = parentArr[i];
		if (pel.localName == 'table'){
			var table = treeInfoEls.getElemInfo(fullSidRef+pel.getAttribute("sid"));
			fullSidRef += pel.getAttribute("sid")+'-'+table.selectedRow+'-';
		} else {
			fullSidRef += pel.getAttribute("sid")+'-';
		}
	}
	//console.log(fullSidRef+el.getAttribute("sid"));
	return fullSidRef;
}


function findCompute(childEl, info) {
	var attrScript = childEl.getAttribute('compute');
	if (attrScript){
		var lNameChildEl = childEl.tagName.replace(new RegExp('.+:', 'g'), '');
		var valChildEl = ieXmlDom != null ? childEl.text : childEl.getValue();
		
		/**
		 * @type EmulateNode|Element
		 */
		var emNode;
		if (info.xformNode)
			{
			emNode = new EmulateNode(lNameChildEl, valChildEl,
			    childEl.parentNode);// так как есть xform-связь
			info.hiddenTeg[lNameChildEl] = emNode;
			}
		else
			emNode = childEl;
		var scriptCompute = new UnitScript(attrScript, info, emNode);
		massAllScripts.push(scriptCompute);
	}
}

