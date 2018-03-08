/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.administration.form.CarPassSearchInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 车辆进出查询Remote
 *
 * @author
 * @version 1.0
 */
@Stateless
public class CarPassSearchFacade implements CarPassSearchFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
    /**
     * 
     * @param startDate 开始时间
     * @param endDate 截止时间
     * @param passcode 通行证号
     * @param firm 所在单位
     * @param rowStartIdxAndCount 开始行及限制行
     * @return PageObject 
     */
	@SuppressWarnings("unchecked")
	public PageObject getCarPassInfoDetails(String startDate,String endDate,String passcode,String firm,String strEnterpriseCode,final int... rowStartIdxAndCount) {
		//EJB log 开始
		LogUtil.log("EJB:进出车辆查询开始",
                Level.INFO, null);
	
		try{
			//参数表
			List lstParams = new ArrayList();
			//实例返回的对象
			PageObject result = new PageObject();
			//查询sql文
			String strSql = "SELECT "
					   + "  A.PASSCODE, "
					   + "  TO_CHAR(A.PASSTIME,'YYYY-MM-DD HH24:MI'), " 
					   + "  A.CAR_NO, "
					   + "  A.PAPERTYPE_CD, " 
					   + "  A.PAPER_ID, "
					   + "  A.FIRM, "
					   + "  A.PREMAN, "
					   + "  TO_CHAR(A.GIVE_DATE,'YYYY-MM-DD HH24:MI'), "
					   + "  A.POSTMAN, "
					   + "  B.PAPERTYPE_NAME "
					   + "FROM "
					   + "  AD_J_CARPASS A  LEFT JOIN " 
					   + "  AD_C_PAPER B  ON A.PAPERTYPE_CD = B.PAPERTYPE_CODE AND B.ENTERPRISE_CODE = ? "
			           + " WHERE   A.IS_USE= ? ";
			//查询sql文,其结果的数量
			String strSqlCount = "SELECT COUNT(*) "
							+  "FROM "
							+ "  AD_J_CARPASS A  LEFT JOIN " 
						    + "  AD_C_PAPER B  ON  A.PAPERTYPE_CD = B.PAPERTYPE_CODE AND B.ENTERPRISE_CODE = ? "
					        + " WHERE   A.IS_USE=  ?  ";
			//查询sql文的条件拼接
			lstParams.add(strEnterpriseCode);
			String strSqlWhere ="";
			//增加Y检索条件
			lstParams.add("Y");
			if((null!=startDate) && (!"".equals(startDate)))
			    lstParams.add(startDate);
				strSqlWhere += "  AND ? <= TO_CHAR(A.PASSTIME,'YYYY-MM-DD') ";
			if ((endDate != null) && (!endDate.equals(""))) {
				lstParams.add(endDate);
				strSqlWhere += "  AND ? >= TO_CHAR(A.PASSTIME,'YYYY-MM-DD') ";
			}
			if((passcode != null) && (!"".equals(passcode)))
			{
				lstParams.add(passcode);
				strSqlWhere +="  AND A.PASSCODE = ? ";
			}
			if((firm != null) && (!"".equals(firm))){
				
				strSqlWhere +="  AND A.FIRM LIKE '%"+firm+"%' ";
			}
			if(strEnterpriseCode != null && !strEnterpriseCode.equals("")){
				lstParams.add(strEnterpriseCode);
				strSqlWhere += " AND A.ENTERPRISE_CODE = ?  ";
			}
			//拼接SQL文
			strSql =strSql+strSqlWhere;
			strSqlCount = strSqlCount+strSqlWhere;
			//log
			LogUtil.log("EJB: 进出车辆查询SQL="+strSql,
	                Level.INFO, null);

			Object[] params = lstParams.toArray();
			//获得记录数
			List lstCount = bll.queryByNativeSQL(strSqlCount,params);
			long lngSize = Long.parseLong(lstCount.get(0).toString());
			//获得记录
			List lstRecord = bll.queryByNativeSQL(strSql, params,rowStartIdxAndCount);

			List<CarPassSearchInfo> arrList = new ArrayList<CarPassSearchInfo>();
			Iterator it = lstRecord.iterator();
			
			//对数据记录进行循环得到
			while(it.hasNext()){
				
				Object[] data = (Object[]) it.next();
                CarPassSearchInfo info = new CarPassSearchInfo();
				//设置CarPassSearchInfo信息
				if(data[0]!=null)
					info.setPassCode(data[0].toString());
				if (data[1] != null)
					info.setPassTime(data[1].toString());
				if (data[2] != null)
					info.setCar_no(data[2].toString());
				if (data[3] != null)
					info.setPaperType_cd(data[3].toString());
				if (data[4] != null)
					info.setPaper_id(data[4].toString());
				if (data[5] != null)
					info.setFirm(data[5].toString());
				if (data[6] != null)
					info.setPreMan(data[6].toString());
				if (data[7] != null)
					info.setGive_date(data[7].toString());
				if (data[8] != null)
					info.setPostMan(data[8].toString());
				if (data[9] != null)
					info.setPaperType_name(data[9].toString());
				
				arrList.add(info);
				}
			//如果记录数大于0，则进行如下的操作
			if(arrList.size()>0)
			{
				result.setList(arrList);
				result.setTotalCount(lngSize);
			}
			LogUtil.log("EJB: 进出车辆查询正常结束。", Level.INFO, null);
			return result;
		}catch (RuntimeException e) {
			   LogUtil.log("EJB: 进出车辆查询失败。", Level.SEVERE, e);
			
			throw e;
		}
		
	}



}
