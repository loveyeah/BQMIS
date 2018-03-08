/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.ca.attendance.attendancerights.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.AttendanceStandardFacadeRemote;
import power.ejb.hr.ca.AttendancedepFollowInfo;
import power.ejb.hr.ca.HrCAttendancedep;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.ca.HrJAttendanceApprove;
import power.ejb.hr.ca.HrJAttendanceApproveFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

/**
 * 考勤权限设置
 * @author chen shoujiang
 *  
 */
public class AttendanceRightsAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	/** 默认根节点id*/
	private static final String ROOT_ID = "0";
	/** 空的树节点信息*/
	private static final String BLANK_NODE = "[]";
	/**标识通过考勤部门查询*/
	private static final String IS_ATTENDANCE_DEPT_FLAG = "f";
	/** 叶子节点 */
	private static final String IS_LEAF_Y = "1";
	/** 非叶子节点*/
	private static final String IS_LEAF_N = "0";
	/** 分隔符逗号*/
	private static final String STR_COMMMA = ",";
	 /** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
 	/**考勤标准维护(自定义)Remote*/
	private AttendanceStandardFacadeRemote attendanceStandardFacadeRemote;
	/**人员基本信息remote*/
	private HrJEmpInfoFacadeRemote hrjEmpInfoFacadeRemote;
	/**考勤部门维护remote*/
	private HrCAttendancedepFacadeRemote hrcAttendancedepFacadeRemote;
	/**上级部门ID */
	private String node;
	/**考勤部门表主键*/
	private String id;
	/** 部门id*/
	private String deptId;
	/**是否是叶子节点 */
	private String isLeaf;
	/**考勤部门名称*/
	private String attendanceDeptName;
	/**考勤类别*/
	private String attendDepType;
	/**代考勤部门id*/
	private String replaceDepId;
	/**上级审核部门ID*/
	private String topCheckDepId;
	/**考勤登记人*/
	private String attendWriterId;
	/**考勤审核人*/
	private String attendCheckerId;
	/**人员id集*/
	private String empIds;
	/**上次修改时间*/
	private String lastModifyDate;
	
	/** 是否考勤部门id */
	private String isAttendanceDeptFlag;
	/**
	 * 构造函数
	 */
	public AttendanceRightsAction() {
		// 考勤标准维护(自定义)Remote
		attendanceStandardFacadeRemote = (AttendanceStandardFacadeRemote) factory
			.getFacadeRemote("AttendanceStandardFacade");
		// 人员基本信息remote
		hrjEmpInfoFacadeRemote = (HrJEmpInfoFacadeRemote) factory
			.getFacadeRemote("HrJEmpInfoFacade");
		// 考勤部门维护remote
		hrcAttendancedepFacadeRemote = (HrCAttendancedepFacadeRemote) factory
			.getFacadeRemote("HrCAttendancedepFacade");
	}

	/**
	 * 取得考勤子部门
	 */
	public void getAttendanceDeptsByTid()
	{
		try {
			String pid = request.getParameter("node");
			if (pid == null || Constants.BLANK_STRING.equals(pid)) {
				pid = ROOT_ID;
			}
			List<TreeNode> list = hrcAttendancedepFacadeRemote.getDeptsByTopDeptid(Long.parseLong(node),employee.getEnterpriseCode());
			if (list != null) {
				super.write(JSONUtil.serialize(list));
			} else {
				super.write(BLANK_NODE);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			super.write(BLANK_NODE);
		}
	}
	
	
	
	
	/**
	 * 查询考勤部门维护信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendanceDeptData() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询考勤部门维护信息开始", Level.INFO, null);
			// 如果节点为空或者“”就设置为-1
			if (node == null || Constants.BLANK_STRING.equals(node)) {
				node = ROOT_ID;
			}
			// 查询考勤部门维护信息
			List<TreeNode> list = hrcAttendancedepFacadeRemote
					.getDeptsByTopDeptid(Long.parseLong(node), employee.getEnterpriseCode());
			// 如果查询到的数据非空的话，就返回页面
			if (list != null && list.size() > 0) {
				//LogUtil.log("Action:查询考勤部门维护信息结束", Level.INFO, null);
				super.write(JSONUtil.serialize(list));
			} else {
				//无需将部门表的数据插入到考勤部门表
//				// 将部门设置表里的数据插入到考勤部门维护表
//				attendanceStandardFacadeRemote.moveDeptData(employee.getWorkerCode(), employee.getEnterpriseCode(), "2");
//				// 查询考勤部门维护信息
//				List<TreeNode> listTree = hrcAttendancedepFacadeRemote
//						.getDeptsByTopDeptid(Long.parseLong(node),
//								employee.getEnterpriseCode());
//				// 如果查询到的数据非空的话，就返回页面
//				if (listTree != null) {
//					LogUtil.log(" Action:查询考勤部门维护信息结束", Level.INFO, null);
//					super.write(JSONUtil.serialize(listTree));
//				} else {
//					LogUtil.log(" Action:查询考勤部门维护信息结束", Level.INFO, null);
//					// 否则设为空值
//					super.write(BLANK_NODE);
//				}
//				LogUtil.log("Action:查询考勤部门维护信息结束", Level.INFO, null);
				super.write(BLANK_NODE);
			}
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            super.write(BLANK_NODE);
			LogUtil.log("Action:查询查询考勤部门维护信息失败", Level.SEVERE, null);
		} 
	}
	
	/**
	 * 查询考勤部门维护信息
	 * for 审批页面考勤部门下来列表
	 * modify by fyyang 20100803 部门考勤员汇总及部门主任审批需要加一级部门过滤
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendDeptDataByPerson() throws JSONException, Exception {
		try{
			String strMonth=request.getParameter("strMonth");
			
			HrJAttendanceApproveFacadeRemote approveRemote=(HrJAttendanceApproveFacadeRemote)factory.getFacadeRemote("HrJAttendanceApproveFacade");
			  WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		  		String entryIds = workflowService.getAvailableWorkflow(new String[] {
		  				"bqWorkAttendance"}, employee.getWorkerCode());
				
				List<HrJAttendanceApprove> approvelist=approveRemote.getAttendanceListForApprove(strMonth, entryIds, employee.getDeptId());
				String status="";
				if(approvelist!=null&&approvelist.size()>0)
				{
					status=approvelist.get(0).getSendState();
				}
			
			// 如果节点为空或者“”就设置为-1
			if (node == null || Constants.BLANK_STRING.equals(node)) {
				node = ROOT_ID;
			}
			// 查询考勤部门维护信息
			List<TreeNode> list=new ArrayList<TreeNode>();
			if(status.equals("")) list=null;
			else
			{
				
				Long empId;
			  if(status.equals("1")||status.equals("2"))
			  {
				   empId=employee.getWorkerId();
				   
			  }
			  else
			  {
				  empId=null;
			  }
			
			  list = hrcAttendancedepFacadeRemote//modify by wpzhu 20100714
			.getDeptsByLogPeople(Long.parseLong(node),empId, employee.getEnterpriseCode());

			}
			// 如果查询到的数据非空的话，就返回页面
			if (list != null && list.size() > 0) {
				
				super.write(JSONUtil.serialize(list));
			} else {
				super.write(BLANK_NODE);
			}
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
            super.write(BLANK_NODE);
			
		} 
	}

	/**
	 * 查询考勤员工信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendanceEmpInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询考勤员工信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 无分页信息时执行
			// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改begin
            boolean isAttendanceDept = true;
            // 通过所属部门检索
			if(IS_ATTENDANCE_DEPT_FLAG.equals(isAttendanceDeptFlag)){
				isAttendanceDept = false;
			}
			if(deptId != null && !deptId.equals(Constants.BLANK_STRING)) {
				obj = hrjEmpInfoFacadeRemote.getEmpFollowInfoByDeptId(
						Long.parseLong(deptId), isAttendanceDept, employee.getEnterpriseCode());
			}
			// 2009-3-16 郑智鹏  UT-BUG-KQ003-004修改end
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj != null && obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询考勤员工信息结束", Level.SEVERE, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询考勤员工信息失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 查询某个考勤部门信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getAttendanceDeptSingleInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询某个考勤部门信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// 无分页信息时执行
			if(deptId != null && !deptId.equals(Constants.BLANK_STRING)) {
				obj = hrcAttendancedepFacadeRemote.getDeptsByAttendanceDeptid(deptId, employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj != null && obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询某个考勤部门信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询某个考勤部门信息失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 保存考勤部门
	 * @throws DataFormatException
	 */
	public void addAttendanceDeptInfo() {
// add by sychen 20100716
		HrCAttendancedep entity = new HrCAttendancedep();
		 boolean existFlag=false;
			existFlag=hrcAttendancedepFacadeRemote.checkAttendanceDeptName(attendanceDeptName,id,employee.getEnterpriseCode());
			if(existFlag==true){
				write("{success:true,existFlag:"+existFlag+"}");
			}
			else{
// add by sychen 20100716 end
				
		LogUtil.log("Action:保存考勤部门开始", Level.INFO, null);
		try{
			// 创建一个
//			HrCAttendancedep entity = new HrCAttendancedep();
			// 考勤部门名称
			if(checkNull(attendanceDeptName))
				entity.setAttendanceDeptName(attendanceDeptName);
			// 考勤类别
			// update by sychen 20100720
			entity.setAttendDepType("1");
//			if(checkNull(attendDepType))
//				entity.setAttendDepType(attendDepType);
			// 上级审核部门
			if(checkNull(topCheckDepId))
				entity.setTopCheckDepId(Long.parseLong(topCheckDepId));
			// 代考勤部门
//			if(checkNull(replaceDepId))
//				entity.setReplaceDepId(Long.parseLong(replaceDepId));
			// 考勤登记人
			if(checkNull(attendWriterId))
				entity.setAttendWriterId(Long.parseLong(attendWriterId));
			// 考勤审核人
//			if(checkNull(attendCheckerId))
//				entity.setAttendCheckerId(Long.parseLong(attendCheckerId));
			// update by sychen 20100720 end 
			
			// 设置企业编码
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			// 设置最后修改人
			entity.setLastModifiyBy(employee.getWorkerCode());
			// 设置是否使用标识
			entity.setIsUse(Constants.IS_USE_Y);
			// 执行保存操作
			hrcAttendancedepFacadeRemote.save(entity);
			write(Constants.ADD_SUCCESS);
			LogUtil.log("Action:保存考勤部门结束", Level.INFO, null); 
		} catch (SQLException jsone) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:保存考勤部门失败", Level.SEVERE, null);
		}
	}
	}
	
	/**
	 *修改考勤部门信息
	 * @throws DataFormatException 
	 * @throws DataFormatException
	 */
	public void modifyAttendanceDeptInfo() throws DataFormatException {
		LogUtil.log("Action:修改考勤部门信息开始", Level.INFO, null);
		try{
			// 创建一个
			HrCAttendancedep entity = new HrCAttendancedep();
			// 通过id查找bean信息
			entity = hrcAttendancedepFacadeRemote.findById(Long.parseLong(id));
			if(lastModifyDate != null && !Constants.BLANK_STRING.equals(lastModifyDate)) {
			if(!(entity.getLastModifiyDate().toString().substring(0, 19)).equals(lastModifyDate)){
					write(Constants.DATA_USING);
				}
			}
			// 考勤类别
			if(checkNull(attendDepType))
				entity.setAttendDepType(attendDepType);
			// 代考勤部门
			if(checkNull(replaceDepId))
				entity.setReplaceDepId(Long.parseLong(replaceDepId));
			else 
				entity.setReplaceDepId(null);
			// 考勤登记人
			if(checkNull(attendWriterId))
				entity.setAttendWriterId(Long.parseLong(attendWriterId));
			else
				entity.setAttendWriterId(null);
			// 考勤审核人
			if(checkNull(attendCheckerId))
				entity.setAttendCheckerId(Long.parseLong(attendCheckerId));
			else 
				entity.setAttendCheckerId(null);
			// 设置最后修改人
			entity.setLastModifiyBy(employee.getWorkerCode());
			// 执行保存操作
			hrcAttendancedepFacadeRemote.update(entity);
			write(Constants.MODIFY_SUCCESS);
			LogUtil.log("Action:修改考勤部门信息结束", Level.INFO, null); 
		} catch (SQLException jsone) {
			write(Constants.SQL_FAILURE);
			LogUtil.log("Action:修改考勤部门信息失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 删除考勤部门信息
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteAttendanceDeptInfo() throws JSONException, Exception {
		LogUtil.log("Action: 删除考勤部门信息开始", Level.INFO, null);
		try{
			// 创建一个
			HrCAttendancedep entity = new HrCAttendancedep();
			List<HrCAttendancedep> list = new ArrayList<HrCAttendancedep>();
			
			if(null != isLeaf && !Constants.BLANK_STRING.equals(isLeaf)) {
				if(IS_LEAF_Y.equals(isLeaf)) {
					// 如果id非空的话
					if(null != deptId && !Constants.BLANK_STRING.equals(deptId))
					{
						PageObject obj = hrcAttendancedepFacadeRemote.getDeptsByAttendanceDeptid(deptId, 
								employee.getEnterpriseCode());
						if(obj != null && obj.getList() != null){
							AttendancedepFollowInfo info = (AttendancedepFollowInfo) obj.getList().get(0);
							// 找到对应的信息
							entity = hrcAttendancedepFacadeRemote.findById(Long.parseLong(info.getId()));
						}
						// 上次修改人
						entity.setLastModifiyBy(employee.getWorkerCode());
						entity.setIsUse(Constants.IS_USE_N);
						hrcAttendancedepFacadeRemote.delete(entity);
					}
				}else if(IS_LEAF_N.equals(isLeaf)) {
					list = hrcAttendancedepFacadeRemote.findAllChildrenDept(deptId,employee.getEnterpriseCode());
					if(null != list && list.size() > 0) {
						for(int i = 0; i < list.size(); i++) {
							HrCAttendancedep bean = new HrCAttendancedep();
							// 通过id找
							bean = hrcAttendancedepFacadeRemote.findById(list.get(i).getId());
							// 上次修改人
							bean.setLastModifiyBy(employee.getWorkerCode());
							bean.setIsUse(Constants.IS_USE_N);
							hrcAttendancedepFacadeRemote.delete(bean);
						}
					}
				}
			}
			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action: 删除考勤部门信息结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action: 删除考勤部门信息失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 *修改人员所属部门信息
	 * @throws DataChangeException 
	 * @throws DataFormatException
	 */
	public void modifyDeptByEmpId() throws DataChangeException {
		LogUtil.log("Action:修改人员所属部门信息开始", Level.INFO, null);
		// 获取所有的人员id
		String[] ids = empIds.split(STR_COMMMA);
		for(int i = 0 ; i < ids.length; i++) {
			// 创建一个
			HrJEmpInfo entity = new HrJEmpInfo();
			// 通过id查找bean信息
			entity = hrjEmpInfoFacadeRemote.findById(Long.parseLong(ids[i]));
			if(checkNull(deptId))
					entity.setAttendanceDeptId(Long.parseLong(deptId));
				;
			// 设置最后修改人
			// modify by liuyi 090915 数据库存id ,而非编码
//				entity.setLastModifiyBy(employee.getWorkerCode());
			entity.setLastModifiyBy(employee.getWorkerId());
			hrjEmpInfoFacadeRemote.update(entity);
		}
		write(Constants.MODIFY_SUCCESS);
		LogUtil.log("Action:修改人员所属部门信息结束", Level.INFO, null);
	}
	
	/**
	 * check字符串是否为空或者null
	 */
	private Boolean checkNull(String chaStr) {
		if(chaStr != null && !chaStr.equals(Constants.BLANK_STRING))
			return true;
		else 
			return false;
	}
	
	//考勤部门管理<考勤人员变更>删除考勤部门人员方法
    // add by sychen 20100713
	public void deleteAttendanceDeptId() {
		String ids = request.getParameter("ids");
		hrcAttendancedepFacadeRemote.deleteAttendanceDeptId(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}

	/**
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * @return the isLeaf
	 */
	public String getIsLeaf() {
		return isLeaf;
	}

	/**
	 * @param isLeaf the isLeaf to set
	 */
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}

	/**
	 * @return the attendanceDeptName
	 */
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}

	/**
	 * @param attendanceDeptName the attendanceDeptName to set
	 */
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}

	/**
	 * @return the attendDepType
	 */
	public String getAttendDepType() {
		return attendDepType;
	}

	/**
	 * @param attendDepType the attendDepType to set
	 */
	public void setAttendDepType(String attendDepType) {
		this.attendDepType = attendDepType;
	}

	/**
	 * @return the replaceDepId
	 */
	public String getReplaceDepId() {
		return replaceDepId;
	}

	/**
	 * @param replaceDepId the replaceDepId to set
	 */
	public void setReplaceDepId(String replaceDepId) {
		this.replaceDepId = replaceDepId;
	}

	/**
	 * @return the topCheckDepId
	 */
	public String getTopCheckDepId() {
		return topCheckDepId;
	}

	/**
	 * @param topCheckDepId the topCheckDepId to set
	 */
	public void setTopCheckDepId(String topCheckDepId) {
		this.topCheckDepId = topCheckDepId;
	}

	/**
	 * @return the attendWriterId
	 */
	public String getAttendWriterId() {
		return attendWriterId;
	}

	/**
	 * @param attendWriterId the attendWriterId to set
	 */
	public void setAttendWriterId(String attendWriterId) {
		this.attendWriterId = attendWriterId;
	}

	/**
	 * @return the attendCheckerId
	 */
	public String getAttendCheckerId() {
		return attendCheckerId;
	}

	/**
	 * @param attendCheckerId the attendCheckerId to set
	 */
	public void setAttendCheckerId(String attendCheckerId) {
		this.attendCheckerId = attendCheckerId;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the empIds
	 */
	public String getEmpIds() {
		return empIds;
	}

	/**
	 * @param empIds the empIds to set
	 */
	public void setEmpIds(String empIds) {
		this.empIds = empIds;
	}

	/**
	 * @return the isAttendanceDept
	 */
	public String getIsAttendanceDeptFlag() {
		return isAttendanceDeptFlag;
	}

	/**
	 * @param isAttendanceDept the isAttendanceDept to set
	 */
	public void setIsAttendanceDeptFlag(String isAttendanceDeptFlag) {
		this.isAttendanceDeptFlag = isAttendanceDeptFlag;
	}

	/**
	 * @return the lastModifyDate
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}

	/**
	 * @param lastModifyDate the lastModifyDate to set
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	
}
