Ext.useShims = true;
Ext.onReady(function() { 
	Ext.QuickTips.init();
	function addHref(url,params)
	{
		if(url.indexOf("?")== -1)
		{
			url +="?" + params;
		}else
		{
			url +="&" + params;
		}
		return url;
	}
	Ext.menu.Menu.prototype.onClick = function(e){
		var cuItem= this.findTargetItem(e);
		 if (cuItem.isLeaf == "true"||cuItem.isLeaf =='Y') {
			var n = tab.getComponent(cuItem.id);
			var iframeId = iframeName + cuItem.id;
			if (!n) {
				var n = tab.add({
					'id' : cuItem.id,
					'title' : cuItem.text,
					iconCls : 'tab-item-unlock',
					border : true,
					autoScroll : true,
					autoHeight : true,
					closable : true,
					html : '<iframe id="' + iframeId + '" name="' + iframeName
							+ '" src=' + addHref(cuItem.url,"id="+cuItem.id)
							+ ' width="100%" frameborder="0"></iframe>'
				});
			}
			tab.setActiveTab(n);
			addFocusToIframe(document.getElementById(iframeId));
			this.hide(true);
			e.stopEvent();
		}
		// 是目录
		else {
		 	e.stopEvent();
		} 
	};
	
	var userName = document.getElementById("userName").value;
	var iframeName = "iframe-content"; 
	var welcomeFile = "/power/index.jsp";
	function addFocusToIframe(iframeObj) {
		if (document.all) {
			iframeObj.attachEvent("onfocus", hiddenMenu);
		} else {
			iframeObj.contentWindow.addEventListener("focus", hiddenMenu, true);
		}
		iframeObj.setAttribute("height",tab.getInnerHeight()-23);
	}
	function hiddenMenu() {
		jsonMenu.hide();
	}
	function getActiveTabFrameId() {
		return iframeName + tab.getActiveTab().getId();
	} 
    var jsonMenu;
	var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", "system/findMenusByWorkerId.action", false);
	conn.send(null);
	if (conn.status == "200") {
		menuObj = Ext.util.JSON.decode(conn.responseText);
		jsonMenu = new Ext.menu.Menu({
			id : 'mainMenu',
			cls : "menu",
//			style:{'position':'relative','z-index':100}, 
			items : menuObj
		});
	}

	
	
//	jsonMenu = new Ext.menu.Menu({
//		id : 'mainMenu',
//		cls : "menu",
//		items : menuObj,
//		listeners : {
//			'itemClick' : handlerItemClick
//		}
//	}); 
	function handlerItemClick(cuItem) { 
		// 是网页 
		if (cuItem.isLeaf == "true"||cuItem.isLeaf =='Y') {
			var n = tab.getComponent(cuItem.id);
			var iframeId = iframeName + cuItem.id;
			if (!n) {
				var n = tab.add({
					'id' : cuItem.id,
					'title' : cuItem.text,
					iconCls : 'tab-item-unlock',
					border : true,
					autoScroll : true,
					autoHeight : true,
					closable : true,
					html : '<iframe id="' + iframeId + '" name="' + iframeName
							+ '" src=' + cuItem.url
							+ ' width="100%" frameborder="0"></iframe>'
				});
			}
			tab.setActiveTab(n);
			addFocusToIframe(document.getElementById(iframeId));
		}
		// 是目录
		else {
			return false;
		}
	}
	
	var changePwdForm = new Ext.FormPanel({
	id : 'form',
	frame : true,
	labelAlign : 'left',
	autoWidth : true,
	autoHeight : true,
	bodyStyle : 'padding:10px',
	items : [{
		xtype : 'fieldset',
		labelAlign : 'right',
		buttonAlign : 'center',
		labelWidth : 140,
		title : '修改密码',
		defaults : {
			width : 150
		},
		defaultType : 'textfield',
		autoHeight : true,
		items : [{
					fieldLabel : '当前密码',
					inputType : 'password',
					id : 'user.loginPwd'
				}, {
					fieldLabel : '新密码',
					inputType : 'password',
					id : 'newPwd'
				}, {
					fieldLabel :'确认新密码',
					inputType : 'password',
					id : 'newPwdConfirm'
				}],
		buttons : [{
			text : '保存',
			handler : function() {
				if (Ext.get("newPwd").dom.value == ""
						|| Ext.get("newPwdConfirm").dom.value == "") {
					Ext.Msg.alert('提示', "新密码/确认密码不能为空!");
					return false;
				}
				if ((Ext.get("newPwd").dom.value != Ext.get("newPwdConfirm").dom.value)) {
					Ext.Msg.alert('提示', "新密码/确认密码不一致!");
					return false;
				}
				changePwdForm.getForm().submit({
					method : 'post',
					url : 'system/modifyPwd.action',
					waitMsg : '正在修改密码...',
					success : function(form, action) {
						Ext.Msg.alert('', '密码修改成功,下次登录请使用新密码!');
						win.hide();
					},
					failure : function(form, action) {
						if (action.failureType == "connect") {
							Ext.Msg.alert('服务器异常', '服务器异常，请与管理员联系！');
						} else {
							Ext.MessageBox.show({
										title : '错误',
										msg : '数据保存失败！'
												+ action.response.responseText,
										buttons : Ext.Msg.OK,
										icon : Ext.MessageBox.ERROR
									});
						}
					}
				});
			}
		},{
			text:'取消',
			handler:function()
			{
				win.hide();
			}
		}]
	}]
});
var win = new Ext.Window({
			width : 400,
			height : 200,
			closeAction : 'hide',
			items : [changePwdForm]
		});
function modifyPass() {
	win.show();
	return false;
}
	
	// ///////////////////////// 
	var tb = new Ext.Toolbar({
		cls : 'transbg', 
		items : [{
			text :'转到', 
			iconCls:'goto',
			menu : jsonMenu
		}, '&nbsp;', {
			text : '修改密码',
			iconCls : 'modify-pwd',
			handler : function() {
				 modifyPass();
			}
		}, '&nbsp;', {
			text : '重新登陆',
			iconCls : 're-login',
			handler : function() {
				 Ext.Msg.confirm('提示', '确定要重新登陆？', function(r) {
					if (r == 'yes')
						window.location = welcomeFile;
				});
			}
		}, '&nbsp;', {
			text : '退出关闭',
			iconCls : 'signout',
			handler : function() {
				 Ext.Msg.confirm('提示','确定要关闭页面？',function(r){ 
				 	if(r == 'yes')
				 	window.close();
				 }); 
			}
		}, '&nbsp;', {
			text : '帮助',
			iconCls : 'help',
			menu : new Ext.menu.Menu({
				items : [{
					text : '生产系统',
					iconCls : 'goto',
					handler : function() {
						alert();
					}
				}, {
					text : '经营系统',
					iconCls : '',
					handler : function() {
						return false;
					}
				}]
			})
		},'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;']
	});  
	var tab = new Ext.TabPanel({
		region : 'center',
		id : "mainTabPanel",
		deferredRender : false,
		activeTab : 0,
		resizeTabs : false,
		minTabWidth : 50,
		tabWidth : 100, 
		enableTabScroll : true,
		plugins : new Ext.ux.TabCloseMenu,
		listeners : {
			render : function(t) {
			    var iframeId = iframeName+(-1);
				var n = t.add({
					'id' : -1,
					iconCls : 'first-tab',
					'title' : '首页',
					closable : false,
					html : '<iframe id="' + iframeId + '" src="system/business/content/content.jsp" name="'+iframeName+'" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>'
				});
				t.setActiveTab(n); 
				addFocusToIframe(document.getElementById(iframeId));
			},
			delay : 10
		}
	});

	var headPanel = new Ext.Panel({
		region : 'north',
		height : '40',
		html : "<table width=\"100%\" height=\"100%\" border=\"0\" class=\"bgnb\">\n"
				+ "  <tr>\n"
				+ "    <td width=\"30\" valign=\"middle\">\n"
				+ "    &nbsp;&nbsp;<img src=\"comm/images/redqi.gif\" border=\"0\" width=\"20\" height=\"20\"/>\n"
				+ "    </td>\n"
				+ "    <td width=\"260\" nowrap align=\"left\" valign=\"top\">\n"
				+ "    欢迎，"
				+ userName
				+ "\n"
				+ "    </td>\n"
				+ "    <td align=\"right\" valign=\"top\">\n"
				+ "      <div id=\"toolbar\"></div>\n"
				+ "    </td>\n"
				+ "    <td width=\"50\"></td>\n" + "  </tr>\n" + "</table>"
	});
	var view = new Ext.Viewport({
		layout : 'border',
		items : [headPanel, tab]
	}); 
	tb.render("toolbar");
	
},this, {delay: Ext.isGecko3? 100 : 0});