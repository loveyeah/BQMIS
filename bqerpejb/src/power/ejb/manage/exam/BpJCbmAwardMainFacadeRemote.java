package power.ejb.manage.exam;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.exam.form.CashModel;

/**
 * Remote interface for BpJCbmAwardMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJCbmAwardMainFacadeRemote {
	 
	public void save(BpJCbmAwardMain entity);
 
	public void delete(BpJCbmAwardMain entity);

	 
	public BpJCbmAwardMain update(BpJCbmAwardMain entity); 
	 
	  
	public List<BpJCbmAwardMain> findAll(int... rowStartIdxAndCount);

	public List<CashModel> getAwardValueList(String datetime);

	/**
	 * 保存
	 * modify by drdu 091201 新加参数 workCode
	 * @param updatelist
	 * @param datetime
	 * @param enterpriseCode
	 * @param workCode
	 * @return
	 */
	public boolean saveAwardValuelist(List<BpJCbmAwardDetail> updatelist,String datetime,String enterpriseCode,String workCode);
	public Long getMaxAwardId();
	
	/**
	 * 根据月份查找奖金申报审批列表
	 * add by drdu 091130
	 * @param enterpriseCode
	 * @param month
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findApproveList(String enterpriseCode,String month,final int... rowStartIdxAndCount);
	
	/**
	 * 奖金申报上报
	 * add by drdu 091130
	 * @param month
	 * @param flowCode
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String month,String flowCode,String workerCode,Long actionId);
	/**
	 * 奖金申报审批
	 * add by drdu 091130
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param date
	 */
	public void awardApproveSign(String declareId,Long entryId, String workerCode,
			Long actionId, String approveText, String nextRoles,String eventIdentify, String date,String responseDate);
	
	
	public PageObject getAllCahsResiter(String month,int... rowStartIdxAndCount);
}