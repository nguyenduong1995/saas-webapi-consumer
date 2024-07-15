/**
 * ConsumerRetailerActionForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

/**
 * ConsumerRetailerActionForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class ConsumerRetailerActionForm implements Serializable {

    private static final long serialVersionUID = 2657762355773224651L;

    private Integer actionType;
    
    private Integer retailerId;

    /**
     * get value of <b>actionType</b>.
     * @return the actionType
     */
    public Integer getActionType() {
        return actionType;
    }

    /**
     * Set value to <b>actionType</b>.
     * @param actionType the actionType to set
     */
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    /**
     * get value of <b>retailerId</b>.
     * @return the retailerId
     */
    public Integer getRetailerId() {
        return retailerId;
    }

    /**
     * Set value to <b>retailerId</b>.
     * @param retailerId the retailerId to set
     */
    public void setRetailerId(Integer retailerId) {
        this.retailerId = retailerId;
    }

    @Override
    public String toString() {
        return "ConsumerRetailerActionForm [actionType=" + actionType + ", retailerId=" + retailerId + "]";
    }
    
}
