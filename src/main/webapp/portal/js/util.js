
$.fn.center = function(){
		$(this).each(function(){
			
				var obj = $(this);
				var win = $(window);
				obj.css({position: 'absolute', 
					left: win.width()/2-obj.width()/2, 
					top: win.height()/2-obj.height()/2});
				
			});		
	};
	
$.fn.markInvalid = function(){
	
	$(this).each(function(){
		$(this).css({backgroundColor:'#ffbbbb'});
		this.invalid = true;
		
	});
};

$.fn.markValid = function(){
	
	$(this).each(function(){
		$(this).css({backgroundColor:'transparent'});
		this.invalid = false;
	});
};
	
$.fn.validate = function(){
	$(this).each(function(){
		if(this.tagName.toUpperCase()=='INPUT' && this.mandatory!='off'){
				if(this.value==''){
					GLOBAL('input', 'invalid');
					$(this).markInvalid();
					return;
				}
				if(this.format=='number'){
					if(this.value.search('[^0-9]')!=-1){
						GLOBAL('input', 'invalid');
						$(this).markInvalid();
						return;
					}					
				}		
				
				if(this.format=='date'){
					if(!(this.value.search('[0-9]{2}[.]{1}[0-9]{2}[.]{1}[0-9]{4}')==0 && this.value.length==10))
					{
						GLOBAL('input', 'invalid');
						$(this).markInvalid();
						return;
					}
					else{
						var day = parseInt(this.value.substring(0, 2),10);
						var month = parseInt(this.value.substring(3,5),10);
						var year = parseInt(this.value.substring(6, 10), 10);

						if(day<=0 || day>31 || month<=0 || month>12 || year<=1990 || year>2100){
							$(this).markInvalid();
							GLOBAL('input', 'invalid');
							return;
						}

					}						
				}

				$(this).markValid();						
				
		}	
	});
};


	
$(document).ready(function(){
	
	applet_adapter = document.getElementById('applet');
	
	//var content_height = $(window).height()-$('.main_footer').height()-$('.main_header').height();
	//$('.content_area').css({height: content_height});
	
	if(window.logon){
		prep_page_log();
	}
	else{
		prep_page_unlog();
	}
	
	$('div.main_header').show();
	
	var content_height = $(window).height()-$('.main_footer').height()-$('.main_header').height();
	$('.content_area').css({height: content_height});

	
	$('.close_btn').attr({'src': 'pics/buttons/button_x.png',
		'alt': 'закрыть'
	});
	$('.close_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/button_x_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/button_x.png';

	});
	
	$('.forward_btn').attr({'src': 'pics/buttons/button_for.png',
		'alt': 'вперед'
	});
	$('.forward_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/button_for_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/button_for.png';

	});
	
	$('.back_btn').attr('src', 'pics/buttons/button_bac.png');
	$('.back_btn').attr('alt', 'назад');

	$('.back_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/button_bac_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/button_bac.png';

	});	
	
	$('.accept_btn').attr('src', 'pics/buttons/button_app.png');
	$('.accept_btn').attr('alt', 'принять');
	$('.accept_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/button_app_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/button_app.png';

	});	
	
	$('.send_btn').attr('src', 'pics/buttons/send.png');
	$('.send_btn').attr('alt', 'отправить');
	$('.send_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/send_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/send.png';

	});		
	
	$('.close_btn').attr({'src': 'pics/buttons/button_x.png',
		'alt': 'закрыть'
	});
	
	$('.wizard_content_block').css({
		'width': $('.wizard_content').width()/3-15, 
		'height': $('.wizard_content').height()/2-15
	});

	
	$('.button_panel').css('cursor', 'pointer');	
	
	hide_pbar();
});


/* INVOKED BEFORE 
 DOCUMENT.READY */
