package matrix;

/**
 * Represents the different type of matrix elements:<br>
 * 
 * - NONE = Needed especially for element (0/0) of matrix or default element.<br>
 * - HEADER = Only for header elements x/y= (0/y) or (x/0)<br>
 * - SPECIFIC_LINK = Element represents a specific link.<br>
 * - MULTIPLE_LINK = Element represents a non-specific link.<br>
 */
public enum MatrixElementType {
	NONE, HEADER, SPECIFIC_LINK, MULTIPLE_LINK; 
}
