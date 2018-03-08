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

/**
 * Facade for entity RunJOpFinwork.
 * 
 * @see power.ejb.opticket.bussiness.RunJOpFinwork
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpFinworkFacade implements RunJOpFinworkFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunJOpFinwork entity) throws CodeRepeatException {
		if (entity.getFinishWorkId() == null) {
			entity.setFinishWorkId(bll.getMaxId("RUN_J_OP_FINWORK",
					"finish_work_id"));
		}
		entity.setRunAddFlag("Y");
		entityManager.persist(entity);
	}

	public void delete(RunJOpFinwork entity) {
		try {
			entity = entityManager.getReference(RunJOpFinwork.class, entity
					.getFinishWorkId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpFinwork update(RunJOpFinwork entity)
			throws CodeRepeatException {
		try {

			RunJOpFinwork result = entityManager.merge(entity);
			return result;

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpFinwork findById(Long id) {
		try {
			RunJOpFinwork instance = entityManager
					.find(RunJOpFinwork.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public List<RunJOpFinwork> findByOpticketCode(String opticketCode) {
		String sql = "select t.* from RUN_J_OP_FINWORK t where t.opticket_code='"
				+ opticketCode + "' order by  t.DISPLAY_NO,t.finish_work_id";
		return bll.queryByNativeSQL(sql, RunJOpFinwork.class);
	}

	private RunJOpFinwork updateModel(Map map) {
		RunJOpFinwork model = new RunJOpFinwork();
		Object opticketCode = map.get("opticketCode");
		Object runAddFlag = map.get("runAddFlag");
		Object finishWorkId = map.get("finishWorkId");
		Object finishWorkName = map.get("finishWorkName");
		Object displayNo = map.get("displayNo");
		Object checkStatus = map.get("checkStatus");
		Object memo = map.get("memo");
		if (finishWorkId == null || "".equals(finishWorkId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(finishWorkId.toString()));
		}
		if (finishWorkName != null) {
			model.setFinishWorkName(finishWorkName.toString());
		}
		if (displayNo != null && !displayNo.toString().equals("")) {
			model.setDisplayNo(Long.parseLong(displayNo.toString()));
		}
		if (checkStatus != null) {
			model.setCheckStatus(checkStatus.toString());
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
		return model;
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr, String currentMan, String enterpriseCode) {
		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_J_OP_FINWORK", "finish_work_id");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunJOpFinwork op = this.updateModel(map);
					if (op.getFinishWorkId() == null) {
						op.setFinishWorkId(count);
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
					RunJOpFinwork opUp = this.updateModel(mapUp);
					if (opUp.getFinishWorkId() != null) {
						opUp.setCheckMan(currentMan);
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
					RunJOpFinwork opDel = this.findById(Long.parseLong(ids[i]));
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