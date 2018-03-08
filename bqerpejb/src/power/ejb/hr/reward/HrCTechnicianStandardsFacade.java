package power.ejb.hr.reward;

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

/**
 * Facade for entity HrCTechnicianStandards.
 * 
 * @see power.ejb.hr.reward.HrCTechnicianStandards
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCTechnicianStandardsFacade implements
		HrCTechnicianStandardsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(HrCTechnicianStandards entity) {
		LogUtil.log("saving HrCTechnicianStandards instance", Level.INFO, null);
		try {
			Long techIdLong = bll.getMaxId("hr_c_technician_standards ",
					"tech_id");
			entity.setTechId(techIdLong);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrCTechnicianStandards entity) {
		LogUtil.log("deleting HrCTechnicianStandards instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(HrCTechnicianStandards.class,
					entity.getTechId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCTechnicianStandards update(HrCTechnicianStandards entity) {
		LogUtil.log("updating HrCTechnicianStandards instance", Level.INFO,
				null);
		try {
			HrCTechnicianStandards result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCTechnicianStandards findById(Long id) {
		LogUtil.log("finding HrCTechnicianStandards instance with id: " + id,
				Level.INFO, null);
		try {
			HrCTechnicianStandards instance = entityManager.find(
					HrCTechnicianStandards.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getTechnicianList(String enterprise) {
		PageObject result = new PageObject();
		String sql = "select t.tech_id,\n" + "       t.tech_standard,\n"
				+ "       t.is_employ,\n" + "       t.effect_start_time,\n"
				+ "       t.effect_end_time,\n" + "       t.memo\n"
				+ "  from hr_c_technician_standards t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterprise + "' \n";
		sql += "order by t.tech_id ";
		List list = bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
	}

	public void deleteTechicianList(String ids) {
		String sql = "update hr_c_technician_standards t "
				+ " set t.is_use='N'" + "where t.tech_id  in (" + ids + ") ";
		bll.exeNativeSQL(sql);
	}

	public void saveTechicianList(List<HrCTechnicianStandards> addList,
			List<HrCTechnicianStandards> updateList) {
		if (addList != null && addList.size() > 0) {

			for (HrCTechnicianStandards entity : addList) {
				this.save(entity);
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrCTechnicianStandards entity : updateList) {
				this.update(entity);
			}
		}
	}

}