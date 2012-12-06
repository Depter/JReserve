package org.jreserve.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Peter Decsi
 */
public abstract class SessionTask {
    
    public static <T> T withOpenSession(Task<T> task) throws Exception {
        List list = Collections.singletonList(task);
        new OpenSessionTask(list).executeTasks();
        return task.getResult();
    }
    
    public static <T> T withOpenCurrentSession(Task<T> task) throws Exception {
        List list = Collections.singletonList(task);
        new OpenCurrentSessionTask(list).executeTasks();
        return task.getResult();
    }
    
    public static <T> T withCurrentSession(Task<T> task) throws Exception {
        List list = Collections.singletonList(task);
        new CurrentSessionTask(list).executeTasks();
        return task.getResult();
    }
    
    public static SessionTask openSession(List<Task> tasks) {
        return new OpenSessionTask(tasks);
    }
    
    public static SessionTask openSession(Task... tasks) {
        return new OpenSessionTask(Arrays.asList(tasks));
    }
    
    public static SessionTask openCurrentSession(List<Task> tasks) {
        return new OpenCurrentSessionTask(tasks);
    }
    
    public static SessionTask openCurrentSession(Task... tasks) {
        return new OpenCurrentSessionTask(Arrays.asList(tasks));
    }
    
    public static SessionTask currentSession(List<Task> tasks) {
        return new CurrentSessionTask(tasks);
    }
    
    public static SessionTask currentSession(Task... tasks) {
        return new CurrentSessionTask(Arrays.asList(tasks));
    }
    
    protected Session session;
    protected Transaction tx;
    protected List<Task> tasks = new ArrayList<Task>();
    
    protected SessionTask(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }
    
    public void executeTasks() throws Exception {
        try {
            beginTransaction();
            doWorks();
            comit();
        } catch (Exception ex) {
            rollback();
            throw ex;
        } finally {
            close();
        }
    }
    
    protected abstract void beginTransaction();

    protected void doWorks() throws Exception {
        for(Task task : tasks)
            task.doWork(session);
    }
    
    protected void comit() {
        if (tx != null) {
            tx.commit();
        }
        tx = null;
    }

    protected void rollback() {
        if (tx != null) {
            tx.rollback();
        }
        tx = null;
    }
    
    protected abstract void close();
    
    public static interface Task<T> {
        public void doWork(Session session) throws Exception;
        
        public T getResult();
    }
    
    public static abstract class AbstractTask<T> implements Task<T> {
        protected T result;
        
        @Override
        public T getResult() {
            return result;
        }
    }
    
    private static class OpenSessionTask extends SessionTask {
    
        private OpenSessionTask(List<Task> tasks) {
            super(tasks);
        }

        @Override
        protected void beginTransaction() {
            session = SessionFactory.openSession();
            tx = session.beginTransaction();
        }

        @Override
        protected void close() {
            if(session != null)
                session.close();
            session = null;
        }
    }
    
    private static class OpenCurrentSessionTask extends SessionTask {
    
        private OpenCurrentSessionTask(List<Task> tasks) {
            super(tasks);
        }

        @Override
        protected void beginTransaction() {
            session = SessionFactory.getCurrentSession();
            tx = session.beginTransaction();
        }

        @Override
        protected void close() {
            session = null;
        }
    }
    
    private static class CurrentSessionTask extends SessionTask {
    
        private CurrentSessionTask(List<Task> tasks) {
            super(tasks);
        }

        @Override
        protected void beginTransaction() {
            session = SessionFactory.getCurrentSession();
        }

        @Override
        protected void comit() {}

        @Override
        protected void rollback() {}

        @Override
        protected void close() {
            session = null;
        }
    }
}
