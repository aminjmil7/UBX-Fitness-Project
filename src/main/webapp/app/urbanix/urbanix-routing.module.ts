import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'urbanixHome',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./urbanix-home/urbanix-home.module').then(m => m.UrbanixHomeModule),
      },
      {
        path: 'Admin',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
      },
      {
        path: 'reportProblem',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./report-problem/report-problem.module').then(m => m.ReportProblemModule),
      },
      {
        path: 'scan-code',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./scan-code/scan-code.module').then(m => m.ScanCodeModule),
      },
      {
        path: 'park',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./park/park.module').then(m => m.ParkModule),
      },
      {
        path: 'equipement',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./equipement/equipement.module').then(m => m.EquipementModule),
      },
      {
        path: 'Workout',
        data: { pageTitle: 'global.menu.urbanix.main' },
        loadChildren: () => import('./workout-calander/workout-calander.module').then(m => m.WorkoutCalanderModule),
      },
    ]),
  ],
  exports: [RouterModule],
})
export class UrbanixRoutingModule {}
