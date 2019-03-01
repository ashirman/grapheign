package com.grapheign.core;

import graphql.schema.DataFetcher;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
* returns {@code Map} where {@code key} is Graphql type name and {@code value} is a collection of {@code DataFetchers}
 * for the fields of given type. For example for {@code Query} type of following schema
 * <pre>
 *{@code
 *     type Query {
 *      hero(episode: Episode): Character
 *      human(id: String!): Human
 *      droid(id: String!): Droid
 *      character(id: String!): Character
 *     }
 * }
 * </pre>
 * it should return structure similar to following:
 * <pre>
 *     {@code
 *      "Query" -> {
 *          {"hero" -> heroDataFetcher},
 *          {"human" -> humanDataFetcher},
 *          {"droid" -> droidDataFetcher},
 *          {"character" -> characterDataFetcher}
 *      }
 *     }
 *
 * </pre>
 *
 * It is supposed that interface can be used for any types of qraphql schema and NOT only for {@code Query} and {@code Mutation}.
 * so having following type defined in graphql schema
 * <pre>
 *     {@code
 *      type Human implements Character {
 *          id: String!
 *          name: String
 *          friends: [Character]
 *          appearsIn: [Episode]
 *          homePlanet: String
 *      }
 *     }
 * </pre>
 *
 * it should be possible to receive mapping as following
 * <pre>
 *     {@code
 *      "Human" -> {
 *          {"friends" -> friendsDataFetcher}
 *      }
 *     }
 * </pre>
 *
 * so, {@code friends} field of {@code Human} will be retrieved by a separate fetcher
 */
public interface GraphqlTypeFetcherSupplier extends Supplier<Map<String, Collection<Map.Entry<String, DataFetcher>>>> {
}
