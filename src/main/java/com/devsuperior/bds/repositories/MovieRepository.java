package com.devsuperior.bds.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsuperior.bds.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{

	public List<Movie> findAllByTitleContainingIgnoreCase(@Param ("title") String title);
	
}
