package zerobaseproject.community.posting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobaseproject.community.posting.dto.PostingRequestDTO;
import zerobaseproject.community.posting.dto.PostingResponseDTO;
import zerobaseproject.community.posting.service.PostingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posting")
public class PostingController {

    private final PostingService postingService;

    @PostMapping("/createpost")
    public ResponseEntity<?> createPosting(@RequestBody PostingRequestDTO requestDTO) {

        PostingResponseDTO responseDTO = postingService.createPosting(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
