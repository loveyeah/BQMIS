package power.web.manage.stat.action;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import power.ejb.manage.stat.BpJReportEntryImpl;
import power.ejb.manage.stat.form.StatItemComputeForm;
import power.ejb.manage.stat.form.StatItemEntry;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;

@SuppressWarnings("serial")
public class AccountItemDateQueryAction extends AbstractAction {
	private BpJReportEntryImpl remote;
	@SuppressWarnings("unused")
	private StatItemComputeForm compute;

	public AccountItemDateQueryAction() {
		remote = (BpJReportEntryImpl) factory.getFacadeRemote("BpJReportEntry");
	}
	//add by wpzhu---------------------
	

    public Calendar getcurrtentTime(Calendar calendar )
    {
    	
    	Calendar calendarNow = Calendar.getInstance();
    	Date nowdate = new Date();
    	calendarNow.setTime(nowdate);
    	
    	
	if(calendar.get(Calendar.YEAR)==calendarNow.get(Calendar.YEAR))
	{
	if(calendar.get(Calendar.MONTH)==calendarNow.get(Calendar.MONTH))
	{
		
		calendar.add(Calendar.DATE,-1);//当前日期的前一天
		return calendar ;
		
		
	}else
	{
            if("6".equals(flag)||flag=="6")
            {
            	calendar.set(calendar.MONTH, 11);
            	calendar.set(calendar.DAY_OF_MONTH, 31);
            }else
            {
		calendar.set(calendar.DAY_OF_MONTH, 1);
		calendar.roll(calendar.DAY_OF_MONTH, -1);
            }
		
		
		
		return calendar;
		
	}
	}else
	{
		  if("6".equals(flag)||flag=="6")
          {
          	calendar.set(calendar.MONTH, 11);
          	calendar.set(calendar.DAY_OF_MONTH, 31);//如果是年指标 为当年最后一天
          }else
          {
		calendar.set(calendar.DAY_OF_MONTH, 1);
		calendar.roll(calendar.DAY_OF_MONTH, -1);//本月最后一天
          }
		
		return calendar;
	}
    }
    //----------------------end ---------------------------------
    String flag="";//add by wpzhu 20100830
	public void findAccountItemDataList() throws JSONException, ParseException {
		String reportCode = request.getParameter("reportCode");// 报表编码
		String dateType = request.getParameter("dateType");// 报表类型
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
	
		nowdate = format.parse(endTime);
		delayDate = format.parse(startTime);
		calendar2.setTime(nowdate);
		calendar.setTime(delayDate);
		
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
				str = this.addValue(reportCode, timedot, date, tableName, str);
				calendar.set(calendar.HOUR_OF_DAY, calendar
						.get(calendar.HOUR_OF_DAY) + 1);
			}
			break;
		case 3:
			tableName = "bp_j_stat_rtz";// 日
			long second3 = nowdate.getTime() - delayDate.getTime();
			long day = second3 / (1000 * 60 * 60 * 24);
			for (int i = 1; i <= day + 1; i++) {
				timedot = calendar.get(calendar.DAY_OF_MONTH);
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str);
				calendar.set(calendar.DAY_OF_MONTH, calendar
						.get(calendar.DAY_OF_MONTH) + 1);
			}
			break;
		case 4:
			tableName = "bp_j_stat_ytz";// 月
			calendar.set(calendar.DAY_OF_MONTH, 1);
			calendar2.set(calendar2.DAY_OF_MONTH, 1);
			calendar=getcurrtentTime(calendar);//add by wpzhu 20100827
			
			
			while (!(calendar.after(calendar2))) {
				timedot = calendar.get(calendar.MONTH);
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str);
//				calendar.set(calendar.MONTH, calendar.get(calendar.MONTH) + 1);
				calendar.add(calendar.MONTH, 1);//modify wpzhu 
				calendar=	getcurrtentTime(calendar);//add by  wpzhu 20100827
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
			calendar=getcurrtentTime(calendar);//add by wpzhu 
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
				str = this.addValue(reportCode, timedot, date, tableName, str);
				calendar.add(calendar.MONTH, 3);//modify by wpzhu
//				calendar.set(calendar.MONTH, calendar.get(calendar.MONTH) + 3);
				calendar=getcurrtentTime(calendar);//add by wpzhu 
			}
			break;
		case 6:
			tableName = "bp_j_stat_ntz";// 年
			calendar2.set(calendar2.MONTH, 0);
			calendar.set(calendar.MONTH, 0);
			calendar2.set(calendar.DAY_OF_MONTH, 1);
			calendar.set(calendar.DAY_OF_MONTH, 1);
			flag="6";
			
			calendar=getcurrtentTime(calendar);//add by wpzhu 
			while (!(calendar.after(calendar2))) {
				timedot = 1;
				date = format.format(calendar.getTime());
				str = this.addValue(reportCode, timedot, date, tableName, str);
				calendar.set(calendar.YEAR, calendar.get(calendar.YEAR) + 1);
				calendar=	getcurrtentTime(calendar);//add by wpzhu 
			}
			break;
		}
		
		if (str.equals("{ 'data':[")) {
			str += "]";
		} else {
			str = str.substring(0, str.length() - 1) + "]";
		}
		List<StatItemEntry> headerlist = remote.findAccountItemListForEntry(
				reportCode, employee.getEnterpriseCode());
		str += ",'columModle':[";
		StringBuffer sb = new StringBuffer();
		if (headerlist.size() > 0) {
			Iterator it = headerlist.iterator();
			str += "{'header' : '数据日期','width':100,'dataIndex' : 'date','align':'center'}";
			while (it.hasNext()) {
				StatItemEntry model = (StatItemEntry) it.next();
				str += ",{'header' : '"
						+ model.getItemName()
						+ "<br>("
						+ model.getUnitName()
						+ ")','width':100,'dataIndex' : '"
						+ model.getItemCode()
						+ "','align':'center','editor':new Ext.form.NumberField({allowDecimals : true,decimalPrecision : 0})}";
			}
		}
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
			sb.delete(sb.length() - 1, sb.length());
			sb.append("]");
			str += sb.toString();
		}
		str +="]";
		str += ",'fieldsNames' : [";
		if (headerlist.size() > 0) {
			Iterator it = headerlist.iterator();
			str += "{'name':'date'}";
			while (it.hasNext()) {
				StatItemEntry model = (StatItemEntry) it.next();
				str += ",{'name':'" + model.getItemCode() + "','type':'float','method':'"+model.getDataType()+"'}";
			}
		}
		str += "]}";
		//System.out.println(str);
		write(str);
	}

	private String addValue(String reportCode, int timedot, String date,
			String tableName, String str) {
		List<StatItemEntry> arraylist = new ArrayList();
		arraylist = remote.findAccountItemValueListForEntry(reportCode,
				timedot, date, tableName, employee.getEnterpriseCode());
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

}
