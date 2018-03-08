/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.action.hr.ca;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrJVacationFacadeRemote;
import power.ejb.hr.ca.StaticDetailBean;
import power.web.birt.bean.hr.ca.StaticBean;
import power.web.birt.constant.commUtils;
import power.web.comm.AbstractAction;

/**
 * (请假、加班、运行班)统计报表Action
 * @author zhujie
 *
 */
public class StaticAction extends AbstractAction{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 远程 */
    private HrJVacationFacadeRemote remote;
    /** 员工姓名截断字节数 */
    private static final int TEN = 10;
    /** 部门名称每行显示字节数 */
    private static final int TWENTYFOUR = 24;
    /** 统计种类为请假 */
    private static final String TYPE_ATTENDENT = "1";
    /** 统计种类为加班 */
    private static final String TYPE_WORKOVERTIME = "2";
    /** 统计种类为运行班 */
    private static final String TYPE_WORKSHIFT = "3";
    /** 请假统计标题 */
    private static final String TITLE_ATTENDENT = "部门职工请假统计";
    /** 加班统计标题 */
    private static final String TITLE_WORKOVERTIME = "部门职工加班统计";
    /** 运行班统计标题 */
    private static final String TITLE_WORKSHIFT = "部门职工运行班统计";
    /** 报表画面只显示Table */
    private static final String SHOW_TABLE = "1";
    /** 方法名：setColumn */
    private static final String METHOD_SETCOLUMN = "setColumn";
    /** 列的长度 */
    private static final int COLUMN_LENGTH = 15;
    /** 列的长度 */
    private static final int COLUMN_NULL = 0;
    
    /**
     * 构造函数
     */
    public StaticAction() {
        remote = (HrJVacationFacadeRemote) factory.getFacadeRemote("HrJVacationFacade");
    }
    
    /**
      * 获取(请假、加班、运行班)统计报表
      * @param year 开始时间
      * @param month 结束时间
      * @param enterpriseCode 企业编码
      * @param type 统计种类
      * @return StaticBean
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
      */
    @SuppressWarnings("unchecked")
    public StaticBean getStatic(String year,String month,String enterpriseCode,String type) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
        StaticBean entity = new StaticBean();
        PageObject pg = new PageObject();
        List<StaticDetailBean> result = new ArrayList<StaticDetailBean>();
        // 调用EJB取得劳动合同台帐信息
        // 统计种类为请假时
        if(TYPE_ATTENDENT.equals(type)){
            pg = remote.getAttendentStaticDetailInfo(enterpriseCode, year, month);
        }
        // 统计种类为加班时
        if(TYPE_WORKOVERTIME.equals(type)){
            pg = remote.getWorkOvertimeStaticDetailInfo(enterpriseCode, year, month);
        }
        // 统计种类为运行班时
        if(TYPE_WORKSHIFT.equals(type)){
            pg = remote.getWorkShiftStaticDetailInfo(enterpriseCode, year, month);
        }
        if(pg!=null){
            result = (List<StaticDetailBean>)pg.getList();
        }
        // 对信息进行处理
        if(result!=null&&result.size()>0){
            StaticDetailBean bean = null;
            for(int i=0;i<result.size();i++){
                bean = result.get(i);
                // 对员工姓名进行截断前10个字节数处理
                if(bean.getChsName()!=null&&bean.getChsName().getBytes().length>TEN){
                    bean.setChsName(commUtils.cutByByte(bean.getChsName(), TEN));
                }
                // 部门名称进行换行处理
                String deptName = commUtils.addChangeLineByByteLengthForCA(bean.getDeptName(),
                        TWENTYFOUR);
                bean.setDeptName(deptName);
                // 上级部门名称进行换行处理
                String pdeptName = commUtils.addChangeLineByByteLengthForCA(bean.getPdeptName(),
                        TWENTYFOUR);
                bean.setPdeptName(pdeptName);
            }
            entity.setList(result);
        }else{
        	PageObject pobj = new PageObject();
        	List<String> lstType = new ArrayList<String>();
        	// 统计种类为请假时
        	if(TYPE_ATTENDENT.equals(type)){
        		pobj = remote.getVacationType(enterpriseCode);
            }
            // 统计种类为加班时
            if(TYPE_WORKOVERTIME.equals(type)){
            	pobj = remote.getWorkOvertimeType(enterpriseCode);
            }
            // 统计种类为运行班时
            if(TYPE_WORKSHIFT.equals(type)){
            	pobj = remote.getWorkShiftType(enterpriseCode);
            }
            if(pobj!=null){
            	lstType = (List<String>)pobj.getList();
            }
            if(lstType!=null&&lstType.size()>0){
            	entity.setUseFlag(SHOW_TABLE);
            	Class beanClass =  StaticBean.class;
            	entity.setColumnCount(lstType.size());
                Method method;
                for(int i = 0; i < Math.min(lstType.size(),COLUMN_LENGTH); i++){
                	method =beanClass.getMethod(METHOD_SETCOLUMN + (i + 1), String.class);
                	method.invoke(entity, lstType.get(i));
                }
            }else{
            	entity.setUseFlag(SHOW_TABLE);
            	entity.setColumnCount(COLUMN_NULL);
            }
            
        }
        // 统计种类为请假时设置相应标题
        if(TYPE_ATTENDENT.equals(type)){
            entity.setTitle(TITLE_ATTENDENT);
        }
        // 统计种类为加班时设置相应标题
        if(TYPE_WORKOVERTIME.equals(type)){
            entity.setTitle(TITLE_WORKOVERTIME);
        }
        // 统计种类为运行班时设置相应标题
        if(TYPE_WORKSHIFT.equals(type)){
            entity.setTitle(TITLE_WORKSHIFT);
        }
        return entity;
    }
}
