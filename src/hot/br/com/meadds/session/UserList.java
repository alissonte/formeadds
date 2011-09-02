package br.com.meadds.session;

import java.util.Arrays;

import javax.persistence.Query;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.ufc.meadds.entity.User;

@Name("userList")
public class UserList extends EntityQuery<User> {

	/**
     * 
     */
	private static final long serialVersionUID = -7095534150303666318L;

	private static final String EJBQL = "select user from User user";

	private static final String[] RESTRICTIONS = {
			"lower(user.email) like lower(concat(#{userList.user.email},'%'))",
			"lower(user.login) like lower(concat(#{userList.user.login},'%'))",
			"lower(user.name) like lower(concat(#{userList.user.name},'%'))",
			"lower(user.password) like lower(concat(#{userList.user.password},'%'))", };

	private User user = new User();

	public UserList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setRestrictionLogicOperator("or");
	}

	public User getUser() {
		return user;
	}

	public User login(String login, String password) {
		Query q = this
				.getEntityManager()
				.createQuery(
						"select u from User u where u.login = :login and u.password = :password");
		q.setParameter("login", login);
		q.setParameter("password", password);
		User usuario = null;
		try {
			usuario = (User) q.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return usuario;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
