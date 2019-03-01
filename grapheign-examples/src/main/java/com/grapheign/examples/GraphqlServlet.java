package com.grapheign.examples;

import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.servlet.GraphQLConfiguration;
import graphql.servlet.GraphQLHttpServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(name = "HelloServlet", urlPatterns = {"graphql"}, loadOnStartup = 1)
public class GraphqlServlet extends GraphQLHttpServlet {
    @Override
    protected GraphQLConfiguration getConfiguration() {
        return GraphQLConfiguration.with(createSchema()).build();
    }

    private GraphQLSchema createSchema() {
        String schema = "type Query{hello: String}";

        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);

        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().type()build()
                .type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world")))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }
}
