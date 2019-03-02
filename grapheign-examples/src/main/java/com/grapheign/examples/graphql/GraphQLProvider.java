package com.grapheign.examples.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.google.common.io.Resources.getResource;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

//@Component
public class GraphQLProvider {

    private GraphQL graphQL;

    @PostConstruct
    public void init() throws IOException {
        String sdl = Resources.toString(getResource("swapi.graphqls"), Charsets.UTF_8);
        this.graphQL = GraphQL.newGraphQL(buildSchema(sdl)).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        return new SchemaGenerator().makeExecutableSchema(typeRegistry, buildWiring());
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .build())
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}
