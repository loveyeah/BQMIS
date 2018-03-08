/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletOutputStream;
import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.TreeNode;
import power.ejb.hr.EmpOrderbyMaitenInfo;
import power.ejb.hr.HrCAppellationFacadeRemote;
import power.ejb.hr.HrCDegreeFacadeRemote;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrCEducationFacadeRemote;
import power.ejb.hr.HrCEmpTypeFacadeRemote;
import power.ejb.hr.HrCIntypeFacadeRemote;
import power.ejb.hr.HrCLanguageFacadeRemote;
import power.ejb.hr.HrCLbgzbmFacadeRemote;
import power.ejb.hr.HrCNationFacadeRemote;
import power.ejb.hr.HrCNativePlaceFacadeRemote;
import power.ejb.hr.HrCOuttypeFacadeRemote;
import power.ejb.hr.HrCPoliticsFacadeRemote;
import power.ejb.hr.HrCSchoolFacadeRemote;
import power.ejb.hr.HrCSpecialtyFacadeRemote;
import power.ejb.hr.HrCStationLevelFacadeRemote;
import power.ejb.hr.HrCStationTypeFacadeRemote;
import power.ejb.hr.HrCStudytypeFacadeRemote;
import power.ejb.hr.HrCTechnologyGradeFacadeRemote;
import power.ejb.hr.HrCTechnologyTitlesFacadeRemote;
import power.ejb.hr.HrCTypeOfWorkFacadeRemote;
import power.ejb.hr.HrCWorkidFacadeRemote;
import power.ejb.hr.HrJCarwhInvoice;
import power.ejb.hr.HrJCarwhInvoiceFacadeRemote;
import power.ejb.hr.HrJCooperateunitFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.form.DrpCommBeanInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 共通Action
 *
 * @author zhangqi
 * @version 1.0
 */
