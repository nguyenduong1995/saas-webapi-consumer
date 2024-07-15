package co.ipicorp.saas.consumerapi.validator;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.consumerapi.form.ConsumerChangePasswordForm;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ConsumerChangePasswordValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
    	ConsumerChangePasswordForm consumerChangePasswordForm = (ConsumerChangePasswordForm) form;
        return this.validate(consumerChangePasswordForm, errors);
    }
    
    private boolean validate(ConsumerChangePasswordForm consumerChangePasswordForm, Errors errors) {
    	if (StringUtils.isEmpty(consumerChangePasswordForm.getCurrentPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Current password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
    	if (StringUtils.isEmpty(consumerChangePasswordForm.getNewPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "New password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
    	if (StringUtils.isEmpty(consumerChangePasswordForm.getConfirmPassword())) {
    		errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Confirm password"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
    	}
    	
        if (consumerChangePasswordForm.getCurrentPassword().equals(consumerChangePasswordForm.getNewPassword())) {
        	errors.reject(ErrorCode.APP_2201_NEW_PASSWORD_IS_SAME_CURRENT_PASSWORD,
                    new Object[] { "Password"},
                    ErrorCode.APP_2201_NEW_PASSWORD_IS_SAME_CURRENT_PASSWORD);    
        }
        
        if (!consumerChangePasswordForm.getNewPassword().equals(consumerChangePasswordForm.getConfirmPassword())) {
        	errors.reject(ErrorCode.APP_2202_NEW_PASSWORD_DIFFERENT_CONFIRM_PASSWORD,
                    new Object[] { "Password"},
                    ErrorCode.APP_2202_NEW_PASSWORD_DIFFERENT_CONFIRM_PASSWORD);
        }
        
        return !errors.hasErrors();
    }

}
