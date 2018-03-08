package power.ejb.productiontec.castBackProtect;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface protectMange 
{
	public  PageObject  getProtectApply(String protectType,String approve,String status,String inOut,String enterpriseCode,String entryIds,
			String applyBy,final int... rowStartIdxAndCount);
	
	public boolean save(PtJProtectApply entity);
	public PtJProtectApply findById(Long  id);
	public PtJProtectApply update(PtJProtectApply entity) ;
	public void deleteProtect(String ids);
	public void ProtectReport(String applyID,String flowCode,String workerCode,Long actionId,String approveText,
			String nextRoles, String eventIdentify);
	//by ghzhou 2010-07-31 获得投退保护单流水号
	public int getMaxId(String type,String enterpriseCode);
	/*public void ProtectReport(Long applyID, Long actionId,
	String workerCode, String approveText, String nextRoles,
	String workflowType);*/
	
	}