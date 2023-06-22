/*
 * Copyright (c) 2019 ETH Zürich, Educational Development and Technology (LET)
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package ch.ethz.seb.sps.domain.model;

import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import ch.ethz.seb.sps.utils.Utils;

/** Data class that defines a Page that corresponds with the SEB Server API page JSON object
 *
 * @param <T> The type of a page entry entity */
public final class Page<T> {

    public static final String ATTR_NAMES_ONLY = "names_only";
    public static final String ATTR_NUMBER_OF_PAGES = "number_of_pages";
    public static final String ATTR_PAGE_NUMBER = "page_number";
    public static final String ATTR_PAGE_SIZE = "page_size";
    public static final String ATTR_SORT = "sort";
    public static final String ATTR_COMPLETE = "complete";
    public static final String ATTR_CONTENT = "content";

    @JsonProperty(ATTR_NUMBER_OF_PAGES)
    public final Integer numberOfPages;
    @JsonProperty(ATTR_PAGE_NUMBER)
    public final Integer pageNumber;
    @JsonProperty(ATTR_PAGE_SIZE)
    public final Integer pageSize;
    @JsonProperty(ATTR_SORT)
    public final String sort;
    @JsonProperty(ATTR_COMPLETE)
    public final boolean complete;

    @JsonProperty(ATTR_CONTENT)
    public final List<T> content;

    @JsonCreator
    public Page(
            @JsonProperty(ATTR_NUMBER_OF_PAGES) final Integer numberOfPages,
            @JsonProperty(ATTR_PAGE_NUMBER) final Integer pageNumber,
            @JsonProperty(ATTR_SORT) final String sort,
            @JsonProperty(ATTR_CONTENT) final Collection<T> content,
            @JsonProperty(ATTR_COMPLETE) final boolean complet) {

        this.numberOfPages = numberOfPages;
        this.pageNumber = pageNumber;
        this.content = Utils.immutableListOf(content);
        this.pageSize = content.size();
        this.sort = sort;
        this.complete = complet;
    }

    public Page(
            final Integer numberOfPages,
            final Integer pageNumber,
            final String sort,
            final Collection<T> content) {

        this.numberOfPages = numberOfPages;
        this.pageNumber = pageNumber;
        this.content = Utils.immutableListOf(content);
        this.pageSize = content.size();
        this.sort = sort;
        this.complete = true;
    }

    public int getNumberOfPages() {
        return (this.numberOfPages != null) ? this.numberOfPages : 1;
    }

    public int getPageNumber() {
        return (this.pageNumber != null) ? this.pageNumber : 0;
    }

    public int getPageSize() {
        return (this.pageSize != null) ? this.pageSize : -1;
    }

    public boolean isComplete() {
        return this.complete;
    }

    public Collection<T> getContent() {
        return this.content;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.content == null || this.content.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Page [numberOfPages=");
        builder.append(this.numberOfPages);
        builder.append(", pageNumber=");
        builder.append(this.pageNumber);
        builder.append(", pageSize=");
        builder.append(this.pageSize);
        builder.append(", sort=");
        builder.append(this.sort);
        builder.append(", content=");
        builder.append(this.content);
        builder.append("]");
        return builder.toString();
    }

}
