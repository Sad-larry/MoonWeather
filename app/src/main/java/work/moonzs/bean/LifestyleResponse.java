package work.moonzs.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class LifestyleResponse {
    /**
     * {
     * "code":"200",
     * "updateTime":"2022-06-23T07:36+08:00",
     * "fxLink":"http://hfx.link/2ax2",
     * "daily":
     * [
     * {"date":"2022-06-23",
     * "type":"1",
     * "name":"运动指数",
     * "level":"2",
     * "category":"较适宜",
     * "text":"天气较好，户外运动请注意防晒，推荐您在室内进行低强度运动。"
     * },
     * {...}
     * ],
     * "refer":
     * {
     * "sources":["QWeather"],
     * "license":["no commercial use"]
     * }
     * }
     */
    private String code;
    private String updateTime;
    private String fxLink;
    private List<DailyDTO> daily;
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

    public List<DailyDTO> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyDTO> daily) {
        this.daily = daily;
    }

    public ReferDTO getRefer() {
        return refer;
    }

    public void setRefer(ReferDTO refer) {
        this.refer = refer;
    }

    public static class DailyDTO {
        /**
         * {"date":"2022-06-23",
         * "type":"1",
         * "name":"运动指数",
         * "level":"2",
         * "category":"较适宜",
         * "text":"天气较好，户外运动请注意防晒，推荐您在室内进行低强度运动。"
         * }
         */
        private String date;
        private String type;
        private String name;
        private String level;
        private String category;
        private String text;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class ReferDTO {
        /**
         * "refer":
         * {
         * "sources":["QWeather"],
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
