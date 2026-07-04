package com.library.management.controller;

import com.library.management.dto.BorrowRecordDTO;
import com.library.management.dto.BorrowRequestDTO;
import com.library.management.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecordDTO> issueBook(@Valid @RequestBody BorrowRequestDTO request) {
        return new ResponseEntity<>(borrowService.issueBook(request), HttpStatus.CREATED);
    }

    @PutMapping("/return/{recordId}")
    public ResponseEntity<BorrowRecordDTO> returnBook(@PathVariable Long recordId) {
        return ResponseEntity.ok(borrowService.returnBook(recordId));
    }

    @GetMapping("/members/{memberId}/borrowed-books")
    public ResponseEntity<List<BorrowRecordDTO>> getCurrentlyBorrowedBooksByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(borrowService.getCurrentlyBorrowedBooksByMember(memberId));
    }

    @GetMapping("/books/{bookId}/borrow-history")
    public ResponseEntity<List<BorrowRecordDTO>> getBorrowHistoryByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(borrowService.getBorrowHistoryByBook(bookId));
    }
}
