import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { BarcodeFormat } from '@zxing/library';
import { EquipementService } from '../equipement/equipement.service';

@Component({
  selector: 'jhi-scan-code',
  templateUrl: './scan-code.component.html',
  styleUrls: ['./scan-code.component.scss'],
})
export class ScanCodeComponent {
  hasDevices = false;
  availableDevices: MediaDeviceInfo[] = [];
  formatsEnabled: BarcodeFormat[] = [BarcodeFormat.CODE_128, BarcodeFormat.DATA_MATRIX, BarcodeFormat.EAN_13, BarcodeFormat.QR_CODE];
  hasPermission = false;

  constructor(protected equipementService: EquipementService, protected router: Router) {}

  onCamerasFound(devices: MediaDeviceInfo[]): void {
    if (devices.length) {
      this.availableDevices = devices;
      this.hasDevices = true;
    } else {
      navigator.mediaDevices.enumerateDevices().then(mediaD => {
        this.availableDevices = mediaD.filter(md => md.kind === 'videoinput')!;
        this.hasDevices = this.availableDevices.length ? true : false;
      });
      const constraints = { audio: true, video: { width: 1280, height: 720 } };
      navigator.mediaDevices.getUserMedia(constraints).then(function (mediaStream) {
        const video = document.querySelector('video');
        video!.srcObject = mediaStream;
      });
    }
  }

  onHasPermission(has: boolean): void {
    this.hasPermission = has;
  }

  onCodeResult(resultString: string): void {
    document.location.href = resultString;
  }
}
