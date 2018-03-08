/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.planregister.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.resource.EquJSparepart;
import power.ejb.resource.EquJSparepartFacadeRemote;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.MrpCPlanOriginal;
import power.ejb.resource.MrpCPlanOriginalFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementHead;
import power.ejb.resource.MrpJPlanRequirementHeadFacadeRemote;
import power.ejb.resource.form.MrpJPlanRequirementDetailEdit;
import power.ejb.run.runlog.RunCSpecialsFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.resource.planquery.action.PlanQueryAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料需求计划登记Action
 * 
 * @author zhouxu
 * 
 */
public class PlanRegisterAction extends AbstractAction {

	/** 共通控件接口 */
	BaseDataManager bll = (BaseDataManager) Ejb3Factory.getInstance()
			.getFacadeRemote("BaseDataManagerImpl");
	private static final long serialVersionUID = 1L;
	/** 主表操作接口 */
	protected MrpJPlanRequirementHeadFacadeRemote remoteHead;
	/** 明细表操作接口 */
	protected MrpJPlanRequirementDetailFacadeRemote remoteDetail;
	/** 计划来源接口 */
	protected MrpCPlanOriginalFacadeRemote remoteOriginal;
	/** 物料信息来源 */
	protected InvCMaterialFacadeRemote remoteMaterial;
	/** 设备备件接口 */
	protected EquJSparepartFacadeRemote remoteEqu;
	/** 公用接口 */
	// protected CommInterfaceFacadeRemote remoteComm;
	/** 主表bean */
	private MrpJPlanRequirementHead mr;
	/** 明细表bean */
	private MrpJPlanRequirementDetail mrd;
	/** 申请周期常量 */
	private String MR_TYPE = "月度";
	/** long型0 */
	private Long ZERO = (long) 0;
	/** 整型 0 */
	private String ZERO_STR = "0";
	public static Map<Long, Date> dtPlanDetail = new HashMap<Long, Date>();

	public SimpleDateFormat dateformat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 返回bean
	 * 
	 * @return
	 */
	public MrpJPlanRequirementHead getMr() {
		return mr;
	}

	/**
	 * 设置bean
	 * 
	 * @param mr
	 */
	public void setMr(MrpJPlanRequirementHead mr) {
		this.mr = mr;
	}

	/** 声明计划来源节点 */
	private Long node;

	/**
	 * 返回计划来源bean
	 * 
	 * @return
	 */
	public Long getNode() {
		return node;
	}

	/**
	 * 设置计划来源bean
	 * 
	 * @param node
	 */
	public void setNode(Long node) {
		this.node = node;
	}

	/**
	 * 返回明细bean
	 * 
	 * @return
	 */
	public MrpJPlanRequirementDetail getMrd() {
		return mrd;
	}

	/**
	 * 设置明细bean
	 * 
	 * @param mrd
	 */
	public void setMrd(MrpJPlanRequirementDetail mrd) {
		this.mrd = mrd;
	}

