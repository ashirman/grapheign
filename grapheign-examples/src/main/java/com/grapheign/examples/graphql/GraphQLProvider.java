package com.grapheign.examples.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.grapheign.core.GraphqlTypeFetcherSupplier;
import com.grapheign.examples.types.Droid;
import com.grapheign.examples.types.Human;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.RuntimeWiring.Builder;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.idl.TypeRuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
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
        Builder wiring = RuntimeWiring.newRuntimeWiring()
                .type("Human", typeWiring -> typeWiring
                        //.dataFetcher("friends", StarWarsData.getFriendsDataFetcher())
                )
                // you can use builder syntax if you don't like the lambda syntax
                .type("Droid", typeWiring -> typeWiring
                        //.dataFetcher("friends", StarWarsData.getFriendsDataFetcher())
                )
                // or full builder syntax if that takes your fancy
                .type(
                        newTypeWiring("Character")
                                .typeResolver(characterTypeResolver())
                                .build()
                );

        graphqlTypeFetcherSupplier.get().forEach((type, fieldDataFetchers) -> wiring.type(typeRuntimeWiring(type, fieldDataFetchers)));


        return wiring.build();
    }

    private TypeResolver characterTypeResolver() {
        return env -> {
            Object javaObject = env.getObject();
            if (javaObject instanceof Human) {
                return env.getSchema().getObjectType("Human");
            } else if (javaObject instanceof Droid) {
                return env.getSchema().getObjectType("Droid");
            } else {
                return env.getSchema().getObjectType("unknown?");
            }
        };
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
