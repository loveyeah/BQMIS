package power.ejb.run.protectinoutapply;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.protectinoutapply.form.ProAppInfoForm;
import power.ejb.run.protectinoutapply.form.ProtectInOutApplyInfo;

/**
 * Remote interface for RunJProtectinoutapplyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJProtectinoutapplyFacadeRemote {
	
	/**
	 * 增加记录
	 * 
	 * @param entity
	 * @return
	 */
	public RunJProtectinoutapply save(RunJProtectinoutapply entity);

	/**
	 * 删除单条记录
	 * 
	 * @param id
	 */
	public void delete(Long id);
	/**
	 * 删除多条记录
	 * 
	 * @param ids
	 */
	public void deleteMulti(String ids);
	/**
	 * 修改记录
	 * 
	 * @param entity
	 * @return
	 */
	public RunJProtectinoutapply update(RunJProtectinoutapply entity);
	
	public RunJProtectinoutapply findById(Long id);

	public List<RunJProtectinoutapply> findByProperty(String propertyName,Object value, int... rowStartIdxAndCount);

	public List<RunJProtectinoutapply> findAll(int... rowStartIdxAndCount);
	
	public PageObject findList(String strWhere, final int... rowStartIdxAndCount);
	/**
	 * 通过申请单号查找信息
	 * 
	 * @param protectNo
	 * @param enterpriseCode
	 * @return
	 */
	public RunJProtectinoutapply findByProtectNo(String protectNo,String enterpriseCode);
	
	/**
	 * 审批列表查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param applyDeptId
	 * @param status
	 * @param entryIds
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 */
	public PageObject findApproveList(String startDate,String endDate,String applyDeptId,String status,String entryIds,String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 登记列表查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param applyDeptId
	 * @param status
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findRegisterList(String startDate,String endDate,String applyDeptId,String status,String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 查询列表
	 * 
	 * @param startDate
	 * @param endDate
	 * @param applyDeptId
	 * @param status
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findListForQuery(String startDate,String endDate,String applyDeptId,String status,String enterpriseCode, final int... rowStartIdxAndCount);
	
	public PageObject findByIsin(String startDate,String endDate,String applyDeptId,String status,String enterpriseCode, String isIn,final int... rowStartIdxAndCount );
	
	public ProAppInfoForm findByNo(String protectNo,String enterpriseCode);
}