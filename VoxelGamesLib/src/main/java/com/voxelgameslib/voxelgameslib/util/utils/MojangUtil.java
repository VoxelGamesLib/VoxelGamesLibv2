package com.voxelgameslib.voxelgameslib.util.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.UUID;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.api.exception.VoxelGameLibException;

/**
 * Small util to access the mojang api
 */
public class MojangUtil {

    private static final String NAME_HISTORY_URL = "https://api.mojang.com/user/profiles/%1/names";

    //TODO we need to do some caching to not break mojang api rate limits

    /**
     * Tries to fetch the current display name for the user
     *
     * @param id the id of the user to check
     * @return the current display name of that user
     * @throws IOException           if something goes wrong
     * @throws VoxelGameLibException if the user has no display name
     */
    @Nonnull
    public static String getDisplayName(@Nonnull UUID id) throws IOException, VoxelGameLibException {
        URL url = new URL(NAME_HISTORY_URL.replace("%1", id.toString().replace("-", "")));
        System.out.println(url.toString());
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(url.openStream())));
        if (scanner.hasNext()) {
            String json = scanner.nextLine();
            try {
                JSONArray jsonArray = (JSONArray) new JSONParser().parse(json);
                if (json.length() > 0) {
                    return (String) ((JSONObject) jsonArray.get(0)).get("name");
                }
            } catch (ParseException ignore) {
            }
        }

        throw new VoxelGameLibException("User has no name! " + id);
    }
}
