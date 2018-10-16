package com.app.risk.model;


public class CountryJson implements Comparable<CountryJson> {
    String country;
    String continent;
    CountryJson()
    {
        super();
    }
    CountryJson(String new_continent,String new_country)
    {
        continent=new_continent;
        country=new_country;
    }
    public String getCountry() {
        return country;
    }

    public void setCountry(String new_country) {
        this.country = new_country;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String new_continent) {
        this.continent = new_continent;
    }
    public int hashCode(){

        return country.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof CountryJson) {
            if (this.country.equalsIgnoreCase(((CountryJson) obj).country)
                    && this.continent.equalsIgnoreCase(((CountryJson) obj).continent)) {
                return true;
            }
        }
        return false;
    }

    public int compareTo(CountryJson countryJson) {

        if (this.country.equals(countryJson.country))
            return 0;
        return this.country.compareToIgnoreCase(countryJson.country);
    }
}
