package power.web.manage.project.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.ConCItemSource;
import power.ejb.manage.contract.ConCItemSourceFacadeRemote;
import power.ejb.manage.project.PrjCStatus;
import power.ejb.manage.project.PrjCStatusFacadeRemote;
import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoFacadeRemote;
import power.web.comm.UploadFileAbstractAction;

@SuppressWarnings("serial")
public class ProjectApplyAction extends UploadFileAbstractAction {
	PrjJInfoFacadeRemote remote;
	PrjCStatusFacadeRemote Sremote;
	private PrjJInfo prjjInfo;
	private File annex;
	private int start;
	private int limit;

	public ProjectApplyAction() {
		remote = (PrjJInfoFacadeRemote) factory
				.getFacadeRemote("PrjJInfoFacade");
		Sremote = (PrjCStatusFacadeRemote) factory
				.getFacadeRemote("PrjCStatusFacade");
	}

	// 项目立项保存
	public void saveProjectApply() throws IOException, CodeRepeatException {
		try {
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
				String Temp = uploadFile(annex, fileName, "project");
				prjjInfo.setAnnex(Temp);
			}
			prjjInfo.setEnterpriseCode(employee.getEnterpriseCode());
			PrjJInfo PrjNo = remote.save(prjjInfo);
			write("{success : true,msg :'操作成功',PrjNo:'" + PrjNo.getPrjNo()
					+ "',PrjNoShow:'" + PrjNo.getPrjNoShow() + "',annex:'"
					+ PrjNo.getAnnex() + "'}");
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	// 项目验收申请保存修改
	public void updateProjectCheck() throws CodeRepeatException {
		try {
			String projectNo = request.getParameter("projectNo");
			PrjJInfo entity = remote.findByPrjNo(projectNo, employee
					.getEnterpriseCode());
			
			entity.setCmlAppraisal(prjjInfo.getCmlAppraisal());
			entity.setFactStartDate(prjjInfo.getFactStartDate());
			entity.setFactEndDate(prjjInfo.getFactEndDate());
			entity.setFactTimeLimit(prjjInfo.getFactTimeLimit());
//addby bjxu 20091015
			entity.setPrjChangeMemo(prjjInfo.getPrjChangeMemo());
			entity.setPrjDataMove(prjjInfo.getPrjDataMove());
			
			remote.update(entity);
			write("{success : true,msg :'操作成功'}");
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	// 项目立项保存修改
	public void updateProjectApply() throws CodeRepeatException {
		try {
			String projectNo = request.getParameter("projectNo");
			PrjJInfo entity = remote.findByPrjNo(projectNo, employee
					.getEnterpriseCode());
			String filePath = request.getParameter("filePath");
			if (entity != null || !projectNo.equals("")) {
				if (filePath != null && !filePath.equals("")) {
					if (entity.getAnnex() != null
							&& filePath.equals(entity.getAnnex().substring(
									entity.getAnnex().lastIndexOf("/") + 1))) {
						prjjInfo.setAnnex(entity.getAnnex());
					} else {
						String result = filePath.substring(filePath
								.lastIndexOf("\\") + 1);
						String fileName = result.replaceAll(" ", "");
						String[] filetemp = fileName.split("\\.");
						if (filetemp[1].equals("txt")) {
							filetemp[1] = ".doc";
							fileName = filetemp[0] + filetemp[1];
						}
						String Temp = uploadFile(annex, fileName, "project");
						prjjInfo.setAnnex(Temp);
					}
				}
				prjjInfo.setId(entity.getId());
				prjjInfo.setPrjStatus(entity.getPrjStatus());
				prjjInfo.setPrjNo(entity.getPrjNo());
				// prjjInfo.setPrjNoShow(entity.getPrjNoShow());
				prjjInfo.setAccWorkFlowNo(entity.getAccWorkFlowNo());
				prjjInfo.setEnterpriseCode(employee.getEnterpriseCode());
				prjjInfo.setIsUse(entity.getIsUse());
				PrjJInfo PrjNo = (remote.update(prjjInfo));
				write("{success : true,msg :'操作成功',PrjNo:'" + PrjNo.getPrjNo()
						+ "',PrjNoShow:'" + PrjNo.getPrjNoShow() + "',annex:'"
						+ PrjNo.getAnnex() + "'}");
			}
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	// 项目安全生产部、商务部修改
	public void updateAprove() throws CodeRepeatException {
		try {

			String projectNo = request.getParameter("projectNo");
			PrjJInfo entity = remote.findByPrjNo(projectNo, employee
					.getEnterpriseCode());
			entity.setPrjTypeId(prjjInfo.getPrjTypeId());
			entity.setPrjNoShow(prjjInfo.getPrjNoShow());
			remote.update(entity);
			write("{success : true,msg :'操作成功'}");
		} catch (Exception e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	// 项目删除
	public void delProjectApply() throws CodeRepeatException {
		String projectNo = request.getParameter("projectNo");
		PrjJInfo entity = remote.findByPrjNo(projectNo, employee
				.getEnterpriseCode());
		if (entity != null) {
			entity.setIsUse("N");
			remote.update(entity);
			write("{success : true,msg :'删除成功'}");
		}
	}

	// 立项申请列表

	public void getProjectApplyList() throws JSONException {
		try {
			String prjNo = request.getParameter("prjNo");

			String prjYear = request.getParameter("prjYear");
			String prjTypeId = request.getParameter("prjTypeId");
			String prjStatus = request.getParameter("prjStatus");
			String chargeDep = request.getParameter("chargeDep");
			String argFuzzy = request.getParameter("argFuzzy");
			String prjStatusType = request.getParameter("prjStatusType");
			String timefromDate = request.getParameter("timefromDate");
			String timetoDate = request.getParameter("timetoDate");
			String isSigned = request.getParameter("isSigned");
			String type = request.getParameter("type");
			String workCode = employee.getWorkerCode();
			// 判断工作流类别
			String workFlowType = request.getParameter("workFlowType");
			// 判断使用entryIds
			String entryType = request.getParameter("entryType");

			if (type != null) {
				workCode = "";
			}

			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			String entryIds = workflowService.getAvailableWorkflow(
					new String[] { workFlowType }, employee.getWorkerCode());

			PageObject obj = remote.FindByMoreCondition(workFlowType,
					entryType, entryIds, workCode, prjNo, prjYear, prjTypeId,
					prjStatus, chargeDep, argFuzzy, prjStatusType,
					timefromDate, timetoDate, start, limit);
			if (obj != null) {
				write(JSONUtil.serialize(obj));
				// System.out.print(JSONUtil.serialize(obj));
			} else {
				write("{list : [],totalCount : 0}");
			}
		} catch (JSONException e) {
			throw e;
		}

	}

	// 返回项目编号
	public void getProjectNoShow() throws JSONException {
		String prjNo = request.getParameter("prjNo");
		PrjJInfo model = remote
				.findByPrjNo(prjNo, employee.getEnterpriseCode());

		if (model != null) {
			write(model.getPrjNoShow());
		} else {
			// modify by fyyang 090618
			write("");
		}

	}

	// 根据类型选项目编号
	public void getProjectTypeByPrjNo() throws JSONException {
		String prjTypeId = request.getParameter("prjTypeId");
		String queryKey = request.getParameter("queryKey");
		String typeChoose = request.getParameter("Choose");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject obj = remote.getProjectTypeByPrjNo(typeChoose, prjTypeId,
				queryKey, start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{list : [],totalCount : 0}");
		}

	}

	// 立项审批列表
	// public void getProjectApproveList() throws JSONException {
	// String timefromDate = request.getParameter("timefromDate");
	// String timetoDate = request.getParameter("timetoDate");
	// String isWait = request.getParameter("isWait");
	// String isSign = request.getParameter("isSign");
	// PageObject pg = remote.FindApproveCondition(timefromDate, timetoDate,
	// isWait, isSign, employee.getWorkerCode(), start, limit);
	// write(JSONUtil.serialize(pg));
	// }

	// 项目状态
	@SuppressWarnings("unchecked")
	public void useStauslist() {
		String prjStatusType = request.getParameter("prjStatusType");
		List<PrjCStatus> list = Sremote.findAll(prjStatusType);
		String str = "";
		int i = 0;
		for (PrjCStatus model : list) {
			i++;
			str += "[\"" + model.getPrjStatusName() + "\",\""
					+ model.getPrjStatusId() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		System.out.print(str);
		write(str);
	}

	/**
	 * 文件上传 （src:原文件，dst：目标文件）
	 * 
	 * @param src
	 * @param dst
	 */
	private static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), 16 * 1024);
			out = new BufferedOutputStream(new FileOutputStream(dst), 16 * 1024);
			byte[] buffer = new byte[16 * 1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 */
	public String uploadefile(String mycode, String filePath) {
		String dstPath = "";
		if (!filePath.equals("")) {
			try {
				File myfile = new File(filePath);
				String uploadPath = session.getServletContext()
						.getInitParameter("upload_dir");

				File f = new File(uploadPath);
				if (!f.exists()) {
					f.mkdir();
				}
				f = new File(uploadPath + "//project");
				if (!f.exists()) {
					f.mkdir();
				}
				String myfileName = myfile.getName();
				String fileType = myfileName.substring(myfileName
						.lastIndexOf("."));
				if (fileType.equals(".txt")) {
					fileType = ".doc";
				}

				dstPath = uploadPath + "/project/" + mycode + fileType;
				File dstFile = new File(dstPath);
				copy(annex, dstFile);
				return (mycode + fileType);
			} catch (Exception e) {
				return null;
			}
		} else {
			return dstPath;
		}
	}

	public PrjJInfo getPrjjInfo() {
		return prjjInfo;
	}

	public void setPrjjInfo(PrjJInfo prjjInfo) {
		this.prjjInfo = prjjInfo;
	}

	public File getAnnex() {
		return annex;
	}

	public void setAnnex(File annex) {
		this.annex = annex;
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
	
	public void getItemSourceInfo() throws JSONException
	{
		String code = request.getParameter("code");
		ConCItemSourceFacadeRemote sourRemote = (ConCItemSourceFacadeRemote)factory.getFacadeRemote("ConCItemSourceFacade");
		ConCItemSource temp = sourRemote.getRecordByCode(code);
		write(JSONUtil.serialize(temp));
		
	}
}
