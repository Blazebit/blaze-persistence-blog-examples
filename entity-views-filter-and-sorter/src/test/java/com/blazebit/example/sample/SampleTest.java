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

package com.blazebit.example.sample;

import com.blazebit.example.model.Post;
import com.blazebit.example.view.NormalPostView;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import com.blazebit.persistence.view.Sorters;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SampleTest extends AbstractSampleTest {

    @Test
    public void testP1Posts() {
        transactional(em -> {
            // Base query
            CriteriaBuilder<Post> postCriteriaBuilder = cbf.create(em, Post.class);
            postCriteriaBuilder.from(Post.class, "post");

            // Entity view setting with filter and sorter
            EntityViewSetting<NormalPostView, CriteriaBuilder<NormalPostView>> setting = EntityViewSetting.create(NormalPostView.class);
            setting.addAttributeFilter("poster.name", "joe");
            setting.addAttributeSorter("publishDate", Sorters.descending());

            CriteriaBuilder<NormalPostView> cb = evm.applySetting(setting, postCriteriaBuilder);
            List<NormalPostView> list = cb.getResultList();

            Assert.assertEquals(4, list.size());
            Assert.assertEquals("Post 5", list.get(0).getTitle());
            Assert.assertEquals("Joe Fox (P1)", list.get(0).getPoster().getName());
        });
    }
}
