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
 * Facade for entity RunJOpMeasures.
 * 
 * @see power.ejb.opticket.bussiness.RunJOpMeasures
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpMeasuresFacade implements RunJOpMeasuresFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(RunJOpMeasures entity) throws CodeRepeatException {
		try {

			if (entity.getDangerId() == null) {
				entity.setDangerId(bll.getMaxId("RUN_J_OP_MEASURES",
						"danger_id"));
			}
			entity.setRunAddFlag("Y");
			entityManager.persist(entity);

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(RunJOpMeasures entity) {
		try {
			entity = entityManager.getReference(RunJOpMeasures.class, entity
					.getDangerId());
			entityManager.remove(entity);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpMeasures update(RunJOpMeasures entity)
			throws CodeRepeatException {
		try {

			RunJOpMeasures result = entityManager.merge(entity);
			return result;

		} catch (RuntimeException re) {
			throw re;
		}
	}

	public RunJOpMeasures findById(Long id) {
		LogUtil.log("finding RunJOpMeasures instance with id: " + id,
				Level.INFO, null);
		try {
			RunJOpMeasures instance = entityManager.find(RunJOpMeasures.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<RunJOpMeasures> findByOpticketCode(String opticketCode) {
		String sql = "select t.* from RUN_J_OP_MEASURES t where t.opticket_code='"
				+ opticketCode + "' order by  t.DISPLAY_NO,t.danger_id";
		return bll.queryByNativeSQL(sql, RunJOpMeasures.class);
	}

	public boolean saveAllOperat(List<Map> addList, List<Map> updateList,
			String delStr, String currentMan, String enterpriseCod) {
		boolean operatStatus = false;
		final int maxSize = 30;
		try {
			if (addList != null) {
				Long count = bll.getMaxId("RUN_J_OP_MEASURES", "danger_id");
				int totalAdd = addList.size();
				for (int i = 0; i < totalAdd; i++) {
					count++;
					Map map = addList.get(i);
					RunJOpMeasures op = this.updateModel(map);
					if (op.getDangerId() == null) {
						op.setDangerId(count);
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
					RunJOpMeasures opUp = this.updateModel(mapUp);
					if (opUp.getDangerId() != null) {
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
					RunJOpMeasures opDel = this
							.findById(Long.parseLong(ids[i]));
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

	private RunJOpMeasures updateModel(Map map) {
		RunJOpMeasures model = new RunJOpMeasures();
		Object opticketCode = map.get("opticketCode");
		Object runAddFlag = map.get("runAddFlag");
		Object dangerId = map.get("dangerId");
		Object dangerName = map.get("dangerName");
		Object measureContent = map.get("measureContent");
		Object displayNo = map.get("displayNo");
		Object memo = map.get("memo");
		if (dangerId == null || "".equals(dangerId.toString())) {
			;
		} else {
			model = this.findById(Long.parseLong(dangerId.toString()));
		}
		if (dangerName != null) {
			model.setDangerName(dangerName.toString());
		}
		if (measureContent != null) {
			model.setMeasureContent(measureContent.toString());
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
		return model;
	}
}