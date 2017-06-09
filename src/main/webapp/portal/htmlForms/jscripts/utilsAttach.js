

function createAttachWin(param){
	/*	param:
	 * 	0 - доступ ко всем кнопкам
	 * 	1 - доступ к скачиванию
	 * 	2 - доступ к открытию
	 */
	
	var winDiv = document.createElement("div");
	winDiv.setAttribute("id", "attachWinId");
	//winDiv.setAttribute("style", "visibility:"+(isvisible?"visible":"hidden")+";z-index:11;background:#FFF;height:536px;left:0;position:absolute;top:0;width:446px;");
	winDiv.setAttribute("style", "z-index:11;background:#FFF;height:515px;width:445px;");
	
//	var titleDiv = document.createElement("div");
//	titleDiv.setAttribute("style","display:table;height:18.6px;left:30px;position:absolute;top:15px;width:156.6px;");
//	var title = document.createElement("div");
//	title.setAttribute("class", "defaulLabelItem");
//	title.setAttribute("style", "color:black;font-family:Arial;font-size:17px;font-weight:600;line-height:1.3;text-align:left;white-space:pre-wrap;");
//	title.innerHTML = 'Вложения';
//	titleDiv.appendChild(title);
//	winDiv.appendChild(titleDiv);
	
	
	var buttonAddDiv = document.createElement("div");
	buttonAddDiv.setAttribute("style", "height:18px;left:25px;min-height:22px;position:absolute;top:45px;width:120px;");
	var buttonAdd = document.createElement("button");
	buttonAdd.setAttribute('class', "defaultButtonItem");
	buttonAdd.setAttribute('style', "height:18px;min-height:22px;width:120px;");
	buttonAdd.innerHTML = 'Добавить файл';
	buttonAdd.setAttribute("value", "Добавить файл");
	if (param!=0) buttonAdd.setAttribute("disabled", "disabled");
	$(buttonAdd).on('click', function(){document.getElementById('attachFileUpload').click();});
	buttonAddDiv.appendChild(buttonAdd);
	winDiv.appendChild(buttonAddDiv);
	
	var upload = document.createElement("input");
	upload.setAttribute("type", "file");
	upload.setAttribute("id", "attachFileUpload");
	upload.setAttribute("name", "attachFileUpload");
	upload.setAttribute("style", "display:none;");
	$(upload).on('change', function() {attachFile();});
	winDiv.appendChild(upload);
	//$(winDiv).on('change', '#attachFileUpload', attachFile());
	
	
	var buttonRemoveDiv = document.createElement("div");
	buttonRemoveDiv.setAttribute("style", "height:18px;left:160px;min-height:22px;position:absolute;top:45px;width:120px;");
	var buttonRemove = document.createElement("button");
	buttonRemove.setAttribute('class', "defaultButtonItem");
	buttonRemove.setAttribute('style', "height:18px;min-height:22px;width:120px;");
	buttonRemove.innerHTML = 'Удалить файл';
	if (param!=0) buttonRemove.setAttribute("disabled", "disabled");
	buttonRemove.setAttribute("value", "Удалить файл");
	$(buttonRemove).on('click', function(){removeAttachFile();});
	buttonRemoveDiv.appendChild(buttonRemove);
	winDiv.appendChild(buttonRemoveDiv);
	
	
	var buttonCancelDiv = document.createElement("div");
	buttonCancelDiv.setAttribute("style", "height:18px;left:295px;min-height:22px;position:absolute;top:45px;width:120px;");
	var buttonCancel = document.createElement("button");
	buttonCancel.setAttribute('class', "defaultButtonItem");
	buttonCancel.setAttribute('style', "height:18px;min-height:22px;width:120px;");
	buttonCancel.innerHTML = 'Назад';
	buttonCancel.setAttribute("value", "Назад");
	$(buttonCancel).on('click', function(){showFormWin();});
	buttonCancelDiv.appendChild(buttonCancel);
	winDiv.appendChild(buttonCancelDiv);
	
	
	var labelDiv = document.createElement("div");
	labelDiv.setAttribute("style","display:table;height:18.6px;left:30px;position:absolute;top:85px;width:156.6px;");
	var label = document.createElement("div");
	label.setAttribute("class", "defaulLabelItem");
	label.setAttribute("style", "color:black;font-family:Arial;font-size:13px;line-height:1.3;text-align:left;white-space:pre-wrap;");
	label.innerHTML = 'Список вложений:';
	labelDiv.appendChild(label);
	winDiv.appendChild(labelDiv);
	
	
	var buttonDownloadDiv = document.createElement("div");
	buttonDownloadDiv.setAttribute("style", "height:18px;left:160px;min-height:22px;position:absolute;top:80px;width:120px;");
	var buttonDownload = document.createElement("button");
	buttonDownload.setAttribute('class', "defaultButtonItem");
	buttonDownload.setAttribute('style', "height:18px;min-height:22px;width:120px;");
	//if (param==2) buttonDownload.setAttribute("disabled", "disabled");
	if (param!=2){
		buttonDownload.innerHTML = 'Сохранить файл';
		buttonDownload.setAttribute("value", "Сохранить файл");
	} else {
		buttonDownload.innerHTML = 'Открыть файл';
		buttonDownload.setAttribute("value", "Открыть файл");
	}
	$(buttonDownload).on('click', function(){getAttach(param);});
	buttonDownloadDiv.appendChild(buttonDownload);
	winDiv.appendChild(buttonDownloadDiv);
	
	
	/*var buttonBrowseDiv = document.createElement("div");
	buttonBrowseDiv.setAttribute("style", "height:18px;left:295px;min-height:22px;position:absolute;top:80px;width:120px;");
	var buttonBrowse = document.createElement("button");
	buttonBrowse.setAttribute('class', "defaultButtonItem");
	buttonBrowse.setAttribute('style', "height:18px;min-height:22px;width:120px;");
	buttonBrowse.innerHTML = 'Открыть файл';
	if (param==1) buttonBrowse.setAttribute("disabled", "disabled");
	buttonBrowse.setAttribute("value", "Открыть файл");
	$(buttonBrowse).on('click', function(){getAttach();});
	buttonBrowseDiv.appendChild(buttonBrowse);
	winDiv.appendChild(buttonBrowseDiv);*/
	
	
	var attachDiv = document.createElement("div");
	attachDiv.setAttribute("id", "attachDivId");
	attachDiv.setAttribute("style","overflow-y:scroll;height:400px;left:25px;position:absolute;top:110px;width:380px;border:1px solid #000;");
	winDiv.appendChild(attachDiv);
	
	if (document.getElementById('windowForAttach').childNodes.length>0){
		var child = document.getElementById('windowForAttach').childNodes[0];
		document.getElementById('windowForAttach').removeChild(child);
	}
	document.getElementById('windowForAttach').appendChild(winDiv);
	
	windowForAttach = document.getElementById("windowForAttach");
	$(windowForAttach).dialog({
		resizable : false,
		dialogClass: "no-close",
		position : 'center',
		closeOnEscape : false,
		draggable : false,
		title : 'Вложения',
		height : 540,
		width : 450,
		modal : true,
		autoOpen : false
	});
	
	attachInit();
}

