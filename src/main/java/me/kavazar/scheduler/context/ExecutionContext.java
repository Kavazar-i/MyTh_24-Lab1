package me.kavazar.scheduler.context;

import java.util.concurrent.Callable;

// Execution Context Interface
public interface ExecutionContext extends Callable<Void> {
}
