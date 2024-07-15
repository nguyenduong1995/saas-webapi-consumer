/**
 * RetailerSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * RetailerSearchForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class RetailerSearchForm extends LimittedForm {
    
    /**
     * 
     */
    private static final long serialVersionUID = 3866533636956473439L;
    private String keyword;

    /**
     * get value of <b>keyword</b>.
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Set value to <b>keyword</b>.
     * @param keyword the keyword to set
     */
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "RetailerSearchForm [keyword=" + keyword + ", getSegment()=" + getSegment() + ", getOffset()=" + getOffset() + "]";
    }

}
