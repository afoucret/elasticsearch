/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.relevancesearch.settings;

import org.elasticsearch.xpack.relevancesearch.settings.relevance.RelevanceSettings;

import java.util.Objects;

public class RelevanceSettingsContext {

    private final String contextName;
    private final String value;

    public RelevanceSettingsContext(String contextName, String value) {
        this.contextName = contextName;
        this.value = value;
    }

    public String getContextName() {
        return contextName;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelevanceSettingsContext that = (RelevanceSettingsContext) o;
        return Objects.equals(contextName, that.contextName) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contextName, value);
    }
}
