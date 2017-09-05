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

import java.security.Principal;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.ejb3.annotation.SecurityDomain;

/**
 * Simple secured EJB using EJB security annotations
 * 
 * @author Sherif Makary
 * 
 */
/**
 * 
 * Annotate this EJB for authorization. Allow only those in the "guest" role. For EJB authorization, you must also specify the
 * security domain. This example uses the "other" security domain which is provided by default in the standalone.xml file.
 * 
 */
@Stateless
@DeclareRoles("java")
@SecurityDomain("custom")
public class SecuredEJB{

    // Inject the Session Context
    @Resource
    private SessionContext ctx;

    @Resource
    private EJBContext ejbCtx;

    /**
     * Secured EJB method using security annotations
     */
    @RolesAllowed("java")
    public String getSecurityInfo() {
        // Session context injected using the resource annotation
        Principal principal = ctx.getCallerPrincipal();
        System.out.println("Inside SecuredEJB.getSecurityInfo...");
        return principal.toString();
    }

    @PermitAll
    public void getName(){
        System.out.println("ctx: " + ctx );
        System.out.println("Principal Name: " + ctx.getCallerPrincipal() );
        System.out.println("ejbCtx: " + ejbCtx );
    }
}
