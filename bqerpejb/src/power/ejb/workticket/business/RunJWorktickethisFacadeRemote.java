package power.ejb.workticket.business;

import java.util.HashMap;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.workticket.form.workticketHisInfo;

/**
 * Remote interface for RunJWorktickethisFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJWorktickethisFacadeRemote {

	public void save(RunJWorktickethis entity);

	
	public void delete(RunJWorktickethis entity);

	
	public RunJWorktickethis update(RunJWorktickethis entity);

	public RunJWorktickethis findById(Long id);

	public List<RunJWorktickethis> findAll(int... rowStartIdxAndCount);
	
	/**
	 * add by fyyang 090327 for 工作票审批信息修改
	 * @param workticketNo
	 * @return
	 */
	public List<workticketHisInfo> findHisInfo(String workticketNo);
	public void updateApproveInfo(RunJWorktickethis endHisModel,RunJWorktickethis pemitHisModel,
			RunJWorktickethis changeChargeModel,RunJWorktickethis delayHisModel,
			RunJWorktickethis safetyExeHisModel,RunJWorktickethis dhModel);
}