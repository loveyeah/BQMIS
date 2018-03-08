/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.register.action;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlockFacadeRemote;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.dao.HrJEmpInfoDao;
import power.ejb.run.runlog.RunCSpecials;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.ejb.run.runlog.shift.RunCUnitprofessionFacadeRemote;
import power.ejb.webservice.run.ticketmanage.WsJTaskFacadeRemote;
import power.ejb.workticket.RunCWorkticketFirelevel;
import power.ejb.workticket.RunCWorkticketFirelevelFacadeRemote;
import power.ejb.workticket.RunCWorkticketSource;
import power.ejb.workticket.RunCWorkticketSourceFacadeRemote;
import power.ejb.workticket.RunCWorkticketType;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.RunCWorkticketWorkcondition;
import power.ejb.workticket.RunCWorkticketWorkconditionFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketActors;
import power.ejb.workticket.business.RunJWorkticketContent;
import power.ejb.workticket.business.RunJWorkticketContentFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketMap;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.WorkticketCharger;
import power.ejb.workticket.business.WorkticketManager;
import power.ejb.workticket.form.ChargerForm;
import power.ejb.workticket.form.WorkticketBusiStatus;
import power.ejb.workticket.form.WorkticketInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票明细（填写）页面设计（Tab1）Action
 * 
 * @author wangpeng
 */
