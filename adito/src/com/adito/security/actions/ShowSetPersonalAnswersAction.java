
				/*
 *  Adito
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.adito.security.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.adito.boot.PropertyClassManager;
import com.adito.core.CoreUtil;
import com.adito.core.actions.AuthenticatedAction;
import com.adito.policyframework.Permission;
import com.adito.policyframework.PolicyConstants;
import com.adito.properties.Property;
import com.adito.properties.attributes.AttributeDefinition;
import com.adito.properties.impl.userattributes.UserAttributeKey;
import com.adito.properties.impl.userattributes.UserAttributes;
import com.adito.security.Constants;
import com.adito.security.LogonControllerFactory;
import com.adito.security.PersonalAnswer;
import com.adito.security.PersonalQuestionsAuthenticationModule;
import com.adito.security.SessionInfo;
import com.adito.security.User;
import com.adito.security.forms.PersonalAnswersForm;

public class ShowSetPersonalAnswersAction extends AuthenticatedAction {

    public ShowSetPersonalAnswersAction() {
        super(PolicyConstants.ATTRIBUTES_RESOURCE_TYPE, new Permission[] {
            PolicyConstants.PERM_MAINTAIN
        });
    }

    public ActionForward onExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        PersonalAnswersForm f = (PersonalAnswersForm) form;
        if (CoreUtil.getPageInterceptListenerById(request.getSession(), "changePersonalAnswers") == null) {
            if (f.getReferer() == null) {
                f.setReferer(CoreUtil.getReferer(request));
            }
        }
        List<PersonalAnswer> personalAnswers = new ArrayList<PersonalAnswer>();
        User user = LogonControllerFactory.getInstance().getUser(request);
        AttributeDefinition def;
        for (int i = 0; i < PersonalQuestionsAuthenticationModule.SECURITY_QUESTIONS.length; i++) {
        	
            String id = PersonalQuestionsAuthenticationModule.SECURITY_QUESTIONS[i];
            def = (AttributeDefinition)PropertyClassManager.getInstance().getPropertyClass(UserAttributes.NAME).getDefinition(id);
            String answer = Property.getProperty(new UserAttributeKey(user, def.getName()));
            personalAnswers.add(new PersonalAnswer(id, answer==null ? "" : answer, def.getLabel()));
        }
        
        f.initialize(personalAnswers);
        if (request.getSession().getAttribute(Constants.REQ_ATTR_PERSONAL_ANSWERS_CHANGE_REASON_MESSAGE) != null) {
            ActionMessages messages = new ActionMessages();
            messages.add(Globals.MESSAGE_KEY, (ActionMessage) request.getSession().getAttribute(
                Constants.REQ_ATTR_PERSONAL_ANSWERS_CHANGE_REASON_MESSAGE));
            saveMessages(request, messages);
        }
        return mapping.findForward("display");
    }

    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT | SessionInfo.USER_CONSOLE_CONTEXT;
    }
}