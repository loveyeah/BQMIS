package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;

@Remote
public interface DeptPostAssignmentMaintenFacadeRemote {

	/**
	 * 待分配岗位的岗位信息取得
	 */
	public PageObject findUnAssignMentPostList(String enterpriseCode,
			Long deptId) throws SQLException;

	/**
	 * 已分配岗位的岗位信息取得
	 * 
	 */
	public PageObject findAssignMentPostList(String enterpriseCode, Long deptId, String stationName)
			throws SQLException;

	public void updateDBOperation(
			List<HrJDepstationcorrespond> updateDepstationcorrespondList,
			List<HrJDepstationcorrespond> deleteDepstationcorrespondList,
			List<HrJDepstationcorrespond> saveDepstationcorrespondList)throws DataChangeException, SQLException,CodeRepeatException;
}
