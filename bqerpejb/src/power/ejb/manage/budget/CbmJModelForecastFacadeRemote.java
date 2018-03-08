package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.form.ForecastItemForm;

/**
 * 预测指标数据表
 * 
 * @author liuyi 090826
 */
@Remote
public interface CbmJModelForecastFacadeRemote {
	/**
	 * 新增一条预测指标数据表记录
	 */
	public CbmJModelForecast save(CbmJModelForecast entity);

	/**
	 * 删除一条预测指标数据表记录
	 */
	public void delete(CbmJModelForecast entity);

	/**
	 * 更新一条预测指标数据表记录
	 */
	public CbmJModelForecast update(CbmJModelForecast entity);

	public CbmJModelForecast findById(Long id);

	public PageObject findAllModelData(String forecastTime, String modelType,
			String enterpriseCode, int... rowStartIdxAndCount);

	/**
	 * 量本利模型分析页面数据
	 */
	public PageObject findModelAnalysisData(String forecastTime,
			String enterpriseCode, int... rowStartIdxAndCount);

	public void saveForecastItem(List<ForecastItemForm> modList);
}