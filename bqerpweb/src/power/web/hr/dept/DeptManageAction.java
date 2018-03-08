/**
 * 
 */
package power.web.hr.dept;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.naming.NamingException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrCDeptType;
import power.ejb.hr.HrCDeptTypeFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.SysCPowerPlant;
import power.ejb.hr.SysCPowerPlantFacadeRemote;
import power.ejb.hr.dao.HrCDeptDao;
import power.ejb.system.SysCUl;
import power.ejb.system.SysCUlFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class DeptManageAction extends AbstractAction {
	private String node;
	private HrCDept dept; 
//	public void getWorkerByDept(){
//		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
//		.getInstance().getFacadeRemote("HrCDeptFacade");
//		String deptId = request.getParameter("deptId");
//		String notInWorkerCodes = request.getParameter("notInWorkerCodes");
//		String queryKey = request.getParameter("queryKey");
//		String start = request.getParameter("start");
//		String limit = request.getParameter("limit");
////		deptId = "2";
////		if(deptId !=null)
////		{
//			PageObject result = null; 
//			if(start !=null && limit !=null )
//			{
//				result = bll.getWorkersByDeptId((deptId==null||"".equals(deptId))?0L:Long.parseLong(deptId),queryKey, notInWorkerCodes,Integer.parseInt(start),Integer.parseInt(limit));
//			}
//			else
//			{
//				result = bll.getWorkersByDeptId((deptId==null||"".equals(deptId))?0L:Long.parseLong(deptId),queryKey, notInWorkerCodes);
//			}
//			try { 
//				if(result == null)
//				{
//					super.write("{list:[],totalCount:0}");
//				}else
//				{
//					super.write(JSONUtil.serialize(result));
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//				super.write("{list:[],totalCount:0}");
//			}
////		}
////		super.write("[]");
//	}
	
	private boolean isLeafdept(Long pid) throws NamingException {
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> ld = bll.findByPdeptId(pid);
		if (ld != null && ld.size() > 0)
			return false;
		return true;
	}
	private String toDeptTreeJsonStr(List<HrCDept> list) throws Exception{
//		StringBuffer JSONStr = new StringBuffer(); 
//		JSONStr.append("[");
//		String icon="";
//		for(int i=0;i<list.size();i++){
//			HrCDept dept=list.get(i);
//			if(isLeafdept(dept.getDeptId())){
//				icon="file";
//			}else{
//				icon="folder";
//			}
//			JSONStr.append("{\"text\":\"" + dept.getDeptName()+ 
//					"\",\"id\":\"" + dept.getDeptId() + 
//					"\",\"leaf\":" + isLeafdept(dept.getDeptId())+
//					",\"cls\":\"" + icon+ "\"},");	
//		}
//		if (JSONStr.length() > 1) {
//			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
//		}
//		JSONStr.append("]"); 
//		return JSONStr.toString();
		List<TreeNode> arrayList=new ArrayList();
		for(int i=0;i<list.size();i++)
		{
			HrCDept dept=list.get(i);
			TreeNode model=new TreeNode();
			if(this.isLeafdept(dept.getDeptId()))
			{
				model.setCls("file");
			}
			else
			{
				model.setCls("folder");
			}
			model.setText(dept.getDeptName());
			model.setId(dept.getDeptId().toString());
			model.setLeaf(this.isLeafdept(dept.getDeptId()));
			arrayList.add(model);
		}
		 return JSONUtil.serialize(arrayList);
	}
	public void findDeptList() throws Exception{
		HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> deptl=remote.findByPdeptId(Long.parseLong(node));
		write(toDeptTreeJsonStr(deptl));
	}
	public void findDeptById() throws Exception{
		HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCDeptFacade");
		HrCDept dept=remote.findById(Long.parseLong(node));
		HrJEmpInfoFacadeRemote empRemote=(HrJEmpInfoFacadeRemote)Ejb3Factory
		.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		SysCUlFacadeRemote sysRemote=(SysCUlFacadeRemote)Ejb3Factory
		.getInstance().getFacadeRemote("SysCUlFacade");
		SysCUl sysModel=new SysCUl();
		HrJEmpInfo empModel=new HrJEmpInfo();
		String createMan="";
		String modifyMan="";
		if(dept.getCreateBy()!=null)
		{
			sysModel =sysRemote.findById(dept.getCreateBy());
			if(sysModel!=null&&sysModel.getWorkerCode()!=null)
			{
				List<HrJEmpInfo> empList=empRemote.findByEmpCode(sysModel.getWorkerCode());
				if(empList!=null&&empList.size()>0)
				{
					empModel=empList.get(0);
					if(empModel!=null)
					{
						createMan=empModel.getChsName();
					}
				}
			}
			
			
		}
		if(dept.getModifiyBy()!=null)
		{
			sysModel =sysRemote.findById(dept.getModifiyBy());
			if(sysModel!=null&&sysModel.getWorkerCode()!=null)
			{
				List<HrJEmpInfo> empList=empRemote.findByEmpCode(sysModel.getWorkerCode());
				if(empList!=null&&empList.size()>0)
				{
					empModel=empList.get(0);
					if(empModel!=null)
					{
						modifyMan=empModel.getChsName();
					}
				}
			}
		}
		write("{data:"+JSONUtil.serialize(dept)+",createMan:'"+createMan+"',modifyMan:'"+modifyMan+"'}");
	}
	/**
	 * 添加部门
	 * 
	 */
	public void addDept() throws IOException, NamingException, JSONException,CodeRepeatException {
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		HrCDept hrcdept = new HrCDept();
		String pid = request.getParameter("pid");
		if(pid.equals(0)){
			hrcdept.setPdeptCode("00");
		}
		hrcdept.setDeptCode(request.getParameter("deptCode"));
		hrcdept.setDeptName(request.getParameter("deptName"));
		if(request.getParameter("peopleNumber")!=null&&!request.getParameter("peopleNumber").equals(""))
		{
		hrcdept.setPeopleNumber(Long.parseLong(request.getParameter("peopleNumber")));//add by wpzhu 100506
		}else 
			{hrcdept.setPeopleNumber(null);
			
			}
		// add by liuyi 090929 班组
		String flagBanzu = request.getParameter("flagBanzu");
		//部门状态 新 0  老  1   新老共用  3 add by bjxu091219
		String deptStatus = request.getParameter("deptStatus");
		hrcdept.setDeptStatus(deptStatus);
		hrcdept.setIsBanzu(flagBanzu);
		hrcdept.setPdeptId(Long.parseLong(pid));
		if(request.getParameter("deptTypeId").toString() == "")
		{
			hrcdept.setDeptTypeId(Long.parseLong("0"));
		}
		else
		{
			hrcdept.setDeptTypeId(Long.parseLong(request.getParameter("deptTypeId")));
		}
		
		// add by liuyi 090929增加部门级别，部门性质，企业编码
		if(request.getParameter("depFeature") == null ||request.getParameter("depFeature").equals("")
				|| request.getParameter("depFeature").equals("null"))
		{
			hrcdept.setDepFeature(null);
		}
		else 
		{
			hrcdept.setDepFeature(request.getParameter("depFeature"));
		}
		if(request.getParameter("deptLevel") == null ||request.getParameter("deptLevel").equals("")
				|| request.getParameter("deptLevel").equals("null"))
		{
			hrcdept.setDeptLevel(null);
		}
		else 
		{
			hrcdept.setDeptLevel(request.getParameter("deptLevel"));
		}
		hrcdept.setEnterpriseCode(employee.getEnterpriseCode());
		
		hrcdept.setIsUse(request.getParameter("u").equals("启用") ? "Y" : "N");//update by sychen 20100901
//		hrcdept.setIsUse(request.getParameter("u").equals("启用") ? "U" : "D");
		hrcdept.setMemo(request.getParameter("memo"));
		hrcdept.setDeptId(getMaxDeptId());
		if (!request.getParameter("manager").equals(""))
			hrcdept.setManger(Long.parseLong(request.getParameter("manager")));
//		SysVUserLogin userlogin = (SysVUserLogin) request.getSession()
//				.getAttribute("userlogin");
//		if (userlogin != null)
//			hrcdept.setCreateBy(userlogin.getEmpId());
		hrcdept.setCreateBy(employee.getWorkerId());
		hrcdept.setCreateDate(new Date());
		hrcdept.setModifiyBy(employee.getWorkerId());
		hrcdept.setModifiyDate(new Date());
		try {
			bll.save(hrcdept);
			
//			TreeNode model=new TreeNode();
//			model.setId(hrcdept.getDeptId().toString());
//			model.setText(hrcdept.getDeptName());
//			String jsonstr=JSONUtil.serialize(model);
			
//			String jsonstr = "id: " + hrcdept.getDeptId() + ", text: '"
//					+ hrcdept.getDeptName() + "', url: '"
//					+ "getDeptInfo.action?id=" + hrcdept.getDeptId() + "'";
//			jsonstr = "{success: true, " + jsonstr + "}";
			String jsonstr = "id: " + hrcdept.getDeptId() + ", text: '"
			+ hrcdept.getDeptName() + "'";
	       jsonstr = "{success: true, " + jsonstr + "}";
			write(jsonstr);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * 返回部门id最大值
	 * 
	 */
	private long getMaxDeptId() {
		long maxid = 0;
		String sqlstr = String
				.format("select nvl(max(dept_id)+1,1) from hr_c_dept");
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			maxid = Long.parseLong(ob.toString());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			LogUtil.log("hr_c_dept err", Level.INFO, e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("hr_c_dept err", Level.INFO, e);
		}
		return maxid;
	}

	private Long getType(String s) throws NamingException {
		HrCDeptTypeFacadeRemote bll = (HrCDeptTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptTypeFacade");
		List<HrCDeptType> lh = bll.findByDeptTypeName(Long.parseLong(s));
		if (lh != null && lh.size() == 1) {
			return lh.get(0).getDeptTypeId();
		}
		return -1L;
	}

	/**
	 * 修改部门
	 * 
	 */
	public void updateDept() throws NamingException, IOException {
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		
		HrCDept hrcdept=new HrCDept();
		if (!request.getParameter("id").equals(""))
			hrcdept = bll.findById(Long.parseLong(request.getParameter("id")));
		hrcdept.setDeptCode(request.getParameter("deptCode"));
		hrcdept.setDeptName(request.getParameter("deptName"));
		if(request.getParameter("peopleNumber")!=null&&!request.getParameter("peopleNumber").equals(""))
		{
		hrcdept.setPeopleNumber(Long.parseLong(request.getParameter("peopleNumber")));//add by wpzhu 100506
		}else 
			{hrcdept.setPeopleNumber(null);
			
			}
		// add by liuyi 090929 班组
		String flagBanzu = request.getParameter("flagBanzu");
		hrcdept.setIsBanzu(flagBanzu);
		//部门状态 新 0  老  1   新老共用  3 add by bjxu091219
		String deptStatus = request.getParameter("deptStatus");
		hrcdept.setDeptStatus(deptStatus);
		
		hrcdept.setIsUse(request.getParameter("u").equals("启用") ? "Y" : "N");//update by sychen 20100901
//		hrcdept.setIsUse(request.getParameter("u").equals("启用") ? "U" : "D");
		if (!request.getParameter("manager").equals(""))
			hrcdept.setManger(Long.parseLong(request.getParameter("manager")));
		if(!request.getParameter("deptTypeId").equals(""))
		{
			hrcdept.setDeptTypeId(Long.parseLong(request.getParameter("deptTypeId")));
		}
		else
		{
			hrcdept.setDeptTypeId(Long.parseLong("0"));
		}
		
		// add by liuyi 090929增加部门级别，部门性质
		if(request.getParameter("depFeature") == null ||request.getParameter("depFeature").equals("")
				|| request.getParameter("depFeature").equals("null") )
		{
			hrcdept.setDepFeature(null);
		}
		else 
		{
			hrcdept.setDepFeature(request.getParameter("depFeature"));
		}
		if(request.getParameter("deptLevel") == null ||request.getParameter("deptLevel").equals("")
				|| request.getParameter("deptLevel").equals("null"))
		{
			hrcdept.setDeptLevel(null);
		}
		else 
		{
			hrcdept.setDeptLevel(request.getParameter("deptLevel"));
		}
		
		hrcdept.setMemo(request.getParameter("memo"));
		hrcdept.setModifiyBy(employee.getWorkerId());
		hrcdept.setModifiyDate(new Date());
		try {
			bll.update(hrcdept);
			ListRange<MenuTree> lmt = new ListRange<MenuTree>();
			lmt.setMessage("{success: true, msg: \'ok\'}");
			lmt.setSuccess(true);
			String jsonstr = JSONUtil.serialize(lmt);
			write(jsonstr);
		} catch (Exception e) {
			write("{success: false, msg: \'error\'}");
		}
	}

	/**
	 * 删除部门
	 * 
	 */
	public void deleteDept() throws NamingException, IOException, JSONException {
		String str = request.getParameter("id");
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		List<Long> cids = getAllChildrenId(Long.parseLong(str));
		if (cids != null && cids.size() > 0) {
			for (Long l : cids) {
				HrCDept hrcdept = new HrCDept();
				hrcdept.setDeptId(l);
				hrcdept.setIsUse("N");//update by sychen 20100901
//				hrcdept.setIsUse("D");
				try {
					bll.update(hrcdept);
				} catch (Exception e) {
					write("{success: false, msg: \'error\'}");
				}
			}
		}
		HrCDept hrcdept = new HrCDept();
		hrcdept.setDeptId(Long.parseLong(str));
		hrcdept.setIsUse("N");//update by sychen 20100901
//		hrcdept.setIsUse("D");
		try {
			bll.update(hrcdept);
		} catch (Exception e) {
			write("{success: false, msg: \'error\'}");
		}
		write("{success: true, msg: \'ok\'}");
	}

	/**
	 * 取得所有子节点id
	 * 
	 */
	private List<Long> getAllChildrenId(Long pid) throws NamingException,
			IOException, JSONException {
		List<Long> list = new ArrayList<Long>();
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> aryResult = bll.findByPdeptId(pid);
		if (aryResult.size() > 0) {
			for (HrCDept o : aryResult) {
				list.add(o.getDeptId());
				HrCDeptDao hrcddao = new HrCDeptDao();
				if (hrcddao.hasChild(o.getDeptId())) {
					for (Long L : getAllChildrenId(o.getDeptId()))
						list.add(L);
				}
			}
			return list;
		}
		return null;
	}

	public void getSelectedNode() throws NamingException, IOException,
			JSONException {
		String str = request.getParameter("id");
		ListRange<DeptInfo> list = new ListRange<DeptInfo>();
		if (str.indexOf("pp") != -1) {
			str = str.substring(2);
			SysCPowerPlantFacadeRemote bll = (SysCPowerPlantFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("SysCPowerPlantFacade");
			SysCPowerPlant scpp = bll.findById(Long.parseLong(str));
			DeptInfo di = new DeptInfo();
			di.setDeptCode(scpp.getPowerPlantCode());
			di.setDeptName(scpp.getPowerPlantName());
			di.setDeptType("生产部门");
			if (scpp.getManager() != null)
				di.setManager(scpp.getManager() + "");
			di.setNotes(scpp.getMemo());
			di.setIsUse(scpp.getIsUse());
			di.setModifyBy(scpp.getModifiyBy() + "");

			list.getRoot().add(di);
		} else {
			HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("HrCDeptFacade");
			HrCDept hrcdept = bll.findById(Long.parseLong(str));
			DeptInfo di = new DeptInfo();
			di.setDeptCode(hrcdept.getDeptCode());
			di.setDeptName(hrcdept.getDeptName());
			HrCDeptType hrcdepttype = ((HrCDeptTypeFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("HrCDeptTypeFacade"))
					.findById(hrcdept.getDeptTypeId());
			if (hrcdepttype != null)
				di.setDeptType(hrcdepttype.getDeptTypeName());
			if (hrcdept.getManger() != null)
				di.setManager(hrcdept.getManger() + "");
			di.setNotes(hrcdept.getMemo());
			di.setIsUse(hrcdept.getIsUse());
			di.setModifyBy(hrcdept.getModifiyBy() + "");

			list.getRoot().add(di);
		}
		String jsonstr = JSONUtil.serialize(list);
		write(jsonstr);
	}

	/**
	 * @return the node
	 */
	public String getNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}
	public HrCDept getDept() {
		return dept;
	}
	public void setDept(HrCDept dept) {
		this.dept = dept;
	}
}