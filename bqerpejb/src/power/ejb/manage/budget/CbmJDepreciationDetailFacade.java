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
import power.ejb.manage.budget.form.CbmJDepreciationForm;

/**
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class CbmJDepreciationDetailFacade implements
		CbmJDepreciationDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "CbmJDepreciationFacade")
	protected CbmJDepreciationFacadeRemote depRemote;

	public void save(CbmJDepreciationDetail entity) {
		LogUtil.log("saving CbmJDepreciationDetail instance", Level.INFO, null);
		try {
			if (entity.getDepreciationDetailId() == null) {
				entity.setDepreciationDetailId(bll.getMaxId(
						"CBM_J_DEPRECIATION_DETAIL", "depreciation_detail_id"));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(CbmJDepreciationDetail entity) {
		LogUtil.log("deleting CbmJDepreciationDetail instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(CbmJDepreciationDetail.class,
					entity.getDepreciationDetailId());
			entityManager.remove(entity);
			String sql = "update CBM_J_DEPRECIATION_DETAIL t\n"
					+ "set t.is_use='N'\n"
					+ "where t.depreciation_detail_id  = "
					+ entity.getDepreciationDetailId() + "";
			bll.exeNativeSQL(sql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String ids) {
		String sql = "update  CBM_J_DEPRECIATION_DETAIL a set a.is_use='N' \n"
				+ " where a.depreciation_detail_id in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}

	public CbmJDepreciationDetail update(CbmJDepreciationDetail entity) {
		LogUtil.log("updating CbmJDepreciationDetail instance", Level.INFO,
				null);
		try {
			CbmJDepreciationDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJDepreciationDetail findById(Long id) {
		LogUtil.log("finding CbmJDepreciationDetail instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJDepreciationDetail instance = entityManager.find(
					CbmJDepreciationDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAllList(String budgetTime, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select * from\n"
				+ "    (select b.*,a.budget_time,a.work_flow_no,a.work_flow_status\n"
				+ "    from CBM_J_DEPRECIATION a,CBM_J_DEPRECIATION_DETAIL b\n"
				+ "      where a.depreciation_id = b.depreciation_id\n"
				+ "      and a.is_use = 'Y'\n" + "      and b.is_use = 'Y'\n"
				+ "      and a.budget_time = '"
				+ budgetTime
				+ "'\n"
				+ "      and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "      and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "      union\n"
				+ "      select 0,null,'合计',sum(b.last_asset),sum(b.add_asset),\n"
				+ "      sum(b.reduce_asset),sum(b.new_asset),999999999999999,\n"
				+ "      sum(b.depreciation_number),sum(b.depreciation_sum),'','','','',null,''\n"
				+ "      from CBM_J_DEPRECIATION a,CBM_J_DEPRECIATION_DETAIL b\n"
				+ "      where a.depreciation_id = b.depreciation_id\n"
				+ "      and a.is_use = 'Y'\n"
				+ "      and b.is_use = 'Y'\n"
				+ "      and a.budget_time = '"
				+ budgetTime
				+ "'\n"
				+ "      and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "      and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "    )tt";

		String sqlCount = "select count(1)\n"
				+ "  from CBM_J_DEPRECIATION a, CBM_J_DEPRECIATION_DETAIL b\n"
				+ " where a.depreciation_id = b.depreciation_id\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and a.budget_time = '" + budgetTime + "'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "'";

		sql += " order by tt.depreciation_detail_id desc";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List arraylist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			CbmJDepreciationForm modelDetail = new CbmJDepreciationForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				modelDetail.setDepreciationDetailId(Long.parseLong(data[0]
						.toString()));
			if (data[1] != null)
				modelDetail.setDepreciationId(Long
						.parseLong(data[1].toString()));
			if (data[2] != null)
				modelDetail.setAssetName(data[2].toString());
			if (data[3] != null)
				modelDetail
						.setLastAsset(Double.parseDouble(data[3].toString()));
			if (data[4] != null)
				modelDetail.setAddAsset(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				modelDetail.setReduceAsset(Double.parseDouble(data[5]
						.toString()));
			if (data[6] != null) {
				modelDetail.setNewAsset(Double.parseDouble(data[6].toString()));
				modelDetail.setNewAssetCount(Double.parseDouble(data[3]
						.toString())
						+ Double.parseDouble(data[4].toString())
						- Double.parseDouble(data[5].toString()));
			}
			if (data[7] != null)
				modelDetail.setDepreciationRate(Double.parseDouble(data[7]
						.toString()));
			if (data[8] != null)
				modelDetail.setDepreciationNumber(Double.parseDouble(data[8]
						.toString()));
			if (data[9] != null)
				modelDetail.setDepreciationSum(Double.parseDouble(data[9]
						.toString()));
			if (data[10] != null)
				modelDetail.setMemo(data[10].toString());
			if (data[11] != null)
				modelDetail.setIsUse(data[11].toString());
			if (data[12] != null)
				modelDetail.setEnterpriseCode(data[12].toString());
			if (data[13] != null)
				modelDetail.setBudgetTime(data[13].toString());
			if (data[14] != null)
				modelDetail.setWorkFlowNo(Long.parseLong(data[14].toString()));
			if (data[15] != null)
				modelDetail.setWorkFlowStatus(data[15].toString());
			arraylist.add(modelDetail);
		}
		pg.setList(arraylist);
		if (list != null) {
			pg
					.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
							.toString()));
		}
		return pg;
	}

	public void saveDepreciationDetails(List<CbmJDepreciationForm> addList,
			List<CbmJDepreciationForm> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			CbmJDepreciationForm flag = addList.get(0);
			CbmJDepreciation model = new CbmJDepreciation();
			model.setBudgetTime(flag.getBudgetTime());
			model.setWorkFlowNo(flag.getWorkFlowNo());
			model.setWorkFlowStatus(flag.getWorkFlowStatus());
			model.setIsUse(flag.getIsUse());
			model.setEnterpriseCode(flag.getEnterpriseCode());
			if (flag.getDepreciationId() != null) {
				model.setDepreciationId(flag.getDepreciationId());
				depRemote.update(model);
			} else {
				model.setDepreciationId(depRemote.getIdByTime(flag
						.getBudgetTime(), flag.getEnterpriseCode()));
				model = depRemote.save(model);

				entityManager.flush();
			}
			for (CbmJDepreciationForm form : addList) {
				CbmJDepreciationDetail detail = new CbmJDepreciationDetail();

				detail.setDepreciationId(model.getDepreciationId());
				detail.setAssetName(form.getAssetName());
				detail.setAddAsset(form.getAddAsset());
				detail.setDepreciationNumber(form.getDepreciationNumber());
				detail.setDepreciationRate(form.getDepreciationRate());
				detail.setDepreciationSum(form.getDepreciationSum());
				detail.setLastAsset(form.getLastAsset());
				detail.setMemo(form.getMemo());
				detail.setNewAsset(form.getNewAsset());
				detail.setReduceAsset(form.getReduceAsset());
				detail.setIsUse(form.getIsUse());
				detail.setEnterpriseCode(form.getEnterpriseCode());
				this.save(detail);

				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (CbmJDepreciationForm form : updateList) {
				CbmJDepreciationDetail detail = new CbmJDepreciationDetail();

				detail.setDepreciationDetailId(form.getDepreciationDetailId());
				detail.setDepreciationId(form.getDepreciationId());
				detail.setAssetName(form.getAssetName());
				detail.setAddAsset(form.getAddAsset());
				detail.setDepreciationNumber(form.getDepreciationNumber());
				detail.setDepreciationRate(form.getDepreciationRate());
				detail.setDepreciationSum(form.getDepreciationSum());
				detail.setLastAsset(form.getLastAsset());
				detail.setMemo(form.getMemo());
				detail.setNewAsset(form.getNewAsset());
				detail.setReduceAsset(form.getReduceAsset());
				detail.setIsUse(form.getIsUse());
				detail.setEnterpriseCode(form.getEnterpriseCode());

				this.update(detail);
			}
		}
		if (ids != null && ids.length() > 0) {
			this.delete(ids);
		}

	}
}