package com.voxelgameslib.voxelgameslib.lang;

import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.serializer.ComponentSerializers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import javax.annotation.Nonnull;

import com.voxelgameslib.voxelgameslib.exception.VoxelGameLibException;

public class LangFormatter {

    @Nonnull
    public static TextComponent parseFormat(@Nonnull String string) {
        //%replace%System.out.println("parse format " + string);
        TextComponent.Builder builder = TextComponent.builder("");
        TextComponent.Builder current = TextComponent.builder("");

        List<String> tokens = tokenize(string);
        //%replace%System.out.println(tokens);

        Stack<ClickEvent> clickEvents = new Stack<>();
        Stack<HoverEvent> hoverEvents = new Stack<>();
        Stack<TextColor> colors = new Stack<>();
        Stack<TextDecoration> decorations = new Stack<>();

        //%replace%System.out.println("start parsing");
        for (String token : tokens) {
            // click
            if (token.startsWith("click")) {
                //%replace%System.out.println("add click " + token);
                clickEvents.push(handleClick(token));
            } else if (token.equals("/click")) {
                //%replace%System.out.println("end click");
                clickEvents.pop();
            }
            // hover
            else if (token.startsWith("hover")) {
                //%replace%System.out.println("add hover " + token);
                hoverEvents.push(handleHover(token));
            } else if (token.equals("/hover")) {
                //%replace%System.out.println("end hover");
                hoverEvents.pop();
            }
            // color
            else if (resolveColor(token).isPresent()) {
                //%replace%System.out.println("add color " + token);
                colors.push(handleColor(token));
            } else if (token.startsWith("/") && resolveColor(token.replace("/", "")).isPresent()) {
                //%replace%System.out.println("end color");
                colors.pop();
            }
            // decoration
            else if (resolveDecoration(token).isPresent()) {
                //%replace%System.out.println("add deco " + token);
                decorations.push(handleDecoration(token));
            } else if (token.startsWith("/") && resolveDecoration(token.replace("/", "")).isPresent()) {
                //%replace%System.out.println("end deco");
                decorations.pop();
            }
            // normal text
            else {
                if (token.equals("")) continue;

                // set everything that is not closed yet
                if (clickEvents.size() > 0) {
                    //%replace%System.out.println("add extra click");
                    current.clickEvent(clickEvents.peek());
                }
                if (hoverEvents.size() > 0) {
                    //%replace%System.out.println("add extra hover");
                    current.hoverEvent(hoverEvents.peek());
                }
                if (colors.size() > 0) {
                    //%replace%System.out.println("add extra color");
                    current.color(colors.peek());
                }
                if (decorations.size() > 0) {
                    //%replace%System.out.println("add extra deco");
                    current.decoration(decorations.peek(), true);
                }

                //%replace%System.out.println("add text " + token);
                // add to main builder and start new component
                builder.append(current.content(token).build());
                current = TextComponent.builder();
            }
        }
        try {
            builder.append(current.build());
        } catch (IllegalStateException e) {
            builder.append(current.content("").build());
        }
        return builder.build();
    }

    public static void main(String[] args) {
        System.out.println("simple mix");
        String test = "{yellow}{name}{/yellow}{aqua} has started a new round of {/aqua}{yellow}{mode}.{/yellow}" +
                " {click:run_command:{command}}{aqua}Click here to join!{/aqua}{/click}";
        test = test.replace("{name}", "MiniDigger");
        test = test.replace("{mode}", "1vs1");
        test = test.replace("{command}", "/game join ssss");
        System.out.println(ComponentSerializers.JSON.serialize(parseFormat(test)));

        System.out.println("color + deco");
        test = "{bold}BOLD! {yellow}YELLOW BOLD {/bold}ONLY YELLOW{/yellow}";
        System.out.println(ComponentSerializers.JSON.serialize(parseFormat(test)));

        System.out.println("nested color");
        test = "{bold}BOLD! {yellow}YELLOW {blue}BOLD {/bold}ONLY{/blue} YELLOW{/yellow}";
        System.out.println(ComponentSerializers.JSON.serialize(parseFormat(test)));

        System.out.println("RECURSION HELL!");
        test = "{yellow}{hover:show_text:\"{red}test{/red}\"}test{/yellow}";
        System.out.println(ComponentSerializers.JSON.serialize(parseFormat(test)));

        System.out.println("RECURSION HELLv2!");
        test = "{yellow}{hover:show_text:\"{red}test{/red}\"}test{/yellow}";
        System.out.println(ComponentSerializers.JSON.serialize(parseFormat(test)));
    }

