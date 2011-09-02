package br.com.meadds.session;

import java.awt.Color;
import java.awt.GradientPaint;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityQuery;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.AlgorithmType;
import br.ufc.meadds.entity.Chart;
import br.ufc.meadds.entity.Device;
import br.ufc.meadds.entity.Library;
import br.ufc.meadds.entity.OperationMode;
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.SampleConsumption;
import br.ufc.meadds.entity.User;

@Name("sampleList")
public class SampleList extends EntityQuery<Sample> {

	private static final class Group implements XYItemLabelGenerator {
		
		public Group() {
			// TODO Auto-generated constructor stub
		}

		private XYSeriesCollection collection = new XYSeriesCollection();

		private Map<Integer, String> names = new HashMap<Integer, String>();

		public String generateLabel(XYDataset d, int s, int i) {
			return names.get(d.getY(s, i));
		}

		/**
		 * @return the collection
		 */
		public XYSeriesCollection getCollection() {
			return collection;
		}

		/**
		 * @return the names
		 */
		public Map<Integer, String> getNames() {
			return names;
		}

	}

	/**
     * 
     */
	private static final long serialVersionUID = 658107203200047368L;

	private static final String EJBQL = "select sample from Sample sample";

	private static final String[] RESTRICTIONS = {};

	private Sample sample = new Sample();

	private Long user;

	private Long library;

	private Long device;

	private Long algorithm;

	private Long operation;

	private Integer slice;

	private List<Long> values = new LinkedList<Long>();

	private Integer textSize;

	private Chart type;

	private List<User> users;

	private List<Device> devices = new LinkedList<Device>();

	private List<AlgorithmType> algorithms = new LinkedList<AlgorithmType>();

	private List<OperationMode> operations = new LinkedList<OperationMode>();

	private List<Integer> textSizes = new LinkedList<Integer>();

	private List<Algorithm> algs = new LinkedList<Algorithm>();

	private List<Library> libraries;

	private byte[] chart;

	public SampleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	@Factory(value = "chartTypes")
	public Chart[] getDocumentCategory() {
		return Chart.values();
	}

	public Sample getSample() {
		return sample;
	}

	/**
	 * @return the user
	 */
	public Long getUser() {
		return user;
	}

	/**
	 * @return the chart
	 */
	public byte[] getChart() {

		if (type != null) {
			String title = "";
			String y = "";
			String x = "Algoritmo";
			CategoryDataset ds = null;
			switch (type) {
			case MEMORY:
				title = "Ocupação de memória por algoritmo";
				y = "Bytes";
				ds = this.getMemoryData();
				break;
			case TIME:
				title = "Tempo de execução por algoritmo";
				y = "Millisegundos";
				ds = this.getTimeData();
				break;
			case CONSUMPTION:
				title = "Cosumo de Bateria por algoritmo";
				y = "%";
				ds = this.getConsumptionData();
				break;
			default:
				x = "Tempo (ms)";
				y = "Consumo %";
				break;
			}

			if (ds != null && type != Chart.PERFORMANCE) {
				JFreeChart chart = ChartFactory.createBarChart(title, x, y, ds,
						PlotOrientation.VERTICAL, true, true, false);
				
				//[start]alisson
		        CategoryPlot plot = chart.getCategoryPlot();
		        plot.setBackgroundPaint(Color.lightGray); // Cor de fundo
		        plot.setDomainGridlinePaint(Color.white); // cor da linha vertical branca
		        plot.setDomainGridlinesVisible(false); // mostra as linhas verticais
		        plot.setRangeGridlinePaint(Color.white); // cor linhas horizontais
		        
		        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis(); 
		        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits()); 
		        rangeAxis.setAutoRangeIncludesZero(true); // novo parâmetro
		        
		        BarRenderer renderer = (BarRenderer) plot.getRenderer();
		        renderer.setDrawBarOutline(false);
		        
		        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.GREEN,
		                0.0f, 0.0f, new Color(0, 0, 64));
		        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.RED,
		                0.0f, 0.0f, new Color(0, 64, 0));
		        
