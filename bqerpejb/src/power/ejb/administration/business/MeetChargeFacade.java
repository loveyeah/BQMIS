/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.DataFormatException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.administration.AdJMeet;
import power.ejb.administration.AdJMeetFacadeRemote;
import power.ejb.administration.AdJMeetMx;
import power.ejb.administration.AdJMeetMxFacadeRemote;
import power.ejb.administration.AdJMeetfile;
import power.ejb.administration.AdJMeetfileFacadeRemote;
import power.ejb.administration.form.MeetChargeInfo;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * 会务费用管理.
 * 
 * @author wangyun
 */
@Stateless
public class MeetChargeFacade implements MeetChargeFacadeRemote {
	/** 日期形式字符串 DATE_FORMAT_YYYYMMDD_HHMM */
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	/** 会议附件信息表接口 */
	@EJB(beanName = "AdJMeetfileFacade")
	protected AdJMeetfileFacadeRemote adjMeetFileFacadeRemote;
	/** 会议审批单表接口 */
	@EJB(beanName = "AdJMeetFacade")
	protected AdJMeetFacadeRemote adjMeetFacadeRemote;
	/** 会议审批费用表接口 */
	@EJB(beanName = "AdJMeetMxFacade")
	protected AdJMeetMxFacadeRemote adjMeetMxFacadeRemote;
	/** IS_USE为Y */
	private static final String IS_USE_Y = "Y";
	/** 单据状态 ： 2(已终结) */
	private static final String DCM_STATE_FINISH = "2";

