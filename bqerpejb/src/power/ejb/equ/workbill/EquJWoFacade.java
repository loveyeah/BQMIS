package power.ejb.equ.workbill;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.standardpackage.EquCStandardWo;
import power.ejb.equ.standardpackage.EquCStandardWoForm;
import power.ejb.equ.workbill.form.WorkbillInfo;
import power.ejb.manage.project.PrjJApply;
import power.ear.comm.CodeRepeatException;

/**
 * Facade for entity EquJWo.
 * 
 * @author slTang
 */
@Stateless
public class EquJWoFacade implements EquJWoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	@EJB(beanName = "EquJWoerFacade")
	protected EquJWoerFacadeRemote woerRemote;

	@EJB(beanName = "EquJOrderstepFacade")
	protected EquJOrderstepFacadeRemote orderstepRemote;

	@EJB(beanName = "EquJManplanFacade")
	protected EquJManplanFacadeRemote manplanRemote;

	@EJB(beanName = "EquJMainmatFacade")
	protected EquJMainmatFacadeRemote mainmatRemote;

	@EJB(beanName = "EquJToolsFacade")
	protected EquJToolsFacadeRemote toolsRemote;

	@EJB(beanName = "EquJStandardServiceFacade")
	protected EquJStandardServiceFacadeRemote serviceRemote;

	@EJB(beanName = "EquJStepdocumentsFacade")
	protected EquJStepdocumentsFacadeRemote stepdocumentsRemote;

	/**
	 * 增加一个工单
	 * 
	 * @param entity
	 * @return woCode
	 */
	@SuppressWarnings("unchecked")
	public String save(EquJWo entity, String failureCode, String stdCode)
			throws CodeRepeatException {
		try {
			if (checkWoCodeShow(entity.getWoCodeShow())) {
				throw new CodeRepeatException("改工单编号已占用,请重新输入!");
			} else {
				if (entity.getWoId() == null) {
					entity.setWoId(bll.getMaxId("equ_j_wo", "wo_id"));
				}
				//add by kzhang 20100928 工单编号为空时，自动生成工单编号
				if(entity.getWoCode()==null||entity.getWoCode().equals("")){
					java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
							"yyyyMMdd" + "hhmmss");
					String woCode = "WO" + tempDate.format(new java.util.Date());
					entity.setWoCode(woCode);
				}
				// "N"表示不关联,无报告
				entity.setWoCodeShow(this.getWoCodeShow(entity
						.getEnterprisecode()));
				entity.setIfWorkticket("N");
				entity.setIfFireticket("N");
				entity.setIfMaterials("N");
				entity.setIfReport("N");
				entity.setIfUse("Y");
				entity.setWfState(0l);
				entity.setRequireTime(new Date());
				entityManager.persist(entity);
				// 保存缺陷编码
				if (failureCode != null) {
					EquJWoer woerModel = new EquJWoer();
					woerModel.setEnterprisecode(entity.getEnterprisecode());
					woerModel.setWoCode(entity.getWoCode());
					woerModel.setFailureCode(failureCode);
					woerRemote.save(woerModel);
				}

				// 标准包数据引入运行表
				if (stdCode != null && !stdCode.equals("")) {
					String woCode = entity.getWoCode();
					String enterpriseCode = entity.getEnterprisecode();
					List<EquJOrderstep> jList = new ArrayList<EquJOrderstep>();
					jList = orderstepRemote.getEquCOrderstepList(stdCode,
							enterpriseCode).getList();

					for (EquJOrderstep data : jList) {
						data.setWoCode(woCode);
						String stdOpCode = data.getOperationStep();

						// 取存人工

						List<EquJManplan> manplanList = new ArrayList<EquJManplan>();
						manplanList = manplanRemote.getEquCManplan(
								enterpriseCode, stdCode, stdOpCode, null)
								.getList();
						for (EquJManplan temp1 : manplanList) {
							temp1.setWoCode(woCode);
						}
						manplanRemote.save(manplanList, null, "");

						// 取存材料
						List<EquJMainmatAdd> mainmatList = new ArrayList<EquJMainmatAdd>();
						mainmatList = mainmatRemote.getEquCMainmat(
								enterpriseCode, stdCode, stdOpCode, null)
								.getList();
						List<EquJMainmat> mainmatList2 = new ArrayList<EquJMainmat>();
						for (EquJMainmatAdd temp1 : mainmatList) {
							mainmatList2.add(temp1.getBaseInfo());
						}

						for (EquJMainmat temp1 : mainmatList2) {
							temp1.setWoCode(woCode);
						}
						mainmatRemote.save(mainmatList2, null, "");

						// 取存工具
						List<EquJToolsAdd> toolsList = new ArrayList<EquJToolsAdd>();
						toolsList = toolsRemote.getEquCTools(enterpriseCode,
								stdCode, stdOpCode, null).getList();
						List<EquJTools> toolsList2 = new ArrayList<EquJTools>();
						for (EquJToolsAdd temp1 : toolsList) {
							toolsList2.add(temp1.getBaseInfo());
						}

						for (EquJTools temp1 : toolsList2) {
							temp1.setWoCode(woCode);
							temp1.setEnterprisecode(enterpriseCode);
						}
						toolsRemote.save(toolsList2, null, "");

						// 取存服务

						List<EquJStandardServiceAdd> serviceList = new ArrayList<EquJStandardServiceAdd>();

						
						serviceList = serviceRemote.getEquCStandardService(
								enterpriseCode, stdCode, stdOpCode, null)
								.getList();

						List<EquJStandardService> serviceList2 = new ArrayList<EquJStandardService>();
						for (EquJStandardServiceAdd temp1 : serviceList) {
							serviceList2.add(temp1.getBaseInfo());
						}

						for (EquJStandardService temp1 : serviceList2) {
							temp1.setWoCode(woCode);
						}
						serviceRemote.save(serviceList2, null, "");

						entityManager.flush();

					}
					orderstepRemote.save(jList, null, "");

					// 取存相关文件
					List<EquJStepdocuments> spList = new ArrayList<EquJStepdocuments>();
					spList = stepdocumentsRemote.getEquCStepdocumentsList(
							enterpriseCode, stdCode, null).getList();

					for (EquJStepdocuments data : spList) {

						data.setWoCode(woCode);
					}

					stepdocumentsRemote.save(spList);
				}

				return entity.getWoCode();
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw re;
		}
	}

	public EquJWo findOnebill(String woCode) {
		String sql = "select t.* from equ_j_wo t where t.wo_code='" + woCode
				+ "' and t.if_use='Y'";
		List<EquJWo> billlist = bll.queryByNativeSQL(sql, EquJWo.class);
		if (billlist != null) {
			return billlist.get(0);
		} else {
			return null;
		}
	}

	// 查询一条工单
	public List<EquJWo> findbill(String woCode) {
		String sql = "select t.* from equ_j_wo t where t.wo_code='" + woCode
				+ "'";
		List<EquJWo> billlist = bll.queryByNativeSQL(sql, EquJWo.class);
		if (billlist != null) {
			return billlist;
		} else {
			return null;
		}
	}

	// 根据KKS编码找标准包
	public PageObject findstdPKageBykks(String kksCode, String queryKey,
			int... rowStartIdxAndCount) {
		String sql = " ";
		String sqlCount = " ";
		PageObject pg = new PageObject();
		if (kksCode == null || kksCode.equals("")
				|| kksCode.equals("undefined")) {
			sql += "select m.wo_id,m.wo_code,m.job_code,m.workorder_title,m.profession_code,"
					+ "(select t.speciality_name from run_c_specials t where m.profession_code = t.speciality_code and t.is_use = 'Y') professionName,"
					// modified by liuyi 091113
					// + " m.repair_model,(select t.repairmode_name from
					// equ_c_standard_repairmode t where m.repair_model =
					// t.repairmode_code and t.if_use = 'Y') repairmodelName,"
					+ " m.repair_model,(select t.repairmode_name from equ_c_standard_repairmode t where m.repair_model = t.repairmode_code and t.if_use = 'Y' and rownum = 1) repairmodelName,"
					+ "n.kks_code as kksCode,getequnamebycode(n.kks_code) as equName "
					+ " from equ_c_standard_wo m,equ_c_standard_equ n"
					+ " where m.wo_code=n.wo_code"
					+ " and m.if_use='Y'"
					+ " and m.status ='C'";

			if (queryKey != null) {
				sql += " and (m.job_code like '%" + queryKey
						+ "%' or n.kks_code like '%" + queryKey + "%')";
			}

			sqlCount += "select count(1) from equ_c_standard_wo m,equ_c_standard_equ n \n"
					+ " where m.wo_code=n.wo_code"
					+ " and m.if_use='Y'"
					+ " and m.status ='C'";
			if (queryKey != null) {
				sqlCount += " and (m.job_code like '%" + queryKey
						+ "%' or n.kks_code like '%" + queryKey + "%')";
			}
		} else {
			sql += "select m.wo_id,m.wo_code,m.job_code,m.workorder_title,m.profession_code,"
					+ "(select t.speciality_name from run_c_specials t where m.profession_code = t.speciality_code and t.is_use = 'Y') professionName,"
					// modified by liuyi 091113
					// + " m.repair_model,(select t.repairmode_name from
					// equ_c_standard_repairmode t where m.repair_model =
					// t.repairmode_code and t.if_use = 'Y') repairmodelName,"
					+ " m.repair_model,(select t.repairmode_name from equ_c_standard_repairmode t where m.repair_model = t.repairmode_code and t.if_use = 'Y' and rownum = 1) repairmodelName,"
					+ "n.kks_code as kksCode,getequnamebycode(n.kks_code) as equName "
					+ " from equ_c_standard_wo m,equ_c_standard_equ n"
					+ " where m.wo_code=n.wo_code"
					+ " and n.kks_code='"
					+ kksCode
					+ "'"
					+ " and m.if_use='Y'"
					+ " and m.status ='C'";

			if (queryKey != null) {
				sql += " and (m.job_code like '%" + queryKey
						+ "%' or n.kks_code like '%" + queryKey + "%')";
			}

			sqlCount += "select count(1) from equ_c_standard_wo m,equ_c_standard_equ n \n"
					+ " where m.wo_code=n.wo_code"
					+ " and n.kks_code='"
					+ kksCode
					+ "'"
					+ " and m.if_use='Y'"
					+ " and m.status ='C'";
			if (queryKey != null) {
				sqlCount += " and (m.job_code like '%" + queryKey
						+ "%' or n.kks_code like '%" + queryKey + "%')";
			}
		}

		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());

		if (count > 0) {
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<EquCStandardWoForm> arr = new ArrayList<EquCStandardWoForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				EquCStandardWo model_2 = new EquCStandardWo();
				EquCStandardWoForm model_1 = new EquCStandardWoForm();
				Object[] ob = (Object[]) it.next();
				if (ob[0] != null)
					model_2.setWoId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					model_2.setWoCode(ob[1].toString());
				if (ob[2] != null)
					model_2.setJobCode(ob[2].toString());
				if (ob[3] != null)
					model_2.setWorkorderTitle(ob[3].toString());
				if (ob[4] != null)
					model_2.setProfessionCode(ob[4].toString());
				if (ob[5] != null)
					model_1.setProfessionName(ob[5].toString());
				if (ob[6] != null)
					model_2.setRepairModel(ob[6].toString());
				if (ob[7] != null)
					model_1.setRepairmodelName(ob[7].toString());
				if (ob[8] != null)
					model_1.setKksCode(ob[8].toString());
				if (ob[9] != null)
					model_1.setEquName(ob[9].toString());

				model_1.setStandardInfo(model_2);
				arr.add(model_1);
			}
			pg.setList(arr);
			pg.setTotalCount(count);
			return pg;
		} else {
			return null;
		}
	}

	/**
	 * 删除一条工单
	 * 
	 * @param entity
	 */
	public void delete(EquJWo entity) {
		try {
			entity.setIfUse("N");
			this.update(entity, null);
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 修改一条工单
	 */
	public EquJWo update(EquJWo entity, String failureCode) {
		try {
			EquJWo result = entityManager.merge(entity);
			// 修改设备编码
			if (failureCode != null && failureCode.length() > 0) {
				List<EquJWoer> list = woerRemote.findByWoCode(entity
						.getWoCode(), entity.getEnterprisecode());
				if (list != null&&list.size()>0) {
					EquJWoer model = list.get(0);
					model.setFailureCode(failureCode);
					woerRemote.update(model);
				} else {
					EquJWoer model = new EquJWoer();
					model.setWoCode(entity.getWoCode());
					model.setFailureCode(failureCode);
					model.setEnterprisecode(entity.getEnterprisecode());
					woerRemote.save(model);
				}
			}
			return result;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public EquJWo findById(Long id) {
		try {
			EquJWo instance = entityManager.find(EquJWo.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	/**
	 * 删除多条工单
	 */
	public boolean delMulti(String ids) {
		boolean isSuccess = false;
		try {
			String sql = "update EQU_J_WO t set t.if_use='N' where t.id in ("
					+ ids + ") ";
			if (bll.exeNativeSQL(sql) > 0) {
				isSuccess = true;
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
		} finally {
			return isSuccess;
		}
	}

	/**
	 * 查询工单
	 */
	public PageObject findByCondition(String reqBeginTime, String reqEndTime,
			String ifWorkticket, String ifMaterials, String workorderType,
			String workorderStatus, String enterprisecode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = " t.if_use='Y'";
		if (reqBeginTime != null && !"".equals(reqBeginTime)) {
			strWhere += " and t.require_time>to_date('" + reqBeginTime
					+ "' || '00:00:00',yyyy-MM-dd hh24:mi:ss)";
		}
		if (reqEndTime != null && !"".equals(reqEndTime)) {
			strWhere += " and t.require_time<to_date('" + reqEndTime
					+ "' || '23:59:59',yyyy-MM-dd hh24:mi:ss)";
		}
		if (ifWorkticket != null && "Y".equals(ifWorkticket)) {
			strWhere += " and t.if_workticket='" + ifWorkticket + "'";
		}
		if (ifMaterials != null && "Y".equals(ifMaterials)) {
			strWhere += " and t.if_Materials='" + ifMaterials + "'";
		}
		if (workorderType != null && "Y".equals(workorderType)) {
			strWhere += " and t.workorder_type='" + workorderType + "'";
		}
		if (workorderStatus != null && "Y".equals(workorderStatus)) {
			strWhere += " and t.workorder_status in ('" + workorderStatus
					+ "')";
		}
		if (enterprisecode != null && !"".equals(enterprisecode)) {
			strWhere += " and t.enterprisecode='" + enterprisecode + "'";
		}
		
		
		
		String sqlCount = "select count(1) from EQU_J_WO t";
		sqlCount += strWhere;
		Long count = Long.parseLong(bll.queryByNativeSQL(sqlCount).toString());
		if (count > 0) {
			String sql = "select t.* from EQU_J_WO t";
			sql += strWhere;
			List<EquJWo> list = bll.queryByNativeSQL(sql, EquJWo.class,
					rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(count);
			return pg;
		} else {
			return null;
		}
	}

	// 根据不同参数查询工单列表
	public PageObject FindByMoreCondition(String reqBeginTime,
			String reqEndTime, String workorderStatus, String workorderType,
			String professionCode, String repairDepartment,
			String ifWorkticket, String ifMaterials, String enterprisecode,
			String argFuzzy,String flag,String entryIds, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String strWhere = " and t.if_use='Y'";
		if (reqBeginTime != null && !"".equals(reqBeginTime)) {
			strWhere += " and t.require_time>=to_date('" + reqBeginTime
					+ "' || '00:00:00','yyyy-MM-dd hh24:mi:ss')";
		}
		if (reqEndTime != null && !"".equals(reqEndTime)) {
			strWhere += " and t.require_time<=to_date('" + reqEndTime
					+ "' || '23:59:59','yyyy-MM-dd hh24:mi:ss')";
		}
		// if (ifWorkticket != null && "Y".equals(ifWorkticket)) {
		// strWhere += " and t.if_workticket='" + ifWorkticket + "'";
		// }
		// if (ifMaterials != null && "Y".equals(ifMaterials)) {
		// strWhere += " and t.if_Materials='" + ifMaterials + "'";
		// }
		if (workorderType != null && !"".equals(workorderType)) {
			strWhere += " and t.workorder_type='" + workorderType + "'";
		}
		if (workorderStatus != null && !"".equals(workorderStatus)) {
			strWhere += " and t.wf_state in ('" + workorderStatus
					+ "')";
		}
		if (enterprisecode != null && !"".equals(enterprisecode)) {
			strWhere += " and t.enterprisecode='" + enterprisecode + "'";
		}
		if (professionCode != null && !"".equals(professionCode)) {
			strWhere += " and t.profession_code='" + professionCode + "'";
		}
		if (repairDepartment != null && !"".equals(repairDepartment)) {
			strWhere += " and t.repair_department='" + repairDepartment + "'";
		}
		if (argFuzzy != null && argFuzzy.length() > 0) {
			strWhere += " and (t.wo_code_show like '%" + argFuzzy
					+ "%' or t.kks_code like '%" + argFuzzy
					+ "%' or getworkername(t.require_man_code) like '%" +
					// argFuzzy+"%' or t.project_num like '%" +
					// argFuzzy+"%' or w.failure_code like '%" +
					argFuzzy + "%')";
		}
		//查询审批工单列表条件  add by qxjiao 20100928
		if("approve".equals(flag)){
			strWhere += " and t.wf_state NOT IN('0','7','8') ";
			if(entryIds!=null&&!"".equals(entryIds)){
				strWhere += " and  t.work_flow_no IN("+entryIds+") ";
			}
		}else if("query".equals(flag)){
			
		}else{
			strWhere+=" and t.wf_state in ('0','8') ";
		}
		
		strWhere += " order by t.require_time desc";
		String sqlCount = "select count(DISTINCT t.wo_id) from EQU_J_WO t where 1=1";
		sqlCount += strWhere;
		String str = bll.getSingal(sqlCount).toString();
		Long count = Long.parseLong(str);
		if (count > 0) {
			String sql = "select t.*,"
					// + "decode(t.workorder_type,0,'缺陷工单',1,'检修工单',2,'独立工单'),"
					// +
					// "decode(t.workorder_status,0,'工作开始',1,'工作结束',2,'审核完结',3,'审核退回'),"
					+ "getworkername(t.work_charge_code) workchargeName,"
					+ "getworkername(t.REQUIRE_MAN_CODE) requiremanName,"
					+ "getdeptname(t.REPAIR_DEPARTMENT) repairdepartmentName,"
					+ "(select distinct(a.speciality_name) from run_c_specials a where a.speciality_code = t.profession_code and rownum = 1) professionName,"
					+ "(select w.failure_code from equ_j_woer w where w.wo_code=t.wo_code and rownum = 1) failureCode,"
					// modified by liuyi 091117 数据库中无该表
					// + "(select h.failure_content from equ_j_failure h where
					// h.failure_code ="
					+ "(select h.failure_content from equ_j_failures h where h.failure_code ="
					+ "(select w.failure_code from equ_j_woer w where w.wo_code=t.wo_code and rownum = 1)) failureContent,"
					+ "getequnamebycode(t.kks_code) equipmentName"
					+ " from EQU_J_WO t where 1=1";

			sql += strWhere;
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<WorkbillInfo> arraylist = new ArrayList<WorkbillInfo>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
				// kk:mm:ss");
				Object[] data = (Object[]) it.next();
				WorkbillInfo model = new WorkbillInfo();
				EquJWo basemodel = new EquJWo();
				if (data[0] != null) {
					basemodel.setWoId(Long.parseLong(data[0].toString()));
				}
				if (data[1] != null) {
					basemodel.setWoCodeShow(data[1].toString());
				}
				if (data[2] != null) {
					basemodel.setWoCode(data[2].toString());
				}
				if (data[3] != null) {
					basemodel.setFaWoCode(data[3].toString());
				}
				if (data[4] != null) {
					basemodel.setWorkorderContent(data[4].toString());
				}
				if (data[5] != null) {
					basemodel.setWorkorderStatus(data[5].toString());
				}
				if (data[6] != null) {
					basemodel.setWorkorderType(data[6].toString());
				}
				if (data[7] != null) {
					basemodel.setProfessionCode(data[7].toString());
				}
				if (data[8] != null) {
					basemodel.setCrewId(data[8].toString());
				}
				if (data[9] != null) {
					basemodel.setMaintDep(data[9].toString());
				}
				if (data[10] != null) {
					basemodel.setRepairModel(data[10].toString());
				}
				if (data[11] != null) {
					basemodel.setRepairmethodCode(data[11].toString());
				}
				if (data[12] != null) {
					basemodel.setFilepackageCode(data[12].toString());
				}
				if (data[13] != null) {
					basemodel.setRelationFilepackageMemo(data[13].toString());
				}
				if (data[14] != null) {
					basemodel.setIfWorkticket(data[14].toString());
				}
				if (data[15] != null) {
					basemodel.setEquCode(data[15].toString());
				}
				if (data[16] != null) {
					basemodel.setKksCode(data[16].toString());
				}
				if (data[17] != null) {
					basemodel.setEquName(data[17].toString());
				}
				if (data[18] != null) {
					basemodel.setEquPostionCode(data[18].toString());
				}
				if (data[19] != null) {
					basemodel.setRemark(data[19].toString());
				}
				if (data[20] != null) {
					basemodel.setIfOutside(data[20].toString());
				}
				if (data[21] != null) {
					basemodel.setIfFireticket(data[21].toString());
				}
				if (data[22] != null) {
					basemodel.setIfMaterials(data[22].toString());
				}
				if (data[23] != null) {
					basemodel.setIfReport(data[23].toString());
				}
				if (data[24] != null) {
					basemodel.setIfContact(data[24].toString());
				}
				if (data[25] != null) {
					basemodel.setIfConform(data[25].toString());
				}
				if (data[26] != null) {
					basemodel.setIfRemove(data[26].toString());
				}
				if (data[27] != null) {
					basemodel.setIfCrane(data[27].toString());
				}
				if (data[28] != null) {
					basemodel.setIfFalsework(data[28].toString());
				}
				if (data[29] != null) {
					basemodel.setIfSafety(data[29].toString());
				}
				if (data[30] != null) {
					basemodel.setProjectNum(data[30].toString());
				}
				if (data[31] != null) {
					basemodel.setRequireStarttime((Date) data[31]);
				}
				if (data[32] != null) {
					basemodel.setRequireEndtime((Date) data[32]);
				}
				if (data[33] != null) {
					basemodel.setPlanStarttime((Date) data[33]);
				}
				if (data[34] != null) {
					basemodel.setPlanEndtime((Date) data[34]);
				}
				if (data[35] != null) {
					basemodel.setFactStarttime((Date) data[35]);
				}
				if (data[36] != null) {
					basemodel.setFactEndtime((Date) data[36]);
				}
				if (data[37] != null) {
					basemodel.setRepairDepartment(data[37].toString());
				}
				if (data[38] != null) {
					basemodel.setWorkChargeCode(data[38].toString());
				}
				if (data[39] != null) {
					basemodel.setProfessionHeader(data[39].toString());
				}
				if (data[40] != null) {
					basemodel.setRequireManCode(data[40].toString());
				}
				if (data[41] != null) {
					basemodel.setRequireTime((Date) data[41]);
				}
				if (data[42] != null) {
					basemodel.setCheckManCode(data[42].toString());
				}
				if (data[43] != null) {
					basemodel.setCheckTime((Date) data[43]);
				}
				if (data[44] != null) {
					basemodel.setCheckReportid(data[44].toString());
				}
				if (data[45] != null) {
					basemodel.setCheckResultid(data[45].toString());
				}
				if (data[46] != null) {
					basemodel.setCheckReasonid(data[46].toString());
				}
				if (data[47] != null) {
					basemodel.setRequireWotime(Double.parseDouble(data[47]
							.toString()));
				}
				if (data[48] != null) {
					basemodel.setRequireWofee(Double.parseDouble(data[48]
							.toString()));
				}
				if (data[49] != null) {
					basemodel
							.setWorkFlowNo(Long.parseLong(data[49].toString()));
				}
				if (data[50] != null) {
					basemodel.setWfState(Long.parseLong(data[50].toString()));
				}
				if (data[51] != null) {
					basemodel.setAssembly(data[52].toString());
				}
				if (data[52] != null) {
					basemodel.setPlanWotime(Double.parseDouble(data[52]
							.toString()));
				}
				if (data[53] != null) {
					basemodel.setPlanWofee(Double.parseDouble(data[53]
							.toString()));
				}
				if (data[54] != null) {
					basemodel.setFactWotime(Double.parseDouble(data[54]
							.toString()));
				}
				if (data[55] != null) {
					basemodel.setFactWofee(Double.parseDouble(data[55]
							.toString()));
				}
				if (data[56] != null) {
					basemodel.setEnterprisecode(data[56].toString());
				}
				if (data[57] != null) {
					basemodel.setIfUse(data[57].toString());
				}
				if (data[58] != null) {
					model.setWorkchargeName(data[58].toString());
				}
				if (data[59] != null) {
					model.setRequiremanName(data[59].toString());
				}
				if (data[60] != null) {
					model.setRepairdepartmentName(data[60].toString());
				}
				if (data[61] != null) {
					model.setProfessionName(data[61].toString());
				}
				if (data[62] != null) {
					model.setFailureCode(data[62].toString());
				}
				if (data[63] != null) {
					model.setFailureContent(data[63].toString());
				}
				if (data[64] != null) {
					model.setEquipmentName(data[64].toString());
				}
				model.setModel(basemodel);
				arraylist.add(model);
			}
			pg.setList(arraylist);
			pg.setTotalCount(count);
			return pg;
		} else {
			return null;
		}
	}

	/* -----------------生成工单编号-------------------- */
	private String createwoCode(EquJWo entity) {
		String woCode = "unknown";
		if (entity != null) {
			woCode = "knowm";
		}
		return woCode;
	}

	// 根据工单编号查找工单
	public EquJWo findBywoCode(String woCode, String enterpriseCode) {
		String sql = "select ttt.* from equ_j_wo ttt\n" + "where ttt.wo_code='"
				+ woCode + "'\n" + "and ttt.enterprisecode='" + enterpriseCode
				+ "'";
		List<EquJWo> list = bll.queryByNativeSQL(sql, EquJWo.class);
		if (list != null && list.size() > 0) {
			EquJWo entity = list.get(0);
			return entity;
		} else {
			return null;
		}
	}

	// 父工单号查询
	public PageObject findByFaWoCode(String reqBeginTime, String reqEndTime,
			String ifWorkticket, String ifMaterials, String workorderType,
			String repairDepartment, String professionCode,
			String workorderStatus, String enterprisecode, String argFuzzy,
			String woCode, String editWoCode, int... rowStartIdxAndCount)
			throws ParseException {
		PageObject pg = new PageObject();
		String strWhere = " where t.if_use='Y'";
		if (reqBeginTime != null && !"".equals(reqBeginTime)) {
			strWhere += " and t.require_time>to_date('" + reqBeginTime
					+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n";
		}
		if (reqEndTime != null && !"".equals(reqEndTime)) {
			strWhere += " and t.require_time<to_date('" + reqEndTime
					+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n";
		}
		if (ifWorkticket != null && "Y".equals(ifWorkticket)) {
			strWhere += " and t.wo_code in(select r.wo_code from run_j_worktickets r where r.workticket_no != null)";
		}
		if (ifMaterials != null && "Y".equals(ifMaterials)) {
			strWhere += "and t.wo_code in(select m.wo_code from equ_j_otma m where m.mat_code !=null)";

		}
		if (workorderType != null && !workorderType.equals("")) {
			strWhere += " and t.workorder_type='" + workorderType + "'";
		}
		if (workorderStatus != null && !workorderStatus.equals("")) {
			strWhere += " and t.workorder_status in ('" + workorderStatus
					+ "')";
		}
		if (professionCode != null && !"".equals(professionCode)) {
			strWhere += " and t.profession_code='" + professionCode + "'";
		}
		if (repairDepartment != null && !"".equals(repairDepartment)) {
			strWhere += " and t.repair_department='" + repairDepartment + "'";
		}
		if (enterprisecode != null && !"".equals(enterprisecode)) {
			strWhere += " and t.enterprisecode='" + enterprisecode + "'";
		}
		if (argFuzzy != null && argFuzzy.length() > 0 && !argFuzzy.equals(" ")) {
			strWhere += " and (t.wo_code_show like '%" + argFuzzy
					+ "%' or t.kks_code like '%" + argFuzzy
					+ "%' or getworkername(t.require_man_code) like '%"
					+ argFuzzy + "%')";
		}
		if (woCode != null && !woCode.equals("")) {
			strWhere += "and t.wo_code != '"
					+ woCode
					+ "'"
					+ " and t.fa_wo_code not in (select e.fa_wo_code from equ_j_wo e where e.fa_wo_code='"
					+ woCode + "')";
		}
		if (editWoCode != null) {
			strWhere += "and t.wo_code = '" + editWoCode + "'";

		}
		// String sql = "select t.* from EQU_J_WO t";
		// sql += strWhere;
		String sqlCount = "select count(1) from EQU_J_WO t";

		sqlCount += strWhere;
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (count > 0) {
			String sql = "";
			if (editWoCode != null) {
				sql += "select t.*,getworkername(t.require_man_code) wokername, "
						+ " getdeptname(t.repair_department) deptName,"
						
						+ " getworkername(t.work_charge_code) workChargeName ,"
						+ " getequnamebycode(t.kks_code) equName, "
						// modified by liuyi 091113
						// + " (select distinct(a.speciality_name) from
						// run_c_unitprofession a where a.speciality_code =
						// t.profession_code) professionName,"
						+ " (select distinct(a.speciality_name) from run_c_specials a where a.speciality_code = t.profession_code  and rownum = 1) professionName,"
						+ " b.failure_code failureCode,"
						+ " getlocationame(t.equ_postion_code) postionName,\n"
						+ "                         to_char(t.fact_starttime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') factSFormatDate,\n"
						+ "                         to_char(t.fact_endtime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') factEFormatDate,\n"
						+ "                         to_char(t.require_starttime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requireSFormatDate,\n"
						+ "                         to_char(t.require_endtime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requrieEFormatDate,\n"
						+ "                         to_char(t.require_time,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requireFormatDate,\n"
						+ "  (select g.wo_code_show from EQU_J_WO g where g.wo_code_show = t.fa_wo_code) as faCodeShow,\n"
						+ "   (select p.prj_no_show from prj_j_info p where p.prj_no = t.project_num) as prjNoShow,\n"
						+ "   (select p.prj_name from prj_j_info p where p.prj_no = t.project_num) as prjName,"
						+ "   (select y.failure_content from EQU_J_FAILURES y where y.failure_code = b.failure_code) failureContent"
						+ " from EQU_J_WO t,equ_j_woer b"
						+ " where t.wo_Code=b.wo_Code(+)"
						+ " and t.wo_code = '"
						+ editWoCode + "'";
			} else {
				sql += "select t.*,getworkername(t.require_man_code) wokername,"
						+ " getdeptname(t.repair_department) deptName,"
						
						+ " getworkername(t.work_charge_code) workChargeName ,"
						+ " getequnamebycode(t.kks_code) equName,"
						// modified by liuyi 091113
						// + " (select distinct(a.speciality_name) from
						// run_c_unitprofession a where a.speciality_code =
						// t.profession_code) professionName,"
						+ " (select distinct(a.speciality_name) from run_c_specials a where a.speciality_code = t.profession_code  and rownum = 1) professionName,"
						+ " b.failure_code failureCode ,"
						+ " getlocationame(t.equ_postion_code) postionName,\n"
						+ "                         to_char(t.fact_starttime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') factSFormatDate,\n"
						+ "                         to_char(t.fact_endtime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') factEFormatDate,\n"
						+ "                         to_char(t.require_starttime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requireSFormatDate,\n"
						+ "                         to_char(t.require_endtime,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requrieEFormatDate,\n"
						+ "                         to_char(t.require_time,\n"
						+ "                                 'yyyy-MM-dd HH24:MI:SS') requireFormatDate, \n"
						+ "  (select g.wo_code_show from EQU_J_WO g where g.wo_code_show = t.fa_wo_code) as faCodeShow,\n"
						+ "  (select p.prj_no_show from prj_j_info p where p.prj_no = t.project_num) as prjNoShow,\n"
						+ "  (select p.prj_name from prj_j_info p where p.prj_no = t.project_num) as prjName,"
						+ "   (select y.failure_content from EQU_J_FAILURES y where y.failure_code = b.failure_code) failureContent"
						+ " from EQU_J_WO t,equ_j_woer b";
				strWhere += " and t.wo_code = b.wo_code";
				strWhere += " order by t.require_time desc";
				sql += strWhere;
			}
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<EquJWoAdd> arr = new ArrayList<EquJWoAdd>();
			Iterator it = list.iterator();
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			while (it.hasNext()) {
				EquJWo model_2 = new EquJWo();
				EquJWoAdd model_1 = new EquJWoAdd();
				Object[] ob = (Object[]) it.next();
				if (ob[0] != null)
					model_2.setWoId(Long.parseLong(ob[0].toString()));
				if (ob[1] != null)
					model_2.setWoCodeShow(ob[1].toString());
				if (ob[2] != null)
					model_2.setWoCode(ob[2].toString());
				if (ob[3] != null)
					model_2.setFaWoCode(ob[3].toString());
				if (ob[4] != null)
					model_2.setWorkorderContent(ob[4].toString());
				if (ob[5] != null)
					model_2.setWorkorderStatus(ob[5].toString());
				if (ob[6] != null)
					model_2.setWorkorderType(ob[6].toString());
				if (ob[7] != null)
					model_2.setProfessionCode(ob[7].toString());
				if (ob[8] != null)
					model_2.setCrewId(ob[8].toString());
				if (ob[9] != null)
					model_2.setMaintDep(ob[9].toString());
				if (ob[10] != null)
					model_2.setRepairModel(ob[10].toString());
				if (ob[11] != null)
					model_2.setRepairmethodCode(ob[11].toString());
				if (ob[12] != null)
					model_2.setFilepackageCode(ob[12].toString());
				if (ob[13] != null)
					model_2.setRelationFilepackageMemo(ob[13].toString());
				if (ob[14] != null)
					model_2.setIfWorkticket(ob[14].toString());
				if (ob[15] != null)
					model_2.setEquCode(ob[15].toString());
				if (ob[16] != null)
					model_2.setKksCode(ob[16].toString());
				if (ob[17] != null)
					model_2.setEquName(ob[17].toString());
				if (ob[18] != null)
					model_2.setEquPostionCode(ob[18].toString());
				if (ob[19] != null)
					model_2.setRemark(ob[19].toString());
				if (ob[20] != null)
					model_2.setIfOutside(ob[20].toString());
				if (ob[21] != null)
					model_2.setIfFireticket(ob[21].toString());
				if (ob[22] != null)
					model_2.setIfMaterials(ob[22].toString());
				if (ob[23] != null)
					model_2.setIfReport(ob[23].toString());
				if (ob[24] != null)
					model_2.setIfContact(ob[24].toString());
				if (ob[25] != null)
					model_2.setIfConform(ob[25].toString());
				if (ob[26] != null)
					model_2.setIfRemove(ob[26].toString());
				if (ob[27] != null)
					model_2.setIfCrane(ob[27].toString());
				if (ob[28] != null)
					model_2.setIfFalsework(ob[28].toString());
				if (ob[29] != null)
					model_2.setIfSafety(ob[29].toString());
				if (ob[30] != null)
					model_2.setProjectNum(ob[30].toString());
				if (ob[31] != null)
					model_2.setRequireStarttime(time.parse(ob[31].toString()));
				if (ob[32] != null)
					model_2.setRequireEndtime(time.parse(ob[32].toString()));

				if (ob[33] != null)
					model_2.setPlanStarttime(time.parse(ob[33].toString()));

				if (ob[34] != null)
					model_2.setPlanEndtime(time.parse(ob[34].toString()));

				if (ob[35] != null)
					model_2.setFactStarttime(time.parse(ob[35].toString()));
				if (ob[36] != null)
					model_2.setFactEndtime(time.parse(ob[36].toString()));

				if (ob[37] != null)
					model_2.setRepairDepartment(ob[37].toString());
				if (ob[38] != null)
					model_2.setWorkChargeCode(ob[38].toString());
				if (ob[39] != null)
					model_2.setProfessionHeader(ob[39].toString());
				if (ob[40] != null)
					model_2.setRequireManCode(ob[40].toString());
				if (ob[41] != null)
					model_2.setRequireTime(time.parse(ob[41].toString()));
				if (ob[42] != null)
					model_2.setCheckManCode(ob[42].toString());
				if (ob[43] != null)
					model_2.setCheckTime(time.parse(ob[43].toString()));
				if (ob[44] != null)
					model_2.setCheckReportid(ob[44].toString());
				if (ob[45] != null)
					model_2.setCheckResultid(ob[45].toString());
				if (ob[46] != null)
					model_2.setCheckReasonid(ob[46].toString());
				// if (ob[47] != null)
				// model_2.setWorkorderContent(ob[47].toString());
				if (ob[47] != null)
					model_2.setRequireWotime(Double.parseDouble(ob[47]
							.toString()));
				if (ob[48] != null)
					model_2.setRequireWofee(Double.parseDouble(ob[48]
							.toString()));
				if (ob[49] != null)
					model_2.setWorkFlowNo(Long.parseLong(ob[49].toString()));
				if (ob[50] != null)
					model_2.setWfState(Long.parseLong(ob[50].toString()));
				if (ob[51] != null)
					model_2.setAssembly(ob[51].toString());
				if (ob[52] != null)
					model_2
							.setPlanWotime(Double
									.parseDouble(ob[52].toString()));
				if (ob[53] != null)
					model_2.setPlanWofee(Double.parseDouble(ob[53].toString()));
				if (ob[54] != null)
					model_2
							.setFactWotime(Double
									.parseDouble(ob[54].toString()));
				if (ob[55] != null)
					model_2.setFactWofee(Double.parseDouble(ob[55].toString()));
				if (ob[56] != null)
					model_2.setEnterprisecode(ob[56].toString());
				if (ob[57] != null)
					model_2.setIfUse(ob[57].toString());
				if (ob[58] != null)
					model_1.setWokerName(ob[58].toString());
				if (ob[59] != null)
					model_1.setDeptName(ob[59].toString());
				if (ob[60] != null)
					model_1.setWorkChargeName(ob[60].toString());
				if (ob[61] != null)
					model_1.setEquName(ob[61].toString());
				if (ob[62] != null)
					model_1.setProfessionName(ob[62].toString());
				if (ob[63] != null)
					model_1.setFailureCode(ob[63].toString());
				if (ob[64] != null)
					model_1.setPostionName(ob[64].toString());
				if (ob[65] != null)
					model_1.setFactSFormatDate(ob[65].toString());
				if (ob[66] != null)
					model_1.setFactEFormatDate(ob[66].toString());
				if (ob[67] != null)
					model_1.setRequireSFormatDate(ob[67].toString());
				if (ob[68] != null)
					model_1.setRequrieEFormatDate(ob[68].toString());
				if (ob[69] != null)
					model_1.setRequireFormatDate(ob[69].toString());
				if (ob[70] != null)
					model_1.setFaCodeShow(ob[70].toString());
				if (ob[71] != null)
					model_1.setPrjNoShow(ob[71].toString());
				if (ob[72] != null)
					model_1.setPrjName(ob[72].toString());
				if (ob[73] != null)
					model_1.setFailureContent(ob[73].toString());
				model_1.setEquJWo(model_2);
				arr.add(model_1);
			}
			pg.setList(arr);
			pg.setTotalCount(count);
			return pg;

		} else {
			return null;
		}
	}

	// 判断工单关联工作票和领料单的状态决定是否删除该工单
	public boolean isDeleteWorkbillCheck(String woCode, String enterprisecode) {
		String sqlWo = "SELECT ((SELECT COUNT(*)\n"
				+ "           FROM equ_j_worktickets a,\n"
				+ "                run_j_worktickets b\n"
				+ "          WHERE a.woticket_code = b.workticket_no\n"
				+ "            AND a.wo_code = '" + woCode + "'\n"
				+ "            AND b.workticket_staus_id IN (1,9,14)\n"
				+ "            AND b.enterprise_code = '" + enterprisecode
				+ "'\n" + "            AND b.is_use = 'Y') -\n"
				+ "       (SELECT COUNT(*)\n"
				+ "           FROM equ_j_worktickets a,\n"
				+ "                run_j_worktickets b\n"
				+ "          WHERE a.woticket_code = b.workticket_no\n"
				+ "            AND a.wo_code = '" + woCode + "'\n"
				+ "            AND b.enterprise_code = '" + enterprisecode
				+ "'\n" + "            AND b.is_use = 'Y')) bb\n"
				+ "           FROM dual";
		;
		String sqlMa = "SELECT ((SELECT COUNT(*)\n"
				+ "           FROM EQU_J_OTMA a,\n"
				+ "                INV_J_ISSUE_HEAD  b\n"
				+ "          WHERE a.MAT_CODE = b.ISSUE_NO\n"
				+ "            AND a.wo_code = '" + woCode + "'\n"
				+ "            AND b.ISSUE_STATUS IN ('0','9')\n"
				+ "            AND b.enterprise_code = '" + enterprisecode
				+ "'\n" + "            AND b.is_use = 'Y') -\n"
				+ "       (SELECT COUNT(*)\n"
				+ "           FROM EQU_J_OTMA a,\n"
				+ "                INV_J_ISSUE_HEAD b\n"
				+ "          WHERE a.MAT_CODE = b.ISSUE_NO\n"
				+ "            AND a.wo_code = '" + woCode + "'\n"
				+ "            AND b.enterprise_code = '" + enterprisecode
				+ "'\n" + "            AND b.is_use = 'Y')) num\n"
				+ "            FROM dual";
		String strWo = bll.getSingal(sqlWo).toString();
		Long numWo = Long.parseLong(strWo);
		String strMa = bll.getSingal(sqlMa).toString();
		Long numMa = Long.parseLong(strMa);
		if (numWo != 0 || numMa != 0) {
			return false;
		} else {
			return true;
		}
	}

	// 工单显示同名判断
	@SuppressWarnings("unchecked")
	private boolean checkWoCodeShow(String WoCodeShow) {
		String sql = "select count(1) from equ_j_wo t where t.wo_code_show=? "
				+ " and t.if_use ='Y'";
		int size = Integer.parseInt(bll.getSingal(sql,
				new Object[] { WoCodeShow }).toString());
		if (size > 0) {
			return true;
		}
		return false;
	}

	// 工单显示编码规则
	@SuppressWarnings("unchecked")
	private String getWoCodeShow(String Enterprisecode) {
		String code = "WO";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyMMdd");
		Date date = new java.util.Date();
		code += tempDate.format(date);
		int count = this.getWoCodeShowCount(Enterprisecode);
		if (count + 1 < 10) {
			code += "00" + (count + 1);
		}
		if (10 <= (count + 1) && (count + 1) < 100) {
			code += "0" + (count + 1);
		}
		if ((count + 1) >= 100) {
			code += count + 1;
		}
		return code;
	}

	// 取得当天工单生成数
	public int getWoCodeShowCount(String Enterprisecode) {
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyMMdd");
		Date date = new java.util.Date();
		String WoCodeShow = "WO" + tempDate.format(date);
		String sql = "select count(1) from equ_j_wo t where t.wo_code_show like '"
				+ WoCodeShow
				+ "%'"
				+ " and t.if_use='Y'"
				+ " and t.enterprisecode='" + Enterprisecode + "'";
		int count = Integer.parseInt(bll.getSingal(sql).toString());
		return count;
	}
	public boolean approveWorkBill(String entryId, String workerCode,
			String actionId, String approveText, String nextRoles,
			String identify, String applyId, String enterpriseCode,String nextRolePerson) {
		EquJWo model = this.findById(Long.parseLong(applyId));
		if(identify.equals("TH")){
			model.setWfState(8l);
		}else if(model.getWfState()==1){
				model.setWfState(2l);
			}else if(model.getWfState()==2){
				model.setWfState(3l);
			}else if(model.getWfState()==3){
				model.setWfState(4l);
			}else if(model.getWfState()==4){
				if(identify.equals("XYDJZSP")){
					model.setWfState(5l);
				}else if(identify.equals("BXYDJZSP")){
					model.setWfState(7l);
				}
			}else if(model.getWfState()==5){
				model.setWfState(6l);
			}else if(model.getWfState()==6){
				model.setWfState(7l);
			}else{
				return false;
			}
		WorkflowService service = new WorkflowServiceImpl();
		service.doAction(Long.parseLong(entryId), workerCode, Long.parseLong(actionId), approveText, null,
				nextRoles, nextRolePerson);
		return true;
		
	}

	public void reportWorkBill(String busitNo, String flowCode,
			String workerCode,String approveText, Long actionId) {
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,busitNo);
		service.doAction(entryId, workerCode, actionId, approveText, null); 
		EquJWo model=this.findById(Long.parseLong(busitNo));
		model.setWfState(1l);
		model.setWorkFlowNo(entryId);
		update(model);
	}

	public EquJWo update(EquJWo entity) {
		// TODO Auto-generated method stub
		EquJWo model = entityManager.merge(entity);
		return model;
	}
	/**
	 * 通过缺陷单号判断是否已生成工单
	 * @param failureCode
	 * @param enterprisecode
	 * @return boolean 存在返回false
	 * add by kzhang 20101009
	 */
	public boolean checkWorkBillIsExists(String failureCode,String enterprisecode){
		String sql=
			"select count(1)\n" +
			"  from equ_j_wo a, EQU_J_WOER b\n" + 
			" where a.wo_code = b.wo_code\n" + 
			"   and a.enterprisecode = '"+enterprisecode+"'\n" + 
			"   and a.if_use = 'Y'\n" + 
			"   and b.enterprisecode = '"+enterprisecode+"'\n" + 
			"   and b.failure_code = '"+failureCode+"'";
		Long count=Long.parseLong(bll.getSingal(sql).toString());
		if (count>0) {
			return false;
		} else {
			return true;
		}
	}
}