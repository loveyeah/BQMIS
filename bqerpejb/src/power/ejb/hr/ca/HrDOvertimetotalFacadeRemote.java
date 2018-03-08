/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * 加班统计接口类
 * 
 * @author zhouxu
 * @version 1.0
 */
@Remote
public interface HrDOvertimetotalFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrDOvertimetotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDOvertimetotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDOvertimetotal entity);

    /**
     * Delete a persistent HrDOvertimetotal entity.
     * 
     * @param entity
     *            HrDOvertimetotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDOvertimetotal entity);

    /**
     * 更新一个加班表bean
     * 
     * @param entity
     *            HrDOvertimetotal 加班表实体
     * @return HrDOvertimetotal 
     *            HrDOvertimetotal 成功后的实体
     * @throws RuntimeException
     *             操作失败的例外
     * @throws DataChangeException
     *             排他例外
     */
    public HrDOvertimetotal update(HrDOvertimetotal entity) throws DataChangeException;

    /**
     * 由ID获得实体
     * 
     * @param id 加班ID
     * @param enterpriseCode 企业编码
     * 
     * @return HrDOvertimetotal 加班实体
     */
    public HrDOvertimetotal findById(HrDOvertimetotalId id, String enterpriseCode);

    /**
     * Find all HrDOvertimetotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDOvertimetotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDOvertimetotal> found by query
     */
    public List<HrDOvertimetotal> findByProperty(String propertyName, Object value);

    public List<HrDOvertimetotal> findByDeptId(Object deptId);

    public List<HrDOvertimetotal> findByDays(Object days);

    public List<HrDOvertimetotal> findByLastModifiyBy(Object lastModifiyBy);

    public List<HrDOvertimetotal> findByIsUse(Object isUse);

    public List<HrDOvertimetotal> findByEnterpriseCode(Object enterpriseCode);

    /**
     * Find all HrDOvertimetotal entities.
     * 
     * @return List<HrDOvertimetotal> all HrDOvertimetotal entities
     */
    public List<HrDOvertimetotal> findAll();
}