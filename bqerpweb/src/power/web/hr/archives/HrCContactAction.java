package power.web.hr.archives;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.hr.HrCAppellationFacadeRemote;
import power.ejb.hr.HrJEmpInfoFacadeRemote;
import power.ejb.hr.HrJFamilymember;
import power.ejb.hr.archives.HrCContact;
import power.ejb.hr.archives.HrCContactFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class HrCContactAction extends AbstractAction {
	protected HrCContactFacadeRemote remote;
	/**
	 * 员工基本联系方式信息
	 */
	private HrCContact conform;
	/**
	 * 人员基本信息管理处理远程对象
	 */
	private HrJEmpInfoFacadeRemote empRemote = null;

	/**
	 * 联系方式导入excel文件
	 */
	private File xlsFile;

	/**
	 * add by ltong 20100612 人员联系方式导入excel模板列名 0:人员编码 1:载波电话 2:外线电话 3:手机号码
	 * 4:邮箱地址 5:邮政编码 6:联系地址
	 */
	private static final String[] CONTACTFASHION_COLUMN_NAMES = { "人员编码",
			"载波电话", "外线电话", "手机号码", "邮箱地址", "邮政编码", "联系地址" };

	public HrCContactAction() {
		// 取得人员基本信息管理处理远程对象
		empRemote = (HrJEmpInfoFacadeRemote) factory
				.getFacadeRemote("HrJEmpInfoFacade");
		remote = (HrCContactFacadeRemote) factory
				.getFacadeRemote("HrCContactFacade");
	}

	public void saveContactForm() {
		HrCContact conInfo = new HrCContact();
		if (conform.getContactId() != null
				&& !"".equals(conform.getContactId())) {
			conInfo.setContactId(conform.getContactId());
		}
		conInfo.setEmpId(conform.getEmpId());
		conInfo.setNewEmpCode(conform.getNewEmpCode());
		conInfo.setContactCarrier(conform.getContactCarrier());
		conInfo.setContactAddress(conform.getContactAddress());
		conInfo.setContactEmail(conform.getContactEmail());
		conInfo.setContactMobile(conform.getContactMobile());
		conInfo.setContactPostalcode(conform.getContactPostalcode());
		conInfo.setContactTel(conform.getContactTel());
		conInfo.setIsUse("Y");
		conInfo.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(conInfo);
		write("{success:true,msg:'保存成功！'}");
	}

	public void getContactForm() throws JSONException {
		String empId = request.getParameter("empId");
		conform = remote.findByEmpId(Long.parseLong(empId), employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(conform));
	}

	public void deleteContactForm() {
		remote.delete(conform);
	}

	public void importContactForm() throws IOException {
		InputStream is = null;
		Workbook workbook = null;
		// 返回到页面的reponseText
		String msg = "";
		List<HrCContact> contactFashionList = new ArrayList<HrCContact>();
		try {
			is = new FileInputStream(xlsFile);
			workbook = Workbook.getWorkbook(is);// 得到工作薄
			Sheet sheet = workbook.getSheet(0);// 得到工作薄中的第一个工作表
			int rows = sheet.getRows();// 得到excel的总行数
			if (rows == 0) {
				msg = "{success:true,msg:'无数据进行导入!'}";
			} else if (rows == 1) {
				msg = "{success:true,msg:'文件除一列头行外，至少还需一行数据!'}";
			} else {
				int[] column_indexs = new int[sheet.getRow(0).length];
				String columnError = null;
				columnError = addColumnIndex(column_indexs, sheet.getRow(0));
				// 列名是否存在问题
				if (columnError != null)
					write(columnError);
				for (int i = 1; i < rows; i++) {
					HrCContact contactFashion = new HrCContact();
					Cell[] cells = sheet.getRow(i);// i行的所有单元格
					for (int j = 0; j < column_indexs.length; j++) {
						/*
						 * 0:人员编码 1:载波电话 2:外线电话 3:手机号码 4:邮箱地址 5:邮政编码 6:联系地址
						 */
						// 0:人员编码
						if (column_indexs[j] == 0) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								Long empId = empRemote
										.getEmpIdByNewEmpCode(cells[j]
												.getContents());
								if (empId == null) {
									i++;
									msg = "第" + i + "行人员编码中:"
											+ cells[j].getContents()
											+ "的人员尚未维护！";
									break;
								} else
									contactFashion.setEmpId(empId);
							} else {
								i++;
								msg = "第" + i + "行人员编码必须填写！";
								break;
							}
						}

						// 1:载波电话
						else if (column_indexs[j] == 1) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								contactFashion.setContactCarrier(cells[j]
										.getContents());
							}
						}
						// 2:外线电话
						else if (column_indexs[j] == 2) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {
								contactFashion.setContactTel(cells[j]
										.getContents());
							}
						}
						// 3:手机号码
						else if (column_indexs[j] == 3) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								contactFashion.setContactMobile(cells[j]
										.getContents());

							}
						}
						// 4:邮箱地址
						else if (column_indexs[j] == 4) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								contactFashion.setContactEmail(cells[j]
										.getContents());

							}
						}
						// 5:邮政编码
						else if (column_indexs[j] == 5) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								contactFashion.setContactPostalcode(cells[j]
										.getContents());

							}
						}
						// 6:联系地址
						else if (column_indexs[j] == 6) {
							if (cells.length > j
									&& !"".equals(cells[j].getContents())) {

								contactFashion.setContactAddress(cells[j]
										.getContents());

							}
						}

					}
					if (msg.equals("")) {
						if (contactFashion.getEmpId() == null) {
							msg = "人员编码列必须存在，请检查导入的excel文件!";
							break;
						}

					}
					contactFashion.setEnterpriseCode(employee
							.getEnterpriseCode());
					contactFashion.setIsUse("Y");
					contactFashionList.add(contactFashion);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workbook.close();
			is.close();
		}

		if (msg.equals("")) {
			remote.importPersonnelFilesContact(contactFashionList);
			write("{success:true,msg:'导入成功！'}");
		} else {
			write("{success:true,msg:'数据填写存在问题：" + msg + "'}");
		}

	}

	/**
	 * 设置excel中列头对应的索引位组成的数组
	 */
	@SuppressWarnings("unused")
	private String addColumnIndex(int[] column_indexs, Cell[] cells) {
		for (int i = 0; i < cells.length; i++) {
			boolean isError = true;

			for (int j = 0; j < CONTACTFASHION_COLUMN_NAMES.length; j++) {
				if (CONTACTFASHION_COLUMN_NAMES[j].equals(cells[i]
						.getContents())) {
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

	public HrCContact getConform() {
		return conform;
	}

	public void setConform(HrCContact conform) {
		this.conform = conform;
	}

	public File getXlsFile() {
		return xlsFile;
	}

	public void setXlsFile(File xlsFile) {
		this.xlsFile = xlsFile;
	}

}