		        renderer.setSeriesPaint(0, gp0);
		        renderer.setSeriesPaint(1, gp1);
		        renderer.setItemMargin(0.0); // Junta as barra
		        
		        CategoryAxis domainAxis = plot.getDomainAxis();
		        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));
				//[end]alisson		        
				try {
					this.chart = ChartUtilities.encodeAsPNG(chart
							.createBufferedImage(1024, 768));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				final Group data = getPerformanceData();
				if (data != null) {
					JFreeChart chart = ChartFactory.createXYLineChart(title, x,
							y, data.getCollection(), PlotOrientation.VERTICAL,
							true, true, true);
					XYPlot plot = (XYPlot) chart.getPlot();
					XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
					renderer.setSeriesLinesVisible(0, true);
					renderer.setSeriesShapesVisible(0, true);
					renderer.setSeriesLinesVisible(1, true);
					renderer.setSeriesShapesVisible(1, true);
					renderer.setSeriesItemLabelsVisible(0, true);
					renderer.setSeriesItemLabelsVisible(1, true);
					renderer.setSeriesItemLabelGenerator(0, data);
					renderer.setSeriesItemLabelGenerator(1, data);
					renderer.setSeriesItemLabelPaint(0, Color.RED);
					renderer.setSeriesItemLabelPaint(1, Color.BLUE);
					renderer.setSeriesPositiveItemLabelPosition(0,
							new ItemLabelPosition(ItemLabelAnchor.OUTSIDE6,
									TextAnchor.HALF_ASCENT_CENTER));
					renderer.setSeriesPositiveItemLabelPosition(0,
							new ItemLabelPosition());
					plot.setRenderer(renderer);
					try {
						this.chart = ChartUtilities.encodeAsPNG(chart
								.createBufferedImage(1024, 768));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return chart;
	}

	private CategoryDataset getMemoryData() {

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Sample sample : getSampleData()) {
			dataset.addValue(sample.getEncryptionMemory(), "Encrypt Memory",
					sample.getAlgorithm().getName());
			dataset.addValue(sample.getDecryptionMemory(), "Decrypt Memory",
					sample.getAlgorithm().getName());
		}

		return dataset;

	}

	private CategoryDataset getTimeData() {

		// create the dataset...
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (Sample sample : getSampleData()) {
			dataset.addValue(sample.getEncryptTime(), "Encrypt Time", sample
					.getAlgorithm().getName());
			dataset.addValue(sample.getDecryptTime(), "Decrypt Time", sample
					.getAlgorithm().getName());
		}

		return dataset;

	}

	@SuppressWarnings("unchecked")
	private List<Sample> getSampleData() {
		List<Sample> list = new LinkedList<Sample>();
		if (device != null && user != null && textSize != null
				&& (values != null && values.size() > 0)) {
			try {
				Query q = getEntityManager().createQuery(
						"select o from Sample o where "
								+ " o.algorithm.id in (:values) "
								+ "and o.user.id = :user "
								+ "and o.textSize = :textSize "
								+ "and o.device.id = :device");
				q.setParameter("values", values);
				q.setParameter("device", device);
				q.setParameter("textSize", textSize);
				q.setParameter("user", user);
				list.addAll(q.getResultList());
			} catch (Exception e) {
				System.err.println("Could not get the Samples");
			}

		}
		return list;
	}

	@SuppressWarnings("unchecked")
	private CategoryDataset getConsumptionData() {
		List<SampleConsumption> list = new LinkedList<SampleConsumption>();
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		if (device != null && user != null
				&& (values != null && values.size() > 0)) {
			try {
				Query q = getEntityManager().createQuery(
						"select o from SampleConsumption o where "
								+ " o.algorithm.id in (:values) "
								+ "and o.user.id = :user "
								+ "and o.device.id = :device");
				q.setParameter("values", values);
				q.setParameter("device", device);
				q.setParameter("user", user);
				list.addAll(q.getResultList());
			} catch (Exception e) {
				System.err.println("Could not get the Samples");
			}

		}

		for (SampleConsumption sampleConsumption : list) {
			dataset.addValue(sampleConsumption.getAverageConsumptionEncrypt(),
					"Average Encrypt Consumption", sampleConsumption
							.getAlgorithm().getName());
			dataset.addValue(sampleConsumption.getAverageConsumptionDecrypt(),
					"Average Decrypt Consumption", sampleConsumption
							.getAlgorithm().getName());
		}
		return dataset;
	}

	@SuppressWarnings("unchecked")
	private Group getPerformanceData() {
		Group group = null;
		if (device != null && user != null
				&& (values != null && values.size() > 0)) {
			group = new Group();
			final XYSeries encrypt = new XYSeries("Encrypt");
			final XYSeries decrypt = new XYSeries("Decrypt");
			List<Long> id = new LinkedList<Long>();
			for (Object o : values) {
				id.add(Long.parseLong(o.toString()));
			}
			Query q = getEntityManager()
					.createQuery(
							"select avg(o.averageConsumptionEncrypt), avg(o.averageConsumptionDecrypt), o.algorithm.id from SampleConsumption o where "
									+ " o.algorithm.id in (:values) "
									+ "and o.user.id = :user "
									+ "and o.device.id = :device group by o.algorithm.id");
			q.setParameter("values", id);
			q.setParameter("device", device);
			q.setParameter("user", user);

			List<Object[]> consumption = q.getResultList();
			Integer y = slice;
			double lEnc = 0;
			double lDec = 0;
			encrypt.add(0, 0);
			decrypt.add(0, 0);
			for (Object[] o : consumption) {
				try {
					q = getEntityManager().createQuery(
							"select avg(o.encryptTime), avg(o.decryptTime) from Sample o where "
									+ " o.algorithm.id = :value "
									+ "and o.user.id = :user "
									+ "and o.textSize = 8192 "
									+ "and o.device.id = :device");
					q.setParameter("value", Long.parseLong(o[2].toString()));
					q.setParameter("device", device);
					q.setParameter("user", user);

					Object[] time = (Object[]) q.getSingleResult();
					Double et = (slice / Double.parseDouble(o[0].toString()))
							* Double.parseDouble(time[0].toString());
					Double dt = (slice / Double.parseDouble(o[1].toString()))
							* Double.parseDouble(time[1].toString());
					lEnc += et;
					lDec += dt;
					encrypt.add(lEnc / 1000, y);
					decrypt.add(lDec / 1000, y);
					for (Algorithm a : algs) {
						if (a.getId().equals(Long.parseLong(o[2].toString()))) {
							group.getNames().put(y, a.getName());
						}
					}
					y += slice;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			group.getCollection().addSeries(encrypt);
			group.getCollection().addSeries(decrypt);
		}

		return group;
	}

	/**
	 * @param chart
	 *            the chart to set
	 */
	public void setChart(byte[] chart) {
		this.chart = chart;
	}

	/**
	 * @return the type
	 */
	public Chart getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Chart type) {
		if (type != null && Chart.PERFORMANCE.equals(type)) {
			textSize = 8192;
		}
		this.type = type;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(Long user) {
		this.user = user;
	}

	/**
	 * @return the libraries
	 */
	@SuppressWarnings("unchecked")
	public List<Library> getLibraries() {
		if (user != null) {
			libraries = getEntityManager()
					.createQuery(
							"select o from Library o join o.users u where u.id = :user")
					.setParameter("user", user).getResultList();
		}
		return libraries;
	}

	/**
	 * @param libraries
	 *            the libraries to set
	 */
	public void setLibraries(List<Library> libraries) {
		this.libraries = libraries;
	}

	/**
	 * @return the library
	 */
	public Long getLibrary() {
		return library;
	}

	/**
	 * @param library
	 *            the library to set
	 */
	public void setLibrary(Long library) {
		this.library = library;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		if (users == null) {
			users = new LinkedList<User>();
			users.add((User) getEntityManager().createQuery(
					"select o from User o where o.login = 'admin'")
					.getSingleResult());
			User loggedUser = (User) Contexts.getSessionContext().get("user");
			if (!users.contains(loggedUser)) {
				users.add(loggedUser);
			}
		}
		return users;
	}

	/**
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the device
	 */
	public Long getDevice() {
		return device;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public void setDevice(Long device) {
		this.device = device;
	}

	/**
	 * @return the devices
	 */
	@SuppressWarnings("unchecked")
	public List<Device> getDevices() {
		if (library != null) {
			String[] entities = new String[] { "Sample", "SampleConsumption" };
			Set<Device> devs = new HashSet<Device>();
			for (String s : entities) {
				try {
					devs.addAll(getEntityManager()
							.createQuery(
									"select distinct o.device from "
											+ s
											+ " o where "
											+ " o.algorithm.library.id = :library "
											+ "and o.user.id = :user")
							.setParameter("library", library)
							.setParameter("user", user).getResultList());
				} catch (Exception e) {
					System.err.println("Could not get the devices");
				}
			}
			devices.clear();
			devices.addAll(devs);
		}
		return devices;
	}

	/**
	 * @param devices
	 *            the devices to set
	 */
	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}

	/**
	 * @return the algorithms
	 */
	@SuppressWarnings("unchecked")
	public List<AlgorithmType> getAlgorithms() {
		if (device != null && user != null && library != null) {
			Query q = null;
			String[] entities = null;
			if (Chart.CONSUMPTION.equals(type)) {
				entities = new String[] { "SampleConsumption" };
			} else if (Chart.PERFORMANCE.equals(type)) {
				entities = new String[] { "Sample", "SampleConsumption" };
			} else {
				entities = new String[] { "Sample" };
			}
			Set<AlgorithmType> devs = new HashSet<AlgorithmType>();
			algorithms.clear();
			for (String s : entities) {
				try {
					q = getEntityManager().createQuery(
							"select distinct o.algorithm.type from " + s
									+ " o where "
									+ " o.algorithm.library.id = :library "
									+ "and o.user.id = :user "
									+ "and o.device.id = :device");
					q.setParameter("library", library);
					q.setParameter("device", device);
					q.setParameter("user", user);
					devs.addAll(q.getResultList());
				} catch (Exception e) {
					System.err.println("Could not get the algorithm types");
				}
			}
			algorithms.addAll(devs);
		}
		return algorithms;
	}

	/**
	 * @param algorithms
	 *            the algorithms to set
	 */
	public void setAlgorithms(List<AlgorithmType> algorithms) {
		this.algorithms = algorithms;
	}

	/**
	 * @return the algorithm
	 */
	public Long getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public void setAlgorithm(Long algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the operation
	 */
	public Long getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(Long operation) {
		this.operation = operation;
	}

	/**
	 * @return the operations
	 */
	@SuppressWarnings("unchecked")
	public List<OperationMode> getOperations() {
		if (device != null && user != null && library != null
				&& algorithm != null) {
			Query q = null;
			String[] entities = null;
			if (Chart.CONSUMPTION.equals(type)) {
				entities = new String[] { "SampleConsumption" };
			} else if (Chart.PERFORMANCE.equals(type)) {
				entities = new String[] { "Sample", "SampleConsumption" };
			} else {
				entities = new String[] { "Sample" };
			}
			Set<OperationMode> devs = new HashSet<OperationMode>();
			algorithms.clear();
			for (String s : entities) {
				try {
					q = getEntityManager().createQuery(
							"select distinct o.algorithm.operationMode from "
									+ s + " o where "
									+ " o.algorithm.library.id = :library "
									+ "and o.user.id = :user "
									+ "and o.algorithm.type.id = :type "
									+ "and o.device.id = :device");
					q.setParameter("library", library);
					q.setParameter("type", algorithm);
					q.setParameter("device", device);
					q.setParameter("user", user);
					devs.addAll(q.getResultList());
				} catch (Exception e) {
					System.err.println("Could not get the operation modes");
				}
			}
			operations.addAll(devs);
		}
		return operations;
	}

	/**
	 * @param operations
	 *            the operations to set
	 */
	public void setOperations(List<OperationMode> operations) {
		this.operations = operations;
	}

	/**
	 * @return the textSize
	 */
	public Integer getTextSize() {
		return textSize;
	}

	/**
	 * @param textSize
	 *            the textSize to set
	 */
	public void setTextSize(Integer textSize) {
		this.textSize = textSize;
	}

	/**
	 * @return the textSizes
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getTextSizes() {
		if (user != null && library != null) {
			textSizes = getEntityManager()
					.createQuery(
							"select distinct o.textSize from Sample o "
									+ "where o.user.id = :user and "
									+ "o.algorithm.library.id = :library")
					.setParameter("user", user)
					.setParameter("library", library).getResultList();
		}
		return textSizes;
	}

	/**
	 * @param textSizes
	 *            the textSizes to set
	 */
	public void setTextSizes(List<Integer> textSizes) {
		this.textSizes = textSizes;
	}

	/**
	 * @return the values
	 */
	public List<Long> getValues() {
		return values;
	}

	/**
	 * @param values
	 *            the values to set
	 */
	public void setValues(List<Long> values) {
		this.values = values;
	}

	/**
	 * @return the algs
	 */
	@SuppressWarnings("unchecked")
	public List<Algorithm> getAlgs() {
		if (device != null && user != null && library != null
				&& algorithm != null) {
			Query q = null;
			String[] entities = null;
			String text = "and o.textSize = :textSize ";
			if (Chart.CONSUMPTION.equals(type)) {
				entities = new String[] { "SampleConsumption" };
				text = "";
			} else if (Chart.PERFORMANCE.equals(type)) {
				entities = new String[] { "Sample", "SampleConsumption" };
			} else {
				entities = new String[] { "Sample" };
			}
			Set<Algorithm> devs = new LinkedHashSet<Algorithm>();//alteração alisson
			algorithms.clear();
			if (Long.valueOf(1).equals(algorithm)) {
				text += "and o.algorithm.operationMode.id = :operation ";
			}
			for (String s : entities) {
				try {
					q = getEntityManager().createQuery(
							"select distinct o.algorithm from " + s
									+ " o where "
									+ " o.algorithm.library.id = :library "
									+ "and o.user.id = :user " + text
									+ "and o.algorithm.type.id = :type "
									+ "and o.device.id = :device");
					q.setParameter("library", library);
					q.setParameter("type", algorithm);
					q.setParameter("device", device);
					q.setParameter("user", user);
					if (!Chart.CONSUMPTION.equals(type) && textSize != null) {
						q.setParameter("textSize", textSize);
					}
					if (Long.valueOf(1).equals(algorithm)) {
						q.setParameter("operation", operation);
					}
					devs.addAll(q.getResultList());
				} catch (Exception e) {
					System.err.println("Could not get the operation modes");
				}
			}
			algs.addAll(devs);
		}
		return algs;
	}

	/**
	 * @param algs
	 *            the algs to set
	 */
	public void setAlgs(List<Algorithm> algs) {
		this.algs = algs;
	}

	/**
	 * @return the slice
	 */
	public Integer getSlice() {
		return slice;
	}

	/**
	 * @param slice
	 *            the slice to set
	 */
	public void setSlice(Integer slice) {
		this.slice = slice;
	}
	
	/**
	 * @return the libraries
	 */
	@SuppressWarnings("unchecked")
	public Algorithm getAlgorithmById(Long id) {
		return (Algorithm)getEntityManager()
				.createQuery("select o from Algorithm o where o.id = :id")
				.setParameter("id", id).getResultList().get(0);
		
	}
		
}
