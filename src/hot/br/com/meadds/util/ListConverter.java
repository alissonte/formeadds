package br.com.meadds.util;

import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

@Name("listConverter")
@BypassInterceptors
@Converter
public class ListConverter implements javax.faces.convert.Converter {

	public Object getAsObject(FacesContext context, UIComponent component,
			String value) throws ConverterException {
		List<Long> result = null;
		if (value != null && value.trim().length() != 0) {
			String[] values = value.split(",");
			if (values != null && values.length > 0 && !values[0].equals("")) {
				result = new LinkedList<Long>();
				for (String l : values) {
					result.add(Long.valueOf(l));
				}
			}
		}

		return result;
	}

	public String getAsString(FacesContext context, UIComponent component,
			Object value) throws ConverterException {
		String result = "";
		String v = value.toString();
		for (Object o : v.substring(1, v.length() - 1).split(",")) {
			result += String.valueOf(o).trim() + ",";
		}
		return result.substring(0, result.length() - 1);
	}

}
