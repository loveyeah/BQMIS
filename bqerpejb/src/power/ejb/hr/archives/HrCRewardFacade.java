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
 * Facade for entity HrCReward.
 * 
 * @see power.ejb.hr.archives.HrCReward
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCRewardFacade implements HrCRewardFacadeRemote {
	// property constants
	public static final String EMP_ID = "empId";
	public static final String REWARD_NAME = "rewardName";
	public static final String REWARD_TYPE = "rewardType";
	public static final String REWARD_LEVEAL = "rewardLeveal";
	public static final String REWARD_UNIT = "rewardUnit";
	public static final String REWARD_REASON = "rewardReason";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	public  PageObject  getRewardList(String empID ,String enterPriseCode,int...rowStartIdxAndCount)
	{
		PageObject result=new PageObject();
		String  sql=
			"select a.reward_id," +
			"a.emp_id," +
			"b.new_emp_code," +
			"a.reward_name," +
			"to_char(a.reward_time,'yyyy-MM-dd')," +
			"a.reward_type," +
			"a.reward_leveal," +
			"a.reward_unit,\n" +
			"a.reward_reason " +
			" from hr_c_reward  a,hr_j_emp_info b\n" + 
			"where a.emp_id = b.emp_id " +
			" and a.emp_id="+empID+"" +
			" and  a.enterprise_code='"+enterPriseCode+"'\n" + 
			" and  b.enterprise_code='"+enterPriseCode+"'\n" + 
//			"and a.is_use='U'\n" +
			"and a.is_use='Y'\n" +//update by sychen 20100831
			"and b.is_use='Y'";
//		System.out.println("the sql"+sql);
	  String  sqlCount="select count(*) from  ("+sql+") ";
	
      List list=bll.queryByNativeSQL(sql, rowStartIdxAndCount);
      Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
      result.setList(list);
      result.setTotalCount(count);
		return result;
		
	}
	public  void saveOrUpdateReward(List<HrCReward> addOrUpdateList)
	{
		for(HrCReward  rewardModel :addOrUpdateList)
		{
			System.out.println("the empId"+rewardModel.getEmpId());
			HrCReward  reward=this.findReard(rewardModel.getEmpId(),rewardModel.getRewardName());
			if(reward==null)
			{//增加记录
				this.save(rewardModel);
				 entityManager.flush();
			}else
			{//修改记录
//				rewardModel.setIsUse("U");
				rewardModel.setIsUse("Y");//update by sychen 20100831
				rewardModel.setEnterpriseCode(reward.getEnterpriseCode());
				rewardModel.setRewardId(reward.getRewardId());
				this.update(rewardModel);
			}
		}
	}
	private HrCReward  findReard(Long empId,String rewardName)
	{
		String  sql="select * from" +
				" hr_c_reward  a  \n"
		        +"where a.emp_id='"+empId+"'" +
		        "and a.reward_name='"+rewardName+"'";
		List<HrCReward>  list=	bll.queryByNativeSQL(sql, HrCReward.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
		
		
	}
	public void save(HrCReward entity) {
		LogUtil.log("saving HrCReward instance", Level.INFO, null);
		try {
			Long rewardID = bll.getMaxId("HR_C_REWARD","REWARD_ID");
			entity.setRewardId(rewardID);
//			entity.setIsUse("U");
			entity.setIsUse("Y");//update by sychen 20100831
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrCReward entity) {
		LogUtil.log("deleting HrCReward instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCReward.class, entity
					.getRewardId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrCReward update(HrCReward entity) {
		LogUtil.log("updating HrCReward instance", Level.INFO, null);
		try {
			HrCReward result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCReward findById(Long id) {
		LogUtil.log("finding HrCReward instance with id: " + id, Level.INFO,
				null);
		try {
			HrCReward instance = entityManager.find(HrCReward.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void  delReward(String ids)
	{
		
		String sql=
			"update hr_c_reward  a " +
			" set  a.is_use='N'\n" + 
			"where a.reward_id  in  ("+ids+")";
		bll.exeNativeSQL(sql);

		
	}
	public Long getEmpIdByNewCode(String empNewCode, String enterpriseCode) {
		Long empId = null;
		String sql = "select a.emp_id\n" +
			"  from hr_j_emp_info a\n" + 
			" where a.new_emp_code = '"+empNewCode+"'\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";
//  System.out.println("the sql"+sql);
		Object obj = bll.getSingal(sql);
		if(obj != null){
			empId = Long.parseLong(obj.toString());
		}
		return empId;
	}
	
}