function attachInit(){
	//var attachDiv = document.getElementById('attachDivId');
	var data = xfdlForm.getElementsByTagName("data");
	for (var j=0;j<data.length;j++){
		if (data.item(j).getAttribute('sid')!=null&&
				data.item(j).getAttribute('sid').indexOf('data')==0){
			var fname = data.item(j).getAttribute('sid');
			var name = data.item(j).getElementsByTagName("filename").item(0).getValue();
			showAttachFile(name, fname, '1');
		}
	}
	$(windowForAttach).dialog("open");
}


function getAttach(param){
	var attachDiv = document.getElementById('attachDivId');
	for (var i=0;i<attachDiv.childNodes.length;i++){
		var attach = attachDiv.childNodes[i];
		if (attach.getAttribute('isSelect')==1){
			if (param==0){
				var fname = attach.getAttribute('fname');
				var isLoad = attach.getAttribute('isLoad');
				parent.window.location = 'forms/attach.get?fname='+fname+'&isload='+isLoad+'&name="'+attach.innerHTML+'"';
			} else {
				var sid = attach.getAttribute('fname');
				var data = xfdlForm.getElementsByTagName("data");
				for (var j=0;j<data.length;j++){
					if (data.item(j).getAttribute('sid')==sid){
						var ndat = data.item(j);
						var mimetype = null;
						var filename = null;
						var mimedata = null;
						var child = ndat.firstChild;
						while (child){
							if (child.tagName!=null){
								switch (child.tagName.replace(/.+:/g, '')){
									case 'mimetype': mimetype = child; break;
									case 'filename': filename = child; break;
									case 'mimedata': mimedata = child; break;
								}
							}
							child = child.nextSibling;
						}
						var typeEncoding = mimedata.getAttribute('encoding');
						if (typeEncoding.toLowerCase().trim() == "base64-gzip") {
							var decodeImg = window.parent.document.applet_adapter.decompressBase64GZip(mimedata.getValue());
							//var stat = document.utilsApplet.saveAttachTempFile(decodeImg);
							var stat = window.parent.document.applet_adapter.saveAttachTempFile(decodeImg);
							if (stat==0){
								parent.window.location = 'forms/attach.save?fname="'+filename.getValue()+'"&type="'+mimetype.getValue()+'"';
							}
						}
					}
				}
			}
		}
	}
}


function showFormWin(){
	//var win = document.getElementById('attachWinId');
	//win.style.visibility = 'hidden';
	$(windowForAttach).dialog("close");
}

