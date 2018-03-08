// 设置树的点击事件

Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.useShims = true;
Ext.onReady(function() {
	function treeClick(node, e) {
		if (node.isLeaf()) {
			e.stopEvent();
			Ext.Ajax.request({
						url : 'system/insertLogin.action',
						method : 'post',
						params : {
							loginFile : node.attributes.href
						},
						susscess : function() {
						},
						failure : function() {
							Ext.Msg.alert("操作失败，请联系管理员！")
						}
					})
			var n = tab.getComponent(node.id);
			if (!n) {
				var n = tab.add({
					'id' : node.id,
					'title' : node.text,
					iconCls : 'function',
					closable : true,
					html : "<iframe src='"
							+ node.attributes.href
							+ "' width='100%' height='100%' frameborder='0'></iframe>"
				});
			}
			tab.setActiveTab(n);
		}
		node.toggle();
	}

	// 生成标签页
	var tab = new Ext.TabPanel({
		region : 'center',
		id : "mainTabPanel",
		deferredRender : false,
		activeTab : 0,
		resizeTabs : true, // turn on tab resizing
		minTabWidth : 115,
		tabWidth : 135,
		enableTabScroll : true,
		plugins : new Ext.ux.TabCloseMenu,
		listeners : {
			render : function(t) {
				var n = t.add({
					'id' : -1,
					iconCls : 'locked',
					'title' : '首页',
					closable : false,
					html : "<iframe src='system/business/content/content.jsp' width='100%' height='100%' frameborder='0'></iframe>"
				});
				t.setActiveTab(n);
			},
			delay : 10
		}
	});

	var Tree = Ext.tree;
	var allFilestree = new Tree.TreePanel({
				root : allroot,
				autoHeight : true,
				animate : true,
				enableDD : false,
				border : false,
				rootVisible : true,
				containerScroll : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'system/findFiles.action'
						})
			});
	var allroot = new Tree.AsyncTreeNode({
				text : '漕泾热电厂',
				draggable : false,
				id : '1'
			});
	allFilestree.setRootNode(allroot);
	allFilestree.on('click', treeClick);
	var tree = new Tree.TreePanel({
				autoHeight : true,
				root : root,
				animate : true,
				enableDD : false,
				border : false,
				rootVisible : false,
				containerScroll : true,
				loader : new Tree.TreeLoader({
							dataUrl : 'system/findFilesByWorkerId.action'
						})
			});
	var root = new Tree.AsyncTreeNode({
				text : '漕泾电厂',
				draggable : false,
				id : '1'
			});
	tree.setRootNode(root);
	tree.on('click', treeClick);
	var _tar = new Ext.Toolbar({
		buttons : [{
			text : '修改密码',
			iconCls : 'add',
			handler : function() {
				return modifyPass();
			} 
		},{
			text : '重新登录',
			iconCls : 'add',
			handler : function() { 
				 if (window.confirm("确定要重新登录系统吗？")) 
				 {
				 	window.location = "/power";
				 }
			} 
		},{
			text : '退出',
			iconCls : 'add',
			handler : function() {
				if (window.confirm("确定要退出系统吗？")) 
				{
					closeWindow();
				}
			} 
		}

		]
	});
	var viewport = new Ext.Viewport({
				layout : 'border',
				items : [
//				new Ext.BoxComponent({
//									region : 'north',
//									el : 'north-div',
//									height : 80
//								}),
								{
							region : 'west',
							id : 'west-panel',
							split : true,
							width : 250,
							layout : 'fit',
							minSize : 175,
							maxSize : 400,
							margins : '0 0 0 0',
							bbar:_tar,
							title : '系统菜单',
							collapsible : true,
							items : [new Ext.TabPanel({
										tabPosition : 'bottom',
										activeTab : 0,
										layoutOnTabChange : true,
										items : [{
													title : '我的面板',
													border : false,
													autoScroll : true,
													items : [tree],
													listeners : {
														'activate' : function() {
															root.expand();
														}
													}
												}]
									})]
						}, tab// 初始标签页
				]
			});
	tab.setHeight(tab.getInnerHeight() - 12);
	setTimeout(function() {
				Ext.get('loading').remove();
				Ext.get('loading-mask').fadeOut({
							remove : true
						});
			}, 250); 
	 var conn = Ext.lib.Ajax.getConnectionObject().conn;
	conn.open("POST", "comm/getCurrentSessionEmployee.action", false);
	conn.send(null); 
	// 成功状态码为200
	if (conn.status == "200") {
		var picData = eval('(' + conn.responseText + ')');
		Ext.getCmp("west-panel").setTitle("<div id=\"west-title\">当前用户： "+picData.workerName+"("+picData.workerCode+")</div>");
	}
   
});

