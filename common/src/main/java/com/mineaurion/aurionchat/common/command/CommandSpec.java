package com.mineaurion.aurionchat.common.command;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CommandSpec {

    BALANCE("/%s balance <user>",
            arg("user", false)),

    WITHDRAW("/%s withdraw <user> <amount>",
            arg("user", true),
            arg("amount", true)
    ),

    ADD("/%s add <user> <amount>",
            arg("user", true),
            arg("amount", true)
    ),

    SET("/%s set <user> <amount>",
            arg("user", true),
            arg("amount", true)
    ),
    PAY("/%s pay <user> <amount>",
            arg("user", true),
            arg("amount", true)
    ),
    CHECK("/%s check <user> <amount> <commands>",
            arg("user", true),
            arg("amount", true),
            arg("commands...", true)
    ),

    TOP("/%s top"),

    LOG("/%s log");

    private final String usage;
    private final List<Argument> args;

    CommandSpec(String usage, PartialArgument... args){
        this.usage = usage;
        this.args = args.length == 0 ? null : Arrays.stream(args)
                .map(builder -> {
                    String key = builder.id.replace(".", "").replace(' ', '-');
                    return new Argument(builder.name, builder.required);
                })
                .collect(Collectors.toList());
    }

    public String usage(){
        return this.usage;
    }

    public List<Argument> args(){
        return this.args;
    }

    private static PartialArgument arg(String name, boolean required){
        return new PartialArgument(name, name, required);
    }

    private static final class PartialArgument {
        private final String id;
        private final String name;
        private final boolean required;

        private PartialArgument(String id, String name, boolean required){
            this.id = id;
            this.name = name;
            this.required = required;
        }
    }
};