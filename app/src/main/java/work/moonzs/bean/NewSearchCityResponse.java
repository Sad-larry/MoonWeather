package work.moonzs.bean;

import java.util.List;

public class NewSearchCityResponse {

    /**
     * {
     * "code":"200",
     * "location":
     * [
     * {
     * "name":"南康",
     * "id":"101240704",
     * "lat":"25.66172",
     * "lon":"114.75693",
     * "adm2":"赣州",
     * "adm1":"江西省",
     * "country":"中国",
     * "tz":"Asia/Shanghai",
     * "utcOffset":"+08:00",
     * "isDst":"0",
     * "type":"city",
     * "rank":"35",
     * "fxLink":"http://hfx.link/38s1"
     * },
     * {...}
     * ],
     * "refer":
     * {
     * "sources":["QWeather"],
     * "license":["commercial license"]
     * }
     * }
     */
    private String code;
    private List<LocationDTO> location;
    private ReferDTO refer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LocationDTO> getLocation() {
        return location;
    }

    public void setLocation(List<LocationDTO> location) {
        this.location = location;
    }

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public static class ReferDTO {
        /**
         * "refer":
         * {
         * "sources":["QWeather"],
         * "license":["commercial license"]
         * }
         */
        private List<String> sources;
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    public static class LocationDTO {
        /**
         * {
         * "name":"南康",
         * "id":"101240704",
         * "lat":"25.66172",
         * "lon":"114.75693",
         * "adm2":"赣州",
         * "adm1":"江西省",
         * "country":"中国",
         * "tz":"Asia/Shanghai",
         * "utcOffset":"+08:00",
         * "isDst":"0",
         * "type":"city",
         * "rank":"35",
         * "fxLink":"http://hfx.link/38s1"
         * }
         */
        private String name;
        private String id;
        private String lat;
        private String lon;
        private String adm2;
        private String adm1;
        private String country;
        private String tz;
        private String utcOffset;
        private String isDst;
        private String type;
        private String rank;
        private String fxLink;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getAdm2() {
            return adm2;
        }

        public void setAdm2(String adm2) {
            this.adm2 = adm2;
        }

        public String getAdm1() {
            return adm1;
        }

        public void setAdm1(String adm1) {
            this.adm1 = adm1;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTz() {
            return tz;
        }

        public void setTz(String tz) {
            this.tz = tz;
        }

        public String getUtcOffset() {
            return utcOffset;
        }

        public void setUtcOffset(String utcOffset) {
            this.utcOffset = utcOffset;
        }

        public String getIsDst() {
            return isDst;
        }

        public void setIsDst(String isDst) {
            this.isDst = isDst;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getFxLink() {
            return fxLink;
        }

        public void setFxLink(String fxLink) {
            this.fxLink = fxLink;
        }
    }
}
