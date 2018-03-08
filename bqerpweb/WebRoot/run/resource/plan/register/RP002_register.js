Ext.BLANK_IMAGE_URL = 'comm/ext/resources/images/default/s.gif';
var register = parent.Ext.getCmp('tabPanel').register;
var entryId = "";

var isDeleted = false;
Ext.onReady(function() {
	Ext.form.Label.prototype.setText = function(argText) {
		this.el.dom.innerHTML = argText;
	}
	Ext.override(Ext.grid.GridView, {
		ensureVisible : function(row, col, hscroll) {
			if (typeof row != "number") {
				row = row.rowIndex;
			}
			if (!this.ds) {
				return;
			}
			if (row < 0 || row >= this.ds.getCount()) {
				return;
			}
			col = (col !== undefined ? col : 0);
			var rowEl = this.getRow(row), cellEl;
			if (!(hscroll === false && col === 0)) {
				while (this.cm.isHidden(col)) {
					col++;
				}
				cellEl = this.getCell(row, col);
			}
			if (!rowEl) {
				return;
			}
			var c = this.scroller.dom;
			var ctop = 0;
			var p = rowEl, stop = this.el.dom;
			while (p && p != stop) {
				ctop += p.offsetTop;
				p = p.offsetParent;
			}
			ctop -= this.mainHd.dom.offsetHeight;
			var cbot = ctop + rowEl.offsetHeight;
			var ch = c.clientHeight;
			var stop = parseInt(c.scrollTop, 10);
			var sbot = stop + ch;
			if (ctop < stop) {
				c.scrollTop = ctop;
			} else if (cbot > sbot) {
				c.scrollTop = cbot - ch;
			}
			if (hscroll !== false) {
				var cleft = parseInt(cellEl.offsetLeft, 10);
				var cright = cleft + cellEl.offsetWidth;
				var sleft = parseInt(c.scrollLeft, 10);
				var sright = sleft + c.clientWidth;
				if (cleft < sleft) {
					c.scrollLeft = cleft;
				} else if (cright > sright) {
					c.scrollLeft = cright - c.clientWidth;
				}
			}
			return cellEl ? Ext.fly(cellEl).getXY() : [this.el.getX(),
					Ext.fly(rowEl).getY()];
		},
		doRender : function(cs, rs, ds, startRow, colCount, stripe) {
			var ts = this.templates, ct = ts.cell, rt = ts.row, last = colCount
					- 1;
			var tstyle = 'width:' + this.getTotalWidth() + ';';
			// buffers
			var buf = [], cb, c, p = {}, rp = {
				tstyle : tstyle
			}, r;
			for (var j = 0, len = rs.length; j < len; j++) {
				r = rs[j];
				cb = [];
				var rowIndex = (j + startRow);
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i == 0 ? 'x-grid3-cell-first ' : (i == last
							? 'x-grid3-cell-last '
							: '');
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					// 判断是否是最后行
					if (r.data["isNewRecord"] == "total") {
						// 替换掉其中的背景颜色
						p.style = c.style
								.replace(/background\s*:\s*[^;]*;/, '');
					} else {
						// 引用原样式
						p.style = c.style;
					};
					if (p.value == undefined || p.value === "")
						p.value = "&#160;";
					if (r.dirty && typeof r.modified[c.name] !== 'undefined') {
						p.css += ' x-grid3-dirty-cell';
					}
					cb[cb.length] = ct.apply(p);
				}
				var alt = [];
				if (stripe && ((rowIndex + 1) % 2 == 0)) {
					alt[0] = "x-grid3-row-alt";
				}
				if (r.dirty) {
					alt[1] = " x-grid3-dirty-row";
				}
				rp.cols = colCount;
				if (this.getRowClass) {
					alt[2] = this.getRowClass(r, rowIndex, rp, ds);
				}
				rp.alt = alt.join(" ");
				rp.cells = cb.join("");
				buf[buf.length] = rt.apply(rp);
			}
			return buf.join("");
		}
	});
	Ext.QuickTips.init();
	var materialNameFlag = "";
	var totalCount = 0;
	var deleteMaterialIds = [];
	var deleteMaterialIdsD = [];
	var allMaterialIds = [];
	var allMaterialIdsD = [];
	var register = parent.Ext.getCmp('tabPanel').register;
	var oldFormData = new Object();
	oldFormData.dueDate = "";
	oldFormData.planOriginalId = "";
	oldFormData.itemId = "";
	oldFormData.costDept = "";
	oldFormData.specialityId = "";
	oldFormData.memo = "";
	oldFormData.prjNo="";
	var headIdMain = "";
	var actionUrl = "resource/addPlanRegister.action";

	// 用于保存明细初值
	var detailRecords = [];
	/**
	 * 金钱格式化
	 */
	function moneyFormat(v) {
		if (v == null || v == "") {
			return "0.0000";
		}
		v = (Math.round((v - 0) * 10000)) / 10000;
		v = (v == Math.floor(v))
				? v + ".0000"
				: ((v * 10 == Math.floor(v * 10))
						? v + "000"
						: ((v * 100 == Math.floor(v * 100)) ? v + "00" : ((v
								* 1000 == Math.floor(v * 1000)) ? v + "0" : v)));
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.0000';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}

	function choseSpare() {
		if (materialGrid.selModel.hasSelection()) {
			var record = materialGrid.getSelectionModel().getSelected()
			var mate = window.showModalDialog(
					'../../../../comm/jsp/equselect/selectAttribute.jsp',
					window, 'dialogWidth=800px;dialogHeight=550px;status=no');
			if (typeof(mate) != "undefined") {
				record.set('equCode', mate.code);
			}
		}
	}

	/**
	 * 数字格式化
	 */
	function numberFormat(v) {
		if (v == null || v == "") {
			return "0.00";
		}
		v = (Math.round((v - 0) * 100)) / 100;
		v = (v == Math.floor(v)) ? v + ".00" : ((v * 10 == Math.floor(v * 10))
				? v + "0"
				: v);
		v = String(v);
		var ps = v.split('.');
		var whole = ps[0];
		var sub = ps[1] ? '.' + ps[1] : '.00';
		var r = /(\d+)(\d{3})/;
		while (r.test(whole)) {
			whole = whole.replace(r, '$1' + ',' + '$2');
		}
		v = whole + sub;
		if (v.charAt(0) == '-') {
			return '-' + v.substr(1);
		}
		return v;
	}
	/**
	 * 获取当前时间
	 */
	function getCurrentDate() {
		var d, s, t;
		d = new Date();
		s = d.getFullYear().toString(10) + "-";
		t = d.getMonth() + 1;
		s += (t > 9 ? "" : "0") + t + "-";
		t = d.getDate();
		s += (t > 9 ? "" : "0") + t;
		return s;
	}
	/**
	 * 去掉时间中T
	 */
	function renderDate(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate : strDate;
	}
	/**
	 * 去掉时间中T
	 */
	function formatTime(value) {
		if (!value)
			return "";

		var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
		var reTime = /\d{2}:\d{2}:\d{2}/gi;

		var strDate = value.match(reDate);
		var strTime = value.match(reTime);
		if (!strDate)
			return "";
		return strTime ? strDate + " " + strTime : strDate;
	}

	/**
	 * 设置归口专业与计划来源联动 modify by fyyang 090515 增加计划说明与计划来源联动
	 */
	function setTxtMrCostSpecial(node) {
		// add by fyyang 090515 计划说明与计划来源联动
		var nowDate = getCurrentDate();
		var strDate = getPlanDateInfoByDate(node.id, nowDate);
		var myDay = parseInt(nowDate.substring(8, 10));
		var myMonth = parseInt(nowDate.substring(5, 7));
		if (myMonth == 0)
			myMonth = parseInt(nowDate.substring(6, 7));
		if (myDay == 0)
			myMonth = parseInt(nowDate.substring(9, 10));
		if (node.id == 4 || node.id == 5) {
			if (myDay > 25) {
				// modify by ywliu 20100127
				// Ext.Msg.confirm("提示", "您所申报计划的日期已经超过本月的25日，您所报的计划月份将为"
				// + strDate + "份计划，确定要申报吗？若需紧急采购，请申请紧急采购计划", function(
				// buttonobj) {
				// if (buttonobj == "yes") {

				setTxtMrCostSpecialAndPlanDate(node, strDate);
				// }
				// });
			} else {
				setTxtMrCostSpecialAndPlanDate(node, strDate);
			}

		} else if (node.id == 6) {
			if (myMonth == 3 || myMonth == 6 || myMonth == 9 || myMonth == 12) {
				if (myDay > 25) {
					// modify by ywliu 20100127
					// Ext.Msg.confirm("提示",
					// "您所申报计划的日期已经超过本季度最后一月的25日，您所报的计划季度将为"
					// + strDate + "计划，确定要申报吗？若需紧急采购，请申请紧急采购计划", function(
					// buttonobj) {
					// if (buttonobj == "yes") {

					setTxtMrCostSpecialAndPlanDate(node, strDate);
					// }
					// });
				} else {
					setTxtMrCostSpecialAndPlanDate(node, strDate);
				}
			} else {
				setTxtMrCostSpecialAndPlanDate(node, strDate);
			}
		} else {

			setTxtMrCostSpecialAndPlanDate(node, strDate);
		}

	}

	function setTxtMrCostSpecialAndPlanDate(node, strDate) {

		txtPlanDateMemo.setValue(strDate);
		// ------归口专业
		Ext.Ajax.request({
					url : 'resource/getOriginalType.action',
					method : 'post',
					params : {
						nodeId : node.id
					},
					success : function(action) {
						var radioData = action.responseText;
						txtMrOriginalH.setValue(radioData);
						// if (radioData == 'M') {
						// txtMrCostSpecial.allowBlank = false;
						// txtMrCostSpecial.enable();
						// } else if (radioData == 'O') {
						// txtMrCostSpecial.setValue("");
						// txtMrCostSpecial.clearInvalid();
						// txtMrCostSpecial.disable();
						// }
					},
					failure : function(action) {

					}
				});

		// ------------
	}

	/**
	 * 验证上部表单是否被更改
	 */
	function isHeadChanged() {
	
		var oldPrjNo=oldFormData.prjNo==null?'':oldFormData.prjNo;
		var newPrjNo=projectCode.getValue()==null?'':projectCode.getValue();
	 
		if (oldFormData != null) {
			if (oldFormData.dueDate == txtMrDueDate.getValue()
					&& oldFormData.planOriginalId == txtMrOriginal.getValue()
					&& oldFormData.itemId == txtMrItem.getValue()// modify by
					// ywliu
					// ,将下拉框改为页面
					// 2009/7/6
					&& oldFormData.costDept == txtMrCostDept.getValue()
					&& oldFormData.specialityId == txtMrCostSpecial.getValue()
					&& oldFormData.memo == txtMrMemo.getValue()
					&& oldPrjNo == newPrjNo) //add by fyyang 20100507
						{
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	/**
	 * 判断物料信息有没有改变
	 */
	function isDetailChanged() {
		// 新物料记录
		var newRecs = getMaterialList(true);
		// 如果有新增记录返回true
		if (newRecs.length > 0) {
			return true;
		}
		// 原db记录
		var oldRecs = getMaterialList();
		// 长度不同,有被删除的记录，返回true
		if (oldRecs.length != totalCount) {
			return true;
		}
		// 判断原有记录是否被修改

		for (var i = 0; i < materialStore.getCount() - 1; i++) {
			var r = materialStore.getAt(i);
			if (r.dirty) {
				for (var a in r.getChanges()) {

					if (a != "estimatedSum" && a != "planOriginalId"
							&& a != "planOriginalIdHId"
							&& a != "planOriginalIdHName") {

						return true;
					}
				}
			}
		}

		return false;
	}
	/**
	 * 验证并清空表单和grid
	 */
	function clearForm() {
		// ---add by fyyang 20100409
		mnoLabel.setText("");
		mnameLabel.setText("");
		materSizeLabel.setText("");

		// ------------------------
		entryId = "";
		Ext.get("txtMrOriginal").dom.disabled = false;
		actionUrl = "resource/addPlanRegister.action";
		// 统计行
		var record = new material({
					requirementDetailId : "",
					materialId : "",
					materialName : "",
					equCode : "",
					materSize : "",
					parameter : "",
					stockUmName : "",
					appliedQty : "",
					estimatedPrice : "",
					left : "",
					maxStock : "",
					usage : "",
					factory : "",
					needDate : "",
					supplier : "",
					memo : "",
					itemId : "",
					planOriginalId : "",
					planOriginalIdHId : "",
					planOriginalIdHName : "",
					lastModifiedDate : "",
					isNewRecord : 'total'
				});
		// modify by liuyi 091102
		if (isDeleted) {
			formPanel.getForm().reset();
			setUserDeptInfo();
			oldFormData.dueDate = "";
			oldFormData.planOriginalId = "";
			oldFormData.itemId = "";
			oldFormData.costDept = "";
			oldFormData.specialityId = "";
			oldFormData.memo = "";
			oldFormData.prjNo = "";
			materialStore.removeAll();
			materialStore.insert(0, record);
			materialGrid.getView().refresh();
			totalCount = materialStore.getCount() - 1;
			btnDelete.disable();
			isHeadChanged();
			isDetailChanged();
			register.headId = "";
			// modify by ywliu 09/07/16
			btnSave.setDisabled(false);
			btnReport.setDisabled(false);
			isDeleted = false;
			return;
		}
		if (isHeadChanged() || isDetailChanged()) {

			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_004,
					function(buttonobj) {
						if (buttonobj == "yes") {
							formPanel.getForm().reset();
							setUserDeptInfo();
							oldFormData.dueDate = "";
							oldFormData.planOriginalId = "";
							oldFormData.itemId = "";
							oldFormData.costDept = "";
							oldFormData.specialityId = "";
							oldFormData.memo = "";
							oldFormData.prjNo = "";
							materialStore.removeAll();
							materialStore.insert(0, record);
							materialGrid.getView().refresh();
							totalCount = materialStore.getCount() - 1;
							btnDelete.disable();
							isHeadChanged();
							isDetailChanged();
							register.headId = "";
							// modify by ywliu 09/07/16
							btnSave.setDisabled(false);
							btnReport.setDisabled(false);
						}
					});
		} else {
			deleteMaterialIds = [];
			deleteMaterialIdsD = [];
			allMaterialIds = [];
			allMaterialIdsD = [];
			formPanel.getForm().reset();
			setUserDeptInfo();
			oldFormData.dueDate = "";
			oldFormData.planOriginalId = "";
			oldFormData.itemId = "";
			oldFormData.costDept = "";
			oldFormData.specialityId = "";
			oldFormData.memo = "";
			oldFormData.prjNo = "";
			materialStore.removeAll();
			// 插入统计行
			materialStore.insert(0, record);
			materialGrid.getView().refresh();
			totalCount = materialStore.getCount() - 1;
			btnDelete.disable();
			register.headId = "";
			// modify by ywliu 09/07/16
			btnSave.setDisabled(false);
			btnReport.setDisabled(false);
		}
	}
	/**
	 * 验证并清空表单和grid
	 */
	function first() {
		actionUrl = "resource/addPlanRegister.action";
		// 统计行
		var record = new material({
					requirementDetailId : "",
					materialId : "",
					materialName : "",
					equCode : "",
					materSize : "",
					parameter : "",
					stockUmName : "",
					appliedQty : "",
					estimatedPrice : "",
					left : "",
					maxStock : "",
					usage : "",
					factory : "",
					needDate : "",
					supplier : "",
					memo : "",
					itemId : "",
					planOriginalId : "",
					planOriginalIdHId : "",
					planOriginalIdHName : "",
					lastModifiedDate : "",
					isNewRecord : 'total'
				});
		deleteMaterialIds = [];
		deleteMaterialIdsD = [];
		allMaterialIds = [];
		allMaterialIdsD = [];
		materialNameFlag = "";
		formPanel.getForm().reset();
		setUserDeptInfo();
		oldFormData.dueDate = "";
		oldFormData.planOriginalId = "";
		oldFormData.itemId = "";
		oldFormData.costDept = "";
		oldFormData.specialityId = "";
		oldFormData.memo = "";
		oldFormData.prjNo = "";
		materialStore.removeAll();
		// 插入统计行
		materialStore.insert(0, record);
		materialGrid.getView().refresh();

	}
	/**
	 * 设置默认申请人申请部门和申请日期
	 */

	function setUserDeptInfo() {
		txtMrDate.setValue(getCurrentDate());
		Ext.Ajax.request({
					url : 'resource/getInfo.action',
					method : 'post',
					success : function(action) {
						var rs = eval("(" + action.responseText + ")");
						if (rs != "" && rs != null) {
							txtMrBy.setValue(rs.workerName);
							txtMrByH.setValue(rs.workerCode);
							txtMrDept.setValue(rs.deptName);
							txtMrDeptH.setValue(rs.deptCode);
							// add by fyyang 090618 归口部门默认为申请部门
							txtMrCostDept.setValue(rs.deptName);
							txtMrCostDeptH.setValue(rs.deptCode);
						}
					},
					failure : function(action) {
					}

				});

	}

	/**
	 * 增加明细
	 */
	function addMaterial() {
		var msg = "" + isRightForm();
		if (msg == "") {
			var mate = null;
			// modify by drdu 091104
			if (txtMrOriginal.getValue() == 12
					|| txtMrOriginal.getValue() == 13
					|| txtMrOriginal.getValue() == 14) {
				mate = window.showModalDialog('selectMaterial13.jsp', window,
						'dialogWidth=800px;dialogHeight=600px;status=no');
			} else {
				mate = window.showModalDialog('selectMater.jsp', window,
						'dialogWidth=800px;dialogHeight=600px;status=no');
			}
			if (typeof(mate) != "undefined") {
				var txtmsg = "";
				for (var i = 0; i < mate.length; i++) {
					// 新记录
					var recordMaterial = new material({
								requirementDetailId : "",
								materialId : "",
								materialName : "",
								equCode : "",
								materSize : "",
								parameter : "",
								stockUmName : "",
								appliedQty : "",
								estimatedPrice : "",
								left : "",
								maxStock : "",
								usage : "",
								factory : "",
								supplier : "",
								memo : "",
								planOriginalIdHId : "",
								planOriginalIdHName : "",
								needDate : txtMrDueDate.getValue(),
								itemId : txtMrItemH.getValue(),
								planOriginalId : txtMrOriginal.getValue(),
								lastModifiedDate : "",
								isNewRecord : true,
								materialCode : null,
								classNo : ""// add by ltong
							});

					// add by drdu 20100406------------------
					var flag = 0;

					for (var j = 0; j < materialStore.getCount() - 1; j++) {
						code = materialStore.getAt(j).get('materialCode');
						if (mate[i].data.materialNo == code) {
							var k = j + 1;
							txtmsg += mate[i].data.materialName + "("
									+ mate[i].data.materialNo + ")与第" + k
									+ "行物资重复！</br>"
							flag = 1;
						}
					}
					if (flag == 0) {
						recordMaterial.set('materialName',
								mate[i].data.materialName);
						recordMaterial.set('materialId',
								mate[i].data.materialId);
						recordMaterial.set('materSize', mate[i].data.specNo);
						recordMaterial.set('parameter', mate[i].data.parameter);
						recordMaterial.set('stockUmName',
								mate[i].data.stockUmName);
						recordMaterial.set('factory', mate[i].data.factory);
						recordMaterial.set('maxStock', mate[i].data.maxStock);
						// add by liuyi 091023 物料编码 materialCode
						recordMaterial.set('materialCode',
								mate[i].data.materialNo);
						// alert(recordMaterial.get('materialCode'))
						// 物资分类 add by ltong
						recordMaterial.set('classNo', mate[i].data.classNo)
						// alert(recordMaterial.getValue())
						Ext.Ajax.request({
							params : {
								materialId : mate[i].data.materialId
							},
							url : 'resource/getMaterialStock.action',
							method : 'post',
							success : function(result) {
								recordMaterial.set('left', result.responseText);
							},
							failure : function(action) {
							}
						});
						// 原数据个数
						var count = materialStore.getCount();
						// 停止原来编辑
						materialGrid.stopEditing();
						materialGrid.getStore().insert(count - 1,
								recordMaterial);
						materialGrid.getView().refresh();
					}
					if (txtmsg != "") {
						Ext.Msg.alert("提示", "下列物资存在重复：</br>" + txtmsg);
					}
				}
				// // 设置物料名
				// record.set('materialName', mate.materialName);
				// record.set('materialId', mate.materialId);
				// record.set('materSize', mate.specNo);
				// record.set('parameter', mate.parameter);
				// record.set('stockUmName', mate.stockUmId);
				// record.set('factory', mate.factory);
				// record.set('maxStock', mate.maxStock);
				// Ext.Ajax.request({
				// params : {
				// materialId : mate.materialId
				// },
				// url : 'resource/getMaterialStock.action',
				// method : 'post',
				// success : function(result) {
				// record.set('left', result.responseText);
				//
				// },
				// failure : function(action) {
				// }
				//
				// });

			}

			/*
			 * addRecord(currentIndex); resetLine(); sm.selectRow(currentIndex);
			 * grid.startEditing(currentIndex, 2);
			 */

			// // 原数据个数
			// var count = materialStore.getCount();
			// // 停止原来编辑
			// materialGrid.stopEditing();
			// // 插入新数据
			// materialStore.insert(count - 1, record);
			// materialGrid.getView().refresh();
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
		}
	}
	/**
	 * 删除明细
	 */
	function deleteMaterial() {

		if (materialGrid.selModel.hasSelection()) {

			var myrecord = materialGrid.getSelectionModel().getSelections();
			var record = [];
			if (myrecord.length > 0) {
				// if (materialStore.indexOf(record) < materialStore.getCount()
				// - 1) {
				// add by fyyang 090513 统计行不删除
				for (i = 0; i < myrecord.length; i++) {

					if (materialStore.indexOf(myrecord[i]) != materialStore
							.getCount()
							- 1)
						record.push(myrecord[i]);
				}

				// 如果选中一行则删除
				for (i = 0; i < record.length; i++) {
					// modify by fyyang 090513

					materialStore.remove(record[i]);
					materialGrid.getView().refresh();
					// 如果不是新增加的记录,保存删除的流水号
					if (!record[i].get('isNewRecord')) {
						deleteMaterialIds.push(record[i]
								.get('requirementDetailId'));
						deleteMaterialIdsD.push(formatTime(record[i]
								.get('lastModifiedDate')));
					}

				}

			}
		} else {
			// 否则弹出提示信息
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_001);
		}
	}
	/**
	 * 整体check
	 */
	function isRight() {
		var msg = "";
		var msgSub = "";
		msg += isRightForm();
		for (var index = 0; index < materialStore.getCount() - 1; index++) {
			var record = materialStore.getAt(index).data;
			msgSub = detailCheck(record, index);
			if (msgSub != "") {
				msg = msg + "明细部分项目：<br />" + msgSub
				break;
			}
		}

		if (msg.trim() != "") {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
			return false;
		} else {
			msg += isMaterialRepeat();
			if (msg.trim() != "") {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, msg);
				return false;
			}
			return true;
		}
	}
	/**
	 * formcheck
	 */
	function isRightForm() {
		var msg = "";

		if (txtMrDueDate.getValue() == "") {
			msg += String.format(Constants.COM_E_003, "需求日期") + "<br />";
		} else {
			var startDate = getCurrentDate();
			var res = compareDateStr(startDate, txtMrDueDate.getValue());
			if (res) {
				msg += String.format(Constants.COM_E_004, "需求日期") + "<br />";
			}
		}
		// U bjxu 091103 null 处理全部
		if (txtMrOriginal.getValue() == ""
				|| txtMrOriginal.getValue() == "null") {
			msg += String.format(Constants.COM_E_003, "计划种类") + "<br />";
		}
		if (txtMrItem.getValue() == "") {
			msg += String.format(Constants.COM_E_003, "费用来源") + "<br />";
		}
		if (txtMrCostDept.getValue() == "") {
			msg += String.format(Constants.COM_E_003, "归口部门") + "<br />";
		}
		if (txtMrMemo.getValue() == "") {
			msg += String.format(Constants.COM_E_002, "申请理由") + "<br />";
		}
		// if (txtMrOriginalH.getValue() == "M"
		// && txtMrCostSpecial.getValue() == "") {
		// msg += String.format(Constants.RP002_E_001) + "<br />";
		// }
		if (msg != "") {
			msg = "头部的项目：<br />" + msg + "<br />"
		}
		return msg;
	}
	/**
	 * 获取所有物料明细信息，db已存在的和新增加的数据分开保存
	 */
	function getMaterialList(isNew) {
		// 记录
		var records = new Array();
		var blnFlag = isNew;
		// 循环
		for (var index = 0; index < materialStore.getCount() - 1; index++) {
			var record = materialStore.getAt(index).data;
			if (isNew) {
				// 新记录
				if (record.isNewRecord) {
					records.push(cloneMaterialRecord(record));
				}
			} else {
				// db中原有记录
				if (!record.isNewRecord) {
					records.push(cloneMaterialRecord(record));
				}
			}
		}
		return records;
	}

	/**
	 * 明细单条check
	 */
	function detailCheck(record, index) {
		var msg = "";
		index = index;
		if (record['materialId'] < 0) {
			msg += String.format(Constants.COM_E_002, "物料名称") + "<br />";
		}
		if (record['appliedQty'] == "" || record['appliedQty'] == null) {
			msg += String.format(Constants.COM_E_002, "申请数量") + "<br />";
		} else if (record['appliedQty'] <= 0) {
			msg += String.format(Constants.COM_E_005, "申请数量", "0") + "<br />";
		}
		if (record['estimatedPrice'] == "" || record['estimatedPrice'] == null)// add
			// By
			// ywliu
			// 2009/5/14
			msg += String.format(Constants.COM_E_002, "估计单价") + "<br />";
		// add By ywliu 2009/5/14
		if (record['needDate'] == "" || record['needDate'] == null) {
			msg += String.format(Constants.COM_E_002, "需求日期") + "<br />";
		} else {
			var startDate = getCurrentDate();
			var res = compareDateStr(startDate, record['needDate']);
			if (res) {
				msg += String.format(Constants.COM_E_004, "需求日期") + "<br />";
			}
		}

		// if (record['itemId'] == "" || record['itemId'] == null)
		// msg += String.format(Constants.COM_E_002, "费用来源") + "<br />";add By
		// ywliu 2009/5/14 将明细费用来源注释了，该数据从主表中的费用来源字段去数据
		// if (record['planOriginalId'] == "" || record['planOriginalId'] ==
		// null)
		// msg += String.format(Constants.COM_E_002, "计划来源") + "<br />";
		return msg;
	}
	/**
	 * 检测物料明细是否有重复
	 */
	function isMaterialRepeat() {
		var msg = "";
		for (var i = 0; i < materialStore.getCount() - 1; i++) {
			for (var j = i + 1; j < materialStore.getCount() - 1; j++) {
				if (materialStore.getAt(i).get('materialId') == materialStore
						.getAt(j).get('materialId')
						&& materialStore.getAt(i).get('materialId') != ""
						&& materialStore.getAt(j).get('materialId') != "") {

					// msg = Constants.RP002_E_002 + "<br />";//update by sychen
					// 20100330
					msg = String.format(Constants.RP002_E_002, j + 1, i + 1)
							+ "<br />";
				}
			}
		}
		return msg;
	}
	/**
	 * 复制物料信息
	 */
	function cloneMaterialRecord(record) {
		var objClone = new Object();
		// 拷贝属性
		objClone['materialId'] = record['materialId'];
		objClone['equCode'] = record['equCode'];
		objClone['appliedQty'] = record['appliedQty'];
		objClone['estimatedPrice'] = record['estimatedPrice'];
		objClone['usage'] = record['usage'];
		objClone['needDate'] = record['needDate'];
		objClone['supplier'] = record['supplier'];
		objClone['memo'] = record['memo'];
		objClone['itemId'] = record['itemId'];
		objClone['planOriginalId'] = record['planOriginalId'];
		objClone['requirementDetailId'] = record['requirementDetailId'];
		objClone['lastModifiedDate'] = record['lastModifiedDate'];
		return objClone;
	}
	/**
	 * 保存数据
	 */
	function saveForm() {

		if (isHeadChanged() || isDetailChanged()) {
			if (materialStore.getCount() > 1) {
				if (txtMrOriginal.getValue() == 12
						|| txtMrOriginal.getValue() == 13
						|| txtMrOriginal.getValue() == 14) {

					for (var i = 0; i < materialStore.getCount() - 1; i++) {
						// update by ltong 物料分类过滤
						if (materialStore.getAt(i).get('classNo') != null
								&& materialStore.getAt(i).get('classNo')
										.substring(0, 6) != '5-E-05') {
							Ext.Msg.alert('提示', '物料明细类型有与计划种类不一致的物料！');
							return;
						}
					}
				} else {
					for (var i = 0; i < materialStore.getCount() - 1; i++) {
						// update by ltong 物料分类过滤
						if (materialStore.getAt(i).get('classNo') != null
								&& materialStore.getAt(i).get('classNo')
										.substring(0, 6) == '5-E-05') {
							Ext.Msg.alert('提示', '物料明细类型有与计划种类不一致的物料！');
							return;
						}
					}
				}
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_001,
						function(buttonobj) {
							if (buttonobj == "yes") {
								if (isRight() == true) {
									if (maxStockRight() == true) {
										Ext.Msg
												.confirm(
														Constants.SYS_REMIND_MSG,
														String
																.format(
																		Constants.COM_W_001,
																		materialNameFlag,
																		"申请"),
														function(buttonobj) {
															if (buttonobj == "yes") {
																formPanel
																		.getForm()
																		.submit(
																				{
																					method : 'POST',
																					url : actionUrl,
																					params : {
																						// 新加的记录
																						addMaterial : Ext.util.JSON
																								.encode(getMaterialList(true)),
																						// 修改记录
																						updateMaterial : Ext.util.JSON
																								.encode(getMaterialList()),
																						// 删除的物料明细id集
																						deleteMaterialIds : deleteMaterialIds
																								.join(','),
																						deleteMaterialIdsD : deleteMaterialIdsD
																								.join(',')
																					},
																					success : function(
																							form,
																							action) {
																						var result = eval("("
																								+ action.response.responseText
																								+ ")");
																						// 如果成功，弹出增加成功
																						if (result.data > 0) {
																							Ext.Msg
																									.alert(
																											Constants.SYS_REMIND_MSG,
																											Constants.COM_I_004);
																							first();

																							getInitData(result.data);
																							register
																									.findH();

																							return true;
																						} else if (result.data == -1) {
																							Ext.Msg
																									.alert(
																											Constants.SYS_REMIND_MSG,
																											Constants.COM_E_015);
																							first();
																							register
																									.findH();
																							return false;
																						} else if (result.data == -2) {
																							// 如果失败，弹出增加失败信息
																							Ext.Msg
																									.alert(
																											Constants.SYS_REMIND_MSG,
																											Constants.COM_E_014);
																							return false;
																						}
																					}
																				});

															}
														});

									} else {
										formPanel.getForm().submit({
											method : 'POST',
											url : actionUrl,
											params : {
												// 新加的记录
												addMaterial : Ext.util.JSON
														.encode(getMaterialList(true)),
												// 修改记录
												updateMaterial : Ext.util.JSON
														.encode(getMaterialList()),
												// 删除的物料明细id集
												deleteMaterialIds : deleteMaterialIds
														.join(','),
												deleteMaterialIdsD : deleteMaterialIdsD
														.join(',')
											},
											success : function(form, action) {
												var result = eval("("
														+ action.response.responseText
														+ ")");
												// 如果成功，弹出增加成功

												if (result.data > 0) {
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_I_004);
													first();
													getInitData(result.data);
													register.findH();
													return true;
												} else if (result.data == -1) {
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_E_015);
													first();
													register.findH();
													return false;
												} else {
													// 如果失败，弹出增加失败信息
													Ext.Msg
															.alert(
																	Constants.SYS_REMIND_MSG,
																	Constants.COM_E_014);
													return false;
												}
											}
										});
									}

								}
							}
						});
			} else {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018);
			}
		} else {
			Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_I_006);
		}

	}

	function maxStockRight() {
		for (var index = 0; index < materialStore.getCount() - 1; index++) {
			materialNameFlag = "";
			var record = materialStore.getAt(index).data;
			if (record['maxStock'] != null && record['maxStock'] != ""
					&& record['maxStock'] * 1 != 0) {
				if ((record['appliedQty'] * 1 + record['left'] * 1) > record['maxStock']) {
					materialNameFlag = record['materialName'];
					break;
				}
			}
		}
		if (materialNameFlag == "") {
			return false
		} else {
			return true;
		}
	}
	/**
	 * 需求时间与当前时间比较
	 */
	function checkTime() {
		var startDate = getCurrentDate();
		var endDate = this.value;
		if (startDate != "" && endDate != "") {
			var res = compareDateStr(startDate, endDate);
			if (res) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, String.format(
								Constants.COM_E_004, "需求日期"));
				// this.focus();
				this.value = "";
			} else {
				this.value = endDate;
			}
		}

	}
	/**
	 * 明细部分日期check
	 */
	function checkTime2() {
		var startDate = getCurrentDate();
		var endDate = this.value;

		materialGrid.getSelectionModel().getSelected().set("needDate", endDate);

	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDate(argDate1, argDate2) {
		return argDate1.getTime() > argDate2.getTime();
	}
	/**
	 * textField显示时间比较方法
	 */
	function compareDateStr(argDateStr1, argDateStr2) {
		var date1 = Date.parseDate(argDateStr1, 'Y-m-d');
		var date2 = Date.parseDate(argDateStr2, 'Y-m-d');
		return compareDate(date1, date2);
	}
	/**
	 * 点击设备名称按钮触发的方法
	 */
	function creatButton(value, cellmeta, record, rowIndex, columnIndex, store) {
		if (rowIndex < store.getCount() - 1) {
			var str = "<input type='button'  class='btn_css' value='···' width='40' />";
			return str;
		} else {
			return "";
		}

	}
	/**
	 * 判断是新增还是修改初始化信息
	 */
	function initInfo(headId) {
		if (headId != "" && headId != null) {
			Ext.Ajax.request({
				params : {
					headId : headId
				},
				url : 'resource/getHeadInfo.action',
				method : 'post',
				success : function(result, request) {
					var record = eval('(' + result.responseText + ')');
					// alert(result.responseText)
					formPanel.getForm().loadRecord(record);
					
					//----add by fyyang 20100507------
					//add by fyyang 090603 --------------
					oldFormData.prjNo=record.data["prjNo"];
					projectCode.setValue(record.data["prjNo"]);
					
	
	                //取工程项目编号
		           if (record.data["prjNo"] != "") {
						
						Ext.get("txtProjectCodeShow").dom.value=getPrjShowNo(record.data["prjNo"])
					}
					//---------------------------------
					if (record.data['mrStatus'] == '9') {
						Ext.get("txtMrOriginal").dom.disabled = true;
					} else {
						Ext.get("txtMrOriginal").dom.disabled = false;
					}
					// add by fyyang 090515 增加字段计划说明
					txtPlanDateMemo.setValue(record.data['planDateMemo']);
					txtMrDate.setValue(renderDate(record.data['mrDate']));
					if (record.data['wfNo'] != null) {
						entryId = record.data['wfNo'];
					} else {
						entryId = "";
					}
					txtMrDueDate.setValue(renderDate(record.data['dueDate']));
					oldFormData.dueDate = txtMrDueDate.getValue();
					oldFormData.memo = txtMrMemo.getValue();
					if (record.data['costSpecial'] != "") {
						for (i = 0; i < storeCbx.getCount(); i++) {
							if (storeCbx.getAt(i).get('specialityCode') == record.data['costSpecial']) {
								txtMrCostSpecial.setValue(storeCbx.getAt(i)
										.get('specialityCode'));
								oldFormData.specialityId = txtMrCostSpecial
										.getValue();
								break;
							}
						}
					}
					txtMrCostDept.setValue(record.data['costDept']);
					oldFormData.costDept = txtMrCostDept.getValue();
					if (record.data['planOriginalId'] != "") {
						Ext.Ajax.request({
							params : {
								planOrigianlId : record.data['planOriginalId']
							},
							url : 'resource/getOrigianlName.action',
							method : 'post',
							success : function(result) {
								var recordSub = eval('(' + result.responseText
										+ ')');
								var obj = new Object();
								if (recordSub.data != null
										&& recordSub.data != "") {
									obj.text = recordSub.data['planOriginalDesc'];
									obj.id = parseInt(record.data['planOriginalId']);
									txtMrOriginal.setValue(obj);
									oldFormData.planOriginalId = txtMrOriginal
											.getValue();
									
								}
							},
							failure : function(action) {
							}

						});
					}

					// modify by fyyang 20100114
					txtMrItem.setValue(getBudgetName(record.data['itemCode']));

					txtAdviceItem.setValue(getBudgetName(
							record.data['itemCode'], 1, txtMrDeptH.getValue()));
					// txtFactItem.setValue(getBudgetName(record.data['itemCode'],2,txtMrDeptH.getValue()));
					// --------add by fyyang 20100419---------------
					var data = getBudgetName(record.data['itemCode'], 3,
							txtMrDeptH.getValue());
					var mydata = data.split(",");
					txtFactItem.setValue(mydata[0]);
					txtCheckPrice.setValue(mydata[1]);
					var data1 = (parseFloat(mydata[1]) / parseFloat(txtAdviceItem
							.getValue()))
							* 100;
					var data2 = (parseFloat(mydata[0]) / parseFloat(txtAdviceItem
							.getValue()))
							* 100;
					txtCheckPriceRate.setValue(parseFloat(txtAdviceItem
							.getValue()) == 0 ? 0 : numberFormat(data1));
					txtFactItemRate.setValue(parseFloat(txtAdviceItem
							.getValue()) == 0 ? 0 : numberFormat(data2));
					// ----------------------------------------------------
					oldFormData.itemId = txtMrItem.getValue();
					txtMrItemH.setValue(record.data['itemCode']);

					Ext.Ajax.request({
								url : 'resource/getInfo.action',
								method : 'post',
								success : function(action) {
									var rs = eval("(" + action.responseText
											+ ")");
									if (rs != "" && rs != null) {
										if (record.data['mrBy'] != rs.workerName
												&& rs.workerName != "测试员") {
											btnDelete.setDisabled(true);
											btnSave.setDisabled(true);
											btnReport.setDisabled(true);
										} else {
											btnDelete.setDisabled(false);
											btnSave.setDisabled(false);
											btnReport.setDisabled(false);
										}
									}
								},
								failure : function(action) {
								}

							});

				},
				failure : function(action) {
				}
			});
			materialStore.load({
						params : {
							headId : headId
						},
						callback : addLine
					});
			actionUrl = "resource/updatePlanRegister.action";
			btnDelete.enable();
		}
	}
	/**
	 * 初始化页面数据
	 */
	function getInitData(headId) {
		// ---add by fyyang 20100409
		mnoLabel.setText("");
		mnameLabel.setText("");
		materSizeLabel.setText("");

		// ------------------------
		if (headId != "" && headId != null) {
			headIdMain = headId;
			initInfo(headId);
			register.headId = "";
		} else {
			headIdMain = "";
			first();
			entryId = "";
		}

	}
	/**
	 * 双击修改明备注
	 */
	function editMemo(grid, rowIndex, columnIndex, e) {
		var record = grid.getStore().getAt(rowIndex);
		var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
		if (fieldName == "memo" && rowIndex != grid.getStore().getCount() - 1) {
			win.show();
			memoText.setValue(record.get("memo"));
			memoText.focus(false);
		}
	}
	/**
	 * 当且仅当点击物料单元格时弹出物料选择窗口
	 */
	function choseEdit(grid, rowIndex, columnIndex, e) {
		if (rowIndex < grid.getStore().getCount() - 1) {
			// var currentRecord = grid.getSelectionModel().getSelected();
			// var count = store.getCount();
			// var currentIndex =
			// currentRecord?currentRecord.get("line")-1:count
			// addRecord(currentIndex);
			// resetLine();
			// sm.selectRow(currentIndex);
			// grid.startEditing(currentIndex, 2);

			var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			// if (fieldName == "materialName") {
			// var mate = window.showModalDialog('selectMaterials.jsp', window,
			// 'dialogWidth=800px;dialogHeight=550px;status=no');
			// if (typeof(mate) != "undefined") {
			// alert(mate.length)
			// for (var i = 0; i < mate.length; i += 1) {
			// // var record = grid.getStore().getAt(rowIndex);
			// var record=new material();
			// record.set('materialName', mate[i].data.materialName);
			// record.set('materialId', mate[i].data.materialId);
			// record.set('materSize', mate[i].data.specNo);
			// record.set('parameter', mate[i].data.parameter);
			// record.set('stockUmName', mate[i].data.stockUmName);
			// record.set('factory', mate[i].data.factory);
			// record.set('maxStock', mate[i].data.maxStock);
			// Ext.Ajax.request({
			// params : {
			// materialId : mate[i].data.materialId
			// },
			// url : 'resource/getMaterialStock.action',
			// method : 'post',
			// success : function(result) {
			// record.set('left', result.responseText);
			//	
			// },
			// failure : function(action) {
			// }
			//							
			// });
			// grid.getStore().insert(rowIndex);
			// }
			//					
			// // // 设置物料名
			// // record.set('materialName', mate.materialName);
			// // record.set('materialId', mate.materialId);
			// // record.set('materSize', mate.specNo);
			// // record.set('parameter', mate.parameter);
			// // record.set('stockUmName', mate.stockUmId);
			// // record.set('factory', mate.factory);
			// // record.set('maxStock', mate.maxStock);
			// // Ext.Ajax.request({
			// // params : {
			// // materialId : mate.materialId
			// // },
			// // url : 'resource/getMaterialStock.action',
			// // method : 'post',
			// // success : function(result) {
			// // record.set('left', result.responseText);
			// //
			// // },
			// // failure : function(action) {
			// // }
			// //
			// // });
			//
			// }
			// }
			if (fieldName == "clickToChose") {
				choseSpare();
			}
		}
	}

	/**
	 * 选择设备编码
	 */

	/**
	 * 判断是否是最后行，如果是则不能编辑
	 */
	function checkIsEdit(obj) {

		if (obj.row == materialStore.getCount() - 1) {

			obj.cancel = true;
		}
	}
	/**
	 * 删除计划
	 */
	function delPlan() {
		if (hdnMrId.getValue() != "") {
			Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_002,
					function(buttonobj) {
						if (buttonobj == "yes") {
							Ext.Ajax.request({
								url : 'resource/deletePlanRegister.action',
								method : 'post',
								params : {
									headId : hdnMrId.getValue(),
									lastModifiedDate : lastModifiedDate
											.getValue(),
									detailId : allMaterialIds.join(","),
									detailIdD : allMaterialIdsD.join(",")

								},
								success : function(result) {
									var re = eval("(" + result.responseText
											+ ")");

									if (re.data == 0) {
										Ext.Msg.show({
													title : Constants.SYS_REMIND_MSG,
													msg : Constants.COM_I_005,
													buttons : Ext.MessageBox.OK,
													fn : function() {
														isDeleted = true;
														clearForm();
														register.find();
													}
												});

									} else if (re.data == -1) {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_015);
										clearForm();
										register.find();
									} else if (re.data == -2) {
										Ext.Msg.alert(Constants.SYS_REMIND_MSG,
												Constants.COM_E_014);
									}

								}
							});
						}
					});
		}
	}

	// add by drdu 091104
	function reportBtnHandler() {
		// 如果画面数据未保存
	
		if (isHeadChanged() || isDetailChanged()) {
			// 是否确认保存
			Ext.Msg.alert("提示", "数据未保存，请先保存！");
			return;
		} else {
			if (!txtMrNo.getValue()) {
				Ext.Msg.alert(Constants.SYS_REMIND_MSG, Constants.COM_E_018)
				return;
			}
			// 上报操作
			reportPlan();
		}
	}

	// 判断月份属于那个季度
	function jdCheck(month) {
		if (month == 1 || month == 2 || month == 3) {
			return 1;
		}
		if (month == 4 || month == 5 || month == 6) {
			return 2;
		}
		if (month == 7 || month == 8 || month == 9) {
			return 3;
		}
		if (month == 10 || month == 11 || month == 12) {
			return 4;
		}
	}
	// 取月份
	function getMonth(tempJ) {
		switch (tempJ) {
			case 1 :
				return 3;
			case 2 :
				return 6
			case 3 :
				return 9;
			case 4 :
				return 12;
		}
	}

	function checkDate() {
		var msg;
		// 当前日期
		var date = new Date();
		// 当前日
		var tempD = date.getDate();
		// 当前月
		var tempM = date.getMonth() + 1;
		// 当前月前一月
		// var tempM1 = date.getMonth();
		// 需求月
		// var mrMonth = txtMrDate.getValue().substring(5, 7);
		// 需求日
		// var mrDay = txtMrDate.getValue().substring(8, 10);

		var flg = true;
		// 生产物资月度计划、计算机材料月度计划
		if (txtMrOriginal.getValue() == '13' || txtMrOriginal.getValue() == '4') {
			if (tempD >= 25) {
				flg = true;
			} else {
				flg = false;
			}
		}

		// 当前季度
		var tempJ = jdCheck(tempM);
		// 行政办公用品季度计划
		if (txtMrOriginal.getValue() == '6') {
			if (tempM == getMonth(tempJ) && tempD >= 25) {
				flg = true;
			} else {
				flg = false;
			}
		}
		return flg;
	}

	/**
	 * 上报计划单
	 */
	function reportPlan() {
		if (!checkDate()) {
			Ext.Msg.alert("提示", "当前时间不能申报!");
			return false;
		}

		if (hdnMrId.getValue() != "") {
			// isDetailChanged()
			if (isHeadChanged() || true) {
				// Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
				// Constants.COM_W_002,
				// function(buttonobj) {});
				// buttonobj == "yes"
				if (true) {
					if (materialStore.getCount() > 1) {
						if (isRight() == true) {
							if (maxStockRight() == true) {
								Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
										String.format(Constants.COM_W_001,
												materialNameFlag, "申请"),
										function(buttonobj) {
											if (buttonobj == "yes") {
												formPanel.getForm().submit({
													method : 'POST',
													url : actionUrl,
													params : {
														addMaterial : Ext.util.JSON
																.encode(getMaterialList(true)),
														updateMaterial : Ext.util.JSON
																.encode(getMaterialList()),
														deleteMaterialIds : deleteMaterialIds
																.join(','),
														deleteMaterialIdsD : deleteMaterialIdsD
																.join(',')
													},
													success : function(form,
															action) {
														var result = eval("("
																+ action.response.responseText
																+ ")");
														if (result.data > 0) {
															first();
															getInitData(result.data);
															register.findH();
															Ext.Msg
																	.confirm(
																			Constants.SYS_REMIND_MSG,
																			Constants.COM_C_006,
																			function(
																					buttonobj) {
																				if (buttonobj == "yes") {
																					openReportWin();
																					// Ext.Ajax.request({
																					// url
																					// :
																					// 'resource/reportPlan.action',
																					// method
																					// :
																					// 'post',
																					// params
																					// : {
																					// headId
																					// :
																					// hdnMrId.getValue(),
																					// lastModifiedDate
																					// :
																					// lastModifiedDate.getValue(),
																					// detailId
																					// :
																					// allMaterialIds.join(","),
																					// detailIdD
																					// :
																					// allMaterialIdsD.join(",")
																					// },
																					// success
																					// :
																					// function(response)
																					// {
																					// var
																					// result
																					// =
																					// eval("("+
																					// response.responseText+
																					// ")");
																					// if
																					// (result.data
																					// ==
																					// -1)
																					// {
																					// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
																					// Constants.COM_E_015);
																					// first();
																					// register.findH();
																					// return
																					// false;
																					// }
																					// else
																					// if
																					// (result.data
																					// ==
																					// -2)
																					// {
																					// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
																					// Constants.COM_E_014);
																					// first();
																					// register.findH();
																					// return
																					// false;
																					// }
																					// else
																					// {
																					// Ext.Msg.show({
																					// title
																					// :
																					// Constants.SYS_REMIND_MSG,
																					// msg
																					// :
																					// Constants.COM_I_007,
																					// buttons
																					// :
																					// Ext.MessageBox.OK,
																					// fn :
																					// function()
																					// {
																					// clearForm();
																					// register.find();
																					// }
																					// });
																					// }
																					// }
																					// });
																				}
																			});

														} else if (result.data == -1) {
															Ext.Msg
																	.alert(
																			Constants.SYS_REMIND_MSG,
																			Constants.COM_E_015);
															first();
															register.findH();
														} else {
															Ext.Msg
																	.alert(
																			Constants.SYS_REMIND_MSG,
																			Constants.COM_E_014);
														}
													}

												});
											}
										});
							} else {
								formPanel.getForm().submit({
									method : 'POST',
									url : actionUrl,
									params : {
										addMaterial : Ext.util.JSON
												.encode(getMaterialList(true)),
										updateMaterial : Ext.util.JSON
												.encode(getMaterialList()),
										deleteMaterialIds : deleteMaterialIds
												.join(','),
										deleteMaterialIdsD : deleteMaterialIdsD
												.join(',')
									},
									success : function(form, action) {
										var result = eval("("
												+ action.response.responseText
												+ ")");
										if (result.data > 0) {
											first();
											getInitData(result.data);
											register.findH();
											Ext.Msg.confirm(
													Constants.SYS_REMIND_MSG,
													Constants.COM_C_006,
													function(buttonobj) {
														if (buttonobj == "yes") {
															openReportWin();
															// Ext.Ajax.request({
															// url
															// :
															// 'resource/reportPlan.action',
															// method
															// :
															// 'post',
															// params
															// : {
															// headId
															// :
															// hdnMrId.getValue(),
															// lastModifiedDate
															// :
															// lastModifiedDate.getValue(),
															// detailId
															// :
															// allMaterialIds.join(","),
															// detailIdD
															// :
															// allMaterialIdsD.join(",")
															// },
															// success
															// :
															// function(response)
															// {
															// var
															// result
															// =
															// eval("("+
															// response.responseText+
															// ")");
															// if
															// (result.data
															// ==
															// -1)
															// {
															// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
															// Constants.COM_E_015);
															// first();
															// register.findH();
															// return
															// false;
															// }
															// else
															// if
															// (result.data
															// ==
															// -2)
															// {
															// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
															// Constants.COM_E_014);
															// first();
															// register.findH();
															// return
															// false;
															// }
															// else
															// {
															// Ext.Msg.show({
															// title
															// :
															// Constants.SYS_REMIND_MSG,
															// msg
															// :
															// Constants.COM_I_007,
															// buttons
															// :
															// Ext.MessageBox.OK,
															// fn :
															// function()
															// {
															// clearForm();
															// register.find();
															// }
															// });
															// }
															// }
															// });
														}
													});

										} else if (result.data == -1) {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_E_015);
											first();
											register.findH();
										} else {
											Ext.Msg.alert(
													Constants.SYS_REMIND_MSG,
													Constants.COM_E_014);
										}
									}

								});
							}
						}
					} else {
						Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								Constants.COM_E_018);
					}

				} else {
					Ext.Msg.confirm(Constants.SYS_REMIND_MSG,
							Constants.COM_C_006, function(buttonobj) {
								if (buttonobj == "yes") {
									openReportWin();
									// Ext.Ajax.request({
									// url :
									// 'resource/reportPlan.action',
									// method : 'post',
									// params : {
									// headId : hdnMrId.getValue(),
									// lastModifiedDate :
									// lastModifiedDate.getValue(),
									// detailId :
									// allMaterialIds.join(","),
									// detailIdD :
									// allMaterialIdsD.join(",")
									// },
									// success : function(response)
									// {
									// var result = eval("("+
									// response.responseText+ ")");
									// if (result.data == -1) {
									// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									// Constants.COM_E_015);
									// first();
									// register.findH();
									// return false;
									// } else if (result.data == -2)
									// {
									// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
									// Constants.COM_E_014);
									// first();
									// register.findH();
									// return false;
									// } else {
									// Ext.Msg.show({
									// title :
									// Constants.SYS_REMIND_MSG,
									// msg : Constants.COM_I_007,
									// buttons : Ext.MessageBox.OK,
									// fn : function() {
									// clearForm();
									// register.find();
									// }
									// });
									// }
									// }
									// });
								}
							});
				}

			} else {
				Ext.Msg.confirm(Constants.SYS_REMIND_MSG, Constants.COM_C_006,
						function(buttonobj) {
							if (buttonobj == "yes") {
								openReportWin();
								// Ext.Ajax.request({
								// url : 'resource/reportPlan.action',
								// method : 'post',
								// params : {
								//
								// headId : hdnMrId.getValue(),
								// lastModifiedDate :
								// lastModifiedDate.getValue(),
								// detailId : allMaterialIds.join(","),
								// detailIdD : allMaterialIdsD.join(",")
								// },
								// success : function(response) {
								// var result = eval("("+ response.responseText+
								// ")");
								// if (result.data == -1) {
								// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								// Constants.COM_E_015);
								// first();
								// register.findH();
								// return false;
								// } else if (result.data == -2) {
								// Ext.Msg.alert(Constants.SYS_REMIND_MSG,
								// Constants.COM_E_014);
								// first();
								// register.findH();
								// return false;
								// } else {
								// Ext.Msg.show({
								// title : Constants.SYS_REMIND_MSG,
								// msg : Constants.COM_I_007,
								// buttons : Ext.MessageBox.OK,
								// fn : function() {
								// clearForm();
								// register.find();
								// }
								// });
								//                                                    
								// }
								// }
								// });
							}
						});
			}
		}

	}

	register.loadRecord = getInitData;
	// -------工具栏按钮-----开始
	// 增加按钮
	var btnAdd = new Ext.Toolbar.Button({
				text : "新增",
				iconCls : Constants.CLS_ADD,
				handler : clearForm
			});
	// 删除按钮
	var btnDelete = new Ext.Toolbar.Button({
				text : "删除",
				iconCls : Constants.CLS_DELETE,
				disabled : true,
				handler : delPlan
			});
	// 保存按钮
	var btnSave = new Ext.Toolbar.Button({
				text : "保存",
				iconCls : Constants.CLS_SAVE,
				handler : saveForm
			});
	// 保存按钮
	var btnReport = new Ext.Toolbar.Button({
				text : "上报",
				iconCls : Constants.CLS_REPOET,
				handler : reportBtnHandler
			});
	var toolBarTop = new Ext.Toolbar({
				region : 'north',
				items : [btnAdd, '-', btnDelete, '-', btnSave, '-', btnReport]
			});
	// --------工具栏按钮-----结束
	// --------第一行textbox--开始
	// 最后修改时间
	var lastModifiedDate = new Ext.form.Hidden({
				id : "lastModifiedDate",
				name : "mr.lastModifiedDate",
				renderer : formatTime
			})
	// 需求计划流水号
	var hdnMrId = new Ext.form.Hidden({
				id : 'requirementHeadId',
				name : "mr.requirementHeadId"
			});
	// 申请单编号
	var txtMrNo = new Ext.form.TextField({
				fieldLabel : '申请单编号',
				isFormField : true,
				readOnly : true,
				maxLength : 20,
				id : 'mrNo',
				name : "mr.mrNo",
				anchor : '100%'
			});
	// 申请日期
	var txtMrDate = new Ext.form.TextField({
				fieldLabel : '申请日期',
				readOnly : true,
				width : 100,
				renderer : renderDate,
				id : 'mrDate',
				name : "mr.mrDate",
				anchor : '100%'
			});
	// 申请人
	var txtMrBy = new Ext.form.TextField({
				fieldLabel : '申请人',
				readOnly : true,
				maxLength : 100,
				name : "mrBy",
				anchor : '100%'
			});
	var txtMrByH = new Ext.form.Hidden({
				name : "mr.mrBy"
			});
	// 申请部门
	var txtMrDept = new Ext.form.TextField({
				fieldLabel : '申请部门',
				readOnly : true,
				maxLength : 100,
				name : "mrDept",
				anchor : '100%'
			});
	var txtMrDeptH = new Ext.form.Hidden({
				name : "mr.mrDept"
			});

	var firstLine = new Ext.Panel({
				border : false,
				height : 30,
				layout : "column",
				style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
						+ "margin-bottom:0px;margin-left:-5px",
				anchor : '100%',
				items : [{
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrNo, hdnMrId, lastModifiedDate]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrDate]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrBy, txtMrByH]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrDept, txtMrDeptH]
						}]
			});
	// --------第一行textbox--结束
	// --------第二行textbox--开始
	// 需求日期
	var txtMrDueDate = new Ext.form.TextField({
				fieldLabel : "需求日期<font color ='red'>*</font>",
				readOnly : true,
				renderer : renderDate,
				id : "dueDate",
				name : "mr.dueDate",
				anchor : '100%',
				style : 'cursor:pointer',
				allowBlank : false,
				listeners : {
					focus : function() {
						WdatePicker({
									// 时间格式
									startDate : '%y-%M-%d',
									dateFmt : 'yyyy-MM-dd',
									alwaysUseStartDate : false,
									onpicked : function() {
										txtMrDueDate.clearInvalid();
									},
									onclearing : function() {
										txtMrDueDate.markInvalid();
									}
								});

					}

				}

			});

	// 计划来源
	var txtMrOriginal = new Ext.ux.ComboBoxTree({
				fieldLabel : '计划种类<font color ="red">*</font>',
				anchor : '100%',
				id : 'txtMrOriginal',
				displayField : 'text',
				valueField : 'id',
				allowBlank : false,
				hiddenName : 'mr.planOriginalId',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'resource/getPlanOriginalNode.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							}),
					listeners : {
						click : setTxtMrCostSpecial
					}
				},
				selectNodeModel : 'leaf'
			});
	var txtMrOriginalH = new Ext.form.Hidden();
	txtMrOriginal.on("beforequery", function(e) {
				if (entryId == "" || entryId == null) {
					return true;
				} else {
					return false;
				}
			});

	// var txtMrItemStore = new Ext.data.SimpleStore({
	// fields : ['id', 'name'],
	// data : [['1', '费用来源1'], ['2', '费用来源2'], ['3', '费用来源3'], ['4', '费用来源4']]
	// }); modify by ywliu ,将下拉框改为页面 2009/7/6

	// // 费用来源
	// var txtMrItem = new Ext.form.ComboBox({
	// fieldLabel : '费用来源<font color ="red">*</font>',
	// allowBlank : false,
	// maxLength : 100,
	// readOnly : true,
	// mode : 'local',
	// displayField : 'name',
	// valueField : 'id',
	// typeAhead : true,
	// forceSelection : true,
	// mode : 'local',
	// triggerAction : 'all',
	// selectOnFocus : true,
	// // store : txtMrItemStore,
	// id : "itemId",
	// value:'1',
	// hiddenName : "mr.itemId",
	// anchor : '100%'
	// }

	// });
	// modify by ywliu ,将费用来源下拉框改为页面 2009/7/6
	function choseItem() {

		if(Ext.get("mr.prjNo").dom.value!="")
		{
		  return;	
		}
		
		var item = window
				.showModalDialog(
						'../../../../comm/jsp/item/budget/budget.jsp',
						null,
						'dialogWidth:560px;dialogHeight:440px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(item) != "undefined") {
			txtMrItem.setValue(item.itemName);
			txtMrItemH.setValue(item.itemCode);

			txtAdviceItem.setValue(getBudgetName(item.itemCode, 1, txtMrDeptH
							.getValue()));
			// txtFactItem.setValue(getBudgetName(item.itemCode,2,txtMrDeptH.getValue()));
			// --------add by fyyang 20100419---------------
			var data = getBudgetName(item.itemCode, 3, txtMrDeptH.getValue());
			var mydata = data.split(",");
			txtFactItem.setValue(mydata[0]);
			txtCheckPrice.setValue(mydata[1]);
			var data1 = (parseFloat(mydata[1]) / parseFloat(txtAdviceItem
					.getValue()))
					* 100;
			var data2 = (parseFloat(mydata[0]) / parseFloat(txtAdviceItem
					.getValue()))
					* 100;
			txtCheckPriceRate
					.setValue(parseFloat(txtAdviceItem.getValue()) == 0
							? 0
							: numberFormat(data1));
			txtFactItemRate.setValue(parseFloat(txtAdviceItem.getValue()) == 0
					? 0
					: numberFormat(data2));
			// ----------------------------------------------------
		}
	}
	
	//----add by fyyang 20100507-----------
		// 项目编号
	var projectCode = new Ext.form.Hidden({
		id : "projectCode",
		name : 'mr.prjNo'
	});
	//add by fyyang 090602-------
	//项目编号显示
	var txtProjectCodeShow=new Ext.form.TextField({
	id : "txtProjectCodeShow",
		fieldLabel : '项目编号',
		readOnly : true,
		isFormField : true,
		anchor : '100%',
		name : 'txtProjectCodeShow'
	});
	
		txtProjectCodeShow.onClick(selectProjectCode);
	function selectProjectCode() {

		var args = {
			selectModel : 'single',
			rootNode : {
				id : "0",
				text : '合同类别'
			}
		}
		var url = "../../../../manage/project/query/ProjectForResourceSelect.jsp";
		var rvo = window
				.showModalDialog(
						url,
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(rvo) != "undefined") {
			Ext.get("mr.prjNo").dom.value = rvo.prjNo;
			Ext.get("txtProjectCodeShow").dom.value = rvo.prjNoShow;
			if (rvo.prjNo != "") {
				txtMrItem.setValue(getBudgetName(rvo.itemCode));
				txtMrItemH.setValue(rvo.itemCode);
				txtAdviceItem.setValue(getBudgetName(rvo.itemCode, 1,
						txtMrDeptH.getValue()));
				var data = getBudgetName(rvo.itemCode, 3, txtMrDeptH.getValue());
				var mydata = data.split(",");
				txtFactItem.setValue(mydata[0]);
				txtCheckPrice.setValue(mydata[1]);
				var data1 = (parseFloat(mydata[1]) / parseFloat(txtAdviceItem
						.getValue()))
						* 100;
				var data2 = (parseFloat(mydata[0]) / parseFloat(txtAdviceItem
						.getValue()))
						* 100;
				txtCheckPriceRate
						.setValue(parseFloat(txtAdviceItem.getValue()) == 0
								? 0
								: numberFormat(data1));
				txtFactItemRate
						.setValue(parseFloat(txtAdviceItem.getValue()) == 0
								? 0
								: numberFormat(data2));
			} else {
				txtMrItem.setValue("");
				txtMrItemH.setValue("");
				txtAdviceItem.setValue(null);
				txtFactItem.setValue(null);
				txtCheckPrice.setValue(null);
				txtCheckPriceRate.setValue(null);
				txtFactItemRate.setValue(null);
			}
			// ----------------------------------------------------
		}

	}
	
	
	
	// ------add end --------------------------------

	// 费用来源
	var txtMrItem = new Ext.form.TriggerField({
				fieldLabel : '费用来源<font color ="red">*</font>',
				displayField : 'text',
				valueField : 'id',
				blankText : '请选择',
				emptyText : '请选择',
				anchor : '100%',
				name : "itemName",
				// value : '生产类费用',
				allowBlank : false,
				readOnly : true,
				hidden : false,
				hideLabel : false
			});
	txtMrItem.onTriggerClick = choseItem;
	var txtMrItemH = new Ext.form.Hidden({
				name : 'mr.itemCode',
				value : ''
			})
	// modify by ywliu ,将费用来源下拉框改为页面 2009/7/6

	function choseDept() {
		var args = {
			selectModel : 'single',
			rootNode : {
				id : '0',
				text : '合肥电厂'
			}
		}
		var dept = window
				.showModalDialog(
						'../../../../comm/jsp/hr/dept/dept.jsp',
						args,
						'dialogWidth:550px;dialogHeight:300px;directories:yes;help:no;status:no;resizable:no;scrollbars:yes;');
		if (typeof(dept) != "undefined") {
			txtMrCostDept.setValue(dept.names);
			txtMrCostDeptH.setValue(dept.codes);
		}
	}
	// 归口部门
	var txtMrCostDept = new Ext.form.TriggerField({
				fieldLabel : '归口部门<font color ="red">*</font>',
				displayField : 'text',
				valueField : 'id',
				blankText : '请选择',
				emptyText : '请选择',
				anchor : '100%',
				name : "costDept",
				allowBlank : false,
				readOnly : true,
				hidden : true,
				hideLabel : true
			});
	txtMrCostDept.onTriggerClick = choseDept;
	var txtMrCostDeptH = new Ext.form.Hidden({
				name : 'mr.costDept'
			})

	// add by fyyang 090515 -------------加计划时间的说明----
	var txtPlanDateMemo = new Ext.form.TextField({
				name : 'mr.planDateMemo',
				fieldLabel : "月份/季度",
				readOnly : true,
				anchor : '100%'
			});

	// ---------------------------------------------------
	var secondLine = new Ext.Panel({
				border : false,
				height : 30,
				layout : "column",
				style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
						+ "margin-bottom:0px;margin-left:-5px",
				anchor : '100%',
				items : [{
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrDueDate]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrOriginal, txtMrOriginalH]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtProjectCodeShow, projectCode]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrItem, txtMrItemH]
						}]
			});
	// --------第二行textbox--结束
	// --------第三行textbox--开始
	// 归口专业
	var storeCbx = new Ext.data.JsonStore({
				root : 'list',
				url : "resource/getSpeciality.action",
				fields : ['specialityCode', 'specialityName']
			})
	// storeCbx.load();
	var txtMrCostSpecial = new Ext.form.ComboBox({
				fieldLabel : '归口专业',
				store : storeCbx,
				allowBlank : false,
				hiddenName : "mr.costSpecial",
				displayField : "specialityName",
				valueField : "specialityCode",
				mode : 'local',
				triggerAction : 'all',
				readOnly : true,
				disabled : true,
				anchor : '100%',
				hidden : true,
				hideLabel : true
			});
	// ------add by fyyang 20100212-------
	var txtAdviceItem = new Ext.form.TextField({
				name : 'txtAdviceItem',
				fieldLabel : "本年预算费用",
				readOnly : true,
				anchor : '100%'
			});

	var txtFactItem = new Ext.form.TextField({
				name : 'txtFactItem',
				fieldLabel : "本年已领费用",
				readOnly : true,
				anchor : '100%'
			});
	// ------add by fyyang 20100212-------

	// ----add by fyyang 20100419------------
	var txtCheckPrice = new Ext.form.TextField({
				name : 'txtCheckPrice',
				fieldLabel : "本年财务审核领料费用",
				readOnly : true,
				anchor : '100%'
			});
	var txtCheckPriceRate = new Ext.form.TextField({
				name : 'txtCheckPriceRate',
				fieldLabel : "本年财务审核领用费用完成率(%)",
				readOnly : true,
				anchor : '100%'
			});
	var txtFactItemRate = new Ext.form.TextField({
				name : 'txtFactItemRate',
				fieldLabel : "本年仓库发出费用完成率(%)",
				readOnly : true,
				anchor : '100%'
			});
	// ---------------------------------------------

	var thirdLine = new Ext.Panel({
				border : false,
				// hidden : true,
				height : 30,
				labelWidth : 140,
				layout : "column",
				style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
						+ "margin-bottom:0px;margin-left:-5px",
				anchor : '100%',
				items : [{
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtPlanDateMemo]
						},{
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtAdviceItem]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtFactItem]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtCheckPrice]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrCostSpecial]
						}]
			});
	var fourLine = new Ext.Panel({
				border : false,
				// hidden : true,
				height : 30,
				labelWidth : 190,
				layout : "column",
				style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
						+ "margin-bottom:0px;margin-left:-5px",
				anchor : '100%',
				items : [{
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrCostDept]
						}, { // ,,
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtMrCostDeptH]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtFactItemRate]
						}, {
							columnWidth : 0.25,
							layout : "form",
							border : false,
							items : [txtCheckPriceRate]
						}]
			});
	// --------第三行textbox--结束
	// --------第四行textbox--开始
	// 备注
	var txtMrMemo = new Ext.form.TextArea({
				fieldLabel : "申请理由<font color ='red'>*</font>",
				allowBlank : false,
				maxLength : 150,
				anchor : '99.6%',
				height : Constants.TEXTAREA_HEIGHT,
				id : "mrReason",
				name : "mr.mrReason"

			});
	var forthLine = new Ext.Panel({
				border : false,
				autoHeight : true,
				layout : "column",
				style : "padding-top:5px;padding-right:0px;padding-bottom:0px;"
						+ "margin-bottom:0px;margin-left:-5px",
				anchor : '100%',
				items : [{
							columnWidth : 1,
							layout : "form",
							border : false,
							items : [txtMrMemo]
						}]
			});
	// --------第四行textbox--结束
	// --------第五行grid--开始
	// 增加明细按钮
	var btnAddmaterial = new Ext.Button({
				text : "新增明细",
				iconCls : Constants.CLS_ADD,
				handler : addMaterial
			});
	// 删除明细按钮
	var btnDeletematerial = new Ext.Button({
				text : "删除明细",
				iconCls : Constants.CLS_DELETE,
				handler : deleteMaterial
			});
	// 物料记录
	var material = Ext.data.Record.create([{
				name : 'requirementDetailId'
			}, {
				name : 'materialId'
			}, {
				name : 'materialName'
			}, {
				name : 'equCode'
			}, {
				name : 'materSize'
			}, {
				name : 'parameter'
			}, {
				name : 'stockUmName'
			}, {
				name : 'appliedQty'
			}, {
				name : 'estimatedPrice'
			}, {
				name : 'left'
			}, {
				name : 'maxStock'
			}, {
				name : 'usage'
			}, {
				name : 'factory'
			}, {
				name : 'needDate'
			}, {
				name : 'supplier'
			}, {
				name : 'memo'
			}, {
				name : 'itemId'
			}, {
				name : 'planOriginalId'
			}, {
				name : 'planOriginalIdHId'
			}, {
				name : 'planOriginalIdHName'
			}, {
				name : 'lastModifiedDate'
			}
			// add by liuyi 0910223 物料编码
			, {
				name : 'materialCode'
			}, {
				name : 'classNo'
			}]);
	// 物料grid的store
	var materialStore = new Ext.data.JsonStore({
				url : 'resource/getMaterialEdit.action',
				root : 'list',
				totalProperty : 'totalCount',
				fields : material
			});

	/**
	 * 添加统计行
	 */
	function addLine() {
		// 统计行
		var record = new material({
					requirementDetailId : "",
					materialId : "",
					materialName : "",
					equCode : "",
					materSize : "",
					parameter : "",
					stockUmName : "",
					appliedQty : "",
					estimatedPrice : "",
					left : "",
					maxStock : "",
					usage : "",
					factory : "",
					needDate : "",
					supplier : "",
					memo : "",
					itemId : "",
					planOriginalId : "",
					planOriginalIdHId : "",
					planOriginalIdHName : "",
					lastModifiedDate : "",
					isNewRecord : "total"
				});

		// 原数据个数
		var count = materialStore.getCount();
		for (i = 0; i < count; i++) {
			var record2 = materialStore.getAt(i);
			allMaterialIds.push(record2.get('requirementDetailId'));
			allMaterialIdsD.push(formatTime(record2.get('lastModifiedDate')));
		}
		// 停止原来编辑
		materialGrid.stopEditing();
		// 插入统计行
		materialStore.insert(count, record);
		materialGrid.getView().refresh();
		totalCount = materialStore.getCount() - 1;
	};
	var totalSum = 0;

	var planGridComboTree = new Ext.ux.ComboBoxTree({
				anchor : '100%',
				displayField : 'text',
				valueField : 'id',
				hiddename : 'planOriginalId',
				blankText : '请选择',
				emptyText : '请选择',
				readOnly : true,
				tree : {
					xtype : 'treepanel',
					// 虚拟节点,不能显示
					rootVisible : false,
					loader : new Ext.tree.TreeLoader({
								dataUrl : 'resource/getPlanOriginalNode.action'
							}),
					root : new Ext.tree.AsyncTreeNode({
								id : '0',
								name : '合肥电厂',
								text : '合肥电厂'
							})

				},
				listeners : {
					collapse : function(obj) {
						materialGrid.getSelectionModel().getSelected().set(
								"planOriginalIdHName", obj.getRawValue());
						materialGrid.getSelectionModel().getSelected().set(
								"planOriginalId", obj.getValue());
					}
				},
				selectNodeModel : 'leaf'
			})

	// var gridItemComboBox = new Ext.form.ComboBox({
	// allowBlank : false,
	// readOnly : true,
	// mode : 'local',
	// displayField : 'name',
	// valueField : 'id',
	// typeAhead : true,
	// forceSelection : true,
	// mode : 'local',
	// triggerAction : 'all',
	// selectOnFocus : true,
	// // store : txtMrItemStore,
	// anchor : '100%'
	// }); // 2009

	// 费用来源
	// var gridTxtMrItem = new Ext.form.TriggerField({
	// // fieldLabel : '费用来源<font color ="red">*</font>',
	// displayField : 'text',
	// valueField : 'id',
	// blankText : '请选择',
	// emptyText : '请选择',
	// anchor : '100%',
	// name : "itemName",
	// allowBlank : false,
	// readOnly : true,
	// hidden :true,
	// hideLabel:true
	// });
	// txtMrCostDept.onTriggerClick = choseItem;
	// var txtMrItemH = new Ext.form.Hidden({
	// name : 'mr.itemCode'
	// })

	var gridMemoTextArea = new Ext.form.TextArea({
		listeners : {
			"render" : function() {
				this.el.on("dblclick", function() {
					var record = materialGrid.getSelectionModel().getSelected();
					var value = record.get('memo');
					memoText.setValue(value);
					win.show();
				})
			},
			"change" : function() {
				if (this.el.getValue().length > 100) {
					gridMemoTextArea.setValue("");
				}
			}

		}
	})
	var _sm = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			})

	// ----------add by drdu 20100407---------------------

	var mnoLabel = new Ext.form.Label({
				id : 'mnoLabel',
				name : 'mnoLabel'
			});
	var mnameLabel = new Ext.form.Label({
				id : "mnameLabel",
				name : 'mnameLabel'
			});
	var materSizeLabel = new Ext.form.Label({
				id : "materSizeLabel",
				name : 'materSizeLabel'
			});

	// --------------------------------------------------
	// 物料grid
	var materialGrid = new Ext.grid.EditorGridPanel({
		region : "center",
		// autoSizeColumns : true,
		frame : false,
		border : false,
		enableColumnMove : false,
		// 单选
		// sm : new Ext.grid.RowSelectionModel({
		// singleSelect : true
		// }),
		sm : _sm,
		tbar : [btnAddmaterial,
				"-",
				btnDeletematerial,
				'-',
				"<div>&nbsp;&nbsp;</div>",
				"<div>&nbsp;&nbsp;</div>",
				// "<font color
				// ='red'>物料编码：</font>",mnoLabel,"<div>&nbsp;&nbsp;</div>","<font
				// color ='red'>物料名称：</font>",mnameLabel,
				// "<div>&nbsp;&nbsp;</div>","<font color
				// ='red'>规格型号：</font>",materSizeLabel
				mnoLabel, "<div>&nbsp;&nbsp;</div>", mnameLabel,
				"<div>&nbsp;&nbsp;</div>", materSizeLabel],
		// 单击修改
		clicksToEdit : 1,
		store : materialStore,
		columns : [_sm, new Ext.grid.RowNumberer({
							header : "行号",
							width : 35
						}), { // 明细表ID
					hidden : true,
					dataIndex : 'requirementDetailId'
				}, {
					header : "物料编码",
					width : 100,
					dataIndex : 'materialCode',
					sortable : true
				}, { // 物料名称
					header : "物料名称<font color ='red'>*</font>",
					width : 100,
					css : CSS_GRID_INPUT_COL,
					dataIndex : 'materialName'
				}, {
					hidden : true,
					dataIndex : 'materialId'
				}, { // 设备名称
					header : "设备名称",
					width : 100,
					dataIndex : 'equCode'
				}, {
					hear : "",
					dataIndex : "clickToChose",
					width : 40,
					renderer : creatButton
				}, { // 规格型号
					header : "规格型号",
					width : 100,
					dataIndex : 'materSize'
				}, { // 材质/参数
					header : "材质/参数",
					width : 100,
					dataIndex : 'parameter'
				}, { // 单位
					header : "单位",
					width : 100,
					dataIndex : 'stockUmName'
				}, { // 申请数量
					header : "申请数量<font color ='red'>*</font>",
					width : 100,
					align : 'right',
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
								maxValue : 99999999999.9999,
								minValue : 0,
								decimalPrecision : 4
							}),
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var appliedQty = record.data.appliedQty;
							// 强行触发renderer事件
							var totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'appliedQty', totalSum);
							}

							return moneyFormat(appliedQty);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('appliedQty');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'appliedQty'
				}, { // 估计单价 modify By ywliu 09/05/14
					header : "估计单价<font color ='red'>*</font>",
					width : 100,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							return moneyFormat(value)
						}
					},
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.NumberField({
								maxValue : 99999999999.9999,
								minValue : 0,
								decimalPrecision : 4
							}),
					dataIndex : 'estimatedPrice'
				}, { // 估计金额
					header : "估计金额",
					width : 100,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							var subSum = record.data.appliedQty
									* record.data.estimatedPrice;
							// 强行触发renderer事件
							var totalSum = 0;
							record.set('estimatedSum', subSum);
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							if (store.getAt(store.getCount() - 1)
									.get('isNewRecord') == 'total') {
								store.getAt(store.getCount() - 1).set(
										'estimatedSum', totalSum);
							}
							return moneyFormat(subSum);
						} else {
							totalSum = 0;
							for (var i = 0; i < store.getCount() - 1; i++) {
								totalSum += store.getAt(i).get('estimatedSum');
							}
							return "<font color='red'>" + moneyFormat(totalSum)
									+ "</font>";
						}
					},
					dataIndex : 'estimatedSum'
				}, { // 当前库存
					header : "当前库存",
					width : 100,
					align : 'right',
					renderer : function(value, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							return numberFormat(value)
						}
					},
					dataIndex : 'left'
				}, { // 最大库存
					hidden : true,
					dataIndex : 'maxStock'
				}, { // 计划来源
					header : "计划种类<font color ='red'>*</font>",
					witdh : 100,
					renderer : function(v, index, record) {

						return record.get("planOriginalIdHName");
					},
					css : CSS_GRID_INPUT_COL,
					editor : planGridComboTree,
					hidden : true,
					dataIndex : 'planOriginalIdHId'

				}, {
					hidden : true,
					dataIndex : 'planOriginalIdHName'
				}, {
					hidden : true,
					renderer : function(v, index, record) {
						if (v != "" && v != null && v != "undefined") {
							Ext.Ajax.request({
								params : {
									planOrigianlId : v
								},
								url : 'resource/getOrigianlName.action',
								method : 'post',
								success : function(result) {
									var recordSub = eval('('
											+ result.responseText + ')');
									if (recordSub.data != null
											&& recordSub.data != "") {
										var text = recordSub.data['planOriginalDesc'];
										record.set("planOriginalIdHName", text);
										record.set("planOriginalIdHId", text);
									}
								}

							});
							return v;
						} else {
							return "";
						}
					},
					dataIndex : 'planOriginalId'
				}, { // 费用来源
					header : "费用来源<font color ='red'>*</font>",
					witdh : 100,
					hidden : true,
					// css : CSS_GRID_INPUT_COL,
					// editor : gridTxtMrItem,
					renderer : function(val, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex < store.getCount() - 1) {
							// if (val == "1") {
							// return "费用来源1";
							// } else if (val == "2") {
							// return "费用来源2";
							// } else if (val == "3") {
							// return "费用来源3";
							// } else if (val == "4") {
							// return "费用来源4";
							// } else {
							// return "";
							// }
							return txtMrItem.getValue();
						} else {
							return "";
						}

					},
					dataIndex : 'itemId'
				}, { // 用途
					header : "用途",
					width : 100,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.TextField({
								maxLength : 100
							}),
					renderer : function(v, cellmeta, record, rowIndex,
							columnIndex, store) {
						if (rowIndex = store.getCount() - 1) {
						}
						return v;
					},
					dataIndex : 'usage'
				}, { // 生产厂家
					header : "生产厂家",
					width : 100,
					dataIndex : 'factory'
				}, { // 需求日期
					header : "需求日期<font color ='red'>*</font>",
					width : 100,
					renderer : renderDate,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.TextField({
								format : 'Y-m-d',
								itemCls : 'sex-left',
								readOnly : true,
								clearCls : 'allow-float',
								checked : true,
								width : 100,
								anchor : '100%',
								style : 'cursor:pointer',
								listeners : {
									focus : function() {
										WdatePicker({
													// 时间格式
													startDate : '%y-%M-%d',
													dateFmt : 'yyyy-MM-dd',
													alwaysUseStartDate : false,
													onclearing : function() {
														materialGrid
																.getSelectionModel()
																.getSelected()
																.set(
																		"needDate",
																		"")
													},
													onpicked : checkTime2
												});

									}

								}

							}),
					dataIndex : 'needDate'
				}, { // 建议供应商
					header : "建议供应商",
					width : 100,
					css : CSS_GRID_INPUT_COL,
					editor : new Ext.form.TextField({
								maxLength : 50
							}),
					dataIndex : 'supplier'
				}, { // 备注
					header : "备注",
					width : 145,
					dataIndex : 'memo',
					css : CSS_GRID_INPUT_COL,
					editor : gridMemoTextArea
				}, {
					hidden : true,
					dataIndex : 'lastModifiedDate'
				}
				// 物料分类add by ltong
				, {
					hidden : true,
					dataIndex : 'classNo'
				}]
	});
	// materialGrid.on('celldblclick', editMemo);
	materialGrid.on('beforeedit', checkIsEdit);
	materialGrid.on('cellclick', choseEdit);
	materialGrid.on('rowmousedown', function(grid, rowIndex, e) {
				if (rowIndex < materialStore.getCount() - 1) {
					return true;
				}
				return false;
			});

	// add by drdu 20100407
	materialGrid.on('rowclick', function() {
				var record = materialGrid.getSelectionModel().getSelected();
				if (record != undefined) {
					var code = record.get('materialCode') == null ? '' : record
							.get('materialCode');
					var name = record.get('materialName') == null ? '' : record
							.get('materialName');
					var size = record.get('materSize') == null ? '' : record
							.get('materSize');
					mnoLabel.setText("<font color ='red'>物料编码：" + code
							+ "</font>");
					mnameLabel.setText("<font color ='red'>物料名称：" + name
							+ "</font>");
					materSizeLabel.setText("<font color ='red'>规格型号：" + size
							+ "</font>");
				}
			});

	// 备注
	var memoText = new Ext.form.TextArea({
				id : "memoText",
				maxLength : 100,
				width : 180
			});
	var win = new Ext.Window({
				height : 170,
				width : 350,
				layout : 'fit',
				resizable : false,
				closeAction : 'hide',
				modal : true,
				items : [memoText],
				buttonAlign : "center",
				title : '详细信息录入窗口',
				buttons : [{
							text : "保存",
							iconCls : Constants.CLS_SAVE,
							handler : function() {
								if (Ext.get("memoText").dom.value.length <= 100) {
									var record = materialGrid.selModel
											.getSelected()
									record.set("memo",
											Ext.get("memoText").dom.value);
									win.hide();
								}
							}
						}, {
							text : "取消",
							iconCls : Constants.CLS_CANCEL,
							handler : function() {
								win.hide();
							}
						}]
			});
	var fifthLine = new Ext.Panel({
				border : false,
				layout : "column",
				anchor : '100%',
				items : [{
							columnWidth : 1,
							layout : "fit",
							border : false,
							autoScroll : false,
							items : [materialGrid]
						}]
			});
	// --------第五行grid--结束
	// 表单panel
	var formPanel = new Ext.FormPanel({
				labelAlign : 'right',
				labelPad : 15,
				labelWidth : 75,
				height : 200,
				border : false,
				region : 'north',
				items : [toolBarTop, firstLine, secondLine, thirdLine,
						fourLine, forthLine, fifthLine]
			});
	var registerPanel = new Ext.Panel({
				frame : false,
				border : false,
				layout : 'border',
				items : [formPanel, materialGrid]
			});

	var layout = new Ext.Viewport({
				layout : 'fit',
				margins : '0 0 0 0',
				region : 'center',
				border : true,
				items : [registerPanel]
			});

	// init
	getInitData(register.headId);

	// add by fyyang 090414
	function openReportWin() {
		var args = new Object();
		var args = new Object();
		args.busiNo = txtMrNo.getValue();
		args.title = "需求申请单上报";
		// alert(Ext.get("mr.planOriginalId").dom.value);
		var flowCode = getFlowCode(Ext.get("mr.planOriginalId").dom.value);

		if (flowCode == "") {
			Ext.Msg.alert("没有此申请单的流程");
			return;
		}
		args.flowCode = flowCode;
		args.entryId = entryId;
		args.mrTypeId = txtMrOriginal.getValue();
		var check = window
				.showModalDialog(
						'../report/reportSign.jsp',
						args,
						'dialogWidth=700px;dialogHeight=500px;center=yes;help=no;resizable=no;status=no;');
		if (check) {
			clearForm();
			formPanel.getForm().reset();// modify by ywliu 20091010
			materialStore.removeAll();// modify by ywliu 20091010
			register.find();
		}
	}

});
