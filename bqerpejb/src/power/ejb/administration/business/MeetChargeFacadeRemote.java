/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.zip.DataFormatException;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJMeet;
import power.ejb.administration.AdJMeetMx;
import power.ejb.administration.AdJMeetfile;

/**
 * 会务费用管理接口.
 * 
 * @author wangyun
 */
@Remote
public interface MeetChargeFacadeRemote {

	/**
	 * 会务费用管理概况一览
	 * 
	 * @param strWorkCode
	 *            登陆用户ID
	 * @param argEnterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 *            分页
	 * @throws SQLException
	 */
	public PageObject getMeetChargeGeneral(String strWorkCode,
			String argEnterpriseCode, int... rowStartIdxAndCount)
			throws SQLException;

	/**
	 * 查询会务附件信息
	 * 
	 * @param meetId
	 *            会议申请单号
	 * @param rowStartIdxAndCount
	 *            分页
	 * 
	 * @throws SQLException
	 */
	public PageObject findMeetFile(String meetId) throws SQLException;

	/**
	 * 根据ID查询会议附件信息
	 * 
	 * @param id
	 *            序号
	 * @return 会议附件信息
	 * 
	 * @throws SQLException
	 */
	public PageObject findMeetFileById(Long id) throws SQLException;

	/**
	 * 费用明细数据检索
	 * 
	 * @param meetId
	 *            会议申请单号
	 * 
	 * @throws SQLException
	 */
	public PageObject findChargeDetail(String meetId) throws SQLException;

	/**
	 * 删除操作
	 * 
	 * @param entity
	 * @param meetFileUpdateTime
	 * @param strWorkCode
	 * @throws SQLException
	 * @throws DataChangeException
	 * @throws ParseException
	 */
	public void deleteMeetFile(AdJMeetfile entity, String meetFileUpdateTime,
			String strWorkCode) throws SQLException, DataChangeException, ParseException;

	/**
	 * 保存会议费用信息
	 * 
	 * @param lstUpdateADJMeet
	 * @param lstUpdateADJMeetMx
	 * @param lstSaveADJMeetMx
	 * @param lstDeleteADJMeetMx
	 * @param meetUpdateTime
	 * @throws DataChangeException
	 * @throws DataFormatException
	 * @throws SQLException
	 */
	public void saveMeet(List<AdJMeet> lstUpdateADJMeet,
			List<AdJMeetMx> lstUpdateADJMeetMx,
			List<AdJMeetMx> lstSaveADJMeetMx,
			List<AdJMeetMx> lstDeleteADJMeetMx, String meetUpdateTime)
			throws DataChangeException, DataFormatException, SQLException;

}
