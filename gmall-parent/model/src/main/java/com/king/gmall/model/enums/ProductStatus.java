package com.king.gmall.model.enums;

/***
 * ClassName: ProductStatus
 * Package: com.king.gmall.model.enums
 * @author GK
 * @date 2023/9/18 14:16
 * @description 商品状态枚举类 上架,下架
 * @version 1.0
 */
public enum ProductStatus {
    ON_SALE((short) 1),
    CANCEL_SALE((short) 0);
    private Short isSale;
    ProductStatus(Short isSale) {
        this.isSale = isSale;
    }

    public Short getIsSale() {
        return isSale;
    }
}
