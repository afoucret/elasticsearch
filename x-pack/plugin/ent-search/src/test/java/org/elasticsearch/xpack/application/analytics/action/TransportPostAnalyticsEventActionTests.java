/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License
 * 2.0; you may not use this file except in compliance with the Elastic License
 * 2.0.
 */

package org.elasticsearch.xpack.application.analytics.action;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.license.XPackLicenseState;
import org.elasticsearch.tasks.Task;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.transport.TransportService;
import org.elasticsearch.xpack.application.analytics.AnalyticsEventIngestService;

import static org.elasticsearch.xpack.application.analytics.action.AnalyticsTransportActionTestUtils.mockLicenseState;
import static org.elasticsearch.xpack.application.analytics.action.AnalyticsTransportActionTestUtils.verifyExceptionIsThrownOnInvalidLicence;
import static org.elasticsearch.xpack.application.analytics.action.AnalyticsTransportActionTestUtils.verifyNoExceptionIsThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransportPostAnalyticsEventActionTests extends ESTestCase {

    public void testWithSupportedLicense() {
        AnalyticsEventIngestService eventIngestService = mock(AnalyticsEventIngestService.class);

        TransportPostAnalyticsEventAction transportAction = createTransportAction(mockLicenseState(true), eventIngestService);
        PostAnalyticsEventAction.Request request = mock(PostAnalyticsEventAction.Request.class);

        @SuppressWarnings("unchecked")
        ActionListener<PostAnalyticsEventAction.Response> listener = mock(ActionListener.class);

        transportAction.doExecute(mock(Task.class), request, listener);
        verify(eventIngestService, times(1)).postEvent(request, listener);
        verifyNoExceptionIsThrown(listener);
    }

    public void testWithUnsupportedLicense() {
        AnalyticsEventIngestService eventIngestService = mock(AnalyticsEventIngestService.class);

        TransportPostAnalyticsEventAction transportAction = createTransportAction(mockLicenseState(false), eventIngestService);
        PostAnalyticsEventAction.Request request = mock(PostAnalyticsEventAction.Request.class);

        @SuppressWarnings("unchecked")
        ActionListener<PostAnalyticsEventAction.Response> listener = mock(ActionListener.class);

        transportAction.doExecute(mock(Task.class), request, listener);

        verify(eventIngestService, never()).postEvent(request, listener);
        verifyExceptionIsThrownOnInvalidLicence(listener);
    }

    private TransportPostAnalyticsEventAction createTransportAction(
        XPackLicenseState xPackLicenseState,
        AnalyticsEventIngestService eventIngestService
    ) {
        return new TransportPostAnalyticsEventAction(
            mock(TransportService.class),
            mock(ActionFilters.class),
            eventIngestService,
            xPackLicenseState
        );
    }
}
