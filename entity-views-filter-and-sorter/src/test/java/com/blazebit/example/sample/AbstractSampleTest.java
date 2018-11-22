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

import com.blazebit.example.model.Comment;
import com.blazebit.example.model.Post;
import com.blazebit.example.model.User;
import com.blazebit.example.view.NormalPostView;
import com.blazebit.example.view.SimpleUserView;
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
import java.sql.Date;
import java.util.function.Consumer;

public abstract class AbstractSampleTest {

    protected static final String TEXT = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea tak";
    
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
            User u1 = new User("Joe", "Fox", "P1");
            User u2 = new User("Jimmy", "Johnson", "P2");
            User u3 = new User("John", "Doe", "P3");
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            
            Post p1 = new Post(Date.valueOf("2017-01-01"), "Post 1", TEXT, u1);
            Post p2 = new Post(Date.valueOf("2017-02-01"), "Post 2", TEXT, u1);
            Post p3 = new Post(Date.valueOf("2017-03-01"), "Post 3", TEXT, u1);
            Post p4 = new Post(Date.valueOf("2017-04-01"), "Post 4", TEXT, u2);
            Post p5 = new Post(Date.valueOf("2017-05-01"), "Post 5", TEXT, u1);
            Post p6 = new Post(Date.valueOf("2017-06-01"), "Post 6", TEXT, u3);
            
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            em.persist(p6);
            
            p1.getComments().add(new Comment("Nice!", u3));
            p2.getComments().add(new Comment("Good work!", u3));
            p2.getComments().add(new Comment("Thanks", u1));
            p2.getComments().add(new Comment("You are welcome", u3));
            p4.getComments().add(new Comment("Wohoo!", u3));
        });
    }

    protected Class<?>[] getEntityViewClasses() {
        return new Class[] {
                NormalPostView.class,
                SimpleUserView.class
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
