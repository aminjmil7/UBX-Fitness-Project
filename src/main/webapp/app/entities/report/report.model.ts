import * as dayjs from 'dayjs';
import { IMedia } from 'app/entities/media/media.model';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { IPark } from 'app/entities/park/park.model';

export interface IReport {
  id?: number;
  mail?: string | null;
  message?: string | null;
  date?: dayjs.Dayjs | null;
  media?: IMedia[] | null;
  equipement?: IEquipement | null;
  park?: IPark | null;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public mail?: string | null,
    public message?: string | null,
    public date?: dayjs.Dayjs | null,
    public media?: IMedia[] | null,
    public equipement?: IEquipement | null,
    public park?: IPark | null
  ) {}
}

export function getReportIdentifier(report: IReport): number | undefined {
  return report.id;
}
