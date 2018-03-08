package power.web.manage.stat.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.BpCReportNew;
import power.ejb.manage.stat.BpCReportNewFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.googlecode.jsonplugin.annotations.JSON;

@SuppressWarnings("serial")
public class BpCReportNewAction extends AbstractAction {

	private BpCReportNewFacadeRemote remote;
	private int start;
	private int limit;

	/**
	 * 
	 * 构造方法
	 * 
	 */

	public BpCReportNewAction() {
		remote = (BpCReportNewFacadeRemote) factory
				.getFacadeRemote("BpCReportNewFacade");
	}
	
	/**
	 * 新增，修改，删除记录方法
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void saveBpCReportNew() throws Exception {
		try {

			String str = request.getParameter("isUpdate");
			String deleteIds = request.getParameter("isDelete");
			Object obj = JSONUtil.deserialize(str);

			List<BpCReportNew> addList = new ArrayList<BpCReportNew>();
			List<BpCReportNew> updateList = new ArrayList<BpCReportNew>();

			List<Map> list = (List<Map>) obj;
			for (Map data : list) {
				BpCReportNew model = new BpCReportNew();
				model.setEnterpriseCode(employee.getEnterpriseCode());
				if (data.get("reportCode") != null && !"".equals(data.get("reportCode"))) {
					model.setReportCode(Long.valueOf(data.get("reportCode").toString()));
				}
				if (data.get("reportName") != null && !"".equals(data.get("reportName"))) {
					model.setReportName(data.get("reportName").toString());
				}
				if (data.get("reportType") != null && !"".equals(data.get("reportType"))) {
					model.setReportType(data.get("reportType").toString());
				}
				if (data.get("timeDelay") != null && !"".equals(data.get("timeDelay"))) {
					model.setTimeDelay((Long)data.get("timeDelay"));
				}
				if (data.get("timeUnit") != null && !"".equals(data.get("timeUnit"))) {
					model.setTimeUnit(data.get("timeUnit").toString());
				}
				if (data.get("displayNo") != null && !"".equals(data.get("displayNo"))) {
					model.setDisplayNo(Long.parseLong(data.get("displayNo").toString()));
				}
				
				// 增加
				if (model.getReportCode() == null || "".equals(model.getReportCode())) {
					if (remote.checkReportName(model.getReportName()) > 0) {
						write("{success: false,msg:'报表名称不能重复！'}");
						return;
					} else {
						addList.add(model);
					}
				} else {
					if(!model.getReportName().equals(remote.findById(model.getReportCode()).getReportName())) {
						if (remote.checkReportName(model.getReportName()) > 0) {
							write("{success: false,msg:'报表名称不能重复！'}");
							return;
						}
						else{
							updateList.add(model);
						}
					}
					else {
						updateList.add(model);
					}
				}
			}

			if (addList.size() > 0)
				remote.save(addList);

			if (updateList.size() > 0)

				remote.update(updateList);

			if (deleteIds != null && !deleteIds.trim().equals(""))

				remote.delete(deleteIds);

			write("{success: true,msg:'保存成功！'}");

		} catch (Exception exc) {
			write("{success: false,msg:'操作失败'}");
			throw exc;

		}
	}

	/**
	 * 获取记录列表
	 * 
	 * @throws JSONException
	 * 
	 */
	@JSON(serialize=false)
	public void getBpCReportNewList() throws JSONException {
		String reportType = request.getParameter("reportType");
		PageObject obj = remote.findAll(reportType,employee.getEnterpriseCode(),employee.getWorkerCode(),start,
				limit);
		
		write(JSONUtil.serialize(obj));
	}
	@JSON(serialize=false)
	public void getReportName() throws JSONException {
		String reportType = request.getParameter("reportType");
		String reportCode=request.getParameter("reportCode");
		PageObject obj = remote.findreportName(reportType,reportCode,employee.getEnterpriseCode(),employee.getWorkerCode());
		write(JSONUtil.serialize(obj));
	}
	/**
	 * 燃料运行班上煤量汇总查询
	 */
	public void finReportListByMon()throws JSONException{
		String date=request.getParameter("date");
		PageObject reportList=remote.finReportListByMon(date, employee.getEnterpriseCode());
		write(JSONUtil.serialize(reportList));
	}
	/**
	 * 燃料运行班上煤量汇总导出
	 * @throws FileNotFoundException
	 * @throws JSONException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void reportExportByMon() throws FileNotFoundException, JSONException,IOException{
		String date=request.getParameter("date");
		int firstTotal=0;
		int secondTotal=0;
		int threeTotal=0;
		int fourTotal=0;
		int fiveTotal=0;
		StringBuffer exportData = new StringBuffer();
		String Mon=date.substring(5);
			exportData.append("<table border=1><tr><td align='center' colspan='11'><b><h1>燃料运行班"+Mon+"月上煤量汇总</h1></b></td></tr>" +
					"<tr><th>序号</th><th colspan='2'>运行一班</th><th colspan='2'>运行二班</th><th colspan='2'>运行三班</th><th colspan='2'>运行四班</th><th colspan='2'>运行五班</th> </tr>");
			List dataList = new ArrayList();
			PageObject object=remote.finReportListByMon(date, employee.getEnterpriseCode());
			if (object!=null&&object.getList()!=null) {
				List result=object.getList();
				if(result != null)
				{
					for(Object rec: result)
					{
						Object[] recs=(Object[])rec;
						firstTotal+=recs[1]==null?0:Integer.parseInt(recs[1].toString());
						secondTotal+=recs[2]==null?0:Integer.parseInt(recs[2].toString());
						threeTotal+=recs[3]==null?0:Integer.parseInt(recs[3].toString());
						fourTotal+=recs[4]==null?0:Integer.parseInt(recs[4].toString());
						fiveTotal+=recs[5]==null?0:Integer.parseInt(recs[5].toString());
						exportData.append("<tr>"); 
						 exportData.append("<td>").append(recs[0]==null?"":recs[0]).append("</td><td colspan='2'>").append(recs[1]==null?"":recs[1])
						 .append("</td><td colspan='2'>").append(recs[2]==null?"":recs[2]).append("</td><td colspan='2'>").append(recs[3]==null?"":recs[3]).append("</td><td colspan='2'>")
						 .append(recs[4]==null?"":recs[4]).append("</td><td colspan='2'>").append(recs[5]==null?"":recs[5]).append("</td></tr>");
					}
				}
				exportData.append("<tr>"); 
				exportData.append("<td>").append("合计").append("</td><td colspan='2'>").append(firstTotal).append("</td><td colspan='2'>").append(secondTotal)
				.append("</td><td colspan='2'>").append(threeTotal).append("</td><td colspan='2'>").append(fourTotal).append("</td><td colspan='2'>")
				.append(fiveTotal).append("</td></tr>");
			
		}
			exportData.append("</table>");
			String temp="燃料运行班"+Mon+"月上煤量汇总";
			String filename = new String(temp.getBytes("gbk"), "ISO8859_1");
			this.exportData(exportData.toString(), filename);
	}
	
	public void exportData(String data,String fileName){
		
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition","inner; filename="+fileName+".xls");
		PrintWriter write = null;
		try {
			write = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		write.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		 write.write(data); 
		 //response.setContentType("application/vnd.ms-excel;charset=utf-8");
		 
		write.flush();
		write.close();  
	}
	// ******************************************get/set变量方法******************************************

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

}
