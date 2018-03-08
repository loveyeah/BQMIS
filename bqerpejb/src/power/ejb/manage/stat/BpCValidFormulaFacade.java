package power.ejb.manage.stat;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * @author liuyi 20100521 
 */
@Stateless
public class BpCValidFormulaFacade implements BpCValidFormulaFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	
	public void save(BpCValidFormula entity) {
		LogUtil.log("saving BpCValidFormula instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String itemCode) {
		String sql = "  delete from bp_c_valid_formula t where t.item_code ='"+itemCode+"'";
		bll.exeNativeSQL(sql);
	}

	
	public BpCValidFormula update(BpCValidFormula entity) {
		LogUtil.log("updating BpCValidFormula instance", Level.INFO, null);
		try {
			BpCValidFormula result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpCValidFormula findById(String itemCode) {
		String sql = 
			"select a.* \n" + 
			"  from bp_c_valid_formula a\n" + 
			" where a.item_code = '"+itemCode+"'\n";
		List<BpCValidFormula> list = bll.queryByNativeSQL(sql, BpCValidFormula.class);
		if(list != null && list.size() > 0)
			return list.get(0);
		else 
			return null;
		
	}

	

	
	@SuppressWarnings("unchecked")
	public List<BpCValidFormula> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all BpCValidFormula instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpCValidFormula model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}


	public List findValidFormulaEntity(String itemCode) {
		List list = null;
		if(itemCode != null && !itemCode.equals("")){
			String sql = 
				"select a.item_code,\n" +
				"       b.item_name,\n" + 
				"       a.conn_item_code,\n" + 
				"       c.item_name conn_item_name,\n" + 
				"       a.min,\n" + 
				"       a.max\n" + 
				"  from bp_c_valid_formula a, bp_c_stat_item b, bp_c_stat_item c\n" + 
				" where a.item_code = '"+itemCode+"'\n" + 
				"   and a.item_code = b.item_code\n" + 
				"   and a.conn_item_code = c.item_code";
			list = bll.queryByNativeSQL(sql);
		}
		return list;
	}

}