package power.ejb.workticket.business;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketInfo;
import power.ejb.workticket.form.WorkticketSafetyBeakOutModel;

/**
 * 工作票主记录管理
 *
 * @author 
 */
@Remote
public interface RunJWorkticketsFacadeRemote {
	/**
	 * 保存
	 * @param entity
	 * @return
	 */
	public RunJWorktickets save(RunJWorktickets entity);
	/**
	 * 删除
	 * @param workticketNo
	 */
	public void delete(String workticketNo);
	/**
	 * 修改
	 * @param entity
	 * @return RunJWorktickets
	 */
	public RunJWorktickets update(RunJWorktickets entity);
	/**
	 * 主键查找
	 * @param id
	 * @return RunJWorktickets
	 */
	public RunJWorktickets findById(String id); 

	 
	
	 
//	/**
//	 * 增删改工作票内容时回写主表工作票内容字段
//	 * modify by fyyang 090311 不需回写
//	 * @param workticketNo
//	 *            工作票号
//	 */
//	public void updateWorkticketContent(String workticketNo);
	
	
	
	/**
	 * 生成工作票号
	 * modify by fyyang 090317
	 */
	public String createWorkticketNo(String enterpriseCode,String workticketType, String deptCode,Long fireLevelId);
	/**
	 * 生成工作票号
	 * modify by fyyang 090317
	 */
	public String createStandardWorkticketNo(String enterpriseCode,String workticketType, Long fireLevelId);
	
	/**
	 * 复制维护的安措到工作票的安措内容表
	 * modify by fyyang 安措内容表已不使用
	 * @param enterpriseCode
	 *            企业编码
	 * @param workticketTypeCode
	 *            工作票类型编码
	 * @param workticketNo
	 *            工作票号
	 */
//	public void copySafetyForWorkticket(String enterpriseCode,
//			String workticketTypeCode, String workticketNo);

	/**
	 * 根据主票内容生成动火票内容(根据现有票生成新票的内容)
	 * 
	 * @param enterpriseCode
	 * @param mainTicketNo
	 * @param fireTicketNo
	 * @param createMan
	 * @param createDate
	 */
	public void copyMainTicketContent(String enterpriseCode,
			String mainTicketNo, String fireTicketNo, String createMan,
			String createDate);

	/**
	 * copy现有票的安措明细到新票
	 * 
	 * @param oldWorkticketNo
	 * @param newWorkTicketNo
	 * @param createMan
	 * @param createDate
	 */
	public void copyWorkticketSafetyDetail(String oldWorkticketNo,
			String newWorkTicketNo, String createMan, String createDate);

	/**
	 * 复制现有票的安措内容到新票
	 * modify by fyyang 090311 安措不回写
	 * @param oldWorkticketNo
	 * @param newWorkticketNo
	 */
//	public void copySafetyContentByOld(String oldWorkticketNo,
//			String newWorkticketNo);

	/**
	 * 将新工作票的所属系统或机组、接收专业以及工作条件改为现有工作票的对应信息
	 * 
	 * @param oldWorkticketNo
	 * @param newWorkticketNo
	 */
	public void updateBlockAndConditionByOld(String oldWorkticketNo,
			String newWorkticketNo);

	/**
	 * 修改工作票安措内容表的安措内容（由安措明细表中数据拼成）
	 * modify by fyyang 090311 安措内容不回写更新
	 * @param workticketNo
	 * @param safetyCode
	 */
//	public void updateSafetyContent(String workticketNo, String safetyCode);
	
	/**
	 * 工作班组人员变动时回写主表的人员及人数
	 * @param workticketNo
	 */
	public void updateWorkMemers(String workticketNo);
	
	/**
	 * 终结票预警查询
	 * @param workicketTypeCode 工作票类型
	 * @param runSpecialCode 运行专业
	 * @param repairSpecialCode 检修专业
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public List<WorkticketInfo> workticketEndWarn(String enterpriseCode,String workicketTypeCode,String runSpecialCode,String repairSpecialCode);
	
	/**
	 * copy危险点及控制措施
	 * @param workticketNo 工作票号
	 * @param dangerId 危险点内容Id
	 * @param workCode 工号
	 */
	public void copyDanger(String workticketNo,String dangerId,String workCode);
	
	/**
	 * 修改时修改危险点及控制措施
	 * @param workticketNo
	 * @param dangerId
	 * @param workCode
	 */
	public void updateDanger(String workticketNo,String dangerId,String workCode);
	
