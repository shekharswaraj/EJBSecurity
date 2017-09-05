/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.ejb_security;

import org.jboss.as.quickstarts.ejb_security.others.CMnJAASLogin;
import org.jboss.ejb3.annotation.RunAsPrincipal;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Hashtable;
import javax.annotation.Resource;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A simple secured Servlet which calls a secured EJB. Upon successful authentication and authorization the EJB will return the
 * principal's name. Servlet security is implemented using annotations.
 * 
 * @author Sherif Makary
 * 
 */
@SuppressWarnings("serial")
@WebServlet("/SecuredEJBServlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = "quickstarts"))
//@RunAs("java")
public class SecuredEJBServlet extends HttpServlet {

    private static String PAGE_HEADER = "<html><head><title>ejb-security</title></head><body>";
    private static String PAGE_FOOTER = "</body></html>";

    // Inject the Secured EJB
    @EJB
    private SecuredEJB securedEJB;

    @Override
    public void init(javax.servlet.ServletConfig arg0) throws javax.servlet.ServletException{
        LoginContext lc = CMnJAASLogin.loginMethod();
        try {
            lc.login();
            runAs(lc.getSubject());
            System.out.println("Inside init...");
            securedEJB.getName();
            securedEJB.getSecurityInfo();
            lc.logout();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        //securedEJB.getSecurityInfo();

    }

    private void runAs(Subject s){
        Subject.doAs(s, new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                System.out.println("Inside privileged action");
                return null;
            }
        });
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String principal = null;
        String authType = null;
        String remoteUser = null;

        securedEJB.getName();
        principal = securedEJB.getSecurityInfo();
        remoteUser = req.getRemoteUser();
        authType = req.getAuthType();



        writer.println(PAGE_HEADER);
        writer.println("<h1>" + "Successfully called Secured EJB " + "</h1>");
        writer.println("<p>" + "Principal  : " + principal + "</p>");
        writer.println("<p>" + "Remote User : " + remoteUser + "</p>");
        writer.println("<p>" + "Authentication Type : " + authType + "</p>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

}
