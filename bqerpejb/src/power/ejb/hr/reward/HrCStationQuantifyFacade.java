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

/**
 * Facade for entity HrCStationQuantify.
 * 
 * @see power.ejb.hr.reward.HrCStationQuantify
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCStationQuantifyFacade implements HrCStationQuantifyFacadeRemote {
	// property constants
	public static final String STATION_NAME = "stationName";
	public static final String QUANTIFY_PROPORTION = "quantifyProportion";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public PageObject  getStationQuantity(String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql=
			"select " +
			"a.quantify_id," +
			"a.station_name," +
			"a.quantify_proportion\n" +
			"from  HR_C_STATION_QUANTIFY  a\n" + 
			"where a.is_use='Y'\n" + 
			"and  a.enterprise_code='"+enterPriseCode+"'";
//		System.out.println("the sql"+sql);
       List list=bll.queryByNativeSQL(sql);
       result.setList(list);
		return result;
	}
	public void  saveStationQuantity(List<HrCStationQuantify>  addList,List<HrCStationQuantify>  updateList)
	{
		if (addList != null &&addList.size() > 0) {

			for (HrCStationQuantify entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (HrCStationQuantify entity : updateList) {
				this.update(entity);
				entityManager.flush();
			}
		}
	}
	public void save(HrCStationQuantify entity) {
		LogUtil.log("saving HrCStationQuantify instance", Level.INFO, null);
		try {
			Long maxId=bll.getMaxId("HR_C_STATION_QUANTIFY", "quantify_id");
			entity.setQuantifyId(maxId);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void delStationQuantity(String ids)
	{
		String sql="update HR_C_STATION_QUANTIFY  a " +
				"set a.is_use='N'" +
				"where a.quantify_id  in ("+ids+")";
		bll.exeNativeSQL(sql);
	}
	
	public void delete(HrCStationQuantify entity) {
		LogUtil.log("deleting HrCStationQuantify instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCStationQuantify.class,
					entity.getQuantifyId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrCStationQuantify update(HrCStationQuantify entity) {
		LogUtil.log("updating HrCStationQuantify instance", Level.INFO, null);
		try {
			HrCStationQuantify result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCStationQuantify findById(Long id) {
		LogUtil.log("finding HrCStationQuantify instance with id: " + id,
				Level.INFO, null);
		try {
			HrCStationQuantify instance = entityManager.find(
					HrCStationQuantify.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	
}