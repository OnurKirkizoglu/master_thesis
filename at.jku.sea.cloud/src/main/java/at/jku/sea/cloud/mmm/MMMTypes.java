package at.jku.sea.cloud.mmm;

import java.util.Arrays;
import java.util.Collection;

import at.jku.sea.cloud.DataStorage;

public class MMMTypes {

  private static Collection<Long> rootArtifacts = Arrays.asList(new Long[] { DataStorage.COMPLEX_TYPE, DataStorage.ENUMM, DataStorage.ENUMM_LITERAL, DataStorage.OPERATION, DataStorage.PARAMETER,
      DataStorage.TYPE, DataStorage.TYPED_ELEMENT, DataStorage.DATA_TYPE, DataStorage.FEATURE, DataStorage.TYPE_PARAMETER });

  // prevent initialization
  private MMMTypes() {
  }

  public static Collection<Long> getRootArtifacts() {
    return rootArtifacts;
  }

}
