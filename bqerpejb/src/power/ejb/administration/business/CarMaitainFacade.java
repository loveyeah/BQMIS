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
import power.ejb.administration.form.CarMaitainDetailInfo;
import power.ejb.administration.form.CarMaitainInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class CarMaitainFacade implements CarMaitainFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/**空值*/
	private String BLANK = "";
	/**是否使用*/
	private String IS_USE_Y = "Y";

	/**
	 * query 车辆申请单查询
	 * @param strApplyNo
	 * @return PageObject
	 * @author daichunlin
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarMaitainInfo(String enterpriseCode,String strApplyNo,final int ...rowStartIdxAndCount)throws SQLException{
		LogUtil.log("Ejb:车辆维修申请单查询开始", Level.INFO, null);
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "SELECT  F.CAR_NAME, " 
				       + " A.CAR_NO, "
			 	       + " to_char(A.REPAIR_DATE,'yyyy-mm-dd') AS REPAIR_DATE,  "			  	     
					   + " A.DRIVE_MILE, "					   
					   + " A.REASON,"
					   + " A.SUM, "
					   + " A.REAL_SUM, "					   
					   + " A.MEMO, "					   
					   + " A.DEP_IDEA, "
					   + " A.DEP_BOSS_CODE, "
					   + " A.DEP_SIGN_DATE, " 					   
					   + " A.VICE_BOSS_IDEA, "
					   + " A.VICE_BOSS_CODE, "					   
					   + " A.VICE_BOSS_SIGN_DATE, "					   
					   + " A.BIG_BOSS_IDEA, "
					   + " A.BIG_BOSS_CODE, "
					   + " A.BIG_BOSS_SIGN_DATE, "
					   + " C.CP_NAME, "
					   + " E.CHS_NAME "					  
					   + "FROM "							
					   + " AD_J_CARWH A "
					   + " LEFT JOIN AD_J_CARFILE F"
					   + " ON A.CAR_NO  = F.CAR_NO AND "
					   + " F.ENTERPRISE_CODE  = ? "
					   + " LEFT JOIN AD_C_CARMEND_WH C"
					   + " ON A.CP_CODE  = C.CP_CODE AND "
					   + " C.ENTERPRISE_CODE  = ? "
					   + " LEFT JOIN HR_J_EMP_INFO E"
					   + " ON A.MANAGER  = E.EMP_CODE AND "	
					   + " E.ENTERPRISE_CODE  = ? "
					   + "WHERE  A.WH_ID = ? AND " 					
					   + " A.IS_USE = ? AND"  
			           + " A.ENTERPRISE_CODE = ? "; 
						  
			// 查询参数数量
			int paramsCnt = 6;		
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;	
			params[i++] = enterpriseCode;	
			params[i++] = enterpriseCode;	
			params[i++] = enterpriseCode;	
			params[i++] = strApplyNo;			
			params[i++] = IS_USE_Y;		
			params[i++] = enterpriseCode;	
			
			// 打印sql文
			LogUtil.log("车辆维修申请单查询sql文："+ strSql, Level.INFO, null);	
			// list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<CarMaitainInfo> arrList = new ArrayList<CarMaitainInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				CarMaitainInfo info = new CarMaitainInfo();
				// 车辆维护表.车名	
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setCarName(data[0].toString());
				}
				// 车辆维护表.车牌号	
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setCarNo(data[1].toString());
				}
				// 车辆维护表.维修日期
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setRepairDate(data[2].toString());
				}
				// 车辆维护表.维修里程
				if(data[3] != null && !data[3].toString().equals(BLANK)){
					info.setDriveMile(dfNumber.format(data[3]));
				}else{
					info.setDriveMile("0.00");
				}
				// 车辆维护表.支出事由
				if(data[4] != null && !data[4].toString().equals(BLANK)){
					info.setReason(data[4].toString());
				}
				// 车辆维护表.预算费用	
				if(data[5] != null && !data[5].toString().equals(BLANK)){
					info.setSum(dfNumber.format(data[5]));
				}else{
					info.setSum("0.00");
				}
				// 车辆维护表.实际费用
				if(data[6] != null && !data[6].toString().equals(BLANK)){
					info.setRealSum(dfNumber.format(data[6]));
				}else{
					info.setRealSum("0.00");
				}
				// 车辆维护表.备注
				if(data[7] != null && !data[7].toString().equals(BLANK)){
					info.setMemo(data[7].toString());
				}
				// 车辆维护表.部门经理审核意见
				if(data[8] != null && !data[8].toString().equals(BLANK)){
					info.setDepIdea(data[8].toString());
				}
				// 车辆维护表.部门经理签字
				if(data[9] != null && !data[9].toString().equals(BLANK)){
					info.setDepBossCode(data[9].toString());
				}
				// 车辆维护表.部门经理签字时间
				if(data[10] != null && !data[10].toString().equals(BLANK)){
					info.setDepSignDate(data[10].toString());
				}
				// 车辆维护表.行政部经理审核意见
				if(data[11] != null && !data[11].toString().equals(BLANK)){
					info.setViceBossIdea(data[11].toString());
				}
				// 车辆维护表.行政部经理签字
				if(data[12] != null && !data[12].toString().equals(BLANK)){
					info.setViceBossCode(data[12].toString());
				}
				// 车辆维护表.行政部经理签字时间
				if(data[13] != null && !data[13].toString().equals(BLANK)){
					info.setViceBossSignDate(data[13].toString());
				}
				// 车辆维护表.总经理审核意见
				if(data[14] != null && !data[14].toString().equals(BLANK)){
					info.setBigBossIdea(data[14].toString());
				}
				// 车辆维护表.总经理签字
				if(data[15] != null && !data[15].toString().equals(BLANK)){
					info.setBigBossCode(data[15].toString());
				}
				// 车辆维护表.总经理签字时间
				if(data[16] != null && !data[16].toString().equals(BLANK)){
					info.setBigBossSignDate(data[16].toString());
				}				
				// 车辆维修单位维护.单位名称
				if(data[17] != null && !data[17].toString().equals(BLANK)){
					info.setCpName(data[17].toString());
				}
				// 人员基本信息表.人员姓名	
				if(data[18] != null && !data[18].toString().equals(BLANK)){
					info.setChsName(data[18].toString());
				}
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:车辆维修申请单查询结束", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("Ejb:车辆维修申请单查询失败", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	/**
	 * query 车辆维修申请单明细查询
	 * @param strApplyNo
	 * @return PageObject
	 * @author daichunlin
	 */
	@SuppressWarnings("unchecked")
	public PageObject getCarMaitainDetailInfo(String enterpriseCode,String strApplyNo,final int ...rowStartIdxAndCount)throws SQLException{
		LogUtil.log("Ejb:车辆维修申请单明细查询开始", Level.INFO, null);
		try{
			PageObject pobj = new PageObject();	
			// 查询sql
			String strSql = "SELECT " 				      			   
					   + " B.PRICE, "
					   + " B.REAL_PRICE, "
					   + " D.PRO_NAME "	
					   + "FROM "
					   + " AD_J_CARWH A LEFT JOIN AD_J_CARWH_MX B"
					   + " ON A.WH_ID = B.WH_ID AND "
					   + " B.IS_USE = ?"					   
					   + " LEFT JOIN AD_C_CARMEND_WH C"
					   + " ON A.CP_CODE  = C.CP_CODE AND"
					   + " C.ENTERPRISE_CODE  = ? "
					   + " LEFT JOIN AD_C_CARWH_PRO D"
					   + " ON B.PRO_CODE  = D.PRO_CODE AND "
					   + " D.ENTERPRISE_CODE  = ? "
					   + "WHERE  A.WH_ID = ? AND " 					
					   + " A.IS_USE = ? AND "
					   + " A.ENTERPRISE_CODE = ? "; 
						  
			// 查询参数数量
			int paramsCnt = 6;		
			// 查询参数数组
			Object[] params = new Object[paramsCnt];
			int i = 0;
			params[i++] = IS_USE_Y;
			params[i++] = enterpriseCode;
			params[i++] = enterpriseCode;
			params[i++] = strApplyNo;			
			params[i++] = IS_USE_Y;	
			params[i++] = enterpriseCode;
			
			// 打印sql文
			LogUtil.log("车辆维修申请单明细查询sql文："+ strSql, Level.INFO, null);	
			// list
			List list = bll.queryByNativeSQL(strSql, params, rowStartIdxAndCount);
			List<CarMaitainDetailInfo> arrList = new ArrayList<CarMaitainDetailInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				CarMaitainDetailInfo info = new CarMaitainDetailInfo();
				
				// 车辆维护明细.预算费用
				if(data[0] != null && !data[0].toString().equals(BLANK)){
					info.setPrice(data[0].toString());
				}
				// 车辆维护明细.实际费用
				if(data[1] != null && !data[1].toString().equals(BLANK)){
					info.setRealPrice(data[1].toString());
				}				
				// 车辆维护之维修项目.项目名称
				if(data[2] != null && !data[2].toString().equals(BLANK)){
					info.setProName(data[2].toString());
				}				
				arrList.add(info);
			}
			// 行数
			pobj.setTotalCount((long)(arrList.size()));
			pobj.setList(arrList);
			LogUtil.log("Ejb:车辆维修申请单明细查询结束", Level.INFO, null);
			//返回PageObject
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("Ejb:车辆维修申请单明细查询失败", Level.SEVERE, e);
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
