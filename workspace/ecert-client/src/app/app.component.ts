import { Component } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  title = 'ecert-client';

  popupAddData() {
    $('#modal').modal('show');
  }

  closePopupAdd() {
    $('#modal').modal('hide');
  }
}
