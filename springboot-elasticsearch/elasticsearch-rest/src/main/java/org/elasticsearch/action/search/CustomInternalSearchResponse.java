package org.elasticsearch.action.search;

import com.daishuai.elasticsearch.client.rest.cutomer.CustomSearchHits;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.common.xcontent.ToXContentFragment;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.InternalAggregations;
import org.elasticsearch.search.internal.InternalSearchResponse;
import org.elasticsearch.search.profile.SearchProfileShardResults;
import org.elasticsearch.search.suggest.Suggest;

import java.io.IOException;

/**
 * @author Daishuai
 * @date 2021/1/13 18:48
 */
public class CustomInternalSearchResponse extends CustomSearchResponseSections implements Writeable, ToXContentFragment {

    public static InternalSearchResponse empty() {
        return new InternalSearchResponse(SearchHits.empty(), null, null, null, false, null, 1);
    }

    public CustomInternalSearchResponse(CustomSearchHits hits, InternalAggregations aggregations, Suggest suggest,
                                        SearchProfileShardResults profileResults, boolean timedOut, Boolean terminatedEarly,
                                        int numReducePhases) {
        super(hits, aggregations, suggest, timedOut, terminatedEarly, profileResults, numReducePhases);
    }

    public CustomInternalSearchResponse(StreamInput in) throws IOException {
        super(
                CustomSearchHits.readSearchHits(in),
                in.readBoolean() ? InternalAggregations.readAggregations(in) : null,
                in.readBoolean() ? Suggest.readSuggest(in) : null,
                in.readBoolean(),
                in.readOptionalBoolean(),
                in.readOptionalWriteable(SearchProfileShardResults::new),
                in.readVInt()
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        hits.writeTo(out);
        out.writeOptionalStreamable((InternalAggregations)aggregations);
        out.writeOptionalStreamable(suggest);
        out.writeBoolean(timedOut);
        out.writeOptionalBoolean(terminatedEarly);
        out.writeOptionalWriteable(profileResults);
        out.writeVInt(numReducePhases);
    }
}
