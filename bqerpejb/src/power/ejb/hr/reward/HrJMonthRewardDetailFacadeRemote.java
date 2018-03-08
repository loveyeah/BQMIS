package power.ejb.hr.reward;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJMonthRewardDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJMonthRewardDetailFacadeRemote {
	
	public void save(HrJMonthRewardDetail entity);

	public void delete(String detailIds);

	public HrJMonthRewardDetail update(HrJMonthRewardDetail entity);

	public HrJMonthRewardDetail findById(Long id);

	public List<HrJMonthRewardDetail> findByProperty(String propertyName,
			Object value);

	public PageObject findAllByRewardId(String rewardId,String workFlowState);
	
	public void saveOrUpdateDetailList(List<HrJMonthRewardDetail> addList, List<HrJMonthRewardDetail> updateList); 
	
	public List<HrJRewardApprove> monthRewardApprove(String detailIds,String rewardId);
	
	public PageObject findAllByRewardMonth(String rewardMonth,String workFlowState, String deptId);
	
	public List<HrJRewardApprove> administratorApprove(String detailId,String actionId,Long deptId);
	
	public int isHasShfit(String deptId);
	
	public PageObject queryMonthRewardDetailByRewardMonth(String rewardMonth);
	
	/**
	 * add by fyyang 20100724 查询月奖明细
	 * @param rewardId 月奖发放id
	 * @param deptId 部门id
	 * @return
	 */
	public PageObject findDetailMonthRewardByDept(Long rewardId,Long deptId,String month);
	
	/**
	 * add by fyyang 20100724 
	 * 查询该记录下的所有部门
	 * @param rewardId 月奖发放id
	 * @return
	 */
	public List getRewardDetailDeptList(Long rewardId);
	
	/**查询最大月份的数据  add by wpzhu 20100727
	 * @param workstatus
	 * @param deptId
	 * @return
	 */
	public String getMaxMonth(String workstatus,Long deptId);
	
	
	
}