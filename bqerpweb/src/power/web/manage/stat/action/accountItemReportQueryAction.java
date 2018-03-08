package power.web.manage.stat.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCAnalyseAccount;
import power.ejb.manage.stat.BpCAnalyseAccountFacadeRemote;
import power.ejb.manage.stat.accountItemReportQuery;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class accountItemReportQueryAction extends AbstractAction {

	private accountItemReportQuery remote;
	private BpCAnalyseAccountFacadeRemote itemRemote;
	
	public accountItemReportQueryAction() {
		remote = (accountItemReportQuery) factory
				.getFacadeRemote("accountItemReportQueryImpl");
		itemRemote = (BpCAnalyseAccountFacadeRemote) factory
		.getFacadeRemote("BpCAnalyseAccountFacade");
	}

	public void getAccountItemModel() throws JSONException {
		String itemId = request.getParameter("itemId");
		BpCAnalyseAccount model = itemRemote.findById(Long.parseLong(itemId));
		write(JSONUtil.serialize(model));
	}

	public void findItemNameListForReportQuery() throws JSONException {
		String accountCode = request.getParameter("accountCode");
		PageObject obj = new PageObject();
		obj = remote
				.findItemNameList(accountCode, employee.getEnterpriseCode());

		String str = "{\"list\":[],\"totalCount\":null}";
		if (obj != null) {
			str = JSONUtil.serialize(obj);
		}
		write(str);
	}

	@SuppressWarnings("unchecked")
	public void findItemDataListForReportQuery() throws ParseException {
		String accountCode = request.getParameter("accountCode");
		String itemType = request.getParameter("itemType");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		int num = this.getDay(itemType, startDate, endDate);
		System.out.println("yfy" + num);
		if (num < 1) {
			return;
		}
		List list = remote.findItemDataList(accountCode, itemType, startDate,
				endDate, employee.getEnterpriseCode());

		String str = "{\"list\":[],\"totalCount\":null}";
		if (list != null) {

			Iterator it = list.iterator();
			str = "{ 'data':[";
			String dataName = "";

			while (it.hasNext()) {
				str += "{";
				Object[] data = null;
				Object objData = null;
				if (num > 1) {
					data = (Object[]) it.next();
				} else {
					objData = (Object) it.next();
				}
				for (int i = 0; i < num; i++) {
					dataName = "data" + i;
					if (num > 1) {
						if (data[i] != null) {
							str += "'" + dataName + "':'" + data[i].toString()
									+ "',";
						} else {
							str += "'" + dataName + "':'',";
						}
					} else {
						if (objData != null) {
							str += "'" + dataName + "':'" + objData.toString()
									+ "',";
						} else {
							str += "'" + dataName + "':'',";
						}
					}
				}
				str = str.substring(0, str.length() - 1);
				str += "},";

			}
			if (str.equals("{ 'data':[")) {
				str += "],";
			} else {
				str = str.substring(0, str.length() - 1) + "],";
			}

			str += "'columModle':[";
			String title = startDate;
			for (int i = 0; i < num; i++) {
				dataName = "data" + i;
				if (itemType.equals("season")) {
					str += "{'header' : '" + title.substring(0, 4) + "第"
							+ title.substring(4, 5)
							+ "季','width':80,'dataIndex' : '" + dataName
							+ "'},";
				} else {
					str += "{'header' : '" + title
							+ "','width':80,'dataIndex' : '" + dataName + "'},";
				}
				title = this.getDataName(itemType, title);
			}
			str = str.substring(0, str.length() - 1) + "],";
			str += "'fieldsNames' : [";
			for (int i = 0; i < num; i++) {
				dataName = "data" + i;
				str += "{'name':'" + dataName + "'},";
			}
			str = str.substring(0, str.length() - 1) + "]}";
			System.out.println(str);
			write(str);
		}

	}

	private int getDay(String itemType, String startDate, String endDate)
			throws ParseException {
		if (itemType.equals("day")) {
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
		} else if (itemType.equals("year")) {
			int start = Integer.parseInt(startDate);
			int end = Integer.parseInt(endDate);
			return end - start;
		} else if (itemType.equals("month")) {
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
		} else if (itemType.equals("hour")) {
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
		} else if (itemType.equals("season")) {
			int startYear = Integer.parseInt(startDate.substring(0, 4));
			int startSeason = Integer.parseInt(startDate.substring(4, 5));
			int endYear = Integer.parseInt(endDate.substring(0, 4));
			int endSeason = Integer.parseInt(endDate.substring(4, 5));
			int season = (endYear - startYear) * 4 + endSeason - startSeason;
			return season;
		} else {
			return 0;
		}

	}

	private String getDataName(String itemType, String startDate)
			throws ParseException {
		if (itemType.equals("day")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date newDate = new Date();

			Date start = sdf.parse(startDate);
			newDate.setTime(start.getTime() + 24 * 60 * 60 * 1000);
			return sdf.format(newDate);

		} else if (itemType.equals("year")) {
			int start = Integer.parseInt(startDate);
			start = start + 1;
			return start + "";
		} else if (itemType.equals("month")) {
			int year = Integer.parseInt(startDate.substring(0, 4));
			int month = Integer.parseInt(startDate.substring(5, 7));

			if (month == 12) {
				year = year + 1;
				month = 1;
			} else {
				month = month + 1;
			}
			String strMonth = month + "";
			if (strMonth.length() == 1) {
				strMonth = "0" + strMonth;
			}
			return year + "-" + strMonth;

		} else if (itemType.equals("hour")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
			Date newDate = new Date();
			Date start = sdf.parse(startDate);
			start = sdf.parse(startDate);
			newDate.setTime(start.getTime() + 60 * 60 * 1000);
			return sdf.format(newDate);
		} else if (itemType.equals("season")) {

			int startYear = Integer.parseInt(startDate.substring(0, 4));
			int startSeason = Integer.parseInt(startDate.substring(4, 5));
			if (startSeason != 4) {
				startSeason = startSeason + 1;
			} else {
				startSeason = 1;
				startYear = startYear + 1;
			}
			return startYear + "" + startSeason;
		}
		return startDate;
	}

	public void getDailyReport() throws JSONException {
		String reportCode = request.getParameter("reportCode");
		String reportDate = request.getParameter("reportdate");
		if (reportCode == null)
			reportCode = "";
		if (reportDate == null)
			reportDate = "";
		write(JSONUtil.serialize(remote.getStringDailyReportList(reportCode, reportDate)));
	}

}
