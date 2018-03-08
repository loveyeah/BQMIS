package power.ejb.run.securityproduction.danger;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class SpJDangerDeptValueFacade implements SpJDangerDeptValueFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public SpJDangerDeptValue save(SpJDangerDeptValue entity) {
		LogUtil.log("saving SpJDangerDeptValue instance", Level.INFO, null);
		try {
			entity.setIsUse("Y");
			entity.setId(bll.getMaxId("SP_J_DANGER_DEPT_VALUE", "id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public SpJDangerDeptValue update(SpJDangerDeptValue entity) {
		LogUtil.log("updating SpJDangerDeptValue instance", Level.INFO, null);
		try {
			SpJDangerDeptValue result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public List<SpJDangerDeptValue> findByDangerId(Long dangerId,String enterpriseCode){
		System.out.println("dangerId"+":"+dangerId+";"+"enterpriseCode"+":"+enterpriseCode);
		String sql="select * from SP_J_DANGER_DEPT_VALUE where danger_id="+dangerId+" and enterprise_code='"+enterpriseCode+"'";
		List list=bll.queryByNativeSQL(sql, SpJDangerDeptValue.class);
		return list;
	}
}