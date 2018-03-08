/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.workticket.register.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketFlag;
import power.ejb.workticket.RunCWorkticketFlagFacadeRemote;
import power.ejb.workticket.RunCWorkticketPressboard;
import power.ejb.workticket.RunCWorkticketPressboardFacadeRemote;
import power.ejb.workticket.RunCWorkticketSafetyKeyFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketContent;
import power.ejb.workticket.business.RunJWorkticketSafety;
import power.ejb.workticket.business.RunJWorkticketSafetyFacadeRemote;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.business.WorkticketManager;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 工作票登记 工作票安措Action
 * 
 * @author zhangqi,wangyun,zhouxu
 * 
 */
public class WorkticketSafetyAction extends AbstractAction {
    /** serial id */
    private static final long serialVersionUID = 1L;

    /** 标点符号获取 */
    private RunCWorkticketFlagFacadeRemote flagRemote;
    /** 关键词获取 */
    private RunCWorkticketSafetyKeyFacadeRemote keywordRemote;
    /** 远程服务 */
    private WorkticketManager remote;
    /**压板选择服务*/
    private RunCWorkticketPressboardFacadeRemote pressboardRemote;
    private RunJWorkticketsFacadeRemote runJWorkticketRemote;
    private  RunJWorkticketSafetyFacadeRemote safetyRemote;
    /** 安全措施实体对象 */
    private RunJWorkticketSafety safety;
    /** 工作票类型编码 */
    private String workticketTypeCode;
    /** 前置后置关键字标识 */
    private String keyType;
    /** 待修改的安全措施明细记录的id */
    private String safetyDetailId;
    /** 工作票编号 */
    private String workticketNo;
    /** 安措编码 */
    private String safetyCode;

    
    
    /**
     * 构造函数
     */
    public WorkticketSafetyAction() {
        // 标点符号服务初始化
        flagRemote = (RunCWorkticketFlagFacadeRemote) factory
                .getFacadeRemote("RunCWorkticketFlagFacade");
        // 关键词服务初始化
        keywordRemote = (RunCWorkticketSafetyKeyFacadeRemote) factory
                .getFacadeRemote("RunCWorkticketSafetyKeyFacade");
        // 压板
        pressboardRemote = (RunCWorkticketPressboardFacadeRemote) factory
                .getFacadeRemote("RunCWorkticketPressboardFacade");
        // 远程服务
        remote = (WorkticketManager) factory
                .getFacadeRemote("WorkticketManagerImpl");
     // 工作票
      runJWorkticketRemote = (RunJWorkticketsFacadeRemote) factory
      		.getFacadeRemote("RunJWorkticketsFacade");
      safetyRemote=(RunJWorkticketSafetyFacadeRemote)factory.getFacadeRemote("RunJWorkticketSafetyFacade");
    }

    /**
     * 查询一条工作票
     * 
     * @throws JSONException
     */
    public void getReportWorkticket() throws JSONException {
    	String strWorkticketNo = request.getParameter("workticketNo");
    	RunJWorktickets runJWorktickets = 
    		runJWorkticketRemote.findById(strWorkticketNo);
    	write(JSONUtil.serialize(runJWorktickets));
    }
    
//    /**
//     * 获取动火票安措内容
//     * delete by fyyang 090328 已不使用
//     * @throws JSONException
//     */
//    public void getWorkticketSafetyMeasureFire() throws JSONException {
//        /** 获取企业编码 */
//        String enterpriseCode = Constants.ENTERPRISE_CODE;
//        /** 获取开始行数和查询行数 */
//        Object objstart = request.getParameter("start");
//        Object objlimit = request.getParameter("limit");
//        /** 设置工作票编号 */
//        String workticketNo = request.getParameter("workticketNo");
//        String workticketTypeCode=request.getParameter("workticketTypeCode");
//        PageObject obj = new PageObject();
//        if (objstart != null && objlimit != null) {
//            int start = Integer.parseInt(request.getParameter("start"));
//            int limit = Integer.parseInt(request.getParameter("limit"));
//            obj = remote.findSafetyContentList(enterpriseCode, workticketNo,workticketTypeCode,
//                    start, limit);
//        } else {
//            obj = remote.findSafetyContentList(enterpriseCode, workticketNo,workticketTypeCode);
//        }
//        String str = JSONUtil.serialize(obj);
//        write(str);
//    }

//    /**
//     * 修改动火票安措内容
//     * 
//     * @throws CodeRepeatException
//     */
//    public void updateWorkticketSafetyMeasureFire() throws CodeRepeatException {
//        try {
//            /** 设置工作票编号 */
//            String strWorkticketNo = request.getParameter("strWorkticketNo");
//            /** 设置安措编码 */
//            String strSafetyCode = request.getParameter("strSafetyCode");
//            remote.updateFireSafety(strWorkticketNo, strSafetyCode);
//        } catch (Exception e) {
//            write(Constants.ADD_FAILURE);
//        }
//    }

