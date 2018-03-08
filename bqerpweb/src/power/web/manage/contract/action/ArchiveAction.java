package power.web.manage.contract.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJArchives;
import power.ejb.manage.contract.business.ConJArchivesFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.web.comm.AbstractAction;

public class ArchiveAction extends AbstractAction{

	/**
	 * 案卷管理
	 */
	private static final long serialVersionUID = 1L;
	private String str;
	private String archiveIds;
	private int limit;
	private int max;
	private Long conTypeId;
	private ConJArchivesFacadeRemote remote;
	private ConJContractInfoFacadeRemote conRemote;
	public ArchiveAction(){
		remote=(ConJArchivesFacadeRemote)factory.getFacadeRemote("ConJArchivesFacade");
		conRemote=(ConJContractInfoFacadeRemote)factory.getFacadeRemote("ConJContractInfoFacade");
	}
	public void addarchive() throws ParseException,Exception{
		List<ConJArchives> list=this.toArcArrayList();
		if(list!=null){
			ConJArchives model=list.get(0);
			model.setEnterpriseCode("hfdc");
			model.setDrawCount(conTypeId);
			remote.save(model);
		}
	}
	public void updateArchives() throws ParseException,Exception{
		List<ConJArchives> list=this.toArcArrayList();
		if(list!=null){
			for(ConJArchives model : list){
				ConJArchives entity=remote.findById(model.getArchivesId());
				if(entity!=null){
					model.setEnterpriseCode("hfdc");
					remote.update(model);
				}
			}
		}	
	}
	public void deleteArchive(){
		remote.deleteMu(archiveIds);
	}
	public void findArchiveList() throws JSONException{
		PageObject pg=remote.findArchives(conTypeId,"hfdc",limit,max);
		String str="";
		if(pg!=null){
			List<ConJArchives> list=pg.getList();
			Long count=pg.getTotalCount();
			str+="{total:"+count+",list:"+JSONUtil.serialize(list)+"}";
		}else{
			str+="{total:0,list:[]}";
		}
		write(str);	
		
	}
	public void judgeArchive(){
		String undertakeNo=request.getParameter("undertakeNo");
		boolean b=conRemote.isArchive(undertakeNo);
		write("{\"isFlag\":"+b+"}");
	}
	private List<ConJArchives> toArcArrayList() throws ParseException,Exception{
		Object object = JSONUtil.deserialize(str);
		List arrList=new ArrayList();
		if(object!=null){
			if(List.class.isAssignableFrom(object.getClass())){
				List list=(List)object;
				int intLen = list.size();
				for(int i=0;i<intLen;i++){
					Map map=(Map)list.get(i);
					ConJArchives arc=this.setConJArchives(map);
					arrList.add(arc);
				}
			}else{
				Map map=(Map)object;
				ConJArchives arc=this.setConJArchives(map);
				arrList.add(arc);
			}
		}
		return arrList;
	}
	private ConJArchives setConJArchives(Map m){
		ConJArchives model=new ConJArchives();
		if(m!=null){
			String archivesCount=m.get("archivesCount").toString();
			String charCount=m.get("charCount").toString();
			String drawCount=m.get("drawCount").toString();
			String pageCount=m.get("pageCount").toString();
			String pieceCount=m.get("pieceCount").toString();
			if(!archivesCount.trim().equals("") && !archivesCount.trim().equals("undefined")){
				model.setArchivesCount(Long.parseLong(m.get("archivesCount").toString()));
			}
			model.setArchivesId(Long.parseLong(m.get("archivesId").toString()));
			model.setArchivesName(m.get("archivesName").toString());
			if(!charCount.trim().equals("") && !charCount.trim().equals("undefined")){
				model.setCharCount(Long.parseLong(m.get("charCount").toString()));
			}
			if(!drawCount.trim().equals("") && !drawCount.trim().equals("undefined")){
				model.setDrawCount(Long.parseLong(m.get("drawCount").toString()));
			}
			model.setJerqueDate(new Date());
			if(!m.get("jerquePeople").toString().trim().equals("undefined")){
				model.setJerquePeople(m.get("jerquePeople").toString());
			}
			if(!m.get("keepLevel").toString().trim().equals("undefined")){
				model.setKeepLevel(m.get("keepLevel").toString());
			}
			if(!m.get("keepPosition").toString().equals("undefined")){
				model.setKeepPosition(m.get("keepPosition").toString());
			}
			if(!m.get("memo").toString().equals("undefined")){
				model.setMemo(m.get("memo").toString());
			}
			if(!pageCount.trim().equals("") && !pageCount.trim().equals("undefined")){
				model.setPageCount(Long.parseLong(m.get("pageCount").toString()));
			}
			if(!pieceCount.trim().equals("") && !pieceCount.trim().equals("undefined")){
				model.setPieceCount(Long.parseLong(m.get("pieceCount").toString()));
			}
			if(!m.get("timeLimit").toString().equals("undefined")){
				model.setTimeLimit(m.get("timeLimit").toString());
			}
			if(!m.get("undertakeNo").toString().equals("undefined")){
				model.setUndertakeNo(m.get("undertakeNo").toString());
			}
			if(!m.get("unitName").toString().equals("undefined")){
				model.setUnitName(m.get("unitName").toString());
			}
			model.setIsUse("Y");
			model.setUpbuildDate(new Date());
			if(!m.get("upbuildPeople").toString().equals("undefined")){
				model.setUpbuildPeople(m.get("upbuildPeople").toString());
			}
			model.setWeaveDate(m.get("weaveDate").toString());
		}
		return model;
	}
	public void queryArch() throws JSONException{
		PageObject pg=new PageObject();
		String undertakeNo1="";
		String undertakeNo2="";
		String no=request.getParameter("no");
		if(no.indexOf("#")!=-1){
			int num=no.indexOf("#");
			undertakeNo1=no.substring(0,num-1);
			undertakeNo2=no.substring(num,no.length());
		}
		else{
			undertakeNo1=no;
		}
		String[] str=undertakeNo1.split(",");
		if(!"".equals(undertakeNo2) && undertakeNo2.length()>0){
			pg=remote.intervalQuery("dfdc", str[0], undertakeNo2,limit,max);
		}else{
			if(str.length>1){
				pg=remote.separatedQuery("hfdc", str,limit,max);
			}else{
				pg=remote.fuzzyQuery("hfdc", str[0],limit,max);
			}
		}
		if(pg!=null){
			String s="{total:"+pg.getTotalCount()+",list:"+JSONUtil.serialize(pg.getList())+"}";
			write(s);
		}else{
			String s="{total:0,list:[]}";
			write(s);
		}
	}
	//档号查询
	@SuppressWarnings("unchecked")
	public void undertakeNolist() throws Exception {
		List<ConJArchives> list = remote.findfileNoList(employee
				.getEnterpriseCode(),conTypeId);
		String str = "[";
		int i = 0;
		for (ConJArchives model : list) {
			i++;
			str += "[\"" + model.getUndertakeNo() + "\",\""
					+ model.getUndertakeNo() + "\"]";
			if (i < list.size()) {
				str += ",";
			}
		}
		str += "]";
		write(str);
	}
	//
	public String getStr() {
		return str;
	}

	public String getArchiveIds() {
		return archiveIds;
	}
	public void setArchiveIds(String archiveIds) {
		this.archiveIds = archiveIds;
	}
	public void setStr(String str) {
		this.str = str;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public Long getConTypeId() {
		return conTypeId;
	}
	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}
}
