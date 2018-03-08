package power.ejb.hr;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.resource.InvCLocation;

/**
 * Facade for entity HrCRewardspunish.
 * 
 * @see power.ejb.hr.HrCRewardspunish
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCRewardspunishFacade implements HrCRewardspunishFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;


	
	public void save(HrCRewardspunish entity) {
		LogUtil.log("saving HrCRewardspunish instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(HrCRewardspunish entity) {
		LogUtil.log("deleting HrCRewardspunish instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCRewardspunish.class, entity
					.getRewardsPunishId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrCRewardspunish update(HrCRewardspunish entity) {
		LogUtil.log("updating HrCRewardspunish instance", Level.INFO, null);
		try {
			HrCRewardspunish result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCRewardspunish findById(Long id) {
		LogUtil.log("finding HrCRewardspunish instance with id: " + id,
				Level.INFO, null);
		try {
			HrCRewardspunish instance = entityManager.find(
					HrCRewardspunish.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	/**
     * 查询
     * @param enterpriseCode 企业编码
     * @param rowStartIdxAndCount 分页
     * @return PageObject  查询结果
     */
    @SuppressWarnings("unchecked")
    public PageObject findAllRewards(String enterpriseCode, final int... rowStartIdxAndCount) throws SQLException{
        LogUtil.log("finding all HrCRewardspunish instances", Level.INFO, null);
        try {
            PageObject result = new PageObject();
            // 查询sql
            String sql=
                "select * \n" +
                " FROM HR_C_REWARDSPUNISH R \n"+
                "where  R.enterprise_code= ? \n" +
                "and R.is_use= ? \n"+
                "order by R.order_by asc " ;
            List<HrCRewardspunish> list=bll.queryByNativeSQL(sql,new Object[]{enterpriseCode,"Y"}, HrCRewardspunish.class, rowStartIdxAndCount);
            String sqlCount=
            	"select COUNT(R.REWARDS_PUNISH_ID) \n" +
                " FROM HR_C_REWARDSPUNISH R \n"+
                "where  R.enterprise_code= ? \n" +
                "and R.is_use=? \n"+
                "order by R.order_by asc " ;
            Long totalCount=Long.parseLong(bll.getSingal(sqlCount,new Object[]{enterpriseCode,"Y"}).toString());
            result.setList(list);
            result.setTotalCount(totalCount);
            return result;

        } catch (RuntimeException re) {
            LogUtil.log("find all HrCRewardspunish failed", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @throws runtimeException
     */
    public HrCRewardspunish addReward(HrCRewardspunish entity) throws SQLException{
        LogUtil.log("saving HrCRewardspunish instance", Level.INFO, null);
        try {
            if (entity.getRewardsPunishId() == null) {
                // 设定主键值
                entity.setRewardsPunishId(bll.getMaxId("HR_C_REWARDSPUNISH",
                        "REWARDS_PUNISH_ID"));
            }
            // 设定修改时间
            entity.setLastModifiedDate(new java.util.Date());
            // 设定是否使用
            entity.setIsUse("Y");
            // 保存
            entityManager.persist(entity);
            LogUtil.log("save HrCRewardspunish instance successful", Level.INFO, null);
            return entity;

        } catch (RuntimeException re) {
            LogUtil.log("save HrCRewardspunish instance  failed", Level.SEVERE, re);
            throw new SQLException();
        }

    }
    /**
     * 删除一条奖惩类别信息
     *
     * @param 奖惩名称ID
     * @param workerCode 登录者id
     * @throws RuntimeException
     *             
     */
    public void deleteByRewardsPunishId(HrCRewardspunish entity) throws SQLException{
        LogUtil.log("deleting HrCRewardspunish instance", Level.INFO, null);
        try {
        	// 设置最后修改时间
		    entity.setLastModifiedDate(new Date());
		    this.update(entity);
        } catch (RuntimeException re) {
            LogUtil.log("delete HrCRewardspunish failed", Level.SEVERE, re);
            throw new SQLException();
        }
    }
    /**
     * 修改一条记录
     *
     * @param entity  要修改的记录
     * @return InvCLocation  修改的记录
     * @throws CodeRepeatException
     */
    public HrCRewardspunish updateReward(HrCRewardspunish entity)throws SQLException {
        LogUtil.log("updating HrCRewardspunish instance", Level.INFO, null);
        try {
            // 修改时间
            entity.setLastModifiedDate(new Date());
            HrCRewardspunish result = entityManager.merge(entity);
            LogUtil.log("update successful", Level.INFO, null);
            return result;
        } catch (RuntimeException re) {
            LogUtil.log("update HrCRewardspunish failed", Level.SEVERE, re);
            throw new SQLException();
        }
    }
}