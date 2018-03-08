package power.ejb.manage.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.budget.form.ModelFormulaForm;

/**
 * Facade for entity CbmCModelFormula.
 * 
 * @see power.ejb.manage.budget.CbmCModelFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmCModelFormulaFacade implements CbmCModelFormulaFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(CbmCModelFormula entity) {
		try {
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void save(List<CbmCModelFormula> addList) {
		Long id = bll.getMaxId("CBM_C_MODEL_FORMULA", "MODEL_FORMULA_ID");
		if (addList != null && addList.size() > 0) {
			for (CbmCModelFormula entity : addList) {
				entity.setModelFormulaId(id++);
				this.save(entity);
			}
		}

	}

	public void delete(CbmCModelFormula entity) {
		try {
			entity = entityManager.getReference(CbmCModelFormula.class, entity
					.getModelFormulaId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String modelItemId) {
		String sql = "delete from CBM_C_MODEL_FORMULA where MODEL_ITEM_ID = ?";
		bll.exeNativeSQL(sql, new Object[] { modelItemId });
	}

	public CbmCModelFormula update(CbmCModelFormula entity) {
		try {
			CbmCModelFormula result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmCModelFormula findById(Long id) {
		try {
			CbmCModelFormula instance = entityManager.find(
					CbmCModelFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CbmCModelFormula> findByProperty(String propertyName,
			final Object value) {
		try {
			final String queryString = "select model from CbmCModelFormula model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all CbmCModelFormula entities.
	 * 
	 * @return List<CbmCModelFormula> all CbmCModelFormula entities
	 */
	@SuppressWarnings("unchecked")
	public List<ModelFormulaForm> findAll(String enterpriseCode,
			String modelItemId) {

		String sql = "select t.MODEL_FORMULA_ID,\n"
				+ "       t.formula_no,\n"
				+ "       t.formula_content,\n"
				+ "       decode(t.FORNULA_TYPE,'1',"
				+ "       (getitemname( t.formula_content)),'2',t.formula_content,'3',t.formula_content) formulaCotent,\n"
				+ "       t.fornula_type \n" + "  from CBM_C_MODEL_FORMULA t\n"
				+ " where t.MODEL_ITEM_ID = ?\n"
				+ "   and t.enterprise_code = ?";
		List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
				modelItemId, enterpriseCode });
		if (list != null) {
			List<ModelFormulaForm> result = new ArrayList<ModelFormulaForm>();
			for (Object[] o : list) {
				ModelFormulaForm m = new ModelFormulaForm();
				if (o[0] != null) {
					m.setModelFormulaId(o[0].toString());
				}
				if (o[1] != null) {
					m.setFormulaNo(o[1].toString());
				}
				if (o[2] != null) {
					m.setRowItemCode(o[2].toString());
				}
				if (o[3] != null) {
					m.setFormulaContent(o[3].toString());
				}
				if (o[4] != null) {
					m.setFornulaType(o[4].toString());
				}
				result.add(m);
			}
			return result;
		}
		return null;
	}

}