function main(){
	
	/*--------------------------------- DOM/CSS ROUTINES  ------------------------------*/

	$('.pbfog').css({
		height: $(window).height(),
		width: $(window).width(), 
		opacity: .3

	});

	

	$('.progress_bar_item').center();
	$('.progress_bar_pane').center();
	
	
	$('div.front').css('width', $('div.front img').width()*2+58);
	$('div.join').center();	
	$('div.front').center();

	$('.content_area').css({height: 0});
	
	$('.close_btn').bind({
		click: function(){
		close_wizard();
	}});
	
	$('.wizard').center();
	
	$('#back_1_btn').bind('click', function(){

		
		switchPage('#wizard0', '#wizard1');
	});
	
	$('#back_2_btn').bind('click', function(){
		switchPage('#wizard1', '#wizard2');
	});
	
	$('#back_3_btn').bind('click', function(){
		switchPage('#wizard2', '#wizard3');
	});
	
	$('#back_4_btn').bind('click', function(){
		switchPage('#wizard3', '#wizard4');
	});
	
	$('#forward_2_btn').bind('click', function(){
		
			
			if(!validate_input())
				return;
		
			/* ЗАПИХИВАЕМ ВВОД ПОЛЬЗОВАТЕЛЯ В GLOBAL('xforms')
			 * СТРУКТУРА ХРАНИТ ЗНАЧЕНИЯ ПОЛЕЙ XFORMS
			 *  
			 *  */		
		var m = GLOBAL('htmlxformsmap');
		assert(m!=null, 'not htmlxformsmap');
		var map = m.map;
		var xforms = {};
		for(var i=0; i<map.length; i++){
			if(document.getElementById(map[i].value)){
				var type = document.getElementById(map[i].value).type;
				if(type=='text')
					xforms[map[i].key] = document.getElementById(map[i].value).value;
				if(type=='checkbox')
					xforms[map[i].key] = document.getElementById(map[i].value).checked ? 'true' : '';

			}
		}
		

		xforms['where_id'] = GLOBAL('target_wrkid');
		GLOBAL('xforms', xforms);
	

		switchPage('#wizard3', '#wizard2');
	});
	
	$('#forward_3_btn').bind('click', function(){
		
		/* GENERATE SUBMIT PAGE CONTENT 
		 * 
		 * НИКОМУ НЕ РАЗБИРАТЬСЯ!
		 * */
		var m = GLOBAL('htmlxformsmap');
		assert(m, 'htmlxformsmap not');
		var map = m.map;
		assert(map, 'no map');
		var html = '';

		for(var i=0; i<map.length; i++){
			if(map[i].displayOnSubmit==1){
			html += '<div>'+
			'<span>'+map[i].displayName+'</span>'+
			'<input value="'+GLOBAL('xforms')[map[i].key]+'" readonly="">'+
			'</div>';
			}
		}

		//assert(html2, 'нет хтмл отправки');
		$('#send_content_div').html(html);
		
		icon_applet = document.getElementById('iconsApplet');
		icon_applet.delete_all();
		icon_applet.pick_images();
		
		switchPage('#wizard4', '#wizard3');
	});
	

	
	$('.watch_btn').attr('src', 'pics/buttons/watch.png');
	$('.watch_btn').attr('alt', 'просмотреть электронный документ');
	$('.watch_btn').hover(
			function(event){
				event.target.src = 'pics/buttons/watch_o.png';
			}, 
	
		function(event){
				event.target.src = 'pics/buttons/watch.png';

	});	
	
	$('.watch_btn').click(function(){
		

		viewDoc();
		
	});
	
	$('.send_btn').click(function(){
		
		save_doc();
		


	});
	

	$('#datepicker').datepicker({dateFormat: 'dd.mm.yy'});
	
	
	
	/*---------------------------- LOGIN -------------------------*/
	
	show_pbar();
	//infinite_pbar();
	
	 logon = etd();



}

/*
 * 
 * 
 * JOIN PAGE ROUTINES
 */

