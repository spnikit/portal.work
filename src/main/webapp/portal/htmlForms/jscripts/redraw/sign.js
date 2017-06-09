$(document).ready(function() {

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// добаляем и убираем подсветку для обязательных полей [mandatory=on]
	$("[mandatory=on]").each(function() {
				$(this).addClass('mandatory').blur(function() {
							if ($(this)[0].value == 0)
								$(this).addClass('mandatory');
							else
								$(this).removeClass('mandatory');
						})
			});
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// добаляем и убираем подсветку для обязательных полей [mandatory=on]
	$("#setData").bind('click', function() {
				$("#text_for_valid").simpeValidate({
							message : "test_message",
							length : {
								min : 2,
								max : 5
							}
						});
			});
	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	// вешаем на каждый элемент с описанием format,подстановка параметров и
	// выбор шаблона подставляется при генерации
	// привязывается по id
	$("#text_for_valid_3").blur(function() {
		// ====================================================================================
		// входные данные (format):
		var datatype = 'integer';// тип вводимых данных, определяет дефолтный
									// шаблон
		// costrains:
		var check = 'none';// проверять ли
		var message = "get it from 'message'";// тест для вывода во
												// всплывающей подсказке
		var mandatory = 'on';// обязательный ли элемент
		var length = {
			min : 0,
			max : 32768
		};// длина вводимого значения в символах [32kB for string. numb
			// +-2^64=>64]
		var patterns = {};// регулярные выражения, которым должно
							// соответствовать
		var range = {
			min : '',
			max : ''
		};// min и max для введенного значения
		var template = '';// шаблон для ввода. для телефона: (...)...-..-..
		// presentation //набор для разных типов данных различен, пока
		// нереализовано.
		// ====================================================================================

		var value = $(this)[0].value;
		var minV = document.getElementById("min_value").value;
		var maxV = document.getElementById("max_value").value;
		datatype = document.getElementById("datatype").value;
		var match = '^.*$';
		// определяем шаблон для соответствующего datatype
		// для связанных с датами использубтся различные формы вывода
		// medium,full и т.д.
		// нет проверок на корректность введенного времени[41.49.9999 51:99]
		if (datatype == 'string') {
			match = '^.{' + length.min + ',' + length.max + '}$';
			length = {
				min : minV,
				max : maxV
			};
		} else if (datatype == 'integer') {
			match = '^[0-9]{' + length.min + ',' + length.max + '}$';
			length = {
				min : minV,
				max : maxV
			};
		} else if (datatype == 'float') {
			match = '^[0-9]{' + length.min + ',' + length.max + '}\\.[0-9]{'
					+ length.min + ',' + length.max + '}$';
			length = {
				min : minV,
				max : maxV
			};
		} else if (datatype == 'date')
			match = '^[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4,4}$';
		else if (datatype == 'date_time')
			match = '^[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4,4}\\s[0-9]{1,2}:[0-9]{2,2}$';
		else if (datatype == 'day_of_week')
			match = '^[a-zA-Z]{2,3}$';
		else if (datatype == 'day_of_month')
			match = '^[0-9]{1,2}$';
		else if (datatype == 'month')
			match = '^[0-9]{1,2}$';
		else if (datatype == 'time')
			match = '^[0-9]{1,2}:[0-9]{2,2}$';
		else if (datatype == 'void')
			match = '^.*$';
		else if (datatype == 'year')
			match = '^[0-9]{4,4}$';
		else if (datatype == 'currency') {
			match = '^[0-9]{' + length.min + ',' + length.max
					+ '}\.[0-9]{2,2}$';
			length = {
				min : minV,
				max : maxV
			};
		}

		// добавляем-удаляем подсветку и всплывающее сообщение
		if ($(this)[0].value.length != 0
				&& (!(new RegExp(match, "i").test(value))
						|| $(this)[0].value.length < length.min || $(this)[0].value.length > length.max)) {
			$(this).addClass('invalid_input').tooltip({
						txt : message
					});
		} else {
			$(this).removeClass('invalid_input');
			$("div.tooltip").remove();
		}
	})
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

})

jQuery.fn.simpeValidate = function(options) {
	var options = jQuery.extend({
		// ====================================================================================
		// входные данные (format):
		datatype : 'string', // тип вводимых данных, определяет дефолтный
								// шаблон
		// costrains:
		check : 'none', // проверять ли
		message : '', // тест для вывода во всплывающей подсказке
		mandatory : 'off', // обязательный ли элемент
		length : {
			min : 0,
			max : 32768
		},// длина вводимого значения в символах [32kB for string. numb
			// +-2^64=>64]
		patterns : {},// регулярные выражения, которым должно соответствовать
		range : {
			min : '',
			max : ''
		},// min и max для введенного значения
		template : ''// шаблон для ввода. для телефона: (...)...-..-..
			// presentation //набор для разных типов данных различен, пока
			// нереализовано.
			// ====================================================================================
		}, options);

	var value = $(this)[0].value;
	var match = '^.*$';

	if (options.datatype == 'string') {
		match = '^.{' + options.length.min + ',' + options.length.max + '}$';
	} else if (options.datatype == 'integer') {
		match = '^[0-9]{' + length.min + ',' + length.max + '}$';
	} else if (options.datatype == 'float') {
		match = '^[0-9]{' + length.min + ',' + length.max + '}\\.[0-9]{'
				+ length.min + ',' + length.max + '}$';
	} else if (options.datatype == 'date')
		match = '^[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4,4}$';
	else if (options.datatype == 'date_time')
		match = '^[0-9]{1,2}\\.[0-9]{1,2}\\.[0-9]{4,4}\\s[0-9]{1,2}:[0-9]{2,2}$';
	else if (options.datatype == 'day_of_week')
		match = '^[a-zA-Z]{2,3}$';
	else if (options.datatype == 'day_of_month')
		match = '^[0-9]{1,2}$';
	else if (options.datatype == 'month')
		match = '^[0-9]{1,2}$';
	else if (options.datatype == 'time')
		match = '^[0-9]{1,2}:[0-9]{2,2}$';
	else if (options.datatype == 'void')
		match = '^.*$';
	else if (options.datatype == 'year')
		match = '^[0-9]{4,4}$';
	else if (options.datatype == 'currency') {
		match = '^[0-9]{' + options.length.min + ',' + options.length.max
				+ '}\.[0-9]{2,2}$';
	}

	// добавляем-удаляем подсветку и всплывающее сообщение
	$(this).blur(function() {
		if ($(this)[0].value.length != 0
				&& (!(new RegExp(match, "i").test($(this)[0].value))
						|| $(this)[0].value.length < options.length.min || $(this)[0].value.length > options.length.max)) {
			$(this).addClass('invalid_input').tooltip({
						txt : options.message
					});
		} else {
			$(this).removeClass('invalid_input');
			$("div.tooltip").remove();
		}
	});
};
