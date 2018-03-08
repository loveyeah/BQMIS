/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCVacationtypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCVacationtypeFacadeRemote {
    /**
     * Perform an initial save of a previously unsaved HrCVacationtype entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *            HrCVacationtype entity to persist
     * @throws RuntimeException
     *             when the operation fails
     */
    public void save(HrCVacationtype entity);

    /**
     * Delete a persistent HrCVacationtype entity.
     * 
     * @param entity
     *            HrCVacationtype entity to delete
     * @throws RuntimeException
     *             when the operation fails
     */
    public void delete(HrCVacationtype entity);

    /**
     * Persist a previously saved HrCVacationtype entity and return it or a copy
     * of it to the sender. A copy of the HrCVacationtype entity parameter is
     * returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *            HrCVacationtype entity to update
     * @return HrCVacationtype the persisted HrCVacationtype entity instance,
     *         may not be the same
     * @throws RuntimeException
     *             if the operation fails
     */
    public HrCVacationtype update(HrCVacationtype entity);

    public HrCVacationtype findById(Long id);

    /**
     * Find all HrCVacationtype entities with a specific property value.
     * 
     * @param propertyName
     *            the name of the HrCVacationtype property to query
     * @param value
     *            the property value to match
     * @return List<HrCVacationtype> found by query
     */
    public List<HrCVacationtype> findByProperty(String propertyName,
            Object value);

    /**
     * Find all HrCVacationtype entities.
     * 
     * @return List<HrCVacationtype> all HrCVacationtype entities
     */
    public List<HrCVacationtype> findAll();
    
    /**
     * 查询
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     * @throws SQLException
     */
    public PageObject findAllVacation(String enterpriseCode, final int... rowStartIdxAndCount)throws SQLException;
    
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws SQLException
     */
    public HrCVacationtype addVacation(HrCVacationtype entity)throws SQLException;
    /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws SQLException
     */
    public HrCVacationtype updateVacation(HrCVacationtype entity)throws SQLException;
    /**
     * 删除一条信息
     *
     * @param 假别ID
     * @param workerCode 登录者id
     * @throws RuntimeException
     *             
     */
    public void deleteByVacationTypeId(HrCVacationtype entity) throws SQLException;

	/**
	 * 查询请假类别信息
	 * @param strEnterpriseCode 企业代码
	 * @return PageObject 请假类别信息
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVacationTypeCmb(String strEnterpriseCode) throws SQLException;
}