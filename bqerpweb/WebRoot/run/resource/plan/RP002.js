Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
Ext.onReady(function() {
	Ext.QuickTips.init();

	// 总tabpanel
	var tabPanel = new Ext.TabPanel({
		renderTo : document.body,
		activeTab : 0,
		tabPosition : 'bottom',
		id : "tabPanel",
		plain : true,
		defaults : {
			autoScroll : true
		},
		frame : false,
		border : false,
		items : [{
			id : 'tabRegister',
			title : '登记',
			html : "<iframe name='tabBaseInfo' src='run/resource/plan/register/RP002_register.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
			}, {
			id : 'tabQuery',
			title : '查询',
			html : "<iframe name='tabBaseInfo' src='run/resource/plan/query/RP002_query.jsp' style='width:100%;height:100%;border:0px;'></iframe>"
		}]
	});
	function Register() {
		// 申请单ID
		this.headId = null;
		// 通过申请单Id加载的监听器
		this.loadRecord = null;
        this.findRecord = null;
		// 新增申请单的监听器
		this.addRecord = null;
		// 数据变更时更新Grid的监听器
		this.headIdChangeHandler = null;

		if (typeof Register._intialized == 'undefined') {
			Register._intialized = true;
			// 追加工作票
			Register.prototype.add = function() {
				// 检查申请单Id
				this._changeHeadId(null);
				// 设置第二个页面为活动页面
				this.toTab2();
				if (this.addRecord && typeof this.addRecord == 'function') {
					// 新增工作票
					this.addRecord.apply(this);
				}
			}
			// 编辑工作票
			Register.prototype.edit = function(argNo) {
				// 设置工作票编号
				this.headId = argNo;
				// 设置第一个页面为活动页面
				this.toTab2();

				if (this.loadRecord && typeof this.loadRecord == 'function') {
					// 通过工作票编号加载工作票记录
					this.loadRecord.call(this, this.headId);
				}

			}
            Register.prototype.find = function() {
                // 设置第一个页面为活动页面
                this.toTab1();
                
                if (this.findRecord && typeof this.findRecord == 'function') {
                    // 通过工作票编号加载工作票记录
                    this.findRecord.call(this, this.headId);
                }
            }

            Register.prototype.findH = function() {
                if (this.findRecord && typeof this.findRecord == 'function') {
                    // 通过工作票编号加载工作票记录
                    this.findRecord.call(this, this.headId);
                }
            }
			// 更新上报列表
			Register.prototype.updateReportList = function() {
				// 数据变更时更新Grid
				if (this.headIdChangeHandler
						&& typeof this.headIdChangeHandler == 'function') {
					this.headIdChangeHandler.apply(this);
				}
			}

			// 迁移到查询Tab页
			Register.prototype.toTab1 = function() {
				tabPanel.setActiveTab('tabQuery');
			}

			// 迁移到登记Tab页
			Register.prototype.toTab2 = function() {
				tabPanel.setActiveTab('tabRegister');
			}

			// private
			Register.prototype._changeHeadId = function(argNo) {
				// 设置工作票编号
				this.headId = argNo;

			}

			Register.prototype.blockShow = function() {
				return false;
			}
		}
	}
	var register = new Register();
	Ext.getCmp('tabPanel').register = register;

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
});
