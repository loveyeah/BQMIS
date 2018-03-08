/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 证件类别维护AdCPaperFacadeRemote
 * @author li chensheng
 *  
 */
@Remote
public interface AdCPaperFacadeRemote {
	/**
	 * 保存证件类别信息
	 * @param entity
	 *            AdCPaper entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCPaper entity)throws SQLException;

	/**
	 * 获取证件类别编码
	 * 
	 * @return String
	 * @throws SQLException
	 */
	public String getPaperTypeCode() throws SQLException;
	/**
	 * Delete a persistent AdCPaper entity.
	 * 
	 * @param entity
	 *            AdCPaper entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCPaper entity);

	/**
	 * 更新证件类别信息
	 * @param entity
	 *            AdCPaper entity to update
	 * @return AdCPaper the persisted AdCPaper entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCPaper update(AdCPaper entity)throws SQLException;

	public AdCPaper findById(Long id);

	/**
	 * Find all AdCPaper entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCPaper property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCPaper> found by query
	 */
	public List<AdCPaper> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdCPaper entities.
	 * 
	 * @return List<AdCPaper> all AdCPaper entities
	 */
	public List<AdCPaper> findAll();
	/**
     * 查询
     * 
     * @param 
     * @return PageObject  查询结果
     */
    public PageObject findAllPaper(String enterpriseCode,final int... rowStartIdxAndCount)throws SQLException ;
    /**
	 * 逻辑删除一条证件类别信息
	 * 
	 * @param entity
	 *            AdCPaper entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void logicDelete(AdCPaper entity)throws SQLException;

}