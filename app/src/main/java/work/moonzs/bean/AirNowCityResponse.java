package work.moonzs.bean;

import java.util.List;

public class AirNowCityResponse {

    /**
     * {
     * "code":"200",
     * "updateTime":"2022-06-24T16:58+08:00",
     * "fxLink":"http://hfx.link/2ax4",
     * "now":
     * {
     * "pubTime":"2022-06-24T16:00+08:00",
     * "aqi":"41",
     * "level":"1",
     * "category":"优",
     * "primary":"NA",
     * "pm10":"9",
     * "pm2p5":"3",
     * "no2":"13",
     * "so2":"2",
     * "co":"0.1",
     * "o3":"129"
     * },
     * "station":
     * [
     * {
     * "pubTime":"2022-06-24T16:00+08:00",
     * "name":"密云镇",
     * "id":"CNA3697",
     * "aqi":"40",
     * "level":"1",
     * "category":"优",
     * "primary":"NA",
     * "pm10":"10",
     * "pm2p5":"4",
     * "no2":"7",
     * "so2":"3",
     * "co":"0.2",
     * "o3":"125"
     * },
     * {...}
     * ],
     * "refer":
     * {
     * "sources":["QWeather","CNEMC"],
     * "license":["no commercial use"]
     * }
     * }
     */
    private String code;
    private String updateTime;
    private String fxLink;
    private NowDTO now;
    private List<StationDTO> station;
    private ReferDTO refer;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public NowDTO getNow() {
        return now;
    }

    public void setNow(NowDTO now) {
        this.now = now;
    }

    public List<StationDTO> getStation() {
        return station;
    }

    public void setStation(List<StationDTO> station) {
        this.station = station;
    }

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public static class NowDTO {
        /**
         * "now":
         * {
         * "pubTime":"2022-06-24T16:00+08:00",
         * "aqi":"41",
         * "level":"1",
         * "category":"优",
         * "primary":"NA",
         * "pm10":"9",
         * "pm2p5":"3",
         * "no2":"13",
         * "so2":"2",
         * "co":"0.1",
         * "o3":"129"
         * },
         */
        private String pubTime;
        private String aqi;
        private String level;
        private String category;
        private String primary;
        private String pm10;
        private String pm2p5;
        private String no2;
        private String so2;
        private String co;
        private String o3;

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm2p5() {
            return pm2p5;
        }

        public void setPm2p5(String pm2p5) {
            this.pm2p5 = pm2p5;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getSo2() {
            return so2;
        }

        public void setSo2(String so2) {
            this.so2 = so2;
        }

        public String getCo() {
            return co;
        }

        public void setCo(String co) {
            this.co = co;
        }

        public String getO3() {
            return o3;
        }

        public void setO3(String o3) {
            this.o3 = o3;
        }
    }

    public static class ReferDTO {
        /**
         * "refer":
         * {
         * "sources":["QWeather","CNEMC"],
         * "license":["no commercial use"]
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

    public static class StationDTO {
        /**
         * {
         * "pubTime":"2022-06-24T16:00+08:00",
         * "name":"密云镇",
         * "id":"CNA3697",
         * "aqi":"40",
         * "level":"1",
         * "category":"优",
         * "primary":"NA",
         * "pm10":"10",
         * "pm2p5":"4",
         * "no2":"7",
         * "so2":"3",
         * "co":"0.2",
         * "o3":"125"
         * }
         */
        private String pubTime;
        private String name;
        private String id;
        private String aqi;
        private String level;
        private String category;
        private String primary;
        private String pm10;
        private String pm2p5;
        private String no2;
        private String so2;
        private String co;
        private String o3;

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

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

        public String getAqi() {
            return aqi;
        }

        public void setAqi(String aqi) {
            this.aqi = aqi;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPrimary() {
            return primary;
        }

        public void setPrimary(String primary) {
            this.primary = primary;
        }

        public String getPm10() {
            return pm10;
        }

        public void setPm10(String pm10) {
            this.pm10 = pm10;
        }

        public String getPm2p5() {
            return pm2p5;
        }

        public void setPm2p5(String pm2p5) {
            this.pm2p5 = pm2p5;
        }

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getSo2() {
            return so2;
        }

        public void setSo2(String so2) {
            this.so2 = so2;
        }

        public String getCo() {
            return co;
        }

        public void setCo(String co) {
            this.co = co;
        }

        public String getO3() {
            return o3;
        }

        public void setO3(String o3) {
            this.o3 = o3;
        }
    }
}
