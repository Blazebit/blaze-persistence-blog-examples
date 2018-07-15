/*
 * Copyright 2014 - 2018 Blazebit.
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

package com.blazebit.example.view;

import com.blazebit.persistence.CorrelationQueryBuilder;
import com.blazebit.persistence.JoinOnBuilder;
import com.blazebit.persistence.view.*;
import com.blazebit.example.model.*;

@EntityView(Post.class)
public interface NormalPostView extends SimplePostView {

    @Mapping("text")
    String getText();
    @MappingCorrelated(
            correlator = PreviousPostCorrelationProvider.class,
            correlationBasis = "id",
            fetch = FetchStrategy.JOIN
    )
    SimplePostView getPreviousPost();

    class PreviousPostCorrelationProvider implements CorrelationProvider {

        @Override
        public void applyCorrelation(CorrelationBuilder correlationBuilder, String correlationExpression) {
            String correlationAlias = correlationBuilder.getCorrelationAlias();
            JoinOnBuilder<CorrelationQueryBuilder> onBuilder = correlationBuilder.correlate(Post.class);
            onBuilder.on(correlationAlias + ".id")
                .in()
                    .from(Post.class, "subPost")
                    .select("subPost.id")
                    .where("subPost.id").notEqExpression(correlationExpression)
                    .where("subPost.poster").eqExpression("VIEW_ROOT(poster)")
                    .where("subPost.publishDate").leExpression("VIEW_ROOT(publishDate)")
                    .orderByDesc("subPost.publishDate")
                    .setMaxResults(1)
                .end()
            .end();
        }
    }

}
