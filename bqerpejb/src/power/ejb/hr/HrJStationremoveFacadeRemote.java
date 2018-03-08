package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJStationremoveFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJStationremoveFacadeRemote {
	/**
     * Perform an initial save of a previously unsaved HrJStationremove entity.
     * All subsequent persist actions of this entity should use the #update()
     * method.
     * 
     * @param entity
     *                HrJStationremove entity to persist
     * @throws RuntimeException
     *                 when the operation fails
     */
    public HrJStationremove save(HrJStationremove entity);

    /**
     * Delete a persistent HrJStationremove entity.
     * 
     * @param entity
     *                HrJStationremove entity to delete
     * @throws DataChangeException 
     * @throws RuntimeException
     *                 when the operation fails
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void delete(HrJStationremove entity,String date) throws SQLException, DataChangeException, DataChangeException;
    
    /**
     * 上报岗位调动单
     * @param entity 岗位调动单bean
     * @throws SQLException
     */
    public void report(HrJStationremove entity,String date) throws SQLException, DataChangeException;

    /**
     * Persist a previously saved HrJStationremove entity and return it or a
     * copy of it to the sender. A copy of the HrJStationremove entity parameter
     * is returned when the JPA persistence mechanism has not previously been
     * tracking the updated entity.
     * 
     * @param entity
     *                HrJStationremove entity to update
     * @return HrJStationremove the persisted HrJStationremove entity instance,
     *         may not be the same
     * @throws RuntimeException
     *                 if the operation fails
     */
    public void update(HrJStationremove entity,String date) throws SQLException, DataChangeException;

    public HrJStationremove findById(Long id);

    /**
     * Find all HrJStationremove entities with a specific property value.
     * 
     * @param propertyName
     *                the name of the HrJStationremove property to query
     * @param value
     *                the property value to match
     * @return List<HrJStationremove> found by query
     */
    public List<HrJStationremove> findByProperty(String propertyName,
	    Object value);

    /**
     * Find all HrJStationremove entities.
     * 
     * @return List<HrJStationremove> all HrJStationremove entities
     */
    public List<HrJStationremove> findAll();
    public HrJStationremove update(HrJStationremove entity);
    /**
     * 查询所有的岗位调动类别维护信息
     * @param enterpriseCode 企业编码
     * @return
     * @throws SQLException
     */
	@SuppressWarnings("unchecked")
	public PageObject getStationRemove(String enterpriseCode) throws SQLException;
	
	/************员工调动查询用*开始**********************/
    /**
     * 员工调动查询之
     * 班组调动查询
     * @param startDate 调动日期上限
     * @param endDate 调动日期下限
     * @param deptBFCode 调动前部门
     * @param deptAFCode 调动后部门
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
	public PageObject getBanZuList(String startDate, String endDate,
			String deptBFCode, String deptAFCode, String enterpriseCode, 
			final int... rowStartIdxAndCount);
    
    /**
     * 员工调动查询之
     * 员工调动查询
     * @param startDate 调动日期上限
     * @param endDate 调动日期下限
     * @param dcmState 单据状态
     * @param deptBFCode 调动前部门
     * @param deptAFCode 调动后部门
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
	public PageObject getEmpMoveList(String startDate, String endDate,
    		String dcmState, String deptBFCode, String deptAFCode, 
    		String enterpriseCode, final int... rowStartIdxAndCount);
    
    /**
     * 员工调动查询之
     * 员工借调查询
     * @param startDate 借调日期上限
     * @param endDate 借调日期下限
     * @param deptBFCode 所属部门
     * @param deptAFCode 借调部门
     * @param ifBack 是否已回
     * @param dcmStatus 单据状态
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 动态查询参数(开始行和查询行)
     * @return PageObject 查询结果
     */
	public PageObject getEmpBorrowList(String startDate, String endDate,
    		String deptBFCode, String deptAFCode, String ifBack, 
    		String dcmStatus, String enterpriseCode, 
    		final int... rowStartIdxAndCount);
    /************员工调动查询用*结束**********************/
	
	/**
	 * 
	 */
	public String getMaxRequisitionNo(String enterpriseCode);
}