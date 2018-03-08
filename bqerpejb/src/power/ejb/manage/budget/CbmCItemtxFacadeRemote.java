package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for CbmCItemtxFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCItemtxFacadeRemote {

	public CbmCItemtx save(CbmCItemtx entity);

	public void delete(CbmCItemtx entity);

	public CbmCItemtx update(CbmCItemtx entity);

	public List<CbmCItemtx> findById(Long id);

	/**
	 * 
	 * @param systemId
	 *            体系id
	 * @return
	 */
	public CbmCItemtx findByItemtxId(Long systemId);

	/**
	 * 
	 * @param itemFCode
	 *            父指标体系
	 * @return
	 */
	public String creatCode(String itemFCode);

	/**
	 * 
	 * @param itemCode
	 * @param enterpriseCode
	 * @return
	 */
	public List<TreeNode> findBudgetTreeList(String year,String itemCode,
			String enterpriseCode);
	
	/**
	 * 物资费用来源指标树查询
	 * @param itemCode
	 * @param enterpriseCode
	 * @param deptCode
	 * @return
	 */
	public List<TreeNode> findBudgetTreeListForWz(String itemCode,
			String enterpriseCode,String deptCode,String mrDept,String flag);
	
	/**
	 * 根据指标编码查询指标名称
	 * @param itemCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getItemNameByCode(String itemCode,String enterpriseCode,String  deptCode);
	
	public List findBudgetCount(String year,String id);
	
	public PageObject findLlFeeDetail(String itemCode, String startTime,String endTime,String wlName,int start,int limit);
	
	public PageObject findContractFeeDetail(String itemCode, String startTime,String endTime,String contractName,int start,int limit);
	
	public PageObject findWWFeeDetail(String itemId,String wwName,String approver, String startTime,String endTime,int start,int limit);
	
	public PageObject findZbFeeDetail(String itemId,String reportBy , String startTime,String endTime,int start,int limit);
}