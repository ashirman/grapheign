package com.grapheign.examples.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.grapheign.core.GraphqlTypeFetcherSupplier;
import com.grapheign.examples.FeignGraphqlTypeFetcherSupplier;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.io.Resources.getResource;
import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;
import static java.util.stream.Collectors.toMap;

@Component
public class GraphQLProvider {
    private final GraphqlTypeFetcherSupplier graphqlTypeFetcherSupplier;

    @Autowired
    public GraphQLProvider(GraphqlTypeFetcherSupplier graphqlTypeFetcherSupplier) {
        this.graphqlTypeFetcherSupplier = graphqlTypeFetcherSupplier;
    }

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
        Builder wiring = RuntimeWiring.newRuntimeWiring();

        graphqlTypeFetcherSupplier.get().forEach((type, fieldDataFetchers) -> wiring.type(typeRuntimeWiring(type, fieldDataFetchers)));

        //wiring.type(newTypeWiring("Character").typeResolver((env) -> env.))

        return wiring.build();
    }

    private TypeRuntimeWiring typeRuntimeWiring(String typename, Collection<Entry<String, DataFetcher>> fieldsDataFetchers) {
        Map<String, DataFetcher> fetchersMap = fieldsDataFetchers.stream().collect(toMap(Entry::getKey, Entry::getValue));
        return newTypeWiring(typename).dataFetchers(fetchersMap).build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}
