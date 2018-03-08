package power.ejb.run.runlog;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity RunCShiftEqu.
 * 
 * @see power.ejb.run.runlog.RunCShiftEqu
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCShiftEquFacade implements RunCShiftEquFacadeRemote {
	public static final String IS_USE = "isUse";
	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public int save(RunCShiftEqu entity){
		
		if(!this.CheckEquCodeSame(entity.getEnterpriseCode(), entity.getEquName(), entity.getSpecialityCode(), entity.getRunKeyId()))			
		{
			if(entity.getRunEquId()==null)
			{
				entity.setRunEquId(bll.getMaxId("run_c_shift_equ", "run_equ_id"));
				
				String equCode =generateTempEquCode();
				
				if(equCode.equals("TMP"))
				{
					entity.setAttributeCode("TMP00000001");
				}
				else
				{ 
					entity.setAttributeCode(equCode);
				}
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getRunEquId().toString());
		
		}
		else
		{
			return -1;
		}
	
	}

	public void delete(Long runequId) throws CodeRepeatException
	{
		RunCShiftEqu entity=this.findById(runequId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}


	public RunCShiftEqu update(RunCShiftEqu entity) throws CodeRepeatException {
		LogUtil.log("updating RunCShiftEqu instance", Level.INFO, null);
		try {
			if(!this.CheckEquNameSameForUpdate(entity.getEnterpriseCode(), entity.getEquName(), entity.getSpecialityCode(), entity.getRunKeyId(), entity.getRunEquId()))
			{
			RunCShiftEqu result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("设备名称不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCShiftEqu findById(Long id) {
		LogUtil.log("finding RunCShiftEqu instance with id: " + id, Level.INFO,
				null);
		try {
			RunCShiftEqu instance = entityManager.find(RunCShiftEqu.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	 
	@SuppressWarnings("unchecked")
	public List<RunCShiftEqu> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCShiftEqu instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftEqu model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunCShiftEqu> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCShiftEqu instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCShiftEqu model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	/**
	 * 运行方式下拉列表
	 */
	
	public List getRunWayList()
	{
		String sql=
			"select r.run_key_id,r.run_way_name from run_c_run_way r\n" +
			"where r.is_use='Y'";
		
		NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
		.getInstance().getFacadeRemote("NativeSqlHelper");
		
		List list=bll.queryByNativeSQL(sql);
		return list;
	}

	
	
	public List<RunCShiftEqu> findByIsUse(Object isUse,int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	} 
	
	
	/**
	 * 根据专业，运行方式查询运行设备列表
	 */
	public PageObject getShiftEquList(String specialsCode,Long runwayId,String enterpriseCode,final int...rowStartIdxAndCount)
	{
		long count = 0;
		String sqlCount = "select count(1)\n" +
		"  from (select a.run_equ_id,\n" + 
		"               getspecialname(a.speciality_code) speciality_name,\n" + 
		"               b.run_way_name,\n" + 
		"               a.attribute_code,\n" + 
		"               a.equ_name,\n" + 
		"               a.is_use,\n" + 
		"               a.enterprise_code\n" + 
		"          from run_c_shift_equ a, run_c_run_way b\n" + 
		"         where a.is_use = 'Y'\n" + 
		"           and b.is_use = 'Y'\n" + 
		"           and a.run_key_id = b.run_key_id\n" + 
		"           and a.run_key_id = "+runwayId+"\n" + 
		"           and a.speciality_code = '"+specialsCode+"'\n" + 
		"           and a.enterprise_code = '"+enterpriseCode+"'\n" + 
		"           and b.enterprise_code = '"+enterpriseCode+"') s";
		Object objCount = bll.getSingal(sqlCount);
		if (objCount != null) {
			count = Long.parseLong(objCount.toString());
		}
		
		String sql ="select a.run_equ_id,\n" +
			"       getspecialname(a.speciality_code) speciality_name,\n" + 
			"       b.run_way_name,\n" + 
			"       a.attribute_code,\n" + 
			"       a.equ_name,\n" + 
			"       a.is_use,\n" + 
			"       a.enterprise_code\n" + 
			"  from run_c_shift_equ a, run_c_run_way b\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and b.is_use = 'Y'\n" + 
			"   and a.run_key_id = b.run_key_id\n" + 
			"   and a.run_key_id = "+runwayId+"\n" + 
			"   and a.speciality_code = '"+specialsCode+"'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"' order by a.attribute_code";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<RunCShiftEquForm> FormList = new ArrayList();
		String str = "";
		for (int i = 0; i < list.size(); i++) {
			RunCShiftEquForm model = new RunCShiftEquForm();
			Object[] obj = (Object[]) list.get(i);
			if (obj[0] != null) {
				model.setRunEquId(obj[0].toString());
				str = str + "{'runEquId':'" + obj[0].toString() + "',";
			} else {
				model.setRunEquId("");
				str = str + "{'runEquId':'',";
			}
			if(obj[1] != null)
			{
				model.setSpecialityName(obj[1].toString());
				str = str + "{'specialityName':'" + obj[1].toString() + "',";
			}
			else{
				model.setSpecialityName("");
				str = str + "{'specialityName':'',";
			}
			if(obj[2] != null)
			{
				model.setRunwayName(obj[2].toString());
				str = str + "{'runwayName':'" + obj[2].toString() + "',";
			}
			else
			{
				model.setRunwayName("");
				str = str + "{'runwayName':'',";
			}
			if(obj[3] != null)
			{
				model.setAttributeCode(obj[3].toString());
				str = str + "{'attributeCode':'" + obj[3].toString() + "',";
			}
			else{
				model.setAttributeCode("");
				str = str + "{'attributeCode':'',";
			}
			if(obj[4] != null)
			{
				model.setEquName(obj[4].toString());
				str = str + "{'equName':'" + obj[4].toString() + "',";
			}
			else{
				model.setEquName("");
				str = str + "{'equName':'',";
			}
			if(obj[5] != null)
			{
				model.setIsUse(obj[5].toString());
				str = str + "{'isUse':'" + obj[5].toString() + "',";
			}
			else{
				model.setIsUse("");
				str = str + "{'isUse':'',";
			}
			if(obj[6] != null)
			{
				model.setEnterpriseCode(obj[6].toString());
				str = str + "{'enterpriseCode':'" + obj[6].toString() + "',";
			}
			else
			{
				model.setEnterpriseCode("");
				str = str + "{'enterpriseCode':'',";
			}
			FormList.add(model);
		}
		PageObject po = new PageObject();
		po.setList(FormList);
		po.setTotalCount(count);
		return po;
	}
	
	/**
	 * 根据专业查询运行方式
	 * @param enterpriseCode
	 * @return 
	 */
	public List getRunWayByProfession(String specialCode,String enterpriseCode)
	{
		String sql = 
					"select distinct a.run_key_id,\n" +
					"b.run_way_name,b.diaplay_no\n" + 
					"from run_c_shift_equ a,run_c_run_way b\n" + 
					"where a.speciality_code = '"+specialCode+"'\n" + 
					"and a.run_key_id = b.run_key_id\n" + 
					"and a.enterprise_code = b.enterprise_code\n" + 
					"and a.enterprise_code = '"+enterpriseCode+"'\n" + 
					"and a.is_use = b.is_use\n" + 
					"and a.is_use = 'Y' order by b.diaplay_no ";

					return bll.queryByNativeSQL(sql);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql="select * from  run_c_shift_equ \n";
			if(strWhere!="")
			{
				sql=sql+" where  "+strWhere;
			}
			List<RunCShiftEqu> list=bll.queryByNativeSQL(sql, RunCShiftEqu.class, rowStartIdxAndCount);
			String sqlCount="select count(*)　from run_c_shift_equ \n";
			if(strWhere!="")
			{
				sqlCount=sqlCount+" where  "+strWhere;
			}
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
			
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	/**
	 * 根据专业、运行方式、设备得到一个对象实体
	 */
	public RunCShiftEqu GetRunWayIdModel(String specialcode, Long runkeyid, String equcode, String enterprisecode)
	{
		String strWhere = 
			" speciality_code='"+specialcode+"'\n" + 
			"and run_key_id="+runkeyid+"\n" + 
			"and attribute_code='"+equcode+"'\n" + 
			"and enterprise_code='"+enterprisecode+"'\n" + 
			"and is_use='Y'";
		
		PageObject result=findList(strWhere);
		if(result.getList()!=null)
		{
			if(result.getList().size()>0)
			{
			return (RunCShiftEqu)result.getList().get(0);
			}
		}
		return null;
	}
	
/**
 * 判断设备名称是否重复
 */
	public boolean CheckEquCodeSame(String enterpriseCode,String equName,String specialCode,Long runkeyId) 
	{ 
		String sql ="select count(1)\n" +
					"from run_c_shift_equ t\n" + 
					"where t.is_use = 'Y'\n" + 
					"and t.enterprise_code = 'hfdc'\n" + 
					"and t.equ_name = '"+equName+"'\n" + 
					"and t.speciality_code = '"+specialCode+"'\n" + 
					"and t.run_key_id = "+runkeyId+"";

		if(Long.parseLong(bll.getSingal(sql).toString())>0)
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * 生成设备的临时编码
	 * 
	 * @return
	 */
	private String generateTempEquCode() {
		String strSql = 
			"select 'TMP' || trim(to_char(max(to_number(substr(attribute_code, 4))) + 1,'00000000'))\n" +
			"       from run_c_shift_equ t\n" + 
			"       where t.is_use = 'Y'";


		String code = "";
		Object obj = bll.getSingal(strSql);
		if (obj != null) {
			code = obj.toString();
		}
		return code;
	}
	/**
	 * 查询专业关心的设备列表
	 * @param specialcode
	 * @param enterprisecode
	 * @return
	 */
	public List<RunCShiftEqu> getListBySpecial(String specialcode,String enterprisecode){
		String sql="select *\n" +
			"  from run_c_shift_equ r\n" + 
			" where r.speciality_code = '"+specialcode+"'\n" + 
			"   and r.is_use = 'Y'\n" + 
			"   and r.enterprise_code = '"+enterprisecode+"'";
		return bll.queryByNativeSQL(sql, RunCShiftEqu.class);
	}

	private boolean CheckEquNameSameForUpdate(String enterpriseCode,String equName,String specialCode,Long runkeyId,Long... runequId) 
	{
		boolean isSame = false;
		String sql ="select count(1)\n" +
					"from run_c_shift_equ t\n" + 
					"where t.is_use = 'Y'\n" + 
					"and t.enterprise_code = '"+enterpriseCode+"'\n" + 
					"and t.equ_name = '"+equName+"'\n" + 
					"and t.speciality_code = '"+specialCode+"'\n" + 
					"and t.run_key_id = "+runkeyId+"";

	    if(runequId !=null&& runequId.length>0){
	    	sql += "  and t.run_equ_id <> " + runequId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
}