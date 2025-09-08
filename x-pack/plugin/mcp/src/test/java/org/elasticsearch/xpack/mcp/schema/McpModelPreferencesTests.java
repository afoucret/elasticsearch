/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */
package org.elasticsearch.xpack.mcp.schema;

import org.elasticsearch.common.io.stream.Writeable;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.xcontent.XContentParser;

import java.io.IOException;

public class McpModelPreferencesTests extends McpSerializationTestCase<McpModelPreferences> {

    @Override
    protected Writeable.Reader<McpModelPreferences> instanceReader() {
        return McpModelPreferences::new;
    }

    @Override
    protected McpModelPreferences createTestInstance() {
        return randomModelPreferences();
    }

    @Override
    protected McpModelPreferences doParseInstance(XContentParser parser) throws IOException {
        return McpModelPreferences.PARSER.parse(parser, null);
    }

    @Override
    protected McpModelPreferences mutateInstance(McpModelPreferences instance) {
        switch (randomInt(3)) {
            case 0:
                return new McpModelPreferences(
                    randomValueOtherThan(
                        instance.hints(),
                        () -> mayBeNull(() -> randomList(5, () -> new McpModelHint(randomAlphaOfLength(10))))
                    ),
                    instance.costPriority(),
                    instance.speedPriority(),
                    instance.intelligencePriority()
                );
            case 1:
                return new McpModelPreferences(
                    instance.hints(),
                    randomValueOtherThan(instance.costPriority(), ESTestCase::randomOptionalDouble),
                    instance.speedPriority(),
                    instance.intelligencePriority()
                );
            case 2:
                return new McpModelPreferences(
                    instance.hints(),
                    instance.costPriority(),
                    randomValueOtherThan(instance.speedPriority(), ESTestCase::randomOptionalDouble),
                    instance.intelligencePriority()
                );
            case 3:
                return new McpModelPreferences(
                    instance.hints(),
                    instance.costPriority(),
                    instance.speedPriority(),
                    randomValueOtherThan(instance.intelligencePriority(), ESTestCase::randomOptionalDouble)
                );
            default:
                throw new IllegalStateException("Unexpected random value");
        }
    }
}
