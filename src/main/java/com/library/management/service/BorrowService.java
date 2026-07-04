package com.library.management.service;

import com.library.management.dto.BorrowRecordDTO;
import com.library.management.dto.BorrowRequestDTO;
import com.library.management.entity.Book;
import com.library.management.entity.BorrowRecord;
import com.library.management.entity.BorrowStatus;
import com.library.management.entity.Member;
import com.library.management.exception.NoCopiesAvailableException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.BorrowRecordRepository;
import com.library.management.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowService(BorrowRecordRepository borrowRecordRepository, 
                         BookRepository bookRepository, 
                         MemberRepository memberRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public BorrowRecordDTO issueBook(BorrowRequestDTO request) {
        logger.info("Issuing book ID {} to member ID {}", request.getBookId(), request.getMemberId());

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + request.getBookId()));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + request.getMemberId()));

        if (book.getAvailableCopies() <= 0) {
            logger.warn("No copies available for book ID {}", request.getBookId());
            throw new NoCopiesAvailableException("No copies available for book: " + book.getTitle());
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setBook(book);
        borrowRecord.setMember(member);
        borrowRecord.setIssueDate(LocalDate.now());
        borrowRecord.setStatus(BorrowStatus.BORROWED);

        BorrowRecord savedRecord = borrowRecordRepository.save(borrowRecord);
        logger.info("Successfully issued book ID {} to member ID {}", request.getBookId(), request.getMemberId());
        
        return mapToDTO(savedRecord);
    }

    @Transactional
    public BorrowRecordDTO returnBook(Long recordId) {
        logger.info("Returning book for record ID {}", recordId);

        BorrowRecord borrowRecord = borrowRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + recordId));

        if (borrowRecord.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException("Book already returned for this record.");
        }

        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecord.setReturnDate(LocalDate.now());

        Book book = borrowRecord.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        
        bookRepository.save(book);
        BorrowRecord updatedRecord = borrowRecordRepository.save(borrowRecord);
        
        logger.info("Successfully returned book for record ID {}", recordId);

        return mapToDTO(updatedRecord);
    }

    public List<BorrowRecordDTO> getCurrentlyBorrowedBooksByMember(Long memberId) {
        // Validate member exists
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        List<BorrowRecord> records = borrowRecordRepository.findByMemberIdAndStatus(memberId, BorrowStatus.BORROWED);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<BorrowRecordDTO> getBorrowHistoryByBook(Long bookId) {
        // Validate book exists
        bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        List<BorrowRecord> records = borrowRecordRepository.findByBookId(bookId);
        return records.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private BorrowRecordDTO mapToDTO(BorrowRecord record) {
        BorrowRecordDTO dto = new BorrowRecordDTO();
        dto.setId(record.getId());
        dto.setBookId(record.getBook().getId());
        dto.setBookTitle(record.getBook().getTitle());
        dto.setMemberId(record.getMember().getId());
        dto.setMemberName(record.getMember().getName());
        dto.setIssueDate(record.getIssueDate());
        dto.setReturnDate(record.getReturnDate());
        dto.setStatus(record.getStatus());
        return dto;
    }
}
