package at.jku.sea.cloud.rest.pojo.predicate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = Id.NAME, property = "__type")
@JsonTypeName(value = "Predicate")
@JsonSubTypes({ @Type(value = PojoPredicateContainsArtifact.class), @Type(value = PojoPredicateContainsKey.class),
    @Type(value = PojoPredicateContainsValue.class), @Type(value = PojoPredicateEq.class),
    @Type(value = PojoPredicateEqContext.class), @Type(value = PojoPredicateExistsElement.class),
    @Type(value = PojoPredicateHasId.class), @Type(value = PojoPredicateHasOwner.class),
    @Type(value = PojoPredicateHasProperty.class), @Type(value = PojoPredicateHasPropertyValue.class),
    @Type(value = PojoPredicateHasTool.class), @Type(value = PojoPredicateHasType.class),
    @Type(value = PojoPredicateHasVersion.class), @Type(value = PojoPredicateInContainer.class),
    @Type(value = PojoPredicateInv.class), @Type(value = PojoPredicateIsAlive.class),
    @Type(value = PojoPredicateIsPropertyAlive.class), @Type(value = PojoPredicateStreamPredicate.class),
    @Type(value = PojoPredicateOr.class), @Type(value = PojoPredicateAnd.class) })
public abstract class PojoPredicate {

}
