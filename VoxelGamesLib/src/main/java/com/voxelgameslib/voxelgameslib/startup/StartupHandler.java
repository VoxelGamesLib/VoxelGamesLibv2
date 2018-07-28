package com.voxelgameslib.voxelgameslib.startup;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;

@Singleton
public class StartupHandler {

    private static final Logger log = Logger.getLogger(StartupHandler.class.getName());

    private Set<String> services = new HashSet<>();
    private boolean interrupted = false;

    public void registerService(String name) {
        if (services.contains(name)) {
            throw new VoxelGameLibException("Service " + name + " is already registered!");
        }
        log.finer("Starting service " + name);
        services.add(name);
    }

    public void unregisterService(String name) {
        if (!services.contains(name)) {
            throw new VoxelGameLibException("Service " + name + " was already unregistered or was never registered at all!");
        }
        services.remove(name);
        log.finer("Service " + name + " stopped, " + services.size() + " left");
    }

    public boolean isReady() {
        return services.size() == 0;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void interrupt() {
        this.interrupted = true;
    }
}
