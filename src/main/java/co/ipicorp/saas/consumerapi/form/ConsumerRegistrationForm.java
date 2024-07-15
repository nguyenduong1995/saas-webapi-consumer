/**
 * ConsumerRegistrationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

/**
 * ConsumerRegistrationForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ConsumerRegistrationForm implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -1623591206719906797L;
    
    private String fullname;
    private String mobile;
    private String password;
    
    private String otp;

    @Override
    public String toString() {
        return "ConsumerRegistrationForm [fullname=" + fullname + ", mobile=" + mobile + ", password=" + password + ", otp=" + otp + "]";
    }

    /**
     * get value of <b>fullname</b>.
     * 
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Set value to <b>fullname</b>.
     * 
     * @param fullname
     *            the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * get value of <b>mobile</b>.
     * 
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Set value to <b>mobile</b>.
     * 
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * get value of <b>password</b>.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set value to <b>password</b>.
     * 
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get value of <b>otp</b>.
     * @return the otp
     */
    public String getOtp() {
        return otp;
    }

    /**
     * Set value to <b>otp</b>.
     * @param otp the otp to set
     */
    public void setOtp(String otp) {
        this.otp = otp;
    }

}
