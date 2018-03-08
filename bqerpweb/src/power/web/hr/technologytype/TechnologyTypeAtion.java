package power.web.hr.technologytype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.naming.NamingException;

import power.web.comm.AbstractAction;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCTechnologyTitlesType;
import power.ejb.hr.HrCTechnologyTitlesTypeFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.web.comm.ListRange;

/**
 * @author ssgu 设置技术职称类别
 */
public class TechnologyTypeAtion extends AbstractAction{
	private String method;
	private String start;
	private String limit;
	private String ttypeids;
	private HrCTechnologyTitlesType newtt;
	


	

	/**
	 * 对技术职称类别信息操作
	 * 
	 * @throws IOException
	 * @throws NamingException
	 */
	public void getALLTechnologyTypeInfo() throws JSONException, IOException {
		try {
			if ("get".equals(method)) {
				getAllTechnologyTypeList();
			} else if ("update".equals(method)) {
				UpdateTechnologyType();
			} else if ("del".equals(method)) {
				DelTechnologyType();
			} else if ("insert".equals(method)) {
				InsertTechnologyType();
			} 
			else if ("form".equals(method)) {
				getTechnologyTypeInfo();
				
			}else if ("getcode".equals(method)){
				getRetrieveCodeInfo();
			}
		} catch (Exception e) {
			ListRange lRange = new ListRange();
			lRange.setSuccess(false);
			lRange.setMessage(e.getMessage());
			String jsonstr = JSONUtil.serialize(lRange);
			write(jsonstr);
		}
	}

	public void getIsUseOfTT()
	{
		write("{total:3,root:[{'id':'Y','name':'使用'},{'id':'N','name':'停用'}]}"); //update by sychen 20100902
//		write("{total:3,root:[{'id':'U','name':'使用'},{'id':'N','name':'停用'}]}");
	}
	