    /**
     * 安措内容列表查询
     * modify by fyyang 090311
     * @throws JSONException
     */
    public void getSafetyContent() throws JSONException {
        String workticketTypeCode=request.getParameter("workticketTypeCode");
        PageObject result = remote.findSafetyContentList(
                Constants.ENTERPRISE_CODE, workticketNo,workticketTypeCode);
        write(JSONUtil.serialize(result));
    }

    /**
     * 安措明细列表查询
     * 
     * @throws JSONException
     */
    public void getSafetyDetail() throws JSONException {
        PageObject result = remote.findSafetyDetailList(
                Constants.ENTERPRISE_CODE, workticketNo, safetyCode);
        write(JSONUtil.serialize(result));
    }
    
    public void getSafetyBy() {
		try {
			RunJWorkticketSafetyFacadeRemote safetyRemote = (RunJWorkticketSafetyFacadeRemote) factory
					.getFacadeRemote("RunJWorkticketSafetyFacade");
			List<RunJWorkticketSafety> list = safetyRemote.getSafetyBy(employee
					.getEnterpriseCode(), workticketNo);
			write(JSONUtil.serialize(list));  
		} catch (Exception exc) {
			exc.printStackTrace();
			write("success:false,msg'" + exc.getMessage() + "'");
		}
	}

    /**
	 * 安全措施内容查询
	 * wzhyan modify 090317
	 * @throws JSONException
	 */
    public void getSafetyContents() throws JSONException {
        String strContents = remote.getSafetyContent(Constants.ENTERPRISE_CODE,
                workticketNo, safetyCode);
        write(JSONUtil.serialize(strContents));
    }

    /**
     * 获取标点符号
     * 
     * @throws JSONException
     */
    public void getFlagIdList() throws JSONException {
        List<RunCWorkticketFlag> result = flagRemote.findByNameOrId(
                Constants.ENTERPRISE_CODE, Constants.ALL_DATA);
        PageObject obj = new PageObject();
        obj.setList(result);
        write(JSONUtil.serialize(obj));
    }

