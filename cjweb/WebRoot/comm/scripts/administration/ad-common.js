// 静态DRP
// 周期类别
Ext.form.CmbCycleType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '0',text : '没有设置'},{value : '1',text : '每日'},{value : '2',text : '隔日'},{value : '3',text : '每周'},{value : '4',text : '隔周'},{value : '5',text : '每月'},{value : '6',text : '隔月'},{value : '7',text : '隔N天'}]})});
Ext.reg('CmbCycleType', Ext.form.CmbCycleType);

// 工作类别
Ext.form.CmbWorkType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '01',text : '物业'},{value : '02',text : '绿化'},{value : '03',text : '保安'}]})});
Ext.reg('CmbWorkType', Ext.form.CmbWorkType);

// 是否标志
Ext.form.CmbIsFlg= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : 'Y',text : '是'},{value : 'N',text : '否'}]})});
Ext.reg('CmbIsFlg', Ext.form.CmbIsFlg);

// 上报状态-->单据状态
Ext.form.CmbReportStatus= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '',text : ''},{value : '0',text : '未上报'},{value : '1',text : '已上报'},{value : '2',text : '已终结'},{value : '3',text : '已退回'}]})});
Ext.reg('CmbReportStatus', Ext.form.CmbReportStatus);

// 完成与否标志
Ext.form.CmbAchieveFlg= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '',text : ''},{value : 'Y',text : '完成'},{value : 'N',text : '未完成'}]})});
Ext.reg('CmbAchieveFlg', Ext.form.CmbAchieveFlg);

// 使用情况
Ext.form.CmbUseFlg= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '',text : ''},{value : 'Y',text : '占用'},{value : 'N',text : '空闲'}]})});
Ext.reg('CmbUseFlg', Ext.form.CmbUseFlg);

// 有无清单
Ext.form.CmbHaveFormFlg= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '',text : ''},{value : 'Y',text : '有'},{value : 'N',text : '无'}]})});
Ext.reg('CmbHaveFormFlg', Ext.form.CmbHaveFormFlg);

// 订单状态
Ext.form.CmbOrdersStatus= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '',text : ''},{value : '1',text : '填写'},{value : '2',text : '审核'},{value : '3',text : '已发送'}]})});
Ext.reg('CmbOrdersStatus', Ext.form.CmbOrdersStatus);

// 用餐类别
Ext.form.CmbMealType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '1',text : '早餐'},{value : '2',text : '中餐'},{value : '3',text : '晚餐'},{value : '4',text : '宵夜'}]})});
Ext.reg('CmbMealType', Ext.form.CmbMealType);

// 费用类别
Ext.form.CmbFeeType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '001',text : '工时费'},{value : '002',text : '材料费'}]})});
Ext.reg('CmbFeeType', Ext.form.CmbFeeType);

// 房间类别
Ext.form.CmbRoomType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : '01',text : '单间'},{value : '02',text : '套间'},{value : '03',text : '标间'}]})});
Ext.reg('CmbRoomType', Ext.form.CmbRoomType);

// 阅读状态
Ext.form.CmbReadStatus= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'text',valueField : 'value',store : new Ext.data.JsonStore({fields : ['value', 'text'],data : [{value : 'Y',text : '已阅读'},{value : 'N',text : '未阅读'}]})});
Ext.reg('CmbReadStatus', Ext.form.CmbReadStatus);

// 动态DRP
// 证件类别
Ext.form.CmbPaperType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'papertypeName',valueField : 'papertypeCode',store : new Ext.data.JsonStore({url : 'administration/getPaperInfoList.action',root : 'list',fields : [{name : 'papertypeName'}, {name : 'papertypeCode'}]})});
Ext.reg('CmbPaperType', Ext.form.CmbPaperType);

// 工作类别取得
Ext.form.CmbSubWorkType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'subWorktypeName',valueField : 'subWorktypeCode',store : new Ext.data.JsonStore({url : 'administration/getWorkTypeInfoList.action',root : 'list',fields : [{name : 'subWorktypeName'}, {name : 'subWorktypeCode'}]})});
Ext.reg('CmbSubWorkType', Ext.form.CmbSubWorkType);

// 与选择部门连动的人员DRP
Ext.form.CmbWorkerByDept= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'name',valueField : 'workerCode',store : new Ext.data.JsonStore({url : 'administration/getWorkerByDept.action',root : 'list',fields : [{name : 'name'}, {name : 'workerCode'}]})});
Ext.reg('CmbWorkerByDept', Ext.form.CmbWorkerByDept);

// 值别取得
Ext.form.CmbDuty= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'dutyTypeName',valueField : 'dutyType',store : new Ext.data.JsonStore({url : 'administration/getDutyTypeInfoList.action',root : 'list',fields : [{name : 'dutyTypeName'}, {name : 'dutyType'}]})});
Ext.reg('CmbDuty', Ext.form.CmbDuty);

