import { IReport } from '../report-problem/report.model';
import { IMedia } from '../media/media.model';
import { IPark } from '../park/park.model';

export interface IEquipement {
  id?: number;
  modelName?: string | null;
  modelNumber?: string | null;
  instruction?: string | null;
  verified?: boolean | null;
  reports?: IReport[] | null;
  media?: IMedia[] | null;
  park?: IPark | null;

  modalPreview?: string | null; // To Local Use
}

export class Equipement implements IEquipement {
  constructor(
    public id?: number,
    public modelName?: string | null,
    public modelNumber?: string | null,
    public instruction?: string | null,
    public verified?: boolean | null,
    public reports?: IReport[] | null,
    public media?: IMedia[] | null,
    public park?: IPark | null
  ) {
    this.verified = this.verified ?? false;
  }
}

export function getEquipementIdentifier(equipement: IEquipement): number | undefined {
  return equipement.id;
}
