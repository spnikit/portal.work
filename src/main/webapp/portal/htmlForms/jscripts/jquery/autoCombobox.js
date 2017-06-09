(function($) {
	$.widget("ui.autocombobox", {
		isBorder : null,
		buttonJqEl : null,
		autocompileJqEl : null,
		_create : function() {

			console.dir(this.options.source);
			var input = this.element;
			var self = this;
			var options = this.options;
			var wrapper = this.wrapper = $("<span>").addClass("ui-combobox")
					.insertAfter(input);

			// input = $("<textarea>").appendTo(wrapper).val(value)
			// .addClass("ui-state-default ui-combobox-input");
			input = $(input).appendTo(wrapper)
					.addClass("ui-combobox-input noBorderRadius");
			var optMinLenght;
			this.isBorder = this.options.isBorder;
			if (this.options.isAutocomp) {
				optMinLenght = this.options.minLength;
			} else {
				optMinLenght = 10000;
			}

			input = (autocompileJqEl = input.autocomplete({
						delay : 0,
						minLength : optMinLenght,
						source : this.options.source,

						select : this.options.select,
						change : this.options.change,
						open : this.options.open,
						close : this.options.close,
						search : this.options.search,
						focus : this.options.focus,
						create : this.options.create

					})).addClass("ui-widget ui-widget-content ui-corner-left");

			input.data("autocomplete")._renderItem = function(ul, item) {
				return $("<li></li>").data("item.autocomplete", item)
						.append("<a>" + item.label + "</a>").appendTo(ul);
			};

			(this.buttonJqEl = $("<a>"))
					.attr("tabIndex", -1)
					.attr("title", "Показать/скрыть все пункты")
					.appendTo(wrapper)
					.button({
								icons : {
									primary : "ui-icon-triangle-1-s"
								},
								text : false

							})
					.removeClass("ui-corner-all ui-state-default")
					.addClass("ui-corner-right ui-combobox-toggle ui-combobox-input-button noBorderRadius")
					.click(function() {
						// close if already visible
						if (input.autocomplete("widget").is(":visible")) {
							input.autocomplete("close");
							return;
						}

						// work around a bug (likely same cause as #5265)
						$(this).blur();

						// pass empty string as value to search for, displaying
						// all
						// results
						var tmpMinLenght = options.minLength;
						input.autocomplete('option', 'minLength', 0);
						input.autocomplete("search", "");
						//--------------------------------------
						var childs = options.source;
						var width = 0;
						for (var i=0;i<childs.length;i++){
							var len = childs[i].label.length;
							if (len > width) width = len;
						}
						var str = "";
						for (var i=0;i<width;i++){str += "w";}
						dopDivSize.setValue(str);
						width = dopDivSize.offsetWidth;
						input.autocomplete("widget")[0].style.width = width+'px';
						//--------------------------------------
						input.focus();
						input.autocomplete('option', 'minLength', tmpMinLenght);
					});
			if (!this.isBorder) {
				this.border(false);
			}
		},
		options : {
			select : null,
			change : null,
			open : null,
			close : null,
			search : null,
			focus : null,
			create : null,

			isAutocomp : false,
			source : [],
			minLength : 0,
			isBorder : true
		},
		destroy : function() {
			this.wrapper.remove();
			this.element.show();
			$.Widget.prototype.destroy.call(this);
		},
		/**
		 * возвращает кнопку комбобокса(обрати внимание что вернет тэг &lt;a>)
		 * 
		 * @return HTMLLinkElement|HTMLElement
		 */
		getButtonEl : function() {
			return this.buttonJqEl.element;
		},
		/**
		 * 
		 * 
		 * @return Boolean|undefined
		 */
		border : function(borderProp) {
			if (arguments.length == 0) {
				return this.isBorder;
			} else {
				this.isBorder = borderProp ? true : false;
				if (this.isBorder) {
					$(this.element).removeClass('noBorder');
					$(this.buttonJqEl).removeClass('noBorder');
				} else {
					$(this.element).addClass('noBorder');
					$(this.buttonJqEl).addClass('noBorder');
				}
			}
		},
		setSource : function(newSource) {
			$(autocompileJqEl).autocomplete('option', 'source', newSource);
		}

	});
})(jQuery);