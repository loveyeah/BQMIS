package power.ejb.hr.dao;

import java.util.List;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

public class HrJEmpInfoDao {
	
	//获取人员信息列表
	public List GetEmpList(String fuzzy,int start,int end) throws Exception{
		
		String sql=
		"select * from\n" +
		"      (\n" + 
		"       select a.emp_id,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女') sex,a.identity_card,\n" + 
		"       a.graduate_school,a.speciality,b.dept_name,c.station_name,\n" + 
		"       decode(a.emp_state,'U','在册','L','注销','N','离职') state,\n" + 
		"       (row_number() over(ORDER BY a.emp_id)) rb\n" + 
		"       from HR_J_EMP_INFO a,hr_c_dept b,hr_c_station c\n" + 
		"       where a.dept_id=b.dept_id(+) and a.station_id=c.station_id(+)\n" + 
		"       and (a.emp_code like '%"+fuzzy+"%' or a.chs_name like '%"+fuzzy+"%' or a.en_name like '%"+fuzzy+"%')\n" + 
		"      ) tt order by tt.emp_code\n" + 
		"      where tt.rb between  "+start+"  and  "+end;
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
			.getInstance().getFacadeRemote("NativeSqlHelper");
			
			List list=bll.queryByNativeSQL(sql);
			return list;
	
	}
	
//	public int getEmpCountByDept(String deptId,String fuzzy)
//	{
////		try
////		{
////			String sql = "select count(1) from hr_j_emp_info t where t.dept_id=" + deptId;
////			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
////			.getInstance().getFacadeRemote("NativeSqlHelper");
////			return (Integer.parseInt(bll.getSingal(sql).toString()));
////		}
////		catch(RuntimeException re)
////		{
////			throw re;
////		}
//		String pDeptId;
//		List list = getSubDeptId(deptId);
//		String allDept = "in (";
//		while(list != null)
//		{
//			Iterator it = list.iterator();
//			while(it.hasNext())
//			{
//				Object[] o = (Object[]) it.next();
//				pDeptId = o[0].toString(); 
//				allDept += pDeptId;
//				allDept += ",";
//				list = getSubDeptId(pDeptId);
//			}		
//		}
//		if(allDept.length() > 4)
//		{
//			allDept = allDept.substring(0,allDept.length() - 1);
//		}
//		allDept += ")";
//		if(allDept.equals("in ()"))
//		{
//			allDept = "=" + deptId;
//		}
//		if(list == null)
//		{
//			String sql=
//				"select count(*) from hr_j_emp_info t where t.dept_id " + allDept;
//			if("0".equals(deptId))
//			{
//				sql = "select count(*) from hr_j_emp_info t";
//			}
//			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
//			.getInstance().getFacadeRemote("NativeSqlHelper");
//			
//			return (Integer.parseInt(bll.getSingal(sql).toString()));
//		}
//		return 0;
//	}  
//	public List GetEmpListByDeptId(String deptId,String fuzzy,int start,int end) throws Exception
//	{
//		String pDeptId;
//		List list = getSubDeptId(deptId);
//		String allDept = "in (";
//		while(list != null)
//		{
//			Iterator it = list.iterator();
//			while(it.hasNext())
//			{
//				Object[] o = (Object[]) it.next();
//				pDeptId = o[0].toString(); 
//				allDept += pDeptId;
//				allDept += ",";
//				list = getSubDeptId(pDeptId);
//			}		
//		}
//		if(allDept.length() > 4)
//		{
//			allDept = allDept.substring(0,allDept.length() - 1);
//		}
//		allDept += ")";
//		if(allDept.equals("in ()"))
//		{
//			allDept = "=" + deptId;
//		}
//		if(list == null)
//		{
//			String sql=
//				"select * from\n" +
//				"      (\n" + 
//				"       select a.emp_id,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女') sex,a.identity_card,\n" + 
//				"       a.graduate_school,a.speciality,b.dept_name,c.station_name,\n" + 
//				"       decode(a.emp_state,'U','在职','L','注销','N','离职 ') state,\n" + 
//				"       (row_number() over(ORDER BY a.dept_id)) rb,a.dept_id,b.dept_code\n" + 
//				"       from HR_J_EMP_INFO a,hr_c_dept b,hr_c_station c\n" + 
//				"       where a.dept_id " + allDept + 
//				" 		and a.dept_id=b.dept_id(+) and a.station_id=c.station_id(+)\n" + 
//				"       and (a.emp_code like '%"+fuzzy+"%' or a.chs_name like '%"+fuzzy+"%' or a.en_name like '%"+fuzzy+"%')\n" + 
//				"      ) tt\n" + 
//				"      where tt.rb between  "+start+"  and  "+end;
//			if("0".equals(deptId))
//			{
//				sql = "select * from\n" +
//				"      (\n" + 
//				"       select a.emp_id,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女') sex,a.identity_card,\n" + 
//				"       a.graduate_school,a.speciality,b.dept_name,c.station_name,\n" + 
//				"       decode(a.emp_state,'U','在职','L','注销','N','离职 ') state,\n" + 
//				"       (row_number() over(ORDER BY a.dept_id)) rb,a.dept_id,b.dept_code\n" + 
//				"       from HR_J_EMP_INFO a,hr_c_dept b,hr_c_station c\n" + 
//				"       where  \n" +
//				" 		a.dept_id=b.dept_id(+) and a.station_id=c.station_id(+)\n" + 
//				"       and (a.emp_code like '%"+fuzzy+"%' or a.chs_name like '%"+fuzzy+"%' or a.en_name like '%"+fuzzy+"%')\n" + 
//				"      ) tt \n" + 
//				"      where tt.rb between  "+start+"  and  "+end;
//			}
//			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
//			.getInstance().getFacadeRemote("NativeSqlHelper");
//			list=bll.queryByNativeSQL(sql);
//			return list;
//		}
//		return null;
//	}
	
