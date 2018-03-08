// 定义JS共通常量
// 2008/12/01 wangjunjie Create

var Constants = {};

// 物资共通常量
Constants.COM_C_001 = "&nbsp&nbsp&nbsp确认要保存吗？&nbsp&nbsp&nbsp"                                                
Constants.COM_C_002 = "&nbsp&nbsp&nbsp确认要删除吗？&nbsp&nbsp&nbsp"                                                
Constants.COM_C_003 = "保存当前画面变更内容吗？"                                      
Constants.COM_C_004 = "放弃已修改的内容吗？"
Constants.COM_C_005 = "&nbsp&nbsp&nbsp确认要取消吗？&nbsp&nbsp&nbsp"
Constants.COM_C_006 = "&nbsp&nbsp&nbsp确认要上报吗？&nbsp&nbsp&nbsp"
Constants.COM_C_007 = "确认要导出数据吗？"
Constants.COM_C_010 = "&nbsp&nbsp&nbsp确认要存档吗？&nbsp&nbsp&nbsp"
Constants.COM_C_011 = "审核以后，数据不可变更，是否审核？"
Constants.COM_C_012 = "是否撤销选择的部门审核数据？"
Constants.COM_C_013 = "&nbsp&nbsp&nbsp确认要销假吗？&nbsp&nbsp&nbsp"
Constants.COM_I_001 = "&nbsp&nbsp&nbsp请选择一行。&nbsp&nbsp&nbsp"                                                  
Constants.COM_I_002 = "该当数据已经被其他的用户更改，请确认。"                        
Constants.COM_I_003 = "没有检索到相应的数据，请确认。"                                
Constants.COM_I_004 = "&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp"  
Constants.COM_I_005 = "&nbsp&nbsp&nbsp删除成功。&nbsp&nbsp&nbsp"
Constants.COM_I_006 = "画面数据未修改。"
Constants.COM_I_007 = "&nbsp&nbsp&nbsp上报成功。&nbsp&nbsp&nbsp"
Constants.COM_I_010 = "请先保存数据。"
Constants.COM_I_025 = "&nbsp&nbsp请维护考勤日期。&nbsp&nbsp"
Constants.COM_I_026 = "考勤数据审核成功！"
Constants.COM_I_027 = "撤销前回审核成功！"
Constants.COM_I_029 = "请维护考勤部门{0}的考勤标准。"
Constants.COM_E_001 = "正在结账，此业务无法进行。"                                    
Constants.COM_E_002 = "{0}不能为空，请输入。"                                         
Constants.COM_E_003 = "{0}不能为空，请选择。"                                         
Constants.COM_E_004 = "{0}不能小于当前日期。"                                         
Constants.COM_E_005 = "{0}必须大于{1}。"                                              
Constants.COM_E_006 = "{0}必须小于{1}。"                                              
Constants.COM_E_007 = "{0}已存在。请重新输入。"                                       
Constants.COM_E_008 = "该数据已经不可使用，请重新选择。"                              
Constants.COM_E_009 = "{0}不能在{1}之后。请确认。"                                    
Constants.COM_E_010 = "{0}在数据库中不存在，请先保存。"                               
Constants.COM_E_011 = "没有打印数据，请确认。"  
Constants.COM_E_012 = "{0}不能为负数。" 
Constants.COM_E_013 = "{0}必须大于0。"
Constants.COM_E_014 = "操作数据库过程中异常终了。"   
Constants.COM_E_015 = "他人使用中。"
Constants.COM_E_016 = "请选择一行。"
Constants.COM_E_017 = "{0}{1}所在行的{2}不能为空，请输入。"
Constants.COM_E_018 = "缺少明细物料信息，请增加明细。"   
Constants.COM_E_023 = "格式化日期过程中发生异常。"
Constants.COM_E_024 = "部门级别不能大于{0}。请确认。"
Constants.COM_E_025 = "该当路径{0}中没有找到对应的文件。请确认。"
Constants.COM_E_026 = "{0}必须不大于{1}。"
Constants.COM_E_027 = "{0}必须不小于{1}。"
Constants.COM_E_028 = "第{0}行的{1}不能为空。"
Constants.COM_E_029 = "第{0}行的{1}长度过长。"
Constants.COM_E_030 = "第{0}行的{1}必须是数字。"
Constants.COM_E_031 = "第{0}行的{1}必须是半角英数字。"
Constants.COM_E_032  ="{0}的数据不可以被{1}，请重新选择。"
Constants.COM_E_038 = "第{0}行的{1}必须是字符串。"
Constants.COM_E_039 = "导入的文件格式必须是Excel格式。"
Constants.COM_E_034 = "该员工信息已存档，不能被修改。"
Constants.COM_E_035 = "该员工信息已存档，不能被删除。"
Constants.COM_E_036 = "员工工号为{0}的已存在。"
Constants.COM_E_037 = "调动后部门不能为调动前部门，请重新选择。"
Constants.COM_E_041 = "导入文件的内容格式不正确，无法导入。请确认"
Constants.COM_E_042 = "第{0}行的{1}不能为数字，请用文本形式存储该字段。"
Constants.COM_E_043 = "第{0}行的{1}必须为整数。"
Constants.COM_E_044 = "第{0}行的{1}不能大于最大值。"
Constants.COM_E_045 = "第{0}行的{1}在DB中不存在。"
Constants.COM_E_046 = "第{0}行的{1}在DB中已存在。"
Constants.COM_E_047 = "第{0}行和第{1}行的{2}重复。"
Constants.COM_E_048 = "第{0}行的{1}数据非法。"
Constants.COM_E_049 = "第{0}行的{1}数据为负。"
Constants.COM_E_050 = "第{0}行数据错误，{1}必须小于{2}。"
Constants.COM_E_051 = "第{0}行数据错误，{1}，{2}必须同时输入。"
Constants.COM_E_052 = "{0}不能大于当前日期。"
Constants.COM_E_053 = "{0}不能大于当前月份。"
Constants.COM_E_054 = "{0}不是出勤日，请确认。"
Constants.UPLOAD_SUCCESS = "&nbsp&nbsp&nbsp上传成功。&nbsp&nbsp&nbsp"

