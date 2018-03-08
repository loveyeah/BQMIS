package power.web.productiontec.retrenchenergy.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnzj;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnzjFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

/**
 * 节能总结
 * @author jiaa
 *
 */
@SuppressWarnings("serial")
public class ReEnergySummaryAction extends UploadFileAbstractAction{

	private PtJnjdJJnzjFacadeRemote summaryRemote;
	private PtJnjdJJnzj summary;
	private File annex;
	public ReEnergySummaryAction()
	{
		summaryRemote=(PtJnjdJJnzjFacadeRemote)factory.getFacadeRemote("PtJnjdJJnzjFacade");
	}
	public void findEnergySummaryList() throws JSONException
	{
        String dateYear=request.getParameter("dateYear");
		
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = summaryRemote.findAll(dateYear, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = summaryRemote.findAll(dateYear, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addEnergySummaryInfo() throws ParseException
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
			summary.setContent(Temp);
		}
		String year=request.getParameter("year");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		summary.setYear(df.parse(year+"-01-01"));
		summary.setEnterpriseCode(employee.getEnterpriseCode());
		summaryRemote.save(summary);
		write("{success : true,msg :'增加成功！'}");
		
	}
	
	public void updateEnergySummaryInfo() throws ParseException
	{
		PtJnjdJJnzj entity=summaryRemote.findById(summary.getSummaryId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					summary.setContent(entity.getContent());
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
					summary.setContent(Temp);
					
				}
			}
			
			String year=request.getParameter("year");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			entity.setYear(df.parse(year+"-01-01"));
			entity.setContent(summary.getContent());
			entity.setMemo(summary.getMemo());
			entity.setTitle(summary.getTitle());
			summaryRemote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	public void deleteEnergySummaryInfo()
	{
		String ids=request.getParameter("ids");
		summaryRemote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	public PtJnjdJJnzj getSummary() {
		return summary;
	}
	public void setSummary(PtJnjdJJnzj summary) {
		this.summary = summary;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
}
