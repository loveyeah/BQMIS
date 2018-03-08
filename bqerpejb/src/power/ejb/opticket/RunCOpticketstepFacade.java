package power.ejb.opticket;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class RunCOpticketstepFacade implements RunCOpticketstepFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunCOpticketstep entity) throws CodeRepeatException {
		try {
//			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
//					.getOperateTaskId(), entity.getOperateStepName())) {
				if (entity.getOperateStepId() == null) {
					entity.setOperateStepId(bll.getMaxId("RUN_C_OPTICKETSTEP",
							"operate_step_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
//			} else {
//				throw new CodeRepeatException("名称重复!");
//			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(RunCOpticketstep entity) {
		try {
			entity.setIsUse("N");
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpticketstep update(RunCOpticketstep entity)
			throws CodeRepeatException {
		try {
//			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
//					.getOperateTaskId(), entity.getOperateStepName(), entity
//					.getOperateStepId())) {
				RunCOpticketstep result = entityManager.merge(entity);
				return result;
//			} else {
//				throw new CodeRepeatException("名称重复!");
//			}
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpticketstep findById(Long id) {
		LogUtil.log("finding RunCOpticketstep instance with id: " + id,
				Level.INFO, null);
		try {
			RunCOpticketstep instance = entityManager.find(
					RunCOpticketstep.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCOpticketstep> findFinworkByTask(String enterpriseCode,
			Long operateTaskId) {
		String sql = "select t.* from RUN_C_OPTICKETSTEP t";
		String whereSql = " where t.is_use='Y'";
		if (enterpriseCode != null) {
			whereSql += " and t.enterprise_code='" + enterpriseCode + "'";
		}
		if (operateTaskId != null) {
			whereSql += " and t.operate_task_id=" + operateTaskId;
		}
		sql += whereSql + " order by t.display_no";
		return bll.queryByNativeSQL(sql, RunCOpticketstep.class);
	}

	private boolean checkNameForAdd(String enterpriseCode, Long operateTaskId,
			String operateStepName, Long... operateStepId) {
		boolean isSame = true;
		String sql = "select count(1) from RUN_C_OPTICKETSTEP t where t.enterprise_code=? and t.operate_step_name=? and t.operate_task_id=? and t.is_use='Y'";
		String whereSql = "";
		if (operateStepId != null && operateStepId.length > 0) {
			whereSql += " and t.operate_step_id<>" + operateStepId[0];
		}
		sql += whereSql;
		if (Integer.parseInt(bll
				.getSingal(
						sql,
						new Object[] { enterpriseCode, operateStepName,
								operateTaskId }).toString()) > 0) {
			isSame = true;
		} else {
			isSame = false;
		}
		return isSame;
	}

	private RunCOpticketstep updateModel(Map map) {
		RunCOpticketstep model = new RunCOpticketstep();
		Object operateStepId = map.get("operateStepId");
		Object operateTaskId = map.get("operateTaskId");
		Object operateStepName = map.get("operateStepName");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		if (operateStepId == null || "".equals(operateStepId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(operateStepId.toString()));
		}
		if (operateTaskId != null) {
			model.setOperateTaskId(Long.parseLong(operateTaskId.toString()));
		}
		if (operateStepName != null) {
			model.setOperateStepName(operateStepName.toString());
		}
		if (displayNo != null) {
			model.setDisplayNo(Long.parseLong(displayNo.toString()));
		}
		if (memo != null) {
			model.setMemo(memo.toString());
		}
		return model;
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr,String currentMan,String enterpriseCod) throws CodeRepeatException {
		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_C_OPTICKETSTEP",
						"operate_step_id");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunCOpticketstep op = this.updateModel(map);
					if (op.getOperateStepId() == null) {
						op.setEnterpriseCode(enterpriseCod);
						op.setModifyBy(currentMan);
						op.setOperateStepId(count);
						this.save(op);
						if (totalAdd > maxSize) {
							if ((i + 1) % maxSize == 0) {
								entityManager.flush();
							}
						}
					}
				}
			}
			if (updateList != null) {
				int totalUpdate = updateList.size();
				for (int i = 0; i < totalUpdate; i++) {
					Map mapUp = updateList.get(i);
					RunCOpticketstep opUp = this.updateModel(mapUp);
					if (opUp.getOperateStepId() != null) {
						opUp.setEnterpriseCode(enterpriseCod);
						opUp.setModifyBy(currentMan);
						this.update(opUp);
						if (totalUpdate > maxSize) {
							if ((i + 1) % maxSize == 0) {
								entityManager.flush();
							}
						}
					}
				}
			}
			if (delStr.length() > 1) {
				String[] ids = delStr.split(",");
				for (int i = 0; i < ids.length; i++) {
					RunCOpticketstep opDel = this.findById(Long
							.parseLong(ids[i]));
					this.delete(opDel);
					if (ids.length > maxSize) {
						if ((i + 1) % maxSize == 0) {
							entityManager.flush();
						}
					}
				}
			}
			operatStatus = true;
		} catch (Exception e) {
			e.printStackTrace();
			operatStatus = false;
			return operatStatus;
		}

		return operatStatus;

	}
}