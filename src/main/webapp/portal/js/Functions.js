Ext.namespace('ETD');
Ext.namespace('Dev');

combocrstoreDI = new Ext.data.JsonStore({
	root : 'datadi',
	fields : [ 'id', 'text' ]
});

Dev.listObject = function(obj) {
	var str = "";
	for ( var i in obj) {
		str += (i + " : " + obj[i] + " \n");
	}
	return str;
};

Dev.listObjectFull = function(val) {
	var str = "";
	if (val == null)
		str = null;
	else if (typeof val == 'function') {

	} else if (typeof val == 'object') {
		for ( var i in val) {
			str += (i + " : " + Dev.listObjectFull(val[i]) + "<br/> \n");
		}
	} else {
		str = val;
	}
	;
	return str;
};

Dev.logger = {
	debug : false,
	consoleWindow : null,
	getWindow : function() {
		if (this.consoleWindow == null)
			this.consoleWindow = window
					.open('', '',
							'width=500,height=400,resizable=1,scrollbars=1,left=1,top=1');
		return this.consoleWindow;
	},
	log : function() {
		if (Dev.logger.debug) {
			var text = '';
			for (var i = 0; i < arguments.length; i++) {
				text += Dev.listObjectFull(arguments[i]) + "\n";
			}
			;
			try {
				console.debug(text);
			} catch (e) {
				this.getWindow().document.write(text)
			}
			;
		}
	}
};

ETD.headers = [ "Наименование документа", "Номер вагона",
		"Дата/время создания", "Персона последней подписи",
		"Дата/время последней подписи", "Краткое содержание", "Создатель",
		"Дата/время отклонения", "Персона отклонения", "Автор",
		"Тип документа", "Текущий исполнитель", "Описание", "Дата поступления",
		"Дата последней подписи", "Договор", "Дата ремонта" ];

ETD.docsPerPage = 20;

ETD.packid = 0;
ETD.packRTKid = 0;
ETD.packCSSid = 0;
ETD.predid = -1;
ETD.droptorgid = 0;
ETD.fpu26ppsdropid = 0;
ETD.transpackid = 0;
ETD.utochnenie = false;
ETD.dropPackid = function() {
	ETD.packid = 0;
	ETD.packview = false;
};
ETD.dropPackRTKid = function() {
	ETD.packRTKid = 0;
	// ETD.packRTKview = false;
}
ETD.dropPackCSSid = function() {
	ETD.packCSSid = 0;
	ETD.packview = false;
};

ETD.setPackid = function(req) {
	ETD.packid = req;
}
ETD.setPackRTKid = function(req) {
	ETD.packRTKid = req;
}
ETD.setPackCSSid = function(req) {
	ETD.packCSSid = req;
}

ETD.setTransPackid = function(req) {
	ETD.transpackid = req;
}
ETD.dropTransPackid = function() {
	ETD.transpackid = 0;
}

ETD.tempdocid = -1;
ETD.tempcert = -1;

ETD.setTemp = function(docid, cert) {
	ETD.tempdocid = docid;
	ETD.tempcert = cert;
}

ETD.dropTemp = function() {
	ETD.tempdocid = -1;
	ETD.tempcert = -1;
}

ETD.setUtoch = function(req) {
	ETD.utochnenie = req;
}

ETD.fr = -1;

ETD.di = "";

ETD.tabname = "";
ETD.isSend = 0;
ETD.predname = "";
ETD.expdoc = false;
ETD.pdfcheck = false;
ETD.packdocs = [ "Расшифровка", "Счет", "Акт оказанных услуг", "ФПУ-26 АСР",
		"РДВ", "ФПУ-26", "МХ-1", "МХ-3", "Акт приема передачи ТМЦ" ];
ETD.dropdocs = [ "Расшифровка", "Счет", "Акт оказанных услуг",
		"Пакет документов", "ФПУ-26 АСР", "ФПУ-26 ППС", "РДВ", "ФПУ-26",
		"ГУ-23", "ГУ-45", "ГУ-2б", "Пакет документов РТК", "ЗПВ", "ЗУВ",
		"СППВ", "ТОРГ-12", "ТОРГ-12 РЖДС", "Акт РЖДС", "ВУ-36М_О",
		"Пакет документов ЦСС", "Счет-фактура", "Счет-фактура ЦСС",
		"Комплект на пересылку в ремонт", "Претензия", "Комплект ремонтопригодности", "Комплект завершение ремонта" ];
ETD.dropdocsroles23 = [ "Счет-фактура", "Счет-фактура ЦСС", "Пакет документов",
		"Претензия" ];
ETD.dropdocsroles2 = [ "Счет-фактура", "Счет-фактура ЦСС", "Претензия" ];
ETD.dropdocsroles3 = [ "Счет-фактура", "Счет-фактура ЦСС", "ТОРГ-12" ];
ETD.transdocaccept = [ "ГУ-23", "ГУ-45", "ГУ-2в", "ГУ-2б", "Счет-фактура",
		"Счет-фактура ЦСС", "Пакет документов", "Пакет документов ЦСС" ];
ETD.newapp = false;
ETD.packview = false;
ETD.getElementsByClass = function(searchClass, node, tag) {
	var classElements = new Array();
	if (node == null)
		node = document;
	if (tag == null)
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\\\s)" + searchClass + "(\\\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if (pattern.test(els[i].className)) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
};

ETD.busyWindow = new Ext.Window({
	id : 'BusyWindow',
	resizable : false,
	headerAsText : false,
	plain : true,
	layout : 'fit',
	width : 1,
	height : 1,
	modal : true,
	shadow : false,
	bodyBorder : false,
	border : false,
	hideBorders : true,
	closable : false
});

// ETD.viewerWindow = new Ext.Window({
// id: 'ViewerWindow',
// resizable:false,
// header: false,
// hideBorders: true,
// plain:true,
// layout: 'fit',
// maximized : true,
// modal:true,
// shadow:false,
// bodyBorder: false,
// border: false,
// closable: false,
// constrainHeader:true,
// html:'<iframe id="viewerid" name="viewername" width="100%" height="100%"
// frameborder="0" src="viewer.html"> '+
// '<h1>IFRAME NOT SUPPORTED</h1> '+
// '</iframe>'
// });

ETD.busy = function() {

	ETD.busyWindow.show();

	ETD.getElementsByClass('x-window x-window-noborder x-window-plain',
			document, 'div')[0].style.visibility = 'hidden';
	// Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
	document.getElementById('loading').style.left = document.body.clientWidth / 2 - 175;
	document.getElementById('loading').style.top = document.body.clientHeight / 2 - 37;
	document.getElementById('loading').style.visibility = 'visible';

};

ETD.ready = function() {
	ETD.busyWindow.hide();
	// Ext.Msg.hide();
	document.getElementById('loading').style.visibility = 'hidden';
};

// Конвертирует чудесные праметры, добавляемые плагином в запрос, в формат,
// подходящий
// для передачи в апплет
ETD.convertRequestParams = function(defParams) {
	// Dev.logger.log("Converting request parameters ..");
	// Dev.logger.log("From : ", defParams);
	var params = {};
	// alert('defParams: '+defParams.start+" "+defParams.limit+"
	// "+defParams.sort+" "+defParams.dir+" ");
	params.start = parseInt(defParams.start, 10);
	// params.start = 21;
	params.limit = parseInt(defParams.limit, 10);

	params.sort = defParams.sort;
	params.dir = defParams.dir;

	params.dateBefore = before.format('m/d/Y');
	params.dateAfter = after.format('m/d/Y');

	if (isdateslist) {
		// console.log(dateslist.length);
		var liststr = '';
		// var list = [];
		for (var iter = 0; iter < dateslist.length; iter++) {
			// console.log(dateslist[iter].format('m/d/Y'));
			// list.push(dateslist[iter].format('m/d/Y'));
			liststr = liststr + dateslist[iter].format('m/d/Y') + ',';
		}

		params.dateEqual = liststr;

	} else {
		params.dateEqual = null;
	}

	params.shift = defParams.shift;
	// console.log(params);
	// alert(params.dateBefore+" "+params.dateAfter+" "+params.dateEqual);
	Dev.logger.log("To : ", params);
	return Ext.util.JSON.encode(params);

	// return params;
};

