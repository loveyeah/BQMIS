Powererp = {}, Powererp.form = {}, Powererp.util = {};
Powererp.layout = {};

var CD_ERR_REGX = /^[-]+|[^\x00-\xff]+|[ '"\\]+/g;// 编码不能输入的字符

/**
 * 编码检证：不能输全角字符，以及空格’”\
 * 
 * @param {}
 *            cd
 */
Powererp.util.isValidCode = function(cd) {
	if (CD_ERR_REGX.test(cd)) {
		return false;
	}
	return true;
}

/**
 * 编码输入框控件
 */
var oldBeforeBlur = Ext.form.TextField.prototype.beforeBlur;
Ext.form.TextField.prototype.beforeBlur = function() {
	oldBeforeBlur.call(this, arguments);
	if (this.codeField == "yes") {
		var rv = this.getRawValue();
		rv = rv.replace(CD_ERR_REGX, '');
		rv = rv.replace(CD_ERR_REGX, '');
		this.setValue(rv);
	}
}

/**
 * 加逗号的数字入力框
 * 
 * @class Powererp.form.NumField
 * @extends Ext.form.TextField
 */
Powererp.form.NumField = Ext.extend(Ext.form.TextField, {

	/**
	 * @cfg {String} fieldClass The default CSS class for the field (defaults to
	 *      "x-form-field x-form-num-field")
	 */
	fieldClass : "x-form-field x-form-num-field",
	/**
	 * @cfg {Boolean} allowDecimals False to disallow decimal values (defaults
	 *      to true)
	 */
	allowDecimals : true,
	/**
	 * @cfg {String} decimalSeparator Character(s) to allow as the decimal
	 *      separator (defaults to '.')
	 */
	decimalSeparator : ".",
	/**
	 * @cfg {Number} decimalPrecision The maximum precision to display after the
	 *      decimal separator (defaults to 2)
	 */
	decimalPrecision : 2,
	/**
	 * @cfg {Boolean} allowNegative False to prevent entering a negative sign
	 *      (defaults to true)
	 */
	allowNegative : true,
	/**
	 * @cfg {Number} minValue The minimum allowed value (defaults to
	 *      Number.NEGATIVE_INFINITY)
	 */
	minValue : Number.NEGATIVE_INFINITY,
	/**
	 * @cfg {Number} maxValue The maximum allowed value (defaults to
	 *      Number.MAX_VALUE)
	 */
	maxValue : Number.MAX_VALUE,
	/**
	 * @cfg {String} minText Error text to display if the minimum value
	 *      validation fails (defaults to "The minimum value for this field is
	 *      {minValue}")
	 */
	minText : "The minimum value for this field is {0}",
	/**
	 * @cfg {String} maxText Error text to display if the maximum value
	 *      validation fails (defaults to "The maximum value for this field is
	 *      {maxValue}")
	 */
	maxText : "The maximum value for this field is {0}",
	/**
	 * @cfg {String} nanText Error text to display if the value is not a valid
	 *      number. For example, this can happen if a valid character like '.'
	 *      or '-' is left in the field with no number (defaults to "{value} is
	 *      not a valid number")
	 */
	nanText : "{0} is not a valid number",
	/**
	 * @cfg {String} baseChars The base set of characters to evaluate as valid
	 *      numbers (defaults to '0123456789').
	 */
	baseChars : "0123456789",

	addComma : true,

	padding : 0,

	onRender : function(ct, position) {
		Powererp.form.NumField.superclass.onRender.call(this, ct, position);
		if (this.hiddenName) {
			this.hiddenField = this.el.insertSibling({
				tag : 'input',
				type : 'hidden',
				name : this.hiddenName,
				id : (this.hiddenId || this.hiddenName)
			}, 'before', true);
			// prevent input submission
			this.el.dom.removeAttribute('name');
			this.hiddenField.value = this.paddingFunc(this
					.rmCommaFunc(this.value));
			this.el.setStyle("text-align", 'right');
		}
	},

	initValue : function() {
		Powererp.form.NumField.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = this.paddingFunc(this
					.rmCommaFunc(this.value));
		}
	},

	clearValue : function() {
		Powererp.form.NumField.superclass.clearValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = '';
		}
	},

	// private
	initEvents : function() {
		Powererp.form.NumField.superclass.initEvents.call(this);
		var allowed = this.baseChars + '';
		if (this.allowDecimals) {
			allowed += this.decimalSeparator;
		}
		if (this.allowNegative) {
			allowed += "-";
		}
		if (this.addComma) {
			var shwChar = "," + allowed;
			this.stripCharsRe = new RegExp('[^' + shwChar + ']', 'gi');
		} else {
			this.stripCharsRe = new RegExp('[^' + allowed + ']', 'gi');
		}

		var keyPress = function(e) {
			var k = e.getKey();
			if (!Ext.isIE
					&& (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
				return;
			}
			var c = e.getCharCode();
			if (allowed.indexOf(String.fromCharCode(c)) === -1) {
				e.stopEvent();
			}
		};
		this.el.on("keypress", keyPress, this);
		if (this.addComma) {
			this.el.on("focus", this.onFcs, this);
		}
	},

	// private
	validateValue : function(value) {
		if (!Powererp.form.NumField.superclass.validateValue.call(this, value)) {
			return false;
		}
		if (value.length < 1) { // if it's blank and textfield didn't flag it
			// then it's valid
			return true;
		}
		if (this.addComma) {
			value = this.rmCommaFunc(String(value));
		}
		value = String(value).replace(this.decimalSeparator, ".");
		if (isNaN(value)) {
			this.markInvalid(String.format(this.nanText, value));
			return false;
		}
		var num = this.parseValue(value);
		if (num < this.minValue) {
			this.markInvalid(String.format(this.minText, this.minValue));
			return false;
		}
		if (num > this.maxValue) {
			this.markInvalid(String.format(this.maxText, this.maxValue));
			return false;
		}
		return true;
	},

	getValue : function() {
		if (this.addComma) {
			var v = Powererp.form.NumField.superclass.getValue.call(this);
			return this.fixPrecision(this.parseValue(this.rmCommaFunc(v)));
		}
		return this.fixPrecision(this
				.parseValue(Powererp.form.NumField.superclass.getValue
						.call(this)));
	},

	addCommaFunc : function(v) {
		if (v) {
			var t = '';
			while ((t = v.replace(/^(-?[0-9]+)([0-9]{3}.*)$/, '$1,$2')) !== v)
				v = t;
			return v;
		} else
			return '';
	},

	rmCommaFunc : function(v) {
		if (v)
			return v.replace(/,/g, '');
		else
			return '';
	},

	paddingFunc : function(v) {
		if (v !== '') {
			if (this.padding > 0) {
				var na = (v + "").split(".");
				return na[0]
						+ "."
						+ ((na.length > 1 ? na[1] : '') + '0000000000')
								.substring(0, this.padding);
			}
		}
		return v;
	},

	setValue : function(v) {
		v = typeof v == 'number' ? v : parseFloat(String(v).replace(
				this.decimalSeparator, "."));
		v = isNaN(v) ? '' : String(v).replace(".", this.decimalSeparator);
		v = this.paddingFunc(v);
		if (this.hiddenField) {
			this.hiddenField.value = v;
		}
		if (this.addComma) {
			v = this.addCommaFunc(v);
		}
		Powererp.form.NumField.superclass.setValue.call(this, v);
	},

	// private
	parseValue : function(value) {
		value = parseFloat(String(value).replace(this.decimalSeparator, "."));
		return isNaN(value) ? '' : value;
	},

	// private
	fixPrecision : function(value) {
		var nan = isNaN(value);
		if (!this.allowDecimals || this.decimalPrecision == -1 || nan || !value) {
			return nan ? '' : value;
		}
		return parseFloat(parseFloat(value).toFixed(this.decimalPrecision));
	},

	beforeBlur : function() {
		var rv = this.getRawValue();
		if (this.addComma) {
			rv = this.rmCommaFunc(rv);
		}
		var v = this.parseValue(rv);
		this.setValue(this.fixPrecision(v));
	},

	onFcs : function(e) {
		if (this.getRawValue() != '') {
			Powererp.form.NumField.superclass.setValue.call(this, this
					.rmCommaFunc(this.getRawValue()));
			var Position = this.getRawValue().length;
			this.selectText(Position, Position);
		}
	}

});
Ext.apply(Powererp.form.NumField.prototype, {
      minText : "该输入项的最小值是 {0}",
      maxText : "该输入项的最大值是 {0}",
      nanText : "{0} 不是有效数值"
   });
