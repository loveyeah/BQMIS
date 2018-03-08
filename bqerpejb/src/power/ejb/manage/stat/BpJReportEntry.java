package power.ejb.manage.stat;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import power.ejb.manage.stat.form.StatItemComputeForm;
import power.ejb.manage.stat.form.StatItemEntry;

import com.opensymphony.db.DBHelper;

@Stateless
public class BpJReportEntry implements BpJReportEntryImpl {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 是否停止计算
	 */
	private static boolean isStopping = false;
	public static void stopComplute()
	{
		isStopping = true;
	}
	public static void resetStop(){
		isStopping = false; 
	}
	public static boolean getIsStopping(){
		return isStopping;
	}
	                                      
	

	@SuppressWarnings("unchecked")
	public List<StatItemEntry> findEntryItemListByReport(String dateType,
			String reportCode, String date, String delayTime, String delayUnit,
			String enterpriseCode) throws ParseException {
		try {
			String tableName = "";
			int timedot = 0;
			String delayTo = "";
			SimpleDateFormat format;
			if ("1".equals(dateType)) {
				format = new SimpleDateFormat("yyyy-MM-dd HH");// 时指标
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd");
			}
			String nowDate = format.format(new Date());
			Date claimDate = format.parse(date);
			Date delayDate = claimDate;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(claimDate);
			switch (Integer.parseInt(dateType)) {
			case 1:
				tableName = "bp_j_stat_stz";
				timedot = calendar.get(calendar.HOUR_OF_DAY);
				break;
			case 3:
				tableName = "bp_j_stat_rtz";
				timedot = calendar.get(calendar.DAY_OF_MONTH);
				break;
			case 4:
				tableName = "bp_j_stat_ytz";// 月延迟时间从月最后一天开始
				timedot = calendar.get(calendar.MONTH) + 1;
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.getActualMaximum(calendar.DAY_OF_MONTH));
				break;
			case 5:
				tableName = "bp_j_stat_jtz";// 季延迟时间从月最后一天开始
				int month = calendar.get(calendar.MONTH) + 1;
				if (month < 4) {
					timedot = 1;
					calendar.set(calendar.MONTH, 2);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else if (month < 7) {
					timedot = 2;
					calendar.set(calendar.MONTH, 5);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else if (month < 10) {
					timedot = 3;
					calendar.set(calendar.MONTH, 8);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else {
					timedot = 4;
					calendar.set(calendar.MONTH, 11);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				}
				break;
			case 6:
				tableName = "bp_j_stat_ntz";
				timedot = 1;
				calendar.set(calendar.MONTH, 11);
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.getActualMaximum(calendar.DAY_OF_MONTH));
				break;
			}
			switch (Integer.parseInt(delayUnit)) {
			case 1:
				calendar.add(calendar.HOUR_OF_DAY, Integer.parseInt(delayTime));
				break;
			case 3:
				calendar
						.add(calendar.DAY_OF_MONTH, Integer.parseInt(delayTime));
				break;
			case 4:
				calendar.add(calendar.MONTH, Integer.parseInt(delayTime));
				break;
			}
			delayDate.setTime(calendar.getTimeInMillis());
			delayTo = format.format(delayDate);
			String sql = "select t.item_code,\n" + "       a.item_name,\n"
					+ "       a.unit_code,\n"
					+ "       getunitname(a.unit_code) unit_name,\n"
					+ "       (select r.data_value\n" + "          from "
					+ tableName
					+ " r\n"
					+ "         where r.item_code = t.item_code\n"
					+ "           and r.data_date = to_date('"
					+ date
					+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value,\n"
					+ "       t.data_type\n"
					+ "  from bp_c_input_report_item  t,\n"
					+ "       bp_c_input_report_setup n,\n"
					+ "       bp_c_stat_item          a\n"
					+ " where t.report_code = '"
					+ reportCode
					+ "'\n"
					+ "   and a.item_code = t.item_code\n"
					+ "   and n.report_code = t.report_code\n"
					+ "   and n.data_time_dot = "
					+ timedot
					+ "\n"
					+ "   and n.if_collect = 1\n"
					+ "   and to_date('"
					+ nowDate
					+ "', 'yyyy-MM-dd hh24') <= to_date('"
					+ delayTo
					+ "', 'yyyy-MM-dd hh24')\n"
					+ "   and n.enterprise_code = t.enterprise_code\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			List list = bll.queryByNativeSQL(sql);
			List<StatItemEntry> arraylist = new ArrayList();
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Object[] o = (Object[]) i.next();
				StatItemEntry model = new StatItemEntry();
				String empty = "";
				if (o[0] != null) {
					model.setItemCode(o[0].toString());
				} else {
					model.setItemCode("");
				}
				if (o[1] != null) {
					model.setItemName(o[1].toString());
				} else {
					model.setItemName("");
				}
				if (o[2] != null) {
					model.setUnitCode(o[2].toString());
				} else {
					model.setUnitCode("");
				}
				if (o[3] != null) {
					model.setUnitName(o[3].toString());
				} else {
					model.setUnitName("");
				}
				if (o[4] != null) {
					model.setDataValue(Double.parseDouble(o[4].toString()));
				}
				if (o[5] != null) {
					model.setDataType(o[5].toString());
				} else {
					model.setDataType("");
				}
				arraylist.add(model);

			}
			return arraylist;

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<StatItemEntry> findAddEntryByReport(String dateType,
			String reportCode, String date, String delayTime, String delayUnit,
			String enterpriseCode) throws ParseException {
		try {
			String tableName = "";
			int timedot = 0;
			String delayTo = "";
			SimpleDateFormat format;
			if ("1".equals(dateType)) {
				format = new SimpleDateFormat("yyyy-MM-dd HH");// 时指标
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd");
			}
			String nowDate = format.format(new Date());
			Date claimDate = format.parse(date);
			Date delayDate = claimDate;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(claimDate);
			switch (Integer.parseInt(dateType)) {
			case 1:
				tableName = "bp_j_stat_stz";
				timedot = calendar.get(calendar.HOUR_OF_DAY);
				break;
			case 3:
				tableName = "bp_j_stat_rtz";
				timedot = calendar.get(calendar.DAY_OF_MONTH);
				break;
			case 4:
				tableName = "bp_j_stat_ytz";// 月延迟时间从月最后一天开始
				timedot = calendar.get(calendar.MONTH) + 1;
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.getActualMaximum(calendar.DAY_OF_MONTH));
				break;
			case 5:
				tableName = "bp_j_stat_jtz";// 季延迟时间从月最后一天开始
				int month = calendar.get(calendar.MONTH) + 1;
				if (month < 4) {
					timedot = 1;
					calendar.set(calendar.MONTH, 2);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else if (month < 7) {
					timedot = 2;
					calendar.set(calendar.MONTH, 5);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else if (month < 10) {
					timedot = 3;
					calendar.set(calendar.MONTH, 8);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				} else {
					timedot = 4;
					calendar.set(calendar.MONTH, 11);
					calendar.set(calendar.DAY_OF_MONTH, calendar
							.getActualMaximum(calendar.DAY_OF_MONTH));
				}
				break;
			case 6:
				tableName = "bp_j_stat_ntz";
				timedot = 1;
				calendar.set(calendar.MONTH, 11);
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.getActualMaximum(calendar.DAY_OF_MONTH));
				break;
			}
			switch (Integer.parseInt(delayUnit)) {
			case 1:
				calendar.add(calendar.HOUR_OF_DAY, Integer.parseInt(delayTime));
				break;
			case 3:
				calendar
						.add(calendar.DAY_OF_MONTH, Integer.parseInt(delayTime));
				break;
			case 4:
				calendar.add(calendar.MONTH, Integer.parseInt(delayTime));
				break;
			}
			delayDate.setTime(calendar.getTimeInMillis());
			delayTo = format.format(delayDate);
			String sql = "select t.item_code,\n" + "       a.item_name,\n"
					+ "       a.unit_code,\n"
					+ "       getunitname(a.unit_code) unit_name,\n"
					+ "       (select r.data_value\n" + "          from "
					+ tableName
					+ " r\n"
					+ "         where r.item_code = t.item_code\n"
					+ "           and r.data_date = to_date('"
					+ date
					+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value,\n"
					+ "       t.data_type\n"
					+ "  from bp_c_input_report_item  t,\n"
					+ "       bp_c_input_report_setup n,\n"
					+ "       bp_c_stat_item          a\n"
					+ " where t.report_code = '"
					+ reportCode
					+ "'\n"
					+ "   and a.item_code = t.item_code\n"
					+ "   and n.report_code = t.report_code\n"
					+ "   and n.data_time_dot = "
					+ timedot
					+ "\n"
					+ "   and n.if_collect = 1\n"
					+ "   and to_date('"
					+ nowDate
					+ "', 'yyyy-MM-dd hh24') > to_date('"
					+ delayTo
					+ "', 'yyyy-MM-dd hh24')\n"
					+ "   and n.enterprise_code = t.enterprise_code\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			List list = bll.queryByNativeSQL(sql);
			List<StatItemEntry> arraylist = new ArrayList();
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Object[] o = (Object[]) i.next();
				StatItemEntry model = new StatItemEntry();
				String empty = "";
				if (o[0] != null) {
					model.setItemCode(o[0].toString());
				} else {
					model.setItemCode("");
				}
				if (o[1] != null) {
					model.setItemName(o[1].toString());
				} else {
					model.setItemName("");
				}
				if (o[2] != null) {
					model.setUnitCode(o[2].toString());
				} else {
					model.setUnitCode("");
				}
				if (o[3] != null) {
					model.setUnitName(o[3].toString());
				} else {
					model.setUnitName("");
				}
				if (o[4] != null) {
					model.setDataValue(Double.parseDouble(o[4].toString()));
				}
				if (o[5] != null) {
					model.setDataType(o[5].toString());
				} else {
					model.setDataType("");
				}
				arraylist.add(model);

			}
			return arraylist;

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject reportQuery(String reportCode, String startDate,
			String timeType, String enterpriseCode) {
		try {
			String tableName = "";
			switch (Integer.parseInt(timeType)) {
			case 1:
				tableName = "bp_j_stat_stz";
				break;
			case 3:
				tableName = "bp_j_stat_rtz";
				break;
			case 4:
				tableName = "bp_j_stat_ytz";
				break;
			case 5:
				tableName = "bp_j_stat_jtz";
				break;
			case 6:
				tableName = "bp_j_stat_ntz";
				break;
			}
			String countsql = "select count(*)\n"
					+ "  from bp_c_input_report_item  t\n"
					+ " where t.report_code = '" + reportCode + "'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			String sql = "select t.item_code,\n" + "       a.item_name,\n"
					+ "       a.unit_code,\n"
					+ "       getunitname(a.unit_code) unit_name,\n"
					+ "       (select r.data_value\n" + "          from "
					+ tableName + " r\n"
					+ "         where r.item_code = t.item_code\n"
					+ "           and r.data_date = to_date('" + startDate
					+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value\n"
					+ "  from bp_c_input_report_item  t,\n"
					+ "       bp_c_stat_item          a\n"
					+ " where t.report_code = '" + reportCode + "'\n"
					+ "   and a.item_code = t.item_code\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			Object count = bll.getSingal(countsql);
			List list = bll.queryByNativeSQL(sql);
			List<StatItemEntry> arraylist = new ArrayList();
			Iterator i = list.iterator();
			while (i.hasNext()) {
				Object[] o = (Object[]) i.next();
				StatItemEntry model = new StatItemEntry();
				String empty = "";
				if (o[0] != null) {
					model.setItemCode(o[0].toString());
				} else {
					model.setItemCode("");
				}
				if (o[1] != null) {
					model.setItemName(o[1].toString());
				} else {
					model.setItemName("");
				}
				if (o[2] != null) {
					model.setUnitCode(o[2].toString());
				} else {
					model.setUnitCode("");
				}
				if (o[3] != null) {
					model.setUnitName(o[3].toString());
				} else {
					model.setUnitName("");
				}
				if (o[4] != null) {
					model.setDataValue(Double.parseDouble(o[4].toString()));
				}
				arraylist.add(model);
			}
			PageObject obj = new PageObject();
			obj.setList(arraylist);
			obj.setTotalCount(Long.parseLong(count.toString()));
			return obj;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void saveReportEntryItemValue(List<StatItemEntry> list,
			String timeType) {
		String tableName = "";
		switch (Integer.parseInt(timeType)) {
		case 1:
			tableName = "bp_j_stat_stz";
			break;
		// add by liuyi 20100513 班组指标
		case 2:
			tableName = "bp_j_stat_stz";
			break;
		case 3:
			tableName = "bp_j_stat_rtz";
			break;
		case 4:
			tableName = "bp_j_stat_ytz";
			break;
		case 5:
			tableName = "bp_j_stat_jtz";
			break;
		case 6:
			tableName = "bp_j_stat_ntz";
			break;
		}
		for (StatItemEntry model : list) {
			String sql1 = "select *\n" + "  from " + tableName + " r\n"
					+ " where r.item_code = '" + model.getItemCode() + "'\n"
					+ "   and r.data_date = to_date('" + model.getDataDate()
					+ "', 'yyyy-MM-dd hh24:mi:ss')";
			String sql2 = "INSERT INTO " + tableName + "\n"
					+ "  (item_code, data_date, data_value, adjust_value)\n"
					+ "VALUES\n" + "  ('" + model.getItemCode()
					+ "', to_date('" + model.getDataDate()
					+ "', 'yyyy-MM-dd hh24:mi:ss'), " + model.getDataValue()
					+ ", 0)";

			String sql3 = "update " + tableName + " r\n"
					+ "     set r.data_value = " + model.getDataValue() + "\n"
					+ "   where r.item_code = '" + model.getItemCode() + "'\n"
					+ "     and r.data_date = to_date('" + model.getDataDate()
					+ "', 'yyyy-MM-dd hh24:mi:ss')";

			Object o = bll.getSingal(sql1);
			if (o == null) {
				bll.exeNativeSQL(sql2);
			} else {
				bll.exeNativeSQL(sql3);
			}
		}
	}

	public void collectCompute(String type) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DBHelper.getConn();
			CallableStatement cs = conn
					.prepareCall("call proc_zbjs_testing.test(?)");
			// 传入入口参数
			// cs.setString(1,"aaaaaaaa");
			Date xx = new Date();
			// cs.setString(1,"2009-6-18 15");
			cs.setDate(1, new java.sql.Date(xx.getTime()));
			// cs.setString(3, "11.0000");
			// 注册返回参数
			// cs.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR);
			cs.execute();
			// 获取返回游标，返回类型为ResultSet
			// rs = (ResultSet)cs.getObject(3);
			// while (rs.next()) {
			// System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t"
			// + rs.getString(3));
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	// modified by liuyi 20100513 增加班组指标 班组描述 desc
//	public List<StatItemEntry> findReportItemValueListForEntry(
//			String reportCode, int timedot, String date, String tableName,
//			String enterpriseCode) {
	@SuppressWarnings("unchecked")
	public List<StatItemEntry> findReportItemValueListForEntry(
			String reportCode, int timedot, String date, String tableName,
			String enterpriseCode, String desc) {
		List<StatItemEntry> arraylist = new ArrayList();
		String sql = "";
		if(desc == null || desc.equals(""))
		{
			sql = "select t.item_code,\n"
					+ "       (select r.data_value\n"
					+ "          from "
					+ tableName
					+ " r\n"
					+ "         where r.item_code = t.item_code\n"
					+ "           and r.data_date = to_date('"
					+ date
					+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value,\n"
					+ "		t.data_type\n"
					+ "  from bp_c_input_report_item t,bp_c_input_report_setup t2\n"
					+ " where t.report_code = " + reportCode + "\n"
					+ "   and t2.report_code=t.report_code\n"
					+ "   and t2.data_time_dot=" + timedot + "\n"
					+ "   and t2.if_collect=1\n"
					+ "   and t2.enterprise_code=t.enterprise_code\n"
					+ "   and t.enterprise_code = '" + enterpriseCode
					+ "' order by t.display_no";
		}else{
			sql = "select t.item_code,\n"
				+ "       (select r.data_value\n"
				+ "          from "
				+ tableName
				+ " r\n"
				+ "         where r.item_code = t.item_code\n"
				+ "           and r.data_date = to_date('"
				+ date
				+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value,\n"
				+ "		t.data_type\n"
				+ "  from bp_c_input_report_item t\n"
				+ " where t.report_code = " + reportCode + "\n"
				+ "   and t.enterprise_code = '" + enterpriseCode
				+ "' order by t.display_no";
	}
		List list = bll.queryByNativeSQL(sql);
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			StatItemEntry model = new StatItemEntry();
			if (o[0] != null) {
				model.setItemCode(o[0].toString());
			} else {
				model.setItemCode("");
			}
			if (o[2] != null) {
				model.setDataType(o[2].toString());
			} else {
				model.setDataType("");
			}
			if (o[1] != null) {
				model.setDataValue(Double.parseDouble(o[1].toString()));
			}
			model.setDataDate(date);
			// add by liuyi 20100513
			model.setBanzuDesc(desc);
			arraylist.add(model);
		}
		return arraylist;
	}

	public List<StatItemEntry> findReportItemListForEntry(String reportCode,
			String enterpriseCode) {
		String sql = "select t.item_code,\n" + "       t.item_alias,\n"
				+ "       r.unit_code,\n"
				+ "       getunitname(r.unit_code) unit_name,\n"
				+ "       t.report_code,\n" + "       t.ITEM_BASE_NAME\n"
				+ "  from bp_c_stat_item r, bp_c_input_report_item t\n"
				+ "  where r.item_code=t.item_code\n" + "  and t.report_code="
				+ reportCode + "\n"
				+ "  and r.enterprise_code=t.enterprise_code\n"
				+ "  and t.enterprise_code='" + enterpriseCode
				+ "' order by t.display_no";
		List list = bll.queryByNativeSQL(sql);
		List<StatItemEntry> arraylist = new ArrayList();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			StatItemEntry model = new StatItemEntry();
			if (o[0] != null) {
				model.setItemCode(o[0].toString());
			} else {
				model.setItemCode("");
			}
			if (o[1] != null) {
				model.setItemName(o[1].toString());
			} else {
				model.setItemName("");
			}
			if (o[3] != null) {
				model.setUnitName(o[3].toString());
			} else {
				model.setUnitName("");
			}
			if (o[5] != null) {
				model.setItemBaseName(o[5].toString());
			} else {
				model.setItemBaseName("");
			}
			arraylist.add(model);
		}
		return arraylist;
	}

	public List<StatItemEntry> findAccountItemListForEntry(String reportCode,
			String enterpriseCode) {
		String sql = "select t.item_code,\n" + "       t.item_alias,\n"
				+ "       r.unit_code,\n"
				+ "       getunitname(r.unit_code) unit_name,\n"
				+ "       t.account_code,\n"
				+ "       t.ITEM_BASE_NAME,r.compute_method\n"
				+ "  from bp_c_stat_item r, bp_c_analyse_account_item t\n"
				+ "  where r.item_code=t.item_code\n" + "  and t.account_code="
				+ reportCode + "\n"
				+ "  and r.enterprise_code=t.enterprise_code\n"
				+ "  and t.enterprise_code='" + enterpriseCode
				+ "' order by t.display_no";
		List list = bll.queryByNativeSQL(sql);
		List<StatItemEntry> arraylist = new ArrayList();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			StatItemEntry model = new StatItemEntry();
			if (o[0] != null) {
				model.setItemCode(o[0].toString());
			} else {
				model.setItemCode("");
			}
			if (o[1] != null) {
				model.setItemName(o[1].toString());
			} else {
				model.setItemName("");
			}
			if (o[3] != null) {
				model.setUnitName(o[3].toString());
			} else {
				model.setUnitName("");
			}
			if (o[5] != null) {
				model.setItemBaseName(o[5].toString());
			} else {
				model.setItemBaseName("");
			}
			if (o[6] != null) {
				model.setDataType(o[6].toString());// 存指标的计算方法，合计是用
			} else {
				model.setDataType("");
			}
			arraylist.add(model);
		}
		return arraylist;
	}

	public List<StatItemEntry> findAccountItemValueListForEntry(
			String reportCode, int timedot, String date, String tableName,
			String enterpriseCode) {
		List<StatItemEntry> arraylist = new ArrayList();
		String sql = "select t.item_code,\n"
				+ "       (select r.data_value\n"
				+ "          from "
				+ tableName
				+ " r\n"
				+ "         where r.item_code = t.item_code\n"
				+ "           and r.data_date = to_date('"
				+ date
				+ "', 'yyyy-MM-dd hh24:mi:ss')) data_value,\n"
				+ "		t.data_type\n"
				+ "  from bp_c_analyse_account_item t,bp_c_analyse_account_setup t2\n"
				+ " where t.account_code = " + reportCode + "\n"
				+ "   and t2.account_code=t.account_code\n"
				+ "   and t2.data_time_dot=" + timedot + "\n"
				+ "   and t2.if_collect=1\n"
				+ "   and t2.enterprise_code=t.enterprise_code\n"
				+ "   and t.enterprise_code = '" + enterpriseCode
				+ "' order by t.display_no";
		List list = bll.queryByNativeSQL(sql);
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			StatItemEntry model = new StatItemEntry();
			if (o[0] != null) {
				model.setItemCode(o[0].toString());
			} else {
				model.setItemCode("");
			}
			if (o[2] != null) {
				model.setDataType(o[2].toString());
			} else {
				model.setDataType("");
			}
			if (o[1] != null) {
				model.setDataValue(Double.parseDouble(o[1].toString()));
			}
			model.setDataDate(date);
			arraylist.add(model);
		}
		return arraylist;
	}

	public void  statItemCollectCompute(StatItemComputeForm model,
			String enterpriseCode) throws Exception {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		conn = DBHelper.getConn();
		SimpleDateFormat format;
		String sql = "";
		try {
			//start 处理录入月指标数据
			if("true".equals(model.getIsCompute()))
			{
				List<String> yearMonth = new ArrayList<String>(); 
				String start;
				String end;
				if (model.getIsTime().equals("true"))// 时指标
				{
					start = model.getStartTime().substring(0, 7);
					end = model.getEndTime().substring(0, 7); 
					this.getBetweenYearMonth(start, end, yearMonth);
				}
				if (model.getIsGroup().equals("true")) // 分组指标
				{ 
					start = model.getGroupStartDate().substring(0, 7);
					end = model.getGroupEndDate().substring(0, 7); 
					this.getBetweenYearMonth(start, end, yearMonth);
				}
				if (model.getIsDate().equals("true"))// 日指标
				{ 
					start = model.getStartDate().substring(0, 7);
					end = model.getEndDate().substring(0, 7); 
					this.getBetweenYearMonth(start, end, yearMonth); 
					
				}
				if (model.getIsMonth().equals("true"))// 月指标
				{
					
					start = model.getStartMonth();
					end = model.getEndMonth() ; 	 
					this.getBetweenYearMonth(start, end, yearMonth); 
				}
				if(yearMonth.size()>0)
				{
					String ym = "";
					for(String s: yearMonth)
					{
						ym += s+",";
					}
					ym = ym.substring(0,ym.length()-1);
					CallableStatement cs = conn.prepareCall("call proc_jhtj2_yxzbjs.jhtj_yxzb_month_handle(?)");
					cs.setString(1, ym);
					cs.execute();
					cs.close();
					System.out.println("录入月指标数据处理完毕。");
				}
			}
			//end
			
			
			if (model.getIsTime().equals("true") )// 时指标
			{ 
				format = new SimpleDateFormat("yyyy-MM-dd HH");
				Date stime = format.parse(model.getStartTime());
				Date etime = format.parse(model.getEndTime());
				long second2 = etime.getTime() - stime.getTime();
				long hour = second2 / (1000 * 60 * 60);
				if (model.getIsCollect().equals("true")) {
					System.out.println("正在执行采集程序----");
					format = new SimpleDateFormat("yyyy-MM-dd");
					Date sdate = format.parse(model.getStartTime());
					Date edate = format.parse(model.getEndTime());
					long second22 = edate.getTime() - sdate.getTime();
					long day = second22 / (1000 * 60 * 60 * 24);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdate);
					CallableStatement cs = conn
							.prepareCall("call proc_jhtj2_yxzbjs.jhtj_yxzb_collect_day(?)");
					int j = 1;
					for (int i = 1; i <= day + 1; i++) {
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						calendar.set(calendar.DAY_OF_MONTH, calendar
								.get(calendar.DAY_OF_MONTH) + 1);
						cs.execute();
						System.out.println("已采集完成"
								+ format.format(calendar.getTime()) + " "
								+ "一天的数据；");
					}
					cs.close();
					System.out.println("采集程序执行完毕----");
					System.out.println("停机处理start");
					
					 cs = conn
						.prepareCall("call proc_jhtj2_yxzbjs.jhtj_yxzb_stop_handle(?,?)");
					 cs.setTimestamp(1,new java.sql.Timestamp(stime.getTime()));
					 cs.setTimestamp(2,new java.sql.Timestamp(etime.getTime()));
					 cs.execute();
					 cs.close();
					System.out.println("停机处理end");
				}
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在执行时指标计算程序----");
					CallableStatement cs = conn
							.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(stime);
					int j = 1;
					for (int i = 1; i <= hour + 1; i++) {
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "1");
						cs.addBatch();
						calendar.set(calendar.HOUR_OF_DAY, calendar
								.get(calendar.HOUR_OF_DAY) + 1);
						if (i % 24 == 0) {
							cs.executeBatch();
							cs.clearBatch();
							System.out.println("已计算完成"
									+ format.format(calendar.getTime()) + " "
									+ 24 * (j++) + "个时间点的数据；");
						}
					}
					cs.executeBatch();
					cs.close();
					System.out.println("时指标计算程序执行完毕----");
				}

				// for (int i = 1; i <= hour + 1; i++) {
				// System.out.println(calendar.getTime().toLocaleString());
				// CallableStatement cs =null;
				// if (model.getIsCollect().equals("true")) {
				// cs = conn
				// .prepareCall("call proc_jhtj2_yxzbjs.jhtj_yxzb_collect(?)");
				// cs.setTimestamp(1, new java.sql.Timestamp(calendar
				// .getTimeInMillis()));
				// cs.execute();
				// }
				// if (model.getIsCompute().equals("true")) {
				// cs = conn.prepareCall("call
				// proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
				// cs.setTimestamp(1, new java.sql.Timestamp(calendar
				// .getTimeInMillis()));
				//						cs.setString(2, "1");
				//						cs.execute();
				//					}   
				//				    cs.close(); 
				//					calendar.set(calendar.HOUR_OF_DAY, calendar
				//							.get(calendar.HOUR_OF_DAY) + 1);
				//				} 
			}
			if (model.getIsGroup().equals("true")) // 分组指标
			{
				
				format = new SimpleDateFormat("yyyy-MM-dd");
				Date sdate = format.parse(model.getGroupStartDate());
				Date edate = format.parse(model.getGroupEndDate()); 
				
				long second2 = edate.getTime() - sdate.getTime();
				long day = second2 / (1000 * 60 * 60 * 24);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdate);
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在分组指标计算程序----");
					CallableStatement cs = conn.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					for (int i = 1; i <= day + 1; i++) { 
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "2");
						cs.addBatch();
						calendar.set(calendar.DAY_OF_MONTH, calendar.get(calendar.DAY_OF_MONTH) + 1);
						 if(i%24 ==0)
						 {
							 cs.executeBatch();
							 cs.clearBatch(); 
							 System.out.println("已计算完成24天的数据；");
						 }
					}
					cs.executeBatch();
					cs.close();
					System.out.println("分组指标计算结束----");
					
					cs = conn.prepareCall("call proc_jhtj2_yxzbjs.jhtj_yxzb_fsdzbjs_stop_handle(?,?)");
					cs.setTimestamp(1, new java.sql.Timestamp(sdate.getTime()));
					cs.setTimestamp(2, new java.sql.Timestamp(edate.getTime()));
					cs.execute();
					cs.close();
					System.out.println("分组指标停机开机异常处理结束----");
				} 
			}

			if (model.getIsDate().equals("true"))// 日指标
			{ 
				format = new SimpleDateFormat("yyyy-MM-dd");
				Date sdate = format.parse(model.getStartDate());
				Date edate = format.parse(model.getEndDate()); 
				
				long second2 = edate.getTime() - sdate.getTime();
				long day = second2 / (1000 * 60 * 60 * 24);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdate);
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在日指标计算程序----");
					CallableStatement cs = conn.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					for (int i = 1; i <= day + 1; i++) {
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "3");
						cs.addBatch();
						calendar.set(calendar.DAY_OF_MONTH, calendar
								.get(calendar.DAY_OF_MONTH) + 1);
						if(i%24 ==0)
						 {
							 cs.executeBatch();
							 cs.clearBatch(); 
							 System.out.println("已计算完成24天的数据；");
						 }
					}
					cs.executeBatch();
					cs.close();
					System.out.println("日指标计算结束---1-");
				} 
