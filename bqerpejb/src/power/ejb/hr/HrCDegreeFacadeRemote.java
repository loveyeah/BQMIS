package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCDegreeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCDegreeFacadeRemote {
	/**
	 * 新增
	 * @param entity
	 */
	public HrCDegree save(HrCDegree entity) throws CodeRepeatException;

/**
 * 删除
 * @param entity
 */
	public void delete(HrCDegree entity);
	
	public void delete(String ids);

	/**
	 * 保存
	 * @param entity
	 * @return HrCDegree
	 */
	public HrCDegree update(HrCDegree entity) throws CodeRepeatException;

	public HrCDegree findById(Long id);

	/**
	 * Find all HrCDegree entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCDegree property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDegree> found by query
	 */
	public List<HrCDegree> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrCDegree> findByDegreeName(Object degreeName,
			int... rowStartIdxAndCount);

	public List<HrCDegree> findByIsUse(Object isUse, int... rowStartIdxAndCount);

	public List<HrCDegree> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCDegree entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDegree> all HrCDegree entities
	 */
	public PageObject findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * 查找所有学位编码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllDegrees(String enterpriseCode);
	
	/**
	 * add by liuyi 20100611 
	 * 通过名称获得id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long getDegreeIdByName(String name,String enterpriseCode);
	
}