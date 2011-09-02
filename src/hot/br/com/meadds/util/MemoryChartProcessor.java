package br.com.meadds.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.User;

@Name("memoryChart")
public class MemoryChartProcessor implements Processor, Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 3219419850783403036L;
	/**
	 * Entity manager to access the database.
	 */
	private EntityManager em = (EntityManager) Component
			.getInstance("entityManager");

	private byte[] chart; // chart image (.png) as a byte array

	private ChartType type;

	private User user = (User) Contexts.getSessionContext().get("user");

	private CategoryDataset getData() {

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		@SuppressWarnings("unchecked")
		List<Sample> list = (List<Sample>) em
				.createQuery("select o from Sample o where o.user.id = :id")
				.setParameter("id", user.getId()).getResultList();

		for (Sample sample : list) {
			dataset.addValue(sample.getEncryptionMemory(), "Encrypt Memory",
					sample.getAlgorithm().getName());
			dataset.addValue(sample.getDecryptionMemory(), "Decrypt Memory",
					sample.getAlgorithm().getName());
		}

		return dataset;

	}

	/**
	 * Getter for chart
	 * 
	 * @return the chart
	 */
	public byte[] getChart() {
		CategoryDataset ds = this.getData();

		JFreeChart chart = ChartFactory.createBarChart(
				"Ocupação de memória por algoritmo", "Algoritmos", "Bytes", ds,
				PlotOrientation.VERTICAL, true, true, false);
		CategoryAxis axis = ((CategoryPlot) chart.getPlot()).getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

		try {
			this.chart = ChartUtilities.encodeAsPNG(chart.createBufferedImage(
					640, 480));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.chart;
	}

	/**
	 * Setter for chart
	 * 
	 * @param chart
	 *            the chart to set
	 */
	public void setChart(byte[] chart) {
		this.chart = chart;
	}

	/**
	 * Getter for type
	 * 
	 * @return the type
	 */
	public ChartType getType() {
		return type;
	}

	/**
	 * Setter for type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(ChartType type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return "Memória";
	}

	@Override
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

}
