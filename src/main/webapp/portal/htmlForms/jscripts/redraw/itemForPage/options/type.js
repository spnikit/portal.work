/**
 * @include "../../../includes.js"
 */
/**
 * @type Object
 */
var functionForType = {
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	cancel : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	display : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	done : function(contextInfoEl) {
		if (contextInfoEl.url) {
			treeInfoPages.hideActivePage();
			if (window.parent.resetFormView)
				with (window.parent) {
					resetFormView();
				}
				
			if (contextInfoEl.url.indexOf('forms/save.form')==0){
				if (funcSaveExit.length>0){
					eval(funcSaveExit);
				} else {
					window.location.assign(appURL+contextInfoEl.url);
				}
			} else if (contextInfoEl.url.indexOf('forms/cancel.form')==0){
				if (funcCancel.length>0){
					eval(funcCancel);
				} else {
					window.location.assign(appURL+contextInfoEl.url);
				}
			} else window.location.assign(appURL+contextInfoEl.url);
		}
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	enclose : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	extract : function(contextInfoEl) {
		createAttachWin(1);
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	link : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	pagedone : function(contextInfoEl) {
		var value = contextInfoEl.url;
		var ref = value.split('.')[0].substring(1);
		var newpage = treeInfoPages.getElemInfo(ref);
		var oldpage = treeInfoPages.getElemInfo(contextInfoEl.parentFullSidRef);
		oldpage.refHTMLElem.style.display = 'none';
		newpage.refHTMLElem.style.display = '';
		currentPage = newpage.pageIndex;
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	print : function(contextInfoEl) {
		if (window.print) {
			window.print();
		} else {
			var WebBrowser = '<OBJECT ID="WebBrowser1" WIDTH=0 HEIGHT=0 CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"></OBJECT>';
			document.body.insertAdjacentHTML('beforeEnd', WebBrowser);
			WebBrowser1.ExecWB(6, 2);// Use a 1 vs. a 2 for a prompting
			// dialog box
			// WebBrowser1.outerHTML = "";
		}
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	refresh : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	remove : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	replace : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	saveform : function(contextInfoEl) {
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	saveas : function(contextInfoEl) {
	},
	/**
	 * заглушка, не имеет реального действия
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	select : function(contextInfoEl) {
		createAttachWin(2);
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	signature : function(contextInfoEl) {
		contextInfoEl.refTegSignature = contextInfoEl.refElem
				.xpath("../xfdl:signature[@sid = '"
						+ contextInfoEl.sidSignature + "']").iterateNext();
		showSignDialog(contextInfoEl);
	},
	/**
	 * 
	 * @param {InfoElement}
	 *            contextInfoEl
	 */
	submit : function(contextInfoEl) {
	}

};
