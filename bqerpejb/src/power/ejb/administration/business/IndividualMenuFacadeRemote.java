/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJUserMenu;
import power.ejb.administration.AdJUserSub;

/**
 * 
 * @author zhaomingjian
 *
 */
@Remote
public interface IndividualMenuFacadeRemote {
	/**
	 * 个人订餐
	 * 
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return PageObject
	 */
	public PageObject getIndividualMenuListInfo(String strUserId,
			String enterprisecode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 
	 * @param rowStartIdxAndCount
	 *            个人子菜单画面
	 * @return
	 */
	public PageObject getIndividualSubMenuInfo(long id,String strEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;

	/**
	 * 菜谱选择画面
	 * 
	 * @param strDate
	 *            订餐信息中的订餐日期
	 * @param strType
	 *            订餐信息中的用餐时间
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getIndividualMenuChooseInfo(String strDate,
			String strType,String strEnterpriseCode, final int... rowStartIdxAndCount)
			throws SQLException;

	/**
	 * 逻辑删除个人用户点菜表信息
	 */
	public void logicDeleteIndividualMenuInfo(String userId, String mId,
			String strOldUpdateTime) throws SQLException, DataChangeException;

	/**
	 * 检查数据
	 */
	public int checkDataRepeat(String strDate, String strType, String strEnterpriseCode, String strWorkerCode)
			throws SQLException;

	/**
	 * 取得工作类型
	 * 
	 * @param userId
	 * @return
	 */
	public String getWorkType(String userId, String strEnterpriseCode)
			throws SQLException;

	/**
	 * 插入用户点菜表和用户点菜子表
	 * 
	 * @param objBean
	 *            用户点菜表实体
	 * @param lstSubList
	 *            用户点菜子表实体序列
	 * @throws SQLException
	 */
	public void addUserMenuAndSubUserMenu(AdJUserMenu objBean,
			List<AdJUserSub> lstSubList) throws SQLException;

	/**
	 * 更新用户点菜表和用户点菜子表
	 * 
	 * @param objBean
	 *            用户点菜表实体
	 * @param lstSubListAdd
	 *            用户点菜子表实体追加序列
	 * @param lstSubListUpdate
	 *            用户点菜子表实体更新序列
	 * @throws SQLException
	 */
	public void updateUserMenuAndSubUserMenu(AdJUserMenu objBean,
			List<AdJUserSub> lstSubListAdd, List<AdJUserSub> lstSubListUpdate)
			throws SQLException, DataChangeException;
}
