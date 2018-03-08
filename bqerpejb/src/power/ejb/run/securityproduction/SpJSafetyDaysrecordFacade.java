package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Date;
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
import power.ejb.run.securityproduction.form.SafetyDaysForm;

/**
 * Facade for entity SpJSafetyDaysrecord.
 * 
 * @see power.ejb.run.securityproduction.SpJSafetyDaysrecord
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetyDaysrecordFacade implements
		SpJSafetyDaysrecordFacadeRemote {
	// property constants
	public static final String IF_BREAK = "ifBreak";
	public static final String SAFETY_DAYS = "safetyDays";
	public static final String MEMO = "memo";
	public static final String RECORD_BY = "recordBy";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SpJSafetyDaysrecord
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyDaysrecord save(SpJSafetyDaysrecord entity) {
		try {
			if(entity.getRecordId() == null){
				entity.setRecordId(bll.getMaxId("SP_J_SAFETY_DAYSRECORD", "RECORD_ID"));
			}
			entityManager.persist(entity);
			setifBreak(entity.getRecordId());
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SpJSafetyDaysrecord entity.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetyDaysrecord entity) {
		LogUtil.log("deleting SpJSafetyDaysrecord instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SpJSafetyDaysrecord.class,
					entity.getRecordId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SpJSafetyDaysrecord entity and return it or a
	 * copy of it to the sender. A copy of the SpJSafetyDaysrecord entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to update
	 * @return SpJSafetyDaysrecord the persisted SpJSafetyDaysrecord entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyDaysrecord update(SpJSafetyDaysrecord entity) {
		LogUtil.log("updating SpJSafetyDaysrecord instance", Level.INFO, null);
		try {
			SpJSafetyDaysrecord result = entityManager.merge(entity);
			setifBreak(entity.getRecordId());
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJSafetyDaysrecord findById(Long id) {
		LogUtil.log("finding SpJSafetyDaysrecord instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSafetyDaysrecord instance = entityManager.find(
					SpJSafetyDaysrecord.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetyDaysrecord entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyDaysrecord property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyDaysrecord> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetyDaysrecord> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJSafetyDaysrecord instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJSafetyDaysrecord model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<SpJSafetyDaysrecord> findByIfBreak(Object ifBreak) {
		return findByProperty(IF_BREAK, ifBreak);
	}

	public List<SpJSafetyDaysrecord> findBySafetyDays(Object safetyDays) {
		return findByProperty(SAFETY_DAYS, safetyDays);
	}

	public List<SpJSafetyDaysrecord> findByMemo(Object memo) {
		return findByProperty(MEMO, memo);
	}

	public List<SpJSafetyDaysrecord> findByRecordBy(Object recordBy) {
		return findByProperty(RECORD_BY, recordBy);
	}

	public List<SpJSafetyDaysrecord> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all SpJSafetyDaysrecord entities.
	 * 
	 * @return List<SpJSafetyDaysrecord> all SpJSafetyDaysrecord entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount) {
		try {
			 PageObject result = new PageObject();
			 String sql = 
				"select t.record_id,\n" +
				"       t.if_break,\n" + 
				"       t.safety_days,\n" + 
				"       t.memo,\n" + 
				"       t.record_by,\n" + 
				"       to_char(t.start_date, 'yyyy-MM-dd') startDate,\n" + 
				"       to_char(t.end_date, 'yyyy-MM-dd') endDate,\n" + 
				"       to_char(t.record_time,'yyyy-MM-dd') recordDate,\n" + 
				"       getworkername(t.record_by) recorderName\n" + 
				"  from sp_j_safety_daysrecord t\n" + 
				"  where t.enterprise_code = '"+enterpriseCode+"'" +
				"  order by t.record_id ";
			 String sqlCount = "select count(*)\n" +
				"  from sp_j_safety_daysrecord t\n" + 
				"  where t.enterprise_code = '"+enterpriseCode+"'";
			 List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			 Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
			 List arrayList = new ArrayList();
			 Iterator it = list.iterator();
			 while(it.hasNext()){
				 SpJSafetyDaysrecord model = new SpJSafetyDaysrecord();
				 SafetyDaysForm form = new SafetyDaysForm();
				 Object[] data = (Object[])it.next();
				 if(data[0] != null)
					 model.setRecordId(Long.parseLong(data[0].toString()));
				 if(data[1] != null)
					 model.setIfBreak(data[1].toString());
				 if(data[2] != null)
					 model.setSafetyDays(Long.parseLong(data[2].toString()));
				 if(data[3] != null)
					 model.setMemo(data[3].toString());
				 if(data[4] != null)
					 model.setRecordBy(data[4].toString());
				 if(data[5] != null)
					 form.setStartDate(data[5].toString());
				 if(data[6] != null)
					 form.setEndDate(data[6].toString());
				 if(data[7] != null)
					 form.setRecordTime(data[7].toString());
				 if(data[8] != null)
					 form.setRecorderName(data[8].toString());
				 form.setSafetyDaysRecord(model);
				 arrayList.add(form); 
			 }
			 result.setList(arrayList);
			 result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
/**
 * 设置中断
 */
	public void setifBreak(Long id){
		String sql = "update sp_j_safety_daysrecord b set b.if_break='Y' where b.record_id != '"+id+"'";
		bll.exeNativeSQL(sql);
	}
}