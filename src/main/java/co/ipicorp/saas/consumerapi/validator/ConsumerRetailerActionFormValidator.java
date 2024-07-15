/**
 * ConsumerRetailerActionFormValidator.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.validator;

import co.ipicorp.saas.consumerapi.form.ConsumerRetailerActionForm;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.RetailerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

/**
 * ConsumerRetailerActionFormValidator.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@Component
public class ConsumerRetailerActionFormValidator extends AbstractFormValidator {
    
    @Autowired
    private RetailerService rtService;

    @Override
    public boolean support(Serializable form) {
        return form instanceof ConsumerRetailerActionForm;
    }

    @Override
    public boolean doValidate(Serializable requestForm, Errors errors) {
        ConsumerRetailerActionForm form = (ConsumerRetailerActionForm) requestForm;

        Integer retailerId = form.getRetailerId();
        if (retailerId == null) {
            errors.reject("input retailerId is required");
            return false;
        }
        
        Integer actionType = form.getActionType();
        if (actionType == null) {
            errors.reject("input actionTypeId is required");
            return false;
        }
        
        Retailer retailer = rtService.getActivated(retailerId);
        if (retailer == null) {
            errors.reject("Retailer is not existed");
        }
        
        return false;
    }

}
