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
 * Facade for entity EquCStandardStepdocuments.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardStepdocuments
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardStepdocumentsFacade implements
		EquCStandardStepdocumentsFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardStepdocuments
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardStepdocuments entity) {
		try {
			entity.setId(dll.getMaxId("EQU_C_STANDARD_STEPDOCUMENTS", "ID"));// 取最大ID,主键					
			entity.setIfUse("Y");// 将记录加删除字段默认值设为Y,使用
			entity.setOperationStep("1");
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Delete a persistent EquCStandardStepdocuments entity.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) {
		try {
			EquCStandardStepdocuments entity = new EquCStandardStepdocuments();
			entity = findById(id);
			entity.setIfUse("N");// 将记录加删除字段默认值设为N,不使用
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_STEPDOCUMENTS t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
  
	public boolean deleteFiles(String ids){
		try {
			String sql = "UPDATE EQU_C_STANDARD_STEPDOCUMENTS t\n"
					+ "   SET t.file_path = ''\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 批量删除记录(假删除)
			return true;
		} catch (RuntimeException re) {
			return false;
		}
		
	}
	/**
	 * Persist a previously saved EquCStandardStepdocuments entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardStepdocuments
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to update
	 * @return EquCStandardStepdocuments the persisted EquCStandardStepdocuments
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardStepdocuments update(EquCStandardStepdocuments entity) {
		try {
			EquCStandardStepdocuments result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquCStandardStepdocuments findById(Long id) {
		try {
			EquCStandardStepdocuments instance = entityManager.find(
					EquCStandardStepdocuments.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardStepdocuments entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardStepdocuments property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardStepdocuments> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardStepdocuments> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardStepdocuments model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardStepdocuments entities.
	 * 
	 * @return List<EquCStandardStepdocuments> all EquCStandardStepdocuments
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n"
					+ "  	FROM EQU_C_STANDARD_STEPDOCUMENTS t\n"
					+ "    WHERE t.if_use = 'Y'\n" + "		 AND t.WO_CODE='"
					+ woCode + "'\n"
					+        "    AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY   t.id";
//			if (operationStep != null)
//				sql += "	 AND t.OPERATION_STEP='" + operationStep + "'\n";
//			sql += "      	 AND t.enterprisecode = '" + enterpriseCode + "'\n"
//					+ " ORDER BY t.orderby,\n" + "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_STEPDOCUMENTS t\n"
					+ "    		WHERE t.if_use = 'Y'\n" + "		 	  AND t.WO_CODE='"
					+ woCode + "'\n"					
					+"		  AND t.enterprisecode = '" + enterpriseCode + "'";
//			if (operationStep != null)
//				sqlCount += "	  AND t.OPERATION_STEP='" + operationStep + "'\n";
//			sqlCount += "		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquCStandardStepdocuments> list = dll.queryByNativeSQL(sql,
					EquCStandardStepdocuments.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}