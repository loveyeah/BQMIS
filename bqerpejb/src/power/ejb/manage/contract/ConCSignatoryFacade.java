package power.ejb.manage.contract;

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
 * @author slTang
 */
@Stateless
public class ConCSignatoryFacade implements ConCSignatoryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public void save(ConCSignatory entity) {
		try {
			if(entity.getSignId() == null){
				entity.setSignId(bll.getMaxId("con_c_signatory", "sign_id"));
				entity.setLastModifiedDate(new Date());
				entityManager.persist(entity);
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}
   
	public void delete(ConCSignatory entity) {
		entity.setIsUse("N");
		this.update(entity);
	}


	public ConCSignatory update(ConCSignatory entity) {
		try {
			ConCSignatory result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public ConCSignatory findById(Long id) {
		LogUtil.log("finding ConCSignatory instance with id: " + id,
				Level.INFO, null);
		try {
			ConCSignatory instance = entityManager
					.find(ConCSignatory.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public PageObject findSignatoryList(String enterpriseCode,final int... rowStartIdxAndCount){
		PageObject pg = new PageObject();
		if(enterpriseCode==null || "".equals(enterpriseCode)){
			enterpriseCode="hfdc";
		}
		String sql=
			"select t.* from con_c_signatory t where t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'";
		String sqlCount=
			"select count(1) from con_c_signatory t where t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'";
		List<ConCSignatory> list=bll.queryByNativeSQL(sql, ConCSignatory.class,rowStartIdxAndCount);
		Long count=Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(count);
		return pg;
	}

}