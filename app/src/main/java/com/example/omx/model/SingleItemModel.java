package com.example.omx.model;



public class SingleItemModel {

    private String title;
    private String imageId;
    private String videoTypeId;
    private String movieGenre;
    private String videoUrl;
    private String banner;
    private String details;

    public String getTotal_time() {
        return total_time;
    }

    public void setTotal_time(String total_time) {
        this.total_time = total_time;
    }

    private String total_time;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getVideoTypeId() {
        return videoTypeId;
    }

    public void setVideoTypeId(String videoTypeId) {
        this.videoTypeId = videoTypeId;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }



    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }



    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }



    public SingleItemModel(String imageId, String title,String movieGenre, String videoUrl,String details,String banner,String total_time) {
        super();
        this.imageId = imageId;
        this.title = title;
        this.videoTypeId = videoTypeId;
        this.movieGenre = movieGenre;
        this.videoUrl = videoUrl;
        this.banner = banner;
        this.details = details;
        this.total_time = total_time;


    }
    public String getImage() {
        return imageId;
    }

    public void setImage(String imageId) {

        this.imageId = imageId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
