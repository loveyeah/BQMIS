package power.ejb.run.securityproduction;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.SafetyMonthForm;

/**
 * Facade for entity SpJSafetyMonth.
 * 
 * @see power.ejb.run.securityproduction.SpJSafetyMonth
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetyMonthFacade implements SpJSafetyMonthFacadeRemote {
	// property constants
	public static final String SUBJECT = "subject";
	public static final String CONTENT = "content";
	public static final String SUMMARY = "summary";
	public static final String FILL_BY = "fillBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved SpJSafetyMonth entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyMonth save(SpJSafetyMonth entity) {
		try{
			String str = "";
			str = this.DateToString(entity.getMonth());
			String sql = "select count(*) from SP_J_SAFETY_MONTH where to_char(month,'yyyy-MM-dd') = '"
				    		+ str +"'";
			if(Long.parseLong(bll.getSingal(sql).toString()) >= 1)
				return null;
			else{
				if(entity.getSafetymonthId() == null){
					entity.setSafetymonthId(bll.getMaxId("SP_J_SAFETY_MONTH", "SAFETYMONTH_ID"));
				}
				entityManager.persist(entity);
				return entity;
			}
		}catch(RuntimeException re){
			LogUtil.log("save failed", Level.SEVERE, re);
			return null;
		}
	}
	/**
	 * Delete a persistent SpJSafetyMonth entity.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(SpJSafetyMonth entity) {
		try {
			Long id = entity.getSafetymonthId();
			String sql = "delete from SP_J_SAFETY_MONTH where safetymonth_id ="
							+  id;
			bll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	/**
	 * Persist a previously saved SpJSafetyMonth entity and return it or a copy
	 * of it to the sender. A copy of the SpJSafetyMonth entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to update
	 * @return SpJSafetyMonth the persisted SpJSafetyMonth entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyMonth update(SpJSafetyMonth entity) {
		try {
			String str = "";
			str = this.DateToString(entity.getMonth());
			String sql = "select count(*) from SP_J_SAFETY_MONTH where to_char(month,'yyyy-MM-dd') = '"
	    		+ str +"'";
			if(Long.parseLong(bll.getSingal(sql).toString()) >= 2)
				return null;
			else{
				SpJSafetyMonth result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	

	public SpJSafetyMonth findById(Long id) {
		try {
			SpJSafetyMonth instance = entityManager.find(SpJSafetyMonth.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetyMonth entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyMonth property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyMonth> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetyMonth> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJSafetyMonth instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJSafetyMonth model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SpJSafetyMonth> findBySubject(Object subject) {
		return findByProperty(SUBJECT, subject);
	}

	public List<SpJSafetyMonth> findByContent(Object content) {
		return findByProperty(CONTENT, content);
	}

	public List<SpJSafetyMonth> findBySummary(Object summary) {
		return findByProperty(SUMMARY, summary);
	}

	public List<SpJSafetyMonth> findByFillBy(Object fillBy) {
		return findByProperty(FILL_BY, fillBy);
	}

	public List<SpJSafetyMonth> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all SpJSafetyMonth entities.
	 * 
	 * @return List<SpJSafetyMonth> all SpJSafetyMonth entities
	 */
	 public SafetyMonthForm findModelForm(String monthString,String enterpriseCode){
		 try{
			 SafetyMonthForm entity=new SafetyMonthForm();
			 String sql="SELECT t.safetymonth_id,\n" +
				 "       t.subject,\n" + 
				 "       t.content,\n" + 
				 "       t.summary,\n" + 
				 "       t.fill_by,\n" +
				 "       GETWORKERNAME(t.fill_by) fill_by_name,\n" + 
				 "       t.fill_date,\n" +
				 "       to_char(t.fill_date, 'yyyy-mm-dd') fill_date_string,\n" +
				 "       t.month,\n" +
				 "       to_char(t.month, 'yyyy-mm') month_string\n" +
				 "  FROM SP_J_SAFETY_MONTH t\n" + 
				 " WHERE to_char(t.month, 'yyyy-mm') = '"+monthString+"'\n" + 
				 "   AND t.enterprise_code = '"+enterpriseCode+"'\n" + 
				 "   AND rownum = 1";
			 Object[] data = (Object[]) bll.getSingal(sql);
			 SpJSafetyMonth model=new SpJSafetyMonth();
			 if(data != null){
				 if (data[0] != null)
				 		model.setSafetymonthId(Long.parseLong(data[0].toString()));
				 	if (data[1] != null)
				 		model.setSubject(data[1].toString());
					if (data[2] != null)
						model.setContent(data[2].toString());
					if (data[3] != null)
						model.setSummary(data[3].toString());
					if(data[4] != null)
						model.setFillBy(data[4].toString());
					if(data[5] != null)
						entity.setWorkName(data[5].toString());
					if(data[6] != null)
						model.setFillDate(java.sql.Date.valueOf(data[6].toString()));
					if(data[7] != null)
						entity.setFilldateString(data[7].toString());
					if(data[8] != null)
						model.setMonth(java.sql.Date.valueOf(data[8].toString()));
					if(data[9] != null)
						entity.setMonthString(data[9].toString());
					entity.setSafetymonth(model);
				 return entity;
			 }else return null;
		 	}catch (RuntimeException re) {
				LogUtil.log("find failed", Level.SEVERE, re);
				return null;
				}
		 	}
	 /**
	  * 
	  * 将日期类型转换为相应字符串
	  * @param date
	  * @return
	  */
	 public String DateToString(Date date){
		 String str = "";
		 java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		 str = format.format(date);
		 return str;
	 }
}