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
 * Facade for entity SpCFireControl.
 * 
 * @see power.ejb.run.securityproduction.safesupervise.SpCFireControl
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SpCFireControlFacade implements SpCFireControlFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public SpCFireControl save(SpCFireControl entity) {
		LogUtil.log("saving SpCFireControl instance", Level.INFO, null);
		try {
			entity.setId(bll.getMaxId("SP_C_FIRE_CONTROL", "id"));
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
		String sql = "update  SP_C_FIRE_CONTROL a set a.is_use = 'N'\n"
				+ "where a.id in (" + ids + ")";
		bll.exeNativeSQL(sql);
	}

	public SpCFireControl update(SpCFireControl entity) {
		LogUtil.log("updating SpCFireControl instance", Level.INFO, null);
		try {
			SpCFireControl result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCFireControl findById(Long id) {
		LogUtil.log("finding SpCFireControl instance with id: " + id,
				Level.INFO, null);
		try {
			SpCFireControl instance = entityManager.find(SpCFireControl.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findFireControlList(String enterpriseCode,String deployPart,final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		
		String sql = "select a.id,\n" +
			"       a.deploy_part,\n" + 
			"       a.type,\n" + 
			"       a.param,\n" + 
			"       a.control_number,\n" + 
			"       a.serial_code,\n" + 
			"       to_char(a.valid_date, 'yyyy-MM-dd'),\n" + 
			"       to_char(a.check_date, 'yyyy-MM-dd'),\n" + 
			"       to_char(a.change_date, 'yyyy-MM-dd'),\n" + 
			"       a.check_by,\n" + 
			"       getworkername(a.check_by),\n" + 
			"       a.memo\n" + 
			"  from SP_C_FIRE_CONTROL a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1)\n" 
				+ "  from SP_C_FIRE_CONTROL a\n"
				+ " where a.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '"
				+ enterpriseCode + "'";
		
		String strWhere = "";
		if(deployPart != null && !deployPart.equals(""))
		{
			strWhere += " and  a.deploy_part like '%" + deployPart + "%'";
		}
		sqlCount = sqlCount + strWhere;
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		
		sql = sql + strWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		return pg;
	}

}