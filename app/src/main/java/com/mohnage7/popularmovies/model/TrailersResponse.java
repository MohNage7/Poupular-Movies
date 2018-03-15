
package com.mohnage7.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

    @SerializedName("id")
    private Long Id;
    @SerializedName("results")
    private List<Trailers> trailers;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public List<Trailers> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailers> trailers) {
        this.trailers = trailers;
    }

}
