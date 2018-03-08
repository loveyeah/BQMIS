package power.ejb.productiontec.castBackProtect;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJProtectApplyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJProtectApplyFacadeRemote {
	
	/**
	 *  保存投退保护审批信息
	 * @param entity
	 * @param applyId
	 * @param status
	 * @param entryId
	 */
	public void saveCastBackProtect(PtJProtectApply entity,String applyId,String status,String entryId);
	
	/**
	 * 通过ID查找投退保护信息
	 * @param applyId
	 * @return
	 */
	public PtJProtectApply findByCastBackProtectId(Long applyId);
	
	/**
	 * 投退保护审批查询列表信息
	 * @param inOut
	 * @param protectionType
	 * @param status
	 * @param entryIds
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getCastBackProtectApproveList(String inOut,String protectionType,String applyId, 
			String status, String entryIds,String enterpriseCode,int... rowStartIdxAndCount);
	
	/**
	 * 审批投退保护
	 * @param applyId
	 * @param workerCode
	 * @param entryId
	 * @param approveText
	 * @param actionId
	 * @param responseDate
	 * @param nextRoles
	 * @param eventIdentify
	 */
	public void approveCastBackProtect(Long applyId,String workerCode, String entryId, String approveText, Long actionId,
			String responseDate, String nextRoles,String nextRolePs,  String eventIdentify);
	
}