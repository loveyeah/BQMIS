/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.MenuSystemInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 订餐信息查询
 * @author zhaomingjian
 *
 */
@Stateless
public class MenuSystemFacade implements MenuSystemFacadeRemote {
	@EJB(beanName = "")
	protected NativeSqlHelperRemote bll;

	/**
	 * 订餐信息查询
	 * @param strDate 查询时间	
	 * @param strManType 人员类别
	 * @param strMenuType 用餐类别
     * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMenuSystemInfo(String strDate, String strManType,
			String strMenuType,String strEnterpriseCode) throws SQLException {

		//EJB log 开始
		LogUtil.log("EJB:订餐信息查询开始", Level.INFO, null);

		try {
			//实例返回的对象
			PageObject result = new PageObject();
			//动态的list
			List lstParams = new ArrayList();
			// xsTan 修改开始 2009-1-23
			//查询sql文
			String strSql = "SELECT "
					+ "  A.M_ID, "
					+ "  NVL(E.CHS_NAME,A.INSERTBY), "
					+ "  F.DEPT_NAME, "
					+ "  A.INSERTDATE, "
					+ "  A.PLACE, "
					+ "  C.MENU_NAME, "
					+ "  D.MENUTYPE_NAME, "
					+ "  B.MENU_AMOUNT, "
					+ "  B.MENU_PRICE, "
					+ "  SUM.SUM,"
					//备注
					+ "  B.MEMO"
					+ " FROM "
					//用户点菜表
					+ "  AD_J_USER_MENU A "
					// 用户点菜子表
					+ " LEFT JOIN AD_J_USER_SUB B "
					+ " ON A.M_ID = B.M_ID AND  B.IS_USE = 'Y' "
					// 菜谱维护表
					+ " LEFT JOIN AD_C_MENU_WH C "
					+ " ON B.MENU_CODE = C.MENU_CODE "
					//菜谱类别表
					+ " LEFT JOIN AD_C_MENU_TYPE D "
					+ " ON C.MENUTYPE_CODE=D.MENUTYPE_CODE "
					//人员基本信息表
					+ " LEFT JOIN HR_J_EMP_INFO E "
					+ " ON A.INSERTBY = E.EMP_CODE "
					//部门编码表 
					+ " LEFT JOIN HR_C_DEPT F "
					+ " ON E.DEPT_ID=F.DEPT_ID "
					// 子查询 检索合计
					+ ", (SELECT MAIN.M_ID, SUM(MENU_TOTAL) AS SUM "
					+ "   FROM AD_J_USER_MENU MAIN, AD_J_USER_SUB SUB";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				strSql = strSql + " ,HR_J_EMP_INFO EMP , HR_C_STATION STATION ";
			}
			strSql = strSql + "   WHERE MAIN.IS_USE = 'Y' AND "
			        + "     MAIN.ENTERPRISE_CODE = ?  AND "
					+ "     SUB.IS_USE = 'Y' AND "
					+ "     TO_CHAR(MAIN.MENU_DATE,'YYYY-MM-DD') = ? AND "
					+ "     MAIN.MENU_TYPE = ? AND "
					+ "     MAIN.M_ID = SUB.M_ID ";
			//添加5子表检索条件
			lstParams.add(strEnterpriseCode);

			//添加日期
			lstParams.add(strDate);
			//添加菜谱类型
			lstParams.add(strMenuType);
			
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				//添加2子表检索条件
				lstParams.add(strEnterpriseCode);
				lstParams.add(strEnterpriseCode);
				//由人员类别得到工作类型
				lstParams.add(strManType);
				strSql += "    AND MAIN.INSERTBY = EMP.EMP_CODE "
				          + "      AND EMP.ENTERPRISE_CODE = ? "    
						  + "      AND EMP.STATION_ID = STATION.STATION_ID "
						  + "      AND STATION.ENTERPRISE_CODE = ? "
						  + "      AND STATION.WORK_KIND = ? ";
			}
			//合计
			strSql += "      GROUP BY MAIN.M_ID "
					+ "      ) SUM ";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				strSql = strSql + " , HR_C_STATION S ";
			}
			// 订单状态
			lstParams.add("3");
			//添加1子表检索条件
			lstParams.add(strEnterpriseCode);
			//添加日期
			lstParams.add(strDate);
			//添加订餐类型
			lstParams.add(strMenuType);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			strSql = strSql + "   WHERE " 
				   + "     A.IS_USE='Y' "
				   + "     AND A.MENU_INFO = ?  "
				   + "     AND A.ENTERPRISE_CODE = ?  "
				   + "     AND A.M_ID = SUM.M_ID "
				   + "     AND TO_CHAR(A.MENU_DATE,'YYYY-MM-DD') = ? "
				   + "     AND A.MENU_TYPE = ? "
					+ " AND C.ENTERPRISE_CODE = ? "
					+ " AND D.ENTERPRISE_CODE = ? "
					+ " AND E.ENTERPRISE_CODE = ? "
					+ " AND F.ENTERPRISE_CODE = ? ";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				//添加2子表检索条件
				lstParams.add(strEnterpriseCode);
				lstParams.add(strEnterpriseCode);
				//添加工作类型
				lstParams.add(strManType);
				strSql = strSql + "  AND A.INSERTBY = E.EMP_CODE "
				        + "  AND A.ENTERPRISE_CODE = ? "
						+ "  AND E.STATION_ID = S.STATION_ID "
						+ "  AND S.ENTERPRISE_CODE = ? "
						+ "  AND S.WORK_KIND = ? ";
			}
			strSql += " ORDER BY A.M_ID ";

			//log
			LogUtil.log("EJB: SQL=" + strSql, Level.INFO, null);
			
			
			
			Object[] params = lstParams.toArray();
			//获得记录
			List lstRecord = bll.queryByNativeSQL(strSql, params);

			List<MenuSystemInfo> arrList = new ArrayList<MenuSystemInfo>();
			Iterator it = lstRecord.iterator();

			String strTemp = "";
			String strID = "";
			//对数据记录进行循环得到
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				MenuSystemInfo info = new MenuSystemInfo();
				if (data[0] != null) {
					strID = data[0].toString();
				}
				// 订单号不同时显示
				if (!strTemp.equals(strID)) {
					strTemp = strID;
					// 订单号
					if (data[0] != null)
						info.setMId(strID);
					// 订餐人
					if (data[1] != null)
						info.setName(data[1].toString());
					// 所属部门
					if (data[2] != null)
						info.setDeptName(data[2].toString());
					// 填单日期
					if (data[3] != null)
						info.setInsertDate(data[3].toString());
					// 就餐地点
					if (data[4] != null)
							if(data[4].equals("1")){
		                      info.setPlace("餐厅");	
							}else if(data[4].equals( "2")){
							  info.setPlace("工作地");	
							}else{
								info.setPlace("");
							}
					// 合计
					if (data[9] != null) {
						info.setMenuTotal(data[9].toString());
					}
				}
				// 子表始终显示
				// 菜谱名称
				if (data[5] != null)
					info.setMenuName(data[5].toString());
				// 菜谱类别
				if (data[6] != null)
					info.setMenuTypeName(data[6].toString());
				// 份数
				if (data[7] != null)
					info.setMenuAmount(data[7].toString());
				// 单价
				if (data[8] != null)
					info.setMenuPrice(data[8].toString());
				// 备注
				if (data[10] != null)
					info.setMemo(data[10].toString());
				// 合计行设置0
				if (info.getMenuTotal() == null) {
					info.setMenuTotal("0");
				}

				arrList.add(info);
			}
			
			result.setList(arrList);
			result.setTotalCount(Long.parseLong(String.valueOf(arrList.size())));

		    // xsTan 修改结束 2009-1-23
			LogUtil.log("EJB: 订餐信息查询正常结束。", Level.INFO, null);

			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:  订餐信息查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

	/**
	 * 订餐信息查询
	 * @author daichunlin
	 * @param strDate 查询时间	
	 * @param strManType 人员类别
	 * @param strMenuType 用餐类别
     * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMenuSystemInfoZp(String enterpriseCode,String strDate, String strManType,
			String strMenuType) throws SQLException {

		//EJB log 开始
		LogUtil.log("EJB:订餐信息查询开始", Level.INFO, null);
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		try {
			//实例返回的对象
			PageObject result = new PageObject();
			//动态的list
			List lstParams = new ArrayList();			
			//查询sql文
			String strSql = "SELECT "
					+ "  A.M_ID, "
					+ "  NVL(E.CHS_NAME,A.INSERTBY), "
					+ "  F.DEPT_NAME, "
					+ "  A.INSERTDATE, "
					+ "  A.PLACE, "
					+ "  C.MENU_NAME, "
					+ "  D.MENUTYPE_NAME, "
					+ "  B.MENU_AMOUNT, "
					+ "  B.MENU_PRICE, "
					+ "  B.MENU_TOTAL,"
					//备注
					+ "  B.MEMO"
					+ " FROM "
					//用户点菜表
					+ "  AD_J_USER_MENU A "
					// 用户点菜子表
					+ " LEFT JOIN AD_J_USER_SUB B "
					+ " ON A.M_ID = B.M_ID"
					// 菜谱维护表
					+ " LEFT JOIN AD_C_MENU_WH C "
					+ " ON B.MENU_CODE = C.MENU_CODE AND C.IS_USE = 'Y' "
					//菜谱类别表
					+ " LEFT JOIN AD_C_MENU_TYPE D "
					+ " ON C.MENUTYPE_CODE=D.MENUTYPE_CODE AND D.IS_USE = 'Y' "
					//人员基本信息表
					+ " LEFT JOIN HR_J_EMP_INFO E "
					+ " ON A.INSERTBY = E.EMP_CODE "
					//部门编码表 
					+ " LEFT JOIN HR_C_DEPT F "
					+ " ON E.DEPT_ID=F.DEPT_ID ";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				strSql = strSql + " , HR_C_STATION S ";
			}
			//添加日期
			lstParams.add(strDate);
			//添加订餐类型
			lstParams.add(strMenuType);
			strSql = strSql + "   WHERE " 
			       + "     A.IS_USE = 'Y'  "
				   + "     AND A.MENU_INFO = '3'  "			       
			       + "     AND A.ENTERPRISE_CODE = '"
			       + enterpriseCode
				   + "'    AND B.IS_USE='Y' "
				   + "     AND C.ENTERPRISE_CODE = '"
			       + enterpriseCode
			       + "'     AND D.ENTERPRISE_CODE = '"
			       + enterpriseCode
			       + "'     AND E.ENTERPRISE_CODE = '"
			       + enterpriseCode
			       + "'     AND F.ENTERPRISE_CODE = '"
			       + enterpriseCode
				   + "'    AND E.IS_USE = 'Y' "
				   + "     AND TO_CHAR(A.MENU_DATE,'YYYY-MM-DD') = ? "
				   + "     AND A.MENU_TYPE = ? ";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				//添加工作类型
				lstParams.add(strManType);
				strSql = strSql + "  AND A.INSERTBY = E.EMP_CODE "
						+ "  AND E.STATION_ID = S.STATION_ID "
						+ "  AND S.WORK_KIND = ? ";
			}
			strSql += " ORDER BY A.M_ID ";

			//log
			LogUtil.log("EJB: SQL=" + strSql, Level.INFO, null);
			
			
			
			Object[] params = lstParams.toArray();
			//获得记录
			List lstRecord = bll.queryByNativeSQL(strSql, params);

			List<MenuSystemInfo> arrList = new ArrayList<MenuSystemInfo>();
			Iterator it = lstRecord.iterator();

			String strTemp = "";
			String strID = "";
			//对数据记录进行循环得到
			while (it.hasNext()) {
				Object[] data = (Object[]) it.next();
				MenuSystemInfo info = new MenuSystemInfo();

				// 订单号
				if (data[0] != null)
					info.setMId(strID);
				// 订餐人
				if (data[1] != null)
					info.setName(data[1].toString());
				// 所属部门
				if (data[2] != null)
					info.setDeptName(data[2].toString());
				// 填单日期
				if (data[3] != null)
					info.setInsertDate(data[3].toString());
				// 就餐地点
				if (data[4] != null)
					if (data[4].equals("1")) {
						info.setPlace("餐厅");
					} else if (data[4].equals("2")) {
						info.setPlace("工作地");
					} else {
						info.setPlace("");
					}
				// 合计
				if (data[9] != null) {
					info.setMenuTotal(dfNumber.format(data[9]));
				}

				// 子表始终显示
				// 菜谱名称
				if (data[5] != null)
					info.setMenuName(data[5].toString());
				// 菜谱类别
				if (data[6] != null)
					info.setMenuTypeName(data[6].toString());
				// 份数
				if (data[7] != null) {
					info.setMenuAmount(dfNumber.format(data[7]));
				} else {
					info.setMenuAmount("0.00");
				}
				// 单价
				if (data[8] != null){
					info.setMenuPrice(dfNumber.format(data[8]));
				}else{
					info.setMenuPrice("0.00");
				}
				// 备注
				if (data[10] != null)
					info.setMemo(data[10].toString());
				// 合计行设置0
				if (info.getMenuTotal() == null) {
					info.setMenuTotal("0.00");
				}

				arrList.add(info);
			}
			
			result.setList(arrList);
			result.setTotalCount(Long.parseLong(String.valueOf(arrList.size())));

			LogUtil.log("EJB: 订餐信息查询正常结束。", Level.INFO, null);

			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:  订餐信息查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 订餐信息统计查询
	 * @param strDate 查询时间	
	 * @param strManType 人员类别
	 * @param strMenuType 用餐类别
     * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMenuSystemSubInfo(String strDate, String strManType, String strMenuType,String strEnterpriseCode)
		throws SQLException{

		//EJB log 开始
		LogUtil.log("EJB:订餐信息统计查询开始", Level.INFO, null);

		try {
			//参数列表
			List lstParams = new ArrayList();
			//实例返回的对象
			PageObject result = new PageObject();
		    // xsTan 修改开始 2009-1-23
			//查询sql文
			//添加检索条件
			lstParams.add(strDate);
			lstParams.add("3");
			lstParams.add(strEnterpriseCode);
			lstParams.add(strMenuType);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
			
			String strSql = "SELECT "
					+ "  C.MENU_NAME, "
					+ "  D.MENUTYPE_NAME, "
					+ "  SUM(B.MENU_AMOUNT), "
					+ "  SUM(B.MENU_TOTAL) "
					+ " FROM "
					//用户点菜表
					+ "  AD_J_USER_MENU A "
					//用户点菜子表
					+ "  LEFT JOIN AD_J_USER_SUB B "
					+ " ON A.M_ID=B.M_ID AND B.IS_USE='Y' "
					// 菜谱维护表
					+ " LEFT JOIN AD_C_MENU_WH C "
					+ " ON B.MENU_CODE = C.MENU_CODE "
					//菜谱类别表
					+ " LEFT JOIN AD_C_MENU_TYPE D "
					+ " ON C.MENUTYPE_CODE = D.MENUTYPE_CODE "
					//人员基本信息表
					+ " LEFT JOIN HR_J_EMP_INFO E"
					+ " ON A.INSERTBY = E.EMP_CODE  "
					//岗位编码表
					+ " LEFT JOIN HR_C_STATION  G "
					+ " ON E.STATION_ID = G.STATION_ID "
					+ " WHERE "
					+ "   TO_CHAR(A.MENU_DATE,'YYYY-MM-DD') = ? "
					+ "   AND A.IS_USE = 'Y' AND A.MENU_INFO = ?  "
					+ "   AND A.ENTERPRISE_CODE = ? "
					+ "   AND A.MENU_TYPE = ? "
					+ "   AND C.ENTERPRISE_CODE = ?  "
					+ "   AND D.ENTERPRISE_CODE = ? "
					+ "   AND E.ENTERPRISE_CODE = ? "
					+ "   AND G.ENTERPRISE_CODE = ? ";
			// 人员类别DRP选择非空
			if (!"".equals(strManType)) {
				lstParams.add(strManType);
				strSql +=  "  AND G.WORK_KIND = ? ";
			}
			strSql += "GROUP BY C.MENU_NAME, D.MENUTYPE_NAME";
			
			//log
			LogUtil.log("EJB:SQL =" + strSql, Level.INFO, null);
            //转化为数组形式
			Object[] objParams = lstParams.toArray();
			//获得记录
			List lstRecord = bll.queryByNativeSQL(strSql,objParams);

			List<MenuSystemInfo> arrList = new ArrayList<MenuSystemInfo>();
			Iterator it = lstRecord.iterator();

			//对数据记录进行循环得到
			while (it.hasNext()) {

				Object[] data = (Object[]) it.next();
				MenuSystemInfo info = new MenuSystemInfo();
				if (data[0] != null)
					info.setMenuName(data[0].toString());
				if (data[1] != null)
					info.setMenuTypeName(data[1].toString());
				if (data[2] != null)
					info.setMenuAmount(data[2].toString());
				if (data[3] != null)
					info.setMenuTotal(data[3].toString());
				arrList.add(info);
			}
			
			result.setList(arrList);
			result.setTotalCount(Long.parseLong(String.valueOf(arrList.size())));
			
		    // xsTan 修改结束 2009-1-23
			LogUtil.log("EJB: 订餐信息统计查询正常结束。", Level.INFO, null);

			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:  订餐信息统计查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}

}
