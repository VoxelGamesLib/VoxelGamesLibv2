package me.minidigger.voxelgameslib.world;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.extern.java.Log;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

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
      git.add().addFilepattern(".").call();
      git.commit().setAuthor("voxelgameslib", "voxelgameslib@minidigger.me")
          .setMessage("Update " + LocalDateTime.now().toString()).call();
      // don't push here, thats need to be done manually
    } catch (IOException | GitAPIException e) {
      e.printStackTrace();
    }
  }

  public String getURL() {
    return url;
  }
}
