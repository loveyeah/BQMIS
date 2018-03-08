package power.ejb.hr.salary;

import java.text.SimpleDateFormat;
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
public class HrCBigAwardFacade  implements HrCBigAwardFacadeRemote  {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	
	public boolean checkAwardName(String awardMonth) {
		String st = "select count(*)  from  HR_C_BIG_AWARD t  where to_char(t.award_month,'yyyy-mm')='"
				+ awardMonth + "' and t.is_use='Y'";
		
		int a = Integer.parseInt(bll.getSingal(st).toString());
		if (a > 0) {
			return true;
		} else{
		  return false;
		}
	}
	
	public void save(HrCBigAward entity) {
		try {
			entity.setBigAwardId(bll.getMaxId("HR_C_BIG_AWARD", "big_award_id"));
			//update by sychen 20100721
			
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//			String awardMonth=sdf.format(entity.getAwardMonth());
//			 String sql =" select max(t.big_award_name)\n" +
//				 "from HR_C_BIG_AWARD t\n" + 
//				 "where t.big_award_name like'%大奖%'\n" + 
//				 "and t.enterprise_code='"+entity.getEnterpriseCode()+"'\n" + 
//				 "and t.is_use='Y'\n" + 
//				 "and to_char(t.award_month,'yyyy-mm') ='"+awardMonth+"'";
//		Object maxNoObj = bll.getSingal(sql);
//		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
//		String no = "";
//		if (maxNo != null && !"".equals(maxNo)) {
//			no = maxNo.substring(2,3);
//			Integer tempNo = Integer.parseInt(no);
//			tempNo++;
//			no = tempNo.toString();
//		} else {
//			no = "1";
//		}
//		String nameStr="大奖";
//		entity.setBigAwardName(nameStr+ no);
	//update by sychen 20100721 end 
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids,String awardMonth) {
		String st ="select count(1) from HR_C_BIG_AWARD t\n" +
		"where to_char(t.award_month,'yyyy-mm') ='"+awardMonth+"'\n" + 
		"and t.is_use='Y'";
	    int a = Integer.parseInt(bll.getSingal(st).toString());
	
		String sql = "update HR_C_BIG_AWARD t\n" + "   set t.is_use = 'N'\n"
				+ " where t.big_award_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
		if (a>1){
			String st2="select count(*)  from  HR_C_BIG_AWARD t  " +
					"where t.big_award_name='大奖1'\n" +
				"and t.big_award_id='"+ids+"'\n" + 
				"and t.is_use='N'";
			int b = Integer.parseInt(bll.getSingal(st2).toString());
			if (b>0){
		String sql2 = "update HR_C_BIG_AWARD t\n" + "   set t.big_award_name = '大奖1'\n"
			+ " where t.big_award_name='大奖2'"
			+ " and to_char(t.award_month,'yyyy-mm') ='"+awardMonth+"'\n";
	     bll.exeNativeSQL(sql2);  
			}
		}
	}

	
	public HrCBigAward update(HrCBigAward entity) {
		LogUtil.log("updating HrCBigAward instance", Level.INFO, null);
		try {
			HrCBigAward result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCBigAward findById(Long id) {
		LogUtil.log("finding HrCBigAward instance with id: " + id, Level.INFO,
				null);
		try {
			HrCBigAward instance = entityManager.find(HrCBigAward.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findBigAwardList(String awardMonth,String enterpriseCode,
			int... rowStartIdxAndCount) throws Exception {
		String strWhere = " where t.enterprise_code='" + enterpriseCode + "'\n"
		      + " and t.is_use='Y'\n";

		if (awardMonth != null && !awardMonth.equals("")) {
			strWhere += " and to_char(t.award_month,'yyyy-mm')='" + awardMonth + "'\n";
		}

		PageObject pg = new PageObject();
		String sql = "select t.big_award_id,\n" +
			"t.big_award_name,\n" + 
			"to_char(t.award_month,'yyyy-mm')award_month,\n" + 
			"to_char(t.assessment_from,'yyyy-mm-dd')assessment_from,\n" + 
			"to_char(t.assessment_to,'yyyy-mm-dd')assessment_to,\n" + 
			"t.half_standarddays,\n" + 
			"t.big_award_base,\n" + 
			"t.memo,\n" +
			"t.all_standarddays\n" + //add by wpzhu 20100716
			"from HR_C_BIG_AWARD t\n";

		sql += strWhere;
		String sqlCount = "select count(1) from HR_C_BIG_AWARD t" + strWhere;
		sql += " order by t.award_month,t.big_award_name";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	
}