ETD.openDocument = function(id, name, forceDropped, send) {

	ETD.busy();

	{
		var ret;
		var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel()
				.getSelected();
		var tabname = Ext.getCmp('tpanel').getActiveTab().getId();

		// alert(name+" "+tabname+" "+row.get('packst'));

		if (!forceDropped) {

			var isSend = 0;
			// var row =
			// Ext.getCmp('tpanel').getActiveTab().getSelectionModel().getSelected();

			if (tabname == 'ID_ARCHIVE_GRID'/* &&row.get('name')=='Счет-фактура' */
					|| tabname == 'ID_FIN_GRID')
				isSend = 1;
			// Ext.Msg.wait("Пожалуйста, подождите...", 'Загрузка');
			if (name == 'ФПУ-26' && tabname == 'ID_SEND_GRID' && ETD.packid > 0
					|| name == 'ФПУ-26 АСР' && tabname == 'ID_SEND_GRID'
					&& ETD.packCSSid > 0 || name == 'РДВ' && ETD.packid > 0
					|| name == 'Акт приема передачи ТМЦ'
					&& tabname == 'ID_SEND_GRID' && ETD.packid > 0) {
				isSend = ETD.isSend;
			}

			if (tabname == 'ID_SEND_GRID' && ETD.fr == 7) {
				isSend = 1;
			}
			if (tpanel.getActiveTab().getSelectionModel().hasSelection()) {
				var status = tpanel.getActiveTab().getSelectionModel()
						.getSelected().get('status');
				// if (name=="Пакет документов"&&status==2||name=="Пакет
				// документов"&&status==3||
				// name=="Пакет документов"&&status==4||name=="Пакет документов
				// ЦСС"&&status==2||name=="Пакет документов ЦСС"&&status==3||
				// name=="Пакет документов
				// ЦСС"&&status==4||name=="Счет-фактура"&&status==2||name=="Счет-фактура"&&status==3||
				// name=="Счет-фактура"&&status==4||name=="Счет-фактура
				// ЦСС"&&status==2||name=="Счет-фактура ЦСС"&&status==3||
				// name=="Счет-фактура ЦСС"&&status==4){
				// isSend = 1;
				// }
				if (ETD.transdocaccept.indexOf(name) > -1 && status == 2
						|| ETD.transdocaccept.indexOf(name) > -1 && status == 3
						|| ETD.transdocaccept.indexOf(name) > -1 && status == 4) {
					isSend = 1;
				}
			}

			ret = eval('('
					+ document.applet_adapter.openDocument(id, name, isSend,
							ETD.tabname) + ')');

		} else {
			ret = eval('('
					+ document.applet_adapter.openDroppedDocument(id, name, 1,
							ETD.tabname) + ')');
		}
		ETD.ready();

		if (ret.refresh) {
			Refresh();
		} else if (ret.error) {
			Ext.MessageBox.alert('Внимание', ret.error);
		} else if (ret.dropped) {
			Ext.ux.prompt.confirm('Внимание!', ret.dropped + '<br/><br/>'
					+ ret.user, function(button) {

				if (name.indexOf('Пакет документов') == -1) {
					if (button == 'yes') {
						ETD.openDocument(id, name, true, 1);
					} else if (button == 'no') {

						if (ETD.packid > 0) {
							Pack.mkpackwin(ETD.packid);
						}

						// else if (ETD.packRTKid>0){
						// PackRTK.mkpackRTKwin(ETD.packRTKid);
						// }

					}
				}

				else if (name == 'Пакет документов') {
					if (button == 'yes') {
						ETD.setPackid(id);
						Pack.req = id;
						Pack.mkpackwin(id);
					}
				} else if (name == 'Пакет документов РТК') {
					if (button == 'yes') {
						ETD.setPackRTKid(id);
						PackRTK.req = id;
						PackRTK.mkPackRTKwin(id);
					}
				} else if (name == 'Пакет документов ЦСС') {
					if (button == 'yes') {
						ETD.setPackCSSid(id);
						PackCSS.req = id;
						PackCSS.mkpackwin(id);
					}
				}
			});
		} else {
			CreateOpenDoc()
		}
		;
	}

};

ETD.openDocumentFPU26PPS = function(id, name, forceDropped, send) {
	var ret;
	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel()
			.getSelected();
	// var tabname = Ext.getCmp('tpanel').getActiveTab().getId();
	var docid = row.get('id');
	var mess = document.applet_adapter.getDocStatus(docid);

	Ext.MessageBox
			.show({
				title : '<div style="margin-left:15px;text-align:center;">Отклонение документа</div>',
				msg : '<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">Введите причину мотивированного отказа:</div>',
				buttons : {
					ok : "Подписать МО",
					no : "Открыть документ",
					cancel : "Отмена"
				},
				value : mess,
				resizable : true,
				minWidth : 600,
				animEl : 'elId',
				multiline : true,
				fn : function(button, text) {

					if (button == 'ok') {
						(ETD.dropMOfn(text, docid)).defer(1);

					}
					if (button == 'no') {
						ETD.openDocument(id, name, forceDropped, send);
					}

				}
			});

};

ETD.openStatusDoc = function(id, name, mess) {
	var ret;

	// Ext.Msg.show({
	// title:'<div style="margin-left:15px;text-align:center;">Внимание</div>',
	// msg: '<div
	// style="margin-bottom:-8px;margin-top:-5px;text-align:center;">'+mess+'</div>',
	// buttons :{ ok: "Продолжить работу", no: "Отклонить документ", cancel:
	// "Отмена"},
	//
	// animEl: 'elId',
	// fn:function(button, text){
	//							  
	// if (button=='ok'){
	// ETD.openDocument(id, name,null, 0);
	//								        		      
	// }
	// // if (button=='no'){
	// // if (name.indexOf('чет-фактура')>-1){
	// // dropdoc(true);
	// // } else
	// // dropdoc(false);
	// // }
	// if (button=='no'){
	// dropdoc(false);
	// }
	//						  
	// }
	// });

	openstatusdoc = new Ext.Window(
			{
				plain : 'true',
				width : 500,
				// height:420,
				// autowidth: true,

				layout : 'fit',
				closeAction : 'close',
				resizable : true,
				modal : true,
				border : true,
				buttonAlign : 'center',
				closable : true,
				title : 'Внимание',
				style : 'text-align: center;',
				items : [ {
					xtype : 'label',
					html : '<div style="margin-top: 7px; margin-bottom:7px;" class="x-unselectable">'
							+ mess + '</div>',
					style : 'font-size: 12px;'
				} ],
				buttons : [
						{
							text : 'Продолжить работу',
							handler : function() {
								if (ETD.fr == 2 || ETD.fr == 3) {
									if (name != 'Пакет документов'
											&& name != 'Пакет документов ЦСС') {
										ETD.dropPackid();
										ETD.openDocument(id, name, null, 0);
									} else {
										if (name == 'Пакет документов') {
											ETD.firstpack(id);
											ETD.setPackid(id);
											Pack.req = id;
											Pack.mkpackwin(id);
										} else if (name == 'Пакет документов ЦСС') {
											ETD.setPackCSSid(id);
											PackCSS.req = id;
											PackCSS.mkpackwin(id);
										}

									}
								} else {
									ETD.openDocument(id, name, null, 0);
								}
								openstatusdoc.close();
								openstatusdoc.destroy();
							}

						},

						{
							text : 'Отклонить документ',
							id : 'dropstatusbtn',
							handler : function() {
								dropdoc(false);
								openstatusdoc.close();
								openstatusdoc.destroy();
							}
						}, {
							text : 'Отмена',
							handler : function() {
								openstatusdoc.close();
								openstatusdoc.destroy();
							}
						}

				]
			});

	if (name.indexOf('чет-фактура') > -1) {
		openstatusdoc.addButton({
			text : 'Уточнение СФ',
			id : 'dropsfbtn',
			handler : function() {
				dropdoc(true);
				openstatusdoc.close();
				openstatusdoc.destroy();
			}
		})
	}
	;
	if (ETD.fr == 6) {
		Ext.getCmp('dropsfbtn').disable();
		Ext.getCmp('dropstatusbtn').disable();
	}
	openstatusdoc.show();

};

ETD.createDocumentFromData = function() {
	if (tpanel.getActiveTab().getSelectionModel().hasSelection()) {
		var row = tpanel.getActiveTab().getSelectionModel().getSelected();

		if (row.get('cCreateSource') == '1') {
			// Берем текущий id и название шаблона
			var id = row.get('id');
			var name = row.get('name');
			ETD.busy();
			(function() {
				document.applet_adapter.createDocumentFromData(id, name);
				ETD.ready();
				CreateOpenDoc();
			}).defer(1);
		} else {
			Ext.Msg.alert('Внимание', 'У вас нет прав для данной операции.');
		}
	} else {
		Ext.Msg.show({
			title : 'Внимание',
			msg : 'Документ не выбран.',
			buttons : Ext.Msg.OK,
			icon : Ext.MessageBox.INFO
		});
	}
	;

};

ETD.dropDocument = function() {
	if (tpanel.getActiveTab().getSelectionModel().hasSelection()) {
		Ext.ux.prompt
				.prompt(
						'',
						'<div style="margin-bottom:-8px;margin-top:-5px;text-align:center;">Введите причину отклонения</div>',
						function(btn, text) {
							if (btn == 'ok') {
								if (text.length > 0) {
									if (tpanel.getActiveTab().getId() == 'ID_DOCUMENT_GRID')
										tab = 0;
									else
										tab = 1;
									var selected = tpanel.getActiveTab()
											.getSelectionModel().getSelected();
									ETD.busy();
									(function() {
										var resp = eval('('
												+ document.applet_adapter
														.dropdoc(
																text,
																tab,
																selected
																		.get('id'))
												+ ')');
										if (resp.error)
											Ext.MessageBox.alert('Ошибка!',
													resp.error);
										else if (tab == 0)
											tpanel.getActiveTab().getStore()
													.reload();
										ETD.ready();
									}).defer(1);
								} else {
									Ext.MessageBox
											.alert('Ошибка',
													'Документ не отклонен. Необходимо ввести причину отклонения.');
								}
							}
						})
	}
};

