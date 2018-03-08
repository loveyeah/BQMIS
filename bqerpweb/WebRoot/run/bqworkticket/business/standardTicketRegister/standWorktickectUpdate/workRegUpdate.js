Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	// 总tabpanel
	var tabPanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 3,
		tabPosition : 'bottom',
		id : "tabPanel",
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			id : 'tabBaseInfo',
			title : '标准票明细'
		}, {
			id : "tabSafety",
			title : '标准票安措'
		}, {
			id : "tabDanger",
			title : '标准票危险点'
		}, {
			id : 'tabReport',
			title : '标准票上报列表',
			html : "<iframe name='tabReport' src='run/bqworkticket/business/standardTicketRegister/standWorktickectUpdate/workUpdateList.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		}]
	});

	// 设定布局器及面板
	var layout = new Ext.Viewport({
		layout : "border",
		border : false,
		items : [{
			title : "",
			region : 'center',
			layout : 'fit',
			border : false,
			margins : '0 0 0 0',
			split : true,
			collapsible : false,
			items : [tabPanel]
		}]
	});

	function Register() {
		// 工作票编号
		this.workticketNo = null;
		this.workFlowNo = null;
		this.firelevelId = null;
		// 所属机组或系统
		this.equAttributeCode = null;
		// 工作票种类/工作票类型编码
		this.workticketTypeCode = null;
		this.workFlowNo = null;
		this.firelevelId = null;
		// 通过工作票编号加载工作票记录的监听器
		this.loadRecord = null;
		// 新增工作票的监听器
		this.addRecord = null;
		// 所属机组或系统变更时的监听器
		this._changeAttCodeHandlers = [];
		// 数据变更时更新Grid的监听器
		this.workticketChangeHandler = null;
		// 由现有票生成时, 更新安措内容
		this.changeSaftyHandler = null;

		// 更新工作票危险点
		this.changeDangerHandler = null;

		if (typeof Register._intialized == 'undefined') {
			Register._intialized = true;
			// 从第一个页面迁移到第二个页面时
			Register.prototype.toNext = function() {
				// 检查工作票编号
				this._checkWorkticketNo();
				// 设置第二个页面为活动页面
				this.toTab3();

					// 更新上报列表
					// this.updateReportList();
			}

			// 追加工作票
			Register.prototype.add = function() {
				// 所属机组或系统
				this.equAttributeCode = null;
				// 工作票种类/工作票类型编码
				this.workticketTypeCode = null;
				this.workFlowNo = null;
				this.firelevelId = null;
				// 检查工作票编号
				this._changeWorkticketNo(null);
				// 设置第一个页面为活动页面
				this.toTab1();

				if (this.addRecord && typeof this.addRecord == 'function') {
					// 新增工作票
					this.addRecord.apply(this);
				}
			}

			// 删除工作票
			Register.prototype.deleteWorkticket = function(argNo) {
				if (this.workticketNo != argNo) {
					return;
				}
				// 所属机组或系统
				this.equAttributeCode = null;
				// 工作票种类/工作票类型编码
				this.workticketTypeCode = null;
				this.workFlowNo = null;
				this.firelevelId = null;
				// 检查工作票编号
				this._changeWorkticketNo(null);

				if (this.addRecord && typeof this.addRecord == 'function') {
					// 新增工作票
					this.addRecord.apply(this);
				}
			}

			// 编辑工作票
			Register.prototype.edit = function(argNo) {
				// 设置工作票编号

				this.workticketNo = argNo;
				// 设置第一个页面为活动页面
				this._checkWorkticketNo();
				this.toTab1();

				if (this.loadRecord && typeof this.loadRecord == 'function') {
					// 通过工作票编号加载工作票记录
					this.loadRecord.call(this, this.workticketNo);
				}

					// 检查工作票编号
					// this._checkWorkticketNo();
			}

			// 上报工作票
			Register.prototype.report = function() {
				// modify by fyyang 090316
				// 更新上报列表
				this.updateReportList();
				// add by fyyang
				this.locationName = null;
				this.firelevel = null;
				this.workFlowNo = null;
				this.isStandard = null;
				this.isCreateByStandard = null;
				// 所属机组或系统
				this.equAttributeCode = null;
				// 工作票种类/工作票类型编码
				this.workticketTypeCode = null;

				// 设置第一个页面为活动页面
				this.toTab1();

				if (this.addRecord && typeof this.addRecord == 'function') {
					// 新增工作票
					this.addRecord.apply(this);
				}
				// 检查工作票编号
				this._changeWorkticketNo(null);
			}

			// 更新上报列表
			Register.prototype.updateReportList = function() {
				// 数据变更时更新Grid
				if (this.workticketChangeHandler
						&& typeof this.workticketChangeHandler == 'function') {
					this.workticketChangeHandler.apply(this);
				}
			}

			// 更新安措内容
			Register.prototype.refreshSafty = function() {
				// 由已终结票生成时
				if (this.changeSaftyHandler
						&& typeof this.changeSaftyHandler == 'function') {
					this.changeSaftyHandler.apply(this);
				}
			}
			// changeDanger

			Register.prototype.refreshDanger = function() {
				// 由现有票生成时，更新危险点
				if (this.changeDangerHandler
						&& typeof this.changeDangerHandler == 'function') {
					this.changeDangerHandler.apply(this);
				}
			}

			// 迁移到工作票明细Tab页
			Register.prototype.toTab1 = function() {
				tabPanel.setActiveTab('tabBaseInfo');
			}

			// // 迁移到工作票内容Tab页
			// Register.prototype.toTab2 = function() {
			// tabPanel.setActiveTab('tabContent');
			// }

			// 迁移到工作票安措Tab页
			Register.prototype.toTab3 = function() {
				tabPanel.setActiveTab('tabSafety');
			}
			// 迁移到工作票危险点Tab页
			Register.prototype.toTab4 = function() {
				tabPanel.setActiveTab('tabDanger');
			}

			// 设置所属机组或系统
			Register.prototype.setEquAttributeCode = function(argCode) {
				if (this.equAttributeCode != argCode) {
					this.equAttributeCode = argCode;

					// 所属机组或系统变更时的监听器
					for (var i = 0; i < this._changeAttCodeHandlers.length; i++) {
						var fun = this._changeAttCodeHandlers[i];
						if (fun && typeof fun == 'function') {
							fun.call(this, argCode);
						}
					}
				}
			}

			// 添加所属机组或系统变更时的监听器
			Register.prototype.addTypeCodeChange = function(argHandler) {
				if (!argHandler || !(typeof argHandler == 'function')) {
					return;
				}
				this._changeAttCodeHandlers.push(argHandler);
			}

			// private
			Register.prototype._changeWorkticketNo = function(argNo) {
				// 设置工作票编号
				this.workticketNo = argNo;
				// 检查工作票编号
				this._checkWorkticketNo();
			}

			// 检查工作票编号
			Register.prototype._checkWorkticketNo = function() {
				var tab1 = tabPanel.getItem('tabBaseInfo');
				// 工作票内容
				// var tab2 = tabPanel.getItem('tabContent');
				// 工作票安措
				var tab3 = tabPanel.getItem('tabSafety');
				// add
				// 工作票危险点
				var tab4 = tabPanel.getItem("tabDanger");
				var tab4Href = 'run/bqworkticket/business/standardTicketRegister/danger/workticketDanger.jsp';
				var tab4Html = "<iframe name='tabDanger' src='"
						+ tab4Href
						+ "'style='width:100%;height:100%;border:0px;'></iframe>";
				// 工作票内容链接地址
				// var tab2Href =
				// 'run/bqworkticket/business/standardTicketRegister/content/workticketContent.jsp';
				// var tab2Html = "<iframe name='tabContent' src='"
				// + tab2Href
				// + "'style='width:100%;height:100%;border:0px;'></iframe>";;
				// 工作票安措链接地址
				var tab3Href = 'run/bqworkticket/business/standardTicketRegister/safety/workticketSafety.jsp';
				var tab3Html = "<iframe name='tabSafety' src='"
						+ tab3Href
						+ "'style='width:100%;height:100%;border:0px;'></iframe>";
				var tab1Href = 'run/bqworkticket/business/standardTicketRegister/baseInfo/workticketBaseInfoForUpdate.jsp';
				var tab1Html = "<iframe name='tabBaseInfo' src='"
						+ tab1Href
						+ "'style='width:100%;height:100%;border:0px;'></iframe>";;

				// 如果已经设置了工作票编号
				if (this.workticketNo) {
					// if (tab2.rendered) {
					// Ext.getCmp('tabPanel').remove(tab2);
					// Ext.getCmp('tabPanel').insert(1, {
					// id : tab2.id,
					// title : tab2.title,
					// html : tab2Html
					// });
					// } else {
					// tab2.html = tab2Html;
					//
					// // 设置工作票内容页面可用
					// tabPanel.getItem('tabContent').un('beforerender',
					// this.blockShow);
					// }

					if (tab1.rendered) {
						Ext.getCmp('tabPanel').remove(tab1);
						Ext.getCmp('tabPanel').insert(0, {
							id : tab1.id,
							title : tab1.title,
							html : tab1Html
						});
					} else {
						tab1.html = tab1Html;

						// 设置工作票危险点页面可用
						tabPanel.getItem('tabBaseInfo').un('beforerender',
								this.blockShow);
					}
					if (tab3.rendered) {
						Ext.getCmp('tabPanel').remove(tab3);
						Ext.getCmp('tabPanel').insert(1, {
							id : tab3.id,
							title : tab3.title,
							html : tab3Html
						});
					} else {
						tab3.html = tab3Html;

						// 设置工作票危险点页面可用
						tabPanel.getItem('tabDanger').un('beforerender',
								this.blockShow);
					}

					if (tab4.rendered) {
						Ext.getCmp('tabPanel').remove(tab4);
						Ext.getCmp('tabPanel').insert(2, {
							id : tab4.id,
							title : tab4.title,
							html : tab4Html
						});
					} else {
						tab4.html = tab4Html;

						// 设置工作票安措页面可用
						tabPanel.getItem('tabSafety').un('beforerender',
								this.blockShow);
					}
				} else {
					if (tab1.rendered) {
						Ext.getCmp('tabPanel').remove(tab1);
						Ext.getCmp('tabPanel').insert(1, {
							id : tab1.id,
							title : tab1.title,
							html : tab1Html
						});
					}
					// 工作票危险点
					if (tab3.rendered) {
						Ext.getCmp('tabPanel').remove(tab3);
						Ext.getCmp('tabPanel').insert(1, {
							id : tab3.id,
							title : tab3.title,
							html : tab3Html
						});
					}
					// 工作票安措
					if (tab4.rendered) {
						Ext.getCmp('tabPanel').remove(tab4);
						Ext.getCmp('tabPanel').insert(2, {
							id : tab4.id,
							title : tab4.title,
							html : tab4Html
						});
					}

					// 设置工作票内容页面不可用
					// tabPanel.getItem('tabContent').on('beforerender',
					// this.blockShow);
					// 设置工作票安措页面不可用
					tabPanel.getItem('tabBaseInfo').on('beforerender',
							this.blockShow);
					tabPanel.getItem('tabSafety').on('beforerender',
							this.blockShow);
					// 设置工作票危险点页面不可用
					tabPanel.getItem('tabDanger').on('beforerender',
							this.blockShow);
				}
			}

			Register.prototype.blockShow = function() {
				return false;
			}
			// add by fyyang 090407
			Register.prototype.updateSafetyAndDanger = function() {
				this.refreshSafty();
				this.refreshDanger();
			}
		}
	}
	var register = new Register();
	Ext.getCmp('tabPanel').register = register;
	tabPanel.getItem('tabBaseInfo').on('beforerender', register.blockShow);
	// 设置工作票内容页面不可用
	// tabPanel.getItem('tabContent').on('beforerender', register.blockShow);
	// 设置工作票安措页面不可用
	tabPanel.getItem('tabSafety').on('beforerender', register.blockShow);
	// 设置工作票危险点页面不可用
	tabPanel.getItem('tabDanger').on('beforerender', register.blockShow);
		// tabPanel.setActiveTab(3);
});
