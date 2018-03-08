package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrJRewardGrantFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJBigRewardGrantRemote {
	// 主表
	public HrJBigRewardGrant save(HrJBigRewardGrant entity);

	public void delete(String bigGrantId);

	public HrJBigRewardGrant update(HrJBigRewardGrant entity);

	public HrJBigRewardGrant findById(Long id);

	/**
	 * 月奖查询
	 * 
	 * @param month
	 * @param deptId
	 * @param groupId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBigRewardGrandList(String month, String fillBy,
			String enterpriseCode);

	// 明细
	public void deleteBigDetail(String bigGrantId);

	public void saveOrUpdateBigRewardDetail(
			List<HrJBigRewardGrantDetail> addList,
			List<HrJBigRewardGrantDetail> updateList, String ids);

	public boolean findByBigRewardDetail(String bigGrantId);

	@SuppressWarnings("unchecked")
	public List getBigRewardGrantDetailList(String bigGrantId,
			String enterpriseCode);

	// 公共
	// 初始化记录
	@SuppressWarnings("unchecked")
	public List<HrJBigRewardGrant> getInintBigRewardGrand(String deptId,
			String bigGrantMonth, String groupId);

	// 部门
	@SuppressWarnings("unchecked")
	public List getInitBigRewardGrantDept(String deptId);

	/**
	 * 取员工系数
	 * 
	 * @param empId
	 * @param grandId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBigRewardMonthAward(String empId, String bigGrantId);

	/**
	 * 取主表最大时间
	 * 
	 * @return
	 */
	public String getMaxBigGarntMonth();

	/**
	 * 取当前时间，当前部门下的班组
	 * 
	 * @param monthDate
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBigApproveGroup(String monthDate, String deptId,
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
	public List getApproveBigRewardGrandList(String month, String deptId,
			String groupId, String roleId, String workFlowState, String enterpriseCode,
			String bigAwardId);

	/**
	 * 汇总，审批
	 * 
	 * @param monthDate
	 * @param deptId
	 * @param workFlowState
	 * @param enterpriseCode
	 * @return
	 */
	public List<HrJBigRewardGrant> bigRewardApprove(String monthDate, String deptId,String roleId, 
			String workFlowState, String enterpriseCode, String bigAwardId);

	/**
	 * 取大奖名称
	 * 
	 * @param monthDate
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBigAwareNameList(String monthDate, String enterpriseCode,
			String deptId);

	/**
	 * 取大奖金额
	 * 
	 * @param monthDate
	 * @param enterpriseCode
	 * @return
	 */
	public String getBigRewardNum(String monthDate, String bigAwardId,
			String deptId, String enterpriseCode);

	/**
	 * 取当前时间，当前部门下的大奖名称
	 * 
	 * @param monthDate
	 * @param deptId
	 * @param enterpriseCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List getBigRewardApproveAwareList(String monthDate, String deptId,
			String groupId, String workFlowState, String enterpriseCode);

}