var replaceQuote = new RegExp("\"", "g");

ETD.shortDescriptionRender = function(value, p, record) {

	acc = 1;

	p.attr = 'ext:qtip="' + value.replace(replaceQuote, "&quot;") + '"';
	return value;
};

ETD.sfcreate = function() {

	SFdoc.grid = new Ext.grid.GridPanel({
		title : 'Просмотр квитанций',
		store : SFdoc.store,
		autoWidth : true,
		// width:1000,
		isLoaded : false,
		cm : new Ext.grid.ColumnModel([ {
			id : 'name',
			header : 'Наименование квитанции',
			width : 300,
			sortable : false,
			dataIndex : 'NAME'
		}, {
			id : 'statusname',
			header : 'Статус',
			width : 70,
			sortable : false,
			dataIndex : 'STATUSNAME'
		} ]),
		sm : new Ext.grid.RowSelectionModel({
			singleSelect : true
		}),

		// height:320,
		// autoWidth: true,
		autoHeight : true,
		viewConfig : {
			forceFit : true
		},
		frame : true,
		autoScroll : true,
		stripeRows : true,
		listeners : {
			render : function(panel) {
				SFdoc.load(SFdoc.req);
			},
			rowdblclick : {
				fn : function(grid, rowIndex, e) {
					viewwin();
					var row = grid.getSelectionModel().getSelected();

					if (row.get('STATUS') == 1) {

						// var xml =
						// document.applet_adapter.getSFviewnotice(row.get('ID'),
						// row.get('NO'));
						Ext.Ajax.request({
							url : 'forms/previewreseipts',
							params : {
								id : row.get('ID'),
								count : row.get('NO'),
								action : '2'
							},
							callback : function(options, success, response) {

								var resp = response.responseText;

								var response = Ext.util.JSON.decode(resp);
								response = response[0].xml;

								// Ext.getCmp('xml').setValue(xml.get('xml'));
								Ext.getCmp('xml').setValue(response);
								viewxml.show();
								viewxml.focus();
							}
						});

					} else {
						Ext.MessageBox.alert('Сообщение',
								'Квитанция не оформлена');
						return;
					}
				}
			}

		}

	});
}

ETD.validateVagnum = function(val) {
	if (val.length > 7) {
		// Проверяем допустимость номера по алгоритму РЖД
		var checkSum = 0;
		var nech;
		for (var digitPos = 8; digitPos > 0; digitPos--) {

			var digit = val.charAt(digitPos - 1);
			// alert(digit);
			if (digitPos % 2 == 1) {
				nech = (digit >= 5) ? ((digit - 5) * 2 + 1) : digit * 2;
				checkSum = checkSum + parseInt(nech, 10);
			} else {
				checkSum = checkSum + parseInt(digit, 10);
			}
			;

		}
	}

	return checkSum % 10 == 0;
}
ETD.firstpack = function(docid) {

	if (ETD.fr != 6) {
		Ext.Ajax.request({
			url : 'forms/firstpack',
			params : {
				docid : docid
			}
		});
	}

}

ETD.fpuacc = function(docid) {
	var acc = 1;
	Ext.Ajax.request({
		url : 'forms/fpuacc',
		params : {
			docid : docid
		},
		callback : function(options, success, response) {
			var resp = response.responseText;
			var arr = Ext.util.JSON.decode(resp);

			if (arr === 'true') {
				acc = 0;
			} else if (arr === 'false') {
				acc = 1;
			}

		}
	});
	return acc;
}

ETD.unlockDoc = function(docid, cert) {
	(function() {
		Ext.Ajax.request({
			url : 'forms/unlock',
			params : {
				docid : docid,
				cert : cert
			}

		});
		ETD.dropTemp();
	}).defer(1);

}
ETD.getServTime = function() {
	// var date = '';
	Ext.Ajax.request({
		url : 'forms/getdatetime',
		params : {},
		callback : function(options, success, response) {
			var resp = response.responseText;
			var arr = Ext.util.JSON.decode(resp);

		}
	});
	return arr[0].date;
	// ret = eval('('+ document.applet_adapter.dropMO(text, 0, docid,
	// arr[0].date) + ')');
	// if (ret.error)
	// Ext.MessageBox.alert('Ошибка!',resp.error);
	// else {
	// tpanel.getActiveTab().getStore().reload();
	// }
	// ETD.ready();

}

ETD.dropMOfn = function(text, docid) {
	ETD.busy();
	var docId = docid;

	/*--------------------------------------------------*/
	/*
	 * Проверка можно ли отклонить док. Берем выгрузку в текущей вкладке, если
	 * ид док-та, который собираемся дропнуть там отсутствует, вернем false ->
	 * дроп запрещен Сам дроп в конце проверки
	 */

	var srchArr = new Array();

	srchArr[0] = '';

	srchArr[1] = Ext.getCmp('from').getRawValue();
	srchArr[2] = Ext.getCmp('to').getRawValue();
	srchArr[3] = Ext.getCmp('content').getValue();
	if (srchArr[3] == null)
		srchArr[3] = '';
	srchArr[4] = Ext.getCmp('dognum').getValue();
	if (srchArr[4] == null)
		srchArr[4] = '';
	srchArr[5] = Ext.getCmp('package').getValue();
	if (srchArr[5] == null)
		srchArr[5] = '';
	srchArr[6] = Ext.getCmp('vagnum').getValue();
	if (srchArr[6] == null)
		srchArr[6] = '';
	srchArr[7] = Ext.getCmp('rf').getRawValue();
	srchArr[8] = Ext.getCmp('di').getValue();
	if (srchArr[8] == null)
		srchArr[8] = '';
	srchArr[9] = Ext.getCmp('rempred').getValue();
	if (srchArr[9] == null)
		srchArr[9] = '';
	srchArr[10] = Ext.getCmp('types').getValue();
	if (srchArr[10] == null)
		srchArr[10] = '';
	srchArr[11] = Ext.getCmp('signer').getValue();
	if (srchArr[11] == null)
		srchArr[11] = '';
	srchArr[12] = Ext.getCmp('stat').getValue();
	srchArr[13] = Ext.getCmp('rt').getRawValue();
	srchArr[14] = Ext.getCmp('servicetype').getValue();
	if (srchArr[14] == null)
		srchArr[14] = '';
	srchArr[15] = Ext.getCmp('color').getValue();
	if (srchArr[15] == null)
		srchArr[15] = '';
	srchArr[16] = Ext.getCmp('sftype').getValue();
	if (srchArr[16] == null)
		srchArr[16] = '';
	srchArr[17] = Ext.getCmp('otc_code').getValue();
	if (srchArr[17] == null)
		srchArr[17] = '';
	srchArr[18] = Ext.getCmp('refer_type').getValue();
	if (srchArr[18] == null)
		srchArr[18] = '';
	srchArr[19] = Ext.getCmp('etdid').getValue();
	if (srchArr[19] == null)
		srchArr[19] = '';
	console.log("docId", docId);

	for (i = 0; i < srchArr.length; i++) {

		if (srchArr[i].length == 0)
			srchArr[i] = '---';
		console.log("srchArr[" + i + "] ", srchArr[i]);
	}
	srchArrStr = srchArr.join("|");
	shParam = srchArrStr;

	var grid = Ext.getCmp('tpanel').getActiveTab();
	var cm = grid.getColumnModel();
	var sort = grid.getStore().sortInfo.field;
	var dir = grid.getStore().sortInfo.direction;

	var fr = ETD.fr;
	var predid = ETD.predid;
	var workid = ETD.wrkid;
	var indexTabs = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel')
			.getActiveTab()) + 1;
	params = predid + ";" + workid + ";" + fr + ";" + before.format('Y-m-d')
			+ ";" + after.format('Y-m-d') + ";" + indexTabs;
	var shiftParams = shParam;
	var params = params
	var sort = sort;
	var dir = dir;
	var judgment;
	Ext.Ajax.request({
		url : 'forms/enabledropcontroller',
		waitMsg : 'Processing your request',
		method : 'POST',
		async : false,
		params : {
			shiftParams : shParam,
			params : params,
			sort : sort,
			dir : dir,
			docId : docId

		},
		failure : function() {
			Ext.MessageBox.show({
				title : 'Ошибка',
				msg : 'Не удалось выполнить запрос',
				buttons : Ext.MessageBox.OK
			});
			return;
		},
		success : function(response) {
			console.log("jud ", judgment);
			if (response.responseText == 'true') {
				Ext.Ajax.request({
					async : false,
					url : 'forms/getdatetime',
					params : {},

					callback : function(options, success, response) {
						var resp = response.responseText;
						var arr = Ext.util.JSON.decode(resp);

						if (arr[0].success) {
							ret = eval('('
									+ document.applet_adapter.dropMO(text, 0,
											docid, arr[0].date) + ')');

							if (ret.error) {
								Ext.MessageBox.alert('Ошибка!', resp.error);
							} else {
								tpanel.getActiveTab().getStore().reload();
							}
							ETD.ready();
						} else {
							tpanel.getActiveTab().getStore().reload();
							ETD.ready();
						}
					}
				});

				judgment = true;
			} else {
				ETD.ready();
				Ext.MessageBox.alert('Внимание',
						'Нельзя отклонить согласованный документ');
				console.log("response is false in success func");
				judgment = false;

			}
			return judgment;
		}// ,
		// callback: function(){
		// console.log("jud ",judgment);
		// if(judgment == false)
		// {
		// ETD.ready();
		// Ext.MessageBox.alert('Внимание', 'Нельзя отклонить согласованный
		// документ');
		// }
		// else{
		// /*--------------------------------------------------*/
		// /*если док есть в списке, то дропаем его */
		// Ext.Ajax.request({
		// async: false,
		// url : 'forms/getdatetime',
		// params : {},
		//
		// callback : function(options, success, response) {
		// var resp = response.responseText;
		// var arr = Ext.util.JSON.decode(resp);
		// alert("resp="+resp);
		// if (arr[0].success){
		// ret = eval('('+ document.applet_adapter.dropMO(text, 0, docid,
		// arr[0].date) + ')');
		// alert("ret ="+ret);
		// alert(ret.responseText);
		// alert(Ext.util.JSON.decode(ret.responseText));
		// if (ret.error){
		// alert("error");
		// Ext.MessageBox.alert('Ошибка!',resp.error);
		// }else {
		// alert("error else");
		// tpanel.getActiveTab().getStore().reload();
		// }
		// ETD.ready();
		// }
		// else {
		// tpanel.getActiveTab().getStore().reload();
		// ETD.ready();
		// }
		// }
		// });
		// }
		// return judgment;
		// }

	});

}

