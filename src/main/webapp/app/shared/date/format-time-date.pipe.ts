import { Pipe, PipeTransform } from '@angular/core';

import * as dayjs from 'dayjs';

@Pipe({
  name: 'formatTimeDate',
})
export class FormatTimeDatePipe implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? dayjs(day).format('HH:mm DD-MM-YYYY') : '';
  }
}
