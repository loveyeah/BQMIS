package power.web.productiontec.insulation.action;

import java.io.File;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJSgdj;
import power.ejb.productiontec.insulation.PtJyjdJSgdjFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class SGDJAction extends UploadFileAbstractAction{

	private PtJyjdJSgdj sgdj;
	private PtJyjdJSgdjFacadeRemote remote;
	private File annexFile;
	
	public SGDJAction()
	{
		remote = (PtJyjdJSgdjFacadeRemote)factory.getFacadeRemote("PtJyjdJSgdjFacade");
	}
	
	/**
	 * 增加一条绝缘事故记录
	 */
	public void addSGDJInfo()
	{
		String filePath = request.getParameter("filePath");
		sgdj.setEnterpriseCode(employee.getEnterpriseCode());
		sgdj.setFillBy(employee.getWorkerCode());
		sgdj.setFillDate(new Date());
		
		if (!filePath.equals("")) {
			String result = filePath
					.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(annexFile, fileName, "productionrec");
			sgdj.setAnnex(Temp);
		}
		sgdj = remote.save(sgdj);
		write("{success : true,msg :'增加成功！'}");
	}

	/**
	 * 修改一条绝缘事故记录
	 */
	public void updateSGDJInfo() {

		PtJyjdJSgdj model = remote.findById(sgdj.getJysgId());
		String filePath = request.getParameter("filePath");
		if (model != null) {
			if (!filePath.equals("")) {
				if (model.getAnnex() != null
						&& filePath.equals(model.getAnnex().substring(
								model.getAnnex().lastIndexOf("/") + 1))) {
					sgdj.setAnnex(model.getAnnex());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(annexFile, fileName, "productionrec");
					sgdj.setAnnex(Temp);

				}
			}
			model.setAccidentTitle(sgdj.getAccidentTitle());
			model.setEquCode(sgdj.getEquCode());
			model.setAnnex(sgdj.getAnnex());
			model.setEquName(sgdj.getEquName());
			model.setHappenDate(sgdj.getHappenDate());
			model.setHandleDate(sgdj.getHandleDate());
			model.setHandleStatus(sgdj.getHandleStatus());
			model.setReasonAnalyse(sgdj.getReasonAnalyse());
			model.setMemo(sgdj.getMemo());
			model.setFillBy(employee.getWorkerCode());
			model.setFillDate(new Date());
			model.setEnterpriseCode(employee.getEnterpriseCode());

			remote.update(model);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条绝缘事故记录
	 */
	public void deleteSGDJInfo()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 显示绝缘事故列表信息
	 * @throws JSONException 
	 */
	public void findSGDJList() throws JSONException
	{
		String equName = request.getParameter("equName");
		String sDate = request.getParameter("sDate");
		String eDate = request.getParameter("eDate");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) 
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findAllList(employee.getEnterpriseCode(), equName, sDate, eDate, start,limit);
		}
		else {
			obj = remote.findAllList(employee.getEnterpriseCode(), equName, sDate, eDate);
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	
	public PtJyjdJSgdj getSgdj() {
		return sgdj;
	}

	public void setSgdj(PtJyjdJSgdj sgdj) {
		this.sgdj = sgdj;
	}

	public File getAnnexFile() {
		return annexFile;
	}

	public void setAnnexFile(File annexFile) {
		this.annexFile = annexFile;
	}
}
