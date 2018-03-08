package power.ejb.run.securityproduction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.run.securityproduction.form.SpJSafemeetingAbsenceForm;

/**
 * Facade for entity SpJSafemeetingAbsence.
 * 
 * @see power.ejb.run.securityproduction.SpJSafemeetingAbsence
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJSafemeetingAbsenceFacade implements
		SpJSafemeetingAbsenceFacadeRemote {
	// property constants
	public static final String MEETING_ID = "meetingId";
	public static final String WORKER_CODE = "workerCode";
	public static final String DEP_CODE = "depCode";
	public static final String REASON = "reason";
	public static final String MAKEUP_RECORD = "makeupRecord";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	public boolean save(SpJSafemeetingAbsence entity) {
		try {
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "DELETE FROM sp_j_safemeeting_absence t\n"
					+ " WHERE t.absence_id IN (" + ids + ")";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean update(SpJSafemeetingAbsence entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean saveAbsList(List<SpJSafemeetingAbsence> addList,
			List<SpJSafemeetingAbsence> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll
						.getMaxId("sp_j_safemeeting_absence", "absence_id");
				int i = 0;
				for (SpJSafemeetingAbsence entity : addList) {
					entity.setAbsenceId(id + (i++));
					this.save(entity);
				}
			}
			for (SpJSafemeetingAbsence entity : updateList) {
				this.update(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getAbsList(String meetingId, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.absence_id,\n" + "       t.meeting_id,\n"
					+ "       t.worker_code,\n"
					+ "       GETWORKERNAME(t.worker_code) worker_name,\n"
					+ "       t.dep_code,\n"
					+ "       GETDEPTNAME(t.dep_code) dep_name,\n"
					+ "       t.reason,\n" + "       t.makeup_record\n"
					+ "  FROM sp_j_safemeeting_absence t\n"
					+ " WHERE t.meeting_id = " + meetingId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'";
			String sqlCount = "SELECT COUNT(1)\n"
					+ "  FROM sp_j_safemeeting_absence t\n"
					+ " WHERE t.meeting_id = " + meetingId + "\n"
					+ "   AND t.enterprise_code = '" + enterpriseCode + "'";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJSafemeetingAbsenceForm model = new SpJSafemeetingAbsenceForm();
				SpJSafemeetingAbsence bodel = new SpJSafemeetingAbsence();
				Object[] data = (Object[]) it.next();
				bodel.setAbsenceId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					bodel.setMeetingId(Long.parseLong(data[1].toString()));
				if (data[2] != null)
					bodel.setWorkerCode(data[2].toString());
				if (data[3] != null)
					model.setAbsName(data[3].toString());
				if (data[4] != null)
					bodel.setDepCode(data[4].toString());
				if (data[5] != null)
					model.setAbsDep(data[5].toString());
				if (data[6] != null)
					bodel.setReason(data[6].toString());
				if (data[7] != null)
					bodel.setMakeupRecord(data[7].toString());
				model.setAbsInfo(bodel);
				arrlist.add(model);
			}
			result.setList(arrlist);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}
}