// code定义
var caCodeDefine = {
    "出勤类别" : [{
            value : "",
            text : ""
        }, {
            value : "0",
            text : "出勤"
        }, {
            value : "1",
            text : "休息"
        }, {
            value : "2",
            text : "旷工"
        }, {
            value : "3",
            text : "迟到"
        }, {
            value : "4",
            text : "早退"
        }, {
            value : "5",
            text : "外借"
        }, {
            value : "6",
            text : "出差"
        }],
    "签字状态" : [{
            value : "",
            text : ""
        }, {
            value : "0",
            text : "未上报"
        }, {
            value : "1",
            text : "已上报"
        }, {
            value : "2",
            text : "已终结"
        }, {
            value : "3",
            text : "已退回"
        }],
    "考勤部门类别" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "考勤部门"
        }, {
            value : "2",
            text : "考勤审核部门"
        }, {
            value : "3",
            text : "代考勤部门"
        }],
    "节假日类别" : [{
            value : "1",
            text : "非周末休息日期"
        }, {
            value : "2",
            text : "周末上班日期"
        }],
    "考勤合计项类型" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "加班"
        }, {
            value : "2",
            text : "运行班"
        }, {
            value : "3",
            text : "请假"
        }, {
            value : "4",
            text : "出勤"
        }],
    "考勤单位" : [{
            value : "1",
            text : "天"
        }, {
            value : "2",
            text : "小时"
        }],
    "合计项是否使用标志" : [{
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "是否设置周期" : [{
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "是否包含周末" : [{
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "迟到FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "早退FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "休息FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "旷工FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "外借FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "出差FLG" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "是否销假" : [{
            value : "",
            text : ""
        }, {
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }],
    "是否发放费用" : [{
            value : "1",
            text : "是"
        }, {
            value : "0",
            text : "否"
        }]
};

/**
 * 静态DRP,参照code定义书
 * 
 * @class Ext.form.CmbCACode
 * @extends Ext.form.ComboBox
 */
Ext.form.CmbCACode = Ext.extend(Ext.form.ComboBox, {
    width : 180,
    /**
     * 类型（比如：部门级别）
     * 
     * @type String
     */
    type : "",
    allowBlank : true,
    forceSelection : true,
    triggerAction : 'all',
    mode : 'local',
    readOnly : true,
    displayField : 'text',
    valueField : 'value',
    // private
    initComponent : function() {
        if (caCodeDefine[this.type]) {
            this.store = new Ext.data.JsonStore({
                fields : ['value', 'text'],
                data : caCodeDefine[this.type]
            })
        } else {
            this.store = new Ext.data.JsonStore({
                fields : ['value', 'text'],
                data : []
            });
        }
        Ext.form.CmbCACode.superclass.initComponent.call(this);
    }
});

/**
 * 根据项目名和code值找到对应code说明
 * 
 * @param {}
 *            type code类别，比如：“部门级别”
 * @param {}
 *            value code值，比如：“0”
 */
function getCACodeName(type, value) {
    var array = caCodeDefine[type];
    if (array instanceof Array) {
        for (var i = 0; i < array.length; i++) {
            if (array[i].value == value)
                return array[i].text;
        }
    }
    return "";
}
/** 去除grid的header的排序图标 */
Ext.grid.GridView.prototype.removeSortIcon = function() {
    var sc = this.sortClasses;
    this.mainHd.select('td').removeClass(sc);
}
/** 添加人: 黄维杰 添加日期: 2009/02/24 添加开始 */
// 当名称中存在如<br>等html语言时，会产生换行等效果，此函数用于去掉该效果。
// 转义HTML元素, 参数onlySpaceAndBr不用传，或者设置为false
function escapeHTML(s, onlySpaceAndBr){
   var sb;
   sb = s + "";
   if (typeof onlySpaceAndBr != 'undefined'
        && onlySpaceAndBr === true) {
        /** 2009/02/26 huyou deleted
         * 如果自己写了列的renderer方法，则需要自己调用这个方法。这时第二个参数不用传，
         * 例如： renderer : function(value) {
         *           return "<font color='red'>" + escapeHTML(value) + "</font>";
         *       }
         */
//      var escapeamp = /<(br\/?)>/gi;
//      var escapenbsp = / (?= )/g;
//      var unescapern = /\r?\n/g;
//      
//      sb = sb.replace(escapeamp, "&lt;$1&gt;");
//      sb = sb.replace(escapenbsp, "&nbsp;");
//      sb = sb.replace(unescapern, "<br/>");
    } else {
        var escapelt = /</g;
        var escapegt = />/g;
        var escapeamp = /&/g;
        var escapequot = /"/g;
        var escapenbsp = / /g;
        var unescapern = /\r?\n/g;
        
        sb = sb.replace(escapeamp, "&amp;");
        sb = sb.replace(escapelt, "&lt;");
        sb = sb.replace(escapegt, "&gt;");
        sb = sb.replace(escapequot, "&quot;");
        sb = sb.replace(escapenbsp, "&nbsp;");
        sb = sb.replace(unescapern, "<br/>");
    }
   
    return sb;
}

var tempRender = Ext.grid.ColumnModel.prototype.getRenderer;
Ext.grid.ColumnModel.prototype.getRenderer = function() {
    var scope = this;
    var args = arguments;
    return function() {
        var value = arguments[0];
        var onlyBr = false;
        if (scope.config && scope.config[args[0]]) {
            onlyBr = !!scope.config[args[0]].renderer;
        }
        // 删除默认的defaultRenderer应用
        if (onlyBr) {
            var rendFun = tempRender.apply(scope, args);
            value = rendFun.apply(this, arguments);
        }
        
        if (typeof value == 'string' && value.length > 0) {
            return escapeHTML(value, onlyBr);
        }
        return value;
    }
}
// 当给ComboBox设置的值在对应的Store中不存在时, 设置为空字串
var tempSetValue = Ext.form.ComboBox.prototype.setValue;
Ext.form.ComboBox.prototype.setValue = function(v, allowBlank) {
    var value = v;
    if (allowBlank === true) {
        // 如果找不到的话,就设置为空字符串
        value = '';
        if (this.valueField) {
            var r = this.findRecord(this.valueField, v);
            if (r) {
                value = v;
            }
        }
    }

    tempSetValue.apply(this, [value]);
    delete tempSetValue;
};
/** 添加人: 黄维杰 添加日期: 2009/02/24 添加结束 */
/**
 * 自然数输入控件
 * 
 * @class Ext.form.naturalNumberField
 * @extends Ext.form.NumberField
 */
Ext.form.NaturalNumberField = Ext.extend(Ext.form.NumberField, {
    /**
     * 是否允许输入0
     * 
     * @type Boolean
     */
    allowZero : false,
    // private
    initEvents : function() {
        Ext.form.NaturalNumberField.superclass.initEvents.call(this);
        var allowed = this.baseChars + '';
        this.stripCharsRe = new RegExp('[^' + allowed + ']', 'gi');
        var keyPress = function(e) {
            var k = e.getKey();
            if (!Ext.isIE && (e.isSpecialKey() || k == e.BACKSPACE || k == e.DELETE)) {
                return;
            }
            var c = e.getCharCode();
            if (allowed.indexOf(String.fromCharCode(c)) === -1) {
                e.stopEvent();
            } else {
                var testValue = this.getTestValue(String.fromCharCode(c));
                var value = this.parseValue(testValue)
                if ((this.allowZero && value < 0) || (!this.allowZero && value <= 0)) {
                    e.stopEvent();
                }
            }
        };
        this.el.on("keypress", keyPress, this);
    },
    // private
    onRender : function(ct, position) {
        Ext.form.NaturalNumberField.superclass.onRender.call(this, ct, position);
        this.el.dom.maxLength = 15;
    },
    processValue : function(value) {
        var value = Ext.form.NaturalNumberField.superclass.processValue.call(this, value);
        if (!this.allowZero) {
            if (parseInt(value) === 0) {
                this.setRawValue("");
                return "";
            }
        }
        return value;
    },
    // private
    getTestValue : function(input) {

        var dom = this.el.dom;
        // 选中的文本
        var select = "";
        dom.focus();
        var currentRange = document.selection.createRange();

        if (currentRange && currentRange.parentElement() == dom) {
            select = currentRange.text;
        }
        var workRange = currentRange.duplicate();

        dom.select();
        var allRange = document.selection.createRange();
        // 光标位置
        var len = 0;
        while (workRange.compareEndPoints("StartToStart", allRange) > 0) {
            workRange.moveStart("character", -1);
            len++;
        }
        currentRange.select();
        var array = this.el.getValue().split("");
        array.splice(len, select.length, input);
        return array.join("");
    }
});
Ext.reg('NaturalNumberField', Ext.form.NaturalNumberField);