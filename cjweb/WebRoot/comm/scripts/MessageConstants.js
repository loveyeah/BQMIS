// 定义Message共通常量
// 2009/01/13 hrxie Create

var MessageConstants = {};

// 共通message
MessageConstants.COM_C_001 = "&nbsp&nbsp&nbsp确认要保存吗？&nbsp&nbsp&nbsp"                                                
MessageConstants.COM_C_002 = "&nbsp&nbsp&nbsp确认要删除吗？&nbsp&nbsp&nbsp"                                                
MessageConstants.COM_C_003 = "保存当前画面变更内容吗？"                                      
MessageConstants.COM_C_004 = "放弃已修改的内容吗？"
MessageConstants.COM_C_005 = "&nbsp&nbsp&nbsp确认要取消吗？&nbsp&nbsp&nbsp"
MessageConstants.COM_C_006 = "&nbsp&nbsp&nbsp确认要上报吗？&nbsp&nbsp&nbsp"
MessageConstants.COM_C_007 = "确认要导出数据吗？"
MessageConstants.COM_I_001 = "&nbsp&nbsp&nbsp请选择一行。&nbsp&nbsp&nbsp"                                                  
MessageConstants.COM_I_002 = "该当数据已经被其他的用户更改，请重新刷新页面。"
MessageConstants.COM_I_003 = "没有检索到相应的数据，请确认。"                                
MessageConstants.COM_I_004 = "&nbsp&nbsp&nbsp保存成功。&nbsp&nbsp&nbsp"  
MessageConstants.COM_I_005 = "&nbsp&nbsp&nbsp删除成功。&nbsp&nbsp&nbsp"
MessageConstants.COM_I_006 = "画面数据未修改。"
MessageConstants.COM_I_007 = "&nbsp&nbsp&nbsp上报成功。&nbsp&nbsp&nbsp"
MessageConstants.COM_I_010 = "请先保存数据。"
MessageConstants.COM_I_013 = "不存在任何{0}，请确认！"
MessageConstants.COM_E_001 = "正在结账，此业务无法进行。"                                    
MessageConstants.COM_E_002 = "{0}不能为空，请输入。"                                         
MessageConstants.COM_E_003 = "{0}不能为空，请选择。"                                         
MessageConstants.COM_E_004 = "{0}不能小于当前日期。"                                         
MessageConstants.COM_E_005 = "{0}必须大于{1}。"                                              
MessageConstants.COM_E_006 = "{0}必须小于{1}。"                                              
MessageConstants.COM_E_007 = "{0}已存在。请重新输入。"                                       
MessageConstants.COM_E_008 = "该数据已经不可使用，请重新选择。"                              
MessageConstants.COM_E_009 = "{0}不能在{1}之后。请确认。"                                    
MessageConstants.COM_E_010 = "{0}在数据库中不存在，请先保存。"                               
MessageConstants.COM_E_011 = "没有打印数据，请确认。"  
MessageConstants.COM_E_012 = "{0}不能为负数。" 
MessageConstants.COM_E_013 = "{0}必须大于0。"
MessageConstants.COM_E_014 = "操作数据库过程中异常终了。"   
MessageConstants.COM_E_015 = "他人使用中。"  
MessageConstants.COM_E_016 = "请选择一行。"
MessageConstants.COM_E_019 = "{0}必须是正整数。"
MessageConstants.COM_E_021 = "{0}不符合电话入力规则，请重新输入。"
MessageConstants.COM_E_022 = "处理文件过程中发生异常。"
MessageConstants.COM_E_023 = "格式化日期过程中发生异常。"
MessageConstants.COM_E_024 = "请确认上传文件路径是否正确!"
MessageConstants.COM_W_002= "数据未保存，是否现在保存？" 
MessageConstants.COM_W_001 = "{0}的库存可能会超过最大库存，确认{1}？"
MessageConstants.COM_W_002 = "数据未保存，是否现在保存？"




