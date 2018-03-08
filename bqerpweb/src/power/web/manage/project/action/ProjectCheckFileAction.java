package power.web.manage.project.action;

import java.io.File;
import java.io.IOException;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjCCheckFile;
import power.ejb.manage.project.PrjCCheckFileFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class ProjectCheckFileAction extends UploadFileAbstractAction {
	
	private PrjCCheckFile checkFile;
	
	private File fjdocFile;
	
	private PrjCCheckFileFacadeRemote remote;
	
	public ProjectCheckFileAction() {
		remote = (PrjCCheckFileFacadeRemote) factory.getFacadeRemote("PrjCCheckFileFacade");
	}
	
	public File getFjdocFile() {
		return fjdocFile;
	}

	public void setFjdocFile(File fjdocFile) {
		this.fjdocFile = fjdocFile;
	}

	// 查询验收书模版
	@SuppressWarnings("unchecked")
	public void findCheckFileList() throws JSONException {
		String fileName = request.getParameter("fuzzytext");
		String flag = request.getParameter("flag");
		PageObject obj = remote.findAll(employee.getEnterpriseCode(), fileName, flag);
		List<PrjCCheckFile> list = obj.getList();
		write(JSONUtil.serialize(list));
	}

	// 增加验收书模版
	public void addCheckFile() throws IOException,CodeRepeatException {

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
			String Temp = uploadFile(fjdocFile, fileName, "project");
			checkFile.setFileUrl(Temp);
		}
		checkFile.setLastModifiedBy(employee.getWorkerCode());
		checkFile.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(checkFile);
		write("{success:true,data:'数据保存成功！'}");

	}

	// 修改验收书模版
	public void updateCheckFile() throws IOException {
		PrjCCheckFile entity = remote.findById(checkFile.getCheckFileId());
		String filePath = request.getParameter("filePath");
		if (entity != null) {
			if (!filePath.equals("")) {
				if (entity.getFileUrl() != null
						&& filePath.equals(entity.getFileUrl().substring(
								entity.getFileUrl().lastIndexOf("/") + 1))) {
					checkFile.setFileUrl(entity.getFileUrl());
				} else {
					String result = filePath.substring(filePath
							.lastIndexOf("\\") + 1);
					String fileName = result.replaceAll(" ", "");
					String[] filetemp = fileName.split("\\.");
					if (filetemp[1].equals("txt")) {
						filetemp[1] = ".doc";
						fileName = filetemp[0] + filetemp[1];
					}
					String Temp = uploadFile(fjdocFile, fileName, "project");
					checkFile.setFileUrl(Temp);
					
				}
			}
		checkFile.setLastModifiedBy(employee.getWorkerCode());
		checkFile.setEnterpriseCode(employee.getEnterpriseCode());
		remote.update(checkFile);
		write("{success:true,data:'数据保存成功！'}");
		}
	}

	// 删除验收书模版
	public void deleteCheckFile() {
		String checkFileIds = request.getParameter("checkFileId");
		if (checkFileIds != null) {
//			PrjCCheckFile model = remote.findById(Long.valueOf(checkFileId));
			remote.deleteMulti(checkFileIds);
//			model.setIsUse("N");
//			remote.update(model);
			write("{success:true,data:'数据删除成功！'}");
		}
	}
	
	public PrjCCheckFile getCheckFile() {
		return checkFile;
	}

	public void setCheckFile(PrjCCheckFile checkFile) {
		this.checkFile = checkFile;
	}

}