@SuppressWarnings("serial")
public class HrCommBusinessAction extends AbstractAction{
	/** 劳动合同附件*/
	private HrJCarwhInvoiceFacadeRemote invoiceRemote;
	/** 动态drp类别*/
	private String strDrpType;
	/** 附件id*/
	private Long fileId;
	/** 合同id*/
	private Long workcontractid;
	/** 附件来源*/
	private String fileOriger;
	/**文件上传**/
	private File fileUpload;
	/**文件名称**/
	private String fileUploadFileName;
	/** 文件类型*/
	private String fileUploadContentType;
	/**  部门id*/
	private Long deptId;
	/**  员工id*/
	private Long empId;
	private static final String SAVE_SUCCESS =
		"{success:true,msg:'&nbsp&nbsp&nbsp保存成功！&nbsp&nbsp&nbsp'}";
	/**
	 * 构造函数
	 */
	public HrCommBusinessAction() {
		// 劳动合同附件
		invoiceRemote = (HrJCarwhInvoiceFacadeRemote)
			factory.getFacadeRemote("HrJCarwhInvoiceFacade");
	}

	
	/**
	 * 劳动合同附件数据查询
	 */
	@SuppressWarnings("unchecked")
	public void getContractAppendFileDatas(){
		try{
			LogUtil.log("Action:检索劳动合同附件开始", Level.INFO	, null);
			// 检索
			PageObject obj = invoiceRemote
				.findAppendFileByIdAndOriger(workcontractid, fileOriger, employee.getEnterpriseCode());

			List<HrJCarwhInvoice> list = obj.getList();
			for(int i = 0; i < list.size(); i++){
				HrJCarwhInvoice entity = list.get(i);
				entity.setFileText(null);
			}
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:检索劳动合同附件成功", Level.INFO	, null);
		}catch (JSONException jsone) {
			LogUtil.log("Action:检索劳动合同附件失败。", Level.SEVERE, jsone);
			write(Constants.DATA_FAILURE);
		} catch (SQLException sqle) {
			LogUtil.log("Action:检索劳动合同附件失败。", Level.SEVERE, sqle);
			write(Constants.SQL_FAILURE);
		}
	}
	/**
	 * 上传劳动合同附件
	 */
	public void uploadContractAppendFile(){
		LogUtil.log("Action:文件的上传操作开始", Level.INFO, null);
		HrJCarwhInvoice entity = new HrJCarwhInvoice();

		InputStream bis = null;
		if (fileUpload != null) {
			if (fileUpload.exists()) {
				Long lngLength = fileUpload.length();
				int intLength = Integer.parseInt(lngLength.toString());
				try {
					bis = new BufferedInputStream(new FileInputStream(
							fileUpload), 1024);
					byte[] data = new byte[intLength];
					while (bis.read(data) == -1) {

					}
					// 文件内容
					entity.setFileText(data);
					// 劳动合同id
					entity.setWorkcontractid(workcontractid);
					// 附件id
					entity.setFileId(fileId);
					// 附件来源
					entity.setFileOriger(fileOriger);
					// 文件名
					entity.setFileName(fileUploadFileName);
					// 是否使用
					entity.setIsUse("Y");
					// 文件类型
//					entity.setFileType(fileUploadContentType);
					// 企业编码
					entity.setEnterpriseCode(employee.getEnterpriseCode());
					// 上次修改人
					entity.setLastModifiedBy(employee.getWorkerCode());
					// 记录人
					entity.setInsertby(employee.getWorkerCode());
					invoiceRemote.save(entity);
					write(Constants.UPLOAD_SUCCESS);
					LogUtil.log("Action:文件的上传操作正常结束", Level.INFO, null);
				} catch (IOException e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.IO_FAILURE);
				}catch (Exception e) {
					LogUtil.log("Action:文件的上传操作异常结束", Level.SEVERE, null);
					write(Constants.SQL_FAILURE);
				}
			}else{
				write(Constants.FILE_NOT_EXIST);
			}
		}
	}
	/**
	 * 删除劳动合同附件
	 */
	public void deleteContractAppendFile() {
		try {
			LogUtil.log("Action:删除劳动合同附件开始。", Level.INFO, null);
			HrJCarwhInvoice entity = invoiceRemote.findById(fileId);
			if(entity != null){
				// 是否使用
				entity.setIsUse(Constants.IS_USE_N);
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				invoiceRemote.update(entity);
			}
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除劳动合同附件结束。", Level.INFO, null);
		} catch (Exception e) {
			// 操作db失败
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:删除劳动合同附件失败。", Level.SEVERE, null);
		}
	}
	/**
	 * 附件下载
	 */
	public void downloadContractAppendFile(){
		try {
			LogUtil.log("Action:下载劳动合同附件开始。", Level.INFO, null);
			HrJCarwhInvoice entity = invoiceRemote.findById(fileId);

			// 情報設定
			String name = URLEncoder.encode(entity.getFileName(),
					"UTF-8");

			response.setHeader("Content-Disposition",
					"attachment; filename=" + name);
			response.setContentType("application/octet-stream");
			ServletOutputStream os = response.getOutputStream();
			BufferedOutputStream out = new BufferedOutputStream(os);
			out.write(entity.getFileText());
			out.flush();
			out.close();
			os.flush();
			os.close();
			LogUtil.log("Action:下载劳动合同附件结束。", Level.INFO, null);

		} catch (IOException e) {
			LogUtil.log("Action:下载劳动合同附件失败。", Level.SEVERE, e);
		}
	}



    /**
     * 根据部门Id, 查找其子部门或部门下的人员。<br/>
     * 返回该部门下的子部门和直属于该部门的所有人员。
     * add by fyyang 
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public void getDeptEmpTreeList() throws Exception {
		try{
			LogUtil.log("Action:查找其子部门或部门下的人员开始。", Level.INFO, null);
			// 取得部门人员处理远程对象
			BaseDataManager manage = (BaseDataManager) Ejb3Factory.getInstance()
				.getFacadeRemote("BaseDataManagerImpl");

			// 取得节点的Id
			String strNode = request.getParameter("node");
			// 是否显示班组
			String needBanzu = request.getParameter("needBanzu");
			if(strNode.indexOf("#") != -1){
				strNode = strNode.substring(1);
			}
			Long lnNode = -1L;
			if (strNode != null && !"".equals(strNode)) {
				lnNode = Long.parseLong(strNode);
			}

			// 查找子部门
			List<TreeNode> lstSubs = manage.getDeptsByPid(lnNode,needBanzu);
			if (lstSubs != null && lstSubs.size() > 0) {
				for (int intCnt = 0; intCnt < lstSubs.size(); intCnt++) {
					TreeNode dept = lstSubs.get(intCnt);
					// 设置节点类型
					dept.setCls("folder");
					dept.setId("#" + dept.getId());
					dept.setLeaf(false);
				}
			}

			// 取得人员基本信息处理远程对象
			BaseDataManager bll = (BaseDataManager) Ejb3Factory.getInstance()
			.getFacadeRemote("BaseDataManagerImpl");  
		//	HrJEmpInfoFacadeRemote  empInfoRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
			// 根据部门Id查询人员
		//	PageObject poEmps = empInfoRemote.getSubWorkersByDeptId(lnNode, employee.getEnterpriseCode());
			PageObject poEmps =bll.getWorkersOnlyInDeptId(lnNode);
			if (poEmps != null) {
				List<Employee> lstEmps = poEmps.getList();
				if (lstEmps != null && lstEmps.size() > 0) {
					if (lstSubs == null) {
						lstSubs = new ArrayList<TreeNode>();
					}
					for (int intCnt = 0; intCnt < lstEmps.size(); intCnt++) {
						TreeNode empNode = new TreeNode();
						Employee emp = lstEmps.get(intCnt);

						// 设置人员ID EMP_ID
						empNode.setId(String.valueOf(emp.getEmpId()));
						// 人员姓名 CHS_NAME(EMP_CODE)
						empNode.setText(emp.getWorkerName()
								+ "(" + emp.getWorkerCode() + ")");
						// 人员编码 EMP_CODE
						empNode.setCode(emp.getWorkerCode());
						// 设置节点类型
						empNode.setCls("file");
						empNode.setLeaf(true);

						// 增加人员节点
						lstSubs.add(empNode);
					}
				}
			}

			// 如果查找的节点不为空
			if (lstSubs != null) {
				write(JSONUtil.serialize(lstSubs));
			} else {
				write("[]");
			}
			LogUtil.log("Action:查找其子部门或部门下的人员成功。", Level.INFO, null);
		} catch (Exception sqle) {
			LogUtil.log("Action:查找其子部门或部门下的人员失败。", Level.SEVERE, sqle);
			write("[]");
		}
	}

	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取动态drp类别
	 *
	 * @return strDrpType 动态drp类别
	 */
	public String getStrDrpType() {
		return strDrpType;
	}
	/**
	 * 设置动态drp类别
	 * @param strDrpType 动态drp类别
	 */
	public void setStrDrpType(String strDrpType) {
		this.strDrpType = strDrpType;
	}
	/**
	 * 获取附件id
	 * @return fileId 附件id
	 */
	public Long getFileId() {
		return fileId;
	}
	/**
	 * 设置附件id
	 * @param fileId 附件id
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取合同id
	 * @return workcontractid 合同id
	 */
	public Long getWorkcontractid() {
		return workcontractid;
	}
	/**
	 * 设置合同id
	 * @param workcontractid 合同id
	 */
	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
	}

	/**
	 * 获取附件来源
	 * @return fileOriger 附件来源
	 */
	public String getFileOriger() {
		return fileOriger;
	}

	/**
	 * 设置附件来源
	 * @param fileOriger 附件来源
	 */
	public void setFileOriger(String fileOriger) {
		this.fileOriger = fileOriger;
	}
	/**
	 * @return the fileUpload
	 */
	public File getFileUpload() {
		return fileUpload;
	}

	/**
	 * @param fileUpload the fileUpload to set
	 */
	public void setFileUpload(File fileUpload) {
		this.fileUpload = fileUpload;
	}

	/**
	 * @return the fileUploadFileName
	 */
	public String getFileUploadFileName() {
		return fileUploadFileName;
	}

	/**
	 * @param fileUploadFileName the fileUploadFileName to set
	 */
	public void setFileUploadFileName(String fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	/**
	 * @return the fileUploadContentType
	 */
	public String getFileUploadContentType() {
		return fileUploadContentType;
	}

	/**
	 * @param fileUploadContentType the fileUploadContentType to set
	 */
	public void setFileUploadContentType(String fileUploadContentType) {
		this.fileUploadContentType = fileUploadContentType;
	}

	/**
	 * 获取员工id
	 * @return empId 员工id
	 */
	public Long getEmpId() {
		return empId;
	}

	/**
	 * 设置员工id
	 * @param empId 员工id
	 */
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	
	/**
	 * add by liuyi 090914
	 * 获得业务共通drp的数据
	 *
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getCommDrpStoreDatas() throws JSONException{
		PageObject obj  = new PageObject();
		//**********modified by liuyi 091201 取消注释开始*******************
		// 籍贯
		if(Constants.DRP_TYPE_NATIVE_PLACE.equals(strDrpType)){
			// 籍贯remote
			HrCNativePlaceFacadeRemote remote = (HrCNativePlaceFacadeRemote) factory.getFacadeRemote(
					"HrCNativePlaceFacade");
			obj = remote.findAllNativePlace(employee.getEnterpriseCode());
		}// 称谓
		else if(Constants.DRP_TYPE_APPELLATION.equals(strDrpType)){
			// 称谓remote
			HrCAppellationFacadeRemote remote = (HrCAppellationFacadeRemote) factory.getFacadeRemote(
					"HrCAppellationFacade");
			obj = remote.findAllAppellations(employee.getEnterpriseCode());
		}// 学位
		else if(Constants.DRP_TYPE_DEGREE.equals(strDrpType)){
			// 学位
			HrCDegreeFacadeRemote remote = (HrCDegreeFacadeRemote) factory.getFacadeRemote(
				"HrCDegreeFacade");
			obj = remote.findAllDegrees(employee.getEnterpriseCode());
		}
//		 学历
		else if(Constants.DRP_TYPE_EDUCATION.equals(strDrpType)){
			// 学历
			HrCEducationFacadeRemote remote = (HrCEducationFacadeRemote) factory.getFacadeRemote(
				"HrCEducationFacade");
			obj = remote.findAllEducations(employee.getEnterpriseCode());
		}
//		 员工类别
		else if(Constants.DRP_TYPE_EMP_TYPE.equals(strDrpType)){
			// 员工类别
			HrCEmpTypeFacadeRemote remote = (HrCEmpTypeFacadeRemote) factory.getFacadeRemote(
				"HrCEmpTypeFacade");
			obj = remote.findAllEmpTypes(employee.getEnterpriseCode());
		}
//		 语种
		else if(Constants.DRP_TYPE_LANGUAGE.equals(strDrpType)){
			// 语种
			HrCLanguageFacadeRemote remote = (HrCLanguageFacadeRemote) factory.getFacadeRemote(
				"HrCLanguageFacade");
			obj = remote.findAllLanguages(employee.getEnterpriseCode());
		}
//		 劳保工种
		else if(Constants.DRP_TYPE_LBGZBM.equals(strDrpType)){
			// 劳保工种
			HrCLbgzbmFacadeRemote remote = (HrCLbgzbmFacadeRemote) factory.getFacadeRemote(
				"HrCLbgzbmFacade");
			obj = remote.findAllLbgzbms(employee.getEnterpriseCode());
		}
		//**********modified by liuyi 091201 取消注释结束*******************
		// 调动部门
		else if(Constants.DRP_TYPE_MOVE_DEPT.equals(strDrpType)){
// 		if(Constants.DRP_TYPE_MOVE_DEPT.equals(strDrpType)){
			// 调动部门 
			HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) factory.getFacadeRemote(
				"HrCDeptFacade");
			obj = remote.findAllBanzuDepts(employee.getEnterpriseCode());
		}
 		//**********modified by liuyi 091201 取消注释开始*******************
		// 民族
		else if(Constants.DRP_TYPE_NATION.equals(strDrpType)){
			// 民族
			HrCNationFacadeRemote remote = (HrCNationFacadeRemote) factory.getFacadeRemote(
				"HrCNationFacade");
			obj = remote.findAllNations(employee.getEnterpriseCode());
		}
		// 政治面貌
		else if(Constants.DRP_TYPE_POLITICS.equals(strDrpType)){
			// 政治面貌
			HrCPoliticsFacadeRemote remote = (HrCPoliticsFacadeRemote) factory.getFacadeRemote(
				"HrCPoliticsFacade");
			obj = remote.findAllPolitics(employee.getEnterpriseCode());
		}
		// 学校
		else if(Constants.DRP_TYPE_SCHOOL.equals(strDrpType)){
			// 学校
			HrCSchoolFacadeRemote remote = (HrCSchoolFacadeRemote) factory.getFacadeRemote(
				"HrCSchoolFacade");
			obj = remote.findAllSchools(employee.getEnterpriseCode());
		}
		// 学习专业
		else if(Constants.DRP_TYPE_SPECIALTY.equals(strDrpType)){
			// 学习专业
			HrCSpecialtyFacadeRemote remote = (HrCSpecialtyFacadeRemote) factory.getFacadeRemote(
				"HrCSpecialtyFacade");
			obj = remote.findAllSpecialtys(employee.getEnterpriseCode());
		}
		// 学习类别
		else if(Constants.DRP_TYPE_STUDYTYPE.equals(strDrpType)){
			// 学习类别
			HrCStudytypeFacadeRemote remote = (HrCStudytypeFacadeRemote) factory.getFacadeRemote(
				"HrCStudytypeFacade");
			obj = remote.findAllStudytypes(employee.getEnterpriseCode());
		}
		// 技术等级
		else if(Constants.DRP_TYPE_TECHNOLOGY_GRADE.equals(strDrpType)){
			// 技术等级
			HrCTechnologyGradeFacadeRemote remote = (HrCTechnologyGradeFacadeRemote) factory.getFacadeRemote(
				"HrCTechnologyGradeFacade");
			obj = remote.findAllTechnologyGrades(employee.getEnterpriseCode());
		}
		// 技术职称
		else if(Constants.DRP_TYPE_TECHNOLOGY_TITLES.equals(strDrpType)){
			// 技术职称
			HrCTechnologyTitlesFacadeRemote remote = (HrCTechnologyTitlesFacadeRemote) factory.getFacadeRemote(
				"HrCTechnologyTitlesFacade");
			obj = remote.findAllTechnologyTitles(employee.getEnterpriseCode());
		}
		// 工种
		else if(Constants.DRP_TYPE_TYPE_OF_WORK.equals(strDrpType)){
			// 工种
			HrCTypeOfWorkFacadeRemote remote = (HrCTypeOfWorkFacadeRemote) factory.getFacadeRemote(
				"HrCTypeOfWorkFacade");
			obj = remote.findAllWorkTypes(employee.getEnterpriseCode());
		}
		// 员工身份
		else if(Constants.DRP_TYPE_WORKID.equals(strDrpType)){
			// 员工身份
			HrCWorkidFacadeRemote remote = (HrCWorkidFacadeRemote) factory.getFacadeRemote(
				"HrCWorkidFacade");
			obj = remote.findAllWorkIds(employee.getEnterpriseCode());
		}
		// 岗位级别
		else if(Constants.DRP_TYPE_STATION_LEVEL.equals(strDrpType)){
			HrCStationLevelFacadeRemote remote = (HrCStationLevelFacadeRemote) factory.getFacadeRemote(
					"HrCStationLevelFacade");
			obj = remote.findAllStationLevels(employee.getEnterpriseCode());
		}
		// 岗位类别
		else if(Constants.DRP_TYPE_STATION_TYPE.equals(strDrpType)){
			HrCStationTypeFacadeRemote remote = (HrCStationTypeFacadeRemote) factory.getFacadeRemote(
					"HrCStationTypeFacade");
			obj = remote.findAllStationTypes(employee.getEnterpriseCode());
		}
		// 进厂类别
		else if(Constants.DRP_TYPE_IN_TYPE.equals(strDrpType)){
			HrCIntypeFacadeRemote remote = (HrCIntypeFacadeRemote)factory.getFacadeRemote(
					"HrCIntypeFacade");
			obj = remote.findAllInTypes(employee.getEnterpriseCode());
		}
		//**********modified by liuyi 091201 取消注释结束*******************
		// 协作单位
		else if(Constants.DRP_TYPE_COOPERATE_UNIT.equals(strDrpType)){
			HrJCooperateunitFacadeRemote remote = (HrJCooperateunitFacadeRemote) factory.getFacadeRemote(
					"HrJCooperateunitFacade");
			obj = remote.findAllCooperateunits(employee.getEnterpriseCode());
		}
		// 离职类别
		else if(Constants.DRP_TYPE_OUT_TYPE.equals(strDrpType)){
			HrCOuttypeFacadeRemote remote = (HrCOuttypeFacadeRemote) factory.getFacadeRemote(
					"HrCOuttypeFacade");
			obj = remote.findAllOuttypes(employee.getEnterpriseCode());
		}

		// 追加空格
		if(obj.getList() != null && obj.getList().size() > 0){// modify by ywliu 2009/09/07
			DrpCommBeanInfo info = new DrpCommBeanInfo();
			info.setId(null);
			info.setText("");
			obj.getList().add(0, info);
		}
		write(JSONUtil.serialize(obj));

	}
	
	
	/**
	 * add by liuyi 091201
	 * 查找员工位置号维护显示顺序
	 */
	public void getEmpOrderbyList(){
		try{
			LogUtil.log("Action:查找员工位置号维护显示顺序开始。", Level.INFO, null);
			HrJEmpInfoFacadeRemote  empInfoRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
			PageObject obj = empInfoRemote.findEmpOrderby(this.getDeptId(),employee.getEnterpriseCode());
			write(JSONUtil.serialize(obj));
			LogUtil.log("Action:查找员工位置号维护显示顺序结束。", Level.INFO, null);
			}catch (JSONException jsone) {
				LogUtil.log("Action:查找员工位置号维护显示顺序失败。", Level.SEVERE, jsone);
				write(Constants.DATA_FAILURE);
			} catch (SQLException sqle) {
				LogUtil.log("Action:查找员工位置号维护显示顺序失败。", Level.SEVERE, sqle);
				write(Constants.SQL_FAILURE);
			}
	}
	/**
	 * add by liuyi 091201
	 * 保存员工位置号维护显示顺序
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveEmpOrderbyMaintenList() throws JSONException{
		try{
			LogUtil.log("Action:处理保存操作开始。", Level.INFO, null);
		String data = request.getParameter("data");
		@SuppressWarnings("unused")
		List<Map> dataMapList = (List<Map>) JSONUtil
		.deserialize(data);
		List<EmpOrderbyMaitenInfo> updateOrderByList = new ArrayList<EmpOrderbyMaitenInfo>(dataMapList.size());
		Map map;
		for(int i =0;i<dataMapList.size();i++){
			map  = dataMapList.get(i);
			EmpOrderbyMaitenInfo bean= new EmpOrderbyMaitenInfo();
			bean.setEmpId(Long.parseLong(map.get("empId").toString()));
			bean.setOrderBy(Long.parseLong(map.get("orderBy").toString()));
			updateOrderByList.add(bean);
		}
		HrJEmpInfoFacadeRemote  empInfoRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		// modified by liuyi 091201 
//		empInfoRemote.updateEmpOrderBy(employee.getWorkerCode(),updateOrderByList);
		empInfoRemote.updateEmpOrderBy(employee.getWorkerId(),updateOrderByList);
		// 获得最新的lastModifiyDate
		HrJEmpInfo empInfo = empInfoRemote.findByEmpId(empId, employee.getEnterpriseCode());
		String strMsg;
		if(empInfo == null) {
			strMsg = "{success:true,msg:''}";
		}else{
			strMsg = "{success:true,msg:'" + empInfo.getLastModifiyDate() + "'}";
		}
		write(strMsg);
		LogUtil.log("Action:处理保存操作结束。", Level.INFO, null);
		}catch (SQLException e) {
			LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		} catch (JSONException e) {
			LogUtil.log("Action:保存DB操作失败", Level.SEVERE, e);
			write(Constants.DATA_FAILURE);
		}
	}
	
	public void getEmpListByFilter() throws JSONException
	{
		HrJEmpInfoFacadeRemote  empInfoRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
		String fuzzy = request.getParameter("fuzzy");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = empInfoRemote.getEmpListByFilter(fuzzy,employee.getEnterpriseCode()
					,Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = empInfoRemote.getEmpListByFilter(fuzzy,employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
}
