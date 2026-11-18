package com.mrcrayfish.furniture.client;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

/**
 * Client settings (config/cfm-client.toml).
 */
public class ClientConfig
{
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> TRUSTED_URL_DOMAINS;

    static
    {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.comment("TVs and photo frames").push("screens");
        TRUSTED_URL_DOMAINS = builder
                .comment("Domains the TV and photo frame may download pictures from. Nothing is downloaded from any other domain, so with",
                        "this empty (the default) they show nothing. For https://i.imgur.com/Jvh1OQm.jpeg the domain is i.imgur.com.")
                .translation("cfm.configgui.trustedUrlDomains")
                .defineListAllowEmpty("trustedUrlDomains", List.of(), () -> "i.imgur.com", value -> value instanceof String);
        builder.pop();
        SPEC = builder.build();
    }
}
