package com.voxelgameslib.voxelgameslib.startup;

import java.util.HashSet;
import java.util.Set;
import javax.inject.Singleton;

import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;

@Singleton
public class StartupHandler {

    private Set<String> services = new HashSet<>();

    public void registerService(String name) {
        if (services.contains(name)) {
            throw new VoxelGameLibException("Service " + name + " is already registered!");
        }
        services.add(name);
    }

    public void unregisterService(String name) {
        if (!services.contains(name)) {
            throw new VoxelGameLibException("Service " + name + " was already unregistered or was never registered at all!");
        }
        services.remove(name);
    }

    public boolean isReady() {
        return services.size() == 0;
    }
}