// 出现未知错误
MessageConstants.UNKNOWN_ERR="出现未知错误.";
// 用户验证失败
MessageConstants.USER_CHECK_ERROR = "用户名或密码输入错误";
// 系统提示信息
MessageConstants.SYS_REMIND_MSG="提示";
// “错误”message提示标题
MessageConstants.SYS_ERROR_MSG="错误";
// “确认”message提示标题
MessageConstants.SYS_CONFIRM_MSG="确认";
// 请选择其中一项进行编辑！
MessageConstants.SELECT_COMPLEX_MSG="请选择其中一项进行编辑！";
// 请先选择要编辑的行！
MessageConstants.SELECT_NULL_UPDATE_MSG="请先选择要编辑的行！";
// 请选择要删除的记录！
MessageConstants.SELECT_NULL_DEL_MSG="请选择要删除的记录！";
//操作出现失败
MessageConstants.OPERATE_ERROR_MSG="操作失败,请联系管理员!";
// 删除成功！
MessageConstants.DEL_SUCCESS="&nbsp&nbsp&nbsp删除成功！&nbsp&nbsp&nbsp";
// 删除时出现未知错误.
MessageConstants.DEL_ERROR="删除时出现未知错误.";
// 显示第 {0} 条到 {1} 条记录，一共 {2} 条
MessageConstants.DISPLAY_MSG="显示第 {0} 条到 {1} 条记录，一共 {2} 条";
// 没有记录
MessageConstants.EMPTY_MSG="没有记录";
// 请先选择工作负责人
MessageConstants.SELECT_CHARGE_BY_MSG = "请先选择工作负责人";
// 您确认将要填写的为[紧急工作票]吗？
// 紧急工作票将不通过[工作票签发人]，
// 而直接发给[运行紧急票接收	人]。
MessageConstants.IS_EMERGENCY = "您确认将要填	写的为[紧急工作票]吗？紧急工作票将不通过[工作票签发人]，而直接发给[运行紧急票接收人]。";
// 删除提示信息
MessageConstants.DelMsg = "确认删除选择的记录？";
MessageConstants.DEL_MSG_1 = '是否确定删除{0}为{1}的记录？';
// 上报成功！
MessageConstants.REPORT_SUCCESS="&nbsp&nbsp&nbsp上报成功！&nbsp&nbsp&nbsp";
// 上传成功！
MessageConstants.UPLOAD_SUCCESS="&nbsp&nbsp&nbsp上传成功！&nbsp&nbsp&nbsp";
// 审批成功！
MessageConstants.APPROVE_SUCCESS="&nbsp&nbsp&nbsp审批成功！&nbsp&nbsp&nbsp";
// 请填写审批意见！
MessageConstants.INPUT_COMMENT="请填写审批意见！";
// 请选择要上报的记录！
MessageConstants.SELECT_NULL_REPORT_MSG="请选择要上报的记录！";
// 请选择要浏览的记录！
MessageConstants.SELECT_NULL_BIRT_MSG="请选择要浏览的记录！";
// 请选择要存档的记录
MessageConstants.SELECT_NULL_SAVEFILE_MSG="请选择要存档的记录!";
// 确定存档选择的记录
MessageConstants.SAVEFILE_MSG="确定存档选择的记录?";
// 存档成功
MessageConstants.SAVEFILE_SUCCESS="&nbsp&nbsp&nbsp存档成功！&nbsp&nbsp&nbsp";
// 请选择其中一项进行编辑！
MessageConstants.SELECT_COMPLEX_VIEW_MSG="请选择其中一行预览！";
// 请先选择要编辑的行！
MessageConstants.SELECT_NULL_VIEW_MSG="请先选择要预览的行！";

//行政管理用message
MessageConstants.AM005_E_001 = "价格没有变化，请确认！"
MessageConstants.AW003_C_001 = "确认要改变周期类别吗？"
MessageConstants.AW003_E_001 = "周期类别为{0}的周期号不合法，请重新输入。"
MessageConstants.AW003_E_002 = "周期号重复，请确认！" 
MessageConstants.AW003_E_003 = "当前周期类别下，周期明细已达到最大限制条数。" 
MessageConstants.AD003_C_001 = "您没有选择菜谱，是否重新选择？"
MessageConstants.AD003_C_002 = "是否放弃当前菜谱选择？"
MessageConstants.AC001_E_001 = "请选择格式为.bmp文件。"
MessageConstants.AC001_E_002 = "该人员已经拥有个性签名，请确认！"
MessageConstants.AC001_C_001 = "是否替换个性签名？"
MessageConstants.AC001_C_002 = "确定上传个性签名吗？"
MessageConstants.AR001_C_001 = "是否重新选择物品？";
MessageConstants.AR001_E_001 = "请选择物品。";
MessageConstants.AS002_C_001 = "该申请尚未保存，请先保存。";
MessageConstants.AW001_E_001 = "该人员已拥有其他权限，请确认！";
MessageConstants.AV001_I_001 = "本维修项目没有详细信息。";
MessageConstants.AV007_E_002 = "费用项目数已达到最大值，请确认！";
MessageConstants.AV004_E_001 = "发车里程大于收车里程！";
// 追加开始 追加人：柴浩
MessageConstants.AW006_I_001 = "当前用户无权限，请进行权限设置！";
// 追加结束 追加人：柴浩
//维修项目维护
// 追加开始 追加人：chenshoujiang
MessageConstants.AV010_C_001 = "删除此费用类别会删除此费用类别下的所有项目,确定删除?";
/// 追加结束 追加人：chenshoujiang
// 值长审核
MessageConstants.AD005_C_001 = "确认要退回订单号为&nbsp{0}&nbsp的订单吗？";
MessageConstants.AD005_C_002 = "订单号为&nbsp{0}&nbsp的订单退回成功！";
MessageConstants.AD005_C_003 = "确认订单号为&nbsp{0}&nbsp的订单通过审核吗？";
MessageConstants.AD005_C_004 = "订单号为&nbsp{0}&nbsp的订单审核成功！";
// 内部申请签报
MessageConstants.AS002_C_001 = "该申请尚未保存，请先保存。";
MessageConstants.AS002_E_001 = "您输入的抄送人员有重复！";
// 个人 订餐
MessageConstants.AD004_E_001 = "您已经点过{0}的{1}"
MessageConstants.AD004_C_001 = "您的选择还没有保存，确认要放弃吗？"
