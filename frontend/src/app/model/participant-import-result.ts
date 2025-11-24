import {Participant} from './participant';
import {ParticipantImportError} from './participant-import-error';


export interface ParticipantImportResult {
  surveyId: number;
  totalRows: number;
  successCount: number;
  errorCount: number;
  importedParticipants: Participant[];
  errors: ParticipantImportError[];
}