public class WorkticketBaseInfoAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 工作票种类取得远程处理对象 */
	private RunCWorkticketTypeFacadeRemote remoteType;
	/** 工作票来源取得远程处理对象 */
	private RunCWorkticketSourceFacadeRemote remoteSource;
	/** 检修专业取得远程处理对象 */
	private RunCSpecialsFacadeRemote remoteSpecials;
	/** 工作条件取得远程处理对象 */
	private RunCWorkticketWorkconditionFacadeRemote remoteWorkCondition;
	/** 所属机组或系统取得远程处理对象 */
	private EquCBlockFacadeRemote remoteBlock;
	/** 动火级别取得远程处理对象 */
	private RunCWorkticketFirelevelFacadeRemote remoteFireLevel;
	/** 工作票信息远程处理对象 */
	private RunJWorkticketsFacadeRemote remoteWorktickets;
	/** 工作票,电气一种票图片远程处理对象 */
	private WorkticketManager workticketManager;
	/** 工作票基本信息 */
	private RunJWorktickets workticketBaseInfo;
	/** 工作票负责人选择远程服务处理对象 */
	private WorkticketCharger remoteWC;
	/** 选择动火执行人页面远程服务处理对象 */
	private HrJEmpInfoFacadeRemote remoteFC;
	/** 工作票监工选择远程处理对象 */
	private HrJEmpInfoFacadeRemote remoteWSource;
	/** 部门remote */
	HrCDeptFacadeRemote deptRemote;
	/** 工作票监工选择远程处理对象 */
	private WsJTaskFacadeRemote remoteTask;

	/** 定义null */
	private static final String NULL = "null";
	/** 上传文件 */
	private File photo;

	/**
	 * 构造方法
	 */
	public WorkticketBaseInfoAction() {

	}

	/** ********************关联主票START********************************* */

	/** 关联主票处理远程对象 */
	private RunJWorkticketsFacadeRemote runJRemote;
	/** 工作票状态处理远程对象 */
	private BqWorkticketApprove workticketApprove;

	/**
	 * 取得工作票状态
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getWorkticketStatusRef() throws JSONException {
		// 工作票状态取得远程处理对象
		workticketApprove = (BqWorkticketApprove) factory
				.getFacadeRemote("BqWorkticketApproveImpl");
		// 取得工作票状态List
		List<WorkticketBusiStatus> list = workticketApprove
				.findBusiStatusList();
		// 结果为null
		if (list == null) {
			list = new ArrayList<WorkticketBusiStatus>();
		}
		// 添加"所有"选项
		WorkticketBusiStatus workticketBusiStatus = new WorkticketBusiStatus();
		// 种类为"所有"
		workticketBusiStatus.setWorkticketStatusName("所有");
		// 编码为空
		workticketBusiStatus.setWorkticketStausId(null);
		// 添加
		list.add(0, workticketBusiStatus);
		// 转化为PageObject
		PageObject object = new PageObject();
		// 设置list
		object.setList(list);
		// 设置长度
		object.setTotalCount(new Long(list.size()));
		write(JSONUtil.serialize(object));
	}

	/**
	 * 工作票类型的取得
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getDetailWorkticketTypeRef() throws JSONException {
		// 工作票类型取得远程处理对象
		remoteType = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		// 根据企业编码取得所有工作票类型
		PageObject object = remoteType.findAll(Constants.ENTERPRISE_CODE);
		// 查询结果为null
		if (object == null) {
			object.setList(new ArrayList<RunCWorkticketType>());
		}
		// 添加"所有"选项
		RunCWorkticketType runCWorkticketType = new RunCWorkticketType();
		// 种类为"所有"
		runCWorkticketType.setWorkticketTypeName(Constants.ALL_SELECT);
		// 编码为空
		runCWorkticketType.setWorkticketTypeCode(Constants.BLANK_STRING);
		List lstResult = object.getList();
		// 添加
		lstResult.add(0, runCWorkticketType);

		// 不显示的工作票类型
		String strNoDisplayCode = request.getParameter("noDisplayCode");
		if (strNoDisplayCode != null && strNoDisplayCode.length() > 0) {
			int intIndex = -1;
			for (int intCnt = 0; intCnt < lstResult.size(); intCnt++) {
				RunCWorkticketType type = (RunCWorkticketType) lstResult
						.get(intCnt);
				// 查找相同的工作票类型编码
				if (type.getWorkticketTypeCode() != null
						&& strNoDisplayCode
								.equals(type.getWorkticketTypeCode())) {
					intIndex = intCnt;
					break;
				}
			}

			if (intIndex > -1) {
				lstResult.remove(intIndex);
			}
		}
		write(JSONUtil.serialize(object));
	}

	/**
	 * 取得关联主票
	 * 
	 * @throws JSONException
	 */
	public void getRefWorkticket() throws JSONException {
		// 关联主票取得远程处理对象
		runJRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		// 取得查询参数: 开始时间
		String startDate = request.getParameter("startDate");
		// 取得查询参数: 结束时间
		String endDate = request.getParameter("endDate");
		// 取得查询参数: 工作票种类
		String workticketTypeCode = request.getParameter("workticketTypeCode");
		// 取得查询参数: 工作票状态
		String workticketStatusId = request.getParameter("workticketStatusId");
		// 取得查询参数: 负责人所在班组
		String block = request.getParameter("block");
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));

		// 根据查询条件，取得关联主票
		PageObject object = runJRemote.getWorkticketRelMainList(
				Constants.ENTERPRISE_CODE, startDate, endDate,
				workticketTypeCode, workticketStatusId, block, intStart,
				intLimit);
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * 获得现有票列表 add by fyyang 081230
	 * 
	 * @throws JSONException
	 */
	public void getHisWorkticketList() throws JSONException {
		// 关联主票取得远程处理对象
		runJRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		// 取得查询参数: 开始时间
		String startDate = request.getParameter("startDate");
		// 取得查询参数: 结束时间
		String endDate = request.getParameter("endDate");
		// 取得查询参数: 工作票种类
		String workticketTypeCode = request.getParameter("workticketTypeCode");
		// 取得查询参数: 工作票状态
		String workticketStatusId = request.getParameter("workticketStatusId");
		// 取得查询参数: 负责人所在班组
		String block = request.getParameter("block");
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));

		String workticketNo = request.getParameter("workticketNo");
		String workticketContent = request.getParameter("content");
		//动火票级别 add by fyyang 090729
		String fireLevel=request.getParameter("fireLevel");
		// 是否标准票
		String isStandard = "";
		Object myobj = request.getParameter("isStandard");
		if (myobj == null) {
			isStandard = "N";
		} else {
			isStandard = myobj.toString();
		}
		// 根据查询条件，取得已终结票
		PageObject object = runJRemote.getWorkticketHisTicketList(
				Constants.ENTERPRISE_CODE, startDate, endDate,
				workticketTypeCode, workticketStatusId, block, workticketNo,
				isStandard,fireLevel,workticketContent, intStart, intLimit);
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * 终结票更新
	 * 
	 * @throws Exception
	 */
	public void updateDetailWorkticketInfoByOld() throws Exception {
		try {
			// 工作票取得远程处理对象
			workticketManager = (WorkticketManager) factory
					.getFacadeRemote("WorkticketManagerImpl");

			String strWorkticketNoNew = request.getParameter("workticketNoNew");
			String strWorkticketNoOld = request.getParameter("workticketNoOld");

			workticketManager.updateWorkticketInfoByOld(strWorkticketNoNew,
					strWorkticketNoOld, Constants.ENTERPRISE_CODE, employee
							.getWorkerCode(), (new Date()).toString());
			write(Constants.MODIFY_SUCCESS);
		} catch (Exception e) {
			write(Constants.MODIFY_FAILURE);
			throw e;
		}
	}

	/** ********************关联主票END********************************* */

	/**
	 * 工作票种类取得
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketTypeName() throws JSONException {
		// 工作票种类取得远程处理对象
		remoteType = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		PageObject obj = remoteType.findAll(Constants.ENTERPRISE_CODE);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 工作票来源取得
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketSource() throws JSONException {
		// 工作票来源取得远程处理对象
		remoteSource = (RunCWorkticketSourceFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketSourceFacade");
		String strSourceLike = new String();
		List<RunCWorkticketSource> list = remoteSource.findByNameOrId(
				Constants.ENTERPRISE_CODE, strSourceLike);
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 检修专业取得
	 * 
	 * @throws JSONException
	 */
	public void getRepairSpecialityType() throws JSONException {
		// 检修专业取得远程处理对象
		remoteSpecials = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		// 取得检修专业
		List<RunCSpecials> list = remoteSpecials.findByType("2",
				Constants.ENTERPRISE_CODE);
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 接收专业取得
	 * 
	 * @throws JSONException
	 */
	public void getReceiveSpecialityType() throws JSONException {
		// // 接收专业取得远程处理对象
		 remoteSpecials = (RunCSpecialsFacadeRemote) factory
		 .getFacadeRemote("RunCSpecialsFacade");
		 // 取得接收专业
		 List<RunCSpecials> list = remoteSpecials.findByType("1",
		 Constants.ENTERPRISE_CODE);
		 String str = JSONUtil.serialize(list);
		 write("{list:" + str + "}");
		// modified by liuyi 091117
//		RunCUnitprofessionFacadeRemote reciveRemote = (RunCUnitprofessionFacadeRemote) factory
//				.getFacadeRemote("RunCUnitprofessionFacade");
//
//		List<Object[]> list = reciveRemote.findUnitList(employee
//				.getEnterpriseCode());
//		StringBuffer sb = new StringBuffer();
//		sb.append("[");
//		for (Object[] o : list) {
//			sb.append("{");
//			sb.append("\"id\":" + o[0] + ",");
//			sb.append("\"specialityCode\":\"" + (o[1] == null ? "" : o[1])
//					+ "\",");
//			sb.append("\"specialityName\":\"" + (o[2] == null ? "" : o[2])
//					+ "\",");
//			sb.append("\"HSpecialityCode\":\"" + (o[3] == null ? "" : o[3])
//					+ "\",");
//			sb.append("\"displayNo\":" + (o[4] == null ? "''" : o[4]) + ",");
//			sb.append("\"HSpecialityName\":\"" + (o[5] == null ? "" : o[5])
//					+ "\"");
//			sb.append("},");
//		}
//		if (sb.length() > 1) {
//			sb.deleteCharAt(sb.lastIndexOf(","));
//		}
//		sb.append("]");
//		write(sb.toString());
	}

	/**
	 * 工作条件取得
	 * 
	 * @throws JSONException
	 */
	public void getWorkCondition() throws JSONException {
		// 工作条件取得远程处理对象
		remoteWorkCondition = (RunCWorkticketWorkconditionFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketWorkconditionFacade");
		// 取得工作条件
		List<RunCWorkticketWorkcondition> list = remoteWorkCondition
				.findByNameOrId(Constants.ENTERPRISE_CODE, "");
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 所属机组或系统取得
	 * 
	 * @throws JSONException
	 */
	public void getEquList() throws JSONException {
		// 所属机组或系统取得远程处理对象
		remoteBlock = (EquCBlockFacadeRemote) factory
				.getFacadeRemote("EquCBlockFacade");
		// 机组编码或名称
		String fuzzy = new String();
		PageObject obj = new PageObject();
		// 取得所属机组或系统
		obj = remoteBlock.findEquList(fuzzy, Constants.ENTERPRISE_CODE);
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 动火级别取得
	 * 
	 * @throws JSONException
	 */
	public void getFireLevel() throws JSONException {
		// 动火级别取得远程处理对象
		remoteFireLevel = (RunCWorkticketFirelevelFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketFirelevelFacade");
		// 动火票级别编码或名称
		String strLevelLike = new String();
		List<RunCWorkticketFirelevel> list = remoteFireLevel.findByNameOrId(
				Constants.ENTERPRISE_CODE, strLevelLike);
		String str = JSONUtil.serialize(list);
		write("{list:" + str + "}");
	}

	/**
	 * 
	 * @param workticketsCode
	 * @throws JSONException
	 */
	public void getFireLevelByWorkticketNo() throws JSONException {
		String workticketNo = request.getParameter("workticketNo");
		// 工作票信息远程处理对象
		remoteWorktickets = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		RunJWorktickets workticket = remoteWorktickets.findById(workticketNo);
		Long firelevelId = workticket.getFirelevelId();

		if (firelevelId == null) {
			write("1");
		} else {
			write(firelevelId.toString());
		}
	}

	/**
	 * 获得电气一种票图片信息
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public void getMapByWorkticketNo() throws JSONException, IOException {
		// 电气一种票图片远程处理对象
		workticketManager = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		// 工作票编码
		String strWorkticketNo = request.getParameter("workticketNo");
		RunJWorkticketMap entity = workticketManager
				.findMapByWorkticketNo(strWorkticketNo);
		response.setContentType("image/jpeg");
		if (entity != null) {
			// 获得图片
			byte[] workticketMap = entity.getWorkticketMap();
			if (workticketMap != null) {
				BufferedOutputStream bos = null;
				try {
					bos = new BufferedOutputStream(response.getOutputStream());
					bos.write(workticketMap);
					bos.flush();
				} catch (IOException e) {
				} finally {
					if (bos != null) {
						bos.close();
					}
				}
			}
		} else {
			// write("{success:true'}");
		}

	}

	/**
	 * 通过工作票编号取得所有工作票基本信息
	 * 
	 * @throws JSONException
	 */
	public void getWorkticketBaseInfoByNo() throws JSONException {
		// 工作票信息远程处理对象
		workticketManager = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		// 工作票编号
		String workticketNo = request.getParameter("workticketNo");
		// 工作票信息
		RunJWorktickets entity = workticketManager
				.findWorkticketByNo(workticketNo);
		String strWorkticketBaseInfo = JSONUtil.serialize(entity);
		write("{success: true,data:" + strWorkticketBaseInfo + "}");
	}

	/**
	 * 通过编码查询一条工作票信息汉字名(修改时)
	 * 
	 * @throws JSONException
	 */
	public void getTicketInfoByCode() throws JSONException {

		// 工作票信息远程处理对象
		remoteWorktickets = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		// 工作票编号
		String workticketNo = request.getParameter("workticketNo");
		// 工作票信息部分汉字名
		WorkticketInfo workticketInfo = remoteWorktickets.queryWorkticket(
				Constants.ENTERPRISE_CODE, workticketNo);
		String strBaseInfo = JSONUtil.serialize(workticketInfo);
		write("{success: true,data:" + strBaseInfo + "}");

	}

	/**
	 * 创建一条工作票记录
	 * 
	 * @throws IOException
	 */
	public void addWorkticketBaseInfo() throws IOException {
		workticketManager = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		// add by fyyang 090310
		workticketBaseInfo.setEntryBy(employee.getWorkerCode());
		// add by fyyang 090217 是否由标准票生成
		String standticketNo = "";

		if (request.getParameter("standticketNo") != null) {

			standticketNo = request.getParameter("standticketNo");

		}
		if (standticketNo.equals("")) {
			workticketBaseInfo.setIsCreatebyStand("N");
		} else {
			runJRemote = (RunJWorkticketsFacadeRemote) factory
					.getFacadeRemote("RunJWorkticketsFacade");
			workticketBaseInfo.setIsCreatebyStand("Y");
			RunJWorktickets model = runJRemote.findById(standticketNo);
			workticketBaseInfo.setWorkticketTypeCode(model
					.getWorkticketTypeCode());
			// workticketBaseInfo.setRepairSpecailCode(model.getRepairSpecailCode());
			// workticketBaseInfo.setSourceId(model.getSourceId());
			// workticketBaseInfo.setPermissionDept(model.getPermissionDept());
			// workticketBaseInfo.setEquAttributeCode(model.getEquAttributeCode());
			// workticketBaseInfo.setConditionName(model.getConditionName());
			// workticketBaseInfo.setLocationName(model.getLocationName());
			// workticketBaseInfo.setDangerCondition(model.getDangerCondition());
			// workticketBaseInfo.setDangerType(model.getDangerType());
			// workticketBaseInfo.setFirelevelId(model.getFirelevelId());
		}
		// enterpriseChar 企业编码的一位标识（可从session里获取）
		String strEnterpriseChar = new String();
		strEnterpriseChar = employee.getEnterpriseChar();
		// 选择图片
		String filename = request.getParameter("file");
		// add by fyyang 危险点内容Id
		Object objdanger = request.getParameter("dangerId");
		String dangerId = "";
		if (objdanger != null) {
			dangerId = objdanger.toString();
		}

		// add by fyyang 是否标准票 Y----是，N-----否
		String isStandard = request.getParameter("isStandard");
		workticketBaseInfo.setIsStandard(isStandard);
		// 企业编码
		workticketBaseInfo.setEnterpriseCode(Constants.ENTERPRISE_CODE);

		// added by jyuan 12/13 start
		if (workticketBaseInfo.getIsEmergency() == null
				|| "".equals(workticketBaseInfo.getIsEmergency())) {
			workticketBaseInfo.setIsEmergency("N");
		}
		// added by jyuan 12/13 end
		// qzhang start
		if (workticketBaseInfo.getRefWorkticketNo() == null
				|| "".equals(workticketBaseInfo.getRefWorkticketNo())) {
			workticketBaseInfo.setRefWorkticketNo("0");
		}
		// qzhang end
		byte[] data = null;
		java.io.FileInputStream fis = null;
		// 结果初始化
		RunJWorktickets entity = null;
		 String existWorkticketNo=request.getParameter("existWorkticketNo");
		try {
			if (filename != null && filename.length() > 0) {
				// 如果有文件上传
				fis = new java.io.FileInputStream(photo);
				data = new byte[(int) fis.available()];
				fis.read(data);
			}

			if (data != null) {
				// 电气一种票--保存图片
				entity = workticketManager.createWorkticket(workticketBaseInfo,
						strEnterpriseChar, dangerId, employee.getWorkerCode(),
						standticketNo,existWorkticketNo, data);
			} else {
				entity = workticketManager.createWorkticket(workticketBaseInfo,
						strEnterpriseChar, dangerId, employee.getWorkerCode(),
						standticketNo,existWorkticketNo);
			}
		} catch (IOException e) {
//			// 读取文件出错
//			entity = workticketManager.createWorkticket(workticketBaseInfo,
//					strEnterpriseChar, dangerId, employee.getWorkerCode(),
//					standticketNo,existWorkticketNo);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		
		//add by fyyang ---090523------------------------
		String memberRecords=request.getParameter("memberRecords");
    //	String contentRecords = request.getParameter("contentRecords");
    	
    	
		
		if (entity != null) {
			String strWorkticketNo = entity.getWorkticketNo();
			  
	            //保存工作内容明细 
	            try { 
//	                RunJWorkticketContentFacadeRemote contentRemote =(RunJWorkticketContentFacadeRemote)factory.getFacadeRemote("RunJWorkticketContentFacade");
//	    			if (contentRecords != null && !contentRecords.trim().equals("")&&!contentRecords.equals("null")) {
//	    				List<RunJWorkticketContent> list = parseContent(contentRecords,strWorkticketNo);
//	    				contentRemote.modifyRecords(strWorkticketNo,list, null);
//	    			}
	    			
	    			//工作班成员
	    			if (memberRecords != null && !memberRecords.trim().equals("")&&!memberRecords.equals("null"))
	    			{
	    				this.saveMembers(memberRecords, strWorkticketNo);
	    			}
	    		} catch (Exception exc) {
	    			 exc.printStackTrace();
	    		}
			write("{success:true,msg:'增加成功！',workticketNo:'" + strWorkticketNo
					+ "'}");
		} else {
			write(Constants.ADD_CODE_FAILURE);
		}
		//-------------add end---------------------------
	}
	
	//----------add by fyyang 090523---------------------
	   @SuppressWarnings("unchecked")
		private void saveMembers(String records,String workticketNo) throws JSONException, CodeRepeatException
	    {
	    	 Object obj = JSONUtil.deserialize(records);
			   List<Map> list = (List<Map>)obj;
			   for(Map data : list)
			   {
				   String actorCode = data.get("actor.actorCode")==null?"": data.get("actor.actorCode").toString();
				   String actorName = data.get("actor.actorName")==null?"":data.get("actor.actorName").toString();
				   String actorDept = data.get("deptName")==null?"":data.get("deptName").toString();
				   String actorType = data.get("actortypename")==null?"":data.get("actortypename").toString();
				   RunJWorkticketActors  model = new RunJWorkticketActors();
				   model.setEnterpriseCode(employee.getEnterpriseCode());
				   model.setActorDept(actorDept);
				   model.setActorType(Long.parseLong(actorType));
				   model.setActorCode(actorCode);
				   model.setActorName(actorName);
				   model.setWorkticketNo(workticketNo);
					workticketManager.addWorkticketMember(model);
			   }
			   
	    }
//	 private List<RunJWorkticketContent> parseContent(String records,String workticketNo) throws JSONException
//		{
//			List<RunJWorkticketContent> result = new ArrayList<RunJWorkticketContent>();
//			Object object = JSONUtil.deserialize(records);
//			List list=(List)object;
//			int intLen = list.size();
//			for(int i=0;i<intLen;i++){
//				Map map=(Map)list.get(i);
//				RunJWorkticketContent m = this.parseContentModal(map,workticketNo);
//				result.add(m);
//			}
//			return result;
//		}
//		private RunJWorkticketContent parseContentModal(Map map,String workticketNo)
//		{
//			RunJWorkticketContent model=new RunJWorkticketContent(); 
//			Object id= map.get("id"); 
//			Object worktypeName= map.get("worktypeName");
//			Object flagDesc= map.get("flagDesc");
//			Object line= map.get("line");
//			Object frontKeyDesc= map.get("frontKeyDesc");
//			Object locationName= map.get("locationName");  
//			Object attributeCode= map.get("attributeCode"); 
//			Object equName= map.get("equName"); 
//			Object backKeyDesc= map.get("backKeyDesc"); 
//			Object isreturn= map.get("isreturn");  
//			if(id!=null)
//			model.setId(Long.parseLong(id.toString()));
//			if(line!=null)
//				model.setLine(Long.parseLong(line.toString()));
//			model.setWorkticketNo(workticketNo);
//			if(worktypeName !=null)
//			model.setWorktypeName(worktypeName.toString());
//			if(flagDesc !=null)
//				model.setFlagDesc(flagDesc.toString());
//			if(frontKeyDesc !=null)
//				model.setFrontKeyDesc(frontKeyDesc.toString());
//			if(locationName !=null)
//				model.setLocationName(locationName.toString());
//			if(attributeCode !=null)
//				model.setAttributeCode(attributeCode.toString());
//			if(equName !=null)
//					model.setEquName(equName.toString());
//			if(backKeyDesc !=null)
//				model.setBackKeyDesc(backKeyDesc.toString());
//			
//			if(isreturn !=null)
//				model.setIsreturn(isreturn.toString()); 
//			model.setIsUse("Y");
//			model.setCreateBy(employee.getWorkerCode());
//			model.setCreateDate(new Date());
//			model.setEnterpriseCode(employee.getEnterpriseCode());
//			return model;
//		}
	//---------------------------------------------------

	/**
	 * 修改工作票的基本信息
	 * 
	 * @throws IOException
	 */
	public void updateWorkticketBaseInfo() throws IOException {

		workticketManager = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		// add by fyyang 危险点内容Id
		Object objdanger = request.getParameter("dangerId");
		String dangerId = "";
		if (objdanger != null) {
			dangerId = objdanger.toString();
		}
		// 工作票编号
		String workticketNo = workticketBaseInfo.getWorkticketNo();
		RunJWorktickets entity = workticketManager
				.findWorkticketByNo(workticketNo);
		// if(entity.getIsCreatebyStand().equals("N"))
		{
			// 标准票的以下字段不能修改
			// 来源 add by fyyang 090309
			entity.setSourceId(workticketBaseInfo.getSourceId());

			entity.setLocationName(workticketBaseInfo.getLocationName());// add
			// by
			// fyyang
			// 090319
			// modify by fyyang 090320 工作负责人及所在部门不能修改
			// // 工作负责人（编码）
			// entity.setChargeBy(workticketBaseInfo.getChargeBy());
			// 监工（编码）
			entity.setWatcher(workticketBaseInfo.getWatcher());
			// // 所属部门
			// entity.setChargeDept(workticketBaseInfo.getChargeDept());
			// add by fyyang 090328 任务单号
			entity.setApplyNo(workticketBaseInfo.getApplyNo());

			// 检修专业可以修改modify by fyyang 090320
			entity.setRepairSpecailCode(workticketBaseInfo
					.getRepairSpecailCode());
			// 接收专业
			entity.setPermissionDept(workticketBaseInfo.getPermissionDept());
			// 工作条件
			// entity.setConditionId(workticketBaseInfo.getConditionId());
			// modify by fyyang 090213 字段更改
			entity.setConditionName(workticketBaseInfo.getConditionName());
			// 所属机组或系统
			entity
					.setEquAttributeCode(workticketBaseInfo
							.getEquAttributeCode());

			// 关联主票
			entity.setRefWorkticketNo(workticketBaseInfo.getRefWorkticketNo());
			// 动火执行人
			entity.setFireticketExecuteBy(workticketBaseInfo
					.getFireticketExecuteBy());
			// 动火票级别
			// entity.setFirelevelId(workticketBaseInfo.getFirelevelId());

			// 是否紧急票 add by fyyang 090109
			// entity.setIsEmergency(workticketBaseInfo.getIsEmergency());
			entity.setDangerCondition(workticketBaseInfo.getDangerCondition());
			entity.setDangerType(workticketBaseInfo.getDangerType());
		}
		entity.setIsNeedMeasure(workticketBaseInfo.getIsNeedMeasure());		
		entity.setMemberCount(workticketBaseInfo.getMemberCount());
        entity.setMembers(workticketBaseInfo.getMembers());
        entity.setWorkticketContent(workticketBaseInfo.getWorkticketContent());
		// 备注
		entity.setWorkticketMemo(workticketBaseInfo.getWorkticketMemo());
		// 计划开始时间
		entity.setPlanStartDate(workticketBaseInfo.getPlanStartDate());
		// 计划结束时间
		entity.setPlanEndDate(workticketBaseInfo.getPlanEndDate());
		entity.setFailureCode(workticketBaseInfo.getFailureCode());// add by
		// fyyang
		// 090309

		// 修改主设备 add by fyyang 090416
		entity.setMainEquCode(workticketBaseInfo.getMainEquCode());
		entity.setMainEquName(workticketBaseInfo.getMainEquName());

		entity.setAutoDeviceName(workticketBaseInfo.getAutoDeviceName());
		// 选择图片
		String filename = request.getParameter("file");

		// 结果初始化
		RunJWorktickets return_entity = null;
		byte[] data = null;
		java.io.FileInputStream fis = null;
		try {
			if (filename != null && filename.length() > 0) {
				// 如果有文件上传
				fis = new java.io.FileInputStream(photo);
				data = new byte[(int) fis.available()];
				fis.read(data);
			}

			if (data != null) {
				// 电气一种票--保存图片
				return_entity = workticketManager.updateWorkticket(entity,
						dangerId, employee.getWorkerCode(), data);
			} else {
				// 读取文件出错
				return_entity = workticketManager.updateWorkticket(entity,
						dangerId, employee.getWorkerCode());
			}
		} catch (IOException e) {
			return_entity = workticketManager.updateWorkticket(entity,
					dangerId, employee.getWorkerCode());
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		if (return_entity != null) {
			write(Constants.MODIFY_SUCCESS);
		} else {
			write("{success:true,msg:'修改失败！'}");
		}
	}

	/**
	 * 工作票负责人选择
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getCharger() throws Exception {

		// 工作票负责人选择远程服务处理对象
		remoteWC = (WorkticketCharger) factory
				.getFacadeRemote("WorkticketChargerImpl");
		// 部门remote
		deptRemote = (HrCDeptFacadeRemote) factory
				.getFacadeRemote("HrCDeptFacade");
		PageObject object = new PageObject();
		// 获得查询参数：工号或姓名
		String userName = Constants.BLANK_STRING;
		// 工作票名称
		Object workticketTypeName = request.getParameter("workticketTypeName");
		// 取得工号或名称
		Object myobj = request.getParameter("userName");

		if (myobj != null && !Constants.BLANK_STRING.equals(myobj)) {
			userName = myobj.toString();
		} else {
			userName = Constants.ALL_DATA;
		}
		// 查询数据
		object.setList(remoteWC.findCharger(workticketTypeName.toString(),
				userName));
		List<ChargerForm> message = object.getList();

		// 查找部门名称dao
		HrJEmpInfoDao dao = new HrJEmpInfoDao();

		// 拼接字符串
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"").append("list").append("\":[");
		for (int intCnt = 0; intCnt < message.size(); intCnt++) {
			ChargerForm model = (ChargerForm) message.get(intCnt);
			i++;
			sb
					.append("{\"empCode\":\"")
					.append(
							model.getWorkerCode() == null ? Constants.BLANK_STRING
									: model.getWorkerCode().toString())
					.append("\"")
					// 姓名
					.append(",\"chsName\":\"")
					.append(
							model.getWorkerName() == null ? Constants.BLANK_STRING
									: model.getWorkerName().toString())
					.append("\"")
					// 部门名称
					.append(",\"deptName\":\"")
					.append(
							model.getDeptName() == null ? Constants.BLANK_STRING
									: model.getDeptName().toString())
					.append("\"")
					// // 部门id
					// .append(",\"deptId\":\"").append(model.getDeptId()).append(
					// "\"")
					// 部门Code
					.append(",\"deptCode\":\"").append(model.getDeptCode())
					.append("\"}");
			if (i < message.size()) {
				sb.append(",");
			} else {
				sb.append("],");
			}

		}
		// 数据为空时添加
		if (message.size() < 1) {
			sb.append("],");
		}
		// 所取数据总数
		sb.append("\"totalCount\":").append(new Long(object.getList().size()))
				.append("}");
		write(sb.toString());

	}

	/**
	 * 选择动火执行人页面:查找部门人员
	 */
	@SuppressWarnings("unchecked")
	public void getFireCharge() throws Exception {
		// 选择动火执行人页面远程服务处理对象
		remoteFC = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		PageObject object = new PageObject();
		// 设置查询参数
		String userName = Constants.BLANK_STRING;
		// deptId:部门id
		Object deptId = request.getParameter("deptId");
		// userName:用户名称或编号，
		Object myobj = request.getParameter("userName");
		// 分页信息
		Object objstart = request.getParameter("start");
		// 分页信息
		Object objlimit = request.getParameter("limit");

		if (myobj != null && !Constants.BLANK_STRING.equals(myobj)) {
			userName = myobj.toString();
		} else {
			userName = Constants.ALL_DATA;
		}
		if (objstart != null && objlimit != null) {
			// 分页
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remoteFC.queryByFuzzy(Constants.ENTERPRISE_CODE, deptId
					.toString(), userName, start, limit);
		} else {
			// 不分页
			object = remoteFC.queryByFuzzy(Constants.ENTERPRISE_CODE, deptId
					.toString(), userName);
		}
		// 得到查找结果
		List<HrJEmpInfo> message = object.getList();
		// 查询部门名称dao
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		// 拼接字符串
		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("{").append("\"").append("list").append("\":[");
		for (int intCnt = 0; intCnt < message.size(); intCnt++) {
			HrJEmpInfo model = (HrJEmpInfo) message.get(intCnt);
			i++;
			// 查询部门名称
			String deptName = dao.GetOneDept(Long.toString(model.getDeptId()));
			// 工号
			sb.append("{\"empCode\":\"").append(
					model.getEmpCode() == null ? Constants.BLANK_STRING : model
							.getEmpCode().toString()).append("\"")
			// 姓名
					.append(",\"chsName\":\"").append(
							model.getChsName() == null ? Constants.BLANK_STRING
									: model.getChsName().toString()).append(
							"\"")
					// 部门名称
					.append(",\"deptName\":\"").append(
							deptName == null ? Constants.BLANK_STRING
									: deptName.toString()).append("\"")
					// 部门id
					.append(",\"deptId\":\"").append(model.getDeptId()).append(
							"\"}");
			if (i < message.size()) {
				sb.append(",");
			} else {
				sb.append("],");
			}

		}
		if (message.size() < 1) {
			sb.append("],");
		}
		// 数据总和
		sb.append("\"totalCount\":").append(new Long(object.getList().size()))
				.append("}");
		write(sb.toString());
	}

	/**
	 * 工作票监工选择:查找工作票监工人员
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public void getWatcher() {
		try {
			WorkticketCharger wc = (WorkticketCharger) factory
					.getFacadeRemote("WorkticketChargerImpl");
			String userName = request.getParameter("userName");
			List<ChargerForm> list = wc.findWatcher(userName);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}

	/**
	 * 得到工作票信息
	 */
	public RunJWorktickets getWorkticketBaseInfo() {
		return workticketBaseInfo;
	}

	/**
	 * 设置工作票信息
	 * 
	 * @param workticketBaseInfo
	 */
	public void setWorkticketBaseInfo(RunJWorktickets argWorkticketBaseInfo) {
		this.workticketBaseInfo = argWorkticketBaseInfo;
	}

	// --------add by fyyang 090122 由标准票生成---------
	public void getStandListForSelect() throws JSONException {
		runJRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		// 取得查询参数: 开始时间
		String strStartDate = request.getParameter("startDate");
		// 取得查询参数: 结束时间
		String strEndDate = request.getParameter("endDate");
		// 取得查询参数: 工作票种类
		String strWorkticketTypeCode = request
				.getParameter("workticketTypeCode");
		// 取得查询参数: 工作票状态
		// String strWorkticketStatusId =
		// request.getParameter("workticketStatusId");
		// 取得查询参数: 所属机组或系统
		String strBlock = request.getParameter("block");
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));
		String newOrOld = request.getParameter("newOrOld");
		String content = request.getParameter("content");
		String workticketNo = request.getParameter("ticketNo");

		String fireLevel=request.getParameter("fireLevel"); //add by fyyang 20100506
		// 根据查询条件，取得工作票
		PageObject object = runJRemote.getStandListForSelect(
				Constants.ENTERPRISE_CODE, strStartDate, strEndDate,
				strWorkticketTypeCode, strBlock, newOrOld, content,workticketNo,fireLevel, intStart,
				intLimit);
		// 输出结果
		String strOutput = "";
		// 查询结果为null,设置页面显示
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		// System.out.println(strOutput);
		write(strOutput);
	}

	// public void createWorkticketByStandard()
	// {
	// String workticketNo=request.getParameter("workticketNo");
	// workticketManager.createWorkticketByStandTicket(workticketNo,
	// employee.getEnterpriseChar(), employee.getWorkerCode());
	// write("{success:true,msg:'生成成功！'}");
	// }

	/**
	 * 查询任务单
	 * 
	 * @throws JSONException
	 */
	public void getTaskList() throws JSONException {

		remoteTask = (WsJTaskFacadeRemote) factory
				.getFacadeRemote("WsJTaskFacade");
		PageObject object = new PageObject();
		// 取得查询条件：模糊任务单编号
		String taskNo = request.getParameter("taskNo");
		// 取得查询条件：任务单类型
		Integer taskType = Integer.parseInt(request.getParameter("taskType"));
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		// 判断是否为null
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			// 查询
			object = remoteTask.findListForSelect(taskNo, taskType, employee
					.getWorkerCode(), start, limit);
		} else {
			// 查询
			object = remoteTask.findListForSelect(taskNo, taskType, employee
					.getWorkerCode());
		}
		// 输出结果
		String strOutput = "";
		// 查询结果为null
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
			// 不为null
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	// --------add end---------------------------------
	public void findEndWorkticketList() throws JSONException {
		remoteWorktickets = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");

		// 计划开始时间
		String strStart = "";
		// 计划结束时间
		String strEnd = "";
		// 类型
		String strType = "";
		// 所属机组或系统
		String strSystem = "";
		// 状态
		String strState = "";
		// 获取前台数据
		Object sd = request.getParameter("startD");
		Object ed = request.getParameter("endD");
		Object tc = request.getParameter("typeC");
		Object syc = request.getParameter("systemC");
		// 取得查询条件: 开始行
		Object objstart = request.getParameter("start");
		// 取得查询条件：结束行
		Object objlimit = request.getParameter("limit");
		if (sd != null) {
			strStart = sd.toString();
		}
		if (ed != null) {
			strEnd = ed.toString();
		}
		if (tc != null) {
			strType = tc.toString();
		}
		if (syc != null) {
			strSystem = syc.toString();
		}

		// modify by fyyang 090513 增加工作内容查询条件
		String workticketContent = request.getParameter("content");
		String workticketNo = request.getParameter("ticketNo");
		
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remoteWorktickets.getEndWorkticketList(employee
					.getEnterpriseCode(), strStart, strEnd, strType, strSystem,
					workticketContent,workticketNo, start, limit);
		} else {
			obj = remoteWorktickets.getEndWorkticketList(employee
					.getEnterpriseCode(), strStart, strEnd, strType, strSystem,
					workticketContent,workticketNo);
		}
		String str = JSONUtil.serialize(obj);
		if (NULL.equals(str)) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		write(str);

	}

	// add by fyyang 090410 由终结票生成新的标准票
	public void createStandardByEndNo() {
		workticketManager = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		String endWorkticketNo = request.getParameter("workticketNo");
		String newWorkticketNo = workticketManager.createStandardByEndTicket(
				endWorkticketNo, employee.getWorkerCode());
		write("{" + newWorkticketNo + "}");
	}

	/**
	 * 获取上传文件
	 */
	public File getPhoto() {
		return photo;
	}

	/**
	 * 设置上传文件
	 * 
	 * @param argPhoto
	 *            上传文件
	 */
	public void setPhoto(File argPhoto) {
		photo = argPhoto;
	}

	public void createNewStandarWorktickect() {
		runJRemote = (RunJWorkticketsFacadeRemote) factory
				.getFacadeRemote("RunJWorkticketsFacade");
		RunJWorktickets model = new RunJWorktickets();
		String worktickectNo = request.getParameter("worktickectNo");
		RunJWorktickets entity = runJRemote.findById(worktickectNo);
		model.setMainEquCode(entity.getMainEquCode());
		model.setMainEquName(entity.getMainEquName());
		model.setAutoDeviceName(entity.getAutoDeviceName());
		model.setLocationName(entity.getLocationName());
		model.setWorkticketTypeCode(entity.getWorkticketTypeCode());
		model.setSourceId(entity.getSourceId());
		model.setRepairSpecailCode(entity.getRepairSpecailCode());
		model.setPermissionDept(entity.getPermissionDept());
		model.setConditionName(entity.getConditionName());
		model.setEquAttributeCode(entity.getEquAttributeCode());
		model.setDangerType(entity.getDangerType());
		model.setDangerCondition(entity.getDangerCondition());
		model.setWorkticketMemo(entity.getWorkticketMemo());
		model.setFirelevelId(entity.getFirelevelId());
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new Date());
		model.setIsCreatebyStand(entity.getIsCreatebyStand());
		model.setIsStandard(entity.getIsStandard());
		model.setIsEmergency(entity.getIsEmergency());
		model.setIsUse(entity.getIsUse());
		model.setEnterpriseCode(entity.getEnterpriseCode());
		String newWorktickectNo = runJRemote.createStandardWorkticketNo(model
				.getEnterpriseCode(), model.getWorkticketTypeCode(), model
				.getFirelevelId());
		model.setWorkticketNo(newWorktickectNo);
		runJRemote.createnewDeloldWorkTickect(model, worktickectNo);
		write(model.getWorkticketNo());
	}
	
	/**
	 * add by liuyi 091118
	 * 工单管理中的接收专业 
	 */
	public void getWorkOrderSpecialityType() {
		RunCUnitprofessionFacadeRemote reciveRemote = (RunCUnitprofessionFacadeRemote) factory
				.getFacadeRemote("RunCUnitprofessionFacade");

		List<Object[]> list = reciveRemote.findUnitList(employee
				.getEnterpriseCode());
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (Object[] o : list) {
			sb.append("{");
			sb.append("\"id\":" + o[0] + ",");
			sb.append("\"specialityCode\":\"" + (o[1] == null ? "" : o[1])
					+ "\",");
			sb.append("\"specialityName\":\"" + (o[2] == null ? "" : o[2])
					+ "\",");
			sb.append("\"HSpecialityCode\":\"" + (o[3] == null ? "" : o[3])
					+ "\",");
			sb.append("\"displayNo\":" + (o[4] == null ? "''" : o[4]) + ",");
			sb.append("\"HSpecialityName\":\"" + (o[5] == null ? "" : o[5])
					+ "\"");
			sb.append("},");
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("]");
		write(sb.toString());
	}
}
