package power.ejb.hr.archives;

import java.util.Date;
import java.util.List;
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
 * Facade for entity HrCPunish.
 * 
 * @see power.ejb.hr.archives.HrCPunish
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCPunishFacade implements HrCPunishFacadeRemote {
	// property constants
	public static final String EMP_ID = "empId";
	public static final String PUNISH_NAME = "punishName";
	public static final String PUNISH_UNIT = "punishUnit";
	public static final String PUNISH_REASON = "punishReason";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	public  PageObject  getPunishList(String empID ,String enterPriseCode,int...rowStartIdxAndCount)
	{
		PageObject result=new PageObject();
		String  sql=
			"select a.punishinf_id," +
			"a.emp_id," +
			"b.new_emp_code," +
			"a.punish_name," +
			"to_char(a.punish_time,'yyyy-MM-dd')," +
			"a.punish_unit,\n" + 
			"to_char(a.punish_execute_time,'yyyy-MM-dd')," +
			"to_char(a.punish_end_time,'yyyy-MM-dd')," +
			"a.punish_deadline," +
			"a.punish_reason," +
			"a.memo\n" + 
			"from hr_c_punish  a,hr_j_emp_info b\n" + 
			"where a.emp_id=b.emp_id\n" + 
			"and a.emp_id="+empID+"\n" + 
//			"and a.is_use='U'\n" + 
			"and a.is_use='Y'\n" + //update by sychen 20100830
			"and b.is_use='Y'\n" + 
			"and a.enterprise_code='"+enterPriseCode+"'\n" + 
			"and b.enterprise_code='"+enterPriseCode+"'\n" ;	
//		System.out.println("the sql"+sql);
	  String  sqlCount="select count(*) from  ("+sql+") ";
	
      List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
      Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
      result.setList(list);
      result.setTotalCount(count);
		return result;
		
	}
	private HrCPunish  findPunish(Long empId,String punishName)
	{
		String  sql="select * from" +
				" hr_c_punish  a  \n"
		        +"where a.emp_id='"+empId+"'" +
		        "and a.punish_name='"+punishName+"'";
//		System.out.println("the punishsql"+sql);
		List<HrCPunish>  list=	bll.queryByNativeSQL(sql, HrCPunish.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
		
		
	}
	public void  delPunish(String ids)
	{
		
		String sql=
			"update hr_c_punish  a " +
			" set  a.is_use='N'\n" + 
			"where a.punishinf_id  in  ("+ids+")";
		bll.exeNativeSQL(sql);

		
	}
	public  void saveOrUpdatePunish(List<HrCPunish> addOrUpdateList)
	{
		for(HrCPunish  punishModel :addOrUpdateList)
		{
			HrCPunish  punish=this.findPunish(punishModel.getEmpId(),punishModel.getPunishName());
			if(punish==null)
			{//增加记录
				this.save(punishModel);
//				 entityManager.flush();
			}else
			{//修改记录
//				punishModel.setIsUse("U");
				punishModel.setIsUse("Y");//update by sychen 20100830
				punishModel.setEnterpriseCode(punish.getEnterpriseCode());
				punishModel.setPunishinfId(punish.getPunishinfId());
				this.update(punishModel);
			}
		}
	}
	
	public void save(HrCPunish entity) {
		LogUtil.log("saving HrCPunish instance", Level.INFO, null);
		try {
			Long punishID = bll.getMaxId("hr_c_punish","punishinf_id");
			entity.setPunishinfId(punishID);
//			entity.setIsUse("U");
			entity.setIsUse("Y");//update by sychen 20100830
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(HrCPunish entity) {
		LogUtil.log("deleting HrCPunish instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCPunish.class, entity
					.getPunishinfId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrCPunish update(HrCPunish entity) {
		LogUtil.log("updating HrCPunish instance", Level.INFO, null);
		try {
			HrCPunish result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCPunish findById(Long id) {
		LogUtil.log("finding HrCPunish instance with id: " + id, Level.INFO,
				null);
		try {
			HrCPunish instance = entityManager.find(HrCPunish.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

}