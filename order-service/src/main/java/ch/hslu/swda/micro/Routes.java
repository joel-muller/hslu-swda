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
    static final String ORDER_RECEIVE_VALIDITY = "order.receive-validity";
    static final String ORDER_RECEIVE = "order.receive";
    static final String ORDER_CUSTOMER_VALIDITY = "order.customer-validity";
    static final String ORDER_UPDATE = "order.update";
    static final String ORDER_CANCEL = "order.cancel";
    static final String ORDER_CONFIRMATION_GET = "order.confirmation.get";

    // Outgoing Messages
    static final String ARTICLES_CHECK_VALIDITY = "articles.check-validity";
    static final String LOG = "logs.new";
    static final String STORE_REQUEST_ARTICLES = "store.request-articles";
    static final String CUSTOMER_VALIDATE = "customer.validate";
    static final String STORE_ORDER_READY = "store.order-ready";
    static final String STORE_ORDER_CANCELLED = "store.order-cancelled";
    static final String INVOICE_CREATE = "invoice.create";



    /**
     * No instance allowed.
     */
    private Routes() {
    }
}