	public List GetEmpListByDept(String deptId,String fuzzy,int start,int end) throws Exception
	{
		try
		{
			String sql=
				"select * from\n" +
				"      (\n" + 
				"       select a.emp_id,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女') sex,a.identity_card,\n" + 
				"       a.graduate_school,a.speciality,b.dept_name,c.station_name,\n" + 
				"       decode(a.emp_state,'U','在职','L','注销','N','离职 ') state,\n" + 
				"       (row_number() over(ORDER BY a.dept_id)) rb,a.dept_id\n" + 
				"       from HR_J_EMP_INFO a,hr_c_dept b,hr_c_station c\n" + 
				"       where a.dept_id =" + deptId + 
				" 		and a.dept_id=b.dept_id(+) and a.station_id=c.station_id(+)\n" + 
				"       and (a.emp_code like '%"+fuzzy+"%' or a.chs_name like '%"+fuzzy+"%' or a.en_name like '%"+fuzzy+"%')\n" + 
				"      ) tt \n" + 
				"      where tt.rb between  "+start+"  and  "+end;
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
			.getInstance().getFacadeRemote("NativeSqlHelper");
			List list=bll.queryByNativeSQL(sql);
			return list;
		}
		catch(RuntimeException re)
		{
			throw re;
		}
	}
	
	//找子部门
	public List getSubDeptId(String pDeptId)
	{
		String sql = 
			"select count(*) from hr_c_dept t where t.pdept_id =" + pDeptId;
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance().getFacadeRemote("NativeSqlHelper");
		Object objPDept = bll.getSingal(sql);
		String strPDept = objPDept.toString();
		if(strPDept == "0")
		{
			return null;
		}
		sql = 
			"select dept_id,dept_name from hr_c_dept\n" +
		"start with dept_id= '" + pDeptId + "'\n" +
		"connect by prior dept_id = pdept_id\n" + 
		"order by dept_id";
		List list = bll.queryByNativeSQL(sql);
		return list;
	}
	
