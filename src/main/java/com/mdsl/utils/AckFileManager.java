package com.mdsl.utils;


import com.mdsl.model.dto.request.SystemCodeUniqueRequestDto;
import com.mdsl.model.dto.response.SystemCodeResponseDto;
import com.mdsl.service.SystemCodeService;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class AckFileManager implements Closeable {

    private final BufferedWriter writer;
    private final String fileName;
    private int totalSuccess = 0;
    private int totalFailed = 0;
    private int fileSuccess = 0;
    private int fileFailed = 0;

    public AckFileManager(File sourceFolder, String programName, SystemCodeService systemCodeService, String institutionId) throws IOException {
        String ackFileName = programName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date()) + ".ack";

        SystemCodeResponseDto systemCodeResponseDto =  systemCodeService.getSystemCodesByUniqueFields(new SystemCodeUniqueRequestDto("FILE_PATH", "ACK_FILE_PATH",institutionId ));

        File directory = new File(systemCodeResponseDto.getCodeValue());

        // Ensure directory exists
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
            }
        }
        File ackFile = new File(directory, ackFileName);

        this.writer = new BufferedWriter(new FileWriter(ackFile, true));
        this.fileName = ackFile.getAbsolutePath();

        writer.write("ACK REPORT - " + programName);
        writer.newLine();
        writer.write("Generated at : " + new Timestamp(System.currentTimeMillis()));
        writer.newLine();
        writer.write("Source folder: " + sourceFolder.getAbsolutePath());
        writer.newLine();
        writer.write("=".repeat(120));
        writer.newLine();
        writer.write(String.format("%-10s | %-35s | %-60s | %s", "LINE_NO", "FILE", "ERROR", "RAW_DATA"));
        writer.newLine();
        writer.write("=".repeat(120));
        writer.newLine();
    }

    public void startFile(String fileName) throws IOException {
        fileSuccess = 0;
        fileFailed = 0;
        writer.write(">> FILE: " + fileName);
        writer.newLine();
    }

    public void writeFailure(int lineNumber, String fileName, String errorMsg, String rawLine) throws IOException {
        writer.write(String.format("%-10s | %-35s | %-60s | %s", lineNumber, fileName, errorMsg, rawLine));
        writer.newLine();
        writer.flush();
        fileFailed++;
        totalFailed++;
    }

    public void recordSuccess() {
        fileSuccess++;
        totalSuccess++;
    }

    public void endFile(String fileName) throws IOException {
        if (fileFailed == 0) {
            writer.write(String.format("   [%s] All records imported successfully. Total imported: %d", fileName, fileSuccess));
        } else {
            writer.write(String.format("   [%s] Success: %d | Failed: %d", fileName, fileSuccess, fileFailed));
        }
        writer.newLine();
        writer.write("-".repeat(120));
        writer.newLine();
        writer.flush();
    }

    public void writeGlobalSummary(int totalFiles) throws IOException {
        writer.write("=".repeat(120));
        writer.newLine();
        if (totalFailed == 0) {
            writer.write(String.format("GLOBAL SUMMARY: All records imported successfully. Files=%d | Total Imported=%d",
                    totalFiles, totalSuccess));
        } else {
            writer.write(String.format("GLOBAL SUMMARY: Files=%d | Total Success=%d | Total Failed=%d",
                    totalFiles, totalSuccess, totalFailed));
        }
        writer.newLine();
        writer.flush();
    }

    public int getTotalFailed()  { return totalFailed; }
    public int getTotalSuccess() { return totalSuccess; }
    public int getFileFailed()   { return fileFailed; }
    public int getFileSuccess()  { return fileSuccess; }

    @Override
    public void close() throws IOException {
        writer.close();
    }


}