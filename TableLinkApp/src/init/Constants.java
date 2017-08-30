package init;

public class Constants {

	private Constants(){}

	public static final String METAMODEL_LINK_SOURCE = "Source";
	public static final String METAMODEL_LINK_TARGET = "Target";
	public static final String METAMODEL_LINK_SOURCE_CARDINALITY = "Source Card";
	public static final String METAMODEL_LINK_TARGET_CARDINALITY = "Target Card";

	// data types
	public static final String INT = "int";
	public static final String BOOLEAN = "boolean";
	public static final String VOID = "void";
	public static final String STRING = "String";
	
	// package names
	public static final String PKG_SETUP = "setupPackage";

	// supertype names
	public static final String TYPE_LINK = "Link";
	
	// special type names (for links)
	public static final String TYPE_DESCRIPTION = "DescriptionType";
	
	// link feature names
	public static final String LINK_DESCRIPTION = "lt::Description";
	public static final String LINK_SOURCE = "lt::Source";
	public static final String LINK_TARGET = "lt::Target";

	/** Feature of the description complex type */
	public static final String DESC_DESCRIPTION = "Description";

	/** Special property exclusive to link packages */
	public static final String PACKAGE_LINK_PROPERTY = "isLinkPackage";
	

	public static final String COMBOBOX_ALL_SELECTION = "-----------------------";


}
