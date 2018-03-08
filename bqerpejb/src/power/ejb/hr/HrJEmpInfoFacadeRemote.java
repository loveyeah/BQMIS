package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.form.EmployeeInfo;

/**
 * 人员基本信息管理
 *
 * @author ghzhou ; modify by  wzhyan
 */
@Remote
public interface HrJEmpInfoFacadeRemote {
	//add by wpzhu 20100622----------------start----------
	/**
	 * 查询是工会主席的员工
	 */
	 
	public PageObject  getChairmanList(String empName);
	
	/**修改一条人员记录，将其修改为是工会主席
	 * @param addList
	 */
	public void saveUnionPersideng(String modifyids);
	
	/**修改一条或者多条记录，将其修改为不是工会主席
	 * @param ids
	 */
	public void delUnionPersident(String ids);
	//-----------------end -----------------------------------
	/**
	
	 * 保存
	 * @param entity
	 */
	public void save(HrJEmpInfo entity);

	/**
	 * 删除
	 * @param entity
	 */
	public void delete(HrJEmpInfo entity);

	/**
	 * 修改
	 * @param entity
	 * @return
	 */
	public HrJEmpInfo update(HrJEmpInfo entity);
	/**
	 * 主键查找
	 * @param id
	 * @return
	 */
	public HrJEmpInfo findById(Long id);

	public List<HrJEmpInfo> findUsersByName(String empName,
			int... rowStartIdxAndCount); 
//	public List<HrJEmpInfo> findByProperty(String propertyName, Object value,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByChsName(Object chsName,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEnName(Object enName,
//			int... rowStartIdxAndCount);
//
	public List<HrJEmpInfo> findByEmpCode(Object empCode,
			int... rowStartIdxAndCount);
//
	public List<HrJEmpInfo> findByDeptId(Object deptId,
			int... rowStartIdxAndCount);

//	public List<HrJEmpInfo> findByRetrieveCode(Object retrieveCode,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByNationId(Object nationId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByNativePlaceId(Object nativePlaceId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByPoliticsId(Object politicsId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findBySex(Object sex, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByIsWedded(Object isWedded,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByArchivesId(Object archivesId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEmpStationId(Object empStationId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByStationId(Object stationId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByStationLevel(Object stationLevel,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByGradation(Object gradation,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEmpTypeId(Object empTypeId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByTechnologyTitlesTypeId(
//			Object technologyTitlesTypeId, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByTechnologyGradeId(Object technologyGradeId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByTypeOfWorkId(Object typeOfWorkId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByIdentityCard(Object identityCard,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByTimeCardId(Object timeCardId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByPayCardId(Object payCardId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findBySocialInsuranceId(Object socialInsuranceId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByMobilePhone(Object mobilePhone,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByFamilyTel(Object familyTel,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByQq(Object qq, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByMsn(Object msn, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByPostalcode(Object postalcode,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByFamilyAddress(Object familyAddress,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByOfficeTel1(Object officeTel1,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByOfficeTel2(Object officeTel2,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByFax(Object fax, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEMail(Object EMail,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByInstancyMan(Object instancyMan,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByInstancyTel(Object instancyTel,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByGraduateSchool(Object graduateSchool,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEducationId(Object educationId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByDegreeId(Object degreeId,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findBySpeciality(Object speciality,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByIsVeteran(Object isVeteran,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByIsBorrow(Object isBorrow,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByIsRetired(Object isRetired,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByEmpState(Object empState,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByOrderBy(Object orderBy,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByRecommendMan(Object recommendMan,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByAssistantManagerUnitsId(
//			Object assistantManagerUnitsId, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByOneStrongSuit(Object oneStrongSuit,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByMemo(Object memo, int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByCreateBy(Object createBy,
//			int... rowStartIdxAndCount);
//
//	public List<HrJEmpInfo> findByLastModifiyBy(Object lastModifiyBy,
//			int... rowStartIdxAndCount);

	public List<HrJEmpInfo> findAll(int... rowStartIdxAndCount);

