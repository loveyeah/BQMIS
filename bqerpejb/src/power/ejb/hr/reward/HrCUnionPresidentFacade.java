package power.ejb.hr.reward;

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
import power.ejb.manage.plan.BpJPlanRepairDetail;

/**
 * Facade for entity HrCUnionPresident.
 * 
 * @see power.ejb.hr.reward.HrCUnionPresident
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCUnionPresidentFacade implements HrCUnionPresidentFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PageObject  getUnionPresident()
	{
		PageObject result=new PageObject();
		String sql=
			"select a.union_per_id," +
			"a.union_per_standard," +
			"to_char(a.effect_start_time,'yyyy-MM-dd')," +
			"to_char(a.effect_end_time,'yyyy-MM-dd')," +
			"a.memo " +
			" from  HR_C_UNION_PRESIDENT  a " +
			"where a.is_use='Y'" +
			" order by  a.union_per_id  ";
//		System.out.println("the sql"+sql);
	   List	 list=bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
		
	}
	public void delUnionPresident(String ids)
	{
		String sql="update HR_C_UNION_PRESIDENT a " +
				" set a.is_use='N'" +
				"where a.union_per_id  in ("+ids+") ";
		bll.exeNativeSQL(sql);
	}
	public void   saveUnionPresident(List<HrCUnionPresident> addList,List<HrCUnionPresident> updateList)
	{
		if (addList != null &&addList.size() > 0) {

			for (HrCUnionPresident entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (HrCUnionPresident entity : updateList) {
				this.update(entity);
				entityManager.flush();
			}
		}
	}
	public void save(HrCUnionPresident entity) {
		LogUtil.log("saving HrCUnionPresident instance", Level.INFO, null);
		try {
			Long unionPerId = bll.getMaxId("HR_C_UNION_PRESIDENT ",
			"union_per_id");
	        entity.setUnionPerId(unionPerId);
	        entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrCUnionPresident entity) {
		LogUtil.log("deleting HrCUnionPresident instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCUnionPresident.class, entity
					.getUnionPerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrCUnionPresident update(HrCUnionPresident entity) {
		LogUtil.log("updating HrCUnionPresident instance", Level.INFO, null);
		try {
			HrCUnionPresident result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCUnionPresident findById(Long id) {
		LogUtil.log("finding HrCUnionPresident instance with id: " + id,
				Level.INFO, null);
		try {
			HrCUnionPresident instance = entityManager.find(
					HrCUnionPresident.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public String   getMaxEndTime()
	{
		String sql=
			"select\n" +
			"nvl(max(to_char(a.effect_end_time,'yyyy-MM-dd')),'') " +
			"from  HR_C_UNION_PRESIDENT  a\n" + 
			"where a.is_use='Y'";
		
		Object obj =bll.getSingal(sql);
		if(obj!=null)
		{
			return obj.toString();
		}else 
		{
			return "";
			
		}

		
		
	}


}