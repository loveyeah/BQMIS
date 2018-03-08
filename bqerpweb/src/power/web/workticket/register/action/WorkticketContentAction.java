/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.register.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.workticket.RunCWorkticketContentKey;
import power.ejb.workticket.RunCWorkticketContentKeyFacadeRemote;
import power.ejb.workticket.RunCWorkticketFireContentFacadeRemote;
import power.ejb.workticket.RunCWorkticketFlag;
import power.ejb.workticket.RunCWorkticketFlagFacadeRemote;
import power.ejb.workticket.RunCWorkticketLocationFacadeRemote;
import power.ejb.workticket.RunCWorkticketTypeFacadeRemote;
import power.ejb.workticket.RunCWorkticketWorktype;
import power.ejb.workticket.RunCWorkticketWorktypeFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketActors;
import power.ejb.workticket.business.RunJWorkticketContent;
import power.ejb.workticket.business.RunJWorkticketContentFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketFireContent;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.WorkticketManager;
import power.ejb.workticket.form.WorkticketActors;
import power.ejb.workticket.form.WorkticketFireContent;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票内容Action
 *
 * @author 陈寿江，黄维杰
 *
 */
@SuppressWarnings("serial")
public class WorkticketContentAction extends AbstractAction {
	/** 远程服务 */
	protected WorkticketManager remoteManager;
	protected RunCWorkticketContentKeyFacadeRemote remoteContentKey;
	protected RunCWorkticketLocationFacadeRemote remoteLocation;
	protected RunCWorkticketTypeFacadeRemote remoteType;
	protected RunCWorkticketFlagFacadeRemote remoteFlag;
	protected RunCWorkticketWorktypeFacadeRemote remoteWorktype;
	protected RunJWorkticketContentFacadeRemote contentRemote;  //add by fyyang 090311
   
	private RunJWorkticketContent entity;
	private String equName;
	private String attributeCode;

	/** 弹出窗口 */
	/** 工作班成员选择窗口 */
	protected WorkticketManager remoteWorkticket;
	/** 获得部门员工 */
	protected HrJEmpInfoFacadeRemote remoteEmpInfo;

	/** 动火票类别选择窗口 */
	/** 所有动火内容远程服务 */
	protected RunCWorkticketFireContentFacadeRemote remoteFireContent;
	/** 工作票动火内容远程服务 */
	protected WorkticketManager remoteWorkticketFireContent;

	/** ************** 弹出窗口 ************************** */

	/** 工作班成员选择 */
	/** 工作票号 */
	private String workticketNo;
	/** 工作班成员 */
	private WorkticketActors actors;
	/** 成员实体 */
	private RunJWorkticketActors workticketEntity;
	/** 临时员工名字 */
	private String actorName;
	/** 成员身份（1.正式员工；2.临时员工） */
	private String actortypename;
	/** 实体身份（1.正式员工；2.临时员工） */
	private Long actorType;
	/** 部门号 */
	private String actorDept;
	/** 单个删除的成员Id */
	private Long id;
	/** 批量处理的成员Ids（包括添加和删除） */
	private String ids;
	/** 批量处理的成员Names（包括添加和删除） */
	private String names;
	/** 部门树节点 */
	private Object depId;
	/** 员工信息 */
	private HrJEmpInfo empInfo;

	/** 动火作业方式选择窗口 */
	/** 动火内容编码或名称 */
	private String contentLike = "";
	// /** 删除的动火id */
	// private Long id;
	// /** 批量操作的动火ids（添加和删除） */
	// private String ids;
	/** 动火票作业方式实体 */
	private RunJWorkticketFireContent entityFireContent;
	/** 动火票作业方式 */
	private WorkticketFireContent fireContents;
	/** 排列号 */
	private Long orderBy;// = 1L;