	/**
	 * 由标准票生成时copy危险点
	 */
	public void copyDangerByOldTicket(String oldWorkticketNo,String newWorkticketNo,String workCode);
	//----------------------------------jyan---------------------------------------
	/**
	 * 查询已终结的工作票列表 090417 用于由终结票生成新的标准票
	 * modify by fyyang 090513  增加工作内容查询条件
	 */
	public PageObject getEndWorkticketList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String equAttributeCode,String workticketContent,String workticketNo,
			int... rowStartIdxAndCount);
	/**
	 * 标准票查询
	 */
	public PageObject queryStandTicketList(String enterpriseCode,String newOrOld,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,String repairSpecailCode,
			String entryBy,String workticketContent,String workticketNo,
			int... rowStartIdxAndCount);
	/**
	 *用于由标准票生成页面的查询列表
	 *modify by fyyang 20100506 增加动火级别查询条件
	 */
	public PageObject getStandListForSelect(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			 String mainEquName,String newOrOld,String workticketContent,String workticketNo,String fireLevel,
			int... rowStartIdxAndCount);
	public PageObject getWorkticketMainList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,
			String repairSpecailCode,String deptId,String workticketContent,String workticketNO,
			int... rowStartIdxAndCount);
	public PageObject getWorkticketRelMainList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,
			int... rowStartIdxAndCount);
	/** 
	 * modify  by  fyyang 090729 增加动火级别查询条件
	 * 查询现有票列表
	 */
	
	public PageObject getWorkticketHisTicketList(String enterpriseCode,
			String sdate, String edate, String workticketTypeCode,
			String workticketStausId, String equAttributeCode,String workticketNo,String isStandard,String firelevel,String workticketContent,
			int... rowStartIdxAndCount);
	/**
	 * 工作票审批查询列表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 * @param equAttributeCode
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getWorkticketApproveList(String enterpriseCode,String sdate,
			String edate,String workticketTypeCode
			,String workticketStausId,String equAttributeCode,String entryIds ,String repairSpecailCode,String deptId,String workticketContent,String wticketNo,int... rowStartIdxAndCount);
	/**
	 * 工作票上报查询列表
	 * modify by fyyang 090310
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 * @param equAttributeCode
	 * @param isStandard
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getWorkticketReportList(String enterpriseCode,String sdate,
			String edate,String workticketTypeCode
			,String workticketStausId,String equAttributeCode,String isStandard,String workerCode,int... rowStartIdxAndCount);
	
	

	public WorkticketInfo queryWorkticket(String enterpriseCode,
			String workticketNo);
	
	/**
	 * 标准工作票审批查询列表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param workticketStausId
	 * @param equAttributeCode
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getStandardTicketApproveList(String enterpriseCode,String sdate,
			String edate,String workticketTypeCode
			,String workticketStausId,String equAttributeCode,String entryIds,String repairSpecialCode, int... rowStartIdxAndCount);
	
	
	//--------------------------------drdu-----------------------------------------
	/**
	 * 变更工作票负责人列表
	 * 
	 * @param enterpriseCode企业编码
	 * @param sdate开始时间
	 * @param edate结束时间
	 * @param workticketTypeCode工作票类型
	 * @param permissionDept运行专业
	 * @param repairSpecailCode检修专业
	 * @param chargeDept工作负责人部门
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryWorkticketetHis(String enterpriseCode,String sdate,
			String edate, String workticketTypeCode,String permissionDept,
			String repairSpecailCode,String chargeDept,final int... rowStartIdxAndCount);
	
	/**
	 * 	延期办理列表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param permissionDept
	 * @param chargeDept 
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryWorkticketForDelayList(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode,String permissionDept,
			String chargeDept,final int... rowStartIdxAndCount);
	/**
	 * 工作票交回,指定工作负责人
	 * add by LiuYingwen
	 * @param entity
	 * @param chargeDept
	 */
	public void workticketDealBack(RunJWorktickethis entity,String chargeDept);

	/**
	 * 工作票交回、恢复查询
	 * add by LiuYingwen
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param workticketTypeCode
	 * @param permissionDept
	 * @param repairSpecailCode
	 * @param chargeDept
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject queryWorkticketet(String enterpriseCode,String sdate,
			String edate, String workticketTypeCode,String permissionDept,
			String repairSpecailCode,String chargeDept,final int... rowStartIdxAndCount);
	
	/**
	 * 变更工作负责人
	 * modify by fyyang 090106 
	 * 增加参数负责人所在部门
	 * @param entity
	 * @param chargeDept
	 */
	public void changeWorkticketChargeBy(RunJWorktickethis entity,String chargeDept);
	/**
	 *取得工作票变更工作负责人名称
	 * @param workticketNo 工作票编号
	 * @return 
	 */
	public String[] getChangeChargeByName(String workticketNo);
	/**
	 * 工作票延期
	 * @param entity
	 */
	public void changeWorktickettime(RunJWorktickethis entity);
	
	/**
	 * 取得延期后的信息
	 * add by fyyang 090108
	 * @param workticketNo 工作票号
	 * @return WorkticketHisForPrint
	 */
	public WorkticketHisForPrint getWorktickeDelayInfo(String workticketNo);

	/**
	 * 安措拆除查询列表
	 * @param enterpriseCode企业编码
	 * @param sdate开始时间
	 * @param edate结束时间
	 * @param workticketTypeCode工作票类型
	 * @param workticketStausId工作票状态
	 * @param safetyExeStatusId安措拆除状态
	 * @param permissionDept运行专业
	 * @param sourceName来源
	 * @param repairSpecailCode检修专业
	 * @param equAttributeCode所属机组或系统
	 * @param fuzzy 按“工作票号”查询
	 * @param rowStartIdxAndCount动态分页
	 * @return
	 */
	public PageObject findSecurityMeasureForBreakOutList(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode, String workticketStausId,
			String permissionDept,String repairSpecailCode,
			String equAttributeCode,String fuzzy,String strSafetyExeStatusId,
			final int... rowStartIdxAndCount);
	/**
	 * 安措拆除详细列表
	 * @param enterpriseCode
	 * @param workticketNO
	 * @return
	 */
	public List<WorkticketSafetyBeakOutModel> findSecurityMeasureForBreakOutByNo(String enterpriseCode,String workticketNO);
	
	/**
	 * 拆除安措
	 * @param workticketNo
	 * @param map
	 * @param reason
	 * @param workerCode
	 */
	public void breakOutSecurityMeasure(String workticketNo,HashMap map, String reason,String workerCode);

	/**
	 * 安措未拆除原因列表
	 * @param workticketNo
	 * @return
	 */
	public List<RunJWorktickethis> findWorkticketHisList(String workticketNo);
	
	/**
	 * 紧急工作票补签   
	 * @param workticketNo 工作票编号
	 * @param approveBy  签发人
	 * @param approveDate签发时间
	 * @param approveStatus审批状态
	 * @param approveDept签发人所在部门
	 * @param fireticketFireman 动态 消防现场监护人
	 */
	public void addForEmergencyTicket(String workticketNo,String approveBy,Date approveDate,String approveStatus,String approveDept,String ...fireticketFireman);
	/**
	 * 紧急票补签列表
	 * @param enterpriseCode企业编码
	 * @param sdate开始时间
	 * @param edate结束时间
	 * @param workticketTypeCode工作票类型
	 * @param permissionDept运行专业
	 * @param chargeDept负责人所属部门
	 * @param rowStartIdxAndCount动态分页
	 * @return
	 */
	public PageObject queryaddForUrgentTicketList(String enterpriseCode, String sdate,
			String edate, String workticketTypeCode,String permissionDept,
			String chargeDept,final int... rowStartIdxAndCount);
	
	/**
	 * 创建一条新标准工作票并且删除旧标准工作票
	 * @param entity
	 * @param oldWorktickectNo
	 */
	public void createnewDeloldWorkTickect(RunJWorktickets entity,String oldWorktickectNo);
	
	/**
	 * add by liuyi 091116
	  * 生成工作票号
	  * @param enterpriseCode 工作票类型
	  * @param enterpriseChar 企业一位标识
	  * @param workticketType 工作票类型
	  * @param specialCode 专业编码
	  * @return
	  */
	public String createWorkticketNo(String enterpriseCode,
			String enterpriseChar, String workticketType, String specialCode);
	
	/**
	 *add by liuyi 091116 
	 * 
	 * 根据工作票号和企业编码查询一条工作票记录
	 * @param enterpriseCode
	 * @param workticketNo
	 * @return
	 */
	public List<WorkticketInfo> getWorkticketByWorkticketNo(String enterpriseCode,String workticketNo);

}