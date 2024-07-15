/**
 * LoginForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.form;

/**
 * LoginForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class LoginForm {
    
    private int loginType = 0;
    
    private String loginName;

    private String password;

    /**
     * get value of <b>loginName</b>.
     * 
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Set value to <b>loginName</b>.
     * 
     * @param loginName
     *            the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
     * get value of <b>loginType</b>.
     * @return the loginType
     */
    public int getLoginType() {
        return loginType;
    }

    /**
     * Set value to <b>loginType</b>.
     * @param loginType the loginType to set
     */
    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "LoginForm [loginType=" + loginType + ", loginName=" + loginName + ", password=" + password + "]";
    }

}