Constants.RB001_E_001 = "供应商的同一种物料在相同时段内不能有不同报价。"
Constants.RI012_W_001 = "删除此分类会把其子分类一起删除，确认删除？"
Constants.RI002_E_001 = "至少选中待选项中的一项。"
Constants.RI002_E_002 = "至少选中已选项中的一项。"
Constants.RI002_E_003 = "人员不能重复。"
// 库存月结
Constants.RS011_E_001 = "请输入正确的结帐年月份。"
Constants.RS011_E_002 = "月结过程中数据异常，请联系管理员。"
Constants.RS011_E_003 = "已经处于结帐状态，拒绝操作。";
Constants.RS011_E_004 = "所有业务已结帐完毕，无需结帐。";
Constants.RS011_C_001 = "确认对本月进行库存结算处理吗？"
Constants.RS011_C_002 = "确认对上次的库存结算进行重新处理吗？"
Constants.RS011_C_003 = "结帐处理成功。"
Constants.RB003_I_001 = "保存成功，到货单号：{0}。"
Constants.RB003_I_002 = "没有到货无法上报。"
Constants.RB003_C_001 = "&nbsp&nbsp&nbsp确认要上报吗？&nbsp&nbsp&nbsp"
Constants.RB003_E_001 = "到货单不存在，请先生成到货单。"
Constants.COM_W_001 = "{0}的库存可能会超过最大库存，确认{1}？"
Constants.COM_W_002 = "数据未保存，是否现在保存？"
// 物料盘点
Constants.RS006_E_001 = "物料{0}正在盘点中。"
// 采购员维护
Constants.RI002_E_004 = "请先选择一个采购员。"
// 计划员维护
Constants.RI009_E_004 = "请先选择一个计划员。"
// 汇率维护
Constants.RI008_E_001 = "基准货币和兑换货币不能为同一种货币。"
Constants.RI008_E_002 = "相同的基准货币和兑换货币在相同时段内不能有不同汇率。"
// 物料基础资料维护
Constants.RI006_E_001 = "次供应商不能与主供应商相同。"
Constants.RI006_E_002 = "最小库存不能超过最大库存。"
// 需求计划登记
Constants.RP002_E_002 = "明细部物料编码不能重复。"
Constants.RP002_E_001 = "计划来源属于生产时，必须输入归口专业。"
// 盘点损益表
Constants.RS008_E_002 = "盘点单{0}不存在。"
//替代物料维护
Constants.RI011_E_001 = "同一种物料在相同的时间段不能有相同替代物料。";
Constants.RI011_E_002 = "物料和替代物料不能相同。";
// textarea的高度
Constants.TEXTAREA_HEIGHT = 48;

