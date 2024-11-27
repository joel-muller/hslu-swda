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

    
    static final String STORE_STATUS = "store.status";
    static final String STORE_CREATION = "store.create";
    static final String INVENTORY_CHECK = "inventory.check";
    static final String LOG = "logs.new";
    static final String ORDER_FROM_CENTRAL_WAREHOUSE = "warehouse.order";

    // Ingoing Mesasges
    static final String REQUEST_ARTICLES = "store.request-articles";
    static final String INVENTORY_UPDATE = "store.inventory-update";
    static final String ORDER_READY = "store.order-ready";

    // Outgoing Messages
    static final String ORDER_UPDATE = "order.update";
    static final String INVENTORY_REQUEST = "central-warehouse.request";


    /**
     * No instance allowed.
     */
    private Routes() {
    }
}
