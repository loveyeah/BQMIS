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
import power.ejb.manage.budget.form.CapitalDetailForm;

/**
 * 资本性支出预算明细
 * 
 * @author liuyi 090824
 */
@Stateless
public class CbmJCapitalDetailFacade implements CbmJCapitalDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName = "CbmJCapitalFacade")
	protected CbmJCapitalFacadeRemote capitalRemote;

	/**
	 * 新增一条 资本性支出预算明细记录
	 */
	public void save(CbmJCapitalDetail entity) {
		LogUtil.log("saving CbmJCapitalDetail instance", Level.INFO, null);
		try {
			entity.setCapitalDetailId(bll.getMaxId("CBM_J_CAPITAL_DETAIL",
					"CAPITAL_DETAIL_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条资本性支出预算明细记录
	 */
	public void delete(CbmJCapitalDetail entity) {
		LogUtil.log("deleting CbmJCapitalDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(CbmJCapitalDetail.class, entity
					.getCapitalDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条或多条 资本性支出预算明细记录
	 */
	public void delete(String ids) {
		String sql = "update  CBM_J_CAPITAL_DETAIL a set a.is_use='N' \n"
				+ " where a.CAPITAL_DETAIL_ID in (" + ids + ") \n";
		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条资本性支出预算明细记录
	 */
	public CbmJCapitalDetail update(CbmJCapitalDetail entity) {
		LogUtil.log("updating CbmJCapitalDetail instance", Level.INFO, null);
		try {
			CbmJCapitalDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public CbmJCapitalDetail findById(Long id) {
		LogUtil.log("finding CbmJCapitalDetail instance with id: " + id,
				Level.INFO, null);
		try {
			CbmJCapitalDetail instance = entityManager.find(
					CbmJCapitalDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject findCapitalDetailList(String budgetTime,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.capital_id, \n" + "a.budget_time, \n"
				+ "a.work_flow_no, \n" + "a.work_flow_status, \n"
				+ "b.capital_detail_id, \n" + "b.project, \n"
				+ "b.material_cost, \n" + "b.working_cost, \n"
				+ "b.other_cost, \n" + "b.device_cost, \n" + "b.total_cost, \n"
				+ "b.memo, \n" + "b.is_use, \n" + "b.enterprise_code \n"
				+ "from CBM_J_CAPITAL a,CBM_J_CAPITAL_DETAIL b \n"
				+ "where a.capital_id=b.capital_id \n" + "and a.is_use='Y' \n"
				+ "and b.is_use='Y' \n" + "and a.enterprise_code='"
				+ enterpriseCode + "' \n" + "and b.enterprise_code='"
				+ enterpriseCode + "' \n" + "and a.budget_time='" + budgetTime
				+ "' \n";
		String sqlCount = "select count(*) from (" + sql + ") ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<CapitalDetailForm> arrlist = new ArrayList<CapitalDetailForm>();
		if (list != null && list.size() > 0) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				CapitalDetailForm form = new CapitalDetailForm();
				Object[] data = (Object[]) it.next();
				if (data[0] != null)
					form.setCapitalId(Long.parseLong(data[0].toString()));
				if (data[1] != null)
					form.setBudgetTime(data[1].toString());
				if (data[2] != null)
					form.setWorkFlowNo(Long.parseLong(data[2].toString()));
				if (data[3] != null)
					form.setWorkFlowStatus(data[3].toString());
				if (data[4] != null)
					form.setCapitalDetailId(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					form.setProject(data[5].toString());
				if (data[6] != null)
					form
							.setMaterialCost(Double.parseDouble(data[6]
									.toString()));
				if (data[7] != null)
					form.setWorkingCost(Double.parseDouble(data[7].toString()));
				if (data[8] != null)
					form.setOtherCost(Double.parseDouble(data[8].toString()));
				if (data[9] != null)
					form.setDeviceCost(Double.parseDouble(data[9].toString()));
				if (data[10] != null)
					form.setTotalCost(Double.parseDouble(data[10].toString()));
				if (data[11] != null)
					form.setMemo(data[11].toString());
				if (data[12] != null)
					form.setIsUse(data[12].toString());
				if (data[13] != null)
					form.setEnterpriseCode(data[13].toString());
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	public void saveCapitalDetails(List<CapitalDetailForm> addList,
			List<CapitalDetailForm> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			CapitalDetailForm flag = addList.get(0);
			CbmJCapital capital = new CbmJCapital();
			capital.setBudgetTime(flag.getBudgetTime());
			capital.setWorkFlowNo(flag.getWorkFlowNo());
			capital.setWorkFlowStatus(flag.getWorkFlowStatus());
			capital.setIsUse(flag.getIsUse());
			capital.setEnterpriseCode(flag.getEnterpriseCode());
			if (flag.getCapitalId() != null) {
				capital.setCapitalId(flag.getCapitalId());
				capitalRemote.update(capital);
			} else {
				capital.setCapitalId(capitalRemote.fingIdByTime(flag
						.getBudgetTime(), flag.getEnterpriseCode()));
				capital = capitalRemote.save(capital);
				entityManager.flush();
			}
			for (CapitalDetailForm form : addList) {
				CbmJCapitalDetail detail = new CbmJCapitalDetail();
				// detail.setCapitalDetailId(form.getCapitalDetailId());
				detail.setCapitalId(capital.getCapitalId());
				detail.setProject(form.getProject());
				detail.setMaterialCost(form.getMaterialCost());
				detail.setWorkingCost(form.getWorkingCost());
				detail.setOtherCost(form.getOtherCost());
				detail.setDeviceCost(form.getDeviceCost());
				detail.setTotalCost(form.getTotalCost());
				detail.setMemo(form.getMemo());
				detail.setIsUse(form.getIsUse());
				detail.setEnterpriseCode(form.getEnterpriseCode());
				this.save(detail);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (CapitalDetailForm form : updateList) {
				CbmJCapitalDetail detail = new CbmJCapitalDetail();
				detail.setCapitalDetailId(form.getCapitalDetailId());
				detail.setCapitalId(form.getCapitalId());
				detail.setProject(form.getProject());
				detail.setMaterialCost(form.getMaterialCost());
				detail.setWorkingCost(form.getWorkingCost());
				detail.setOtherCost(form.getOtherCost());
				detail.setDeviceCost(form.getDeviceCost());
				detail.setTotalCost(form.getTotalCost());
				detail.setMemo(form.getMemo());
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