// 按钮:保存
BTN_SAVE = "保存0";
// 按钮:保存
Constants.BTN_SAVE = "保存";
// CSS样式：保存
Constants.CLS_SAVE="save";

// 按钮:查询
Constants.BTN_QUERY = "查询";
// CSS样式：查询
Constants.CLS_QUERY = "query";

// 按钮:增加
Constants.BTN_ADD = "新增";
// CSS样式：增加
Constants.CLS_ADD = "add";

// 按钮:修改
Constants.BTN_UPDATE = "修改";
// CSS样式：修改
Constants.CLS_UPDATE = "update";

// 按钮:删除
Constants.BTN_DELETE = "删除";
// CSS样式：删除
Constants.CLS_DELETE = "delete";

// 按钮:取消
Constants.BTN_CANCEL = "取消";
// CSS样式：取消
Constants.CLS_CANCEL = "cancer";

// 按钮:">>"
Constants.BTN_TO_RIGHT = ">>";

// 按钮:"<<"
Constants.BTN_TO_LEFT = "<<";

// 自动生成
Constants.AUTO_CREATE = "自动生成";
// 正在保存数据...
Constants.DATA_SAVING="正在保存数据...";
// 注意
Constants.NOTICE="注意";
// 错误
Constants.ERROR="错误";
// 出现未知错误
Constants.UNKNOWN_ERR="出现未知错误.";
// 用户验证失败
Constants.USER_CHECK_ERROR = "用户名或密码输入错误";
// 系统提示信息
Constants.SYS_REMIND_MSG="提示信息";
// 请选择其中一项进行编辑！
Constants.SELECT_COMPLEX_MSG="请选择其中一项进行编辑！";
// 请先选择要编辑的行！
Constants.SELECT_NULL_UPDATE_MSG="请先选择要编辑的行！";
// 请选择要删除的记录！
Constants.SELECT_NULL_DEL_MSG="请选择要删除的记录！";
//操作出现失败
Constants.OPERATE_ERROR_MSG="操作失败,请联系管理员!";
// 删除成功！
Constants.DEL_SUCCESS="&nbsp&nbsp&nbsp删除成功！&nbsp&nbsp&nbsp";
// 删除时出现未知错误.
Constants.DEL_ERROR="删除时出现未知错误.";
// 显示第 {0} 条到 {1} 条记录，一共 {2} 条
Constants.DISPLAY_MSG="显示第 {0} 条到 {1} 条记录，一共 {2} 条";
// 没有记录
Constants.EMPTY_MSG="没有记录";
// 提交方法：POST
Constants.POST = "POST";
// 下一步
Constants.NEXT_STEP = "下一步";
// checkbox选中的value
Constants.CHECKED_VALUE = "Y";
// checkbox未选中的value
Constants.UNCHECKED_VALUE = "N";

// 正在结账wait
Constants.BALANCING_WAIT = '正在结账，请稍等...';

// 请先选择工作负责人
Constants.SELECT_CHARGE_BY_MSG = "请先选择工作负责人";
// 您确认将要填写的为[紧急工作票]吗？
// 紧急工作票将不通过[工作票签发人]，
// 而直接发给[运行紧急票接收	人]。
Constants.IS_EMERGENCY = "您确认将要填	写的为[紧急工作票]吗？紧急工作票将不通过[工作票签发人]，而直接发给[运行紧急票接收人]。";
// 是否确定删除id为" + ids + "的记录？
Constants.DelMsgById=funcDelMsgById;//需要参数ids
// 删除提示信息
Constants.DelMsg = "确认删除选择的记录？";
Constants.DEL_MSG_1 = '是否确定删除{0}为{1}的记录？';

