package power.ejb.manage.stat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.stat.form.dailyReportForm;
import power.ejb.manage.stat.form.itemInfoForQuery;

@Stateless
public class accountItemReportQueryImpl implements accountItemReportQuery {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	@SuppressWarnings("unchecked")
	public PageObject findItemNameList(String accountCode,
			String enterpriseCode, final int... rowStartIdxAndCount) {

		String sqlCount = "select count(*)\n"
				+ " from BP_C_ANALYSE_ACCOUNT_ITEM a,BP_C_STAT_ITEM b\n"
				+ " where a.item_code=b.item_code\n"
				+ " and a.enterprise_code='" + enterpriseCode
				+ "' and b.enterprise_code='" + enterpriseCode + "'\n"
				+ " and a.account_code=" + accountCode + "\n"
				+ " order by a.display_no asc";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (totalCount > 0) {
			PageObject obj = new PageObject();
			obj.setTotalCount(totalCount);
			List result = new ArrayList();
			String sql = "select a.item_code,b.unit_code,GETUNITNAME(b.unit_code),b.item_name\n"
					+ " from BP_C_ANALYSE_ACCOUNT_ITEM a,BP_C_STAT_ITEM b\n"
					+ " where a.item_code=b.item_code(+)\n"
					+ " and a.enterprise_code='"
					+ enterpriseCode
					+ "' and b.enterprise_code(+)='"
					+ enterpriseCode
					+ "'\n"
					+ " and a.account_code="
					+ accountCode
					+ "\n"
					+ " order by a.display_no asc";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				itemInfoForQuery model = new itemInfoForQuery();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					model.setItemCode(data[0].toString());
				}
				if (data[1] != null) {
					model.setUnitCode(data[1].toString());
				}
				if (data[2] != null) {
					model.setUnitName(data[2].toString());
				}
				if (data[3] != null) {
					model.setItemName(data[3].toString());
				}
				result.add(model);

			}
			obj.setList(result);
			return obj;

		}

		return null;
	}

	/** 计算日期相差天数 */
	private int getMinusDay(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf.parse(startDate);
		Date end = sdf.parse(endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		long timeStart = calendar.getTimeInMillis();
		calendar.setTime(end);
		long timeEnd = calendar.getTimeInMillis();
		long theday = (timeEnd - timeStart) / (1000 * 60 * 60 * 24);
		return Integer.parseInt(theday + "");
	}

	private int getMinusHour(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Date start = sdf.parse(startDate);
		Date end = sdf.parse(endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		long timeStart = calendar.getTimeInMillis();
		calendar.setTime(end);
		long timeEnd = calendar.getTimeInMillis();
		long theday = (timeEnd - timeStart) / (1000 * 60 * 60);
		return Integer.parseInt(theday + "");
	}

	private int getMinusMonth(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date start = sdf.parse(startDate);
		Date end = sdf.parse(endDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(start);
		long timeStart = calendar.getTimeInMillis();
		calendar.setTime(end);
		long timeEnd = calendar.getTimeInMillis();
		long theday = (timeEnd - timeStart) / (1000 * 60 * 60 * 24);
		theday = theday / 30;
		return Integer.parseInt(theday + "");
	}

	private int getMinusYear(String startDate, String endDate) {
		int start = Integer.parseInt(startDate);
		int end = Integer.parseInt(endDate);
		return end - start;

	}

	private int getMinusSeason(String startDate, String endDate) {
		int startYear = Integer.parseInt(startDate.substring(0, 4));
		int startSeason = Integer.parseInt(startDate.substring(4, 5));
		int endYear = Integer.parseInt(endDate.substring(0, 4));
		int endSeason = Integer.parseInt(endDate.substring(4, 5));
		int season = (endYear - startYear) * 4 + endSeason - startSeason;
		return season;
	}

	@SuppressWarnings("unchecked")
	public List findItemDataList(String accountCode, String itemType,
			String startDate, String endDate, String enterpriseCode)
			throws ParseException {
		String sql = "select \n";
		String myDate = "";
		String tabelName = "";
		String dataName = "";
		if (itemType.equals("day")) {
			// ---------------------------日指标--------------------------------------

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 相差天数
			int totalDay = this.getMinusDay(startDate, endDate);

			if (totalDay > 0) {

				for (int i = 0; i < totalDay; i++) {
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += tabelName + "." + dataName + ",";

				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from ";
				Date newDate1 = new Date();

				Date start1 = sdf.parse(startDate);
				newDate1.setTime(start1.getTime() - 24 * 60 * 60 * 1000);
				startDate = sdf.format(newDate1);
				for (int i = 0; i < totalDay; i++) {
					// 日期加一天
					Date newDate = new Date();

					Date start = sdf.parse(startDate);
					newDate.setTime(start.getTime() + 24 * 60 * 60 * 1000);
					myDate = sdf.format(newDate);
					startDate = myDate;
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += " (\n"
							+ "select a.item_code, a.data_value as "
							+ dataName
							+ "  \n"
							+ "from BP_J_STAT_RTZ a,BP_C_ANALYSE_ACCOUNT_ITEM b\n"
							+ "where a.item_code(+)=b.item_code\n"
							+ "and a.enterprise_code(+)='" + enterpriseCode
							+ "'  and b.enterprise_code='" + enterpriseCode
							+ "'\n" + "and b.account_code=" + accountCode
							+ "\n" + "and to_char(a.data_date,'yyyy-MM-dd')='"
							+ myDate + "'\n" + ") " + tabelName + ",";

				}
				// sql=sql.substring(0, sql.length()-1);
				sql += "BP_C_ANALYSE_ACCOUNT_ITEM tt";

				sql += " \n where  tt.account_code=" + accountCode;
				for (int i = 0; i < totalDay; i++) {
					tabelName = "t" + i;
					sql += "  and tt.item_code=" + tabelName
							+ ".item_code(+) \n";

				}

				// List list=bll.queryByNativeSQL(sql);
				// return list;
			}
			// ---------------------------日指标--------------------------------------
		} else if (itemType.equals("hour")) {
			// ---------------------------时指标--------------------------------------
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			// 相差小时数
			int totalHour = this.getMinusHour(startDate, endDate);
			if (totalHour > 0) {

				for (int i = 0; i < totalHour; i++) {
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += tabelName + "." + dataName + ",";

				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from ";
				// 减1小时
				Date newDate = new Date();
				Date start = sdf.parse(startDate);
				newDate.setTime(start.getTime() - 60 * 60 * 1000);
				startDate = sdf.format(newDate);
				for (int i = 0; i < totalHour; i++) {
					// 加一小时
					start = sdf.parse(startDate);
					newDate.setTime(start.getTime() + 60 * 60 * 1000);
					myDate = sdf.format(newDate);
					startDate = myDate;
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += " (\n"
							+ "select a.item_code, a.data_value as "
							+ dataName
							+ "  \n"
							+ "from BP_J_STAT_STZ a,BP_C_ANALYSE_ACCOUNT_ITEM b\n"
							+ "where a.item_code(+)=b.item_code\n"
							+ "and a.enterprise_code(+)='" + enterpriseCode
							+ "'  and b.enterprise_code='" + enterpriseCode
							+ "'\n" + "and b.account_code=" + accountCode
							+ "\n"
							+ "and to_char(a.data_date,'yyyy-MM-dd HH24')='"
							+ myDate + "'\n" + ") " + tabelName + ",";

				}
				sql += "BP_C_ANALYSE_ACCOUNT_ITEM tt";

				sql += " \n where  tt.account_code=" + accountCode;
				for (int i = 0; i < totalHour; i++) {
					tabelName = "t" + i;
					sql += "  and tt.item_code=" + tabelName
							+ ".item_code(+) \n";

				}

			}

			// ---------------------------时指标--------------------------------------
		} else if (itemType.equals("month")) {
			// ---------------------------月指标--------------------------------------
			@SuppressWarnings("unused")
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			int totalMonth = this.getMinusMonth(startDate, endDate);
			if (totalMonth > 0) {

				for (int i = 0; i < totalMonth; i++) {
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += tabelName + "." + dataName + ",";

				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from ";
				// 减1个月
				int year = Integer.parseInt(startDate.substring(0, 4));
				int month = Integer.parseInt(startDate.substring(5, 7));

				if (month == 1) {
					year = year - 1;
					month = 12;
				} else {
					month = month - 1;
				}
				String strMonth = month + "";
				if (strMonth.length() == 1) {
					strMonth = "0" + strMonth;
				}
				startDate = year + "-" + strMonth;
				for (int i = 0; i < totalMonth; i++) {
					// 加1个月
					year = Integer.parseInt(startDate.substring(0, 4));
					month = Integer.parseInt(startDate.substring(5, 7));

					if (month == 12) {
						year = year + 1;
						month = 1;
					} else {
						month = month + 1;
					}
					strMonth = month + "";
					if (strMonth.length() == 1) {
						strMonth = "0" + strMonth;
					}
					startDate = year + "-" + strMonth;
					// System.out.println("yyy"+startDate);
					myDate = startDate;
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += " (\n"
							+ "select a.item_code, a.data_value as "
							+ dataName
							+ "  \n"
							+ "from BP_J_STAT_YTZ a,BP_C_ANALYSE_ACCOUNT_ITEM b\n"
							+ "where a.item_code(+)=b.item_code\n"
							+ "and a.enterprise_code(+)='" + enterpriseCode
							+ "'  and b.enterprise_code='" + enterpriseCode
							+ "'\n" + "and b.account_code=" + accountCode
							+ "\n" + "and to_char(a.data_date,'yyyy-MM')='"
							+ myDate + "'\n" + ") " + tabelName + ",";

				}
				sql += "BP_C_ANALYSE_ACCOUNT_ITEM tt";

				sql += " \n where  tt.account_code=" + accountCode;
				for (int i = 0; i < totalMonth; i++) {
					tabelName = "t" + i;
					sql += "  and tt.item_code=" + tabelName
							+ ".item_code(+) \n";

				}

			}
			// ---------------------------月指标--------------------------------------
		} else if (itemType.equals("year")) {
			// ---------------------------年指标--------------------------------------
			int totalYear = this.getMinusYear(startDate, endDate);

			if (totalYear > 0) {

				for (int i = 0; i < totalYear; i++) {
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += tabelName + "." + dataName + ",";

				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from ";
				// 减1年
				int start = Integer.parseInt(startDate);
				start = start - 1;
				for (int i = 0; i < totalYear; i++) {
					// 加1年
					start = start + 1;
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += " (\n"
							+ "select a.item_code, a.data_value as "
							+ dataName
							+ "  \n"
							+ "from BP_J_STAT_NTZ a,BP_C_ANALYSE_ACCOUNT_ITEM b\n"
							+ "where a.item_code(+)=b.item_code\n"
							+ "and a.enterprise_code(+)='" + enterpriseCode
							+ "'  and b.enterprise_code='" + enterpriseCode
							+ "'\n" + "and b.account_code=" + accountCode
							+ "\n" + "and to_char(a.data_date,'yyyy')='"
							+ start + "'\n" + ") " + tabelName + ",";

				}
				sql += "BP_C_ANALYSE_ACCOUNT_ITEM tt";

				sql += " \n where  tt.account_code=" + accountCode;
				for (int i = 0; i < totalYear; i++) {
					tabelName = "t" + i;
					sql += "  and tt.item_code=" + tabelName
							+ ".item_code(+) \n";

				}

			}

			// ---------------------------年指标--------------------------------------
		} else if (itemType.equals("season")) {
			// ---------------------------季指标--------------------------------------
			int totalSeason = this.getMinusSeason(startDate, endDate);
			// System.out.println("season"+totalSeason);

			if (totalSeason > 0) {

				for (int i = 0; i < totalSeason; i++) {
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += tabelName + "." + dataName + ",";

				}
				sql = sql.substring(0, sql.length() - 1);
				sql += " from ";
				// 减1季
				int start = Integer.parseInt(startDate);
				start = start - 1;

				int startYear = Integer.parseInt(startDate.substring(0, 4));
				int startSeason = Integer.parseInt(startDate.substring(4, 5));
				if (startSeason != 1) {
					startSeason = startSeason - 1;
				} else {
					startSeason = 4;
					startYear = startYear - 1;
				}
				myDate = startYear + "" + startSeason;
				startDate = myDate;
				for (int i = 0; i < totalSeason; i++) {
					// 加1季
					startYear = Integer.parseInt(startDate.substring(0, 4));
					startSeason = Integer.parseInt(startDate.substring(4, 5));
					if (startSeason != 4) {
						startSeason = startSeason + 1;
					} else {
						startSeason = 1;
						startYear = startYear + 1;
					}

					if (startSeason == 1) {
						myDate = "01";
					} else if (startSeason == 2) {
						myDate = "04";
					} else if (startSeason == 3) {
						myDate = "07";
					} else if (startSeason == 4) {
						myDate = "10";
					}
					startDate = startYear + "" + startSeason;
					myDate = startYear + "-" + myDate;
					tabelName = "t" + i;
					dataName = "data" + i;
					sql += " (\n"
							+ "select a.item_code, a.data_value as "
							+ dataName
							+ "  \n"
							+ "from BP_J_STAT_JTZ a,BP_C_ANALYSE_ACCOUNT_ITEM b\n"
							+ "where a.item_code(+)=b.item_code\n"
							+ "and a.enterprise_code(+)='" + enterpriseCode
							+ "'  and b.enterprise_code='" + enterpriseCode
							+ "'\n" + "and b.account_code=" + accountCode
							+ "\n" + "and to_char(a.data_date,'yyyy-MM')='"
							+ myDate + "'\n" + ") " + tabelName + ",";

				}
				sql += "BP_C_ANALYSE_ACCOUNT_ITEM tt";

				sql += " \n where  tt.account_code=" + accountCode;
				for (int i = 0; i < totalSeason; i++) {
					tabelName = "t" + i;
					sql += "  and tt.item_code=" + tabelName
							+ ".item_code(+) \n";

				}

			}

			// ---------------------------季指标--------------------------------------
		} else {
			return null;
		}

		if (!sql.equals("select \n")) {
			sql += " \n  order by tt.display_no asc";
			List list = bll.queryByNativeSQL(sql);
			return list;
		} else {
			return null;
		}

	}


	//add by bjxu 100604
	@SuppressWarnings("unchecked")
	private List<dailyReportForm> getItemValueList(String reportCode,String reportDate){
		String tabelString ="dual";
		if("37".equals(reportCode) ){
			tabelString = "bp_j_stat_ytz";
			
		}else{
			tabelString = "bp_j_stat_rtz";
		}
		String sql =
			"select a.item_base_name as itemName,\n" +
			"       (select getunitname(t.unit_code)\n" + 
			"          from bp_c_stat_item t\n" + 
			"         where t.item_code = a.item_code) unitName,\n" + 
			"        round(decode(a.is_show_zero,'N',(decode(b.data_value,0,null,b.data_value)), b.data_value),a.data_type) as RValue,\n" + 
			"       a.item_second_name as secondName\n" +
			"  from bp_c_item_report_new a, "+tabelString+" b\n" + 
			" where a.report_code = '"+reportCode+"'\n" + 
			"   and a.item_code = b.item_code(+)\n" + 
			"   and b.data_date(+) = to_date('"+reportDate+"', 'yyyy-mm-dd')\n" + 
			"order by to_number(a.item_second_name),a.display_no";
 
		List list = bll.queryByNativeSQL(sql);
		//System.out.println(sql);
		List<dailyReportForm> arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			dailyReportForm model = new dailyReportForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setItemName(data[0].toString());
			if (data[1] != null)
				model.setUnitName(data[1].toString());
			if (data[2] != null)
				model.setRValue(data[2].toString());
			if (data[3] != null)
				model.setSecondName(data[3].toString());
			arrlist.add(model);
		}
		return arrlist;
	}
	
	private int getItemValueCount(String reportCode,String name){
		String sql =	"select count(*)\n" +
			"  from bp_c_item_report_new t\n" + 
			" where t.report_code = '"+reportCode+"'\n" + 
			"   and t.item_second_name = '"+name+"'";
		return Integer.parseInt(bll.getSingal(sql).toString());
		
		
	}
	
	public List<String[]> getStringDailyReportList(String reportCode,String reportDate){
		    List<dailyReportForm> list =  this.getItemValueList(reportCode,reportDate);
		    List<String[]> result = null;
		    Iterator<dailyReportForm> iter =  list.iterator();
		    if(iter.hasNext())
		    {
		    	result = new ArrayList<String[]>();
		    	do{
		    		String[] r1 = new String[9];
		    		dailyReportForm rec = iter.next();
		    		int count = this.getItemValueCount(reportCode, rec.getSecondName());
		    		r1[0] = rec.getItemName();
		    		r1[1] = rec.getUnitName();
		    		r1[2] = rec.getRValue();
		    		for(int j=0;j < count-1;j++)
		    		{
		    			dailyReportForm rec1 = iter.next();
		 //   			System.out.println(rec1.getItemName());
		    			r1[3+j] =rec1.getRValue();
		    		}
		    		result.add(r1); 
		    	}while(iter.hasNext());
		    }
			return result; 
		
	}
	
	
	//
	//modify by bjxu 2010/06/04 未用 
	//modify by wzhyan 2009/09/17 
	@SuppressWarnings("unchecked")
	public List getDailyReport(String reportCode, String reportDate,
			String enterpriseCode) {
		//		
		// wzhyan modify 20090907
		String sql = "SELECT *\n"
				+ "  FROM (SELECT DISTINCT a.item_name,\n"
				+ "                        (SELECT u.unit_name\n"
				+ "                           FROM bp_c_measure_unit u\n"
				+ "                          WHERE u.unit_id = a.unit_code) unit_name,\n"
				+ "                        (func_jhtj_report.getItemValue(a.item_code,a.enterprise_code,to_date(to_date('"+ reportDate +"','yyyy-MM-dd')),3)) r_value,\n"
				+ "                        (func_jhtj_report.getItemValue(a.item_code,a.enterprise_code,last_day(to_date('"+ reportDate +"','yyyy-MM-dd')),4)) y_value,\n"
				+ "                        (func_jhtj_report.getItemValue(a.item_code,a.enterprise_code,to_date('" + reportDate + "','yyyy-MM-dd'),5)) j_value,\n"
				+ "                        (func_jhtj_report.getItemValue(a.item_code,a.enterprise_code,to_date('" + reportDate + "','yyyy-MM-dd'),6)) n_value,\n"
				+ "                        t.display_no\n"
				+ "          FROM BP_C_STAT_ITEM a, bp_c_stat_report_item t\n"
				+ "         WHERE a.item_code = t.item_code\n"
				+ "           AND t.report_code = '" + reportCode + "'\n"
				+ "           AND t.enterprise_code = '" + enterpriseCode
				+ "')\n" + " ORDER BY display_no"; 
		List list = bll.queryByNativeSQL(sql);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			dailyReportForm model = new dailyReportForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setItemName(data[0].toString());
			if (data[1] != null)
				model.setUnitName(data[1].toString());
			if (data[2] != null)
				model.setRValue(data[2].toString());
			if (data[3] != null)
				model.setYValue(data[3].toString());
			if (data[4] != null)
				model.setJValue(data[4].toString());
			if (data[5] != null)
				model.setNValue(data[5].toString());
			arrlist.add(model);
		}
		return arrlist;
	}
}
