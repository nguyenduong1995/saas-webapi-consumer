/**
 * RegistrationController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.consumerapi.form.ConsumerRegistrationForm;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.model.ConsumerAuth;
import co.ipicorp.saas.nrms.service.ConsumerAuthService;
import co.ipicorp.saas.nrms.service.ConsumerRetailerActionHistoryService;
import co.ipicorp.saas.nrms.service.ConsumerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;

/**
 * RegistrationController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
public class RegistrationController {
    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ConsumerRetailerActionHistoryService service;
    
    @Autowired
    private ConsumerService consumerService;
    
    @Autowired
    private ConsumerAuthService consumerAuthService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    private Map<String, Consumer> mapConsumer = new ConcurrentHashMap<String, Consumer>();
    private Map<String, String> mapOtp = new ConcurrentHashMap<String, String>();
    private Map<String, LocalDateTime> mapExpired = new ConcurrentHashMap<String, LocalDateTime>();
    
    @PostMapping(value = ControllerAction.APP_REGISTER_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> register(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerRegistrationForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Consumer consumer = consumerService.getByMobile(form.getMobile());
                if (consumer != null) {
                    errors.reject("Số điện thoại đã tồn tại", "Số điện thoại đã tồn tại");
                } else {
                    consumer = new Consumer();
                    consumer.setMobile(form.getMobile());
                    consumer.setFullname(form.getFullname());
                    mapConsumer.put(form.getMobile(), consumer);
                    mapOtp.put(form.getMobile(), "999999");
                    mapExpired.put(form.getMobile(), LocalDateTime.now());
                }
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_REGISTER_ACTION + "/confirm")
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> confirmRegistration(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerRegistrationForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Consumer consumer = consumerService.getByMobile(form.getMobile());
                if (consumer != null) {
                    errors.reject("Số điện thoại đã tồn tại", "Số điện thoại đã tồn tại");
                } else {
                    LocalDateTime dt = mapExpired.get(form.getMobile());
                    if (dt == null) {
                        errors.reject("Sai thông tin", "Sai thông tin");
                    } else {
                        Duration duration = Duration.between(dt, LocalDateTime.now());
                        if (duration.getSeconds() > 60) {
                            errors.reject("Mã OPT đã hết hạn", "Sai thông tin");
                        } else {
                            String otp = mapOtp.get(form.getMobile());
                            if (!otp.equals(form.getOtp())) {
                                errors.reject("Mã OTP không đúng", "Mã OTP không đúng");
                            }
                        }
                    }
                    
                    if (!errors.hasErrors()) {
                        consumer = mapConsumer.get(form.getMobile());
                        consumer.setFullname(form.getFullname());
                        consumer.setGender(1);
                        consumer.setStatus(Status.ACTIVE);
                        consumer.setImage("profile.png");
                        consumer.setExtraData(new LinkedHashMap<String, Object>());
                        consumerService.create(consumer);
                        
                        ConsumerAuth auth = new ConsumerAuth();
                        auth.setConsumerId(consumer.getId());
                        auth.setLoginName(consumer.getMobile());
                        auth.setMobile(consumer.getMobile());
                        String salt = "ERKFDHksafshfanueoqaAgldh";
                        String enscriptedPassword = "";
                        try {
                            enscriptedPassword = SystemUtils.getInstance().md5(form.getPassword() + salt);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        auth.setPassword(enscriptedPassword);
                        auth.setSalt(salt);
                        auth.setStatus(Status.ACTIVE);
                        
                        consumerAuthService.create(auth);
                        removeMobile(form.getMobile());
                    }
                }
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }
    
    private void removeMobile(String mobile) {
        mapConsumer.remove(mobile);
        mapOtp.remove(mobile);
        mapExpired.remove(mobile);
    }
}
