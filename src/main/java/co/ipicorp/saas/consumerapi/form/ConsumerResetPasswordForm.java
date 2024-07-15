/**
 * ConsumerResetPasswordForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

/**
 * 
 * ConsumerResetPasswordForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class ConsumerResetPasswordForm implements Serializable {

	private static final long serialVersionUID = -7403795054821191559L;

	private String otp;

	private String phone;
	
    private String newPassword;
    
    private String confirmPassword;

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
    public String toString() {
        return "RetailerChangePasswordForm [otp=" + otp + ", phone=" + phone +", newPassword=" + newPassword + ", confirmPassword=" + confirmPassword + "]";
    }

}
