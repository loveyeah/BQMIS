package power.web.administration.individualmenu.action;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJUserMenu;
import power.ejb.administration.AdJUserSub;
import power.ejb.administration.business.IndividualMenuFacadeRemote;
import power.ejb.hr.LogUtil;
import power.web.comm.AbstractAction;
import power.web.comm.CodeConstants;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 个人订单维护
 * 
 * @author zhaomingjian
 * 
 */
public class IndividualMenuAction extends AbstractAction {
	/**
	 * serialVersionUID 序列化
	 */
	private static final long serialVersionUID = 1L;
	/** 更新Flg */
	private static final String FLG_UPDATE = "0";
	/** 追加Flg */
	private static final String FLG_ADD = "1";
	/** 删除Flg */
	private static final String FLG_DEL = "2";
	/** Flg */
	private static final String FLG = "flag";
	/** 日期格式 带时分秒 */
	private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/** 日期格式 不带时分秒 */
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	/** 使用标志 使用 */
	private static final String USE_FLG_Y = "Y";
	/** 使用标志 不使用 */
	private static final String USE_FLG_N = "N";
	/**
	 * 起始查询行
	 */
	private int start = 0;
	/**
	 * 限制查询行数
	 */
	private int limit = 0;

	protected IndividualMenuFacadeRemote remote;

	/**
	 * 无参构造函数
	 */
	public IndividualMenuAction() {
		remote = (IndividualMenuFacadeRemote) factory
				.getFacadeRemote("IndividualMenuFacade");
	}

	/**
	 * 订餐信息一览
	 * 
	 * @throws JSONException
	 */
	public void getIndividualMenuListInfo() throws JSONException {
		try {
			// log 开始
			LogUtil.log("Action:订餐信息一览开始。", Level.INFO, null);
			// 取得当前用户
			String strUserId = employee.getWorkerCode();
			// add by huangweijie 20090207 start
			String enterprisecode = employee.getEnterpriseCode();
			// 取得远程对象
			PageObject pobj = (PageObject) remote.getIndividualMenuListInfo(
					strUserId, enterprisecode, start, limit);
			// add by huangweijie 20090207 end
			// 转换为字符串形式
			String strPageObject = null;
			if (pobj.getTotalCount() <= 0) {
				strPageObject = "{\"list\":[],\"totalCount\":null}";
			} else {
				strPageObject = JSONUtil.serialize(pobj);
			}
			// 返回客户端

			write(strPageObject);
			// log 结束
			LogUtil.log("Action:  订餐信息一览正常结束", Level.INFO, null);
		} catch (SQLException e) {
			LogUtil.log("Action:订餐信息一览失败。", Level.SEVERE, e);
		} catch (JSONException ee) {
			write(Constants.DATA_FAILURE);
			throw ee;
		}
	}

	/**
	 * 取得用户名
	 * 
	 * @throws JSONException
	 */
	public void getMenuUserNameInfo() {
		try {
			// 取得用户名
			String strUsername = employee.getWorkerName();
			// 取得部门名
			String strDeptname = employee.getDeptName();
			if (strDeptname == null) {
				strDeptname = "";
			}
			// 返回客户端数据
			write("{success:true,msg:'OK', user:'" + strUsername + "',dept:'"
					+ strDeptname + "'}");

		} catch (Exception e) {
			write(Constants.DATA_FAILURE);
			LogUtil.log("Action:查询用户与部门失败失败。", Level.SEVERE, e);

		}
	}

	/**
	 * 
	 * @throws JSONException
	 */
	public void getIndividualSubMenuInfo() throws JSONException {
		try {
			// log 开始
			LogUtil.log("Action:点菜一览grid信息查询开始。", Level.INFO, null);
			long mid = Long.parseLong(request.getParameter("mId").toString());
			// 取得远程对象
			PageObject pobj = (PageObject) remote.getIndividualSubMenuInfo(mid,employee.getEnterpriseCode(),
					start);
			// 转换为字符串形式
			String strPageObject = null;
			if (pobj.getTotalCount() <= 0) {
				strPageObject = "{\"list\":[],\"totalCount\":null}";
			} else {
				strPageObject = JSONUtil.serialize(pobj);
			}
			// 返回客户端

			write(strPageObject);
			// log 结束
			LogUtil.log("Action: 点菜一览grid信息查询正常结束", Level.INFO, null);
		} catch (JSONException e) {
			LogUtil.log("Action:点菜一览grid信息查询失败。", Level.SEVERE, e);
		} catch (SQLException ee) {
			LogUtil.log("Action:点菜一览grid信息查询失败。", Level.SEVERE, ee);
			write(Constants.DATA_FAILURE);
		}
	}

