package com.example.myapplication.Domains;

public class SliderItems {
    private String image;
    private String name;
    private String genre;
    private String age;
    private String year;
    private String time;

    public String toString() {
        return image + name + genre + age + year + time;
    }

    public SliderItems() {
    }

    public SliderItems(String image, String name, String genre, String age, String year, String time) {
        this.image = image;
        this.name = name;
        this.genre = genre;
        this.age = age;
        this.year = year;
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImages(String images) {
        this.image = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
