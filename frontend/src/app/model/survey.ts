import {Group} from './group';

export interface Survey {
  id?: number,
  name: string,
  description?: string,
  startTime: Date | null,
  endTime: Date | null
  groups: Group[]
}
