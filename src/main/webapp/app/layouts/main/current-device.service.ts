import { Injectable } from '@angular/core';
import { DeviceDetectorService } from 'ngx-device-detector';

@Injectable({
  providedIn: 'root',
})
export class CurrentDeviceService {
  isMobile!: boolean;
  constructor(private deviceService: DeviceDetectorService) {}

  CheckDevice(): void {
    this.isMobile = this.deviceService.isMobile() || this.deviceService.isTablet();
  }
}
