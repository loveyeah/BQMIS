package power.ejb.hr.reward;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJMonthRewardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJMonthRewardFacadeRemote {
	
	/**
	 * 保存月奖发放主记录并判断是否需要保存明细记录
	 * @param entity 主记录
	 * @param DatailIsNull 是否保存明细记录
	 */
	public HrJMonthReward save(HrJMonthReward entity, Boolean detailIsNull) throws CodeRepeatException ;

	public void delete(String rewardId);

	public HrJMonthReward update(HrJMonthReward entity);

	public HrJMonthReward findById(Long id);

	public List<HrJMonthReward> findByProperty(String propertyName, Object value);

	public PageObject findAll(String rewardMonth,String workFlowState);
	
	public HrJRewardApprove monthRewardReport(String rewardId);
	
	/**
	 * 月奖发放审批方法 add by sychen 20100830
	 * @param rewardId
	 */
	public void appvoveMonthAward(String rewardId);
	
}