	/**
	 * 会务费用管理概况一览
	 * 
	 * @param strWorkCode
	 *            登陆用户ID
	 * @param argEnterpriseCode
	 * 			  企业编码
	 * @param rowStartIdxAndCount
	 *            分页
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject getMeetChargeGeneral(String strWorkCode,
			String argEnterpriseCode, int... rowStartIdxAndCount)
			throws SQLException {
		try {
			PageObject pobj = new PageObject();
			String strSql = "";
			strSql = "SELECT "
					+ "A.MEET_ID, "
					+ "C.CHS_NAME, "
					+ "B.DEPT_NAME, "
					+ "A.MEET_NAME, "
					+ "TO_CHAR(A.STARTMEET_DATE, 'yyyy-mm-dd hh24:mi'), "
					+ "TO_CHAR(A.ENDMEET_DATE, 'yyyy-mm-dd hh24:mi'), "
					+ "A.MEET_PLACE, "
					+ "A.ROOM_NEED, "
					+ "A.CIG_NAME, "
					+ "A.CIG_PRICE, "
					+ "A.CIG_NUM, "
					+ "A.WINE_NAME, "
					+ "A.WINE_PRICE, "
					+ "A.WINE_NUM, "
					+ "A.TF_NUM, "
					+ "A.TF_THING, "
					+ "A.DJ_NUM, "
					+ "A.DJ_THING, "
					+ "A.BJ_NUM, " 
					+ "A.BJ_THING, "
					+ "TO_CHAR(A.DINNER_TIME, 'yyyy-mm-dd hh24:mi'), "
					+ "A.DINNER_NUM, "
					+ "A.DINNER_BZ, " 
					+ "A.BUDPAY_INALL, "
					+ "A.REALPAY_INALL, " 
					+ "A.MEET_OTHER, "
					+ "TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss') "
					+ "FROM AD_J_MEET A "
					+ "LEFT JOIN "
					+ "HR_J_EMP_INFO C "
					+ "ON A.APPLY_MAN = C.EMP_CODE "
					+ "AND C.ENTERPRISE_CODE = ? "
					+ "LEFT JOIN "
					+ "HR_C_DEPT B "
					+ "ON C.DEPT_ID = B.DEPT_ID "
					+ "AND B.ENTERPRISE_CODE = ? "
					+ "WHERE "
					+ "A.APPLY_MAN = ? "
					+ "AND A.DCM_STATUS = ? "
					+ "AND A.IS_USE = ? "
					+ "AND A.ENTERPRISE_CODE = ?";
			String strSqlCount = "SELECT "
				+ "COUNT(A.MEET_ID) "
				+ "FROM AD_J_MEET A "
				+ "LEFT JOIN "
				+ "HR_J_EMP_INFO C "
				+ "ON A.APPLY_MAN = C.EMP_CODE "
				+ "AND C.ENTERPRISE_CODE = ? "
				+ "LEFT JOIN "
				+ "HR_C_DEPT B "
				+ "ON C.DEPT_ID = B.DEPT_ID "
				+ "AND B.ENTERPRISE_CODE = ? "
				+ "WHERE "
				+ "A.APPLY_MAN = ? "
				+ "AND A.DCM_STATUS = ? "
				+ "AND A.IS_USE = ? "
				+ "AND A.ENTERPRISE_CODE = ?";
			Object[] objParams = new Object[6];
			objParams[0] = argEnterpriseCode;
			objParams[1] = argEnterpriseCode;
			objParams[2] = strWorkCode;
			objParams[3] = DCM_STATE_FINISH;
			objParams[4] = IS_USE_Y;
			objParams[5] = argEnterpriseCode;
			
			LogUtil.log("EJB:取得会务费用管理一览开始。SQL=" + strSql, Level.INFO, null);
			Long totalCount = Long
				.parseLong(bll.getSingal(strSqlCount, objParams).toString());
			List list = bll.queryByNativeSQL(strSql, objParams, rowStartIdxAndCount);
			List<MeetChargeInfo> arrlist = new ArrayList<MeetChargeInfo>();
			Iterator it = list.iterator();
			while(it.hasNext()) {
				MeetChargeInfo meetChargeInfo = new MeetChargeInfo();
				Object[] data = (Object[]) it.next();
				// 会议申请单号
				if (null != data[0]) {
					meetChargeInfo.setMeetId(data[0].toString());
				}
				// 姓名
				if (null != data[1]) {
					meetChargeInfo.setName(data[1].toString());
				}
				// 申请部门
				if(null != data[2]) {
					meetChargeInfo.setDepName(data[2].toString());
				}
				// 会议名称
				if (null != data[3]) {
					meetChargeInfo.setMeetName(data[3].toString());
				}
				// 会议开始时间
				if (null != data[4]) {
					meetChargeInfo.setStartMeetDate(data[4].toString());
				}
				// 会议结束时间
				if (null != data[5]) {
					meetChargeInfo.setEndMeetDate(data[5].toString());
				}
				// 会议地点
				if (null != data[6]) {
					meetChargeInfo.setMeetPlace(data[6].toString());
				}
				// 会场要求
				if (null != data[7]) {
					meetChargeInfo.setRoomNeed(data[7].toString());
				}
				// 会议用烟名称
				if (null != data[8]) {
					meetChargeInfo.setCigName(data[8].toString());
				}
				// 会议用烟价格
				if (null != data[9]) {
					meetChargeInfo.setCigPrice(Double.parseDouble(data[9].toString()));
				}
				// 会议用烟数量
				if (null != data[10]) {
					meetChargeInfo.setCigNum(Long.parseLong(data[10].toString()));
				}
				// 会议用酒名称
				if (null != data[11]) {
					meetChargeInfo.setWineName(data[11].toString());
				}
				// 会议用酒价格
				if (null != data[12]) {
					meetChargeInfo.setWinePrice(Double.parseDouble(data[12].toString()));
				}
				// 会议用酒数量
				if (null != data[13]) {
					meetChargeInfo.setWineNum(Long.parseLong(data[13].toString()));
				}
				// 会议住宿-套房数量
				if (null != data[14]) {
					meetChargeInfo.setTfNum(Long.parseLong(data[14].toString()));
				}
				// 会议住宿-套房用品
				if (null != data[15]) {
					meetChargeInfo.setTfThing(data[15].toString());
				}
				// 会议住宿-单间数量
				if (null != data[16]) {
					meetChargeInfo.setDjNum(Long.parseLong(data[16].toString()));
				}
				// 会议住宿-单间用品
				if (null != data[17]) {
					meetChargeInfo.setDjThing(data[17].toString());
				}
				// 会议住宿-标间数量
				if (null != data[18]) {
					meetChargeInfo.setBjNum(Long.parseLong(data[18].toString()));
				}
				// 会议住宿-标间用品
				if (null != data[19]) {
					meetChargeInfo.setBjThing(data[19].toString());
				}
				// 就餐时间
				if (null != data[20]) {
					meetChargeInfo.setDinnerTime(data[20].toString());
				}
				// 就餐人数
				if (null != data[21]) {
					meetChargeInfo.setDinnerNum(Long.parseLong(data[21].toString()));
				}
				// 用餐标准
				if (null != data[22]) {
					meetChargeInfo.setDinnerBz(Double.parseDouble(data[22].toString()));
				}
				// 预计费用汇总
				if (null != data[23]) {
					meetChargeInfo.setBudpayInall(Double.parseDouble(data[23].toString()));
				}
				// 实际费用汇总
				if (null != data[24]) {
					meetChargeInfo.setRealpayInall(Double.parseDouble(data[24].toString()));
				}
				// 会议其他要求
				if (null != data[25]) {
					meetChargeInfo.setMeetOther(data[25].toString());
				}
				// 修改时间
				if (null != data[26]) {
					meetChargeInfo.setMeetUpdateTime(data[26].toString());
				}
				arrlist.add(meetChargeInfo);
			}
			if (arrlist.size() > 0) {
				pobj.setList(arrlist);
				pobj.setTotalCount(totalCount);
			}
			LogUtil.log("EJB:取得会务费用管理一览结束。", Level.INFO, null);
			return pobj;
		} catch (Exception e) {
			LogUtil.log("EJB:取得会务费用管理一览失败。", Level.SEVERE, e);
			throw new SQLException();
		}
	}
	
	/**
	 * 查询会务附件信息
	 * 
	 * @param meetId 会议申请单号
	 * @param rowStartIdxAndCount 分页
	 * 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMeetFile(String meetId) throws SQLException {
		try {
			PageObject pobj = new PageObject();
			// 查询sql
        	String strSql = "SELECT "
        		+ "A.ID, "
        		+ "A.FILE_NAME, "
        		+ "TO_CHAR(A.UPDATE_TIME, 'yyyy-mm-dd hh24:mi:ss') "
        		+ "FROM "
        		+ "AD_J_MEETFILE A "
        		+ "WHERE "
        		+ "A.MEET_ID = ? AND "
        		+ "A.IS_USE = ?";
        	
        	Object[] objParams = new Object[2];
        	objParams[0] = meetId;
        	objParams[1] = IS_USE_Y;
        	LogUtil.log("EJB:查询会务附件信息开始。SQL=" + strSql, Level.INFO, null);
        	// 查询
			List list = bll.queryByNativeSQL(strSql, objParams);
        	List<MeetChargeInfo> arrlist = new ArrayList<MeetChargeInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) {
            	MeetChargeInfo info = new MeetChargeInfo();
            	Object[] data = (Object[]) it.next();
            	// 序号
            	if(null != data[0]) {
            		info.setId(Long.parseLong(data[0].toString()));
            	}
            	// 附件名称
            	if(null != data[1]) {
            		info.setFileName(data[1].toString());
            	}
            	// 修改时间
            	if(null != data[2]) {
            		info.setMeetFileUpdateTime(data[2].toString());
            	}
            	arrlist.add(info);
            }
            pobj.setList(arrlist);
            pobj.setTotalCount((long)list.size());
			// Log结束
    		LogUtil.log("EJB:查询会务附件信息结束。", Level.INFO, null);
        	return pobj;
		} catch (Exception e) {
        	LogUtil.log("EJB:查询会务附件信息失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	
		
	}

	/**
	 * 根据ID查询会议附件信息
	 * 
	 * @param id 序号
	 * 
	 * @return 会议附件信息
	 * 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findMeetFileById(Long id) throws SQLException {

		try {
			PageObject pobj = new PageObject();
			// 查询sql
        	String strSql = "SELECT " 
        		+ "A.ID, " 
        		+ "A.MEET_ID, "
        		+ "A.FILE_TYPE, " 
        		+ "A.FILE_KIND, " 
        		+ "A.FILE_NAME, " 
        		+ "A.FILE_TEXT, " 
        		+ "A.IS_USE, " 
        		+ "A.UPDATE_USER, " 
        		+ "A.UPDATE_TIME " 
        		+ "FROM " 
        		+ "AD_J_MEETFILE A " 
        		+ "WHERE " 
        		+ "A.ID = ? AND " 
        		+ "A.IS_USE = ?";	
			
        	Object[] objParams = new Object[2];
        	objParams[0] = id;
        	objParams[1] = IS_USE_Y;
        	LogUtil.log("EJB:查询会议附件信息开始。SQL=" + strSql, Level.INFO, null);
        	// 查询
        	List list = bll.queryByNativeSQL(strSql, objParams, AdJMeetfile.class);
            if(list == null) {
            	pobj.setList(new ArrayList());
            	pobj.setTotalCount((long)0);
            } else {
            	pobj.setList(list);
            	pobj.setTotalCount((long)(list.size()));
            }
			// Log结束
    		LogUtil.log("EJB:查询会议附件信息结束。", Level.INFO, null);
        	return pobj;
		} catch (Exception e) {
        	LogUtil.log("EJB:查询会议附件信息失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
	
	/**
	 * 费用明细数据检索
	 * 
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public PageObject findChargeDetail(String meetId) throws SQLException {
		try {
			PageObject pobj = new PageObject();
			// 查询sql
        	String strSql = "SELECT "
        		+ "A.ID, "
        		+ "A.MEET_ID, "
        		+ "A.PAY_NAME, "
        		+ "A.PAY_BUDGET, "
        		+ "A.PAY_REAL, "
        		+ "A.NOTE, "
        		+ "A.IS_USE, "
        		+ "A.UPDATE_USER, "
        		+ "A.UPDATE_TIME "
        		+ "FROM "
        		+ "AD_J_MEET_MX A "
        		+ "WHERE "
        		+ "A.MEET_ID = ? AND "
        		+ "A.IS_USE = ?";
        	Object[] objParams = new Object[2];
        	objParams[0] = meetId;
        	objParams[1] = IS_USE_Y;
        	LogUtil.log("EJB:费用明细数据检索开始。SQL=" + strSql, Level.INFO, null);
        	// 查询
        	List list = bll.queryByNativeSQL(strSql, objParams, AdJMeetMx.class);
            if(list == null) {
            	pobj.setList(new ArrayList());
            	pobj.setTotalCount((long)0);
            } else {
            	pobj.setList(list);
            	pobj.setTotalCount(Long.parseLong(list.size() + ""));
            }
			// Log结束
    		LogUtil.log("EJB:费用明细数据检索结束。", Level.INFO, null);
        	return pobj;
		} catch (Exception e) {
        	LogUtil.log("EJB:费用明细数据检索失败。", Level.SEVERE, e);
        	throw new SQLException();
        }
	}
	
	/**
	 * 删除操作
	 * 
	 * @param entity AdJMeetfile
	 * @param meetFileUpdateTime 修改时间
	 * @param strWorkCode 修改人
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public void deleteMeetFile(AdJMeetfile entity, 
			String meetFileUpdateTime, String strWorkCode)
			throws SQLException, DataChangeException, ParseException{
		try {
			SimpleDateFormat sdfFrom = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			Date dtNowTime = sdfFrom.parse(meetFileUpdateTime);
			entity.setUpdateTime(dtNowTime);
			LogUtil.log("EJB:删除会议附件信息表开始。", Level.INFO, null);
			entity.setIsUse("N");
			entity.setUpdateUser(strWorkCode);
			adjMeetFileFacadeRemote.update(entity);
			LogUtil.log("EJB:删除会议附件信息表结束。", Level.INFO, null);
		} catch (DataChangeException e) {
			LogUtil.log("EJB:删除会议附件信息表失败。", Level.SEVERE, null);
			throw e;
		} catch (ParseException e) {
			LogUtil.log("EJB:删除会议附件信息表失败。", Level.SEVERE, null);
			throw e;
		} catch (RuntimeException e) {
			LogUtil.log("EJB:删除会议附件信息表失败。", Level.SEVERE, null);
			throw e;
		}
	}
	
	/**
	 * 保存会议费用信息
	 * @param lstUpdateADJMeet
	 * @param lstUpdateADJMeetMx
	 * @param lstSaveADJMeetMx
	 * @param lstDeleteADJMeetMx
	 * @param meetUpdateTime
	 * @throws DataChangeException 
	 * @throws DataFormatException 
	 * @throws SQLException 
	 */
	public void saveMeet(List<AdJMeet> lstUpdateADJMeet,
			List<AdJMeetMx> lstUpdateADJMeetMx,
			List<AdJMeetMx> lstSaveADJMeetMx,
			List<AdJMeetMx> lstDeleteADJMeetMx, String meetUpdateTime)
			throws DataChangeException, DataFormatException, SQLException {
			// 更新会议审批单表
			if (lstUpdateADJMeet.size() > 0) {
				AdJMeet entity = lstUpdateADJMeet.get(0);
				String strNowTime = entity.getUpdateTime().toString();
				if(!strNowTime.substring(0, 19).equals(meetUpdateTime)) {
					throw new DataChangeException(null);
				} else {
					LogUtil.log("EJB:更新会议审批单表开始。", Level.INFO, null);
					adjMeetFacadeRemote.update(entity);
					LogUtil.log("EJB:更新会议审批单表结束。", Level.INFO, null);
				}
			}
			// 删除会议审批费用表数据
			if (lstDeleteADJMeetMx.size() > 0) {
				for (int i = 0; i < lstDeleteADJMeetMx.size(); i++) {
					AdJMeetMx entity = adjMeetMxFacadeRemote
						.findById(lstDeleteADJMeetMx.get(i).getId());

					Date dtNowTime = entity.getUpdateTime();
					String strNowTime = dtNowTime.toString();
					Date dtFrontTime = lstDeleteADJMeetMx.get(i)
						.getUpdateTime();
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
					String strFrontTime = dateFormat.format(dtFrontTime);
					if (!strNowTime.substring(0, 19)
							.equals(strFrontTime)) {
						throw new DataChangeException(null);
					} else {
						LogUtil.log("EJB:删除会议审批费用表数据开始。", Level.INFO, null);
						lstDeleteADJMeetMx.get(i).setUpdateTime(new java.util.Date());
						adjMeetMxFacadeRemote.update(lstDeleteADJMeetMx.get(i));
						LogUtil.log("EJB:删除会议审批费用表数据结束。", Level.INFO, null);
					}
				}
			}
			// 插入会议接待审批费用表数据
			if (lstSaveADJMeetMx.size() > 0) {
				Long id = bll.getMaxId("AD_J_MEET_MX", "ID");
				for (int i = 0; i < lstSaveADJMeetMx.size(); i++) {
					AdJMeetMx entity = lstSaveADJMeetMx.get(i);
					entity.setId(id++);
					LogUtil.log("EJB:插入会议接待审批费用表数据开始。", Level.INFO, null);
					entity.setUpdateTime(new java.util.Date());
					adjMeetMxFacadeRemote.save(entity);
					LogUtil.log("EJB:插入会议接待审批费用表数据结束。", Level.INFO, null);
				}
			}
			// 更新会议审批费用表数据
			if (lstUpdateADJMeetMx.size() > 0) {
				for (int i = 0; i < lstUpdateADJMeetMx.size(); i++) {
					AdJMeetMx entity = adjMeetMxFacadeRemote
						.findById(lstUpdateADJMeetMx.get(i).getId());
					Date dtNowTime = entity.getUpdateTime();
					String strNowTime = dtNowTime.toString();
					Date dtFrontTime = lstUpdateADJMeetMx.get(i)
						.getUpdateTime();
					SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
					String strFrontTime = dateFormat.format(dtFrontTime);
					if (!strNowTime.substring(0, 19)
							.equals(strFrontTime)) {
						throw new DataChangeException(null);
					} else {
						LogUtil.log("EJB:更新会议审批费用表数据开始。", Level.INFO, null);
						lstUpdateADJMeetMx.get(i).setUpdateTime(new java.util.Date());
						adjMeetMxFacadeRemote.update(lstUpdateADJMeetMx.get(i));
						LogUtil.log("EJB:更新会议审批费用表数据结束。", Level.INFO, null);
					}
				}
			}
		}
}
