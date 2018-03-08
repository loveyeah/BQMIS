package power.ejb.hr.labor;

import java.util.List;
import javax.ejb.Remote;

/**
 * 一次性劳保发卡统计表录入
 * 
 * @author fyyang 20100622
 */
@Remote
public interface HrJLaborSendcardFacadeRemote {
	public void  sendCardReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) ;
	/**
	 * 
	 * @param entity
	 * @return
	 */
	public Long saveDetail(HrJLaborSendcard entity,String enterpriseCode,String workCode);
	public HrJLaborSendcard save(HrJLaborSendcard entity);

	/**
	 * 
	 * @param ids
	 */
	public void delete(String ids);

    /**
     * 
     * @param entity
     * @return
     */	
	public HrJLaborSendcard update(HrJLaborSendcard entity);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public HrJLaborSendcard findById(Long id);
	/**根据年度和季度查询主表有没有此条记录
	 * @param sendYear
	 * @param sendKind
	 * @param enterpriseCode
	 * @return
	 */
	public HrJLaborSendcard findMainInfo(String  sendYear ,String sendKind,String enterpriseCode);
	
	
	/**一次性劳保发卡统计表主表数据审批
	 * @param mainId
	 * @param actionId
	 * @param entryId
	 * @param workerCode
	 * @param approveText
	 * @param nextRoles
	 */
	
	public void sendCardApprove(Long mainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles);
	/**
	 * 查询主记录的审批状态
	 * @param sendYear
	 * @param sendKind
	 * @param entryBy
	 * @param enterpriseCode
	 * @return
	 */
	public String  getSendCardStatus(String sendYear,String sendKind,String entryBy,String enterpriseCode);
	
	/**查询当前登录人可以审批的记录列表
	 * @param sendYear
	 * @param sendKind
	 * @param entryIds
	 * @param enterpriseCode
	 * @return
	 */
	public List  getApprovelist(String sendYear,String sendKind,String entryIds,String enterpriseCode);
	

}