	/**
	 * 获得有效技术职称类别列表信息
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public void getAllTechnologyTypeList()throws JSONException, IOException {
		ListRange<HrCTechnologyTitlesType> lTtype= new ListRange<HrCTechnologyTitlesType>();
		try {
              
			int[] obj = { Integer.parseInt(start), Integer.parseInt(limit)};
			HrCTechnologyTitlesTypeFacadeRemote bll = (HrCTechnologyTitlesTypeFacadeRemote) Ejb3Factory
					.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
			String strWhere = " model.isUse<>:param ";
			Object param="D";
			List<HrCTechnologyTitlesType> ltypeList = bll.findByPropertys(strWhere, param, obj);
			lTtype.setTotalProperty(GetALLRoleCount());
			lTtype.setRoot(ltypeList);
			String jsonstr = JSONUtil.serialize(lTtype);
//			System.out.println(jsonstr);
			write(jsonstr);

		} catch (Exception ex) {
			LogUtil.log("获取技术职称类别信息错误", Level.INFO, ex);

		}
	}
	/**
	 * 修改技术职称类别信息
	 * @throws Exception 
	 * 
	 */
	public void UpdateTechnologyType() throws Exception {
		ListRange<HrCTechnologyTitlesType> tlistType = new ListRange<HrCTechnologyTitlesType>();
		
		try {
			HrCTechnologyTitlesTypeFacadeRemote bll =(HrCTechnologyTitlesTypeFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
	    if(newtt!=null){
			HrCTechnologyTitlesType model = bll.findById(newtt.getTechnologyTitlesTypeId());
			model.setTechnologyTitlesTypeId(newtt.getTechnologyTitlesTypeId());
			model.setTechnologyTitlesTypeName(newtt.getTechnologyTitlesTypeName());
			model.setIsUse(newtt.getIsUse());
			model.setRetrieveCode(newtt.getRetrieveCode());
			bll.update(model);
			}
		tlistType.setSuccess(true);
		tlistType.setMessage("您修改技术职称类别信息成功！");
		String jsonstr = JSONUtil.serialize(tlistType);
		write(jsonstr);
			
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");}
		
		
	}
	/**
	 * 删除技术职称类别信息
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	public void DelTechnologyType() throws Exception{
		 ListRange lttype = new ListRange();
		
		 HrCTechnologyTitlesTypeFacadeRemote bll = (HrCTechnologyTitlesTypeFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
       
         if (this.ttypeids != null && ttypeids.length() > 0) {
	     String[] typeidArray = ttypeids.split(",");
	    for (String idCode : typeidArray) {
		HrCTechnologyTitlesType model = bll.findById(Long.parseLong(idCode));
        model.setIsUse("D");
		bll.update(model);
	    }
	     }	
         lttype.setSuccess(true);
         lttype.setMessage("您删除技术职称类别信息成功!");
        String jsonstr = JSONUtil.serialize(lttype);
        write(jsonstr);
		    
	}
	/**
	 * 新增技术职称类别信息
	 * @throws NamingException 
	 * @throws JSONException 
	 * @throws IOException 
	 * 
     */
	public void InsertTechnologyType() throws NamingException, JSONException, IOException {
		try{
			HrCTechnologyTitlesTypeFacadeRemote bll =(HrCTechnologyTitlesTypeFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
			ListRange<HrCTechnologyTitlesType> tlistType = new ListRange<HrCTechnologyTitlesType>();
			HrCTechnologyTitlesType tTitlesType =new HrCTechnologyTitlesType();
			if(newtt!=null){
				tTitlesType.setTechnologyTitlesTypeId(getMaxtechnologytypeId());
				tTitlesType.setTechnologyTitlesTypeName(newtt.getTechnologyTitlesTypeName());
				tTitlesType.setIsUse(newtt.getIsUse());
				tTitlesType.setRetrieveCode(newtt.getRetrieveCode());
				bll.save(tTitlesType);
				tlistType.setSuccess(true);
				tlistType.setMessage("您添加技术职称类别成功！");
				String jsonstr = JSONUtil.serialize(tlistType);
				write(jsonstr);
				}
		}catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * 获取有效技术职称类别总数
	 * 
	 * @return
	 */
	private int GetALLRoleCount() {
		int counts = 0;
		String sqlstr = "select count(1) from HR_C_TECHNOLOGY_TITLES_TYPE where TECHNOLOGY_TITLES_TYPE_ID > 0 And IS_USE<>'Y'";//update by sychen 20100902
//		String sqlstr = "select count(1) from HR_C_TECHNOLOGY_TITLES_TYPE where TECHNOLOGY_TITLES_TYPE_ID > 0 And IS_USE<>'D'";
						
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			counts = Integer.parseInt(ob.toString());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			LogUtil.log("sys_c_role err", Level.INFO, e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("sys_c_role err", Level.INFO, e);
		}
		return counts;
	}
	public void getTechnologyTypeInfo() throws JSONException, IOException, NamingException{
		long typeid =Long.parseLong(request.getParameter("ttypeid")) ;
		HrCTechnologyTitlesTypeFacadeRemote bll = (HrCTechnologyTitlesTypeFacadeRemote) Ejb3Factory
		.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
		HrCTechnologyTitlesType newtt =bll.findById(typeid);
		List<HrCTechnologyTitlesType> techtypeList = new ArrayList<HrCTechnologyTitlesType>();
		techtypeList.add(newtt);
		ListRange<HrCTechnologyTitlesType> atechlist = new ListRange<HrCTechnologyTitlesType>();
	    atechlist.setRoot(techtypeList);
		String jsonstr = JSONUtil.serialize(atechlist);
		write(jsonstr);
	}
	
	public void name() {
		
	}
	
	/**
	 * 获取技术职称类别表中最大的id+1
	 * 
	 * @return
	 */
	private long getMaxtechnologytypeId() {
		long maxid = 0;
		String sqlstr = String
				.format("select nvl(max(TECHNOLOGY_TITLES_TYPE_ID)+1,1) from HR_C_TECHNOLOGY_TITLES_TYPE");
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			maxid = Long.parseLong(ob.toString());

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		}
		return maxid;
	}
	
	
	/**
	 * 通过函数获取技术职称检索码
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	
	public void getRetrieveCodeInfo() throws JSONException, IOException {
		String ttypename =request.getParameter("typenames");
		String sqlstr = String
		.format("select fun_spellcode('%s') from dual",ttypename);
		try {
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			String retrievecode=(ob.toString());
//			HrCTechnologyTitlesType o =new HrCTechnologyTitlesType();
//			o.setRetrieveCode(retrievecode);
//			List<HrCTechnologyTitlesType> ttypeList = new ArrayList<HrCTechnologyTitlesType>();
//			ttypeList.add(o);
			ListRange<HrCTechnologyTitlesType> ttechlist = new ListRange<HrCTechnologyTitlesType>();
//			ttechlist.setRoot(ttypeList);
			ttechlist.setMessage(retrievecode);
			ttechlist.setSuccess(true);
			String jsonstr = JSONUtil.serialize(ttechlist);
//			System.out.println(jsonstr);
			write(jsonstr);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		}
	}
	public String getMethod() {
		return method;
	}



	public void setMethod(String method) {
		this.method = method;
	}



	public String getStart() {
		return start;
	}



	public void setStart(String start) {
		this.start = start;
	}



	public String getLimit() {
		return limit;
	}



	public void setLimit(String limit) {
		this.limit = limit;
	}
	public String getTtypeids() {
		return ttypeids;
	}


	public void setTtypeids(String roleids) {
		this.ttypeids = roleids;
	}


	public HrCTechnologyTitlesType getNewtt() {
		return newtt;
	}


	public void setNewtt(HrCTechnologyTitlesType newtt) {
		this.newtt = newtt;
	}

}
