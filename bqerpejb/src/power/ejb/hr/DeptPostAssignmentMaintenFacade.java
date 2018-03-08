package power.ejb.hr;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceContext;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class DeptPostAssignmentMaintenFacade implements
		DeptPostAssignmentMaintenFacadeRemote {
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	@PersistenceContext
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	@EJB(beanName = "HrJDepstationcorrespondFacade")
	protected HrJDepstationcorrespondFacadeRemote stationRemote;
	
	/**
	 * 待分配岗位的岗位信息取得
	 */
	@SuppressWarnings("unchecked")
	public PageObject findUnAssignMentPostList(String enterpriseCode,Long deptId) throws SQLException{
		try {
			// EJB log 开始
			StringBuilder strSql = new StringBuilder();
			strSql.append("SELECT A.STATION_ID,");
			strSql.append("A.STATION_CODE,");
			strSql.append("A.STATION_NAME ");
			strSql.append("FROM HR_C_STATION A ");
			strSql.append("WHERE A.IS_USE=? AND A.ENTERPRISE_CODE=? ");
			strSql.append("AND A.STATION_ID NOT IN(");
			strSql.append("SELECT B.STATION_ID FROM HR_J_DEPSTATIONCORRESPOND B WHERE B.DEPT_ID=? ");
			strSql.append("AND B.IS_USE=? AND B.ENTERPRISE_CODE=?) ");
			strSql.append("ORDER BY A.STATION_ID");
			LogUtil.log("EJB:查找待分配岗位的岗位信息开始。SQL=" + strSql.toString(), Level.INFO, null);
			List<Object> list = bll.queryByNativeSQL(strSql.toString(), new Object[] {"Y", enterpriseCode,deptId,YES,enterpriseCode});//update by sychen 20100901
//			List<Object> list = bll.queryByNativeSQL(strSql.toString(), new Object[] {"U", enterpriseCode,deptId,YES,enterpriseCode});
			PageObject obj = new PageObject();
			List<DeptPostAssignmentMaintenInfo> arraylist = new ArrayList<DeptPostAssignmentMaintenInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptPostAssignmentMaintenInfo model = new DeptPostAssignmentMaintenInfo();
				if(data[0]!=null){
					// 岗位Id
					model.setStationId(Long.parseLong(data[0].toString()));
				}
				if(data[1]!=null){
					// 岗位编码
					model.setStationCode(data[1].toString());
				}
				if(data[2]!=null){
					// 岗位名称
					model.setStationName((data[2].toString()));
				}
				arraylist.add(model);
			}
			obj.setList(arraylist);
			LogUtil.log("EJB:查找待分配岗位的岗位信息结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找待分配岗位的岗位信息失败。", Level.SEVERE, e);
			throw new SQLException();
		} 
	}
	
	//modify by ming_lian 2010-08-12
	/**
	 * 已分配岗位的岗位信息取得
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAssignMentPostList(String enterpriseCode,Long deptId,String stationName)throws SQLException{
		
		try {
			// EJB log 开始
			StringBuilder strSql = new StringBuilder();
			strSql.append("SELECT A.DEPSTATIONCORRESPONDID,");
			strSql.append("A.DEPT_ID,");
			strSql.append("A.STANDARD_PERSON_NUM,");
			strSql.append("TO_CHAR(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')UPDATE_TIME,");
			strSql.append("A.IS_LEAD,");
			strSql.append("A.STATION_ID,");
			strSql.append("B.STATION_CODE,");
			strSql.append("B.STATION_NAME ");
			strSql.append("FROM HR_J_DEPSTATIONCORRESPOND A,");
			strSql.append("HR_C_STATION B WHERE ");
			strSql.append("A.IS_USE=? AND B.IS_USE=? AND ");
			strSql.append("A.ENTERPRISE_CODE =? AND B.ENTERPRISE_CODE =? AND ");
			strSql.append("A.STATION_ID = B.STATION_ID AND A.DEPT_ID =? ");
			strSql.append(" AND B.STATION_NAME like '%"+stationName+"%' ");
			strSql.append(" ORDER BY A.DEPSTATIONCORRESPONDID");
			System.out.println(strSql.toString());
			//LogUtil.log("EJB:查找已分配岗位的岗位信息开始。SQL=" + strSql.toString(), Level.INFO, null);
			List<Object> list = bll.queryByNativeSQL(strSql.toString(), new Object[] {YES,"Y", enterpriseCode,enterpriseCode,deptId});//update by sychen 20100901
//			List<Object> list = bll.queryByNativeSQL(strSql.toString(), new Object[] {YES,"U", enterpriseCode,enterpriseCode,deptId});
			PageObject obj = new PageObject();
			List<DeptPostAssignmentMaintenInfo> arraylist = new ArrayList<DeptPostAssignmentMaintenInfo>(
					list.size());
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				DeptPostAssignmentMaintenInfo model = new DeptPostAssignmentMaintenInfo();
				if(data[0]!=null){
					// 部门岗位ID
					model.setDepstationcorrespondid(Long.parseLong(data[0].toString()));
				}
				if(data[1]!=null){
					// 部门ID
					model.setDeptId(Long.parseLong(data[1].toString()));
				}
				if(data[2]!=null){
					// 标准人数
					model.setStandardPersonNum(Long.parseLong(data[2].toString()));
				}
				if(data[3]!=null){
					// 修改时间
					model.setLastModifiedDate(data[3].toString());
				}
				if(data[4]!=null){
					// 是否领导岗位
					model.setIsLead(data[4].toString());
				}
				if(data[5]!=null){
					// 岗位ID
					model.setStationId(Long.parseLong(data[5].toString()));
				}
				if(data[6]!=null){
					// 岗位编码
					model.setStationCode((data[6].toString()));
				}
				if(data[7]!=null){
					// 岗位名称
					model.setStationName((data[7].toString()));
				}
				// 从DB检索出来，未修改状态
				model.setFlag("0");
				arraylist.add(model);
			}
			obj.setList(arraylist);
			LogUtil.log("EJB:查找已分配岗位的岗位信息结束。", Level.INFO, null);
			return obj;
		} catch (Exception e) {
			LogUtil.log("EJB:查找已分配岗位的岗位信息失败。", Level.SEVERE, e);
			throw new SQLException();
		} 
		
	}
	
	public void updateDBOperation(
			List<HrJDepstationcorrespond> updateDepstationcorrespondList,
			List<HrJDepstationcorrespond> deleteDepstationcorrespondList,
			List<HrJDepstationcorrespond> saveDepstationcorrespondList) throws DataChangeException, SQLException,CodeRepeatException{
		try{
			LogUtil.log("部门岗位对应表排他开始。", Level.INFO, null);
			Long id;
			if(updateDepstationcorrespondList.size()>0){
				for (int i = 0; i < updateDepstationcorrespondList.size(); i++) {
					HrJDepstationcorrespond bean = updateDepstationcorrespondList.get(i);
					id = bean.getDepstationcorrespondid();
					HrJDepstationcorrespond entity = stationRemote.findById(id);
					if (entity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(bean.getLastModifiedDate())
								.equals(
										DateToString(entity
												.getLastModifiedDate()))) {
							throw new DataChangeException(null);
						}

					}
				}
			}
			if(deleteDepstationcorrespondList.size()>0){
				for (int i = 0; i < deleteDepstationcorrespondList.size(); i++) {
					HrJDepstationcorrespond bean = deleteDepstationcorrespondList.get(i);
					id = bean.getDepstationcorrespondid();
					HrJDepstationcorrespond entity = stationRemote.findById(id);
					if (entity == null) {
						throw new DataChangeException(null);
					} else {
						if (!DateToString(bean.getLastModifiedDate())
								.equals(
										DateToString(entity
												.getLastModifiedDate()))) {
							throw new DataChangeException(null);
						}

					}
				}
			}
			LogUtil.log("部门岗位对应表排他结束。", Level.INFO, null);
			LogUtil.log("部门岗位对应表更新开始。", Level.INFO, null);
			for (int i = 0; i < updateDepstationcorrespondList.size(); i++) {
				HrJDepstationcorrespond bean = updateDepstationcorrespondList.get(i);
				HrJDepstationcorrespond entity = stationRemote.findById(bean.getDepstationcorrespondid());
				entity.setStandardPersonNum(bean.getStandardPersonNum());
				entity.setIsLead(bean.getIsLead());
				entity.setLastModifiedBy(bean.getLastModifiedBy());
				entity.setLastModifiedDate(new Date());
				stationRemote.update(entity);
			}
			LogUtil.log("部门岗位对应表更新结束。", Level.INFO, null);
			LogUtil.log("部门岗位对应表删除开始。", Level.INFO, null);
			for (int i = 0; i < deleteDepstationcorrespondList.size(); i++) {
				HrJDepstationcorrespond bean = deleteDepstationcorrespondList.get(i);
				HrJDepstationcorrespond entity = stationRemote.findById(bean.getDepstationcorrespondid());
				entity.setIsUse(NO);
				entity.setLastModifiedBy(bean.getLastModifiedBy());
				entity.setLastModifiedDate(new Date());
				stationRemote.update(entity);
			}
			LogUtil.log("部门岗位对应表删除结束。", Level.INFO, null);
			
			for (int i = 0; i < saveDepstationcorrespondList.size(); i++) {
				HrJDepstationcorrespond bean = saveDepstationcorrespondList.get(i);
				if(findStationIdIsExist(bean.getDeptId(), bean.getStationId(), bean.getEnterpriseCode())){
					throw new CodeRepeatException(null);
				}
			}
			
			LogUtil.log("部门岗位对应表插入开始。", Level.INFO, null);
			id =bll.getMaxId("HR_J_DEPSTATIONCORRESPOND", "DEPSTATIONCORRESPONDID");
			for (int i = 0; i < saveDepstationcorrespondList.size(); i++) {
				HrJDepstationcorrespond bean = saveDepstationcorrespondList.get(i);
				bean.setDepstationcorrespondid(id);
				bean.setLastModifiedDate(new Date());
				stationRemote.save(bean);
				id++;
			}
			LogUtil.log("部门岗位对应表插入结束。", Level.INFO, null);
			
		}catch(DataChangeException e){
        	throw e;
		}catch(CodeRepeatException e){
        	throw e;
		} catch (Exception e) {
			LogUtil.log("部门岗位对应表操作DB失败。", Level.INFO, null);
			throw new SQLException();
		}
	}
	/**
	 * 
	 * @param date
	 * @return
	 */
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				DATE_FORMAT);
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}
	
	@SuppressWarnings("unchecked")
	private boolean findStationIdIsExist(Long deptId,Long stationId,String enterpriseCode){
		String strSql = "SELECT A.* FROM HR_J_DEPSTATIONCORRESPOND A " +
				"WHERE A.DEPT_ID =? AND A.STATION_ID=? AND A.IS_USE =? AND A.ENTERPRISE_CODE=?";
		List<HrJDepstationcorrespond> list = bll.queryByNativeSQL(strSql,new Object[] {deptId,stationId,YES, enterpriseCode},HrJDepstationcorrespond.class);
		if(list.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
}
