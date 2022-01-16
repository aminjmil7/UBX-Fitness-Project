import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import * as dayjs from 'dayjs';
import { Events, IEvents } from './events.model';
import Swals2 from 'sweetalert2';
import { EventsService } from './events.service';
import { AccountService } from 'app/core/auth/account.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-workout-calander',
  templateUrl: './workout-calander.component.html',
  styleUrls: ['./workout-calander.component.scss'],
})
export class WorkoutCalanderComponent implements AfterViewInit, OnInit {
  events: Events[] = [];
  eventsSelected: Events[] = [];
  currentUser!: number;
  startDate!: dayjs.Dayjs;
  endDate!: dayjs.Dayjs;
  selectedDate!: Date;
  isLoading = true;
  date!: Date | null;
  @ViewChild('calendar') calendar: ElementRef | undefined;

  constructor(protected eventsService: EventsService, protected accountService: AccountService) {}

  ngAfterViewInit(): void {
    const previous = this.calendar!.nativeElement.getElementsByClassName('mat-calendar-previous-button')![0];
    previous.addEventListener('click', () => {
      const monthView = this.calendar!.nativeElement.getElementsByTagName('mat-month-view')![0];
      const activeDate = monthView.getAttribute('ng-reflect-active-date');
      this.date = new Date(activeDate);
      this.selectedDate = this.date;
      this.loadEvents();
    });

    const next = this.calendar!.nativeElement.getElementsByClassName('mat-calendar-next-button')![0];
    next.addEventListener('click', () => {
      const monthView = this.calendar!.nativeElement.getElementsByTagName('mat-month-view')![0];
      const activeDate = monthView.getAttribute('ng-reflect-active-date');
      this.date = new Date(activeDate);
      this.selectedDate = this.date;
      this.loadEvents();
    });
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.currentUser = account!.id;
      this.date = new Date();
      this.selectedDate = this.date;
      this.loadEvents();
    });
  }

  loadEvents(): void {
    this.isLoading = true;
    this.startDate = dayjs(new Date(this.date!.getFullYear(), this.date!.getMonth(), 1, 1));
    this.endDate = dayjs(new Date(this.date!.getFullYear(), this.date!.getMonth() + 1, 1, 1));

    if (this.currentUser) {
      this.eventsService
        .query({
          'user_id.equals': this.currentUser,
          'eventDate.greaterThanOrEqual': this.startDate.toISOString(),
          'eventDate.lessThanOrEqual': this.endDate.toISOString(),
          size: 1000,
        })
        .subscribe((resEvents: HttpResponse<IEvents[]>) => {
          this.isLoading = false;
          this.events = resEvents.body!;
          this.getSpecialDate();
          this.eventsSelected = this.events.filter(
            event => dayjs(event.eventDate!).format('YYYY-MM-DD') === dayjs(this.date!).format('YYYY-MM-DD')
          );
        });
    }
  }

  getSpecialDate(): void {
    const listView = this.calendar!.nativeElement.getElementsByClassName('mat-calendar-body-cell');
    // dateView.classList.add('special-date');
    for (let i = 0; i < listView.length; i++) {
      const dateView = listView[i].getElementsByClassName('mat-calendar-body-cell-content')[0];
      const day = +dateView.innerHTML;
      if (this.events.filter(event => +dayjs(event.eventDate!).format('DD') === day).length > 0) {
        listView[i].classList.add('special-date');
      }
    }

    // const start = Number(this.startDate.format('DD'));
    // const end = Number(dayjs(new Date(this.date!.getFullYear(), this.date!.getMonth(), 0)).format('DD'));
    // for (let date = start; date <= end; date++) {
    //   if (this.events.filter(event => +dayjs(event.eventDate!).format('DD') === date).length > 0) {
    //     console.log(date);
    //     'special-date';
    //   } else {
    //     '';
    //   }
    // }
  }

  onSelectDate(date: Date | null): void {
    if (date) {
      this.selectedDate = date;
      this.eventsSelected = this.events.filter(event => dayjs(event.eventDate!).format('YYYY-MM-DD') === dayjs(date).format('YYYY-MM-DD'));
    }
  }

  // dateClass() {
  //   return (date: Date): MatCalendarCellCssClasses => {
  //     console.log(date)
  //     if (this.events.filter(event => dayjs(event.eventDate!).format('YYYY-MM-DD') === dayjs(date).format('YYYY-MM-DD')).length > 0) {
  //       return 'special-date';
  //     } else {
  //       return '';
  //     }
  //   };
  // }

  addEvents(): void {
    if (this.currentUser) {
      Swals2.fire({
        title: 'Add Event',
        input: 'text',
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        showCancelButton: true,
      }).then(res => {
        if (res.value) {
          const event: Events = {};
          event.eventName = res.value;
          event.user_id = this.currentUser;
          event.eventDate = dayjs(this.selectedDate);
          this.eventsService.create(event).subscribe(() => this.loadEvents());
        }
      });
    }
  }

  onSelectedMonth(event: Date): void {
    this.isLoading = true;
    this.date = new Date(event);
    this.selectedDate = this.date;
    this.loadEvents();
  }

  onViewChanged(event: string): void {
    this.date = event === 'month' ? new Date(event) : null;
    this.selectedDate = this.date!;
  }

  log(event: any): void {
    this.date = new Date(event);
  }
}
