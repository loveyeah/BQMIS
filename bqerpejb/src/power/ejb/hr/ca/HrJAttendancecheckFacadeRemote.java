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
 * 考勤审核操作接口类
 * 
 * @author zhouxu
 */
@Remote
public interface HrJAttendancecheckFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrJAttendancecheck
     * entity. All subsequent persist actions of this entity should use the
     * #update() method.
     * 
     * @param entity
     *            HrJAttendancecheck entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrJAttendancecheck entity);

    /**
     * Delete a persistent HrJAttendancecheck entity.
     * 
     * @param entity
     *            HrJAttendancecheck entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrJAttendancecheck entity);

    /**
     * 更新考勤审核
     * 
     * @param entity
     *            需要被更新的bean的内容
     * @throws RuntimeException
     *             更新失败
     * @throws DataChangeException
     *             排他失败
     */
    public HrJAttendancecheck update(HrJAttendancecheck entity) throws DataChangeException;

    /**
     * 根据id查询考勤审核记录
     * 
     * @param id
     * @param enterpriseCode
     * @return
     */
    public HrJAttendancecheck findById(HrJAttendancecheckId id, String enterpriseCode);

    /**
     * Find all HrJAttendancecheck entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrJAttendancecheck property to query
     * @param value
     *            the property value to match
     * @return List<HrJAttendancecheck> found by query
     */
    public List<HrJAttendancecheck> findByProperty(String propertyName, Object value);

    public List<HrJAttendancecheck> findByDepCharge1(Object depCharge1);

    public List<HrJAttendancecheck> findByDepCharge2(Object depCharge2);

    public List<HrJAttendancecheck> findByDepCharge3(Object depCharge3);

    public List<HrJAttendancecheck> findByDepCharge4(Object depCharge4);

    public List<HrJAttendancecheck> findByLastModifiyBy(Object lastModifiyBy);

    public List<HrJAttendancecheck> findByIsUse(Object isUse);

    public List<HrJAttendancecheck> findByEnterpriseCode(Object enterpriseCode);

    /**
     * Find all HrJAttendancecheck entities.
     * 
     * @return List<HrJAttendancecheck> all HrJAttendancecheck entities
     */
    public List<HrJAttendancecheck> findAll();
}