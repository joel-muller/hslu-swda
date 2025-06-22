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

    // Ingoing Mesasges
    static final String STORE_REQUEST_ARTICLES = "store.request-articles";
    static final String STORE_INVENTORY_UPDATE = "store.inventory-update";
    static final String STORE_ORDER_READY = "store.order-ready";
    static final String STORE_CREATION = "store.create-create";
    static final String STORES_GET = "stores.get";
    static final String STORE_INTERNAL_ORDER = "store.internal-order";
    static final String STORE_ORDER_CANCELLED = "store.order-cancelled";

 
    // Outgoing Messages
    static final String ORDER_UPDATE = "order.update";
    static final String WAREHOUSE_REQUEST = "central-warehouse.request";
    static final String LOG = "logs.new";


    /**
     * No instance allowed.
     */
    private Routes() {
    }
}
