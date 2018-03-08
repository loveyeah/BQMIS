package power.web.manage.stat.action;

import java.io.File;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.stat.ZbJReportLoad;
import power.ejb.manage.stat.ZbJReportLoadFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class ZbJReportLoadAction extends UploadFileAbstractAction {
	
	public ZbJReportLoad reportLoad;
	private ZbJReportLoadFacadeRemote reportLoadFacade;
	private File fjdocFile;
	
	public ZbJReportLoad getReportLoad() {
		return reportLoad;
	}

	public void setReportLoad(ZbJReportLoad reportLoad) {
		this.reportLoad = reportLoad;
	}

	public File getFjdocFile() {
		return fjdocFile;
	}

	public void setFjdocFile(File fjdocFile) {
		this.fjdocFile = fjdocFile;
	}

	public ZbJReportLoadAction() {
		reportLoadFacade = (ZbJReportLoadFacadeRemote) factory.getFacadeRemote("ZbJReportLoadFacade");
	}
	
	public void saveOrUpdateReportLoad() {
		String filePath = request.getParameter("filePath");
		if (!filePath.equals("")) {
			String result = filePath
					.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			reportLoad.setLoadName(fileName);
			String Temp = uploadFile(fjdocFile, fileName, "project");
			reportLoad.setAnnexAddress(Temp);
		}
		reportLoad.setLoadBy(employee.getWorkerCode());
		reportLoad.setEnterpriseCode(employee.getEnterpriseCode());
		reportLoad.setFirstDeptCode(employee.getDeptCode());
		try {
			reportLoadFacade.save(reportLoad);
		} catch (CodeRepeatException e) {
			write("{success:true,data:'同一个报表每天只能上传一次，如果想重新上传，请先将原来的删除!'}");
		}
		write("{success:true,data:'数据保存成功！'}");
	}
	
	public void queryReportLoadListByReportCode() throws JSONException {
		String reportCode = request.getParameter("reportCode");
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");//add by wpzhu 20100605
		String loadName = request.getParameter("loadName");
		String objStart = request.getParameter("start");
		/** 查询行数 */
		String objLimit = request.getParameter("limit");
		PageObject result = new PageObject();
		if (objStart != null && objLimit != null) {
			result = reportLoadFacade.findAllByReportCode(startTime, endTime, loadName, reportCode, employee.getEnterpriseCode(), Integer.parseInt(objStart),Integer.parseInt(objLimit));
		} else {
		    result  = reportLoadFacade.findAllByReportCode(startTime, endTime, loadName, reportCode, employee.getEnterpriseCode());
		}
		String str = "{\"list\":[],\"totalCount\":}";
		if (result != null) {
			str = JSONUtil.serialize(result);
		} 
		write(str); 
		
	}
	
	// add by wpzhu 20100605
	public void delReportLoad()
	{
		String ids = request.getParameter("loadID");
		reportLoadFacade.deleteReportLoad(ids,employee.getWorkerCode());
		write("{success:true,data:'数据删除成功！'}");
	}

}
