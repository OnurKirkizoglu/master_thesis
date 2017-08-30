package at.jku.sea.cloud.rest.server.test.handler;

import at.jku.sea.cloud.DataStorage;

public class TestCloudHandler {

  public void reset() {
    try {
      DataStorage storage = (DataStorage) trmi.Naming.lookup("//localhost:1099/DataStorage");
      storage.truncateAll();
      storage.init();
    } catch (final Exception e) {
      System.err.println(e.toString());
    }
  }

}
