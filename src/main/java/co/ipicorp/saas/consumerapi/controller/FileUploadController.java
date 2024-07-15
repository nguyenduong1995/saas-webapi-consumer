/**
 * FileUploadController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.controller;

import co.ipicorp.saas.core.web.components.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * FileUploadController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
public class FileUploadController {
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @PostMapping(value ="/uploadFiles")
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> uploadMultipleFiles(HttpServletRequest request, HttpServletResponse response, @RequestParam("files") MultipartFile[] files) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                List<String> filePaths = fileStorageService.storeMultipleFiles(files);
                getRpcResponse().addAttribute("files", filePaths);
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
        
    }
    
    @PostMapping(value ="/uploadFile")
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile file) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                String filePath = fileStorageService.storeFile(file, "prod_test");
                getRpcResponse().addAttribute("file", filePath);
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
        
    }
    
    @DeleteMapping(value ="/deleteFile")
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> deleteFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("filePath") String filePath) {
        
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                String result = fileStorageService.deleteFile(filePath);
                getRpcResponse().addAttribute("result", result);
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
        
    }
       
}