function join(){
	$('.wizard').center();
	

	
	$('.pbfog').css({
		height: $(window).height(),
		width: $(window).width(), 
		opacity: .3

	});
	
	$('div.front').css('width', $('div.front img').width()*2+58);
	

	$('.progress_bar_item').center();
	$('.progress_bar_pane').center();
	
	$('#forward_10_btn').click(function(){
		
/*		var fill = $('#fname').val() && $('#lname').val() &&
		$('#mname').val() && $('#org').val()&&$('#adress').val();*/
		var fill = true;
		$('.pinput').each(function(){
			if(this.value==''){
				fill = false;
				return false;
			}
		});
		
	//	$('.mail').each(function(){
		//	fill&=this.value.search('([a-zA-Z0-9._]@[.]([a-zA-Z]{2,6})');
	///		
	//	});
		
		if(!fill){
			message('необходимо заполнить поля формы')
			return;
		}
		else{
		switchPage('#wizard14', '#wizard10');
		/*show_pbar();
		infinite_pbar();*/

				
		}
	});
	
	/*$('#forward_11_btn').click(function(){
		switchPage('#wizard12', '#wizard11');
		
		show_pbar();
		infinite_pbar();

		$.ajax('joinOrgs', {
			timeout: 30000,
			success: function(data, textStatus){

			$('#wizard_3').html(data);
			
			hide_pbar();
		},
			error:function(jqXHR, textStatus, errorThrown){
			hide_pbar();
			critical_error(errorThrown);
		}
				
		});
		
	});*/
	
	$('.send_btn').click(function(){
		join_submit();
	});
	
/*$('#forward_12_btn').click(function(){
		
		
		
		
		

		$('#wizard_2 .wizard_content_block img').each(function(){
			if(this.clicked){
			
				$('#exchangeDocsDiv').append($(this.parentNode).clone(true));
			}	
				
		});
		
		$('#wizard_3 .wizard_content_block img').each(function(){
			if(this.clicked){
				
				$('#partnersDiv').append($(this.parentNode).clone(true));
			}	
				
		});
		
		
		
		switchPage('#wizard14', '#wizard12');
	});*/
	
	
	
	$('#forward_14_btn').click(function(){
		$('#regOrg').val($('#org').val());
		$('#regName').val($('#name').val());
		$('#regAddr').val($('#adress').val());
		$('#regPhone').val($('#phone').val());
		$('#regNum').val($('#number').val());
			$('#checkedTypesDiv').html('');
		

		//$('#checkedTypesDiv').append('<div class="wizard_content_block">');
		$($('#wizard_5').find("TR")).each(function(){
			if($(this).find("input").attr("CHECKED")){
				$('#checkedTypesDiv').append('<div>'+$(this).find("span").html()+'</div>');
			}				
		});				
		if($('#checkedTypesDiv').html()==''){
			$('#checkedTypesDiv').html('(виды не выбраны)');
	}
		
		switchPage('#wizard13', '#wizard14');
	});
	
	$('#back_14_btn').click(function(){
		switchPage('#wizard10', '#wizard14');
	});
	
	$('#back_13_btn').click(function(){
		switchPage('#wizard14', '#wizard13');
	});
	/*
	$('#back_14_btn').click(function(){
		switchPage('#wizard12', '#wizard14');
	});
	
	$('#back_13_btn').click(function(){
		//switchPage('#wizard12', '#wizard13');
		switchPage('#wizard14', '#wizard13');
	});*/
	
	$('.close_btn').click(function(){
		$('.wizard').hide();
		document.location = 'index.jsp';
	});
	
	/*$('#wizard_2 .wizard_content_block img').live('click',function(){

		if(!this.clicked)
			this.clicked = false;
		
		this.clicked ^= true;
		
		var src = this.src;
		var alt = this.alt;
		this.src = alt;
		this.alt = src;
	});
	
	$('#wizard_3 .wizard_content_block img').live('click',function(){
		if(!this.clicked)
			this.clicked = false;
		
		this.clicked ^= true;
		
		if(this.clicked)
		$(this).css({'border-color': 'red'});
		else
		$(this).css({'border-color': 'transparent'});
	});	*/
	
	//$('#wizard10').show();
	switchPage('#wizard10', '#wizard-1');
	
	
	$('.pinput').first().each(function(){this.focus();});
	
	
	$('.pinput').first().each(function(){this.focus();});
}

