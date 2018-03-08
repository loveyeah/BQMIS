package power.ejb.run.runlog;

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
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.form.WorkticketInfo;

/**
 * Facade for entity RunJEarthtar.
 * 
 * @see power.ejb.run.runlog.RunJEarthtar
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJEarthtarFacade implements RunJEarthtarFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved RunJEarthtar entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEarthtar entity) {
		LogUtil.log("saving RunJEarthtar instance", Level.INFO, null);
		try {
			if(entity.getEarthRecordId() == null)
			{
				entity.setEarthRecordId(bll.getMaxId("run_j_earthtar", "earth_record_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJEarthtar entity.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEarthtar entity) {
		LogUtil.log("deleting RunJEarthtar instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJEarthtar.class, entity
					.getEarthRecordId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJEarthtar entity and return it or a copy of
	 * it to the sender. A copy of the RunJEarthtar entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to update
	 * @returns RunJEarthtar the persisted RunJEarthtar entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEarthtar update(RunJEarthtar entity) {
		LogUtil.log("updating RunJEarthtar instance", Level.INFO, null);
		try {
			RunJEarthtar result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJEarthtar findById(Long id) {
		LogUtil.log("finding RunJEarthtar instance with id: " + id, Level.INFO,
				null);
		try {
			RunJEarthtar instance = entityManager.find(RunJEarthtar.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJEarthtar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEarthtar property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJEarthtar> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEarthtar> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJEarthtar instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJEarthtar model where model."
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
	 * Find all RunJEarthtar entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEarthtar> all RunJEarthtar entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEarthtar> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJEarthtar instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunJEarthtar model";
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
//	public List findInstallListBySpecial(String specialcode,String enterprisecode){
//		String sql="select r.earth_record_id,\n" +
//			"       r.earth_id,\n" + 
//			"       getspecialname(r.speciality_code) speciality_name,\n" + 
//			"       getworkername(r.install_man) install_man,\n" + 
//			"       to_char(r.install_time, 'yyyy-MM-dd hh24:mi:ss') install_time,\n" + 
//			"       r.install_place,\n" + 
//			"       getworkername(r.install_charger) install_charger,\n" + 
//			"       getworkername(r.backout_man) backout_man,\n" + 
//			"       to_char(r.backout_time, 'yyyy-MM-dd hh24:mi:ss') backout_time,\n" + 
//			"       getworkername(r.backout_charger) backout_charger\n" + 
//			"  from run_j_earthtar r\n" + 
//			"  where r.speciality_code='"+specialcode+"'\n" + 
//			"  and r.is_back='N'\n" + 
//			"  and r.enterprise_code='"+enterprisecode+"'\n" + 
//			"  and r.is_use='Y'";
//		return bll.queryByNativeSQL(sql);
//	}
	public PageObject findInstallListBySpecial(String specialcode,String enterprisecode,final int...rowStartIdxAndCount){
		long count=0;
		String sql1="select count(1)\n" +
					"  from run_j_earthtar r\n" + 
					" where r.speciality_code = '"+specialcode+"'\n" + 
					"   and r.is_back = 'N'\n" + 
					"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
					"   and r.is_use = 'Y'";
		Object objcount=bll.getSingal(sql1);
		if(objcount != null)
		{
			count=Long.parseLong(objcount.toString());
		}
		String sql2="select r.earth_record_id,\n" +
					"       r.earth_id,\n" + 
					"       getspecialname(r.speciality_code) speciality_name,\n" + 
					"       getworkername(r.install_man) install_man,\n" + 
					"       to_char(r.install_time, 'yyyy-MM-dd hh24:mi:ss') install_time,\n" + 
					"       r.install_place,\n" + 
					"       getworkername(r.install_charger) install_charger,\n" + 
					"       (select t.earth_name from run_c_earthtar t where t.earth_id=r.earth_id) earth_name,\n" +
					"       getworkername(r.backout_man) backout_man,\n" + 
					"       to_char(r.backout_time, 'yyyy-MM-dd hh24:mi:ss') backout_time,\n" + 
					"       getworkername(r.backout_charger) backout_charger\n" + 
					"  from run_j_earthtar r\n" + 
					"  where r.speciality_code='"+specialcode+"'\n" + 
					"  and r.is_back='N'\n" + 
					"  and r.enterprise_code='"+enterprisecode+"'\n" + 
					"  and r.is_use='Y' order by r.install_time";
		List list=bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
		List<RunJEarthtarForm> arraylist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			RunJEarthtarForm model=new RunJEarthtarForm();
			if(data[0]!=null)
			{
				model.setEarthRecordId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setEarthId(Long.parseLong(data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setSpecialityName(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setInstallManName(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setInstallTime(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setInstallPlace(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setInstallChargerName(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setEarthName(data[7].toString());
			}
			arraylist.add(model);
 		}
		PageObject page=new PageObject();
		page.setList(arraylist);
		page.setTotalCount(count);
		return page;

	}
//	public List queryInstallListBySpecial(String specialcode,String enterprisecode){
//		String sql="select r.earth_record_id,\n" +
//			"       r.earth_id,\n" + 
//			"       getspecialname(r.speciality_code) speciality_name,\n" + 
//			"       getworkername(r.install_man) install_man,\n" + 
//			"       to_char(r.install_time, 'yyyy-MM-dd hh24:mi:ss') install_time,\n" + 
//			"       r.install_place,\n" + 
//			"       getworkername(r.install_charger) install_charger,\n" + 
//			"       getworkername(r.backout_man) backout_man,\n" + 
//			"       to_char(r.backout_time, 'yyyy-MM-dd hh24:mi:ss') backout_time,\n" + 
//			"       getworkername(r.backout_charger) backout_charger\n" + 
//			"  from run_j_earthtar r\n" + 
//			"  where r.speciality_code='"+specialcode+"'\n" + 
//			"  and r.enterprise_code='"+enterprisecode+"'\n" + 
//			"  and r.is_use='Y' order by r.earth_record_id";
//		return bll.queryByNativeSQL(sql);
//	}
	public PageObject queryInstallListBySpecial(String specialcode,String enterprisecode,final int...rowStartIdxAndCount){
		long count=0;
		String sql1="select count(1)\n" +
					"  from run_j_earthtar r\n" + 
					" where r.speciality_code = '"+specialcode+"'\n" + 
					"   and r.enterprise_code = '"+enterprisecode+"'\n" + 
					"   and r.is_use = 'Y' order by r.earth_record_id";
		Object objcount=bll.getSingal(sql1);
		if(objcount != null)
		{
			count=Long.parseLong(objcount.toString());
		}
		String sql2="select r.earth_record_id,\n" +
					"       r.earth_id,\n" + 
					"       getspecialname(r.speciality_code) speciality_name,\n" + 
					"       getworkername(r.install_man) install_man,\n" + 
					"       to_char(r.install_time, 'yyyy-MM-dd hh24:mi:ss') install_time,\n" + 
					"       r.install_place,\n" + 
					"       getworkername(r.install_charger) install_charger,\n" + 
					"       (select t.earth_name from run_c_earthtar t where t.earth_id=r.earth_id) earth_name,\n" +
					"       getworkername(r.backout_man) backout_man,\n" + 
					"       to_char(r.backout_time, 'yyyy-MM-dd hh24:mi:ss') backout_time,\n" + 
					"       getworkername(r.backout_charger) backout_charger\n" + 
					"  from run_j_earthtar r\n" + 
					"  where r.speciality_code='"+specialcode+"'\n" + 
					"  and r.enterprise_code='"+enterprisecode+"'\n" + 
					"  and r.is_use='Y' order by r.earth_record_id";
		List list=bll.queryByNativeSQL(sql2, rowStartIdxAndCount);
		List<RunJEarthtarForm> arraylist=new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
 		{
			Object[] data=(Object[])it.next();
			RunJEarthtarForm model=new RunJEarthtarForm();
			if(data[0]!=null)
			{
				model.setEarthRecordId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setEarthId(Long.parseLong(data[1].toString()));
			}
			if(data[2]!=null)
			{
				model.setSpecialityName(data[2].toString());
			}
			if(data[3]!=null)
			{
				model.setInstallManName(data[3].toString());
			}
			if(data[4]!=null)
			{
				model.setInstallTime(data[4].toString());
			}
			if(data[5]!=null)
			{
				model.setInstallPlace(data[5].toString());
			}
			if(data[6]!=null)
			{
				model.setInstallChargerName(data[6].toString());
			}
			if(data[7]!=null)
			{
				model.setEarthName(data[7].toString());
			}
			if(data[8]!=null)
			{
				model.setBackoutManName(data[8].toString());
			}
			if(data[9]!=null)
			{
				model.setBackoutTime(data[9].toString());
			}
			if(data[10]!=null)
			{
				model.setBackoutCharger(data[10].toString());
			}
			arraylist.add(model);
 		}
		PageObject page=new PageObject();
		page.setList(arraylist);
		page.setTotalCount(count);
		return page;
	}
}