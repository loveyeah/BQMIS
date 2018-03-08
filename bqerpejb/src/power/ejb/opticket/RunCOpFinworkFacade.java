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
import power.ejb.opticket.bussiness.RunJOpFinwork;

@Stateless
public class RunCOpFinworkFacade implements RunCOpFinworkFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunCOpFinwork entity) throws CodeRepeatException {
		try {
			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
					.getOperateTaskId(), entity.getFinishWorkName())) {
				if (entity.getFinishWorkId() == null) {
					entity.setFinishWorkId(bll.getMaxId("RUN_C_OP_FINWORK",
							"finish_work_id"));
				}
				entity.setIsUse("Y");
				entity.setModifyDate(new Date());
				entityManager.persist(entity);
			} else {
				throw new CodeRepeatException("名称重复!");
			}

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(RunCOpFinwork entity) throws CodeRepeatException {
		try {
			entity.setIsUse("N");
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpFinwork update(RunCOpFinwork entity)
			throws CodeRepeatException {
		try {
			if (!this.checkNameForAdd(entity.getEnterpriseCode(), entity
					.getOperateTaskId(), entity.getFinishWorkName(), entity
					.getFinishWorkId())) {
				RunCOpFinwork result = entityManager.merge(entity);
				return result;
			} else {
				throw new CodeRepeatException("名称重复!");
			}
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunCOpFinwork findById(Long id) {
		LogUtil.log("finding RunCOpFinwork instance with id: " + id,
				Level.INFO, null);
		try {
			RunCOpFinwork instance = entityManager
					.find(RunCOpFinwork.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunCOpFinwork> findFinworkByTask(String enterpriseCode,
			Long operateTaskId) {
		String sql = "select t.* from RUN_C_OP_FINWORK t";
		String whereSql = " where t.is_use='Y'";
		if (enterpriseCode != null) {
			whereSql += " and t.enterprise_code='" + enterpriseCode + "'";
		}
		if (operateTaskId != null) {
			whereSql += " and t.operate_task_id=" + operateTaskId;
		}
		sql += whereSql + " order by t.display_no";
		return bll.queryByNativeSQL(sql, RunCOpFinwork.class);
	}

	private boolean checkNameForAdd(String enterpriseCode, Long operateTaskId,
			String finishWorkName, Long... finishWorkId) {
		boolean isSame = true;
		String sql = "select count(1) from RUN_C_OP_FINWORK t where t.enterprise_code= ? and t.finish_work_name= ? and t.operate_task_id=? and t.is_use='Y'";
		String whereSql = "";
		if (finishWorkId != null && finishWorkId.length > 0) {
			whereSql += " and t.finish_work_id<>" + finishWorkId[0];
		}
		sql += whereSql;
		if (Integer.parseInt(bll.getSingal(sql,
				new Object[] { enterpriseCode, finishWorkName, operateTaskId })
				.toString()) > 0) {
			isSame = true;
		} else {
			isSame = false;
		}
		return isSame;
	}

	private RunCOpFinwork updateModel(Map map) {
		RunCOpFinwork model = new RunCOpFinwork();
		Object finishWorkId = map.get("finishWorkId");
		Object finishWorkName = map.get("finishWorkName");
		Object operateTaskId = map.get("operateTaskId");
		Object displayNo = map.get("displayNo");
		Object checkStatus = map.get("checkStatus");
		Object memo = map.get("memo");
		if (finishWorkId == null || "".equals(finishWorkId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(finishWorkId.toString()));
		}
		if (operateTaskId != null) {
			model.setOperateTaskId(Long.parseLong(operateTaskId.toString()));
		}
		if (finishWorkName != null) {
			model.setFinishWorkName(finishWorkName.toString());
		}
		if (displayNo != null) {
			model.setDisplayNo(Long.parseLong(displayNo.toString()));
		}
		if (checkStatus != null) {
			model.setCheckStatus(checkStatus.toString());
		}
		if (memo != null) {
			model.setMemo(memo.toString());
		}
		return model;
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr, String currentMan, String enterpriseCod)
			throws CodeRepeatException {
		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_C_OP_FINWORK", "finish_work_id");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunCOpFinwork op = this.updateModel(map);
					if (op.getFinishWorkId() == null) {
						op.setFinishWorkId(count);
						op.setEnterpriseCode(enterpriseCod);
						op.setModifyBy(currentMan);
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
					RunCOpFinwork opUp = this.updateModel(mapUp);
					if (opUp.getFinishWorkId() != null) {
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
					RunCOpFinwork opDel = this.findById(Long.parseLong(ids[i]));
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
			throw new CodeRepeatException("名称重复");
		}
		return operatStatus;
	}
}