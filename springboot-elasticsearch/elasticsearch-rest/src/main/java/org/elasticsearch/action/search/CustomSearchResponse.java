package org.elasticsearch.action.search;

import com.daishuai.elasticsearch.client.rest.cutomer.CustomSearchHits;
import org.elasticsearch.Version;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.StatusToXContentObject;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.RestActions;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.profile.ProfileShardResult;
import org.elasticsearch.search.profile.SearchProfileShardResults;
import org.elasticsearch.search.suggest.Suggest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.elasticsearch.action.search.ShardSearchFailure.readShardSearchFailure;
import static org.elasticsearch.common.xcontent.XContentParserUtils.ensureExpectedToken;

/**
 * @author Daishuai
 * @date 2021/1/13 17:31
 */
public class CustomSearchResponse extends ActionResponse implements StatusToXContentObject {

    private static final ParseField SCROLL_ID = new ParseField("_scroll_id");
    private static final ParseField TOOK = new ParseField("took");
    private static final ParseField TIMED_OUT = new ParseField("timed_out");
    private static final ParseField TERMINATED_EARLY = new ParseField("terminated_early");
    private static final ParseField NUM_REDUCE_PHASES = new ParseField("num_reduce_phases");

    private CustomSearchResponseSections internalResponse;

    private String scrollId;

    private int totalShards;

    private int successfulShards;

    private int skippedShards;

    private ShardSearchFailure[] shardFailures;

    private CustomSearchResponse.Clusters clusters;

    private long tookInMillis;

    public CustomSearchResponse() {
    }

    public CustomSearchResponse(CustomSearchResponseSections internalResponse, String scrollId, int totalShards, int successfulShards,
                          int skippedShards, long tookInMillis, ShardSearchFailure[] shardFailures, CustomSearchResponse.Clusters clusters) {
        this.internalResponse = internalResponse;
        this.scrollId = scrollId;
        this.clusters = clusters;
        this.totalShards = totalShards;
        this.successfulShards = successfulShards;
        this.skippedShards = skippedShards;
        this.tookInMillis = tookInMillis;
        this.shardFailures = shardFailures;
        assert skippedShards <= totalShards : "skipped: " + skippedShards + " total: " + totalShards;
    }

    @Override
    public RestStatus status() {
        return RestStatus.status(successfulShards, totalShards, shardFailures);
    }

    /**
     * The search hits.
     */
    public CustomSearchHits getHits() {
        return internalResponse.hits();
    }

    public Aggregations getAggregations() {
        return internalResponse.aggregations();
    }


    public Suggest getSuggest() {
        return internalResponse.suggest();
    }

    /**
     * Has the search operation timed out.
     */
    public boolean isTimedOut() {
        return internalResponse.timedOut();
    }

    /**
     * Has the search operation terminated early due to reaching
     * <code>terminateAfter</code>
     */
    public Boolean isTerminatedEarly() {
        return internalResponse.terminatedEarly();
    }

    /**
     * Returns the number of reduce phases applied to obtain this search response
     */
    public int getNumReducePhases() {
        return internalResponse.getNumReducePhases();
    }

    /**
     * How long the search took.
     */
    public TimeValue getTook() {
        return new TimeValue(tookInMillis);
    }

    /**
     * The total number of shards the search was executed on.
     */
    public int getTotalShards() {
        return totalShards;
    }

    /**
     * The successful number of shards the search was executed on.
     */
    public int getSuccessfulShards() {
        return successfulShards;
    }


    /**
     * The number of shards skipped due to pre-filtering
     */
    public int getSkippedShards() {
        return skippedShards;
    }

    /**
     * The failed number of shards the search was executed on.
     */
    public int getFailedShards() {
        // we don't return totalShards - successfulShards, we don't count "no shards available" as a failed shard, just don't
        // count it in the successful counter
        return shardFailures.length;
    }

    /**
     * The failures that occurred during the search.
     */
    public ShardSearchFailure[] getShardFailures() {
        return this.shardFailures;
    }

    /**
     * If scrolling was enabled ({@link SearchRequest#scroll(org.elasticsearch.search.Scroll)}, the
     * scroll id that can be used to continue scrolling.
     */
    public String getScrollId() {
        return scrollId;
    }

    public void scrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    /**
     * If profiling was enabled, this returns an object containing the profile results from
     * each shard.  If profiling was not enabled, this will return null
     *
     * @return The profile results or an empty map
     */
    @Nullable
    public Map<String, ProfileShardResult> getProfileResults() {
        return internalResponse.profile();
    }

    /**
     * Returns info about what clusters the search was executed against. Available only in responses obtained
     * from a Cross Cluster Search request, otherwise <code>null</code>
     * @see SearchResponse.Clusters
     */
    public CustomSearchResponse.Clusters getClusters() {
        return clusters;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject();
        innerToXContent(builder, params);
        builder.endObject();
        return builder;
    }

