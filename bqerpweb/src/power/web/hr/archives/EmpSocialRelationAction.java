package power.web.hr.archives;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.archives.HrCSocialRelations;
import power.ejb.hr.archives.HrCSocialRelationsFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EmpSocialRelationAction extends AbstractAction {
	private HrJEmpInfoFacadeRemote empRemote = null;
	protected HrCSocialRelationsFacadeRemote remote;
	private HrCSocialRelations relation;

	private File uploadFile;

	private static final String[] SOCIALRELATION_COLUMN_NAMES = { "人员编码", "姓名","称谓", "是否已故","已故时间", "有无重大历史问题", "获得奖励情况", "职业", "政治面貌", "是否港澳台侨民" };

	public EmpSocialRelationAction() {
		empRemote = (HrJEmpInfoFacadeRemote) factory.getFacadeRemote("HrJEmpInfoFacade");
		remote = (HrCSocialRelationsFacadeRemote) factory
				.getFacadeRemote("HrCSocialRelationsFacade");
	}

	public void addAndUpdateRelation() {
		String strAddFlag = request.getParameter("isAdd");
		String empId = request.getParameter("empId");
		if (Boolean.parseBoolean(strAddFlag)) {
			relation.setEmpId(Long.parseLong(empId));
			relation.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(relation);
			write("{success:true,id:'" + relation.getRelationId()
					+ "',msg:'增加成功！'}");
		} else {
			HrCSocialRelations model = remote
					.findById(relation.getRelationId());
			model.setDeathDate(relation.getDeathDate());
			model.setFace(relation.getFace());
			model.setIsDeath(relation.getIsDeath());
			model.setIsMajorProblem(relation.getIsMajorProblem());
			model.setIsNationals(relation.getIsNationals());
			model.setMajorProblem(relation.getMajorProblem());
			model.setProfessional(relation.getProfessional());
			model.setRelationName(relation.getRelationName());
			model.setRelationTitle(relation.getRelationTitle());

			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}

	public void deleteRelation() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void findEmpSocialRelation() throws JSONException {
		String empId = request.getParameter("empId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findSocialRelationList(empId, employee
					.getEnterpriseCode(), start, limit);
		} else {
			obj = remote.findSocialRelationList(empId, employee
					.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void importEmpSocialRelationFiles() throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrCSocialRelations> relationlist = new ArrayList<HrCSocialRelations>();
			try {
				is = new FileInputStream(uploadFile);
				workbook = Workbook.getWorkbook(is);// 得到工作薄
				Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
				int rows = sheet.getRows();// 得到excel的总行数
				if (rows == 0) {
					msg =  "{success:true,msg:'无数据进行导入!'}";
				} else if(rows == 1){
					msg =  "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
				}else {
					int[] column_indexs = new int[sheet.getRow(0).length];
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String columnError = null;
					columnError = addColumnIndex(column_indexs,sheet.getRow(0));
					// 列名是否存在问题
					if(columnError != null)
					write(columnError);
					for (int i = 1; i < rows; i++) {
						HrCSocialRelations relations = new HrCSocialRelations();
						Cell[] cells = sheet.getRow(i);// i行的所有单元格
						for(int j = 0; j < column_indexs.length; j++){
							//  0:人员编码
							if(column_indexs[j] == 0){
								if(cells.length > j &&!"".equals(cells[j].getContents())){
									Long empId = empRemote.getEmpIdByNewEmpCode(cells[j].getContents());
									if(empId == null){
										i++;
										msg = "第" + i + "行人员编码中:"+ cells[j].getContents() + "的人员尚未维护！";
										break;
									}else
										relations.setEmpId(empId);
								}else{
									i++;
									msg = "第" + i + "行人员编码必须填写！";
									break;
								}
							}
							
							// 1：姓名
							else if (column_indexs[j] == 1) {
								if (cells.length > j && !"".equals(cells[j].getContents())) {
										relations.setRelationName(cells[j].getContents());
								} else {
									i++;
									msg = "第" + i + "行姓名必须填写！";
									break;
								}
							}
							// 2：称谓
							else if(column_indexs[j] == 2){
								if(cells.length > j && !"".equals(cells[j].getContents())){
									relations.setRelationTitle(cells[j].getContents());
								}else{
									i++;
									msg = "第" + i + "行称谓必须填写！";
									break;
								}
							}
//							// 3：是否已故
//							else if (column_indexs[j] == 3) {
//								if (cells.length > j && !"".equals(cells[j].getContents())) {
//									if (cells[j].getContents().trim().equals("是")) {
//										relations.setIsDeath("Y");
//									} else if (cells[j].getContents().trim().equals("否")) {
//										relations.setIsDeath("N");
//									}
//								}
//							}
//							// 4：已故时间
//							else if(column_indexs[j] == 4){
//								if (cells.length > j && !"".equals(cells[j].getContents())) {
//									String[] startDayArr = cells[j].getContents().split("-");
//									if (startDayArr.length != 3) {
//										i++;
//										msg = "第" + i + "行已故时间格式填写不正确！";
//										break;
//									}
//									if (startDayArr[1].length() == 1)
//										startDayArr[1] = "0" + startDayArr[1];
//									if (startDayArr[2].length() == 1)
//										startDayArr[2] = "0" + startDayArr[2];
//									try {
//										relations.setDeathDate(df.parse(startDayArr[0]
//												+ "-" + startDayArr[1] + "-"
//												+ startDayArr[2]));
//									} catch (Exception e1) {
//										i++;
//										msg = "第" + i + "行已故时间格式填写不正确！";
//										break;
//									}
//								}
//							}
							//update by sychen 20100715
							// 5:是否有重大历史问题
//							else if (column_indexs[j] == 5) {
//								if (cells.length > j && !"".equals(cells[j].getContents())) {
//									if (cells[j].getContents().trim().equals("有")) {
//										relations.setIsMajorProblem("Y");
//									} else if (cells[j].getContents().trim().equals("无")) {
//										relations.setIsMajorProblem("N");
//									}
//								}
//							}
////							重大历史问题
//							else if(column_indexs[j] ==6)
//							{
//								if (cells.length > j && !"".equals(cells[j].getContents())) {
//									relations.setMajorProblem(cells[j].getContents());
//								}
//							}
						    //获得奖励情况
							else if(column_indexs[j] ==6)
							{
								if (cells.length > j && !"".equals(cells[j].getContents())) {
									relations.setMajorProblem(cells[j].getContents());
								}
							}
							//update by sychen 20100715 end
							
							//职业
							else if(column_indexs[j]==7)
							{
								if (cells.length > j && !"".equals(cells[j].getContents())) {
									relations.setProfessional(cells[j].getContents());
								}
							}
							//政治面貌
							else if(column_indexs[j]==8)
							{
								if (cells.length > j && !"".equals(cells[j].getContents())) {
									relations.setFace(cells[j].getContents());
								}
							}
							//是否港澳台侨民
							else if(column_indexs[j]==9)
							{
								if (cells.length > j && !"".equals(cells[j].getContents())) {
									if (cells[j].getContents().trim().equals("是")) {
										relations.setIsNationals("Y");
									} else if (cells[j].getContents().trim().equals("否")) {
										relations.setIsNationals("N");
									}
								}
							}
						}
						if (msg.equals("")) {
							if (relations.getEmpId() == null) {
								msg = "人员编码列必须存在，请检查导入的excel文件!";
								break;
							}
							if (relations.getRelationName() == null) {
								msg = "名称列必须存在，请检查导入的excel文件!";
								break;
							}
							if (relations.getRelationTitle() == null) {
								msg = "称谓列必须存在，请检查导入的excel文件!";
								break;
							}
//							if (relations.getIsDeath().equals("Y")) {
//								if(relations.getDeathDate()==null)
//								{
//									msg = "已故人员，已故时间列必须存在，请检查导入的excel文件!";
//									break;
//								}
//							}else if(relations.getIsDeath().equals("N"))
//							{
//								if(relations.getDeathDate()!=null)
//								{
//									msg = "在世人员，已故时间列不能填写，请检查导入的excel文件!";
//									break;
//								}
//							}
						}
						relations.setEnterpriseCode(employee.getEnterpriseCode());
						relations.setIsUse("Y");
						relationlist.add(relations);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				workbook.close();
				is.close();
			}
			if (msg.equals("")) {
				remote.importSocialRelationFilesInfo(relationlist);
				write("{success:true,msg:'导入成功！'}");
			} else {
				write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
			}
	}

	private String addColumnIndex(int[] column_indexs, Cell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			boolean isError = true;
			for (int j = 0; j < SOCIALRELATION_COLUMN_NAMES.length; j++) {
				if (SOCIALRELATION_COLUMN_NAMES[j].equals(cells[i].getContents())) {
					column_indexs[i] = j;
					isError = false;
				}
			}
			if (isError) {
				return "{success:true,msg:'" + cells[i].getContents()
						+ "列不是要导入的具体列！'}";
			}
		}
		return null;
	}

	public HrCSocialRelations getRelation() {
		return relation;
	}

	public void setRelation(HrCSocialRelations relation) {
		this.relation = relation;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
}
