package jdlf.compass.timetableIntegration.config;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQlCustomScalarTypeConfig {

    private static final String GRAPHQL_VOID_SCALAR_BEAN = "voidScalar";
    private static final String GRAPHQL_LONG_SCALAR_BEAN = "longScalar";

    @Bean(name = GRAPHQL_VOID_SCALAR_BEAN)
    public GraphQLScalarType voidScalar() {
        Coercing<Void, Void> coercing = new Coercing<>() {
            @Override
            public Void serialize(Object dataFetcherResult) {
                return null;
            }

            @Override
            public Void parseValue(Object input) {
                return null;
            }

            @Override
            public Void parseLiteral(Object input) {
                return null;
            }
        };

        return GraphQLScalarType.newScalar()
                .name("GraphQLVoid")
                .description("Represents void values")
                .coercing(coercing)
                .build();
    }

    @Bean(name = GRAPHQL_LONG_SCALAR_BEAN)
    public GraphQLScalarType longScalar() {
        Coercing<String, Long> coercing = new Coercing<>() {
            @Override
            public Long serialize(Object dataFetcherResult) {
                return Long.parseLong(dataFetcherResult.toString());
            }

            @Override
            public String parseValue(Object input) {
                if (!(input instanceof StringValue)) return null;
                return ((StringValue) input).getValue();
            }

            @Override
            public String parseLiteral(Object input) {
                if (!(input instanceof StringValue)) return null;
                return ((StringValue) input).getValue();
            }
        };

        return GraphQLScalarType.newScalar()
                .name("GraphQLLong")
                .description("Used to parse long values")
                .coercing(coercing)
                .build();
    }
}
