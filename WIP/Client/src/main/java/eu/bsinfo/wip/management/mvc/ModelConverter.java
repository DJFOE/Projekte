package eu.bsinfo.wip.management.mvc;

public interface ModelConverter<E, M> {


    M entityToModel(E entity);

    E modelToEntity(M model);

}
