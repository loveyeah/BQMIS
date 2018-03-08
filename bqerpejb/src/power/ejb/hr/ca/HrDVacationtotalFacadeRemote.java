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
 * 请假统计方法远程接口
 * 
 * @author huangweijie
 * @version 1.0
 */
@Remote
public interface HrDVacationtotalFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrDVacationtotal entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrDVacationtotal entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrDVacationtotal entity);

    /**
     * Delete a persistent HrDVacationtotal entity.
     * 
     * @param entity
     *            HrDVacationtotal entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrDVacationtotal entity);

    /**
     * 请假实体更新方法
     * 
     * @param entity
     *            请假实体
     * @return 成功后的实体
     * @throws RuntimeException
     *             运行时错误异常
     * @throws DataChangeException 
     *             排他异常
     */
    public HrDVacationtotal update(HrDVacationtotal entity) throws DataChangeException;

    /**
     * 由请假统计ID获得请假统计实体
     * 
     * @param id 请假统计ID
     * @param enterpriseCode 企业编码
     * 
     * @return 请假统计实体
     */
    public HrDVacationtotal findById(HrDVacationtotalId id, String enterpriseCode);

    /**
     * Find all HrDVacationtotal entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrDVacationtotal property to query
     * @param value
     *            the property value to match
     * @return List<HrDVacationtotal> found by query
     */
    public List<HrDVacationtotal> findByProperty(String propertyName, Object value);

    public List<HrDVacationtotal> findByDays(Object days);

    public List<HrDVacationtotal> findByLastModifiyBy(Object lastModifiyBy);

    public List<HrDVacationtotal> findByIsUse(Object isUse);

    public List<HrDVacationtotal> findByEnterpriseCode(Object enterpriseCode);

    /**
     * Find all HrDVacationtotal entities.
     * 
     * @return List<HrDVacationtotal> all HrDVacationtotal entities
     */
    public List<HrDVacationtotal> findAll();
}