function prep_page_log(){
	//document.location='index.jsp';//'indexscan.html';
	$('.main_header .link_log').show();
	$('.main_header .link').hide();
	
	
	$('.join').hide();
	$('.front').show();
	
	

}

function prep_page_unlog(){
	$('.main_header .link_log').hide();
	$('.main_header .link').show();
	
	$('.join').show();
	$('.front').hide();
}


/*function open_wizard(){

	
	$('.front').hide();
	//$('#wizard0').show();
	switchPage('#wizard0','#wizard-1');
	
	show_pbar();
	infinite_pbar();
	
	GLOBAL('*', '');
	wizard_applet = document.getElementById('scan_applet');
	wizard_applet.delete_images();
	
	
	var docsToCr = eval('('+applet_adapter.getDocsToCreate()+')');
	var data = docsToCr.data;
	var html = '';
	

	if(data === undefined){
		hide_pbar();
		error_msg("Маршрутизация не настроена");
		
		return;
	}
	
	for(var i=0; i<data.length; i++){
		html+='<div id="wizard_doc'+i+'" class="wizard_content_block wizard_doc"> '+
			'<img alt="" src="pics/misc/Doc_s.png">'+
			'<div>'+data[i].docname+'</div>'+
			'</div>';
	}
	

	$('#wizard_docs').html(html);
	for(var i=0; i<data.length; i++){
		
		document.getElementById('wizard_doc'+i).template = data[i].docname;
		document.getElementById('wizard_doc'+i).docid = data[i].docid;
	
	}

	// обработчик клика типа док-та
	$('.wizard_doc img').click(
			function(){
				try{
				
				switchPage('#wizard1', '#wizard0');
				
		var el = this.parentNode;
		

		show_pbar();

		infinite_pbar();

		assert(el.template!=null, 'template value  null');
		

		get_template(' '+el.template);
		GLOBAL('template', el.template);
		GLOBAL('docid', el.docid);


		var docHtml = eval('('+applet_adapter.getDocHTML(GLOBAL('docid'))+')');

		GLOBAL('html', docHtml.html);
		GLOBAL('htmlxformsmap', docHtml.htmlxformsmap);
		$('#wizard_fill').html(GLOBAL('html'));
		$('.badInputPopup').detach();
		$(document.body).append("<div class='badInputPopup' style='display:none;font-size: 12px;position: absolute; left:0; top:0; width:200px; height: 30px; background-color: white; color: red'>Значение введено не верно. Пожалуйста, повторите попытку</div>");
		$('#wizard_fill input').each(function(){

				$(this).mouseover(function(e){
					if(this.invalid){
						//$(document.body).append("<div id='inputFailPopup' style='font-size: 12px;position: absolute; width:200px; height: 30px; background-color: white; color: red'>Значение введено не верно. Пожалуйста, повторите попытку</div>");
						$('.badInputPopup').show();
						//var cursor = getMouseCoords(e);
						
						$('.badInputPopup').css({left:e.pageX+5, top:e.pageY});
					}
				});
				
				$(this).mousemove(function(e){
					if(this.invalid){
						//var cursor = getMouseCoords(e);
						$('.badInputPopup').css({left:e.pageX+5, top:e.pageY});
					}
				});
				
				$(this).mouseout(function(e){
					$('.badInputPopup').hide();//css({left:cursor.x, top:cursor.y});
				});
			
		});
		
		
		$('#wizard_fill input').change(function(){
			$(this).validate();
		});
		$('#wizard_fill input').blur(function(){
			$(this).validate();
		});		
		

		var recipData = eval('('+applet_adapter.getRecipientsData(GLOBAL('docid')) +')');

		
		var data = recipData.data;
		if(data === undefined){
			hide_pbar();
			error_msg("Маршрутизация не настроена");
			
			return;
		}
		
		$('#wizard_recipients').html('');
		
		
		for(var i = 0; i<data.length; i++){
			$('#wizard_recipients').append('<div class="wizard_content_block wizard_recip" id="wizard_recip'+i+'" >'+
				'<img alt="" src="joinOrgLogoBl?t='+ data[i].wrkid +'" >'+
			'</div>');
		}
		
		for(var i = 0;i<data.length; i++){
			document.getElementById('wizard_recip'+i).okpo = data[i].okpo;
			document.getElementById('wizard_recip'+i).name = data[i].name;
			document.getElementById('wizard_recip'+i).wrkid = data[i].wrkid;

		}

		
		// обработчик клика получателя
		$('.wizard_recip img').click(function(){
			
			switchPage('#wizard2', '#wizard1');			


			
			
			var el = this.parentNode;
			var person_data = eval('('+applet_adapter.getUserRequisites()+')');

			
			assert(person_data, 'no persondata');
			GLOBAL('persondata', person_data);
			
			$('#imgdiv img').attr('src', el.firstChild.src);
			$('#v4dNameField').val(person_data.wrkname);
			$('#stampField').val(person_data.stamp);
			$('#railwayField').val(person_data.dorname);
			$('#ownerField').val(el.name);
			$('#okpoField').val(el.okpo);
			
			

			GLOBAL('target_okpo', el.okpo);
			GLOBAL('target_icon', el.firstChild.src);
			GLOBAL('target_wrkid', el.wrkid);
			
	

			
			$('#dateField').datepicker({     
				beforeShow: function(){
			
					var left = $('#wizard_fill').offset().left+$('#wizard_fill').width()-$('#ui-datepicker-div').width()-$('#dateField').offset().left;
					var top = $('#wizard_fill').offset().top+$('#wizard_fill').height()-270-$('#dateField').offset().top;
					$('#ui-datepicker-div').css({marginLeft:left +'px', marginTop:top+'px'});
			},
			
				dateFormat: 'dd.mm.yy'
			});

			
			$('#wizard_fill input').each(function(){
				if(this.value==''){
					this.focus();
					return false;
				}
			});

			
		});
		
		
		hide_pbar();

			}
				catch(e){
					critical_error(e.message);
				}
	}); 


	
	hide_pbar();
	
//}
catch(e){
	critical_error(e.message);
}
}*/


