package jdlf.compass.timetableIntegration.resolver;

import jdlf.compass.timetableIntegration.model.CompassId;
import jdlf.compass.timetableIntegration.model.Rotation;
import jdlf.compass.timetableIntegration.service.RotationService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public class RotationResolver {
    private RotationResolver() {
    }

    @Component
    public static class Query implements GraphQLQueryResolver {

        private final RotationService rotationService;

        @Autowired
        public Query(RotationService rotationService) {
            this.rotationService = rotationService;
        }

//        public List<Rotation> rotationsInWorkingSet(String workingSetId, int page, int size) {
//            return rotationService.findAllByIdWorkingSetId(workingSetId, PageRequest.of(page, size));
//        }

        public List<Rotation> rotations(int page, int size) {
            return rotationService.findAll(page,size);
        }

        public Optional<Rotation> rotation(CompassId id) {
            return rotationService.findById(id);
        }

//        public List<String> rotationHistory(CompassId id, int page, int size) {
//            return rotationService.history(id, PageRequest.of(page, size));
//        }
    }

    @Component
    public static class Mutation implements GraphQLMutationResolver {

        private final RotationService rotationService;

        @Autowired
        public Mutation(RotationService rotationService) {
            this.rotationService = rotationService;
        }

        public CompassId createRotation(Rotation rotation) {
            return rotationService.create(rotation).getId();
        }

//        public CompassId updateRotation(Rotation rotation) {
//            return rotationService.update(rotation).getId();
//        }

        public void deleteRotation(CompassId compassId) {
            rotationService.delete(compassId);
        }
    }
}
