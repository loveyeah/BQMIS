package power.ejb.hr.labor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.labor.form.LoborRegisterForm;
@Stateless
public class LaborManageImpl implements LaborManage{
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	
	public DynaicQueryClass  findDeptSendList(String year,String season,String enterpriseCode,String[] plantname,String[] plantcount,String[] planID)
	{    
		String time="";
		if (season=="1"||season.equals("1"))
		{ time=year+'-'+"01";}
	else if (season=="2"||season.equals("2"))
		{time=year+'-'+"04";}
	else if (season=="3"||season.equals("3"))
	{ time=year+'-'+"07";}
	else if (season=="4"||season.equals("4"))
	{time=year+'-'+"10";}
	
		
	        //构造列头
			String headerSql = 

				"select distinct m.labor_material_id,m.labor_material_name\n" +
				"from HR_C_LABOR_MATERIAL m where m.is_use='Y'\n" + 
				"  and (m.receive_kind='2' or m.receive_kind='3')  and  m.is_send='Y' \n"+ //add by fyyang 100105
				"order by m.labor_material_id ";

				/*"and r.enterprise_code='"+enterpriseCode+"'";*/
			System.out.println("headerSql is:"+headerSql);
			String dataSql="select max(r.dept_register_id),d.dept_name,max(initcap('"+time+"')) dataDate,d.dept_code";
			List list = bll.queryByNativeSQL(headerSql);
			List<HrJLaborDeptRegister> arraylist = new ArrayList();
			Iterator it = list.iterator();
			//i用于定义有多少列，每迭代一次i++
			int i=0;
			int j=0 ;
			String id="";
			String name="";
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
			/*	HrJLaborDeptRegister model = new HrJLaborDeptRegister();
				if (o[0] != null) {
					model.setLaborMaterialName((o[0].toString()));
				} else {
					model.setLaborMaterialName("");
				}
				if (o[1] != null) {
					model.setSendNum(Long.parseLong(o[1].toString()));
				} else {
					model.setSendNum(0l);
				}
				arraylist.add(model);*/
				id="";
				name="";
				if(data[0]!=null)
		    	{
					id=data[0].toString();          
		    	}
				if(data[1]!=null){
					name=data[1].toString();
				}
				dataSql+=",max(decode(r.labor_material_id,'"+id+"',r.send_num,null))\n";
				//通过迭代把列名放进plantname数组里面
			    plantname[i++]=data[1].toString();
			    planID[j++]=data[0].toString();
			    
			} 
			
			dataSql+="from HR_C_LABOR_MATERIAL m,HR_J_LABOR_DEPT_REGISTER  r,hr_c_dept d\n" +
				     "where m.labor_material_id(+)=r.labor_material_id\n"+
				     "and d.pdept_id(+)=0 " +
				     "and d.dept_code=r.dept_code(+)" +
				     " and r.send_season(+)= '"+season+"'"+
				     "and   to_char(r.send_date(+),'yyyy' )='"+year+"'" +
				     "group by d.dept_name,d.dept_code \n"+
				     "  order by  d.dept_name ";

	
		//	System.out.println("*************************");
		//	System.out.println("new sql is:"+dataSql);
			System.out.println("**");
			plantcount[0]=Integer.toString(i);
			List dataList=bll.queryByNativeSQL(dataSql);
			
