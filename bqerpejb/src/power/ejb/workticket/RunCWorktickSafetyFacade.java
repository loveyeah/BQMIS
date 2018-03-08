package power.ejb.workticket;

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
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.workticket.form.WorticketSafety;

/**
 * Facade for entity RunCWorktickSafety.
 * 
 * @see power.ejb.workticket.RunCWorktickSafety
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorktickSafetyFacade implements RunCWorktickSafetyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public RunCWorktickSafety save(RunCWorktickSafety entity) throws CodeRepeatException {
		LogUtil.log("saving RunCWorktickSafety instance", Level.INFO, null);
		try {
			if(!this.checkSafeCodeForAdd(entity.getSafetyCode(), entity.getEnterpriseCode()))
			{
				if(entity.getSafetyId()==null)
				{
					entity.setSafetyId(bll.getMaxId("RUN_C_WORKTICK_Safety", "safety_id"));
				}
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
			  entityManager.persist(entity);
			  LogUtil.log("save successful", Level.INFO, null);
			  return entity;
			}
			else
			{
				throw new CodeRepeatException("安措编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long safetyId) throws CodeRepeatException {
		RunCWorktickSafety entity=this.findById(safetyId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
     
	}
	
	public void deleteMulti(String safetyIds)
	{
		String sql=
			"update RUN_C_WORKTICK_Safety t\n" +
			"set t.is_use='N'\n" + 
			"where t.safety_id in("+safetyIds+")";
        bll.exeNativeSQL(sql);
		
	}

	
	public RunCWorktickSafety update(RunCWorktickSafety entity) throws CodeRepeatException {
		LogUtil.log("updating RunCWorktickSafety instance", Level.INFO, null);
		try {
			if(!this.checkSafeCodeForAdd(entity.getSafetyCode(), entity.getEnterpriseCode(), entity.getSafetyId()))
			{
				entity.setModifyDate(new java.util.Date());
				RunCWorktickSafety result = entityManager.merge(entity);
				LogUtil.log("update successful", Level.INFO, null);
				return result;
			}
			else
			{
				throw new CodeRepeatException("安措编码不能重复!");
			}
			
			
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorktickSafety findById(Long id) {
		LogUtil.log("finding RunCWorktickSafety instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorktickSafety instance = entityManager.find(
					RunCWorktickSafety.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<RunCWorktickSafety> getMaintSafetyBy(String enterpriseCode,String workticketType)
	{
		List<RunCWorktickSafety>  list = null;
		String sql =  "select *\n" +
			"            from run_c_worktick_safety a\n" + 
			"           where a.enterprise_code = ?\n" + 
			"             and a.workticket_type_code = ? and a.is_use='Y'\n" + 
			"           order by a.order_by";
		list = bll.queryByNativeSQL(sql,new Object[]{enterpriseCode,workticketType},RunCWorktickSafety.class);
		return list; 
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String workticketTypeCode,String enterpriseCode,final int... rowStartIdxAndCount) throws ParseException {
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select t.*,b.workticket_type_name,c.markcard_type_name\n" +
				"from RUN_C_WORKTICK_Safety t,RUN_C_WORKTICKET_TYPE b,RUN_C_MARKCARD_TYPE c\n" + 
				"where t.workticket_type_code=b.workticket_type_code(+)\n" + 
				"and t.markcard_type_id=c.markcard_type_id(+)\n" + 
				"and  t.workticket_type_code like '"+workticketTypeCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";
			// 修改开始 QA111
			sql +="\n" + 
				"and b.is_use(+)='Y'" + "\n" +
				"and c.is_use(+)='Y'";
			// 修改结束
			List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			String sqlCount=
				"select count(1)\n" +
				"from RUN_C_WORKTICK_Safety t,RUN_C_WORKTICKET_TYPE b,RUN_C_MARKCARD_TYPE c\n" + 
				"where t.workticket_type_code=b.workticket_type_code(+)\n" + 
				"and t.markcard_type_id=c.markcard_type_id(+)\n" + 
				"and  t.workticket_type_code like '"+workticketTypeCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y' \n"+
			"and b.is_use(+)='Y'" + "\n" +
			"and c.is_use(+)='Y'";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			List arrlist = new ArrayList();
			Iterator it=list.iterator();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			while(it.hasNext())
			{
				WorticketSafety model=new WorticketSafety();
				RunCWorktickSafety safemodel=new RunCWorktickSafety();
				Object[] data=(Object[])it.next();
				safemodel.setSafetyId(Long.parseLong(data[0].toString()));
				safemodel.setWorkticketTypeCode(data[1].toString());
				safemodel.setSafetyCode(data[2].toString());
				safemodel.setSafetyDesc(data[3].toString());
				if(data[4]!=null)
				{
					safemodel.setOrderBy(Long.parseLong(data[4].toString()));
				}
				if(data[5]!=null)
				{
					safemodel.setMarkcardTypeId(Long.parseLong(data[5].toString()));
				}
				if(data[6]!=null)
				{
					//safemodel.setIsRunAdd(data[6].toString());
					//modify 090213 字段改变
					safemodel.setSafetyType(data[6].toString());
				}
				if(data[7]!=null)
				{
					safemodel.setModifyBy(data[7].toString());
				}
				if(data[8]!=null)
				{
					safemodel.setModifyDate(sf.parse(data[8].toString()));
				}
				if(data[11]!=null)
				{
					model.setWorkticketTypeName(data[11].toString());
				}
				if(data[12]!=null)
				{
					model.setMarkcardTypeName(data[12].toString());
				}
				model.setSafe(safemodel);
				arrlist.add(model);
				
			}
			result.setList(arrlist);
			result.setTotalCount(totalCount);
		
			return result;
		
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	public boolean checkSafeCodeForAdd(String safetyCode,String enterpriseCode,Long... safetyId)
	{
		boolean isSame = false;
		String sql=
			"select count(*) from RUN_C_WORKTICK_Safety t\n" +
			"where t.safety_code='"+safetyCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";

			
	    if(safetyId !=null&& safetyId.length>0){
	    	sql += "  and t.safety_id <> " + safetyId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}