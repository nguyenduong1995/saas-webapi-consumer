/**
 * TestFirebase.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.consumerapi;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * TestFirebase. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class TestFirebase {
    public static void main(String... args) throws Exception {
        String path = "classpath:firebase/saas-nrms-bd25d7659a91.json";
        File templateFile = ResourceUtils.getFile(path);
        FileInputStream serviceAccount = new FileInputStream(templateFile);
        GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);
        GoogleCredential scoped = googleCred.createScoped(
                Arrays.asList(
                  "https://www.googleapis.com/auth/firebase.database",
                  "https://www.googleapis.com/auth/userinfo.email"
                )
            );

            // Use the Google credential to generate an access token
            scoped.refreshToken();
            String token = scoped.getAccessToken();
            System.err.println("TOKEN: " + token);
    }
}
