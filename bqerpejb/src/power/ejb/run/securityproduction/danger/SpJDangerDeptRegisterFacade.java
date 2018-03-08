package power.ejb.run.securityproduction.danger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity SpJDangerDeptRegister.
 * 
 * @see power.ejb.run.securityproduction.danger.SpJDangerDeptRegister
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJDangerDeptRegisterFacade implements
		SpJDangerDeptRegisterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**
	 * 保存
	 * @param entity
	 * @throws RuntimeException
	 */
	public void save(SpJDangerDeptRegister entity) {
		LogUtil.log("saving SpJDangerDeptRegister instance", Level.INFO, null);
		try {

			entity.setDangerId(bll.getMaxId("SP_J_DANGER_DEPT_REGISTER", "danger_id"));

			entity.setDangerId(bll.getMaxId("SP_J_DANGER_DEPT_REGISTER", "DANGER_ID"));

			entityManager.persist(entity);
			
			
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 更新
	 * @param entity
	 * @return SpJDangerDeptRegister 
	 * @throws RuntimeException
	 */


	public void delete(String ids) {
		String sql=
			"update SP_J_DANGER_DEPT_REGISTER t\n" +
			" set t.Is_Use='N' where t.DANGER_ID in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * Persist a previously saved SpJDangerDeptRegister entity and return it or
	 * a copy of it to the sender. A copy of the SpJDangerDeptRegister entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJDangerDeptRegister entity to update
	 * @return SpJDangerDeptRegister the persisted SpJDangerDeptRegister entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */

	public SpJDangerDeptRegister update(SpJDangerDeptRegister entity) {
		LogUtil
				.log("updating SpJDangerDeptRegister instance", Level.INFO,
						null);
		try {
			SpJDangerDeptRegister result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 改变状态
	 */
	public void changeStatus(String status, String ids, String enterpriseCode) {
		String sql="update SP_J_DANGER_DEPT_REGISTER ss set ss.status='"+status+"' where enterprise_code='"+enterpriseCode+"' and danger_id in ("+ids+")";
		bll.exeNativeSQL(sql);
		//System.out.println("status 修改成功");
	}

	/**
	 * 按照Id查询
	 * @param id
	 * @return SpJDangerDeptRegister
	 */
	public SpJDangerDeptRegister findById(Long id) {
		LogUtil.log("finding SpJDangerDeptRegister instance with id: " + id,
				Level.INFO, null);
		try {
			SpJDangerDeptRegister instance = entityManager.find(
					SpJDangerDeptRegister.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 *按照年度查询
	 * 
	 * @param dangerYear
	 * @param status
	 * @param deptCode
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List findByDangerYear(String dangerYear,String status,String deptCode,
			String enterpriseCode, Integer start,Integer limit) {
		try {
			String queryString = "select dd.danger_id,dd.danger_year,dd.danger_name,dd.finish_date,dd.assess_dept,GETDEPTNAME(dd.assess_dept) as deptName,dd.charge_by,GETWORKERNAME(dd.charge_by) as chargebYName,dd.memo,dd.work_flow_no,dd.status,dd.order_by,dd.is_use" +
					" from SP_J_DANGER_DEPT_REGISTER dd" +
					" where dd.danger_year='"+dangerYear+
			"' and dd.enterprise_code='"+enterpriseCode+"' and dd.is_use='Y' and dd.status in("+status+")";
			if (deptCode!=null) {
				//update by kzhang 20100810
				//queryString+=" and assess_dept='"+deptCode+"'";
				queryString+="and ( GETFirstLevelBYID((select t.dept_id from hr_c_dept t where t.dept_code = dd.assess_dept))" +
						"=GETFirstLevelBYID((select t.dept_id from hr_c_dept t where t.dept_code = '"+deptCode+"')) or dd.assess_dept='"+deptCode+"')";
			}
			queryString+=" order by order_by";
			System.out.println();
			List list=bll.queryByNativeSQL(queryString, start,limit);
			return list;
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
	/**
	 * 获得查询总条数
	 * @param dangerYear
	 * @param status
	 * @param deptCode
	 * @param enterpriseCode
	 * @return Long 
	 */
	@SuppressWarnings("unchecked")
	public Long getTotalCount(String dangerYear,String status,String deptCode, String enterpriseCode) {
		String sql="select count(*) from SP_J_DANGER_DEPT_REGISTER  dd where dd.danger_year='"+dangerYear+
		"' and dd.enterprise_code='"+enterpriseCode+"' and dd.is_use='Y' and dd.status in ("+status+")";
		if (deptCode!=null) {
			//update by kzhang 20100810
			//sql+=" and assess_dept='"+deptCode+"'";
			sql+="and ( GETFirstLevelBYID((select t.dept_id from hr_c_dept t where t.dept_code = dd.assess_dept))" +
			"=GETFirstLevelBYID((select t.dept_id from hr_c_dept t where t.dept_code = '"+deptCode+"')) or dd.assess_dept='"+deptCode+"')";
		}
		List list=bll.queryByNativeSQL(sql);
		Long count=0L;
		if (list!=null&&list.size()>0) {
			count=Long.parseLong(list.get(0).toString());
		}
		return count;
	}

	/**
	 * 检查导入的负责人是否存在 
	 * 并回错误行信息
	 * @param dutyNames
	 * @param dutyCr
	 * @return String 
	 */
	@SuppressWarnings("unchecked")
	public String  checkInputManName(String dutyNames,String dutyCr)
	{
		String msg="";
		String [] arrDutyName=dutyNames.split(",");
		String sql=
			"select count(*) from hr_j_emp_info t\n" +
			"where t.chs_name in ("+dutyNames+")";
        if(arrDutyName.length==Integer.parseInt(bll.getSingal(sql).toString()))
        {
        	msg="";
        }
        else
        {	String sqltemp="select count(*) from hr_j_emp_info t\n" +
			"where t.chs_name in (";
        	int i=this.findErrorData(arrDutyName,sqltemp);
        	msg=i+"";
        	 if(i!=-1)
    		 {
        		 String [] arrDutyCr=dutyCr.split(";");
    			 String [] crData=arrDutyCr[i].split(",");
    			 crData[0]=(Integer.parseInt(crData[0])+1)+"";
    			 crData[1]=(Integer.parseInt(crData[1])+1)+"";
    			 msg="第"+crData[0]+"行第"+crData[1]+"列";
    		 }
        }
		return msg;
	}
	/**
	 * 检测输入的部门名称是否存在 返回错误行信息
	 * @param deptNames
	 * @param deptCr
	 * @return String 
	 */
	@SuppressWarnings("unchecked")
	public String  checkInputDeptName(String deptNames,String deptCr)
	{
		String msg="";
		String [] arrDeptName=deptNames.split(",");
		String sql=
			"select count(*) from hr_c_dept d\n" +
			"where d.dept_name in ("+deptNames+")";
        if(arrDeptName.length==Integer.parseInt(bll.getSingal(sql).toString()))
        {
        	msg="";
        }
        else
        {	
        	String sqltemp="select count(*) from hr_c_dept d\n" +
			"where d.dept_name in (";
        	int i=this.findErrorData(arrDeptName,sqltemp);
        	msg=i+"";
        	 if(i!=-1)
    		 {	
        		 System.out.println("deptCr:"+deptCr);
        		 System.out.println(i+"arrDeptName:"+arrDeptName[i]);
    			 String [] crData=arrDeptName[i].split(",");
    			 crData[0]=(Integer.parseInt(crData[0])+1)+"";
    			 crData[1]=(Integer.parseInt(crData[1])+1)+"";
    			 msg="第"+crData[0]+"行第"+crData[1]+"列";
    		 }
        }
		return msg;
	}
/**
 * 查找错误信息行号
 * @param Names
 * @param sqltemp
 * @return int
 */
@SuppressWarnings("unchecked")
private int findErrorData(String [] Names,String sqltemp)
{
	int start=0;
	int end=Names.length-1;
	
	for(int i=0;i<Names.length;i++)
	{
		if(start==end) return start;
		
		int k=(end-start)/2;
		String strArrOne="";
		for(int j=start;j<=k;j++)
		{
			if(strArrOne.equals(""))
			{
			strArrOne=Names[j];
			}
			else
			{
				strArrOne +=","+Names[j];
			}
		}
		
		if(strArrOne.equals("")) return -1;
		else
		{
			sqltemp=sqltemp+strArrOne+")";
			 if(strArrOne.split(",").length==Integer.parseInt(bll.getSingal(sqltemp).toString()))
			 {
				 strArrOne="";
					for(int j=k+1;j<=end;j++)
					{
						if(strArrOne.equals(""))
						{
						 strArrOne=Names[j];
						}
						else
						{
							strArrOne +=","+Names[j];
						}
					}
					sqltemp=sqltemp+strArrOne+")";
					 if(strArrOne.split(",").length==Integer.parseInt(bll.getSingal(sqltemp).toString()))
					 {
						 return -1;
					 }
					 else
					 {
						 start=k+1;
					 }
			 }
			 else
			 {
				end=k; 
			 }
		}
	}
	return -1;
}
/**
 * 将要导入的List分为，更新list和插入list
 * @param dangerNames 
 * @param ddrlist
 * @return Map<Integer,List<SpJDangerDeptRegister>>
 */
@SuppressWarnings("unchecked")
private Map<Integer,List<SpJDangerDeptRegister>> findSaveOrUpdate(String dangerNames,List<SpJDangerDeptRegister> ddrlist){
	if (ddrlist!=null&&ddrlist.size()>0) {
		String getEnterpriseCode=ddrlist.get(0).getEnterpriseCode();
		String dangerYear=ddrlist.get(0).getDangerYear();
		Map<Integer,List<SpJDangerDeptRegister>> list=new HashMap<Integer, List<SpJDangerDeptRegister>>();
		List<SpJDangerDeptRegister> saveList=new ArrayList<SpJDangerDeptRegister>();
		List<SpJDangerDeptRegister> updateList=new ArrayList<SpJDangerDeptRegister>();
		String Sql="select * from SP_J_DANGER_DEPT_REGISTER d\n" +
		"where d.is_use='Y' and d.enterprise_code='"+getEnterpriseCode+"' and d.danger_year='"+dangerYear+"' and d.danger_name in ("+dangerNames+")";
		updateList=bll.queryByNativeSQL(Sql, SpJDangerDeptRegister.class);
		ourter:
		for (SpJDangerDeptRegister ddr1 : ddrlist) {
			for (SpJDangerDeptRegister ddr2 : updateList) {
				if (ddr1.getDangerName().equals(ddr2.getDangerName())) {
					ddr2.setDangerYear(ddr1.getDangerYear());
					ddr2.setFinishDate(ddr1.getFinishDate());
					ddr2.setMemo(ddr1.getMemo());
					ddr2.setChargeBy(ddr1.getChargeBy());
					ddr2.setAssessDept(ddr1.getAssessDept());
					ddr2.setLastModifiedBy(ddr1.getLastModifiedBy());
					ddr2.setLastModifiedDate(ddr1.getLastModifiedDate());
					continue ourter;
				}
			}
			saveList.add(ddr1);
		}
		list.put(1, saveList);
		list.put(2, updateList);
		return list;
	}
	return null;
}
/**
 * 导入
 */
@SuppressWarnings("unchecked")
public void importDangerDeptValue(String dangerNames,
		List<SpJDangerDeptRegister> ddrlist) {
	Map<Integer,List<SpJDangerDeptRegister>> map=findSaveOrUpdate(dangerNames,ddrlist);
	if (map!=null) {
	StringBuffer sqlsb = new StringBuffer();
	sqlsb.append("begin \n");
	String sql="";
	//System.out.println(bll.getMaxId("SP_J_DANGER_DEPT_REGISTER", "danger_id"));
	Long maxDetailId=bll.getMaxId("SP_J_DANGER_DEPT_REGISTER", "danger_id");
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String FinishDate=null;
	String lastmodifiedDate=null;
	if (map.get(1).size()>0) {
	  //System.out.println("savelist:"+map.get(1).size());
	    List objects=new ArrayList();
		for(SpJDangerDeptRegister ddr:map.get(1)){
				ddr.setDangerId(maxDetailId);
				FinishDate=df.format(ddr.getFinishDate());
				lastmodifiedDate=df.format(ddr.getLastModifiedDate());
				sql=
					"insert into SP_J_DANGER_DEPT_REGISTER aa(aa.danger_id,aa.danger_year,aa.danger_name," +
					"aa.finish_date,aa.memo,aa.status,aa.is_use,aa.enterprise_code,aa.last_modified_by,aa.last_modified_date," +
					"aa.assess_dept";
				if (ddr.getChargeBy()!=null&&!ddr.getChargeBy().equals("")) {
					sql+=",aa.charge_by";
				}
					sql+=")"+
					"select "+ddr.getDangerId()+",'"+ddr.getDangerYear()+"',?,to_date('"+FinishDate+"', 'yyyy-mm-dd hh24:mi:ss'),?,'"+
					ddr.getStatus()+"','"+ddr.getIsUse()+"','"+ddr.getEnterpriseCode()+"','"+ddr.getLastModifiedBy()+"',to_date('"+lastmodifiedDate+"', 'yyyy-mm-dd hh24:mi:ss'),";
					if (ddr.getChargeBy()!=null&&!ddr.getChargeBy().equals("")) {
						sql+="cd.dept_code,en.emp_code from hr_c_dept cd,hr_j_emp_info en where cd.dept_name='"+ddr.getAssessDept()+"' and chs_name='" +ddr.getChargeBy()+"'; \n";
					}else{
						sql+="cd.dept_code from hr_c_dept cd where cd.dept_name='"+ddr.getAssessDept()+"'; \n";
					}
					
				sqlsb.append(sql);
					objects.add(ddr.getDangerName());
					objects.add(ddr.getMemo());
				maxDetailId++;
		}		
				sqlsb.append("commit;\n");
				sqlsb.append("end;\n");
				Object[] arr = (Object[])objects.toArray( new  String[objects.size()]);  
				bll.exeNativeSQL(sqlsb.toString(),arr);
				//System.out.println("save成功");
	}
	if (map.get(2).size()>0) {
				for (SpJDangerDeptRegister spJDangerDeptRegister : map.get(2)) {
					FinishDate=df.format(spJDangerDeptRegister.getFinishDate());
					lastmodifiedDate=df.format(spJDangerDeptRegister.getLastModifiedDate());
					String updateSql="update SP_J_DANGER_DEPT_REGISTER ss " +
							"set ss.danger_year='"+spJDangerDeptRegister.getDangerYear()+"',ss.danger_name=?,ss.finish_date=" +
									"to_date('"+FinishDate+"', 'yyyy-mm-dd hh24:mi:ss'),ss.memo=?,ss.enterprise_code='"+spJDangerDeptRegister.getEnterpriseCode()+"'," +
											"ss.last_modified_by='"+spJDangerDeptRegister.getLastModifiedBy()+"',ss.last_modified_date=to_date('"+lastmodifiedDate+"', 'yyyy-mm-dd hh24:mi:ss')," +
													"ss.assess_dept=(select cd.dept_code from hr_c_dept cd where cd.dept_name='"+spJDangerDeptRegister.getAssessDept()+"')," +
															"ss.charge_by=(select en.emp_code from hr_j_emp_info en where en.chs_name='"+spJDangerDeptRegister.getChargeBy()+"') where ss.danger_id="+spJDangerDeptRegister.getDangerId();
					bll.exeNativeSQL(updateSql,new Object[]{spJDangerDeptRegister.getDangerName(),spJDangerDeptRegister.getMemo()});	
					//System.out.println("更新成功！！！");
				}
	}
				
	}
}



	/**
	 * 功能：查询责任人自查报告回录信息
	 * @author add by qxjiao 20100804
	 * @param year 
	 * @param enterprise_code
	 */
	
	public List<SpJDangerDeptRegister> findResultList(String year,
			String enterprise_code,String worker,String status, int start, int limit) {
		String sql= "select t.danger_id,"
							+"t.danger_year," +
							"t.danger_name," +
							"t.finish_date," +
							"GETDEPTNAME(t.assess_dept) as assess_dept," +
							"GETWORKERNAME(t.charge_by) as charge_by," +
							"t.memo,t.work_flow_no," +
							"t.status,t.order_by," +
							"t.d_value," +
							"t.d1_value," +
							"t.value_level," +
							"t.annex," +
							"GETWORKERNAME(last_modified_by) as last_modified_by," +
							"t.last_modified_date," +
							"t.is_use," +
							"t.enterprise_code" +
							" from SP_J_DANGER_DEPT_REGISTER t";
		
		if("2".equals(status)){
			sql += " where t.ENTERPRISE_CODE = '"+enterprise_code+"'" +
					" and t.Is_Use='Y'" +
					" and t.DANGER_YEAR like'%"+year+"%'" +
							" and t.status = '"+status+"'" +
									" and t.charge_by='"+worker+"' order by t.Danger_Year desc";
		}else if("3".equals(status)){
			sql += " where t.ENTERPRISE_CODE = '"+enterprise_code+"'" +
					" and t.Is_Use='Y'" +
					" and t.DANGER_YEAR like'%"+year+"%'" +
							" and t.status = '"+status+"' order by t.Danger_Year desc";
		}
		System.out.println(sql);
		List<SpJDangerDeptRegister> result = bll.queryByNativeSQL(sql,SpJDangerDeptRegister.class, start,limit);
		return result;
	}

	public int getCount(String year, String enterprise_code,String worker,String status) {
		String sql= "";
		if("2".equals(status)){
			sql = "select * from SP_J_DANGER_DEPT_REGISTER where ENTERPRISE_CODE = '"+enterprise_code+"' and Is_Use='Y' and DANGER_YEAR like'%"+year+"%' and status = '"+status+"' and charge_by='"+worker+"'";
		}else if("3".equals(status)){
			sql = "select * from SP_J_DANGER_DEPT_REGISTER where ENTERPRISE_CODE = '"+enterprise_code+"' and Is_Use='Y' and DANGER_YEAR like'%"+year+"%' and status = '"+status+"'";
		}
		
		
		int count = bll.queryByNativeSQL(sql).size();
		System.out.println("count is :"+count);
		return count;
	}
	/**
	 * 功能：查询危险源L,B2,D值
	 * @author add by qxjiao 20100804
	 * @param year 年度
	 * @param type 查询类型（L,B2,D）
	 * @param enterprise_code 企业编码
	 * @param start ,limit 分页条件
	 */
	public List findDangerValue(String year ,String type,String enterprise_code,int start,int limit) {
		String sql = "";
		String sort ="";
		List result = null;
		if(type.equals("L"))
			sort = "1";
		if(type.equals("B2"))
			sort= "2";
		if(type.equals("D")){
			sql = "select danger_id ,danger_name,d1_value,value_level from SP_J_DANGER_DEPT_REGISTER where danger_year = '"+year+"' and enterprise_code = '"+enterprise_code+"' and status = '3' order by danger_year";
		}else{
			sql = "select td.danger_id,td.danger_name,tv.score1,tv.score2,tv.score3,tv.score4,tv.score5,tv.score6,tv.score7,tv.score8,tv.score9,tv.score10,td.d1_value,td.value_level from SP_J_DANGER_DEPT_REGISTER td ,SP_J_DANGER_DEPT_VALUE tv where tv.score_sort = '"+sort+"' and td.danger_id=tv.danger_id and td.danger_year = '"+year+"' and td.enterprise_code = '"+enterprise_code+"' and td.status = '3' order by td.danger_id desc";
			
		}
		//System.out.println("search sql is -:"+sql);
		result = bll.queryByNativeSQL(sql, start,limit);
		
		return result;
	}
	/**
	 * 功能：查询L,B2,D值的总条数
	 * add by qxjiao 
	 */
	public int getValueCount(String year ,String type,String enterprise_code) {
		String sql = "";
		String sort ="";
		if(type.equals("L"))
			sort = "1";
		if(type.equals("B2"))
			sort= "2";
		if(type.equals("D")){
			sql = "select count(*) from SP_J_DANGER_DEPT_REGISTER where danger_year = '"+year+"' and enterprise_code = '"+enterprise_code+"' and status = '3'";
		}else{
			sql = "select count(*) from SP_J_DANGER_DEPT_REGISTER td ,SP_J_DANGER_DEPT_VALUE tv where tv.score_sort = '"+sort+"' and td.danger_id=tv.danger_id and td.danger_year = '"+year+"' and td.enterprise_code = '"+enterprise_code+"' and td.status = '3' ";
		}
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		System.out.println("L/B2 VALUE IS "+count);
		return count;
	}
	/**
	 * 功能：导出数据
	 * add by qxjiao 
	 */
	public List getExportList(String type,String year,String enterprise_code,String worker){
		String sql = "";
		String sort ="";
		List result = null;
		if(type.equals("L"))
			sort = "1";
		if(type.equals("B2"))
			sort= "2";
		if(type.equals("D")){
			sql = "select danger_id ,danger_name,d1_value,value_level from SP_J_DANGER_DEPT_REGISTER where danger_year = '"+year+"' and enterprise_code = '"+enterprise_code+"' and status = '3' and charge_by='"+worker+"' order by danger_year";
		}else{
			sql = "select td.danger_id,td.danger_name,tv.score1,tv.score2,tv.score3,tv.score4,tv.score5,tv.score6,tv.score7,tv.score8,tv.score9,tv.score10,td.d1_value,td.value_level from SP_J_DANGER_DEPT_REGISTER td ,SP_J_DANGER_DEPT_VALUE tv where tv.score_sort = '"+sort+"' and td.danger_id=tv.danger_id and td.danger_year = '"+year+"' and td.enterprise_code = '"+enterprise_code+"' and td.status = '3' and td.charge_by = '"+worker+"' order by td.danger_id desc";
			
		}
		System.out.println("------"+sql);
		result = bll.queryByNativeSQL(sql);
		return result;
	}
	/**
	 * 功能：上报危险源报告
	 * add by qxjiao 
	 */
	public void reportRecord(String worker){
		String LBsql ="SELECT tv.danger_id,tv.score1,"+
							        "tv.score2,"+
							        "tv.score3,"+
							        "tv.score4,"+
							        "tv.score5,"+
							        "tv.score6,"+
							        "tv.score7,"+
							        "tv.score8,"+
							        "tv.score9,"+
							        "tv.score10"+
							        " FROM SP_J_DANGER_DEPT_REGISTER td,"+
							        	"SP_J_DANGER_DEPT_VALUE    tv"+
							  " WHERE td.danger_id = tv.danger_id"+
							    " AND td.d1_value is not null"+
							    " AND td.status = '2' "+
							    " AND td.d_value is not null"+
							    " AND td.value_level is not null" +
							    " AND td.annex is not null";
		List<Object[]> preResult = bll.queryByNativeSQL(LBsql); //查询LB值都为空的记录
		List nullId = this.checkNullBValue(); //查询未填写LB值的danger_id
		String id = "";
		for(int i = 0 ;i<nullId.size();i++){//拼装未填写LB值的danger_id
			id+=nullId.get(i).toString()+",";
		}
		
		for(int i = 0;i<preResult.size();i++){//查询LB值为空的记录，并将其danger_id 拼入id中
			Object[] obj = preResult.get(i);
			if(obj[1]==null&&obj[2]==null&&obj[3]==null&&obj[4]==null&&obj[5]==null&&obj[6]==null&&obj[7]==null&&obj[8]==null&&obj[9]==null&&obj[10]==null){
				id+=obj[0].toString()+",";
			}
		}
		id = id.substring(0,id.length()-1);
		
		String sql = "UPDATE SP_J_DANGER_DEPT_REGISTER ts " +
							" SET ts.status = '3'"+
							" WHERE  ts.status = '2'"+
							" AND ts.d1_value is not null"+
							" AND ts.d_value is not null"+
							" AND ts.value_level is not null"+
							" AND ts.annex is not null" +
							" AND ts.danger_id not in("+id+")";
		bll.exeNativeSQL(sql);
	}
	/**
	 * 功能：确定危险源汇总信息
	 * add by qxjiao 
	 */
	public void confirmReport(){
		String sql = "update SP_J_DANGER_DEPT_REGISTER  set  status = '4' where status = '3'";
		bll.exeNativeSQL(sql);
	}
	 /**
	  * 功能：检验L,B2值是否已经录入
	  * add by qxjiao 
	  */
	public List checkLBValue(String year,String worker,String enterprise_code){
			String sql = "select td.danger_id,tv.score_sort,tv.score1,tv.score2," +
								 "tv.score3,tv.score4,tv.score5,tv.score6,tv.score7," +
								 "tv.score8,tv.score9,tv.score10" +
								 " from SP_J_DANGER_DEPT_REGISTER td ,SP_J_DANGER_DEPT_VALUE tv" +
								 " where td.danger_id=tv.danger_id" +
								 " and td.danger_year = '"+year+"'" +
								 " and td.enterprise_code = '"+enterprise_code+"'" +
								 " and td.status = '2'" +
								 " and td.charge_by = '"+worker+"'" +
								 " and td.is_use='Y' order by td.danger_id desc";
			System.out.println("sql statement is :"+sql);
			List result = bll.queryByNativeSQL(sql);
			return result;
	}
	
	public List checkNullBValue(){
		String sql = "select danger_id from SP_J_DANGER_DEPT_REGISTER  where danger_id not in (select distinct(danger_id) from SP_J_DANGER_DEPT_VALUE )";
		List result = bll.queryByNativeSQL(sql);
		return result;
	}
	

	/**
	 * 功能：查询所有重大危险源信息
	 * add by qxjiao 
	 */
	public PageObject queryDangerList(String year ,String status ,String name ,String enterprise_code,int start ,int limit ){
		String sql = "select t.danger_id,"
						+"t.danger_year," +
						"t.danger_name," +
						"t.finish_date," +
						"GETDEPTNAME(t.assess_dept) as assess_dept," +
						"GETWORKERNAME(t.charge_by) as charge_by," +
						"t.memo,t.work_flow_no," +
						"t.status,t.order_by," +
						"t.d_value," +
						"t.d1_value," +
						"t.value_level," +
						"t.annex," +
						"GETWORKERNAME(last_modified_by) as last_modified_by," +
						"t.last_modified_date," +
						"t.is_use," +
						"t.enterprise_code" +
						" from SP_J_DANGER_DEPT_REGISTER t";
		
		String sqlCondition = "";
		if("0".equals(status)){
			sqlCondition =  " where t.ENTERPRISE_CODE = '"+enterprise_code+"' " +
							" and t.Is_Use='Y' " +
							" and t.DANGER_YEAR like'%"+year+"%'" +
									" and t.danger_name like '%"+name+"%' order by t.Danger_Year desc";
		}else {
			sqlCondition= " where t.ENTERPRISE_CODE = '"+enterprise_code+"'" +
			" and t.Is_Use='Y'" +
			" and t.DANGER_YEAR like'%"+year+"%'" +
					" and t.status = '"+status+"'" +
							" and t.danger_name like '%"+name+"%' order by t.Danger_Year desc";
		}
		sql = sql + sqlCondition;
		String sqlCount = "select count(*) from SP_J_DANGER_DEPT_REGISTER t "+sqlCondition;
		List<SpJDangerDeptRegister> result = bll.queryByNativeSQL(sql,SpJDangerDeptRegister.class, start,limit);
		PageObject obj = new PageObject();
		obj.setList(result);
		obj.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return obj;
	}
	
	public List getApproveWork(Long workId,Long deptId,String workCode) {
		String sqlString = "select t.status,\n" +
				"       count(t.status),\n" + 
				"       (SELECT d.file_addr\n" + 
				"          FROM sys_c_fls     d,\n" + 
				"               sys_j_rrs     e,\n" + 
				"               sys_j_ur      a,\n" + 
				"               sys_c_ul      b,\n" + 
				"               hr_j_emp_info c\n" + 
				"         WHERE d.file_id = e.file_id\n" + 
				"           and e.role_id = a.role_id\n" + 
				"           and a.worker_id = b.worker_id\n" + 
				"           and b.worker_code = c.emp_code\n" + 
				"           and d.is_use = 'Y'\n" + 
				"           and e.is_use = 'Y'\n" + 
				"           and a.is_use = 'Y'\n" + 
				"           and b.is_use = 'Y'\n" + 
				"           and c.emp_id = "+workId+"\n" + 
				"           and d.file_addr = decode(t.status,\n" + 
				"                                    '1',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/bigDangerDeptChargeBy.jsp',\n" + 
				"                                    '2',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/dangerReport.jsp',\n" + 
				"                                    '3',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/dangerReportDownload.jsp',\n" + 
				"                                    ''))\n" + 
				"  from sp_j_danger_dept_register t\n" + 
				" where t.status in ('1', '2', '3')\n" + 
				"   and decode(t.status,\n" + 
				"              1,\n" + 
				"              (SELECT t.dept_id\n" + 
				"                 FROM hr_c_dept t\n" + 
				"                where t.dept_level = 1\n" + 
				"                  and rownum = 1\n" + 
				"                START WITH t.dept_id = '"+deptId+"'\n" + 
				"               CONNECT BY PRIOR t.dept_id = t.pdept_id),\n" + 
				"              t.assess_dept) = t.assess_dept\n" + 
				"   and decode(t.status, 2, '"+workCode+"', t.charge_by) = t.charge_by\n" + 
				"   and (SELECT count(1)\n" + 
				"          FROM sys_c_fls     d,\n" + 
				"               sys_j_rrs     e,\n" + 
				"               sys_j_ur      a,\n" + 
				"               sys_c_ul      b,\n" + 
				"               hr_j_emp_info c\n" + 
				"         WHERE d.file_id = e.file_id\n" + 
				"           and e.role_id = a.role_id\n" + 
				"           and a.worker_id = b.worker_id\n" + 
				"           and b.worker_code = c.emp_code\n" + 
				"           and d.is_use = 'Y'\n" + 
				"           and e.is_use = 'Y'\n" + 
				"           and a.is_use = 'Y'\n" + 
				"           and b.is_use = 'Y'\n" + 
				"           and c.emp_id = "+workId+"\n" + 
				"           and d.file_addr = decode(t.status,\n" + 
				"                                    '1',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/bigDangerDeptChargeBy.jsp',\n" + 
				"                                    '2',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/dangerReport.jsp',\n" + 
				"                                    '3',\n" + 
				"                                    'run/securityproduction/bigDanger/dangerManage/dangerReportDownload.jsp',\n" + 
				"                                    '')) <> 0\n" + 
				" group by t.status";
		List workList = bll.queryByNativeSQL(sqlString);
		return workList;
		
	}
	/**
	 * 重大危险源落实部门录入页面
	 * 添加，导入按钮是否可用
	 * add by kzhang 20100811
	 * @param year
	 * @param enterpriseCode
	 * @return Long 大于0可用
	 */
	public Long checkIsEditable(String year, String enterpriseCode) {
		String sql="select decode( (select count(*) " +
				"from sp_j_danger_dept_register t " +
				"where t.danger_year=? " +
				"and t.is_use='Y' " +
				"and t.enterprise_code=?),0,1," +
				"(select count(*) " +
				"from sp_j_danger_dept_register t " +
				"where t.danger_year=?" +
				" and t.is_use='Y'" +
				" and t.enterprise_code=?))-" +
				"(select count(*)" +
				" from sp_j_danger_dept_register t" +
				" where t.danger_year=?" +
				" and t.status<>'0'" +
				" and t.is_use='Y'" +
				" and t.enterprise_code=?)" +
				"from dual";
		long x=Long.parseLong(bll.getSingal(sql,new Object[]{year,enterpriseCode,year,enterpriseCode,year,enterpriseCode}).toString());
		return x;
	}
}