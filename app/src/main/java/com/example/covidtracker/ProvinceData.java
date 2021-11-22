package com.example.covidtracker;

public class ProvinceData {

    private String province;
    private String cases;

    public ProvinceData(String province, String cases) {
        this.province = province;
        this.cases = cases;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

}
