package power.ejb.manage.budget;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.CodeRepeatException;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * 部门预算编制
 * 
 * @author fyyang 090818
 */
@Remote
public interface CbmJBudgetMakeFacadeRemote {

	public CbmJBudgetMake save(CbmJBudgetMake entity);

	public void delete(CbmJBudgetMake entity);

	public CbmJBudgetMake update(CbmJBudgetMake entity);

	public CbmJBudgetMake findById(Long id);

	/**
	 * 通过，预算时间，预算部门和主题查找编制单信息
	 * 
	 * @param time
	 * @param centerId
	 * @param topicId
	 * @return
	 */
	public List<CbmJBudgetItemForm> findByTopicAndDept(String time,
			String deptCode, Long topicId);

	/**
	 * 部门预算编制上报 add by fyyang 090820
	 * 
	 * @param budgetMakeId
	 * @param workflowType
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void reportTo(Long budgetMakeId, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles, String eventIdentify);

	/**
	 * 预算编制审批列表
	 * 
	 * @param topicId
	 * @param centerId
	 * @param entryIds
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findMakeApproveList(String topicId, String centerId,
			String entryIds, int... rowStartIdxAndCount);

	/**
	 * 审批签字
	 * 
	 * @param budgetMakeId
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void MakeCommSign(Long budgetMakeId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify);

	/**
	 * 根据编制单id获得部门预算明细
	 * 
	 * @param budgetMakeId
	 * @return
	 */
	public List<CbmJBudgetItemForm> findMakeItemByMakeId(Long budgetMakeId);

	/**
	 * 通过部门，主题，时间获得一个实例的id列表 add by liuyi 090820
	 * 
	 * @param centerId
	 *            部门id
	 * @param topicId
	 *            主题id
	 * @param budgetTime
	 *            时间
	 * @param enterpriseCode
	 *            企业编码
	 * @return
	 */
	public List<Long> getInstance(Long centerId, Long topicId,
			String budgetTime, String enterpriseCode);
	/**
	 * 通过部门，主题，时间获得部门预算指标列表 add by ltong 20100504
	 * 
	 * @param centerId
	 *            部门id
	 * @param centerTopicId
	 *            预算部门主题id
	 * @param budgetTime
	 *            时间
	 * @param enterpriseCode
	 *            企业编码
	 * @return
	 */
	public List<CbmJBudgetItemForm> findDeptTopicItemList(String centerTopicId,
			String centerId, String budgetTime, String enterpriseCode);
	
	/**
	 * 现金预算编制依据明细查询
	 * add by drdu 20100702
	 * @param time
	 * @param deptCode
	 * @param topicId
	 * @return
	 */
	public PageObject findBudgetBasisList(String time,String deptCode, Long topicId);
}