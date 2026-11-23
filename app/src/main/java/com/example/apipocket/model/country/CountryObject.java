package com.example.apipocket.model.country;

import com.google.gson.annotations.SerializedName;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CountryObject {

    @SerializedName("names")
    private CountryNames names;

    @SerializedName("codes")
    private CountryCodes codes;

    @SerializedName("flag")
    private CountryFlag flag;

    @SerializedName("capitals")
    private List<CountryCapital> capitals;

    @SerializedName("region")
    private String region;

    @SerializedName("subregion")
    private String subregion;

    @SerializedName("population")
    private long population;

    @SerializedName("currencies")
    private List<CountryCurrency> currencies;

    @SerializedName("languages")
    private List<CountryLanguage> languages;

    @SerializedName("calling_codes")
    private List<String> callingCodes;

    @SerializedName("timezones")
    private List<String> timezones;

    @SerializedName("descriptions")
    private CountryDescriptions descriptions;

    public CountryNames getNames() {
        return names;
    }

    public CountryCodes getCodes() {
        return codes;
    }

    public CountryFlag getFlag() {
        return flag;
    }

    public List<CountryCapital> getCapitals() {
        return capitals;
    }

    public String getRegion() {
        return region;
    }

    public String getSubregion() {
        return subregion;
    }

    public long getPopulation() {
        return population;
    }

    public List<CountryCurrency> getCurrencies() {
        return currencies;
    }

    public List<CountryLanguage> getLanguages() {
        return languages;
    }

    public List<String> getCallingCodes() {
        return callingCodes;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public CountryDescriptions getDescriptions() {
        return descriptions;
    }

    // Helper Getters for UI
    public String getCommonName() {
        return names != null && names.getCommon() != null ? names.getCommon() : "N/A";
    }

    public String getOfficialName() {
        return names != null && names.getOfficial() != null ? names.getOfficial() : "N/A";
    }

    public String getAlpha2Code() {
        return codes != null && codes.getAlpha2() != null ? codes.getAlpha2() : "";
    }

    public String getFlagPngUrl() {
        return flag != null ? flag.getUrlPng() : null;
    }

    public String getCapitalName() {
        if (capitals != null && !capitals.isEmpty() && capitals.get(0).getName() != null) {
            return capitals.get(0).getName();
        }
        return "N/A";
    }

    public String getFormattedPopulation() {
        return NumberFormat.getInstance(Locale.US).format(population);
    }

    public String getCurrencyFormatted() {
        if (currencies != null && !currencies.isEmpty()) {
            CountryCurrency curr = currencies.get(0);
            String name = curr.getName() != null ? curr.getName() : "";
            String sym = curr.getSymbol() != null ? curr.getSymbol() : "";
            String code = curr.getCode() != null ? curr.getCode() : "";
            if (!sym.isEmpty() || !code.isEmpty()) {
                return name + " (" + sym + (sym.isEmpty() || code.isEmpty() ? "" : " / ") + code + ")";
            }
            return name;
        }
        return "N/A";
    }

    public String getLanguageName() {
        if (languages != null && !languages.isEmpty() && languages.get(0).getName() != null) {
            return languages.get(0).getName();
        }
        return "N/A";
    }

    public String getCallingCodeFormatted() {
        if (callingCodes != null && !callingCodes.isEmpty() && callingCodes.get(0) != null) {
            String cc = callingCodes.get(0);
            return cc.startsWith("+") ? cc : "+" + cc;
        }
        return "N/A";
    }

    public String getTimezoneFormatted() {
        if (timezones != null && !timezones.isEmpty() && timezones.get(0) != null) {
            return timezones.get(0);
        }
        return "N/A";
    }

    public String getShortDescription() {
        if (descriptions != null && descriptions.getShortDescription() != null) {
            return descriptions.getShortDescription();
        }
        return "";
    }
}
