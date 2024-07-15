/**
 * AppRealm.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi.security;

import co.ipicorp.saas.core.web.security.AppRealm;

/**
 * AppRealm.
 * <<< Detail note.
 * @author hieumicro
 */
public class ConsumerAppRealm extends AppRealm {

    @Override
    public String getSaasAuthorizationCacheName() {
        return "ConsumerAuthorizationCache";
    }

    @Override
    public String getRealmName() {
        return "consumerRealm";
    }

}