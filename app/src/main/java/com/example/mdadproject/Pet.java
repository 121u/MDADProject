package com.example.mdadproject;

import android.app.Person;

class Pet {

    public Pet() {

    }

    public Pet(String pid, String name, String pet, String sex, String breed, String age, String dateofadoption, String height, String weight, String image, String username) {
        this.pid = pid;
        this.name = name;
        this.pet = pet;
        this.sex = sex;
        this.breed = breed;
        this.age = age;
        this.dateofadoption = dateofadoption;
        this.height = height;
        this.weight = weight;
        this.image = image;
        this.username = username;
    }


    String pid;
    String name;
    String pet;
    String sex;
    String breed;
    String age;
    String dateofadoption;
    String height;
    String weight;
    String image;
    String username;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPet() {
        return pet;
    }

    public void setPet(String pet) {
        this.pet = pet;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateofadoption() {
        return dateofadoption;
    }

    public void setDateofadoption(String dateofadoption) {
        this.dateofadoption = dateofadoption;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
