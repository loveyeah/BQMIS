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
 * Facade for entity HrCEmpType.
 * 
 * @see powereai.po.hr.HrCEmpType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCEmpTypeFacade implements HrCEmpTypeFacadeRemote {
	// property constants
	public static final String EMP_TYPE_NAME = "empTypeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 保存
	 */
	public HrCEmpType save(HrCEmpType entity) throws CodeRepeatException { 
		try {
			if(this.checkNameSameForAdd(entity.getEmpTypeName()))
			{
				throw new CodeRepeatException("员工类别名称已经存在!");
			}
			if(entity.getEmpTypeId() == null)
			{
				entity.setEmpTypeId(bll.getMaxId("hr_c_emp_type", "emp_type_id"));
			} 
			entityManager.persist(entity); 
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	 
	public HrCEmpType update(HrCEmpType entity) throws CodeRepeatException { 
		try {
			if(this.checkNameSameForUpdate(entity.getEmpTypeId(),entity.getEmpTypeName()))
			{
				throw new CodeRepeatException("员工类别名称已经存在!");
			}
			HrCEmpType result = entityManager.merge(entity); 
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public String getEmpTypeName(Long id)
	{
		String sql = 
			"select t.emp_type_name from  hr_c_emp_type t where  t.emp_type_id =" + id;
		String result;
		result = bll.getSingal(sql).toString();
		if(result != null)
		{
			return result;
		}
		return "";

	}
	
	private boolean checkNameSameForAdd(String empTypeName)//modify by drdu 091027 新加了IS-USE过滤
	{
		String sql = "select count(1) from hr_c_emp_type t where t.emp_type_name=? and t.is_use = 'Y'";//update by sychen 20100831
//		String sql = "select count(1) from hr_c_emp_type t where t.emp_type_name=? and t.is_use = 'U'";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{empTypeName}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	private boolean checkNameSameForUpdate(Long empTypeId,String empTypeName)//modify by drdu 091027 新加了IS-USE过滤
	{
		String sql = "select count(1) from hr_c_emp_type t where t.emp_type_name=? and t.emp_type_id <> ? and t.is_use = 'Y'";//update by sychen 20100831
//		String sql = "select count(1) from hr_c_emp_type t where t.emp_type_name=? and t.emp_type_id <> ? and t.is_use = 'U'";
		int size =Integer.parseInt(bll.getSingal(sql, new Object[]{empTypeName,empTypeId}).toString());
		if(size>0)
		{
			return true;
		}
		return false; 
	}
	
 
	public void delete(HrCEmpType entity) {
	 
		try {
			entity = entityManager.getReference(HrCEmpType.class, entity
					.getEmpTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public HrCEmpType findById(Long id) {
		LogUtil.log("finding HrCEmpType instance with id: " + id, Level.INFO,
				null);
		try {
			HrCEmpType instance = entityManager.find(HrCEmpType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCEmpType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCEmpType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCEmpType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCEmpType> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCEmpType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCEmpType model where model."
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

	public List<HrCEmpType> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount) {
		try {
			final String queryString = "select model from HrCEmpType model where "+strWhere+"'"+o+"'";
			Query query = entityManager.createQuery(queryString);
			//			final String sql ="select * from hr_c_emp_type where "+strWhere+"'"+o+"'";
//				query.setParameter("param",o);
//			return bll.queryByNativeSQL(sql,HrCEmpType.class,rowStartIdxAndCount);
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
	
	public List<HrCEmpType> findByEmpTypeName(Object empTypeName,
			int... rowStartIdxAndCount) {
		return findByProperty(EMP_TYPE_NAME, empTypeName, rowStartIdxAndCount);
	}

	public List<HrCEmpType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCEmpType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCEmpType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCEmpType> all HrCEmpType entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCEmpType> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCEmpType instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCEmpType model";
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
	 * 查找所有员工类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllEmpTypes(String enterpriseCode){
		try{
			String sql = "SELECT E.EMP_TYPE_ID,E.EMP_TYPE_NAME FROM HR_C_EMP_TYPE E \n" +
			// modified by liuyi 091123 用U表示使用中，无企业编码 已解决
					" WHERE E.IS_USE = 'Y' AND E.ENTERPRISE_CODE = '" + enterpriseCode +"'";
//			" WHERE E.IS_USE = 'U' ";
			LogUtil.log("所有员工类别id和名称开始。SQL=" + sql, Level.INFO, null);
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
			LogUtil.log("查找所有员工类别id和名称结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("查找所有员工类别id和名称失败", Level.SEVERE, re);
			throw re;
		}
	}
}