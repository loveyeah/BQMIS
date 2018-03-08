package power.ejb.hr.reward;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJBigRewardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJBigRewardFacadeRemote {
	
	public Long save(HrJBigReward entity) throws CodeRepeatException;

	
	public void delete(HrJBigReward entity);

	
	public HrJBigReward update(HrJBigReward entity);

	public HrJBigReward findById(Long id);

	
	/**查询所有大奖发放信息
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getBigReward(String workflowStatus ,String rewardMonth, String enterPriseCode,final int... rowStartIdxAndCount);
	
	
	
	  
	/**删除一条大奖记录
	 * @param id
	 */
	public void delBigReward (Long id);
	
	
	/**一条大奖主记录上报
	 * @param mainId
	 * @param actionId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 * @param workflowType
	 */
	public HrJRewardApprove rewardReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) ;
	
	/**
	 * 大奖发放审批 add by sychen 20100830
	 * @param bigRewardId
	 */	
	public void appvoveBigAward(String bigRewardId);
}