ETD.exporttoexcelsf = function(name, depname, predid) {

	var dateBefore;
	var dateAfter;
	var dateBeforeValue;
	var dateAfterValue;
	var sellerValue, responseIdValue;

	var comboSeller, comboResponseId;
	var sellerArray, responseIdArray;

	comboSeller = new Ext.data.JsonStore({
		root : 'datadi',
		fields : [ 'id', 'text' ]
	});

	comboResponseId = new Ext.data.JsonStore({
		root : 'sign',
		fields : [ 'id', 'name' ]
	});

	sellerArray = eval('(' + document.applet_adapter.getDI() + ')');
	if (sellerArray.datadi) {
		comboSeller.loadData(sellerArray);
	}
	;

	responseIdArray = eval('(' + document.applet_adapter.getPersTypes() + ')');

	if (responseIdArray.sign) {
		comboResponseId.loadData(responseIdArray);
	}
	;

	var dateFrom = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repfrom',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'С',
		width : 150,
		allowBlank : false
	});
	var dateTo = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repto',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'По',
		width : 150,
		allowBlank : false
	});
	var seller = {
		xtype : 'multiselect',
		labelStyle : Ext.isIE ? 'width:120px;' : 'width:109px;',
		store : comboSeller,
		width : 150,
		displayField : 'text',
		valueField : 'id',
		hidden : false,
		editable : false,
		mode : 'local',
		triggerAction : 'all',
		id : 'seller',
		fieldLabel : 'Продавец',
		allowBlank : true,
		width : 150
	};
	var responseId = {
		xtype : 'multiselect',
		labelStyle : Ext.isIE ? 'width:120px;' : 'width:109px;',
		store : comboResponseId,
		width : 150,
		displayField : 'name',
		valueField : 'id',
		hidden : false,
		editable : false,
		mode : 'local',
		triggerAction : 'all',
		id : 'responseId',
		fieldLabel : 'Ответственный',
		allowBlank : true,
		width : 150
	};

	var items = [ {
		xtype : 'hidden',
		value : 'new',
		name : 'act'
	}, seller, responseId, dateFrom, dateTo ];

	var param = 'act=export&name=' + name + '&rolename=' + depname + '&predid='
			+ predid;

	var form1 = new Ext.form.FormPanel({
		id : 'form1',
		border : false,
		autoScroll : false,
		autoHeight : true,
		style : 'margin-left: 5px;margin-top:5px;margin-right: 5px',
		labelWidth : 120,
		buttons : [
				{
					xtype : 'button',
					text : 'Применить',
					id : 'btn',/* disabled:true, */
					handler : function() {
						param = 'act=export&name=' + name + '&rolename='
								+ depname + '&predid=' + predid;
						dateBeforeValue = Ext.getCmp('repfrom').getValue();
						dateAfterValue = Ext.getCmp('repto').getValue();
						sellerValue = Ext.getCmp('seller').getValue();
						responseIdValue = Ext.getCmp('responseId').getValue();

						if (dateBeforeValue == '' || dateAfterValue == '') {
							Ext.MessageBox.alert('Внимание!',
									'Пожалуйста, введите даты выборки');
						} else {

							dateBefore = dateBeforeValue.format('Y-m-d');
							dateAfter = dateAfterValue.format('Y-m-d');

							param = param + '&dateBefore=' + dateBefore
									+ '&dateAfter=' + dateAfter + '&seller='
									+ sellerValue + '&responseId='
									+ responseIdValue;
							var form = Ext.DomHelper.append(document.body, {
								tag : 'form',
								method : 'post',
								action : 'forms/sfspsexcel' + '?' + param
							});
							createwin.destroy();
							document.body.appendChild(form);
							form.submit();
							document.body.removeChild(frame);

							Ext.getCmp('repto').setValue('');
							Ext.getCmp('seller').setValue('');
							Ext.getCmp('responseId').setValue('');
							Ext.getCmp('repfrom').setValue('');
							dateBeforeValue = '';
							dateAfterValue = '';
							dateBefore = '';
							dateAfter = '';
							sellerValue = '';
							responseIdValue = '';
							param = 'act=export&name=' + name + '&rolename='
									+ depname + '&predid=' + predid;
							reportwin.hide();

						}

					}
				},
				{
					xtype : 'button',
					text : 'Отмена',
					handler : function() {
						Ext.getCmp('repto').setValue('');
						Ext.getCmp('seller').setValue('');
						Ext.getCmp('responseId').setValue('');
						Ext.getCmp('repfrom').setValue('');
						dateBeforeValue = '';
						dateAfterValue = '';
						dateBefore = '';
						dateAfter = '';
						sellerValue = '';
						responseIdValue = '';
						param = 'act=export&name=' + name + '&rolename='
								+ depname + '&predid=' + predid;
						reportwin.hide();
						createwin.destroy();
					}
				},
				{
					xtype : 'button',
					text : 'Очистить',
					handler : function() {
						Ext.getCmp('repfrom').setValue('');
						Ext.getCmp('repto').setValue('');
						Ext.getCmp('seller').setValue('');
						Ext.getCmp('responseId').setValue('');
						dateBeforeValue = '';
						dateAfterValue = '';
						dateBefore = '';
						dateAfter = '';
						sellerValue = '';
						responseIdValue = '';
						param = 'act=export&name=' + name + '&rolename='
								+ depname + '&predid=' + predid;
						reportwin.hide();
					}
				} ],
		items : items
	});

	var createwin = new Ext.Window({
		closable : false,
		width : 300,
		autoScroll : true,
		resizable : false,
		title : 'Введите параметры поиска',
		items : [ form1 ],
		modal : true
	});
	createwin.show();
	createwin.focus();
}

