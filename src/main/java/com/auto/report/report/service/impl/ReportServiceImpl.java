package com.auto.report.report.service.impl;


import com.auto.report.common.Status;
import com.auto.report.data.entity.DataEntity;
import com.auto.report.data.repository.DataRepository;
import com.auto.report.report.entity.ReportEntity;
import com.auto.report.report.repository.ReportRepository;
import com.auto.report.report.service.ReportService;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final DataRepository dataRepository;

    @Value("${report.pdf.path}")
    private String pdfPath; // PDF 저장 경로

    @Value("${report.pdf.font-path}")
    private String fontPath; // PDF 폰트 경로

    @Override
    public int runReport() {

        List<DataEntity> byPending = dataRepository.findByStatus(Status.PENDING);
        if (byPending.size() == 0) {
            log.info("PDF 생성할 데이터가 없습니다.");
            return 9999;
        }

        int successCount = 0;
        for (DataEntity entity : byPending) {
            LocalDateTime startedAt = LocalDateTime.now();
            try {
                String path = createPdf(entity);

                // 성공 status update
                entity.setStatus(Status.COMPLETED);
                dataRepository.save(entity);
                log.info("PDF 생성 성공: {}", path);

                // 결과 저장
                reportRepository.save(ReportEntity.toEntity(entity.getId(), Status.COMPLETED, startedAt, LocalDateTime.now()));
                successCount++;

            } catch (Exception e) {
                log.error("PDF 생성 실패: {}", e.getMessage());

                // 실패 status update
                entity.setStatus(Status.FAILED);
                dataRepository.save(entity);

                // 결과 저장
                reportRepository.save(ReportEntity.toEntity(entity.getId(), Status.FAILED, startedAt, LocalDateTime.now()));
            }
        }

        return successCount;
    }


    /**
     * DB 데이터 pdf 생성 및 export
    */
    private String createPdf(DataEntity data) throws IOException {
        // PDF 생성 로직
        String fileName = data.getBirth().substring(0, 8) + "_" + data.getName() + "_" + data.getId() + ".pdf";

        // dir 생성
        File dir = new File(pdfPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fullPath = pdfPath + File.separator + fileName;

        Document document = new Document();
        FileOutputStream fos = new FileOutputStream(fullPath);
        try {

            PdfWriter.getInstance(document, fos);
            document.open();

            // 폰트 설정
            BaseFont baseFont = BaseFont.createFont(
                    fontPath,
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            Font font = new Font(baseFont, 12);

            document.add(new Paragraph(fileName));

            document.add(new Paragraph(createElement("이름", data.getName(), font)));
            document.add(new Paragraph(createElement("생년월일", data.getBirth(), font)));
            document.add(new Paragraph(createElement("나이", String.valueOf(data.getAge()), font)));
            document.add(new Paragraph(createElement("성별", "woman".equals(data.getGender()) ? "여성" : "남성", font)));

            String formattedDate = data.getCreateAt()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            document.add(new Paragraph(createElement("등록일", formattedDate, font)));

        } catch (Exception e) {
            log.error("PDF 생성 중 오류 발생: {}", e.getMessage());
            throw e;
        } finally {
            document.close();
        }

        return fullPath;

    }

    private Paragraph createElement(String key, String value, Font font) {
        Paragraph paragraph = new Paragraph(key + ": " + value, font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        return paragraph;
    }

}
