package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity SpJCarRepair.
 * 
 * @see power.ejb.run.securityproduction.safesupervise.SpJCarRepair
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpJCarRepairFacade implements SpJCarRepairFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public SpJCarRepair save(SpJCarRepair entity) {
		LogUtil.log("saving SpJCarRepair instance", Level.INFO, null);
		try {
			entity.setRepairId(bll.getMaxId("SP_J_CAR_REPAIR", "repair_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	 public void deleteMulti(String ids) {
		String sql = "update  SP_J_CAR_REPAIR a set a.is_use = 'N'\n" + "where a.repair_id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public SpJCarRepair update(SpJCarRepair entity) {
		LogUtil.log("updating SpJCarRepair instance", Level.INFO, null);
		try {
			SpJCarRepair result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpJCarRepair findById(Long id) {
		LogUtil.log("finding SpJCarRepair instance with id: " + id, Level.INFO,
				null);
		try {
			SpJCarRepair instance = entityManager.find(SpJCarRepair.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findCarRepairList(String enterpriseCode,String workCode,String sDate,String eDate,String carNo,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.repair_id,\n" +
			"       a.car_id,\n" + 
			"       (select c.car_no\n" + 
			"          from SP_C_CARFILE c\n" + 
			"         where c.car_id = a.car_id\n" + 
			"           and c.is_use = 'Y') car_no,\n" + 
			"       a.now_km_num,\n" + 
			"       a.send_person,\n" + 
			"       getworkername(a.send_person),\n" + 
			"       to_char(a.repair_date,'yyyy-MM-dd'),\n" + 
			"       a.repair_contend\n" + 
			"  from SP_J_CAR_REPAIR a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";

		String sqlcount = 
			"select count(1)\n" +
			"  from SP_J_CAR_REPAIR a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";
		
		String strWhere = "";
		String strOrder ="";
		if(workCode != null && !workCode.equals(""))
		{
			strWhere += " and  a.send_person = '" + workCode + "'";
		}
		if(carNo != null && !carNo.equals(""))
		{
			strWhere += " and  (select c.car_no from SP_C_CARFILE c where c.car_id = a.car_id and c.is_use = 'Y') like '%" + carNo + "%'";
		}
		if (sDate != null && sDate.length() > 0) {
			strWhere += " and a.repair_date>=to_date('" + sDate
					+ "'||'00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (eDate != null && eDate.length() > 0) {
			strWhere += " and a.repair_date<=to_date('" + eDate
					+ "'||'23:59:59', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		strOrder = "order by a.repair_date,a.car_id";
		sqlcount = sqlcount + strWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlcount).toString());
		pg.setTotalCount(totalCount);
		
		sql = sql + strWhere + strOrder;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		return pg;
	}

}