// 行政管理 追加开始
// 司机编码重复
Constants.ADD_DRIVERCODE_FAILURE = "DCF";
// 按钮:打印申请单
Constants.BTN_PRINT_APPLY= "打印申请单";
// CSS样式：取消
Constants.CLS_PRINT = "print";

// 按钮:导出
Constants.BTN_EXPORT= "导出";
// CSS样式：导出
Constants.CLS_EXPORT = "export";

// 按钮:上报
Constants.BTN_REPOET= "上报";
// CSS样式：上报
Constants.CLS_REPOET = "upcommit";

// 按钮:上传
Constants.BTN_UPLOAD= "上传";
// CSS样式：上传
Constants.CLS_UPLOAD = "upLoad";

// 按钮:发送
Constants.BTN_SEND= "发送";
// CSS样式：发送
Constants.CLS_SEND = "send";

// 按钮:审核
Constants.BTN_APPROVE= "审核";
// CSS样式：审核
Constants.CLS_APPROVE = "approve";

// 按钮:退回
Constants.BTN_UNTREAD= "退回";
// CSS样式：退回
Constants.CLS_UNTREAD = "untread";

// 按钮:明细
Constants.BTN_DETAIL= "明细";
// CSS样式：明细
Constants.CLS_DETAIL = "detail";

// 按钮:维修结束
Constants.BTN_STOP= "维修结束";
// CSS样式：停止
Constants.CLS_STOP = "stop";

// 按钮:打印
Constants.BTN_PRINT= "打印";
// 按钮:打印
Constants.BTN_CLOSE= "关闭";

// 按钮:确认
Constants.BTN_CONFIRM = "确认";
// CSS样式：确认
Constants.CLS_OK = "ok";

// 按钮:登记  add by drdu  090414
Constants.BTN_WRITE = "登记";
// CSS样式：登记
Constants.CLS_WRITE = "write";

// 按钮:按计划生成  add by drdu  090414
Constants.BTN_LIST = "按计划生成";
// CSS样式：按计划生成
Constants.CLS_LIST = "list";

// 提示信息:确认
Constants.NOTICE_CONFIRM = "确认";
// 确定
Constants.CONFIRM="确定";
// 注意
Constants.NOTICE="注意";
// 选择
Constants.SELECT="选择";
// 存档
Constants.SAVEFILE="存档";
// 排他处理:数据正在被使用
Constants.DATA_USING = "U";
// 操作DB失败
Constants.SQL_FAILURE = "SQL";
// 操作文件失败
Constants.IO_FAILURE = "IO";
// 数据格式化失败
Constants.DATE_FAILURE = "DATA";
// 文件不存在
Constants.FILE_NOT_EXIST = "FILE";
/* 追加开始 追加人：柴浩 */
// 车牌号重复
Constants.ADD_CARNO_FAILURE = "CNF";
/* 追加结束 追加人：柴浩 */
// 行政管理 追加结束
function getMsg(id)
{
	if(arguments.length == 1)
	{
		return Constants[id];
	} else if(arguments.length > 1) {
		var msg = Constants[id];
		for(var i = 0; i < arguments.length - 1; i++) 
		{
			msg = msg.replace('{' + i +'}', arguments[i + 1]);
		}
		return msg;
	}
}

function funcDelMsgById(Ids)
{
	return "是否确定删除id为" + Ids + "的记录？";
}


// 是否确定删除编码为" + ids + "的记录？
Constants.DelMsgByCode=funcDelMsgByCode;//需要参数ids
function funcDelMsgByCode(Ids)
{
	return "是否确定删除编码为" + Ids + "的记录？";
}

// 是否确定删除区域名称为" + ids + "的记录？
Constants.DelMsgByLocationName=funcDelMsgByLocationName;//需要参数ids
function funcDelMsgByLocationName(Ids)
{
	return "是否确定删除区域名称为" + Ids + "的记录？";
}
// 默认显示条数
Constants.PAGE_SIZE = 18;
//选择工作票类型
Constants.SELECT_WORKTICKET_TYPE="请选择工作票类型！";

// 是否确定删除编号为" + ids + "的记录？
Constants.DelMsgByCardCode=funcDelMsgByCardCode;//需要参数ids
function funcDelMsgByCardCode(Ids)
{
	return "是否确定删除编号为" + Ids + "的记录？";
}

