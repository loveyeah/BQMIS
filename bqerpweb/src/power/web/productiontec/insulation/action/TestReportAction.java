package power.web.productiontec.insulation.action;

import java.io.File;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJSybglh;
import power.ejb.productiontec.insulation.PtJyjdJSybglhFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class TestReportAction extends UploadFileAbstractAction{

	private PtJyjdJSybglhFacadeRemote remote;
	private PtJyjdJSybglh report;
	private File annex;
	
	public TestReportAction()
	{
		remote = (PtJyjdJSybglhFacadeRemote)factory.getFacadeRemote("PtJyjdJSybglhFacade");
	}

	/**
	 * 增加一条绝缘监督实验报告记录
	 */
	public void addSybgInfo()
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
		remote.save(report);
		write("{success : true,msg :'增加成功！'}");
	}
	
	/**
	 * 修改一条绝缘监督实验报告记录
	 */
	public void updateSybgInfo()
	{
		PtJyjdJSybglh entity=remote.findById(report.getReportId());
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
			entity.setEquCode(report.getEquCode());
			entity.setEquName(report.getEquName());
			entity.setMemo(report.getMemo());
			entity.setContent(report.getContent());
			
			write("{success : true,msg :'修改成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条绝缘监督实验报告记录
	 */
	public void deleteSybgInfo()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	
	/**
	 * 查找绝缘监督实验报告记录列表
	 * @throws JSONException
	 */
	public void findSybgAllList() throws JSONException
	{
		String equName = request.getParameter("equName");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(equName, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = remote.findAll(equName, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	public PtJyjdJSybglh getReport() {
		return report;
	}

	public void setReport(PtJyjdJSybglh report) {
		this.report = report;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
	}
	
}
