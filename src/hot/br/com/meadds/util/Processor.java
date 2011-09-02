package br.com.meadds.util;

import br.ufc.meadds.entity.User;

public interface Processor {

	String getName();

	byte[] getChart();
	
	void setUser(User user);

}
