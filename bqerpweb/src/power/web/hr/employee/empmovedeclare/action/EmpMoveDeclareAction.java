/**
 * Copyright ustcsoft.com
 * All right reserved.
 */

package power.web.hr.employee.empmovedeclare.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.EmpMoveDeclareFacadeRemote;
import power.ejb.hr.HrCStationLevelFacadeRemote;
import power.ejb.hr.HrCStationmovetypeFacadeRemote;
import power.ejb.hr.HrJDepstationcorrespondFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJSalayradjust;
import power.ejb.hr.HrJSalayradjustFacadeRemote;
import power.ejb.hr.HrJStationremove;
import power.ejb.hr.HrJStationremoveFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工调动单申报
 * @author chen shoujiang
 *  
 */
public class EmpMoveDeclareAction extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private static final String DATE_FORMAT ="yyyy-MM-dd";
	/** 更新标识 */
	private static final String UPDATA_FLAG = "1";
	/** 保存标识 */
	private static final String SAVE_FLAG = "2";
	/** 岗位调动类别 */
	private static final String STATION_MOVE_TYPE_1 = "1";
	/**状态 */
	private static final String DCMSTATE_0 = "0"; 
	/** 查询结果为null,传到前台的string */
    private static String STR_JSON_NULL = "{\"list\":[],\"totalCount\":0}";
    /**保存数据成功返回前台的String*/
    private static String STR_SAVE_SUCCESS = "{success:true,msg:'success'}";
    /** 上报成功返回的值*/
    private static String STR_REPORT_SUCCESS = "{success:true,msg:'&nbsp&nbsp&nbsp上报成功。&nbsp&nbsp&nbspp'}";
    /** 人员信息表remote add by liuyi 20100507  */
    private HrJEmpInfoFacadeRemote hrJEmpInfoFacadeRemote;
	/** 员工调动单申报remote**/
	private EmpMoveDeclareFacadeRemote empMoveDeclareFacadeRemote;
	/**岗位调动单remote*/
	private HrJStationremoveFacadeRemote hrJStationremoveFacadeRemote;
	/** 岗位调动类别维护remote */
	private HrCStationmovetypeFacadeRemote hrCStationmovetypeFacadeRemote;
	/**薪酬变动登记 */
	private HrJSalayradjustFacadeRemote hrJSalayradjustFacade;
	/**部门岗位对应表 */
	private HrJDepstationcorrespondFacadeRemote hrJDepstationcorrespondFacadeRemote;
	/**岗位级别设置表 */
	private HrCStationLevelFacadeRemote hrCStationLevelFacadeRemote;
	/**调动开始日期*/
	private String startDate;
	/**调动结束日期*/
	private String endDate;
	/**调动前部门*/
	private String beforeDeptCode;
	/**调动后部门*/
	private String afterDeptCode;
	/**单据状态*/
	private String dcmStatus;
	/**标示初始状态*/
	private String flag;
	/**人员编码 */
	private String empCode;
	/**人员id */
	private String empId;
	/** 岗位调动单ID */
	private String stationRemoveId;
	/** 调动通知单号*/
	private String requisitionNo;
	/** 部门ID*/
	private String deptId;
	/** 岗位id */
	private String stationId;
	private String bfStationId;
	/** 岗位级别*/
	private String staionLevel;
	private String bfStaionLevel;
	/**调动类别*/
	private String stationType;
	/**调动日期*/
	private String removeDate;
	/** 起薪日期*/
	private String doDate2;
	/** 原因 */
	private String reason;
	/** 备注*/
	private String memo;
	
	/**变动类别 */
	private String adjustType;
	/**岗薪变化类别 */
	private String stationChangeType;
	/**变更前执行岗级*/
	private String oldCheckStationGrade;
	/**变更后执行岗级*/
	private String newCheckStationGrade;
	private String oldStationGrade;
	private String newStationGrade;
	private String oldSalaryGrade;
	private String newSalaryGrade;
	private String doDate;
	private String subReason;
	private String momo;
	private String lastModifiedDate;
	private String lastModifiedDateSub;
	private String start;
	private String limit;
	
	private String oldBanzu;//add by sychen 20100721
	private String newBanzu;//add by sychen 20100721
	private String printDate;//add by sychen 20100723
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	/**
	 * 构造函数
	 */
	public EmpMoveDeclareAction() {
		empMoveDeclareFacadeRemote = (EmpMoveDeclareFacadeRemote) factory
				.getFacadeRemote("EmpMoveDeclareFacade");
		hrJStationremoveFacadeRemote = (HrJStationremoveFacadeRemote) factory
		.getFacadeRemote("HrJStationremoveFacade");
		hrCStationmovetypeFacadeRemote = (HrCStationmovetypeFacadeRemote) factory
		.getFacadeRemote("HrCStationmovetypeFacade");
		hrJSalayradjustFacade = (HrJSalayradjustFacadeRemote) factory
		.getFacadeRemote("HrJSalayradjustFacade");
		/**部门岗位对应表 */
		hrJDepstationcorrespondFacadeRemote = (HrJDepstationcorrespondFacadeRemote) factory
		.getFacadeRemote("HrJDepstationcorrespondFacade");
		/**岗位级别设置表 */
		hrCStationLevelFacadeRemote = (HrCStationLevelFacadeRemote) factory
		.getFacadeRemote("HrCStationLevelFacade");
		 /** 人员信息表remote add by liuyi 20100507  */
		hrJEmpInfoFacadeRemote = (HrJEmpInfoFacadeRemote)factory.getFacadeRemote("HrJEmpInfoFacade");
	}
	
	/**
	 * 查询员工调动单申报信息
	 * @throws JSONException
	 * @throws Exception
	 */
	public void getEmpMoveDeclareInfo() throws JSONException, Exception {
		try{
			LogUtil.log("Action:查询员工调动单申报信息开始", Level.INFO, null);
			// 分页信息
			PageObject obj = new PageObject();
			// add by liuyi 20100507 查询类型
			String moveType = request.getParameter("moveType");
			// 调动通知单编号
			String queryNo = request.getParameter("queryNo");
			// 登记时间
			String insertDate = request.getParameter("insertDate");
			// 姓名
			String queryText = request.getParameter("queryText");
			
	        String deptFlag = request.getParameter("deptFlag");//add by sychen 20100716 用于查询部门下所有子部门数据的判断
			
			// 如果非空
			if (start != null && limit != null && !(Constants.BLANK_STRING).equals(start) && !Constants.BLANK_STRING.equals(limit)) {

				// 有分页信息时执行
				obj = empMoveDeclareFacadeRemote.getEmpMoveDeclareInfo(deptFlag,queryText,insertDate,queryNo,moveType,flag,startDate, endDate, beforeDeptCode, afterDeptCode, dcmStatus, 
						employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			}else {
				// 无分页信息时执行
				obj = empMoveDeclareFacadeRemote.getEmpMoveDeclareInfo(flag,queryText,insertDate,queryNo,moveType,flag,startDate, endDate, beforeDeptCode, afterDeptCode, dcmStatus, 
						employee.getEnterpriseCode());
			}
			// 输出
			String strOutput = Constants.BLANK_STRING;
			//　要是查询结果不为空的话，就赋值
			if(obj.getList() != null && obj.getList().size() > 0) {
				strOutput = JSONUtil.serialize(obj);
			} else {
				// 否则设为空值
				strOutput = STR_JSON_NULL;
			}
			write(strOutput);
			LogUtil.log("Action:查询员工调动单申报信息结束", Level.INFO, null);
		} catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询员工调动单申报信息失败", Level.INFO, null);
		} catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询员工调动单申报信息失败", Level.INFO, null);
            throw sqle;
		}
	}
	
	/**
	 * 调动类别初始化
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getStationMoveTypeList() throws JSONException {
		try{
			LogUtil.log("Action:查询调动类别CboList开始", Level.INFO, null);
			PageObject obj = hrCStationmovetypeFacadeRemote.getStationRemove(employee.getEnterpriseCode());
			String str = JSONUtil.serialize(obj);
			LogUtil.log("Action:查询调动类别CboList结束", Level.INFO, null);
			write(str);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询调动类别CboList失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:查询调动类别CboList失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 部门岗位联动查询
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void linkDeptStation() throws JSONException {
		try{
			LogUtil.log("Action:部门岗位联动查询开始", Level.INFO, null);
			PageObject obj = hrJDepstationcorrespondFacadeRemote.findByDeptId(deptId,employee.getEnterpriseCode());
			String str = JSONUtil.serialize(obj);
			write(str);
			LogUtil.log("Action:部门岗位联动查询结束", Level.INFO, null);
		}catch(JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:部门岗位联动查询失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:部门岗位联动查询失败", Level.SEVERE, null);
		}
	}
	
	/**
	 * 班组联动查询  add by sychen 20100721
	 * @throws JSONException
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public void getBeforeBanZuList() throws JSONException, SQLException {
			PageObject obj = hrJDepstationcorrespondFacadeRemote.getBeforeBanZuList(deptId,employee.getEnterpriseCode());
			String str = JSONUtil.serialize(obj);
			write(str);
	}
	
	/**
	 * 岗位级别联动查询
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void linkStationLevel() throws JSONException {
		try{
			LogUtil.log("Action:岗位级别联动查询开始", Level.INFO, null);
			PageObject obj = hrCStationLevelFacadeRemote.findByStationId(stationId, employee.getEnterpriseCode());
			String str = JSONUtil.serialize(obj);
			LogUtil.log("Action:岗位级别联动查询结束", Level.INFO, null);
			write(str);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:岗位级别联动查询失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:岗位级别联动查询失败", Level.SEVERE, null);
		}
	}
	
	
	/**
	 * 获取部门岗位级别信息
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getDeptStationLevelInfo() throws JSONException {
		try{
			LogUtil.log("Action:获取部门岗位级别信息开始", Level.INFO, null);
			PageObject obj = empMoveDeclareFacadeRemote.getDeptStationLevel(empId,employee.getEnterpriseCode());
			// 输出
			String strOutput = Constants.BLANK_STRING;
				strOutput = JSONUtil.serialize(obj);
			write(strOutput);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action:获取部门岗位级别信息失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action:获取部门岗位级别信息失败", Level.SEVERE, null);
		}
	}	
	
	/**
	 * 根据人员id查找酬变动记录信息
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getSalaryAdjustInfo() throws JSONException {
		try{
			LogUtil.log("Action: 根据id查找酬变动记录信息开始", Level.INFO, null);
			PageObject obj = hrJSalayradjustFacade.getSalayAdjustByRemoveId(stationRemoveId,employee.getEnterpriseCode());
			// 输出
			String strOutput = Constants.BLANK_STRING;
				strOutput = JSONUtil.serialize(obj);
			write(strOutput);
		}catch (JSONException jsone) {
            write(Constants.DATA_FAILURE);
			LogUtil.log("Action: 根据id查找酬变动记录信息失败", Level.SEVERE, null);
		}catch (SQLException sqle) {
            write(Constants.SQL_FAILURE);
            LogUtil.log("Action: 根据id查找酬变动记录信息失败", Level.SEVERE, null);
		}
	}	
	
	/**
	 * 删除岗位调动单记录
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteEmpMoveDeclareInfo() throws JSONException, Exception {
		LogUtil.log("Action:删除岗位调动单记录开始", Level.INFO, null);
		try{
			empMoveDeclareFacadeRemote.delete(stationRemoveId, employee.getWorkerCode(),
					employee.getEnterpriseCode(), lastModifiedDate);
				write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:删除岗位调动单记录结束", Level.INFO, null);
		}catch (DataChangeException e){
			 LogUtil.log("Action:上报岗位调动单记录失败", Level.SEVERE, null);
	            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:删除岗位调动单记录失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}

	/**
	 *上报岗位调动单记录
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void reportEmpMoveDeclareInfo() throws JSONException, Exception {
		LogUtil.log("Action:上报岗位调动单记录开始", Level.INFO, null);
		try{
			empMoveDeclareFacadeRemote.report(stationRemoveId, employee.getWorkerCode(), 
					employee.getEnterpriseCode(), lastModifiedDate);
			write(STR_REPORT_SUCCESS);
			LogUtil.log("Action:上报岗位调动单记录结束", Level.INFO, null);
		}catch (DataChangeException e){
			 LogUtil.log("Action:上报岗位调动单记录失败", Level.SEVERE, null);
	            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:上报岗位调动单记录失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * 更新岗位调动单
	 * @throws JSONException
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updateStationRemove() throws JSONException, Exception{
		LogUtil.log("Action:更新岗位调动单开始", Level.INFO, null);
		try{
			HrJStationremove entity = new HrJStationremove();
			HrJSalayradjust bean = new HrJSalayradjust();
			String bdeptId=request.getParameter("bdeptId");
			String bstationId=request.getParameter("bstationId");
			// 如果序号非空的话
			if(null != stationRemoveId && !Constants.BLANK_STRING.equals(stationRemoveId))
			{
				// 找到对应序号信息		
				entity = hrJStationremoveFacadeRemote.findById(Long.parseLong(stationRemoveId));
				entity.setRequisitionNo(requisitionNo);
				if(checkNull(deptId))
				entity.setNewDepId(Long.parseLong(deptId));
				//add by wpzhu   20100703-----
				if(checkNull(bdeptId))
				entity.setOldDepId(Long.parseLong(bdeptId));
				if(checkNull(bstationId))
				entity.setOldStationId(Long.parseLong(bstationId));
				if(stationType!=null&&!stationType.equals(""))
				{
					entity.setStationMoveTypeId(Long.parseLong(stationType));
				}
				if(checkNull(stationId))
				entity.setNewStationId(Long.parseLong(stationId));
				entity.setRemoveDate(formatStringToDate(removeDate,DATE_FORMAT));
				entity.setDoDate2(formatStringToDate(doDate2,DATE_FORMAT));
				// 如果数据非空，就设置值
				if(checkNull(reason))
					entity.setReason(reason);
				// 如果数据非空，就设置值
				if(checkNull(memo))
					entity.setMemo(memo);
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				
				// add by liuyi 20100617 
				// 如果数据非空，就设置值
				if(checkNull(oldStationGrade))
					entity.setOldStationGrade(Long.parseLong(oldStationGrade));
				entity.setNewStationGrade(Long.parseLong(newStationGrade));
				//add by sychen 20100721
				if(checkNull(oldBanzu))
				    entity.setOldBanzu(Long.parseLong(oldBanzu));
				if(checkNull(newBanzu))
				    entity.setNewBanzu(Long.parseLong(newBanzu));
				//add by sychen 20100721 end
				
			    entity.setPrintDate(formatStringToDate(printDate,DATE_FORMAT));//add by sychen 20100723
				
				// 如果数据非空，就设置值
				if(checkNull(oldSalaryGrade))	
					entity.setOldSalaryGrade(Long.parseLong(oldSalaryGrade));
				entity.setNewSalaryGrade(Long.parseLong(newSalaryGrade));
				String oldSalaryPoint = request.getParameter("oldSalaryPoint");
				String newSalaryPoint = request.getParameter("newSalaryPoint");
				if(checkNull(oldSalaryPoint))
					entity.setOldSalaryPoint(Double.parseDouble(oldSalaryPoint));
				if(checkNull(newSalaryPoint))
					entity.setNewSalaryPoint(Double.parseDouble(newSalaryPoint));
				
				// add by liuyi 20100507 修改人员基本信息表 
				HrJEmpInfo empInfo = hrJEmpInfoFacadeRemote.findById(entity.getEmpId());
				empInfo.setDeptId(entity.getNewDepId());
				empInfo.setStationId(entity.getNewStationId());
				
				// add by liuyi 20100507 执行岗级
//				empInfo.setCheckStationGrade(entity.getNewCheckStationGrade());
				// add by liuyi 20100507 标准岗级
//				empInfo.setStationGrade(entity.getNewStationGrade());
				empInfo.setCheckStationGrade(entity.getNewStationGrade());
				// add by liuyi 20100507 薪级
				empInfo.setSalaryLevel(entity.getNewSalaryGrade());
				
				// modified by liuyi 20100617 注释掉
//				if(newCheckStationGrade != null && !newCheckStationGrade.equals(Constants.BLANK_STRING)) {
//					PageObject obj = hrJSalayradjustFacade.getSalayAdjustByRemoveId(stationRemoveId,employee.getEnterpriseCode());
//					if(obj.getList() != null && obj.getList().size() > 0) {
//						List<HrJSalayradjust> list = obj.getList();
//						bean = list.get(0);
//						bean.setAdjustType(adjustType);
//						bean.setStationChangeType(stationChangeType);
//						// 如果数据非空，就设置值
//						if(checkNull(oldCheckStationGrade))
//							bean.setOldCheckStationGrade(Long.parseLong(oldCheckStationGrade));
//						bean.setNewCheckStationGrade(Long.parseLong(newCheckStationGrade));						
//						// 如果数据非空，就设置值
//						if(checkNull(oldStationGrade))
//							bean.setOldStationGrade(Long.parseLong(oldStationGrade));
//						bean.setNewStationGrade(Long.parseLong(newStationGrade));											
//						// 如果数据非空，就设置值
//						if(checkNull(oldSalaryGrade))
//							bean.setOldSalaryGrade(Long.parseLong(oldSalaryGrade));
//						bean.setNewSalaryGrade(Long.parseLong(newSalaryGrade));						
//						
//						bean.setDoDate(formatStringToDate(doDate,DATE_FORMAT));
//						// 如果数据非空，就设置值
//						if(checkNull(subReason))
//							bean.setReason(subReason);
//						// 如果数据非空，就设置值
//						if(checkNull(momo))
//							bean.setMemo(momo);
//						bean.setStationremoveid(Long.parseLong(stationRemoveId));
//						bean.setLastModifiedBy(employee.getWorkerCode());
//						empMoveDeclareFacadeRemote.update(entity, lastModifiedDate, bean, lastModifiedDateSub, UPDATA_FLAG);
//						
//						// add by liuyi 20100507 执行岗级
//						empInfo.setCheckStationGrade(bean.getNewCheckStationGrade());
//						// add by liuyi 20100507 标准岗级
//						empInfo.setStationGrade(bean.getNewStationGrade());
//						// add by liuyi 20100507 薪级
//						empInfo.setSalaryLevel(bean.getNewSalaryGrade());
//					}else {
//						bean.setStationremoveid(Long.parseLong(stationRemoveId));
//						bean.setEmpId(entity.getEmpId());
//						bean.setAdjustType(adjustType);
//						bean.setStationChangeType(stationChangeType);
//						// 如果数据非空，就设置值
//						if(checkNull(oldCheckStationGrade))
//							bean.setOldCheckStationGrade(Long.parseLong(oldCheckStationGrade));
//						bean.setNewCheckStationGrade(Long.parseLong(newCheckStationGrade));
//						// 如果数据非空，就设置值
//						if(checkNull(oldStationGrade))
//							bean.setOldStationGrade(Long.parseLong(oldStationGrade));
//						bean.setNewStationGrade(Long.parseLong(newStationGrade));
//						// 如果数据非空，就设置值
//						if(checkNull(oldSalaryGrade))
//							bean.setOldSalaryGrade(Long.parseLong(oldSalaryGrade));
//						bean.setNewSalaryGrade(Long.parseLong(newSalaryGrade));
//						bean.setDoDate(formatStringToDate(doDate,DATE_FORMAT));
//						// 如果数据非空，就设置值
//						if(checkNull(momo))
//							bean.setMemo(momo);
//						// 如果数据非空，就设置值
//						if(checkNull(subReason))
//							bean.setReason(subReason);
//						bean.setDcmState(null);
//						bean.setLastModifiedBy(employee.getWorkerCode());
//						bean.setEnterpriseCode(employee.getEnterpriseCode());
//						bean.setInsertby(employee.getWorkerCode());
//						empMoveDeclareFacadeRemote.update(entity, lastModifiedDate, bean,Constants.BLANK_STRING, SAVE_FLAG);
//						
//						// add by liuyi 20100507 执行岗级
//						empInfo.setCheckStationGrade(bean.getNewCheckStationGrade());
//						// add by liuyi 20100507 标准岗级
//						empInfo.setStationGrade(bean.getNewStationGrade());
//						// add by liuyi 20100507 薪级
//						empInfo.setSalaryLevel(bean.getNewSalaryGrade());
//					}
//				}else {
					empMoveDeclareFacadeRemote.update(entity, lastModifiedDate, bean, Constants.BLANK_STRING, SAVE_FLAG);
//				}
				
				// add by liuyi 20100507 
				hrJEmpInfoFacadeRemote.update(empInfo);
			}
			
			write( STR_SAVE_SUCCESS);
			LogUtil.log("Action:更新岗位调动单结束", Level.INFO, null);
		}catch (DataChangeException e){
			 LogUtil.log("Action:上报岗位调动单记录失败", Level.SEVERE, null);
	            write(Constants.DATA_USING);
		}catch (SQLException sqle) {
            LogUtil.log("Action:更新岗位调动单失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void addStationRemove() throws JSONException, Exception{
		LogUtil.log("Action:新增岗位调动单开始", Level.INFO, null);
		try{
			HrJStationremove entity = new HrJStationremove();
			HrJSalayradjust subBean = new HrJSalayradjust();
				entity.setEmpId(Long.parseLong(empId));
				entity.setRequisitionNo(requisitionNo);
				entity.setNewDepId(Long.parseLong(deptId));
				// 如果数据非空，就设置值
				if(checkNull(beforeDeptCode))
					entity.setOldDepId(Long.parseLong(beforeDeptCode));
				if(checkNull(stationId))
				entity.setNewStationId(Long.parseLong(stationId));
				// 如果数据非空，就设置值
				if(checkNull(bfStationId))
					entity.setOldStationId(Long.parseLong(bfStationId));
				// add by liuyi 20100507 修改调动类别 
				String moveTypeId = request.getParameter("moveTypeId");
				if(moveTypeId != null && !moveTypeId.equals("")){
					entity.setStationMoveTypeId(Long.parseLong(moveTypeId));
				}else
					entity.setStationMoveTypeId(Long.parseLong(STATION_MOVE_TYPE_1));
				entity.setDcmState(DCMSTATE_0);
				entity.setRemoveDate(formatStringToDate(removeDate,DATE_FORMAT));
				entity.setDoDate2(formatStringToDate(doDate2,DATE_FORMAT));
				// 如果数据非空，就设置值
				if(checkNull(reason))
					entity.setReason(reason);
				// 如果数据非空，就设置值
				if(checkNull(memo))
					entity.setMemo(memo);
				// 上次修改人
				entity.setLastModifiedBy(employee.getWorkerCode());
				entity.setEnterpriseCode(employee.getEnterpriseCode());
				entity.setInsertby(employee.getWorkerCode());
				
				// add by liuyi 20100617 
				// 如果数据非空，就设置值
				if(checkNull(oldStationGrade))
					entity.setOldStationGrade(Long.parseLong(oldStationGrade));
				entity.setNewStationGrade(Long.parseLong(newStationGrade));
				
				//add by sychen 20100721
				if(checkNull(oldBanzu))
				     entity.setOldBanzu(Long.parseLong(oldBanzu));
				if(checkNull(newBanzu))
				     entity.setNewBanzu(Long.parseLong(newBanzu));
				//add by sychen 20100721 end
				
			    entity.setPrintDate(formatStringToDate(printDate,DATE_FORMAT));//add by sychen 20100723
			    
				// 如果数据非空，就设置值
				if(checkNull(oldSalaryGrade))	
					entity.setOldSalaryGrade(Long.parseLong(oldSalaryGrade));
				entity.setNewSalaryGrade(Long.parseLong(newSalaryGrade));
				String oldSalaryPoint = request.getParameter("oldSalaryPoint");
				String newSalaryPoint = request.getParameter("newSalaryPoint");
				if(checkNull(oldSalaryPoint))
					entity.setOldSalaryPoint(Double.parseDouble(oldSalaryPoint));
				if(newSalaryPoint!=null&&!newSalaryPoint.equals(""))
				{
				entity.setNewSalaryPoint(Double.parseDouble(newSalaryPoint));
				}
				
				
				// add by liuyi 20100507 修改人员基本信息表 
				HrJEmpInfo empInfo = hrJEmpInfoFacadeRemote.findById(entity.getEmpId());
				empInfo.setDeptId(entity.getNewDepId());
				empInfo.setStationId(entity.getNewStationId());
				
				// add by liuyi 20100507 执行岗级
//				empInfo.setCheckStationGrade(entity.getNewCheckStationGrade());
				// add by liuyi 20100507 标准岗级
//				empInfo.setStationGrade(entity.getNewStationGrade());
				empInfo.setCheckStationGrade(entity.getNewStationGrade());
				// add by liuyi 20100507 薪级
				empInfo.setSalaryLevel(entity.getNewSalaryGrade());
				
				// modified by liuyi 20100617 注释掉
//				if(newCheckStationGrade != null && !newCheckStationGrade.equals(Constants.BLANK_STRING)) {
//					// 如果数据非空，就设置值
//					if(checkNull(adjustType))
//						subBean.setAdjustType(adjustType);
//					// 如果数据非空，就设置值
//					if(checkNull(stationChangeType))
//						subBean.setStationChangeType(stationChangeType);
//					// 如果数据非空，就设置值
//					if(checkNull(oldCheckStationGrade))
//						subBean.setOldCheckStationGrade(Long.parseLong(oldCheckStationGrade));
//					subBean.setNewCheckStationGrade(Long.parseLong(newCheckStationGrade));
//					// 如果数据非空，就设置值
//					if(checkNull(oldStationGrade))
//						subBean.setOldStationGrade(Long.parseLong(oldStationGrade));
//					subBean.setNewStationGrade(Long.parseLong(newStationGrade));
//					// 如果数据非空，就设置值
//					if(checkNull(oldSalaryGrade))	
//						subBean.setOldSalaryGrade(Long.parseLong(oldSalaryGrade));
//					subBean.setNewSalaryGrade(Long.parseLong(newSalaryGrade));
//					subBean.setDoDate(formatStringToDate(doDate,DATE_FORMAT));
//					// 如果数据非空，就设置值
//					if(checkNull(subReason))	
//						subBean.setReason(subReason);
//					// 如果数据非空，就设置值
//					if(checkNull(momo))
//						subBean.setMemo(momo);
//					subBean.setDcmState(null);
//					subBean.setLastModifiedBy(employee.getWorkerCode());
//					subBean.setInsertby(employee.getWorkerCode());
//					subBean.setEmpId(Long.parseLong(empId));
//					subBean.setEnterpriseCode(employee.getEnterpriseCode());
//					
//					// add by liuyi 20100507 执行岗级
//					empInfo.setCheckStationGrade(subBean.getNewCheckStationGrade());
//					// add by liuyi 20100507 标准岗级
//					empInfo.setStationGrade(subBean.getNewStationGrade());
//					// add by liuyi 20100507 薪级
//					empInfo.setSalaryLevel(subBean.getNewSalaryGrade());
//					
//				}
			empMoveDeclareFacadeRemote.add(entity, subBean);	
			
			// add by liuyi 20100507 
			hrJEmpInfoFacadeRemote.update(empInfo);
			write( STR_SAVE_SUCCESS);
			LogUtil.log("Action:新增岗位调动单结束", Level.INFO, null);
		}catch (SQLException sqle) {
            LogUtil.log("Action:新增岗位调动单失败", Level.SEVERE, null);
            write(Constants.SQL_FAILURE);
		} 
	}
	
	/**
	 * add by fyyang 20100707
	 */
	public void getMaxRequisitionNo()
	{
		String requisitionNo= hrJStationremoveFacadeRemote.getMaxRequisitionNo(employee.getEnterpriseCode()); 
		write(requisitionNo);
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
	/**
	 * 根据日期日期字符串和形式返回日期
	 * 
	 * @param argDateStr
	 *            日期字符串
	 * @param argFormat
	 *            日期形式字符串
	 * @return 日期
	 */
	private Date formatStringToDate(String argDateStr, String argFormat)
			throws DataFormatException {
		if (argDateStr == null || argDateStr.trim().length() < 1) {
			return null;
		}
		// 日期形式
		SimpleDateFormat sdfFrom = null;
		// 返回日期
		Date dtresult = null;

		try {
			sdfFrom = new SimpleDateFormat(argFormat);
			// 格式化日期
			dtresult = sdfFrom.parse(argDateStr);
		} catch (Exception e) {
			dtresult = null;
		} finally {
			sdfFrom = null;
		}

		return dtresult;
	}
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getBeforeDeptCode() {
		return beforeDeptCode;
	}

	public void setBeforeDeptCode(String beforeDeptCode) {
		this.beforeDeptCode = beforeDeptCode;
	}

	public String getAfterDeptCode() {
		return afterDeptCode;
	}

	public void setAfterDeptCode(String afterDeptCode) {
		this.afterDeptCode = afterDeptCode;
	}

	public String getDcmStatus() {
		return dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getRequisitionNo() {
		return requisitionNo;
	}

	public void setRequisitionNo(String requisitionNo) {
		this.requisitionNo = requisitionNo;
	}

	public String getStaionLevel() {
		return staionLevel;
	}

	public void setStaionLevel(String staionLevel) {
		this.staionLevel = staionLevel;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getRemoveDate() {
		return removeDate;
	}

	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}

	public String getDoDate2() {
		return doDate2;
	}

	public void setDoDate2(String doDate2) {
		this.doDate2 = doDate2;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	public String getStationChangeType() {
		return stationChangeType;
	}

	public void setStationChangeType(String stationChangeType) {
		this.stationChangeType = stationChangeType;
	}

	public String getOldCheckStationGrade() {
		return oldCheckStationGrade;
	}

	public void setOldCheckStationGrade(String oldCheckStationGrade) {
		this.oldCheckStationGrade = oldCheckStationGrade;
	}

	public String getNewCheckStationGrade() {
		return newCheckStationGrade;
	}

	public void setNewCheckStationGrade(String newCheckStationGrade) {
		this.newCheckStationGrade = newCheckStationGrade;
	}

	public String getOldStationGrade() {
		return oldStationGrade;
	}

	public void setOldStationGrade(String oldStationGrade) {
		this.oldStationGrade = oldStationGrade;
	}

	public String getNewStationGrade() {
		return newStationGrade;
	}

	public void setNewStationGrade(String newStationGrade) {
		this.newStationGrade = newStationGrade;
	}

	public String getOldSalaryGrade() {
		return oldSalaryGrade;
	}

	public void setOldSalaryGrade(String oldSalaryGrade) {
		this.oldSalaryGrade = oldSalaryGrade;
	}

	public String getNewSalaryGrade() {
		return newSalaryGrade;
	}

	public void setNewSalaryGrade(String newSalaryGrade) {
		this.newSalaryGrade = newSalaryGrade;
	}

	public String getDoDate() {
		return doDate;
	}

	public void setDoDate(String doDate) {
		this.doDate = doDate;
	}

	public String getSubReason() {
		return subReason;
	}

	public void setSubReason(String subReason) {
		this.subReason = subReason;
	}

	public String getMomo() {
		return momo;
	}

	public void setMomo(String momo) {
		this.momo = momo;
	}

	public String getBfStationId() {
		return bfStationId;
	}

	public void setBfStationId(String bfStationId) {
		this.bfStationId = bfStationId;
	}

	public String getBfStaionLevel() {
		return bfStaionLevel;
	}

	public void setBfStaionLevel(String bfStaionLevel) {
		this.bfStaionLevel = bfStaionLevel;
	}
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getStationRemoveId() {
		return stationRemoveId;
	}

	public void setStationRemoveId(String stationRemoveId) {
		this.stationRemoveId = stationRemoveId;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getLastModifiedDateSub() {
		return lastModifiedDateSub;
	}

	public void setLastModifiedDateSub(String lastModifiedDateSub) {
		this.lastModifiedDateSub = lastModifiedDateSub;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getOldBanzu() {
		return oldBanzu;
	}

	public void setOldBanzu(String oldBanzu) {
		this.oldBanzu = oldBanzu;
	}

	public String getNewBanzu() {
		return newBanzu;
	}

	public void setNewBanzu(String newBanzu) {
		this.newBanzu = newBanzu;
	}

	public String getPrintDate() {
		return printDate;
	}

	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	
	
}
