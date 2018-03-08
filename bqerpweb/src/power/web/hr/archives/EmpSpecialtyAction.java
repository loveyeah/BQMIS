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
import power.ejb.hr.archives.HrCEmpSpecialty;
import power.ejb.hr.archives.HrCEmpSpecialtyFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EmpSpecialtyAction extends AbstractAction {

	protected HrCEmpSpecialtyFacadeRemote remote;
	private HrJEmpInfoFacadeRemote empRemote = null;
	private HrCEmpSpecialty specialty;
	private File xlsFile;

	private static final String[] SPECIALTY_COLUMN_NAMES = { "人员编码", "特长名称",
			"特长等级", "发证单位", "发证时间", "备注" };

	public EmpSpecialtyAction() {
		remote = (HrCEmpSpecialtyFacadeRemote) factory
				.getFacadeRemote("HrCEmpSpecialtyFacade");
		 empRemote = (HrJEmpInfoFacadeRemote) factory.getFacadeRemote("HrJEmpInfoFacade");
	}

	public void findSpecialtyList() throws JSONException {
		String empId = request.getParameter("empId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findSepecialtyList(empId,
					employee.getEnterpriseCode(), start, limit);
		} else {
			obj = remote
					.findSepecialtyList(empId, employee.getEnterpriseCode());
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	public void editEmpSpecialtyRecord() {
		String strAddFlag = request.getParameter("isAdd");
		String empId = request.getParameter("empId");
		if (Boolean.parseBoolean(strAddFlag)) {
			specialty.setEmpId(Long.parseLong(empId));
			specialty.setEnterpriseCode(employee.getEnterpriseCode());
			remote.save(specialty);
			write("{success:true,id:'" + specialty.getSpecialtyId()
					+ "',msg:'增加成功！'}");
		} else {
			HrCEmpSpecialty model = remote.findById(specialty.getSpecialtyId());
			model.setSpecialtyName(specialty.getSpecialtyName());
			model.setSpecialtyLevel(specialty.getSpecialtyLevel());
			model.setMemo(specialty.getMemo());
			model.setAwardUnit(specialty.getAwardUnit());
			model.setAwardDate(specialty.getAwardDate());

			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		}
	}

	public void deleteEmpSpecialtyRecord() {
		String ids = request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void importEmpSpecialtyFile() throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		String msg = "";
		List<HrCEmpSpecialty> spelist = new ArrayList<HrCEmpSpecialty>();
		try {
			is = new FileInputStream(xlsFile);
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
					HrCEmpSpecialty specialty = new HrCEmpSpecialty();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for(int j = 0; j < column_indexs.length; j++){
						//  0:人员编码
						if(column_indexs[j] == 0){
							if(cells.length > j &&!"".equals(cells[j].getContents())){
								Long empId = empRemote.getEmpIdByNewEmpCode(cells[j].getContents());
								if(empId == null){
									i++;
									msg = "第" + i + "行人员编码中:"
													+ cells[j].getContents() + "的人员尚未维护！";
									break;
								}else
									specialty.setEmpId(empId);
							}else{
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}
						
						// 1：特长名称
						else if (column_indexs[j] == 1) {
							if (cells.length > j && !"".equals(cells[j].getContents())) {
									specialty.setSpecialtyName(cells[j].getContents());
							} else {
								i++;
								msg = "第" + i + "行特长名称必须填写！";
								break;
							}
						}
						// 2：特长等级
						else if(column_indexs[j] == 2){
							if(cells.length > j && !"".equals(cells[j].getContents())){
								specialty.setSpecialtyLevel(cells[j].getContents());
							}
						}
						// 3：发证单位
						else if (column_indexs[j] == 3) {
							if (cells.length > j && !"".equals(cells[j].getContents())) {
								specialty.setAwardUnit(cells[j].getContents());
							}
						}
						// 4：发证时间
						else if(column_indexs[j] == 4){
							if (cells.length > j && !"".equals(cells[j].getContents())) {
								String[] startDayArr = cells[j].getContents().split("-");
								if (startDayArr.length != 3) {
									i++;
									msg = "第" + i + "行发证时间格式填写不正确！";
									break;
								}
								if (startDayArr[1].length() == 1)
									startDayArr[1] = "0" + startDayArr[1];
								if (startDayArr[2].length() == 1)
									startDayArr[2] = "0" + startDayArr[2];
								try {
									specialty.setAwardDate(df.parse(startDayArr[0]
											+ "-" + startDayArr[1] + "-"
											+ startDayArr[2]));
								} catch (Exception e1) {
									i++;
									msg = "第" + i + "行发证时间格式填写不正确！";
									break;
								}
							}
						}
						// 5:备注
						else if (column_indexs[j] == 5) {
							if (cells.length > j && !"".equals(cells[j].getContents())) {
								specialty.setMemo(cells[5].getContents());
							}
						}
					}
					if (msg.equals("")) {
						if (specialty.getEmpId() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}
						if (specialty.getSpecialtyName() == null) {
							msg = "特长名称列必须存在，请检查导入的excel文件!";
							break;
						}
						if (specialty.getSpecialtyLevel() == null) {
							msg = "特长等级列必须存在，请检查导入的excel文件!";
							break;
						}
					}
					specialty.setEnterpriseCode(employee.getEnterpriseCode());
					specialty.setIsUse("Y");
					spelist.add(specialty);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}
		if (msg.equals("")) {
			remote.importEmpSpeFilesInfo(spelist);
			write("{success:true,msg:'导入成功！'}");
		} else {
			write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
		}
	}

	private String addColumnIndex(int[] column_indexs, Cell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			boolean isError = true;
			for (int j = 0; j < SPECIALTY_COLUMN_NAMES.length; j++) {
				if (SPECIALTY_COLUMN_NAMES[j].equals(cells[i].getContents())) {
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

	public HrCEmpSpecialty getSpecialty() {
		return specialty;
	}

	public void setSpecialty(HrCEmpSpecialty specialty) {
		this.specialty = specialty;
	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}

}
