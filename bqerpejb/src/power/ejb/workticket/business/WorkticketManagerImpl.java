package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.WorkticketSafetyContent;

@Stateless
public class WorkticketManagerImpl implements WorkticketManager {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected RunJWorkticketsFacadeRemote baseRemote;
	protected RunJWorkticketMapFacadeRemote mapRemote;
	protected RunJWorkticketContentFacadeRemote contentRemote;
	protected RunJWorkticketSafetyFacadeRemote safetyRemote;

	protected RunJWorkticketActorsFacadeRemote actorRemote;
	protected RunJWorkticketFireContentFacadeRemote fireContentRemote;
	protected RunJWorkticketDangerFacadeRemote dangerRemote;

	public WorkticketManagerImpl() {
		baseRemote = (RunJWorkticketsFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorkticketsFacade");
		mapRemote = (RunJWorkticketMapFacadeRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("RunJWorkticketMapFacade");
		contentRemote = (RunJWorkticketContentFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketContentFacade");
		safetyRemote = (RunJWorkticketSafetyFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketSafetyFacade");

		actorRemote = (RunJWorkticketActorsFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketActorsFacade");
		fireContentRemote = (RunJWorkticketFireContentFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote(
						"RunJWorkticketFireContentFacade");
		dangerRemote = (RunJWorkticketDangerFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("RunJWorkticketDangerFacade");
	}

	public RunJWorktickets findWorkticketByNo(String workticketNo) {
		return baseRemote.findById(workticketNo);
	}

	/**
	 * 创建工作票
	 */

	public RunJWorktickets createWorkticket(RunJWorktickets entity,
			String enterpriseChar, String dangerId, String workCode,
			String standticketNo, String existWorkticketNo, byte[]... image) {
		if (entity.getIsCreatebyStand().equals("Y")) {
			// 由标准票生成
			// 创建票号
			entity.setWorkticketNo(baseRemote.createWorkticketNo(entity
					.getEnterpriseCode(), entity.getWorkticketTypeCode(),
					entity.getChargeDept(), entity.getFirelevelId())
					+ "B");
			entity = baseRemote.save(entity);
			if (entity.getWorkticketTypeCode().equals("1") && image != null
					&& image.length > 0) {
				RunJWorkticketMap mapEntity = new RunJWorkticketMap();
				mapEntity.setWorkticketNo(entity.getWorkticketNo());
				mapEntity.setWorkticketMap(image[0]);
				mapRemote.save(mapEntity);
			}
			// copy安措
			// modify by fyyang 090316 安措内容表已不使用
			// baseRemote.copySafetyForWorkticket(entity.getEnterpriseCode(),
			// entity.getWorkticketTypeCode(), entity.getWorkticketNo());
			// 由标准票生成的
			// copy工作内容
			baseRemote.copyMainTicketContent(entity.getEnterpriseCode(),
					standticketNo, entity.getWorkticketNo(), workCode, "");
			// copy安措明细
			baseRemote.copyWorkticketSafetyDetail(standticketNo, entity
					.getWorkticketNo(), workCode, "");
			// 复制安措内容
			// modify by fyyang 090311 安措内容不回写
			// baseRemote.copySafetyContentByOld(standticketNo,entity.getWorkticketNo()
			// );
			// copy 危险点分析
			baseRemote.copyDangerByOldTicket(standticketNo, entity
					.getWorkticketNo(), workCode);
			entityManager.flush();
			// 回写主表内容
			// 工作内容不回写 modify by fyyang 090311 工作内容回写 090513
			// baseRemote.updateWorkticketContent(entity.getWorkticketNo());
			// contentRemote.updateWorkticketsContent(entity.getWorkticketNo());
		} else {
			// 不是由标准票生成的
			if (entity.getWorkticketTypeCode().equals("1")) {
				if (image != null && image.length > 0) {
					entity = this.createBase(entity, enterpriseChar, image[0]);
				} else {
					entity = this.createBase(entity, enterpriseChar);
				}
			} else {
				entity = this.createBase(entity, enterpriseChar);
			}
			entityManager.flush();
			// 回写主表工作内容
			// 工作内容不回写 modify by fyyang 090311 工作内容回写 090513
			if (entity.getWorkticketTypeCode().equals("4")) {
				// contentRemote
				// .updateWorkticketsContent(entity.getWorkticketNo());
				// baseRemote.updateWorkticketContent(entity.getWorkticketNo());
			}
			if (!dangerId.equals("")) {
				baseRemote.copyDanger(entity.getWorkticketNo(), dangerId,
						workCode);
			}

		}

		// ------------add by fyyang 090523 -----------------
		if (existWorkticketNo != null && !existWorkticketNo.equals("")) {
			baseRemote.copyWorkticketSafetyDetail(existWorkticketNo, entity
					.getWorkticketNo(), workCode, "");
			baseRemote.copyDangerByOldTicket(existWorkticketNo, entity
					.getWorkticketNo(), workCode);

		}
		// ----------------------------------------------------
		return entity;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RunJWorktickets createBase(RunJWorktickets entity,
			String enterpriseChar, byte[]... image) {

		if ("Y".equals(entity.getIsStandard())) {
			entity.setWorkticketNo(baseRemote.createStandardWorkticketNo(entity
					.getEnterpriseCode(), entity.getWorkticketTypeCode(),
					entity.getFirelevelId()));
		} else {
			// 创建票号
			entity.setWorkticketNo(baseRemote.createWorkticketNo(entity
					.getEnterpriseCode(), entity.getWorkticketTypeCode(),
					entity.getChargeDept(), entity.getFirelevelId()));
		}
		// add 090122
		// 是否由标准票生成
		entity.setIsCreatebyStand("N");
		// 保存基本信息
		entity = baseRemote.save(entity);
		// copy安措
		// modify by fyyang 090316 安措内容表已不使用
		// baseRemote.copySafetyForWorkticket(entity.getEnterpriseCode(),
		// entity.getWorkticketTypeCode(), entity.getWorkticketNo());
		if (entity.getWorkticketTypeCode().equals("1")) {
			// 电一票保存图片
			if (image != null && image.length > 0) {
				RunJWorkticketMap mapEntity = new RunJWorkticketMap();
				mapEntity.setWorkticketNo(entity.getWorkticketNo());
				mapEntity.setWorkticketMap(image[0]);
				mapRemote.save(mapEntity);
			}
		}

		if (entity.getWorkticketTypeCode().equals("4")) {
			// 动火票，copy关联主票的内容
			baseRemote.copyMainTicketContent(entity.getEnterpriseCode(), entity
					.getRefWorkticketNo(), entity.getWorkticketNo(), entity
					.getEntryBy(), entity.getEntryDate().toString());

		}
		return entity;
	}

	/**
	 * 修改工作票基本信息
	 */
	public RunJWorktickets updateWorkticket(RunJWorktickets entity,
			String dangerId, String workCode, byte[]... image) {
		if (image != null && image.length > 0) {
			entity = this.updateBase(entity, image[0]);
		} else {
			entity = this.updateBase(entity);
		}

		entityManager.flush();
		// 回写主表工作内容
		// 工作内容不回写 modify by fyyang 090311 工作内容回写 090513
		if (entity.getWorkticketTypeCode().equals("4")) {
			// contentRemote.updateWorkticketsContent(entity.getWorkticketNo());
			// baseRemote.updateWorkticketContent(entity.getWorkticketNo());
		}
		if (dangerId != null && !dangerId.equals("")) {
			baseRemote.updateDanger(entity.getWorkticketNo(), dangerId,
					workCode);
		}

		return entity;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RunJWorktickets updateBase(RunJWorktickets entity, byte[]... image) {
		if (entity.getWorkticketTypeCode().equals("1")) {
			// 电气一种票 修改图片
			if (image != null && image.length > 0) {
				RunJWorkticketMap mapEntity = mapRemote
						.findByWorkticketNo(entity.getWorkticketNo());
				if (mapEntity != null) {
					mapEntity.setWorkticketMap(image[0]);
					mapRemote.update(mapEntity);
				} else {
					mapEntity = new RunJWorkticketMap();
					mapEntity.setWorkticketNo(entity.getWorkticketNo());
					mapEntity.setWorkticketMap(image[0]);
					mapRemote.save(mapEntity);
				}
			}
		}

		if (entity.getWorkticketTypeCode().equals("4")
				&& entity.getIsStandard().equals("N")) {
			// 动火票，修改工作内容
			RunJWorktickets model = baseRemote.findById(entity
					.getWorkticketNo());
			if (model != null) {
				if (!model.getRefWorkticketNo().equals(
						entity.getRefWorkticketNo())) {
					// 删除动火票对应的工作内容
					// contentRemote.deleteByNO(entity.getWorkticketNo());

					// copy新的关联主票的内容
					baseRemote.copyMainTicketContent(
							entity.getEnterpriseCode(), entity
									.getRefWorkticketNo(), entity
									.getWorkticketNo(), entity.getEntryBy(),
							entity.getEntryDate().toString());

				}
			}
		}
		entity = baseRemote.update(entity);
		return entity;
	}

	/**
	 * 删除未上报的工作票
	 * 
	 * @param workticketNo
	 */
	public void deleteWorkticket(String workticketNo) {

		RunJWorktickets model = baseRemote.findById(workticketNo);
		if (model.getWorkticketStausId() == 1l
				|| model.getWorkticketStausId() == 5l) {
			// 仅未上报的允许删除
			if (model.getWorkticketTypeCode().equals("1")) {
				// 删除电一票图片
				RunJWorkticketMap mapEntity = mapRemote
						.findByWorkticketNo(workticketNo);
				if (mapEntity != null) {
					mapRemote.delete(mapEntity);
				}
			}
			// 删除工作内容
			// contentRemote.deleteByNO(workticketNo);
			// 删除安措明细
			safetyRemote.deleteByNO(workticketNo);
			// 删除危险点
			dangerRemote.deleteDangerByNo(workticketNo);

			// 删除基本信息
			baseRemote.delete(workticketNo);
		}
	}

	/**
	 * 通过现有票生成新票（即改变新票的信息）
	 */
	public void updateWorkticketInfoByOld(String newWorkticketNo,
			String oldWorkticketNo, String enterpriseCode, String createMan,
			String createDate) {
		// 删除新票原来的安措
		safetyRemote.deleteByNO(newWorkticketNo);
		// 删除新票原来的工作内容
		// contentRemote.deleteByNO(newWorkticketNo);
		// 删除危险点
		dangerRemote.deleteDangerByNo(newWorkticketNo);
		entityManager.flush();
		// copy工作内容
		baseRemote.copyMainTicketContent(enterpriseCode, oldWorkticketNo,
				newWorkticketNo, createMan, createDate);
		// copy安措明细
		baseRemote.copyWorkticketSafetyDetail(oldWorkticketNo, newWorkticketNo,
				createMan, createDate);
		// 复制安措内容
		// modify by fyyang 090311 安措内容不回写
		// baseRemote.copySafetyContentByOld(oldWorkticketNo, newWorkticketNo);
		// 修改新票的所属机组或系统、接收专业及工作条件
		baseRemote.updateBlockAndConditionByOld(oldWorkticketNo,
				newWorkticketNo);

		// copy 危险点分析 add by fyyang 090407
		baseRemote.copyDangerByOldTicket(oldWorkticketNo, newWorkticketNo,
				createMan);
		entityManager.flush();
		// add by fyyang 090513 工作内容回写
		// contentRemote.updateWorkticketsContent(newWorkticketNo);
	}

	/**
	 * 由标准票生成新工作票 未使用 add by fyyang 090122
	 */
	// public void createWorkticketByStandTicket(String workticketNo,
	// String enterpriseChar, String workCode) {
	// RunJWorktickets entity = baseRemote.findById(workticketNo);
	// RunJWorktickets model = baseRemote.findById(workticketNo);
	// String newWorkticketNo = baseRemote.createWorkticketNo(entity
	// .getEnterpriseCode(), entity.getWorkticketTypeCode(), entity
	// .getChargeDept(), entity.getFirelevelId());
	// model.setWorkticketNo(newWorkticketNo);
	// model.setIsStandard("N");
	// model.setIsCreatebyStand("Y");
	// // 保存基本信息
	// baseRemote.save(model);
	// // copy工作内容
	// baseRemote.copyMainTicketContent(entity.getEnterpriseCode(),
	// workticketNo, newWorkticketNo, workCode, "");
	// // copy安措明细
	// baseRemote.copyWorkticketSafetyDetail(workticketNo, newWorkticketNo,
	// workCode, "");
	// // 复制安措内容
	// // modify by fyyang 090311 安措内容不回写
	// // baseRemote.copySafetyContentByOld(workticketNo, newWorkticketNo);
	// // copy 危险点分析
	// baseRemote.copyDangerByOldTicket(workticketNo, newWorkticketNo,
	// workCode);
	// }
	// 由终结票生成新的标准工作票
	public String createStandardByEndTicket(String endWorkticketNo,
			String enterBy) {
		RunJWorktickets entity = baseRemote.findById(endWorkticketNo);
		// String newWorkticketNo = baseRemote.createWorkticketNo(entity
		// .getWorkticketNo(), entity.getWorkticketTypeCode(), "", entity
		// .getFirelevelId());
		String newWorkticketNo = baseRemote.createStandardWorkticketNo(entity
				.getWorkticketNo(), entity.getWorkticketTypeCode(), entity
				.getFirelevelId());
		String sql = "insert into  run_j_worktickets t\n"
				+ "(t.workticket_no,t.workticket_type_code,t.source_id,\n"
				+ "t.repair_specail_code,t.permission_dept,\n"
				+ "t.condition_name,t.equ_attribute_code,t.location_name,\n"
				+ "t.danger_type,t.danger_condition,t.workticket_memo,\n"
				+ "t.firelevel_id,t.enterprise_code,t.entry_by,t.entry_date,t.is_standard,t.workticket_staus_id,t.main_equ_code,t.main_equ_name,t.workticket_content)\n"
				+ "select '"
				+ newWorkticketNo
				+ "',a.workticket_type_code,a.source_id,\n"
				+ "a.repair_specail_code,a.permission_dept,\n"
				+ "a.condition_name,a.equ_attribute_code,a.location_name,\n"
				+ "a.danger_type,a.danger_condition,a.workticket_memo,\n"
				+ "a.firelevel_id,a.enterprise_code,'"
				+ enterBy
				+ "',sysdate,'Y',1,a.main_equ_code,a.main_equ_name,a.workticket_content\n"
				+ "from run_j_worktickets a\n" + "where a.workticket_no='"
				+ endWorkticketNo + "'";
		bll.exeNativeSQL(sql);
		// copy工作内容
		baseRemote.copyMainTicketContent(entity.getEnterpriseCode(),
				endWorkticketNo, newWorkticketNo, enterBy, "");
		// copy安措明细
		baseRemote.copyWorkticketSafetyDetail(endWorkticketNo, newWorkticketNo,
				enterBy, "");
		// copy危险点
		baseRemote.copyDangerByOldTicket(endWorkticketNo, newWorkticketNo,
				enterBy);
		return newWorkticketNo;
	}

	public RunJWorkticketMap findMapByWorkticketNo(String workticketNo) {
		return mapRemote.findByWorkticketNo(workticketNo);
	}

	public RunJWorkticketActors addWorkticketMember(RunJWorkticketActors entity)
			throws CodeRepeatException {
		entity = actorRemote.save(entity);
		entityManager.flush();
		baseRemote.updateWorkMemers(entity.getWorkticketNo());
		return entity;
	}

	public void deleteWorkticketMember(Long id) throws CodeRepeatException {
		RunJWorkticketActors entity = actorRemote.findById(id);
		if (entity != null) {
			actorRemote.delete(id);
			entityManager.flush();
			baseRemote.updateWorkMemers(entity.getWorkticketNo());
		}
	}

	public void deleteMultiMember(String ids) {
		String[] actorids = ids.split(",");
		RunJWorkticketActors entity = new RunJWorkticketActors();
		if (actorids.length > 0) {
			entity = actorRemote.findById(Long.parseLong(actorids[0]));
		}
		actorRemote.deleteMulti(ids);
		entityManager.flush();
		if (entity != null) {
			baseRemote.updateWorkMemers(entity.getWorkticketNo());
		}
	}

	public PageObject findAllActors(String enterpriseCode, String workticketNo,
			final int... rowStartIdxAndCount) {
		if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
			return actorRemote.findAll(enterpriseCode, workticketNo,
					rowStartIdxAndCount[0], rowStartIdxAndCount[1]);
		} else {
			return actorRemote.findAll(enterpriseCode, workticketNo);
		}
	}

	// -----------动火票作业内容------------------
	public RunJWorkticketFireContent saveWorkticketFireContent(
			RunJWorkticketFireContent entity) throws CodeRepeatException {
		return fireContentRemote.save(entity);
	}

	public void deleteWorkticketFireContent(Long id) {
		fireContentRemote.delete(id);
	}

	public void deleteMultiFireContent(String ids) {
		fireContentRemote.deleteMutil(ids);
	}

	public PageObject findFireContentList(String enterpriseCode,
			String workticketNo, final int... rowStartIdxAndCount) {
		if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 1) {
			return fireContentRemote.findAll(workticketNo, enterpriseCode,
					rowStartIdxAndCount[0], rowStartIdxAndCount[1]);

		} else {
			return fireContentRemote.findAll(workticketNo, enterpriseCode);
		}
	}

	// --------------------------------------
	// public void updateFireSafety(String workticketNo,String safetyCode)
	// {
	// //delete by fyyang 090328 现动火票修改和其他票一样
	// //safetyContentRemote.updateFireSafety(workticketNo, safetyCode);
	// }

	@SuppressWarnings("unchecked")
	public PageObject findSafetyContentList(String enterpriseCode,
			String workticketNo, String workticketTypeCode,
			final int... rowStartIdxAndCount) {
		// modify by fyyang 090311
		PageObject obj = new PageObject();
		List safetyList = new ArrayList();
		if (workticketTypeCode != null && workticketTypeCode.equals("4")) {
			RunJWorktickets baseModel = baseRemote.findById(workticketNo);
			if (baseModel != null) {
				if (baseModel.getFirelevelId() == 1) {
					workticketTypeCode = "4";
				}
				if (baseModel.getFirelevelId() == 2) {
					workticketTypeCode = "6";
				}
			}
		}
		// modify 090318

		String sql = "SELECT c.safety_code,\n" + "       c.safety_desc,\n"
				+ "       a.front_keyword || a.equ_name || "
				+ "   decode(a.attribute_code,'temp','', '(' ||\n"
				+ "       a.attribute_code || ')') || a.back_keyword ||\n"
				+ "       a.flag_desc  safety_content,\n"
				+ "       c.safety_type,\n" + "       c.order_by,\n"
				+ "       c.markcard_type_id,\n" + "       a.is_return\n"
				+ "  FROM run_j_workticket_safety a,\n"
				+ "       run_c_worktick_safety   c\n" + " WHERE \n"
				+ "    a.safety_code(+) = c.safety_code\n"
				+ "   and c.is_use = 'Y'\n" + "   and a.workticket_no(+) = '"
				+ workticketNo + "'\n" + "   and c.workticket_type_code = '"
				+ workticketTypeCode + "'\n" + "   AND a.is_use(+) = 'Y'\n"
				+ "  and c.enterprise_code='" + enterpriseCode + "'  \n"
				+ " order by c.order_by, a.safety_code, a.operation_order";

		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			WorkticketSafetyContent safety = new WorkticketSafetyContent();

			if (data[0] != null) {
				safety.setSafetyCode(data[0].toString());
			}
			if (data[1] != null) {
				safety.setSafetyDesc(data[1].toString());
			}
			if (data[2] != null) {
				if (!data[2].toString().equals("()")) {
					safety.setSafetyContent(data[2].toString());
				}

			}
			if (data[3] != null) {
				safety.setSafetyType(data[3].toString());
			}
			if (data[4] != null) {
				safety.setLine(Long.parseLong(data[4].toString()));
			}
			if (data[5] != null) {
				safety.setMarkcardTypeId(Long.parseLong(data[5].toString()));
			}
			safetyList.add(safety);
			obj.setList(safetyList);
		}
		return obj;

		// String sqlcSafety=
		// "select * from run_c_worktick_safety t\n" +
		// "where t.workticket_type_code='"+workticketTypeCode+"'\n" +
		// "and t.enterprise_code='"+enterpriseCode+"' and t.is_use='Y'\n" +
		// "order by t.order_by";
		// String sqlcCount=
		// "select count(*) from run_c_worktick_safety t\n" +
		// "where t.workticket_type_code='"+workticketTypeCode+"' and
		// t.is_use='Y'\n" +
		// "and t.enterprise_code='"+enterpriseCode+"'";
		// Long safeCount=Long.parseLong(bll.getSingal(sqlcCount).toString());
		//
		// List<RunCWorktickSafety> cList=bll.queryByNativeSQL(sqlcSafety,
		// RunCWorktickSafety.class,rowStartIdxAndCount);
		// String sqljSafety=
		// " SELECT a.safety_code,\n" +
		// " a.front_keyword || a.equ_name
		// ||'('||nvl(a.attribute_code,'temp')||')'|| a.back_keyword ||\n" +
		// " b.flag_name||chr(13) safety_content,\n" +
		// " a.is_return\n" +
		// " FROM run_j_workticket_safety a,run_c_workticket_flag b\n" +
		// "WHERE a.flag_id=b.flag_id(+) and b.is_use(+)='Y'\n" +
		// "and a.workticket_no = '"+workticketNo+"'\n" +
		// " AND a.is_use = 'Y' order by a.operation_order";
		//
		// List jList=bll.queryByNativeSQL(sqljSafety);
		// for(int i=0;i<safeCount;i++)
		// {
		// WorkticketSafetyContent safety=new WorkticketSafetyContent();
		// RunCWorktickSafety model=cList.get(i);
		// safety.setSafetyCode(model.getSafetyCode());
		// safety.setLine(model.getOrderBy());
		// safety.setSafetyDesc(model.getSafetyDesc());
		// safety.setSafetyType(model.getSafetyType());
		// safety.setMarkcardTypeId(model.getMarkcardTypeId());
		// Iterator it = jList.iterator();
		// String safetyContent="";
		// int num=1;
		// while (it.hasNext()) {
		// Object[] data = (Object[]) it.next();
		// if(data[0]!=null)
		// {
		// if(model.getSafetyCode().equals(data[0].toString()))
		// {
		// if(data[1]!=null)
		// {
		// if(safetyContent.equals(""))
		// {
		// safetyContent+="("+num+")"+data[1].toString();
		// num++;
		// }
		// else
		// {
		// safetyContent+="("+num+")"+data[1].toString();
		// num++;
		// }
		// }
		// }
		// }
		// }
		//			
		// safety.setSafetyContent(safetyContent);
		// safetyList.add(safety);
		// }
		//		
		// obj.setList(safetyList);
		// obj.setTotalCount(safeCount);
		//		
		// return obj;

	}

	@SuppressWarnings("unchecked")
	public String getSafetyContent(String enterpriseCode, String workticketNo,
			String safetyCode) {
		// modify by fyyang 090311
		String safetyContent = "";

		String sqljSafety = " SELECT a.safety_code,\n"
				+ "      a.front_keyword || a.equ_name ||'('||nvl(a.attribute_code,'temp')||')'|| a.back_keyword ||\n"
				+ "      a.flag_desc||chr(13) safety_content,\n"
				+ "      a.is_return\n" + " FROM run_j_workticket_safety a\n"
				+ "WHERE \n" + " a.workticket_no = '" + workticketNo + "'\n"
				+ "and a.safety_code='" + safetyCode + "' \n"
				+ "  AND a.is_use = 'Y'   order by a.operation_order";
		List jList = bll.queryByNativeSQL(sqljSafety);
		Iterator it = jList.iterator();
		int num = 1;
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();

			if (data[1] != null) {
				if (safetyContent.equals("")) {
					safetyContent += "(" + num + ")" + data[1].toString();
					num++;
				} else {
					safetyContent += "(" + num + ")" + data[1].toString();
					num++;
				}
			}

		}
		return safetyContent;
		// return safetyContentRemote.findSafetyContent(enterpriseCode,
		// workticketNo, safetyCode);
	}

	/**
	 * 查询工作内容列表
	 * 
	 * @param enterpriseCode
	 * @param workticketNO
	 * @return
	 */
	public PageObject findWorkticketContentList(String enterpriseCode,
			String workticketNO, final int... rowStartIdxAndCount) {
		if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 1) {
			return contentRemote.findAll(enterpriseCode, workticketNO,
					rowStartIdxAndCount[0], rowStartIdxAndCount[1]);
		} else {
			return contentRemote.findAll(enterpriseCode, workticketNO);
		}
	}

	/**
	 * 查找一条工作内容记录
	 * 
	 * @param contentId
	 * @return
	 */
	public RunJWorkticketContent findWorkticketContenById(Long contentId) {
		return contentRemote.findById(contentId);
	}

	/**
	 * 增加工作内容 在使用
	 * 
	 * @param entity
	 */

	public RunJWorkticketContent addWorkticketContent(
			RunJWorkticketContent entity) {

		entity = contentRemote.save(entity);

		// modify by fyyang 090311 工作票内容不回写
		// entityManager.flush();
		// baseRemote.updateWorkticketContent(entity.getWorkticketNo());
		return entity;
	}

	// /**
	// * 修改工作内容
	// * delete by fyyang 090328 已不用
	// * @param entity
	// */
	// //@TransactionAttribute(TransactionAttributeType.NEVER)
	// public RunJWorkticketContent
	// updateWorkticketContent(RunJWorkticketContent entity)
	// {
	// entity=contentRemote.update(entity);
	// //modify by fyyang 090311 工作票内容不回写
	// // entityManager.flush();
	// // baseRemote.updateWorkticketContent(entity.getWorkticketNo());
	// return entity;
	// }

	/**
	 * 删除工作内容 在使用
	 * 
	 * @param entity
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deleteWorkticketContent(Long contentId) {
		RunJWorkticketContent entity = contentRemote.findById(contentId);
		contentRemote.delete(entity.getId());
	}

	/**
	 * 查询安措明细列表
	 * 
	 * @param enterpriseCode
	 * @param workticketNO
	 * @param safetyCode
	 * @return
	 */
	public PageObject findSafetyDetailList(String enterpriseCode,
			String workticketNO, String safetyCode,
			final int... rowStartIdxAndCount) {
		if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 1) {
			return safetyRemote.findAll(enterpriseCode, workticketNO,
					safetyCode, rowStartIdxAndCount[0], rowStartIdxAndCount[1]);
		} else {
			return safetyRemote.findAll(enterpriseCode, workticketNO,
					safetyCode);
		}

	}

	/**
	 * 查找一条安措明细记录
	 * 
	 * @param safetyId
	 * @return
	 */
	public RunJWorkticketSafety findSafetyDetailById(Long safetyId) {
		return safetyRemote.findById(safetyId);
	}

	
	 //工单模块中创建一条工作票记录
	// add by liuyi 091116
	public RunJWorktickets createBaseWorkticket(RunJWorktickets entity,String enterpriseChar){
		entity.setWorkticketNo(baseRemote.createWorkticketNo(entity
				.getEnterpriseCode(), enterpriseChar, entity
				.getWorkticketTypeCode(), entity.getRepairSpecailCode()));
		entity = baseRemote.save(entity);
		return entity;
	}
	// /**
	// * 增加安措明细 delete by fyyang 090328 已不用
	// * @param entity
	// * @return
	// */
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// public RunJWorkticketSafety
	// addWorkticketSafetyDetail(RunJWorkticketSafety entity)
	// {
	// entity=safetyRemote.save(entity);
	// //modify by fyyang 090311 安措内容不回写
	// // entityManager.flush();
	// // baseRemote.updateSafetyContent(entity.getWorkticketNo(),
	// entity.getSafetyCode());
	// return entity;
	// }

	// /**
	// * 修改安措明细
	// * delete by fyyang 090328 已不用
	// * @param entity
	// * @return
	// */
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// public RunJWorkticketSafety
	// updateWorkticketSafetyDetail(RunJWorkticketSafety entity)
	// {
	// entity=safetyRemote.update(entity);
	// //modify by fyyang 090311 安措内容不回写
	// // entityManager.flush();
	// // baseRemote.updateSafetyContent(entity.getWorkticketNo(),
	// entity.getSafetyCode());
	// return entity;
	// }
	//	
	// /**
	// * 删除安措明细
	// * delete by fyyang 090328
	// * @param safetyid
	// */
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	// public void deleteWorkticketSafetyDetail(Long safetyid)
	// {
	// //RunJWorkticketSafety entity=safetyRemote.findById(safetyid);
	// safetyRemote.delete(safetyid);
	// //modify by fyyang 090311 安措内容不回写
	// // entityManager.flush();
	// // baseRemote.updateSafetyContent(entity.getWorkticketNo(),
	// entity.getSafetyCode());
	//		
	// }

}