Ext.reg('numfield', Powererp.form.NumField);

/**
 * 向TextField加双击事件
 * 
 * @param {}
 *            f
 */
Ext.form.TextField.prototype.onDblClick = function(f) {
	this.on("render", function() {
		this.el.on("dblclick", f);
	});
}

/**
 * 下拉框空选项
 */
Ext.override(Ext.form.ComboBox, {
	initList : (function() {
		if (!this.tpl) {
			this.tpl = new Ext.XTemplate(
					'<tpl for="."><div class="x-combo-list-item">{',
					this.displayField, ':this.blank}</div></tpl>', {
						blank : function(value) {
							return value === '' ? '&nbsp' : value;
						}
					});
		}
	}).createSequence(Ext.form.ComboBox.prototype.initList)
});

/**
 * grid header部分显示不全修正
 * 
 * @param {}
 *            vw
 * @param {}
 *            vh
 */
var oldOnLayout = Ext.grid.GridView.prototype.onLayout;
Ext.grid.GridView.prototype.onLayout = function(vw, vh) {
	oldOnLayout.call(this, vw, vh);
	this.mainBody.dom.style.width = this.getTotalWidth();
}

/**
 * TextField 单击事件（label排除）修正
 * 
 */
var tt = new Ext.Template('<div class="x-form-item {5}" tabIndex="-1">',
		'<label style="{2}" class="x-form-item-label">{1}{4}</label>',
		'<div class="x-form-element" id="x-form-el-{0}" style="{3}">',
		'</div><div class="{6}"></div>', '</div>');
tt.disableFormats = true;
tt.compile();
Ext.layout.FormLayout.prototype.fieldTpl = tt;
Ext.form.TextField.prototype.onClick = function(f) {
	this.on("render", function() {
		this.el.on("click", f);
	});
}

/**  行政管理  添加者:胡由  添加日期:2009/01/19  添加开始     */
// 解决场景描述: 关闭弹出画面时隐藏时间控件
var tempFun = Ext.Window.prototype.onRender;
Ext.Window.prototype.onRender = function() {
	tempFun.apply(this, arguments);
	delete tempFun;
	
	var tempHide = function() {
		if ((typeof $dp != 'undefined') && $dp) {
			$dp.hide();
		}
	};
	this.on({'move': tempHide,
			'hide': tempHide,
			'close': tempHide
	});
}
/**  行政管理  添加者:胡由  添加日期:2009/01/19  添加结束     */