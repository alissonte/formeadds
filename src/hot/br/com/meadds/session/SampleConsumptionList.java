package br.com.meadds.session;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.SampleConsumption;

@Name("sampleConsumptionList")
public class SampleConsumptionList extends EntityQuery<SampleConsumption> {

	/**
     * 
     */
	private static final long serialVersionUID = 4190801442793712364L;

	private static final String EJBQL = "select sampleConsumption from SampleConsumption sampleConsumption";

	private static final String[] RESTRICTIONS = {};

	private SampleConsumption sampleConsumption = new SampleConsumption();

	public SampleConsumptionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SampleConsumption getSampleConsumption() {
		return sampleConsumption;
	}

	@SuppressWarnings("unchecked")
	public List<Library> getLibraries() {
		Set<Library> libraries = new HashSet<Library>();
		List<Sample> memory = getEntityManager().createQuery(
				"select o from Sample o").getResultList();
		List<SampleConsumption> battery = getEntityManager().createQuery(
				"select o from SampleConsumption o").getResultList();
		if ((memory != null && !memory.isEmpty())
				&& (battery != null && !battery.isEmpty())) {
			for (Sample o : memory) {
				libraries.add(o.getAlgorithm().getLibrary());
			}
			for (SampleConsumption o : battery) {
				libraries.add(o.getAlgorithm().getLibrary());
			}
		}
		return new LinkedList<Library>(libraries);
	}
}
