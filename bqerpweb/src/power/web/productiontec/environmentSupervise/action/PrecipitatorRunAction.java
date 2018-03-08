package power.web.productiontec.environmentSupervise.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJSgdjFacadeRemote;
import power.ejb.productiontec.metalSupervise.PtJReport;
import power.ejb.productiontec.metalSupervise.PtJReportFacadeRemote;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnfx;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class PrecipitatorRunAction extends UploadFileAbstractAction{
	private PtJReportFacadeRemote remote;
	private File annex;
	private PtJReport model;
	public PrecipitatorRunAction(){
		remote = (PtJReportFacadeRemote)factory.getFacadeRemote("PtJReportFacade");
	}
	public PtJReport getModel() {
		return model;
	}
	public void setModel(PtJReport model) {
		this.model = model;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
	public void findPtJReportList() throws JSONException{
		String year = request.getParameter("year");
		String smartDate = request.getParameter("smartDate");
		String reportType = request.getParameter("reportType");
		String timeType = request.getParameter("timeType");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(year,smartDate,reportType,timeType, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = remote.findAll(year,smartDate,reportType,timeType, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		System.out.println(strOutput);
		write(strOutput);
	}
	public void addPtJReportInfo() throws ParseException{	
		String reportType = request.getParameter("reportType");
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
			model.setContent(Temp);
		}
		model.setReportType(reportType);
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setFillDate(new Date());
		model.setTimeType(Long.parseLong(timeType));
		model.setCheckMark(0L);
		remote.save(model);
		write("{success : true,msg :'增加成功！'}");
	}
	
	public void updatePtJReportInfo() throws ParseException{
		PtJReport entity=remote.findById(model.getReportId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					model.setContent(entity.getContent());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(annex, fileName, "productionrec");
					model.setContent(Temp);
					
				}
			}
			entity.setTopic(model.getTopic());
			entity.setFillBy(model.getFillBy());
			entity.setSmartDate(model.getSmartDate());
			entity.setYear(model.getYear());
			entity.setContent(model.getContent());
			entity.setMemo(model.getMemo());
			entity.setFillDate(new Date());
			remote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	
	public void deletePtJReportInfo(){
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
}
