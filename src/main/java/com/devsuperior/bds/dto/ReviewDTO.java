package com.devsuperior.bds.dto;


import com.devsuperior.bds.entities.Review;

public class ReviewDTO {

	private Long id;
	private String text;
	private Long userId;
	private Long movieId;

	public ReviewDTO(Review entity, Long userId, Long movieId) {
		this.id = entity.getId();
		this.text = entity.getText();
		this.userId = userId;
		this.movieId = movieId;
	}

	public ReviewDTO (Review entity){
		text = entity.getText();
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
