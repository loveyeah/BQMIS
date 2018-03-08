package power.web.productiontec.retrenchenergy.action;

import java.io.File;
import java.text.SimpleDateFormat;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.retrenchenergy.PtJnjdJZcqjngh;
import power.ejb.productiontec.retrenchenergy.PtJnjdJZcqjnghFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

/**
 * 中长期节能规划
 * @author fyyang 090707
 *
 */
@SuppressWarnings("serial")
public class RetEnergyProgramAction extends UploadFileAbstractAction{
	private PtJnjdJZcqjnghFacadeRemote programRemote;
	private PtJnjdJZcqjngh program;
	private File annex;
	public RetEnergyProgramAction()
	{
		programRemote=(PtJnjdJZcqjnghFacadeRemote)factory.getFacadeRemote("PtJnjdJZcqjnghFacade");
	}
	
	public void findEnergyProgramList() throws JSONException
	{
         String dateYear=request.getParameter("dateYear");
		
		Object objstart = request.getParameter("start");
		
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = programRemote.findAll(dateYear, employee.getEnterpriseCode(), start,limit);
		} else {
			
			object = programRemote.findAll(dateYear, employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	public void addEnergyProgramInfo()
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
			program.setContent(Temp);
		}
		program.setEnterpriseCode(employee.getEnterpriseCode());
		programRemote.save(program);
		write("{success : true,msg :'增加成功！'}");
	}
	public void updateEnergyProgramInfo()
	{
		PtJnjdJZcqjngh entity=programRemote.findById(program.getJnghzdId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getContent() != null
						&& filePath.equals(entity.getContent().substring(
								entity.getContent().lastIndexOf("/") + 1))) {
					program.setContent(entity.getContent());
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
					program.setContent(Temp);
					
				}
			}
			entity.setContent(program.getContent());
			entity.setCommonDate(program.getCommonDate());
			entity.setMainTopic(program.getMainTopic());
			entity.setMemo(program.getMemo());
			programRemote.update(entity);
			write("{success : true,msg :'修改成功！'}");
		}
		
	}
	public void deleteEnergyProgramInfo()
	{
		String ids=request.getParameter("ids");
		programRemote.deleteMulti(ids);
		write("{success : true,msg :'删除成功！'}");
	}
	
	
	public PtJnjdJZcqjngh getProgram() {
		return program;
	}
	public void setProgram(PtJnjdJZcqjngh program) {
		this.program = program;
	}
	public File getAnnex() {
		return annex;
	}
	public void setAnnex(File annex) {
		this.annex = annex;
	}

}
