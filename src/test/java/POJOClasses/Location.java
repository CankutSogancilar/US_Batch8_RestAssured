package POJOClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Location {
    String postCode; // post code from out body
    String country;
    String countryAbbreviation;
    ArrayList<Place> places;

    // bunlarin hepsini body den yazdik


    public String getPostCode() {
        return postCode;
    }

    @JsonProperty("post code") // arada bosluk birakinca sorun, o yuzden bunu kullandik.
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryAbbreviation() {
        return countryAbbreviation;
    }

    @JsonProperty("country abbreviation")
    public void setCountryAbbreviation(String countryAbbreviation) {
        this.countryAbbreviation = countryAbbreviation;
    }

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }
}