// 司机取得
Ext.form.CmbDriver= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'driverName',valueField : 'driverCode',store : new Ext.data.JsonStore({url : 'administration/getDriverInfoList.action',root : 'list',fields : [{name : 'driverName'}, {name : 'driverCode'}]})});
Ext.reg('CmbDriver', Ext.form.CmbDriver);

// 计量单位取得
Ext.form.CmbUnit= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'strUnitName',valueField : 'strUnitID',store : new Ext.data.JsonStore({url : 'administration/getUnit.action',root : 'list',fields : [{name : 'strUnitName'}, {name : 'strUnitID'}]})});
Ext.reg('CmbUnit', Ext.form.CmbUnit);

// 菜谱类别DRP
Ext.form.CmbCMenuType= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'strMenuTypeName',valueField : 'strMenuTypeCode',store : new Ext.data.JsonStore({url : 'administration/getAllCMenuType.action',root : 'list',fields : [{name : 'strMenuTypeName'}, {name : 'strMenuTypeCode'}]})});
Ext.reg('CmbCMenuType', Ext.form.CmbCMenuType);

// 抄送人员DRP
Ext.form.CmbCCUser= Ext.extend(Ext.form.ComboBox, {width : 180,allowBlank : true,forceSelection : true,triggerAction : 'all',mode : 'local',readOnly : true,displayField : 'name',valueField : 'workerCode',store : new Ext.data.JsonStore({url : 'administration/getCCUserIDAndName.action',root : 'list',fields : [{name : 'name'}, {name : 'workerCode'}]})});
Ext.reg('CmbCCUser', Ext.form.CmbCCUser);

// 共通函数
/**
 * 加逗号的数字入力框
 * 
 * @class Powererp.form.NumField
 * @extends Ext.form.TextField
 */
Ext.form.MoneyField = Ext.extend(Ext.form.TextField, {

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
	// 增加的字符
	appendChar : '',

	onRender : function(ct, position) {
		Ext.form.MoneyField.superclass.onRender.call(this, ct, position);
		this.appendLen = this.appendChar.length;
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
		Ext.form.MoneyField.superclass.initValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = this.paddingFunc(this
					.rmCommaFunc(this.value));
		}
	},

	clearValue : function() {
		Ext.form.MoneyField.superclass.clearValue.call(this);
		if (this.hiddenField) {
			this.hiddenField.value = '';
		}
	},

	// private
	initEvents : function() {
		Ext.form.MoneyField.superclass.initEvents.call(this);
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
		if (!Ext.form.MoneyField.superclass.validateValue.call(this, value)) {
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
//		if (this.addComma) {
//			var v = Powererp.form.NumField.superclass.getValue.call(this);
//			return this.fixPrecision(this.parseValue(this.rmCommaFunc(v)));
//		}
		return this.fixPrecision(this
				.parseValue(this.getRawValue()));
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
		if (!this.isNull(v)) {
			v = v + this.appendChar;
		}
		Ext.form.MoneyField.superclass.setValue.call(this, v);
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
			Ext.form.MoneyField.superclass.setValue.call(this, this
					.rmCommaFunc(this.getRawValue()));
			var Position = this.getRawValue().length;
			this.selectText(Position, Position);
		}
	},
	getRawValue : function() {
		var v = this.rendered ? this.el.getValue() : Ext.value(this.value, '');
        if(v === this.emptyText){
            v = '';
        } else if (!this.isNull(v) && v.slice(0-this.appendLen) == this.appendChar){
        	v = v.slice(0, 0-this.appendLen);
        }
        return String(v).replace(/,/g, '');
	},
	isNull : function(v) {
		return !v && v != '0';
	}

});
//Ext.apply(Ext.form.MoneyField.prototype, {
//      minText : "该输入项的最小值是 {0}",
//      maxText : "该输入项的最大值是 {0}",
//      nanText : "{0} 不是有效数值"
//   });
Ext.reg('numfield', Ext.form.MoneyField);
// 整除
function Div(exp1, exp2) {
	var n1 = Math.round(exp1); // 四舍五入
	var n2 = Math.round(exp2); // 四舍五入

	var rslt = n1 / n2; // 除

	if (rslt >= 0) {
		rslt = Math.floor(rslt); // 返回值为小于等于其数值参数的最大整数值。
	} else {
		rslt = Math.ceil(rslt); // 返回值为大于等于其数字参数的最小整数。
	}

	return rslt;
}
// 追加逗号
function divide(value) {
	if (value == null) {
		return "";
	}
	var svalue = value + "";
	var decimal = "";
	var negative = false;
	var tempV = "";
	// 如果有小数
	if (svalue.indexOf(".") > 0) {
		decimal = svalue.substring(svalue.indexOf("."), svalue.length);
	}
	// 如果是负数
	if (svalue.indexOf("-") >= 0) {
		negative = true;
		svalue = svalue.substring(1, svalue.length);
	}
	tempV = svalue.substring(0, svalue.length - decimal.length);
	svalue = "";
	while (Div(tempV, 1000) > 0) {
		var temp = Div(tempV, 1000);
		var oddment = tempV - temp * 1000;
		var soddment = "";
		tempV = Div(tempV, 1000);
		soddment += (0 == Div(oddment, 100)) ? "0" : Div(oddment, 100);
		oddment -= Div(oddment, 100) * 100;
		soddment += (0 == Div(oddment, 10)) ? "0" : Div(oddment, 10);
		oddment -= Div(oddment, 10) * 10;
		soddment += (0 == Div(oddment, 1)) ? "0" : Div(oddment, 1);
		oddment -= Div(oddment, 1) * 1;
		svalue = soddment + "," + svalue;
	}
	svalue = tempV + "," + svalue;
	svalue = svalue.substring(0, svalue.length - 1);
	svalue += decimal;
	if (true == negative) {
		svalue = "-" + svalue;
	}
	return svalue;
}

