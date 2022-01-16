import * as dayjs from 'dayjs';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { IMedia } from 'app/entities/media/media.model';
import { IReport } from 'app/entities/report/report.model';

export interface IPark {
  id?: number;
  parkName?: string | null;
  parkAddress?: string | null;
  longtitude?: number | null;
  latitude?: number | null;
  verified?: boolean | null;
  dateInstall?: dayjs.Dayjs | null;
  dateOpen?: dayjs.Dayjs | null;
  dateClose?: dayjs.Dayjs | null;
  note?: string | null;
  reseller?: string | null;
  equipements?: IEquipement[] | null;
  media?: IMedia[] | null;
  reports?: IReport[] | null;
}

export class Park implements IPark {
  constructor(
    public id?: number,
    public parkName?: string | null,
    public parkAddress?: string | null,
    public longtitude?: number | null,
    public latitude?: number | null,
    public verified?: boolean | null,
    public dateInstall?: dayjs.Dayjs | null,
    public dateOpen?: dayjs.Dayjs | null,
    public dateClose?: dayjs.Dayjs | null,
    public note?: string | null,
    public reseller?: string | null,
    public equipements?: IEquipement[] | null,
    public media?: IMedia[] | null,
    public reports?: IReport[] | null
  ) {
    this.verified = this.verified ?? false;
  }
}

export function getParkIdentifier(park: IPark): number | undefined {
  return park.id;
}
