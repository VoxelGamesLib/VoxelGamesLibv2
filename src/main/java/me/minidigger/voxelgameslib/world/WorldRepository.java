package me.minidigger.voxelgameslib.world;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.java.Log;

@Log
public class WorldRepository {

    private static final String defaultRepoURL = "https://github.com/VoxelGamesLib/maps.git";

    //TODO finish world repo stuff

    @Inject
    @Named("WorldsFolder")
    private File worldsDir;

    private String url;

    public WorldRepository() {
        this(defaultRepoURL);
    }

    public WorldRepository(String url) {
        this.url = url;
    }

    public void cloneRepo() {
        try {
            Git git = Git.cloneRepository().setURI(url).setDirectory(worldsDir).call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void updateRepo() {
        try {
            Git git = Git.open(worldsDir);
            git.pull().call();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void commitRepo() {
        try {
            Git git = Git.open(worldsDir);
            new CustomAddCommand(git.getRepository()).addFilepattern(".").call();
            git.commit().setAuthor("voxelgameslib", "voxelgameslib@minidigger.me")
                    .setMessage("Update " + LocalDateTime.now().toString()).call();
            // don't push here, thats need to be done manually
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void fixDaFuckingStupidGitignore() {
        File[] files = worldsDir.listFiles();
        if (files == null) return;
        File gitignore = new File(worldsDir, ".gitignore");
        try (PrintWriter writer = new PrintWriter(new FileWriter(gitignore, false))) {
            for (String name : Arrays.stream(files).map(File::getName).filter(name -> name.endsWith(".zip")).collect(Collectors.toList())) {
                writer.println("!" + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getURL() {
        return url;
    }
}
