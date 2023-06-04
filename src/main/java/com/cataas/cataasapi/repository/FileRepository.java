package com.cataas.cataasapi.repository;


import com.cataas.cataasapi.entity.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileDetails,Integer> {
}