	//获取人员信息记录数
	public int GetEmpCount(String fuzzy) throws Exception{
		
		
		String sql=
			 "select count(*) from HR_J_EMP_INFO a " +
			 " where a.emp_code like '%"+fuzzy+"%' or a.chs_name like '%"+fuzzy+"%' or a.en_name like '%"+fuzzy+"%'";
			

		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		Object count= bll.getSingal(sql);
		return Integer.parseInt(count.toString());
	

}
	//删除一条人员信息
	public void DeleteEmp(long empid) throws Exception{
		

		String sql=
			 "	 delete from HR_J_EMP_INFO a where a.emp_id="+empid;
		String photosql= 
			"	 delete from HR_J_EMP_PHOTO a where a.emp_id="+empid;
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		bll.exeNativeSQL(sql);
		bll.exeNativeSQL(photosql);
		
		
//		HrJEmpInfoFacadeRemote bll=(HrJEmpInfoFacadeRemote)Ejb3Factory
//		.getInstance().getFacadeRemote("HrJEmpInfoFacade");
	
		
	
}
public long GetMaxEmpId() throws Exception{
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		long id = 0;
		String sql="select nvl(max(emp_id)+1,1) from HR_J_EMP_INFO";
		Object obj= bll.getSingal(sql);
		if(obj != null){
			id=Long.parseLong(obj.toString());
		}
		return id;

	
}

public boolean HasEmpCode(String empcode) throws Exception{
	NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
	.getInstance().getFacadeRemote("NativeSqlHelper");
	String sql=
		"select count(*) from hr_j_emp_info t  where t.emp_code='"+empcode+"'";
	  int id= Integer.parseInt(bll.getSingal(sql).toString());
	  if(id==0)
	  {
		  return false;
	  }
	  else
	  {
		  return true;
	  }

}

public boolean HasEmpCode(String empcode,String empid) throws Exception{
	NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
	.getInstance().getFacadeRemote("NativeSqlHelper");
	String sql=
		"select count(*) from hr_j_emp_info t  where t.emp_code='"+empcode+"' and  t.emp_id<>"+empid;
	  int id= Integer.parseInt(bll.getSingal(sql).toString());
	  if(id==0)
	  {
		  return false;
	  }
	  else
	  {
		  return true;
	  }

}



//获得民族
public List GetAllNation() throws Exception{
	String sql="select a.nation_id,a.nation_name from COM_C_NATION a";

	
	NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
	.getInstance().getFacadeRemote("NativeSqlHelper");
	
	List list=bll.queryByNativeSQL(sql);
	return list;
}


// 获得政治面貌
	public List GetAllPolitics() throws Exception{
	String sql="select t.politics_id,t.politics_name from HR_C_POLITICS t where t.is_use='Y'"; //update by sychen 20100902
//	String sql="select t.politics_id,t.politics_name from HR_C_POLITICS t where t.is_use='U'";
	
	NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
	.getInstance().getFacadeRemote("NativeSqlHelper");
	
	List list=bll.queryByNativeSQL(sql);
	return list;
	}
	
	// 获得籍贯
	public List GetAllNativePlace() throws Exception{
	String sql=
		"select t.native_place_id,t.native_place_name  from HR_C_NATIVE_PLACE t where t.is_use='Y' and t.native_place_id like '340%' "; //update by sychen 20100902
//	"select t.native_place_id,t.native_place_name  from HR_C_NATIVE_PLACE t where t.is_use='U' and t.native_place_id like '340%' "; 
	
	
	NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
	.getInstance().getFacadeRemote("NativeSqlHelper");
	
	List list=bll.queryByNativeSQL(sql);
	return list;
	}
	
