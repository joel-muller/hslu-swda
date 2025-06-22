/*
 * Copyright 2024 Roland Gisler, HSLU Informatik, Switzerland
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
package ch.hslu.swda.model;

import java.util.Properties;
import java.io.InputStream;
import io.micronaut.serde.annotation.Serdeable;
import java.io.IOException;

/**
 * Einfaches Ping für eine String-Message.
 */
@Serdeable
public final class Ping {

    private static final String LOCAL = "${env";
    
    private final Properties props;
    private final String message;
    private final String version;
    private String commitSha;
    private String author;
    private String buildId;
    private String branch;
    private String tag;

    /**
     * Konstruktor.
     *
     * @param message Message für Ping.
     * @throws java.io.IOException version.properties kann nicht gelesen werden.
     */
    public Ping(final String message) throws IOException {
        super();
        this.message = message;
        this.props = new Properties();
        try (InputStream is = Ping.class.getClassLoader().getResourceAsStream("version.properties")) {
            this.props.load(is);

            this.commitSha = this.props.getProperty("version.commit_sha", "unknown");
            if (this.commitSha.startsWith(LOCAL)) {
                this.commitSha = "(no commit-sha)";
            }
            this.version = this.props.getProperty("version.version", "unknown") + " " + this.commitSha;

            this.buildId = this.props.getProperty("version.job_id", "unknown");
            if (this.buildId.startsWith(LOCAL)) {
                this.buildId = "none (locdev)";
            }
            this.author = this.props.getProperty("version.commit_author", "unknown");
            if (this.author.startsWith(LOCAL)) {
                this.author = "unknown (locdev)";
            }
            this.author = this.props.getProperty("version.commit_author", "unknown");
            if (this.author.startsWith(LOCAL)) {
                this.author = "unknown (locdev)";
            }
            this.branch = this.props.getProperty("version.commit_branch", "unknown");
            if (this.branch.startsWith(LOCAL)) {
                this.branch = "unknown";
            }
            this.tag = this.props.getProperty("version.commit_tag", "unknown");
            if (this.tag.startsWith(LOCAL)) {
                this.tag = "no tag";
            }
        }
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return the version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * @return the job id.
     */
    public String getBuildId() {
        return this.buildId;
    }

    /**
     * @return the author.
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * @return the branch.
     */
    public String getBranch() {
        return this.branch;
    }
    
    /**
     * @return the tag.
     */
    public String getTag() {
        return this.tag;
    }

    @Override
    public String toString() {
        return String.format("Ping[message='%s', version='%s']", this.message, this.version);
    }
}
