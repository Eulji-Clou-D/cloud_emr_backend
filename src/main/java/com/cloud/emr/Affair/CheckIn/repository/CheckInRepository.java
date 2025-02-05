package com.cloud.emr.Affair.CheckIn.repository;

import com.cloud.emr.Affair.CheckIn.entity.CheckInEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//                                                            엔터티명, 기본키 자료형
@Repository
public interface CheckInRepository extends JpaRepository<CheckInEntity, Long> {


}

/*
  저장: save()
  조회: findById(), findAll(), findAllById(), count(), existsById()
  삭제: deleteById(), delete(), deleteAll(), deleteAllById()
*/
