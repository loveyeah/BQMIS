/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.action.hr;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import power.ejb.hr.HrJEducationBean;
import power.ejb.hr.HrJEducationFacadeRemote;
import power.ejb.hr.HrJEmpInfoBean;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJFamilyMemberFacadeRemote;
import power.ejb.hr.HrJFamilymemberBean;
import power.ejb.hr.HrJWorkresume;
import power.ejb.hr.HrJWorkresumeFacadeRemote;
import power.web.birt.bean.hr.EmployeeRecordBean;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * 职工履历表Action
 * @author wangjunjie
 */
public class EmployeeRecordAction extends AbstractAction {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 人员基本信息表远程接口 Remote */
    private HrJEmpInfoFacadeRemote detailRemote;
    /** 工作简历登记列表远程接口 Remote */
    private HrJWorkresumeFacadeRemote workresumeRemote;
    /** 学历教育登记列表远程接口 Remote */
    private HrJEducationFacadeRemote educationRemote;
    /** 社会关系登记列表远程接口 Remote */
    private HrJFamilyMemberFacadeRemote familymemberRemote;
    /** 婚否状况 (已婚) */
    public static final String IS_WEDDED_MARRIED = "0";
    public static final String STATUS_WEDDED_MARRIED = "已婚";
    /** 婚否状况 (未婚) */
    public static final String IS_WEDDED_UNMARRIED = "1";
    public static final String STATUS_WEDDED_UNMARRIED = "未婚";
    /** 婚否状况 (离异) */
    public static final String IS_WEDDED_DIVORCE = "2";
    public static final String STATUS_WEDDED_DIVORCE = "离异";
    /** 婚否状况 (丧偶) */
    public static final String IS_WEDDED_BEREFT = "3";
    public static final String STATUS_WEDDED_BEREFT = "丧偶";
    /** 性别 (男) */
    public static final String MALE_CODE = "M";
    public static final String MALE = "男";
    /** 性别 (女) */
    public static final String FEMALE_CODE = "W";
    public static final String FEMALE = "女";
    /** 籍贯栏 学历栏 */
    public static final int NUM_14 = 14;    
    /** 员工姓名栏 */
    public static final int NUM_18 = 18;    
    /** 政治面貌栏 */
    public static final int NUM_46 = 46;
    /** 学习专业栏 */
    public static final int NUM_30 = 30;
    /** 称谓栏 */
    public static final int NUM_16 = 16;
    /** 家庭住址栏 */
    public static final int NUM_80 = 80;

    /**
     * 构造函数
     */
    public EmployeeRecordAction() {
        detailRemote = (HrJEmpInfoFacadeRemote) factory
                .getFacadeRemote("HrJEmpInfoFacade");
        workresumeRemote = (HrJWorkresumeFacadeRemote) factory
                .getFacadeRemote("HrJWorkresumeFacade");
        educationRemote = (HrJEducationFacadeRemote) factory
                .getFacadeRemote("HrJEducationFacade");
        familymemberRemote = (HrJFamilyMemberFacadeRemote) factory
                .getFacadeRemote("HrJFamilyMemberFacade");
    }

