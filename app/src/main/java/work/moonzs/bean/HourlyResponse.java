package work.moonzs.bean;

import java.util.List;

public class HourlyResponse {
    /**
     * {
     * "code":"200",
     * "updateTime":"2022-06-24T15:35+08:00",
     * "fxLink":"http://hfx.link/2ax1",
     * "hourly":
     * [
     * {
     * "fxTime":"2022-06-24T17:00+08:00",
     * "temp":"35",
     * "icon":"100",
     * "text":"晴",
     * "wind360":"295",
     * "windDir":"西北风",
     * "windScale":"3-4",
     * "windSpeed":"14",
     * "humidity":"19",
     * "pop":"7",
     * "precip":"0.0",
     * "pressure":"990",
     * "cloud":"10",
     * "dew":"8"
     * },
     * {...}
     * ],
     * "refer":
     * {
     * "sources":["QWeather","NMC","ECMWF"],
     * "license":["no commercial use"]
     * }
     * }
     */
    private String code;
    private String updateTime;
    private String fxLink;
    private List<HourlyDTO> hourly;
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

    public List<HourlyDTO> getHourly() {
        return hourly;
    }

    public void setHourly(List<HourlyDTO> hourly) {
        this.hourly = hourly;
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
         * "sources":["QWeather","NMC","ECMWF"],
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

    public static class HourlyDTO {
        /**
         * {
         * "fxTime":"2022-06-24T17:00+08:00",
         * "temp":"35",
         * "icon":"100",
         * "text":"晴",
         * "wind360":"295",
         * "windDir":"西北风",
         * "windScale":"3-4",
         * "windSpeed":"14",
         * "humidity":"19",
         * "pop":"7",
         * "precip":"0.0",
         * "pressure":"990",
         * "cloud":"10",
         * "dew":"8"
         * }
         */
        private String fxTime;
        private String temp;
        private String icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String pop;
        private String precip;
        private String pressure;
        private String cloud;
        private String dew;

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getWind360() {
            return wind360;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
        }

        public String getWindDir() {
            return windDir;
        }

        public void setWindDir(String windDir) {
            this.windDir = windDir;
        }

        public String getWindScale() {
            return windScale;
        }

        public void setWindScale(String windScale) {
            this.windScale = windScale;
        }

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getDew() {
            return dew;
        }

        public void setDew(String dew) {
            this.dew = dew;
        }
    }


}
