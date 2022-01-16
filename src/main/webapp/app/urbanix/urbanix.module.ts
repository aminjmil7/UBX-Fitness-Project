import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { UrbanixRoutingModule } from './urbanix-routing.module';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import {
  faCheckSquare,
  faChevronCircleLeft,
  faExclamationCircle,
  faMapMarkerAlt,
  faShareAlt,
  faSquare,
  faStream,
  faArrowDown,
  faChevronDown,
  faPlayCircle,
  faChevronCircleRight,
  faSearch,
} from '@fortawesome/free-solid-svg-icons';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  imports: [
    AgmCoreModule.forRoot({
      apiKey: '',
    }),
    UrbanixRoutingModule,
    SharedModule,
  ],
  declarations: [],
  entryComponents: [],
  exports: [RouterModule],
})
export class UrbanixModule {
  constructor(private library: FaIconLibrary) {
    library.addIcons(
      faSquare,
      faCheckSquare,
      faStream,
      faChevronCircleLeft,
      faChevronCircleRight,
      faSearch,
      faShareAlt,
      faExclamationCircle,
      faMapMarkerAlt,
      faChevronDown,
      faPlayCircle,
      faArrowDown
    );
  }
}
