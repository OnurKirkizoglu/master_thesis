package at.jku.sea.cloud.rest.pojo.stream.provider;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Provider")
@JsonSubTypes({ @Type(value = PojoCollectionArtifactOnlyArtifactProvider.class),
    @Type(value = PojoCollectionArtifactProvider.class), @Type(value = PojoCollectionProvider.class),
    @Type(value = PojoContainerProvider.class), @Type(value = PojoContainerWithFilterProvider.class),
    @Type(value = PojoMapArtifactKeyProvider.class), @Type(value = PojoMapArtifactValueProvider.class),
    @Type(value = PojoMetaModelProvider.class), @Type(value = PojoOwnerProvider.class),
    @Type(value = PojoProjectPackageProvider.class), @Type(value = PojoProjectProvider.class),
    @Type(value = PojoToolProvider.class), @Type(value = PojoWorkspaceWithFilterProvider.class),
    @Type(value = PojoResourceProvider.class) })
public abstract class PojoProvider {

}
