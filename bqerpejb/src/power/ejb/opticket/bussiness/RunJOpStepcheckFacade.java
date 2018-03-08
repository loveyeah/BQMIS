package power.ejb.opticket.bussiness;

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
import power.ejb.opticket.RunCOpStepcheck;

/**
 * Facade for entity RunJOpStepcheck.
 * 
 * @see power.ejb.opticket.bussiness.RunJOpStepcheck
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpStepcheckFacade implements RunJOpStepcheckFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunJOpStepcheck entity) {
		if (entity.getStepCheckId() == null) {
			entity.setStepCheckId(bll.getMaxId("RUN_J_OP_STEPCHECK",
					"step_check_id"));
		}
		entity.setRunAddFlag("Y");
		entityManager.persist(entity);
	}

	public void delete(RunJOpStepcheck entity) {
		LogUtil.log("deleting RunJOpStepcheck instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJOpStepcheck.class, entity
					.getStepCheckId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJOpStepcheck update(RunJOpStepcheck entity)
			throws CodeRepeatException {
		RunJOpStepcheck result = entityManager.merge(entity);
		return result;
	}

	public RunJOpStepcheck findById(Long id) {
		LogUtil.log("finding RunJOpStepcheck instance with id: " + id,
				Level.INFO, null);
		try {
			RunJOpStepcheck instance = entityManager.find(
					RunJOpStepcheck.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunJOpStepcheck> findByOpticketCode(String opticketCode) {
		String sql = "select t.* from RUN_J_OP_STEPCHECK t where t.opticket_code='"
				+ opticketCode + "' order by  t.DISPLAY_NO,t.step_check_id";
		return bll.queryByNativeSQL(sql, RunJOpStepcheck.class);
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr, String currentMan, String enterpriseCod) {

		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_J_OP_STEPCHECK", "STEP_CHECK_ID");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunJOpStepcheck op = this.updateModel(map);
					if (op.getStepCheckId() == null) {
						op.setStepCheckId(count);
						op.setCheckMan(currentMan);
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
					RunJOpStepcheck opUp = this.updateModel(mapUp);
					if (opUp.getStepCheckId() != null) {
						this.update(opUp);
						opUp.setCheckMan(currentMan);
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
					RunJOpStepcheck opDel = this.findById(Long
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

	private RunJOpStepcheck updateModel(Map map) {
		RunJOpStepcheck model = new RunJOpStepcheck();
		Object opticketCode = map.get("opticketCode");
		Object runAddFlag = map.get("runAddFlag");
		Object stepCheckId = map.get("stepCheckId");
		Object stepCheckName = map.get("stepCheckName");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		Object checkStatus = map.get("checkStatus");
		if (stepCheckId == null || "".equals(stepCheckId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(stepCheckId.toString()));
		}
		if (stepCheckName != null) {
			model.setStepCheckName(stepCheckName.toString());
		}
		if (displayNo != null) {
			model.setDisplayNo(Long.parseLong(displayNo.toString()));
		}
		if (memo != null) {
			model.setMemo(memo.toString());
		}
		if (opticketCode != null) {
			model.setOpticketCode(opticketCode.toString());
		}
		if (runAddFlag != null) {
			model.setRunAddFlag(runAddFlag.toString());
		}
		if (checkStatus != null) {
			model.setCheckStatus(checkStatus.toString());
		}
		return model;
	}
}