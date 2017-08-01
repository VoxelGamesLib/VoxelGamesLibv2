package me.minidigger.voxelgameslib.lang;

import javax.annotation.Nonnull;

/**
 * Created by Martin on 12.10.2016. <p> Style note: the members needs to be sorted alphabetical!
 */
public enum LangKey {

    COMMAND_NO_PERMISSION("{red}You need to be {yellow}{role}{red} to execute this command!", "role"),
    COMMAND_NO_CONSOLE("{red}This command can't be executed via console!"),
    COMMAND_TOO_FEW_ARGUMENTS(
            "{red}You entered too few arguments! Minimum is {yellow}{min}{red}, you entered {yellow}{actual}",
            "min", "actual"),
    COMMAND_TOO_MANY_ARGUMENTS(
            "{red}You entered too many arguments! Maximum is {yellow}{max}{red}, you entered {yellow}{actual}",
            "min", "actual"),
    COMMAND_USAGE("{red}Usage: {usage}", "usage"),

    DATA_NOT_LOADED("{red}Your data has not been loaded, please join again!"),

    EDITMODE_ALREADY_ENABLED("{red}Edit mode already enabled!"),
    EDITMODE_NOT_ENABLED("{red}Edit mode not enabled! Enable to via {yellow}/editmode on"),
    EDITMODE_ENABLED("{green}Edit mode {yellow}enabled{green}!"),
    EDITMODE_DISABLED("{green}Edit mode {yellow}disabled{green}!"),

    GAME_GAMEMODE_INSTALLED("{aqua}Installed gamemodes: {yellow}{modes}", "modes"),
    GAME_GAMEMODE_UNKNOWN("{red}Unknown GameMode, {yellow}{mode}{red}, Is it installed?", "mode"),
    GAME_PLAYER_JOIN("{yellow}{name}{aqua} has joined the game", "name"),
    GAME_PLAYER_LEAVE("{yellow}{name}{aqua} has left the game", "name"),
    GAME_CANT_SPECTATE("{red}You can't spectate this game right now"),
    GAME_GAME_STARTED("{green}Game started"),
    GAME_END("{aqua}Game ended"),
    GAME_ABORT("{red}Game aborted"),
    GAME_COULD_NOT_START("{red}Could not start game!"),
    GAME_NO_GAME_FOUND("{red}No game to join found!"),
    GAME_STARTING("{green}The game is about to start"),
    GAME_START_ABORTED("{red}Game start aborted!"),
    GAME_STARTING_ACCELERATED("{green}We have reached the max amount of players. Speeding up countdown!"),
    GAME_STOP_IN_NO_GAME("{red}You don't play in any game, trying spectating games..."),
    GAME_STOP_IN_NO_GAME_SPEC("{red}You don't spectate any game. Can't stop"),

    GAME_ANNOUNCE_GAME_STARTED("{yellow}{name}{aqua} has started a new round of {yellow}{mode}. {click:run_command:{command}}{aqua}Click here to join!{/click}", "command", "name", "mode"),

    GAME_GAMELIST_HEADER("{gold}##### Currently running games ####"),
    GAME_GAMELIST_ENTRY(
            "{aqua}# {yellow}{uuid} {aqua}- {yellow}{mode} {aqua}in phase {yellow}{phase} {aqua}with {yellow}{players}({spectators}) {aqua}players",
            "uuid", "mode", "phase", "players", "spectators"),
    GAME_GAMELIST_FOOTER("{gold}###############################"),

    GENERAL_INVALID_ARGUMENT("{red}u w00t m8? {yellow}{arg}{red} is not an valid argument!", "arg"),
    GENERAL_INVALID_NUMBER("{yellow}{num}{red} is not a valid number!", "num"),
    GENERAL_INVALID_USER_NAME("{red}Unknown user {yellow}{user}{red}!", "user"),

