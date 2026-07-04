package com.library.management.repository;

import com.library.management.entity.BorrowRecord;
import com.library.management.entity.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    List<BorrowRecord> findByMemberIdAndStatus(Long memberId, BorrowStatus status);
    List<BorrowRecord> findByBookId(Long bookId);
}