function close_wizard(){
	$('.front').show();
	$('.wizard').hide();
}

function get_template(template){


		applet_adapter.createdocOCO(template,false);

}

function jamin_data(){
	

	applet_adapter.jamin_xfdl($.toJSON(GLOBAL('xforms')));
	
	
}

function join_submit(){
	$('body').append('<div id="dialog_accept" title="Сообщение">'+
			'<p align="center">Отправить заявку?</p>'+
			'</div>');
	
	
	
	$('#dialog_accept').dialog({
		draggable:false,
		width: 400,
		height: 220,
		modal: true,
		resizable: false,
		closable: false,
		
		buttons: {
		'Принять':function(){
		
		var data = {};
		data.name = $('#regName').val();
		/*data.lname = $('#regLName').val();
		data.mname = $('#regMName').val();*/
		data.org = $('#regOrg').val();
		data.addr = $('#regAddr').val();
		data.phone = $('#regPhone').val();
		data.number = $('#regNum').val();
		
		//data.partners = [];	
		//data.docs = [];
		data.checkTypes = [];
		//data.icon = $('#icon').attr('src');
		/*
		data.icon = $('#icon').attr('src');
		$('#partnersDiv .wizard_content_block img').each(function(){
		
			data.partners.push($(this).attr('alt'));
		});	*/
		/*$('#exchangeDocsDiv .wizard_content_block div').each(function(){
	
			data.docs.push($(this).html());
		});*/
		$($('#checkedTypesDiv').find('div')).each(function(){			
			data.checkTypes.push($(this).html());
		});
		$.ajax('joinSubmit',{type: 'POST', data: {data: $.toJSON(data)},
			success: function (data, textStatus, jqXHR){
			
			//message('Заявка отправлена');
			
			$('body').append('<div id="dialog_accept" title="Сообщение">'+
					'<p align="center">Заявка принята</p>'+
					'</div>');
			
			
			
			$('#dialog_accept').dialog({
				close: function(){
				document.location='index.jsp';
			},
				draggable:false,
				width: 400,
				modal: true,
				height: 220,
				resizable: false,
				closable: false,
				buttons: {'Закрыть': function(){document.location='index.jsp';} }
			
			});
				
		},
		
		error: function (jqXHR, textStatus, errorThrown){
			critical_error(errorThrown);
		}
		
		
		});
		
		
		$('#dialog_accept').dialog('close');		
		$('#dialog_accept').detach();
		//document.location = 'index.jsp';
		
	}, 'Закрыть':function(){		$('#dialog_accept').dialog('close');		
	$('#dialog_accept').detach();}
		
	}});
}


