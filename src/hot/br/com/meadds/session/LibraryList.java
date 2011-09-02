package br.com.meadds.session;

import br.ufc.meadds.entity.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("libraryList")
public class LibraryList extends EntityQuery<Library> {

    /**
     * 
     */
    private static final long serialVersionUID = 6708117885835066935L;

    private static final String EJBQL = "select library from Library library";

    private static final String[] RESTRICTIONS = {
        "lower(library.language) like lower(concat(#{libraryList.library.language},'%'))",
        "lower(library.name) like lower(concat(#{libraryList.library.name},'%'))",
        "lower(library.plataform) like lower(concat(#{libraryList.library.plataform},'%'))",};

    private Library library = new Library();

    public LibraryList() {
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        setMaxResults(25);
        setRestrictionLogicOperator("or");
    }

    public Library getLibrary() {
        return library;
    }
}
