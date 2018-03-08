package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.stat.form.StatItemFormula;

/**
 * Facade for entity BpCExtendFormula.
 * 
 * @see power.ejb.manage.stat.BpCExtendFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCExtendFormulaFacade implements BpCExtendFormulaFacadeRemote {
	// property constants
	public static final String FORMULA_CONTENT = "formulaContent";
	public static final String FORNULA_TYPE = "fornulaType";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "BpCStatItemFacade")
	protected BpCStatItemFacadeRemote statRemote;

	/**
	 * 保存
	 */
	public void save(BpCExtendFormula entity) {
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public boolean checkFormulaCorrect(String sqlContent){
		String hql = "select %s from dual";
		hql = String.format(hql, sqlContent);
		try{ 
			Query query = entityManager.createNativeQuery(hql);
			query.getSingleResult(); 
			return true;
		}catch(Exception exc)
		{
			System.out.println("检测公式，执行的sql:"+hql);
			return false;
		}
	} 
	/**
	 * 保存
	 */
	public void save(List<BpCExtendFormula> addList) {

		if (addList != null && addList.size() > 0) {
			for (BpCExtendFormula entity : addList) {
				this.save(entity);
			}
		}
	}

	public void delete(BpCExtendFormula entity) {
		LogUtil.log("deleting BpCExtendFormula instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpCExtendFormula.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean delete(String itemCode) {
		try {
			String hql = "delete from BpCExtendFormula m where m.id.itemCode=:code";
			Query query = entityManager.createQuery(hql);
			query.setParameter("code", itemCode);
			query.executeUpdate();
			entityManager.flush();
//			String sql = "delete from bp_c_extend_formula where item_code = ?";
//			bll.exeNativeSQL(sql, new Object[] { itemCode });
			return true;
		} catch (RuntimeException re) {
			re.printStackTrace();
			return false;
		}
	}

	public boolean deleteFormula(String itemCode, String enterpriseCode) {
		try {
			String sql = "delete from bp_c_extend_formula t"
					+ " where t.item_code='" + itemCode + "'"
					+ " and t.enterprise_code='" + enterpriseCode + "'";
			bll.exeNativeSQL(sql);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public BpCExtendFormula findById(BpCExtendFormulaId id) {
		LogUtil.log("finding BpCExtendFormula instance with id: " + id,
				Level.INFO, null);
		try {
			BpCExtendFormula instance = entityManager.find(
					BpCExtendFormula.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpCExtendFormula entities.
	 * 
	 * @return List<BpCExtendFormula> all BpCExtendFormula entities
	 */
	@SuppressWarnings("unchecked")
	public List<StatItemFormula> findAll(String enterpriseCode, String itemCode) {
		try {
			String sql = "select t.formula_no,\n"
					+ "       t.formula_content,\n"
					+ "       nvl(getstatitemname(t.formula_content), t.formula_content),\n"
					+ "       t.fornula_type\n"
					+ "  from bp_c_extend_formula t\n"
					+ " where t.item_code = ?\n"
					+ "   and t.enterprise_code = ?";
			List<Object[]> list = bll.queryByNativeSQL(sql, new Object[] {
					itemCode, enterpriseCode });
			if (list != null) {
				List<StatItemFormula> result = new ArrayList<StatItemFormula>();
				for (Object[] o : list) {
					StatItemFormula m = new StatItemFormula();
					m.setItemCode(itemCode);
					if (o[0] != null) {
						m.setFormulaNo(o[0].toString());
					}
					 if (o[1] != null) {
					 m.setRowItemCode(o[1].toString());
					 }
//					 by xlhe,2009/08/07

					if (o[2] != null) {
						m.setFormulaContent(o[2].toString());
					}
					if (o[3] != null) {
						m.setFornulaType(o[3].toString());
					}
					result.add(m);
				}
				return result;
			}
			return null;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public PageObject findAllStatItem(String argFuzzy, String dataTimeType,
			String itemType, int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		if ("".equals(argFuzzy)) {
			argFuzzy = "%";
		}

		String sql = "select t.*\n" + "  from bp_c_stat_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ " and t.is_item='Y'";
		String sqlCount = "select count(*)\n" + "  from bp_c_stat_item t\n"
				+ " where (t.item_name like '%" + argFuzzy + "%'\n"
				+ "   or t.item_code like '%" + argFuzzy + "%')"
				+ "and t.is_item='Y'";
		if (dataTimeType != null && !dataTimeType.equals("")) {
			String str = " and t.data_time_type='" + dataTimeType + "'";
			sql += str;
			sqlCount += str;
		}
		if (itemType != null && !itemType.equals("")) {
			String str = " and t.item_type='" + itemType + "'";
			sql += str;
			sqlCount += str;
		}
		List<BpCStatItem> list = bll.queryByNativeSQL(sql, BpCStatItem.class,
				rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		result.setList(list);
		result.setTotalCount(count);

		return result;
	}

}