jQuery.fn.tooltip = function(options) {
	var options = jQuery.extend({
				txt : '',
				maxWidth : 300
			}, options);

	if (options.txt == '')
		return null;

	var helper, effect = {}, el_tips = {};
	if (!$("div.tooltip").length)
		$(function() {
					helper = $('<div class="tooltip"></div>')
							.appendTo(document.body).hide();
				});
	else
		helper = $("div.tooltip").hide();

	return this.each(function() {
				if (options.txt)
					el_tips[$.data(this)] = options.txt;
				else
					el_tips[$.data(this)] = this.title;
				this.title = '';
				this.alt = '';
			}).mouseover(function(e) {
				if (el_tips[$.data(this)] != '') {
					helper.css('width', '');
					helper.html(el_tips[$.data(this)]);
					if (helper.width() > options.maxWidth)
						helper.width(options.maxWidth);
					helper.show();
					var pos = getPosition(this);
					helper.css({
								left : pos.x + 3 + "px"
							});
					helper.css({
								top : pos.y + this.offsetHeight + 3 + "px"
							});
				}
			}).mouseout(function() {
				helper.hide();
			});

	function getPosition(obj) {
		var x = 0, y = 0;
		while (obj) {
			x += obj.offsetLeft;
			y += obj.offsetTop;
			obj = obj.offsetParent;
		}
		return {
			x : x,
			y : y
		};
	}
};
