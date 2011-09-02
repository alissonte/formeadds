package br.ufc.meadds.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;

import br.ufc.meadds.entity.Algorithm;
import br.ufc.meadds.entity.Device;
import br.ufc.meadds.entity.SampleConsumption;
import br.ufc.meadds.entity.User;

/**
 * Servlet implementation class SampleReceiver
 */
public class ConsumptionFileReceiver extends AbstractServletReceiver {

	private static final long serialVersionUID = 1L;

	public void doWork(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		EntityManager em = (EntityManager) Component
				.getInstance("entityManager");
		String content = (String) request.getAttribute("content");

		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		int counter = 0;
		EntityTransaction tx = em.getTransaction();
		try {
			User user = null;
			Device device = null;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			tx.begin();
			while ((line = reader.readLine()) != null) {
				switch (counter) {
				case 0:
					user = (User) em
							.createQuery(SELECT_USER_BY_ID)
							.setParameter("id",
									Long.parseLong(line.split(",")[1]))
							.getSingleResult();
					if (user == null) {
						throw new RuntimeException("Unable to fnd user");
					}
					break;
				case 1:
					device = (Device) em
							.createQuery(SELECT_DEVICE_BY_ID)
							.setParameter("id",
									Long.parseLong(line.split(",")[1]))
							.getSingleResult();
					if (device.getUser() != user) {
						throw new RuntimeException("Invalid device owner.");
					}
					break;
				case 2:
					break;
				default:
					String[] o = line.split(",");
					if (o.length != 9) {
						break;
					}
					SampleConsumption consumption = new SampleConsumption();
					Algorithm algorithm = (Algorithm) em
							.createQuery(SELECT_ALGORITHM_BY_NAME)
							.setParameter("name", o[2])
							.setParameter("type", o[1])
							.setParameter("mode", o[3])
							.setParameter("library", o[0]).getSingleResult();
					consumption.setAlgorithm(algorithm);
					consumption.setDate(df.parse(o[4]));
					consumption.setAverageConsumptionEncrypt(Double
							.parseDouble(o[5]));
					consumption.setAverageConsumptionDecrypt(Double
							.parseDouble(o[6]));
					consumption.setEncryptionRounds(Integer.parseInt(o[7]));
					consumption.setDecryptionRounds(Integer.parseInt(o[8]));
					consumption.setUser(user);
					consumption.setDevice(device);
					em.persist(consumption);
				}
				counter++;
			}
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
