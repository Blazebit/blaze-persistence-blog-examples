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

import com.blazebit.example.model.User;
import com.blazebit.example.view.UserAbstractClass;
import com.blazebit.example.view.UserData;
import com.blazebit.example.view.UserInterface;
import com.blazebit.example.view.UserRecord;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.view.EntityViewSetting;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SampleTest extends AbstractSampleTest {

    @Test
    public void testRecord() {
        transactional(em -> {
            // Base query
            CriteriaBuilder<User> postCriteriaBuilder = cbf.create(em, User.class);
            postCriteriaBuilder.from(User.class, "u")
                    .orderByAsc("userName")
                    .setMaxResults(1);

            CriteriaBuilder<UserRecord> cb = evm.applySetting(EntityViewSetting.create(UserRecord.class), postCriteriaBuilder);
            List<UserRecord> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("P1", list.get(0).userName());
        });
    }

    @Test
    public void testData() {
        transactional(em -> {
            // Base query
            CriteriaBuilder<User> postCriteriaBuilder = cbf.create(em, User.class);
            postCriteriaBuilder.from(User.class, "u")
                .orderByAsc("userName")
                .setMaxResults(1);

            CriteriaBuilder<UserData> cb = evm.applySetting(EntityViewSetting.create(UserData.class), postCriteriaBuilder);
            List<UserData> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("P1", list.get(0).getUserName());
        });
    }

    @Test
    public void testInterface() {
        transactional(em -> {
            // Base query
            CriteriaBuilder<User> postCriteriaBuilder = cbf.create(em, User.class);
            postCriteriaBuilder.from(User.class, "u")
                .orderByAsc("userName")
                .setMaxResults(1);

            CriteriaBuilder<UserInterface> cb = evm.applySetting(EntityViewSetting.create(UserInterface.class), postCriteriaBuilder);
            List<UserInterface> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("P1", list.get(0).getUserName());
        });
    }

    @Test
    public void testAbstractClass() {
        transactional(em -> {
            // Base query
            CriteriaBuilder<User> postCriteriaBuilder = cbf.create(em, User.class);
            postCriteriaBuilder.from(User.class, "u")
                .orderByAsc("userName")
                .setMaxResults(1);

            CriteriaBuilder<UserAbstractClass> cb = evm.applySetting(EntityViewSetting.create(UserAbstractClass.class), postCriteriaBuilder);
            List<UserAbstractClass> list = cb.getResultList();

            Assert.assertEquals(1, list.size());
            Assert.assertEquals("P1", list.get(0).getUserName());
        });
    }
}