ETD.exporttoexcel = function() {

	var shParam = '';
	var srchArr = new Array();

	srchArr[0] = '';

	srchArr[1] = Ext.getCmp('from').getRawValue();
	srchArr[2] = Ext.getCmp('to').getRawValue();
	srchArr[3] = Ext.getCmp('content').getValue();
	if (srchArr[3] == null)
		srchArr[3] = '';
	srchArr[4] = Ext.getCmp('dognum').getValue();
	if (srchArr[4] == null)
		srchArr[4] = '';
	srchArr[5] = Ext.getCmp('package').getValue();
	if (srchArr[5] == null)
		srchArr[5] = '';
	srchArr[6] = Ext.getCmp('vagnum').getValue();
	if (srchArr[6] == null)
		srchArr[6] = '';
	srchArr[7] = Ext.getCmp('rf').getRawValue();
	srchArr[8] = Ext.getCmp('di').getValue();
	if (srchArr[8] == null)
		srchArr[8] = '';
	srchArr[9] = Ext.getCmp('rempred').getValue();
	if (srchArr[9] == null)
		srchArr[9] = '';
	srchArr[10] = Ext.getCmp('types').getValue();
	if (srchArr[10] == null)
		srchArr[10] = '';
	srchArr[11] = Ext.getCmp('signer').getValue();
	if (srchArr[11] == null)
		srchArr[11] = '';
	srchArr[12] = Ext.getCmp('stat').getValue();
	srchArr[13] = Ext.getCmp('rt').getRawValue();
	srchArr[14] = Ext.getCmp('servicetype').getValue();
	if (srchArr[14] == null)
		srchArr[14] = '';
	srchArr[15] = Ext.getCmp('color').getValue();
	if (srchArr[15] == null)
		srchArr[15] = '';
	srchArr[16] = Ext.getCmp('sftype').getValue();
	if (srchArr[16] == null)
		srchArr[16] = '';
	srchArr[17] = Ext.getCmp('otc_code').getValue();
	if (srchArr[17] == null)
		srchArr[17] = '';
	srchArr[18] = Ext.getCmp('refer_type').getValue();
	if (srchArr[18] == null)
		srchArr[18] = '';
	srchArr[19] = Ext.getCmp('etdid').getValue();
	if (srchArr[19] == null)
		srchArr[19] = '';
	for (i = 0; i < srchArr.length; i++) {
		// alert(srchArr[i]);
		if (srchArr[i].length == 0)
			srchArr[i] = '---';
	}
	srchArrStr = srchArr.join("|");
	shParam = srchArrStr;

	var grid = Ext.getCmp('tpanel').getActiveTab();
	var cm = grid.getColumnModel();
	var sort = grid.getStore().sortInfo.field;
	var dir = grid.getStore().sortInfo.direction;
	var headerField = "";

	for (var i = 0; i < cm.getColumnCount(); i++) {
		if (!cm.isHidden(i)) {
			if (i == cm.getColumnCount() - 1)
				headerField = headerField + cm.getColumnHeader(i).toString();
			else
				headerField = headerField + cm.getColumnHeader(i).toString()
						+ ";";

		}
	}

	var fr = ETD.fr;
	var predid = ETD.predid;
	var workid = ETD.wrkid;
	var indexTabs = Ext.getCmp('tpanel').items.indexOf(Ext.getCmp('tpanel')
			.getActiveTab()) + 1;
	params = predid + ";" + workid + ";" + fr + ";" + before.format('Y-m-d')
			+ ";" + after.format('Y-m-d') + ";" + indexTabs;
	var param = 'act=export&shiftParams=' + shParam + '&params=' + params
			+ '&headerField=' + headerField + '&sort=' + sort + '&dir=' + dir;
	param = encodeURI(param);

	var form = Ext.DomHelper.append(document.body, {
		tag : 'form',
		method : 'post',
		action : 'forms/aploadedtoexcel' + '?' + param
	});
	document.body.appendChild(form);
	form.submit();
	document.body.removeChild(frame);

}

ETD.exportTORreport = function() {
	var dateFrom = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repfrom',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'С',
		width : 150
	});
	var dateTo = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repto',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'По',
		width : 150
	});

	var items = [ dateFrom, dateTo ];
	var predid = ETD.predid;
	var grid = Ext.getCmp('tpanel').getActiveTab();

	var form1 = new Ext.form.FormPanel({
		id : 'form1',
		border : false,
		autoScroll : false,
		autoHeight : true,
		style : 'margin-left: 5px;margin-top:5px;margin-right: 5px',
		labelWidth : 120,
		buttons : [
				{
					xtype : 'button',
					text : 'Применить',
					handler : function() {
						// createwin.destroy();
						if (dateFrom.getValue().length < 1)
							grid.getStore().baseParams.dateFrom = null;
						else {
							grid.getStore().baseParams.dateFrom = dateFrom
									.getValue().format('Y-m-d');
						}
						if (dateTo.getValue().length < 1)
							grid.getStore().baseParams.dateTo = null;
						else {
							grid.getStore().baseParams.dateTo = dateTo
									.getValue().format('Y-m-d');
						}
						if (dateTo.getValue() < dateFrom.getValue()) {
							alert("Выберите корректные даты")
						} else {
							// console.log(dateTo.getValue());
							var param = 'act=export&predid=' + predid
									+ '&dateFrom='
									+ dateFrom.getValue().format('Y-m-d')
									+ '&dateTo='
									+ dateTo.getValue().format('Y-m-d');
							var form = Ext.DomHelper.append(document.body, {
								tag : 'form',
								method : 'post',
								action : 'forms/report_tor' + '?' + param
							});
							document.body.appendChild(form);
							form.submit();
							// document.body.removeChild(frame);
							createwin.destroy();
						}
					}
				}, {
					xtype : 'button',
					text : 'Выход',
					handler : function() {
						createwin.destroy()
					}
				}, {
					xtype : 'button',
					text : 'Очистить',
					handler : function() {
						dateFrom.setValue('');
						dateTo.setValue('');
						/*
						 * createwin.destroy();
						 * grid.getStore().baseParams.dateFrom=null;
						 * grid.getStore().baseParams.dateTo=null;
						 * grid.getStore().baseParams.searchField=null;
						 * grid.getStore().load();
						 */
					}
				} ],
		items : items
	});

	var createwin = new Ext.Window({
		closable : false,
		width : 350,
		autoScroll : true,
		resizable : false,
		title : 'Введите параметры поиска',
		items : [ form1 ],
		modal : true
	});
	createwin.show();

}

ETD.printPackage = function() {

	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selectionModel = grid.getSelectionModel();
	var selectedCount = selectionModel.getCount()
	if (selectedCount == 0) {
		Ext.MessageBox.alert('Выберите пакет документов');
		return;
	} else if (selectedCount > 10) {
		Ext.MessageBox
				.alert(
						'Ошибка',
						'Невозможно распечатать данное количество документов. Установите другие параметры');
		return;
	}
	var docArray = selectionModel.getSelections();
	var idPackages = '';
	for (var j = 0; j < docArray.length; j++) {
		var docname = docArray[j].get('name');
		if (docname != 'Пакет документов') {
			Ext.MessageBox.alert('Ошибка', 'Выберите пакет документов');
			return;
		}
		if (idPackages == '') {
			idPackages = docArray[j].get('idpak');
		} else {
			idPackages = idPackages + ';' + docArray[j].get('idpak');
		}
	}

	Ext.Ajax.request({
		url : 'forms/print_package',
		method : 'POST',
		params : {
			idPak : idPackages
		},
		success : function(response) {
			PrintPack.mkpackwin(response);
		}
	});
}
ETD.acceptDocs = function() {
	var tabname = Ext.getCmp('tpanel').getActiveTab().getId();
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selectionModel = grid.getSelectionModel();
	var selectedCount = selectionModel.getCount()
	if (selectedCount == 0) {
		Ext.MessageBox.alert('Выберите пакет документов');
		return;
	} else if (selectedCount > 10) {
		Ext.MessageBox
				.alert('Невозможно согласовать данное количество пакетов документов. Установите другие параметры');
		return;
	}

	var docArray = selectionModel.getSelections();
	var idPackages = '';
	for (var j = 0; j < docArray.length; j++) {
		var docname = docArray[j].get('name');
		if (docname != 'Пакет документов') {
			Ext.MessageBox.alert('Выберите пакет документов');
			return;
		}
		if (idPackages == '') {
			idPackages = docArray[j].get('idpak');
		} else {
			idPackages = idPackages + ';' + docArray[j].get('idpak');
		}
	}

	Ext.Ajax.request({
		url : 'forms/accept_docs',
		method : 'POST',
		params : {
			idPak : idPackages
		},
		success : function(response) {
			var resp = response.responseText;
			var data = Ext.util.JSON.decode(resp);
			Ext.MessageBox.alert('Сообщение', 'Согласовано '
					+ data.numberOfDocs + ' документов в ' + data.numberOfPack
					+ ' пакетах');
		}
	});
}
openstatusdoc = new Ext.Window({
	plain : 'true',
	width : 300,
	// height:420,
	layout : 'form',
	closeAction : 'close',
	resizable : false,
	modal : true,
	border : true,
	buttonAlign : 'center',
	closable : false,
	title : 'Введите параметры поиска',
	style : 'text-align: center;',

	buttons : [ {
		text : 'OK',
		handler : function() {

		}

	}, {
		text : 'Отмена',
		handler : function() {

		}
	}, {
		text : 'Сбросить',
		handler : function() {

		}
	}

	]
});

