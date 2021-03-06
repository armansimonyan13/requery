/*
 * Copyright 2016 requery.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.requery.converter;

import io.requery.Converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Converts from a {@link LocalDate} to a {@link java.sql.Date} for Java 8.
 */
public class LocalDateConverter implements Converter<LocalDate, java.sql.Date> {

    @Override
    public Class<LocalDate> mappedType() {
        return LocalDate.class;
    }

    @Override
    public Class<java.sql.Date> persistedType() {
        return java.sql.Date.class;
    }

    @Override
    public Integer persistedSize() {
        return null;
    }

    @Override
    public java.sql.Date convertToPersisted(LocalDate value) {
        if (value == null) {
            return null;
        }
        Instant instant = value.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return new java.sql.Date(instant.toEpochMilli());
    }

    @Override
    public LocalDate convertToMapped(Class<? extends LocalDate> type, java.sql.Date value) {
        if (value == null) {
            return null;
        }
        return value.toLocalDate();
    }
}
