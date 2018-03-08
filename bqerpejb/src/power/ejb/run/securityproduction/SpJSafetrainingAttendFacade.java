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


import power.ejb.run.securityproduction.form.SpJSafetrainingAttendForm;
import power.ejb.run.securityproduction.form.SpJSafetrainingForm;

/**
 * Facade for entity SpJSafetrainingAttend.
 * 
 * @see power.ejb.run.securityproduction.SpJSafetrainingAttend
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafetrainingAttendFacade implements
		SpJSafetrainingAttendFacadeRemote {
	// property constants


	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved SpJSafetrainingAttend
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SpJSafetrainingAttend entity) {
		LogUtil.log("saving SpJSafetrainingAttend instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void save(List<SpJSafetrainingAttend> addList) {
		if (addList != null && addList.size() > 0) {
            int i=0;
           Long id=bll.getMaxId("SP_J_SAFETRAINING_ATTEND", "attend_id") ;			
			for (SpJSafetrainingAttend entity : addList) {
                entity.setAttendId(id+i++);
				this.save(entity);
			}
		}
	}
	/**
	 * Delete a persistent SpJSafetrainingAttend entity.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetrainingAttend entity) {
		LogUtil
				.log("deleting SpJSafetrainingAttend instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(SpJSafetrainingAttend.class,
					entity.getAttendId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	public boolean delete(String ids) {
		try {

			String[] temp1 = ids.split(",");

			for (String i : temp1) {
			
				this.delete(this.findById(Long.parseLong(i)));
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * Persist a previously saved SpJSafetrainingAttend entity and return it or
	 * a copy of it to the sender. A copy of the SpJSafetrainingAttend entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to update
	 * @return SpJSafetrainingAttend the persisted SpJSafetrainingAttend entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetrainingAttend update(SpJSafetrainingAttend entity) {
		LogUtil
				.log("updating SpJSafetrainingAttend instance", Level.INFO,
						null);
		try {
			SpJSafetrainingAttend result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void update(List<SpJSafetrainingAttend> updateList) {

		try {
			for (SpJSafetrainingAttend data : updateList) {
				this.update(data);
			}
		} catch (RuntimeException e) {
			throw e;
		}

	}
	public SpJSafetrainingAttend findById(Long id) {
		LogUtil.log("finding SpJSafetrainingAttend instance with id: " + id,
				Level.INFO, null);
		try {
			SpJSafetrainingAttend instance = entityManager.find(
					SpJSafetrainingAttend.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SpJSafetrainingAttend entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetrainingAttend property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetrainingAttend> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetrainingAttend> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding SpJSafetrainingAttend instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SpJSafetrainingAttend model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}



	/**
	 * Find all SpJSafetrainingAttend entities.
	 * 
	 * @return List<SpJSafetrainingAttend> all SpJSafetrainingAttend entities
	 */
	@SuppressWarnings("unchecked")
	public List<SpJSafetrainingAttend> findAll() {
		LogUtil.log("finding all SpJSafetrainingAttend instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from SpJSafetrainingAttend model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PageObject findAll(String trainingId,String enterpriseCode,final int... rowStartIdxAndCount) {
		try {
			 String queryString = "select t.*,getworkername(t.attend_code) workerName," +
			 		" GETDEPTNAME(GETDEPTBYWORKCODE(t.attend_code)) deptName" +
			 		" from SP_J_SAFETRAINING_ATTEND t " +
			 		" where t.enterprise_code='"+enterpriseCode+"'" +
			 				" and t.training_id='"+trainingId+"'" +
			 						" order by t.attend_id";
			 String countsql="select count(*)"+
			 " from SP_J_SAFETRAINING_ATTEND t " +
		 		" where t.enterprise_code='"+enterpriseCode+"'" +
		 				" and t.training_id='"+trainingId+"'" ;
        PageObject result=new PageObject();  
		List list=bll.queryByNativeSQL(queryString,rowStartIdxAndCount);
		
		List<SpJSafetrainingAttendForm> arraylist= new ArrayList<SpJSafetrainingAttendForm>();
		
		Iterator it = list.iterator();
		while (it.hasNext()) {

			Object[] data = (Object[]) it.next();
			SpJSafetrainingAttendForm model = new SpJSafetrainingAttendForm();
			SpJSafetrainingAttend atnInfo=new SpJSafetrainingAttend();
	
			if (data[0] != null) {
				atnInfo.setAttendId(Long.parseLong(data[0].toString()));
			}
			
			if (data[1] != null) {
				atnInfo.setTrainingId(Long.parseLong(data[1].toString()));
			}
			
			if (data[2] != null) {
				atnInfo.setAttendCode(data[2].toString());
			}
			
			if (data[4] != null) {
				model.setWorkerName(data[4].toString());
			}
			if (data[5] != null) {
				model.setDeptName(data[5].toString());
			}
			model.setAtnInfo(atnInfo);
			arraylist.add(model);
		}
		Long count=Long.parseLong(bll.getSingal(countsql).toString());
		
		 result.setList(arraylist);
		 result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void deleteById(Long trainingId){
		String sql = "delete SP_J_SAFETRAINING_ATTEND where training_id = "+trainingId;
		bll.exeNativeSQL(sql);
	}

}