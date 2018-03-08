package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity SpJPressureRepair.
 * 
 * @see power.ejb.run.securityproduction.safesupervise.SpJPressureRepair
 * @author sychen 20100423
 */
@Stateless
public class SpJPressureRepairFacade implements SpJPressureRepairFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public PageObject findSpJPressureRepairList(String nextTime, String type,
			String enterpriseCode, String queryName, String startTime,
			String endTime, String fillBy, String isMaint,
			int... rowStartIdxAndCount) {
		// add by ltong
		PageObject pg = new PageObject();
		String strWhere = "";
		String sql = "";
		// update by ltong 20100517
		if ("3".equals(type)) {
			sql += "select t.boiler_repair_id,\n" + "t.boiler_id,\n"
					+ "t.type,\n" + " a.device_name deviceName,\n"
					+ "to_char(t.repair_begin,'yyyy-MM-dd'),\n"
					+ "to_char(t.repair_end,'yyyy-MM-dd'),\n"
					+ "t.report_no,\n" + "t.repair_unit,\n"
					+ "t.repair_result,\n"
					+ "to_char(t.next_time,'yyyy-MM-dd')\n"
					+ "from sp_j_pressure_repair t,pt_ylrq_j_dj a \n"
					+ "where t.is_use='Y'\n"
					+ " and a.device_code=t.boiler_id \n"
					+ "and t.enterprise_code='" + enterpriseCode + "' ";
		} else {
			sql += "select t.boiler_repair_id,\n" + "t.boiler_id,\n"
					+ "t.type,\n" + " a.boiler_name boilerName,\n"
					+ "to_char(t.repair_begin,'yyyy-MM-dd'),\n"
					+ "to_char(t.repair_end,'yyyy-MM-dd'),\n"
					+ "t.report_no,\n" + "t.repair_unit,\n"
					+ "t.repair_result,\n"
					+ "to_char(t.next_time,'yyyy-MM-dd')\n"
					+ "from sp_j_pressure_repair t,SP_C_BOILER a \n"
					+ "where t.is_use='Y'\n"
					+ " and a.boiler_id=t.boiler_id \n"
					+ " and a.is_use='Y' \n" + "and a.enterprise_code='"
					+ enterpriseCode + "' \n" + "and t.enterprise_code='"
					+ enterpriseCode + "' ";
		}

		if (nextTime != null && !nextTime.equals("")) {
			strWhere += " and to_char(t.next_time,'yyyy-MM')='" + nextTime
					+ "' \n";
		}
		if (type != null && !type.equals("")) {
			strWhere += " and t.type='" + type + "'\n";
		}
		if (queryName != null && !queryName.equals("")) {
			strWhere += " and a.boiler_name like '%" + queryName + "%' \n";
		}
		if (startTime != null && !startTime.equals("")) {
			strWhere += " and to_char(t.repair_begin,'yyyy-MM-dd') >='"
					+ startTime + "' \n";
		}
		if (endTime != null && !endTime.equals("")) {
			strWhere += " and to_char(t.repair_end,'yyyy-MM-dd') <='" + endTime
					+ "' \n";
		}
		// add by ltong
		if (isMaint != null && isMaint.equals("1")) {
			strWhere += " and t.fill_by='" + fillBy + "' \n";
		}
		sql += strWhere;
		String sqlCount = "select count(*) from (" + sql + ") ";
		sql += " order by t.boiler_repair_id";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	// add by ltong 20100517
	@SuppressWarnings("unchecked")
	public PageObject findptYlrqJDjList() {
		PageObject pg = new PageObject();
		String sql = "select t.device_code, t.device_name from pt_ylrq_j_dj t";
		String sqlCount = "select count(*) from (" + sql + ") ";
		sql += " order by t.device_code";
		List list = bll.queryByNativeSQL(sql);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public SpJPressureRepair findById(Long id) {
		try {
			SpJPressureRepair instance = entityManager.find(
					SpJPressureRepair.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJPressureRepair saveSpJPressureRepair(SpJPressureRepair entity) {

		try {
			entity.setBoilerRepairId(bll.getMaxId("SP_J_PRESSURE_REPAIR",
					"BOILER_REPAIR_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	public SpJPressureRepair updateSpJPressureRepair(SpJPressureRepair entity) {
		try {
			SpJPressureRepair result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void deleteSpJPressureRepair(String ids) {

		String sql = "update SP_J_PRESSURE_REPAIR t\n"
				+ "   set t.is_use = 'N'\n" + " where t.BOILER_REPAIR_ID in ("
				+ ids + ")";

		bll.exeNativeSQL(sql);

	}

}
