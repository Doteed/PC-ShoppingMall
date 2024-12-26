package com.project.easyBuild.board.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.easyBuild.board.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	List<Announcement> findAll(Sort sort);
}