ETD.reportRTK = function() {
	var dateFromPeriod = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repfromP',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'С',
		width : 150
	});
	var dateToPeriod = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'reptoP',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'По',
		width : 150
	});
	var dateFromSign = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repfromS',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'С',
		width : 150
	});
	var dateToSign = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'reptoS',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'По',
		width : 150
	});
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var formRTK_Report = new Ext.form.FormPanel(
			{
				id : 'formRTK_Report',
				border : false,
				autoScroll : false,
				autoHeight : true,
				style : 'margin-left: 5px;margin-top:5px;margin-right: 5px',
				labelWidth : 120,
				items : [
						{
							xtype : 'label',
							text : 'Период формирования документа:',
							style : 'font-size: 12px; text-align: center; position: relative; left: 30px;'
						},
						dateFromPeriod,
						dateToPeriod,
						{
							xtype : 'label',
							text : 'Дата подписи документа:',
							style : 'font-size: 12px; text-align: center; position: relative; left: 30px;'
						}, dateFromSign, dateToSign ],
				buttons : [
						{
							text : 'Применить',
							handler : function() {
								if (dateFromPeriod.getValue().length < 1)
									grid.getStore().baseParams.dateFromPeriod = null;
								else {
									grid.getStore().baseParams.dateFromPeriod = dateFromPeriod
											.getValue().format('Y-m-d');
								}
								if (dateToPeriod.getValue().length < 1)
									grid.getStore().baseParams.dateToPeriod = null;
								else {
									grid.getStore().baseParams.dateToPeriod = dateToPeriod
											.getValue().format('Y-m-d');
								}

								if (dateFromSign.getValue().length < 1)
									grid.getStore().baseParams.dateFromSign = '';
								else {
									grid.getStore().baseParams.dateFromSign = dateFromSign
											.getValue().format('Y-m-d');
								}
								if (dateToSign.getValue().length < 1)
									grid.getStore().baseParams.dateToSign = '';
								else {
									grid.getStore().baseParams.dateToSign = dateToSign
											.getValue().format('Y-m-d');
								}

								if (dateToPeriod.getValue() < dateFromPeriod
										.getValue()) {
									Ext.MessageBox.alert('Сообщение',
											'Выберите корректные даты');
								} else if (dateToSign.getValue() < dateFromSign
										.getValue()) {
									Ext.MessageBox.alert('Сообщение',
											'Выберите корректные даты');
								} else {
									var dateFS;
									var dateTS;
									if (dateFromSign.getValue().length < 1
											|| dateToSign.getValue().length < 1) {
										dateFS = '';
										dateTS = '';
									} else {
										dateFS = dateFromSign.getValue()
												.format('Y-m-d');
										dateTS = dateToSign.getValue().format(
												'Y-m-d');
									}
									var param = 'act=export&predid='
											+ ETD.predid
											+ '&dateFromPeriod='
											+ dateFromPeriod.getValue().format(
													'Y-m-d')
											+ '&dateToPeriod='
											+ dateToPeriod.getValue().format(
													'Y-m-d') + '&dateFromSign='
											+ dateFS + '&dateToSign=' + dateTS;
									var form = Ext.DomHelper.append(
											document.body, {
												tag : 'form',
												method : 'post',
												action : 'forms/report_rtk'
														+ '?' + param
											});
									document.body.appendChild(form);
									form.submit();
									createwin.destroy();
								}
							}

						}, {
							text : 'Отмена',
							handler : function() {
								createwin.destroy();
							}
						}, {
							text : 'Очистить',
							handler : function() {
								dateFromPeriod.setValue('');
								dateToPeriod.setValue('');
								dateFromSign.setValue('');
								dateToSign.setValue('');
							}
						}

				]
			});

	var createwin = new Ext.Window({
		closable : false,
		width : 300,
		height : 240,
		autoScroll : false,
		resizable : false,
		title : 'Введите параметры поиска',
		items : [ formRTK_Report ],
		modal : true
	});
	createwin.show();
}
var massobj = null;
var winMassSign = null;
var progressBar = null;
ETD.signvrkDocs = function() {
	var resp;
	var data;
	var row = Ext.getCmp('tpanel').getActiveTab().getSelectionModel()
			.getSelected();
	// massobj = VRKMassSign.createwin();
	// winMassSign = massobj[0];
	// progressBar = massobj[1];
	// progressBar.updateProgress(0, 'Подписание документов...');
	// winMassSign.show();
	// winMassSign.focus();
	Ext.Ajax
			.request({
				url : 'forms/getcountvrk',
				method : 'POST',
				params : {
					idPak : row.get('idpak'),
					id : row.get('id')
				},

				success : function(response) {
					resp = response.responseText;
					data = Ext.util.JSON.decode(resp);
					if (data[0].error) {
						Ext.MessageBox.alert('Сообщение',
						'Ошибка подписания документа');
						return;
					}
					if (data[0].success) {
						massobj = VRKMassSign.createwin();
						winMassSign = massobj[0];
						progressBar = massobj[1];
						winMassSign.show();
						winMassSign.focus();
						progressBar.updateProgress(0,
								'Подписание документов...');
						for (var i = 0; i < data[1].docs.length; i++) {
							progressBar
									.updateProgress(i / data[1].docs.length,
											'Подписывается документ '
													+ data[1].docs[i].name
													+ ' ...'/*
															 * , 'Подписание
															 * документа '+(i+1) +'
															 * из
															 * '+data[1].docs.length
															 */);
							ret = eval('('
									+ document.applet_adapter
											.SignVRKDocs(data[1].docs[i].docid)
									+ ')');
						}
						progressBar.updateProgress(1, 'Обработка завершена ');
					} else {
						// winMassSign.hide();
						// winMassSign.destroy();
						Ext.MessageBox.alert('Сообщение',
								'Этот документ уже был подписан');
					}
					// progressBar.updateProgress(1, 'Обработка завершена ');
				}
			});
	// progressBar.wait({
	// duration : 10000,
	// increment : 12,
	// text : 'Saving...',
	// scope : this,
	// fn : function(){
	// progressBar.updateText('Saved Successfully!');
	// }
	// });

}

ETD.signvrkDocsfn = function(idpak) {
	ret = eval('(' + document.applet_adapter.SignVRKDocs(idpak) + ')');
	return ret;
}

ETD.updtprogress = function(iter, name) {
	progressBar
			.updateProgress(iter, 'Подписывается документ ' + name + ' ...'/*
																			 * ,
																			 * 'Подписание
																			 * документа
																			 * '+(i+1) +'
																			 * из
																			 * '+data[1].docs.length
																			 */);
	//		
}
// ETD.wait = function(wait){
// if (wait) {
// console.log('wait: '+wait);
// setTimeout(function(){ETD.wait(wait)},100);
// };
// }
ETD.downloadProposal = function() {

	var form1 = new Ext.form.FormPanel({
		id : 'form1',
		fileUpload : true,
		border : false,
		autoScroll : false,
		autoHeight : true,
		trackLabels : true,
		style : 'margin-left: 5px;margin-top:5px;margin-right: 5px;',
		// labelWidth:120,
		buttons : [ {
			xtype : 'button',
			text : 'Загрузить',
			handler : function() {

				if (form1.getForm().isValid()) {
					form1.getForm().submit({
						url : 'forms/uploadClaim',
						waitMsg : 'Загрузка файла...',
						success : function(fp, o) {
							ETDDocPPS_Role2.reload();
							createwin.destroy();
							Ext.Msg.alert('Сообщение', 'Файл был загружен.');
						},
						failure : function(form, action) {
							Ext.Msg.alert('Ошибка', action.result.description);
							createwin.destroy();
						}
					});
				}

			}
		}, {
			xtype : 'button',
			text : 'Отмена',
			handler : function() {
				createwin.destroy()
			}
		} ],
		items : [ {
			xtype : 'textfield',
			inputType : 'file',
			id : 'fileExcel',
			name : 'fileExcel',
			fieldLabel : 'Файл',
			width : 200,
			labelWidth : 10,
			labelStyle : 'left: 68; top: 28',
			style : 'margin-top: 30;'
		}, {
			xtype : 'textfield',
			id : 'predid',
			name : 'predid',
			value : ETD.predid,
			hidden : true,
			hidelabel : true
		}, {
			xtype : 'hidden',
			value : 'new',
			name : 'act'
		} ]
	});

	var createwin = new Ext.Window({
		closable : false,
		width : 330,
		autoScroll : false,
		resizable : false,
		items : [ form1 ],
		modal : true
	});
	createwin.show();
	Ext.getCmp("predid").getEl().findParent("div", 50, true).prev("label")
			.hide();
}

ETD.readOnlyReportPPS = 0;

