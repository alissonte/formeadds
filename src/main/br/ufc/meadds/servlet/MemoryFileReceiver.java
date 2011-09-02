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
import br.ufc.meadds.entity.Sample;
import br.ufc.meadds.entity.User;

/**
 * Servlet implementation class SampleReceiver
 */
public class MemoryFileReceiver extends AbstractServletReceiver {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2904080058260678441L;

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
					if (o.length != 10) {
						break;
					}
					Sample sample = new Sample();
					Algorithm algorithm = (Algorithm) em
							.createQuery(SELECT_ALGORITHM_BY_NAME)
							.setParameter("name", o[2])
							.setParameter("type", o[1])
							.setParameter("mode", o[3])
							.setParameter("library", o[0]).getSingleResult();
					sample.setAlgorithm(algorithm);
					sample.setUser(user);
					sample.setDevice(device);
					sample.setSampleDate(df.parse(o[4]));
					sample.setTextSize(Integer.parseInt(o[5]));
					sample.setEncryptTime(Long.parseLong(o[6]));
					sample.setDecryptTime(Long.parseLong(o[7]));
					sample.setEncryptionMemory(Integer.parseInt(o[8]));
					sample.setDecryptionMemory(Integer.parseInt(o[9]));
					em.persist(sample);
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
