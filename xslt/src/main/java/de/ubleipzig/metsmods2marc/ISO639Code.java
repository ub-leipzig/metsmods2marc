package de.ubleipzig.metsmods2marc;

import com.fasterxml.jackson.annotation.JsonProperty;

class ISO639Code {

    @JsonProperty("ISO_639_2")
    private String ISO_639_2;

    public String getISO_639_2(){
        return ISO_639_2;
    }

    @JsonProperty("ISO_639_1")
    private String ISO_639_1;

    public String getISO_639_1(){
        return ISO_639_1;
    }

    @JsonProperty("English_name_of_Language")
    private String English_name_of_Language;

    public String getEnglishName(){
        return English_name_of_Language;
    }

    @JsonProperty("French_name_of_Language")
    private String French_name_of_Language;

    public String getFrenchName(){
        return French_name_of_Language;
    }

    @JsonProperty("German_name_of_Language")
    private String German_name_of_Language;

    public String getGermanName(){
        return German_name_of_Language;
    }
}
