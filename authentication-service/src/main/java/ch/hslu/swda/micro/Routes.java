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

    static final String USER_CREATE = "user.create";
    static final String USER_READ = "user.read";
    static final String USER_UPDATE = "user.update";
    static final String USER_DELETE = "user.delete";
    static final String USER_LOGIN = "user.login";

    /**
     * No instance allowed.
     */
    private Routes() {
    }
}
