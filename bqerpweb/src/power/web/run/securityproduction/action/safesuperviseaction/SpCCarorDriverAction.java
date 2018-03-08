package power.web.run.securityproduction.action.safesuperviseaction;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.BpJPlanJobDepMainFacadeRemote;
import power.ejb.run.securityproduction.safesupervise.SpCCarfile;
import power.ejb.run.securityproduction.safesupervise.SpCCarorDriverManager;
import power.ejb.run.securityproduction.safesupervise.SpCCarorDriverManagerImpl;
import power.ejb.run.securityproduction.safesupervise.SpCCarordriverPhoto;
import power.ejb.run.securityproduction.safesupervise.SpCDriver;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SpCCarorDriverAction extends AbstractAction {
	private SpCCarorDriverManager maintRemote;
	protected BpJPlanJobDepMainFacadeRemote mRemote;
	private SpCCarfile car;
	private SpCDriver driver;
	private File photo;
	private int start;
	private int limit;

	public SpCCarorDriverAction() {
		maintRemote = (SpCCarorDriverManager) factory
				.getFacadeRemote("SpCCarorDriverManagerImpl");
		mRemote = (BpJPlanJobDepMainFacadeRemote) factory
				.getFacadeRemote("BpJPlanJobDepMainFacade");
	}

	// 显示照片
	public void showphoto() throws Exception {
		SpCCarorDriverManager photobll = (SpCCarorDriverManager) Ejb3Factory
				.getInstance().getFacadeRemote("SpCCarorDriverManagerImpl");

		SpCCarordriverPhoto model = new SpCCarordriverPhoto();
		long id = 0;
		Object obj = request.getParameter("Id");
		id = Long.parseLong(obj.toString());
		String type = request.getParameter("type");
		model = photobll.findByPhotoId(id, type);
		if (model != null) {
			byte[] data = model.getPhoto();
			response.setContentType("image/jpeg");
			OutputStream outs = response.getOutputStream();
			for (int i = 0; i < data.length; i++) {
				outs.write(data[i]);// 输出到页面
			}
			outs.close();
		} else {
			write("http://localhost:8080/power/comm/images/UnknowBody.jpg");
		}
	}

	/**
	 * 取得照片数据
	 * 
	 * @throws IOException
	 */
	private byte[] getPhotoData() throws IOException {
		InputStream bis = null;

		if (photo == null) {
			return null;
		}
		try {
			bis = new BufferedInputStream(new FileInputStream(photo), 1024 * 16);

			int intLength = (int) photo.length();
			byte[] data = new byte[intLength];

			bis.read(data);
			return data;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	/**
	 * 车辆档案维护
	 * 
	 * @throws Exception
	 */
	public void saveSpCcarfile() throws Exception {
		boolean existFlag = false;
		// String abs = mRemote.getManagerDept(employee.getDeptId());
		// String[] arr = abs.split(",");
		// String deptCode = "";
		// if(arr != null && arr.length > 0)
		// deptCode = arr[0];
		//	
		existFlag = maintRemote.checkSpCcarfileCardNo(car);
		if (existFlag == true) {
			write("{success:true,existFlag:" + existFlag + "}");
		} else {
			String strFilePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			// 如果有文件上传
			if (strFilePath != null && strFilePath.length() > 0) {
				// 取得照片数据
				byte[] photoData = getPhotoData();

				SpCCarorDriverManagerImpl carPhoto = new SpCCarorDriverManagerImpl();
				long id = carPhoto.GetMaxCarId();
				// 通过车辆ID取得员工照片信息
				SpCCarordriverPhoto photoInfo = maintRemote.findByPhotoId(id,
						type);
				if (photoInfo == null) {
					// 增加车辆照片信息
					photoInfo = new SpCCarordriverPhoto();
					photoInfo.setCarDriverId(id);
					photoInfo.setType("C");
					// 照片
					photoInfo.setPhoto(photoData);
					photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					photoInfo.setLastModifiedDate(new Date());
					photoInfo.setIsUse("Y");
					maintRemote.saveCarordriverPhoto(photoInfo);
				} else {
					photoInfo.setPhoto(photoData);
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					maintRemote.updateCarordriverPhoto(photoInfo);
				}
			}
			car.setEnterpriseCode(employee.getEnterpriseCode());
			car.setLastModifiedBy(employee.getWorkerCode());
			car.setDeptCode(employee.getDeptCode());
			// car.setDeptCode(deptCode);
			car.setLastModifiedTime(new Date());
			maintRemote.saveSpCcarfile(car);
			write("{success:true,msg:'增加成功！'}");
		}
	}

	public void updateSpCcarfile() throws Exception {
		// String abs = mRemote.getManagerDept(employee.getDeptId());
		// String[] arr = abs.split(",");
		// String deptCode = "";
		// if(arr != null && arr.length > 0)
		// deptCode = arr[0];

		boolean existFlag = false;
		existFlag = maintRemote.checkSpCcarfileCardNo(car);
		if (existFlag == true) {
			write("{success:true,existFlag:" + existFlag + "}");
		} else {
			String strFilePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			// 如果有文件上传
			if (strFilePath != null && strFilePath.length() > 0) {

				// 取得照片数据
				byte[] photoData = getPhotoData();

				// 通过车辆ID取得员工照片信息
				SpCCarordriverPhoto photoInfo = maintRemote.findByPhotoId(car
						.getCarId(), type);
				if (photoInfo == null) {
					// 增加车辆照片信息
					photoInfo = new SpCCarordriverPhoto();
					photoInfo.setCarDriverId(car.getCarId());
					photoInfo.setType("C");
					photoInfo.setPhoto(photoData);
					photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					photoInfo.setLastModifiedDate(new Date());
					photoInfo.setIsUse("Y");
					maintRemote.saveCarordriverPhoto(photoInfo);
				} else {
					photoInfo.setPhoto(photoData);
					photoInfo.setLastModifiedBy(employee.getWorkerCode());

					maintRemote.updateCarordriverPhoto(photoInfo);
				}
			}
			SpCCarfile entity = maintRemote.findByCarId(car.getCarId());
			entity.setIsUse("Y");
			entity.setCarNo(car.getCarNo());
			entity.setBelongTo(car.getBelongTo());
			entity.setRightCode(car.getRightCode());
			entity.setFactoryType(car.getFactoryType());
			entity.setCarType(car.getCarType());
			entity.setCarColor(car.getCarColor());
			entity.setSeeSize(car.getSeeSize());
			entity.setFuelType(car.getFuelType());
			entity.setTireType(car.getTireType());
			entity.setWheelbase(car.getWheelbase());
			entity.setPassergerCapacity(car.getPassergerCapacity());
			if (car.getInOut() != null && !"".equals(car.getInOut())
					&& !"null".equals(car.getInOut())) {
				entity.setInOut(car.getInOut());
			}
			entity.setOutFactoryDate(car.getOutFactoryDate());
			entity.setFirstRegisterDate(car.getFirstRegisterDate());
			entity.setEngineCode(car.getEngineCode());
			entity.setDiscernCode(car.getDiscernCode());
			entity.setSupplier(car.getSupplier());
			entity.setLastModifiedBy(employee.getWorkerCode());
			entity.setDeptCode(employee.getDeptCode());
			// entity.setDeptCode(deptCode);
			entity.setLastModifiedTime(new Date());
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			maintRemote.updateSpCcarfile(entity);
			write("{success:true,msg:'修改成功！'}");
		}

	}

	public void deleteSpCcarfile() {
		String ids = request.getParameter("carIds");
		maintRemote.deleteSpCcarfile(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void getSpCcarfileList() throws JSONException {
		String flag = request.getParameter("flag");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String likestr = request.getParameter("likestr");
		String dept = request.getParameter("dept");// add by sychen 20100612

		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String deptCode = "";
		if (arr != null && arr.length > 0)
			deptCode = arr[0];

		PageObject pg = null;
		if (flag != null && flag.equals("F")) {
			if (start != null && limit != null && !start.equals(""))
				pg = maintRemote.findSpCcarfileList(likestr, deptCode, flag,
						employee.getEnterpriseCode(), Integer.parseInt(start),
						Integer.parseInt(limit));
			else
				pg = maintRemote.findSpCcarfileList(likestr, deptCode, flag,
						employee.getEnterpriseCode());
		}
		// add by sychen 20100612 车辆档案查询列表
		else if (flag != null && flag.equals("query")) {
			if (start != null && limit != null && !start.equals(""))
				pg = maintRemote.findSpCcarfileList(likestr, dept, flag,
						employee.getEnterpriseCode(), Integer.parseInt(start),
						Integer.parseInt(limit));
			else
				pg = maintRemote.findSpCcarfileList(likestr, dept, flag,
						employee.getEnterpriseCode());
		}
		// add by sychen 20100612 end
		else {
			if (start != null && limit != null && !start.equals(""))
				pg = maintRemote.findSpCcarfileList(likestr, employee
						.getDeptCode(), flag, employee.getEnterpriseCode(),
						Integer.parseInt(start), Integer.parseInt(limit));
			else
				pg = maintRemote.findSpCcarfileList(likestr, employee
						.getDeptCode(), flag, employee.getEnterpriseCode());
		}
		write(JSONUtil.serialize(pg));

	}

	/**
	 * 司机档案维护
	 * 
	 * @throws Exception
	 */
	public void saveSpCDriver() throws Exception {
		boolean existFlag = false;
		existFlag = maintRemote.checkSpCDriverDriverCode(driver);
		if (existFlag == true) {
			write("{success:true,existFlag:" + existFlag + "}");
		} else {
			String strFilePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			// 如果有文件上传
			if (strFilePath != null && strFilePath.length() > 0) {
				// 取得照片数据
				byte[] photoData = getPhotoData();
				SpCCarorDriverManagerImpl driverPhoto = new SpCCarorDriverManagerImpl();
				long id = driverPhoto.GetMaxDriverId();
				// 通过司机ID取得员工照片信息
				SpCCarordriverPhoto photoInfo = maintRemote.findByPhotoId(id,
						type);
				if (photoInfo == null) {
					// 增加司机照片信息
					photoInfo = new SpCCarordriverPhoto();
					photoInfo.setCarDriverId(id);
					photoInfo.setType("D");
					// 照片
					photoInfo.setPhoto(photoData);
					photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					photoInfo.setLastModifiedDate(new Date());
					photoInfo.setIsUse("Y");
					maintRemote.saveCarordriverPhoto(photoInfo);
				} else {
					photoInfo.setPhoto(photoData);
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					maintRemote.updateCarordriverPhoto(photoInfo);
				}
			}
			System.out.println("the deptcode" + driver.getDeptCode());
			driver.setEnterpriseCode(employee.getEnterpriseCode());
			// driver.setDeptCode(employee.getDeptCode());
			maintRemote.saveSpCDriver(driver);

			write("{success:true,msg:'增加成功！'}");

		}
	}

	public void updateSpCDriver() throws Exception {
		boolean existFlag = false;
		existFlag = maintRemote.checkSpCDriverDriverCode(driver);
		if (existFlag == true) {
			write("{success:true,existFlag:" + existFlag + "}");
		} else {
			String strFilePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			// 如果有文件上传
			if (strFilePath != null && strFilePath.length() > 0) {

				// 取得照片数据
				byte[] photoData = getPhotoData();

				// 通过司机ID取得员工照片信息
				SpCCarordriverPhoto photoInfo = maintRemote.findByPhotoId(
						driver.getDriverId(), type);
				if (photoInfo == null) {
					// 增加司机照片信息
					photoInfo = new SpCCarordriverPhoto();
					photoInfo.setCarDriverId(driver.getDriverId());
					photoInfo.setType("D");
					photoInfo.setPhoto(photoData);
					photoInfo.setEnterpriseCode(employee.getEnterpriseCode());
					photoInfo.setLastModifiedBy(employee.getWorkerCode());
					photoInfo.setLastModifiedDate(new Date());
					photoInfo.setIsUse("Y");
					maintRemote.saveCarordriverPhoto(photoInfo);
				} else {
					photoInfo.setPhoto(photoData);
					photoInfo.setLastModifiedBy(employee.getWorkerCode());

					maintRemote.updateCarordriverPhoto(photoInfo);
				}
			}
			SpCDriver entity = maintRemote.findByDriverId(driver.getDriverId());
			entity.setIsUse("Y");
			entity.setDriverName(driver.getDriverName());
			if (driver.getSex() != null && !"".equals(driver.getSex())
					&& !"null".equals(driver.getSex())) {
				entity.setSex(driver.getSex());
			}
			if (driver.getNativePlaceId() != null
					&& !"".equals(driver.getNativePlaceId())
					&& !"null".equals(driver.getNativePlaceId())) {
				entity.setNativePlaceId(driver.getNativePlaceId());
			}
			entity.setBrithday(driver.getBrithday());
			if (driver.getDeptCode() != null
					&& !"".equals(driver.getDeptCode())
					&& !"null".equals(driver.getDeptCode())) {
				entity.setDeptCode(driver.getDeptCode());
			}
			entity.setWorkTime(driver.getWorkTime());
			entity.setHomePhone(driver.getHomePhone());
			if (driver.getPoliticsId() != null
					&& !"".equals(driver.getPoliticsId())
					&& !"null".equals(driver.getPoliticsId())) {
				entity.setPoliticsId(driver.getPoliticsId());
			}
			entity.setJoinInTime(driver.getJoinInTime());
			entity.setMobilePhone(driver.getMobilePhone());
			entity.setDriveCode(driver.getDriveCode());
			entity.setAllowDriverType(driver.getAllowDriverType());
			entity.setMemo(driver.getMemo());
			entity.setEnterpriseCode(employee.getEnterpriseCode());
			maintRemote.updateSpCDriver(entity);
			write("{success:true,msg:'修改成功！'}");
		}

	}

	public void deleteSpCDriver() {
		String ids = request.getParameter("driverIds");
		maintRemote.deleteSpCDriver(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public void getSpCDriverList() throws JSONException {

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String likestr = request.getParameter("likestr");
		String flag = request.getParameter("flag");// add by sychen 20100612
		String deptQuery = request.getParameter("deptQuery");// add by sychen 20100612
		// add by ltong
		String abs = mRemote.getManagerDept(employee.getDeptId());
		String[] arr = abs.split(",");
		String deptCode = "";
		if (arr != null && arr.length > 0)
			deptCode = arr[0];

		PageObject pg = null;
		if (start != null && limit != null && !start.equals(""))
			pg = maintRemote.findSpCDriverList(flag,deptQuery,likestr, deptCode, employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = maintRemote.findSpCDriverList(flag,deptQuery,likestr, deptCode, employee
					.getEnterpriseCode());
		write(JSONUtil.serialize(pg));

	}

	/**
	 * 获得籍贯combo数据源
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getNativePlaceData() throws JSONException {
		List list = maintRemote.getNativePlaceData();
		write(JSONUtil.serialize(list));
	}

	/**
	 * 获得政治面貌combo数据源
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getPoliticsData() throws JSONException {
		List list = maintRemote.getPoliticsData();
		write(JSONUtil.serialize(list));
	}
	
	
	@SuppressWarnings("unchecked")
	public void getDeptListForCarOrDriver() throws JSONException
	{
		String flag = request.getParameter("flag");
			List list =maintRemote.getDeptListForCarOrDriver(flag);
			write(JSONUtil.serialize(list));
	}

	public SpCCarfile getCar() {
		return car;
	}

	public void setCar(SpCCarfile car) {
		this.car = car;
	}

	public SpCDriver getDriver() {
		return driver;
	}

	public void setDriver(SpCDriver driver) {
		this.driver = driver;
	}

	public File getPhoto() {
		return photo;
	}

	public void setPhoto(File photo) {
		this.photo = photo;
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