// 代表“未上报”状态的WORKTICKET_STAUS_ID
Constants.WORKTICKET_STAUS_ID_NotReport="1";
// 代表“已退票”状态的WORKTICKET_STAUS_ID
Constants.WORKTICKET_STAUS_ID_Back="9";
// 上报成功！
Constants.REPORT_SUCCESS="&nbsp&nbsp&nbsp上报成功！&nbsp&nbsp&nbsp";
// 审批成功！
Constants.APPROVE_SUCCESS="&nbsp&nbsp&nbsp审批成功！&nbsp&nbsp&nbsp";
// 确定
Constants.CONFIRM="确定";
// 请填写审批意见！
Constants.INPUT_COMMENT="请填写审批意见！";
// 请选择要上报的记录！
Constants.SELECT_NULL_REPORT_MSG="请选择要上报的记录！";
// 请选择要浏览的记录！
Constants.SELECT_NULL_BIRT_MSG="请选择要浏览的记录！";
// 是否确定上报id为" + ids + "的记录？
Constants.ReportMsgById=funcReportMsgById;//需要参数ids
function funcReportMsgById(Ids)
{
	return "是否确定上报id为" + Ids + "的记录？";
}
// 存档
Constants.SAVEFILE="存档";
// 请选择要存档的记录
Constants.SELECT_NULL_SAVEFILE_MSG="请选择要存档的记录!";
// 确定存档选择的记录
Constants.SAVEFILE_MSG="确定存档选择的记录?";
// 存档成功
Constants.SAVEFILE_SUCCESS="&nbsp&nbsp&nbsp存档成功！&nbsp&nbsp&nbsp";

Constants.BirtNull="E";
Constants.BirtReport=funcBirtReport;

// 请选择其中一项进行编辑！
Constants.SELECT_COMPLEX_VIEW_MSG="请选择其中一行预览！";
// 请先选择要编辑的行！
Constants.SELECT_NULL_VIEW_MSG="请先选择要预览的行！";

// 电气一种票CODE
Constants.ELECTRICONE_CODE="1";
// 安措CODE
Constants.SAFETY_CODE="s";
// 安措CODE
Constants.SAFETY_KBN_ACTION="1";
// 安措CODE
Constants.SAFETY_KBN_CANCEL="2";
//参数 工作票编号 strNo
//参数 工作票类型 strType
//参数 动火级别 安措拆除执行区分 strLevel
//参数 电气一种票状态 strStatus
function funcBirtReport(strNo,strType,strLevel,strStatus){
		  var strAdds="";			
    	  if(strType=="1"){
		    strAdds= "/powerrpt/report/webfile/bqmis/ElectricOne.jsp?no="+strNo+"&status="+strStatus;
		  }
		  else if(strType=="2"){
			strAdds="/powerrpt/report/webfile/bqmis/ElectricTwo.jsp?no="+strNo;
		  }
		  else if(strType=="3"){
			strAdds="/powerrpt/report/webfile/bqmis/energMachine.jsp?no="+strNo;
		  }
		  else if(strType=="5"){
			strAdds="/powerrpt/report/webfile/bqmis/energControl.jsp?no="+strNo;
		  }
		  else if(strType=="4"){
		  	if(strLevel=="1")
			strAdds="/powerrpt/report/webfile/bqmis/HeatOne.jsp?no="+strNo;
			else if(strLevel=="2")
			strAdds="/powerrpt/report/webfile/bqmis/HeatTwo.jsp?no="+strNo;
		  }	
		  else if(strType=="s"){
		  	if(strLevel=="1")
			strAdds="/powerrpt/report/webfile/bqmis/safetyAction.jsp?no="+strNo;
			else if(strLevel=="2")
			strAdds="/powerrpt/report/webfile/bqmis/safetyCancel.jsp?no="+strNo;
		  }		 
		  else{	
		  	strAdds="E";
		  }	
		  return strAdds;
}

// 验证编码
function validateCode(value) {
    if(!/^\w+$/.test(value)) {
    	Ext.Msg.alert(Constants.ERROR, "编码只能是字符和数字，不能有汉字和空格！");
    	return false;
    }
    return true;
}

//---------------add by fyyang 090908----------------------------------------

