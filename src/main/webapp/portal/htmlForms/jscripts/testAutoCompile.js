/**
 * @include "jsdoc/console.js"
 */

$(function()
	    {
	    var availableTags = [{
		        id		: 1,
		        value	: "a_val",
		        label	: "a_label"
	        }, {
		        id		: 1,
		        value	: "b_val",
		        label	: "b_labelqweqweaa"
	        }, {
		        id		: 1,
		        value	: "c_val",
		        label	: "c_label"
	        }, {
		        id		: 1,
		        value	: "d_val",
		        label	: "d_label"
	        }, {
		        id		: 1,
		        value	: "e_val",
		        label	: "e_label"
	        }, {
		        id		: 1,
		        value	: "f_val",
		        label	: "f_label"
	        }

	    ];
	    
	    var div = document.createElement('div');
	    var hiddenSel = document.createElement('input');
	    var style = '';
	    var styleDiv = '';
	    styleDiv += 'position:absolute;';
	    styleDiv += 'top:120px;left:150px;';
	    styleDiv += 'height:30px;';
	    styleDiv += 'width:100px';
	    styleDiv += 'z-index:15;';
	    style += 'width:76px;height:100%px;z-index:15;';
	    hiddenSel.setAttribute('style', style);
	    div.setAttribute('style', styleDiv);
	    
	    div.appendChild(hiddenSel);
	    var allData;
	    
	    window.jqu = $(hiddenSel).autocombobox({
		        isAutocomp	: true,
		        source		 : availableTags
		        
	        });
	        
	    document.body.appendChild(div);
	    $.ajax({
		        url		   : "../PredRelatesNames.php?predId=1&typeId=2",
		        async		 : true,
		        timeout		: 10000,
		        dataType	: 'json',
		        complete	: function(response, status)
			        {
			        if (status == 'success')
				        {
				        allData = eval('(' + response.responseText + ')');
				        }
			        else
				        {
				        allData = {
					        descriptor	: 'error',
					        info				: 'Не получено ответа с сервера',
					        content			: []
				        }
				        }
			        availableTags = allData.content;
			        window.jqu.autocombobox('setSource', availableTags);
			        }
	        });
	    
	    });
