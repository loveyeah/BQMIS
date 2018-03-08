package power.ejb.hr.train;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.comm.TreeNode;
import power.ejb.hr.LogUtil;
import power.ejb.hr.form.EmpInfo;
import power.ejb.hr.labor.HrCLaborMaterial;
import power.ejb.hr.train.form.OutTrainForm;
import power.ejb.run.runlog.RunCSpecials;

/**
 * Facade for entity HrCTrainSort.
 * 
 * @see power.ejb.hr.train.HrCTrainSort
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class TrainSortMaintImpl implements TrainSortMaint {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	/**
	 * @param entity
	 *            HrCTrainSort entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	/**
	 * 费用类别维护
	 */
	public void saveTrainSort(List<HrCTrainSort> addList,
			List<HrCTrainSort> updateList, String ids) {

		if (addList != null && addList.size() > 0) {
			for (HrCTrainSort entity : addList) {
				entity.setFeeSortId(bll.getMaxId("HR_C_TRAIN_SORT",
						"FEE_SORT_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrCTrainSort entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update HR_C_TRAIN_SORT a set a.is_use='N' where a.FEE_SORT_ID in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		}

	}

	public PageObject findTrainSortList(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.fee_sort_id,\n" + "t.fee_sort_name,\n"
				+ "t.order_by\n" + "from HR_C_TRAIN_SORT t\n"
				+ "where t.is_use='Y'\n" + "and t.enterprise_code='"
				+ enterpriseCode + "'\n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by t.order_by";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	/**
	 * 职工外出培训登记
	 */
	public PageObject findOutTrainList(String trainStartdate,
			String trainEnddate, String certificateStartTime,
			String certificateEndTime, String personName, String deptName,
			String certificateType, String enterpriseCode,
			int... rowStartIdxAndCount) {
		String strWhere = "";
		if (trainStartdate != null && !trainStartdate.equals("")) {
			strWhere += " and (to_char(t.train_startdate,'yyyy-mm-dd') between '"
					+ trainStartdate + "'\n";
		}
		if (trainEnddate != null && !trainEnddate.equals("")) {
			strWhere += " and '" + trainEnddate + "')\n";
		}
		if (!"".equals(certificateStartTime) && certificateStartTime != null) {//modify by wpzhu 20100714
			strWhere += " and to_char(t.certificate_start_time,'yyyy-mm-dd') >= '"
					+ certificateStartTime + "'\n";
		}
		if (!"".equals(certificateEndTime) && certificateEndTime != null) {
			strWhere += " and '" + certificateEndTime + "' >=to_char(t.certificate_end_time,'yyyy-mm-dd')\n";
		}
		if (!"".equals(personName) && personName != null) {
			strWhere += " and getworkername(t.work_code) like '%" + personName
					+ "%'";
		}
		if (!"".equals(deptName) && deptName != null) {
			strWhere += " and getdeptname(t.dept_code) like '%" + deptName
					+ "%'";
		}
		if (!"".equals(certificateType) && certificateType != null && !"0".equals(certificateType)) {
			strWhere += " and t.certificate_sort='" + certificateType + "'";

		}
		PageObject pg = new PageObject();
		String sql = "select t.train_id,\n"
				+ "t.work_code,\n"
				+ "(select a.chs_name from hr_j_emp_info a where a.emp_code=t.work_code)workerName,\n"
				+ "to_char(t.train_startdate,'yyyy-mm-dd'),\n"
				+ "to_char(t.train_enddate,'yyyy-mm-dd'),\n"
				+ "t.train_site,\n"
				+ "t.train_content,\n"
				+ "t.train_character,\n"
				+ "t.certificate_sort,\n"
				+ "t.certificate_no,\n"
				+ "t.train_total_fee,\n"
				+ "(select m.name from hr_c_certificate_type m where m.id = t.certificate_sort) as certificateName,\n"
				+" t.train_insititution,\n" 
				+" decode(t.is_received,'Y','是','N','否'),\n"
				+ "to_char(t.certificate_start_time,'yyyy-mm-dd')||'-'||to_char(t.certificate_end_time,'yyyy-mm-dd') as effectTime,\n"
				+ "(select count(1) from dual where to_char(sysdate,'yyyy-mm-dd') between to_char(t.certificate_start_time ,'yyyy-mm-dd')\n" 
				+" and   to_char( t.certificate_end_time,'yyyy-mm-dd')) as effectFlg,\n" //modify  by wpzhu 20100714
				+  "t.certificate_start_time,t.certificate_end_time\n"
				+ " from HR_J_OUTTRAIN t\n" + "where t.is_use='Y'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n";
		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
//		System.out.println("the sql"+sql);
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public HrJOuttrain saveOuttrain(HrJOuttrain entity) {
		try {
			entity.setTrainId(bll.getMaxId("HR_J_OUTTRAIN", "train_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			entityManager.flush();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
		return entity;

	}

	public HrJOuttrain updateOuttrain(HrJOuttrain entity) {
		try {
			HrJOuttrain result = entityManager.merge(entity);
			entityManager.flush();
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public HrJOuttrain findById(Long id) {
		try {
			HrJOuttrain instance = entityManager.find(HrJOuttrain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EmpInfo getEmpInfoInfo(String workerCode) {
		EmpInfo emp = null;
		String sql = "select t.emp_id,\n"
				+ "t.emp_code,\n"
				+ "t.chs_name,\n"
				+ "t.dept_id,\n"
				+ "t.sex,\n"
				+ "t.station_id,\n"
				+ "(select a.station_name from hr_c_station a where a.station_id=t.station_id)stationName,\n"
				+ "t.technology_titles_type_id,\n"
				+ "(select b.technology_titles_name from hr_c_technology_titles b where b.technology_titles_id=t.technology_titles_type_id)technologyTitlesTypeName,\n"
				+ "d.dept_code,\n" + "d.dept_name\n"
				+ "from hr_j_emp_info t, hr_c_dept d\n"
				+ "where t.dept_id = d.dept_id(+)\n" + "and t.emp_code = '"
				+ workerCode + "'\n" + "and d.is_use(+) = 'Y'"; //update by sychen 20100902
//		+ workerCode + "'\n" + "and d.is_use(+) = 'U'"; 
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			emp = new EmpInfo();
			Object[] r = (Object[]) list.get(0);
			emp.setEmpId(r[0].toString());
			emp.setEmpCode(r[1].toString());
			if (r[2] != null)
				emp.setChsName(r[2].toString());
			if (r[3] != null)
				emp.setDeptId(r[3].toString());
			if (r[4] != null)
				emp.setSex(r[4].toString());
			if (r[5] != null)
				emp.setStationId(r[5].toString());
			if (r[6] != null)
				emp.setStationName(r[6].toString());
			if (r[7] != null)
				emp.setTechnologyTitlesTypeId(r[7].toString());
			if (r[8] != null)
				emp.setTechnologyTitlesTypeName(r[8].toString());
			if (r[9] != null)
				emp.setDeptCode(r[9].toString());
			if (r[10] != null)
				emp.setDeptName(r[10].toString());
		}
		return emp;
	}

	public PageObject findStationIdList(String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.station_id,\n"
				+ "t.station_name from hr_c_station t\n"
				+ "where t.is_use='Y' and t.enterprise_code='" + enterpriseCode //update by sychen 20100902
//				+ "where t.is_use='U' and t.enterprise_code='" + enterpriseCode
				+ "'";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public PageObject findTechnologyTitlesType(int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select t.technology_titles_id,\n"
				+ "t.technology_titles_name from hr_c_technology_titles t\n"
				+ "where t.is_use='Y'"; //update by sychen 20100902
//		+ "where t.is_use='U'";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public OutTrainForm getOuttrainInfo(Long trainId, String enterpriseCode) {
		OutTrainForm dataForm = new OutTrainForm();
		String sql = "select t.train_id,\n"
				+ "t.work_code,\n"
				+ "(select a.chs_name from hr_j_emp_info a where a.emp_code=t.work_code)workerName,\n"
				+ "t.sex,\n"
				+ "t.dept_code,\n"
				+ "(select c.dept_name from hr_c_dept c where c.dept_code=t.dept_code)deptName,\n"
				+ "t.station_id,\n"
				+ "t.technology_titles_type_id,\n"
				+ "t.now_station_id,\n"
				+ "t.train_source,\n"
				+ "t.train_site,\n"
				+ "t.train_content,\n"
				+ "t.train_startdate,\n"
				+ "t.train_enddate,\n"
				+ "t.train_character,\n"
				+ "t.certificate_sort,\n"
				+ "t.certificate_no,\n"
				+ "(select m.name from hr_c_certificate_type m where m.id = t.certificate_sort) certificateName,\n"
				+ "t.certificate_start_time,\n" + "t.certificate_end_time,\n" //add by wpzhu 20100702
				+ "t.train_insititution,\n" 
				+ "t.is_received\n"
				+ " from HR_J_OUTTRAIN t\n" + "where t.is_use='Y'\n"
				+ "and t.enterprise_code='" + enterpriseCode + "'\n"
				+ "and t.train_id=" + trainId + "\n";
		List list = bll.queryByNativeSQL(sql);
		if (list != null && list.size() > 0) {
			Object[] r = (Object[]) list.get(0);
			dataForm.setTrainId(r[0].toString());
			dataForm.setWorkCode(r[1].toString());
			if (r[2] != null)
				dataForm.setWorkerName(r[2].toString());
			if (r[3] != null)
				dataForm.setSex(r[3].toString());
			if (r[4] != null)
				dataForm.setDeptCode(r[4].toString());
			if (r[5] != null)
				dataForm.setDeptName(r[5].toString());
			if (r[6] != null)
				dataForm.setStationId(r[6].toString());
			if (r[7] != null)
				dataForm.setTechnologyTitlesTypeId(r[7].toString());
			if (r[8] != null)
				dataForm.setNowStationId(r[8].toString());
			if (r[9] != null)
				dataForm.setTrainSource(r[9].toString());
			if (r[10] != null)
				dataForm.setTrainSite(r[10].toString());
			if (r[11] != null)
				dataForm.setTrainContent(r[11].toString());
			if (r[12] != null)
				dataForm.setTrainStartdate(r[12].toString());
			if (r[13] != null)
				dataForm.setTrainEnddate(r[13].toString());
			if (r[14] != null)
				dataForm.setTrainCharacter(r[14].toString());
			if (r[15] != null)
				dataForm.setCertificateSort(r[15].toString());
			if (r[16] != null)
				dataForm.setCertificateNo(r[16].toString());
			if (r[17] != null)
				dataForm.setCertificateName(r[17].toString());
			if (r[18] != null)
				dataForm.setCertificateStartTime(r[18].toString());
			if (r[19] != null)
				dataForm.setCertificateEndTime(r[19].toString());
			
			//add by wpzhu 20100702----------------------
			if (r[20] != null)
				dataForm.setTrainInsititution(r[20].toString());
			if (r[21] != null)
				dataForm.setIsReceived(r[21].toString());
		}
		return dataForm;
	}

	/**
	 * 费用明细
	 */
	public void saveOuttrainDetail(List<HrJOuttrainDetail> addList,
			List<HrJOuttrainDetail> updateList, String ids) {

		Long trainId = 0l;
		if (addList != null && addList.size() > 0) {
			trainId = addList.get(0).getTrainId();
			for (HrJOuttrainDetail entity : addList) {
				entity.setFeeId(bll.getMaxId("HR_J_OUTTRAIN_DETAIL", "fee_id"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			trainId = updateList.get(0).getTrainId();
			for (HrJOuttrainDetail entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String[] feeIds = ids.split(",");
			trainId = this.findById(Long.parseLong(feeIds[0])).getTrainId();

			String sql = "update HR_J_OUTTRAIN_DETAIL a set a.is_use='N' where a.fee_id in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);

		}
		entityManager.flush();
		String detailSql = "select nvl(sum(t.actual_fee),0)  from hr_j_outtrain_detail t where t.train_id ="
				+ trainId + "";
		Long totalFee = Long.parseLong(bll.getSingal(detailSql).toString());

		String trainSql = "update HR_J_OUTTRAIN a set a.train_total_fee= "
				+ totalFee + " where a.train_id=" + trainId + "";
		bll.exeNativeSQL(trainSql);

	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findOuttrainDetailList(Long trainId,
			String enterpriseCode) {
		String sql = "select t.fee_id,\n"
				+ "t.train_id,\n"
				+ "t.fee_sort_id,\n"
				+ "(select a.fee_sort_name from HR_C_TRAIN_SORT a where a.fee_sort_id=t.fee_sort_id)feeSortName,\n"
				+ "t.actual_fee,\n"
				+ "t.fee_dept,\n"
				+ "(select b.dept_name from hr_c_dept b where b.dept_id=t.fee_dept)deptName,\n"
				+ "t.memo\n" + " from HR_J_OUTTRAIN_DETAIL t\n"
				+ "where t.is_use='Y'\n" + "and t.enterprise_code=? \n"
				+ "and t.train_id= ? ";
		return bll.queryByNativeSQL(sql,
				new Object[] { enterpriseCode, trainId });
	}

	/**
	 * *******************************证书类别维护 bjxu
	 * 0622********************************************
	 */

	public void deleteCertificateType(HrCCertificateType entity) {
		String sql = "delete  HR_C_CERTIFICATE_TYPE t where t.id ='"
				+ entity.getId() + "' ";
		bll.exeNativeSQL(sql);
	}

	public HrCCertificateType findByCertificateTypeId(Long id) {
		try {
			HrCCertificateType instance = entityManager.find(
					HrCCertificateType.class, id);
			return instance;
		} catch (RuntimeException re) {
			throw re;
		}

	}

	public List<TreeNode> findTreeCertificateTypeList(Long pId) {
		List<TreeNode> res = null;
		try {
			String sql = "";
			sql = "select distinct t.id,\n" + "       t.name,\n"
					+ "       t.memo,\n" + "       connect_by_isleaf \n"
					+ "  from HR_C_CERTIFICATE_TYPE  t\n"
					+ "where  t.is_use='Y' and level = 1\n"
					+ " start with t.p_id = ?\n"
					+ "connect by prior t.id = t.p_id order by t.id\n";

			List<Object[]> list = bll.queryByNativeSQL(sql,
					new Object[] { pId });
			if (list != null && list.size() > 0) {
				res = new ArrayList<TreeNode>();
				for (Object[] o : list) {
					TreeNode n = new TreeNode();
					n.setId(o[0].toString());
					if (o[1] != null)
						n.setText(o[1].toString());
					if (o[2] != null)
						n.setDescription(o[2].toString());
					if (o[3] != null)
						n.setLeaf(o[3].toString().equals("1") ? true : false);
					res.add(n);
				}
			}
			return res;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void saveCertificateType(HrCCertificateType entity) {
		Long id = bll.getMaxId("HR_C_CERTIFICATE_TYPE", "ID");
		entity.setId(id);
		entity.setIsUse("Y");
		entityManager.persist(entity);
	}

	public void updateCertificateType(HrCCertificateType entity) {
		entityManager.merge(entity);
	}

	public boolean findByPid(Long pid) {
		String sql = "select count(1) from HR_C_CERTIFICATE_TYPE t where t.is_use='Y' and t.p_id ="
				+ pid + " ";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
}