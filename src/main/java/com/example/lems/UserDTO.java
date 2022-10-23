package com.example.lems;

public class UserDTO {

    public String name;
    public String email;
    public String phone;
    public String birth;
    public String gender;
    public String department;
    public String major;
    public String sign;

    UserDTO() {

    }
    UserDTO(String name, String email, String phone, String birth, String gender) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.gender = gender;
    }
    UserDTO(String name, String email, String phone, String birth, String gender, String department, String major) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birth = birth;
        this.gender = gender;
        this.department = department;
        this.major = major;
        this.sign = "인증필요";
    }
}
