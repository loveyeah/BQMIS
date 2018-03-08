package power.web.productiontec.retrenchenergy.action;

import java.io.File;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnkb;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnkbFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

/**
 * 节能快报
 * @author jiaa
 *
 */
@SuppressWarnings("serial")
public class ReEnergyReportAction extends UploadFileAbstractAction{
	private PtJnjdJJnkbFacadeRemote reportRemote;
	private PtJnjdJJnkb report;
	private File annex;
	public ReEnergyReportAction()
	{
		reportRemote=(PtJnjdJJnkbFacadeRemote)factory.getFacadeRemote("PtJnjdJJnkbFacade");
	}
	
	public void findEnergyReportList() throws JSONException
	{
        String title=request.getParameter("title");
		
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = reportRemote.findAll(title, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = reportRemote.findAll(title, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	public void addEnergyReportInfo()
	{
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
			String Temp = uploadFile(annex, fileName, "productionrec");
			report.setContent(Temp);
		}
		report.setEnterpriseCode(employee.getEnterpriseCode());
		reportRemote.save(report);
		write("{success : true,msg :'增加成功！'}");
	}
	
	public void updateEnergyReportInfo()
	{
		PtJnjdJJnkb entity=reportRemote.findById(report.getJnkbId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					report.setContent(entity.getContent());
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
					report.setContent(Temp);
					
				}
			}
			entity.setContent(report.getContent());
			entity.setMainTopic(report.getMainTopic());
			entity.setMemo(report.getMemo());
			reportRemote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	
	public void deleteEnergyReportInfo()
	{
		String ids=request.getParameter("ids");
		reportRemote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}

	public PtJnjdJJnkb getReport() {
		return report;
	}

	public void setReport(PtJnjdJJnkb report) {
		this.report = report;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}

}
