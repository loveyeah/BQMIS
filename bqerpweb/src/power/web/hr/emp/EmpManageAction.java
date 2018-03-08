package power.web.hr.emp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.HrCDept;
import power.ejb.hr.HrCDeptFacadeRemote;
import power.ejb.hr.HrJEmpInfo;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJEmpPhoto;
import power.ejb.hr.HrJEmpPhotoFacadeRemote;
import power.ejb.hr.ca.HrCAttendancedep;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.dao.HrJEmpInfoDao;
import power.ejb.hr.form.EmpInfo;
import power.ejb.hr.form.EmployeeInfo;
import power.ejb.hr.salary.HrCSalaryLevelFacadeRemote;
import power.web.comm.AbstractAction;
import power.ejb.hr.HrCStation;
import power.ejb.hr.HrCStationFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.googlecode.jsonplugin.annotations.JSON;

import power.ejb.hr.HrCEmpTypeFacadeRemote;

import power.ejb.hr.HrCEducationFacadeRemote;

/**
 * @author fyyang
 * 
 * 员工基本信息
 */

@SuppressWarnings("serial")
public class EmpManageAction extends AbstractAction {
	// private String node;
	private EmpInfo empinfo;
	private HrCStation staion;
	private String node;
	private HrCStationFacadeRemote staRemote;
	private HrCEmpTypeFacadeRemote empTypeRemote;
	private HrCEducationFacadeRemote eduRemote;
	private HrJEmpInfoFacadeRemote empRemote ;
	String empTypeName;
	String educationName;
	
