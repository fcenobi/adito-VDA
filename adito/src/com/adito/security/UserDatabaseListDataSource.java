package com.adito.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ModuleUtils;

import com.adito.core.CoreServlet;
import com.adito.core.UserDatabaseManager;
import com.adito.properties.Pair;
import com.adito.properties.PairListDataSource;

/**
 * Implementation of {@link PairListDataSource} that retrieves its data from the
 * list of installed user databases.
 */
public class UserDatabaseListDataSource implements PairListDataSource {

    /*
     * (non-Javadoc)
     * 
     * @see com.adito.properties.PairListDataSource#getValues(javax.servlet.http.HttpServletRequest)
     */
    public List getValues(HttpServletRequest request) {
        List<Pair> l = new ArrayList<Pair>();
        ServletContext context = CoreServlet.getServlet().getServletContext();
        for (UserDatabaseDefinition def : UserDatabaseManager.getInstance().getUserDatabaseDefinitions()) {
            l.add(new Pair(def.getName(),
                            ((MessageResources) context.getAttribute(def.getMessageResourcesKey()
                                            + ModuleUtils.getInstance().getModuleConfig(request, context).getPrefix())).getMessage("security.userDatabase.value."
                                            + def.getName())));
        }
        return l;
    }

}
