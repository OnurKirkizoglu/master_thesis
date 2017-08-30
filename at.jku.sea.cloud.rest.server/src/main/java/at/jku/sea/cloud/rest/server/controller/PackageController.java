package at.jku.sea.cloud.rest.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import at.jku.sea.cloud.rest.pojo.PojoPackage;
import at.jku.sea.cloud.rest.server.handler.PackageHandler;

/**
 * 
 * @author gkanos
 */
@Controller
@RequestMapping("/designspace/packages")
public class PackageController {

  @Autowired
  PackageHandler handler;

  @RequestMapping(value = "/id={id}&v={version}", method = RequestMethod.GET)
  public ResponseEntity<PojoPackage[]> getPackages(@PathVariable long id, @PathVariable long version) {
    PojoPackage[] result = handler.getPackages(id, version);
    return new ResponseEntity<PojoPackage[]>(result, HttpStatus.OK);
  };
}
