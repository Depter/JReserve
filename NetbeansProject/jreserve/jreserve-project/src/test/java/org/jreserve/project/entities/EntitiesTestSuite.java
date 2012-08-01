package org.jreserve.project.entities;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Peter Decsi
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    ClaimTypeTest.class,
    DataTypeTest.class,
    ClaimDataTest.class,
    LoBTest.class,
    ProjectTest.class,
    ChangeLogTest.class,
    PersistenceTest.class
})
public class EntitiesTestSuite {
}