    LANG_CURRENT(
            "{aqua}You are currently using language {yellow}{lang}{aqua}, use {yellow}/lang set <language>{aqua} to set it to another language.",
            "lang"),
    LANG_INSTALLED(
            "{aqua}The following languages are installed and available on this server: {yellow}{langs}",
            "langs"),
    LANG_UNKNOWN(
            "{red}Unknown language {yellow}{lang}{red}! Use {yellow}/lang{red} to see which languages are available!",
            "lang"),
    LANG_UPDATE("{green}You are now receiving messages in {yellow}{lang}{green}!", "lang"),
    LANG_NOT_ENABLED(
            "{red}However, {yellow}{lang}{red} is not enabled on this server. Ask the admins very kindly to enable it!",
            "lang"),

    LOG_LEVEL_CURRENT("{aqua}The current log level is {yellow}{level}", "level"),
    LOG_LEVEL_SET("{green}Log level was set to {yellow}{level}", "level"),
    LOG_LEVEL_UNKNOWN("{red}Unknown log level {yellow}{level}", "level"),

    ROLE_SELF("{aqua}You are {yellow}{role}{aqua}.", "role"),
    ROLE_OTHERS("{yellow}{user}{aqua} is {yellow}{role}{aqua}.", "user", "role"),
    ROLE_UNKNOWN_ROLE("{red}Unknown role {yellow}{role}{red}!", "role"),
    ROLE_UPDATED_OTHER("{green}The role of {yellow}{user}{green} was updated to {yellow}{role}",
            "user", "role"),
    ROLE_UPDATED_SELF("{green}Your role was updated to {yellow}{role}", "user", "role"),

    SIGNS_BREAK_NO_PERM(
            "{red}You are not allowed to break this {yellow}{placeholder}{red} sign, only a {yellow}{role}{red} can do that!",
            "placeholder", "role"),
    SIGNS_BREAK_SUCCESS("{green}You have destroyed a {yellow}{placeholder}{green} sign.",
            "placeholder"),
    SIGNS_PLACE_NO_PERM(
            "{red}You are not allowed to place this {yellow}{placeholder}{red} sign, only a {yellow}{role}{red} can do that!",
            "placeholder", "role"),
    SIGNS_PLACE_SUCCESS("{green}You have placed a {yellow}{placeholder}{green} sign.", "placeholder"),

    VOTE_ALREADY_VOTED("{red}You've already voted!"),
    VOTE_END(
            "{aqua}Map {yellow}{name}{aqua} by {yellow}{author}{aqua} won with {yellow}{votes}{aqua} votes",
            "name", "author", "votes"),
    VOTE_MESSAGE_TOP("{gold}### You can now vote for a map! ###"),
    VOTE_MESSAGE_MAP("{click:run_command:{command}}{aqua}#{yellow}{num}{aqua}: {yellow}{name}{aqua} by {yellow}{author}{/click}", "command", "num", "name", "author"),
    VOTE_MESSAGE_BOT("{gold}###############################"),
    VOTE_NO_MAPS_FOUND("{red}Could not find any maps!"),
    VOTE_SUBMITTED("{green}Vote for map #{yellow}{mapId}{green} ({yellow}{mapName}{green}) submitted", "mapName", "mapId"),
    VOTE_UNKNOWN_MAP("{red}Unknown map {yellow}{map}", "map"),

