package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrJRewardApproveFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJRewardApproveFacadeRemote {
	
	public void save(HrJRewardApprove entity);

	
	public void delete(HrJRewardApprove entity);

	
	public HrJRewardApprove update(HrJRewardApprove entity);

	public HrJRewardApprove findById(Long id);

	
	public List<HrJRewardApprove> findByProperty(String propertyName,
			Object value);

	
	public List<HrJRewardApprove> findAll(Long deptId, Long workerId);
	
	/**
	 * 大奖月奖发放
	 * modify by fyyang 20100731
	 * @param url
	 * @param deptId
	 * @return
	 */
	public String getNextSetpRolesTelephone(String url,String deptIds);

	/**
	 * 获得下一步审批人的手机号(大奖月奖上报)
	 * add by fyyang 2010-07-15
	 * @param url
	 * @param deptId
	 * @return
	 */
	public String getNextSetpRolesTelForReport(String url,Long deptId);
	
	/**
	 * add by fyyang 20100715
	 * @param ids
	 * @param flag
	 * @return
	 */
	public List<HrJRewardApprove> findListByDetailId(String ids,String flag);
}