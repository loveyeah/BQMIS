package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCTechnologyTitlesFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCTechnologyTitlesFacadeRemote {
	 
	public void save(HrCTechnologyTitles entity) throws CodeRepeatException;

	 
	public void delete(HrCTechnologyTitles entity);

	 
	public HrCTechnologyTitles update(HrCTechnologyTitles entity) throws CodeRepeatException;

	public HrCTechnologyTitles findById(Long id);

	/**
	 * Find all HrCTechnologyTitles entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyTitles property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyTitles> found by query
	 */
	public List<HrCTechnologyTitles> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);
	public List<HrCTechnologyTitles> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitles> findByTechnologyTitlesTypeId(
			Object technologyTitlesTypeId, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitles> findByTechnologyTitlesName(
			Object technologyTitlesName, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitles> findByTechnologyTitlesLevel(
			Object technologyTitlesLevel, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitles> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCTechnologyTitles> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount); 
	 
	public List<HrCTechnologyTitles> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询技术职称设置
	 * @param rowStartIdxAndCount 
	 * @return PageObject
	 */
	public  PageObject  getTechnologyTitlesList(int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 091123
	 * 查找所有技术职称
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllTechnologyTitles(String enterpriseCode);
	
	/**
	 * add by liuyi 20100603 
	 * 通过职称名查找该id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long findTitleTypeIdByName(String name,String enterpriseCode);
}