	private File photo;
	public EmpManageAction() 
	{
		 empRemote = (HrJEmpInfoFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrJEmpInfoFacade");
	}
	public File getPhoto() {
		return photo;
	}


	public void setPhoto(File photo) {
		this.photo = photo;
	}


	public EmpInfo getEmpinfo() {
		return empinfo;
	}

	
	public void setEmpinfo(EmpInfo empinfo) {
		this.empinfo = empinfo;
	}
	@JSON(serialize=false)
	public void getEmpList() throws Exception {
		GetEmpList();
	}

	public void saveViateAndSociety() {
		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		HrJEmpInfo model = new HrJEmpInfo();
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = bll.findById(id);
		if (!(empinfo.getCurriculumVitae().equals(""))
				|| empinfo.getCurriculumVitae().equals("null")) {
			model.setCurriculumVitae(empinfo.getCurriculumVitae());
		}
		if (!(empinfo.getSocietyInfo().equals(""))
				|| empinfo.getSocietyInfo().equals("null")) {
			model.setSocietyInfo(empinfo.getSocietyInfo());
		}
		bll.update(model);
		String str = "{success: true,msg:'操作成功'}";
		write(str);
	}

	public void empManage() throws Exception {
		String method = request.getParameter("method").toString();
		String needCheck = request.getParameter("needCheck");
		String deptId = request.getParameter("deptId");
		if (method.equals("getlist")) {
			// 获得员工基本信息列表
			GetEmpList();
		} else if (method.equals("test")) {
			// 获得员工照片
			showphoto();
		} else if (method.equals("add")) {
			// 增加员工基本信息tab1中的内容
			AddEmp();
		} else if (method.equals("tab2add")) {
			// 增加员工基本信息tab2中的内容
			Addtab2();
		} else if (method.equals("tab3add")) {
			// 增加员工基本信息tab3中的内容
			Addtab3();
		} else if (method.equals("tab4add")) {
			// 增加员工基本信息tab3中的内容
			Addtab4();
		} else if (method.equals("update")) {
			// 修改员工基本信息tab1中的内容
			UpdateEmp();
		} else if (method.equals("tab4update")) {
			// 修改员工基本信息tab4中的内容
			Addtab4();
		} else if (method.equals("tab2update")) {
			// 修改员工基本信息tab2中的内容
			Addtab2();
		} else if (method.equals("tab3update")) {
			// 修改员工基本信息tab3中的内容
			Addtab3();
		} else if (method.equals("delete")) {
			// 删除一个或多个员工基本信息
			DeleteEmp();
		} else if (method.equals("getdata")) {
			// 获取某个员工的所有信息
			ShowUpdateData();
		} else if (method.equals("getnation")) {
			// 民族
			GetNationData();
		} else if (method.equals("getpolitics")) {
			// 政治面貌
			GetPoliticsData();
		} else if (method.equals("getnativeplace")) {
			// 籍贯
			GetNativePlaceData();
		} else if (method.equals("getemptype")) {
			// 员工类别
			GetEmptypeData();
		} else if (method.equals("gettypeofwork")) {
			// 工种
			GettypeOfWorkData();

		} else if (method.equals("getTechnology")) {
			// 技术职称
			GetTechnologyData();

		} else if (method.equals("getDep")) {
			// 所属部门
			// modify by liuyi 090921 去掉班组
//			GetDeptData();
			String flag=request.getParameter("flag");//如果是角色对应用户页面则查询部门时过滤掉老部门
			                                        //  modify by wpzhu 20100707
			getDeptDataList(flag,needCheck);

		} else if(method.equals("getBanzu")){
			// 班组
			System.out.println("aaaa " + deptId);
			if(deptId != null)
				this.getBanzuList(Long.parseLong(deptId));
		} 
		// add by liuyi 090929 薪级
		else if(method.equals("getSalaryLevelStore")){
			this.getSalaryData();
		}else if (method.equals("getstation")) {
			// 工作岗位
			GetStationData();

		} else if (method.equals("getdegree")) {
			// 学位
			GetDegreeData();

		} else if (method.equals("geteducation")) {
			// 学历
			GetEducationData();

		} else if (method.equals("getunits")) {
			// 协理单位
			GetAssistantManagerUnitsData();

		} else if (method.equals("getStationlevel")) {
			// 岗位级别
			GetStationlevelData();
		} /*else if (method.equals("getDep")) {
			// 所属部门
//			GetDepData();
			getDeptDataList(needCheck);
		}*/ else {
			// 技术等级
			if (method.equals("getTechnologylevel")) {
				GetTechnologylevelData();
			}
		}

	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	@JSON(serialize=false)
	// 获得某个combox的数据源
	public void GetDataForCombox(List list) throws Exception {
		Iterator it = list.iterator();
		String str = "[";
		String id = "";
		String name = "";
		while (it.hasNext()) {
			id = "";
			name = "";
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				id = data[0].toString();
			}
			if (data[1] != null) {
				name = data[1].toString();
			}
			str = str + "{'id':" + id + ",'text':'" + name + "'},";
		}
		if (!str.equals("[")) {
			str = str.substring(0, str.length() - 1);
		}
		str = str + "]";

		// str="[{'id':0,'name':'汉族'},{'id':1,'name':'苗族'}]";

		write(str);
	}

	// 民族
	@JSON(serialize=false)
	public void GetNationData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllNation());

	}

	// 政治面貌
	@JSON(serialize=false)
	public void GetPoliticsData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllPolitics());

	}

	// 籍贯
	@JSON(serialize=false)
	public void GetNativePlaceData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllNativePlace());

	}

	// 员工类别
	@JSON(serialize=false)
	public void GetEmptypeData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllempType());

	}

	// 工种
	@JSON(serialize=false)
	public void GettypeOfWorkData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAlltypeOfWork());

	}

	// 技术职称
	@JSON(serialize=false)
	public void GetTechnologyData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllTechnology());

	}

	// 所属部门
	@JSON(serialize=false)
	public void GetDeptData() throws Exception {
		HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> deptl = remote.findByPdeptId(Long.parseLong(node));
		System.out.println(toDeptTreeJsonStr(deptl));
		write(toDeptTreeJsonStr(deptl));
		// HrJEmpInfoDao dao=new HrJEmpInfoDao();
		// toDeptTreeJsonStr(dao.GetAllDept());
		// GetDataForCombox(dao.GetAllDept());

	}

	// add by liuyi 090921
	@JSON(serialize=false)
	public void getDeptDataList(String flag,String needCheck) throws Exception
	{
		HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCDeptFacade");
		List<HrCDept> deptl = remote.getByParentId(Long.parseLong(node),needCheck,flag);
		write(toDeptTreeJsonStr(deptl));
	}
	
	//add by liuyi 090921
	// 获得具体部门下的班组
	@JSON(serialize=false)
	public void getBanzuList(Long deptId) throws Exception
	{
		HrCDeptFacadeRemote remote = (HrCDeptFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCDeptFacade");
		List deptlist= remote.getBanzuByDept(deptId);
		GetDataForCombox(deptlist);
	}
	private boolean isLeafdept(Long pid) throws NamingException {
		HrCDeptFacadeRemote bll = (HrCDeptFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCDeptFacade");
		// modify by liuyi 090921 去掉下面的班组
//		List<HrCDept> ld = bll.findByPdeptId(pid);
		List<HrCDept> ld = bll.getListByParentId(pid, "yes");
		if (ld != null && ld.size() > 0)
			return false;
		return true;
	}

	private String toDeptTreeJsonStr(List<HrCDept> list) throws Exception {
		// StringBuffer JSONStr = new StringBuffer();
		// JSONStr.append("[");
		// String icon="";
		// for(int i=0;i<list.size();i++){
		// HrCDept dept=list.get(i);
		// if(isLeafdept(dept.getDeptId())){
		// icon="file";
		// }else{
		// icon="folder";
		// }
		// JSONStr.append("{\"text\":\"" + dept.getDeptName()+
		// "\",\"id\":\"" + dept.getDeptId() +
		// "\",\"pcode\":\"" +dept.getPdeptCode()+
		// "\",\"leaf\":" + isLeafdept(dept.getDeptId())+
		// ",\"cls\":\"" + icon+ "\"},");
		// }
		// if (JSONStr.length() > 1) {
		// JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		// }
		// JSONStr.append("]");
		// return JSONStr.toString();
		List<TreeNode> arrayList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			TreeNode model = new TreeNode();
			HrCDept dept = list.get(i);
			if (isLeafdept(dept.getDeptId())) {
				model.setCls("file");
			} else {
				model.setCls("folder");
			}
			model.setText(dept.getDeptName());
			model.setId(dept.getDeptId().toString());
			model.setLeaf(this.isLeafdept(dept.getDeptId()));
			model.setCode(dept.getDeptCode());
			arrayList.add(model);
		}
		return JSONUtil.serialize(arrayList);

	}

	// 薪级 add by liuyi 090929
	@JSON(serialize=false)
	public void getSalaryData() throws Exception
	{
		HrCSalaryLevelFacadeRemote salaryRemote;
		salaryRemote = (HrCSalaryLevelFacadeRemote) factory
			.getFacadeRemote("HrCSalaryLevelFacade");
		List list = salaryRemote.getSalaryLevelList();
		GetDataForCombox(list);
		
	}
	// 工作岗位
	@JSON(serialize=false)
	public void GetStationData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllStation());

	}

	// 学位
	@JSON(serialize=false)
	public void GetDegreeData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllDegree());

	}

	// 学历
	@JSON(serialize=false)
	public void GetEducationData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllEducation());

	}

	// 协理单位
	@JSON(serialize=false)
	public void GetAssistantManagerUnitsData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllAssistantManagerUnits());

	}

	// 岗位级别
	@JSON(serialize=false)
	public void GetStationlevelData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAllStationlevel());

	}

	// 所属部门
	@JSON(serialize=false)
	public void GetDepData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetDep());
	}

	// 技术等级
	@JSON(serialize=false)
	public void GetTechnologylevelData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		GetDataForCombox(dao.GetAlltechnologygrade());

	}
	@JSON(serialize=false)
	public void getEmpbyList() throws JSONException {
		HrJEmpInfoFacadeRemote eremote;
		eremote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		List<HrJEmpInfo> list = eremote.findByDeptId(Long.parseLong(request
				.getParameter("deptId")));
		write(JSONUtil.serialize(list));
	}

	// 获得员工基本信息列表（根据工号、中文名及英文名模糊查询）
	@JSON(serialize=false)
	public void GetEmpList() throws Exception {
		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		String deptId = request.getParameter("deptId");
		Long _deptId = (deptId == null || "".equals(deptId)) ? 0L : Long
				.parseLong(deptId);

		String queryKey = request.getParameter("fuzzy");
		int start = Integer.parseInt(request.getParameter("start"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		PageObject result = null;
		result = bll.getWorkersByDeptId(_deptId, queryKey, start, limit);
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

		// // String fuzzy=request.getParameter("fuzzy");
		// //
		// //
		// //// Object obj=request.getParameter("fuzzy");
		// //// if(obj!=null)
		// //// {
		// //// fuzzy=obj.toString();
		// //// }
		// // int start = Integer.parseInt(request.getParameter("start"));
		// // int limit = Integer.parseInt(request.getParameter("limit"));
		// //
		// //// Object objDept = request.getParameter("deptId");
		// // String deptId = request.getParameter("deptId");
		// // int count = 0;
		// // int end = start + limit;
		// // HrJEmpInfoDao dao=new HrJEmpInfoDao();
		// // List list;
		// // if(deptId != null && deptId != "")
		// // {
		// //// deptId = request.getParameter("deptId");
		// // list=dao.GetEmpListByDeptId(deptId,fuzzy,start+1, end);
		// // count = dao.getEmpCountByDept(deptId,fuzzy);
		// // }
		// // else
		// // {
		// //// list=dao.GetEmpList(fuzzy,start+1, end);
		// //// count = dao.GetEmpCount(fuzzy);
		// // list = null;
		// // }
		// // if(list != null)
		// // {
		// // Iterator it=list.iterator();
		// // List blocklist = new ArrayList();
		// //
		// //
		// // while(it.hasNext())
		// // {
		// // EmpInfo myempinfo=new EmpInfo();
		// // Object[] o =(Object[]) it.next();
		// //
		// // if(!o[0].toString().equals(""))
		// // {
		// // myempinfo.setEmpId(o[0].toString());
		// // }
		// // if(o[1]!=null)
		// // {
		// // myempinfo.setEmpCode(o[1].toString());
		// // }
		// // if(o[2]!=null)
		// // {
		// // myempinfo.setChsName(o[2].toString());
		// // }
		// // if(o[3]!=null)
		// // {
		// //
		// // myempinfo.setSex(o[3].toString());
		// //
		// // }
		// // if(o[4]!=null)
		// // {
		// // myempinfo.setIdentityCard(o[4].toString());
		// // }
		// // if(o[5]!=null)
		// // {
		// //
		// // myempinfo.setGraduateSchool(o[5].toString());
		// // }
		// // if(o[6]!=null)
		// // {
		// // myempinfo.setSpeciality(o[6].toString());
		// // }
		// // if(o[7]!=null)
		// // {
		// // myempinfo.setDeptName(o[7].toString());
		// // }
		// // if(o[8]!=null)
		// // {
		// //
		// // myempinfo.setStationId(o[8].toString());
		// //
		// // }
		// // if(o[9]!=null)
		// // {
		// // myempinfo.setEmpState(o[9].toString());
		// // }
		// // if(o[11]!=null)
		// // {
		// // myempinfo.setDeptId(o[11].toString());
		// // }
		// // if(o[12]!=null)
		// // {
		// // myempinfo.setDeptCode(o[12].toString());
		// // }
		// //
		// // blocklist.add(myempinfo);
		// //
		// // }
		// // String str = "{total :"+count+", root:" +
		// JSONUtil.serialize(blocklist) + "}";
		// // write(str);
		// }

	}

	// （增加时或修改时）修改tab2的信息
	public void Addtab2() throws Exception {
		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		HrJEmpInfo model = new HrJEmpInfo();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = bll.findById(id);

		// tab2
		// 第一列
		// deptId,stationLevel,gradation,stationId,technologyGradeId,missionDate,
		// dimissionDate,workDate,timeCardId,socialInsuranceDate
		if (isNumeric(empinfo.getDeptId())) {
			if (!(empinfo.getDeptId() == null || empinfo.getDeptId().equals("") || empinfo
					.getDeptId().equals("null"))) {
				model.setDeptId(Long.parseLong(empinfo.getDeptId()));
			}
		}
		
		// add by liuyi 090922 增加班组
		if(empinfo.getBanzuId() != null)
		{
			if (isNumeric(empinfo.getBanzuId())) {
				if (!(empinfo.getBanzuId() == null || empinfo.getBanzuId().equals("") || empinfo
						.getBanzuId().equals("null"))) {
					model.setBanzuId(Long.parseLong(empinfo.getBanzuId()));
				}
			}
		}
		// add by liuyi 增加  标准岗级,薪级,执行岗级,考勤部门
		if (!(empinfo.getStationGrade() == null || empinfo.getStationGrade().equals("") || empinfo
				.getStationGrade().equals("null"))) {
			model.setStationGrade(Long.parseLong(empinfo.getStationGrade()));
			//modify by fyyang 091027
			model.setStationLevel(Long.parseLong(empinfo.getStationGrade()));
		}
		if (!(empinfo.getSalaryLevel() == null || empinfo.getSalaryLevel().equals("") || empinfo
				.getSalaryLevel().equals("null"))) {
			model.setSalaryLevel(Long.parseLong(empinfo.getSalaryLevel()));
		}
		if (!(empinfo.getCheckStationGrade() == null || empinfo.getCheckStationGrade().equals("") || empinfo
				.getCheckStationGrade().equals("null"))) {
			model.setCheckStationGrade(Long.parseLong(empinfo.getCheckStationGrade()));
		}
		if (isNumeric(empinfo.getAttendanceDeptId())) {
			if (!(empinfo.getAttendanceDeptId() == null
					|| empinfo.getAttendanceDeptId().equals("")
					|| empinfo.getAttendanceDeptId().equals("null"))){
				model.setAttendanceDeptId(Long.parseLong(empinfo
						.getAttendanceDeptId()));
			}
		}
		if (!(empinfo.getStationLevel() == null ||empinfo.getStationLevel().equals("") || empinfo.getStationLevel()
				.equals("null"))) {
			model.setStationLevel(Long.parseLong(empinfo.getStationLevel()));
		}
		
		
		if (!(empinfo.getGradation().equals("") || empinfo.getGradation()
				.equals("null"))) {
			model.setGradation(Long.parseLong(empinfo.getGradation()));
		}
		if (!(empinfo.getStationId().equals("") || empinfo.getStationId()
				.equals("null"))) {
			model.setStationId(Long.parseLong(empinfo.getStationId()));
		}
		if (!(empinfo.getTechnologyGradeId().equals("") || empinfo
				.getTechnologyGradeId().equals("null"))) {
			model.setTechnologyGradeId(Long.parseLong(empinfo
					.getTechnologyGradeId()));
		}
		if (!(empinfo.getMissionDate().equals("") || empinfo.getMissionDate()
				.equals("null"))) {
			model.setMissionDate(sf.parse(empinfo.getMissionDate()));
		}
		if (!(empinfo.getDimissionDate().equals("") || empinfo
				.getDimissionDate().equals("null"))) {
			model.setDimissionDate(sf.parse(empinfo.getDimissionDate()));
		}
		if (!(empinfo.getWorkDate().equals("") || empinfo.getWorkDate().equals(
				"null"))) {
			model.setWorkDate(sf.parse(empinfo.getWorkDate()));
		}
		if (!empinfo.getTimeCardId().equals("null")) {
			model.setTimeCardId(empinfo.getTimeCardId());
		}
		if (!(empinfo.getSocialInsuranceDate().equals("") || empinfo
				.getSocialInsuranceDate().equals("null"))) {
			model.setSocialInsuranceDate(sf.parse(empinfo
					.getSocialInsuranceDate()));
		}
		//add by drdu 091223 退休日期
		if(!(empinfo.getRetirementDate().equals("") || empinfo.getRetirementDate().equals("null")))
		{
			model.setRetirementDate(sf.parse(empinfo.getRetirementDate()));
		}
		
		// 第二列
		// empStationId,typeOfWorkId,empTypeId,technologyTitlesTypeId,fax,
		// postalcode,familyTel,socialInsuranceId,payCardId,instancyMan
		if (!(empinfo.getEmpStationId().equals("") || empinfo.getEmpStationId()
				.equals("null"))) {
			model.setEmpStationId(Long.parseLong(empinfo.getEmpStationId()));
		}
		if (!(empinfo.getTypeOfWorkId().equals("") || empinfo.getTypeOfWorkId()
				.equals("null"))) {
			model.setTypeOfWorkId(Long.parseLong(empinfo.getTypeOfWorkId()));
		}
		if (!(empinfo.getEmpTypeId().equals("") || empinfo.getEmpTypeId()
				.equals("null"))) {
			model.setEmpTypeId(Long.parseLong(empinfo.getEmpTypeId()));
		}
		if (!(empinfo.getTechnologyTitlesTypeId().equals("") || empinfo
				.getTechnologyTitlesTypeId().equals("null"))) {
			model.setTechnologyTitlesTypeId(Long.parseLong(empinfo
					.getTechnologyTitlesTypeId()));
		}
		if (!empinfo.getFax().equals("null")) {
			model.setFax(empinfo.getFax());
		}
		if (!empinfo.getPostalcode().equals("null")) {
			model.setPostalcode(empinfo.getPostalcode());
		}
		if (!empinfo.getFamilyTel().equals("null")) {
			model.setFamilyTel(empinfo.getFamilyTel());
		}
		if (!empinfo.getSocialInsuranceId().equals("null")) {
			model.setSocialInsuranceId(empinfo.getSocialInsuranceId());
		}
		if (!empinfo.getPayCardId().equals("null")) {
			model.setPayCardId(empinfo.getPayCardId());
		}
		if (!empinfo.getInstancyMan().equals("null")) {
			model.setInstancyMan(empinfo.getInstancyMan());
		}

		model.setCurriculumVitae(empinfo.getCurriculumVitae());

		model.setSocietyInfo(empinfo.getSocietyInfo());

		bll.update(model);
		String str = "{success: true,msg:'增加成功'}";
		write(str);

	}

	public void Addtab4() throws Exception {
		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		HrJEmpInfo model = new HrJEmpInfo();
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = bll.findById(id);
		// System.out.print(empinfo.getCurriculumVitae()+"empinfo.getCurriculumVitae");
		model.setCurriculumVitae(request.getParameter("empinfo.curriculumVitae"));
		model.setSocietyInfo(request.getParameter("empinfo.societyInfo")); 
		bll.update(model);
		String str = "{success: true,msg:'操作成功'}";
		write(str);
	}

	// （增加时或修改时）修改tab3的信息
	public void Addtab3() throws Exception {
		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		HrJEmpInfo model = new HrJEmpInfo();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = bll.findById(id);
		// tab3
		// 第一列
		// graduateSchool,graduateDate,degreeId,empState,
		// isVeteran,qq,orderBy,oneStrongSuit,createBy,lastModifiyBy
		if (!(empinfo.getGraduateDate().equals("") || empinfo.getGraduateDate()
				.equals("null"))) {
			model.setGraduateDate(sf.parse(empinfo.getGraduateDate()));
		}
		if (!empinfo.getGraduateSchool().equals("null")) {
			model.setGraduateSchool(empinfo.getGraduateSchool());
		}
		if (!(empinfo.getDegreeId().equals("") || empinfo.getDegreeId().equals(
				"null"))) {
			model.setDegreeId(Long.parseLong(empinfo.getDegreeId()));
		}
		if (!empinfo.getEmpState().equals("null")) {
			model.setEmpState(empinfo.getEmpState());
		}
		if (!empinfo.getIsVeteran().equals("null")) {
			model.setIsVeteran(empinfo.getIsVeteran());
		}
		if (!empinfo.getQq().equals("null")) {
			model.setQq(empinfo.getQq());
		}
		if (!(empinfo.getOrderBy().equals("") || empinfo.getOrderBy().equals(
				"null"))) {
			model.setOrderBy(Long.parseLong(empinfo.getOrderBy()));
		}
		if (!empinfo.getOneStrongSuit().equals("null")) {
			model.setOneStrongSuit(empinfo.getOneStrongSuit());
		}
		// if(!(empinfo.getCreateBy().equals("")||empinfo.getCreateBy().equals("null")))
		// {
		// model.setCreateBy(Long.parseLong(empinfo.getCreateBy()));
		// }

		// if(!(empinfo.getLastModifiyBy().equals("")||empinfo.getLastModifiyBy().equals("null")))
		// {
		// model.setLastModifiyBy(Long.parseLong(empinfo.getLastModifiyBy()));
		// }
		model.setLastModifiyBy(employee.getWorkerId());
		// 第二列
		// educationId,speciality,assistantManagerUnitsId,isRetired,
		// isBorrow,msn,recommendMan,memo,createDate,lastModifiyDate
		if (!(empinfo.getEducationId().equals("") || empinfo.getEducationId()
				.equals("null"))) {
			model.setEducationId(Long.parseLong(empinfo.getEducationId()));
		}
		if (!empinfo.getSpeciality().equals("null")) {
			model.setSpeciality(empinfo.getSpeciality());
		}
		if (!(empinfo.getAssistantManagerUnitsId().equals("") || empinfo
				.getAssistantManagerUnitsId().equals("null"))) {
			model.setAssistantManagerUnitsId(Long.parseLong(empinfo
					.getAssistantManagerUnitsId()));
		}
		if (!empinfo.getIsRetired().equals("null")) {
			model.setIsRetired(empinfo.getIsRetired());
		}
		if (!empinfo.getIsBorrow().equals("null")) {
			model.setIsBorrow(empinfo.getIsBorrow());
		}
		if (!empinfo.getMsn().equals("null")) {
			model.setMsn(empinfo.getMsn());
		}
		if (!empinfo.getRecommendMan().equals("null")) {
			model.setRecommendMan(empinfo.getRecommendMan());
		}
		if (!empinfo.getMemo().equals("null")) {
			model.setMemo(empinfo.getMemo());
		}
		// if(!(empinfo.getCreateDate().equals("")||empinfo.getCreateDate().equals("null")))
		// {
		// model.setCreateDate(sf.parse(empinfo.getCreateDate()));
		// }

		// if(!(empinfo.getLastModifiyDate().equals("")||empinfo.getLastModifiyDate().equals("null")))
		// {
		//		
		// model.setLastModifiyDate(sf.parse(empinfo.getLastModifiyDate()));
		// }
		// if(!(empinfo.getCurriculumVitae().equals(""))||empinfo.getCurriculumVitae().equals("null"))
		// {
		// model.setCurriculumVitae(empinfo.getCurriculumVitae());
		// }
		// if(!(empinfo.getSocietyInfo().equals(""))||empinfo.getSocietyInfo().equals("null"))
		// {
		// model.setSocietyInfo(empinfo.getSocietyInfo());
		// }
		model.setLastModifiyDate(new java.util.Date());
		bll.update(model);
		String str = "{success: true,msg:'操作成功'}";
		write(str);

	}

	// 增加员工基本信息tab1的信息
	public void AddEmp() throws Exception {

		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		if (dao.HasEmpCode(empinfo.getEmpCode())) {
			String str = "{success: true,id:'0'}";
			write(str);

		} else {
			HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("HrJEmpInfoFacade");
			long id = dao.GetMaxEmpId();
			// 增加照片
//			String filename = request.getParameter("file");
//			if (!filename.equals("")) {
//				photo(id, filename);
//			}
			if (photo!=null) {
				photo(id, photo);
			}

			HrJEmpInfo model = new HrJEmpInfo();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			// tab1
			// empId,chsName,enName,brithday,nationId,
			// politicsId,sex,mobilePhone,officeTel1,officeTel2,identityCard
			// 第一列
			model.setEmpId(id);
			model.setChsName(empinfo.getChsName());
			model.setEnName(empinfo.getEnName());
			if (!empinfo.getBrithday().equals("")) {
				model.setBrithday(sf.parse(empinfo.getBrithday()));
			}
			if (!empinfo.getNationId().equals("")) {
				model.setNationId(Long.parseLong(empinfo.getNationId()));
			}
			if (!empinfo.getPoliticsId().equals("")) {
				model.setPoliticsId(Long.parseLong(empinfo.getPoliticsId()));
			}
			model.setSex(empinfo.getSex());
			model.setMobilePhone(empinfo.getMobilePhone());
			model.setOfficeTel1(empinfo.getOfficeTel1());
			model.setOfficeTel2(empinfo.getOfficeTel2());
			model.setIdentityCard(empinfo.getIdentityCard());
			// 第二列
			// empCode,archivesId,retrieveCode,nativePlaceId,
			// isWedded,photo,instancyTel,familyAddress,EMail
			model.setEmpCode(empinfo.getEmpCode());
			model.setArchivesId(empinfo.getArchivesId());
			model.setRetrieveCode(empinfo.getRetrieveCode());
			if (!(empinfo.getNativePlaceId().equals("") || (empinfo
					.getNativePlaceId().equals("null")))) {
				model.setNativePlaceId(Long.parseLong(empinfo
						.getNativePlaceId()));
			}
			model.setIsWedded(empinfo.getIsWedded());
			model.setInstancyTel(empinfo.getInstancyTel());
			model.setFamilyAddress(empinfo.getFamilyAddress());
			model.setEMail(empinfo.getEMail());
			model.setCreateBy(employee.getWorkerId());
			model.setCreateDate(new java.util.Date());
			model.setIsUse("Y");
			model.setEnterpriseCode(employee.getEnterpriseCode());
			bll.save(model);
			String str = "{success: true,id:'" + model.getEmpId() + "'}";
			write(str);
		}

	}

	// （增加或修改时）将照片存入数据库
	
	// （增加或修改时）将照片存入数据库
	public void photo(long id, File photoFile) throws Exception {
		HrJEmpPhotoFacadeRemote photobll = (HrJEmpPhotoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpPhotoFacade");
		// filename="C:/test.jpg";
		// File file=new File(filename);
		System.out.println(photoFile.getPath());
		java.io.FileInputStream fis = new java.io.FileInputStream(photoFile);

		byte[] data = new byte[(int) fis.available()];

		fis.read(data);
		HrJEmpPhoto model = new HrJEmpPhoto();
		model = photobll.findById(id);
		if (model == null) {
			model = new HrJEmpPhoto();
			model.setEmpId(id);
			model.setPhoto(data);
			photobll.save(model);
		} else {
			model.setPhoto(data);
			photobll.update(model);
		}
	}
	
	public void photo(long id, String filename) throws Exception {
		HrJEmpPhotoFacadeRemote photobll = (HrJEmpPhotoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpPhotoFacade");
		// filename="C:/test.jpg";
		// File file=new File(filename);
		java.io.FileInputStream fis = new java.io.FileInputStream(filename);

		byte[] data = new byte[(int) fis.available()];

		fis.read(data);
		HrJEmpPhoto model = new HrJEmpPhoto();
		model = photobll.findById(id);
		if (model == null) {
			model = new HrJEmpPhoto();
			model.setEmpId(id);
			model.setPhoto(data);
			photobll.save(model);
		} else {
			model.setPhoto(data);
			photobll.update(model);
		}

	}
	// add by liuyi 090929
	@JSON(serialize=false)
	private Long getEmpId(String workerCode) {
		String sql = " select t.emp_id  from hr_j_emp_info t where t.emp_code=? and rownum = 1";
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
				.getInstance().getFacadeRemote("NativeSqlHelper");
		Object o = bll.getSingal(sql, new Object[] { workerCode });
		if (o != null) {
			return Long.parseLong(o.toString());
		}
		return null;
	}

	// 显示照片
	public void showphoto() throws Exception {
		HrJEmpPhotoFacadeRemote photobll = (HrJEmpPhotoFacadeRemote) factory
				.getFacadeRemote("HrJEmpPhotoFacade");
		HrJEmpPhoto model = new HrJEmpPhoto();
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		model = photobll.findById(id);
		if (model != null) {
			byte[] data = model.getPhoto();
			response.setContentType("image/jpeg");
			OutputStream outs = response.getOutputStream();
			for (int i = 0; i < data.length; i++) {
				outs.write(data[i]);// 输出到页面
			}
			outs.close();
		} else {
			write("http://localhost:8080/power/comm/images/UnknowBody.jpg");
		}
	}

	// 修改tab1（员工基本信息）
	public void UpdateEmp() throws Exception {
		long id = 0;
		id = Long.parseLong(empinfo.getEmpId());

		// 修改图片
//		String filename = request.getParameter("file");
//		if (!filename.equals("")) {
//			photo(id, filename);
//		}
		if (photo!=null) {
			photo(id, photo);
		}
		
		HrJEmpInfoDao dao = new HrJEmpInfoDao();

		if (dao.HasEmpCode(empinfo.getEmpCode(), empinfo.getEmpId())) {
			String str = "{success: true,id:'0'}";
			write(str);

		} else {
			HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("HrJEmpInfoFacade");

			HrJEmpInfo model = new HrJEmpInfo();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

			model = bll.findById(id);
			model.setChsName(empinfo.getChsName());
			if (!empinfo.getEnName().equals("null")) {
				model.setEnName(empinfo.getEnName());
			}
			if (!empinfo.getBrithday().equals("null")) {
				if (!empinfo.getBrithday().equals("")) {
					model.setBrithday(sf.parse(empinfo.getBrithday()));
				}
			}

			if (!empinfo.getNationId().equals("null")) {
				if (!empinfo.getNationId().equals("")) {
					model.setNationId(Long.parseLong(empinfo.getNationId()));
				}
			}

			if (!empinfo.getPoliticsId().equals("null")) {
				if (!empinfo.getPoliticsId().equals("")) {
					model
							.setPoliticsId(Long.parseLong(empinfo
									.getPoliticsId()));
				}
			}
			if (!empinfo.getSex().equals("null")) {
				model.setSex(empinfo.getSex());
			}
			if (!empinfo.getMobilePhone().equals("null")) {
				model.setMobilePhone(empinfo.getMobilePhone());
			}
			if (!empinfo.getOfficeTel1().equals("null")) {
				model.setOfficeTel1(empinfo.getOfficeTel1());
			}
			if (!empinfo.getOfficeTel2().equals("null")) {
				model.setOfficeTel2(empinfo.getOfficeTel2());
			}
			if (!empinfo.getIdentityCard().equals("null")) {
				model.setIdentityCard(empinfo.getIdentityCard());
			}
			// 第二列
			// empCode,archivesId,retrieveCode,nativePlaceId,
			// isWedded,photo,instancyTel,familyAddress,EMail
			if (!empinfo.getEmpCode().equals("null")) {
				model.setEmpCode(empinfo.getEmpCode());
			}
			if (!empinfo.getArchivesId().equals("null")) {
				model.setArchivesId(empinfo.getArchivesId());
			}
			if (!empinfo.getRetrieveCode().equals("null")) {
				model.setRetrieveCode(empinfo.getRetrieveCode());
			}
			if (!empinfo.getNativePlaceId().equals("null")) {
				if (!empinfo.getNativePlaceId().equals("")) {
					model.setNativePlaceId(Long.parseLong(empinfo
							.getNativePlaceId()));
				}
			}
			if (!empinfo.getIsWedded().equals("null")) {
				model.setIsWedded(empinfo.getIsWedded());
			}
			if (!empinfo.getInstancyTel().equals("null")) {
				model.setInstancyTel(empinfo.getInstancyTel());
			}
			if (!empinfo.getFamilyAddress().equals("null")) {
				model.setFamilyAddress(empinfo.getFamilyAddress());
			}
			if (!empinfo.getEMail().equals("null")) {
				model.setEMail(empinfo.getEMail());
			}
			bll.update(model);
			String str = "{success: true,id:'" + model.getEmpId() + "'}";
			write(str);
		}

	}

	// 显示要修改的人员信息
	public void ShowUpdateData() throws Exception {
		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		String deptname = "";
		// add by liuyi 090922 班组名称
		String banzuName = "";
		// add by liuyi 090922 考勤部门名称
		String  attendanceDeptName= "";
		String stationname = "";
		long id = 0;
		Object obj = request.getParameter("id");
		id = Long.parseLong(obj.toString());
		// 显示图片
		// showphoto(id);

		HrJEmpInfoFacadeRemote bll = (HrJEmpInfoFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		EmpInfo info = new EmpInfo();
		HrJEmpInfo model = new HrJEmpInfo();
		String idStr = request.getParameter("id");
		if ("sessionPerson".equals(idStr)) {
			id = this.getEmpId(employee.getWorkerCode());

		} else {
			id = Long.parseLong(idStr);

		}
		model = bll.findById(id);
		info.setArchivesId(model.getArchivesId());

		if (model.getAssistantManagerUnitsId() != null) {
			if (model.getAssistantManagerUnitsId() != -1) {
				info.setAssistantManagerUnitsId(model
						.getAssistantManagerUnitsId().toString());
			}
		}
		if (model.getCurriculumVitae() != null) {
			info.setCurriculumVitae(model.getCurriculumVitae());
		}
		if (model.getSocietyInfo() != null) {
			info.setSocietyInfo(model.getSocietyInfo());
		}
		if (model.getBrithday() != null) {
			info.setBrithday(model.getBrithday().toString());
		}

		info.setChsName(model.getChsName());
		if (model.getCreateBy() != null) {
			if (model.getCreateBy() != -1) {
				info.setCreateBy(model.getCreateBy().toString());
			}
		}

		if (model.getCreateDate() != null) {
			info.setCreateDate(model.getCreateDate().toString());
		}
		if (model.getDegreeId() != null) {
			if (model.getDegreeId() != -1) {
				info.setDegreeId(model.getDegreeId().toString());
			}
		}
		if (model.getDeptId() != null) {
			if (model.getDeptId() != -1) {
				info.setDeptId(model.getDeptId().toString());
				deptname = dao.GetOneDept(model.getDeptId().toString());
			}
		} else {
			info.setDeptId("0");
			deptname = "";
		}
		// add by liuyi 090922 显示班组名称
		if (model.getBanzuId() != null) {
			if (model.getBanzuId() != -1) {
				info.setBanzuId(model.getBanzuId().toString());
				banzuName = dao.GetOneDept(model.getBanzuId().toString());
			}
		} else {
			info.setBanzuId("");
			banzuName = "";
		}
		
		// add by liuyi 090922 增加  标准岗级,薪级,执行岗级,考勤部门 
		if(model.getStationGrade() != null)
			info.setStationGrade(model.getStationGrade().toString());
		if(model.getSalaryLevel() != null)
			info.setSalaryLevel(model.getSalaryLevel().toString());
		if(model.getCheckStationGrade() != null)
			info.setCheckStationGrade(model.getCheckStationGrade().toString());
		if(model.getAttendanceDeptId() != null)
		{
			info.setAttendanceDeptId(model.getAttendanceDeptId().toString());
			HrCAttendancedepFacadeRemote attendanceRemote = (HrCAttendancedepFacadeRemote) Ejb3Factory
			.getInstance().getFacadeRemote("HrCAttendancedepFacade");
			List<HrCAttendancedep> list = attendanceRemote.findByAttendanceDeptId(model.getAttendanceDeptId());
			attendanceDeptName = list.get(0).getAttendanceDeptName();
		}
		else
		{
			info.setAttendanceDeptId("");
			attendanceDeptName = "";
		}
		
		if (model.getMissionDate() != null) {
			info.setMissionDate(model.getMissionDate().toString());
		}
		if (model.getDimissionDate() != null) {
			info.setDimissionDate(model.getDimissionDate().toString());
		}

		HrCEducationFacadeRemote educationBll = (HrCEducationFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCEducationFacade");
		if (model.getEducationId() != null) {
			if (model.getEducationId() != -1) {
				info.setEducationId(model.getEducationId().toString());
				educationName = educationBll.getEducationName(model.getEducationId());
			}
		}
		info.setEMail(model.getEMail());
		info.setEmpCode(model.getEmpCode());

		if (model.getEmpId() != null) {
			if (model.getEmpId() != -1) {
				info.setEmpId(model.getEmpId().toString());
			}
		}
		info.setEmpState(model.getEmpState());
		info.setEmpState(model.getEmpState());

		HrCStationFacadeRemote stationBll = (HrCStationFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCStationFacade");
		if (model.getEmpStationId() != null) {
			if (model.getEmpStationId() != -1) {
				info.setEmpStationId(model.getEmpStationId().toString());
				// modify by liuyi 090930 页面上不需要
//				stationname = stationBll.getStationName(model.getEmpStationId()).toString();
			}
		}

		HrCEmpTypeFacadeRemote empTypeBll = (HrCEmpTypeFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCEmpTypeFacade");
		if (model.getEmpTypeId() != null) {
			if (model.getEmpTypeId() != -1) {
				info.setEmpTypeId(model.getEmpTypeId().toString());
				empTypeName = empTypeBll.getEmpTypeName(model.getEmpTypeId());
			}
		}
		info.setEnName(model.getEnName());
		info.setFamilyAddress(model.getFamilyAddress());
		info.setFamilyTel(model.getFamilyTel());
		info.setFax(model.getFax());
		if (model.getGradation() != null) {
			if (model.getGradation() != -1) {
				info.setGradation(model.getGradation().toString());
			}
		}

		if (model.getGraduateDate() != null) {
			info.setGraduateDate(model.getGraduateDate().toString());
		}
		info.setGraduateSchool(model.getGraduateSchool());
		info.setIdentityCard(model.getIdentityCard());
		info.setInstancyMan(model.getInstancyMan());
		info.setInstancyTel(model.getInstancyTel());
		info.setIsBorrow(model.getIsBorrow());
		info.setIsRetired(model.getIsRetired());
		info.setIsVeteran(model.getIsVeteran());
		info.setIsWedded(model.getIsWedded());
		if (model.getLastModifiyBy() != null) {
			if (model.getLastModifiyBy() != -1) {
				info.setLastModifiyBy(model.getLastModifiyBy().toString());
			}
		}
		if (model.getLastModifiyDate() != null) {
			info.setLastModifiyDate(model.getLastModifiyDate().toString());
		}
		info.setMemo(model.getMemo());

		if (model.getMissionDate() != null) {
			info.setMissionDate(model.getMissionDate().toString());
		}
		info.setMobilePhone(model.getMobilePhone());
		info.setMsn(model.getMsn());

		if (model.getNationId() != null) {
			if (model.getNationId() != -1) {
				info.setNationId(model.getNationId().toString());
			}
		}

		if (model.getNativePlaceId() != null) {
			if (model.getNativePlaceId() != -1) {
				info.setNativePlaceId(model.getNativePlaceId().toString());
			}
		}
		info.setOfficeTel1(model.getOfficeTel1());
		info.setOfficeTel2(model.getOfficeTel2());
		info.setOneStrongSuit(model.getOneStrongSuit());

		if (model.getOrderBy() != null) {
			if (model.getOrderBy() != -1) {
				info.setOrderBy(model.getOrderBy().toString());
			}
		}
		info.setPayCardId(model.getPayCardId());

		if (model.getPoliticsId() != null) {
			if (model.getPoliticsId() != -1) {
				info.setPoliticsId(model.getPoliticsId().toString());
			}
		}
		info.setPostalcode(model.getPostalcode());
		info.setQq(model.getQq());
		info.setRecommendMan(model.getRecommendMan());
		info.setRetrieveCode(model.getRetrieveCode());
		info.setSex(model.getSex());
		if (model.getSocialInsuranceDate() != null) {
			info.setSocialInsuranceDate(model.getSocialInsuranceDate()
					.toString());
		}
		//add by drdu 091223 退休日期
		if(model.getRetirementDate() != null)
		{
			info.setRetirementDate(model.getRetirementDate().toString());
		}
		info.setSocialInsuranceId(model.getSocialInsuranceId());
		info.setSpeciality(model.getSpeciality());

		if (model.getStationId() != null) {
			if (model.getStationId() != -1) {
				info.setStationId(model.getStationId().toString());
				// modify by liuyi 090930 页面上不需要
//				stationname = stationBll.getStationName(model.getStationId()).toString();
			}
		}

		if (model.getStationLevel() != null) {
			if (model.getStationLevel() != -1) {
				info.setStationLevel(model.getStationLevel().toString());
			}
		}
		if (model.getTechnologyGradeId() != null) {
			if (model.getTechnologyGradeId() != -1) {
				info.setTechnologyGradeId(model.getTechnologyGradeId()
						.toString());
			}
		}

		if (model.getTechnologyTitlesTypeId() != null) {
			if (model.getTechnologyTitlesTypeId() != -1) {
				info.setTechnologyTitlesTypeId(model
						.getTechnologyTitlesTypeId().toString());
			}
		}

		info.setTimeCardId(model.getTimeCardId());

		if (model.getTypeOfWorkId() != null) {
			if (model.getTypeOfWorkId() != -1) {
				info.setTypeOfWorkId(model.getTypeOfWorkId().toString());
			}
		}
		if (model.getWorkDate() != null) {
			info.setWorkDate(model.getWorkDate().toString());
		}
		String str = JSONUtil.serialize(info);
		String detail = "{}";
		if ("sessionPerson".equals(idStr)) {
			EmployeeInfo emp = bll.getEmpInfoDetail(employee.getWorkerCode());
			detail = JSONUtil.serialize(emp);
			// System.out.println(detail);
		}
		write("{success: true,educationname:'"+educationName+"',emptypename:'"+empTypeName+"',stationname:'"+stationname
				+"',deptname:'" + deptname + "',banzuName:'" + banzuName + "',attendanceDeptName:'" + attendanceDeptName + "',data:" + str + "}");
		write("{success: true,deptname:'" + deptname + "',banzuName:'" + banzuName + "',attendanceDeptName:'" + attendanceDeptName + " ',data:" + str+ " ,detail:" + detail + "}");
	}

	// 删除员工基本信息
	public void DeleteEmp() throws Exception {

		HrJEmpInfoDao dao = new HrJEmpInfoDao();
		String ids = request.getParameter("ids");
		String[] empids = ids.split(",");
		for (int i = 0; i < empids.length; i++) {
			if (!empids[i].equals("")) {
				Long empid = Long.parseLong(empids[i]);
				dao.DeleteEmp(empid);
				String str = "{success: true,msg:\'ok\'}";
				write(str);
			}
		}
	}
	
	/**
	 * 退休预警查询
	 * add by drdu 091223
	 * @throws JSONException
	 */
	public void finRetirementDateQueryList() throws JSONException
	{
		HrJEmpInfoFacadeRemote remote = (HrJEmpInfoFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrJEmpInfoFacade");
		
		String retirementDate = request.getParameter("fuzzy");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.finRetirementDateQueryList(retirementDate, employee.getEnterpriseCode(), start,limit);
		} else {
			object = remote.finRetirementDateQueryList(retirementDate, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	//add by wpzhu 20100723--------------------
	public void saveEmpRec() throws ParseException {
		String empId = request.getParameter("empId");
		
		HrJEmpInfo empInfo = empRemote.findById(Long.parseLong(empId));
		
//		empInfo.setChsName(empinfo.getChsName());
		// 所属部门
		if(empinfo.getDeptId()!=null&&!"".equals(empinfo.getDeptId()))
		{
		empInfo.setDeptId(Long.parseLong(empinfo.getDeptId()));
		}
		// 政治面貌
		if(empinfo.getPoliticsId()!=null&&!"".equals(empinfo.getPoliticsId()))
		{
			System.out.println("政治面貌："+empinfo.getPoliticsId());
		empInfo.setPoliticsId(Long.parseLong(empinfo.getPoliticsId()));
		}
		// 出生日期
		SimpleDateFormat  df=new SimpleDateFormat("yyyy-MM-dd");
		if(!"".equals(empinfo.getBrithday())&&empinfo.getBrithday()!=null)
		{
		empInfo.setBrithday(df.parse(empinfo.getBrithday()));
		}
		// 性别
        if(empinfo.getSex()!=null && !"".equals(empinfo.getSex()))
		    empInfo.setSex(empinfo.getSex());
		// 工作岗位
      
        if(!"".equals(empinfo.getStationId())&&empinfo.getStationId()!=null&&!"null".equals(empinfo.getStationId()))
        {
		empInfo.setStationId(Long.parseLong(empinfo.getStationId()));
        }
		// 参加工作时间
        if(empinfo.getWorkDate()!=null&&!"".equals(empinfo.getWorkDate()))
        {
		empInfo.setWorkDate(df.parse(empinfo.getWorkDate()));
        }
		// 进厂时间
        if(empinfo.getMissionDate()!=null&&!"".equals(empinfo.getMissionDate()))
        {
		empInfo.setMissionDate(df.parse(empinfo.getMissionDate()));
        }
        //是否退休 
        if(empinfo.getIsRetired()!=null&&!"".equals(empinfo.getIsRetired()))
        {
        empInfo.setIsRetired(empinfo.getIsRetired());
        }
        //add by kzhang 20100809
        if(empinfo.getIsSpecailTrades()!=null&&!"".equals(empinfo.getIsSpecailTrades()))
        {
        empInfo.setIsSpecialTrades(empinfo.getIsSpecailTrades());
        }
        //-----------------------End
		empInfo.setIsSpecialTrades(empinfo.getIsSpecailTrades());
		empInfo.setIsCadres(empinfo.getIsCardes());

		empRemote.update(empInfo);
		write("{success:true,msg:'修改成功！'}");
	}
	//---------------------------------------------------------------------------
	/**
	 * 退居二线预警查询
	 * add by kzhang 20100806
	 * @throws JSONException
	 */
	public void finRetirementByDeptAndName() throws JSONException
	{
		String deptId = request.getParameter("deptId");
		String empName = request.getParameter("empName");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			System.out.println("deptId:"+deptId+" empName:"+empName+" start:"+start+" limit:"+limit);
			object = empRemote.finRetirementListByDeptAndName(deptId, empName, employee.getEnterpriseCode(), start,limit );
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	/**
	 * 导出退居二线预警信息
	 * @param data
	 * @param fileName
	 */
	@SuppressWarnings("unchecked")
	public void retirementExport() throws FileNotFoundException, JSONException,IOException{
		String deptId=request.getParameter("deptId");
		String empName=request.getParameter("empName");
		System.out.println("deptId:"+deptId+" empName:"+empName);
		String startnum=request.getParameter("start");
		String limitnum=request.getParameter("limit");
		//String type = request.getParameter("type");
		StringBuffer exportData = new StringBuffer();
		String year=Calendar.getInstance().get(Calendar.YEAR)+"";
		System.out.println("YEAR:"+year);
		int start=0;
		int limit=18;
		//if("D".equals(type)){
			exportData.append("<table border=1><tr><td align='center' colspan='13'><b><h1>灞桥热电厂"+year+"年退居二线预警</h1></b></td></tr>" +
					"<tr><th>员工工号</th><th>中文名</th><th>性别</th><th>部门</th><th>岗位</th><th>年龄</th>" +
					"<th>工龄</th><th>是否特殊工种</th><th>是否干部</th><th>退休标准</th><th>出生日期</th><th>参加工作时间</th><th>是否退休</th></tr>");
			
			List dataList = new ArrayList();
			if (startnum!=null&&startnum!=""&&limitnum!=null&&limitnum!="") {
				start=Integer.parseInt(startnum);
				limit=Integer.parseInt(limitnum);
			}
			System.out.println("deptId:"+deptId+" empName:"+empName+" start:"+start+" limit:"+limit);
			empRemote.finRetirementListByDeptAndName(deptId, empName, employee.getEnterpriseCode());
			PageObject object = empRemote.finRetirementListByDeptAndName(deptId, empName, employee.getEnterpriseCode(), start,limit );
			if (object!=null&&object.getList()!=null) {
				List result=object.getList();
				if(result != null)
				{
					for(Object rec: result)
					{
						Object[] recs=(Object[])rec;
						exportData.append("<tr>"); 
						 exportData.append("<td>").append(recs[1]==null?"":recs[1]).append("</td><td>").append(recs[2]==null?"":recs[2])
						 .append("</td><td>").append(recs[3]==null?"":recs[3]).append("</td><td>").append(recs[4]==null?"":recs[4]).append("</td><td>")
						 .append(recs[8]==null?"":recs[8]).append("</td><td>").append(recs[6]==null?"":recs[6]).append("</td><td>")
						 .append(recs[11]==null?"":recs[11]).append("</td><td>").append(recs[12]==null?"":recs[12]).append("</td><td>")
						 .append(recs[13]==null?"":recs[13]).append("</td><td>").append(recs[14]==null?"":recs[14]).append("</td><td>")
						 .append(recs[5]==null?"":recs[5]).append("</td><td>").append(recs[15]==null?"":recs[15]).append("</td><td>")
						 .append(recs[18]==null?"":recs[18]);
						exportData.append("</td></tr>");
					}
				}
			
			
		}
			exportData.append("</table>");
			String temp="灞桥热电厂"+year+"年退居二线预警";
			String filename = new String(temp.getBytes("gbk"), "ISO8859_1");
			this.exportData(exportData.toString(), filename);
			System.out.println("data String is :---"+exportData.toString());
	}
	
	public void exportData(String data,String fileName){
		
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition","inner; filename="+fileName+".xls");
		PrintWriter write = null;
		try {
			write = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		write.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
		 write.write(data); 
		 //response.setContentType("application/vnd.ms-excel;charset=utf-8");
		 
		write.flush();
		write.close();  
	}
	
	
	
	
	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}


	public HrCStation getStaion() {
		return staion;
	}


	public void setStaion(HrCStation staion) {
		this.staion = staion;
	}
}
