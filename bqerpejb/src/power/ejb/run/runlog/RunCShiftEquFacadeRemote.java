package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCShiftEquFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftEquFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShiftEqu entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftEqu entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public int save(RunCShiftEqu entity);

	/**
	 * Delete a persistent RunCShiftEqu entity.
	 * 
	 * @param entity
	 *            RunCShiftEqu entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Long runequId) throws CodeRepeatException;

	/**
	 * 保存
	 * @param entity  运行方式实体
	 * @return  RunCShiftEqu
	 */
	public RunCShiftEqu update(RunCShiftEqu entity) throws CodeRepeatException;

	public RunCShiftEqu findById(Long id);

	/**
	 * Find all RunCShiftEqu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftEqu property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftEqu> found by query
	 */
	public List<RunCShiftEqu> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCShiftEqu entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftEqu> all RunCShiftEqu entities
	 */
	public List<RunCShiftEqu> findAll(int... rowStartIdxAndCount);
	
	
	/**
	 * 运行方式下拉列表
	 * @return
	 */
	public List getRunWayList();
	
	/**
	 * 根据使用状态显示运行方式列表
	 * @param isUse
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<RunCShiftEqu> findByIsUse(Object isUse,int... rowStartIdxAndCount);

	/**
	 * 根据专业，运行方式查询运行设备列表
	 * @param specialsCode
	 * @param runwayId
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getShiftEquList(String specialsCode,Long runwayId,String enterpriseCode,final int...rowStartIdxAndCount);
	
	/**
	 * 根据专业查询运行方式
	 * @param enterpriseCode
	 * @return
	 */
	public List getRunWayByProfession(String specialCode,String enterpriseCode);

	
	/**
	 * 根据专业、运行方式、设备得到一个对象实体
	 * @param specialcode
	 * @param runkeyid
	 * @param equcode
	 * @param enterprisecode
	 * @return
	 */
	public RunCShiftEqu GetRunWayIdModel(String specialcode, Long runkeyid, String equcode, String enterprisecode);

	/**
	 * 查询专业关心的设备列表
	 * @param specialcode
	 * @param enterprisecode
	 * @return
	 */
	public List<RunCShiftEqu> getListBySpecial(String specialcode,String enterprisecode);
}