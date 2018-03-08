/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;


import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdCMenuWhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCMenuWhFacadeRemote {
	/**
	 * 增加菜谱维护信息
	 * @author Li Chensheng
	 * @param entity
	 *            AdCMenuWh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCMenuWh entity);
	/**
	 * 修改菜谱维护信息
	 * @author Li Chensheng
	 * @param entity
	 *            AdCMenuWh entity to update
	 * @return AdCMenuWh the persisted AdCMenuWh entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCMenuWh update(AdCMenuWh entity) throws DataChangeException, SQLException;

	public AdCMenuWh findById(Long id);

	/**
	 * Find all AdCMenuWh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCMenuWh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCMenuWh> found by query
	 */
	public List<AdCMenuWh> findByProperty(String propertyName, Object value);

	/**
	 * 根据菜谱类别得到菜谱信息
	 * 
	 * @param menutypeCode 菜谱类别
	 * 
	 * @return 所有的菜谱
	 */
	public PageObject getAllMenu(String menutypeCode, final int... rowStartIdxAndCount);
	/**
     * 查询
     * 
     * 
     * @param 
     * @return PageObject  查询结果
     */
    public PageObject findMenu(String menuTypeCode,String enterpriseCode,final int... rowStartIdxAndCount)throws SQLException;
    /**
	 *  删除一条菜谱维护信息
	 *  @author Li Chensheng
	 * @throws DataChangeException, SQLException
	 * 
	 */
    public void delete(Long id,String workerCode,String updateTime)throws DataChangeException, SQLException;
}