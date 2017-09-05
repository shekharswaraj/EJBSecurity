package org.jboss.as.quickstarts.ejb_security.others;

import org.jboss.security.SimpleGroup;
import org.jboss.security.auth.spi.UsernamePasswordLoginModule;

import javax.security.auth.login.LoginException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.HashMap;

/**
 * Created by sshekhar on 8/22/17.
 */
public class CMnEJBAuthenticator extends UsernamePasswordLoginModule {

    @Override
    protected String getUsersPassword() throws LoginException {
        System.out.println("CMnEJBAuthenticator: User is: " + super.getUsername() + " and unautnticatedIdentity is:" + super.getUnauthenticatedIdentity().getName());
        return "java";
    }

    @Override
    protected boolean validatePassword(String passwordWant, String passwordHave){
        System.out.println("Inside CMnEJBAuthenticator:validatePassword...");
        return true;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        System.out.println("Inside CMnEJBAuthenticator:getRoleSets...");
        HashMap setsMap = new HashMap();
        String groupName = "Roles";
        Group group = (Group) setsMap.get(groupName);
        if (group == null) {
            group = new SimpleGroup(groupName);
            setsMap.put(groupName, group);
        }
        try {
            Principal p = super.createIdentity("java");
            group.addMember(p);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Group[] roleSets = new Group[setsMap.size()];
        setsMap.values().toArray(roleSets);
        return roleSets;
    }
}
