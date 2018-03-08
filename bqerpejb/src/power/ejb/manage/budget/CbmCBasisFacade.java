package power.ejb.manage.budget;

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
 * Facade for entity CbmCBasis.
 * 
 * @see power.ejb.manage.budget.CbmCBasis
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCBasisFacade implements CbmCBasisFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmCBasis entity) {
		LogUtil.log("saving CbmCBasis instance", Level.INFO, null);
		try {
			entity.setBasisId(bll.getMaxId("CBM_C_BASIS", "basis_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleMutl(String ids)
	{
		String sql="update CBM_C_BASIS  a " +
				"set a.is_use='N'" +
				"where a.basis_id  in ("+ids+")";
		bll.exeNativeSQL(sql);
	}
	
	public void delete(CbmCBasis entity) {
		LogUtil.log("deleting CbmCBasis instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmCBasis.class, entity
					.getBasisId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public CbmCBasis update(CbmCBasis entity) {
		LogUtil.log("updating CbmCBasis instance", Level.INFO, null);
		try {
			CbmCBasis result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void  saveBasis(List<CbmCBasis>  addList,List<CbmCBasis>  updateList)
	{
		if (addList != null &&addList.size() > 0) {
			for (CbmCBasis entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (CbmCBasis entity : updateList) {
				this.update(entity);
				entityManager.flush();
			}
		}
	}
	
	public CbmCBasis findById(Long id) {
		LogUtil.log("finding CbmCBasis instance with id: " + id, Level.INFO,
				null);
		try {
			CbmCBasis instance = entityManager.find(CbmCBasis.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject  findBasisList(String enterPriseCode,String budgetItemId,final int... rowStartIdxAndCount)
	{
		PageObject result=new PageObject();
		String sql="select s.basis_id,\n" +
			"       s.budget_item_id,\n" + 
			"       s.budget_basis,\n" + 
			"       s.budget_amount,\n" + 
			"       getworkername(s.last_modify_by),\n" + 
			"       to_char(s.last_modify_date, 'yyyy-MM-dd')\n" + 
			"  from CBM_C_BASIS s\n" + 
			" where s.budget_item_id = "+budgetItemId+"\n" + 
			"   and s.is_use = 'Y'\n" + 
			"   and s.enterprise_code = '"+enterPriseCode+"'";
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		String sqlCount = "select count(1) from (" + sql + ")";
		
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(totalCount);
		return result;
	}

}