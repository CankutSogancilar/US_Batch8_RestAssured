package POJOClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Place {
    String placeName; // place name
    String longitude;
    String state;
    String stateAbbreviation;
    String latitude;

    // bunlarin hepsini de body den yazdik

    public String getPlaceName() {
        return placeName;
    }

    @JsonProperty("place name") // this matches placeName variable with place name value from the request body
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longtitude) {
        this.longitude = longtitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateAbbreviation() {
        return stateAbbreviation;
    }
    @JsonProperty("state abbreviation")
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
