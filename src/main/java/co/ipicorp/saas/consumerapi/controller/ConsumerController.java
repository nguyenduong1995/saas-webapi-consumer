/**
 * ConsumerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.consumerapi.form.ConsumerChangePasswordForm;
import co.ipicorp.saas.consumerapi.form.ConsumerResetPasswordForm;
import co.ipicorp.saas.consumerapi.form.ConsumerRetailerActionForm;
import co.ipicorp.saas.consumerapi.form.ConsumerTelephoneUpdateForm;
import co.ipicorp.saas.consumerapi.form.ConsumerUpdateForm;
import co.ipicorp.saas.consumerapi.security.ConsumerSessionInfo;
import co.ipicorp.saas.consumerapi.util.Constants;
import co.ipicorp.saas.consumerapi.util.ControllerAction;
import co.ipicorp.saas.consumerapi.util.ErrorCode;
import co.ipicorp.saas.consumerapi.validator.ConsumerChangePasswordValidator;
import co.ipicorp.saas.consumerapi.validator.ConsumerExistedValidator;
import co.ipicorp.saas.consumerapi.validator.ConsumerRetailerActionFormValidator;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.Consumer;
import co.ipicorp.saas.nrms.model.ConsumerAuth;
import co.ipicorp.saas.nrms.model.ConsumerRetailerActionHistory;
import co.ipicorp.saas.nrms.service.ConsumerAuthService;
import co.ipicorp.saas.nrms.service.ConsumerRetailerActionHistoryService;
import co.ipicorp.saas.nrms.service.ConsumerService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.exception.HttpNotFoundException;
import grass.micro.apps.web.util.RequestUtils;

/**
 * ConsumerController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
public class ConsumerController {

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

//    private static Map<String, Object> mapOtp = new HashMap<>();
    private Map<String, Consumer> mapConsumer = new ConcurrentHashMap<String, Consumer>();
    private Map<String, String> mapOtp = new ConcurrentHashMap<String, String>();
    private Map<String, LocalDateTime> mapExpired = new ConcurrentHashMap<String, LocalDateTime>();
    
    @RequestMapping(value = ControllerAction.APP_ACTION_ON_RETAILER_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @Logged
    @Validation(validators = { ConsumerRetailerActionFormValidator.class})
    public ResponseEntity<?> actionOnRetailer(HttpServletRequest request, HttpServletResponse response,
            @GetBody ConsumerRetailerActionForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ConsumerRetailerActionHistory history = new ConsumerRetailerActionHistory();
                history.setRetailerId(form.getRetailerId());
                history.setActionType(form.getActionType());
                history.setActionTime(LocalDateTime.now());
                history.setConsumerId(info.getConsumer().getId());
                service.create(history);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_CONSUMER_ACTION + "/profile", method = RequestMethod.PUT)
    @ResponseBody
    @Logged
    @Validation(schema = @AppJsonSchema("/schema/consumer_update.json") )
    public ResponseEntity<?> updateProfile(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerUpdateForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	Integer consumerId = info.getConsumer().getId();
            	Consumer consumer = ConsumerController.this.doUpdateProfile(consumerId, form, (BindingResult) errors);
                getRpcResponse().addAttribute("consumer", consumer);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_CONSUMER_ACTION + "/image", method = RequestMethod.PUT)
    @ResponseBody
    @Logged
    public ResponseEntity<?> updateImage(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerUpdateForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	Integer consumerId = info.getConsumer().getId();
            	Consumer consumer = ConsumerController.this.processUploadAndUpdateImage(consumerId, form, 1);
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", consumer.getId());
                map.put("mobile", consumer.getMobile());
                map.put("name", consumer.getFullname());
                map.put("email", consumer.getEmail());
                map.put("extraData", consumer.getExtraData());

                String avatar = consumer.getImage();
                if (StringUtils.isNotBlank(avatar)) {
                    avatar = ResourceUrlResolver.getInstance().resolveConsumerUrl(1, avatar);
                } else {
                    avatar = "";
                }

                map.put("avatar", avatar);
                getRpcResponse().addAttribute("consumer", map);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_CONSUMER_ACTION + "/telephone", method = RequestMethod.GET)
    @ResponseBody
    @Logged
    public ResponseEntity<?> getTelephoneAndOtp(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam("id") Integer consumerId) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	String telephone = ConsumerController.this.getOldTelephone(consumerId);
            	String opt = ConsumerController.this.generateOtp();
            	String ipAddress = request.getRemoteAddr();
            	mapOtp.put(ipAddress, opt);
            	getRpcResponse().addAttribute("telephone", telephone);
                getRpcResponse().addAttribute("otp", opt);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_CONSUMER_ACTION + "/telephone", method = RequestMethod.PUT)
    @ResponseBody
    @Logged
    @Validation(validators = { ConsumerExistedValidator.class})
    public ResponseEntity<?> updateTelephone(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody ConsumerTelephoneUpdateForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	String ipAddress = request.getRemoteAddr();
            	Consumer consumer = ConsumerController.this.updateTelephone(form, ipAddress);
            	getRpcResponse().addAttribute("message", "successful");
                getRpcResponse().addAttribute("consumerId", consumer.getId());
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PutMapping(value = ControllerAction.APP_CONSUMER_ACTION + "/change_password")
    @Validation(schema = @AppJsonSchema("/schema/change_password.json"), validators = {ConsumerChangePasswordValidator.class})
    @Logged
    public ResponseEntity<?> changePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody ConsumerChangePasswordForm form,
            BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	ConsumerSessionInfo info = (ConsumerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	ConsumerAuth consumerAuth = ConsumerController.this.consumerAuthService.getByConsumerId(info.getConsumer().getId());
                String enscriptedPasswd = SystemUtils.getInstance().generatePassword(form.getCurrentPassword(), consumerAuth.getSalt());
                if (!enscriptedPasswd.equals(consumerAuth.getPassword())) {
                    errors.reject(ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH, ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH);
                    return;
                }
                
                consumerAuth.setPassword(SystemUtils.getInstance().generatePassword(form.getNewPassword(), consumerAuth.getSalt()));
                EntityUpdateTracker.getInstance().track(ConsumerAuth.class, consumerAuth.getId(), consumerAuth.getUpdateCount());
                
                try {
                	ConsumerController.this.consumerAuthService.updatePartial(consumerAuth);
                    getRpcResponse().addAttribute("message", "success");
                } catch (Exception ex) {
                    errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR, ErrorCode.APP_1000_SYSTEM_ERROR);
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    private Consumer updateTelephone(ConsumerTelephoneUpdateForm form, String ipAddress) {
    	Consumer consumer = this.consumerService.getActivated(form.getConsumerId());
    	if (mapOtp.get(ipAddress).equals(form.getOtp())) {
    		consumer.setMobile(form.getMobile());
    		this.consumerService.updatePartial(consumer);
        	
        	ConsumerAuth consumerAuth = this.consumerAuthService.getByConsumerId(consumer.getId());
        	if (consumerAuth != null && consumerAuth.getLoginName().equals(consumer.getEmail())) {
        		consumerAuth.setMobile(form.getMobile());
        		this.consumerAuthService.updatePartial(consumerAuth);
        	}
        	
    	}
    	
    	return consumer;
    }
    
    private String getOldTelephone(Integer consumerId) {
    	String telephone = "";
    	Consumer consumer = null;
    	if (consumerId != null) {
    		consumer = this.consumerService.getActivated(consumerId);
    	}
    	
    	if (consumer != null) {
    		telephone = consumer.getMobile();
    	}
    	
    	return telephone;
    }
    
    private String generateOtp() {
    	Random r = new Random( System.currentTimeMillis() );
        DecimalFormat df = new DecimalFormat("000000");
        String numFormat = "" + r.nextInt(100000);
        return df.format(Integer.valueOf(numFormat));
    }
    
    private Consumer doUpdateProfile(Integer consumerId, ConsumerUpdateForm form, BindingResult errors) {
    	Consumer consumer = this.consumerService.getActivated(consumerId);
    	if (consumer == null || consumer.isDeleted()) {
            throw new HttpNotFoundException("Can not find the consumer with id " + consumerId);
        }
    	
    	consumer.setFullname(StringUtils.isNotEmpty(form.getFullname()) ? form.getFullname() : consumer.getFullname());
    	consumer.setEmail(StringUtils.isNotEmpty(form.getEmail()) ? form.getEmail() : consumer.getEmail());
    	consumer.setMobile(StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : consumer.getMobile());
    	this.consumerService.updatePartial(consumer);
    	
    	return consumer;
    }
    
    /**
     * @param form
     * @param customer
     * @return
     */
    protected Consumer processUploadAndUpdateImage(Integer consumerId, ConsumerUpdateForm form, Integer customerId) {
    	Consumer consumer = this.consumerService.getActivated(consumerId);
        if (StringUtils.isNotEmpty(form.getImage())) {
            //delete old image
            if (StringUtils.isNotEmpty(form.getImage())) {
                this.fileStorageService.deleteFile(consumer.getImage());
            }
            String[] data = form.getImage().split(",");
            String imagePath = uploadImageToFTP(form.getImage(), customerId, consumer.getId());
            String extension = data[0].split(";")[0].split("/")[1];

			String path = imagePath + "." + extension;
            consumer.setImage(path);
        }
        
        consumer = this.consumerService.updatePartial(consumer);
        return consumer;
    }
    
    /**
     * @param image
     * @return
     */
    private String uploadImageToFTP(String image, Integer customerId, Integer consumerId) {
        String location = ResourceUrlResolver.getInstance().resolveFtpConsumerPath(customerId, "");
        String fileName = SystemUtils.getInstance().generateCode("C", customerId, "C", consumerId);
        this.fileStorageService.storFile(image, location, fileName);
        return fileName;
    }
    
    @GetMapping(value = ControllerAction.APP_VERIFY_PHONE_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> verifyPhone(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("phone") String phone) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
			@Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Consumer consumer = consumerService.getByMobile(phone);
                if (consumer != null) {
                	mapConsumer.put(consumer.getMobile(), consumer);
                    mapOtp.put(consumer.getMobile(), "999999");
                    mapExpired.put(consumer.getMobile(), LocalDateTime.now());
                } else {
                	errors.reject("Số điện thoại chưa đăng ký", "Số điện thoại chưa đăng ký");
                }
            }
        };
            
        return support.doSupport(request, null, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_VERIFY_OTP_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> verifyOtp(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerResetPasswordForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
			@Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Consumer consumer = consumerService.getByMobile(form.getPhone());
                if (consumer == null) {
                    errors.reject("Số điện thoại chưa đăng ký", "Số điện thoại chưa đăng ký");
                } else {
                    LocalDateTime dt = mapExpired.get(form.getPhone());
                    if (dt == null) {
                        errors.reject("Sai thông tin", "Sai thông tin");
                    } else {
                        Duration duration = Duration.between(dt, LocalDateTime.now());
                        if (duration.getSeconds() > 60) {
                            errors.reject("EXPIRED", "OTP quá hạn");
                        } else {
                            String otp = mapOtp.get(form.getPhone());
                            if (!otp.equals(form.getOtp())) {
                                errors.reject("Mã OTP không đúng", "Mã OTP không đúng");
                            }
                        }
                    }
                }
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_RESET_PASSWORD_ACTION)
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> resetPassword(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ConsumerResetPasswordForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
			@Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Consumer consumer = consumerService.getByMobile(form.getPhone());
                if (consumer == null) {
                    errors.reject("Số điện thoại chưa đăng ký", "Số điện thoại chưa đăng ký");
                } else {
                	if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                		errors.reject("New password không khớp với confirm password", "New password không khớp với confirm password");
                	} else {
                		ConsumerAuth consumerAuth = ConsumerController.this.consumerAuthService.getByConsumerId(consumer.getId());
                		if (consumerAuth == null) {
                			errors.reject("Số điện thoại chưa đăng ký", "Số điện thoại chưa đăng ký");
                		} else {
                			consumerAuth.setPassword(SystemUtils.getInstance().generatePassword(form.getNewPassword(), consumerAuth.getSalt()));
                            EntityUpdateTracker.getInstance().track(ConsumerAuth.class, consumerAuth.getId(), consumerAuth.getUpdateCount());
                            
                            try {
                            	ConsumerController.this.consumerAuthService.updatePartial(consumerAuth);
                            	ConsumerController.this.removeMobile(form.getPhone());
                                getRpcResponse().addAttribute("message", "success");
                            } catch (Exception ex) {
                                errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR, ErrorCode.APP_1000_SYSTEM_ERROR);
                            }
                		}
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