/** 添加人: 胡由  添加日期: 2009/01/21  添加开始 */
// 当给ComboBox设置的值在对应的Store中不存在时, 设置为空字串
var tempSetValue = Ext.form.ComboBox.prototype.setValue;
Ext.form.ComboBox.prototype.setValue = function(v, allowBlank) {
	var value = v;
	if (allowBlank === true) {
		// 如果找不到的话,就设置为空字符串
		value = '';
	    if(this.valueField){
	        var r = this.findRecord(this.valueField, v);
	        if(r){
	            value = v;
	        }
	    }
	}
    
    tempSetValue.call(this, value);
    delete tempSetValue;
}

// 清空上传文件框的内容
Ext.form.TextField.prototype.clearFilePath = function() {
	if (this.el && this.el.dom) {
	    // 清除附件内容
	    var domAppend = this.el.dom;
	    var parent = domAppend.parentNode;
	    
	    // 保存
	    var domForSave = domAppend.cloneNode();
	    // 移除附件控件
	    parent.removeChild(domAppend);
	    // 再追加控件
	    parent.appendChild(domForSave);
	    // 应用该控件
	    this.applyToMarkup(domForSave);
	}
}
/** 添加人: 胡由  添加日期: 2009/01/21  添加结束 */

/** 添加人: 胡由  添加日期: 2009/02/03  添加开始 */
// 在TextField设置onlyLetter： 只允许输入字母
var tempRender = Ext.form.TextField.prototype.onRender;
var onlyLetter = function() {
	tempRender.apply(this, arguments);
	delete tempRender;
	
	if (this.onlyLetter === true) {
	    // 只允许输入字母
	    var keyPress = function(e){
	        var k = e.getKey();
	        if(!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)){
	            return;
	        }
	        var c = String.fromCharCode(e.getCharCode());
	        if(!/^\w$/i.test(c) || !isNaN(c)){
	            e.stopEvent();
	        }
	    };
	    // 禁用Ctrl + V
	    var keydown = function(e) {
	    	var k = e.getKey();
	        if(!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)){
	            return;
	        }
	        var c = String.fromCharCode(e.getCharCode());
	        
	        if(e.ctrlKey && /^v$/i.test(c)){
	            e.stopEvent();
	        }
	    }
	    this.el.on("keypress", keyPress, this);
	    this.el.on("keydown", keydown, this);
	    // 禁用右键
	    this.el.on("contextmenu", function(e){e.stopEvent();}, this);
	}
}
Ext.form.TextField.prototype.onRender = onlyLetter;
/** 添加人: 胡由  添加日期: 2009/02/03  添加结束 */

// 判断是否是正确的文件路径
function checkFilePath(filePath) {
	if (!filePath) return false;
	return /^(\w:|\\\\)/.test(String.escape(filePath));
//	filePath = String.escape(filePath).replace(/(\\|\/)(?:\1+)/g, '/')
//		.replace(/(\\|\/)(?:\1+)/g, '/');
//	
//	return /^[^\s\/\\]+([\/\\][^\/\\]+)*$/.test(filePath);
}

// 设置Label的标签
if (!Ext.form.Label.prototype.setText) {
	Ext.form.Label.prototype.setText = function(argText) {
		this.el.dom.innerHTML = argText;
	}
}
// 设置TextField的标签
if (!Ext.form.TextField.prototype.setLabel) {
	Ext.form.TextField.prototype.setLabel = function(argText) {
		if (!this.hideLabel) {
			if (!this.labelSeparator) {
				this.labelSeparator = ':';
			}
			this.getEl().up('.x-form-item').dom.firstChild.innerHTML = argText + this.labelSeparator;
		}
	}
}

// 当隐藏列时重新移动列表头
Ext.grid.GridView.prototype.updateColumnHidden =
	Ext.grid.GridView.prototype.updateColumnHidden.createSequence(function() {
		this.syncHeaderScroll();
	});