function showAttachFile(name, fname, isload){
	var attachDiv = document.getElementById('attachDivId');
	
	var file = document.createElement("div");
	$(file).on('mouseover', function(){this.style.backgroundColor = '#DFE8F6';});
	$(file).on('mouseout', function(){this.style.backgroundColor = '#FFFFFF';});
	$(file).on('click', function(){attachSelect(this);});
	file.setAttribute("isSelect", "0");
	file.setAttribute("isLoad", isload);
	file.setAttribute("fname", fname);
	file.setAttribute("class", "defaulLabelItem");
	file.setAttribute("style", "cursor:pointer;padding-left:10px;color:black;font-family:Arial;font-size:13px;line-height:1.3;text-align:left;white-space:pre-wrap;");
	file.innerHTML = name;
	attachDiv.appendChild(file);
}

function attachSelect(file){
	var attachDiv = document.getElementById('attachDivId');
	for (var i=0;i<attachDiv.childNodes.length;i++){
		var attach = attachDiv.childNodes[i];
		if (attach.getAttribute('isSelect')==1){
			attach.style.backgroundColor = '#FFFFFF';
			attach.setAttribute("isSelect", "0");
			$(attach).on('mouseout', function(){this.style.backgroundColor = '#FFFFFF';});
		}
	}
	$(file).unbind('mouseout');
	file.style.backgroundColor = '#DFE8F6';
	file.setAttribute("isSelect", "1");
}


function getNewAttachFileNum(data){
	var num = 1;
	var attachFiles = new Array();
	for (var j=0;j<data.length;j++){
		if (data.item(j).getAttribute('sid')!=null&&
				data.item(j).getAttribute('sid').indexOf('data')==0){
			attachFiles.push(parseInt(data.item(j).getAttribute('sid').replace('data','')));
		}
	}
	var arr = attachFiles.sort(function(a,b){return a-b;});
	for (var i=0;i<attachFiles.length;i++){
		if (arr[i]==num) num++;
	}
	return num;
}


function attachFile(){
	//var self = this;
	var data = xfdlForm.getElementsByTagName("data");
	var num = getNewAttachFileNum(data);
	
	$.ajaxFileUpload(
		{
			url:'forms/attach.form?num='+num,
			secureuri:false,
			fileElementId: 'attachFileUpload',
			dataType: 'json',
			beforeSend:function(){
				window.parent.document.getElementById('loading').style.visibility = 'visible';
			},
			complete:function(){
				window.parent.document.getElementById('loading').style.visibility = 'hidden';
			},				
			success: function (data, status){
				if (data!=''){
					
					var page = xfdlForm.getElementsByTagName("page").item(0);
					var file = xfdlForm.createElement('data');
					file.setAttribute('sid', data.fname);
					
					var mimetype = xfdlForm.createElement('mimetype');
					var mimetypeText = xfdlForm.createTextNode(data.type);
					mimetype.appendChild(mimetypeText);
					file.appendChild(mimetype);
					
					var filename = xfdlForm.createElement('filename');
					var filenameText = xfdlForm.createTextNode(data.name);
					filename.appendChild(filenameText);
					file.appendChild(filename);
					
					var datagroup = xfdlForm.createElement('datagroup');
					var datagroupref = xfdlForm.createElement('datagroupref');
					var datagrouprefText = xfdlForm.createTextNode('attachments');
					datagroupref.appendChild(datagrouprefText);
					datagroup.appendChild(datagroupref);
					file.appendChild(datagroup);
					
					/*var mimedata = xfdlForm.createElement('mimedata');
					mimedata.setAttribute('encoding', 'base64-gzip');
					var mimedataText = xfdlForm.createTextNode(data.code);
					mimedata.appendChild(mimedataText);
					file.appendChild(mimedata);*/
					
					page.appendChild(file);
//					console.log(page);
					showAttachFile(data.name, data.fname, '0');
				}
			}
		}
	)
	return false;
}


var attachDeleteMass = '';

function removeAttachFile(){
	
	var attachDiv = document.getElementById('attachDivId');
	top: for (var i=0;i<attachDiv.childNodes.length;i++){
		var attach = attachDiv.childNodes[i];
		if (attach.getAttribute('isSelect')==1){
			var sid = attach.getAttribute('fname');
			var data = xfdlForm.getElementsByTagName("data");
			for (var j=0;j<data.length;j++){
				if (data.item(j).getAttribute('sid')==sid){
					if (attach.getAttribute('isLoad')=='0'){
						$.ajax({
							url: 'forms/attach.remove',
							data: {fname: sid, act: '0'},
							type: 'POST',
							async : true,
							success: function (resp, textStatus, jqXHR){
								if (resp=='1'){
									attachDiv.removeChild(attach);
									data.item(j).parentNode.removeChild(data.item(j));
								}
							},
							error: function (jqXHR, textStatus, errorThrown){}
						});
					} else {
						attachDeleteMass += sid+':::';
						attachDiv.removeChild(attach);
						data.item(j).parentNode.removeChild(data.item(j));
					}
					break top;
				}
			}
		}
	}
//	console.log(xfdlForm.getElementsByTagName("page").item(0));
}

