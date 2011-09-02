package br.ufc.meadds.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.security.management.RoleName;


@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3276987898959629360L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@RoleName
	@NotNull
	private String name;

	@ManyToMany(mappedBy = "roles")
	private Set<User> users = new HashSet<User>(0);

	/**
	 * Getter for id
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter for id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter for name
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for users
	 * 
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * Setter for users
	 * 
	 * @param users
	 *            the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}

}