//-------------------------
				calendar.setTime(sdate);
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在日指标计算程序----");
					CallableStatement cs = conn.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					for (int i = 1; i <= day; i++) {
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "3");
						cs.addBatch();
						calendar.set(calendar.DAY_OF_MONTH, calendar
								.get(calendar.DAY_OF_MONTH) + 1);
						if(i%24 ==0)
						 {
							 cs.executeBatch();
							 cs.clearBatch(); 
							 System.out.println("已计算完成24天的数据；");
						 }
					}
					cs.executeBatch();
					cs.close();
					System.out.println("日指标计算结束---2-");
				} 
//------------------------
			}

			if (model.getIsMonth().equals("true"))// 月指标
			{
				
				format = new SimpleDateFormat("yyyy-MM-dd");
				Date smonth = format.parse(model.getStartMonth() + "-01");
				Date emonth = format.parse(model.getEndMonth() + "-01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(smonth);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(emonth);
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在月指标计算程序----");
					CallableStatement cs = conn
					.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					while (!(calendar.after(calendar2))) {
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "4");
						cs.addBatch();
						calendar.set(calendar.MONTH, calendar
								.get(calendar.MONTH) + 1); 
					}
					cs.executeBatch();
					cs.close();
					System.out.println("月指标计算结束----");
				} 
			}
			
			if (model.getIsQuarter().equals("true"))// 季指标
			{
				format = new SimpleDateFormat("yyyy-MM-dd");
				String squarter = String.valueOf((Integer.parseInt(model
						.getStartQuarter()) - 1) * 3 + 1);
				String equarter = String.valueOf((Integer.parseInt(model
						.getEndQuarter()) - 1) * 3 + 1);
				String sdate = model.getStartQuarterYear() + "-" + squarter
						+ "-01";
				String edate = model.getEndQuarterYear() + "-" + equarter
						+ "-01";
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(format.parse(sdate));
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(format.parse(edate));
				if (model.getIsCompute().equals("true")) {
					System.out.println("正在季指标计算程序----");
					CallableStatement cs = conn
					.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					while (!(calendar.after(calendar2))) { 
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "5");
						cs.addBatch();
						calendar.set(calendar.MONTH, calendar
								.get(calendar.MONTH) + 3);
					}
					cs.executeBatch();
					cs.close();
					System.out.println("季指标计算结束----");
				} 
			}
			if (model.getIsYear().equals("true"))// 年指标
			{
				System.out.println("正在年指标计算程序----");
				format = new SimpleDateFormat("yyyy-MM-dd");
				Date sdate = format.parse(model.getStartYear() + "-01-01");
				Date edate = format.parse(model.getEndYear() + "-01-01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdate);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(edate);
				if (model.getIsCompute().equals("true")) {
					CallableStatement cs = conn.prepareCall("call proc_jhtj3_zbjs.jhtj_zbjs_zy(?,?)");
					while (!(calendar.after(calendar2))) { 
						if(isStopping)
						{
							System.out.println("终止执行统计计算。");
							return;
						}
						cs.setTimestamp(1, new java.sql.Timestamp(calendar
								.getTimeInMillis()));
						cs.setString(2, "6");
						cs.addBatch();
						calendar.set(calendar.YEAR,
								calendar.get(calendar.YEAR) + 1);
					}
					cs.executeBatch();
					cs.close();
				} 
				System.out.println("年指标计算结束----");
			}
			System.out.println("此次指标计算结束！");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	private void getBetweenYearMonth(String start,String end,List<String> list)
	{ 
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM"); 
			try {
				Date ds = f.parse(start) ;
				Date de = f.parse(end); 
			    Calendar c = Calendar.getInstance();
			    c.setTime(ds); 
			    while(de.after(c.getTime()) || de.equals(c.getTime()))
			    {
			    	String yearMonth = f.format(c.getTime());
			    	if(!list.contains(yearMonth))
				    {
				    	list.add(yearMonth);
				    }
			    	c.add(Calendar.MONTH, 1);
			    }
				
			} catch (ParseException e) {
				 
				e.printStackTrace();
			}  
	}
	

	public PageObject findComputeStatItemList(String dataTimeType, int start,
			int limit) {
		String countsql = "select count(*)\n" + "  from bp_c_stat_item t\n"
				+ " where t.account_order > 0\n"
				+ "   and t.data_time_type in " + dataTimeType + "\n"
				+ " order by t.data_collect_way, t.account_order";
		Object count = bll.getSingal(countsql);
		String sql = "select t.item_code,\n" + "       t.item_name,\n"
				+ "       t.data_collect_way,\n"
				+ "       decode(t.data_collect_way,\n"
				+ "              '1',\n" + "              '手工录入',\n"
				+ "              '2',\n" + "              '实时采集',\n"
				+ "              '3',\n"
				+ "              '派生计算') data_collect_way_name,\n"
				+ "       t.account_order\n" + "  from bp_c_stat_item t\n"
				+ " where t.account_order > 0\n"
				+ "   and t.data_time_type in " + dataTimeType + "\n"
				+ " order by t.data_collect_way, t.account_order";
		List list = bll.queryByNativeSQL(sql, start, limit);
		List<BpCStatItem> arraylist = new ArrayList();
		Iterator i = list.iterator();
		while (i.hasNext()) {
			Object[] o = (Object[]) i.next();
			BpCStatItem model = new BpCStatItem();
			if (o[0] != null) {
				model.setItemCode(o[0].toString());
			} else {
				model.setItemCode("");
			}
			if (o[1] != null) {
				model.setItemName(o[1].toString());
			} else {
				model.setItemName("");
			}
			if (o[2] != null) {
				model.setDataCollectWay(o[2].toString());
			} else {
				model.setDataCollectWay("");
			}
			if (o[4] != null) {
				model.setAccountOrder(Long.parseLong(o[4].toString()));
			} else {
				model.setAccountOrder(0L);
			}

			arraylist.add(model);
		}
		PageObject o = new PageObject();
		o.setList(arraylist);
		o.setTotalCount(Long.parseLong(count.toString()));
		return o;
	}
}
