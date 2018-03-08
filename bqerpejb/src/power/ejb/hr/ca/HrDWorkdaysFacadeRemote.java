/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * 出勤统计方法接口
 * 
 * @author huangweijie
 * @version 1.0
 */
@Remote
public interface HrDWorkdaysFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrDWorkdays entity. All
     * subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDWorkdays entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDWorkdays entity);

    /**
     * Delete a persistent HrDWorkdays entity.
     * 
     * @param entity
     *            HrDWorkdays entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDWorkdays entity);

    /**
     * 出勤实体更新方法
     * 
     * @param entity
     *            出勤实体
     * @return 成功后的实体
     * @throws RuntimeException
     *             运行时错误异常
     * @throws DataChangeException 
     *             排他异常
     */
    public HrDWorkdays update(HrDWorkdays entity) throws DataChangeException;

    /**
     * 由出勤统计ID获得出勤统计实体
     * 
     * @param id 出勤统计ID
     * @param enterpriseCode 企业编码
     * 
     * @return 出勤统计实体
     */
    public HrDWorkdays findById(HrDWorkdaysId id, String enterpriseCode);

    /**
     * Find all HrDWorkdays entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDWorkdays property to query
     * @param value
     *            the property value to match
     * @return List<HrDWorkdays> found by query
     */
    public List<HrDWorkdays> findByProperty(String propertyName, Object value);

    public List<HrDWorkdays> findByDeptId(Object deptId);

    public List<HrDWorkdays> findByDays(Object days);

    public List<HrDWorkdays> findByLastModifiyBy(Object lastModifiyBy);

    public List<HrDWorkdays> findByIsUse(Object isUse);

    public List<HrDWorkdays> findByEnterpriseCode(Object enterpriseCode);

    /**
     * Find all HrDWorkdays entities.
     * 
     * @return List<HrDWorkdays> all HrDWorkdays entities
     */
    public List<HrDWorkdays> findAll();
}