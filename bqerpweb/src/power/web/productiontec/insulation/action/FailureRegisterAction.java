package power.web.productiontec.insulation.action;

import java.io.File;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.insulation.PtJyjdJQxdj;
import power.ejb.productiontec.insulation.PtJyjdJQxdjFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class FailureRegisterAction extends UploadFileAbstractAction{

	private PtJyjdJQxdjFacadeRemote remote;
	private PtJyjdJQxdj failure;
	private File annexFile;
	public FailureRegisterAction()
	{
		remote = (PtJyjdJQxdjFacadeRemote)factory.getFacadeRemote("PtJyjdJQxdjFacade");
	}
	
	/**
	 * 查找绝缘缺陷记录列表
	 * @throws JSONException
	 */
	public void findQxdjList() throws JSONException
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
			obj = remote.findQxdjList(employee.getEnterpriseCode(), equName, sDate, eDate, start,limit);
		}
		else {
			obj = remote.findQxdjList(employee.getEnterpriseCode(), equName, sDate, eDate);
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
	
	/**
	 * 增加一条绝缘缺陷记录
	 */
	public void addQxdjInfo()
	{
		String filePath = request.getParameter("filePath");
		
		if (!filePath.equals("")) {
			String result = filePath.substring(filePath.lastIndexOf("\\") + 1);
			String fileName = result.replaceAll(" ", "");
			String[] filetemp = fileName.split("\\.");
			if (filetemp[1].equals("txt")) {
				filetemp[1] = ".doc";
				fileName = filetemp[0] + filetemp[1];
			}
			String Temp = uploadFile(annexFile, fileName, "productionrec");
			failure.setAnnex(Temp);
		}
		failure.setEnterpriseCode(employee.getEnterpriseCode());
		failure.setFillBy(employee.getWorkerCode());
		
		failure = remote.save(failure);
		write("{success : true,msg :'增加成功！'}");
	}
	
	/**
	 * 修改一条绝缘缺陷记录
	 */
	public void updateQxdjInfo()
	{
		PtJyjdJQxdj model = remote.findById(failure.getJyqxId());
		String filePath = request.getParameter("filePath");
		if (model != null) {
			if (!filePath.equals("")) {
				if (model.getAnnex() != null
						&& filePath.equals(model.getAnnex().substring(
								model.getAnnex().lastIndexOf("/") + 1))) {
					failure.setAnnex(model.getAnnex());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(annexFile, fileName,
							"productionrec");
					failure.setAnnex(Temp);

				}
			}
			model.setAccidentTitle(failure.getAccidentTitle());
			model.setAnnex(failure.getAnnex());
			model.setBugStatus(failure.getBugStatus());
			model.setClearTime(failure.getClearTime());
			model.setEquCode(failure.getEquCode());
			model.setEquName(failure.getEquName());
			model.setFindTime(failure.getFindTime());
			model.setReasonAnalyse(failure.getReasonAnalyse());
			model.setMemo(failure.getMemo());
			model.setFillBy(employee.getWorkerCode());
			model.setFillDate(new Date());
			
			remote.update(model);
			write("{success : true,msg :'修改成功！'}");
		}
	}
	
	/**
	 * 删除一条或多条绝缘缺陷记录
	 */
	public void deleteQxdjInfo()
	{
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	public PtJyjdJQxdj getFailure() {
		return failure;
	}
	public void setFailure(PtJyjdJQxdj failure) {
		this.failure = failure;
	}
	public File getAnnexFile() {
		return annexFile;
	}
	public void setAnnexFile(File annexFile) {
		this.annexFile = annexFile;
	}
}
