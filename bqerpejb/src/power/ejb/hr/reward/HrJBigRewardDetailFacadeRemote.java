package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJBigRewardDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJBigRewardDetailFacadeRemote {

	public void save(HrJBigRewardDetail entity);

	public void delete(HrJBigRewardDetail entity);

	public HrJBigRewardDetail update(HrJBigRewardDetail entity);

	public HrJBigRewardDetail findById(Long id);

	public PageObject getAllawardName(String yearMonth);

	public PageObject getBigRewardDetail(Long mainId,String workFlowState);

	public void saveBigRewardDetail(List<HrJBigRewardDetail> addList,
			List<HrJBigRewardDetail> updateList) throws CodeRepeatException;

	public void deleteBigRewardDetail(String ids);

	public List<HrJRewardApprove> rewardDetailReport(String detailIds, String rewardId);

	public boolean IsallReport(Long id);

	public PageObject getRewardNotice(String rewardName);
	
	public void buildData(String rewardId, String rewardMonth,String bigRewardBase);
	
	public PageObject findAllByBigAwardName(String bigRewardId,String workFlowState, String deptId);
	
	public List<HrJRewardApprove> administratorApprove(String detailId,String actionId,Long deptId);

	public PageObject queryBigRewardDetailByRewardMonth(String rewardMonth);
	
	/**
	 *  add by fyyang 20100724
	 * 查询大奖部门明细信息
	 * @param rewardId 大奖发放主表id
	 * @param deptId 部门id
	 * @return
	 */
	public PageObject queryDetailBigRewardByDept(Long rewardId,Long deptId);
	
	/**
	 * 查询该大奖下的所有部门
	 * add by fyyang 20100724
	 * @param rewardId 大奖发放主表id
	 * @return
	 */
	public List queryRewardDeptList(Long rewardId);
}