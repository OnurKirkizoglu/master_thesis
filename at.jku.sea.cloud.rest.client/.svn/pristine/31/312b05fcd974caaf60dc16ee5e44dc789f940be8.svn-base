package at.jku.sea.cloud.rest.client.test.utility;

import at.jku.sea.cloud.rest.client.handler.AbstractHandler;

public class ResetUtility extends AbstractHandler {

  public void resetCloud() {
    try {
      template.delete(this.CLOUD_ADDRESS + "/reset");
    } catch (Exception e) {
      System.err.println("TestServer probably not started");
    }
  }
}
