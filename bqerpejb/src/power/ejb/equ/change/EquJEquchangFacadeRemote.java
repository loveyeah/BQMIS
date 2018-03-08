package power.ejb.equ.change;

import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJEquchangFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJEquchangFacadeRemote {
	
	public int save(EquJEquchang entity);

	public void delete(Long changeId);

	public EquJEquchang update(EquJEquchang entity);

	public EquJEquchang findById(Long id);
	public PageObject findChangeList(String workerCOde,String changeNo,String changeTitle,String flag,String entryIds,
			String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException;
	
	/**
	 * 根据部门编码查询部门名称
	 * @param deptCode
	 * @return
	 */
	public String getEquChangeDeptName(String deptCode);
	
	public int getMaxNo(String maxNo,String enterpriseCode);
	
	public void reportEquChange(Long equChangeId, Long actionId,String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType);
	
	public EquJEquchang approveEquChange(String equChangeId,String workflowType,String workerCode,
			String actionId,String eventIdentify, String approveText, String nextRoles, String enterpriseCode);
	
	public String getMajorNo(String special);

	public String getEquSpecialName(String specialCode);
	
	public String getEquChangeSourceName(String sourceCode,String enterpriseCode);
	
	public void saveEquJEquchangBackfill(EquJEquchangBackfill entity);
	
	public EquJEquchangBackfill findEquJEquchangBackfill(String changeNo) throws ParseException;
	public String getApplyManName(String applyMan);
	/**
	 * 根据设备编码查询异动信息
	 * @param equCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 *add by kzhang 20100921
	 */
	public PageObject findChangeListByEquCode(String equCode,String enterpriseCode,final int... rowStartIdxAndCount);

}