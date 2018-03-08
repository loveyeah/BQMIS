/**
 * Copyright ustcsoft.com
 * All right reserved
 */
package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdCRightFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCRightFacadeRemote {

    /**
     * 权限重复检查
     * 
     * @param strUserCode 人员编码
     * @param strEnterpriseCode 企业代码
     * @return boolean 是否重复
     */
    public boolean checkRight(String strUserCode, String strEnterpriseCode) throws SQLException;

    /**
     * 新增工作权限
     * 
     * @param entity 新增工作权限实体
     */
    public void save(AdCRight entity) throws SQLException ;

    /**
     * Delete a persistent AdCRight entity.
     * 
     * @param entity
     *            AdCRight entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(AdCRight entity);

    /**
     * 更新工作权限
     * 
     * @param entity 更新工作权限实体
     * @return 工作权限实体
     */
    public AdCRight update(AdCRight entity) throws SQLException;

    /**
     * 按序号查找工作权限
     * 
     * @param id 序号
     * @return
     */
    public AdCRight findById(Long id) throws SQLException;

    /**
     * Find all AdCRight entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the AdCRight property to query
     * @param value
     *            the property value to match
     * @return List<AdCRight> found by query
     */
    public List<AdCRight> findByProperty(String propertyName, Object value);

    /**
     * Find all AdCRight entities.
     * 
     * @return List<AdCRight> all AdCRight entities
     */
    public List<AdCRight> findAll();
}