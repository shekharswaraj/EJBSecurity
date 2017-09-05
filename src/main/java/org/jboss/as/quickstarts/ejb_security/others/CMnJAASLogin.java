package org.jboss.as.quickstarts.ejb_security.others;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CMnJAASLogin {

    public static LoginContext loginMethod() {
        LoginContext lc = null;

        CallbackHandler cabHndlr = new CallbackHandler() {
            @Override
            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                for (int i = 0; i < callbacks.length; i++) {
                    if (callbacks[i] instanceof NameCallback) {
                        NameCallback nc = (NameCallback) callbacks[i];
                        nc.setName("java");
                    } else if (callbacks[i] instanceof PasswordCallback) {
                        PasswordCallback pc = (PasswordCallback) callbacks[i];
                        pc.setPassword("java".toCharArray());
                    } else {
                        throw new UnsupportedCallbackException(callbacks[i], "Unrecognized Callback");
                    }
                }
            }
        };
        try {
            String configurationName = "JBoss Test";
            Configuration config = new JBossJaasConfiguration(configurationName);

            lc = new LoginContext(configurationName, new Subject(), cabHndlr, config);
            return lc;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    static class JBossJaasConfiguration extends Configuration {
        private final String configurationName;

        JBossJaasConfiguration(String configurationName) {
            this.configurationName = configurationName;
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
            if (!configurationName.equals(name)) {
                throw new IllegalArgumentException("Unexpected configuration name '" + name + "'");
            }

            return new AppConfigurationEntry[]{

                    createClientLoginModuleConfigEntry(),

            };
        }

        private AppConfigurationEntry createClientLoginModuleConfigEntry() {
            Map<String, String> options = new HashMap<String, String>();
            options.put("multi-threaded", "true");
            options.put("restore-login-identity", "true");

            return new AppConfigurationEntry("org.jboss.security.ClientLoginModule",
                    AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
        }
    }
}