function Go(type) {
	switch (type) {
		case 1 : {
			if (!window.confirm("确定要重新登录此系统吗？"))
				return false;
			break;
		}
	}
}

// function op() {
// parent.formx.action = "changepassword.htm";
// parent.formx.submit();
// // alert("您返回的值为：" + y)
// }

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
		labelWidth : 60,
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
					fieldLabel : '确认密码',
					inputType : 'password',
					id : 'newPwdConfirm'
				}],
		buttons : [{
			text : '保存',
			handler : function() {
				if (Ext.get("newPwd").dom.value == ""
						|| Ext.get("newPwdConfirm").dom.value == "") {
					Ext.Msg.alert("提示", "新密码/确认密码不能为空!");
					return false;
				}
				if ((Ext.get("newPwd").dom.value != Ext.get("newPwdConfirm").dom.value)) {
					Ext.Msg.alert("提示", "新密码/确认密码不一致!");
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
			el : 'div-buttons',
			width : 300,
			height : 200,
			closeAction : 'hide',
			items : [changePwdForm]
		});
// Ext.get("a_changepwd").dom.onclick = function() {
// win.show();
// return false;
// }
function modifyPass() {
	win.show();
	return false;
}
function closeWindow() {
	window.opener = null;
	window.open("", "_self", ""); // 加上这一句，对IE7有效
	window.close();
}

Ext.ux.TabCloseMenu = function() {
	var tabs, menu, ctxItem;
	this.init = function(tp) {
		tabs = tp;
		tabs.on('contextmenu', onContextMenu);
	}

	function onContextMenu(ts, item, e) {
		if (!menu) { // create context menu on first right click
			menu = new Ext.menu.Menu([{
						id : tabs.id + '-close',
						text : '关闭标签',
						handler : function() {
							tabs.remove(ctxItem);
						}
					}, {
						id : tabs.id + '-close-others',
						text : '关闭其他标签',
						handler : function() {
							tabs.items.each(function(item) {
										if (item.closable && item != ctxItem) {
											tabs.remove(item);
										}
									});
						}
					}, {
						id : tabs.id + '-close-all',
						text : '关闭全部标签',
						handler : function() {
							tabs.items.each(function(item) {
										if (item.closable) {
											tabs.remove(item);
										}
									});
						}
					}]);
		}
		ctxItem = item;
		var items = menu.items;
		items.get(tabs.id + '-close').setDisabled(!item.closable);
		var disableOthers = true;
		tabs.items.each(function() {
					if (this != item && this.closable) {
						disableOthers = false;
						return false;
					}
				});
		items.get(tabs.id + '-close-others').setDisabled(disableOthers);
		var disableAll = true;
		tabs.items.each(function() {
					if (this.closable) {
						disableAll = false;
						return false;
					}
				});
		items.get(tabs.id + '-close-all').setDisabled(disableAll);
		menu.showAt(e.getPoint());
	}
};

$(document).ready(function() {
			$('#dock').Fisheye({
						maxWidth : 50,
						items : 'a',
						itemsText : 'span',
						container : '.dock-container',
						itemWidth : 40,
						proximity : 70,
						halign : 'center'
					})
		});