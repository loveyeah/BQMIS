package power.web.manage.stat.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpJReportEntry;
import power.ejb.manage.stat.BpJReportEntryImpl;
import power.ejb.manage.stat.form.StatItemComputeForm;
import power.ejb.manage.stat.form.StatItemEntry;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.util.Data;

@SuppressWarnings("serial")
public class BpReportEntryAction extends AbstractAction {
	private BpJReportEntryImpl remote;
	private StatItemComputeForm compute;
	private static boolean isComputing = false;

	public BpReportEntryAction() {
		remote = (BpJReportEntryImpl) factory.getFacadeRemote("BpJReportEntry");
	}

	// 运行报表录入列表
	public void findReportEntryList() throws JSONException, ParseException {
		String reportCode = request.getParameter("reportCode");
		String dateType = request.getParameter("dateType");
		String date = request.getParameter("date");
		String delayTime = request.getParameter("delayTime");
		String delayUnit = request.getParameter("delayUnit");
		List<StatItemEntry> list = remote.findEntryItemListByReport(dateType,
				reportCode, date, delayTime, delayUnit, employee
						.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}

	// 保存录入数据
	public void saveReportEntryValue() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String date = request.getParameter("entryDate");
			String timeType = request.getParameter("timeType");
			Object obj = JSONUtil.deserialize(str);
			List<StatItemEntry> updateList = new ArrayList<StatItemEntry>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				StatItemEntry model = new StatItemEntry();
				if (data.get("itemCode") != null
						&& !"".equals(data.get("itemCode"))) {
					model.setItemCode(data.get("itemCode").toString());
				}
				if (data.get("dataValue") != null
						&& !"".equals(data.get("dataValue"))) {
					model.setDataValue(Double.parseDouble(data.get("dataValue")
							.toString()));
				}
				model.setDataDate(date);
				updateList.add(model);
			}
			remote.saveReportEntryItemValue(updateList, timeType);
			write("{success: true,msg:'保存成功！'}");
		} catch (Exception e) {
			write("{success: false,msg:'保存失败！'}");
		}
	}

	// 运行报表录入和补充录入及查询列表（2）
	public void findRunReportEntryList() throws JSONException, ParseException {
		String reportCode = request.getParameter("reportCode");// 报表编码
		String dateType = request.getParameter("dateType");// 报表类型
		String delayTime = request.getParameter("delayTime");// 延期时间(录入)
		String delayUnit = request.getParameter("delayUnit");// 延期单位（录入）
		String method = request.getParameter("method");// 是否录入
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String tableName = "";
		String date = "";
		int timedot = 0;
		SimpleDateFormat format;
		if ("1".equals(dateType)) {
			format = new SimpleDateFormat("yyyy-MM-dd HH");// 时指标
		} else {
			format = new SimpleDateFormat("yyyy-MM-dd");
		}
		Calendar calendar = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		Date nowdate = new Date();// 查询结束
		Date delayDate = new Date();// 查询开始
		if ("query".equals(method))// 补充录入和查询
		{
			nowdate = format.parse(endTime);
			delayDate = format.parse(startTime);
			int startHour=delayDate.getHours();
			if(startHour%2==0){
				delayDate.setHours(startHour+1);
			}
			
			calendar2.setTime(nowdate);
			calendar.setTime(delayDate);
		} else// 录入
		{
			//calendar2.setTime(nowdate);
			//calendar.setTime(delayDate);
			switch (Integer.parseInt(delayUnit)) {
			case 1:
				calendar
						.add(calendar.HOUR_OF_DAY, -Integer.parseInt(delayTime));
				int aa=calendar
				.get(calendar.HOUR_OF_DAY);
				if(aa%2==0)
					calendar
					.add(calendar.HOUR_OF_DAY, 1);
				break;
			case 3:
				calendar.add(calendar.DAY_OF_MONTH, -Integer
						.parseInt(delayTime));
				break;
			case 4:
				calendar.add(calendar.MONTH, -Integer.parseInt(delayTime));
				break;
			}
			delayDate.setTime(calendar.getTimeInMillis());
		}
		String str = "{ 'data':[";
		switch (Integer.parseInt(dateType)) {
		case 1:
			tableName = "bp_j_stat_stz";// 时
			long second2 = nowdate.getTime() - delayDate.getTime();
			long hour = second2 / (1000 * 60 * 60);
			for (int i = 1; i <= hour + 1; i++) {
				timedot = calendar.get(calendar.HOUR_OF_DAY);
				if (timedot == 0) {
					timedot = 24;
				}
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str,
						null);
				int rc=Integer.parseInt(reportCode);
				if(rc==32)
				{
					calendar.set(calendar.HOUR_OF_DAY, calendar
							.get(calendar.HOUR_OF_DAY) + 1);
				}else
				{
					calendar.set(calendar.HOUR_OF_DAY, calendar
							.get(calendar.HOUR_OF_DAY) + 2);
				}
				/*calendar.set(calendar.HOUR_OF_DAY, calendar
						.get(calendar.HOUR_OF_DAY) + 1);*/
			}
			break;
		// add by liuyi 20100513
		case 2:
			tableName = "bp_j_stat_stz";// 时
			// long hour = second2 / (1000 * 60 * 60);
			// int[] arr = {8,14,20,2};
			int[] arr = { 8, 14, 20, 2, 23 };
			// String[] strArr = {"后夜班","上午班","下午班","前夜班"};
			String[] strArr = { "后夜班", "上午班", "下午班", "前夜班", "23:00" }; 
			
			if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 3)
			{
				 calendar.add(Calendar.DAY_OF_MONTH, -1);
			}
			
			for (int i = 0; i <= arr.length - 1; i++) {
				timedot = arr[i];
				// if(i == arr.length - 1)
				if (i == arr.length - 2 )
				{
					calendar.add(calendar.DAY_OF_MONTH, 1);
				}
				
//				if (nowHours < 2) {
//					calendar.add(calendar.DAY_OF_MONTH, -1);
//				}
				date = format.format(calendar.getTime());
				if (i == 0) {
					date += " 08";
				} else if (i == 1) {
					date += " 14";
				} else if (i == 2) {
					date += " 20";
				} else if (i == 3) {
					date += " 02";
				} else if (i == 4) {
					date += " 23";
				}

				str = this.addValue(reportCode, timedot, date, tableName, str,
						strArr[i]);
				// add
				if (i == arr.length - 2) {
					calendar.add(calendar.DAY_OF_MONTH, -1);
				}
				calendar.set(calendar.HOUR_OF_DAY, arr[i]);
			}
			break;

		case 3:
			tableName = "bp_j_stat_rtz";// 日
			long second3 = nowdate.getTime() - delayDate.getTime();
			long day = second3 / (1000 * 60 * 60 * 24);
			for (int i = 1; i <= day + 1; i++) {
				timedot = calendar.get(calendar.DAY_OF_MONTH);
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str,
						null);
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.get(calendar.DAY_OF_MONTH) + 1);
			}
			break;
		case 4:
			tableName = "bp_j_stat_ytz";// 月
			calendar.set(calendar.DAY_OF_MONTH, 1);
			calendar2.set(calendar2.DAY_OF_MONTH, 1);
			while (!(calendar.after(calendar2))) {
				timedot = calendar.get(calendar.MONTH) + 1; // modify by fyyang
															// 090922
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str,
						null);
				calendar.set(calendar.MONTH, calendar.get(calendar.MONTH) + 1);
			}
			break;
		case 5:
			tableName = "bp_j_stat_jtz";// 季
			long month2 = calendar2.get(calendar2.MONTH) + 1;
			long month = calendar.get(calendar.MONTH) + 1;
			if (month2 >= 1 && month2 <= 3)
				month2 = 1;
			if (month2 >= 4 && month2 <= 6)
				month2 = 4;
			if (month2 >= 7 && month2 <= 9)
				month2 = 7;
			if (month2 >= 10 && month2 <= 12)
				month2 = 10;
			if (month >= 1 && month <= 3)
				month = 1;
			if (month >= 4 && month <= 6)
				month = 4;
			if (month >= 7 && month <= 9)
				month = 7;
			if (month >= 10 && month <= 12)
				month = 10;
			calendar2.set(calendar2.MONTH, (int) month2 - 1);
			calendar.set(calendar.MONTH, (int) month - 1);
			calendar2.set(calendar.DAY_OF_MONTH, 1);
			calendar.set(calendar.DAY_OF_MONTH, 1);
			while (!(calendar.after(calendar2))) {
				long dotmonth = calendar.get(calendar.MONTH) + 1;
				if (dotmonth >= 1 && dotmonth <= 3)
					timedot = 1;
				if (dotmonth >= 4 && dotmonth <= 6)
					timedot = 2;
				if (dotmonth >= 7 && dotmonth <= 9)
					timedot = 3;
				if (dotmonth >= 10 && dotmonth <= 12)
					timedot = 4;
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str,
						null);
				calendar.set(calendar.MONTH, calendar.get(calendar.MONTH) + 3);
			}
			break;
		case 6:
			tableName = "bp_j_stat_ntz";// 年
			calendar2.set(calendar2.MONTH, 0);
			calendar.set(calendar.MONTH, 0);
			calendar2.set(calendar.DAY_OF_MONTH, 1);
			calendar.set(calendar.DAY_OF_MONTH, 1);
			while (!(calendar.after(calendar2))) {
				timedot = 1;
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str,
						null);
				calendar.set(calendar.YEAR, calendar.get(calendar.YEAR) + 1);
			}
			break;
		}
		if (str.equals("{ 'data':[")) {
			str += "]";
		} else {
			str = str.substring(0, str.length() - 1) + "]";
		}
		List<StatItemEntry> headerlist = remote.findReportItemListForEntry(
				reportCode, employee.getEnterpriseCode());
		str += ",'columModle':[";
		StringBuffer sb = new StringBuffer();
		if (headerlist.size() > 0) {
			Iterator it = headerlist.iterator();
			if ("2".equals(dateType)) {
				str += "{'header' : '班组','width':100,'dataIndex' : 'desc','align':'center'}";
			} else
				str += "{'header' : '数据日期','width':100,'dataIndex' : 'date','align':'center'}";
			while (it.hasNext()) {
				StatItemEntry model = (StatItemEntry) it.next();
				str += ",{'header' : '"
						+ model.getItemName()
						+ "<br>("
						+ model.getUnitName()
						+ ")','width':120,'dataIndex' : '"
						+ model.getItemCode()
						+ "','align':'center','renderer':function(value, metadata, record){metadata.attr='style=\"white-space:normal;\"';return value;},'editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})}";
				// modify by ywliu 20090911
			}
		}
		// if ("2".equals(dateType)) {
		// str += ",{'header' : '数据日期','width':100,'hidden':'true','dataIndex' :
		// 'date','align':'center'}";
		// }
		str += "],'rows':[";
		if (headerlist.size() > 0) {
			String head = "";
			int i = 0;
			// 与数据日期对应的列，第一列
			sb.append("[{'header':''},");
			// 如果第一个数据一级名称为空，显示为空，否则把第一个数据放入head中
			if (headerlist.get(0).getItemBaseName() == null
					|| headerlist.get(0).getItemBaseName().equals("")) {
				sb.append("{'header':''},");
			} else {
				head = headerlist.get(0).getItemBaseName();
				i++;
			}
			// 从第二个数据开始循环
			for (int k = 1; k < headerlist.size(); k++) {
				StatItemEntry en = (StatItemEntry) headerlist.get(k);
				// 如果一级名称为空，根据head输出
				if (en.getItemBaseName() == null
						|| en.getItemBaseName().equals("")) {
					if (head.equals("")) {
						sb.append("{'header':''},");
					} else {
						sb.append("{'header':'" + head + "','colspan':" + i
								+ ",'align':'center'},");
						head = "";
						i = -1;
					}
				}
				// 如果一级名称不为空，如果head存储的相同，i++，不相同，输出
				else {
					if (head.equals(en.getItemBaseName())) {
						i++;
						head = en.getItemBaseName();
					} else {
						if (i == -1 && head.equals("")) {
							sb.append("{'header':''},");
						}
						// 初始值不输出
						else if (i == 0 && head.equals("")) {
							;
						} else {
							sb.append("{'header':'" + head + "','colspan':" + i
									+ ",'align':'center'},");
						}
						i = 1;
						head = en.getItemBaseName();
					}
				}
			}
			// 输出最后放入缓存中的head
			if (!head.equals("") && i > 0) {
				sb.append("{'header':'" + head + "','colspan':" + i
						+ ",'align':'center'},");
			} else if (head.equals("") && i < 0) {
				sb.append("{'header':''},");
			} else {
				;
			}
			// add by liuyi 20100513
			// if ("2".equals(dateType)){
			// sb.append("{'header':''},");
			// }
			sb.delete(sb.length() - 1, sb.length());
			sb.append("]");
			str += sb.toString();
		}
		str += "],'fieldsNames' : [";
		if (headerlist.size() > 0) {
			Iterator it = headerlist.iterator();
			// modified by liuyi 20100513
			// str += "{'name':'date'}";
			str += "{'name':'date'},{'name':'desc'}";
			while (it.hasNext()) {
				StatItemEntry model = (StatItemEntry) it.next();
				str += ",{'name':'" + model.getItemCode() + "'},{'name':'"
						+ model.getItemCode() + "type'}";
			}
		}
		str += "]";
		
		int nowHour = Calendar.getInstance().get(Calendar.HOUR);

		str += ",'nowHour':"+nowHour+"}";
		
		//System.out.println("json is:" + str);
		write(str);
	}

	
	// 保存录入数据2
	public void saveEntryValue() throws JSONException {
		try {
			String str = request.getParameter("isUpdate");
			String timeType = request.getParameter("timeType");
			Object obj = JSONUtil.deserialize(str);
			List<StatItemEntry> updateList = new ArrayList<StatItemEntry>();
			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				StatItemEntry model = new StatItemEntry();
				if (data.get("itemCode") != null
						&& !"".equals(data.get("itemCode"))) {
					model.setItemCode(data.get("itemCode").toString());
				}
				if (data.get("value") != null && !"".equals(data.get("value"))) {
					model.setDataValue(Double.parseDouble(data.get("value")
							.toString()));
				}
				if (data.get("date") != null && !"".equals(data.get("date"))) {
					if ("1".equals(timeType))
						model.setDataDate(data.get("date").toString());
					// add by liuyi 20100513 班组指标
					if ("2".equals(timeType))
						model.setDataDate(data.get("date").toString());

					if ("3".equals(timeType))
						model.setDataDate(data.get("date").toString());
					if ("4".equals(timeType))
						model.setDataDate(data.get("date").toString() + "-01");
					if ("5".equals(timeType)) {
						String year = data.get("date").toString().substring(0,
								4);
						String sdate = String
								.valueOf((Integer.parseInt(data.get("date")
										.toString().substring(6, 7)) - 1) * 3 + 1);
						model.setDataDate(year + "-" + sdate + "-01");
					}
					if ("6".equals(timeType))
						model.setDataDate(data.get("date").toString()
								+ "-01-01");
				}
				updateList.add(model);
			}
			remote.saveReportEntryItemValue(updateList, timeType);
			write("{success: true,msg:'保存成功！'}");
		} catch (Exception e) {
			write("{success: false,msg:'保存失败！'}");
		}
	}

	// modified by liuyi 20100513 班组指标 班组描述 desc
	// private String addValue(String reportCode, int timedot, String date,
	// String tableName, String str) {
	private String addValue(String reportCode, int timedot, String date,
			String tableName, String str, String desc) {
		List<StatItemEntry> arraylist = new ArrayList();
		// arraylist = remote.findReportItemValueListForEntry(reportCode,
		// timedot,
		// date, tableName, employee.getEnterpriseCode());
		arraylist = remote.findReportItemValueListForEntry(reportCode, timedot,
				date, tableName, employee.getEnterpriseCode(), desc);
		if (arraylist.size() > 0) {
			Iterator it = arraylist.iterator();
			str += "{";
			if ("bp_j_stat_ytz".equals(tableName)) {
				str += "'date':'" + date.substring(0, 7) + "'";
			} else if ("bp_j_stat_jtz".equals(tableName)) {
				str += "'date':'" + date.substring(0, 4) + "年第" + timedot
						+ "季'";
			} else if ("bp_j_stat_ntz".equals(tableName)) {
				str += "'date':'" + date.substring(0, 4) + "'";
			} else {
				str += "'date':'" + date + "'";
			}
			// add by liuyi 20100513
			str += ",'desc':'" + desc + "'";
			while (it.hasNext()) {
				StatItemEntry model = (StatItemEntry) it.next();
				if (model.getDataValue() != null) {
					DecimalFormat df1 = new DecimalFormat("0");
					if ("0".equals(model.getDataType()))
						df1 = new DecimalFormat("0");
					if ("1".equals(model.getDataType()))
						df1 = new DecimalFormat("0.0");
					if ("2".equals(model.getDataType()))
						df1 = new DecimalFormat("0.00");
					if ("3".equals(model.getDataType()))
						df1 = new DecimalFormat("0.000");
					if ("4".equals(model.getDataType()))
						df1 = new DecimalFormat("0.0000");
					str += ",'" + model.getItemCode() + "':'"
							+ df1.format(model.getDataValue()) + "'";
				} else {
					str += ",'" + model.getItemCode() + "':''";
				}
				str += ",'" + model.getItemCode() + "type':'"
						+ model.getDataType() + "'";
			}
			str += "},";
		}
		return str;
	}

	// 运行报表补充录入列表
	public void findReportAddEntryList() throws JSONException, ParseException {
		String reportCode = request.getParameter("reportCode");
		String dateType = request.getParameter("dateType");
		String date = request.getParameter("date");
		String delayTime = request.getParameter("delayTime");
		String delayUnit = request.getParameter("delayUnit");
		List<StatItemEntry> list = remote.findAddEntryByReport(dateType,
				reportCode, date, delayTime, delayUnit, employee
						.getEnterpriseCode());
		write(JSONUtil.serialize(list));
	}

	// 运行报表数据查询
	public void findReportList() throws JSONException {
		String reportCode = request.getParameter("reportCode");
		String startDate = request.getParameter("startDate");
		String dateType = request.getParameter("dateType");
		PageObject obj = remote.reportQuery(reportCode, startDate, dateType,
				employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj.getList()));
	}

	public void itemCollectCompute() {
		// try {
		// remote.collectCompute("compute");
		// write("{success : true,msg : '采集成功！'}");
		// } catch (Exception e) {
		// write("{success : false,msg : '操作失败！'}");
		// }

	}

	public void stopCompute() {
		if (isComputing) {
			BpJReportEntry.stopComplute();
			write("{success:true}");
		} else {
			write("{success:false,msg:'计算尚未开始执行.'}");
		}

	}

	// 统计指标处理（采集、计算）
	public void statItemCollectCopmute() throws Exception {
		if (!isComputing) {
			try {
				isComputing = true;
				remote.statItemCollectCompute(compute, employee
						.getEnterpriseCode());
				if (BpJReportEntry.getIsStopping()) {
					write("{success : true,msg : '计算终止！'}");
				} else {
					write("{success : true,msg : '操作成功！'}");
				}
			} catch (Exception e) {
				write("{success : false,msg : '操作失败！'}");
			} finally {
				isComputing = false;
				if (BpJReportEntry.getIsStopping()) {
					BpJReportEntry.resetStop();
				}
			}
		} else {
			write("{success : false,msg : '指标正在计算中...！'}");
		}
	}

	// 查询指标列表
	public void findComputeStatItemList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String dateType = "(";
		if (compute.getIsTime().equals("true")) {
			dateType += "1,";
		}
		if (compute.getIsDate().equals("true")) {
			dateType += "3,";
		}
		if (compute.getIsGroup().equals("true")) {
			dateType += "2,";
		}
		if (compute.getIsMonth().equals("true")) {
			dateType += "4,";
		}
		if (compute.getIsQuarter().equals("true")) {
			dateType += "5,";
		}
		if (compute.getIsYear().equals("true")) {
			dateType += "6,";
		}
		dateType = dateType.substring(0, dateType.length() - 1) + ")";
		PageObject obj = remote.findComputeStatItemList(dateType, Integer
				.parseInt(start), Integer.parseInt(limit));
		write(JSONUtil.serialize(obj));
	}

	public StatItemComputeForm getCompute() {
		return compute;
	}

	public void setCompute(StatItemComputeForm compute) {
		this.compute = compute;
	}

	public boolean isComputing() {
		return isComputing;
	}

	public void setComputing(boolean isComputing) {
		this.isComputing = isComputing;
	}
}
