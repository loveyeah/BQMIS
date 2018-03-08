package power.web.productiontec.retrenchenergy.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnfx;
import power.ejb.productiontec.retrenchenergy.PtJnjdJJnfxFacadeRemote;

import power.web.comm.UploadFileAbstractAction;
/**
 * 节能分析
 * @author fyyang 090706
 *
 */
@SuppressWarnings("serial")
public class RetEnergyAnalyseAction extends UploadFileAbstractAction{

	private PtJnjdJJnfxFacadeRemote analyseRemote;
	private PtJnjdJJnfx analyse;
	private File annex;
	
	public  RetEnergyAnalyseAction()
	{
		analyseRemote=(PtJnjdJJnfxFacadeRemote)factory.getFacadeRemote("PtJnjdJJnfxFacade");
	}
	public PtJnjdJJnfx getAnalyse() {
		return analyse;
	}
	public void setAnalyse(PtJnjdJJnfx analyse) {
		this.analyse = analyse;
	}
	
	public void findEnergAnalyseList() throws JSONException
	{
		String dateMonth=request.getParameter("dateMonth");
		
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = analyseRemote.findAll(dateMonth, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = analyseRemote.findAll(dateMonth, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addEnergAnalyseInfo() throws ParseException
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
			analyse.setContent(Temp);
		}
		String month=request.getParameter("month");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		analyse.setMonth(df.parse(month+"-01"));
		
		analyse.setEnterpriseCode(employee.getEnterpriseCode());
		analyseRemote.save(analyse);
		write("{success : true,msg :'数据增加成功！'}");
	}
	
	public void updateEnergAnalyseInfo() throws ParseException
	{
		PtJnjdJJnfx entity=analyseRemote.findById(analyse.getAnalyseId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					analyse.setContent(entity.getContent());
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
					analyse.setContent(Temp);
					
				}
			}
			
			String month=request.getParameter("month");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			entity.setMonth(df.parse(month+"-01"));
			entity.setContent(analyse.getContent());
			entity.setMemo(analyse.getMemo());
			
			entity.setTitle(analyse.getTitle());
			analyseRemote.update(entity);
			write("{success : true,msg :'数据修改成功！'}");
		}
	}
	
	public void deleteEnergAnalyseInfo()
	{
		String ids=request.getParameter("ids");
		analyseRemote.deleteMulti(ids);
		write("{success : true,msg :'数据删除成功！'}");
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}
}
