package br.com.meadds.session;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import br.ufc.meadds.entity.Role;
import br.ufc.meadds.entity.User;

@Name("authenticator")
public class Authenticator {
    @Logger
    private Log log;

    @In
    Identity identity;
    @In
    Credentials credentials;
    @In(create = true)
    UserList userList;

    public boolean authenticate() {
        String username = credentials.getUsername();
        log.info("authenticating {0}", username);

        User user = userList.login(credentials.getUsername(), credentials.getPassword());
        boolean authenticated = false;
        if (user != null) {
            Contexts.getSessionContext().set("user", user);

            for (Role r : user.getRoles()) {
                identity.addRole(r.getName());
            }

            authenticated = true;
        }
        return authenticated;
    }

}
