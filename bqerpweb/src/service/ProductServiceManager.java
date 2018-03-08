package service;

import power.ejb.webservice.run.ticketmanage.RunJTaskInformation;

public interface ProductServiceManager { 
	public void synchronizeCreateTicket(String wsAddr,RunJTaskInformation bean);
	public void synchronizeBeforeDestroyTask(String wsAddr,RunJTaskInformation bean);
	public void synchronizeDisuseTicket(String wsAddr,RunJTaskInformation bean);

	/**
	 * 两票系统创建（开票或重开票）一张票，修改系统中所对应的任务信息
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param TaskNo 任务单号
	 * @param TickeNo 两票票号
	 * @param IsStandardTicket 是否标准票
	 * @param IsWorkTicket 是否工作票
	 * @param TicketTypeID 两票类型ID
	 * @param TicketTypeName 两票类型名称
	 * @param WatchTypeID 值类别
	 * @param WatchTypeName 值类别名称
	 * @param MachineTypeID 机组类别ID
	 * @param MachineTypeName 机组类别名称
	 */
	public void CreateTicket(String wsAddr, String enterpriseCode,
			String operator, String TaskNo, String TickeNo,
			String IsStandardTicket, String IsWorkTicket, String TicketTypeID,
			String TicketTypeName, String WatchTypeID, String WatchTypeName,
			String MachineTypeID, String MachineTypeName);
	
	/**
	 * 两票系统废止一张票,清空原有两票传入的信息
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param TaskNo 任务单号
	 */
	public void DestroyTicket(String wsAddr, String enterpriseCode,
			String operator, String TaskNo);
	
	/**
	 * 两票系统终结一张票，修改系统中所对应的任务为终结
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param TaskNo 工作/操作任务单编号（不为空）
	 * @param DisuseTime 任务的终结时间
	 */
	public void DisuseTicket(String wsAddr, String enterpriseCode,
			String operator, String TaskNo, String DisuseTime);

	/**
	 * 两票系统终结一张票，修改系统中所对应的任务为终结
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param TaskNo 工作/操作任务单编号（不为空）
	 * @param DisuseTime 任务的终结时间
	 * @param BeginTime 工作票上工作开始时间
	 */
	public void DisuseTicketXT(String wsAddr, String enterpriseCode,
			String operator, String TaskNo, String DisuseTime, String BeginTime);
	
	/**
	 * 根据下达时间得到已作废的任务单编号
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param StrYear 查询的年份
	 * @param StrMonth 查询的月份
	 * @return String 任务单编号
	 */
	public String GetBlankOutTaskInfo(String wsAddr, String enterpriseCode,
			String operator, String StrYear, String StrMonth);
	
	/**
	 * 两票系统中工作票办理延期成功时修改计划完工时间
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param taskNo 工作/操作任务单编号（不为空）
	 * @param finishEndTime 计划完工时间，时间格式为:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public void ExtensionTicket(String wsAddr, String enterpriseCode,
			String operator, String taskNo, String finishEndTime);
	
	/**
	 * 两票系统中登记票“未执行”时，根据条件修改任务单状态为:申请作废状态/作废状态（未执行）
	 * @param wsAddr webService服务地址
	 * @param enterpriseCode 企业编码
	 * @param operator 操作人
	 * @param taskNo 工作/操作任务单编号（不为空）
	 * @param Flag 0:申请作废;1:直接作废
	 * @param Causes 作废原因 （不能为空）
	 * @return
	 */
	public void BeforeDestroyTask(String wsAddr, String enterpriseCode,
			String operator, String TaskNo, String Flag, String Causes);
}