	public PageObject queryByFuzzy(String enterpriseCode, String deptId,
			String fuzzy, int... rowStartIdxAndCount);
	public PageObject queryByDeptTypeId(String enterpriseCode, Long deptTypeId,
			String userName, int... rowStartIdxAndCount);
	public PageObject queryByDeptCode(String enterpriseCode, String deptCode, int... rowStartIdxAndCount);
	/**
	 * 模糊查询人员列表
	 * @param deptid
	 * @param fuzzy
	 * @return
	 */
	public List<HrJEmpInfo> findEmpListByDept(String fuzzy);
	/**
	 * 根据部门编码获得人员信息
	 * @param enterpriseCode
	 * @param deptCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List getListByDeptCode(String enterpriseCode,String deptCode);
	
	/**
	 * 由部门查询人员(级连查询)
	 * @param deptId 部门编码
	 * @param notInWorkerCodes 格式为 : '1440','0689'
	 * @return PageObject 其中 list为List<Employee>
	 */ 
	public PageObject getWorkersByDeptId(Long deptId, String queryKey,
			 final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 090929
	 * 取得人员详细信息
	 * @param workerCode 工号
	 * @return EmployeeInfo
	 */
	public  EmployeeInfo getEmpInfoDetail(String workerCode);
	
	// add by liuyi 090914 
	/**
	 * 
	 * @param bean
	 * @return
	 */
	public HrJEmpInfo update2(HrJEmpInfo bean)throws Exception;
	
	// add by liuyi 090914 
	public HrJEmpInfo findByEmpId(Long id, String enterpriseCode);
	
	/**
	 * add by liuyi 090915
	 * 由部门查询直属于该部门的所有人员的部门附带信息。
	 * @param deptId
	 *            部门编码
	 * @param enterpriseCode
	 *            企业编码
	 * @return PageObject 其中 list为List<Employee>.返回直属于该部门的所有人员。
	 */
	@SuppressWarnings("unchecked")
	public PageObject getEmpFollowInfoByDeptId(Long deptId, boolean isAttendanceDept, String enterpriseCode,
			final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091123
	 * 根据员工Id, 获得其基本维护信息
	 * @param argEmpId 员工Id
	 * @param argEnterpriseCode 企业编码
	 * @return 员工基本信息
	 */
//	public HrJEmpInfoBean getEmpMaintBaseInfo(Long argEmpId, String argEnterpriseCode);
	public List<HrJEmpInfoBean> getEmpMaintBaseInfo(Long argEmpId, String argEnterpriseCode);
	
	/**
	 * add by liuyi 091123
	 * modify by fyyang 20100630 加姓名查询条件，加分页
	 * @param enterpriseCode
	 * @param empState
	 * @param isUse
	 * @return
	 */
	public PageObject queryAllEmployee(String enterpriseCode,String empState,String isUse,String chsName,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091123
	 * @param empCode
	 * @param enterpriseCode
	 * @return
	 */
	public int findByEmpCode(String empCode,String enterpriseCode);
	
	/**
	 * add by liuyi 091123
	 * @param list
	 * @param insertBy
	 * @param enterpriseCode
	 * @return
	 */
	public boolean importToDatabase(List<HrJEmpInfo> list,
			String insertBy, String enterpriseCode);
	
	
	/**
	 * add by liuyi 091125
	 * 取得该部门下的员工的显示顺序
	 * @param deptId 部门Id
	 * @return PageObject
	 */
	public PageObject findEmpOrderby(Long deptId,String enterpriseCode) throws SQLException;
	/**
	 * add by liuyi 091125
	 * 更新该部门下的员工的显示顺序
	 */
//	public void updateEmpOrderBy(String workCode,List<EmpOrderbyMaitenInfo> updateOrderByList)
	public void updateEmpOrderBy(Long workerId,List<EmpOrderbyMaitenInfo> updateOrderByList)
		throws SQLException;

	/**
	 * 退休预警查询
	 * add by drdu 091223
	 * @param retirementDate
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject finRetirementDateQueryList(String retirementDate,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 091231 通过员工编号，姓名过滤人员
	 * @param fuzzy
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getEmpListByFilter(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * add by liuyi 20100603 
	 * 导入人字档案信息
	 * @param empList
	 */
	void importPersonnelFilesInfo(List<HrJEmpInfo> empList);
	
	/**
	 * add by liuyi 20100611 
	 * 通过新编码获取人员id
	 * @param newEmpCode
	 * @param enterpriseCode
	 * @return
	 */
	Long getEmpIdByNewEmpCode(String newEmpCode);
	
	/**
	 * 根据部门找该部门下所有员工信息
	 * @param deptId
	 * @param argEnterpriseCode
	 * @return
	 */
	public List<HrJEmpInfoBean> getEmpMaintBaseInfoList(Long deptId,
			String argEnterpriseCode);
	/**
	 * 根据部门导出部门的父部门
	 * @param deptId
	 * @return
	 */
	public List getEmpDeptAll(String deptId);
	/**
	 * add by kzhang 20100806
	 * 根据部门,姓名模糊查询中层干部退休预警信息
	 * @param deptId
	 * @param empName
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return	PageObject
	 */
	public PageObject finRetirementListByDeptAndName(String deptId,String empName,String enterpriseCode,final int... rowStartIdxAndCount);
 
}