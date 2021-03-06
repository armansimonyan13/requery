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

package io.requery.rx;

import io.requery.TransactionIsolation;
import io.requery.TransactionListener;
import io.requery.meta.Type;
import io.requery.proxy.EntityProxy;
import io.requery.util.function.Supplier;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * {@link TransactionListener} for listening to entity commits and emitting the types that have
 * changed through an Rx {@link rx.subjects.Subject}.
 *
 * @author Nikhil Purushe
 */
final class TypeChangeListener implements Supplier<TransactionListener> {

    private final SerializedSubject<Set<Type<?>>, Set<Type<?>>> commitSubject;
    private final SerializedSubject<Set<Type<?>>, Set<Type<?>>> rollbackSubject;

    TypeChangeListener() {
        commitSubject = new SerializedSubject<>(PublishSubject.<Set<Type<?>>>create());
        rollbackSubject = new SerializedSubject<>(PublishSubject.<Set<Type<?>>>create());
    }

    @Override
    public TransactionListener get() {
        return new TransactionListener() {
            @Override
            public void beforeBegin(TransactionIsolation isolation) {
            }

            @Override
            public void afterBegin(TransactionIsolation isolation) {
            }

            @Override
            public void beforeCommit(Set<EntityProxy<?>> entities) {

            }

            @Override
            public void afterCommit(Set<EntityProxy<?>> entities) {
                emitTypes(commitSubject, entities);
            }

            @Override
            public void beforeRollback(Set<EntityProxy<?>> entities) {

            }

            @Override
            public void afterRollback(Set<EntityProxy<?>> entities) {
                emitTypes(rollbackSubject, entities);
            }
        };
    }

    Subject<Set<Type<?>>, Set<Type<?>>> commitSubject() {
        return commitSubject;
    }

    private void emitTypes(Subject<Set<Type<?>>, Set<Type<?>>> subject,
                           Set<EntityProxy<?>> entities) {
        Set<Type<?>> types = new LinkedHashSet<>();
        for (EntityProxy proxy : entities) {
            types.add(proxy.type());
        }
        subject.onNext(types);
    }
}
