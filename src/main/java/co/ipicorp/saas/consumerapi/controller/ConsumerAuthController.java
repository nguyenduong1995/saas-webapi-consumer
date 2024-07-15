/**
 * ConsumerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.nrms.model.ConsumerAuth;
import co.ipicorp.saas.nrms.service.ConsumerService;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * ConsumerController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class ConsumerAuthController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ConsumerService consumerAuthService;

    @RequestMapping(value = "consumer-auth/list", method = RequestMethod.GET)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return consumerAuthService.getAll();
            }

            @Override
            public String getAttributeName() {
                return "consumerAuths";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchconsumerAuthServices((List<ConsumerAuth>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

}