//人事业务消息
//↓------------------------------------------------↓//
Constants.PD023_I_001 = "请选择待加入的班组。"                                                                                                                
Constants.PD023_I_002 = "该部门已加入，请重新选择。"                                                                                                             
Constants.PD023_I_003 = "该部门非底层部门，不能作为班组。"                                                                                                              
Constants.PD023_I_004 = "请选择班组。"   
Constants.PD023_E_001 = "该部门已加入，请重新选择。"  
Constants.PD023_E_002 = "该部门非底层部门，不能作为班组。"  
Constants.PD023_C_001 = "是否撤销班组\"{0}\"？"                                                                                                             
Constants.PO004_E_001 = "拆分/合并部门明细部记录的条数必须大于等于2。请增加。"                                                                                                               
Constants.PO004_E_002 = "变更后上级部门不能为当前部门的子孙部门或者自身。请确认。"                                                                                                              
Constants.PO004_E_003 = "该当部门有子部门不能撤销。请确认。"                                                                                                             
Constants.PO004_E_004 = "变更后上级部门的部门级别和该当部门的子孙层数之和大于{0} 。请确认。"                                                                                                               
Constants.PO004_E_005 = "追加的部门中部门编码有重复项。请确认。"                                                                                                               
Constants.PO004_E_006 = "追加的部门在DB中已经存在。请确认。"
Constants.PO004_E_007 = "当前部门将被撤销，不能追加子部门。请确认。"
Constants.PO004_E_008 = "变更后上级部门不能为当前上级部门。请确认。"
Constants.PO004_E_009 = "明细部分的部门{0}有子部门，不能被增加合并。请确认。"
Constants.PO005_I_001 = "待变更部门人员没有处理完成，还需要再次处理。"                                                                                                                
Constants.PO005_C_001 = "是否将已选中的职工调入选中的部门？"                                                                                                             
Constants.PO005_C_002 = "是否将所有的待分配职工调入选中的部门？"                                                                                                               
Constants.PO005_C_003 = "是否将已选中的职工调入待变更的部门？"                                                                                                                
Constants.PO005_C_004 = "是否将所有的待分配职工调入待变更的部门？"                                                                                                              
Constants.PD021_E_001 = "请输入合法合同到期月份(YYYYMM)。"                                                                                                              
Constants.PD016_E_001 = "{0}已经签过合同。" 
Constants.PD016_E_002 = "该员工在下面的列表中已经存在。" 
Constants.PD001_I_001 = "该员工信息已存档，不能被删除。"                                                                                                               
Constants.PD001_I_002 = "员工编码为{0}的已存在。"                                                                                                             
Constants.PD001_I_019 = "员工工号为{0}的已存在。"
Constants.PE002_I_001 =  "员工工号为{0}的员工已派遣出,请重新选择员工。" 
Constants.PE002_I_002 =  "员工工号重复，请重新选择员工。"   
//员工工号维护
Constants.PD001_E_001 = "第{0}行{1}列不能为空。";
Constants.PD001_E_002 = "第{0}行{1}列长度过长。";
Constants.PD001_E_003 = "第{0}行{1}列数据类型不正确。";
// 离职员工登记
Constants.PD004_E_001 = "该人员已登记，请重新选择人员。"
Constants.PD003_E_001 ="主岗位有且只有一个，请确认。"
//终止合同登记
Constants.PD019_C_001 ="确认要终止合同吗？";
Constants.PD019_I_001 ="合同终止成功。";
Constants.PD019_E_001 ="终止类别不能为空。";
Constants.PE004_E_001 = "{0}的薪酬变动单已经存在，请重新选择员工。"                                                                                                                
Constants.PE003_E_001 = "{0}的员工调动单已经存在，请重新选择员工。"  
Constants.PE003_E_002 = "调动后部门为调动前部门的场合，调动后岗位不能为调动前岗位，请重新选择。"  
Constants.PE005_E_001 = "员工姓名为{0}的员工已借调出，请重新选择员工。" 
Constants.PO002_E_001 = "岗位编码为{0}的已存在。"   
Constants.PO002_E_002 = "第{0}行的{1}在DB中不存在。" 
Constants.PO002_E_003 = "第{0}行的{1}在DB中已存在。"
Constants.PO002_E_004 = "第{0}行和第{1}行的{2}重复。"
Constants.PO002_E_005 = "第{0}行的{1}数据非法。"
Constants.PO002_E_006 = "第{0}行的{1}数据为负。"
Constants.PO003_I_001 = "请选择待分配的岗位。"                                                                                                                
Constants.PO003_I_002 = "请选择已分配的岗位。"
Constants.PO003_E_001 = "分配的岗位已经存在。"
Constants.PO001_E_001 = "第{0}行的部门类别在DB中不存在。"  
Constants.PO001_E_002 = "第{0}行的部门级别不能大于5。"  
Constants.PO001_E_003 = "第{0}行的部门性质数据非法（1、2、3或者4）。"  
Constants.PO001_E_004 = "第{0}行的班组数据非法（0或者1）。"  
Constants.PO001_E_005 = "第{0}行数据非法，不能够反映到部门信息树当中。"  
Constants.PO001_E_006 = "数据非法，有数据的父子关系构成了死循环。"  


