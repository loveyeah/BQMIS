package power.ejb.run.securityproduction;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author slTang
 */
@Stateless
public class SpJAntiAccidentDetailsFacade implements
		SpJAntiAccidentDetailsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(SpJAntiAccidentDetails entity) {
		LogUtil.log("saving SpJAntiAccidentDetails instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		try {
			String sql = "delete SP_J_ANTI_ACCIDENT_DETAILS t where t.DETAILS_ID in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public SpJAntiAccidentDetails update(SpJAntiAccidentDetails entity) {
		LogUtil.log("updating SpJAntiAccidentDetails instance", Level.INFO,
				null);
		try {
			SpJAntiAccidentDetails result = entityManager.merge(entity);		
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJAntiAccidentDetails findById(Long id) {
		LogUtil.log("finding SpJAntiAccidentDetails instance with id: " + id,
				Level.INFO, null);
		try {
			SpJAntiAccidentDetails instance = entityManager.find(
					SpJAntiAccidentDetails.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public void saveModified(List addList, List updateList, String deleteCode,
			String mCode) {
		if (addList != null) {
			Iterator addIt = addList.iterator();
			Long count = bll.getMaxId("SP_J_ANTI_ACCIDENT_DETAILS",
					"DETAILS_ID") - 1;
			while (addIt.hasNext()) {
				Object ob = addIt.next();
				if (ob instanceof SpJAntiAccidentDetails) {
					count++;
					SpJAntiAccidentDetails model = (SpJAntiAccidentDetails) ob;
					model.setDetailsId(count);
					model.setMeasureCode(mCode);
					this.save(model);
				}

			}
		}
		if (updateList != null) {
			Iterator updateIt = updateList.iterator();
			while (updateIt.hasNext()) {
				Object ob = updateIt.next();
				if (ob instanceof SpJAntiAccidentDetails){
					SpJAntiAccidentDetails model = (SpJAntiAccidentDetails) ob;
					this.update(model);
				}
					
			}
		}
		if (deleteCode != null && !deleteCode.equals("")) {
			// deleteCode是用，隔开的字符串集
			this.delete(deleteCode);
		}
	}

	@SuppressWarnings("unchecked")
	public List<SpJAntiAccidentDetails> findByCode(String mCode) {
		String sql = "select * from SP_J_ANTI_ACCIDENT_DETAILS t where t.MEASURE_CODE='"
				+ mCode + "' order by t.DETAILS_ID";
		return bll.queryByNativeSQL(sql, SpJAntiAccidentDetails.class);
	}
}