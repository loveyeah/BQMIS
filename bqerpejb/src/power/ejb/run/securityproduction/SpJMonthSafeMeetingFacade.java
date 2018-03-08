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
import power.ejb.run.securityproduction.form.SpJMonthSafeMeetingFrom;

/**
 * Facade for entity SpJMonthSafeMeeting.
 * 
 * @see power.ejb.run.securityproduction.SpJMonthSafeMeeting
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJMonthSafeMeetingFacade implements
		SpJMonthSafeMeetingFacadeRemote {
	// property constants
	public static final String DEP_CODE = "depCode";
	public static final String MEETING_ADDRESS = "meetingAddress";
	public static final String MODERATOR = "moderator";
	public static final String RECORD_BY = "recordBy";
	public static final String CONTENT = "content";
	public static final String MEMO = "memo";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	public boolean save(SpJMonthSafeMeeting entity) {
		try {
			entity.setMeetingId(dll.getMaxId("SP_J_MONTH_SAFE_MEETING",
					"MEETING_ID"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean delete(SpJMonthSafeMeeting entity) {
		try {
			entity = entityManager.getReference(SpJMonthSafeMeeting.class,
					entity.getMeetingId());
			entityManager.remove(entity);
			String sql1 = "DELETE FROM SP_J_SAFEMEETING_ABSENCE t\n"
					+ " WHERE t.MEETING_ID=" + entity.getMeetingId();
			dll.exeNativeSQL(sql1);
			String sql2 = "DELETE FROM SP_J_SAFEMEETING_ATTEND t\n"
					+ " WHERE t.MEETING_ID=" + entity.getMeetingId();
			dll.exeNativeSQL(sql2);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			return false;
		}
	}

	public boolean update(SpJMonthSafeMeeting entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findByDate(String meetingDate, String enterpriseCode,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.meeting_id,\n"
					+ "       GETDEPTNAME(t.dep_code) dep_name,\n"
					+ "       to_char(t.meeting_date, 'yyyy-mm'),\n"
					+ "       GETWORKERNAME(t.moderator) moderator_name\n"
					+ "  FROM sp_j_month_safe_meeting t\n"
					+ " WHERE to_char(t.meeting_date, 'yyyy') = '"
					+ meetingDate + "'\n" + "   AND t.enterprise_code = '"
					+ enterpriseCode + "'\n"
					+ " ORDER BY t.meeting_date,t.meeting_id";
			String sqlCount = "SELECT COUNT(1)\n"
					+ "  FROM sp_j_month_safe_meeting t\n"
					+ " WHERE to_char(t.meeting_date, 'yyyy') = '"
					+ meetingDate + "'\n" + "   AND t.enterprise_code = '"
					+ enterpriseCode + "' order by t.meeting_id";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				SpJMonthSafeMeetingFrom model = new SpJMonthSafeMeetingFrom();
				SpJMonthSafeMeeting bodel = new SpJMonthSafeMeeting();
				Object[] data = (Object[]) it.next();
				bodel.setMeetingId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					model.setDepName(data[1].toString());
				if (data[2] != null)
					model.setMeetingDateString(data[2].toString());
				if (data[3] != null)
					model.setModeratorName(data[3].toString());
				model.setMeetingInfo(bodel);
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

	public SpJMonthSafeMeetingFrom findFromModel(Long id) {
		try {
			SpJMonthSafeMeetingFrom entity = new SpJMonthSafeMeetingFrom();
			String sql = "SELECT t.meeting_id,\n"
					+ "       t.dep_code,\n"
					+ "       GETDEPTNAME(t.dep_code) dep_name,\n"
					+ "       t.meeting_date,\n"
					+ "       t.meeting_address,\n"
					+ "       t.moderator,\n"
					+ "       GETWORKERNAME(t.moderator) moderator_name,\n"
					+ "       t.record_by,\n"
					+ "       GETWORKERNAME(t.record_by) record_by_name,\n"
					+ "       t.content,\n"
					+ "       t.memo,\n"
					+ "       to_char(t.meeting_date, 'yyyy-mm') meetingDatestring\n"
					+ "  FROM sp_j_month_safe_meeting t\n"
					+ " WHERE t.meeting_id = " + id;
			Object[] data = (Object[]) dll.getSingal(sql);
			SpJMonthSafeMeeting model = new SpJMonthSafeMeeting();

			model.setMeetingId(id);
			if (data[1] != null)
				model.setDepCode(data[1].toString());
			if (data[2] != null)
				entity.setDepName(data[2].toString());
			if (data[3] != null)
				model.setMeetingDate(java.sql.Date.valueOf(data[3].toString()));
			if (data[4] != null)
				model.setMeetingAddress(data[4].toString());
			if (data[5] != null)
				model.setModerator(data[5].toString());
			if (data[6] != null)
				entity.setModeratorName(data[6].toString());
			if (data[7] != null)
				model.setRecordBy(data[7].toString());
			if (data[8] != null)
				entity.setRecordByName(data[8].toString());
			if (data[9] != null)
				model.setContent(data[9].toString());
			if (data[10] != null)
				model.setMemo(data[10].toString());
			if (data[11] != null)
				entity.setMeetingDateString(data[11].toString());
			entity.setMeetingInfo(model);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJMonthSafeMeeting findModel(Long id) {
		try {
			SpJMonthSafeMeeting instance = entityManager.find(
					SpJMonthSafeMeeting.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
}