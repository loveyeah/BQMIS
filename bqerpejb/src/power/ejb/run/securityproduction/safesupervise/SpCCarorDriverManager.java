package power.ejb.run.securityproduction.safesupervise;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface SpCCarorDriverManager {

/**
 * 车辆档案维护查询列表
 * @param deptCode
 * @param enterpriseCode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject findSpCcarfileList(String likestr,String deptCode,String flag,String enterpriseCode,
			int... rowStartIdxAndCount);

/**
 * 通过照片主键、类型查找车辆、司机图片
 * @param id
 * @param type
 * @return
 */
public SpCCarordriverPhoto findByPhotoId(Long id,String type);

/**
 * 保存车辆、司机照片信息
 * @param entity
 */
public void saveCarordriverPhoto(SpCCarordriverPhoto entity);

/**
 * 修改车辆、司机图片
 * @param entity
 * @return
 */
public SpCCarordriverPhoto updateCarordriverPhoto(SpCCarordriverPhoto entity);


/**
 * 通过主键查找车辆档案详细信息
 * @param id
 * @return
 */
public SpCCarfile findByCarId(Long id);

/**
 * 核查车辆档案的牌照号码是否重复
 * @param entity
 * @return
 */
public boolean checkSpCcarfileCardNo(SpCCarfile entity);

/**
 * 车辆档案增加信息
 * @param entity
 * @return
 */
public SpCCarfile saveSpCcarfile(SpCCarfile entity);

/**]
 * 车辆档案修改信息
 * @param entity
 * @return
 */
public SpCCarfile updateSpCcarfile(SpCCarfile entity);

/**
 *删除车辆档案信息 
 * @param ids
 */
public void deleteSpCcarfile(String ids);

/**
 * 获取车辆最大主键
 * @return
 * @throws Exception
 */
public long GetMaxCarId() throws Exception;


/**
 * 查询司机档案信息列表
 * @param deptCode
 * @param enterpriseCode
 * @param rowStartIdxAndCount
 * @return
 */
public PageObject findSpCDriverList(String flag,String deptQuery,String likestr, String deptCode,
		String enterpriseCode, int... rowStartIdxAndCount) ;

/**
 * 通过主键查找司机信息
 * @param id
 * @return
 */
public SpCDriver findByDriverId(Long id);

/**
 * 核查司机驾驶证号的唯一性
 * @param entity
 * @return
 */
public boolean checkSpCDriverDriverCode(SpCDriver entity);

/**
 * 保存司机档案信息
 * @param entity
 * @return
 */
public SpCDriver saveSpCDriver(SpCDriver entity);

/**
 * 修改司机档案信息
 * @param entity
 * @return
 */
public SpCDriver updateSpCDriver(SpCDriver entity);

/**
 * 删除司机档案信息
 * @param ids
 */
public void deleteSpCDriver(String ids);

/**
 * 获得司机最大主键
 * @return
 * @throws Exception
 */
public long GetMaxDriverId() throws Exception;

/**
 * 获取籍贯combo信息
 * @return
 */
public List getNativePlaceData();

/**
 * 获取政治面貌combo信息
 * @return
 */
public List getPoliticsData();

/**
 *  车辆司机档案查询列表部门combox数据源
 * add by sychen 20100612
 * @param flag
 * @return
 */
public List getDeptListForCarOrDriver(String flag);
}
