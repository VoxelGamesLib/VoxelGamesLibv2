package me.minidigger.voxelgameslib.persistence;

import com.google.gson.annotations.Expose;

/**
 * Stores persistence related config values
 */
public class PersistenceConfig {

    @Expose
    public String user = "voxelgameslib";
    @Expose
    public String pass = "pass";
    @Expose
    public String driver = "com.mysql.jdbc.Driver";
    @Expose
    public String url = "jdbc:mysql://localhost:3306/voxelgameslib";
    @Expose
    public String dialect = "org.hibernate.dialect.MySQL5Dialect";
    @Expose
    public int pool_size = 10;
    @Expose
    public boolean showSQL = true;
}
