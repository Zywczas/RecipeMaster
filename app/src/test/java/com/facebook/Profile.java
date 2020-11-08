package com.facebook;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.net.URI;

public class Profile {

    private static Profile profile = new Profile();

    public Profile() {}

    private final @Nullable String name = "Krzysztof Jerzyna";
    private final @Nullable String firstName = "Krzysztof";
    private final @Nullable String middleName = "Ze Szczecina";
    private final @Nullable String lastName = "Jerzyna";
    private final @Nullable String url = "https://www.facebook.com/";
    private final @Nullable String id = "007";

    public static Profile getCurrentProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLinkUri() {
        return url;
    }
}
