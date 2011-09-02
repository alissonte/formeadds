package br.ufc.meadds.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.servlet.ContextualHttpServletRequest;

public abstract class AbstractServletReceiver extends HttpServlet {

	protected static final String SELECT_USER_BY_ID = "select o from User o where o.id = :id";
	protected static final String SELECT_DEVICE_BY_ID = "select o from Device o where o.id = :id";
	protected static final String SELECT_ALGORITHM_BY_NAME = "select o from Algorithm o where o.name = :name "
			+ "and o.library.name = :library and o.type.name = :type and o.operationMode.name = :mode";

	/**
	 * 
	 */
	private static final long serialVersionUID = 2705076283831750610L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		InputStream is = request.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		byte[] b = new byte[1024];
		int length = 0;
		while ((length = is.read(b)) != -1) {
			baos.write(b, 0, length);
		}
		request.setAttribute("content", new String(baos.toByteArray()));
		new ContextualHttpServletRequest(request) {
			@Override
			public void process() throws Exception {
				doWork(request, response);
			}
		}.run();

	}

	protected abstract void doWork(HttpServletRequest request,
			HttpServletResponse response) throws IOException;

}
