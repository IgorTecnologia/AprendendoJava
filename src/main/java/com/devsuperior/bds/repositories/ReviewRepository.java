package com.devsuperior.bds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsuperior.bds.entities.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
	
	public List<Review> findAllByTextContainingIgnoreCase(@Param("text") String text); 

}
