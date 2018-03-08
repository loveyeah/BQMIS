package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCMainItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCMainItemFacadeRemote {
	 
	public RunCMainItem save(RunCMainItem entity) throws CodeRepeatException; 
	public void delete(RunCMainItem entity) ; 
	public RunCMainItem update(RunCMainItem entity) throws CodeRepeatException; 
	public RunCMainItem findById(Long id); 
	public List<RunCMainItem> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCMainItem entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCMainItem> all RunCMainItem entities
	 */
	public List<RunCMainItem> findAll(int... rowStartIdxAndCount);
	
	public List<RunCMainItem> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);
	
	/**
	 * 根据名称或检索码模糊查询
	 * @param fuzzy 计量单位名称或检索码
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findMainItemList(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
	
	public boolean existsByCode(String code, String enterpriseCode);
	
	/**
	 * 根据编码查找对象
	 * @param mainItemCode
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return RunCMainItem
	 */
	@SuppressWarnings("unchecked")
	public RunCMainItem findListByCode(String mainItemCode,
			String enterpriseCode, final int... rowStartIdxAndCount);
}