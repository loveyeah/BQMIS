package power.ejb.workticket.business;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
 
@Stateless
public class RunJWorkticketSafetyFacade implements
		RunJWorkticketSafetyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public RunJWorkticketSafety save(RunJWorkticketSafety entity) {
		try {
			if(entity.getId()==null)
			{
				entity.setId(bll.getMaxId("RUN_J_WORKTICKET_SAFETY", "ID"));
			}
			if(entity.getAttributeCode()==null||entity.getAttributeCode().equals(""))
			{
				entity.setAttributeCode("temp");
			}
			entity.setCreateDate(new java.util.Date());
			entity.setIsUse("Y");
			entity.setSafetyExecuteDate(new Date());
			entity.setIsReturn("Y");
		  entityManager.persist(entity);
		  return entity;
	} catch (RuntimeException re) {
		LogUtil.log("save failed", Level.SEVERE, re);
		throw re;
	}
	}

	public void delete(Long Id)
	{
		try {
			RunJWorkticketSafety entity=this.findById(Id);
			if(entity!=null)
			{
				entity.setIsUse("N");
				this.update(entity);
			}
		}
		catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String Ids)
	{
		try{
			String sql=
				"update RUN_J_WORKTICKET_SAFETY t\n" +
				"set t.is_use='N'\n" + 
				"where t.ID in("+Ids+")";
	        bll.exeNativeSQL(sql);
		}
		catch(RuntimeException re){
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	public RunJWorkticketSafety update(RunJWorkticketSafety entity) {
		try {
			entity.setCreateDate(new java.util.Date());
			entity.setIsReturn("Y");
			RunJWorkticketSafety result = entityManager.merge(entity);
			return result;
	} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
	}
	}

	public RunJWorkticketSafety findById(Long id) {
		try {
			RunJWorkticketSafety instance = entityManager.find(
					RunJWorkticketSafety.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<RunJWorkticketSafety> getSafetyBy(String enterpriseCode,String workticketNo)
	{
//		String sql =
//			"select m.safety_code, m.safety_desc, t.*\n" +
//			"  from Run_j_Workticket_Safety t, run_c_worktick_safety m\n" + 
//			" where t.safety_code(+) = m.safety_code\n" + 
//			"   and m.is_use = 'Y'\n" + 
//			"   and m.workticket_type_code = '1'\n" + 
//			"   and t.is_use(+) = 'Y'\n" + 
//			"   and t.enterprise_code(+) = 'hfdc'\n" + 
//			"   and t.workticket_no(+) = 'BQCLDQ10903001'\n" + 
//			" order by m.order_by, t.operation_order";

		
		List<RunJWorkticketSafety> list = null;
		String sql = 
			"select t.*\n" +
			"  from RUN_J_WORKTICKET_SAFETY t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.workticket_no = '"+workticketNo+"'\n" + 
			" order by t.SAFETY_CODE, t.operation_order";  
		list = bll.queryByNativeSQL(sql,RunJWorkticketSafety.class);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode,String workticketNO,String safetyCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject();
			String sql = 
				"select t.* from RUN_J_WORKTICKET_SAFETY t where t.is_use = 'Y' \n" +
				" and t.enterprise_code =?\n" + 
				" and t.workticket_no =?\n" +
				" and t.SAFETY_CODE=? order by t.operation_order";	
			List<RunJWorkticketSafety> list = bll.queryByNativeSQL(sql,new Object[]{
					enterpriseCode,workticketNO,safetyCode
			},RunJWorkticketSafety.class, rowStartIdxAndCount);
			String sqlCount = "select count(1) from RUN_J_WORKTICKET_SAFETY t where t.is_use = 'Y' \n" +
						" and t.enterprise_code =?\n" + 
						" and t.workticket_no =?\n"+
						" and t.SAFETY_CODE=?";
			Long totalCount = Long.parseLong(bll.getSingal(sqlCount,new Object[]{
							enterpriseCode,workticketNO,safetyCode}).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result; 
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void deleteByNO(String workticketNO)
	{
	
		try{
				String sql = 
					"update RUN_J_WORKTICKET_SAFETY t set t.is_use = 'N'\n" +
					"where t.workticket_no = '" + workticketNO + "'";
				bll.exeNativeSQL(sql);
		}
		catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	//modify by fyyang 090311 安措执行内容不回写
	public void exeSafety(RunJWorkticketSafety entity)
	{
		this.update(entity);
//		entityManager.flush();
//		String sql=
//			"update run_j_worktick_sfaty_content t\n" +
//			"set t.safety_exe_content=GETEXESECURITYMEASURE('"+entity.getWorkticketNo()+"','"+entity.getSafetyCode()+"')\n" + 
//			"where t.workticket_no='"+entity.getWorkticketNo()+"'\n" + 
//			"and t.safety_code='"+entity.getSafetyCode()+"'\n" + 
//			"and t.is_use='Y'";
//		bll.exeNativeSQL(sql);
		
	}
	
	/**
	 * 判断安措填写的是否冲突
	 * 暂时未用
	 * @param entity
	 * @return
	 */
	private boolean checkSame(RunJWorkticketSafety entity)
	{
		boolean check=true;
		String sql=
			"select count(*) from run_j_workticket_safety t\n" +
			"where  t.workticket_no='"+entity.getWorkticketNo()+"'\n" + 
			"and t.attribute_code='"+entity.getAttributeCode()+"'\n" + 
			"and t.front_key_id in (select a.safety_key_id from run_c_workticket_safety_key a\n" + 
			"where a.safety_key_id=t.front_key_id and a.reverse_key_id is not null)\n" + 
			"and  "+entity.getFrontKeyId()+"  in (select a.safety_key_id from run_c_workticket_safety_key a\n" + 
			"where a.safety_key_id="+entity.getFrontKeyId()+" and a.reverse_key_id is not null)\n" + 
			"and t.is_use='Y'\n" + 
			"and t.enterprise_code='"+entity.getEnterpriseCode()+"'";
		if(entity.getId()!=null)
		{
			sql=sql+"   and t.id<>"+entity.getId();
		}
		if(Integer.parseInt(bll.getSingal(sql).toString())>0)
		{
			check=true;
		}
		else
		{
			check=false;
		}
		return check;

	}
	//editgrid
	//add by fyyang 090325 安措优化后的增删改
	public void modifyRecords(List<RunJWorkticketSafety>list,String delIds){
		if(list!=null)
		{
			for(RunJWorkticketSafety m:list)
			{
				if (m.getId()!= null) {
					update(m);
				} else {
					save(m);
				}
				entityManager.flush();
			}
		}
		if(delIds != null&& !delIds.trim().equals(""))
		{
			String sql = "delete from RUN_J_WORKTICKET_SAFETY c where c.id in("+delIds+")";  
			bll.exeNativeSQL(sql);
		}
	}
}