	//员工类别
	public List GetAllempType() throws Exception{
		String sql=
			"select t.emp_type_id,t.emp_type_name from HR_C_EMP_TYPE t where t.is_use='Y'"; //update by sychen 20100902
//		"select t.emp_type_id,t.emp_type_name from HR_C_EMP_TYPE t where t.is_use='U'";
		
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	//工种
	public List GetAlltypeOfWork() throws Exception{
		String sql=

			"select t.type_of_work_id,t.type_of_work_name from  HR_C_TYPE_OF_WORK t where t.is_use='U'"; 

		
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	//技术职称
	public List GetAllTechnology() throws Exception{
		String sql=
			"select t.technology_titles_id,t.technology_titles_name from HR_C_TECHNOLOGY_TITLES  t\n" +
			"where t.is_use='Y'"; //update by sychen 20100902
//		"where t.is_use='U'"; 

		
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	//所属部门
	public List GetAllDept() throws Exception{
		String sql=
			"select t.dept_id,t.dept_name from HR_C_DEPT t where t.is_use='Y'"; //update by sychen 20100902
//		"select t.dept_id,t.dept_name from HR_C_DEPT t where t.is_use='U'"; 
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	public String GetOneDept(String id) throws Exception{
		String sql=
			"select t.dept_name from HR_C_DEPT t where t.is_use='Y' and t.dept_id="+id;  //update by sychen 20100902
//		"select t.dept_name from HR_C_DEPT t where t.is_use='U' and t.dept_id="+id;
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		String name=id;
		if( bll.getSingal(sql)!=null)
			{
			name=bll.getSingal(sql).toString();
			}
		
		return name;
		
		}
	
	
	//工作岗位
	public List GetAllStation() throws Exception{
		String sql=
			"select t.station_id,t.station_name from HR_C_STATION t where t.is_use='Y'";   //update by sychen 20100902
//		"select t.station_id,t.station_name from HR_C_STATION t where t.is_use='U'";  
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	//学位
	public List GetAllDegree() throws Exception{
		String sql=
			"select k.degree_id,k.degree_name from HR_C_DEGREE k where k.is_use='Y'";  //update by sychen 20100902
//		"select k.degree_id,k.degree_name from HR_C_DEGREE k where k.is_use='U'"; 
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	//学历
	public List GetAllEducation() throws Exception{
		String sql=
			"select m.education_id,m.education_name from HR_C_EDUCATION m where m.is_use='Y'";  //update by sychen 20100902
//		"select m.education_id,m.education_name from HR_C_EDUCATION m where m.is_use='U'"; 
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	
	//协理单位
	public List GetAllAssistantManagerUnits() throws Exception{
		String sql=
			"select kk.assistant_manager_units_id,kk.assistant_manager_units_name\n" +
			" from HR_C_ASSISTANT_MANAGER_UNITS kk\n" + 
			" where kk.is_use='Y'";  //update by sychen 20100902
//		" where kk.is_use='U'";

		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	//岗位级别
	public List GetAllStationlevel() throws Exception{
		String sql=
			"select t.station_level_id,t.station_level_name\n" +
			"from HR_C_STATION_LEVEL t\n" + 
			"where t.is_use='Y'";  //update by sychen 20100902 
//		"where t.is_use='U'"; 
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	//所属部门
	public List GetDep() throws Exception
	{
		String sql=
			"select t.dept_id,t.dept_name\n" +
			"from hr_c_dept t\n" + 
			"where t.is_use='Y'";  //update by sychen 20100902 
//		"where t.is_use='U'"; 
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
	}
	//技术等级
	public List GetAlltechnologygrade() throws Exception{
		String sql=
			"select t.technology_grade_id,t.technology_grade_name\n" +
			"from HR_C_TECHNOLOGY_GRADE t\n" + 
			"where t.is_use='Y'";  //update by sychen 20100902  
//		"where t.is_use='U'";  
			
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
		}
	


}
