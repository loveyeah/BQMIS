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
import power.ejb.administration.form.MeetApprove;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 会务审批查询
 * MeetApproveFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Stateless
public class MeetApproveFacade implements MeetApproveFacadeRemote {

	//fields
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private String DCM_STATUS_1 = "1";
	private String DCM_STATUS_0 = "0";
	private String DCM_STATUS_2 = "2";
	private String DCM_STATUS_3 = "3";
	/**上报状态*/
	private String STATUS_0 = "未上报";
	private String STATUS_1 = "已上报";
	private String STATUS_2 = "已终结";
	private String STATUS_3 = "已退回";
	/**差额**/
	private String BALANCE_0 = "0";
	/**空值*/
	private String BLANK = "";
	/**是否使用*/
	private String IS_USE_Y = "Y";
	
	
	/**
	 * query 会务审批查询
	 * @param startDate,endDate..
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMeetApproveInfo(String strStartDate,String strEndDate,String strDepCode,String strPerson,
				String strOverSpend,String strDcmStatus,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException{
		LogUtil.log("Ejb:会务审批查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String sql = "SELECT  D.MEET_ID AS meetId, " 
				+ "  C.CHS_NAME AS name, "
			  	+ " B.DEPT_NAME as depName, "
			  	+ " to_char(D.STARTMEET_DATE,'yyyy-mm-dd hh24:mi:ss') AS startMeetDate,  "
			  	+ " to_char(D.ENDMEET_DATE ,'yyyy-mm-dd hh24:mi:ss') AS endMeetDate, "
			  	+ " D.MEET_NAME as meetName, "
			  	+ " D.MEET_PLACE AS meetPlace, "
			  	+ " D.ROOM_NEED AS roomNeed, "
			  	+ " D.MEET_OTHER AS meetOther,"
			  	+ " D.CIG_NAME AS cigName, "
			  	+ " D.CIG_PRICE AS cigPrice, "
			  	+ " D.CIG_NUM AS cigNum, "
			  	+ " D.WINE_NAME AS wineName, "
			  	+ " D.WINE_NUM AS wineNum, " 
			  	+ " D.TF_NUM AS tfNum, "
			  	+ " D.TF_THING AS tfThing, "
			  	+ " D.DJ_NUM AS djNum, "
			  	+ " D.DJ_THING AS djThing, "
			  	+ " D.BJ_NUM AS bjNum, "
			  	+ " D.BJ_THING AS bjThing, "
			  	+ " D.DINNER_TIME AS dinnerTime, "
			  	+ " D.DINNER_NUM AS dinnerNum, "
			  	+ " D.DINNER_BZ AS dinnerBz, "
			  	+ " D.BUDPAY_INALL AS budpayInall, "
			  	+ " D.REALPAY_INALL AS realpayInall, "
			  	+ " E.PAY_NAME AS payName, "
			  	+ " E.PAY_BUDGET AS payBudget, "
			  	+ " E.PAY_REAL AS payReal, "
			  	+" D.DCM_STATUS AS dcmStatus "
				+ "FROM "
				+ " AD_J_MEET D "
				+ " Left join  "
				+ " HR_J_EMP_INFO C "
				+ " ON  "
				+ " D.APPLY_MAN = C.EMP_CODE "
				+ " AND C.ENTERPRISE_CODE=? "
				+ " Left join  "
				+ " AD_J_MEET_MX E  "
				+ " ON  "
				+ " D.MEET_ID = E.MEET_ID "
				+ " AND E.IS_USE = ? "
				+ " Left join  "
			  	+ " hr_c_dept B "
			  	+ " ON  "
			  	+ "C.DEPT_ID = B.DEPT_ID "
			  	+ " AND B.ENTERPRISE_CODE=? "
			  	+ "  WHERE D.IS_USE = ? "
			  	+ " and  D.ENTERPRISE_CODE=? ";
			// 查询参数数量
			int paramsCnt = 5;
			if(checkNull(strStartDate))
				paramsCnt++;
			if(checkNull(strEndDate))
				paramsCnt++;
			if(checkNull(strDepCode))
				paramsCnt++;
			if(checkNull(strPerson))
				paramsCnt++;
			if(checkNull(strDcmStatus))
				paramsCnt++;
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = enterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = enterpriseCode;
			params[i++] = IS_USE_Y;
			params[i++] = enterpriseCode;
			
			//如果画面.开始日期存在
			if(checkNull(strStartDate)) {
				sql += " AND TO_CHAR(D.STARTMEET_DATE,'YYYY-MM-DD') >= ? ";
				params[i++] = strStartDate;
			}
			//如果画面.结束日期存在
			if(checkNull(strEndDate)) {
				sql += " AND TO_CHAR(D.STARTMEET_DATE,'YYYY-MM-DD') <= ? ";
				params[i++] = strEndDate;
			}
			
			//如果画面.部门存在
			if(checkNull(strDepCode)) {
				sql += " AND B.DEPT_CODE like ? ";
				params[i++] = strDepCode + "%";
//				strSqlWhere += "  and e.dept_id = ? ";
//				strSqlWhere += "  and d.dept_code like ? ";
//				params[i++] = strDeptCode + "%";
			}
			// 如果画面.人员存在
			if(checkNull(strPerson)) {
				sql += "AND D.APPLY_MAN = ? ";
				params[i++] = strPerson;
			}
			// 如果超支
			if(strOverSpend != null && ("Y").equals(strOverSpend)) {
				sql += "AND (E.PAY_REAL - E.PAY_BUDGET) > 0 ";
			}else if(strOverSpend != null && ("N").equals(strOverSpend)) {
				// 如果未超支
				sql += "AND (E.PAY_REAL - E.PAY_BUDGET) <= 0 ";
			}
			// 如果单据状态选择
			if(checkNull(strDcmStatus)) {
				sql += "AND D.DCM_STATUS = ? ";
				params[i++] = strDcmStatus;
			}
			sql += " ORDER BY D.MEET_ID";
			// 打印sql文
			LogUtil.log("sql 文："+sql, Level.INFO, null);	
			//list
			List list = bll.queryByNativeSQL(sql, params,rowStartIdxAndCount);
			List<MeetApprove> arrList = new ArrayList<MeetApprove>();
			Iterator it = list.iterator();
			// 订单号
			String strMId = "";
			String strTemp = "";
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				MeetApprove info = new MeetApprove();
				// 订单号
				if(data[0] != null){
					// 设置隐藏订单号
					info.setMeetHidId(data[0].toString());
					strMId = data[0].toString();
				}
				// 订单号不同时显示
				if (!strTemp.equals(strMId)) {
					strTemp = strMId;
					// 会议审批单.会议申请单号	
					if(data[0] != null && !data[0].toString().equals(BLANK)){
						info.setMeetId(data[0].toString());
					}
					// 会议审批单.会议审批单.会议开始时间	
					if(data[3] != null && !data[3].toString().equals(BLANK)){
						info.setStartMeetDate(data[3].toString());
					}
					// 会议审批单.会议结束时间
					if(data[4] != null && !data[4].toString().equals(BLANK)){
						info.setEndMeetDate(data[4].toString());
					}
					// 会议审批单.会议名称		
					if(data[5] != null && !data[5].toString().equals(BLANK)){
						info.setMeetName(data[5].toString());
					}
					// 会议审批单.会议地点
					if(data[6] != null && !data[6].toString().equals(BLANK)){
						info.setMeetPlace(data[6].toString());
					}
					// 会议审批单.会场要求		
					if(data[7] != null && !data[7].toString().equals(BLANK)){
						info.setRoomNeed(data[7].toString());
					}
					// 会议审批单.会议其他要求		
					if(data[8] != null && !data[8].toString().equals(BLANK)){
						info.setMeetOther(data[8].toString());
					}
					// 会议审批单.会议用烟名称
					if(data[9] != null && !data[9].toString().equals(BLANK)){
						info.setCigName(data[9].toString());
					}
					// 会议审批单.会议用烟价格		
					if(data[10] != null && !data[10].toString().equals(BLANK)){
						info.setCigPrice(data[10].toString());
					}
					// 会议审批单.会议用烟数量	
					if(data[11] != null && !data[11].toString().equals(BLANK)){
						info.setCigNum(data[11].toString());
					}
					// 会议审批单.会议用酒名称		
					if(data[12] != null && !data[12].toString().equals(BLANK)){
						info.setWineName(data[12].toString());
					}
					// 会议审批单.会议用酒数量		
					if(data[13] != null && !data[13].toString().equals(BLANK)){
						info.setWineNum(data[13].toString());
					}
					// 会议审批单.会议住宿-套房数量			
					if(data[14] != null && !data[14].toString().equals(BLANK)){
						info.setTfNum(data[14].toString());
					}
					// 会议审批单.会议住宿-套房用品
					
					if(data[15] != null && !data[15].toString().equals(BLANK)){
						info.setTfThing(data[15].toString());
					}
					// 会议审批单.会议住宿-单间数量	
					if(data[16] != null && !data[16].toString().equals(BLANK)){
						info.setDjNum(data[16].toString());
					}
					// 会议审批单.会议住宿-单间用品	
					if(data[17] != null && !data[17].toString().equals(BLANK)){
						info.setDjThing(data[17].toString());
					}
					// 会议审批单.会议住宿-标间数量
					if(data[18] != null && !data[18].toString().equals(BLANK)){
						info.setBjNum(data[18].toString());
					}
					// 会议审批单.会议住宿-标间用品
					if(data[19] != null && !data[19].toString().equals(BLANK)){
						info.setBjThing(data[19].toString());
					}
					// 会议审批单.就餐时间	
					if(data[20] != null && !data[20].toString().equals(BLANK)){
						info.setDinnerTime(data[20].toString());
					}
					//会议审批单.就餐人数		
					if(data[21] != null && !data[21].toString().equals(BLANK)){
						info.setDinnerNum(data[21].toString());
					}
					// 会议审批单.用餐标准	
					if(data[22] != null && !data[22].toString().equals(BLANK)){
						info.setDinnerBz(data[22].toString());
					}
					// 会议审批单.预计费用汇总			
					if(data[23] != null && !data[23].toString().equals(BLANK)){
						info.setBudpayInall(data[23].toString());
					}
					// 会议审批单.实际费用汇总
					if(data[24] != null && !data[24].toString().equals(BLANK)){
						info.setRealpayInall(data[24].toString());
					}
					// 人员编码表.姓名	
					if(data[1] != null && !data[1].toString().equals(BLANK)){
						info.setName(data[1].toString());
					}
					// 部门编码表.部门名称
					if(data[2] != null && !data[2].toString().equals(BLANK)){
						info.setDepName(data[2].toString());
					}
					// 会议接待审批费用.单据状态	
					if(data[28] != null && !data[28].toString().equals(BLANK)){
						if(DCM_STATUS_0.equals(data[28].toString()))
							info.setDcmStatus(STATUS_0);
						else if(DCM_STATUS_1.equals(data[28].toString()))
							info.setDcmStatus(STATUS_1);
						else if(DCM_STATUS_2.equals(data[28].toString()))
							info.setDcmStatus(STATUS_2);
						else if(DCM_STATUS_3.equals(data[28].toString()))
							info.setDcmStatus(STATUS_3);
					}
					// 设置差额
					if(info.getBudpayInall()!= null && !BLANK.equals(info.getBudpayInall())) {
						if(info.getRealpayInall() != null && !BLANK.equals(info.getRealpayInall())) {
							info.setBalance(String.valueOf((Double.parseDouble(info.getBudpayInall())-
									Double.parseDouble(info.getRealpayInall()))));
						}else {
							info.setBalance(info.getBudpayInall());
						}
					}else {
						if(info.getRealpayInall() != null && !BLANK.equals(info.getRealpayInall())) {
							info.setBalance(String.valueOf(0-Double.parseDouble(info.getRealpayInall())));
						}else {
							info.setBalance(BALANCE_0);
						}
					}
				}
				// 会议接待审批费用.费用名称		
				if(data[25] != null && !data[25].toString().equals(BLANK)){
					info.setPayName(data[25].toString());
				}
				// 会议接待审批费用.费用预算		
				if(data[26] != null && !data[26].toString().equals(BLANK)){
					info.setPayBudget(data[26].toString());
				}
				// 会议接待审批费用.实际费用
				if(data[27] != null && !data[27].toString().equals(BLANK)){
					info.setPayReal(data[27].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:会务审批查询结束", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:会务审批查询失败", Level.INFO, null);
			throw new SQLException();
		}
	}
	
	/**
	 * query 会议申请查询帐票
	 * @param strApplyNo
	 * @return PageObject
	 * @author daichunlin
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMeetApproveInfo(String strApplyNo,String enterpriseCode,final int ...rowStartIdxAndCount)throws SQLException{
		LogUtil.log("Ejb:会议申请查询开始", Level.INFO, null);
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "SELECT  A.MEET_ID AS meetId, " 
				       + " A.MEET_NAME as meetName, "
			 	       + " to_char(A.STARTMEET_DATE,'yyyy-mm-dd hh24:mi') AS startMeetDate,  "
			  	       + " to_char(A.ENDMEET_DATE ,'yyyy-mm-dd hh24:mi') AS endMeetDate, "
			  	       + " A.MEET_PLACE AS meetPlace, "
					   + " A.ROOM_NEED AS roomNeed, "
					   + " A.MEET_OTHER AS meetOther,"
					   + " A.CIG_NAME AS cigName, "
					   + " A.CIG_PRICE AS cigPrice, "
					   + " A.CIG_NUM AS cigNum, "
					   + " A.WINE_NAME AS wineName, "
					   + " A.WINE_PRICE AS winePrice, "
					   + " A.WINE_NUM AS wineNum, " 
					   + " A.TF_NUM AS tfNum, "
					   + " A.TF_THING AS tfThing, "					   
					   + " A.DJ_NUM AS djNum, "
					   + " A.DJ_THING AS djThing, "
					   + " A.BJ_NUM AS bjNum, "
					   + " A.BJ_THING AS bjThing, "	
					   + " to_char(A.DINNER_TIME ,'yyyy-mm-dd hh24:mi:ss') AS dinnerTime, "
					   + " A.DINNER_NUM AS dinnerNum, "
					   + " A.DINNER_BZ AS dinnerBz, "
					   + " A.BUDPAY_INALL AS budpayInall, "
					   + " A.REALPAY_INALL AS realpayInall, "
					   + " B.CHS_NAME AS name, "
					   + " C.DEPT_NAME AS depName "
					   + "FROM "							
					   + " AD_J_MEET A LEFT JOIN HR_J_EMP_INFO B"
					   + " ON A.APPLY_MAN = B.EMP_CODE  AND "
					   + " B.IS_USE = ?"
					   + " LEFT JOIN HR_C_DEPT C"
					   + " ON B.DEPT_ID  = C.DEPT_ID AND "
					   + " C.IS_USE = ?"
					   + "WHERE  A.MEET_ID = ? AND " 					
					   + " A.IS_USE = ? AND" 
			           + " A.ENTERPRISE_CODE = ? ";
						  
			// 查询参数数量
			int paramsCnt = 5;		
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;
			params[i++] = "Y"; //update by sychen 20100902
//			params[i++] = "U";
			params[i++] = strApplyNo;			
			params[i++] = IS_USE_Y;			
			params[i++] = enterpriseCode;
			// 打印sql文
			LogUtil.log("会议申请查询sql文："+ strSql, Level.INFO, null);	
			// list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<MeetApprove> arrList = new ArrayList<MeetApprove>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				MeetApprove info = new MeetApprove();
				// 会议审批单.会议申请单号	
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setMeetId(data[0].toString());
				}
				// 会议审批单.会议名称		
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setMeetName(data[1].toString());
				}
				// 会议审批单.会议开始时间	
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setStartMeetDate(data[2].toString());
				}
				// 会议审批单.会议结束时间
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setEndMeetDate(data[3].toString());
				}
				// 会议审批单.会议地点
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setMeetPlace(data[4].toString());
				}
				// 会议审批单.会场要求		
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setRoomNeed(data[5].toString());
				}
				// 会议审批单.会议其他要求		
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setMeetOther(data[6].toString());
				}
				// 会议审批单.会议用烟名称
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setCigName(data[7].toString());
				}
				// 会议审批单.会议用烟价格		
				if(data[8] != null && !data[8].toString().equals(BLANK)){
					info.setCigPrice(dfNumber.format(data[8]));
				}
				// 会议审批单.会议用烟数量	
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					info.setCigNum(data[9].toString());
				}
				// 会议审批单.会议用酒名称		
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setWineName(data[10].toString());
				}
				// 会议审批单.会议用酒价格		
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setWinePrice(dfNumber.format(data[11]));
				}
				// 会议审批单.会议用酒数量		
				if(data[12] != null && !data[12].toString().equals(BLANK)){
					info.setWineNum(data[12].toString());
				}
				// 会议审批单.会议住宿-套房数量			
				if(data[13] != null && !data[13].toString().equals(BLANK)){
					info.setTfNum(data[13].toString());
				}
				// 会议审批单.会议住宿-套房用品
				
				if(data[14] != null && !data[14].toString().equals(BLANK)){
					info.setTfThing(data[14].toString());
				}
				// 会议审批单.会议住宿-单间数量	
				if(data[15] != null && !data[15].toString().equals(BLANK)){
					info.setDjNum(data[15].toString());
				}
				// 会议审批单.会议住宿-单间用品	
				if(data[16] != null && !data[16].toString().equals(BLANK)){
					info.setDjThing(data[16].toString());
				}
				// 会议审批单.会议住宿-标间数量
				if(data[17] != null && !data[17].toString().equals(BLANK)){
					info.setBjNum(data[17].toString());
				}
				// 会议审批单.会议住宿-标间用品
				if(data[18] != null && !data[18].toString().equals(BLANK)){
					info.setBjThing(data[18].toString());
				}
				// 会议审批单.就餐时间	
				if(data[19] != null && !data[19].toString().equals(BLANK)){
					info.setDinnerTime(data[19].toString());
				}
				//会议审批单.就餐人数		
				if(data[20] != null && !data[20].toString().equals(BLANK)){
					info.setDinnerNum(data[20].toString());
				}
				// 会议审批单.用餐标准	
				if(data[21] != null && !data[21].toString().equals(BLANK)){
					info.setDinnerBz(dfNumber.format(data[21]));
				}
				// 会议审批单.预计费用汇总			
				if(data[22] != null && !data[22].toString().equals(BLANK)){
					info.setBudpayInall(dfNumber.format(data[22]));
				}
				// 会议审批单.实际费用汇总
				if(data[23] != null && !data[23].toString().equals(BLANK)){
					info.setRealpayInall(dfNumber.format(data[23]));
				}	
				// 人员编码表.姓名	
				if(data[24] != null && !data[24].toString().equals(BLANK)){
					info.setName(data[24].toString());
				}
				// 部门编码表.部门名称
				if(data[25] != null && !data[25].toString().equals(BLANK)){
					info.setDepName(data[25].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:会议申请查询结束", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log(" Ejb:会议申请查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * check 字符串是否为空
	 * @param str
	 * @return 空返回false，非空返回true
	 */
	public Boolean checkNull(String str) {
		if(str != null && !(BLANK).equals(str))
			return true;
		else return false;
	}
}
