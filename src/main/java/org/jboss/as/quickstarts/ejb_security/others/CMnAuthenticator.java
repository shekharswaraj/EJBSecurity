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
public class CMnAuthenticator extends UsernamePasswordLoginModule{
    @Override
    protected String getUsersPassword() throws LoginException {
        System.out.println("User is: " + super.getUsername());
        return "java";
    }

    @Override
    protected boolean validatePassword(String passwordWant, String passwordHave){
        System.out.println("Inside validatePassword...");
        return true;
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {
        System.out.println("Inside getRoleSets...");
        HashMap setsMap = new HashMap();
        String groupName = "Roles";
        Group group = (Group) setsMap.get(groupName);
        if (group == null) {
            group = new SimpleGroup(groupName);
            setsMap.put(groupName, group);
        }
        try {
            Principal p = super.createIdentity("quickstarts");
            group.addMember(p);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Group[] roleSets = new Group[setsMap.size()];
        setsMap.values().toArray(roleSets);
        return roleSets;
    }

    @Override
    protected String[] getUsernameAndPassword() throws LoginException {
        String[] string = new String[] {"java", "java"};
        return string;
    }
}
