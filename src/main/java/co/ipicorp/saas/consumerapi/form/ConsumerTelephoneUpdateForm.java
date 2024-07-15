/**
 * ConsumerTelephoneUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ConsumerTelephoneUpdateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class ConsumerTelephoneUpdateForm implements Serializable {
	
	private static final long serialVersionUID = -6258982145132432459L;

	@JsonProperty("consumerId")
    private Integer consumerId;
    
    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("otp")
    private String otp;
    
    public Integer getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Integer consumerId) {
		this.consumerId = consumerId;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	@Override
    public String toString() {
        return "ConsumerTelephoneUpdateForm [consumerId=" + consumerId + ", mobile=" + mobile + ", otp=" + otp + "]";
    }
	
}
