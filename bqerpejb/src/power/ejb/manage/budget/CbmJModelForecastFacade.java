package power.ejb.manage.budget;

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
import power.ejb.manage.budget.form.ForecastItemForm;
import power.ejb.manage.budget.form.ModelForecastRecordForm;

/**
 * 预测指标数据表
 * 
 * @author liuyi 090826
 */
@Stateless
public class CbmJModelForecastFacade implements CbmJModelForecastFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条预测指标数据表记录
	 */
	public CbmJModelForecast save(CbmJModelForecast entity) {
		LogUtil.log("saving CbmJModelForecast instance", Level.INFO, null);
		try {
			entity.setForecastId(bll.getMaxId("CBM_J_MODEL_FORECAST",
					"FORECAST_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;
	}

	/**
	 * 删除一条预测指标数据表记录
	 */
	public void delete(CbmJModelForecast entity) {
		LogUtil.log("deleting CbmJModelForecast instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJModelForecast.class, entity
					.getForecastId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条预测指标数据表记录
	 */
	public CbmJModelForecast update(CbmJModelForecast entity) {
		LogUtil.log("updating CbmJModelForecast instance", Level.INFO, null);
		try {
			CbmJModelForecast result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJModelForecast findById(Long id) {
		LogUtil.log("finding CbmJModelForecast instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJModelForecast instance = entityManager.find(
					CbmJModelForecast.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findAllModelData(String forecastTime, String modelType,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.model_item_id, \n" + "a.model_item_code, \n"
				+ "a.model_item_name, \n" + "a.model_type, \n"
				+ "a.is_item, \n" + "getunitname(a.unit_id), \n"
				+ "a.come_from, \n" + "a.model_order, \n" + "a.display_no, \n"
				+ "a.model_item_explain, \n" + "b.forecast_id, \n"
				+ "b.forecast_time, \n" + "b.forecast_value \n"
				+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
				+ "where a.model_item_id=b.model_item_id \n"
				+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
				+ enterpriseCode + "' \n" + " and b.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
				+ "and a.model_type='" + modelType + "' \n"
				+ " and b.forecast_time='" + forecastTime + "' \n"
				+ "order by a.display_no ";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arrlist = new ArrayList<ForecastItemForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ForecastItemForm form = new ForecastItemForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setModelItemId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setModelItemCode(data[1].toString());
				if (data[2] != null)
					form.setModelItemName(data[2].toString());
				if (data[3] != null)
					form.setModelType(data[3].toString());
				if (data[4] != null)
					form.setIsItem(data[4].toString());
				if (data[5] != null)
					form.setUnitName(data[5].toString());
				if (data[6] != null)
					form.setComeFrom(data[6].toString());
				if (data[7] != null)
					form.setModelOrder(Long.parseLong(data[7].toString()));
				if (data[8] != null)
					form.setDisplayNo(Long.parseLong(data[8].toString()));
				if (data[9] != null)
					form.setModelItemExplain(data[9].toString());
				if (data[10] != null)
					form.setForecastId(Long.parseLong(data[10].toString()));
				if (data[11] != null)
					form.setForecastTime(data[11].toString());
				if (data[12] != null)
					form.setForecastValue(Double.parseDouble(data[12]
							.toString()));
				else
					form.setForecastValue(0.0);
				arrlist.add(form);
			}
		} else {
			sql = "select a.model_item_id, \n" + "a.model_item_code, \n"
					+ "a.model_item_name, \n" + "a.model_type, \n"
					+ "a.is_item, \n" + "getunitname(a.unit_id), \n"
					+ "a.come_from, \n" + "a.model_order, \n"
					+ "a.display_no, \n" + "a.model_item_explain \n"
					+ " from CBM_C_MODEL a \n" + "where a.is_use='Y' \n"
					+ " and a.enterprise_code='" + enterpriseCode + "' \n"
					+ "and a.is_item='Y' \n" + "and a.model_type='" + modelType
					+ "' \n" + "order by a.display_no ";
			sqlCount = "select count(*) from (" + sql + ") \n";
			list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			arrlist = new ArrayList<ForecastItemForm>();
			if (list != null && list.size() > 0) {
				Iterator it = list.iterator();
				while (it.hasNext()) {
					ForecastItemForm form = new ForecastItemForm();
					Object[] data = (Object[]) it.next();
					if (data[0] != null)
						form.setModelItemId(Long.parseLong(data[0].toString()));
					if (data[1] != null)
						form.setModelItemCode(data[1].toString());
					if (data[2] != null)
						form.setModelItemName(data[2].toString());
					if (data[3] != null)
						form.setModelType(data[3].toString());
					if (data[4] != null)
						form.setIsItem(data[4].toString());
					if (data[5] != null)
						form.setUnitName(data[5].toString());
					if (data[6] != null)
						form.setComeFrom(data[6].toString());
					if (data[7] != null)
						form.setModelOrder(Long.parseLong(data[7].toString()));
					if (data[8] != null)
						form.setDisplayNo(Long.parseLong(data[8].toString()));
					if (data[9] != null)
						form.setModelItemExplain(data[9].toString());
					form.setForecastTime(forecastTime);
					arrlist.add(form);
				}
			}

		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	/**
	 * 量本利模型分析页面数据
	 */
	public PageObject findModelAnalysisData(String forecastTime,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select distinct a.model_item_code, \n"
				+ "a.model_item_name, \n" + "getunitname(a.unit_id) \n"
				+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
				+ "where a.model_item_id=b.model_item_id \n"
				+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
				+ enterpriseCode + "' \n" + " and b.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
				+ " and b.forecast_time='" + forecastTime + "' \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<ModelForecastRecordForm> arrlist = new ArrayList<ModelForecastRecordForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				ModelForecastRecordForm form = new ModelForecastRecordForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setModelItemCode(data[0].toString());
				if (data[1] != null)
					form.setModelItemName(data[1].toString());
				if (data[2] != null)
					form.setUnitName(data[2].toString());
				arrlist.add(form);
			}
		}
		if (arrlist != null && arrlist.size() > 0) {
			for (int i = 0; i <= arrlist.size() - 1; i++) {
				String firSql = "select b.forecast_value \n"
						+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
						+ "where a.model_item_id=b.model_item_id \n"
						+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
						+ enterpriseCode + "' \n" + " and b.enterprise_code='"
						+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
						+ " and b.forecast_time='" + forecastTime + "' \n"
						+ "and a.model_item_code='"
						+ arrlist.get(i).getModelItemCode() + "' \n"
						+ "and a.model_type='1' \n";
				if (bll.getSingal(firSql) != null)
					arrlist.get(i).setFirstValue(
							Double
									.parseDouble(bll.getSingal(firSql)
											.toString()));
				else
					arrlist.get(i).setFirstValue(0.0);
				String secSql = "select b.forecast_value \n"
						+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
						+ "where a.model_item_id=b.model_item_id \n"
						+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
						+ enterpriseCode + "' \n" + " and b.enterprise_code='"
						+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
						+ " and b.forecast_time='" + forecastTime + "' \n"
						+ "and a.model_item_code='"
						+ arrlist.get(i).getModelItemCode() + "' \n"
						+ "and a.model_type='2' \n";
				if (bll.getSingal(secSql) != null)
					arrlist.get(i).setSecondValue(
							Double
									.parseDouble(bll.getSingal(secSql)
											.toString()));
				else
					arrlist.get(i).setSecondValue(0.0);
				String thiSql = "select b.forecast_value \n"
						+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
						+ "where a.model_item_id=b.model_item_id \n"
						+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
						+ enterpriseCode + "' \n" + " and b.enterprise_code='"
						+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
						+ " and b.forecast_time='" + forecastTime + "' \n"
						+ "and a.model_item_code='"
						+ arrlist.get(i).getModelItemCode() + "' \n"
						+ "and a.model_type='3' \n";
				if (bll.getSingal(thiSql) != null)
					arrlist.get(i).setThirdValue(
							Double
									.parseDouble(bll.getSingal(thiSql)
											.toString()));
				else
					arrlist.get(i).setThirdValue(0.0);
				String forSql = "select b.forecast_value \n"
						+ " from CBM_C_MODEL a,CBM_J_MODEL_FORECAST b \n"
						+ "where a.model_item_id=b.model_item_id \n"
						+ "and a.is_use='Y' \n" + " and a.enterprise_code='"
						+ enterpriseCode + "' \n" + " and b.enterprise_code='"
						+ enterpriseCode + "' \n" + "and a.is_item='Y' \n"
						+ " and b.forecast_time='" + forecastTime + "' \n"
						+ "and a.model_item_code='"
						+ arrlist.get(i).getModelItemCode() + "' \n"
						+ "and a.model_type='4' \n";
				if (bll.getSingal(forSql) != null)
					arrlist.get(i).setForthValue(
							Double
									.parseDouble(bll.getSingal(forSql)
											.toString()));
				else
					arrlist.get(i).setForthValue(0.0);
				arrlist.get(i).setAverageValue(
						(arrlist.get(i).getFirstValue()
								+ arrlist.get(i).getSecondValue()
								+ arrlist.get(i).getThirdValue() + arrlist.get(
								i).getForthValue()) / 4);

			}
		}
		pg.setList(arrlist);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		return pg;
	}

	public void saveForecastItem(List<ForecastItemForm> modList) {
		if (modList != null && modList.size() > 0) {
			for (ForecastItemForm form : modList) {
				if (form.getForecastId() == null) {
					CbmJModelForecast entity = new CbmJModelForecast();
					entity.setModelItemId(form.getModelItemId());
					entity.setForecastTime(form.getForecastTime());
					entity.setForecastValue(form.getForecastValue());
					entity.setEnterpriseCode(form.getEnterpriseCode());
					entity = this.save(entity);
					entityManager.flush();
					form.setForecastId(entity.getForecastId());

				} else {
					CbmJModelForecast entity = this.findById(form
							.getForecastId());
					entity.setForecastValue(form.getForecastValue());
					this.update(entity);
					entityManager.flush();
				}
			}

			// for(ForecastItemForm form : modList)
			// {
			this.calForecastItemValue(modList.get(0).getForecastTime(), modList
					.get(0).getModelType());
			// }
		}
	}

	public void calForecastItemValue(String budgetTime, String modelType) {
		// String [] itemIds=new String[15];
		List<CbmCModel> list = new ArrayList<CbmCModel>();

		String sql = "select t.MODEL_ITEM_ID,t.MODEL_ORDER from CBM_C_MODEL t,CBM_J_MODEL_FORECAST a\n"
				+ "where t.MODEL_ITEM_ID= a.MODEL_ITEM_ID \n"
				+ " and t.MODEL_TYPE='"
				+ modelType
				+ "' \n"
				+ " and a.FORECAST_TIME='"
				+ budgetTime
				+ "' \n"
				+ "and t.MODEL_ORDER <> 1  and t.come_from='2'\n"
				+ "order by t.MODEL_ORDER asc";
		List objList = bll.queryByNativeSQL(sql);
		Iterator it = objList.iterator();
		while (it.hasNext()) {
			CbmCModel entity = new CbmCModel();
			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				entity.setModelItemId(Long.parseLong(data[0].toString()));
				list.add(entity);
			}

		}
		for (CbmCModel itemModel : list) {

			String sqlValue = "select GetModelItemValue('" + budgetTime + "',"
					+ itemModel.getModelItemId() + ",'" + modelType
					+ "') from dual";
			String itemValue = bll.getSingal(sqlValue).toString();
			String sqlUpdate = "update CBM_J_MODEL_FORECAST b\n"
					+ "set b.FORECAST_VALUE=" + itemValue + " \n" +
					// "where b.MODEL_ITEM_ID="+modelItemId+" and
					// b.MODEL_ITEM_ID="+itemModel.getModelItemId();
					"where  b.MODEL_ITEM_ID=" + itemModel.getModelItemId();
			bll.exeNativeSQL(sqlUpdate);
			entityManager.flush();

		}

	}
}