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
import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViews;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public abstract class AbstractSampleTest {

    protected EntityManagerFactory emf;
    protected CriteriaBuilderFactory cbf;
    protected EntityViewManager evm;

    @Before
    public void init() {
        emf = Persistence.createEntityManagerFactory("default");
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        cbf = config.createCriteriaBuilderFactory(emf);

        EntityViewConfiguration entityViewConfiguration = EntityViews.createDefaultConfiguration();

        for (Class<?> entityViewClazz : getEntityViewClasses()) {
            entityViewConfiguration.addEntityView(entityViewClazz);
        }

        evm = entityViewConfiguration.createEntityViewManager(cbf);
        
        transactional(em -> {
            User u1 = new User("P1");
            User u2 = new User("P2");
            User u3 = new User("P3");
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
        });
    }

    protected Class<?>[] getEntityViewClasses() {
        return new Class[] {
                UserRecord.class,
                UserData.class,
                UserInterface.class,
                UserAbstractClass.class
        };
    }
    
    protected void transactional(Consumer<EntityManager> consumer) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        boolean success = false;
        
        try {
            tx.begin();
            consumer.accept(em);
            success = true;
        } finally {
            try {
                if (success) {
                    tx.commit();
                } else {
                    tx.rollback();
                }
            } finally {
                em.close();
            }
        }
    }

    @After
    public void destruct() {
        emf.close();
    }
}
