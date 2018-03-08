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
 * Facade for entity EquCStandardRepairmethod.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardRepairmethod
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardRepairmethodFacade implements
		EquCStandardRepairmethodFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardRepairmethod
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardRepairmethod entity) {
		try {
			entity.setId(dll.getMaxId("EQU_C_STANDARD_REPAIRMETHOD", "ID"));// 取最大ID,主键
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entity.setStatus("C");// 将记录使用状态默认为C,正常
			if (entity.getOrderby() == null)// 如果没有指定排序号,就将主键值放入排序号字段
				entity.setOrderby(entity.getId());
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 假删除
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardRepairmethod entity = new EquCStandardRepairmethod();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 假删除
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmethod t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 锁定记录
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean lock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmethod t\n"
					+ "   SET t.STATUS = 'L'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量锁定记录
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 解锁记录
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean unlock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmethod t\n"
					+ "   SET t.STATUS = 'C'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量解锁记录
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 删除附件
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean deleteFile(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmethod t\n"
					+ "   SET t.REPAIRMETHOD_FILE = null\n" + " WHERE id IN ("
					+ ids + ")";
			dll.exeNativeSQL(sql);// 批量删除附件
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardRepairmethod entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardRepairmethod
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmethod entity to update
	 * @return EquCStandardRepairmethod the persisted EquCStandardRepairmethod
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardRepairmethod update(EquCStandardRepairmethod entity) {
		try {
			EquCStandardRepairmethod result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquCStandardRepairmethod findById(Long id) {
		try {
			EquCStandardRepairmethod instance = entityManager.find(
					EquCStandardRepairmethod.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardRepairmethod entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardRepairmethod property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardRepairmethod> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardRepairmethod> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardRepairmethod model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardRepairmethod entities.
	 * 
	 * @return List<EquCStandardRepairmethod> all EquCStandardRepairmethod
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM equ_c_standard_repairmethod t\n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM equ_c_standard_repairmethod t\n"
					+ " 		WHERE t.if_use = 'Y'\n"
					+ "   		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquCStandardRepairmethod> list = dll.queryByNativeSQL(sql,
					EquCStandardRepairmethod.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllToUse(String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM equ_c_standard_repairmethod t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.Status='C'\n"
					+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM equ_c_standard_repairmethod t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "		 AND t.Status='C'\n"
					+ "   		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquCStandardRepairmethod> list = dll.queryByNativeSQL(sql,
					EquCStandardRepairmethod.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}