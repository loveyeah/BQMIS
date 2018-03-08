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
import power.ejb.administration.form.DutyNoteSearchInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Remote class 值班记事查询EJB.
 * 
 * @author 赵明建
 */
@Stateless
public class DutyNoteSearchFacade implements DutyNoteSearchFacadeRemote {
	 /**实例NativeSqlHelper*/
    @EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    /**
     * 返回记事查询结果
     * @Param startDate  起始时间 
     * @Param endDate  结束时间
     * @Param workTypeCode 工作类型编码
     * @Param subWorkTypeCode 子工作类型编码
     * @Param  valueTypeCode 值类型编码
     * @Param  rowStartIdxAndCount 起始行及最大行限制
     * @return PageObject
     */
    public PageObject getOnDutyListInfo(String startDate, String endDate,
    		String workTypeCode,String subWorkTypeCode, String valueTypeCode,
			String strEnterPriseCode,int... rowStartIdxAndCount) {

		try{
			//参数列表
			List lstParams = new ArrayList();
			PageObject result = new PageObject();
			 //查询sql文
			lstParams.add(strEnterPriseCode);
			lstParams.add(strEnterPriseCode);
			
			String strSql = "SELECT  "
						  + "  ID, "
						  + "  TO_CHAR(REG_TIME,'YYYY-MM-DD HH24:MI'), "
						  + "  TO_CHAR(REG_TIME,'HH24:MI'), "
						  + "  REG_TEXT, "
						  + "  B.SUB_WORKTYPE_NAME, "
						  + "  C.DUTY_TYPE_NAME, "
						  + "  DUTYMAN "
						  + "FROM "
						  +"   AD_J_ONDUTY  A  LEFT JOIN  AD_C_WORKTYPE B "
						  +"  ON A.WORKTYPE_CODE = B.WORKTYPE_CODE "
						  +"  AND A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE AND B.ENTERPRISE_CODE = ? "
						  +"  LEFT JOIN AD_C_DUTYTYPE C "
						  +"  ON A.DUTY_TYPE =C.DUTY_TYPE AND A.WORKTYPE_CODE = C.WORKTYPE_CODE "
						  +"  AND C.ENTERPRISE_CODE = ? ";
           String strSqlCount = "SELECT  COUNT(*)  "
				          + "FROM "
				          +"   AD_J_ONDUTY  A  LEFT JOIN AD_C_WORKTYPE B "
				          +"  ON A.WORKTYPE_CODE = B.WORKTYPE_CODE "
				          +"  AND A.SUB_WORKTYPE_CODE = B.SUB_WORKTYPE_CODE AND B.ENTERPRISE_CODE = ?  "
				          +"  LEFT JOIN AD_C_DUTYTYPE C "
				          +"  ON A.DUTY_TYPE =C.DUTY_TYPE AND A.WORKTYPE_CODE = C.WORKTYPE_CODE " 
				          +"  AND C.ENTERPRISE_CODE = ? ";			
           //查询条件sql文
			lstParams.add("Y");
			 String strSqlWhere = "WHERE A.IS_USE= ?  ";
			//添加企业编码检索
			 lstParams.add(strEnterPriseCode);
            strSqlWhere += " AND A.ENTERPRISE_CODE = ? ";
			//开始日期
            if((startDate != null) && (!startDate.equals(""))){
				lstParams.add(startDate);
				strSqlWhere += " AND  TO_CHAR(A.REG_TIME,'YYYY-MM-DD') >= ? ";
            }
            //结束日期
			if((endDate != null) && (!endDate.equals(""))){
				lstParams.add(endDate);
				strSqlWhere += " AND  TO_CHAR(A.REG_TIME,'YYYY-MM-DD') <= ? ";
	        }
            //工作类型编码            			
			if((workTypeCode != null) && (!workTypeCode.equals(""))){
				lstParams.add(workTypeCode);
				strSqlWhere +=" AND A.WORKTYPE_CODE = ? ";
	        }
			//子工作类型编码
			if((subWorkTypeCode) != null && (!subWorkTypeCode.equals(""))){
				lstParams.add(subWorkTypeCode);
				strSqlWhere +=" AND A.SUB_WORKTYPE_CODE = ? ";
	        }
			//值类编码
			if((valueTypeCode!=null) && (!valueTypeCode.equals(""))){
				lstParams.add(valueTypeCode);
				strSqlWhere +=" AND A.DUTY_TYPE = ? ";
	        }
			//参数转化为数组形式
			Object[] objParams = lstParams.toArray();
			 //拼接sql文
			strSql  += strSqlWhere;
			strSqlCount += strSqlWhere;
			//log
			LogUtil.log("EJB:值班记事查询开始。SQL=" + strSql, Level.INFO, null);
		
            //读取数据库记录数
			long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,objParams).toString());
			 //调用EJB获取RegularWorkSearchInfo实例
			List<DutyNoteSearchInfo> arrList = new ArrayList<DutyNoteSearchInfo>();
			List list =bll.queryByNativeSQL(strSql,objParams, rowStartIdxAndCount);
			Iterator it = list.iterator();
			while(it.hasNext()){
				DutyNoteSearchInfo info = new DutyNoteSearchInfo();
				//设置值班记事信息
				Object[] data =(Object[])it.next();
				if((data[0]!=null) && (!"".equals(data[0].toString()))){
					info.setNoteId(Long.parseLong(data[0].toString()));
				}
				if((data[1]!=null) && (!"".equals(data[1].toString()))){
					info.setRegDate(data[1].toString());
				}
				if((data[2]!=null) && (!"".equals(data[2].toString()))){
					info.setRegTime(data[2].toString());
				}
				if((data[3]!=null) && (!"".equals(data[3].toString()))){
					info.setRegText(data[3].toString());
				}
				if((data[4]!=null) && (!"".equals(data[4].toString()))){
					info.setWorktypeCode(data[4].toString());
				}
				if((data[5]!=null) && (!"".equals(data[5].toString()))){
					info.setDutyType(data[5].toString());
				}
				if((data[6]!=null) && (!"".equals(data[6].toString()))){
					info.setDutyman(data[6].toString());
				}
				arrList.add(info);
			}
			//返回PageObject
			result.setTotalCount(lngTotalCount);
			result.setList(arrList);
			//log
			LogUtil.log("EJB:值班记事查询结束。", Level.INFO, null);
			return result;
			
		}catch(RuntimeException e){
			LogUtil.log("EJB:值班记事查询失败。", Level.SEVERE, e);
			throw e;
		}
		
	}

}