    /**
     * 取得供应商信息
     * 
     * @param strEmpId
     *            人员ID
     * @param enterpriseCode
     *            企业编码
     * @return EmployeeRecordBean 职工履历表信息
     * @throws java.text.ParseException
     */
    @SuppressWarnings("unchecked")
    public EmployeeRecordBean getEmpInfo(String strEmpId,
            String enterpriseCode)  {
        EmployeeRecordBean entity = new EmployeeRecordBean(); 
        if (strEmpId != null && enterpriseCode != null) {
            //参数人员ID转化为检索用类型
             try {
                 entity.setEmpId(Long.parseLong(strEmpId));
                 
             } catch(RuntimeException pe) {
                 entity.setEmpId(new Long(-1));
             }
//             HrJEmpInfoBean empInfo = detailRemote.getEmpMaintBaseInfo(
//                     entity.getEmpId(), enterpriseCode);
             HrJEmpInfoBean empInfo = detailRemote.getEmpMaintBaseInfo(
                     entity.getEmpId(), enterpriseCode).get(0);
             List<HrJEducationBean> educationList =educationRemote.getEducationInfo(
                         entity.getEmpId(), enterpriseCode).getList();
             HrJEducationBean oriEducationInfo=educationRemote.getOriEducationInfo(
                        entity.getEmpId(), enterpriseCode);
             List<HrJWorkresume> workresumeList =workresumeRemote.getWorksumeInfo(
                      entity.getEmpId(), enterpriseCode).getList();
             List<HrJFamilymemberBean> familymemberList =familymemberRemote.getFamilyMemberInfo(
                       entity.getEmpId(), enterpriseCode).getList();  
             if(workresumeList!=null){
	             for(int i=0;i<workresumeList.size();i++){
	            	 workresumeList.get(i).setUnit(commUtils.cutByByte(workresumeList.get(i).getUnit(),NUM_30));
	            	 workresumeList.get(i).setHeadshipName(commUtils.cutByByte(workresumeList.get(i).getHeadshipName(),NUM_30));
	            	 
	             }
             }
             if(educationList!=null){
	             for(int i=0;i<educationList.size();i++){
	            	 educationList.get(i).setEducationName(commUtils.cutByByte(educationList.get(i).getEducationName(),NUM_14));
	            	 educationList.get(i).setSchoolName(commUtils.cutByByte(educationList.get(i).getSchoolName(),NUM_30));
	            	 educationList.get(i).setSpecialtyName(commUtils.cutByByte(educationList.get(i).getSpecialtyName(),NUM_30));
	             }
             }
             if(familymemberList!=null){
	             for(int i=0;i<familymemberList.size();i++){
	            	 familymemberList.get(i).setCallsName(commUtils.cutByByte(familymemberList.get(i).getCallsName(),NUM_16));
	            	 familymemberList.get(i).setUnit(commUtils.cutByByte(familymemberList.get(i).getUnit(),NUM_30));
	            	 familymemberList.get(i).setHeadshipName(commUtils.cutByByte(familymemberList.get(i).getHeadshipName(),NUM_30));
	             }
             }
            if (empInfo != null) {
                // 姓名
                entity.setName(commUtils.cutByByte(empInfo.getChsName(),NUM_18));
                // 英文名
                entity.setNameEn(commUtils.cutByByte(empInfo.getEnName(),NUM_18));
                // 性别
                entity.setSex(getSexChar(empInfo.getSex()));
                // 出生年月
                entity.setBirthday(empInfo.getBrithday());
                // 民族
                entity.setNation(commUtils.cutByByte(empInfo.getNationName(),NUM_18));
                // 籍贯
                entity.setHome(commUtils.cutByByte(empInfo.getNativePlaceName(),NUM_14));
                // 婚姻状况
                entity.setMarryStatus(this.getWeddedStatus(empInfo.getIsWedded()));
                // 身份证号码
                entity.setIdNum(empInfo.getIdentityCard());
                // 参加工作时间
                entity.setFirstJobTime(empInfo.getWorkDate());
                // 入本企业时间
                entity.setToHereTime(empInfo.getMissionDate());
                // 政治面貌
                entity.setPolitics(commUtils.cutByByte(empInfo.getPoliticsName(),NUM_46));                
                // 技术职称
                entity.setTechnologyPost(commUtils.cutByByte(empInfo.getTechnologyTitlesName(),NUM_46));
                if (oriEducationInfo != null) {
                    // 原始学历
                    entity.setOriginalEdu(commUtils.cutByByte(oriEducationInfo.getEducationName(),NUM_18));
                    // 原始所学专业
                    entity.setOriginalSpecial(commUtils.cutByByte(oriEducationInfo
                            .getSpecialtyName(),NUM_30));
                    // 原始毕业时间
                    entity.setOriGraduateTime(oriEducationInfo
                            .getGraduateDate());
                }
                // 当前学历
                entity.setNowEdu(commUtils.cutByByte(empInfo.getEducationName(),NUM_18));
                // 所学专业
                entity.setNowSpecial(commUtils.cutByByte(empInfo.getSpecialtyName(),NUM_30));
                // 毕业时间
                entity.setNowGraduateTime(empInfo.getGraduateDate());
                // 家庭住址
                entity.setHomeAdds(commUtils.cutByByte(empInfo.getFamilyAddress(),NUM_80));
                // 学历教育登记列表
                entity.setEducationList(educationList);
                // 工作简历登记列表
                entity.setWorkresumeList(workresumeList);
                // 社会关系登记列表
                entity.setFamilymemberList(familymemberList);
            }
        }
        return entity;
    }

    /**
     * 取得当前时间
     * @return String 当前时间
     */ 
    public String getNowDate() {
        String strDate = "";
        // 设置当前日期
        SimpleDateFormat mFormatTimeOnly = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        Date dDate = new Date();
        strDate =commUtils.formatTime(mFormatTimeOnly.format(dDate));
        return strDate;
    }
    
   
    
    /**
     * 取得婚姻状况
     * 
     * @param IS_WEDDED_CODE
     *            婚姻状况
     * @return String 婚姻状况
     */
    public String getWeddedStatus(String strWeddedCode) {
        String strWeddedStatus = "";
        if(IS_WEDDED_MARRIED.equals(strWeddedCode)){
            strWeddedStatus=STATUS_WEDDED_MARRIED;
        }
        else if(IS_WEDDED_UNMARRIED.equals(strWeddedCode)){
            strWeddedStatus=STATUS_WEDDED_UNMARRIED;
        }
        else if(IS_WEDDED_DIVORCE.equals(strWeddedCode)){
            strWeddedStatus=STATUS_WEDDED_DIVORCE;
        }
        else if(IS_WEDDED_BEREFT.equals(strWeddedCode)){
            strWeddedStatus=STATUS_WEDDED_BEREFT;
        }        
        return strWeddedStatus;
    }
    /**
     * 取得性别
     * 
     * @param IS_WEDDED_CODE
     *            性别code
     * @return String 性别
     */
    public String getSexChar(String strSexCode) {
        String strSex = "";
        if(MALE_CODE.equals(strSexCode)){
        	strSex=MALE;
        }
        else if(FEMALE_CODE.equals(strSexCode)){
        	strSex=FEMALE;
        }     
        return strSex;
    }

   
}
