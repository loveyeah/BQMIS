/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.business;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJMeet;
import power.ejb.administration.AdJMeetfileFacadeRemote;
import power.ejb.administration.form.MeetApplyReportInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 会务申请上报
 * @author huangweijie
 */
@Stateless
public class MeetApplyReportFacade implements MeetApplyReportFacadeRemote{
	//fields
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB(beanName ="AdJMeetfileFacade")
	protected AdJMeetfileFacadeRemote remote;
	/**空值*/
	private String BLANK = "";
	
	/**
	 * query 会务申请上报查询
	 * @param workerCode 登陆的员工号
	 * @param argEnterpriseCode 企业编码
	 * @return PageObject
	 * @throws SQLException 数据库异常
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMeetApplyReportList(String workerCode, String argEnterpriseCode,
			final int ...rowStartIdxAndCount) throws SQLException {
		LogUtil.log("Ejb:会务申请上报查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String sql = 
			"SELECT DISTINCT " +
				"A.MEET_ID, C.CHS_NAME, B.DEPT_NAME, " +
				"A.MEET_NAME, to_char(A.STARTMEET_DATE,'yyyy-mm-dd hh24:mi'), " +
				"to_char(A.ENDMEET_DATE,'yyyy-mm-dd hh24:mi'), " +
				"A.MEET_PLACE, A.ROOM_NEED, A.MEET_OTHER, " +
				"to_char(A.DINNER_TIME,'yyyy-mm-dd hh24:mi'), " +
				"A.DINNER_NUM, A.CIG_NAME, " +
				"A.CIG_PRICE, A.CIG_NUM, A.WINE_NAME, " +
				"A.WINE_PRICE, A.WINE_NUM, A.TF_NUM, " +
				"A.TF_THING, A.DJ_NUM, A.DJ_THING, " +
				"A.BJ_NUM, A.BJ_THING, A.DINNER_BZ, " +
				"A.BUDPAY_INALL, TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss'), " +
				"A.ID, A.REALPAY_INALL " +
			"FROM " +
				"AD_J_MEET A, " +
			 	"HR_J_EMP_INFO C " +
			 	"LEFT JOIN HR_C_DEPT B ON C.DEPT_ID = B.DEPT_ID AND B.ENTERPRISE_CODE = ? " +
			"WHERE " +
			    "A.APPLY_MAN = ? AND " +
			    "A.APPLY_MAN = C.EMP_CODE AND " +
			    "A.DCM_STATUS IN ('0', '3') AND " +
			    "A.IS_USE = 'Y' AND " +
			    "A.ENTERPRISE_CODE = ? " +
			    "AND A.ENTERPRISE_CODE = C.ENTERPRISE_CODE " +
			"ORDER BY " +
				"A.MEET_ID";
			// 打印sql文
			LogUtil.log("sql="+sql, Level.INFO, null);	
			//list
			List list = bll.queryByNativeSQL(sql, new Object[]{argEnterpriseCode, workerCode, argEnterpriseCode}, rowStartIdxAndCount);
			List<MeetApplyReportInfo> arrList = new ArrayList<MeetApplyReportInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				AdJMeet info = new AdJMeet();
				MeetApplyReportInfo myinfo = new MeetApplyReportInfo();
				// 会议审批单.会议申请单号	
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setMeetId(data[0].toString());
				}
				// 人员编码表.姓名	
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					myinfo.setWorkerName(data[1].toString());
				}
				// 部门编码表.部门名称
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					myinfo.setDeptName(data[2].toString());
				}
				// 会议审批单.会议名称		
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setMeetName(data[3].toString());
				}
				// 会议开始时间	
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					myinfo.setMeetStartTime(data[4].toString());
				}
				// 会议结束时间
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					myinfo.setMeetEndTime(data[5].toString());
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
				// 会议审批单.就餐时间	
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					myinfo.setDinnerTime(data[9].toString());
				}
				// 会议审批单.就餐人数		
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setDinnerNum(Long.parseLong(data[10].toString()));
				}
				// 会议审批单.会议用烟名称
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setCigName(data[11].toString());
				}
				// 会议审批单.会议用烟价格		
				if(data[12] != null && !data[12].toString().equals(BLANK)){
					info.setCigPrice(Double.parseDouble(data[12].toString()));
				}
				// 会议审批单.会议用烟数量	
				if(data[13] != null && !data[13].toString().equals(BLANK)){
					info.setCigNum(Long.parseLong(data[13].toString()));
				}
				// 会议审批单.会议用酒名称		
				if(data[14] != null && !data[14].toString().equals(BLANK)){
					info.setWineName(data[14].toString());
				}
				// 会议审批单.会议用酒价格		
				if(data[15] != null && !data[15].toString().equals(BLANK)){
					info.setWinePrice(Double.parseDouble(data[15].toString()));
				}
				// 会议审批单.会议用酒数量		
				if(data[16] != null && !data[16].toString().equals(BLANK)){
					info.setWineNum(Long.parseLong(data[16].toString()));
				}
				// 会议审批单.会议住宿-套房数量			
				if(data[17] != null && !data[17].toString().equals(BLANK)){
					info.setTfNum(Long.parseLong(data[17].toString()));
				}
				// 会议审批单.会议住宿-套房用品
				
				if(data[18] != null && !data[18].toString().equals(BLANK)){
					info.setTfThing(data[18].toString());
				}
				// 会议审批单.会议住宿-单间数量	
				if(data[19] != null && !data[19].toString().equals(BLANK)){
					info.setDjNum(Long.parseLong(data[19].toString()));
				}
				// 会议审批单.会议住宿-单间用品	
				if(data[20] != null && !data[20].toString().equals(BLANK)){
					info.setDjThing(data[20].toString());
				}
				// 会议审批单.会议住宿-标间数量
				if(data[21] != null && !data[21].toString().equals(BLANK)){
					info.setBjNum(Long.parseLong(data[21].toString()));
				}
				// 会议审批单.会议住宿-标间用品
				if(data[22] != null && !data[22].toString().equals(BLANK)){
					info.setBjThing(data[22].toString());
				}
				// 会议审批单.用餐标准	
				if(data[23] != null && !data[23].toString().equals(BLANK)){
					info.setDinnerBz(Double.parseDouble(data[23].toString()));
				}
				// 会议审批单.预计费用汇总			
				if(data[24] != null && !data[24].toString().equals(BLANK)){
					info.setBudpayInall(Double.parseDouble(data[24].toString()));
				}
				// 上次修改时间（用于排他）
				if(data[25] != null && !data[25].toString().equals(BLANK)){
					myinfo.setModifyTime(data[25].toString());
				}
				// ID（用于更新时从数据库取得该条记录）
				if(data[26] != null && !data[26].toString().equals(BLANK)){
					info.setId(Long.parseLong(data[26].toString()));
				}
				// 会议审批单.实际费用汇总			
				if(data[27] != null && !data[27].toString().equals(BLANK)){
					info.setRealpayInall(Double.parseDouble(data[27].toString()));
				}
				
				myinfo.setMeetInfo(info);
				arrList.add(myinfo);
			}
			sql = sql.replaceFirst("SELECT.*? FROM ", "SELECT COUNT(DISTINCT A.ID) FROM ");;
			// 打印sql文
			LogUtil.log("sql="+sql, Level.INFO, null);
			Object obj = bll.getSingal(sql, new Object[]{argEnterpriseCode, workerCode, argEnterpriseCode});
			if (null != obj) {
				pobj.setTotalCount(Long.parseLong(obj.toString()));
			} else {
				pobj.setTotalCount(0L);
			}
			// 行数
//			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:会务申请上报查询结束", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("Ejb:会务申请上报查询失败", Level.INFO, null);
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
