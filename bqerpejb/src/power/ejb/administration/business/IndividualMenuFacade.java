/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJUserMenu;
import power.ejb.administration.AdJUserMenuFacadeRemote;
import power.ejb.administration.AdJUserSub;
import power.ejb.administration.AdJUserSubFacadeRemote;
import power.ejb.administration.form.IndividualMenuInfo;
import power.ejb.administration.form.IndividualSubMenuInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 个人订餐
 * @author zhaomingjian
 *
 */
@Stateless
public class IndividualMenuFacade implements IndividualMenuFacadeRemote {
	 /**实例NativeSqlHelper*/
    @EJB(beanName ="NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    // xsTan 追加开始 2009-1-28
	 /**用户点菜表接口*/
    @EJB(beanName ="AdJUserMenuFacade")
    protected AdJUserMenuFacadeRemote adjUserMenu;
    /**用户子点菜接口*/
    @EJB(beanName = "AdJUserSubFacade")
    protected AdJUserSubFacadeRemote adjUserSub;
    // xsTan 追加结束 2009-1-28
    
	/**
	 * 订餐信息一览grid初始化
	 * 
	 * @param rowStartIdxAndCount
	 *            分页
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject getIndividualMenuListInfo (String strUserId,String enterprisecode,int... rowStartIdxAndCount)throws SQLException{
	   try{
		   LogUtil.log("EJB:订餐信息一览grid初始化开始" , Level.INFO, null);
		   PageObject result =  new PageObject();
		   List lstParams = new ArrayList();
		   lstParams.add(enterprisecode);
		   lstParams.add(enterprisecode);
		   //modify by liuyi 090909 11:05
//		   lstParams.add(enterprisecode);
		   //个人订餐查询sql
		   String strSql = "SELECT "
			                     + " A.M_ID, "
			                     + " A.MENU_DATE, "
			                     + " A.MENU_TYPE, "
			                     + " B.CHS_NAME, "
			                     + " C.DEPT_NAME, "
			                     + " A.INSERTDATE, "
			                     + " A.MENU_INFO, " 
			                     + " D.STATION_NAME,  "
			                     // xsTan 追加开始 2009-1-28 排他时间
			                     + " TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'), "
			                     + " A.PLACE "
			                     // xsTan 追加结束 2009-1-28 排他时间
			                     + " FROM "
			                     //用户点菜表
			                     + " AD_J_USER_MENU  A  "
			                     //人员基本信息表
			                     + " LEFT JOIN  HR_J_EMP_INFO B " 
			                     // modify by liuyi 090909 11:00 B表中无is_use暂无
//			                     + " ON A.INSERTBY = B.EMP_CODE AND B.IS_USE = 'Y' AND B.ENTERPRISE_CODE = ? "
			                     + " ON A.INSERTBY = B.EMP_CODE  AND B.ENTERPRISE_CODE = ? "
                                 //部门编码表
			                     + " LEFT JOIN HR_C_DEPT C "
			                     // xsTan 修改开始 2009-1-28 修改连接条件
			                     + " ON B.DEPT_ID = C.DEPT_ID AND C.IS_USE = 'Y' AND C.ENTERPRISE_CODE = ? "
			                     // xsTan 修改结束 2009-1-28
			                     //岗位编码表
			                     // xsTan 修改开始 2009-1-28 修改表名，连接条件
			                     + " LEFT JOIN HR_C_STATION  D "
			                     // modify by liuyi 090909 11:05 D表中ENTERPRISE_CODE暂无
//			                     + " ON B.STATION_ID = D.STATION_ID AND D.IS_USE ='Y' AND D.ENTERPRISE_CODE = ? ";
			                     + " ON B.STATION_ID = D.STATION_ID AND D.IS_USE ='Y'  ";
                                 // xsTan 修改结束 2009-1-28
		   //返回record数
		   String strSqlCount = "SELECT "
			                  + " COUNT(A.M_ID) "
			                  + " FROM "
			                     //用户点菜表
			                     + " AD_J_USER_MENU  A  "
			                     //人员基本信息表
			                     + " LEFT JOIN  HR_J_EMP_INFO B " 
			                     // modify by liuyi 090909 11:00 B表中无is_use暂无
//			                     + " ON A.INSERTBY = B.EMP_CODE AND B.IS_USE = 'Y' AND B.ENTERPRISE_CODE = ? "
			                     + " ON A.INSERTBY = B.EMP_CODE  AND B.ENTERPRISE_CODE = ? "
                              //部门编码表
			                     + " LEFT JOIN HR_C_DEPT C "
			                     // xsTan 修改开始 2009-1-28 修改连接条件
			                     + " ON B.DEPT_ID = C.DEPT_ID AND C.IS_USE = 'Y' AND C.ENTERPRISE_CODE = ? "
			                     // xsTan 修改结束 2009-1-28
			                     //岗位编码表
			                     // xsTan 修改开始 2009-1-28 修改表名，连接条件
			                     + " LEFT JOIN HR_C_STATION  D "
			                     // modify by liuyi 090909 11:05 D表中ENTERPRISE_CODE暂无
//			                     + " ON B.STATION_ID = D.STATION_ID AND D.IS_USE ='Y' AND D.ENTERPRISE_CODE = ? ";
			                     + " ON B.STATION_ID = D.STATION_ID AND D.IS_USE ='Y'  ";
                              // xsTan 修改结束 2009-1-28
		    //查询条件
		   lstParams.add(strUserId);
		   lstParams.add(enterprisecode);
		    String strSqlWhere = "WHERE A.IS_USE = 'Y' "
			                   + " AND A.INSERTBY = ? "
			                   + " AND TO_CHAR(A.MENU_DATE,'YYYY-MM-DD') >=TO_CHAR(SYSDATE,'YYYY-MM-DD') "
			                   // add 黄维杰 企业编码添加
		    				   + " AND A.ENTERPRISE_CODE = ?"
		    				   + " ORDER BY A.MENU_DATE ,A.MENU_TYPE ";
            //拼接sql 
		    strSql += strSqlWhere;
            strSqlCount += strSqlWhere;
            //生成参数
            // add by huagnweijie 20090207 start enterprisecode add
            Object[] params = lstParams.toArray();
            
            // add by huagnweijie 20090207 end
            //log
           LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			 //读取数据库记录数
			long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,params).toString());
			 //调用EJB获取RegularWorkSearchInfo实例
			List<IndividualMenuInfo> arrList = new ArrayList<IndividualMenuInfo>();
			List list =bll.queryByNativeSQL(strSql, params,rowStartIdxAndCount);
			Iterator it = list.iterator();
			while(it.hasNext()){
				IndividualMenuInfo info = new IndividualMenuInfo();
				//设置值班记事信息
				Object[] data =(Object[])it.next();
				if((data[0]!=null) && (!"".equals(data[0].toString()))){
					info.setMId(data[0].toString());
				}
				if((data[1]!=null) && (!"".equals(data[1].toString()))){
					info.setMenuDate(data[1].toString());
				}
				if((data[2]!=null) && (!"".equals(data[2].toString()))){
					if(data[2].equals("1")){
						info.setMenuType("早餐");	
					}else if(data[2].equals("2")){
						info.setMenuType("中餐");
					}else if(data[2].equals("3")){
						info.setMenuType("晚餐");
					}else if(data[2].equals("4")){
						info.setMenuType("宵夜");
					}else{
						info.setMenuType("");
					}
					
				}
				if((data[3]!=null) && (!"".equals(data[3].toString()))){
					info.setUserName(data[3].toString());
				}
				if((data[4]!=null) && (!"".equals(data[4].toString()))){
					info.setDepName(data[4].toString());
				}
				if((data[5]!=null) && (!"".equals(data[5].toString()))){
					info.setInsertDate(data[5].toString());
				}
				if((data[6]!=null) && (!"".equals(data[6].toString()))){
					info.setMenuInfo(data[6].toString());
				}
				if((data[7]!=null) && (!"".equals(data[7].toString()))){
					// TODO 检查data[6]是否正确
					info.setStationName(data[7].toString());
				}
                // xsTan 追加开始 2009-1-28 排他时间
				if((data[8]!=null) && (!"".equals(data[8].toString()))){
					info.setStrUpdateTime(data[8].toString());
				}
				if((data[9] !=null) && (!"".equals(data[9].toString()))){
					info.setPlace(data[9].toString());
				}else{
					info.setPlace("1");
				}
				//添加地址
                // xsTan 追加结束 2009-1-28 排他时间
				arrList.add(info);
			}
			//返回PageObject
			result.setTotalCount(lngTotalCount);
			result.setList(arrList);
			LogUtil.log("EJB:订餐信息一览grid初始化结束。", Level.INFO, null);
			return result;
       }catch(Exception e){
		   LogUtil.log("EJB:订餐信息一览grid初始化失败。", Level.SEVERE, e);
		   throw  new SQLException();
	   }
	}
	/**
	 * 给点菜一览grid赋值查询开始
	 * @param rowStartIdxAndCount
	 */
	@SuppressWarnings("unchecked")
	public PageObject getIndividualSubMenuInfo(long id,String strEnterpriseCode,final int... rowStartIdxAndCount)throws SQLException{
		try{
			 LogUtil.log("EJB:订餐信息和点菜一览grid中显示查询开始" , Level.INFO, null);
			 PageObject result =  new PageObject();
			 List lstParams = new ArrayList();
			   lstParams.add(strEnterpriseCode);
			   lstParams.add(strEnterpriseCode);
			   lstParams.add(id);
			 //子个人订餐查询sql
			 String strSql = "SELECT "
				                   +" A.ID,"
				                   +" B.MENU_NAME, "
				                   +" C.MENUTYPE_NAME, "
				                   +" A.MENU_AMOUNT, "
				                   +" A.MENU_PRICE,"
				                   +" A.MENU_TOTAL,"
				                   +" A.MEMO, "
				                   +" TO_CHAR(A.UPDATE_TIME,'YYYY-MM-DD HH24:MI:SS'), "
				                   +" A.M_ID, "
				                   +" A.MENU_CODE "
				                   +" FROM "
				                   +" AD_J_USER_SUB A LEFT JOIN AD_C_MENU_WH B "
					  			   // xsTan 修改开始 2009-1-28 修改连接条件 删除子表是否使用
				                   +" ON A.MENU_CODE = B.MENU_CODE AND B.IS_USE = 'Y' AND B.ENTERPRISE_CODE = ? "
				                   +" LEFT JOIN AD_C_MENU_TYPE C"
				                   +" ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND C.IS_USE = 'Y' AND  C.ENTERPRISE_CODE = ?" 
					  			   // xsTan 修改结束 2009-1-28 修改连接条件
				                   +" WHERE A.M_ID = ?  AND A.IS_USE= 'Y'  ";
			//查询number
			 String strSqlCount ="SELECT COUNT(*) "
				                   +" FROM "
					               +" AD_J_USER_SUB A LEFT JOIN AD_C_MENU_WH B "
	  			   // xsTan 修改开始 2009-1-28 修改连接条件 删除子表是否使用
					               +" ON A.MENU_CODE = B.MENU_CODE AND B.IS_USE = 'Y' AND B.ENTERPRISE_CODE = ? "
					               +" LEFT JOIN AD_C_MENU_TYPE C"
                                   +" ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND C.IS_USE = 'Y' AND  C.ENTERPRISE_CODE = ?" 
	  			   // xsTan 修改结束 2009-1-28 修改连接条件
                                 +" WHERE A.M_ID = ?  AND A.IS_USE= 'Y'  ";
			 //查询条件
			Object[] params = lstParams.toArray();
			// log
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

			// 读取数据库记录数
			long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,
					params).toString());
			// 调用EJB获取IndividualSubMenuInfo实例
			List<IndividualSubMenuInfo> arrList = new ArrayList<IndividualSubMenuInfo>();
			List list = bll.queryByNativeSQL(strSql, params,
					rowStartIdxAndCount);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				// 转化记录
				Object[] data = (Object[]) it.next();
				IndividualSubMenuInfo info = new IndividualSubMenuInfo();
				if ((data[0] != null) && (!"".equals(data[0].toString()))) {
					info.setId(data[0].toString());
				}
				if ((data[1] != null) && (!"".equals(data[1].toString()))) {
					info.setMenuName(data[1].toString());
				}
				if ((data[2] != null) && (!"".equals(data[2].toString()))) {
					info.setMenuTypeName(data[2].toString());
				}
				if ((data[3] != null) && (!"".equals(data[3].toString()))) {
					info.setMenuAmount(data[3].toString());
				}
				if ((data[4] != null) && (!"".equals(data[4].toString()))) {
					info.setMenuPrice(data[4].toString());
				}
				if ((data[5] != null) && (!"".equals(data[5].toString()))) {
					info.setMenuTotal(data[5].toString());
				}
				if ((data[6] != null) && (!"".equals(data[6].toString()))) {
					info.setMemo(data[6].toString());
				}
				// xsTan 追加开始 2009-1-28 排他时间
				if ((data[7] != null) && (!"".equals(data[7].toString()))) {
					info.setStrUpdateTime(data[7].toString());
				}
				//追加mid
				if ((data[8] != null) && (!"".equals(data[8].toString()))) {
					info.setMId(data[8].toString());
				}
			    if((data[9] != null) && !data[9].equals("")){
			    	info.setMenuCode(data[9].toString());
			    }
				// xsTan 追加结束 2009-1-28 排他时间
				info.setFlag("0");
				arrList.add(info);
			}
			// 返回PageObject
			result.setTotalCount(lngTotalCount);
			result.setList(arrList);
			LogUtil.log("EJB:订餐信息和点菜一览grid中显示查询结束。", Level.INFO, null);
			return result;
		} catch (Exception e) {
			LogUtil.log("EJB:订餐信息和点菜一览grid中显示查询失败。", Level.SEVERE, e);
			throw new SQLException();
		}
		
	}
	
    /**
     * 菜谱选择画面
     * @param strDate  订餐信息中的订餐日期
     * @param strType 订餐信息中的用餐时间
     * @param rowStartIdxAndCount
     * @return
     */
    @SuppressWarnings("unchecked")
	public PageObject getIndividualMenuChooseInfo(String strDate,String strType,String strEnterpriseCode,final int ... rowStartIdxAndCount)throws  SQLException{
    	try{
    		LogUtil.log("EJB:弹出菜谱选择画面查询开始" , Level.INFO, null);
    		PageObject result =  new PageObject();
    		List lstParams = new ArrayList();
			lstParams.add(strEnterpriseCode);
			lstParams.add(strEnterpriseCode);
    		//个人菜谱选择查询sql
    		String strSql = "SELECT "
    			                  +" A.ID, "
    			                  +" A.MEMU_CODE,"
    			                  +" B.MENU_NAME, "
    			                  +" C.MENUTYPE_NAME, "
    			                  +" A.MENU_PRICE, "
    			                  +" A.MEMO "
    			                  +" FROM "
    			                  +" AD_J_RESTAURANT_PLAN A LEFT JOIN AD_C_MENU_WH  B "
    			                  +" ON A.MEMU_CODE = B.MENU_CODE AND B.ENTERPRISE_CODE = ? AND B.IS_USE = 'Y' "
    			                  +" LEFT JOIN AD_C_MENU_TYPE  C "
    			                  +" ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND C.IS_USE = 'Y' AND C.ENTERPRISE_CODE = ? ";
    		String strSqlCount = "SELECT COUNT(*) "
    			                  +" FROM "
                                  +" AD_J_RESTAURANT_PLAN A LEFT JOIN AD_C_MENU_WH  B "
                                  +" ON A.MEMU_CODE = B.MENU_CODE AND B.ENTERPRISE_CODE = ? AND B.IS_USE = 'Y' "
                                  +" LEFT JOIN AD_C_MENU_TYPE  C "
                                  +" ON B.MENUTYPE_CODE = C.MENUTYPE_CODE AND C.IS_USE = 'Y' AND C.ENTERPRISE_CODE = ? ";
    		String strSqlWhere = " WHERE A.IS_USE = ?"
    			+ " AND A.ENTERPRISE_CODE = ? ";
    		
    		lstParams.add("Y");
    		lstParams.add(strEnterpriseCode);
    		if((strDate != null)  && !strDate.equals("") )
    		{
    			strSqlWhere += " AND TO_CHAR(A.PLAN_DATE,'YYYY-MM-DD') = ?";
        		lstParams.add(strDate);
    		}
    		if((strType != null)  && !strType.equals("") )
    		{
    			strSqlWhere += " AND A.MENU_TYPE = ?";
        		lstParams.add(strType);
    		}
    		Object[] objParams = lstParams.toArray();
    		
         strSql += strSqlWhere;
         strSqlCount += strSqlWhere;
		//log
        LogUtil.log("EJB:弹出菜谱选择画面查询开始" , Level.INFO, null);
		LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
		
	    //读取数据库记录数
		long lngTotalCount = Long.parseLong(bll.getSingal(strSqlCount,objParams).toString());
		 //调用EJB获取IndividualSubMenuInfo实例 
		List<IndividualSubMenuInfo> arrList = new ArrayList<IndividualSubMenuInfo>();
		List list =bll.queryByNativeSQL(strSql, objParams, rowStartIdxAndCount);
		Iterator it = list.iterator();
	    while(it.hasNext()){
	    	   //转化记录
	    	   Object[] data =(Object[])it.next();
	    	   IndividualSubMenuInfo info = new IndividualSubMenuInfo();
			   if((data[0]!=null) && (!"".equals(data[0].toString()))){
				info.setId(data[0].toString());
			   }
			   if((data[1]!=null) && (!"".equals(data[1].toString()))){
				info.setMenuCode(data[1].toString());
			   }
			  if((data[2]!=null) && (!"".equals(data[2].toString()))){
				info.setMenuName(data[2].toString());
			  }
			  if((data[3]!=null) && (!"".equals(data[3].toString()))){
				info.setMenuTypeName(data[3].toString());
			   }
			   if((data[4]!=null) && (!"".equals(data[4].toString()))){
				info.setMenuPrice(data[4].toString());
			   }
			   if((data[5]!=null) && (!"".equals(data[5].toString()))){
				info.setMemo(data[5].toString());
			   }
			   arrList.add(info);
	        }
		   //返回PageObject
		   result.setTotalCount(lngTotalCount);
		   result.setList(arrList);
    	   LogUtil.log("EJB:弹出菜谱选择画面查询正常结束。", Level.INFO, null);
    	   return result;
    	}catch(Exception e){
			 LogUtil.log("EJB:弹出菜谱选择画面查询失败。", Level.SEVERE, e);
			 throw new SQLException();
    	}
    }
    /**
     *  逻辑删除个人用户点菜表信息
     */
    public void logicDeleteIndividualMenuInfo(String userId,String mId, String strOldUpdateTime)throws  SQLException, DataChangeException{
    	try{
    		LogUtil.log("EJB:菜谱选择明细行删除开始" , Level.INFO, null);
    		// xsTan 追加开始 2009-1-28 排他
    		// 取得DB最新的bean
    		AdJUserMenu objDB = adjUserMenu.findById(Long.parseLong(mId));
    		
    		// 取得DB最新的更新时间
    		String strNewUpdateTime = objDB.getUpdateTime().toString().substring(0, 19);
    		if (!strNewUpdateTime.equals(strOldUpdateTime))
    		{
    			throw new DataChangeException("");
    		}
    		objDB.setUpdateTime(new Date());
    		objDB.setUpdateUser(userId);
    		objDB.setIsUse("N");
    		adjUserMenu.update(objDB);
            
    		List lstParams = new ArrayList();
    		lstParams.add(userId);
    		lstParams.add(mId);
    		//更新用户点菜字表
    		String sql = "UPDATE AD_J_USER_SUB SET IS_USE ='N',UPDATE_USER = ? ,UPDATE_TIME= SYSDATE WHERE M_ID = ? ";
    		Object[] objParams = lstParams.toArray(); 
    		bll.exeNativeSQL(sql, objParams);
		} catch (DataChangeException de) {
			LogUtil.log("EJB:菜谱选择明细行删除异常", Level.SEVERE, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB:菜谱选择明细行删除异常", Level.SEVERE, e);
			throw new SQLException();
		}
    }
    /**
	 * @param strDate
	 *            日期
	 * @param strType
	 *            类型
	 * @param strEnterpriseCode
	 *            企业代码
	 * @param strWorkerCode
	 *            人员编码
	 * @return int
	 */
    public int checkDataRepeat(String strDate,String strType, String strEnterpriseCode, String strWorkerCode) throws SQLException{
    	try{
    		LogUtil.log("EJB:菜谱选择合法性开始", Level.INFO, null);
			// 用户菜谱信息删除sql
			String strSql = "SELECT COUNT(M_ID)  "
				          + " FROM  AD_J_USER_MENU  ";

			String strSqlWhere = " WHERE IS_USE= ? AND  TO_CHAR(MENU_DATE,'YYYY-MM-DD') = ?  " +
					"AND MENU_TYPE = ? AND ENTERPRISE_CODE=? AND INSERTBY = ? ";
			// 设置参数
			Object[] params = new Object[5];
			params[0] = "Y";
			params[1] = strDate;
			params[2] = strType;
			params[3] = strEnterpriseCode;
			params[4] = strWorkerCode;
			
			strSql += strSqlWhere;

			// log
			LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);
			// 返回删除行数
			int intNum1 = Integer.parseInt(bll.getSingal(strSql, params)
					.toString());

			if ((intNum1 <= 0)) {
				return 0;
			} else {
				return 1;
			}
		} catch (Exception e) {
			LogUtil.log("EJB:菜谱CHECK失败。", Level.SEVERE, e);
			throw new SQLException();
		}
    }
    
    /**
	 * 取得工作类型
	 */
    public String getWorkType(String userId,String strEnterpriseCode)throws SQLException{
    	try{
    		
    		// 用户菜谱信息删除sql
    		String strSql = "SELECT  B.WORK_KIND  "
    			                  + " FROM  HR_J_EMP_INFO  A " 
    			                  + " INNER JOIN  HR_C_STATION B "
    			                  + " ON A.STATION_ID=B.STATION_ID  "
    			                  + " AND B.IS_USE=? "
    			                  + " WHERE A.EMP_CODE = ? AND A.ENTERPRISE_CODE= ? " ;
    							// modify by liuyi 090909 11:15 B表中ENTERPRISE_CODE暂无
//    			                  " AND B.ENTERPRISE_CODE = ? ";
    			                  
    	   List  lstParams = new ArrayList();
    	   lstParams.add("Y"); //update by sychen 20100902
//    	   lstParams.add("U");
    	   lstParams.add(userId);
    	   lstParams.add(strEnterpriseCode);
    	   // modify by liuyi 090909 11：15 
//    	   lstParams.add(strEnterpriseCode);
    	   
    	   Object[] objParams=lstParams.toArray();
         //log
            LogUtil.log("EJB:菜谱CHECK开始" , Level.INFO, null);
	   		LogUtil.log("EJB:SQL=" + strSql, Level.INFO, null);

     	    //读取数据库记录数
		
		    //调用EJB获取IndividualSubMenuInfo实例 
	   		if(bll.getSingal(strSql,objParams) != null)
	   			return (String)bll.getSingal(strSql,objParams);
	   		else{
	   			return "";
	   		}
	            
	   		
	        
	    }catch(Exception e	){
			 LogUtil.log("EJB:菜谱CHECK失败。", Level.SEVERE, e);
			 throw new SQLException();
    	}
    }
    
    /**
	 * 插入用户点菜表和用户点菜子表
	 * @param objBean 用户点菜表实体
	 * @param lstSubList 用户点菜子表实体序列
	 * @throws SQLException
	 */
	public void addUserMenuAndSubUserMenu(AdJUserMenu objBean,
			List<AdJUserSub> lstSubList) throws SQLException {
		try {
			LogUtil.log("EJB:个人订餐DB操作开始", Level.INFO, null);
			LogUtil.log("EJB:插入用户点菜表开始", Level.INFO, null);
			// 取得最大ID
			long lngMId = bll.getMaxId("AD_J_USER_MENU ", "M_ID");
			objBean.setMId(lngMId);
			objBean.setUpdateTime(new Date());
			adjUserMenu.save(objBean);
			LogUtil.log("EJB:插入用户点菜表结束", Level.INFO, null);

			LogUtil.log("EJB:插入用户点菜子表开始", Level.INFO, null);
			long lngSubId = bll.getMaxId("AD_J_USER_SUB ", "ID");
			for (int i = 0; i < lstSubList.size(); i++) {
				AdJUserSub objSubBean = lstSubList.get(i);
				objSubBean.setUpdateTime(new Date());
				objSubBean.setMId(lngMId);
				objSubBean.setId(lngSubId);
				adjUserSub.save(objSubBean);
				lngSubId++;
			}
			LogUtil.log("EJB:插入用户点菜子表结束", Level.INFO, null);

			LogUtil.log("EJB:个人订餐DB操作结束", Level.INFO, null);
		} catch (Exception e) {
			LogUtil.log("EJB:个人订餐DB操作失败", Level.INFO, null);
			throw new SQLException();
		}
	}

	/**
	 * 更新用户点菜表和用户点菜子表
	 * @param objBean 用户点菜表实体
	 * @param lstSubListAdd 用户点菜子表实体追加序列
	 * @param lstSubListUpdate 用户点菜子表实体更新序列
	 * @throws SQLException
	 */
	public void updateUserMenuAndSubUserMenu(AdJUserMenu objBean,
			List<AdJUserSub> lstSubListAdd, List<AdJUserSub> lstSubListUpdate)
			throws SQLException, DataChangeException{
		try {
			LogUtil.log("EJB:个人订餐DB操作开始", Level.INFO, null);	
			
			LogUtil.log("EJB:更新用户点菜表开始", Level.INFO, null);
			Long lngMId = objBean.getMId();
			AdJUserMenu objUserMenu = adjUserMenu.findById(lngMId);
			// 排他处理
			String strNewDate = objUserMenu.getUpdateTime().toString().substring(0, 19);
			SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String dteDB = sdfFrom.format(objBean.getUpdateTime());
			String strOldDate = dteDB.toString();
			if (!strNewDate.equals(strOldDate)) {
				throw new DataChangeException("");
			}
			//订单状态
			objUserMenu.setMenuInfo(objBean.getMenuInfo());
			objUserMenu.setPlace(objBean.getPlace());
			objUserMenu.setUpdateTime(new Date());
			adjUserMenu.update(objUserMenu);
			LogUtil.log("EJB:更新用户点菜表结束", Level.INFO, null);

			LogUtil.log("EJB:插入用户点菜子表开始", Level.INFO, null);
			long lngSubId = bll.getMaxId("AD_J_USER_SUB ", "ID");
			for (int i = 0; i < lstSubListAdd.size(); i++) {
				AdJUserSub objSubBean = lstSubListAdd.get(i);
				objSubBean.setUpdateTime(new Date());
				objSubBean.setMId(lngMId);
				objSubBean.setId(lngSubId);
				adjUserSub.save(objSubBean);
				lngSubId++;
			}
			LogUtil.log("EJB:插入用户点菜子表结束", Level.INFO, null);

			LogUtil.log("EJB:更新用户点菜子表开始", Level.INFO, null);
			for (int i = 0; i < lstSubListUpdate.size(); i++) {
				AdJUserSub objSubBean = lstSubListUpdate.get(i);
				AdJUserSub objSubUpdate = adjUserSub.findById(objSubBean.getId());
				// 排他处理
				strNewDate = sdfFrom.format(objSubUpdate.getUpdateTime());
				strOldDate = sdfFrom.format(objSubBean.getUpdateTime());
				if (!strNewDate.equals(strOldDate)) {
					throw new DataChangeException("");
				}
				// 份数
				objSubUpdate.setMenuAmount(objSubBean.getMenuAmount());
				// 合计
				objSubUpdate.setMenuTotal(objSubBean.getMenuTotal());
				// 备注
				objSubUpdate.setMemo(objSubBean.getMemo());
				// 修改时间
				objSubUpdate.setUpdateTime(new Date());
				// 是否使用
				objSubUpdate.setIsUse(objSubBean.getIsUse());
				adjUserSub.update(objSubUpdate);
			}
			LogUtil.log("EJB:更新用户点菜子表结束", Level.INFO, null);

			LogUtil.log("EJB:个人订餐DB操作结束", Level.INFO, null);
		} catch (DataChangeException de) {
			LogUtil.log("EJB:个人订餐DB操作失败", Level.INFO, de);
			throw de;
		} catch (Exception e) {
			LogUtil.log("EJB:个人订餐DB操作失败", Level.INFO, e);
			throw new SQLException();
		}
	}
}
