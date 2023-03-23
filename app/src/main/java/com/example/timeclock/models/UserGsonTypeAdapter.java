package com.example.timeclock.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;

public class UserGsonTypeAdapter implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("Role") && jsonObject.get("Role").isJsonPrimitive()
                && EnumUtils.isValidEnum(User.UserType.class, jsonObject.get("Role").getAsString())) {
            User.UserType userType = User.UserType.valueOf(jsonObject.get("Role").getAsString());
            switch (userType) {
                case administrator:
                    return context.deserialize(json, AdminUser.class);
                case nonadministrator:
                default:
                    return context.deserialize(json, NonAdminUser.class);
            }
        } else {
            return context.deserialize(json, NonAdminUser.class);
        }
    }
}