    /**
     * 获取关键词列表
     * 
     * @throws JSONException
     */
    public void getKeywordList() throws JSONException {
        PageObject obj = keywordRemote.findAllWithComm(
                Constants.ENTERPRISE_CODE, workticketTypeCode, keyType);
        write(JSONUtil.serialize(obj));
    }

//    /**
//     * 增加安全措施明细记录，有可能多条
//     * delete by fyyang 090328 
//     */
//    public void addSafetyBaseInfo() {
//        // 如果没有选择前关键词，设空
//        if(safety.getFrontKeyId() == null ){
//            safety.setFrontKeyword(null);
//        }
//        // 如果没有选择后关键词
//        if(safety.getBackKeyId() == null ){
//            safety.setBackKeyword(null);
//        }        
//        // 分割设备名称内容
//        String[] strEquNames = safety.getEquName().split(",");
//        // 分割设备功能编码
//        String[] strAttributeCodes = safety.getAttributeCode().split(",");
//        try {
//            for (int i = 0; i < strEquNames.length; i++) {
//                // 设备名称
//                safety.setEquName(strEquNames[i]);
//                // 设备编码
//                safety.setAttributeCode(strAttributeCodes[i]);
//                // 创建者
//                safety.setCreateBy(employee.getWorkerCode());
//                // 企业编码
//                safety.setEnterpriseCode(Constants.ENTERPRISE_CODE);
//
//                remote.addWorkticketSafetyDetail(safety);
//            }
//            write(Constants.ADD_SUCCESS);
//        } catch (Exception e) {
//            write(Constants.ADD_FAILURE);
//        }
//    }

//    /**
//     * 修改安全措施明细记录
//     * 	delete by fyyang 090328 已不用
//     */
//    public void updateSafetyBaseInfo() {
//        // 查找要修改的记录
//        RunJWorkticketSafety entity = remote.findSafetyDetailById(Long
//                .parseLong(safetyDetailId));
//
//        // 设定修改的值
//        if(safety.getFrontKeyId() != null ){
//            // 前关键词id
//            entity.setFrontKeyId(safety.getFrontKeyId());
//            // 前关键词
//            entity.setFrontKeyword(safety.getFrontKeyword());
//        }
//        
//        // 设备名称
//        entity.setEquName(safety.getEquName());
//        // 设备功能编码
//        entity.setAttributeCode(safety.getAttributeCode());
//        
//        if(safety.getBackKeyId() != null) {
//            // 后关键词id
//            entity.setBackKeyId(safety.getBackKeyId());
//            // 后关键词
//            entity.setBackKeyword(safety.getBackKeyword());
//        }        
//        // 标点
//        entity.setFlagId(safety.getFlagId());
//        // 操作序号
//        entity.setOperationOrder(safety.getOperationOrder());
//
//        try {
//            remote.updateWorkticketSafetyDetail(entity);
//            write(Constants.MODIFY_SUCCESS);
//        } catch (Exception e) {
//            write(Constants.MODIFY_FAILURE);
//        }
//
//    }

//    /**
//     * 删除安全措施明细记录
//     * delete by fyyang 090328
//     */
//    public void deleteSafetyDetail() {
//        String id = request.getParameter("id");
//        String ids[] = id.split(",");
//
//        for (int i = 0; i < ids.length; i++) {
//            if (!"".equals(ids[i])) {
//                remote.deleteWorkticketSafetyDetail(Long.parseLong(ids[i]));
//            }
//        }
//        write(Constants.DELETE_SUCCESS);
//    }

    /**
     * 获得压板代码树（用于选择页面：checkbox是否存在）
     */
    public void getTreeForSelect()
    {
        // 父节点id
        String code="";
        // 单选还是多选
        String method="";
        // 获得参数
        code=request.getParameter("id");
        Object myobj=request.getParameter("method");
        if(myobj!=null)
        {
            method=myobj.toString();
        }
    
        StringBuffer JSONStr = new StringBuffer(); 
        JSONStr.append("[");
        List<RunCWorkticketPressboard> list=    pressboardRemote.findByParentPressboardId(Long.parseLong(code));
        if(list!=null)
        {
             for (int i = 0; i < list.size(); i++)
             {
                 RunCWorkticketPressboard pressboard= list.get(i);
                 // 是否是叶子节点
                 boolean isLeaf ="Y".equals(pressboard.getIsLeaf())?true:false;
                 if(method.equals("many"))
                 {
                //选择多项时树带checkbox
                 JSONStr.append("{id:'"+pressboard.getPressboardId()+"'" +
                         ",text:'"+pressboard.getPressboardCode()+" "+pressboard.getPressboardName()+
                         "',leaf:"+isLeaf+",checked:false},");
             
                 }
                 else
                 {
                     // 不带checkbox
                     JSONStr.append("{id:'"+pressboard.getPressboardId()+
                             "',text:'"+pressboard.getPressboardCode()+" "+
                             pressboard.getPressboardName()+"',leaf:"+isLeaf+"},");
                 }
             }
             if (JSONStr.length() > 1) {
                JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
            }
             
        }    
       JSONStr.append("]");
        write(JSONStr.toString());
    }
    /**
     * 前置后置关键字标识
     * 
     * @return the keyType
     */
    public String getKeyType() {
        return keyType;
    }

    /**
     * 前置后置关键字标识
     * 
     * @param keyType
     *            前置后置关键字标识
     * 
     */
    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    /**
     * 获取安全措施实体对象
     * 
     * @return safety 安全措施实体对象
     */
    public RunJWorkticketSafety getSafety() {
        return safety;
    }

    /**
     * 设置安全措施实体对象
     * 
     * @param safety
     *            安全措施实体对象
     */
    public void setSafety(RunJWorkticketSafety safety) {
        this.safety = safety;
    }

    /**
     * 获取安全措施明细记录的id
     * 
     * @return safetyDetailId 安全措施明细记录的id
     */
    public String getSafetyDetailId() {
        return safetyDetailId;
    }

