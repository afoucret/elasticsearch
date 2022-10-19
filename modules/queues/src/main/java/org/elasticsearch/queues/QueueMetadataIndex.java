/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0 and the Server Side Public License, v 1; you may not use this file except
 * in compliance with, at your election, the Elastic License 2.0 or the Server
 * Side Public License, v 1.
 */

package org.elasticsearch.queues;

import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.SystemIndexDescriptor;
import org.elasticsearch.xcontent.XContentBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

import static org.elasticsearch.index.mapper.MapperService.SINGLE_MAPPING_NAME;
import static org.elasticsearch.xcontent.XContentFactory.jsonBuilder;

public class QueueMetadataIndex {

    public static final String QUEUE_ORIGIN = "queue";
    public static SystemIndexDescriptor getQueueSystemIndexDescriptor() {
        return SystemIndexDescriptor.builder()
            .setIndexPattern(indexName() + "*")
            .setPrimaryIndex(indexName())
            .setDescription("Queue metadata storage")
            .setMappings(indexMapping())
            .setSettings(indexSettings())
            .setVersionMetaKey("version")
            .setOrigin(QUEUE_ORIGIN)
            .build();
    }

    public static String indexName() {
        return ".queues-metadata";
    }

    public static XContentBuilder indexMapping() {
        final XContentBuilder builder;
        try {
            builder = jsonBuilder();
            {
                builder.startObject().startObject(SINGLE_MAPPING_NAME);
                {
                    builder.startObject("_meta");
                    {
                        builder.field("version", Version.CURRENT);
                    }
                    builder.endObject();

                    builder.startObject("properties");
                    {
                        builder.startObject("name");
                        builder.field("type", "keyword");
                        builder.endObject();
                    }
                    {
                        builder.startObject("size");
                        builder.field("type", "integer");
                        builder.endObject();
                    }
                    builder.endObject();
                }
                builder.endObject().endObject();
            }

            return builder;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to build " + indexName() + " index mappings", e);
        }
    }

    private static Settings indexSettings() {
        return Settings.builder()
            .put(IndexMetadata.SETTING_NUMBER_OF_SHARDS, 1)
            .put(IndexMetadata.SETTING_AUTO_EXPAND_REPLICAS, "0-1")
            .build();
    }
}
