package br.com.meadds.session;

import br.ufc.meadds.entity.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sampleProfileList")
public class SampleProfileList extends EntityQuery<SampleProfile> {

	/**
     * 
     */
	private static final long serialVersionUID = 5696685110798349940L;

	private static final String EJBQL = "select sampleProfile from SampleProfile sampleProfile";

	private static final String[] RESTRICTIONS = {
			"lower(sampleProfile.name) like lower(concat(#{sampleProfileList.sampleProfile.name},'%'))",
			"sampleProfile.user.id = #{sampleProfileList.user.id}" };

	private final User user = (User) Contexts.getSessionContext().get("user");

	private SampleProfile sampleProfile = new SampleProfile();

	public SampleProfileList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SampleProfile getSampleProfile() {
		return sampleProfile;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
}
