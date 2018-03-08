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
 * 运行班统计方法接口
 * 
 * @author huangweijie
 * @version 1.0
 */
@Remote
public interface HrDWorkshifttotalFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrDWorkshifttotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDWorkshifttotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDWorkshifttotal entity);

    /**
     * Delete a persistent HrDWorkshifttotal entity.
     * 
     * @param entity
     *            HrDWorkshifttotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDWorkshifttotal entity);

    /**
     * 运行班实体更新方法
     * 
     * @param entity
     *            运行班实体
     * @return 成功后的实体
     * @throws RuntimeException
     *             运行时错误异常
     * @throws DataChangeException 
     *             排他异常
     */
    public HrDWorkshifttotal update(HrDWorkshifttotal entity) throws DataChangeException;

    /**
     * 由运行班统计ID获得运行班统计实体
     * 
     * @param id 运行班统计ID
     * @param enterpriseCode 企业编码
     * 
     * @return 运行班统计实体
     */
    public HrDWorkshifttotal findById(HrDWorkshifttotalId id, String enterpriseCode);

    /**
     * Find all HrDWorkshifttotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDWorkshifttotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDWorkshifttotal> found by query
     */
    public List<HrDWorkshifttotal> findByProperty(String propertyName, Object value);

    public List<HrDWorkshifttotal> findByDeptId(Object deptId);

    public List<HrDWorkshifttotal> findByDays(Object days);

    public List<HrDWorkshifttotal> findByMoney(Object money);

    public List<HrDWorkshifttotal> findByLastModifiyBy(Object lastModifiyBy);

    public List<HrDWorkshifttotal> findByIsUse(Object isUse);

    public List<HrDWorkshifttotal> findByEnterpriseCode(Object enterpriseCode);

    /**
     * Find all HrDWorkshifttotal entities.
     * 
     * @return List<HrDWorkshifttotal> all HrDWorkshifttotal entities
     */
    public List<HrDWorkshifttotal> findAll();
}