ETD.reportPPS = function() {

	Ext.Ajax
			.request({
				url : 'forms/reportPPS',
				method : 'POST',
				params : {
					predid : ETD.predid,
					action : 'checkOnEdit',
					fr : ETD.fr,
					cert : document.applet_adapter.getCertSer()

				},
				success : function(response) {
					var userName;
					var json = Ext.util.JSON.decode(response.responseText);
					if (json.isEdit == '0') {
						ETD.readOnlyReportPPS = 0;
					} else if (json.isEdit == '1') {
						ETD.readOnlyReportPPS = 1;
						userName = json.username;
					}

					var param = 'predid=' + ETD.predid + '&action=' + 0
							+ '&fr=' + ETD.fr;
					var htm = '<iframe id = "ifr" name = "frame"  frameborder="no" width="100%" height="100%"'
							+ 'src="forms/reportPPS?' + param + '"></iframe>';

					var viewWin = new Ext.Window(
							{
								id : 'PPSReport',
								plain : 'true',
								title : 'Отчет обработки вагонов-цистерн',
								width : Ext.getBody().getViewSize().width * .99,
								height : Ext.getBody().getViewSize().height * .99,
								layout : 'fit',
								resizable : true,
								modal : true,
								border : true,
								buttonAlign : 'center',
								autoScroll : false,
								closable : false,
								html : htm,

								listeners : {

									beforerender : function(component, eOpts) {

										if (ETD.fr == '5') {
											Ext.getCmp('genVU').disable();
											Ext.getCmp('genGU2b').disable();
										}
										if (ETD.readOnlyReportPPS == 1) {
											Ext.getCmp('genVU').disable();
											Ext.getCmp('genGU2b').disable();
										}
									}
								},

								buttons : [
										{
											text : 'Сформировать ВУ',
											id : 'genVU',
											disabled : false,
											handler : function() {
												var iframe = document
														.getElementById('ifr');
												var iframeDocument = iframe.contentDocument
														|| iframe.contentWindow.document;
												var params1 = '';
												var params2 = '';
												var arrayRow = iframeDocument
														.getElementsByTagName("tr");
												for (var i = 0; i < arrayRow.length; i++) {
													if (arrayRow[i].id == 'r1') {

														var div = arrayRow[i]
																.getElementsByTagName("div");
														for (var j = 0; j < div.length; j++) {

															if (div[j]
																	.getAttribute("name") == 'p17') {
																var valGu45Pod = div[j].innerHTML;
																// alert(valGu45Pod);

																if (valGu45Pod.length > 0) {
																	var childArray = arrayRow[i]
																			.getElementsByTagName("select");
																	for (var j = 0; j < childArray.length; j++) {
																		if (childArray[j]
																				.getAttribute("name") == 'act_name_p7') {
																			var val = childArray[j].options[childArray[j].selectedIndex].value;
																			if (params1.length == 0) {
																				params1 = val
																						+ ',';
																			} else {
																				params1 = params1
																						+ val
																						+ ',';
																			}

																			var divInRow = arrayRow[i]
																					.getElementsByTagName("div");

																			for (var j = 0; j < divInRow.length; j++) {

																				if (divInRow[j]
																						.getAttribute("name") == 'id') {
																					var valueId = divInRow[j]
																							.getElementsByTagName("textarea")[0].value;
																					params1 = params1
																							+ valueId
																							+ ',';
																				}

																				if (divInRow[j]
																						.getAttribute("name") == 'p19') {

																					var valueP_19 = divInRow[j]
																							.getElementsByTagName("textarea")[0].value;
																					if (valueP_19.length == 0) {
																						valueP_19 = ' ';
																					}
																					params1 = params1
																							+ valueP_19
																							+ ',';
																				}

																			}

																			// alert('params1
																			// ' +
																			// params1);

																		}

																	}
																}
															}
														}

													}
													if (arrayRow[i].id == 'r2') {

														var div = arrayRow[i]
																.getElementsByTagName("div");
														for (var j = 0; j < div.length; j++) {

															if (div[j]
																	.getAttribute("name") == 'p17_1') {
																var valGu45Pod = div[j].innerHTML;
																if (valGu45Pod.length > 0) {
																	var childArray = arrayRow[i]
																			.getElementsByTagName("select");

																	for (var j = 0; j < childArray.length; j++) {

																		if (childArray[j]
																				.getAttribute("name") == 'act_name_p7_1') {
																			var val = childArray[j].options[childArray[j].selectedIndex].value;

																			params1 = params1
																					+ val
																					+ ',';
																			// }
																			var divInRow = arrayRow[i]
																					.getElementsByTagName("div");

																			for (var j = 0; j < divInRow.length; j++) {

																				if (divInRow[j]
																						.getAttribute("name") == 'p1_1') {
																					var valueP_1_1 = divInRow[j]
																							.getElementsByTagName("textarea")[0].value;
																					if (valueP_1_1.length == 0) {
																						valueP_1_1 = ' ';
																					}
																					params1 = params1
																							+ valueP_1_1
																							+ ';';
																					// alert('params2
																					// ' +
																					// params1);
																				}
																				;

																			}

																		}
																	}
																}
															}
														}
													}
												}

												// alert('params1 ' + params1);
												Ext.Ajax
														.request({
															url : 'forms/reportPPS',
															method : 'POST',
															params : {
																predid : ETD.predid,
																action : 1,
																params1 : params1
															},
															success : function(
																	response) {
																var code = document
																		.getElementById('ifr');
																code.contentWindow.location
																		.replace(code.src);
																var json = Ext.util.JSON
																		.decode(response.responseText);

																code
																		.attachEvent(
																				"onload",
																				function() {
																					Ext.Msg
																							.alert(
																									'Сообщение',
																									json.message);
																				})
															}
														});
											}
										},
										{
											text : 'Сформировать ГУ-2б',
											id : 'genGU2b',
											disabled : false,
											handler : function() {

												var iframe = document
														.getElementById('ifr');
												var iframeDocument = iframe.contentDocument
														|| iframe.contentWindow.document;
												var params1 = '';
												var params2 = '';
												var arrayRow = iframeDocument
														.getElementsByTagName("tr");
												for (var i = 0; i < arrayRow.length; i++) {
													if (arrayRow[i].id == 'r1') {
														var childArray = arrayRow[i]
																.getElementsByTagName("select");
														for (var j = 0; j < childArray.length; j++) {
															if (childArray[j]
																	.getAttribute("name") == 'act_name_p7') {
																var val = childArray[j].options[childArray[j].selectedIndex].value;
																if (params1.length == 0) {
																	params1 = val
																			+ ',';
																} else {
																	params1 = params1
																			+ val
																			+ ',';
																}

																var divInRow = arrayRow[i]
																		.getElementsByTagName("div");

																for (var j = 0; j < divInRow.length; j++) {

																	if (divInRow[j]
																			.getAttribute("name") == 'id') {
																		var valueId = divInRow[j]
																				.getElementsByTagName("textarea")[0].value;
																		params1 = params1
																				+ valueId
																				+ ',';
																	}

																	if (divInRow[j]
																			.getAttribute("name") == 'p19') {

																		var valueP_19 = divInRow[j]
																				.getElementsByTagName("textarea")[0].value;
																		if (valueP_19.length == 0) {
																			valueP_19 = ' ';
																		}
																		params1 = params1
																				+ valueP_19
																				+ ',';
																	}

																}

																// alert('params1
																// ' + params1);

															}

														}

													}
													if (arrayRow[i].id == 'r2') {
														var childArray = arrayRow[i]
																.getElementsByTagName("select");

														for (var j = 0; j < childArray.length; j++) {

															if (childArray[j]
																	.getAttribute("name") == 'act_name_p7_1') {
																var val = childArray[j].options[childArray[j].selectedIndex].value;

																params1 = params1
																		+ val
																		+ ',';
																// }
																var divInRow = arrayRow[i]
																		.getElementsByTagName("div");

																for (var j = 0; j < divInRow.length; j++) {

																	if (divInRow[j]
																			.getAttribute("name") == 'p1_1') {
																		var valueP_1_1 = divInRow[j]
																				.getElementsByTagName("textarea")[0].value;
																		if (valueP_1_1.length == 0) {
																			valueP_1_1 = ' ';
																		}
																		params1 = params1
																				+ valueP_1_1
																				+ ';';
																		// alert('params2
																		// ' +
																		// params1);
																	}
																	;

																}

															}
														}
													}

												}

												Ext.Ajax
														.request({
															url : 'forms/reportPPS',
															method : 'POST',
															params : {
																predid : ETD.predid,
																action : 2,
																params1 : params1
															},
															success : function(
																	response) {
																var json = Ext.util.JSON
																		.decode(response.responseText);
																Ext.Msg
																		.alert(
																				'Сообщение',
																				json.message);

															}
														});

											}
										},
										{
											text : 'Печать',
											id : 'print',
											disabled : false,
											handler : function() {

												window.frames["frame"].focus();
												window.frames.print();

											}
										},
										{
											text : 'Закрыть',
											id : 'close',
											disabled : false,
											handler : function() {
												if (ETD.readOnlyReportPPS == 0) {
													Ext.Ajax
															.request({
																url : 'forms/reportPPS',
																method : 'POST',
																params : {
																	predid : ETD.predid,
																	action : 'close',
																	cert : document.applet_adapter
																			.getCertSer()
																},
																success : function(
																		response) {
																	ETDDocPPS_Role2
																			.reload();
																	viewWin
																			.destroy();
																}

															});
												}
												ETDDocPPS_Role2.reload();
												viewWin.destroy();

											}
										} ]
							});

					if (ETD.readOnlyReportPPS == 1) {
						Ext.MessageBox
								.show({
									title : "Сообщение",
									msg : "В данных момент отчет редактируется пользователем "
											+ userName
											+ " и будет открыт только на просмотр",
									buttons : Ext.MessageBox.OKCANCEL,
									fn : function(buttonId) {
										if (buttonId === "ok") {
											viewWin.show();
										}
									}
								});
					} else {
						viewWin.show();
					}

				}
			});

}