//终止合同登记
Constants.PD019_C_001 ="确认要终止合同吗？";
Constants.PD019_I_001 ="合同终止成功。";
Constants.PD019_E_001 ="终止类别不能为空。";
Constants.QJ003_E_001 ="该人员的请假时间有重复，请确认。";
Constants.QJ003_E_003 ="{0}年的{1}仅剩余{2}小时，请确认。";
Constants.QJ003_E_002 ="{0}不是出勤日，请确认。";
Constants.QJ003_E_004 ="请假时长不能为0小时，请重新选择请假时间。";
// 自动生成
Constants.AUTO_CREATE = "自动生成";
// 正在保存数据...
Constants.DATA_SAVING="正在保存数据...";
// 注意
Constants.NOTICE="注意";
// 错误
Constants.ERROR="错误";
// 提示
Constants.REMIND="提示";
// 确认
Constants.CONFIRM="确认";
// 出现未知错误
Constants.UNKNOWN_ERR="出现未知错误.";
// 用户验证失败
Constants.USER_CHECK_ERROR = "用户名或密码输入错误";
// 系统提示信息
Constants.SYS_REMIND_MSG="提示信息";
// 请选择其中一项进行编辑！
Constants.SELECT_COMPLEX_MSG="请选择其中一项进行编辑！";
// 请先选择要编辑的行！
Constants.SELECT_NULL_UPDATE_MSG="请先选择要编辑的行！";
// 请选择要删除的记录！
Constants.SELECT_NULL_DEL_MSG="请选择要删除的记录！";
//操作出现失败
Constants.OPERATE_ERROR_MSG="操作失败,请联系管理员!";
// 删除成功！
Constants.DEL_SUCCESS="&nbsp&nbsp&nbsp删除成功！&nbsp&nbsp&nbsp";
// 删除时出现未知错误.
Constants.DEL_ERROR="删除时出现未知错误.";
// 显示第 {0} 条到 {1} 条记录，一共 {2} 条
Constants.DISPLAY_MSG="显示第 {0} 条到 {1} 条记录，一共 {2} 条";
// 没有记录
Constants.EMPTY_MSG="没有记录";

Constants.COM_E_026 = "{0}必须不大于{1}。"

//↑------------------------------------------------↑//


// 按钮:合同变更
Constants.BTN_CONTRACT_CHANGE = "合同变更";
// CSS样式：合同变更
Constants.CLS_CONTRACT_CHANGE = "contract_change";

// 按钮:合同续签
Constants.BTN_CONTRACT_CONTINUE = "合同续签";
// CSS样式：合同续签
Constants.CLS_CONTRACT_CONTINUE = "contract_continue";

// 按钮:合同终止
Constants.BTN_CONTRACT_END = "合同终止";
// CSS样式：合同终止
Constants.CLS_CONTRACT_END = "contract_end";

// 按钮:人员安排
Constants.BTN_EMP_ARRANGE = "人员安排";
// CSS样式：人员安排
Constants.CLS_EMP_ARRANGE = "emp_arrange";

// CSS样式：左移
Constants.CLS_LEFT_MOVE = "left_move";

// CSS样式：右移
Constants.CLS_RIGHT_MOVE = "right_move";