	/**
	 * 构造函数
	 */
	public PlanRegisterAction() {
		remoteHead = (MrpJPlanRequirementHeadFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementHeadFacade");
		remoteDetail = (MrpJPlanRequirementDetailFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementDetailFacade");
		remoteOriginal = (MrpCPlanOriginalFacadeRemote) factory
				.getFacadeRemote("MrpCPlanOriginalFacade");
		remoteMaterial = (InvCMaterialFacadeRemote) factory
				.getFacadeRemote("InvCMaterialFacade");
		remoteEqu = (EquJSparepartFacadeRemote) factory
				.getFacadeRemote("EquJSparepartFacade");
	}

	/**
	 * 增加一条记录到主表，和相应的明细记录到明细表
	 * 
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void addPlanRegister() throws CodeRepeatException {
		try {
			// 设置企业代码
			mr.setEnterpriseCode(employee.getEnterpriseCode());
			// 设置为未删除
			mr.setIsUse(Constants.IS_USE_Y);
			// 设置申请周期
			mr.setMrType(MR_TYPE);
			// 设置计划等级
			mr.setPlanGrade(ZERO);
			// 设置工作流状态
			mr.setMrStatus(ZERO_STR);
			// 设置最后更新者
			mr.setLastModifiedBy(employee.getWorkerCode());
			// 设置申请单编号
			mr.setMrNo(remoteHead.getMaxBookNo("JH000000",
					"MRP_J_PLAN_REQUIREMENT_HEAD", "REQUIREMENT_HEAD_ID"));
			/** 获取增加的明细内容 */
			String addMaterial = request.getParameter("addMaterial");
			/** 保存主表并返回主表流水号含回滚操作 */
			String tempString = "";
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				tx.begin();
				long headId = saveHead(mr);
				tempString = Long.toString(headId);
				if (!Constants.BLANK_STRING.equals(addMaterial)) {
					/** 将明细内容反序列化 */
					List<Map> newMaterialDetail = (List<Map>) JSONUtil
							.deserialize(addMaterial);
					// 调用本地方法保存明细内容
					addMaterialDetail(newMaterialDetail, headId);
				}
				tx.commit();
			} catch (Exception e) {
				tx.rollback();
				throw e;
			}
			write("{success:true,data:" + tempString + "}");
		} catch (Exception e) {
			write("{success:true,data:-2}");
		}
	}

	@SuppressWarnings("unchecked")
	public void updatePlanRegister() throws CodeRepeatException {
		try {
			/** 声明一个临时主表bean */
			MrpJPlanRequirementHead mrTemp = new MrpJPlanRequirementHead();
			/** 根据主表Id查找主表信息 */
			mrTemp = remoteHead.findById(mr.getRequirementHeadId());
			/** 获取新增的明细 */
			String addMaterial = request.getParameter("addMaterial");
			@SuppressWarnings("unused")
			/** 获取更新的明细 */
			String updateMaterial = request.getParameter("updateMaterial");
			/** 获取删除的明细ID */
			String deleteMaterialIds = request
					.getParameter("deleteMaterialIds");
			/** 获取删除的明细最后修改时间 */
			String deleteMaterialIdsD = request
					.getParameter("deleteMaterialIdsD");
			/** 删除排他标志字段 */
			boolean delCheck = true;
			/** 更新排他标志字段 */
			boolean updateCheck = true;
			if (!Constants.BLANK_STRING.equals(deleteMaterialIds)) {
				if (deleteMaterialIds == null || deleteMaterialIds.length() < 1) {
					delCheck = true;
				}
				String[] id = deleteMaterialIds.split(",");
				String[] dates = deleteMaterialIdsD.split(",");
				// 检索已删除的记录
				for (int i = 0; i < id.length; i++) {
					// 物料信息
					MrpJPlanRequirementDetail material = remoteDetail
							.findById(Long.parseLong(id[i]), employee
									.getEnterpriseCode());
					// 修改者
					Date lastDate = material.getLastModifiedDate();
					if (!dateformat.format(lastDate).equals(
							getString(dates[i]).replace("T", " "))) {
						delCheck = false;
						break;
					}
					delCheck = true;
				}

			}

			// 判断是否有更新的内容
			if (!Constants.BLANK_STRING.equals(updateMaterial)) {
				/** 将更新的内容json格式化 */
				List<Map> newMaterialDetail = (List<Map>) JSONUtil
						.deserialize(updateMaterial);
				/** 对更新部分做排他check返回结果给标志字段 */
				updateCheck = checkDetailTime(newMaterialDetail);
			}
			// 判断所有排他都是满足的
			if (dateformat.format(mr.getLastModifiedDate()).equals(
					dateformat.format(mrTemp.getLastModifiedDate()))
					&& delCheck && updateCheck) {
				// 设置需求日期
				mrTemp.setDueDate(mr.getDueDate());
				// 设置计划来源
				mrTemp.setPlanOriginalId(mr.getPlanOriginalId());
				// 设置费用来源
				if (mr.getItemCode() != null && !"".equals(mr.getItemCode())) {
					mrTemp.setItemCode(mr.getItemCode());// modify by ywliu
															// 2009/7/6
				}
				// 设置归口部门
				if (mr.getCostDept() != null
						&& !Constants.BLANK_STRING.equals(mr.getCostDept())) {
					mrTemp.setCostDept(mr.getCostDept());
				}
				// 设置归口专业
				mrTemp.setCostSpecial(mr.getCostSpecial());
				// 设置企业代码
				mrTemp.setLastModifiedBy(employee.getWorkerCode());
				// 设置计划等级
				mrTemp.setPlanGrade(ZERO);
				// 设置备注
				mrTemp.setMrReason(mr.getMrReason());
				// add by fyyang 091019 设置月份/季度
				mrTemp.setPlanDateMemo(mr.getPlanDateMemo());
				
				mrTemp.setPrjNo(mr.getPrjNo());//add by fyyang 20100507
				@SuppressWarnings("unused")
				/** 获取主表Id */
				long headId = mrTemp.getRequirementHeadId();
				/** 更新主表数据 */
				UserTransaction tx = (UserTransaction) new InitialContext()
						.lookup("java:comp/UserTransaction");
				try {
					tx.begin();
					updateHead(mrTemp);
					if (!Constants.BLANK_STRING.equals(addMaterial)) {
						/** 将新增明细json格式化 */
						List<Map> newMaterialDetail = (List<Map>) JSONUtil
								.deserialize(addMaterial);
						// 新增明细关联到主表Id
						addMaterialDetail(newMaterialDetail, headId);

					}
					if (!Constants.BLANK_STRING.equals(deleteMaterialIds)) {
						// 删除明细
						deleteMaterialDetail(deleteMaterialIds);
					}
					if (!Constants.BLANK_STRING.equals(updateMaterial)) {
						/** 将更新明细json格式化 */
						List<Map> newMaterialDetail = (List<Map>) JSONUtil
								.deserialize(updateMaterial);
						updateMaterialDetail(newMaterialDetail);
					}
					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					throw e;
				}
				// remoteHead.update(mrTemp);
				// // 判断删除明细是否为空，执行删除明细操作
				// if (!Constants.BLANK_STRING.equals(deleteMaterialIds)) {
				// // 删除明细
				// deleteMaterialDetail(deleteMaterialIds);
				// }
				// // 判断新增明细是否为空，执行新增明细操作
				// if (!Constants.BLANK_STRING.equals(addMaterial)) {
				// /** 将新增明细json格式化 */
				// List<Map> newMaterialDetail = (List<Map>)
				// JSONUtil.deserialize(addMaterial);
				// try {
				// // 新增明细关联到主表Id
				// addMaterialDetail(newMaterialDetail, headId);
				// } catch (CodeRepeatException e) {
				// return;
				// }
				// }
				// // 判断更新明细是否为空，执行更新明细操作
				// if (!Constants.BLANK_STRING.equals(updateMaterial)) {
				// /** 将更新明细json格式化 */
				// List<Map> newMaterialDetail = (List<Map>)
				// JSONUtil.deserialize(updateMaterial);
				// try {
				// // 更新明细
				// updateMaterialDetail(newMaterialDetail);
				// } catch (CodeRepeatException e) {
				// return;
				// }
				// }
				/** 返回被更新的主表Id */
				String headIdString = Long.toString(headId);
				// 如果成功返回主表id
				write("{success:true,data:" + headIdString + "}");
			} else {
				// 排他失败返回
				write("{success:true,data:-1}");
			}
		} catch (Exception e) {
			// 数据更新异常返回
			write("{success:true,data:-2}");
		}
	}

	/**
	 * 删除主表和明细表
	 * 
	 * @throws CodeRepeatException
	 */
	public void deletePlanRegister() throws CodeRepeatException {
		try {
			// 判断有无需要删除的主表Id
			if (!Constants.BLANK_STRING.equals(request.getParameter("headId"))) {
				/** 设置要删除的主表Id */
				Long idLong = Long.parseLong(request.getParameter("headId"));
				/** 设置要删除的主表Id最后更新时间 */
				@SuppressWarnings("unused")
				String headDate = request.getParameter("lastModifiedDate");
				/** 实例化一个主表bean */
				MrpJPlanRequirementHead mrTemp = new MrpJPlanRequirementHead();
				/** 定义一个明细部分排他标志字段 */
				boolean detailCheck = true;
				/** 查询返回要删除的主表Id的记录Bean */
				mrTemp = remoteHead.findById(idLong);
				/** 获取要删除的明细部分Id记录集 */
				String[] detailIds = request.getParameter("detailId")
						.split(",");
				/** 获取要删除的明细部分Id最后修改时间记录集 */
				String[] detailDates = request.getParameter("detailIdD").split(
						",");
				for (int i = 0; i < detailIds.length; i++) {
					MrpJPlanRequirementDetail mrD = remoteDetail.findById(Long
							.parseLong(detailIds[i]), employee
							.getEnterpriseCode());
					if (!dateformat.format(mrD.getLastModifiedDate()).equals(
							getString(detailDates[i]).replace("T", " "))) {
						detailCheck = false;
						break;
					}

				}
				// 判断所有排他都正确，进行删除操作
				if (dateformat
						.format(PlanQueryAction.dtPlanHead.get(idLong))
						.equals(dateformat.format(mrTemp.getLastModifiedDate()))
						&& detailCheck) {
					// 设置要删除的记录删除标记字段为N
					mrTemp.setIsUse(Constants.IS_USE_N);
					// 设置最后修改人
					mrTemp.setLastModifiedBy(employee.getWorkerCode());
					// 删除记录

					UserTransaction tx = (UserTransaction) new InitialContext()
							.lookup("java:comp/UserTransaction");
					try {
						tx.begin();
						// 更新主表
						updateHead(mrTemp);
						List<MrpJPlanRequirementDetail> mrD = remoteDetail
								.findByProperty(employee.getEnterpriseCode(),
										"requirementHeadId", idLong);
						// 循环删除记录集中明细记录
						for (MrpJPlanRequirementDetail temp : mrD) {
							// 设置明细部分记录为删除状态
							temp.setIsUse(Constants.IS_USE_N);
							// 设置最后更新人
							temp.setLastModifiedBy(employee.getWorkerCode());
							// 删除明细记录
							updateDetail(temp);
						}
						tx.commit();
					} catch (Exception e) {
						tx.rollback();
						throw e;
					}

					// remoteHead.update(mrTemp);
					// /** 查询并返回所有关联主表Id有效的明细记录集 */
					// List<MrpJPlanRequirementDetail> mrD =
					// remoteDetail.findByProperty(employee.getEnterpriseCode(),
					// "requirementHeadId", idLong);
					// // 循环删除记录集中明细记录
					// for (MrpJPlanRequirementDetail temp : mrD) {
					// // 设置明细部分记录为删除状态
					// temp.setIsUse(Constants.IS_USE_N);
					// // 设置最后更新人
					// temp.setLastModifiedBy(employee.getWorkerCode());
					// // 删除明细记录
					// remoteDetail.update(temp);
					// }
					// // 成功返回
					write("{success:true,data:0}");
				} else {
					// 排他失败返回
					write("{success:true,data:-1}");
				}
			}
		} catch (Exception e) {
			// 数据库操作异常返回
			write("{success:true,data:-2}");
		}
	}

	/**
	 * 删除物料明细需求记录
	 * 
	 * @param ids
	 * @throws CodeRepeatException
	 */
	private void deleteMaterialDetail(String ids) throws CodeRepeatException {
		// 如果id集为空不需要删除
		if (ids == null || ids.length() < 1) {
			return;
		}
		/** 获取需要删除的明细记录集 */
		String[] id = ids.split(",");
		// 检索需要删除的记录
		for (int i = 0; i < id.length; i++) {
			/** 返回需要删除的明细记录 */
			MrpJPlanRequirementDetail material = remoteDetail.findById(Long
					.parseLong(id[i]), employee.getEnterpriseCode());
			// 设置最后修改人
			material.setLastModifiedBy(employee.getWorkerCode());
			// 设置删除标记
			material.setIsUse(Constants.IS_USE_N);
			// 更新
			remoteDetail.update(material);
		}
	}

	/**
	 * 增加物料明细
	 * 
	 * @param materialDetailList
	 * @param headId
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings( { "unused", "unchecked" })
	private void addMaterialDetail(List<Map> materialDetailList, long headId)
			throws CodeRepeatException {
		Map map;
		/** 声明一个明细bean */
		MrpJPlanRequirementDetail materialDetail;
		/** 实例化一个日期格式化 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 循环新增需要新增的明细
		for (int i = 0; i < materialDetailList.size(); i++) {
			/** 获取标志位 */
			map = materialDetailList.get(i);
			/** 实例化一个明细bean */
			materialDetail = new MrpJPlanRequirementDetail();
			// 设置物料id
			materialDetail.setMaterialId(Long.parseLong(getString(map
					.get("materialId"))));
			/** 根据物料id和设备code关联获取设备id */
			Long tempId = materialAndEquId(Long.parseLong(getString(map
					.get("materialId"))), getString(map.get("equCode")));
			// 设置设备id
			materialDetail.setEquSparepartId(tempId);
			// 设置申请数量
			materialDetail.setAppliedQty(Double.parseDouble(getString(map
					.get("appliedQty"))));
			// 设置建议厂家
			materialDetail.setSupplier(getString(map.get("supplier")));
			// 格式化需求日期
			try {
				materialDetail.setDueDate(dateFormat.parse(getString(map
						.get("needDate"))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// 设置工单项次号默认为0
			materialDetail.setWoLine(ZERO);
			if (getString(map.get("estimatedPrice")) != null
					&& !"".equals(getString(map.get("estimatedPrice")))) {
				// 设置建议价格
				materialDetail.setEstimatedPrice(Double
						.parseDouble(getString(map.get("estimatedPrice"))));
			}
			// 设置用途
			materialDetail.setUsage(getString(map.get("usage")));
			// 设置备注
			materialDetail.setMemo(getString(map.get("memo")));
			// 设置关联的主表Id
			materialDetail.setRequirementHeadId(headId);
			// 设置企业编码
			materialDetail.setEnterpriseCode(employee.getEnterpriseCode());
			// 最后更新者
			materialDetail.setLastModifiedBy(employee.getWorkerCode());
			// 设置计划来源
			// modify by fyyang 090618 明细的计划来源不存
			// materialDetail.setPlanOriginalId(Long.parseLong(getString(map.get("planOriginalId"))));
			// 设置费用来源
			materialDetail.setItemCode(mr.getItemCode());// modify by ywliu
															// 2009/7/6
			// 设置计划等级
			materialDetail.setPlanGrade(ZERO);
			// 设置采购单标记
			materialDetail.setIsGenerated(Constants.IS_USE_N);
			// 设置为Y
			materialDetail.setIsUse(Constants.IS_USE_Y);
			remoteDetail.save(materialDetail);
		}
	}

	/**
	 * 更新物料明细
	 * 
	 * @param materialDetailList
	 * @param headId
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	private void updateMaterialDetail(List<Map> materialDetailList)
			throws CodeRepeatException {
		Map map;
		/** 实例化一个日期格式化 */
		MrpJPlanRequirementDetail materialDetail;
		/** 实例化一个日期格式化 */
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 循环更新需要更新的明细
		for (int i = 0; i < materialDetailList.size(); i++) {
			/** 获取标志位 */
			map = materialDetailList.get(i);
			/** 实例化一个明细bean */
			materialDetail = new MrpJPlanRequirementDetail();
			// 根据明细Id查找bean
			if (!Constants.BLANK_STRING.equals(getString(map
					.get("requirementDetailId")))) {
				materialDetail = remoteDetail.findById(Long
						.parseLong(getString(map.get("requirementDetailId"))),
						employee.getEnterpriseCode());
			}
			/** 根据物料id和设备code关联获取设备id */
			Long tempId = materialAndEquId(Long.parseLong(getString(map
					.get("materialId"))), getString(map.get("equCode")));
			materialDetail.setEquSparepartId(tempId);
			materialDetail.setMaterialId(Long.parseLong(getString(map
					.get("materialId"))));
			materialDetail.setAppliedQty(Double.parseDouble(getString(map
					.get("appliedQty"))));
			materialDetail.setSupplier(getString(map.get("supplier")));
			try {
				materialDetail.setDueDate(dateFormat.parse(getString(map
						.get("needDate"))));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// modify By ywliu 090518
			if (map.get("estimatedPrice") != null
					&& !"".equals(map.get("estimatedPrice"))) {
				materialDetail.setEstimatedPrice(Double
						.parseDouble(getString(map.get("estimatedPrice"))));
			}
			materialDetail.setUsage(getString(map.get("usage")));
			materialDetail.setMemo(getString(map.get("memo")));
			materialDetail.setLastModifiedBy(employee.getWorkerCode());
			// modify by fyyang 090618
			// materialDetail.setPlanOriginalId(Long.parseLong(getString(map.get("planOriginalId"))));
			if (mr.getItemCode() != null && !"".equals(mr.getItemCode())) {
				materialDetail.setItemCode(mr.getItemCode());// modify by
																// ywliu
																// 2009/7/6
			}
			materialDetail.setPlanGrade((long) 0);
			materialDetail.setIsGenerated(Constants.IS_USE_N);
			remoteDetail.update(materialDetail);
		}
	}

	/**
	 * 计划单上报
	 * 
	 * @throws CodeRepeatException
	 */
	public void reportPlan() throws CodeRepeatException {
		try {
			// 判断获取到要上报的计划单id
			if (!Constants.BLANK_STRING.equals(request.getParameter("headId"))) {
				/** 设置要上报的计划单id */
				Long idLong = Long.parseLong(request.getParameter("headId"));
				/** 实例化一个主表bean */
				MrpJPlanRequirementHead mrTemp = new MrpJPlanRequirementHead();
				/** 设置明细排他标志字段 */
				boolean detailCheck = true;
				/** 根据主表id返回bean */
				mrTemp = remoteHead.findById(idLong);
				/** 获取需要上报计划单关联的明细id */
				String[] detailIds = request.getParameter("detailId")
						.split(",");
				/** 获取需要上报计划单关联的明细id最后修改时间 */
				String[] detailDates = request.getParameter("detailIdD").split(
						",");
				for (int i = 0; i < detailIds.length; i++) {
					MrpJPlanRequirementDetail mrD = remoteDetail.findById(Long
							.parseLong(detailIds[i]), employee
							.getEnterpriseCode());
					if (!dateformat.format(mrD.getLastModifiedDate()).equals(
							getString(detailDates[i]).replace("T", " "))) {
						detailCheck = false;
						break;
					}

				}
				// 判断所有排他都正确，进行上报操作
				if (dateformat
						.format(PlanQueryAction.dtPlanHead.get(idLong))
						.equals(dateformat.format(mrTemp.getLastModifiedDate()))
						&& detailCheck) {
					mrTemp.setLastModifiedBy(employee.getWorkerCode());
					// 设置上报标记
					mrTemp.setMrStatus("1");
					UserTransaction tx = (UserTransaction) new InitialContext()
							.lookup("java:comp/UserTransaction");
					try {
						tx.begin();
						updateHead(mrTemp);
						tx.commit();
					} catch (Exception e) {
						tx.rollback();
						throw e;
					}
					remoteHead.update(mrTemp);
					// 上报成功返回
					write("{success:true,data:0}");
				} else {
					// 排他失败返回
					write("{success:true,data:-1}");
				}
			}
		} catch (Exception e) {
			// 数据库操作异常返回
			write("{success:true,data:-2}");
		}
	}

	/**
	 * 关联物料ID和设备名称
	 * 
	 * @param materialId
	 * @param attributeCode
	 * @return
	 */
	private Long materialAndEquId(Long materialId, String attributeCode)
			throws CodeRepeatException {
		/** 获取企业编码 */
		String enterpriseCode = employee.getEnterpriseCode();
		/** 获取最后修改者 */
		String user = employee.getWorkerCode();
		/** 实例化一个设备备件bean */
		EquJSparepart temp = new EquJSparepart();
		/** 根据物料Id 设备code 关联，并返回设备Id */
		temp = remoteEqu.findByMaterialIdAndAttributeCode(materialId,
				attributeCode, enterpriseCode, user);
		// 返回设备id
		return temp.getEquSparepartId();
	}

	/**
	 * 获取计划来源类型（M或O）
	 * 
	 * @throws JSONException
	 */
	public void getOriginalType() throws JSONException {
		// 判断节点是否为空
		if (!"".equals(request.getParameter("nodeId"))&& !"null".equals(request.getParameter("nodeId"))) {
			/** 获取节点值 */
			long nodeId = Long.parseLong(request.getParameter("nodeId"));
			/** 查询节点bean */
			MrpCPlanOriginal str = remoteOriginal.findById(nodeId);
			// 判断节点属性是否为空
			if (!"".equals(str.getOriginalType())) {
				String type = str.getOriginalType();
				// 返回节点类型
				write(type);
			}
		}
	}

	/**
	 * 获得字符串值
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}

	/**
	 * 生成树的数据
	 * 
	 * @throws JSONException
	 */
	public void getPlanOriginalNode() throws JSONException {
		// 序列化为JSON对象的字符串形式
		String str = toJSONStr(remoteOriginal.findByParentId(node, employee
				.getEnterpriseCode()), node);
		// 以html方式输出字符串
		write(str);
	}

	/**
	 * 将list转换为json格式数据
	 * 
	 * @param 计划来源
	 * 
	 * @return json格式数据
	 */
	private String toJSONStr(List<MrpCPlanOriginal> palnOriginalList, Long node) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("[");
		if (node == 0) {
			sbf.append("{\"text\":\"请选择\",\"id\":\"null\",\"leaf\":" + true + "},");
		}
		MrpCPlanOriginal ori = null;
		for (int intCnt = 0; intCnt < palnOriginalList.size(); intCnt++) {
			// 需求计划来源对象
			ori = palnOriginalList.get(intCnt);
			// 是否是叶子节点
			boolean isLeaf = isLeafdept(ori.getPlanOriginalId());
			// 设置json格式化
			sbf.append("{\"text\":\"" + ori.getPlanOriginalDesc()
					+ "\",\"id\":\"" + getEmptyString(ori.getPlanOriginalId())
					+ "\",\"type\":\"" + ori.getOriginalType() + "\",\"leaf\":"
					+ isLeaf + "},");
		}
		if (sbf.length() > 1) {
			sbf.deleteCharAt(sbf.lastIndexOf(","));
		}
		sbf.append("]");
		return sbf.toString();
	}

	/**
	 * 返回非NULL字符串
	 * 
	 * @param argObj
	 *            对象
	 * @return 非NULL字符串
	 */
	private String getEmptyString(Object argObj) {
		return argObj == null ? "" : argObj.toString();
	}

	/**
	 * 是否是叶子节点
	 * 
	 * @param pid
	 * @return
	 */
	private boolean isLeafdept(Long pid) {
		List<MrpCPlanOriginal> ld = remoteOriginal.findByParentId(pid, employee
				.getEnterpriseCode());
		if (ld != null && ld.size() > 0)
			return false;
		return true;
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @throws CodeRepeatException
	 */
	public void getInfo() throws CodeRepeatException {
		Employee result = null;
		result = bll.getEmployeeInfo(employee.getWorkerCode());
		try {
			if (result == null) {
				super.write("{}");
			} else {
				super.write(JSONUtil.serialize(result));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			super.write("{}");
		}
	}

	/**
	 * 获取物料明细
	 * 
	 * @throws JSONException
	 */
	public void getMaterialInfo() throws JSONException {
		if (!Constants.BLANK_STRING.equals(request.getParameter("materialId"))) {
			PageObject obj = new PageObject();
			List<InvCMaterial> list = new ArrayList<InvCMaterial>();
			InvCMaterial temp = remoteMaterial.findById(Long.parseLong(request
					.getParameter("materialId")));
			list.add(temp);
			obj.setList(list);
			String str = JSONUtil.serialize(obj);
			write(str);
		}
	}

	/**
	 * 根据物料Id获取当前库存
	 * 
	 * @throws JSONException
	 */
	public void getMaterialStock() throws JSONException {
		// 如果有物料id
		if (!Constants.BLANK_STRING.equals(request.getParameter("materialId"))) {
			write(remoteHead.getMaterialStock(Long.parseLong(request
					.getParameter("materialId")), employee.getEnterpriseCode()));
		}
	}

	/**
	 * 获取头部form信息
	 * 
	 * @throws JSONException
	 */
	public void getHeadInfo() throws JSONException {
		// 如果有主表Id
		if (!Constants.BLANK_STRING.equals(request.getParameter("headId"))) {
			MrpJPlanRequirementHead headInfo = remoteHead.findByIdWithName(Long
					.parseLong(request.getParameter("headId")));
			String headInfoStr = JSONUtil.serialize(headInfo);
			write("{success: true,data:" + headInfoStr + "}");
		}
	}
	public void getFormInfo() throws JSONException {
		// 如果有主表Id
		if (!Constants.BLANK_STRING.equals(request.getParameter("headId"))) {
			MrpJPlanRequirementHead headInfo = remoteHead.findMaterialById(Long
					.parseLong(request.getParameter("headId")));
			String headInfoStr = JSONUtil.serialize(headInfo);
			write("{success: true,data:" + headInfoStr + "}");
		}
	}
	/**
	 * 获取计划来源名称
	 * 
	 * @throws JSONException
	 */
	public void getOrigianlName() throws JSONException {
		// 如果计划来源Id不为空
		if (!Constants.BLANK_STRING.equals(request
				.getParameter("planOrigianlId"))) {
			MrpCPlanOriginal ld = remoteOriginal.findById(Long
					.parseLong(request.getParameter("planOrigianlId")));
			if (ld != null
					&& !Constants.BLANK_STRING.equals(ld.getPlanOriginalDesc())) {
				String str = JSONUtil.serialize(ld);
				write("{success: true,data:" + str + "}");
			} else {
				write("{success: true,data:null }");
			}
		}
	}
	/**
	 * 获取物料明细
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getMaterial() throws JSONException {
		/** 获取主表id */
		String headId = request.getParameter("headId");
		PageObject obj = new PageObject();
		/** 如果主表Id不为空 */
		if (!Constants.BLANK_STRING.equals(headId)) {
			/** 根据主表Id返回主表信息 */
			obj = remoteHead.getMaterial(employee.getEnterpriseCode(), Long
					.parseLong(headId));
			// 判断结果是否为空
			if (null == obj.getList()) {
				List list = new ArrayList();
				obj.setList(list);
			} else {
				/** 实例化一个明细bean */
				MrpJPlanRequirementDetailEdit temp = new MrpJPlanRequirementDetailEdit();
				List<MrpJPlanRequirementDetailEdit> arrlist = obj.getList();
				/** 获取明细部分的最后修改时间 */
				for (int i = 0; i < arrlist.size(); i++) {
					temp = arrlist.get(i);
					dtPlanDetail.put(temp.getRequirementDetailId(), temp
							.getLastModifiedDate());
				}
			}
			/** json格式化结果 */
			String str = JSONUtil.serialize(obj);
			// 如果查询返回结果为空，则替换为如下返回结果
			if (Constants.BLANK_STRING.equals(str) || null == str) {
				str = "{\"list\":[],\"totalCount\":null}";
			}
			// 返回结果
			write(str);
		} else {
			// 错误返回
			write("false");
		}
	}

	/**
	 * 获取物料编辑明细
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialEdit() throws JSONException {
		/** 获取主表id */
		String headId = request.getParameter("headId");
		PageObject obj = new PageObject();
		/** 如果主表Id不为空 */
		if (!Constants.BLANK_STRING.equals(headId)) {
			/** 根据主表Id返回主表信息 */
			obj = remoteHead.getMaterialEdit(employee.getEnterpriseCode(), Long
					.parseLong(headId));
			// 判断结果是否为空
			if (null == obj.getList()) {
				List list = new ArrayList();
				obj.setList(list);
			} else {
				/** 实例化一个明细bean */
				MrpJPlanRequirementDetailEdit temp = new MrpJPlanRequirementDetailEdit();
				List<MrpJPlanRequirementDetailEdit> arrlist = obj.getList();
				/** 获取明细部分的最后修改时间 */
				for (int i = 0; i < arrlist.size(); i++) {
					temp = arrlist.get(i);
					dtPlanDetail.put(temp.getRequirementDetailId(), temp
							.getLastModifiedDate());
				}
			}
			/** json格式化结果 */
			String str = JSONUtil.serialize(obj);
			// 如果查询返回结果为空，则替换为如下返回结果
			if (Constants.BLANK_STRING.equals(str) || null == str) {
				str = "{\"list\":[],\"totalCount\":null}";
			}
			// 返回结果
			write(str);
		} else {
			// 错误返回
			write("false");
		}
	}

	/**
	 * 生成专业接口list
	 */
	public void getSpeciality() throws JSONException {
		/** 实例化一个pageobj对应 */
		PageObject obj = new PageObject();
		// 对应 commInterface jar包删除
		/** 实例化专业接口 */
		RunCSpecialsFacadeRemote speRemote = (RunCSpecialsFacadeRemote) factory
				.getFacadeRemote("RunCSpecialsFacade");
		// 通过共通方法获取专业接口的list
		obj.setList(speRemote.findSpeList(employee.getEnterpriseCode()));
		// json格式化
		String str = JSONUtil.serialize(obj);
		// 如果查询返回结果为空，则替换为如下返回结果
		if (Constants.BLANK_STRING.equals(str) || null == str) {
			str = "{\"list\":[],\"totalCount\":null}";
		}
		// 返回结果
		write(str);
	}

	/**
	 * 通过费用编码查出费用名称(物资公用方法)
	 * 
	 */
	public void getItemNameByItemCode() {
		String itemCode = request.getParameter("itemCode");
		String itemName = remoteHead.getItemNameByItemCode(itemCode);
		// 返回结果
		write(itemName);
	}

	/**
	 * 明细部门排他
	 * 
	 * @param materialDetailList
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private boolean checkDetailTime(List<Map> materialDetailList)
			throws Exception {
		Map map;
		MrpJPlanRequirementDetail materialDetail;
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		for (int i = 0; i < materialDetailList.size(); i++) {
			map = materialDetailList.get(i);
			materialDetail = new MrpJPlanRequirementDetail();
			if (!Constants.BLANK_STRING.equals(getString(map
					.get("requirementDetailId")))) {
				materialDetail = remoteDetail.findById(Long
						.parseLong(getString(map.get("requirementDetailId"))),
						employee.getEnterpriseCode());
			}
			Date lastDate = materialDetail.getLastModifiedDate();
			if (!dateformat.format(lastDate).equals(
					getString(map.get("lastModifiedDate")).replace("T", " "))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 更新主表
	 * 
	 * @param mr
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unused")
	private void updateHead(MrpJPlanRequirementHead mr)
			throws CodeRepeatException {
		remoteHead.update(mr);
	}

	/**
	 * 保存主表
	 * 
	 * @param mr
	 * @return
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unused")
	private Long saveHead(MrpJPlanRequirementHead mr)
			throws CodeRepeatException {
		return remoteHead.save(mr).getRequirementHeadId();
	}

	/**
	 * 更新明细表
	 * 
	 * @param mrd
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unused")
	private void updateDetail(MrpJPlanRequirementDetail mrd)
			throws CodeRepeatException {
		remoteDetail.update(mrd);
	}

}
