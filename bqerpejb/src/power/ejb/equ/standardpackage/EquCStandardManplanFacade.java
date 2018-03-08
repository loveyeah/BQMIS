package power.ejb.equ.standardpackage;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCStandardManplan.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardManplan
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardManplanFacade implements
		EquCStandardManplanFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardManplan
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	
	public boolean save(EquCStandardManplan entity) {
		try {

			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getId());// 设置排序号
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	public void save(List<EquCStandardManplan> addList){
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("EQU_C_STANDARD_MANPLAN", "ID");// 取最大ID,主键
			int i = 0;
			for (EquCStandardManplan entity : addList) {
				entity.setId(id + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * Delete a persistent EquCStandardManplan entity.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardManplan entity = new EquCStandardManplan();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_MANPLAN t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardManplan entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardManplan entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to update
	 * @return EquCStandardManplan the persisted EquCStandardManplan entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardManplan update(EquCStandardManplan entity) {
		try {
			EquCStandardManplan result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	public void update(List<EquCStandardManplan> updateList){
		int i;
		for(i=0;i<updateList.size();i++){
			update(updateList.get(i));
		}
		
	}

	public EquCStandardManplan findById(Long id) {
		try {
			EquCStandardManplan instance = entityManager.find(
					EquCStandardManplan.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardManplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardManplan property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardManplan> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardManplan> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardManplan model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardManplan entities.
	 * 
	 * @return List<EquCStandardManplan> all EquCStandardManplan entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM EQU_C_STANDARD_MANPLAN t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n" + "		 AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "      AND t.enterprisecode = '"
					+ enterpriseCode + "'\n" + " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_MANPLAN t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "			  AND t.WO_CODE='"
					+ woCode + "'\n" + "		 	  AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "   		  AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List<EquCStandardManplan> list = dll.queryByNativeSQL(sql,
					EquCStandardManplan.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}