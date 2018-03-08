package power.ejb.equ.workbill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardService;
import power.ejb.equ.standardpackage.EquCStandardServiceAdd;
import power.ejb.hr.LogUtil;

@Stateless
public class EquJStandardServiceFacade implements
		EquJStandardServiceFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;


	public void save(EquJStandardService entity) {
		LogUtil.log("saving EquCStandardService instance", Level.INFO, null);
		try {
			if (entity.getOrderby() == null)
				entity.setOrderby(entity.getId());// 璁剧疆鎺掑簭鍙?
			entity.setIfUse("Y");// 灏嗚褰曞姞鍒犻櫎瀛楁榛樿鍊艰涓篩,浣跨敤
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean save(List<EquJStandardService> addList,
			List<EquJStandardService> updateList, String delIds) {
		try {
			if (addList != null && addList.size() > 0) {
				Long id = dll.getMaxId("EQU_J_STANDARD_SERVICE", "ID");
				int i = 0;
				for (EquJStandardService entity : addList) {
					entity.setId(id + (i++));
					this.save(entity);
				}
			}
			for (EquJStandardService entity : updateList) {
				this.update(entity);
			}
			if (delIds != null && !delIds.trim().equals("")) {
				this.delete(delIds);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean delete(String ids) {
		try {
			String sql = "UPDATE EQU_J_STANDARD_SERVICE t\n"
					+ "   SET t.if_use = 'N'\n" + " WHERE id IN (" + ids + ")";
			dll.exeNativeSQL(sql);// 鎵归噺鍒犻櫎璁板綍(鍋囧垹闄?
			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}

	public EquJStandardService update(EquJStandardService entity) {
		LogUtil.log("updating EquJStandardService instance", Level.INFO, null);
		try {
			EquJStandardService result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJStandardService findById(Long id) {
		LogUtil.log("finding EquJStandardService instance with id: " + id,
				Level.INFO, null);
		try {
			EquJStandardService instance = entityManager.find(
					EquJStandardService.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<EquJStandardService> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding EquJStandardService instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJStandardService model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "SELECT t.*,s.name replaceName,getworkername(t.fact_Service_Code) replaceName2\n"
					+ "  	FROM EQU_J_STANDARD_SERVICE t,equ_c_wo_service s \n"
					+ "    WHERE t.if_use = 'Y'\n"
					+ "AND t.plan_Service_Code=s.code"
					+ "		 AND t.WO_CODE='"
					+ woCode
					+ "'\n"
					+ "		 AND t.OPERATION_STEP='"
					+ operationStep
					+ "'\n"
					+ "      AND t.enterprisecode = '"
					+ enterpriseCode
					+ "'\n"
					+ " ORDER BY t.orderby,\n"
					+ "          t.id";

			String sqlCount = "SELECT count(*)\n"
					+ "  		 FROM EQU_J_STANDARD_SERVICE t\n"
					+ " 		WHERE t.if_use = 'Y'\n" + "			  AND t.WO_CODE='"
					+ woCode + "'\n" + "		 	  AND t.OPERATION_STEP='"
					+ operationStep + "'\n" + "   		  AND t.enterprisecode = '"
					+ enterpriseCode + "'";

			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List arrlist = new ArrayList();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquJStandardService baseInfo = new EquJStandardService();
				EquJStandardServiceAdd omodel = new EquJStandardServiceAdd();
				Object[] data = (Object[]) it.next();

				if (data[0] != null) {
					baseInfo.setId(Long.parseLong(data[0].toString()));
				}
				;
				if (data[1] != null) {
					baseInfo.setWoCode(data[1].toString());
				}
				;
				if (data[2] != null) {
					baseInfo.setOperationStep(data[2].toString());
				}
				;
				if (data[3] != null) {
					baseInfo.setPlanServiceCode(data[3].toString());
				}
				;
				if (data[4] != null) {
					baseInfo.setPlanServiceUnit(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanFee(Double.parseDouble(data[5].toString()));
				}
				;

				if (data[6] != null) {
					baseInfo.setFactServiceCode(data[6].toString());
				}
				;
				if (data[7] != null) {
					baseInfo.setFactServiceUnit(data[7].toString());
				}
				;
				if (data[8] != null) {
					baseInfo.setFactFee(Double.parseDouble(data[8].toString()));
				}
				;

				if (data[12] != null) {
					omodel.setServName(data[12].toString());
				}
				;
				if (data[13] != null) {
					omodel.setFactServName(data[13].toString());
				}
				;
				omodel.setBaseInfo(baseInfo);
				arrlist.add(omodel);
			}

			Long count = Long.parseLong(dll.getSingal(sqlCount).toString());
			result.setList(arrlist);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getEquCStandardService(String enterpriseCode,
			String woCode, String operationStep, int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select s.id,"
					+ "s.wo_code,"
					+ "s.operation_step,"
					+ "s.plan_service_code,"
					+ "s.plan_service_unit,"
					+ "s.plan_fee,\n"
					+ "s.orderby,"
					+ "s.enterprisecode,"
					+ "s.if_use,"
					+ "(select t.name from equ_c_wo_service  t where t.code=s.plan_service_code)"
					+ " from EQU_C_STANDARD_SERVICE  s"

					+ "    where s.if_use = 'Y'\n" + "		 and s.WO_CODE='"
					+ woCode + "'\n" + "		 and s.OPERATION_STEP='"
					+ operationStep + "'\n" + "      and s.enterprisecode = '"
					+ enterpriseCode + "'\n" + " order by  s.orderby,\n"
					+ "          s.id";
//			/System.out.println("the service:" + sql);
			// String sqlCount = "SELECT count(*)\n"
			// + " FROM EQU_C_STANDARD_SERVICE t\n"
			// + " WHERE t.if_use = 'Y'\n" + " AND t.WO_CODE='"
			// + woCode + "'\n" + " AND t.OPERATION_STEP='"
			// + operationStep + "'\n" + " AND t.enterprisecode = '"
			// + enterpriseCode + "'";
			List list=new ArrayList();
				list = dll.queryByNativeSQL(sql);

			// List list = entityManager.createNativeQuery(sql).getResultList();
			List<EquCStandardServiceAdd> arrlist = new ArrayList<EquCStandardServiceAdd>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardService baseInfo = new EquCStandardService();
				EquCStandardServiceAdd omodel = new EquCStandardServiceAdd();
				Object[] data = (Object[]) it.next();

				if (data[2] != null) {
					baseInfo.setOperationStep(data[2].toString());
				}
				;
				if (data[3] != null) {
					baseInfo.setPlanServiceCode(data[3].toString());
				}
				;
				if (data[4] != null) {
					baseInfo.setPlanServiceUnit(data[4].toString());
				}
				;
				if (data[5] != null) {
					baseInfo.setPlanFee(Double.parseDouble(data[5].toString()));
				}
				;

				if (data[9] != null) {
					omodel.setServName(data[9].toString());
				}
				;

				omodel.setBaseInfo(baseInfo);
				arrlist.add(omodel);
			}
			List<EquJStandardServiceAdd> jList = new ArrayList<EquJStandardServiceAdd>();

			int i;

			for (i = 0; i < arrlist.size(); i++) {

				EquJStandardService data = new EquJStandardService();
				EquJStandardServiceAdd data2 = new EquJStandardServiceAdd();
				if (arrlist.get(i).getBaseInfo().getOperationStep() != null
						&& !arrlist.get(i).getBaseInfo().getOperationStep()
								.equals("")) {
					data.setOperationStep(arrlist.get(i).getBaseInfo()
							.getOperationStep());
				}
				if (arrlist.get(i).getBaseInfo().getPlanServiceCode() != null
						&& !arrlist.get(i).getBaseInfo().getPlanServiceCode()
								.equals("")) {
					data.setPlanServiceCode(arrlist.get(i).getBaseInfo()
							.getPlanServiceCode());
				}
				if (arrlist.get(i).getBaseInfo().getPlanFee() != null
						&& !arrlist.get(i).getBaseInfo().getPlanFee()
								.equals("")) {
					data.setPlanFee(arrlist.get(i).getBaseInfo().getPlanFee());
				}
				if (arrlist.get(i).getBaseInfo().getPlanServiceUnit() != null
						&& !arrlist.get(i).getBaseInfo().getPlanServiceUnit()
								.equals("")) {
					data.setPlanServiceUnit(arrlist.get(i).getBaseInfo()
							.getPlanServiceUnit());
				}
				if (enterpriseCode != null && !enterpriseCode.equals("")) {
					data.setEnterprisecode(enterpriseCode);
				}
				if (arrlist.get(i).getServName() != null
						&& !arrlist.get(i).getServName().equals("")) {
					data2.setServName(arrlist.get(i).getServName());
				}
				data2.setBaseInfo(data);

				jList.add(data2);

			}

			Long count = (long) (jList.size());
			result.setList(jList);
			result.setTotalCount(count);
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}
