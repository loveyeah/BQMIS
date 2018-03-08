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
 * Remote interface for AdCMenuTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCMenuTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdCMenuType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	
	/**
	 * 菜谱类型保存
	 * @param entity
	 * 
	 *  @throws RuntimeException 
	 *             
	 */
	public void save(AdCMenuType entity);

	/**
	 * 菜谱类型删除.
	 * 
	 * @param entity
	 *            AdCMenuType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCMenuType entity);
	
	/**
	 * 菜谱类别逻辑删除
	 * @param  entity
	 */
	public void logicDelete(AdCMenuType entity) throws SQLException ;
	/**
	 * Persist a previously saved AdCMenuType entity and return it or a copy of
	 * it to the sender. A copy of the AdCMenuType entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCMenuType entity to update
	 * @return AdCMenuType the persisted AdCMenuType entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCMenuType update(AdCMenuType entity);

	public AdCMenuType findById(Long id);

	/**
	 * Find all AdCMenuType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCMenuType property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCMenuType> found by query
	 */
	public List<AdCMenuType> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdCMenuType entities.
	 * 
	 * @return List<AdCMenuType> all AdCMenuType entities
	 */
	public List<AdCMenuType> findAll();
	/**
     * 查询
     * 
     * @param 
     * @return PageObject  查询结果
     */
    public PageObject findMenutype(String strEnterpriseCode,final int... rowStartIdxAndCount) ;
   
	/**
     * 查询值长审核订餐信息
     * 
     * @param strWorkTypeCode 订餐类别
	 * @param strSubWorkTypeCode 订餐日期
	 * @param strEnterpriseCode 企业代码
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
	public  PageObject findWatchAduitInfo(String strMenuType, String strMenuDate, String strEnterpriseCode);
}