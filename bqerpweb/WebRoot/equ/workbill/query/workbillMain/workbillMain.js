Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';

Ext.onReady(function() {

	function getCode(psName, postUrl) {
		var result = "";
		var str = postUrl.search.substring(0);
		if (str.indexOf(psName) != -1
				&& (str.substr(str.indexOf(psName) - 1, 1) == "?" || str
						.substr(str.indexOf(psName) - 1, 1) == "&")) {
			if (str.substring(str.indexOf(psName), str.length).indexOf("&") != -1) {
				var Test = str.substring(str.indexOf(psName), str.length);
				result = Test.substr(Test.indexOf(psName), Test.indexOf("&")
								- Test.indexOf(psName));
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			} else {
				result = str.substring(str.indexOf(psName), str.length);
				result = result.substring(result.indexOf("=") + 1,
						result.length);
			}
		}
		return result;

	}

	var id = 0;
	var _url1 = "equ/workbill/query/workbillList/workbillList.jsp";
	var _url2 = "equ/workbill/query/billMessage/BMessage.jsp";
	var _url3 = "equ/workbill/query/plannedCost/plannedCost.jsp";
	var _url4 = "equ/workbill/query/relateworktickets/reworktickets.jsp";
	var _url5 = "equ/workbill/query/relatemateriel/RMateriel.jsp";
	var _url6 = "equ/workbill/query/factualCost/factualCost.jsp";
	var _url7 = "equ/workbill/query/wolog/wolog.jsp";
	var _url8 = "equ/workbill/query/finishedRpt/finishedRpt.jsp";
	var getWocode = "";
	var workorderStatus;
	var woCode;
	var faWoCode;

	var tabPanel = new Ext.TabPanel({
		id : 'tabPanel',
		activeTab : 0,
		border : false,
		// deferredRender : true,
		tabPosition : 'top',
		autoScroll : true,
		items : [{
			id : 'tab1',
			title : '工单列表',
			html : '<iframe id="iframe1" name="iframe1"  src="' + _url1
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab2',
			title : '工单信息',
			listeners : {
				activate : function() {

					// if (getCode("getWocode", iframe2.location) != getWocode
					// )
					if (getWocode != null && getWocode.length > 1
							&& getCode("woCode", iframe2.location) != woCode) {
						iframe2.location = "../../../../" + _url2 + getWocode;
					}
				}
			},
			html : '<iframe id="iframe2" name="iframe2"  src="' + _url2
					+ '" frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab3',
			title : '计划成本',
			listeners : {
				activate : function() {
					if (getWocode != " null"
							&& getWocode.length > 1
							&& (getCode("woCode", iframe3.location) != woCode || getCode(
									"faWoCode", iframe3.location) != faWoCode)) {
						if (woCode == ""&&faWoCode=="")
							iframe3.location = 'about:blank'
						else
							iframe3.location = "../../../../" + _url3 + getWocode;
					}

				}
			},
			html : '<iframe  id="iframe3" name=" iframe3" frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab4',
			title : '相关工作票',
			listeners : {

				activate : function() {
					if (getWocode != null && getWocode.length > 1
							&& getCode("woCode", iframe4.location) != woCode) {

						if (woCode == "")
							iframe4.location = 'about:blank'
						else
							iframe4.location = "../../../../" + _url4 + getWocode;
					}
					// if (iframe4.location == 'about:blank') {
					// tabPanel.setActiveTab(0);
					//
					// }
				}
			},
			html : '<iframe  id="iframe4" name="iframe4" "  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab5',
			title : '相关物料',
			listeners : {
				activate : function() {
					// alert(getWocode)
					if (getWocode != null && getWocode.length > 1
							&& getCode("woCode", iframe5.location) != woCode) {

						if (woCode == "")
							iframe5.location = 'about:blank'
						else
							iframe5.location = "../../../../" + _url5 + getWocode;
					}
					// if (iframe5.location == 'about:blank') {
					// tabPanel.setActiveTab(0);
					//
					// }
				}
			},
			html : '<iframe  id="iframe5" src="'
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab6',
			title : '实际成本',
			listeners : {
				activate : function() {

					if (getWocode != null
							&& getWocode.length > 1
							&& getCode("woCode", iframe6.location) != woCode
							&& (getCode("workorderStatus", iframe6.location) != workorderStatus
							|| workorderStatus == 1)) {
						if (workorderStatus >= 1) {
							iframe6.location = "../../../../" + _url6 + getWocode;

						} else
							iframe6.location = 'about:blank'
					}

				}
			},
			html : '<iframe  id="iframe6" src="'
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab7',
			title : '执行日志',
			listeners : {//modify by drdu 091116
				activate : function() {
					if (getWocode != " null"
							&& getWocode.length > 1
							&& (getCode("woCode", iframe7.location) != woCode || getCode(
									"faWoCode", iframe7.location) != faWoCode)) {
						if (woCode == ""&&faWoCode=="")
							iframe7.location = 'about:blank'
						else
							iframe7.location = "../../../../" + _url7 + getWocode;
					}

				}
//					if (getWocode != null
//							&& getWocode.length > 1
//							&& getCode("woCode", iframe7.location) != woCode
//							&& getCode("workorderStatus", iframe7.location) != workorderStatus) {
//
//						if (woCode == "")
//							iframe7.location = 'about:blank'
//						else
//							iframe7.location = "../../../../" + _url7 + getWocode;
//					}
					// if (iframe7.location == 'about:blank') {
					//
					// tabPanel.setActiveTab(0);
					//
					// }
		//		}
			},
			html : '<iframe  id="iframe7" src="'
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}, {
			id : 'tab8',
			title : '完结报告',
			listeners : {
				activate : function() {
					// alert(workorderStatus);

					if (getWocode != null
							&& getWocode.length > 1
							&& getCode("woCode", iframe8.location) != woCode
							&& (getCode("workorderStatus", iframe8.location) != workorderStatus
							|| workorderStatus == 1)) {
						if (workorderStatus >= 1) {
							iframe8.location = "../../../../" + _url8 + getWocode;

						} else
							iframe8.location = 'about:blank'
					}
					// if (iframe8.location == 'about:blank') {
					// tabPanel.setActiveTab(0);
					// }
				}
			},
			html : '<iframe  id="iframe8" src="'
					+ '"  frameborder="0"  width="100%" height="100%" />'
		}

		]

	});

	function Register() {
		Register.prototype.edit = function(argNo, argFano, argStatus) {
			getWocode = "?woCode=" + argNo + "&faWoCode=" + argFano
					+ "&workorderStatus=" + argStatus;
			workorderStatus = argStatus;
			woCode = argNo;
			faWoCode = argFano;
			tabPanel.setActiveTab(1);
		}
		Register.prototype.rowl = function(argNo, argFano, argStatus) {
			getWocode = "?woCode=" + argNo + "&faWoCode=" + argFano
					+ "&workorderStatus=" + argStatus;
			workorderStatus = argStatus;
			woCode = argNo;
			faWoCode = argFano;
		}
		Register.prototype.parentoAdd = function(argNo, argShowno) {
			getWocode = "?woCodeadd=" + argNo + "&woCodeShowAdd=" + argShowno;
			tabPanel.setActiveTab(1);
		}
		Register.prototype.Add = function() {
			getWocode = "";
			tabPanel.setActiveTab(1);
			iframe2.location = "../../../../" + _url2;
		}
		// Register.prototype.Complete = function(){
		//			
		// }
	}
	var register = new Register();
	Ext.getCmp('tabPanel').register = register;

	var layout = new Ext.Viewport({
				layout : "border",
				border : false,
				items : [{
							region : "center",
							layout : 'fit',
							collapsible : true,
							split : true,
							border : false,
							margins : '0 0 0 0',
							// 注入表格
							items : [tabPanel]
						}]
			})
})
