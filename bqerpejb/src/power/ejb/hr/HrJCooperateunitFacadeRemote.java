package power.ejb.hr;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJCooperateunitFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJCooperateunitFacadeRemote {

	/**
	 * 新增协作单位维护
	 *
	 * @param entity 新增协作单位维护实体
	 * @throws SQLException
	 */
	public void save(HrJCooperateunit entity) throws SQLException;

	/**
	 * Delete a persistent HrJCooperateunit entity.
	 *
	 * @param entity
	 *            HrJCooperateunit entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJCooperateunit entity);

	/**
	 * 更新协作单位维护
	 *
	 * @param entity 更新协作单位维护实体
	 * @return 协作单位维护实体
	 * @throws SQLException
	 */
	public HrJCooperateunit update(HrJCooperateunit entity) throws SQLException ;

	public HrJCooperateunit findById(Long id);

	/**
	 * Find all HrJCooperateunit entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrJCooperateunit property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJCooperateunit> found by query
	 */
	public List<HrJCooperateunit> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJCooperateunit entities.
	 *
	 * @return List<HrJCooperateunit> all HrJCooperateunit entities
	 */
	public List<HrJCooperateunit> findAll();

	/**
	 * 查询所有协作单位维护信息
	 * @param strEnterpriseCode 企业代码
	 * @return 所有协作单位维护信息
	 * @throws SQLException
	 * @throws ParseException
	 * @author zhaozhijie
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCooperateUnit(String strEnterpriseCode,final int... rowStartIdxAndCount)
	    throws SQLException;
	/**
	 * 查找所有协作单位
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllCooperateunits(String enterpriseCode);
	/**
	 * 
	 */
	public PageObject getCooperateUnitIDAndName(String strEnterpriseCode);
}