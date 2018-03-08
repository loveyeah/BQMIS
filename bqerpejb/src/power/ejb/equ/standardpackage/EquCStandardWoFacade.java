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
 * Facade for entity EquCStandardWo.
 * 
 * @see power.ejb.equ.standardpackage.EquCStandardWo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCStandardWoFacade implements EquCStandardWoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * Perform an initial save of a previously unsaved EquCStandardWo entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardWo entity) {
		try {
			SimpleDateFormat codeFormat = new SimpleDateFormat("yyyyMMddhhmmss");
			Date codevalue = new Date();
			entity.setWoCode("SP" + codeFormat.format(codevalue));
			entity.setWoId(dll.getMaxId("EQU_C_STANDARD_WO", "WO_ID"));
			entity.setIfUse("Y");
			entity.setStatus("C");
			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getWoId());
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
  
   public Long checkJobCode(String jobCode){
	   String sql="select count(*) from equ_c_standard_wo t where  t.job_code='"+jobCode+"'";
	    Long count=Long.parseLong(dll.getSingal(sql).toString());
     return count;
   }
	
	/**
	 * Delete a persistent EquCStandardWo entity.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_WO t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE wo_id IN (" + ids
					+ ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean lock(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_WO t\n"
					+ "   SET t.status = 'L'\n" + " WHERE wo_id IN (" + ids
					+ ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public boolean unlock(String ids) {
		try {
			String sql = "UPDATE EQU_C_STANDARD_WO t\n"
					+ "   SET t.status = 'C'\n" + " WHERE wo_id IN (" + ids
					+ ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved EquCStandardWo entity and return it or a copy
	 * of it to the sender. A copy of the EquCStandardWo entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardWo entity to update
	 * @return EquCStandardWo the persisted EquCStandardWo entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardWo update(EquCStandardWo entity) {
		try {
			EquCStandardWo result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquCStandardWo findById(Long id) {
		try {
			EquCStandardWo instance = entityManager.find(EquCStandardWo.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardWo entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardWo property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardWo> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCStandardWo> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from EquCStandardWo model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * Find all EquCStandardWo entities.
	 * 
	 * @return List<EquCStandardWo> all EquCStandardWo entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT *\n" + "  	FROM EQU_C_STANDARD_WO t\n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "      AND t.enterprisecode = '" + enterpriseCode + "'\n"
					+ " ORDER BY t.orderby,\n" + "          t.WO_ID";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_C_STANDARD_WO t\n"
					+ " 		WHERE t.if_use = 'Y'\n"
					+ "   		  AND t.enterprisecode = '" + enterpriseCode + "'";

			List<EquCStandardWo> list = dll.queryByNativeSQL(sql,
					EquCStandardWo.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 根据KKSCODE查找解决方案
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllToUse(String enterpriseCode, String kksCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT b.*\n" + "  	FROM equ_c_standard_equ a,\n"
					+ "			 equ_c_standard_wo  b\n"
					+ "	   WHERE a.wo_code = b.wo_code\n"
					+ "   	 AND a.kks_code = '" + kksCode + "'\n"
					+ "   	 AND a.enterprisecode = '" + enterpriseCode + "'\n"
					+ "   	 AND b.enterprisecode = '" + enterpriseCode + "'\n"
					+ "   	 AND a.if_use = 'Y'\n" + "   AND b.if_use = 'Y'\n"
					+ " ORDER BY b.orderby,\n" + " 	     b.WO_ID DESC";

			String sqlCount = "SELECT count(1)\n"
					+ "  		 FROM equ_c_standard_equ a,\n"
					+ "			 	  equ_c_standard_wo  b\n"
					+ "	   		WHERE a.wo_code = b.wo_code\n"
					+ "   	 	  AND a.kks_code = '" + kksCode + "'\n"
					+ "   	 	  AND a.enterprisecode = '" + enterpriseCode
					+ "'\n" + "   	 	  AND b.enterprisecode = '"
					+ enterpriseCode + "'\n" + "   	 	  AND a.if_use = 'Y'\n"
					+ "      	  AND b.if_use = 'Y'\n";

			List<EquCStandardWo> list = dll.queryByNativeSQL(sql,
					EquCStandardWo.class, rowStartIdxAndCount);

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(count);

			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}
}