/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.contentindices.action;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.ActionType;
import org.elasticsearch.action.IndicesRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.MasterNodeReadRequest;
import org.elasticsearch.cluster.metadata.ContentIndex;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.xcontent.ParseField;
import org.elasticsearch.xcontent.ToXContent;
import org.elasticsearch.xcontent.ToXContentObject;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GetContentIndicesAction extends ActionType<GetContentIndicesAction.Response> {

    public static final GetContentIndicesAction INSTANCE = new GetContentIndicesAction();
    public static final String NAME = "indices:admin/content_index/get";

    private GetContentIndicesAction() {
        super(NAME, Response::new);
    }

    public static class Request extends MasterNodeReadRequest<GetContentIndicesAction.Request> implements IndicesRequest.Replaceable {

        private String[] names;
        private IndicesOptions indicesOptions = IndicesOptions.fromOptions(false, true, true, true, false, false, true, false);

        public Request(String[] names) {
            this.names = names;
        }

        public Request(StreamInput in) throws IOException {
            super(in);
            this.names = in.readOptionalStringArray();
            this.indicesOptions = IndicesOptions.readIndicesOptions(in);
        }

        @Override
        public ActionRequestValidationException validate() {
            return null;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.writeOptionalStringArray(names);
            indicesOptions.writeIndicesOptions(out);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GetContentIndicesAction.Request request = (GetContentIndicesAction.Request) o;
            return Arrays.equals(names, request.names) && indicesOptions.equals(request.indicesOptions);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(indicesOptions);
            result = 31 * result + Arrays.hashCode(names);
            return result;
        }

        @Override
        public String[] indices() {
            return names;
        }

        public String[] getNames() {
            return names;
        }

        @Override
        public IndicesOptions indicesOptions() {
            return indicesOptions;
        }

        public Request indicesOptions(IndicesOptions indicesOptions) {
            this.indicesOptions = indicesOptions;
            return this;
        }

        @Override
        public boolean includeDataStreams() {
            return false;
        }

        @Override
        public IndicesRequest indices(String... indices) {
            this.names = indices;
            return this;
        }
    }

    public static class Response extends ActionResponse implements ToXContentObject {

        public static final ParseField NAME_FIELD = new ParseField("name");
        public static final ParseField INDEX_FIELD = new ParseField("index");

        private final List<ContentIndex> contentIndices;

        public Response(List<ContentIndex> contentIndices) {
            this.contentIndices = contentIndices;
        }

        public Response(StreamInput in) throws IOException {
            this(in.readList(ContentIndex::new));
        }


        public List<ContentIndex> getContentIndices() {
            return contentIndices;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            out.writeList(contentIndices);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GetContentIndicesAction.Response response = (GetContentIndicesAction.Response) o;
            return contentIndices.equals(response.contentIndices);
        }

        @Override
        public int hashCode() {
            return Objects.hash(contentIndices);
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, ToXContent.Params params) throws IOException {
            builder.startObject();

            for (ContentIndex contentIndex: contentIndices) {
                builder.field(contentIndex.getName());
                builder.startObject();

                builder.field(NAME_FIELD.getPreferredName(), contentIndex.getName());

                builder.field(INDEX_FIELD.getPreferredName());

                contentIndex.getIndex().toXContent(builder, params);
                builder.endObject();
            }
            builder.endObject();
            return builder;
        }
    }
}
