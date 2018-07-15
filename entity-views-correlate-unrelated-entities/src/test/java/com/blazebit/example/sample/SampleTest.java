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

import com.blazebit.example.view.NormalPostView2;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import java.util.List;
import com.blazebit.example.model.Post;
import com.blazebit.example.view.NormalPostView;
import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends AbstractSampleTest {

    @Test
    public void testPreviousPostOfP1() {
        transactional(em -> {
            CriteriaBuilder<Post> postCriteriaBuilder = cbf.create(em, Post.class);
            postCriteriaBuilder.from(Post.class, "post")
                    .where("poster.userName").eq("P1")
                    .orderByDesc("publishDate")
                    .setMaxResults(1);

            EntityViewSetting<NormalPostView, CriteriaBuilder<NormalPostView>> setting = EntityViewSetting.create(NormalPostView.class);
            CriteriaBuilder<NormalPostView> cb = evm.applySetting(setting, postCriteriaBuilder);
            List<NormalPostView> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("Post 5", list.get(0).getTitle());
            Assert.assertEquals("Post 3", list.get(0).getPreviousPost().getTitle());
        });
    }

    @Test
    public void testPreviousPostOfP3() {
        transactional(em -> {
            CriteriaBuilder<Post> postCriteriaBuilder = cbf.create(em, Post.class);
            postCriteriaBuilder.from(Post.class, "post")
                    .where("poster.userName").eq("P3")
                    .orderByDesc("publishDate")
                    .setMaxResults(1);

            EntityViewSetting<NormalPostView, CriteriaBuilder<NormalPostView>> setting = EntityViewSetting.create(NormalPostView.class);
            CriteriaBuilder<NormalPostView> cb = evm.applySetting(setting, postCriteriaBuilder);
            List<NormalPostView> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("Post 6", list.get(0).getTitle());
            Assert.assertNull(list.get(0).getPreviousPost());
        });
    }

    @Test
    public void testPostsOfP1() {
        transactional(em -> {
            CriteriaBuilder<Post> postCriteriaBuilder = cbf.create(em, Post.class);
            postCriteriaBuilder.from(Post.class, "post")
                    .where("title").eq("Post 1");

            EntityViewSetting<NormalPostView2, CriteriaBuilder<NormalPostView2>> setting = EntityViewSetting.create(NormalPostView2.class);
            CriteriaBuilder<NormalPostView2> cb = evm.applySetting(setting, postCriteriaBuilder);
            List<NormalPostView2> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("Post 1", list.get(0).getTitle());
            Assert.assertEquals(4, list.get(0).getPostersPosts().size());
        });
    }

    @Test
    public void testPostsOfP3() {
        transactional(em -> {
            CriteriaBuilder<Post> postCriteriaBuilder = cbf.create(em, Post.class);
            postCriteriaBuilder.from(Post.class, "post")
                    .where("title").eq("Post 6");

            EntityViewSetting<NormalPostView2, CriteriaBuilder<NormalPostView2>> setting = EntityViewSetting.create(NormalPostView2.class);
            CriteriaBuilder<NormalPostView2> cb = evm.applySetting(setting, postCriteriaBuilder);
            List<NormalPostView2> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("Post 6", list.get(0).getTitle());
            Assert.assertEquals(1, list.get(0).getPostersPosts().size());
        });
    }
}
