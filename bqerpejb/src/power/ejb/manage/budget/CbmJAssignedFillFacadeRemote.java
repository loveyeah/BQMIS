package power.ejb.manage.budget;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for CbmJAssignedFillFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmJAssignedFillFacadeRemote {
	
	public CbmJAssignedFill save(CbmJAssignedFill entity);

	
	public void delete(Long id);

	
	public CbmJAssignedFill update(CbmJAssignedFill entity);

	public CbmJAssignedFill findById(Long id);

	
	public List<CbmJAssignedFill> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	
	public List<CbmJAssignedFill> findAll(int... rowStartIdxAndCount);
	
	public PageObject findAssignedFillList(String workerCode,String enterpriseCode,String assignName,final int... rowStartIdxAndCount) throws ParseException;


	public void deleteMulti(String ids);


	public void reportTo(String assignId, String flowCode, String workerCode,
			long actionId);
	
	public PageObject findAssignedFillApproveList(String workerCode,String enterpriseCode,String assignName,String entryIds,final int... rowStartIdxAndCount) throws ParseException;


	public void approveSign(String assignId, String approveText,
			String workflowNo, String workerCode, Long actionId,
			String responseDate, String nextRoles, String eventIdentify);


	public PageObject findAssignedFillListall(String workerCode,String enterpriseCode,String assignName,String applyBy, final int... rowStartIdxAndCount) throws ParseException;


	public List<TreeNode> getItemIdTree(String itemCode,Long deptId,String budgetTime,String itemType);


	public Long getCurrentItemFinanceLeft(Long itemId,String budgetTime);
}