ETD.createPretension = function() {

	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selectionModel = grid.getSelectionModel();
	var selectedCount = selectionModel.getCount()
	if (selectedCount == 0) {
		Ext.MessageBox.alert('Ошибка', 'Выберите пакет документов');
		return;
	} else if (selectedCount > 1) {
		Ext.MessageBox.alert('Ошибка', 'Выберите один пакет документов');
		return;
	}
	var docArray = selectionModel.getSelections();
	var docname = docArray[0].get('name');
	if (docname != 'Пакет документов') {
		Ext.MessageBox.alert('Ошибка', 'Выберите пакет документов');
		return;
	}
	var idPackage = docArray[0].get('idpak');
	Ext.Ajax.request({
		url : 'forms/checkPretension',
		method : 'POST',
		params : {
			predid : ETD.predid,
			packId : idPackage
		},
		success : function(response) {

			var json = Ext.util.JSON.decode(response.responseText);
			if (json[0].message.length > 0) {
				Ext.MessageBox.alert('Сообщение', json[0].message);
			}
		}
	});
}

ETD.exportTorFromArchive = function() {
	var dateFromPeriod = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'repfromA',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'С',
		width : 150
	});
	var dateToPeriod = new Ext.form.DateField({
		xtype : 'datefield',
		id : 'reptoA',
		maxDate : new Date(),
		hidden : false,
		editable : false,
		fieldLabel : 'По',
		width : 150
	});

	combocrstoreDI.loadData(eval('(' + document.applet_adapter.getDI() + ')'));

	var grid = Ext.getCmp('tpanel').getActiveTab();

	var formTorReportFromArchive = new Ext.form.FormPanel(
			{
				id : 'formTorReportFromArchive',
				border : false,
				autoScroll : false,
				autoHeight : true,
				style : 'margin-left: 5px;margin-top:5px;margin-right: 5px',
				labelWidth : 120,
				items : [
						{
							xtype : 'label',
							text : 'Период формирования документа:',
							style : 'font-size: 12px; text-align: center; position: relative; left: 30px;'
						},
						dateFromPeriod,
						dateToPeriod,
						{
							xtype : 'multiselect',
							labelStyle : Ext.isIE ? 'width:120px;'
									: 'width:109px;',
							store : combocrstoreDI,
							width : 150,
							displayField : 'text',
							valueField : 'id',
							hidden : false,
							editable : false,
							mode : 'local',
							triggerAction : 'all',
							ddReorder: true, 
							selectOnFocus:true,
							id : 'di_num',
							fieldLabel : 'ДИ',
							allowBlank : true,
							width : 150
						},
						{
							xtype : 'textfield',
							width : 150,
							displayField : 'value',
							hidden : false,
							editable : true,
							triggerAction : 'all',
							id : 'vagon_num',
							fieldLabel : 'Номер вагона',
							allowBlank : true,
							validator : function(val) {
								return ETD.validateVagnum(val);
							}
						},
						{
							xtype : 'field',
							labelStyle : Ext.isIE ? 'width:120px;'
									: 'width:109px;',
							displayField : 'text',
							hidden : false,
							editable : false,
							mode : 'local',
							triggerAction : 'all',
							id : 'packageDoc',
							fieldLabel : 'Пакет',
							width : 150
						} ],
				buttons : [
						{
							text : 'Применить',
							handler : function() {
								var di = Ext.getCmp('di_num').getValue();
								var vagnum = Ext.getCmp('vagon_num').getValue();
								var packageDoc = Ext.getCmp('packageDoc')
										.getValue();
								if (di.length < 1) {
									di = null;
								}
								if (vagnum.length < 1) {
									vagnum = null;
								}
								if (packageDoc.length < 1) {
									packageDoc = null;
								}
								if (dateFromPeriod.getValue().length < 1)
									grid.getStore().baseParams.dateFromPeriod = null;
								else {
									grid.getStore().baseParams.dateFromPeriod = dateFromPeriod
											.getValue().format('Y-m-d');
								}
								if (dateToPeriod.getValue().length < 1)
									grid.getStore().baseParams.dateToPeriod = null;
								else {
									grid.getStore().baseParams.dateToPeriod = dateToPeriod
											.getValue().format('Y-m-d');
								}
								if (dateFromPeriod.getValue().length < 1) {
									Ext.MessageBox.alert('Сообщение',
											'Выберите корректные даты');
								}
								if (dateToPeriod.getValue().length < 1) {
									Ext.MessageBox.alert('Сообщение',
											'Выберите корректные даты');
								}

								if (dateToPeriod.getValue() < dateFromPeriod
										.getValue()) {
									Ext.MessageBox.alert('Сообщение',
											'Выберите корректные даты');
								} else {

									var param = 'act=export&predid='
											+ ETD.predid
											+ '&dateFromPeriod='
											+ dateFromPeriod.getValue().format(
													'Y-m-d')
											+ '&dateToPeriod='
											+ dateToPeriod.getValue().format(
													'Y-m-d') + '&di=' + di
											+ '&vagon=' + vagnum
											+ '&packageDoc=' + packageDoc;
									var form = Ext.DomHelper
											.append(
													document.body,
													{
														tag : 'form',
														method : 'post',
														action : 'forms/exportTOR_Archive'
																+ '?' + param
													});
									document.body.appendChild(form);
									form.submit();
									createwin.destroy();
								}
							}
						},

						{
							text : 'Отмена',
							handler : function() {
								createwin.destroy();
							}
						}, {
							text : 'Очистить',
							handler : function() {
								dateFromPeriod.setValue('');
								dateToPeriod.setValue('');
								Ext.getCmp('di_num').setValue('');
							}
						} ]
			})
	var createwin = new Ext.Window({
		closable : false,
		width : 300,
		height : 240,
		autoScroll : false,
		resizable : false,
		title : 'Введите параметры поиска',
		items : [ formTorReportFromArchive ],
		modal : true
	});
	createwin.show();

}

ETD.exportXML = function() {
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selectionModel = grid.getSelectionModel();
	var selectedCount = selectionModel.getCount();

	var docArray = selectionModel.getSelections();
	var docId = 0;
	var Ids = '';
	
	if (docArray.length == 0) {
		Ext.MessageBox.alert('Сообщение','Не выбрано ни одного документа!');
		return;
	}
	for (var j = 0; j < docArray.length; j++) {
		var docname = docArray[j].get('name');
		if (docname != 'Пакет документов' && docname != 'ФПУ-26 АСР'
				&& docname != 'Пакет документов РТК' && docname != 'ТОРГ-12' 
					&& docname != 'Пакет документов ЦСС') {
			Ext.MessageBox.alert('Сообщение',
					'Для выбранных типов документов выгрузка XML недоступна');
			return;
		}

		Ids = Ids + docArray[j].get('id') + ';';

	}
	var param = 'act=export&ids='+Ids;
	Ext.Ajax
			.request({
				url : 'forms/exportXMLArchive',
				method : 'POST',
				params : {
					act : 'get',
					ids : Ids,
					name : docname
				},
				success : function(response) {
					var resp = Ext.util.JSON.decode(response.responseText);
					if (resp.count == false) {
						Ext.MessageBox
								.alert('Сообщение',
										'В одном из выбранных Пакетов документов отсутствует ФПУ-26');
						return;
					}
					if (resp.index == 0) {
						Ext.MessageBox
						.alert('Сообщение',
								'Данные выбранных документов отсутствуют в таблице');
						return;
					}
					if (resp.index == 1) {
						Ext.MessageBox.alert('Сообщение',
								'Данные одного или нескольких документов отсутствуют в таблице',
								function () {
							Ext.DomHelper.append(document.body, {
								tag: 'iframe',
								id:'downloadXML',
								frameBorder: 0,
								width: 0,
								height: 0,
								css: 'display:none;visibility:hidden;height:0px;',
								src: 'forms/exportXMLArchive' + '?' + param
						})
						}
						)
						
					}
					if (resp.index == 2) {
						Ext.DomHelper.append(document.body, {
							tag: 'iframe',
							id:'downloadXML',
							frameBorder: 0,
							width: 0,
							height: 0,
							css: 'display:none;visibility:hidden;height:0px;',
							src: 'forms/exportXMLArchive' + '?' + param
					})
					
				}
				}
			})
			
}

ETD.exportPDF = function() {
	var grid = Ext.getCmp('tpanel').getActiveTab();
	var selectionModel = grid.getSelectionModel();
	var selectedCount = selectionModel.getCount();

	var docArray = selectionModel.getSelections();
	var id_pack = 0;
	var Ids = '';
	var idDoc = '';
	var choice = '';
	
	if (docArray.length == 0) {
		Ext.MessageBox.alert('Сообщение','Не выбрано ни одного пакета!');
		return;
	}

	for (var j = 0; j < docArray.length; j++) {
		var docname = docArray[j].get('name');
		if (docname == 'ТОРГ-12') {
		Ids = Ids + docArray[j].get('id') + ';';
		choice = choice + "doc"+';';
		}
		if (docname == 'Пакет документов'){
		Ids = Ids + docArray[j].get('idpak') + ';';
		choice = choice + "pack"+';';
		}
	}
	var param = 'act=export&ids='+Ids+'&choice='+choice;
	Ext.DomHelper.append(document.body, {
		tag: 'iframe',
		id:'exportPDF',
		frameBorder: 0,
		width: 0,
		height: 0,
		css: 'display:none;visibility:hidden;height:0px;',
		src: 'forms/exportPDF' + '?' + param
})
	
	
}

