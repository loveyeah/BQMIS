package power.ejb.manage.stat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import power.ejb.manage.stat.form.InputReprotItemForm;

/**
 * Facade for entity BpCInputReport.
 * 
 * @see power.ejb.manage.stat.BpCInputReport
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCInputReportFacade implements BpCInputReportFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	/**
	 * 保存报表名称信息
	 * 
	 * @param entity
	 */
	public void save(BpCInputReport entity) {
		LogUtil.log("saving BpCInputReport instance", Level.INFO, null);
		try {
			if (entity.getReportCode() == null) {
				entity.setReportCode(dll.getMaxId("BP_C_INPUT_REPORT",
						"REPORT_CODE"));
			}
			entityManager.persist(entity);
			dll.exeNativeSQL(createStartData(entity.getReportCode(), entity
					.getReportType(), entity.getEnterpriseCode()));
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量增加录入报表数据
	 * 
	 * @param addList
	 *            报表数据
	 */
	public void save(List<BpCInputReport> addList) {
		if (addList != null && addList.size() > 0) {
			Long reportCode = dll.getMaxId("BP_C_INPUT_REPORT", "REPORT_CODE");
			int i = 0;
			for (BpCInputReport entity : addList) {
				entity.setReportCode(reportCode + (i++));
				this.save(entity);
			}
		}
	}

	/**
	 * 删除一条录入报表数据
	 * 
	 * @param entity
	 */
	public void delete(BpCInputReport entity) {
		LogUtil.log("deleting BpCInputReport instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCInputReport.class, entity
					.getReportCode());
			entityManager.remove(entity);
			String sql = "DELETE FROM BP_C_INPUT_REPORT_SETUP t\n"
					+ " WHERE t.REPORT_CODE = '" + entity.getReportCode()
					+ "'\n" + "   AND t.enterprise_code = '"
					+ entity.getEnterpriseCode() + "'";
			dll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 批量删除录入报表数据
	 * 
	 * @param ids
	 * @return true 删除成功 false 删除不成功
	 */
	public boolean delete(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpCInputReport entity = new BpCInputReport();
				entity = this.findById(Long.parseLong(i));
				this.delete(entity);
			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	/**
	 * 批量更新报表数据
	 * 
	 * @param updateList
	 *            报表数据
	 */
	public void update(List<BpCInputReport> updateList) {

		try {
			for (BpCInputReport data : updateList) {
				if (!data.getReportType().equals(
						this.findById(data.getReportCode()).getReportType())) {
					String sql = "DELETE FROM BP_C_INPUT_REPORT_SETUP t\n"
							+ " WHERE t.REPORT_CODE = '" + data.getReportCode()
							+ "'\n" + "   AND t.enterprise_code = '"
							+ data.getEnterpriseCode() + "'";
					dll.exeNativeSQL(sql);
					dll.exeNativeSQL(createStartData(data.getReportCode(), data
							.getReportType(), data.getEnterpriseCode()));
				}

				String sql = "update BP_C_INPUT_REPORT t\n"
						+ "set t.REPORT_CODE='" + data.getReportCode() + "',"
						+ " t.REPORT_NAME='" + data.getReportName() + "',"
						+ " t.REPORT_TYPE='" + data.getReportType() + "',"
						+ " t.TIME_DELAY='" + data.getTimeDelay() + "',"
						+ " t.TIME_UNIT='" + data.getTimeUnit() + "',"
						+ " t.DISPLAY_NO='" + data.getDisplayNo() + "'";
				// -------update by ltong-----------
				if (data.getGroupNature() != null) {
					sql += ", t.group_nature='" + data.getGroupNature() + "'";
				}
				sql += "where t.REPORT_CODE='" + data.getReportCode() + "'\n";
				dll.exeNativeSQL(sql);

			}
		} catch (RuntimeException e) {
			throw e;
		}

	}

	/**
	 * 通过报表编码查询对应的报表信息
	 * 
	 * @param id
	 *            报表编码
	 * @return BpCInputReport 报表信息
	 */
	public BpCInputReport findById(Long id) {
		LogUtil.log("finding BpCInputReport instance with id: " + id,
				Level.INFO, null);
		try {
			BpCInputReport instance = entityManager.find(BpCInputReport.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String type,
			String workerCode, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = null;
			String sqlCount = "SELECT count(*)\n"
					+ "  	FROM BP_C_INPUT_REPORT t\n"
					+ "    WHERE  t.enterprise_code = '" + enterpriseCode
					+ "'\n";
			// +
			if (type != null && type.equals("2")) {
				sqlCount += " and t.report_type ='"
						+ type
						+ "' \n"
						+ "   and (t.report_code || '_lr' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"
						+ workerCode
						+ "') or\n"
						+ "   t.report_code || '_lr' not in (select r.code from JXL_REPORTS_RIGHT r)\n"
						+ "   )";
			}
			sqlCount += " ORDER BY t.display_no\n";

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			if (count > 0) {
				sql = "SELECT t.*\n" + "  	FROM BP_C_INPUT_REPORT t\n"
						+ "    WHERE  t.enterprise_code = '" + enterpriseCode
						+ "'\n";
				// + " ORDER BY t.display_no\n";
				if (type != null && type.equals("2")) {
					sql += " and t.report_type = '"
							+ type
							+ "' \n"
							+ "   and (t.report_code || '_lr' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"
							+ workerCode
							+ "') or\n"
							+ "   t.report_code || '_lr' not in (select r.code from JXL_REPORTS_RIGHT r)\n"
							+ "   )";
				} else if (type != null && type.equals("lr")) {
					sql += "   and (t.report_code || '_lr' in (select r.code from JXL_REPORTS_RIGHT r where r.worker_code='"
							+ workerCode
							+ "') or\n"
							+ "   t.report_code || '_lr' not in (select r.code from JXL_REPORTS_RIGHT r)\n"
							+ "   )";
				}
				sql += " ORDER BY t.display_no\n";
				List<BpCInputReport> list = dll.queryByNativeSQL(sql,
						BpCInputReport.class, rowStartIdxAndCount);
				result.setList(list);
				result.setTotalCount(count);
			}
			System.out.println(sql);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 判断报表名称是否重复
	 * 
	 * @param reportName
	 *            报表名称
	 * @return Long类型 大于0 表示名称重复 不大于0 表示名称不重复
	 */
	public Long checkReportName(String reportName) {
		String sql = "select count(1) from BP_C_INPUT_REPORT t"
				+ " where t.REPORT_NAME= '" + reportName + "'";
		Long count = Long.valueOf(dll.getSingal(sql).toString());
		return count;
	}

	/**
	 * 获取报表时间类型初始值SQL
	 */
	public String createStartData(Long reportCode, String timeType,
			String enterpriseCode) {
		String strSQL = "";
		try {
			switch (Integer.parseInt(timeType)) {
			// 时数据
			case 1:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 24; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',24,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			// 分组
			case 2:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 5; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',5,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			// 日数据
			case 3:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 31; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',31,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 4:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 12; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',12,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 5:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP";
				for (int i = 1; i < 4; i++) {
					strSQL += " SELECT '" + reportCode + "'," + i + ",'1','"
							+ timeType + "','1','" + enterpriseCode
							+ "'FROM dual UNION ALL";
				}
				strSQL += " SELECT '" + reportCode + "',4,'1','" + timeType
						+ "','1','" + enterpriseCode + "'FROM dual";
				break;
			case 6:
				strSQL = "INSERT INTO BP_C_INPUT_REPORT_SETUP\n" + "VALUES\n"
						+ "  ('" + reportCode + "',\n" + "   1,\n"
						+ "   '1',\n" + "   \n" + "   '" + timeType + "','1','"
						+ enterpriseCode + "')";

				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		return strSQL;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findGroupReportItem(String date, String reportCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		List arrlist = new ArrayList();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		// String sql = "select b.item_code,\n"
		// + " b.item_name,\n"
		// + " getunitname(b.unit_code) as unitName,\n"
		// + " (select d.data_value\n"
		// + " from bp_j_stat_fztz d\n"
		// + " where d.item_code = b.item_code\n"
		// + " and d.data_date = to_date('" + new
		// java.sql.Date(calendar.getTime().getTime())
		// +"', 'yyyy-MM-dd')\n"
		// + " and d.class_times = c.class_times) as sdataValue,\n"
		// + " c.data_value\n"
		// + " from bp_c_input_report_item a, bp_c_stat_item b, bp_j_stat_fztz
		// c\n"
		// + " where a.report_code = '" + reportCode + "'\n"
		// + " and c.report_code = '" + reportCode + "'\n"
		// + " and c.data_date = to_date('" + date
		// + "', 'yyyy-MM-dd')\n" + " and c.item_code = b.item_code"
		// + " and a.item_code = b.item_code";

		String sql = "select a.item_code,\n"
				+ "       getitemname(a.item_code),\n"
				+ "       (select getunitname(b.unit_code)\n"
				+ "          from bp_c_stat_item b\n"
				+ "         where b.item_code = a.item_code) as unitName,\n"
				+ "       (select d.data_value\n"
				+ "          from bp_j_stat_fztz d\n"
				+ "         where d.item_code = a.item_code\n"
				+ "           and d.data_date = to_date('"
				+ new java.sql.Date(calendar.getTime().getTime())
				+ "', 'yyyy-MM-dd')\n"
				+ "           and d.class_times = c.class_times) as sdataValue,\n"
				+ "       c.data_value\n"
				+ "  from bp_c_input_report_item a, bp_j_stat_fztz c\n"
				+ " where a.report_code = '" + reportCode + "'\n"
				+ "   and c.item_code = a.item_code\n"
				+ "   and c.data_date = to_date('" + date + "', 'yyyy-MM-dd')";

		String sqlCount = "select count(1) from bp_c_input_report_item a, bp_j_stat_fztz c\n"
				+ " where a.report_code = '"
				+ reportCode
				+ "'\n"
				+ "   and c.item_code = a.item_code\n"
				+ "   and c.data_date = to_date('" + date + "', 'yyyy-MM-dd')";
		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
		// System.out.println(sql);
		Iterator it = list.iterator();
		StringBuffer tempCodes = new StringBuffer();
		if (count > 0) {
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				InputReprotItemForm model = new InputReprotItemForm();
				if (data[0] != null) {
					model.setItemCode(data[0].toString());
					tempCodes.append("'" + model.getItemCode() + "',");
				}
				if (data[1] != null) {
					model.setItemName(data[1].toString());
				}
				if (data[2] != null) {
					model.setUnitName(data[2].toString());
				}
				if (data[3] != null) {
					model.setSdataValue(Double.parseDouble(data[3].toString()));
				}
				if (data[4] != null) {
					model.setDataValue(Double.parseDouble(data[4].toString()));
				}
				arrlist.add(model);
			}
		}

		String itemCodes = null;
		if (tempCodes.length() > 0) {
			itemCodes = tempCodes.substring(0, tempCodes.length() - 1);
		} else {
			itemCodes = "'temp'";
		}
		// String sql1 = "select distinct a.item_code, getitemname(a.item_code),
		// getunitname(c.unit_code),\n"
		// + " (select d.data_value\n"
		// + " from bp_j_stat_fztz d\n"
		// + " where d.item_code = c.item_code\n"
		// + " and d.data_date = to_date('" + new
		// java.sql.Date(calendar.getTime().getTime())
		// + "', 'yyyy-MM-dd')"
		// + " ) as sdataValue"
		// + " from bp_c_input_report_item a, BP_C_INPUT_REPORT b,
		// bp_c_stat_item c, bp_j_stat_fztz dd\n"
		// + " where b.report_code = '" + reportCode + "'\n"
		// + " and a.report_code = b.report_code\n"
		// + " and a.item_code = c.item_code\n"
		// + " and a.item_code = dd.item_code\n"
		// + " and a.item_code not in (" + itemCodes + ")";

		String sql1 = "select a.item_code,\n"
				+ "        getitemname(a.item_code),\n"
				+ "        (select getunitname(b.unit_code)\n"
				+ "           from bp_c_stat_item b\n"
				+ "          where b.item_code = a.item_code) as unitName,\n"
				+ "        (select d.data_value\n"
				+ "           from bp_j_stat_fztz d\n"
				+ "          where d.item_code = a.item_code\n"
				+ "            and d.data_date = to_date('"
				+ new java.sql.Date(calendar.getTime().getTime())
				+ "', 'yyyy-MM-dd')) as sdataValue\n"
				+ "   from bp_c_input_report_item a\n"
				+ "  where a.report_code = '" + reportCode + "'\n"
				+ "    and a.item_code not in (" + itemCodes + ")";

		// System.out.println(sql1);
		// String sqlCount1 = "select count(1)\n"
		// + " from bp_c_input_report_item a, BP_C_INPUT_REPORT b,
		// bp_c_stat_item c, bp_j_stat_fztz dd\n"
		// + " where b.report_code = '" + reportCode + "'\n"
		// + " and a.report_code = b.report_code\n"
		// + " and a.item_code = c.item_code\n"
		// + " and a.item_code = dd.item_code\n"
		// + " and dd.data_date (+)= to_date('"+ new
		// java.sql.Date(calendar.getTime().getTime())
		// + "', 'yyyy-MM-dd')"
		// + " and a.item_code not in (" + itemCodes + ")";
		List list1 = dll.queryByNativeSQL(sql1, rowStartIdxAndCount);
		Long count1 = Long.parseLong(String.valueOf(list1.size()));
		Iterator it1 = list1.iterator();

		if (count1 > 0) {
			while (it1.hasNext()) {
				Object[] data = (Object[]) it1.next();
				InputReprotItemForm model = new InputReprotItemForm();
				if (data[0] != null) {
					model.setItemCode(data[0].toString());
					tempCodes.append("'" + model.getItemCode() + "',");
				}
				if (data[1] != null) {
					model.setItemName(data[1].toString());
				}
				if (data[2] != null) {
					model.setUnitName(data[2].toString());
				}
				if (data[3] != null) {
					model.setSdataValue(Double.parseDouble(data[3].toString()));
				}
				arrlist.add(model);
			}
		}
		pg.setList(arrlist);
		pg.setTotalCount(count + count1);
		return pg;
	}

	public boolean judgeItemFztz(String date, String itemCode,
			String groupNature) {
		String sql = "select count(1)\n"
				+ "  from bp_j_stat_fztz a, bp_c_stat_item b\n"
				+ " where a.item_code = '" + itemCode + "'\n"
				+ "   and a.data_date = to_date('" + date + "','yyyy-MM-dd')\n"
				+ "   and a.class_times = '" + groupNature + "'\n"
				+ "   and b.item_code = a.item_code\n"
				+ "   and b.data_collect_way = '1'";
		Long count = Long.parseLong(dll.getSingal(sql).toString());
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void saveGroupReportValue(List<InputReprotItemForm> updateList)
			throws ParseException {
		if (updateList.size() > 0) {
			for (InputReprotItemForm model : updateList) {
				if (judgeItemFztz(model.getDate(), model.getItemCode(), model
						.getUnitName())) {
					String updateSql = "update bp_j_stat_fztz a\n"
							+ "set a.data_value = '" + model.getDataValue()
							+ "'\n" + "  where  a.item_code = '"
							+ model.getItemCode() + "'\n"
							+ "   and a.data_date = to_date('"
							+ model.getDate() + "','yyyy-MM-dd')\n"
							+ "   and a.class_times = '" + model.getUnitName()
							+ "'";
					dll.exeNativeSQL(updateSql);
				} else {
					String str = "select func_jhtj_runlog.getClassId(4,to_date('"
							+ model.getDate()
							+ "','yyyy-MM-dd'),'"
							+ model.getUnitName() + "') from dual";
					String zhibie = dll.queryByNativeSQL(str).get(0).toString();
					String insertSql = "insert into bp_j_stat_fztz a\n"
							+ "     (a.item_code, a.data_date, a.class_times, a.data_value, a.zhibie)\n"
							+ "   values\n" + "     ('" + model.getItemCode()
							+ "',to_date('" + model.getDate()
							+ "','yyyy-MM-dd'),'" + model.getUnitName() + "',"
							+ model.getDataValue() + ",'" + zhibie + "')";
					dll.exeNativeSQL(insertSql);
				}
			}
		}

	}
}