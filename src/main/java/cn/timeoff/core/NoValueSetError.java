package cn.timeoff.core;

public class NoValueSetError extends Exception {
    private static final long serialVersionUID = 1L;

    public NoValueSetError(String msg) {
        super(msg);
    }

    public NoValueSetError(String msg, Throwable t) {
        super(msg, t);
    }
}
