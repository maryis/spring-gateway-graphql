package jdlf.compass.timetableIntegration.it;

import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@com.graphql.spring.boot.test.GraphQLTest
public class GraphQLTest {

        @Autowired
        private GraphQLTestTemplate graphQLTestTemplate;

//        @Test
//        public void routes() throws IOException {
//            GraphQLResponse response = graphQLTestTemplate.postForResource("test.graphql");
//            assertNotNull(response);
//            assertTrue(response.isOk());
////            assertEquals("1", response.get("$.data"));
//            System.out.println(response.get(("$.data")));
//        }
}
