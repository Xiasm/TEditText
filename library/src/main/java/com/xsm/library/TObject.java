package com.xsm.library;

/**
 * Author: Xiasem
 * Date: 17-2-13
 * Email: xiasem@163.com <br/>
 * Github: https://github.com/Xiasm
 *
 * <p>
 *      TObject,话题对象,可通过继承此对象,做一些逻辑上的处理
 * </p>
 */
public class TObject {

    private String objectRule = "#";// 匹配规则
    private String objectText;// 高亮文本

    public String getObjectRule() {
        return objectRule;
    }

    public void setObjectRule(String objectRule) {
        this.objectRule = objectRule;
    }

    public String getObjectText() {
        return objectText;
    }

    public void setObjectText(String objectText) {
        this.objectText = objectText;
    }

}
