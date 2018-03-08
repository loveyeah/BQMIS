package power.ejb.productiontec.technologySupervise;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.productiontec.technologySupervise.form.PtJJdwcyForm;

/**
 * Facade for entity PtJJdwcy.
 * 
 * @see power.ejb.productiontec.technologySupervise.PtJJdwcy
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJJdwcyFacade implements PtJJdwcyFacadeRemote {
	// property constants
	public static final String WORKER_CODE = "workerCode";
	public static final String JDZY_ID = "jdzyId";
	public static final String NET_DUTY = "netDuty";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtJJdwcy entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdwcy entity) {
		LogUtil.log("saving PtJJdwcy instance", Level.INFO, null);
		try {
			entity.setJdwcyId(bll.getMaxId("PT_J_JDWCY", "JDWCY_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJJdwcy entity.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteMulti(String ids) {
		String sql=
			"delete PT_J_JDWCY t\n" +
			"where t.JDWCY_ID in ("+ids+")";
		bll.exeNativeSQL(sql); 
	}

	/**
	 * Persist a previously saved PtJJdwcy entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdwcy entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to update
	 * @return PtJJdwcy the persisted PtJJdwcy entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdwcy update(PtJJdwcy entity) {
		LogUtil.log("updating PtJJdwcy instance", Level.INFO, null);
		try {
			PtJJdwcy result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJJdwcy findById(Long id) {
		LogUtil.log("finding PtJJdwcy instance with id: " + id, Level.INFO,
				null);
		try {
			PtJJdwcy instance = entityManager.find(PtJJdwcy.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJJdwcy entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdwcy property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdwcy> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJJdwcy> findByProperty(String propertyName, final Object value) {
		LogUtil.log("finding PtJJdwcy instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJJdwcy model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<PtJJdwcy> findByWorkerCode(Object workerCode) {
		return findByProperty(WORKER_CODE, workerCode);
	}

	public List<PtJJdwcy> findByJdzyId(Object jdzyId) {
		return findByProperty(JDZY_ID, jdzyId);
	}

	public List<PtJJdwcy> findByNetDuty(Object netDuty) {
		return findByProperty(NET_DUTY, netDuty);
	}

	public List<PtJJdwcy> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all PtJJdwcy entities.
	 * 
	 * @return List<PtJJdwcy> all PtJJdwcy entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String jdzyId,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount = 
			"select count(*)\n" +
			"  from pt_j_jdwcy t\n" + 
			" where t.jdzy_id = "+jdzyId+"\n" + 
			"   and t.enterprise_code ='"+enterpriseCode+"'";
		String sql = 
			"select t.*,\n" +
			"       getworkername(t.worker_code) workerName,\n" + 
			"       GETDEPTNAME(GETDEPTBYWORKCODE(t.worker_code)) deptName\n" + 
			"  from pt_j_jdwcy t\n" + 
			" where t.jdzy_id ="+jdzyId+"\n" + 
			"   and t.enterprise_code ='"+enterpriseCode+"'";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount > 0){
			PageObject obj = new PageObject();
			obj.setTotalCount(totalCount);
			List<PtJJdwcyForm>  list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrylist = new ArrayList();
			Iterator it = list.iterator();
			if(list != null && list.size() > 0){
				while (it.hasNext()){
					PtJJdwcyForm form = new PtJJdwcyForm();
					PtJJdwcy model = new PtJJdwcy();
					Object[] data = (Object[]) it.next();
					model.setJdwcyId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						model.setWorkerCode(data[1].toString());
					if (data[2] != null)
						model.setJdzyId(Long.parseLong(data[1].toString()));
					if (data[3] != null)
						model.setNetDuty(data[3].toString());
					if (data[5] != null)
						form.setWorkerName(data[5].toString());
					if (data[6] != null)
						form.setDeptName(data[6].toString());
					form.setModel(model);
					arrylist.add(form);
				}
			}
			obj.setList(arrylist);
			return obj;
		}else{
			return null;
		}
	}

}