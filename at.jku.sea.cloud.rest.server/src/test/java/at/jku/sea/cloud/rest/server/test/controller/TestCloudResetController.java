package at.jku.sea.cloud.rest.server.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import at.jku.sea.cloud.rest.server.handler.CloudHandler;
import at.jku.sea.cloud.rest.server.test.handler.TestCloudHandler;

@Controller
@RequestMapping("/designspace")
public class TestCloudResetController {

  @Autowired
  TestCloudHandler handler;

  @RequestMapping(value = "/reset", method = RequestMethod.DELETE)
  @ResponseBody
  public void reset() {
    handler.reset();
  }

}
