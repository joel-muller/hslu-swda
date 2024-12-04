/*
 * Copyright 2024 Roland Christen, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.swda.micro;

/**
 * Holds all constants for message routes.
 */
public final class Routes {

    // Ingoing Mesages
    static final String RECEIVE_ORDER_VALIDITY = "order.receive-validity";
    static final String RECEIVE_ORDER = "order.receive";
    static final String CUSTOMER_RECEIVE_VALIDITY = "order.customer-validity";
    static final String ORDER_UPDATE = "order.update";

    // Outgoing Messages
    static final String CHECK_ORDER_VALIDITY = "articles.check-validity";
    static final String LOG = "logs.new";
    static final String REQUEST_ARTICLES = "store.request-articles";
    static final String CHECK_CUSTOMER = "customer.validate";
    static final String ORDER_READY = "store.order-ready";
    static final String ORDER_CANCELLED = "store.order-cancelled";

    /**
     * No instance allowed.
     */
    private Routes() {
    }
}
