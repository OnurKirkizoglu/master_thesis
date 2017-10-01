package application.setup;
/** Listener to notificate views when changes occur in the model.
 * 
 */
public interface ModelListener {
    /**
     * the selection of data packages changed
     */
    void dataPackagesChanged();

    /**
     * the content of the selected data packages changed
     */
    void dataContentChanged();

    /**
     * indicates a switch to another linkPackage
     */
    void linkPackageChanged();
    
    /**
     * 
     */
    void linkContentChanged();
}
