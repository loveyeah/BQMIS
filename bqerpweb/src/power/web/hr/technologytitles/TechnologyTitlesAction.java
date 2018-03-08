package power.web.hr.technologytitles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.naming.NamingException; 

import power.ear.comm.CodeRepeatException;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.HrCTechnologyGrade;
import power.ejb.hr.HrCTechnologyGradeFacadeRemote;
import power.ejb.hr.HrCTechnologyTitles;
import power.ejb.hr.HrCTechnologyTitlesFacadeRemote;
import power.ejb.hr.HrCTechnologyTitlesType;
import power.ejb.hr.HrCTechnologyTitlesTypeFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.ListCombox;
import power.web.comm.ListRange;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class TechnologyTitlesAction extends AbstractAction
{
	private String method;
	private int start;
	private int limit;
	private String techtitlesids;
	private HrCTechnologyTitles newtl;

	/**
	 * 对技术职称信息操作
	 * 
	 * @throws IOException
	 * @throws NamingException
	 */
	public void getALLTechnologyTitlesInfo() throws JSONException, IOException
	{
		try
		{
			if ("get".equals(method))
			{
				getAllTechnologyTitles();
			} else if ("update".equals(method))
			{
				UpdateTechnologyTitles();
			} else if ("del".equals(method))
			{
				DelTechnologyTitles();
			} else if ("insert".equals(method))
			{
				InsertTechnologyTitles();
			} else if ("form".equals(method))
			{
				getTechnologyTitlesInfo();
			} else if ("getcode".equals(method))
			{
				getTechTitleRetrieveCodeInfo();
			} else if ("type".equals(method))
			{
				getTechTypeList();
			} else if ("grade".equals(method))
			{
				getTechGradeList();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			ListRange lRange = new ListRange();
			lRange.setSuccess(false);
			lRange.setMessage(e.getMessage());
			String jsonstr = JSONUtil.serialize(lRange);
			write(jsonstr);
		}
	}

	public void getIsUseOfTL()
	{
		write("{total:3,root:[{'id':'Y','name':'使用'},{'id':'N','name':'停用'},{'id':'L','name':'注销'}]}");//update by sychen 20100902
//		write("{total:3,root:[{'id':'U','name':'使用'},{'id':'N','name':'停用'},{'id':'L','name':'注销'}]}");
	}
	
	/*
	 * 为技术职称类别配置下拉列表数据源
	 */
	public void getTechTypeList() throws IOException, NamingException,
			JSONException
	{
		List<ListCombox> typecombox = new ArrayList<ListCombox>();
		HrCTechnologyTitlesTypeFacadeRemote bll = (HrCTechnologyTitlesTypeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTechnologyTitlesTypeFacade");
		//===================add by drdu 091027=============================
		
		String strWhere = " model.isUse<>:param ";
		Object param="Y"; //update by sychen 20100902
//		Object param="D"; 
		List<HrCTechnologyTitlesType> typelist = bll.findByPropertys(strWhere, param);
		
		//==================================================================
		//List<HrCTechnologyTitlesType> typelist = bll.findAll();
		if (typelist.size() > 0)
		{
			for (HrCTechnologyTitlesType o : typelist)
			{
				ListCombox lCombox = new ListCombox();
				lCombox.setId(Long.parseLong(o.getTechnologyTitlesTypeId().toString()));
				lCombox.setName(o.getTechnologyTitlesTypeName());
				typecombox.add(lCombox);
			}
		}
		String strlist = JSONUtil.serialize(typecombox);
		// System.out.println(strlist);
		write(strlist);
	}

	/*
	 * 为技术职称等级配置下拉列表数据源
	 */
	public void getTechGradeList() throws IOException, NamingException,
			JSONException
	{
		List<ListCombox> gradecombox = new ArrayList<ListCombox>();
		HrCTechnologyGradeFacadeRemote bll = (HrCTechnologyGradeFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTechnologyGradeFacade");
		List<HrCTechnologyGrade> gradelist = bll.findAll();
		if (gradelist.size() > 0)
		{
			for (HrCTechnologyGrade o : gradelist)
			{
				ListCombox lCombox = new ListCombox();
				lCombox.setId(Long.parseLong(o.getTechnologyGradeId().toString()));
				lCombox.setName(o.getTechnologyGradeName());
				gradecombox.add(lCombox);
			}
		}
		String strlist = JSONUtil.serialize(gradecombox);
		// System.out.println(strlist);
		write(strlist);
	}

	/**
	 * 获得有效技术职称列表信息
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public void getAllTechnologyTitles() throws NumberFormatException,
			Exception
	{
		ListRange<TechnologyTitlesBean> lTtype = new ListRange<TechnologyTitlesBean>();
		HrCTechnologyTitlesFacadeRemote remote = (HrCTechnologyTitlesFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTechnologyTitlesFacade");
		PageObject result = remote.getTechnologyTitlesList(start, limit);
		if(result == null)
		{
			write("{list:[],totalCount:0}");
		}
		else
		{
			List ttitlesList = result.getList();
			List<TechnologyTitlesBean> ltypeList = new ArrayList<TechnologyTitlesBean>();
			if (ttitlesList != null && ttitlesList.size() > 0) {
				for (Object o : ttitlesList) { 
					Object[] obj = (Object[]) o;
					TechnologyTitlesBean tecttitleBean = new TechnologyTitlesBean();
					tecttitleBean.setTechnologyTitlesId(obj[0] == null ? ""
							: obj[0].toString());
					tecttitleBean.setTechnologyTitlesName(obj[1] == null ? ""
							: obj[1].toString());
					tecttitleBean.setTechnologyTitlesType(obj[2] == null ? ""
							: obj[2].toString());
					tecttitleBean.setTechnologyTitlesLevel(obj[3] == null ? ""
							: obj[3].toString());
					tecttitleBean.setIsUse(obj[4] == null ? "" : obj[4].toString());
					tecttitleBean.setRetrieveCode(obj[5] == null ? "" : obj[5]
							.toString());
					ltypeList.add(tecttitleBean);
				}
				lTtype.setTotalProperty(result.getTotalCount());
				lTtype.setRoot(ltypeList);
				String jsonstr = JSONUtil.serialize(lTtype);  
				write(jsonstr);
			}
		}		
		 
	}

	/**
	 * 修改技术职称信息
	 * 
	 * @throws Exception
	 * 
	 */
	public void UpdateTechnologyTitles() throws Exception
	{
		ListRange<HrCTechnologyTitles> tlistType = new ListRange<HrCTechnologyTitles>();
		try
		{
			HrCTechnologyTitlesFacadeRemote bll = (HrCTechnologyTitlesFacadeRemote) factory.getFacadeRemote("HrCTechnologyTitlesFacade");
			if (newtl != null)
			{
				HrCTechnologyTitles model = bll.findById(newtl
						.getTechnologyTitlesId());
				model.setTechnologyTitlesTypeId(newtl.getTechnologyTitlesId());
				model.setTechnologyTitlesTypeId(newtl
						.getTechnologyTitlesTypeId());
				model.setTechnologyTitlesName(newtl.getTechnologyTitlesName());
				model
						.setTechnologyTitlesLevel(newtl
								.getTechnologyTitlesLevel());
				model.setIsUse(newtl.getIsUse());
				model.setRetrieveCode(newtl.getRetrieveCode());
				bll.update(model);
			}
			tlistType.setSuccess(true);
			tlistType.setMessage("您修改技术职称信息成功！");
			String jsonstr = JSONUtil.serialize(tlistType);
			write(jsonstr);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * 删除技术职称信息
	 * 
	 * @throws Exception
	 * 
	 * 
	 * 
	 */
	public void DelTechnologyTitles() throws Exception
	{
		ListRange lttitles = new ListRange();
		HrCTechnologyTitlesFacadeRemote bll = (HrCTechnologyTitlesFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTechnologyTitlesFacade");
		if (this.techtitlesids != null && techtitlesids.length() > 0)
		{
			String[] typeidArray = techtitlesids.split(",");
			for (String idCode : typeidArray)
			{
				HrCTechnologyTitles model = bll
						.findById(Long.parseLong(idCode));
				model.setIsUse("N"); //update by sychen 20100902
//				model.setIsUse("D");
				bll.update(model);
			}
		}
		lttitles.setSuccess(true);
		lttitles.setMessage("您删除技术职称类别信息成功!");
		String jsonstr = JSONUtil.serialize(lttitles);
		write(jsonstr);
	}

	/**
	 * 新增技术职称信息
	 * 
	 * @throws NamingException
	 * @throws JSONException
	 * @throws IOException
	 * 
	 */
	public void InsertTechnologyTitles() throws NamingException, JSONException,
			IOException
	{
		HrCTechnologyTitlesFacadeRemote bll = (HrCTechnologyTitlesFacadeRemote) factory.getFacadeRemote("HrCTechnologyTitlesFacade");
		ListRange<HrCTechnologyTitles> tlistTitles = new ListRange<HrCTechnologyTitles>();
		HrCTechnologyTitles tTitlesType = new HrCTechnologyTitles();
		try {
		if (newtl != null)
		{ 
			tTitlesType.setTechnologyTitlesTypeId(newtl
					.getTechnologyTitlesTypeId());
			tTitlesType
					.setTechnologyTitlesName(newtl.getTechnologyTitlesName());
			tTitlesType.setTechnologyTitlesLevel(newtl
					.getTechnologyTitlesLevel());
			tTitlesType.setIsUse(newtl.getIsUse());
			tTitlesType.setRetrieveCode(newtl.getRetrieveCode());
			bll.save(tTitlesType);
		}
		tlistTitles.setSuccess(true);
		tlistTitles.setMessage("您添加技术职称类别成功！");
		String jsonstr = JSONUtil.serialize(tlistTitles);
		write(jsonstr);
		} catch (CodeRepeatException e) {
			write("{failure:true,errMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * 获取有效技术职称总数
	 * 
	 * @return
	 */
//	private int getALLTechnologyTitlesCount()
//	{
//		int counts = 0;
//		String sqlstr = "select count(1) from HR_C_TECHNOLOGY_TITLES where TECHNOLOGY_TITLES_ID > 0 And IS_USE<>'D'";
//		try
//		{
//			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
//					.getInstance().getFacadeRemote("NativeSqlHelper");
//			Object ob = bll.getSingal(sqlstr);
//			counts = Integer.parseInt(ob.toString());
//		} catch (NumberFormatException e)
//		{
//			// TODO Auto-generated catch block
//			LogUtil.log("HR_C_TECHNOLOGY_TITLES err", Level.INFO, e);
//		} catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			LogUtil.log("HR_C_TECHNOLOGY_TITLES err", Level.INFO, e);
//		}
//		return counts;
//	}

	public void getTechnologyTitlesInfo() throws JSONException, IOException,
			NamingException
	{
		long typeid = Long.parseLong(request.getParameter("ttypeid"));
		HrCTechnologyTitlesFacadeRemote bll = (HrCTechnologyTitlesFacadeRemote) Ejb3Factory
				.getInstance().getFacadeRemote("HrCTechnologyTitlesFacade");
		HrCTechnologyTitles ttitles = bll.findById(typeid);
		List<HrCTechnologyTitles> techtitlesList = new ArrayList<HrCTechnologyTitles>();
		techtitlesList.add(ttitles);
		ListRange<HrCTechnologyTitles> atechtitles = new ListRange<HrCTechnologyTitles>();
		atechtitles.setRoot(techtitlesList);
		String jsonstr = JSONUtil.serialize(atechtitles);
//		System.out.println(jsonstr);
		write(jsonstr);
	}

	public void name()
	{
	}

	/**
	 * 获取技术职称表中最大的id+1
	 * 
	 * @return
	 */
//	private long getMaxtechnologyTitlesId()
//	{
//		long maxid = 0;
//		String sqlstr = String
//				.format("select nvl(max(TECHNOLOGY_TITLES_ID)+1,1) from HR_C_TECHNOLOGY_TITLES");
//		try
//		{
//			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
//					.getInstance().getFacadeRemote("NativeSqlHelper");
//			Object ob = bll.getSingal(sqlstr);
//			maxid = Long.parseLong(ob.toString());
//		} catch (NumberFormatException e)
//		{
//			// TODO Auto-generated catch block
//			LogUtil.log("HR_C_TECHNOLOGY_TITLES err", Level.INFO, e);
//		} catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			LogUtil.log("HR_C_TECHNOLOGY_TITLES err", Level.INFO, e);
//		}
//		return maxid;
//	}

	/**
	 * 通过函数获取技术职称检索码
	 * 
	 * @throws JSONException
	 * @throws IOException
	 */
	public void getTechTitleRetrieveCodeInfo() throws JSONException,
			IOException
	{
		String ttitlename = request.getParameter("titlesnames");
		String sqlstr = String.format(
				"select fun_spellcode('%s') from dual",
				ttitlename);
		try
		{
			NativeSqlHelperRemote bll = (NativeSqlHelperRemote) Ejb3Factory
					.getInstance().getFacadeRemote("NativeSqlHelper");
			Object ob = bll.getSingal(sqlstr);
			String retrievecode = (ob.toString());
			// HrCTechnologyTitlesType o =new HrCTechnologyTitlesType();
			// o.setRetrieveCode(retrievecode);
			// List<HrCTechnologyTitlesType> ttypeList = new
			// ArrayList<HrCTechnologyTitlesType>();
			// ttypeList.add(o);
			ListRange<HrCTechnologyTitles> ttechtitles = new ListRange<HrCTechnologyTitles>();
			// ttechlist.setRoot(ttypeList);
			ttechtitles.setMessage(retrievecode);
			ttechtitles.setSuccess(true);
			String jsonstr = JSONUtil.serialize(ttechtitles);
//			System.out.println(jsonstr);
			write(jsonstr);
		} catch (NumberFormatException e)
		{
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			LogUtil.log("SYS_C_WEBPAGE err", Level.INFO, e);
		}
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	 

	public HrCTechnologyTitles getNewtl()
	{
		return newtl;
	}

	public void setNewtl(HrCTechnologyTitles newtl)
	{
		this.newtl = newtl;
	}

	public String getTechtitlesids()
	{
		return techtitlesids;
	}

	public void setTechtitlesids(String techtitlesids)
	{
		this.techtitlesids = techtitlesids;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
