package me.minidigger.voxelgameslib.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import me.minidigger.voxelgameslib.lang.Locale;
import me.minidigger.voxelgameslib.signs.SignLocation;
import me.minidigger.voxelgameslib.user.UserData;

/**
 * Empty persistence provider, used when persistence is disabled
 */
public class EmptyPersistenceProvider implements PersistenceProvider {

  @Override
  public void start() {
    // ignore
  }

  @Override
  public void stop() {
    // ignore
  }

  @Override
  public void saveUserData(UserData user) {
    // ignore
  }

  @Override
  public Optional<UserData> loadUserData(UUID id) {
    return Optional.empty();
  }

  @Override
  public void saveLocale(Locale locale) {
    // ignore
  }

  @Override
  public List<Locale> loadLocales() {
    return new ArrayList<>();
  }

  @Override
  public List<SignLocation> loadSigns() {
    return new ArrayList<>();
  }

  @Override
  public void saveSigns(List<SignLocation> signs) {
    // ignore
  }

  @Override
  public void deleteSigns(List<SignLocation> signs) {
    // ignore
  }
}