			DynaicQueryClass queryClass=new DynaicQueryClass();
			queryClass.setList(dataList);
			queryClass.setHeaderNames(plantname);
			queryClass.setHeaderCount(i);
			queryClass.setPlanID(planID);
			return queryClass;
		} 
	
		
	
	
	public boolean saveDeptSendInfo(String year,String season,List<HrJLaborDeptRegister> addlist,List<HrJLaborDeptRegister>updatelist,String enterpriseCode) throws ParseException
	{
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String time="";
		if (season=="1"||season.equals("1"))
		{ time=year+'-'+"01"+'-'+"01";}
		else if (season=="2"||season.equals("2"))
			{time=year+'-'+"04"+'-'+"01";}
		else if (season=="3"||season.equals("3"))
		{ time=year+'-'+"07"+'-'+"01";}
		else if (season=="4"||season.equals("4"))
		{time=year+'-'+"10"+'-'+"01";}
	
			try {
				long id = bll.getMaxId("HR_J_LABOR_DEPT_REGISTER", "dept_register_id");
				if (addlist.size()>0){
					for (HrJLaborDeptRegister model : addlist) {
						model.setDeptRegisterId(id);
						model.setEnterpriseCode(enterpriseCode);
						model.setSendDate(format.parse(time));
						model.setSendSeason(season);
						model.setIsUse("Y");
						model.setActualNum(0L);
						model.setStatus("Y");
						id++;
						this.save(model);
						
					}
				}
				if (updatelist.size()>0){
					
					for (HrJLaborDeptRegister model : updatelist) {
						this.update(model);
						
					}
				}
				return true;
			} catch (RuntimeException re) {
				throw re;
			}
		
	
		
	}
	public void save(HrJLaborDeptRegister entity) {
		try { /*Long    id=entity.getDeptRegisterId();
		  Long  num=entity.getSendNum();
			System.out.println(entity.getSendNum());
			String sql="insert into HR_J_LABOR_DEPT_REGISTER(dept_register_id,.send_num)" +
					"values('"+id+"','"+num+"') ";*/
			entityManager.persist(entity);
			
			
			
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public HrJLaborDeptRegister findByDeptAndLabor(String deptCode,Long laborMaterialId,String year,String season,String enterpriseCode)  {
		
		
			String time="";
			if (season=="1"||season.equals("1"))
			{ time=year+'-'+"01"+'-'+"01";}
			else if (season=="2"||season.equals("2"))
				{time=year+'-'+"04"+'-'+"01";}
			else if (season=="3"||season.equals("3"))
			{ time=year+'-'+"07"+'-'+"01";}
			else if (season=="4"||season.equals("4"))
			{time=year+'-'+"10"+'-'+"01";}
			String sql=
				"select * from HR_J_LABOR_DEPT_REGISTER t\n" +
				"where to_char(t.send_date,'yyyy-MM-dd')='"+time+"'\n" + 
				"and t.dept_code='"+deptCode+"'\n" + 
				"and t.labor_material_id="+laborMaterialId+"\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";


	
			List list = bll.queryByNativeSQL(sql, HrJLaborDeptRegister.class);
			if(list!=null&&list.size()>0){
				return (HrJLaborDeptRegister)list.get(0);
			}
			return null;
		
	}
	
public List findLabor() throws Exception {
		
		try {
			
			String sql=
				"select distinct m.labor_material_id,m.labor_material_name\n" +
				"from HR_C_LABOR_MATERIAL m where m.is_use='Y' \n" + 
				"order by m.labor_material_id";

//System.out.println("********"+sql);
			List list = bll.queryByNativeSQL(sql);
			if(list!=null){
				return list;
			}
			return null;
		} catch (Exception re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

public List findById(String id) throws Exception {
	
	try {
		
		String sql=

			"select t.* from HR_J_LABOR_DEPT_REGISTER t\n" +
			"where t.dept_register_id="+id;


		List list = bll.queryByNativeSQL(sql);
		if(list!=null){
			return list;
		}
		return null;
	} catch (Exception re) {
		LogUtil.log("find failed", Level.SEVERE, re);
		throw re;
	}
}
	public  void  update(HrJLaborDeptRegister entity) {
		try {
			HrJLaborDeptRegister result = entityManager.merge(entity);
//			/entityManager.flush();
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PageObject  getDeptList(String enterpriseCode){
           PageObject pg=new PageObject();
		
		String sql=
			"select t.dept_name ,t.dept_id from  hr_c_dept  t\n" +
			"where  t.pdept_id=0" +
			"and  t.enterprise_code='"+enterpriseCode+"'";
		List list = bll.queryByNativeSQL(sql);
		pg.setList(list);	
		

		return pg ;
		
	}
	
	@SuppressWarnings("unchecked")
	public PageObject findLaborRegisterList(String enterpriseCode,
			String deptId, String sendSeason, final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();

		String sqlLoborMaterial = "select a.*\n"
				+ "  from HR_C_LABOR_MATERIAL a\n" + " where a.is_use = 'Y'\n"
				+" and (a.receive_kind='1'or a.receive_kind='3')  and  a.is_send='Y'\n"  //add by fyyang 100105
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ " order by a.labor_material_id asc";
		List<HrCLaborMaterial> list = bll.queryByNativeSQL(sqlLoborMaterial,
				HrCLaborMaterial.class);
		String sqlLoborRegCount = "select count(1)\n"
				+ "  from HR_J_LABOR_REGISTER g, hr_j_emp_info h\n"
				+ " where  g.work_code = h.emp_code\n"
				+ "   and g.send_season = '" + sendSeason + "'\n"
				+ "   and h.dept_id = " + deptId + "\n"
				+ "   and g.is_use = 'Y'\n" + "   and h.is_use = 'Y'\n"
				+ "   and g.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and h.enterprise_code = '" + enterpriseCode + "'";

		Long sqlloborregcount = Long.parseLong(bll.getSingal(sqlLoborRegCount)
				.toString());

		if (sqlloborregcount == 0) {
			String sql = "select distinct null,a.dept_id,t.dept_code,getdeptname(t.dept_code) as deptName,GETLaborIdByWorkCode(a.emp_code),l.lb_work_name,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女') \n";
			String sqlTitle = "select '发放季度','部门ID','部门编码','部门名称','劳保ID','劳保工种','人员编码','人员名称','性别'\n";
			String dataIndex = "select 'sendSeason','deptId','deptCode','deptName','lbWorkId','lbWorkName','empCode','chsName','sex'\n";
			for (int i = 0; i < list.size(); i++) {
				// 标题
				sqlTitle += ",(select t.LABOR_MATERIAL_NAME\n"
						+ "  from HR_C_LABOR_MATERIAL t\n"
						+ " where t.LABOR_MATERIAL_ID = "
						+ list.get(i).getLaborMaterialId() + "\n"
						+ "   and t.is_use = 'Y'\n"
						+ "   and t.enterprise_code = '" + enterpriseCode
						+ "')";
				dataIndex += ",'type" + list.get(i).getLaborMaterialId() + "'";

				sql += ",nvl((select trunc((s.material_num/s.spacing_month)*3) material_num\n"
						+ "  from HR_J_LABOR_STANDARD s, HR_C_LABOR_MATERIAL m\n"
						+ " where s.labor_material_id = m.labor_material_id\n"
						+ "   and s.lb_work_id=a.lb_work_id\n"
						+ "   and s.labor_material_id="
						+ list.get(i).getLaborMaterialId() + "\n"
						+ "   and s.is_use = 'Y'\n"
						+ "   and m.is_use = 'Y' \n"
						+ "   and s.enterprise_code = '" + enterpriseCode+"' \n"
						//add by fyyang 100104 劳保用品分男用女用及公用
						+"    and  (s.send_kind='1' or s.send_kind=decode(a.sex,'M','2','W','3','') )\n"
						+ "),0)";
			}
			sql += "  from hr_j_emp_info a,hr_c_dept t,hr_c_lbgzbm l\n"
					+ "where \n"
					+ " a.dept_id = t.dept_id\n"
					//+ "and a.lb_work_id = l.lb_work_id(+)\n"
					+ "and GETLaborIdByWorkCode(a.emp_code) = l.lb_work_id(+)\n"
					
					+ "and a.dept_id = "
					+ deptId
					+ "\n"
					+ "and a.is_use = 'Y'\n"
					+ "and t.is_use = 'Y'\n" //update by sychen 20100901
//					+ "and t.is_use = 'U'\n" 
					+"  order by a.chs_name ";  //add by fyyang 100105 排序
			// String sqlcount = "select count(1) from (" + sql + ")";
			sqlTitle += "  from dual\n";
			dataIndex += "  from dual\n";
			List titleList = bll.queryByNativeSQL(sqlTitle + "\n union \n "
					+ dataIndex);

			List dataList = new ArrayList();
			dataList = bll.queryByNativeSQL(sql);
			dataList.add(0, titleList.get(0));
			dataList.add(1, titleList.get(1));
			pg.setList(dataList);
			pg.setTotalCount(Long.parseLong(list.size() + ""));
			return pg;
		} else {
			// 修改时
			String sql = "select distinct g.send_season,a.dept_id,t.dept_code,getdeptname(t.dept_code) as deptName,a.LB_WORK_ID,l.lb_work_name,a.emp_code,a.chs_name,decode(a.sex,'M','男','W','女')\n";
			String sqlTitle = "select '发放季度','部门ID','部门编码','部门名称','劳保ID','劳保工种','人员编码','人员名称','性别'\n";
			String dataIndex = "select 'sendSeason','deptId','deptCode','deptName','lbWorkId','lbWorkName','empCode','chsName','sex'\n";

			for (int i = 0; i < list.size(); i++) {
				sql += ",nvl((select lg.send_num  from HR_J_LABOR_REGISTER lg\n"
						+ "where lg.work_code = a.emp_code and lg.send_season = g.send_season and lg.labor_material_id='"
						+ list.get(i).getLaborMaterialId()
						+ "'"
						+ "and lg.enterprise_code='"
						+ enterpriseCode
						+ "' and rownum = 1),0) as sendNum"
						+ list.get(i).getLaborMaterialId();

				// 标题
				sqlTitle += ",(select t.LABOR_MATERIAL_NAME\n"
						+ "  from HR_C_LABOR_MATERIAL t\n"
						+ " where t.LABOR_MATERIAL_ID = "
						+ list.get(i).getLaborMaterialId() + "\n"
						+ "   and t.is_use = 'Y'\n"
						+ "   and t.enterprise_code = '" + enterpriseCode
						+ "')";
				dataIndex += ",'type" + list.get(i).getLaborMaterialId() + "'";

			}
			sql += "  from hr_j_emp_info a,HR_J_LABOR_REGISTER g,hr_c_dept t,hr_c_lbgzbm l\n"
					+ "where g.work_code = a.emp_code\n"
					+ "and a.dept_id = t.dept_id\n"
					+ "and a.lb_work_id = l.lb_work_id(+)\n"
					+ "and a.dept_id = "
					+ deptId
					+ "\n"
					+ "and a.is_use = 'Y'\n"
					+ "and g.is_use = 'Y'\n"
					+ "and t.is_use = 'Y'\n" //update by sychen 20100901
//					+ "and t.is_use = 'U'\n" 
					+ "and g.enterprise_code = '"
					+ enterpriseCode + "' \n"
					+"    and g.send_season='"+sendSeason+"' \n"   //add by fyyang 100105
					+"  order by a.chs_name ";  //add by fyyang 100105 排序

			// String sqlcount = "select count(1) from (" + sql + ")";
			sqlTitle += "  from dual\n";
			dataIndex += "  from dual\n";
			List titleList = bll.queryByNativeSQL(sqlTitle + "\n union \n "
					+ dataIndex);

			List dataList = new ArrayList();
			dataList = bll.queryByNativeSQL(sql);
			dataList.add(0, titleList.get(0));
			dataList.add(1, titleList.get(1));
			pg.setList(dataList);
			pg.setTotalCount(Long.parseLong(list.size() + ""));
			return pg;
		}
	}

	public void saveLoborRegisterRecord(String method,
			List<LoborRegisterForm> arrlist) {

		if (method.equals("add")) {
			for (LoborRegisterForm form : arrlist) {
				List<HrJLaborRegister> loborList = form.getLoborlList();
				for (HrJLaborRegister loborRegister : loborList) {
					if(loborRegister.getSendNum()!=0) //等于0的不用保存 add by fyyang 100105
					{
					loborRegister.setLaborRegisterId(bll.getMaxId(
							"HR_J_LABOR_REGISTER", "labor_register_id"));
					loborRegister = entityManager.merge(loborRegister);
					entityManager.flush();
					}
				}
			}
		} else {
			for (LoborRegisterForm form : arrlist) {
				List<HrJLaborRegister> detailList = form.getLoborlList();

				for (HrJLaborRegister detail : detailList) {
					if(this.existLoborRegisterRecord(detail))
					{
					String sql = "update HR_J_LABOR_REGISTER b\n"
							+ "   set b.send_num = " + detail.getSendNum()
							+ "\n" + " where b.labor_material_id = "
							+ detail.getLaborMaterialId() + "\n"
							+ "   and b.send_season = '"
							+ detail.getSendSeason() + "'\n"
							+ "   and b.work_code = '" + detail.getWorkCode()
							+ "'";

					bll.exeNativeSQL(sql);
					}
					else
					{
						if(detail.getSendNum()!=0)
						{
							detail.setLaborRegisterId(bll.getMaxId(
									"HR_J_LABOR_REGISTER", "labor_register_id"));
							 entityManager.merge(detail);
							entityManager.flush();
						}
					}
					
				}
			}
		}
	}
	
 //add by fyyang 100105 判断记录是否存在
	private boolean existLoborRegisterRecord(HrJLaborRegister model)
	{
		String sql=
			"select count(*) from hr_j_labor_register a\n" +
			"where a.work_code='"+model.getWorkCode()+"'\n" + 
			"and a.labor_material_id="+model.getLaborMaterialId()+"\n" + 
			"and a.send_season='"+model.getSendSeason()+"'\n" + 
			"and a.enterprise_code='"+model.getEnterpriseCode()+"'\n" + 
			"and a.is_use='Y'";
		if(bll.getSingal(sql).toString().equals("0"))
		{
			return false;
		}
		else
		{
			return true;
		}

		
	}
	
}
