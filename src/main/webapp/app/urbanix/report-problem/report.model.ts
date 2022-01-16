import { IMedia } from '../media/media.model';
import { IEquipement } from '../equipement/equipement.model';
import { IPark } from '../park/park.model';
import * as dayjs from 'dayjs';

export interface IReport {
  id?: number;
  mail?: string | null;
  message?: string | null;
  media?: IMedia[] | null;
  equipement?: IEquipement | null;
  park?: IPark | null;
  date?: dayjs.Dayjs | null;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public mail?: string | null,
    public message?: string | null,
    public media?: IMedia[] | null,
    public equipement?: IEquipement | null,
    public date?: dayjs.Dayjs | null,
    public park?: IPark | null
  ) {}
}

export function getReportIdentifier(report: IReport): number | undefined {
  return report.id;
}
