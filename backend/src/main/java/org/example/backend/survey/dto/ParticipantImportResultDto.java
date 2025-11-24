package org.example.backend.survey.dto;

import java.util.List;

public record ParticipantImportResultDto(
        Long surveyId,
        int totalRows,
        int successCount,
        int errorCount,
        List<ParticipantDto> importedParticipants,
        List<ParticipantImportErrorDto> errors
) {
}