	/**
	 * 取得个人订餐选择画面
	 * 
	 * @throws JSONException
	 */
	public void getIndividualMenuChooseInfo() throws JSONException {
		try {
			// log 开始
			LogUtil.log("Action:弹出菜谱选择画面查询开始。", Level.INFO, null);
			// 订餐日期
			String strDate = request.getParameter("strDate");
			// 订餐类型
			String strType = request.getParameter("strType");
			// 取得远程对象
			PageObject pobj = (PageObject) remote.getIndividualMenuChooseInfo(
					strDate, strType,employee.getEnterpriseCode(), start, limit);
			// 转换为字符串形式
			String strPageObject = null;
			if (pobj.getTotalCount() <= 0) {
				strPageObject = "{\"list\":[],\"totalCount\":null}";
			} else {
				strPageObject = JSONUtil.serialize(pobj);
			}
			// 返回客户端

			write(strPageObject);
			// log 结束
			LogUtil.log("Action: 弹出菜谱选择画面查询正常结束", Level.INFO, null);
		} catch (RuntimeException e) {
			LogUtil.log("Action:弹出菜谱选择画面查询失败。", Level.SEVERE, e);
		} catch (SQLException ee) {
			LogUtil.log("Action:弹出菜谱选择画面查询失败。", Level.SEVERE, ee);
			write(Constants.DATA_FAILURE);
		}
	}

	/**
	 * 删除个人订餐信息
	 * 
	 * @throws JSONException
	 */
	public void deleteIndividualMenuInfo() throws JSONException {
		try {
			// log 开始
			LogUtil.log("Action:删除个人订餐开始。", Level.INFO, null);
			// 用户ID
			String userId = employee.getWorkerCode();
			// 订单号
			String mId = request.getParameter("id");
			// 更新时间
			String strUpdateTime = request.getParameter("updateTime");

			// 逻辑删除DB信息
			remote.logicDeleteIndividualMenuInfo(userId, mId, strUpdateTime);

			write(Constants.DELETE_SUCCESS);
			LogUtil.log("Action:  查询个人订餐正常结束", Level.INFO, null);
		} catch (SQLException ee) {
			LogUtil.log("Action:菜谱选择查询失败。", Level.SEVERE, ee);
			write(Constants.SQL_FAILURE);
		} catch (DataChangeException de) {
			LogUtil.log("Action:菜谱选择查询失败。", Level.SEVERE, de);
			write(Constants.DATA_USING);
		}
	}

