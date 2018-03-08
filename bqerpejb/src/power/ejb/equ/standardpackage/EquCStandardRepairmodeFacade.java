package power.ejb.equ.standardpackage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCStandardRepairmode.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardRepairmode
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardRepairmodeFacade implements
		EquCStandardRepairmodeFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardRepairmode
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardRepairmode entity) {
		try {
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entity.setRepairmodeCode("RM" + codeFormat.format(codevalue));
			if (entity.getOrderby() == null)// 如果没有指定排序号,就将主键值放入排序号字段
				entity.setOrderby(entity.getId());
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
		}
	
	public void save(List<EquCStandardRepairmode> addList) {
		if (addList != null && addList.size() > 0) {
			Long id = dll.getMaxId("EQU_C_STANDARD_REPAIRMODE", "ID");// 取最大ID,主键
			int i = 0;
			for (EquCStandardRepairmode entity : addList) {
				entity.setId(id + (i++));
				this.save(entity);
			}
		}
	}
	/**
	 * Delete a persistent EquCStandardRepairmethod entity. 假删除
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardRepairmode entity = new EquCStandardRepairmode();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardRepairmode entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmode t\n"
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
	 *            EquCStandardRepairmode entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean lock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmode t\n"
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
	 *            EquCStandardRepairmode entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean unlock(String ids) {
		try {
			String sql = "UPDATE equ_c_standard_repairmode t\n"
					+ "   SET t.STATUS = 'C'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量解锁记录
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	/**
	 * Persist a previously saved EquCStandardRepairmode entity and return it or
	 * a copy of it to the sender. A copy of the EquCStandardRepairmode entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardRepairmode entity to update
	 * @return EquCStandardRepairmode the persisted EquCStandardRepairmode
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardRepairmode update(EquCStandardRepairmode entity) {
		try {
	EquCStandardRepairmode result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	
	public void update(List<EquCStandardRepairmode> updateList) {
		int i;
		for (i = 0; i < updateList.size(); i++) {
			update(updateList.get(i));
		}

	}

	public EquCStandardRepairmode findById(Long id) {
		try {
			EquCStandardRepairmode instance = entityManager.find(
					EquCStandardRepairmode.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardRepairmode entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardRepairmode property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardRepairmode> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardRepairmode> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardRepairmode model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {			
			throw re;
		}
	}

	/**
	 * Find all EquCStandardRepairmode entities.
	 * 
	 * @return List<EquCStandardRepairmode> all EquCStandardRepairmode entities
	 */
	@SuppressWarnings("unchecked")
public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM equ_c_standard_repairmode t\n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM equ_c_standard_repairmode t\n"
					+ " 		WHERE t.if_use = 'Y'\n"
					+ "   		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquCStandardRepairmode> list = dll.queryByNativeSQL(sql,
					EquCStandardRepairmode.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}	
	

	/**
	 * Find all EquCStandardRepairmode entities.
	 * 
	 * @return List<EquCStandardRepairmode> all EquCStandardRepairmode entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAlltoUse(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM equ_c_standard_repairmode t\n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ "      AND t.status='C'\n"
					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM equ_c_standard_repairmode t\n"
					+ " 		WHERE t.if_use = 'Y'\n"
					+ "   		  AND t.enterprisecode = '" + enterpriseCode + "'"
					+ "   		  AND t.status='C'\n";

			List<EquCStandardRepairmode> list = dll.queryByNativeSQL(sql,
					EquCStandardRepairmode.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}