    /**
     * 设置安全措施明细记录的id
     * 
     * @param safetyDetailId
     *            安全措施明细记录的id
     */
    public void setSafetyDetailId(String safetyDetailId) {
        this.safetyDetailId = safetyDetailId;
    }

    /**
     * 工作票类型编码
     * 
     * @return the workticketTypeCode
     */
    public String getWorkticketTypeCode() {
        return workticketTypeCode;
    }

    /**
     * 工作票类型编码
     * 
     * @param workticketTypeCode
     *            the workticketTypeCode to set
     */
    public void setWorkticketTypeCode(String workticketTypeCode) {
        this.workticketTypeCode = workticketTypeCode;
    }

    /**
     * 获取工作票编号
     * 
     * @return workticketNo
     */
    public String getWorkticketNo() {
        return workticketNo;
    }

    /**
     * 设置工作票编号
     * 
     * @param workticketNo
     */
    public void setWorkticketNo(String workticketNo) {
        this.workticketNo = workticketNo;
    }

    /**
     * 设置安措编码
     * 
     * @return safetyCode
     */
    public String getSafetyCode() {
        return safetyCode;
    }

    /**
     * 设置安措编码
     * 
     * @param safetyCode
     */
    public void setSafetyCode(String safetyCode) {
        this.safetyCode = safetyCode;
    }
    //add by fyyang 090325
    
    /**
     * 安措明细增删改
     */
	public void modifySafety()
	{
		try {
			String addOrUpdateRecords = request.getParameter("addOrUpdateRecords"); 
			String delIds = request.getParameter("deleteRecords");
			if (addOrUpdateRecords != null) {
				List<RunJWorkticketSafety> list = parseContent(addOrUpdateRecords);
				safetyRemote.modifyRecords(list, delIds);
				write("{success:true}");
			} 
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{success:false}");
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<RunJWorkticketSafety> parseContent(String records) throws JSONException
	{
		List<RunJWorkticketSafety> result = new ArrayList<RunJWorkticketSafety>();
		Object object = JSONUtil.deserialize(records);
		List list=(List)object;
		int intLen = list.size();
		for(int i=0;i<intLen;i++){
			Map map=(Map)list.get(i);
			RunJWorkticketSafety m = this.parseContentModal(map);
			result.add(m);
		}
		return result;
	}
	@SuppressWarnings("unchecked")
	private RunJWorkticketSafety parseContentModal(Map map)
	{
		RunJWorkticketSafety model=new RunJWorkticketSafety(); 
		Object id= map.get("id");
		Object myworkticketNo= request.getParameter("workticketNo"); 
		Object safetyCode= request.getParameter("safetyCode");
		Object attributeCode= map.get("attributeCode");
		Object equName= map.get("equName");
//		Object markcardCode= map.get("markcardCode");
//		Object namecardContent= map.get("namecardContent");  
	//	Object frontKeyId= map.get("frontKeyId"); 
		Object frontKeyword= map.get("frontKeyword"); 
//		Object backKeyId= map.get("backKeyId"); 
		Object backKeyword= map.get("backKeyword");  
//		Object flagId= map.get("flagId");  
		Object flagDesc= map.get("flagDesc");  
		Object operationOrder= map.get("operationOrder");  
		if(id!=null&&!id.equals(""))
		 model.setId(Long.parseLong(id.toString()));
		if(myworkticketNo!=null)
		{
			model.setWorkticketNo(myworkticketNo.toString());
		}
		if(safetyCode!=null)
		{
		model.setSafetyCode(safetyCode.toString());
		}
		if(attributeCode!=null)
		{
			model.setAttributeCode(attributeCode.toString());
		}
		if(equName!=null)
		{
			model.setEquName(equName.toString());
		}
	
		if(frontKeyword!=null)
		{
			model.setFrontKeyword(frontKeyword.toString());
		}
		
		if(backKeyword!=null)
		{
			model.setBackKeyword(backKeyword.toString());
		}
		
		if(flagDesc!=null)
		{
			model.setFlagDesc(flagDesc.toString());
		}
		if(operationOrder!=null&&!operationOrder.equals(""))
		{
			model.setOperationOrder(Long.parseLong(operationOrder.toString()));
		}
		model.setIsUse("Y");
		model.setCreateBy(employee.getWorkerCode());
		model.setCreateDate(new Date());
		model.setEnterpriseCode(employee.getEnterpriseCode());
		return model;
	}
    
}