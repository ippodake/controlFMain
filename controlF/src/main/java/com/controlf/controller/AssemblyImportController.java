package com.controlf.controller;

import com.controlf.dto.AssemblyMemberDTO;
import com.controlf.dto.ImportLeyesRequestDTO;
import com.controlf.dto.ImportResultDTO;
import com.controlf.dto.VotingDTO;
import com.controlf.service.AssemblyImportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AssemblyImportController {

    private final AssemblyImportService assemblyImportService;

    @GetMapping("/assembly-members")
    public ResponseEntity<List<AssemblyMemberDTO>> getAssemblyMembers() {
        return ResponseEntity.ok(assemblyImportService.getAssemblyMembers());
    }

    
 @GetMapping("/assembly-members/{id}/votings")
public ResponseEntity<List<VotingDTO>> getVotings(@PathVariable Long id) {
    try {
        return ResponseEntity.ok(assemblyImportService.getVotings(id));
    } catch (Exception e) {
        throw new RuntimeException(e.getMessage(), e);
    }
}




    @PostMapping("/import-votings/{memberId}/selected")
    public ResponseEntity<ImportResultDTO> importSelectedVotings(
            @PathVariable Long memberId,
            @RequestBody List<Long> selectedIds) {
        return ResponseEntity.ok(assemblyImportService.importSelectedVotings(memberId, selectedIds));
    }

    @PostMapping("/import-leyes")
    public ResponseEntity<ImportResultDTO> importLeyesForPoliticos(@Valid @RequestBody ImportLeyesRequestDTO request) {
        return ResponseEntity.ok(assemblyImportService.importLeyesForPoliticos(request.getPoliticoIds()));
    }

    @PostMapping("/import-votings/{id}")
    public ResponseEntity<ImportResultDTO> importVotings(@PathVariable Long id) {
        return ResponseEntity.ok(assemblyImportService.importVotings(id));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