    public XContentBuilder innerToXContent(XContentBuilder builder, Params params) throws IOException {
        if (scrollId != null) {
            builder.field(SCROLL_ID.getPreferredName(), scrollId);
        }
        builder.field(TOOK.getPreferredName(), tookInMillis);
        builder.field(TIMED_OUT.getPreferredName(), isTimedOut());
        if (isTerminatedEarly() != null) {
            builder.field(TERMINATED_EARLY.getPreferredName(), isTerminatedEarly());
        }
        if (getNumReducePhases() != 1) {
            builder.field(NUM_REDUCE_PHASES.getPreferredName(), getNumReducePhases());
        }
        RestActions.buildBroadcastShardsHeader(builder, params, getTotalShards(), getSuccessfulShards(), getSkippedShards(),
                getFailedShards(), getShardFailures());
        clusters.toXContent(builder, params);
        internalResponse.toXContent(builder, params);
        return builder;
    }

    public static CustomSearchResponse fromXContent(XContentParser parser) throws IOException {
        ensureExpectedToken(XContentParser.Token.START_OBJECT, parser.nextToken(), parser::getTokenLocation);
        parser.nextToken();
        return innerFromXContent(parser);
    }

    static CustomSearchResponse innerFromXContent(XContentParser parser) throws IOException {
        ensureExpectedToken(XContentParser.Token.FIELD_NAME, parser.currentToken(), parser::getTokenLocation);
        String currentFieldName = parser.currentName();
        CustomSearchHits hits = null;
        Aggregations aggs = null;
        Suggest suggest = null;
        SearchProfileShardResults profile = null;
        boolean timedOut = false;
        Boolean terminatedEarly = null;
        int numReducePhases = 1;
        long tookInMillis = -1;
        int successfulShards = -1;
        int totalShards = -1;
        int skippedShards = 0; // 0 for BWC
        String scrollId = null;
        List<ShardSearchFailure> failures = new ArrayList<>();
        CustomSearchResponse.Clusters clusters = CustomSearchResponse.Clusters.EMPTY;
        for (XContentParser.Token token = parser.nextToken(); token != XContentParser.Token.END_OBJECT; token = parser.nextToken()) {
            if (token == XContentParser.Token.FIELD_NAME) {
                currentFieldName = parser.currentName();
            } else if (token.isValue()) {
                if (SCROLL_ID.match(currentFieldName, parser.getDeprecationHandler())) {
                    scrollId = parser.text();
                } else if (TOOK.match(currentFieldName, parser.getDeprecationHandler())) {
                    tookInMillis = parser.longValue();
                } else if (TIMED_OUT.match(currentFieldName, parser.getDeprecationHandler())) {
                    timedOut = parser.booleanValue();
                } else if (TERMINATED_EARLY.match(currentFieldName, parser.getDeprecationHandler())) {
                    terminatedEarly = parser.booleanValue();
                } else if (NUM_REDUCE_PHASES.match(currentFieldName, parser.getDeprecationHandler())) {
                    numReducePhases = parser.intValue();
                } else {
                    parser.skipChildren();
                }
            } else if (token == XContentParser.Token.START_OBJECT) {
                if (SearchHits.Fields.HITS.equals(currentFieldName)) {
                    hits = CustomSearchHits.fromXContent(parser);
                } else if (Aggregations.AGGREGATIONS_FIELD.equals(currentFieldName)) {
                    aggs = Aggregations.fromXContent(parser);
                } else if (Suggest.NAME.equals(currentFieldName)) {
                    suggest = Suggest.fromXContent(parser);
                } else if (SearchProfileShardResults.PROFILE_FIELD.equals(currentFieldName)) {
                    profile = SearchProfileShardResults.fromXContent(parser);
                } else if (RestActions._SHARDS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token.isValue()) {
                            if (RestActions.FAILED_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                parser.intValue(); // we don't need it but need to consume it
                            } else if (RestActions.SUCCESSFUL_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                successfulShards = parser.intValue();
                            } else if (RestActions.TOTAL_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                totalShards = parser.intValue();
                            } else if (RestActions.SKIPPED_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                skippedShards = parser.intValue();
                            } else {
                                parser.skipChildren();
                            }
                        } else if (token == XContentParser.Token.START_ARRAY) {
                            if (RestActions.FAILURES_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                while((token = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
                                    failures.add(ShardSearchFailure.fromXContent(parser));
                                }
                            } else {
                                parser.skipChildren();
                            }
                        } else {
                            parser.skipChildren();
                        }
                    }
                } else if (SearchResponse.Clusters._CLUSTERS_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                    int successful = -1;
                    int total = -1;
                    int skipped = -1;
                    while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                        if (token == XContentParser.Token.FIELD_NAME) {
                            currentFieldName = parser.currentName();
                        } else if (token.isValue()) {
                            if (SearchResponse.Clusters.SUCCESSFUL_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                successful = parser.intValue();
                            } else if (SearchResponse.Clusters.TOTAL_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                total = parser.intValue();
                            } else if (SearchResponse.Clusters.SKIPPED_FIELD.match(currentFieldName, parser.getDeprecationHandler())) {
                                skipped = parser.intValue();
                            } else {
                                parser.skipChildren();
                            }
                        } else {
                            parser.skipChildren();
                        }
                    }
                    clusters = new CustomSearchResponse.Clusters(total, successful, skipped);
                } else {
                    parser.skipChildren();
                }
            }
        }
        CustomSearchResponseSections searchResponseSections = new CustomSearchResponseSections(hits, aggs, suggest, timedOut, terminatedEarly,
                profile, numReducePhases);
        return new CustomSearchResponse(searchResponseSections, scrollId, totalShards, successfulShards, skippedShards, tookInMillis,
                failures.toArray(new ShardSearchFailure[failures.size()]), clusters);
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        internalResponse = new CustomInternalSearchResponse(in);
        totalShards = in.readVInt();
        successfulShards = in.readVInt();
        int size = in.readVInt();
        if (size == 0) {
            shardFailures = ShardSearchFailure.EMPTY_ARRAY;
        } else {
            shardFailures = new ShardSearchFailure[size];
            for (int i = 0; i < shardFailures.length; i++) {
                shardFailures[i] = readShardSearchFailure(in);
            }
        }
        if (in.getVersion().onOrAfter(Version.V_6_1_0)) {
            clusters = new CustomSearchResponse.Clusters(in);
        } else {
            clusters = CustomSearchResponse.Clusters.EMPTY;
        }
        scrollId = in.readOptionalString();
        tookInMillis = in.readVLong();
        if (in.getVersion().onOrAfter(Version.V_5_6_0)) {
            skippedShards = in.readVInt();
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        internalResponse.writeTo(out);
        out.writeVInt(totalShards);
        out.writeVInt(successfulShards);

        out.writeVInt(shardFailures.length);
        for (ShardSearchFailure shardSearchFailure : shardFailures) {
            shardSearchFailure.writeTo(out);
        }
        if (out.getVersion().onOrAfter(Version.V_6_1_0)) {
            clusters.writeTo(out);
        }
        out.writeOptionalString(scrollId);
        out.writeVLong(tookInMillis);
        if(out.getVersion().onOrAfter(Version.V_5_6_0)) {
            out.writeVInt(skippedShards);
        }
    }

    @Override
    public String toString() {
        return Strings.toString(this);
    }

    /**
     * Holds info about the clusters that the search was executed on: how many in total, how many of them were successful
     * and how many of them were skipped.
     */
    public static class Clusters implements ToXContent, Writeable {

        public static final CustomSearchResponse.Clusters EMPTY = new CustomSearchResponse.Clusters(0, 0, 0);

        static final ParseField _CLUSTERS_FIELD = new ParseField("_clusters");
        static final ParseField SUCCESSFUL_FIELD = new ParseField("successful");
        static final ParseField SKIPPED_FIELD = new ParseField("skipped");
        static final ParseField TOTAL_FIELD = new ParseField("total");

        private final int total;
        private final int successful;
        private final int skipped;

        public Clusters(int total, int successful, int skipped) {
            assert total >= 0 && successful >= 0 && skipped >= 0
                    : "total: " + total + " successful: " + successful + " skipped: " + skipped;
            assert successful <= total && skipped == total - successful
                    : "total: " + total + " successful: " + successful + " skipped: " + skipped;
            this.total = total;
            this.successful = successful;
            this.skipped = skipped;
        }

        private Clusters(StreamInput in) throws IOException {
            this.total = in.readVInt();
            this.successful = in.readVInt();
            this.skipped = in.readVInt();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(total);
            out.writeVInt(successful);
            out.writeVInt(skipped);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            if (this != EMPTY) {
                builder.startObject(_CLUSTERS_FIELD.getPreferredName());
                builder.field(TOTAL_FIELD.getPreferredName(), total);
                builder.field(SUCCESSFUL_FIELD.getPreferredName(), successful);
                builder.field(SKIPPED_FIELD.getPreferredName(), skipped);
                builder.endObject();
            }
            return builder;
        }

        /**
         * Returns how many total clusters the search was requested to be executed on
         */
        public int getTotal() {
            return total;
        }

        /**
         * Returns how many total clusters the search was executed successfully on
         */
        public int getSuccessful() {
            return successful;
        }

        /**
         * Returns how many total clusters were during the execution of the search request
         */
        public int getSkipped() {
            return skipped;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            CustomSearchResponse.Clusters clusters = (CustomSearchResponse.Clusters) o;
            return total == clusters.total &&
                    successful == clusters.successful &&
                    skipped == clusters.skipped;
        }

        @Override
        public int hashCode() {
            return Objects.hash(total, successful, skipped);
        }

        @Override
        public String toString() {
            return "Clusters{total=" + total + ", successful=" + successful + ", skipped=" + skipped + '}';
        }
    }


}
