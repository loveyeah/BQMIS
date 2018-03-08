package power.ejb.hr;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCTechnologyGrade.
 * 
 * @see powereai.po.hr.HrCTechnologyGrade
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCTechnologyGradeFacade implements HrCTechnologyGradeFacadeRemote {
	// property constants
	public static final String TECHNOLOGY_GRADE_NAME = "technologyGradeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCTechnologyGrade
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTechnologyGrade entity) {
		LogUtil.log("saving HrCTechnologyGrade instance", Level.INFO, null);
		try {
			// add by liuyi 091124 设置id
			if(entity.getTechnologyGradeId() == null)
				entity.setTechnologyGradeId(bll.getMaxId("HR_C_TECHNOLOGY_GRADE", "TECHNOLOGY_GRADE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCTechnologyGrade entity.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCTechnologyGrade entity) {
		LogUtil.log("deleting HrCTechnologyGrade instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCTechnologyGrade.class,
					entity.getTechnologyGradeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCTechnologyGrade entity and return it or a
	 * copy of it to the sender. A copy of the HrCTechnologyGrade entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTechnologyGrade entity to update
	 * @return HrCTechnologyGrade the persisted HrCTechnologyGrade entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTechnologyGrade update(HrCTechnologyGrade entity) {
		LogUtil.log("updating HrCTechnologyGrade instance", Level.INFO, null);
		try {
			HrCTechnologyGrade result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCTechnologyGrade findById(Long id) {
		LogUtil.log("finding HrCTechnologyGrade instance with id: " + id,
				Level.INFO, null);
		try {
			HrCTechnologyGrade instance = entityManager.find(
					HrCTechnologyGrade.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCTechnologyGrade entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyGrade property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCTechnologyGrade> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyGrade> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCTechnologyGrade instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCTechnologyGrade model where model."
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

	public List<HrCTechnologyGrade> findByTechnologyGradeName(
			Object technologyGradeName, int... rowStartIdxAndCount) {
		return findByProperty(TECHNOLOGY_GRADE_NAME, technologyGradeName,
				rowStartIdxAndCount);
	}

	public List<HrCTechnologyGrade> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCTechnologyGrade> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCTechnologyGrade entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyGrade> all HrCTechnologyGrade entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCTechnologyGrade> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCTechnologyGrade instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from HrCTechnologyGrade model";
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
	 * add by liuyi 091123
	 * 查找所有技术等级
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllTechnologyGrades(String enterpriseCode){
		try{
			String sql = "SELECT E.TECHNOLOGY_GRADE_ID,E.TECHNOLOGY_GRADE_NAME \n" +
					" FROM HR_C_TECHNOLOGY_GRADE E \n" +
					" WHERE E.IS_USE = 'Y' AND E.ENTERPRISE_CODE = '" + enterpriseCode +"'";
			LogUtil.log("所有技术等级id和名称开始。SQL=" + sql, Level.INFO, null);
			List list = bll.queryByNativeSQL(sql);
			List<DrpCommBeanInfo> arraylist = new ArrayList<DrpCommBeanInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DrpCommBeanInfo model = new DrpCommBeanInfo();
				if (data[0] != null) {
					model.setId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					model.setText(data[1].toString());
				}
				arraylist.add(model);
			}
			PageObject result = new PageObject();
			result.setList(arraylist);
			LogUtil.log("查找所有技术等级id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有技术等级id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long findGradeIdByName(String name, String enterpriseCode) {
		Long gradeId = null;
		String sql = 
			"select a.technology_grade_id\n" +
			"  from hr_c_technology_grade a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and a.technology_grade_name = '"+name+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			gradeId = Long.parseLong(obj.toString());

		return gradeId;
	}
}