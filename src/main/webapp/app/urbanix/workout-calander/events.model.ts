import * as dayjs from 'dayjs';

export interface IEvents {
  id?: number;
  eventName?: string | null;
  eventDate?: dayjs.Dayjs | null;
  user_id?: number | null;
}

export class Events implements IEvents {
  constructor(
    public id?: number,
    public eventName?: string | null,
    public eventDate?: dayjs.Dayjs | null,
    public user_id?: number | null
  ) {}
}

export function getEventsIdentifier(events: IEvents): number | undefined {
  return events.id;
}