    WORLD_CREATOR_DONE(
            "{green}Done creating the world! Restart the game to be able to play the map."),
    WORLD_CREATOR_AUTHOR_SET(
            "{green}Author set to {yellow}{author}{green}.{aqua} Select the gamemodes this map should be played on.",
            "author"),
    WORLD_CREATOR_DONE_QUERY("{aqua}Click here if you are done."),
    WORLD_CREATOR_EDIT_MODE_ON("{aqua}Click here to enter map editing mode"),
    WORLD_CREATOR_EDIT_MODE_OFF("{aqua}Click here when you are done"),
    WORLD_CREATOR_ENTER_AUTHOR(
            "{green}Name set to {yellow}{name}{green},{aqua} click here and enter the author", "name"),
    WORLD_CREATOR_ENTER_DISPLAY_NAME(
            "{green}Radius set.{aqua} Click here and enter the display name for the world."),
    WORLD_CREATOR_ENTER_CENTER(
            "{green}World loaded.{aqua} Walk to the middle of the world and click here to mark it."),
    WORLD_CREATOR_ENTER_RADIUS(
            "{green}Center set.{aqua} Click here and enter the radius of this world (in which we should search for markers and load chunks and stuff)."),
    WORLD_CREATOR_ENTER_WORLD_NAME(
            "{aqua}Click here and enter the name of the world that you want to create."),
    WORLD_CREATOR_GAMEMODE_ADDED("{green}Added gamemode, press another or press done to continue."),
    WORLD_CREATOR_GAMEMODE_DONE_BUTTON("{gold}Done."),
    WORLD_CREATOR_INFO(
            "TODO: Enter a nice info message about the world creator"),//TODO world creator info
    WORLD_CREATOR_IN_USE("{red}The user {yellow}{user}{red} is currently using the world creator!",
            "user"),
    WORLD_CREATOR_MAP_SUMMARY(
            "TODO: Enter a nice summary message {displayname} {worldname} {author} {center} {radius} {gamemodes}",
            "displayname", "worldname", "author", "center", "radius",
            "gamemodes"),//TODO nice summery about map
    WORLD_CREATOR_SAVE_CONFIG_ERROR(
            "{red}Error while saving the world config, {yellow}{msg}:{exception}", "msg", "exception"),
    WORLD_CREATOR_SAVE_ZIP_ERROR("{red}Error while zipping the world, {yellow}{msg}:{exception}",
            "msg", "exception"),
    WORLD_CREATOR_WRONG_STEP(
            "{red}You are trying to do the wrong step! You are at step {yellow}{step}{red}, you want to do step {yellow}{entered}{red}!",
            "step", "entered"),

    WORLD_UNKNOWN_MAP("{red}Could not find a map named {yellow}{map}{red} :/", "map"),

    DUEL_WRONG_PLAYER_COUNT("{red}You can't duel with {yellow}{players} {red}players, you need {yellow}2{red}!",
            "players"),

    TEAM_AUTO_ASSIGNED("{aqua}You didn't select a team and have been automatically assigned to team {yellow}{team}", "team"),
    TEAM_AUTO_BALANCED("{aqua}You were auto balanced from {yellow}{team1}{aqua} to {yellow}{team2}", "team1", "team2"),

    WORLD_MODIFY_START("{green}World modifier started"),
    WORLD_MODIFY_NOT_STARTED("{red}You need to start the world modifier using {yellow}/wc m start {red}first"),
    WORLD_MODIFY_DISPLAYNAME_VIEW("{aqua}The current display name is {yellow}{name}", "name"),
    WORLD_MODIFY_DISPLAYNAME_EDIT("{aqua}Changed the display name to {yellow}{name}", "name"),
    WORLD_MODIFY_AUTHOR_VIEW("{aqua}The current author is {yellow}{author}", "author"),
    WORLD_MODIFY_AUTHOR_EDIT("{aqua}Changed the author to {yellow}{author}", "author"),
    WORLD_MODIFY_RADIUS_VIEW("{aqua}The current radius is {yellow}{radius}", "radius"),
    WORLD_MODIFY_RADIUS_EDIT("{aqua}Changed the radius to {yellow}{radius}", "radius"),
    WORLD_MODIFY_CENTER_VIEW("{aqua}The current center is {yellow}{center}", "center"),
    WORLD_MODIFY_CENTER_EDIT("{aqua}Changed the center to {yellow}{center}", "center"),
    WORLD_MODIFY_HELP("Idk some fancy help text");//TODO help for wc m

    @Nonnull
    private final String defaultValue;

    @Nonnull
    private final String[] args;


    LangKey(@Nonnull String defaultValue, @Nonnull String... args) {
        this.defaultValue = defaultValue;
        this.args = args;
    }

    /**
     * @return the default value for this lang key, in english
     */
    @Nonnull
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @return the arguments that this key requires
     */
    @Nonnull
    public String[] getArgs() {
        return args;
    }

}
