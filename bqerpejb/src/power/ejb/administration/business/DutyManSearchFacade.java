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
import power.ejb.administration.form.DutyManSearchInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * DutyManSearchFacade 值班人员查询.
 * 
 * @author zhaomingjian
 */
@SuppressWarnings("serial")
@Stateless
public class DutyManSearchFacade implements DutyManSearchFacadeRemote {
	
	//fields
	@EJB(beanName ="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
    /**
     * @param strStartDate 起始时间
     * @param strEndDate 截止时间
     * @param  strWorkTypeCode 工作类型码
     * @param  strSubWorkTypeCode 子工作类型码
     * @param  strDutyTypeCode 值类型码
     * @return  PageObject
     */
	public PageObject getOnDutyManInfo(String strStartDate,String strEndDate,String strWorkTypeCode,String strSubWorkTypeCode,String strDutyTypeCode,String strEnterPriseCode,final int ...rowStartIdxAndCount)
	{
		try{
			
			PageObject pobj = new PageObject();	
			//动态的list
			List lstParams = new ArrayList();
			// 查询sql
			String strSql ="SELECT" 
				  + "  A.DUTYMAN, " 
				  + "  A.REPLACEMAN, "
				  + "  A.LEAVEMAN, "
				  + "  TO_CHAR(A.DUTYTIME,'YYYY-MM-DD HH24:MI'), "
				  + "  A.POSITION, "
				  + "  B.DUTY_TYPE_NAME, "
				  + "  C.SUB_WORKTYPE_NAME, "
				  + "  A.REASON "
				  + "FROM "
				  + " AD_J_DUTYMAN  A  "
				  +"  LEFT JOIN AD_C_DUTYTYPE   B  "
				  +"  ON A.DUTYTYPE = B.DUTY_TYPE  "
				  +"  AND     A.WORKTYPE_CODE= B.WORKTYPE_CODE "
				  +"  AND     B.ENTERPRISE_CODE = ? "
				  +"  LEFT JOIN AD_C_WORKTYPE C "
				  +"  ON  A.WORKTYPE_CODE= C.WORKTYPE_CODE  "
				  +"  AND A.SUB_WORKTYPE_CODE= C.SUB_WORKTYPE_CODE AND C.ENTERPRISE_CODE = ? ";
				

			String strSqlCount = "SELECT COUNT(*) "
				  + "FROM "
				  + " AD_J_DUTYMAN  A  "
				  +"  LEFT JOIN AD_C_DUTYTYPE   B  "
				  +"  ON A.DUTYTYPE = B.DUTY_TYPE  "
				  +"  AND     A.WORKTYPE_CODE= B.WORKTYPE_CODE "
				  +"  AND     B.ENTERPRISE_CODE = ? "
				  +"  LEFT JOIN AD_C_WORKTYPE C "
				  +"  ON  A.WORKTYPE_CODE= C.WORKTYPE_CODE  "
				  +"  AND A.SUB_WORKTYPE_CODE= C.SUB_WORKTYPE_CODE  AND C.ENTERPRISE_CODE = ?  ";
			//子表增加企业检索编码
			lstParams.add(strEnterPriseCode);
			lstParams.add(strEnterPriseCode);
			//是否使用
			String strSqlWhere = "WHERE A.IS_USE= ?  " ;
			lstParams.add("Y");
			//工作类型非空操作
			if(strWorkTypeCode != null && !strWorkTypeCode.equals("")){
				lstParams.add(strWorkTypeCode);
				strSqlWhere += "  AND A.WORKTYPE_CODE = ? ";
			}
			//起始日期非空操作
			if(strStartDate !=null && !strStartDate.equals("")){
				lstParams.add(strStartDate);
				strSqlWhere += "  AND TO_CHAR(A.DUTYTIME,'YYYY-MM-DD') >=? ";
			}
			//截止时间非空操作
			if(strEndDate != null && !strEndDate.equals("")){
				lstParams.add(strEndDate);
				strSqlWhere += "  AND TO_CHAR(A.DUTYTIME,'YYYY-MM-DD') <=? ";
			}
			//子工作类型非空操作
			if(strSubWorkTypeCode !=null && !strSubWorkTypeCode.equals("")){
				lstParams.add(strSubWorkTypeCode);
				strSqlWhere += "  AND A.SUB_WORKTYPE_CODE = ? ";
			}
			//值类型非空操作
			if(strDutyTypeCode != null && !strDutyTypeCode.equals("")){
				lstParams.add(strDutyTypeCode);
				strSqlWhere += "  AND A.DUTYTYPE = ? ";
			}
			if(strEnterPriseCode != null && !strEnterPriseCode.equals("")){
				lstParams.add(strEnterPriseCode);
				strSqlWhere += "  AND A.ENTERPRISE_CODE = ? ";
			}
			                   
			//拼接查询sql
			strSql += strSqlWhere;
			strSqlCount += strSqlWhere;
			LogUtil.log("EJB : 值班人员查询开始。" , Level.INFO, null);
			LogUtil.log("EJB : SQL=" + strSql, Level.INFO, null);
			
			//数组参数
			Object[] params = lstParams.toArray();
			//获取数目
			long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,params).toString());
			//list
			List list = bll.queryByNativeSQL(strSql, params,rowStartIdxAndCount);
			List<DutyManSearchInfo> arrList = new ArrayList<DutyManSearchInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()){
				Object[] data = (Object[])it.next();
				//读取db数据并设置对象
				DutyManSearchInfo info = new DutyManSearchInfo();
				//设置人员名
				if(data[0] != null && !data[0].toString().equals("")){
					info.setDutyManName(data[0].toString());
				}
				//设置替换人
				if(data[1] != null && !data[1].toString().equals("")){
					info.setReplaceManName(data[1].toString());
				}
				//设置请假人
				if(data[2] != null && !data[2].toString().equals("")){
					info.setLeaveManName(data[2].toString());
				}
				//设置值班日期
				if(data[3] != null && !data[3].toString().equals("")){
					info.setDutyTime(data[3].toString());
				}
				//设置岗位名称
				if(data[4] != null && !data[4].toString().equals("")){
					info.setPosition(data[4].toString());
				}
				//设置值别名称
				if(data[5] != null && !data[5].toString().equals("")){
					info.setDutyTypeName(data[5].toString());
				}
				//设置子工作名
				if(data[6] != null && !data[6].toString().equals("")){
					info.setSubWorkTypeName(data[6].toString());
				}
				//设置请假人名
				if(data[7] != null && !data[7].toString().equals("")){
					info.setReason(data[7].toString());
				}
      	arrList.add(info);
			}
			                 
			pobj.setList(arrList);
			pobj.setTotalCount(lngTotalCount);
			//返回PageObject
			 LogUtil.log("EJB:  DutyManSearchFacade 结束。", Level.INFO, null);
			return pobj;
		}catch(RuntimeException e){
			LogUtil.log("EJB: DutyManSearchFacade 失败。", Level.SEVERE, e);
			throw e;
		}

	}
	
}
