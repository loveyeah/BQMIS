function ItemBaseAttr() {
	// 公式内容属性
	this.formulaContentType = {
		item : 1,// 指标
		data : 2,// 数据
		operate : 3, // 操作符 
		input : 4 //输入字符
	};
	// 运算符
	this.operates = [{
				key : '(',
				value : '('
			}, {
				key : '+',
				value : '+'
			}, {
				key : '-',
				value : '-'
			}, {
				key : '*',
				value : '*'
			}, {
				key : '/',
				value : '/'
			}, {
				key : ')',
				value : ')'
			}];
};
function spellExtendFormualByStore(store) {
	var strTemp = "";
	for (var i = 0; i < store.getCount(); i++) {
		var rec = store.getAt(i);
		if (rec.get("fornulaType") == itemBaseAttr.formulaContentType.item)
			strTemp += " [" + rec.get("formulaContent") + "] ";
		else
			strTemp += " " + rec.get("formulaContent") + " ";
	}
	Ext.get("winspace").dom.value = strTemp;
}
function spellExtendFormual(list) {
	if (list == null || list.length == 0) {
		return "";
	}
	var strTemp = "";
	for (var i = 0; i < list.length; i++) {
		var rec = list[i];
		if (rec.fornulaType == itemBaseAttr.formulaContentType.item)
			strTemp += " [" + rec.formulaContent + "] ";
		else
			strTemp += " " + rec.formulaContent + " ";
	}
	return strTemp;
}

function spellTimeFormual(list) {
	if (list == null || list.length == 0) {
		return "";
	}

	data : [['1', '高峰'], ['2', '腰荷'], ['3', '低谷']]

	var strTemp = "";
	var strTemp = "";
	for (var i = 0; i < list.length; i++) {
		var rec = list[i];

		strTemp += rec.runFormulaInfo.operatorCode
				+ rec.runDataCodeName
				+ "("
				+ (rec.runFormulaInfo.sdType == '1'
						? '高峰'
						: (rec.runFormulaInfo.sdType == '2' ? '腰荷' : '低谷'))
				+ ")";

	}
	strTemp = strTemp.substring(1);
	return strTemp;
}

function spellTableFormual(list) {
	if (list == null || list.length == 0) {
		return "";
	}

	var strTemp = "";
	for (var i = 0; i < list.length; i++) {
		var rec = list[i];
		
		strTemp += rec.runFormulaInfo.operatorCode + rec.runDataCodeName;
	}
	strTemp = strTemp.substring(1);

	return strTemp;
}

function spellEnthalpyFormual(o) {
	if (o == null || o.length == 0) {
		return "";
	}
	var strTemp = "焓值(" + o.ylZbbmName + "," + o.wdZbbmName + ")";

	return strTemp;
}

var itemBaseAttr = new ItemBaseAttr();