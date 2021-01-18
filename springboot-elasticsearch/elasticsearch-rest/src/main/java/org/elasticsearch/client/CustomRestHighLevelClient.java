package org.elasticsearch.client;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.search.CustomSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.CheckedConsumer;
import org.elasticsearch.common.CheckedFunction;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;

/**
 * @author Daishuai
 * @date 2021/1/13 17:15
 */
public class CustomRestHighLevelClient extends RestHighLevelClient {
    public CustomRestHighLevelClient(RestClientBuilder restClientBuilder) {
        super(restClientBuilder);
    }

    protected CustomRestHighLevelClient(RestClientBuilder restClientBuilder, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClientBuilder, namedXContentEntries);
    }

    protected CustomRestHighLevelClient(RestClient restClient, CheckedConsumer<RestClient, IOException> doClose, List<NamedXContentRegistry.Entry> namedXContentEntries) {
        super(restClient, doClose, namedXContentEntries);
    }

    public CustomSearchResponse search(SearchRequest searchRequest, RequestOptions options, String def) throws IOException {
        return performRequestAndParseEntityCustom(
                searchRequest,
                r -> CustomRequestConverters.search(r, "_search"),
                options,
                CustomSearchResponse::fromXContent,
                emptySet());
    }

    /**
     * @deprecated If creating a new HLRC ReST API call, consider creating new actions instead of reusing server actions. The Validation
     * layer has been added to the ReST client, and requests should extend {@link Validatable} instead of {@link ActionRequest}.
     */
    @Deprecated
    protected final <Req extends ActionRequest, Resp> Resp performRequestAndParseEntityCustom(Req request,
                                                                                        CheckedFunction<Req, Request, IOException> requestConverter,
                                                                                        RequestOptions options,
                                                                                        CheckedFunction<XContentParser, Resp, IOException> entityParser,
                                                                                        Set<Integer> ignores) throws IOException {
        return performRequest(request, requestConverter, options,
                response -> parseEntity(response.getEntity(), entityParser), ignores);
    }

}