    @Nonnull
    private static ClickEvent handleClick(@Nonnull String token) {
        String[] args = token.split(":");
        ClickEvent clickEvent;
        if (args.length < 2)
            throw new VoxelGameLibException("Can't parse click action (too few args) " + token);
        switch (args[1]) {
            case "run_command":
                clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, token.replace("click:run_command:", ""));
                break;
            case "suggest_command":
                clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, token.replace("click:suggest_command:", ""));
                break;
            case "open_url":
                clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, token.replace("click:open_url:", ""));
                break;
            case "change_page":
                clickEvent = new ClickEvent(ClickEvent.Action.CHANGE_PAGE, token.replace("click:change_page:", ""));
                break;
            default:
                throw new VoxelGameLibException("Can't parse click action (invalid type " + args[1] + ") " + token);
        }
        return clickEvent;
    }

    @Nonnull
    private static HoverEvent handleHover(@Nonnull String token) {
        String[] args = token.split(":");
        HoverEvent hoverEvent;
        if (args.length < 2)
            throw new VoxelGameLibException("Can't parse hover action (too few args) " + token);
        switch (args[1]) {
            case "show_text":
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, parseFormat(token.replace("hover:show_text:", "")));
                break;
            case "show_item":
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, parseFormat(token.replace("hover:show_item:", "")));
                break;
            case "show_entity":
                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, parseFormat(token.replace("hover:show_entity:", "")));
                break;
            default:
                throw new VoxelGameLibException("Can't parse hover action (invalid type " + args[1] + ") " + token);
        }
        return hoverEvent;
    }

    @Nonnull
    private static TextColor handleColor(@Nonnull String token) {
        return resolveColor(token).orElseThrow(() -> new VoxelGameLibException("Can't parse color " + token));
    }

    @Nonnull
    private static TextDecoration handleDecoration(@Nonnull String token) {
        return resolveDecoration(token).orElseThrow(() -> new VoxelGameLibException("Can't parse decoration " + token));
    }

    @Nonnull
    private static Optional<TextColor> resolveColor(@Nonnull String token) {
        try {
            return Optional.of(TextColor.valueOf(token.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    private static Optional<TextDecoration> resolveDecoration(@Nonnull String token) {
        try {
            return Optional.of(TextDecoration.valueOf(token.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    @Nonnull
    private static List<String> tokenize(@Nonnull String input) {
        List<String> result = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        boolean skip = false;

        for (int i = 0; i < input.length(); i++) {
            char cI = input.charAt(i);
            if (cI == '"') {
                if (i > 0 && input.charAt(i - 1) == '}'
                        || i < input.length() - 1 && input.charAt(i + 1) == '{') {
                    // don't add the surrounding "s
                    continue;
                }
            } else if (cI == '{') {
                if (i > 0 && input.charAt(i - 1) == '"') {
                    // we found a nested value, lets skip the next brackets till we find the end of the nesting
                    skip = true;
                    buffer.append('{');
                    continue;
                } else {
                    if (!skip) {
                        // split at {
                        result.add(buffer.toString());
                        buffer = new StringBuilder();
                        continue;
                    }
                }
            } else if (cI == '}') {
                if (i < input.length() - 1 && input.charAt(i + 1) == '"') {
                    // the end of the nesting was found, time to stop skipping
                    skip = false;
                    buffer.append('}');
                    continue;
                } else {
                    if (!skip) {
                        // split at }
                        result.add(buffer.toString());
                        buffer = new StringBuilder();
                        continue;
                    }
                }
            }

            // normal char, lets add it
            buffer.append(cI);
        }

        // add rest
        result.add(buffer.toString());

        return result;
    }
}
