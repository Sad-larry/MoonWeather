package work.moonzs.bean;

import java.util.List;

public class NowResponse {

    /**
     * {
     * "code":"200",
     * "updateTime":"2022-06-22T00:32+08:00",
     * "fxLink":"http://hfx.link/2ax1",
     * "now":
     * {
     * "obsTime":"2022-06-22T00:22+08:00",
     * "temp":"27",
     * "feelsLike":"28",
     * "icon":"104",
     * "text":"阴",
     * "wind360":"45",
     * "windDir":"东北风",
     * "windScale":"3",
     * "windSpeed":"14",
     * "humidity":"73",
     * "precip":"0.0",
     * "pressure":"997",
     * "vis":"5",
     * "cloud":"91",
     * "dew":"19"
     * },
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
    private NowDTO now;
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
         * "obsTime":"2022-06-22T00:22+08:00",
         * "temp":"27",
         * "feelsLike":"28",
         * "icon":"104",
         * "text":"阴",
         * "wind360":"45",
         * "windDir":"东北风",
         * "windScale":"3",
         * "windSpeed":"14",
         * "humidity":"73",
         * "precip":"0.0",
         * "pressure":"997"
         * ,"vis":"5",
         * "cloud":"91",
         * "dew":"19"
         * }
         */
        private String obsTime;
        private String temp;
        private String feelsLike;
        private String icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
        private String cloud;
        private String dew;

        public String getObsTime() {
            return obsTime;
        }

        public void setObsTime(String obsTime) {
            this.obsTime = obsTime;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
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

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
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
}



