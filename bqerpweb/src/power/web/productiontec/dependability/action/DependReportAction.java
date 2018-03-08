package power.web.productiontec.dependability.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.report.ptJDependReport;
import power.ejb.productiontec.report.ptJDependReportFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class DependReportAction extends UploadFileAbstractAction{
	
	private ptJDependReportFacadeRemote remote;
	private ptJDependReport model;
	
	private File annex;//文件路径
	private String dependEntry;//上传人工号
	private String dependYear;//年份
	private String dependName;//报表名称
	private String fillDate;//上传时间
	private String dependMemo;//备注
	private String dependId;//Id
	private String dependEntryCode;
	private String smartDate1;
	
	public DependReportAction(){
		remote = (ptJDependReportFacadeRemote)factory.getFacadeRemote("ptJDependReportFacade");
	}
	
	private Date str2date(String s) throws java.text.ParseException {
		Date datea = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(s);
		datea = date;
		return datea;
}
	/**
	 * 修改可靠性报表 ghzhou 091102
	 */
	public void updateDependYearReport(){
		model = new ptJDependReport();
		//默认时间为当前年份
		String dependYear = request.getParameter("dependYear");
		ptJDependReport entity = remote.findById(dependId);
		if((!"".equals(annex)) && (!"".equals(dependName))){
			String filePath = request.getParameter("filePath");
			String timeType = request.getParameter("timeType");
			if (!filePath.equals("")) {
				String result = filePath
						.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Temp = uploadFile(annex, fileName, "productionrec");
				model.setDependPath(Temp);
			}
			try{
				model.setDependDate(str2date(fillDate));
			}
			catch(Exception e){	
			}
			model.setDependId(Long.parseLong(dependId));
			model.setDependEntry(dependEntry);
			model.setDependMemo(dependMemo);
			model.setDependName(dependName);
			model.setDependMemo(dependMemo);
			model.setDependTimeType(timeType);
			if("Y".equals(timeType)){
				model.setDependYear(dependYear);//年报
			}else if("M".equals(timeType)){
				model.setDependYear(dependYear);
			}else {
				model.setDependYear(dependYear);//季报
			}
			remote.update(model);
		}
	}
	/**
	 *新增可靠性报表 ghzhou 091029
	 */
	public void addDependYearReport(){
		model = new ptJDependReport();
		//默认时间为当前年份
		String dependYear = request.getParameter("dependYear");
		if((!"".equals(annex)) && (!"".equals(dependName))){
			String filePath = request.getParameter("filePath");
			String timeType = request.getParameter("timeType");
			if (!filePath.equals("")) {
				String result = filePath
						.substring(filePath.lastIndexOf("\\") + 1);
				String fileName = result.replaceAll(" ", "");
				String[] filetemp = fileName.split("\\.");
				if (filetemp[1].equals("txt")) {
					filetemp[1] = ".doc";
					fileName = filetemp[0] + filetemp[1];
				}
				String Temp = uploadFile(annex, fileName, "productionrec");
				model.setDependPath(Temp);
			}
			try{
				model.setDependDate(str2date(fillDate));
			}
			catch(Exception e){	
			}
			model.setDependEntry(dependEntryCode);
			model.setDependEntryName(dependEntry);
			model.setDependMemo(dependMemo);
			model.setDependName(dependName);
			model.setDependMemo(dependMemo);
			model.setDependTimeType(timeType);
			if("Y".equals(timeType)){
				model.setDependYear(dependYear);//年报
			}else if("M".equals(timeType)){
				model.setDependYear(dependYear);
			}else {
				model.setDependYear(dependYear + smartDate1);//季报
			}
			remote.save(model);
		}
	}
	
	/**
	 * 查询可靠性年报表 ghzhou 091029
	 * @throws JSONException
	 */
	public void findDependOnlyYear() throws JSONException{
 		String dependType = request.getParameter("dependType");
		if(dependType == null){
			dependType = "";
		}
		String temp = dependEntry;
		//默认时间为当前年份
		String dependYear = request.getParameter("dependYear");
		String dependSession = request.getParameter("dependSession");
		Date currentDate = new Date();
		if(dependYear == null){
			dependYear = String.valueOf(currentDate.getYear());
		}
		if(dependSession != null){
			dependYear = dependSession;
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		
		if(objstart != null && objlimit != null){
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAllOnlyYear(dependType, dependYear, start,limit);
		}else{
			object = remote.findAllOnlyYear(dependType, dependYear);
		}
		String strOutput = "";
		if(object == null ||object.getList() == null)
		{
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}else{
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	/**
	 * 删除可靠性报表
	 * @return
	 */
	public void deleteDependYearReport(){
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public String getDependEntry() {
		return dependEntry;
	}

	public void setDependEntry(String dependEntry) {
		this.dependEntry = dependEntry;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}

	public String getDependDate() {
		return dependYear;
	}

	public void setDependDate(String dependYear) {
		this.dependYear = dependYear;
	}

	public String getDependName() {
		return dependName;
	}

	public void setDependName(String dependName) {
		this.dependName = dependName;
	}

	public String getFillDate() {
		return fillDate;
	}

	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}

	public String getDependMemo() {
		return dependMemo;
	}

	public void setDependMemo(String dependMemo) {
		this.dependMemo = dependMemo;
	}

	public String getDependId() {
		return dependId;
	}

	public void setDependId(String dependId) {
		this.dependId = dependId;
	}

	public String getDependYear() {
		return dependYear;
	}

	public void setDependYear(String dependYear) {
		this.dependYear = dependYear;
	}

	public String getDependEntryCode() {
		return dependEntryCode;
	}

	public void setDependEntryCode(String dependEntryCode) {
		this.dependEntryCode = dependEntryCode;
	}

	public String getSmartDate1() {
		return smartDate1;
	}

	public void setSmartDate1(String smartDate1) {
		this.smartDate1 = smartDate1;
	}

}
