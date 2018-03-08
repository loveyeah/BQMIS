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

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.form.DrpCommBeanInfo;

/**
 * Facade for entity HrCEducation.
 * 
 * @see powereai.po.hr.HrCEducation
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCEducationFacade implements HrCEducationFacadeRemote {
	// property constants
	public static final String EDUCATION_NAME = "educationName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
 
	public String getEducationName(Long id)
	{
		String sql = 
			"select t.education_name from hr_c_education t where t.education_id =" + id;
		String result = bll.getSingal(sql).toString();
		if(result != null)
		{
			return result;
		}
		return "";

	}
	
	public void save(HrCEducation entity) throws CodeRepeatException  { 
		try {
			if(this.checkNameSameForAdd(entity.getEducationName()))
			{
				throw new CodeRepeatException("学历名称已经存在，请重新输入！");
			}
			if(entity.getEducationId() == null)
			{
				entity.setEducationId(bll.getMaxId("hr_c_education", "education_id"));
			} 
			entityManager.persist(entity); 
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public HrCEducation update(HrCEducation entity) throws CodeRepeatException  {
	 
		try {
			if(this.checkNameSameForUpdate(entity.getEducationId(),entity.getEducationName()))
			{
				throw new CodeRepeatException("学历名称已经存在，请重新输入！");
			}
			HrCEducation result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	private boolean checkNameSameForAdd(String educationName)
	{
		String sql = "select count(1) from hr_c_education t where t.education_name=?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{educationName}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(Long educationId,String educationName)
	{
		String sql = "select count(1) from hr_c_education t where t.education_name=? and t.education_id <> ?";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{educationName,educationId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	  
	public void delete(HrCEducation entity) {
		LogUtil.log("deleting HrCEducation instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCEducation.class, entity
					.getEducationId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	

	public HrCEducation findById(Long id) {
		LogUtil.log("finding HrCEducation instance with id: " + id, Level.INFO,
				null);
		try {
			HrCEducation instance = entityManager.find(HrCEducation.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCEducation entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCEducation property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCEducation> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCEducation> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCEducation instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCEducation model where model."
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

	public List<HrCEducation> findByEducationName(Object educationName,
			int... rowStartIdxAndCount) {
		return findByProperty(EDUCATION_NAME, educationName,
				rowStartIdxAndCount);
	}

	public List<HrCEducation> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCEducation> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCEducation entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEducation> all HrCEducation entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCEducation> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCEducation instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCEducation model";
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
	 * 查找所有学历编码
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllEducations(String enterpriseCode){
		try{
			String sql = "SELECT L.EDUCATION_ID,L.EDUCATION_NAME FROM HR_C_EDUCATION L \n" +
			// modified by liuyi 091123 用U表示使用中，企业编码属性无 已解决
					" WHERE L.IS_USE = 'Y' AND L.ENTERPRISE_CODE = '" + enterpriseCode +"'";
//			" WHERE L.IS_USE = 'U' ";
			LogUtil.log("所有学历编码id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有学历编码id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有学历编码id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}

	public Long findEducationIdByName(String name, String enterpriseCode) {
		Long educationId = null;
		String sql = 
			"select b.education_id\n" +
			"  from Hr_c_Education b\n" + 
			" where b.education_name = '"+name+"'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";
		Object obj = bll.getSingal(sql);
		if(obj != null)
			educationId = Long.parseLong(obj.toString());

		return educationId;
	}
}