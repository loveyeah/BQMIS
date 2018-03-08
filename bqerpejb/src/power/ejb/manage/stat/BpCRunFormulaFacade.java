package power.ejb.manage.stat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.stat.form.BpCRunFormulaForm;

/**
 * Facade for entity BpCRunFormula.
 * 
 * @see power.ejb.manage.stat.BpCRunFormula
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpCRunFormulaFacade implements BpCRunFormulaFacadeRemote {
	// property constants
	public static final String OPERATOR_CODE = "operatorCode";
	public static final String DERIVE_DATA_TYPE = "deriveDataType";
	public static final String SD_TYPE = "sdType";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;

	protected boolean save(BpCRunFormula entity) {
		try {
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	protected boolean delete(String itemCode) {
		try {

			String sql = "DELETE FROM bp_c_run_formula t\n"
					+ " WHERE t.item_code='" + itemCode + "'";
			dll.exeNativeSQL(sql);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	protected boolean update(BpCRunFormula entity) {
		try {
			entityManager.merge(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getRunFormulaList(String itemCode, String deriveDataType,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		String sql = "SELECT t.item_code,\n" + "       t.run_data_code,\n"
				+ "       t.operator_code,\n" + "       t.derive_data_type,\n"
				+ "       t.sd_type,\n"
				+ "       getitemname(t.item_code) item_code_name,\n"
				+ "       getitemname(t.run_data_code) run_data_code_name,\n"
				+ "  t.enterprise_code ,t.display_no "
				+ "  FROM bp_c_run_formula t\n" + " WHERE t.item_code = '"
				+ itemCode + "'\n" + "   AND t.derive_data_type = '"
				+ deriveDataType + "'\n" + "   AND t.enterprise_code = '"
				+ enterpriseCode + "'" + " order by t.display_no ";

		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			BpCRunFormulaForm model = new BpCRunFormulaForm();
			BpCRunFormula bodel = new BpCRunFormula();
			BpCRunFormulaId idmodel = new BpCRunFormulaId();
			Object[] data = (Object[]) it.next();
			idmodel.setItemCode(itemCode);
			if (data[1] != null)
				idmodel.setRunDataCode(data[1].toString());
			if (data[2] != null)
				bodel.setOperatorCode(data[2].toString());
			if (data[3] != null)
				bodel.setDeriveDataType(data[3].toString());
			if (data[4] != null)
				bodel.setSdType(data[4].toString());
			if (data[5] != null)
				model.setItemCodeName(data[5].toString());
			if (data[6] != null)
				model.setRunDataCodeName(data[6].toString());

			if (data[7] != null)
				bodel.setEnterpriseCode(data[7].toString());
			if (data[8] != null)
				bodel.setDisplayNo(data[8].toString());

			bodel.setId(idmodel);
			model.setRunFormulaInfo(bodel);
			arrlist.add(model);
		}
		result.setList(arrlist);
		return result;
	}

	public String getRunFormulaVchar(String itemCode, String enterpriseCode) {
		String sql = "SELECT GETREWORDRUNFORMULA('" + itemCode + "', '"
				+ enterpriseCode + "') reword\n" + "  FROM dual";
		Object data = dll.getSingal(sql);
		return data.toString();
	}

	public boolean saveRunFormulaList(String itemCode,
			List<BpCRunFormula> addList) {
		try {
			delete(itemCode);
			if (addList != null && addList.size() > 0) {
				for (BpCRunFormula entity : addList) {
					BpCRunFormulaId id = new BpCRunFormulaId();
					id.setItemCode(itemCode);
					id.setRunDataCode(entity.getId().getRunDataCode());
					entity.setId(id);
					this.save(entity);
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}