// 单据状态:未上报
Constants.NOT_REPORT="0";
Constants.NOT_REPORT_MESSAGE ="未上报";
// 单据状态:已上报
Constants.ALREADY_REPORT="1";
Constants.ALREADY_REPORT_MESSAGE ="已上报";
// 单据状态:已终结
Constants.ALREADY_OVER="2";
Constants.ALREADY_OVER_MESSAGE ="已终结";
// 单据状态:已退回
Constants.ALREADY_RETURN="3";
Constants.ALREADY_RETURN_MESSAGE ="已退回";


// 变动类别 (调动)
Constants.ADJUST_TYPE_TRANSFER = "1";
Constants.ADJUST_TYPE_TRANSFER_MESSAGE = "调动";
// 变动类别 (绩效变动)
Constants.ADJUST_TYPE_PERFORM = "2";
Constants.ADJUST_TYPE_PERFORM_MESSAGE = "绩效变动";
// 变动类别 (奖惩违纪)
Constants.ADJUST_TYPE_REWARDS_PUNISH = "3";
Constants.ADJUST_TYPE_REWARDS_PUNISH_MESSAGE = "奖惩违纪";
// 变动类别 (其它)
Constants.ADJUST_TYPE_ELSE = "4";
Constants.ADJUST_TYPE_ELSE_MESSAGE = "其它";

//实际换休时间
Constants.ACTUAL_BREAK_TIME ="实际换休时间";
//月累计时间
Constants.CUMULATIVE_TIME = "月累计时间";


// 岗位调动类别 (低岗至高岗)
Constants.STATION_MOVE_TYPE_UP = "1";
Constants.STATION_MOVE_TYPE_UP_MESSAGE = "低岗至高岗";
// 岗位调动类别 (高岗至低岗)
Constants.STATION_MOVE_TYPE_DOWN = "2";
Constants.STATION_MOVE_TYPE_DOWN_MESSAGE = "高岗至低岗";
// 岗位调动类别 (平级调动)
Constants.STATION_MOVE_TYPE_LATERAL = "3";
Constants.STATION_MOVE_TYPE_LATERAL_MESSAGE = "平级调动";

// CSS样式：行复制
Constants.CLS_LINE_COPY = "copy";
// CSS样式：部门审核
Constants.CLS_DEPT_CHECK = "dept_check";
// CSS样式：撤销前回审核
Constants.CLS_CHECK_ABOLISH = "check_abolish";
// 按钮: 登记
Constants.BTN_REGISTER = "登记";
// CSS样式：登记
Constants.CLS_REGISTER = "register";
// CSS样式：考勤人员变更
Constants.CLS_PERSON_CHANGE = "person_change";
// CSS样式：考勤审核
Constants.CLS_ATTEND_CHECK= "attend_check";
// CSS样式：清除
Constants.CLS_CLEAR = "clear";
// CSS样式：审核记录查询
Constants.CLS_RECORD_QUERY  = "record_query";
// CSS样式：审批查询
Constants.CLS_CHECK_QUERY= "check_query";
// CSS样式：退出
Constants.CLS_EXIT = "exit";
// CSS样式：销假
Constants.CLS_RESUME_LEAVE = "resume_leave";
// 部门考勤登记
Constants.KQ005_C_001 ="考勤数据已审核，已不可更新，是否查看？";
Constants.KQ005_C_002 ="考勤数据已存在，是否更新？";

//考勤标准维护
Constants.KQ002_I_001 = "请选择一个考勤部门！"
Constants.KQ002_C_001 = "确认保存时包含子部门吗？"

// 考勤权限设置
Constants.KQ003_E_001 = "该部门不是审核部门，不能添加子部门。"
Constants.KQ003_I_001 = "该操作将删除该部门下的所有子部门 确定要继续吗？"

// 考勤员审核
Constants.KQ006_I_001 ="本部门考勤已审核,不可修改！";
Constants.KQ006_I_002 ="下级部门（{0}）考勤未审核！";                                                                                                             

// 负责人审核
Constants.KQ007_I_001 = "考勤员还未对部门（{0}）进行审核！";
// 节假日维护
Constants.KQ010_E_001 = "请选择是周末的日期。";
Constants.KQ010_E_002 = "请选择非周末的日期。";
