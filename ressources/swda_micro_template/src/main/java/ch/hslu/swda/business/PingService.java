/*
 * Copyright 2024 Hochschule Luzern Informatik.
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
package ch.hslu.swda.business;

import ch.hslu.swda.model.Ping;
import io.micrometer.core.annotation.Timed;
import io.micronaut.tracing.annotation.NewSpan;
import jakarta.inject.Singleton;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapselt die Funktionalität des Ping-Service.
 */
@Singleton
public class PingService {

    private static final Logger LOG = LoggerFactory.getLogger(PingService.class);

    @NewSpan("Ping-Service")
    @Timed("ch.hslu.swda.service.ping")
    public Ping ping() throws IOException {
        final Ping ping = new Ping("PING - REST-Service unter /api/v1/students");
        LOG.info("REST: " + ping);
        return ping;
    }

}
