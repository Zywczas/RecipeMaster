package com.facebook;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.net.URI;

public class Profile {

    private static Profile profile = new Profile();

    public Profile() {}

    private final @Nullable String name = "James Błąd";
    private final @Nullable String firstName = "James";
    private final @Nullable String middleName = "Double oh seven";
    private final @Nullable String lastName = "Błąd";
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