	/**
	 * 构造函数，初始化remoteManager
	 */
	public WorkticketContentAction() {
		//add by fyyang 090311
		contentRemote=(RunJWorkticketContentFacadeRemote)factory.getFacadeRemote("RunJWorkticketContentFacade");
		
		remoteManager = (WorkticketManager) factory.getInstance()
				.getFacadeRemote("WorkticketManagerImpl");
		remoteContentKey = (RunCWorkticketContentKeyFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketContentKeyFacade");
		remoteLocation = (RunCWorkticketLocationFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketLocationFacade");
		remoteType = (RunCWorkticketTypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketTypeFacade");
		remoteFlag = (RunCWorkticketFlagFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketFlagFacade");
		remoteWorktype = (RunCWorkticketWorktypeFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketWorktypeFacade");

		/** ***************** 弹出窗口 ******************** */
		/** 工作班员工选择窗口 */
		remoteWorkticket = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		/** 获得部门员工 */
		remoteEmpInfo = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");

		/** 动火作业方式选择窗口 */
		remoteFireContent = (RunCWorkticketFireContentFacadeRemote) factory
				.getFacadeRemote("RunCWorkticketFireContentFacade");
		remoteWorkticketFireContent = (WorkticketManager) factory
				.getFacadeRemote("WorkticketManagerImpl");
		
		
	}  
	/**
	 * 查询工作票内容
	 *
	 * @exception Exception
	 */
	public void getContentWorkticketList() throws Exception { 
		String workticketNo = request.getParameter("workticketNo"); 
		PageObject result = new PageObject(); 
			result = remoteManager.findWorkticketContentList(employee.getEnterpriseCode(),
					workticketNo); 
			
		String str ="{\"list\":[],\"totalCount\":0}";
		if(result!=null)
		{
			str=JSONUtil.serialize(result); 
		}
		write(str);
	}

	/**
	 * 获得工作内容
	 *
	 * @exception Exception
	 */
	public void getContentWorkticketByNo() throws Exception {
		// 新建一个对象
		
		String content ="";
		// 获得工作票号
		String workticket = request.getParameter("workticketNo");
		if (!Constants.BLANK_STRING.equals(workticket.trim())) {
			// 如果从前台获得的工作票非空的话，就执行查询操作
			content = contentRemote.getWorkticketContent(workticketNo);
		} 
		write(JSONUtil.serialize(content));
	}

	/**
	 * 获得关键词
	 *
	 * @exception Exception
	 */
	public void getContentAllWithComm() throws Exception {

		// 获得工作票类型编码
		String workticketTypeCode = request.getParameter("workticketTypeCode");
		// 获得标识前后关键字的keyType
		String keyType = request.getParameter("keyType");
		// 执行查询操作
		PageObject result = remoteContentKey.findAllWithComm(
				workticketTypeCode, Constants.ENTERPRISE_CODE, keyType);
		// 获得查询结果的list
		List<RunCWorkticketContentKey> workticketTypes = result.getList();

		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("[");
		// 把查询结果里的工作编码和工作内容取出
		for (RunCWorkticketContentKey model : workticketTypes) {
			i++;
			sb.append("{\"").append("contentKeyId").append("\":\"").append(
					model.getContentKeyId()).append("\",\"").append(
					"contentKeyName").append("\":\"").append(
					model.getContentKeyName()).append("\"}");
			if (i < workticketTypes.size()) {
				sb.append(",");
			}
		}
		sb.append("]");
		// 返回给前台
		write(sb.toString());
	}

	/**
	 * 获得区域名称和编码
	 *
	 * @exception Exception
	 */
	public void getContentAreaContent() throws Exception {

		/** 设置工作票编号 */
		String blockCode = request.getParameter("blockCode");
		String fuzzy = request.getParameter("fuzzy");
		if(fuzzy== null || fuzzy.length() <1){
			fuzzy = "%";
		}
		// 执行查询操作
		PageObject result = remoteLocation.findAll(Constants.ENTERPRISE_CODE,
				blockCode,fuzzy);
		// 把区域名称和区域编码返回前台
		write(JSONUtil.serialize(result));
	}

	/**
	 * 获得工作类型
	 *
	 * @exception Exception
	 *
	 */
	public void getContentWorktypeName() throws Exception {

		// workticketTypeCode :工作票类型编码（tab1页中所选择的工作票类型）
		String workticketTypeCode = request
				.getParameter("workticketTypeCode");
		// 执行查询操作
		List<RunCWorkticketWorktype> list = remoteWorktype
				.findByWorkticketTypeCode(Constants.ENTERPRISE_CODE,
						workticketTypeCode);

		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("[");
		// 把list里的工作类型id和工作类型名取出
		for (RunCWorkticketWorktype model : list) {
			i++;
			sb.append("{\"").append("worktypeId").append("\":\"").append(
					model.getworktypeId()).append("\",\"").append(
					"worktypeName").append("\":\"").append(
					model.getworktypeName()).append("\"}");
			if (i < list.size()) {
				sb.append(",");
			}
		}
		sb.append("]");
		// 返回前台
		write(sb.toString());
	}

	/**
	 * 获得标点符号
	 *
	 * @exception Exception
	 */
	public void getContentFlagId() throws Exception {

		// %---查询条件（编码或名称)
		List<RunCWorkticketFlag> list = remoteFlag.findByNameOrId(
				Constants.ENTERPRISE_CODE, "%");

		StringBuilder sb = new StringBuilder();
		int i = 0;
		sb.append("[");
		// 取出查询结果的标点ID和标点
		for (RunCWorkticketFlag model : list) {
			i++;
			sb.append("{\"").append("flagId").append("\":\"").append(
					model.getFlagId()).append("\",\"").append("flagName")
					.append("\":\"").append(model.getFlagName()).append("\"}");
			if (i < list.size()) {
				sb.append(",");
			}
		}
		sb.append("]");

		write(sb.toString());
	}

	/**
	 * 动火票类别选择
	 *
	 * @exception Exception
	 */
	public void getContentFireList() throws JSONException {

		/** 设置工作票编号 */
		String workticketNo = request.getParameter("workticketNo");
		// 执行查询操作
		PageObject result = remoteManager.findFireContentList(
				Constants.ENTERPRISE_CODE, workticketNo);
		// 获得查询结果里的list
		List<WorkticketFireContent> list = result.getList();

		StringBuilder sb = new StringBuilder();
		if (null != list) {
			int i = 0;
			// 获得查询结果的动火票名称
			for (WorkticketFireContent model : list) {
				i++;
				sb.append(model.getFirecontentName());
				if (i < list.size()) {
					sb.append("、");
				}
			}
		}
		// 返回前台
		write(JSONUtil.serialize(sb.toString()));
	}

	/**
	 * 工作班成员及人数显示
	 *
	 * @exception Exception
	 */
	public void getContentWorkticketMember() throws Exception {

		try {
			/** 设置工作票编号 */
			String workticketNo = request.getParameter("workticketNo");

			// 作票实体对象
			RunJWorktickets entity = remoteManager
					.findWorkticketByNo(workticketNo);

			StringBuilder sb = new StringBuilder();
			// 如果查询结果非空，就返回工作班成员和人数
			if (null != entity) {
				String content = entity.getMembers();
				if (content == null) {
					content = "";
				}
				String count = "";
				if (entity.getMemberCount() != null) {
					count = entity.getMemberCount().toString();
				}
				sb.append("{").append("\"content\"").append(":\"").append(content)
						.append("\",").append("\"count\"").append(":\"").append(
								count).append("\"}");
			} else {
				sb.append(" ");
			}
			// 返回给前台
			write(sb.toString());
		} catch (Exception e) {
			throw e;
		}
	}
	public void modifyContents()
	{
		try {
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String delIds = request.getParameter("deleteRecords");
			if (addOrUpdateRecords != null) {
				List<RunJWorkticketContent> list = parseContent(addOrUpdateRecords);
			//	contentRemote.modifyRecords(list, delIds);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	
	private List<RunJWorkticketContent> parseContent(String records) throws JSONException
	{
		List<RunJWorkticketContent> result = new ArrayList<RunJWorkticketContent>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			RunJWorkticketContent m = this.parseContentModal(map);
			result.add(m);
		}
		return result;
	}
	private RunJWorkticketContent parseContentModal(Map map)
	{
		RunJWorkticketContent model=new RunJWorkticketContent(); 
		Object id= map.get("id");
		Object workticketNo= map.get("workticketNo"); 
		Object worktypeName= map.get("worktypeName");
		Object flagDesc= map.get("flagDesc");
		Object line= map.get("line");
		Object frontKeyDesc= map.get("frontKeyDesc");
		Object locationName= map.get("locationName");  
		Object attributeCode= map.get("attributeCode"); 
		Object equName= map.get("equName"); 
		Object backKeyDesc= map.get("backKeyDesc"); 
		Object isreturn= map.get("isreturn");  
		if(id!=null)
		model.setId(Long.parseLong(id.toString()));
		if(line!=null)
			model.setLine(Long.parseLong(line.toString()));
		model.setWorkticketNo(workticketNo.toString());
		if(worktypeName !=null)
		model.setWorktypeName(worktypeName.toString());
		if(flagDesc !=null)
			model.setFlagDesc(flagDesc.toString());
		if(frontKeyDesc !=null)
			model.setFrontKeyDesc(frontKeyDesc.toString());
		if(locationName !=null)
			model.setLocationName(locationName.toString());
		if(attributeCode !=null)
			model.setAttributeCode(attributeCode.toString());
		if(equName !=null)
				model.setEquName(equName.toString());
		if(backKeyDesc !=null)
			model.setBackKeyDesc(backKeyDesc.toString());
		
		if(isreturn !=null)
			model.setIsreturn(isreturn.toString()); 
		model.setIsUse("Y");
		model.setCreateBy(employee.getWorkerCode());
		model.setCreateDate(new Date());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		return model;
	}

//	/**
//	 * 增加
//	 *delete by fyyang 090328 已不用
//	 * @exception Exception
//	 */
//	public void addContentWorkticketContent() throws Exception {
//
//		if (!"".equals(entity.getEquName()) && null != entity.getEquName()) {
//			// 取得设备编码字符串
//			String strAttributeCodes = entity.getAttributeCode();
//			// 取得设备名称字符串
//			String strEquNames = entity.getEquName();
//
//			// 将ID设置为空值
//			entity.setId(null);
//			// 企业编码
//			entity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
//			// 设置使用
//			entity.setIsUse(Constants.IS_USE_Y);
//			// 设置增加者
//			entity.setCreateBy(employee.getWorkerCode());
//			// 取得当前顺序
//			Long line = entity.getLine();
//			// 顺序加1
//			//entity.setLine(line++);
//
//			if (-1 != strEquNames.indexOf(",")) {
//				// 多选的时候分割
//				String[] strCodes = strAttributeCodes.split(",");
//				String[] strEqus = strEquNames.split(",");
////				// 将ID设置为空值
////				entity.setId(null);
////				// 企业编码
////				entity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
////				// 设置使用
////				entity.setIsUse(Constants.IS_USE_Y);
////				// 设置增加者
////				entity.setCreateBy(employee.getWorkerCode());
////				Long line = entity.getLine();
//				// 增加多条
//				for (int i = 0; i < strCodes.length; i++) {
//					// 设置设备编码
//					entity.setAttributeCode(strCodes[i]);
//					// 设置设备名称
//					entity.setEquName(strEqus[i]);
//					// 排列顺序
//					entity.setLine(line++);
//					// 新增
//					RunJWorkticketContent result = remoteManager
//							.addWorkticketContent(entity);
//				}
//			} else {
//				// 只有单个设备时
//				RunJWorkticketContent result = remoteManager
//						.addWorkticketContent(entity);
//			}
//		}
//		write(Constants.ADD_SUCCESS);
//	}

//	/**
//	 * 更新
//	 *delete by fyyang 090328 已不用
//	 * @exception Exception
//	 */
//	public void updateContentWorkticketContent() throws Exception {
//
//		// 企业编码
//		entity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
//		entity.setIsUse(Constants.IS_USE_Y);
//		entity.setCreateBy(employee.getWorkerCode());
//
//		if (!"".equals(entity.getEquName()) && null != entity.getEquName()) {
//			// 判断设备是否有多个
//			if (-1 != entity.getEquName().indexOf(",")) {
//
//				String[] strEquNames = equName.split(",");
//				String[] strAttributeCodes = attributeCode.split(",");
//
//				for (int i = 0; i < strAttributeCodes.length; i++) {
//
//					// 设备名称
//					entity.setEquName(strEquNames[i]);
//					// 设备功能编码
//					entity.setAttributeCode(strAttributeCodes[i]);
//
//					RunJWorkticketContent result = remoteManager
//							.updateWorkticketContent(entity);
//				}
//			} else {
//				// 只有单个设备时
//				RunJWorkticketContent result = remoteManager
//						.updateWorkticketContent(entity);
//			}
//		}
//		write(Constants.MODIFY_SUCCESS);
//	}
//
//	/**
//	 * 删除
//	 *delete by fyyang 090328 已不用
//	 * @exception Exception
//	 */
//	public void deleteContentWorkticketContent() {
//
//		try {
//			// 批量删除ID
//			String ids = request.getParameter("ids");
//			String[] id = ids.split(",");
//
//			for (int i = 0; i < id.length; i++) {
//				// 执行删除操作
//				remoteManager.deleteWorkticketContent(Long.parseLong(id[i]));
//			}
//
//			write(Constants.DELETE_SUCCESS);
//		} catch (Exception e) {
//			write(Constants.DELETE_FAILURE);
//		}
//	}

	//由终结票生成的数据
	@SuppressWarnings("unchecked")
	public void updateContent() throws Exception {

		//点击由已终结票返回的终结票号
		String lastWorkticketNo = request.getParameter("lastWorkticketNo");
		//工作票号
		String workticketNo = request.getParameter("workticketNo");

		//工作票号
		PageObject result = new PageObject();
		//由工作票号对应的数据
		result = remoteManager.findWorkticketContentList(Constants.ENTERPRISE_CODE,
				workticketNo);
		List<RunJWorkticketContent> list = result.getList();

		//删除工作票号对应的内容
		if (null != list) {
			//
			for (RunJWorkticketContent model : list) {
				remoteManager.deleteWorkticketContent(model.getId());
				}
		}

		//查询已终结票号对应的内容
		PageObject resultLast = new PageObject();
		resultLast = remoteManager.findWorkticketContentList(Constants.ENTERPRISE_CODE,
				lastWorkticketNo);
		List<RunJWorkticketContent> listLast = result.getList();

		if (null != listLast) {
			// 如果已终结票对应的内容是非空的话，就将该内容插入到当前工作票中
			for (RunJWorkticketContent mo : listLast) {
				mo.setWorkticketNo(workticketNo);
				mo.setId(null);
				//插入该数据
				RunJWorkticketContent resultNew = remoteManager
				.addWorkticketContent(mo);
				}
		}
	}
	/** ********************* 弹出窗口 ******************** */

	/** 工作班成员选择窗口 */

	/**
	 * 获得工作班成员列表
	 *
	 * @throws Exception
	 */
	public void getActorList() throws Exception {
		PageObject pobjInfo = new PageObject();
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		// 查询工作条件信息列表
		if (objstart != null && objlimit != null) {
			if (workticketNo != null) {
				int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				pobjInfo = remoteManager.findAllActors(Constants.ENTERPRISE_CODE,
						workticketNo, start, limit);
				// 序列化为JSON对象的字符串形式
				String strInfo = JSONUtil.serialize(pobjInfo);
				// 以HTML方式输出字符串
				write(strInfo);
			}
		} else {
			pobjInfo = remoteManager
					.findAllActors(Constants.ENTERPRISE_CODE, workticketNo);
		}
	}

	/**
	 * 获得工作班记录条数
	 * @throws Exception
	 */
	public void getRecordTotal() throws Exception {
		PageObject pobjInfo = new PageObject();
		pobjInfo = remoteManager
		.findAllActors(Constants.ENTERPRISE_CODE, workticketNo);
		write(pobjInfo.getTotalCount().toString());
	}

	/**
	 * 添加临时工成员
	 */
	public void addFloaterActor() throws Exception {
		try {
			if (null == workticketEntity) {
				workticketEntity = new RunJWorkticketActors();
			}
			workticketEntity.setActorName(actorName);
			workticketEntity.setActorType(actorType);
			workticketEntity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			workticketEntity.setWorkticketNo(workticketNo);

			remoteManager.addWorkticketMember(workticketEntity);
			write(Constants.ADD_SUCCESS);
		} catch (Exception e) {
			write(Constants.ADD_FAILURE);
		}
	}

	/**
	 * 添加正式成员
	 */
	public void addNormalActor() throws Exception {
		try {
			// 实例化成员实体
			if (null == workticketEntity) {
				workticketEntity = new RunJWorkticketActors();
			}
			String[] _ids = ids.split(",");
			String[] _names = names.split(",");

			workticketEntity.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			workticketEntity.setActorDept(actorDept);
			workticketEntity.setActorType(actorType);
			workticketEntity.setWorkticketNo(workticketNo);

			// 若添加若干条记录的中途出现异常能回滚吗
			for (int i = 0; i < _ids.length; i++) {
				workticketEntity.setActorCode(_ids[i]);
				workticketEntity.setActorName(_names[i]);
				remoteWorkticket.addWorkticketMember(workticketEntity);
			}
			write("{success:true,msg:'增加成功：" + names + "'}");
		} catch (Exception e) {
			write(Constants.ADD_FAILURE);
		}
	}

	/**
	 * 删除成员
	 */
	public void deleteActor() throws Exception {
		try {
			// 批量删除
			if (ids != null) {
				remoteWorkticket.deleteMultiMember(ids);
			}// 单个删除
			else if (id != null) {
				remoteWorkticket.deleteWorkticketMember(id);
			}
			write(Constants.DELETE_SUCCESS);
		} catch (Exception e) {
			write("删除失败：未知原因");
		}
	}

	/**
	 * 获得部门的员工列表
	 *
	 * @throws Exception
	 */
	public void getStaffList() throws Exception {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject pobjInfo = new PageObject();
		// 查询工作条件信息列表
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			if (depId != null) {
				String deptId[] = (String[]) depId;
				pobjInfo.setList(remoteEmpInfo.findByDeptId(Long
						.parseLong(deptId[0]), start, limit));
			}
		} else {
			if (depId != null) {
				String deptId[] = (String[]) depId;
				pobjInfo.setList(remoteEmpInfo.findByDeptId(Long
						.parseLong(deptId[0])));
			}
		}
		// 记录总数
		pobjInfo.setTotalCount(new Long(pobjInfo.getList().size()));
		// 序列化为JSON对象的字符串形式
		String strInfo = JSONUtil.serialize(pobjInfo);
		// 以HTML方式输出字符串
		write(strInfo);
	}

	/** 动火作业方式选择窗口 */

	/**
	 * 获得所有动火工作方式列表
	 *
	 * @throws Exception
	 */
	public void getFireContentList() throws Exception {
		PageObject pobjInfo = new PageObject();
		pobjInfo.setList(remoteFireContent.findByNameOrId(Constants.ENTERPRISE_CODE,
				contentLike));
		// 记录总数
		pobjInfo.setTotalCount(new Long(pobjInfo.getList().size()));
		// 序列化为JSON对象的字符串形式
		String strInfo = JSONUtil.serialize(pobjInfo);
		// 以HTML方式输出字符串
		write(strInfo);
	}

	/**
	 * 获得工作票的已选工作方式记录条数
	 * @throws Exception
	 */
	public void getFireRecordTotal() throws Exception {
		PageObject pobjInfo = new PageObject();
		pobjInfo = remoteWorkticketFireContent.findFireContentList(
				Constants.ENTERPRISE_CODE, workticketNo);
		write(pobjInfo.getTotalCount().toString());
	}

	/**
	 * 获得工作票的已选工作方式
	 *
	 * @throws Exception
	 */
	public void getTicketFireContentList() throws Exception {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remoteWorkticketFireContent.findFireContentList(
					Constants.ENTERPRISE_CODE, workticketNo, start, limit);
		} else {
			obj = remoteWorkticketFireContent.findFireContentList(
					Constants.ENTERPRISE_CODE, workticketNo);
		}
		// 序列化为JSON对象的字符串形式
		String strInfo = JSONUtil.serialize(obj);
		// 以HTML方式输出字符串
		write(strInfo);
	}

	/**
	 * 删除工作票工作方式
	 *
	 * @throws Exception
	 */
	public void delFireContent() throws Exception {
		try {
			// 批量删除
			if (ids != null) {
				remoteWorkticketFireContent.deleteMultiFireContent(ids);
			}// 单个删除
			else if (id != null) {
				remoteWorkticketFireContent.deleteWorkticketFireContent(id);
			}
			write(Constants.DELETE_SUCCESS);
		} catch (Exception e) {
			write("删除失败：未知原因");
		}
	}

	/**
	 * 添加工作票工作方式
	 *
	 * @throws Exception
	 */
	public void addFireContent() throws Exception {
		try {
			if (null == entity) {
				entityFireContent = new RunJWorkticketFireContent();
			}
			String[] _ids = ids.split(",");

			entityFireContent.setOrderBy(orderBy);
			entityFireContent.setEnterpriseCode(Constants.ENTERPRISE_CODE);
			entityFireContent.setWorkticketNo(workticketNo);
			// 设置更新者
			entityFireContent.setModifyBy(employee.getWorkerCode());

			for (int i = 0; i < _ids.length; i++) {
				entityFireContent.setFirecontentId(Long.parseLong(_ids[i]));
				remoteWorkticketFireContent
						.saveWorkticketFireContent(entityFireContent);
			}
			write(Constants.ADD_SUCCESS);
		} catch (Exception e) {
			write(Constants.ADD_FAILURE);
		}
	}
	
	/**
	 * 根据部门编码获得部门ID
	 */
	public void getDeptIdByCode()
	{
		String deptCode=request.getParameter("deptCode");
	    HrCDeptFacadeRemote remote=(HrCDeptFacadeRemote)factory.getFacadeRemote("HrCDeptFacade");
	    HrCDept deptModel= remote.getDeptInfoByDeptCode(deptCode);
		write("{success:true,deptId:'"+deptModel.getDeptId()+"',deptName:'"+deptModel.getDeptName()+"'}");
	}

	/**
	 * @return the remoteManager
	 */
	public WorkticketManager getRemote() {
		return remoteManager;
	}

	/**
	 * @param remoteManager
	 *            the remote to set
	 */
	public void setRemote(WorkticketManager remote) {
		this.remoteManager = remote;
	}

	/**
	 * @return the entity
	 */
	public RunJWorkticketContent getEntity() {
		return entity;
	}

	/**
	 * @param entity
	 *            the entity to set
	 */
	public void setEntity(RunJWorkticketContent entity) {
		this.entity = entity;
	}

	/**
	 * @return the equName
	 */
	public String getEquName() {
		return equName;
	}

	/**
	 * @param equName
	 *            the equName to set
	 */
	public void setEquName(String equName) {
		this.equName = equName;
	}

	/**
	 * @return the attributeCode
	 */
	public String getAttributeCode() {
		return attributeCode;
	}

	/**
	 * @param attributeCode
	 *            the attributeCode to set
	 */
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	/**
	 * @return the workticketNo
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}

	/**
	 * @param workticketNo
	 *            the workticketNo to set
	 */
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	/**
	 * @return the actors
	 */
	public WorkticketActors getActors() {
		return actors;
	}

	/**
	 * @param actors
	 *            the actors to set
	 */
	public void setActors(WorkticketActors actors) {
		this.actors = actors;
	}

	/**
	 * @return the workticketEntity
	 */
	public RunJWorkticketActors getWorkticketEntity() {
		return workticketEntity;
	}

	/**
	 * @param workticketEntity
	 *            the workticketEntity to set
	 */
	public void setWorkticketEntity(RunJWorkticketActors workticketEntity) {
		this.workticketEntity = workticketEntity;
	}

	/**
	 * @return the actorName
	 */
	public String getActorName() {
		return actorName;
	}

	/**
	 * @param actorName
	 *            the actorName to set
	 */
	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	/**
	 * @return the actortypename
	 */
	public String getActortypename() {
		return actortypename;
	}

	/**
	 * @param actortypename
	 *            the actortypename to set
	 */
	public void setActortypename(String actortypename) {
		this.actortypename = actortypename;
	}

	/**
	 * @return the actorType
	 */
	public Long getActorType() {
		return actorType;
	}

	/**
	 * @param actorType
	 *            the actorType to set
	 */
	public void setActorType(Long actorType) {
		this.actorType = actorType;
	}

	/**
	 * @return the actorDept
	 */
	public String getActorDept() {
		return actorDept;
	}

	/**
	 * @param actorDept
	 *            the actorDept to set
	 */
	public void setActorDept(String actorDept) {
		this.actorDept = actorDept;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the ids
	 */
	public String getIds() {
		return ids;
	}

	/**
	 * @param ids
	 *            the ids to set
	 */
	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * @return the names
	 */
	public String getNames() {
		return names;
	}

	/**
	 * @param names
	 *            the names to set
	 */
	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * @return the depId
	 */
	public Object getDepId() {
		return depId;
	}

	/**
	 * @param depId
	 *            the depId to set
	 */
	public void setDepId(Object depId) {
		this.depId = depId;
	}

	/**
	 * @return the empInfo
	 */
	public HrJEmpInfo getEmpInfo() {
		return empInfo;
	}

	/**
	 * @param empInfo
	 *            the empInfo to set
	 */
	public void setEmpInfo(HrJEmpInfo empInfo) {
		this.empInfo = empInfo;
	}

	/**
	 * @return the contentLike
	 */
	public String getContentLike() {
		return contentLike;
	}

	/**
	 * @param contentLike
	 *            the contentLike to set
	 */
	public void setContentLike(String contentLike) {
		this.contentLike = contentLike;
	}

	/**
	 * @return the entityFireContent
	 */
	public RunJWorkticketFireContent getEntityFireContent() {
		return entityFireContent;
	}

	/**
	 * @param entityFireContent
	 *            the entityFireContent to set
	 */
	public void setEntityFireContent(RunJWorkticketFireContent entityFireContent) {
		this.entityFireContent = entityFireContent;
	}

	/**
	 * @return the fireContents
	 */
	public WorkticketFireContent getFireContents() {
		return fireContents;
	}

	/**
	 * @param fireContents
	 *            the fireContents to set
	 */
	public void setFireContents(WorkticketFireContent fireContents) {
		this.fireContents = fireContents;
	}

	/**
	 * @return the orderBy
	 */
	public Long getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy
	 *            the orderBy to set
	 */
	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}
}
