package jdlf.compass.timetableIntegration.service;

import java.util.List;
import java.util.Optional;

public interface CompassService<TPojo, TID> {
  TPojo create(TPojo entity);

  void delete(TID id);

  //  void deleteAll();

  // boolean existsById(TID id);

  //    Iterable<TPojo> findAll();

  List<TPojo> findAll(int page, int size);

  Optional<TPojo> findById(TID id);

  //    TPojo findOrThrow(TID id);

  //  String getEntityClassSimpleName();

  //    Page<TPojo> history(TID id, Pageable pageable);

  TPojo update(TPojo entity, TID id);
}
