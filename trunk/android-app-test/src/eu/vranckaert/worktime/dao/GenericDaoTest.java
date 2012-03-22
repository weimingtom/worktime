/*
 *  Copyright 2012 Dirk Vranckaert
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.vranckaert.worktime.dao;

import eu.vranckaert.worktime.dao.generic.GenericDao;
import eu.vranckaert.worktime.dao.impl.CommentHistoryDaoImpl;
import eu.vranckaert.worktime.model.CommentHistory;
import eu.vranckaert.worktime.test.cases.DaoTestCase;

import java.util.Date;
import java.util.List;

/**
 * User: DIRK VRANCKAERT
 * Date: 22/03/12
 * Time: 21:07
 */
public class GenericDaoTest extends DaoTestCase<CommentHistoryDaoImpl> {
    public GenericDaoTest() {
        super(CommentHistoryDaoImpl.class);
    }

    @Deprecated
    public CommentHistoryDaoImpl getDao() {
        return super.getDao();
    }

    public GenericDao<CommentHistory, Integer> getGenericDao() {
        return (GenericDao) super.getDao();
    }
    
    public void testSaveAndFindAll() {
        List<CommentHistory> objects = null;

        objects = getGenericDao().findAll();
        assertNotNull("The list should not be null", objects);
        assertEquals("The list should contain O elements", 0, objects.size());

        CommentHistory o1 = new CommentHistory("TEST");
        o1.setEntranceDate(new Date());
        o1 = getGenericDao().save(o1);
        assertNotNull(o1);
        assertNotNull(o1.getId());
        assertTrue(o1.getId() > -1);

        objects = getGenericDao().findAll();
        assertNotNull("The list should not be null", objects);
        assertEquals("The list should contain 1 element", 1, objects.size());
        assertNotNull(objects.get(0));
        assertNotNull(objects.get(0).getId());
        assertEquals(o1.getId(), objects.get(0).getId());

        CommentHistory o2 = new CommentHistory("TEST");
        o2.setEntranceDate(new Date());
        o2 = getGenericDao().save(o2);
        assertNotNull(o2);
        assertNotNull(o2.getId());
        assertTrue(o2.getId() > -1);

        objects = getGenericDao().findAll();
        assertNotNull("The list should not be null", objects);
        assertEquals("The list should contain 2 elements", 2, objects.size());
    }
    
    public void testFindByIdForExistingId() {
        CommentHistory o = new CommentHistory("TEST");
        o = getGenericDao().save(o);
        
        CommentHistory result = getGenericDao().findById(o.getId());
        assertNotNull("The result should not be null", result);
        assertEquals(o.getId(), result.getId());
    }
    
    public void testFindByIdForNonExistingId() {
        CommentHistory result = getGenericDao().findById(-9000);
        assertNull(result);
    }
    
    public void testContainsIdForExistingAndNonExistingIds() {
        CommentHistory o1 = new CommentHistory("TEST");
        o1 = getGenericDao().save(o1);

        CommentHistory o2 = new CommentHistory("TEST");
        o2 = getGenericDao().save(o2);

        CommentHistory o3 = new CommentHistory("TEST");
        o3 = getGenericDao().save(o3);
        
        assertTrue(getGenericDao().contains(o1.getId()));
        assertTrue(getGenericDao().contains(o2.getId()));
        assertFalse(getGenericDao().contains(-9000));
        assertFalse(getGenericDao().contains(4322));
        assertTrue(getGenericDao().contains(o3.getId()));
    }
}
