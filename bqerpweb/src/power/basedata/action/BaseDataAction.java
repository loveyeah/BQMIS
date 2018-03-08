package power.basedata.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.KeyValue;
import power.ejb.comm.TreeNode;
import power.ejb.commodel.SysCParametersFacadeRemote;
import power.ejb.hr.form.Parameter;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.googlecode.jsonplugin.annotations.JSON;

/**
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BaseDataAction extends AbstractAction {
	BaseDataManager bll = (BaseDataManager) Ejb3Factory.getInstance()
			.getFacadeRemote("BaseDataManagerImpl");  
	SysCParametersFacadeRemote remote=(SysCParametersFacadeRemote) Ejb3Factory.getInstance()
	.getFacadeRemote("SysCParametersFacade");
	
	public void createOpration() {
		try {
			List<String[]> catalog = new ArrayList<String[]>();
			
			String bp = "D:/工作事务/灞桥电厂事务/资料/用户提供的资料/标准操作票/300MW电气操作票/直流系统/UPS/";
			// 路径，设备ID,设备Code
			catalog.add(new String[] { bp+"1号UPS", "1349", "0001080101" });
			catalog.add(new String[] { bp+"2号UPS", "1350", "0001080102" });

			
			for (String[] equ : catalog) {
				String path = equ[0];// request.getParameter("path");
				String equLevelId = equ[1];// request.getParameter("equLevelId");
				String equLevelCode =equ[2];// request.getParameter("equLevelCode");
				try
				{ 
					File file = new File(path);
					ImportExcel ie = new ImportExcel();
					ie.insertToDB(file, Integer.parseInt(equLevelId), equLevelCode);
					System.out.println("导入终止，success!");
					
				}
				catch(Exception exc)
				{
					System.out.println("非常重要error:"+path+"\n"+exc.getMessage());
					continue;
				}
			}
			System.out.println("全部导入完成，success!");
			write("{success:true}");
		} catch (Exception exc) {
			exc.printStackTrace();
			write("{failure:true}");
		}
		
	}
	
	/**
	 * 取得当前Session中的人员信息
	 */
	public void getCurrentSessionEmployee()
	{ 
	    try { 
			write(JSONUtil.serialize(employee));
		} catch (JSONException e) { 
			e.printStackTrace(); 
		}
	}
	
	//add by fyyang 090212
	/** 用户验证 */
	public void workticketApproveCheckUser(){
			// 工号
			String workerCode = request.getParameter("workerCode");
			// 密码
			String loginPwd = request.getParameter("loginPwd");
			boolean blnOK = bll.checkUserRight(employee.getEnterpriseCode(), workerCode, loginPwd);
			write(String.valueOf(blnOK));
	}
	/**
	 * 取得子部门
	 */
	public void getDeptsByPid()
	{ 
		try {
			String pid = request.getParameter("node");
			//modified by kzhang 20100901
			//是否班组过滤
			//不等于null或""时，不进行班组过滤
			String flag = request.getParameter("flag");
			//---------------------------
			if (pid == null || "".equals(pid)) {
				pid = "-1";
			}
			// modify by liuyi 090930 9:00
//			List<TreeNode> list = bll.getDeptsByPid(Long.parseLong(pid));
//			List<TreeNode> list = bll.getDeptsByPid(Long.parseLong(pid),null);
			List<TreeNode> list = bll.getDeptsByPid(Long.parseLong(pid),(flag!=null&&!flag.equals("")?"yes":null));
			if (list != null) {
				super.write(JSONUtil.serialize(list));
			} else {
				super.write("[]");
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			super.write("[]");
		} 
	}
	/**
	 * 部门选人
	 */
	public void getWorkerByDept() { 
		String deptId = request.getParameter("deptId");
		String flag = request.getParameter("flag");
		String notInWorkerCodes = request.getParameter("notInWorkerCodes");
		String queryKey = request.getParameter("queryKey");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject result = null;
		if (start != null && limit != null) {
			//update by sychen 20100818 增加本部门过滤
			if(flag!=null&&flag.equals("deptFilter")&&deptId.equals("")){

				result = bll.getWorkersByDeptId(0L,employee.getDeptId(),flag,queryKey,
						notInWorkerCodes, Integer.parseInt(start), Integer
								.parseInt(limit));
			
			}
			else{
				result = bll.getWorkersByDeptId((deptId == null || ""
						.equals(deptId)) ? 0L : Long.parseLong(deptId),null,flag,queryKey,
						notInWorkerCodes, Integer.parseInt(start), Integer
								.parseInt(limit));
			}
//			result = bll.getWorkersByDeptId((deptId == null || ""
//					.equals(deptId)) ? 0L : Long.parseLong(deptId), queryKey,
//					notInWorkerCodes, Integer.parseInt(start), Integer
//							.parseInt(limit));

			//update by sychen 20100818 end
		} else {
			result = bll.getWorkersByDeptId((deptId == null || ""
					.equals(deptId)) ? 0L : Long.parseLong(deptId),null,flag, queryKey,
					notInWorkerCodes);
		}
		try {
			if (result == null) {
				super.write("{list:[],totalCount:0}");
			} else {
				super.write(JSONUtil.serialize(result));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			super.write("{list:[],totalCount:0}");
		}
	}
	
	public void checkIsDianJianYuan()
	{
		String workerCode = employee.getWorkerCode();
	    boolean b = bll.checkIsDianJianYuan(workerCode);
	    super.write((b?"Y":"N")); 
	}
	
	/**
	 * 取子专业
	 */
	public void getSpecialsByPCode(){
		try{
			String enterpriseCode=request.getParameter("enterpriseCode");
//			String specialCode=request.getParameter("specialCode");
			String specialCode=request.getParameter("node");
			String specialType=request.getParameter("specialType");
			if(specialCode != null && !"".equals(specialCode)){
				List<TreeNode> list=bll.getSpecialsByPCode(enterpriseCode, specialType, specialCode);
				if (list != null) {
					super.write(JSONUtil.serialize(list));
				} else {
					super.write("[]");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			super.write("[]");
		}
	}
	/**
	 * 更新参数值
	 */
	public void updateParamsValue(){
		try{
			String parameterNo=request.getParameter("parmNo");
			String parameterValue=request.getParameter("parmValue");
			bll.updateParamValue(parameterNo, parameterValue);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 查找参数列表
	 */
	public void getParamsList(){
		try{
			List<Parameter> list=bll.getParamsList();
			if(list!=null && list.size()>0){
				super.write(JSONUtil.serialize(list));
				
			} else {
				super.write("[]");
			}
		}catch(Exception e){
			e.printStackTrace();
			super.write("[]");
		}
	}
	
	public void getOriCurrency() throws Exception{
		super.write("{\"data\":\""+bll.getOriCurrency()+"\"}");
	}
	public void getIssueType(){
		super.write("{\"data\":\""+bll.getIssueType()+"\"}");
	}
	
	public void getPamValue(){
		String pamNo=request.getParameter("pamNo");
		Object pamValue=remote.findBypamNo(pamNo, employee.getEnterpriseCode());
		if(pamValue!=null){
			write(pamValue.toString());
		}
	}
	
	/**
	 * 取得计划统计基本数据
	 */
	public void getBpBasicDataByType(){
		try
		{
			String type = request.getParameter("type");
			List<KeyValue> list = bll.getBpBasicDataByType(employee.getEnterpriseCode(), type);
			write(JSONUtil.serialize(list));
		}
		catch(Exception exc)
		{
			write("[]");
			exc.printStackTrace();
		}
	}
	public void findUnitList() throws JSONException{
		String queryKey = request.getParameter("queryKey");
		if(queryKey == null || queryKey.equals(""))
		{
			queryKey = "%";
		}else
		{
			queryKey = "%"+queryKey+"%";
		}
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject result = null;
		if((start!=null && !start.trim().equals("")) && (limit!=null && !limit.trim().equals("")))
		{
			result = bll.findUnitList(queryKey, employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
		}
		else
		{
			result = bll.findUnitList(queryKey, employee.getEnterpriseCode());
		} 
		if(result!=null)
		{
			write(JSONUtil.serialize(result));
		}
		else
		{
			write("{list:[],totalCount:0}");
		}
	}
	
	
	public void getAllItemToCommon() throws JSONException
	{
		String argFuzzy = request.getParameter("argFuzzy");
		String dataTimeType = request.getParameter("dataTimeType");
		String itemType = request.getParameter("itemType");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if(start != null && limit != null)
			pg = bll.getAllItemToCommon(argFuzzy, dataTimeType, itemType, employee.getEnterpriseCode(), 
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = bll.getAllItemToCommon(argFuzzy, dataTimeType, itemType, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	public void findItemTreeToCommon()
	{
		String pid = request.getParameter("pid");
		String dataTimeType = request.getParameter("dataTimeType");
		String itemType = request.getParameter("itemType");
		String searchKey = request.getParameter("searchKey");
		try {
			List<TreeNode> list = bll.findItemTreeToCommon(pid,dataTimeType,itemType, employee
					.getEnterpriseCode(), searchKey);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}
	
	/**
	 * 获得经营指标findOperateItemTreeForSelect
	 * @throws JSONException 
	 */
	public void getOperateItemForSelect() throws JSONException
	{
		String argFuzzy = request.getParameter("argFuzzy");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if(start != null && limit != null)
			pg = bll.getOperateItemForSelect(argFuzzy,employee.getEnterpriseCode(), 
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = bll.getOperateItemForSelect(argFuzzy, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	/**
	 * 获得经营指标树
	 * @throws JSONException 
	 */
	public void findOperateItemTreeForSelect()
	{
		String pid = request.getParameter("pid");
		String searchKey = request.getParameter("searchKey");
		try {
			List<TreeNode> list = bll.findOperateItemTreeForSelect(pid, employee
					.getEnterpriseCode(), searchKey);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}
	/**
	 * 根据部门Id查询一级部门信息（部门Id，部门编码，部门名称）
	 * 参数 部门Id
	 * 无则默认查询当前登录人的一级部门信息
	 */
	public void findFirstLeverDeptDetailByDeptId()throws JSONException
	{
		String deptId=request.getParameter("deptId");
		if (deptId==null||"".equals(deptId)) {
			deptId=employee.getDeptId().toString();
		}
		List list=bll.getFirstLeverDeptByDeptId(deptId);
		if(list.size()>0){
		Object[] objects=(Object[])list.get(0);
			write("{success:true,deptId:'"+objects[0]+"',deptCode:'"+objects[1]+"',deptName:'"+objects[2]+"'}");
		}else{
			write("{success:false}");
		}
	}
}