/* ------------------------  UTILS -----------------*/

function validate_input(){
	
	GLOBAL('input', '');
	$('#wizard_fill input').validate();
	if(GLOBAL('input')=='invalid')
		return false;
	return true;
	
}

function switchPage(newpage, oldpage){
	//$(oldpage).hide();
	//$(newpage).show();
	$(oldpage).animate({opacity:0},'slow');
	$(oldpage).hide();
	$(newpage).show();
	$(newpage).animate({opacity:1},'slow');	
}

function assert(exp, comment){

	if(!exp){
		var err = 'assertion failed! (Ошибка)\n'+
		comment == null ? 'что-то пошло не так!':comment ;
				
		critical_error(comment);
	}
}

function okpo_filter(){
	
	var filter = $('#okpo_filter').val();

	var el;
	var i = 0;
	while((el=document.getElementById('wizard_recip'+i))!=null)
	{

		if(el.okpo.toString().search(filter)==-1){
			
			$(el).hide();
		}
		else{
			$(el).show();
		}
		i++;
	}
	
	
}

/* GLOBAL DATA OBJECT
 * to clear call GLOBAL ('*', '')
 * */
var GLOBAL = function (){
	var data = {

	};
	
	return function(variable, value){	
		
		if(variable=='*'){
			for(var prop in data){
				data[prop] = value;
			}		
			return;
		}
		
		if(value==null){
			return data[variable];
		}
		else{
			var v = data[variable];
			data[variable]=value;
			return v;
		}
	};
	
}();

function hide_pbar(){
	
	if(window.pbar_timer!=undefined){
		clearInterval(window.pbar_timer);
		 pbar_timer = null ;
	}
	

	$('.progress_bar_item').hide();
	$('.progress_bar_pane').hide();
	$('.pbfog').hide();
	


}

function sign_pbar(){
	$('.pbline').css({width:0});
	$('.progress_bar_item').show();
	$('.pbfog').show();
	$('.progress_bar_item').show();
	$('#CP').show();
	//infinite_pbar();
}

function sending_pbar(){
	$('.pbline').css({width: 0});
	$('.progress_bar_item').show();
	$('.pbfog').show();
	$('.progress_bar_item').show();
	$('#sending').show();
	//infinite_pbar();	
}

function show_pbar(){
	$('.pbline').css({width:0});
	$('.progress_bar_item').show();
	$('.pbfog').show();
	$('.progress_bar_item').show();
	$('#loading').show();
	
}

function infinite_pbar(period){
	//return;

	period = period == null ? 2500 : period; 
	

	//if pbar is running then simply return

		
	if(window.pbar_timer!=null)
		return;
	

	
	var interval = period / 3;
	var progress = 0.0;
	//alert('update pbar')
	

	update_pbar(0);



	pbar_timer = setInterval(function(){

		if(progress>=1.0)
			progress = 0.0;
		else
			progress += interval / period;
		
		update_pbar(progress);
				
	}, interval);
	
	
	

}



function update_pbar(progress){

	//$('.progress_bar_pane').show();
	//$('.progress_bar_item').show();
	//$('.pbfog').show();
	
	if(progress>1.0)
		progress = 1.0;
	if(progress<0.0)
		progress = 0.0;
	
	
	$('.pbline').css({width: 310.0 * progress});

}

