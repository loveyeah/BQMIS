/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.planquery.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.components.Else;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.MrpJPlanInquireDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.PurchaseListBean;
import power.ejb.resource.form.CostFromInfo;
import power.ejb.resource.form.MRPArriveCangoDetailInfo;
import power.ejb.resource.form.MRPIssueMaterialDetailInfo;
import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
/**
 * 物料需求计划登记查询
 * 
 * @author zhouxu
 * 
 */
/**
 * @author code_cj
 *
 */
public class PlanQueryAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    protected MrpJPlanRequirementHeadFacadeRemote remoteHead;
    private Long node;
    private Long requirementDetailId;
    protected PurJOrderDetailsFacadeRemote orderDetailsRemote;
    protected PurJArrivalDetailsFacadeRemote arrivalDetailsRemote;
    protected InvJIssueDetailsFacadeRemote issueDetailsRemote;
    protected MrpJPlanInquireDetailFacadeRemote inquireDetailRemote;
    protected HrCDeptFacadeRemote remoteComm;
    protected MrpJPlanRequirementDetailFacadeRemote planDetailRemote;
    public static Map<Long, Date> dtPlanHead = new HashMap<Long, Date>();
    public Long getNode() {
        return node;
    }


    public void setNode(Long node) {
        this.node = node;
    }
   
    public PlanQueryAction() {
        remoteHead = (MrpJPlanRequirementHeadFacadeRemote) factory.getFacadeRemote("MrpJPlanRequirementHeadFacade");
        orderDetailsRemote = (PurJOrderDetailsFacadeRemote) factory.getFacadeRemote("PurJOrderDetailsFacade");
        arrivalDetailsRemote = (PurJArrivalDetailsFacadeRemote) factory.getFacadeRemote("PurJArrivalDetailsFacade");
        issueDetailsRemote = (InvJIssueDetailsFacadeRemote) factory.getFacadeRemote("InvJIssueDetailsFacade");
        inquireDetailRemote = (MrpJPlanInquireDetailFacadeRemote) factory.getFacadeRemote("MrpJPlanInquireDetailFacade");
        planDetailRemote=(MrpJPlanRequirementDetailFacadeRemote)factory.getFacadeRemote("MrpJPlanRequirementDetailFacade");
    }
    //根据条件查询材料领用费用信息
    public void  getCostFrom() throws JSONException
    {
    	  String strDate = request.getParameter("strDate");
    	  String endDate = request.getParameter("endDate");
    	  String outSTime = request.getParameter("outSTime");
    	  String outETime = request.getParameter("outETime");
    	  String deptCode = request.getParameter("deptCode");
    	  String costFrom = request.getParameter("costFrom");
    	  PageObject result = new PageObject();	  
    	  result=remoteHead.getCostFrom(outSTime,outETime,strDate, endDate, deptCode, costFrom);
    	  write(JSONUtil.serialize(result));
    	  
    	
    }
    /**
     * 根据条件获取物料计划登记列表
     * 
     * @throws JSONException
     */
    public void getPlanHeadByFuzzy() throws JSONException {
        /** 计划开始时间 */
        String dateFrom = request.getParameter("dateFrom");
        /** 计划结束时间 */
        String dateTo = request.getParameter("dateTo");
        /** 按月份查询 */
        String monthPlan = request.getParameter("monthPlan");
        /** 按季度查询 */
        String quarterYear = request.getParameter("quarterYear");
        String quarter = request.getParameter("quarter");
        /** 按季度查询 */
        String planOriginalID = request.getParameter("mrOriginal");
        if("6".equals(planOriginalID)) {
        	if("1".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)-1;
        		dateFrom = year.toString() + "-12-26";
        		dateTo = quarterYear + "-03-25";
        	} else if("2".equals(quarter)){
        		dateFrom = quarterYear + "-03-26";
        		dateTo = quarterYear + "-06-25";
        	} else if("3".equals(quarter)) {
        		dateFrom = quarterYear + "-06-26";
        		dateTo = quarterYear + "09-25";
        	} else if("4".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)+1;
        		dateFrom = year.toString() + "-09-26";
        		dateTo = quarterYear + "12-25";
        	}
        } else if("4".equals(planOriginalID)||"5".equals(planOriginalID)) {
        	String year = monthPlan.substring(0, 5);
        	String month = monthPlan.substring(5);
        	dateFrom = year+((Long.valueOf(month)-1) > 9 ? "" : "0") + (Long.valueOf(month)-1)+"-26";
        	dateTo = monthPlan+"-25";
        }
        /** 申请部门 CODE */
        String mrDept = request.getParameter("mrDept");
        /** 申请人 */
        String mrBy = request.getParameter("mrBy");
        /** 物料名称 */
        String maName = request.getParameter("maName");
        /** 物料类型 */
        String maClassNo = request.getParameter("maClassNo");
        /** 开始查询位置 */
        int start = Integer.parseInt(request.getParameter("start"));
        /** 查询行数 */
        int limit = Integer.parseInt(request.getParameter("limit"));
        /** 获取企业编码 */
        String enterpriseCode = employee.getEnterpriseCode();
        //----add by fyyang 090424---审批状态
        Object objStatus=request.getParameter("status");
        String status="";
        if(objStatus!=null)
        {
        	status=objStatus.toString();
        }
        PageObject obj = new PageObject();
        obj = remoteHead.findByFuzzy(enterpriseCode, dateFrom, dateTo, mrDept, mrBy, maName, maClassNo,status,planOriginalID, start, limit);
        // 如果查询list返回结果为空，则替换为长度为0的list
        if (null == obj.getList()) {
            List<Object> list = new ArrayList<Object>();
            obj.setList(list);
        }else {
            MrpJPlanRequirementHead temp = new MrpJPlanRequirementHead();
            List<MrpJPlanRequirementHead> arrlist=obj.getList();
            for(int i=0;i<arrlist.size();i++){
                temp=arrlist.get(i);
                dtPlanHead.put(temp.getRequirementHeadId(), temp.getLastModifiedDate());
            }
        }
        String str = JSONUtil.serialize(obj);
        // 如果查询返回结果为空，则替换为如下返回结果
        if (Constants.BLANK_STRING.equals(str) || null == str) {
            str = "{\"list\":[],\"totalCount\":null}";
        }
        write(str);

    }
    
    /**
     * 根据条件获取物料计划登记列表
     * add by ywliu 20091110
     * @throws JSONException
     */
    public void getPlanHeadByLogin() throws JSONException {
    	//add by fyyang 100113 申请单号
    	String mrPlanNo=request.getParameter("mrPlanNo");
        /** 计划开始时间 */
        String dateFrom = request.getParameter("dateFrom");
        /** 计划结束时间 */
        String dateTo = request.getParameter("dateTo");
        /** 按月份查询 */
        String monthPlan = request.getParameter("monthPlan");
        /** 按季度查询 */
        String quarterYear = request.getParameter("quarterYear");
        String quarter = request.getParameter("quarter");
        /** 按季度查询 */
        String planOriginalID = request.getParameter("mrOriginal");
        if("6".equals(planOriginalID)) {
        	if("1".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)-1;
        		dateFrom = year.toString() + "-12-26";
        		dateTo = quarterYear + "-03-25";
        	} else if("2".equals(quarter)){
        		dateFrom = quarterYear + "-03-26";
        		dateTo = quarterYear + "-06-25";
        	} else if("3".equals(quarter)) {
        		dateFrom = quarterYear + "-06-26";
        		dateTo = quarterYear + "09-25";
        	} else if("4".equals(quarter)) {
        		Long year = Long.valueOf(quarterYear)+1;
        		dateFrom = year.toString() + "-09-26";
        		dateTo = quarterYear + "12-25";
        	}
        } else if("4".equals(planOriginalID)||"5".equals(planOriginalID)) {
        	String year = monthPlan.substring(0, 5);
        	String month = monthPlan.substring(5);
        	dateFrom = year+((Long.valueOf(month)-1) > 9 ? "" : "0") + (Long.valueOf(month)-1)+"-26";
        	dateTo = monthPlan+"-25";
        }
        /** 申请部门 CODE */
        String mrDept = request.getParameter("mrDept");
        /** 物料名称 */
        String maName = request.getParameter("maName");
        /** 物料类型 */
        String maClassNo = request.getParameter("maClassNo");
        /** 开始查询位置 */
        int start = Integer.parseInt(request.getParameter("start"));
        /** 查询行数 */
        int limit = Integer.parseInt(request.getParameter("limit"));
        /** 获取企业编码 */
        String enterpriseCode = employee.getEnterpriseCode();
        //----add by fyyang 090424---审批状态
        Object objStatus=request.getParameter("status");
        String status="";
        if(objStatus!=null)
        {
        	status=objStatus.toString();
        }
        /** 申请人 */
        String mrBy = request.getParameter("mrBy");
        // add by ywliu 20091112  1 为登陆人上报, 2 为登陆人审批, 3为查看所有的
		String queryType = request.getParameter("queryType");
		// add by ywliu 20091113 登陆人
		String observer = request.getParameter("observer");
        PageObject obj = new PageObject();
    	obj = remoteHead.findPlanHeadByLogin(enterpriseCode, dateFrom, dateTo, mrBy, maName, maClassNo,status,planOriginalID, queryType, mrDept, observer, mrPlanNo,start, limit);
       
        // 如果查询list返回结果为空，则替换为长度为0的list
        if (null == obj.getList()) {
            List<Object> list = new ArrayList<Object>();
            obj.setList(list);
        }else {
            MrpJPlanRequirementHead temp = new MrpJPlanRequirementHead();
            List<MrpJPlanRequirementHead> arrlist=obj.getList();
            for(int i=0;i<arrlist.size();i++){
                temp=arrlist.get(i);
                dtPlanHead.put(temp.getRequirementHeadId(), temp.getLastModifiedDate());
            }
        }
        String str = JSONUtil.serialize(obj);
        // 如果查询返回结果为空，则替换为如下返回结果
        if (Constants.BLANK_STRING.equals(str) || null == str) {
            str = "{\"list\":[],\"totalCount\":null}";
        }
        write(str);

    }

    /**
     * 获取物料明细
     * modify by fyyang 090807
     * 增加了flag条件  不传值时表示查询未作废的（is_use='Y'），传值时表示查询已作废和未作废的(is_use=Y or C)
     * @throws JSONException
     */
    public void getMaterialDetail() throws JSONException {
        /** 主表流水号 */
        String headId = request.getParameter("headId");
        String flag=request.getParameter("flag");
        PageObject obj = new PageObject();
        // 如果流水号不为空
        if (!Constants.BLANK_STRING.equals(headId)) {
            /** 根据主表流水号获取明细 */
            obj = remoteHead.getMaterialDetail(employee.getEnterpriseCode(), Long.parseLong(headId),flag);
            /** 如果结果为空，设置空list，以保证gird刷新 */
            if (null == obj.getList()) {
                List<Object> list = new ArrayList<Object>();
                obj.setList(list);
            }
            /** 将结果转换为json格式 */
            String str = JSONUtil.serialize(obj);
            // 如果查询返回结果为空，则替换为如下返回结果
            if (Constants.BLANK_STRING.equals(str) || null == str) {
                str = "{\"list\":[],\"totalCount\":null}";
            }
            // 返回结果
            write(str);
        } else {
            // 失败返回
            write("false");
        }
    }
    
    /**
     * 根据需求计划明细ID查询采购单信息
     * add By ywliu 09/05/05
     * @throws JSONException 
     */
    public void getOrdersByDetailID() throws JSONException {
    	PurchaseListBean model = orderDetailsRemote.findOrdersByDetailID(requirementDetailId);
    	String str = JSONUtil.serialize(model);
    	write(str);
    }
    
    /**
     * 根据需求计划明细ID查询到货单信息
     * add By ywliu 09/05/05
     */
    public void getArrivalListByDetailID() throws JSONException {
    	List<MRPArriveCangoDetailInfo> resultList = arrivalDetailsRemote.findForReceiveGoodsByDetailID(requirementDetailId);
    	String str = JSONUtil.serialize(resultList);
    	write(str);
    }
    
    /**
     * 根据需求计划明细ID查询领料单信息
     * add By ywliu 09/05/05
     */
    public void getIssueDetailByDetailID() throws JSONException {
    	MRPIssueMaterialDetailInfo model = issueDetailsRemote.findIssueDetailByDetailID(requirementDetailId);
    	String str = JSONUtil.serialize(model);
    	write(str);
    }
    
    /**
     * 根据需求计划明细ID查询询价信息
     * add By ywliu 09/05/05
     */
    public void getInquirePriceInfo() throws JSONException {
    	String materialName=request.getParameter("materialName");
		String buyer=request.getParameter("buyer");
		String requirementDetailId = request.getParameter("requirementDetailId");
		/** 开始查询位置 */
        Object objstart = Integer.parseInt(request.getParameter("start"));
        /** 查询行数 */
        Object objLimit = Integer.parseInt(request.getParameter("limit"));
        PageObject obj = new PageObject();
        if(objstart!=null&&objLimit!=null)
        {
        	int start=Integer.parseInt(objstart.toString());
        	int limit=Integer.parseInt(objLimit.toString());
        	obj=inquireDetailRemote.getInquirePriceInfo(materialName, buyer, requirementDetailId, employee.getEnterpriseCode(), start,limit);
        }
        else
        {
        	obj=inquireDetailRemote.getInquirePriceInfo(materialName, buyer, requirementDetailId, employee.getEnterpriseCode());
        }
        
        String str = JSONUtil.serialize(obj);
        write(str);
    }
    
    ///删除
    public void deletePlanRequirement()
    {
    	String entryId=request.getParameter("entryId");
    	String headId=request.getParameter("headId");
    	if(entryId!=null&&!entryId.equals(""))
    	{
    		remoteHead.deletePlanRequirement(Long.parseLong(entryId), Long.parseLong(headId));
    	}
    	else
    	{
    		remoteHead.deletePlanRequirement(null, Long.parseLong(headId));
    	}
    }


	public Long getRequirementDetailId() {
		return requirementDetailId;
	}


	public void setRequirementDetailId(Long requirementDetailId) {
		this.requirementDetailId = requirementDetailId;
	}
	
	// add by liuyi 0091029 需求物资查询
	public void findMaterialDetailsByCond() throws JSONException
	{
		// 需求计划上报日期开始
		String fromDate = request.getParameter("fromDate");
		// 需求计划上报日期至
		String toDate = request.getParameter("toDate");
		// 物资编码
		String materialNo = request.getParameter("materialNo");
		// 物资名称
		String materialName = request.getParameter("materialName");
		// 物资规格
		String specNo = request.getParameter("specNo");
		//需求计划单号
		String needPlanNo = request.getParameter("needPlanNo");
		//需求计划填写人
		String mrBy = request.getParameter("mrBy");
		//需求部门 add by ywliu 20091112  
		String mrDept = request.getParameter("mrDept");
		//采购员
		String buyBy = request.getParameter("buyBy");
		// add by ywliu 20091112  1 为登陆人上报, 2 为登陆人审批, 3为查看所有的
		String queryType = request.getParameter("queryType");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// add by ywliu 20091113 登陆人
		String observer = request.getParameter("observer");
		
		String status = request.getParameter("status");
		
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remoteHead.findMaterialDetailByCond(fromDate,toDate,materialNo,materialName,
					specNo,needPlanNo,mrBy,buyBy,mrDept,queryType,observer,status,
					employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remoteHead.findMaterialDetailByCond(fromDate,toDate,materialNo,materialName,
					specNo,needPlanNo,mrBy,buyBy,mrDept,queryType,observer,status,
					employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	
	public void findMaterialDetailByPurOrIssue() throws JSONException
	{
		
		String orderNo=request.getParameter("orderNo");
		String flag=request.getParameter("flag");
		List<MrpJPlanRequirementDetailInfo> list =planDetailRemote.getMaterialDetailByPurOrIssue(orderNo, flag);
		PageObject obj=new PageObject();
		obj.setList(list);
		obj.setTotalCount(Long.parseLong(list.size()+""));
		 String str = JSONUtil.serialize(obj);
	        write(str);
	}
}
