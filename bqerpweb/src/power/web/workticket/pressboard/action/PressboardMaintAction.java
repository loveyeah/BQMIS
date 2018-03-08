/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.workticket.pressboard.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import power.ear.comm.CodeRepeatException;
import power.ejb.workticket.RunCWorkticketPressboard;
import power.ejb.workticket.RunCWorkticketPressboardFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;

/**
 * 压板维护处理Action
 * @author huyou
 *
 */
public class PressboardMaintAction extends AbstractAction{

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** 压板处理远程对象 */
    private RunCWorkticketPressboardFacadeRemote remote;
    /** 压板 */
    private RunCWorkticketPressboard pressboard;
    /** 压板节点ID */
    private Long node;
    
    /**
     * 构造函数
     */
    public PressboardMaintAction () {
        // 取得压板处理远程对象
        remote = (RunCWorkticketPressboardFacadeRemote) factory.getFacadeRemote("RunCWorkticketPressboardFacade");
    }
    
    /**
     * 取得压板
     */
    public RunCWorkticketPressboard getPressboard() {
        return pressboard;
    }
    
    /**
     * 设置压板
     * @param argPressboard 压板
     */
    public void setPressboard(RunCWorkticketPressboard argPressboard) {
        pressboard = argPressboard;
    }

    /**
     * 取得压板节点ID
     */
    public Long getNode() {
        return node;
    }
    
    /**
     * 设置压板节点ID
     * @param argNode 压板节点ID
     */
    public void setNode(Long argNode) {
        node = argNode;
    }

    /**
     * 根据父压板id, 获得其子压板列表
     * 
     * @throws JSONException
     */
    public void getPressboardTreeNode() throws JSONException {
        // 序列化为JSON对象的字符串形式
        String str = toJSONStr(remote.findByParentPressboardId(node));
        // 以html方式输出字符串
        write(str);
    }

    /**
     * 增加压板
     * 
     * @throws Exception
     */
    public void addPressboard() throws Exception {
        pressboard.setEnterpriseCode(Constants.ENTERPRISE_CODE);
        RunCWorkticketPressboard parentPressBoard = new RunCWorkticketPressboard();
        // 如果不是根节点
        if (pressboard.getParentPressboardId().longValue() != 0L) {
            // 查找父节点
            parentPressBoard = remote.findById(pressboard.getParentPressboardId());
            if(Constants.IS_USE_Y.equals(parentPressBoard.getIsLeaf())){
                // 设置父节点类型
                parentPressBoard.setIsLeaf(Constants.IS_USE_N);
                // 设置填写人
                parentPressBoard.setModifyBy(employee.getWorkerCode());
                // 设置填写时间
                parentPressBoard.setModifyDate(new Date());
                remote.update(parentPressBoard);
            }
        }

        try {
            // 压板id
            pressboard.setPressboardId(null);
            // 填写人
            pressboard.setModifyBy(employee.getWorkerCode());
            // 填写日期
            pressboard.setModifyDate(new Date());

            // 增加一条压板记录
            remote.save(pressboard);
            write(Constants.ADD_SUCCESS);
        } catch (CodeRepeatException e) {
            write(Constants.ADD_FAILURE);
            throw e;
        } finally {
            pressboard.setPressboardId(new Long(-1));
        }
    }

    /**
     * 修改压板
     */
    public void updatePressboard() throws Exception {
        // 查找这条压板记录
        RunCWorkticketPressboard model = remote.findById(pressboard
                .getPressboardId());
        // 压板编号
        model.setPressboardCode(pressboard.getPressboardCode());
        // 压板类型名称
        model.setPressboardName(pressboard.getPressboardName());
        // 排序
        model.setOrderBy(pressboard.getOrderBy());
        if (model.getOrderBy() == null) {
            // 若排序字段为空，在库中排序字段存入压板id
            model.setOrderBy(pressboard.getPressboardId());
        }
        // 填写人
        model.setModifyBy(employee.getWorkerCode());
        // 填写日期
        model.setModifyDate(new Date());
        model.setIsLeaf(pressboard.getIsLeaf());

        try {
            // 修改这条压板记录
            remote.update(model);

            write(Constants.MODIFY_SUCCESS);
        } catch (CodeRepeatException e) {
            write(Constants.MODIFY_FAILURE);
            throw e;
        }
    }

    /**
     * 删除压板
     */
    public void deletePressboard() throws Exception {
        // 从请求中获得删除的ID
        String strId = request.getParameter("id");

        try {
            // 查找这条压板记录
            RunCWorkticketPressboard model = remote.findById(new Long(strId));
            // 设置填写人
            model.setModifyBy(employee.getWorkerCode());
            // 设置填写时间
            model.setModifyDate(new Date());
            // 删除压板记录
            remote.delete(model);

            write(Constants.DELETE_SUCCESS);
        } catch (CodeRepeatException e) {
            write(Constants.DELETE_FAILURE);
            throw e;
        }
    }
    
    /**
     * 将list转换为json格式数据
     * 
     * @param argPressboardList
     *            压板
     * @return json格式数据
     */
    private String toJSONStr(List<RunCWorkticketPressboard> argPressboardList) {
        StringBuffer sbf = new StringBuffer();
        sbf.append("[");
        
        RunCWorkticketPressboard board = null;
        for (int intCnt = 0; intCnt < argPressboardList.size(); intCnt++) {
            // 压板数据对象
            board = argPressboardList.get(intCnt);
            // 节点ID
            String strPressboardId = getEmptyString(board.getPressboardId());
            // 是否是叶子节点
            boolean isLeaf = !getEmptyString(board.getIsLeaf()).equals("N");
            
            sbf.append("{\"text\":\"" + board.getPressboardName()
                    + "\",\"id\":\"" + strPressboardId
                    + "\",\"leaf\":" + isLeaf
                    + ",\"pressboardCode\":\"" + board.getPressboardCode()
                    + "\",\"orderBy\":\"" + getEmptyString(board.getOrderBy())
                    + "\",\"modifyBy\":\"" + board.getModifyBy()
                    + "\",\"modifyDate\":\"" + formatDate(board.getModifyDate(), "yyyy-MM-dd")
                    + "\"},"
                    );
        }

        if (sbf.length() > 1) {
            sbf.deleteCharAt(sbf.lastIndexOf(","));
        }
        
        sbf.append("]");
        return sbf.toString();
    }
    
    /**
     * 根据日期和形式返回日期字符串
     * @param argDate 日期
     * @param argFormat 日期形式字符串
     * @return 日期字符串
     */
    private String formatDate(Date argDate, String argFormat) {
        // 日期形式
        SimpleDateFormat sdfFrom = null;
        // 返回字符串
        String strResult = null;

        try {
            sdfFrom = new SimpleDateFormat(argFormat);
            // 格式化日期
            strResult = sdfFrom.format(argDate).toString();
        } catch (Exception e) {
            strResult = "";
        } finally {
            sdfFrom = null;
        }

        return strResult;
    }
    
    /**
     * 返回非NULL字符串
     * @param argObj 对象
     * @return 非NULL字符串
     */
    private String getEmptyString(Object argObj) {
        return argObj == null ? "" : argObj.toString();
    }
}
