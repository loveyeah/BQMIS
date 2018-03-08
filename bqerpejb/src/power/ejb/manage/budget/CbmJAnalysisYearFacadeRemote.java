package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 部门年度预算分析
 * 
 * @author liuyi 090814
 */
@Remote
public interface CbmJAnalysisYearFacadeRemote {
	/**
	 * 新增一条部门年度预算分析记录
	 */
	public void save(CbmJAnalysisYear entity);

	/**
	 * 删除一条部门年度预算分析记录
	 */
	public void delete(CbmJAnalysisYear entity);

	/**
	 * 删除一条或多条部门年度预算分析记录
	 */
	public void delete(String ids);

	/**
	 * 更新一条部门年度预算分析记录
	 */
	public CbmJAnalysisYear update(CbmJAnalysisYear entity);

	/**
	 * 通过id查找一条部门年度预算分析记录
	 * 
	 * @param id
	 * @return
	 */
	public CbmJAnalysisYear findById(Long id);

	/**
	 * Find all CbmJAnalysisYear entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmJAnalysisYear> all CbmJAnalysisYear entities
	 */
	public PageObject findAnalysisYearList(Long centerId, String dataTime,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void saveAnalysisYearModified(List<CbmJAnalysisYear> addList,
			List<CbmJAnalysisYear> updateList);
}