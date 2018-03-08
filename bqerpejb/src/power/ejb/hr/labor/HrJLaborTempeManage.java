package power.ejb.hr.labor;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJWorkresume;

/**
 * 
 * @author sychen 20100703
 *
 */

@Remote
public interface HrJLaborTempeManage {

	/**
	 * 全厂高温,中温，低温人员金额统计表通过明细ID查明细表信息
	 * @param id
	 * @return
	 */
	public HrJLaborTempeDetail findByDetailId (Long id);
	
	/**
	 * 全厂高温,中温，低温人员金额统计表通过主表ID查主表信息
	 * @param id
	 * @return
	 */
	public HrJLaborTempe findByMainId(Long id) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表查询主表ID
	 * @param tempeMonth
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getHrJLaborTempeMainId(String tempeMonth, String workerCode,
			String enterpriseCode) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表查询当前记录工作流状态
	 * @param tempeMonth
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getHrJLaborTempeStatus(String tempeMonth, String workerCode,
			String enterpriseCode) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表主表保存方法
	 * @param entity
	 * @param flag
	 * @param workFlowType
	 * @param workerCode
	 * @return
	 */
	public HrJLaborTempe save(HrJLaborTempe entity,String flag,String workFlowType,String workerCode) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表主表修改方法
	 * @param entity
	 * @return
	 */
	public HrJLaborTempe update(HrJLaborTempe entity) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表明细表保存方法
	 * @param addList
	 */
	public void saveDetail(List<HrJLaborTempeDetail> addList) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表明细表删除方法
	 * @param ids
	 * @return
	 */
	public void deleteDetail(String ids);
	
	/**
	 * 全厂高温,中温，低温人员金额统计表明细表修改方法
	 * @param updateList
	 */
	public void updateDetail(List<HrJLaborTempeDetail> updateList);
	
	/**
	 * 全厂高温,中温，低温人员金额统计表上报方法
	 * @param tempeMainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param nextRolePs
	 * @param workflowType
	 */
	public void reportHrJLaborTempe(Long tempeMainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表查询列表
	 * @param flag
	 * @param tempeMonth
	 * @param entryIds
	 * @param workerCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getHrJLaborTempeList(String flag,String tempeMonth,String entryIds,String workerCode
			,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 全厂高温,中温，低温人员金额统计表审批方法
	 * @param tempeMainId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param eventIdentify
	 * @param approveText
	 * @param nextRoles
	 * @param stepId
	 * @param enterpriseCode
	 * @return
	 */
	public HrJLaborTempe hrJLaborTempeApprove(String tempeMainId,String workflowType,
			String workerCode,String actionId,String eventIdentify, String approveText, String nextRoles,
			String stepId, String enterpriseCode) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表主表信息
	 * @param tempeMonth
	 * @param entryIds
	 * @param enterpriseCode
	 * @return
	 */
	public List getLaborTempeInfo(String tempeMonth,String entryIds,String enterpriseCode) ;
	
	/**
	 * 全厂高温,中温，低温人员金额统计表录入模板导入
	 * @param laborTempeList
	 * @param tempeMonth
	 * @param workerCode
	 * @param enterpriseCode
	 */
	public void importLaborTempeRecords(List<HrJLaborTempeDetail> laborTempeList,String enterpriseCode)  ;
	
	/**查询最近日期的待审批信息
	 * @param enterpriseCode
	 * @return
	 */
	public String  getTempeApproveInfo(String enterpriseCode);//add by wpzhu 20100716
}
