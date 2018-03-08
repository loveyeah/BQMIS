package power.web.message.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.message.MessageInterface;
import power.ejb.message.SysJMessage;
import power.ejb.message.SysJMessageEmp;
import power.ejb.message.SysJMessageEmpFacadeRemote;
import power.ejb.message.SysJMessageFacadeRemote;
import power.ejb.message.bussiness.HrJCompanyWorkerFacadeRemote;
import power.ejb.message.form.WorkerInfo;
import power.ejb.system.SysCUl;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class MessageAdminAction extends AbstractAction {
	private SysJMessageEmp sysmessageemp;
	private SysJMessage sysmessage;
	private MessageInterface remote;
	private SysJMessageEmpFacadeRemote meRemote;
	private SysJMessageFacadeRemote mRemote;
	private HrJCompanyWorkerFacadeRemote hrRemote;
	private Long messageId;
	private String receiveCode;
	private int start;
	private int limit;
	private File docName;

	public File getDocName() {
		return docName;
	}

	public void setDocName(File docName) {
		this.docName = docName;
	}

	public MessageAdminAction() {
		remote = (MessageInterface) Ejb3Factory.getInstance().getFacadeRemote(
				"MessageImp");
		meRemote = (SysJMessageEmpFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("SysJMessageEmpFacade");
		mRemote = (SysJMessageFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("SysJMessageFacade");
		hrRemote = (HrJCompanyWorkerFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("HrJCompanyWorkerFacade");
	}

	// 消息发送页面查询
	public void getMessageList() throws IOException, NamingException,
			JSONException {
		String startTime = request.getParameter("starttime");
		String endTime = request.getParameter("endtime");
		String statusFlag = request.getParameter("statusFlag");
		int start = (int) Long.parseLong(request.getParameter("start"));
		int limit = (int) Long.parseLong(request.getParameter("limit"));

		PageObject list = remote.findMessageList(employee.getWorkerCode(),
				startTime, endTime, statusFlag, start, limit);
		if (list != null) {
			String Str = JSONUtil.serialize(list.getList());
			write("{totalProperty:" + list.getTotalCount() + ",root:" + Str
					+ "}");
		} else {
			write("{root:[]}");
		}
	}

	// 消息接收页面查询
	public void getReceiveMessageList() throws IOException, NamingException,
			JSONException {
		String startTime = request.getParameter("startDate");
		String endTime = request.getParameter("end");
		String statusFlag = request.getParameter("statusFlag");
		PageObject list = remote.findReceiveMessageList(employee
				.getWorkerCode(), startTime, endTime, statusFlag, start, limit);
		if (list != null) {
			String reStr = JSONUtil.serialize(list.getList());
			write("{data:{totalCount:" + list.getTotalCount() + ",list:"
					+ reStr + "}}");
		} else {
			write("{data:{totalCount:0,list:[]}");
		}
	}

	/*
	 * 消息保存
	 */
	public void saveMessage() throws FileNotFoundException, IOException {
		String codes = request.getParameter("codes");
		String filePath = request.getParameter("filePath");
		String zbbmtxCode = request.getParameter("comCode");
		// 判断是否上传了文件
		if (docName != null) {
			// 文件存入数据库
			FileInputStream ins = new FileInputStream(docName);
			byte[] data = new byte[(int) ins.available()];
			ins.read(data);
			sysmessage.setDocContent(data);
			sysmessage.setDocName(this.getDocFileName(docName, filePath));
		}
		sysmessage.setEnterpriseCode(employee.getEnterpriseCode());
		sysmessage.setIsUse("Y");
		// 得到消息保存list，0标识消息为撰写状态
		List<SysJMessageEmp> list = this.getArrayList(codes, 0l, zbbmtxCode);
		remote.saveMessage(sysmessage, list);
		write("{success : true,msg:'操作成功'}");
	}

	/*
	 * 消息发送
	 */
	public void sendMessage() throws FileNotFoundException, IOException {
		String filePath = request.getParameter("filePath");
		String codes = request.getParameter("codes");
		String zbbmtxCode = request.getParameter("comCode");
		// 判断是否上传了文件
		if (docName != null) {
			// 文件存入数据库
			FileInputStream ins = new FileInputStream(docName);
			byte[] data = new byte[(int) ins.available()];
			ins.read(data);
			sysmessage.setDocContent(data);
			sysmessage.setDocName(this.getDocFileName(docName, filePath));
		}
		sysmessage.setEnterpriseCode(employee.getEnterpriseCode());
		sysmessage.setIsUse("Y");
		// 得到消息保存list，1标识消息为发送状态
		List<SysJMessageEmp> list = this.getArrayList(codes, 1l, zbbmtxCode);
		remote.saveMessage(sysmessage, list);
		write("{success : true,msg:'操作成功'}");
	}

	/*
	 * 消息更新
	 */
	public void updateMessage() {
		String id = request.getParameter("id");
		if (id != null && !"".equals(id)) {
			SysJMessageEmp model = meRemote.findById(Long.parseLong(id));
			if (model != null) {
				model.setMessageStatus(1l);
				model.setSendById(employee.getWorkerCode());
				model.setSendDate(new Date());
				meRemote.update(model);
			}
		}
		write("{success : true,msg:'操作成功'}");
	}

	/*
	 * 消息文档查看
	 */
	public void watchMessage() {
		String id = request.getParameter("id");
		SysJMessageEmp model = meRemote.findById(Long.parseLong(id));
		if (model != null) {
			model.setMessageStatus(2l);
			meRemote.update(model);
		}
	}

	// 消息删除
	public void deleteMessage() throws Exception {
		SysJMessageEmpFacadeRemote bll = (SysJMessageEmpFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("SysJMessageEmpFacade");
		String delId = request.getParameter("id");
		String[] ids = delId.split(",");
		// System.out.print(ids);
		for (int i = 0; i < ids.length; i++) {
			Long Id = Long.parseLong(ids[i]);
			SysJMessageEmp model = bll.findById(Id);
			if (model != null) {
				bll.delete(model);
			}
		}
	}

	// 消息SysJMessageEmp List
	private List<SysJMessageEmp> getArrayList(String codes, Long messageStatus,
			String zbbmtxCode) {
		List<SysJMessageEmp> list = new ArrayList();
		// 选择了消息接收人
		if (codes != null && !"".equals(codes)) {
			String[] str = codes.split(",");
			for (String s : str) {
				SysJMessageEmp model = new SysJMessageEmp();
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setMessageStatus(messageStatus);
				model.setReceiveById(s);
				model.setSendById(employee.getWorkerCode());
				model.setSendDate(new Date());
				list.add(model);
			}
		}
		// 没有选择消息接收人
		else {
			List<WorkerInfo> workerlist = null;
			// 向管理员发送消息
			if (zbbmtxCode == null || "".equals(zbbmtxCode)
					|| "管理员".equals(zbbmtxCode)) {
				BaseDataManager bdm = (BaseDataManager) factory
						.getFacadeRemote("BaseDataManagerImpl");
				List<SysCUl> ul = bdm.getAdminWorker(employee
						.getEnterpriseCode());
				if (ul != null && ul.size() > 0) {
					workerlist = new ArrayList<WorkerInfo>();
					for (SysCUl u : ul) {
						WorkerInfo tmp = new WorkerInfo();
						tmp.setWorkerCode(u.getWorkerCode());
						tmp.setWorkerName(u.getWorkerName());
						workerlist.add(tmp);
					}
				}
			} else {
				// 像客户公司联系人发送消息
				workerlist = hrRemote.findByZbbmtxCode(zbbmtxCode);
			}
			if (workerlist != null) {
				for (int i = 0; i < workerlist.size(); i++) {
					SysJMessageEmp entity = new SysJMessageEmp();
					entity.setEnterpriseCode(employee.getEnterpriseCode());
					entity.setMessageStatus(messageStatus);
					entity.setReceiveById(workerlist.get(i).getWorkerCode());
					entity.setSendById(employee.getWorkerCode());
					entity.setSendDate(new Date());
					list.add(entity);
				}
			}
		}
		return list;
	}

	// 获得上传文件名称
	private String getDocFileName(File file, String docFilePath) {
		String docfileName = "";
		if (file != null) {
			// 文件名称
			String fileName = file.getName();
			// 文件后缀名
			String fileSuffix = "";
			if (fileName.indexOf(".") != -1) {
				fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1,
						fileName.length());
			}
			// 判断用户输入的文件名是否有文件后缀
			if (docFilePath != null) {
				if (docFilePath.indexOf(".") != -1) {
					docfileName = docFilePath;
				} else {
					if (!docFilePath.trim().equals("")) {
						if (!"".equals(fileSuffix))
							docfileName = docFilePath.trim() + "." + fileSuffix;
						else
							docfileName = docFilePath.trim();
					} else {
						docfileName = fileName;
					}
				}
			}
		}
		return docfileName;
	}

	private String findContentType(String str) {
		String sFormart = "";
		String returnStr = "application/octet-stream";
		if (str != null && !str.equals("")) {
			if (str.indexOf(".") != -1) {
				sFormart = str
						.substring(str.lastIndexOf(".") + 1, str.length());
			}
		}
		if (!sFormart.equals("")) {
			if (sFormart.equals("txt")) {
				returnStr = "text/plain;charset=utf-8";
			} else if (sFormart.endsWith("doc")) {
				returnStr = "application/msword;charset=utf-8";
			} else if (sFormart.equals("xsl")) {
				returnStr = "application/x-msdownload;charset=utf-8";
			} else if (sFormart.equals("vsd")) {
				returnStr = "application/vnd.visio;charset=utf-8";
			} else if (sFormart.equals("zip")) {
				returnStr = "application/zip;charset=utf-8";
			} else if (sFormart.equals("rar")) {
				returnStr = "application/octet-stream;charset=utf-8";
			}
		}
		return returnStr;
	}

	// 下载文档附件
	public void downloadFile() throws IOException {
		// 以流的形式下载文件。
		SysJMessage model = mRemote.findById(messageId);
		// 清空response
		response.reset();
		// 设置response的Header
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(model.getDocName().getBytes(),"ISO-8859-1"));
		response.addHeader("Content-Length", "" + model.getDocContent().length);
		OutputStream toClient = new BufferedOutputStream(response
				.getOutputStream());
		// response.setContentType("application/octet-stream");
		response.setContentType(this.findContentType(model.getDocName()));
		toClient.write(model.getDocContent());
		toClient.flush();
		toClient.close();

	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public SysJMessageEmp getSysmessageemp() {
		return sysmessageemp;
	}

	public void setSysmessageemp(SysJMessageEmp sysmessageemp) {
		this.sysmessageemp = sysmessageemp;
	}

	public SysJMessage getSysmessage() {
		return sysmessage;
	}

	public void setSysmessage(SysJMessage sysmessage) {
		this.sysmessage = sysmessage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
