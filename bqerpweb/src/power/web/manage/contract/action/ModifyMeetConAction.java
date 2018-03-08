package power.web.manage.contract.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJConDocFacadeRemote;
import power.ejb.manage.contract.form.ConDocForm;
import power.web.comm.AbstractAction;



@SuppressWarnings("serial")
public class ModifyMeetConAction extends AbstractAction {
	private ConJConDocFacadeRemote docremote;
	private Long conid;
	private File conFile;
	//附件id
	private Long conDocId;
//合同类型
	//private Long conTypeId;
	// private String filePath;
	public ModifyMeetConAction() {
		docremote = (ConJConDocFacadeRemote) factory
				.getFacadeRemote("ConJConDocFacade");
	}

	
	/**
	 * 合同审批上传修改合同文本信息
	 * 
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void modifyMeetConInfo() throws IOException {
		System.out.println(conid.toString());
//		System.out.println(conFile.getAbsolutePath());
		if (conFile != null) {
			String filePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			PageObject obj = docremote.findConDocList(employee
					.getEnterpriseCode(), conid, type,conDocId);
			List<ConDocForm> list = obj.getList();
			if(list.size() == 0)
			{
				ConJConDoc temp = new ConJConDoc();
				temp.setKeyId(conid);
				temp.setDocType(type);
				java.io.FileInputStream fis = new java.io.FileInputStream(
						conFile);
				int index = filePath.lastIndexOf(".");
				int ssdex = filePath.lastIndexOf("\\");
				byte[] data = new byte[(int) fis.available()];
				fis.read(data);
				if (index > 0) {
					temp.setOriFileExt(filePath.substring(index + 1));
					temp.setOriFileName(filePath.substring(ssdex + 1, index));
					temp.setDocName(filePath.substring(ssdex+1, index));
				}
				temp.setDocContent(data);
				temp.setLastModifiedBy(employee.getWorkerCode());
				temp.setLastModifiedDate(new Date());
				temp.setEnterpriseCode(employee.getEnterpriseCode());
				temp.setIsUse("Y");
				
				docremote.save(temp);
			}else if(list.size() > 0)
			{
			Iterator it = obj.getList().iterator();
			if (it.hasNext()) {
				ConDocForm formmodel = (ConDocForm) it.next();
				ConJConDoc model = docremote.findById(formmodel.getConDocId());
				java.io.FileInputStream fis = new java.io.FileInputStream(
						conFile);
				int index = filePath.lastIndexOf(".");
				int ssdex = filePath.lastIndexOf("\\");
				byte[] data = new byte[(int) fis.available()];
				fis.read(data);
				if (index > 0) {
					model.setOriFileExt(filePath.substring(index + 1));
					model.setOriFileName(filePath.substring(ssdex + 1, index));
					model.setDocName(filePath.substring(ssdex+1, index));
				}
				model.setDocContent(data);
				model.setLastModifiedBy(employee.getWorkerCode());
				model.setLastModifiedDate(new Date());								
				
					docremote.update(model);
				}			
				write("{success:true,data:'数据保存成功！'}");

			}

		}
	}



	public Long getConid() {
		return conid;
	}

	public void setConid(Long conid) {
		this.conid = conid;
	}



	public File getConFile() {
		return conFile;
	}

	public void setConFile(File conFile) {
		this.conFile = conFile;
	}


	public Long getConDocId() {
		return conDocId;
	}

	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}
}
