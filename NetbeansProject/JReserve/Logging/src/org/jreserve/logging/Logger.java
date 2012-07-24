package org.jreserve.logging;

/**
 *
 * @author Peter Decsi
 * @version 1.0
 */
public interface Logger {
    public void fatal(Object message);
    public void fatal(Throwable t, Object message);
    public void fatal(String msgFormat, Object... params);
    public void fatal(Throwable t, String msgFormat, Object... params);

    public void error(Object message);
    public void error(Throwable t, Object message);
    public void error(String msgFormat, Object... params);
    public void error(Throwable t, String msgFormat, Object... params);

    public void warn(Object message);
    public void warn(Throwable t, Object message);
    public void warn(String msgFormat, Object... params);
    public void warn(Throwable t, String msgFormat, Object... params);

    public void info(Object message);
    public void info(Throwable t, Object message);
    public void info(String msgFormat, Object... params);
    public void info(Throwable t, String msgFormat, Object... params);

    public void debug(Object message);
    public void debug(Throwable t, Object message);
    public void debug(String msgFormat, Object... params);
    public void debug(Throwable t, String msgFormat, Object... params);

    public void trace(Object message);
    public void trace(Throwable t, Object message);
    public void trace(String msgFormat, Object... params);
    public void trace(Throwable t, String msgFormat, Object... params);
}
