package com.daily.log.dailyLog;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.daily.log.dailyLogComment.DailyLogCommentRequestDto;
import com.daily.log.dailyLogComment.DailyLogCommentResponseDto;
import com.daily.log.dailyLogComment.DailyLogCommentService;
import com.daily.log.team.TeamResponseDto;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DailyLogController {

    final DailyLogService dailyLogService;
    final DailyLogCommentService dailyLogCommentService;

    @PostMapping("/user/dailyLog/{date}/list")
    public ResponseEntity<List<DailyLogResponseDto>> myLogList(HttpServletRequest request, @PathVariable String date){
        List<DailyLogResponseDto> dailyLogList = dailyLogService.getDailyLogsByUserIdAndLogDateAndDelYnOrderBySetStartTime((String) request.getAttribute("loginUser"),date,"N");
        return ResponseEntity.ok(dailyLogList);
    }

    @PutMapping("/user/dailyLog")
    public ResponseEntity<DailyLogResponseDto> registerLog(HttpServletRequest request, @RequestBody DailyLogRequestDto dailyLogRequestDto){
        dailyLogRequestDto.setUserId((String) request.getAttribute("loginUser"));
        DailyLogResponseDto dailyLogList = dailyLogService.saveDailyLog(dailyLogRequestDto);
        return ResponseEntity.ok(dailyLogList);
    }

    @GetMapping("/dailyLog/{logSeq}")
    public ResponseEntity<DailyLogResponseDto> getDailyLogDetail(HttpServletRequest request, @PathVariable Long logSeq){
        DailyLogResponseDto dailyLogResponseDto = dailyLogService.getDailyLogById(logSeq, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(dailyLogResponseDto);
    }

    @GetMapping("/tenant/dept/user/dailyLog/{date}/list")
    public ResponseEntity<List<TeamResponseDto>> getTenantDailyLogList(HttpServletRequest request, @PathVariable String date){
        return ResponseEntity.ok(dailyLogService.getTenantDailyLogsByUserId((String) request.getAttribute("loginUser"),date,"N"));
    }

    @DeleteMapping("/dailyLog/{logSeq}")
    public ResponseEntity<Map<String,Object>> deleteDailyLog(HttpServletRequest request, @PathVariable Long logSeq){
        Map<String,Object> result = new HashMap<>();
        dailyLogService.deleteDailyLogById(logSeq, (String) request.getAttribute("loginUser"),result);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/user/dailyLog/{logSeq}/operationTime/{type}")
    public ResponseEntity<Map<String,Object>> setDailyLogOperationType(HttpServletRequest request, @PathVariable Long logSeq, @PathVariable String type){
        Map<String,Object> result = new HashMap<>();
        dailyLogService.setDailyLogOperationTime(logSeq, (String) request.getAttribute("loginUser"), type, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/dailyLog/{logSeq}/comment")
    public ResponseEntity<List<DailyLogCommentResponseDto>> getDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq){
        List<DailyLogCommentResponseDto> commentList = dailyLogCommentService.getDailyLogCommentsByLogSeqAndDelYnOrderByRegDate(logSeq,"N",(String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(commentList);
    }

    @PutMapping("/dailyLog/{logSeq}/comment")
    public ResponseEntity<List<DailyLogCommentResponseDto>> registerDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq, @RequestBody String commentContent){
        DailyLogCommentRequestDto dailyLogCommentRequestDto = new DailyLogCommentRequestDto(logSeq,commentContent,(String) request.getAttribute("loginUser"));
        List<DailyLogCommentResponseDto> commentList = dailyLogCommentService.saveDailyLogComment(dailyLogCommentRequestDto, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(commentList);
    }

    @DeleteMapping("/dailyLog/{logSeq}/comment/{commentSeq}")
    public ResponseEntity<Map<String,Object>> deleteDailyLogComment(HttpServletRequest request, @PathVariable Long logSeq, @PathVariable Long commentSeq){
        Map<String,Object> result = new HashMap<>();
        dailyLogCommentService.deleteDailyLogComment(result, logSeq, commentSeq, (String) request.getAttribute("loginUser"));
        return ResponseEntity.ok(result);
    }

    @Setter
    @Value("${aws.s3.accesskey}")
    private String accessKey;
    @Setter
    @Value("${aws.s3.secretkey}")
    private String secretKey;
    private Regions region = Regions.valueOf("AP_NORTHEAST_2");
    @Value("${aws.s3.bucket}")
    private String bucket;
    private AmazonS3 s3Client;

    @PostConstruct
    public void setS3Client() {
        System.out.println("access : " + accessKey + " / secret : " + secretKey);
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region).build();
    }

    @PostMapping("/dailyLog/image/upload")
    public ResponseEntity<String> imageUpload(@RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        String fileNm = UUID.randomUUID().toString();
        s3Client.putObject(new PutObjectRequest(bucket, fileNm, imageFile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.info(s3Client.getUrl(bucket, fileNm).toString());
        return ResponseEntity.ok(s3Client.getUrl(bucket, fileNm).toString());
    }
}
