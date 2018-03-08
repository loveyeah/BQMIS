/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.resource.business;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.form.BookCheckInfo;
/**
 * 物料盘点调整implements
 * @author huangweijie
 */
@Stateless
public class BookCheckImp implements BookCheck{
    @EJB(beanName = "NativeSqlHelper")
    protected NativeSqlHelperRemote bll;
    @EJB (beanName="InvCTransactionFacade")
    protected InvCTransactionFacadeRemote transremote;
    @EJB (beanName="BpCMeasureUnitFacade")
    protected BpCMeasureUnitFacadeRemote infoRemote;
    
    /**
     * 查询盘点单号
     * @param String bookNo 盘点单号Id
     */
    @SuppressWarnings("unchecked")
	public PageObject findBookDetails(String bookNo, String enterpriseCode, final int... rowStartIdxAndCount){
        LogUtil.log("finding BookCheckInfo instance with bookNo: " + bookNo,
                Level.INFO, null);
        try {
            PageObject pobj = new PageObject();
            // 取得事务作用
            List<InvCTransaction> transList = transremote.findByTransCode("A");
            String sql=
            "SELECT DISTINCT\n" + 
                "B.ID, A.BOOK_ID, B.WHS_NO, B.LOCATION_NO,\n" +
                "C.MATERIAL_ID, C.MATERIAL_NO, C.MATERIAL_NAME,\n" + 
                "C.SPEC_NO, C.STOCK_UM_ID, D.WHS_NAME, E.LOCATION_NAME,\n" +
                "B.LOT_NO, B.BOOK_QTY, to_char(A.LAST_MODIFIED_DATE,'yyyy-mm-dd hh24:mi:ss')\n" +
            "FROM\n" +
                "INV_J_BOOK A,\n" +
                "INV_J_BOOK_DETAILS B,\n" +
                "INV_C_MATERIAL C,\n" +
                "INV_C_WAREHOUSE D,\n" +
                "INV_C_LOCATION E\n" +
            "WHERE\n" +
                "A.BOOK_NO = ? AND\n" +
                "A.BOOK_NO = B.BOOK_NO AND\n" +
                "B.MATERIAL_ID = C.MATERIAL_ID AND\n" +
                "B.WHS_NO = D.WHS_NO AND\n" +
                "B.WHS_NO = E.WHS_NO(+) AND\n" +
                "B.LOCATION_NO = E.LOCATION_NO(+) AND\n" +
                "A.BOOK_STATUS = 'PRT' AND\n" +
                "A.IS_USE = 'Y' AND\n" +
                "A.ENTERPRISE_CODE = ? AND\n" +
                "B.IS_USE = 'Y' AND\n" +
                "B.ENTERPRISE_CODE = ? AND\n" +
                "C.IS_USE = 'Y' AND\n" +
                "C.ENTERPRISE_CODE = ? AND\n" +
                "D.IS_USE = 'Y' AND\n" +
                "D.ENTERPRISE_CODE = ? AND\n" +
                "E.IS_USE(+) = 'Y' AND\n" +
                "E.ENTERPRISE_CODE(+) = ?\n" +
                "ORDER BY C.MATERIAL_ID";
            List list = bll.queryByNativeSQL(sql, new Object[]{bookNo, 
            		enterpriseCode, enterpriseCode, enterpriseCode, enterpriseCode,
            		enterpriseCode}, rowStartIdxAndCount);
            List<BookCheckInfo> arrlist = new ArrayList<BookCheckInfo>();
            Iterator it = list.iterator();
            while (it.hasNext()) { 
                BookCheckInfo bookinfo = new BookCheckInfo();                
                Object[] data = (Object[]) it.next();
                // 物料盘点明细表流水号
                if(null != data[0])
                bookinfo.setBookDetailId(Long.parseLong(data[0].toString()));
                // 物料盘点表id
                if(null != data[1])
                bookinfo.setBookId(Long.parseLong(data[1].toString()));
                // 仓库号
                if(null != data[2]) {
                    bookinfo.setWhsNo(data[2].toString());
                }
                // 库位号
                if(null != data[3])
                bookinfo.setLocationNo(data[3].toString());
                // 物料主文件流水号
                if(null != data[4])
                bookinfo.setMaterialId(Long.parseLong(data[4].toString()));
                // 物料主文件编码
                if(null != data[5])
                bookinfo.setMaterialNo(data[5].toString());
                // 物料名称
                if(null != data[6])
                bookinfo.setMaterialName(data[6].toString());
                // 规格型号
                if (null != data[7]) {
                    bookinfo.setSpecNo(data[7].toString());
                } else {
                    bookinfo.setSpecNo("");
                }
                // 存货计量单位
                if (null != data[8]) {
					String stockUmNo = data[8].toString();
					BpCMeasureUnit bp = infoRemote.findById(Long.parseLong(stockUmNo));
					bookinfo.setStockUmName(bp.getUnitName());
                } 
                // 仓库名称
                bookinfo.setWhsName(data[9].toString());
                // 库位名称，判断库位no是否为空，为空则名称为空
                if (null == data[3] || "".equals(data[3].toString())) {
                    bookinfo.setLocationName(null);
                } else {
                    bookinfo.setLocationName(data[10].toString());
                }
                // 批号
                bookinfo.setLotNo(data[11].toString());
                // 账面数量
                bookinfo.setBookQty(Double.parseDouble(data[12].toString()));
                // 上次修改时间
                bookinfo.setModifyDate(data[13].toString());
                // 事务影响
                if (transList.size() > 0) {
                    bookinfo.setTrans(transList.get(0));
                }
                arrlist.add(bookinfo);
            }
            if(arrlist.size()>0)
            {
                String sqlCount = 
                "SELECT\n" + 
                    "count(1)" +
                "FROM\n" +
                    "INV_J_BOOK A,\n" +
                    "INV_J_BOOK_DETAILS B,\n" +
                    "INV_C_MATERIAL C,\n" +
                    "INV_C_WAREHOUSE D,\n" +
                    "INV_C_LOCATION E\n" +
                "WHERE\n" +
                    "A.BOOK_NO = ? AND\n" +
                    "A.BOOK_NO = B.BOOK_NO AND\n" +
                    "B.MATERIAL_ID = C.MATERIAL_ID AND\n" +
                    "B.WHS_NO = D.WHS_NO AND\n" +
                    "B.WHS_NO = E.WHS_NO(+) AND\n" +
                    "B.LOCATION_NO = E.LOCATION_NO(+) AND\n" +
                    "A.BOOK_STATUS = 'PRT' AND\n" +
                    "A.IS_USE = 'Y' AND\n" +
                    "A.ENTERPRISE_CODE = ? AND\n" +
                    "B.IS_USE = 'Y' AND\n" +
                    "B.ENTERPRISE_CODE = ? AND\n" +
                    "C.IS_USE = 'Y' AND\n" +
                    "C.ENTERPRISE_CODE = ? AND\n" +
                    "D.IS_USE = 'Y' AND\n" +
                    "D.ENTERPRISE_CODE = ? AND\n" +
                    "E.IS_USE(+) = 'Y' AND\n" +
                    "E.ENTERPRISE_CODE(+) = ?\n" +
                    "ORDER BY C.MATERIAL_ID";
                Long totalCount = Long.parseLong(bll
                    .getSingal(sqlCount, new Object[] {bookNo, 
                    		enterpriseCode, enterpriseCode, enterpriseCode, enterpriseCode,
                    		enterpriseCode}).toString());
                pobj.setList(arrlist);
                pobj.setTotalCount(totalCount);
            }    
            return pobj;
        } catch (RuntimeException e) {
            LogUtil.log("find all failed", Level.SEVERE, e);
            throw e;
        }
    }
}