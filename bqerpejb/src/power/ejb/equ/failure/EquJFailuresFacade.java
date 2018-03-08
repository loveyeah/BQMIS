package power.ejb.equ.failure;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJFailures.
 * 
 * @see power.ejb.equ.failure.EquJFailures
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJFailuresFacade implements EquJFailuresFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@SuppressWarnings("unused")
	private HrCDeptFacadeRemote deptRemote; 

	/**
	 * Perform an initial save of a previously unsaved EquJFailures entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJFailures entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJFailures entity) {
		LogUtil.log("saving EquJFailures instance", Level.INFO, null);
		try {
			if(entity.getId() == null || "".equals(entity.getId()))
			{
				entity.setId(Long.parseLong(bll.getMaxId("equ_j_failures", "id")
					.toString()));
			}
			entity.setFailureCode(this.newFailureCode());
			if (entity.getAttributeCode() == null
					|| entity.getAttributeCode().equals("")) {
				entity.setAttributeCode(generateTempEquCode());
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquJFailures entity.
	 * 
	 * @param entity
	 *            EquJFailures entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJFailures entity) {
		LogUtil.log("deleting EquJFailures instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJFailures.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquJFailures entity and return it or a copy of
	 * it to the sender. A copy of the EquJFailures entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJFailures entity to update
	 * @return EquJFailures the persisted EquJFailures entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJFailures update(EquJFailures entity) {
		LogUtil.log("updating EquJFailures instance", Level.INFO, null);
		try {
			if (entity.getAttributeCode() == null) {
				entity.setAttributeCode(generateTempEquCode());
			}
			EquJFailures result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJFailures findById(Long id) {
		LogUtil.log("finding EquJFailures instance with id: " + id, Level.INFO,
				null);
		try {
			EquJFailures instance = entityManager.find(EquJFailures.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJFailures entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJFailures property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJFailures> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJFailures> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJFailures instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJFailures model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJFailures entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailures> all EquJFailures entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquJFailures> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquJFailures instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquJFailures model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 生成新的缺陷单编号
	 * @return String 缺陷单编号
	 */
	public String newFailureCode() {
		String strSql = "select 'CM' || to_char(sysdate, 'yyyymmdd') ||\n"
				+ "       nvl(trim((select to_char(max(to_number(substr(t.failure_code, 11))) + 1,\n"
				+ "                               '0000')\n"
				+ "                  from equ_j_failures t\n"
				+ "                 where t.failure_code like\n"
				+ "                       'CM' || to_char(sysdate, 'yyyymmdd') || '%')),\n"
				+ "           trim(to_char('1', '0000')))\n" + "  from dual";

		String code = "";
		Object obj = bll.getSingal(strSql);
		if (obj != null) {
			code = obj.toString();
		}
		return code;
	}
	/**
	 * 生成设备的临时编码
	 * 
	 * @return
	 */
	private String generateTempEquCode() {
		String strSql = "select 'TMP' || trim(to_char(nvl(max(to_number(substr(t.attribute_code, 4))) + 1,1),\n"
				+ "                             '00000000'))\n"
				+ "  from equ_j_failures t\n"
				+ " where t.attribute_code like 'TMP%'\n"
				+ "   and t.isuse = 'Y'";

		String code = "";
		Object obj = bll.getSingal(strSql);
		if (obj != null) {
			code = obj.toString();
		}
		return code;
	}
	/**
	 * 根据缺陷编码查找缺陷
	 */
	public EquJFailures findFailureByCode(String failureCode,String enterpriseCode)
	{
		String sql="select r.*\n" +
					"  from equ_j_failures r\n" + 
					" where r.failure_code = '"+failureCode+"'\n" + 
					"   and r.entreprise_code = '"+enterpriseCode+"'\n" + 
					"   and r.isuse = 'Y'";
		List<EquJFailures> list=bll.queryByNativeSQL(sql, EquJFailures.class);
		if(list.size() >0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
}