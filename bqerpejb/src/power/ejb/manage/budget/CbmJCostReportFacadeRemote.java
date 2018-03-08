package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for CbmJCostReportFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmJCostReportFacadeRemote {
	/**
	 * 增加费用报销单信息
	 * @param entity
	 * @return CbmJCostReport
	 */
	public CbmJCostReport save(CbmJCostReport entity);
	/**
	 * 删除费用报销单信息
	 * @param ids
	 */
	public void delete(String ids);
	/**
	 * 保存费用报销单修改
	 * @param entity
	 * @return CbmJCostReport
	 */
	public CbmJCostReport update(CbmJCostReport entity);
	/**
	 * 按照费用报销单Id查询
	 * @param id
	 * @return CbmJCostReport
	 */
	public CbmJCostReport findById(Long id);
	/**
	 * 费用报销单查询
	 * @param flag 是否审批 1 审批  2费用报销单查询
	 * @param startDate	开始时间
	 * @param endDate 结束时间
	 * @param reportorName 报销人名称模糊
	 * @param reportUse 用途模糊
	 * @param status 工作流状态
	 * @param deptId 当前登录人部门Id
	 * @param entryIds 工作流实例号
	 * @param enterpriseCode 企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findAll(String flag,String startDate,String endDate,String reportorName,String reportUse,String status,long deptId,String entryIds,String enterpriseCode,int... rowStartIdxAndCount);
	/**
	 * 费用报销单审批
	 * @param applyId 费用报销单Id
	 * @param entryId 
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void expRApprove(String applyId,String entryId,String workerCode,String actionId,String approveText,String nextRoles,String eventIdentify);
	/**
	 * 费用报销单上报
	 * @param checkId
	 * @param workercode
	 * @param actionId
	 * @param approveText
	 * @param workFlowCode
	 */
	public void endCheckReportTo(String checkId, String workercode,String actionId, String approveText,String workFlowCode);
	/**
	 * 费用报销查询列表
	 * @param fuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * add by yfyu 20100910
	 */
	
	public PageObject getCostReportList(String fuzzy, String enterpriseCode,String workerCode,  int... rowStartIdxAndCount);
}