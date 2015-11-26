/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ajh1779.threading;

import com.ajh1779.debug.AuroraException;
import com.ajh1779.debug.AuroraLogs;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for thread looping.
 * 
 * Tested 29/08/2014. This method works and does not require replacing. If the
 * functionality is not desired, alter it.
 * @author AJH1779
 * @version InDev 0.1.0
 */
public abstract class Synchro implements Runnable {
    private static final Logger LOG = AuroraLogs.getLogger(Synchro.class);
    
    /**
     * Creates a new independent thread.
     */
    public Synchro(String name) {
        this.name = name;
        dependent = null;
        LOG.info("Created new independent Synchro");
    }
    /**
     * Returns a new thread that is running so long as the provided thread is
     * running.
     * @param dependent
     */
    public Synchro(String name, Synchro dependent) {
        this.name = name;
        this.dependent = dependent;
        LOG.log(Level.INFO, "Created new dependent Synchro({0})", dependent);
    }
    private final String name;
    private final Synchro dependent;
    private volatile boolean halted = false, running = false,
            looping = false, threading = false;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    
    /**
     * Begins the thread with the default priority. The thread created for this
     * task is returned.
     * @param thread_name The thread name.
     * @return The Created Thread
     */
    public final Thread start() {
        halted = false;
        Thread thread = new Thread(this, this.name);
        thread.start();
        LOG.log(Level.INFO, "Created New Thread: {0}", this.name);
        return thread;
    }
    /**
     * Begins the thread with the specified priority. The thread created for
     * this task is returned.
     * @param thread_name The thread name.
     * @param priority The specified priority.
     * @return The Created Thread
     */
    public final Thread start(int priority) {
        halted = false;
        Thread thread = new Thread(this, this.name);
        thread.setPriority(priority);
        thread.start();
        LOG.log(Level.INFO, "Created New Thread with Priority: {0} priority {1}",
                new Object[]{this.name, priority});
        return thread;
    }
    
    /**
     * Creates a new thread with the specified name and priority, waiting this
     * thread until the created thread is confirmed as created.
     * @param synchro
     * @param priority 
     */
    public final void waitForStart(Synchro synchro, int priority) {
        LOG.log(Level.INFO, "Synchro \"{0}\" is waiting for \"{1}\" to start.",
                new Object[]{this.name, synchro.name});
        lock.lock();
        try {
            if(synchro.dependent == this && !synchro.getThreading()) {
                synchro.start(priority);
                condition.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Synchro.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
        LOG.log(Level.INFO, "Synchro \"{0}\" is resuming after \"{1}\" started.",
                new Object[]{this.name, synchro.name});
    }
    /**
     * Holds this thread until the specified thread has stopped running.
     * @param synchro 
     */
    public final void waitForStop(Synchro synchro) {
        LOG.log(Level.INFO, "Synchro \"{0}\" is waiting for \"{1}\" to stop.",
                new Object[]{this.name, synchro.name});
        lock.lock();
        try {
            if(synchro.dependent == this && synchro.getThreading()) {
                condition.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Synchro.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            lock.unlock();
        }
        LOG.log(Level.INFO, "Synchro \"{0}\" is resuming after \"{1}\" stopped.",
                new Object[]{this.name, synchro.name});
    }
    /**
     * Safely closes the thread.
     */
    public final void synchroClose() {
        lock.lock();
        try {
            condition.signal();
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Terminates the update without an exception being thrown. This is useful
     * for abruptly ending a update externally without relying on the specific
     * mechanics of the update.
     */
    public final void halt() {
        halted = true;
    }
    /**
     * Returns true if the thread has been halted, false otherwise.
     * @return If the thread has been halted.
     */
    public final boolean getHalted() {
        return halted;
    }
    /**
     * Returns true if the thread exists and is running.
     * @return If this Synchro is running in a thread.
     */
    public final boolean getThreading() {
        return threading;
    }
    /**
     * Returns true if the program is in the initialisation or looping stage.
     * @return If this thread is running.
     */
    public final boolean getRunning() {
        return running;
    }
    /**
     * Returns true if this thread is currently in the looping stage.
     * @return If this thread is looping.
     */
    public final boolean getLooping() {
        return looping;
    }
    
    /**
     * The thread update.
     */
    @Override
    public void run() {
        try {
            threading = true;
            if(dependent != null) dependent.synchroClose();
            initialise();
            running = true; looping = true;
            while(!halted && (dependent == null || dependent.getRunning())
                    && isRunning()) {
                update();
            }
        } catch (AuroraException ex) {
            looping = running = false;
            processException(ex);
        } finally {
            looping = running = false;
            shutdown();
            halted = threading = false;
            if(dependent != null) dependent.synchroClose();
        }
    }
    
    /**
     * The method called at the beginning of thread creation. At this time,
     * <code>getThreading()</code> returns true whilst <code>getRunning()</code>
     * and <code>getLooping()</code> returns false.
     * 
     * This thread is only called once.
     * 
     * This method should not be called outside of its own thread.
     * @throws AuroraException 
     */
    protected abstract void initialise() throws AuroraException;
    /**
     * This method is called at the beginning of each thread update. At this time,
     * <code>getThreading()</code>, <code>getRunning()</code>
     * and <code>getLooping()</code> all return true.
     * 
     * Returns false if the thread is ending gracefully.
     * 
     * This method should not be called outside of its own thread.
     * @return If the thread should continue to run.
     * @throws AuroraException 
     */
    protected abstract boolean isRunning() throws AuroraException;
    /**
     * This method is called to perform each thread update. At this time,
     * <code>getThreading()</code>, <code>getRunning()</code>
     * and <code>getLooping()</code> all return true.
     * 
     * This method should not be called outside of its own thread.
     * @throws AuroraException 
     */
    protected abstract void update() throws AuroraException;
    /**
     * This method is called to perform fatal exception handling. At this time,
     * <code>getThreading()</code> and <code>getRunning()</code>
     * returns true whilst <code>getLooping()</code> returns false.
     * 
     * This method is only called once.
     * 
     * This method should not be called outside of its own thread.
     * @param ex The Fatal Exception
     */
    protected abstract void processException(AuroraException ex);
    /**
     * This method is always called when the thread is closing. Releasing of
     * resources is performed here and a best effort attempt must be made.
     * At this time, <code>getThreading()</code> and <code>getRunning()</code>
     * returns true whilst <code>getLooping()</code> returns false.
     * 
     * This is called even when <code>halt()</code> is used to end the thread.
     * This method must account for that possibility.
     */
    protected abstract void shutdown();
    
    @Override
    public String toString() {
        return name;
    }
}