function SayIt(msg){
	//hide_pbar();
	
	GLOBAL('save', 'error');
	error_msg(msg);
}

function viewDoc(){

	
	jamin_data();
	var win = window.open('viewer.html', 'new', 'width=1000, height=600');

	applet_adapter.preparePreview();
	var xfdl=applet_adapter.getPreviewXml();
	
	/*
	 * 
	 * WAIT UNTIL DOM IS READY
	 */
	var deferred = function(){
		if(!win.CreateOpenViewer){
			setTimeout(function(){
				deferred();
			}, 3000);
		}
		else{
			win.CreateOpenViewer(xfdl);			
		}
	}
	
	deferred();	
	

}
function critical_error(text){
	 var padding = '20px';
		$.blockUI({css:{
			paddingTop: padding, 
			paddingRight: padding,
			paddingBottom: padding,
			paddingLeft: padding,
			backgroundColor: 'red'},
			message: '<p style="color:white; font-family: Verdana;font-weight: bold; font-size: 12px;">Ошибка! '+text+'</p>' });
		//setTimeout($.unblockUI, 60000);
		$('.blockUI').css({cursor:'default'});
		$('.blockUI').click(function(){
			$.unblockUI();
		});
}

function error_msg(text){
	
	$('body').append('<div id="dialog-error" title="Ошибка">'+
			'<p align="center">'+text+'</p>'+
			'</div>');
	
	
	
	$('#dialog-error').dialog({
		draggable:false,
		width: 400,
		height: 220,
		resizable: false,
		
		buttons: {
		'Закрыть':function(){
		$('#dialog-error').dialog('close');
		
		$('#dialog-error').detach();
	}
		
	}});
}

function Update_pbar(val){
	update_pbar(val);
}


function message(text){

	$('body').append('<div id="dialog" title="Сообщение">'+
			'<p align="center">'+text+'</p>'+
			'</div>');
	
	
	
	$('#dialog').dialog({
		draggable:false,
		width: 400,
		modal: true,
		height: 220,
		resizable: false,
		
		buttons: {
		'Закрыть':function(){
		$('#dialog').dialog('close');
		
		$('#dialog').detach();
	}
		
	}});
	
}


/* --------------------------------- ETD CALLS ------------*/

function save_doc(){
	
	jamin_data();
	//pbar_msg('sign');
	wizard_applet.move_images();
	
	
	
	sign_pbar();
	infinite_pbar();
	
	setTimeout(function(){
		hide_pbar();
		//pbar_msg('send');
		sending_pbar();
		applet_adapter.saveDocOCOw(false);		
		close_wizard();
		
		if(GLOBAL('save')!='error'){
			message('Документ сохранен');
		}
		
		hide_pbar();	
		
	}, 3000);
	


}


var select_depo = function(){

 	var json = eval('('+applet_adapter.getWorkPosition()/*getWrkNames()*/+')');

 	if(json.error){
 		hide_pbar();
 		
 		error_msg(json.error);
 		return false;
 	}else
 		return true;
 };

 function etd()
 {

 		applet_adapter = document.applets["zzz"];

 		if (applet_adapter == null ) 
 		{
 			hide_pbar();
 			return false;
 		}		
 		
 		var updated=eval('('+applet_adapter.Updated()+')');
 		if(updated.updated)
 		{
 	 		initjson=eval('('+applet_adapter.getinitvalues()+')');	
 	 		//userreq = eval('('+applet_adapter.getUserRequisites()+')');

 			if(!login())	
 				return false;
 			
 		} else{	hide_pbar(); return false;}	
 		
 		
 		return true;
 }

 function login(){
 		//applet_adapter = document.applets['zzz'];
 		if(eval('('+applet_adapter.login(' ')+')').value)
 		{
 		
// 		busy();
 		return select_depo();
 		}else{
 			
 			hide_pbar();
 			
 		return false;
 		};
 		
 		hide_pbar();
 		
 return false;
 };