import { IPark } from 'app/entities/park/park.model';
import { IEquipement } from 'app/entities/equipement/equipement.model';
import { IReport } from 'app/entities/report/report.model';
import { AuthType } from 'app/entities/enumerations/auth-type.model';

export interface IMedia {
  id?: number;
  fileName?: string | null;
  filePath?: string | null;
  fileType?: string | null;
  authType?: AuthType | null;
  park?: IPark | null;
  equipement?: IEquipement | null;
  report?: IReport | null;
}

export class Media implements IMedia {
  constructor(
    public id?: number,
    public fileName?: string | null,
    public filePath?: string | null,
    public fileType?: string | null,
    public authType?: AuthType | null,
    public park?: IPark | null,
    public equipement?: IEquipement | null,
    public report?: IReport | null
  ) {}
}

export function getMediaIdentifier(media: IMedia): number | undefined {
  return media.id;
}