	/**
	 * 检查数据合法性
	 * 
	 * @throws JSONException
	 */
	public void checkDataRepeat() throws JSONException {
		try {
			// log 开始
			LogUtil.log("Action:检查个人订餐数据重复性开始。", Level.INFO, null);

			// 订餐日期
			String strDate = request.getParameter("strDate");
			// 菜谱类型
			String strType = request.getParameter("strType");

			// DB信息
			int intNum = remote.checkDataRepeat(strDate, strType, employee.getEnterpriseCode(), employee.getWorkerCode());
			if (intNum <= 0) {
				write(Constants.ADD_SUCCESS);
			} else {
				write(Constants.DATA_USING);
			}
			// log 结束
			LogUtil.log("Action:  检查个人订餐数据重复性正常结束", Level.INFO, null);
		} catch (SQLException ee) {
			LogUtil.log("Action:菜谱选择查询失败。", Level.SEVERE, ee);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 插入用户菜谱表
	 * 
	 * @throws JSONException
	 */

	@SuppressWarnings("unchecked")
	public void saveUserMenuAndUserSubInfo() throws JSONException {
		try {
			UserTransaction tx = (UserTransaction) new InitialContext()
					.lookup("java:comp/UserTransaction");
			try {
				// 取得用户编码
				String strUserId = employee.getWorkerCode();
				String strEnterpriseCode = employee.getEnterpriseCode();
				// 填单时间
				String insertDate = request.getParameter("insertDate");
				String strFlag = request.getParameter("strFlag");
				String strPlace = request.getParameter("strPlace");
				String strDate = request.getParameter("strDate");
				String strType = request.getParameter("strType");
				String strUpdate = request.getParameter("strUpdate");
				String strMethod = request.getParameter("strMethod");
				String strMId = request.getParameter("strMId");
				String strUpdateTime = request.getParameter("strUpdateTime");
				// 转换前台批量数据为对象形式
				Object objUpdate = JSONUtil.deserialize(strUpdate);
				List lstObj = (List) objUpdate;
				// 重新组建客户端的数据 ，分别组建为更新用户点菜子表数据和插入用户点菜子表数据
				// 用于点菜表
				AdJUserMenu objUserMenu = new AdJUserMenu();
				// 订单号
				if ((strMId != null) && (!"".equals(strMId))) {
					objUserMenu.setMId(Long.parseLong(strMId));
				}
				// 填单日期
				SimpleDateFormat sdfFrom = new SimpleDateFormat(DATE_FORMAT);
				SimpleDateFormat sdfFromTime = new SimpleDateFormat(TIME_FORMAT);
				Date dteInsertDate = sdfFrom.parse(insertDate);
				objUserMenu.setInsertdate(dteInsertDate);
				// 订餐类别
				objUserMenu.setMenuType(strType);
				// 订餐人
				objUserMenu.setInsertby(strUserId);
				// 订餐日期
				Date dteWriteDate = sdfFrom.parse(strDate);
				objUserMenu.setMenuDate(dteWriteDate);
				// 修改人
				objUserMenu.setUpdateUser(strUserId);
				// 是否使用
				objUserMenu.setIsUse(USE_FLG_Y);
				// 企业代码
				objUserMenu.setEnterpriseCode(strEnterpriseCode);
				// 用餐地点
				objUserMenu.setPlace(strPlace);
				// 更新时间
				if ((strUpdateTime != null)
						&& (!"".equals(strUpdateTime.trim()))) {
					Date dteupdateTime = sdfFromTime.parse(strUpdateTime);
					objUserMenu.setUpdateTime(dteupdateTime);
				}
				// 订单状态
				String strMenuInfo = "";
				// 在发送的情况下
				if ((strFlag != null) && strFlag.equals("send")) {
					// 取得工作类别
					String strWorkType = remote.getWorkType(strUserId,
							strEnterpriseCode).toString();
					if (CodeConstants.MAN_TYPE_1.equals(strWorkType)) {
						// b）上面1检索的岗位编码.工作类别为'1'(常白班)时，保存数据时把订单状态设为3（已发送）
						strMenuInfo = CodeConstants.ORDER_STATUS_3;
					} else if (CodeConstants.MAN_TYPE_2.equals(strWorkType)) {
						// a）上面1检索的岗位编码.工作类别为'2'(运行班)时，保存数据时把订单状态设为2（审核）
						strMenuInfo = CodeConstants.ORDER_STATUS_2;
					}
					// 非发送情况和其他情况
				} else {
					strMenuInfo = CodeConstants.ORDER_STATUS_1;
				}
				// 订单状态
				objUserMenu.setMenuInfo(strMenuInfo);

				// 插入list
				List<AdJUserSub> lstInsert = new ArrayList<AdJUserSub>();
				// 更新list
				List<AdJUserSub> lstUpdate = new ArrayList<AdJUserSub>();
				// 整合数据
				getList(lstObj, lstInsert, lstUpdate, strUserId);

				// 事务处理起点
				tx.begin();

				// 如果是新增的，则进行如下操作
				if (strMethod.equals("add")) {
					remote.addUserMenuAndSubUserMenu(objUserMenu, lstInsert);
					// 如果是更新则进行如下操作
				} else if (strMethod.equals("update")) {
					remote.updateUserMenuAndSubUserMenu(objUserMenu, lstInsert,
							lstUpdate);
				}
				tx.commit();
				LogUtil.log("Action:登记个人订餐数据正常结束。", Level.INFO, null);
				write(Constants.ADD_SUCCESS);
			} catch (DataChangeException e) {
				LogUtil.log("Action：登记个人订餐数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_USING);
			} catch (SQLException e) {
				LogUtil.log("Action：登记个人订餐数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.SQL_FAILURE);
			} catch (JSONException e) {
				LogUtil.log("Action：登记个人订餐数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_FAILURE);
			} catch (ParseException e) {
				LogUtil.log("Action：登记个人订餐数据异常结束", Level.SEVERE, e);
				tx.rollback();
				write(Constants.DATA_FAILURE);
			}
		} catch (Exception e) {
			LogUtil.log("Action：登记个人订餐数据异常结束", Level.SEVERE, e);
			write(Constants.SQL_FAILURE);
		}
	}

	/**
	 * 整合数据
	 * 
	 * @param lstSrc
	 *            数据源序列
	 * @param lstAdd
	 *            追加数据序列
	 * @param lstUpdate
	 *            更新数据序列
	 * @param strUserId
	 *            登陆用户ID
	 */
	@SuppressWarnings("unchecked")
	private void getList(List lstSrc, List<AdJUserSub> lstAdd,
			List<AdJUserSub> lstUpdate, String strUserId) throws ParseException {
		for (int i = 0; i < lstSrc.size(); i++) {
			// 转化list为Map
			Map mapSrc = (Map) lstSrc.get(i);
			// 定义list类型
			AdJUserSub objBean = getBean(mapSrc);
			objBean.setUpdateUser(strUserId);
			// 更新标志
			String strFlg = mapSrc.get(FLG).toString();
			if (FLG_UPDATE.equals(strFlg)) {
				objBean.setIsUse(USE_FLG_Y);
				lstUpdate.add(objBean);
			} else if (FLG_ADD.equals(strFlg)) {
				objBean.setIsUse(USE_FLG_Y);
				lstAdd.add(objBean);
			} else if (FLG_DEL.equals(strFlg)) {
				objBean.setIsUse(USE_FLG_N);
				lstUpdate.add(objBean);
			}
		}

	}

	/**
	 * 数据转换，从Map植入AdJUserSub
	 * 
	 * @param mapSrc
	 *            数据源
	 * @return AdJUserSub实体
	 */
	@SuppressWarnings("unchecked")
	private AdJUserSub getBean(Map mapSrc) throws ParseException {
		AdJUserSub objBean = new AdJUserSub();
		// 取得菜谱编码
		if (mapSrc.get("menuCode") != null) {
			objBean.setMenuCode(mapSrc.get("menuCode").toString());
		}

		// 取得订餐号
		if ((mapSrc.get("MId") != null) && (!"".equals(mapSrc.get("MId")))) {
			objBean.setMId(Long.parseLong(mapSrc.get("MId").toString()));
		}

		// 份数
		if ((mapSrc.get("menuAmount") != null)
				&& (!"".equals(mapSrc.get("menuAmount")))) {
			objBean.setMenuAmount(Long.parseLong((mapSrc.get("menuAmount")
					.toString())));
		}

		// 单价
		if ((mapSrc.get("menuPrice") != null)
				&& (!"".equals(mapSrc.get("menuAmount")))) {
			objBean.setMenuPrice(Double.parseDouble((mapSrc.get("menuPrice")
					.toString())));
		}

		// 总计
		if ((mapSrc.get("menuTotal") != null)
				&& (!"".equals(mapSrc.get("menuAmount")))) {
			objBean.setMenuTotal(Double.parseDouble((mapSrc.get("menuTotal")
					.toString())));
		}

		// 注释
		if (mapSrc.get("memo") != null) {
			objBean.setMemo((mapSrc.get("memo").toString()));
		}

		// 用户点菜子表Id
		if ((mapSrc.get("id") != null)
				&& (!"".equals(mapSrc.get("menuAmount")))) {
			objBean.setId(Long.parseLong((mapSrc.get("id").toString())));
		}

		// 更新时间
		if ((mapSrc.get("strUpdateTime") != null)
				&& (mapSrc.get("strUpdateTime").toString().trim().length() > 1)) {
			SimpleDateFormat sdfFrom = new SimpleDateFormat(TIME_FORMAT);
			Date dteupdateTime = sdfFrom.parse(mapSrc.get("strUpdateTime")
					.toString());
			objBean.setUpdateTime(dteupdateTime);
		}
		return objBean;
	}

	/**
	 * 
	 * @return start 开始查询行
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 
	 * @param start
	 *            开始查询行
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 
	 * @return 限制查询行数
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 *            限制查询行数
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
