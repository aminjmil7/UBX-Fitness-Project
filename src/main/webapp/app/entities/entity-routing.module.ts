import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'park',
        data: { pageTitle: 'ubxFitnessWebApp.park.home.title' },
        loadChildren: () => import('./park/park.module').then(m => m.ParkModule),
      },
      {
        path: 'equipement',
        data: { pageTitle: 'ubxFitnessWebApp.equipement.home.title' },
        loadChildren: () => import('./equipement/equipement.module').then(m => m.EquipementModule),
      },
      {
        path: 'report',
        data: { pageTitle: 'ubxFitnessWebApp.report.home.title' },
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule),
      },
      {
        path: 'media',
        data: { pageTitle: 'ubxFitnessWebApp.media.home.title' },
        loadChildren: () => import('./media/media.module').then(m => m.MediaModule),
      },
      {
        path: 'events',
        data: { pageTitle: 'ubxFitnessWebApp.events.home.title' },
        loadChildren: () => import('./events/events.module').then(m => m.EventsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
