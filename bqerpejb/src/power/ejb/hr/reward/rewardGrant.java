package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.hr.train.HrJOuttrainDetail;

/**
 * Remote interface for HrJRewardGrantFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface rewardGrant {
	// 主表
	public HrJRewardGrant save(HrJRewardGrant entity);

	public void delete(String grantId);

	public HrJRewardGrant update(HrJRewardGrant entity);

	public HrJRewardGrant findById(Long id);

	public List<HrJRewardGrant> findAll();

	/**
	 * 月奖查询
	 * 
	 * @param month
	 * @param deptId
	 * @param groupId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getRewardGrandList(String month, String deptId, String groupId,
			String fillBy, String enterpriseCode);

	// 明细
	public void saveOrUpdateRewardDetail(List<HrJRewardGrantDetail> addList,
			List<HrJRewardGrantDetail> updateList, String ids);

	public void deleteDetail(String grantId);

	public HrJRewardGrantDetail updateDetail(HrJRewardGrantDetail entity);

	public HrJRewardGrantDetail findByDetailId(Long id);

	public List<HrJRewardGrantDetail> findAllDetail();

	@SuppressWarnings("unchecked")
	public List getRewardGrantDetailList(String grandId);

	// 公共
	// 初始化记录
	@SuppressWarnings("unchecked")
	public List<HrJRewardGrant> getInintRewardGrand(String deptId,
			String grantMonth, String groupId);

	// 部门
	@SuppressWarnings("unchecked")
	public List getInitRewardGrantDept(String deptId);

	/**
	 * 取得班组列表
	 * 
	 * @param deptId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getGroupNameList(String deptId, String enterpriseCode);

	/**
	 * 取员工系数
	 * 
	 * @param empId
	 * @param grandId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getRewardMonthAward(String empId, String grandId);

	/**
	 * 取主表最大时间
	 * 
	 * @return
	 */
	public String getMaxGarntMonth();

	/**
	 * 取当前时间，当前部门下的班组
	 * 
	 * @param monthDate
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getApproveGroup(String monthDate, String deptId,
			String enterpriseCode);

	/**
	 * 取当前时间，当前部门下的待审批列表
	 * 
	 * @param month
	 * @param deptId
	 * @param groupId
	 * @param workFlowState
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getApproveRewardGrandList(String month, String deptId,
			String groupId, String flag, String workFlowState, String enterpriseCode);

	/**
	 * 汇总，审批
	 * 
	 * @param monthDate
	 * @param deptId
	 * @param workFlowState
	 * @param enterpriseCode
	 * @return
	 */
	public List<HrJRewardGrant> rewardApprove(String monthDate, String deptId,String groupId,String roleId,
			String workFlowState, String enterpriseCode);

	/**
	 * 取该部门下应有人数
	 * 
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	public String getDeptPeopleNum(String depiId, String enterpriseCode);

	/**
	 * 取所有一级部门
	 * 
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getAllFirstDept(String enterpriseCode);

	/**
	 * 取部门月奖金额
	 * 
	 * @param enterpriseCode
	 * @return
	 */
	public String getMonthRewardNum(String monthDate, String deptId,
			String enterpriseCode);

	
	/**
	 * 根据当前登陆人workerID等到对应角色ID
	 * @param workerID
	 * @param enterpriseCode
	 * @return
	 */
	public List findRoleIDByWorkerId(Long workerID,String enterpriseCode);
}